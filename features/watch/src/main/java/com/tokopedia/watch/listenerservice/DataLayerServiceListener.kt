package com.tokopedia.watch.listenerservice

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.watch.di.DaggerTkpdWatchComponent
import com.tokopedia.watch.notification.model.NotificationListModel
import com.tokopedia.watch.notification.usecase.GetNotificationListUseCase
import com.tokopedia.watch.orderlist.model.AcceptBulkOrderModel
import com.tokopedia.watch.orderlist.model.OrderListModel
import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderStatusUiModel
import com.tokopedia.watch.orderlist.param.SomListGetAcceptBulkOrderStatusParam
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase.Companion.ORDER_STATUS_NEW_ORDER
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase.Companion.ORDER_STATUS_READY_TO_SHIP
import com.tokopedia.watch.orderlist.usecase.SomListAcceptBulkOrderUseCase
import com.tokopedia.watch.orderlist.usecase.SomListGetAcceptBulkOrderStatusUseCase
import com.tokopedia.watch.ordersummary.model.SummaryDataModel
import com.tokopedia.watch.ordersummary.usecase.GetSummaryUseCase
import dagger.Lazy
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import rx.Subscriber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DataLayerServiceListener : WearableListenerService(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val nodeClient by lazy { Wearable.getNodeClient(this) }

    @Inject
    lateinit var getOrderListUseCase: Lazy<GetOrderListUseCase>

    @Inject
    lateinit var getNotificationListUseCase: Lazy<GetNotificationListUseCase>

    @Inject
    lateinit var getSummaryUseCase: Lazy<GetSummaryUseCase>

    @Inject
    lateinit var somListAcceptBulkOrderUseCase: Lazy<SomListAcceptBulkOrderUseCase>

    @Inject
    lateinit var somListGetAcceptBulkOrderStatusUseCase: Lazy<SomListGetAcceptBulkOrderStatusUseCase>

    @Inject
    lateinit var userSession: Lazy<UserSession>

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()

        val component = DaggerTkpdWatchComponent.builder().baseAppComponent(
            (this.application as BaseMainApplication).baseAppComponent
        ).build()
        component.inject(this)
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        when (messageEvent.path) {
            ACCEPT_BULK_ORDER_PATH -> {
                runBlocking {
                    try {
                        val jsonListOrderId = messageEvent.data.decodeToString()
                        val listType = object : TypeToken<List<String>>() {}.type
                        val listOrderId = Gson().fromJson<List<String>>(jsonListOrderId, listType)
                        listOrderId?.let {
                            acceptOrder(it)
                        }
                    } catch (_: Exception) {
                    }
                }
            }
            MESSAGE_CLIENT_START_ORDER_ACTIVITY -> {
            }
            MESSAGE_CLIENT_APP_DETECTION -> {
                messageClient.sendMessage(messageEvent.sourceNodeId, MESSAGE_CLIENT_APP_DETECTION, byteArrayOf())
            }
            GET_ORDER_LIST_PATH -> {
                runBlocking {
                    getOrderList(ORDER_STATUS_NEW_ORDER)
                    getOrderList(ORDER_STATUS_READY_TO_SHIP)
                }
            }
            GET_NOTIFICATION_LIST_PATH -> {
                runBlocking {
                    getNotificationList()
                }
            }
            GET_SUMMARY_PATH -> {
                runBlocking {
                    getSummaryData()
                }
            }
            GET_ALL_DATA_PATH -> {
                runBlocking {
                    getOrderList(ORDER_STATUS_NEW_ORDER)
                    getOrderList(ORDER_STATUS_READY_TO_SHIP)
                    getSummaryData()
                }
            }
            GET_PHONE_STATE -> {
                runBlocking {
                    if (!userSession.get().isLoggedIn) {
                        sendMessageToWatch(
                            DataLayerServiceListener.GET_PHONE_STATE,
                            STATE.COMPANION_NOT_LOGIN.getStringState()
                        )
                    } else {
                        sendMessageToWatch(
                            DataLayerServiceListener.GET_PHONE_STATE,
                            STATE.CONNECTED.getStringState()
                        )
                    }
                }
            }
            OPEN_LOGIN_PAGE -> {
                val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            OPEN_READY_TO_SHIP -> {
                val intent = RouteManager.getIntent(this, ApplinkConst.SELLER_SHIPMENT).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            OPEN_NEW_ORDER_LIST -> {
                val intent = RouteManager.getIntent(this, ApplinkConst.SELLER_NEW_ORDER).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun acceptOrder(listOrderId: List<String>) {
        launchCatchError(Dispatchers.IO, block = {
            val doAcceptOrderResponse = doAcceptOrder(listOrderId)
            val acceptOrderStatusResponse = getAcceptOrderStatus(
                doAcceptOrderResponse?.data?.batchId.orEmpty()
            )

            val newOrderListDataAsyncData = asyncCatchError(block = {
                getOrderListUseCase.get().createObservable(
                    RequestParams().apply {
                        putObject(GetOrderListUseCase.PARAM_STATUS_LIST, ORDER_STATUS_NEW_ORDER)
                    }
                ).toBlocking().first()
            }) {
                null
            }.await()

            val readyToShipOrderListDataAsyncData = asyncCatchError(block = {
                getOrderListUseCase.get().createObservable(
                    RequestParams().apply {
                        putObject(GetOrderListUseCase.PARAM_STATUS_LIST, ORDER_STATUS_READY_TO_SHIP)
                    }
                ).toBlocking().first()
            }) {
                null
            }.await()

            val orderSummaryAsyncData = asyncCatchError(block = {
                getSummaryUseCase.get().createObservable(RequestParams()).toBlocking().first()
            }) {
                null
            }.await()

            newOrderListDataAsyncData?.let { orderListData ->
                sendMessageToWatch(
                    GET_ORDER_LIST_PATH,
                    Gson().toJson(orderListData)
                )
            }
            readyToShipOrderListDataAsyncData?.let { orderListData ->
                sendMessageToWatch(
                    GET_ORDER_LIST_PATH,
                    Gson().toJson(orderListData)
                )
            }
            orderSummaryAsyncData?.let { orderSummaryData ->
                sendMessageToWatch(
                    GET_SUMMARY_PATH,
                    Gson().toJson(orderSummaryData)
                )
            }
            sendMessageToWatch(
                ACCEPT_BULK_ORDER_PATH,
                ""
            )
        }) {
        }
    }

    private suspend fun doAcceptOrder(listOrderId: List<String>): AcceptBulkOrderModel? {
        return somListAcceptBulkOrderUseCase.get()?.run {
            setParams(listOrderId, userSession.get().userId)
            executeOnBackground()
        }
    }

    private suspend fun getAcceptOrderStatus(batchId: String): SomListAcceptBulkOrderStatusUiModel? {
        return somListGetAcceptBulkOrderStatusUseCase.get()?.run {
            delay(DELAY_GET_ACCEPT_ORDER_STATUS)
            setParams(
                SomListGetAcceptBulkOrderStatusParam(
                    batchId = batchId,
                    shopId = userSession.get().shopId
                )
            )
            executeOnBackground()
        }
    }

    private fun getOrderList(orderStatus: List<Int>) {
        if (!userSession.get().isLoggedIn) {
            return
        }

        getOrderListUseCase.get().executeSync(
            RequestParams().apply {
                putObject(GetOrderListUseCase.PARAM_STATUS_LIST, orderStatus)
            },
            getLoadOrderListDataSubscriber()
        )
    }

    private fun getNotificationList() {
        if (!userSession.get().isLoggedIn) {
            return
        }
        getNotificationListUseCase.get().executeSync(RequestParams(), getLoadNotificationListDataSubscriber())
    }

    private fun getSummaryData() {
        if (!userSession.get().isLoggedIn) {
            return
        }
        getSummaryUseCase.get().executeSync(RequestParams(), getLoadSummaryDataSubscriber())
    }

    private fun getLoadOrderListDataSubscriber(): Subscriber<OrderListModel> {
        return object : Subscriber<OrderListModel>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(orderListModel: OrderListModel) {
                sendMessageToWatch(
                    GET_ORDER_LIST_PATH,
                    Gson().toJson(orderListModel)
                )
            }
        }
    }

    private fun getLoadNotificationListDataSubscriber(): Subscriber<NotificationListModel> {
        return object : Subscriber<NotificationListModel>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(notificationListModel: NotificationListModel) {
                sendMessageToWatch(
                    GET_NOTIFICATION_LIST_PATH,
                    Gson().toJson(notificationListModel)
                )
            }
        }
    }

    private fun getLoadSummaryDataSubscriber(): Subscriber<SummaryDataModel> {
        return object : Subscriber<SummaryDataModel>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(summaryDataModel: SummaryDataModel) {
                sendMessageToWatch(
                    GET_SUMMARY_PATH,
                    Gson().toJson(summaryDataModel)
                )
            }
        }
    }

    private fun sendMessageToWatch(key: String, message: String) {
        launch {
            try {
                val nodes = nodeClient.connectedNodes.await()

                // Send a message to all nodes in parallel
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(
                            node.id,
                            key,
                            message.toByteArray()
                        )
                            .await()
                    }
                }.awaitAll()
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    enum class STATE {
        CONNECTED,
        SYNC,
        COMPANION_NOT_LOGIN,
        COMPANION_NOT_REACHABLE,
        COMPANION_NOT_INSTALLED;

        fun getStringState() = when (this) {
            SYNC -> "sync"
            CONNECTED -> "connected"
            COMPANION_NOT_LOGIN -> "companion_not_login"
            COMPANION_NOT_REACHABLE -> "companion_not_reachable"
            COMPANION_NOT_INSTALLED -> "companion_not_installed"
        }
    }

    companion object {
        const val MESSAGE_CLIENT_START_ORDER_ACTIVITY = "/start-order-activity"
        const val MESSAGE_CLIENT_APP_DETECTION = "/app-detection"
        const val GET_ORDER_LIST_PATH = "/get-order-list"
        const val GET_NOTIFICATION_LIST_PATH = "/get-notification-list"
        const val GET_SUMMARY_PATH = "/get-summary"

        const val GET_ALL_DATA_PATH = "/get-all-data"
        const val GET_PHONE_STATE = "/get-phone-state"

        const val ACCEPT_BULK_ORDER_PATH = "/accept-bulk-order"
        const val OPEN_LOGIN_PAGE = "/open-login-page"

        const val OPEN_READY_TO_SHIP = "/open-ready-to-ship"
        const val OPEN_NEW_ORDER_LIST = "/open-new-order-list"
        private val DELAY_GET_ACCEPT_ORDER_STATUS = 1000L
    }
}
