package com.tokopedia.watch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.watch.databinding.ActivityTokopediaWatchBinding
import com.tokopedia.watch.listenerservice.DataLayerServiceListener
import com.tokopedia.watch.orderlist.mapper.OrderListMapper
import com.tokopedia.watch.orderlist.model.OrderListModel
import com.tokopedia.watch.orderlist.usecase.GetOrderListUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import rx.Subscriber
import java.text.SimpleDateFormat
import java.util.*

class TokopediaWatchActivity : AppCompatActivity(),
    DataClient.OnDataChangedListener {
    companion object {
        const val MESSAGE_CLIENT_START_ACTIVITY_PATH = "/start-activity"
        const val MESSAGE_CLIENT_MESSAGE_PATH = "/data-message"

        const val DATA_CLIENT_PATH = "/data-client-message"
        const val DATA_CLIENT_MESSAGE_KEY = "devara"

        const val DATA_CLIENT_PATH_FROM_WATCH = "/data-client-message-watch"
        const val DATA_CLIENT_MESSAGE_KEY_FROM_WATCH = "devara-watch"

        const val TAG = "TokopediaWatchActivity"
    }

    private var binding: ActivityTokopediaWatchBinding? by viewBinding()
    private val nodeClient by lazy { Wearable.getNodeClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val dataClient: DataClient by lazy { Wearable.getDataClient(this) }

    private val _activityLog = MutableStateFlow(mutableListOf<String>())
    private val activityLog: StateFlow<List<String>> = _activityLog

    private val logList = mutableListOf<String>()

    lateinit var userSession: UserSessionInterface

    override fun onResume() {
        super.onResume()
        dataClient.addListener(this)
        updateConnectedDevice()
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokopedia_watch)

        userSession = UserSession(this)

        binding?.tvConnectedDevice?.text = ""
        binding?.tvLog?.text = ""

        binding?.btnStartWatch?.setOnClickListener {
            startWearableActivity()
        }

//        binding?.btnSendMessage?.setOnClickListener {
//            sendMessageToWatch()
//        }

        binding?.btnSendData?.setOnClickListener {
            sendDataToWatch()
        }

        binding?.tvLastMessage?.text = ""

        lifecycleScope.launch {
            activityLog.collect {
                binding?.tvLog?.text = it.joinToString("\n\n")
            }
        }

        getOrderList()
    }

    private fun sendDataToWatch() {
        val message = binding?.etData?.editText?.text.toString()
        lifecycleScope.launch {
            try {
                val request = PutDataMapRequest.create(DATA_CLIENT_PATH).apply {
                    dataMap.putString(DATA_CLIENT_MESSAGE_KEY, message)
                }
                    .asPutDataRequest()
                    .setUrgent()

                val result = dataClient.putDataItem(request).await()

                sendLog("Send data to watch success: $message")
                Log.d(TAG, "DataItem saved: $message")
            } catch (cancellationException: CancellationException) {
                sendLog("Failed: Cancellation Exception, while send data to watch: $message")
                throw cancellationException
            } catch (exception: Exception) {
                sendLog("Failed, while send data to watch: $message")
                Log.d(TAG, "Saving DataItem failed: $exception")
            }
        }
    }

    private fun sendMessageToWatch(key: String, message: String) {
        lifecycleScope.launch {
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

                sendLog("Send data to watch success: $message")
                Log.d(TAG, "Send data to watch success: $message")
            } catch (cancellationException: CancellationException) {
                sendLog("Failed: CancellationException, while send data to watch: $message")
                throw cancellationException
            } catch (exception: Exception) {
                sendLog("Failed, while send data to watch: $message")
                Log.d(TAG, "Send data to watch failed: $exception")
            }
        }
    }

    private fun updateConnectedDevice() {
        lifecycleScope.launch {
            try {
                val nodes = nodeClient.connectedNodes.await()
                binding?.tvConnectedDevice?.text = ""
                nodes.forEach {
                    val currentText = binding?.tvConnectedDevice?.text ?:""
                    binding?.tvConnectedDevice?.text = ("$currentText\n${it.displayName}")
                    sendLog("Updated connected device success: ${it.displayName}")
                }
            } catch (cancellationException: CancellationException) {
                sendLog("Failed cancellation exception, updated connected device")
                throw cancellationException
            } catch (exception: Exception) {
                sendLog("Failed, updated connected device")
                Log.d(TAG, "Starting activity failed: $exception")
            }
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path) {
                        DATA_CLIENT_PATH_FROM_WATCH -> {
                            val message = DataMapItem.fromDataItem(dataEvent.dataItem)
                                .dataMap
                                .getString(DATA_CLIENT_MESSAGE_KEY_FROM_WATCH)
                            binding?.tvLastMessage?.text = message
                            sendLog("Got new data from watch app: $message")
                        }
                    }
                }
            }
        }
    }

    private fun startWearableActivity() {
        lifecycleScope.launch {
            try {
                val nodes = nodeClient.connectedNodes.await()

                // Send a message to all nodes in parallel
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(node.id, MESSAGE_CLIENT_START_ACTIVITY_PATH, byteArrayOf())
                            .await()
                    }
                }.awaitAll()

                sendLog("Starting activity requests sent successfully to wear app")
                Log.d(TAG, "Starting activity requests sent successfully")

            } catch (cancellationException: CancellationException) {
                sendLog("Failed: CancellationException, when starting activity")
                throw cancellationException
            } catch (exception: Exception) {
                sendLog("Failed, when starting activity")
                Log.d(TAG, "Starting activity failed: $exception")
            }
        }
    }

    private fun sendLog(logMessage: String) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        logList.add("[$currentDate] $logMessage")
        _activityLog.value = logList.toMutableList()
    }

    private fun getOrderList() {
        if (!userSession.isLoggedIn) {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), 123)
        }

        val useCase = GetOrderListUseCase(
            GraphqlUseCase(),
            OrderListMapper()
        )

        useCase.execute(RequestParams(), getLoadOrderListDataSubscriber())
    }

    private fun getLoadOrderListDataSubscriber(): Subscriber<OrderListModel> {
        return object: Subscriber<OrderListModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(orderListModel: OrderListModel) {
                sendMessageToWatch(
                    DataLayerServiceListener.GET_ORDER_LIST_PATH,
                    Gson().toJson(orderListModel)
                )
            }
        }
    }

}