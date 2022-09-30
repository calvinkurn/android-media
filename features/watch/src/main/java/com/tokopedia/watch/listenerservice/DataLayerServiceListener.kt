package com.tokopedia.watch.listenerservice

import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.watch.TokopediaWatchActivity
import com.tokopedia.watch.di.DaggerTkpdWatchComponent
import com.tokopedia.watch.orderlist.model.OrderListModel
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase
import com.tokopedia.watch.ordersummary.model.SummaryDataModel
import com.tokopedia.watch.ordersummary.usecase.GetSummaryUseCase
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import rx.Subscriber
import javax.inject.Inject

class DataLayerServiceListener: WearableListenerService() {

    private val job = SupervisorJob()

    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val nodeClient by lazy { Wearable.getNodeClient(this) }

    @Inject
    lateinit var getOrderListUseCase: Lazy<GetOrderListUseCase>

    @Inject
    lateinit var getSummaryUseCase: Lazy<GetSummaryUseCase>

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
            MESSAGE_CLIENT_START_ORDER_ACTIVITY -> {
                startActivity(
                    Intent(this, TokopediaWatchActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            MESSAGE_CLIENT_APP_DETECTION -> {
                messageClient.sendMessage(messageEvent.sourceNodeId, MESSAGE_CLIENT_APP_DETECTION, byteArrayOf())
            }
            GET_ORDER_LIST_PATH -> {
                runBlocking {
                    getOrderList()
                }
            }
            GET_SUMMARY_PATH -> {
                runBlocking {
                    getSummaryData()
                }            }
            GET_ALL_DATA_PATH -> {
                runBlocking {
                    getOrderList()
                    getSummaryData()
                }
            }
        }
    }

    private fun getOrderList() {
        if (!userSession.get().isLoggedIn) {
            startActivity(RouteManager.getIntent(this, ApplinkConst.LOGIN))
        }

        getOrderListUseCase.get().executeSync(RequestParams(), getLoadOrderListDataSubscriber())
    }

    private fun getSummaryData() {
        if (!userSession.get().isLoggedIn) {
            startActivity(RouteManager.getIntent(this, ApplinkConst.LOGIN))
        }
        getSummaryUseCase.get().executeSync(RequestParams(), getLoadSummaryDataSubscriber())
    }



    private fun getLoadOrderListDataSubscriber(): Subscriber<OrderListModel> {
        return object: Subscriber<OrderListModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, e?.message?:"")
            }

            override fun onNext(orderListModel: OrderListModel) {
                sendMessageToWatch(
                    DataLayerServiceListener.GET_ORDER_LIST_PATH,
                    Gson().toJson(orderListModel)
                )
            }
        }
    }

    private fun getLoadSummaryDataSubscriber(): Subscriber<SummaryDataModel> {
        return object: Subscriber<SummaryDataModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(summaryDataModel: SummaryDataModel) {
                sendMessageToWatch(
                    DataLayerServiceListener.GET_SUMMARY_PATH,
                    Gson().toJson(summaryDataModel)
                )
            }
        }
    }

    private fun sendMessageToWatch(key: String, message: String) {
        scope.launch {
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
                Log.d(TAG, "Send data to watch success: $message")
            } catch (cancellationException: CancellationException) {
                Log.d(TAG, "Send data to watch failed (cancelled): $message")
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Send data to watch failed: $exception")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        const val MESSAGE_CLIENT_START_ORDER_ACTIVITY = "/start-order-activity"
        const val MESSAGE_CLIENT_APP_DETECTION = "/app-detection"
        const val GET_ORDER_LIST_PATH = "/get-order-list"
        const val GET_SUMMARY_PATH = "/get-summary"

        const val GET_ALL_DATA_PATH = "/get-all-data"
        const val ACCEPT_BULK_ORDER_PATH = "/accept_bulk-order"
        const val TAG = "DataLayerServiceListener"
    }
}