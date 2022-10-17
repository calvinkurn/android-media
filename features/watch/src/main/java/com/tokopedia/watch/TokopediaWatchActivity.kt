package com.tokopedia.watch

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.watch.databinding.ActivityTokopediaWatchBinding
import com.tokopedia.watch.di.component.DaggerTokopediaWatchComponent
import com.tokopedia.watch.listenerservice.DataLayerServiceListener
import com.tokopedia.watch.util.CapabilityConstant.CAPABILITY_WEAR_APP
import kotlinx.android.synthetic.main.activity_tokopedia_watch.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TokopediaWatchActivity : AppCompatActivity(),
    DataClient.OnDataChangedListener{
    companion object {
        const val MESSAGE_CLIENT_START_ACTIVITY_PATH = "/start-activity"
        const val MESSAGE_CLIENT_MESSAGE_PATH = "/data-message"

        const val DATA_CLIENT_PATH = "/data-client-message"
        const val DATA_CLIENT_MESSAGE_KEY = "devara"

        const val DATA_CLIENT_PATH_FROM_WATCH = "/data-client-message-watch"
        const val DATA_CLIENT_MESSAGE_KEY_FROM_WATCH = "devara-watch"

        const val TAG = "TokopediaWatchActivity"

        const val REQ_PATH_GET_ORDER_LIST = "/get-order-list"
        const val REQ_PATH_GET_SUMMARY = "/get-summary"
    }

    private var binding: ActivityTokopediaWatchBinding? by viewBinding()
    private val nodeClient by lazy { Wearable.getNodeClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val dataClient: DataClient by lazy { Wearable.getDataClient(this) }
    private val capabilityClient: CapabilityClient by lazy { Wearable.getCapabilityClient(this) }

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
        binding = ActivityTokopediaWatchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

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

        checkIfPhoneHasApp()

        showWearAppDialogIfMeetCondition()
    }

    private fun showWearAppDialogIfMeetCondition() {
        /**
         * Check first if there is capable device
         */
        lifecycleScope.launch {
            try {
                val capabilityInfo = capabilityClient
                    .getCapability(CAPABILITY_WEAR_APP, CapabilityClient.FILTER_ALL)
                    .await()

                val capableTkpdWearAppDeviceNearby = withContext(Dispatchers.Main) {
                    // There should only ever be one phone in a node set (much less w/ the correct
                    // capability), so I am just grabbing the first one (which should be the only one).
                    val nodes = capabilityInfo.nodes
                    nodes.size
                }
                if (capableTkpdWearAppDeviceNearby.isZero()) {

                    /**
                     * Check connected device if there is no capable device
                     */
                    val nodes = nodeClient.connectedNodes.await()
                    val connectedNearbyDevice = nodes.firstOrNull { it.isNearby }
                    if (connectedNearbyDevice != null) {
                        /**
                         * remoteActivityHelper only available for device sdk >= 23
                         */
                        val remoteActivityHelper = RemoteActivityHelper(this@TokopediaWatchActivity.applicationContext, Dispatchers.IO.asExecutor())

                        if (Build.VERSION.SDK_INT >= 23) {
                            this@TokopediaWatchActivity.run {
                                val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                                dialog.setTitle("Sekarang ada app tokopedia untuk Wear OS lho!")
                                dialog.setDescription("Mau coba sekarang? Click install untuk menginstall app tokopedia di ${connectedNearbyDevice.displayName}")
                                dialog.setSecondaryCTAText("Nanti")
                                dialog.setPrimaryCTAText("Install")
                                dialog.setPrimaryCTAClickListener {
                                    lifecycleScope.launch {
                                        remoteActivityHelper.startRemoteActivity(
                                            Intent(Intent.ACTION_VIEW)
                                                .addCategory(Intent.CATEGORY_BROWSABLE)
                                                .setData(
                                                    Uri.parse("https://play.google.com/store/apps/details?id=com.tompod.wearnotes&hl=en_US&gl=US")
                                                ),
                                            connectedNearbyDevice.id
                                        ).await()
                                        dialog.dismiss()
                                        binding?.let {
                                            Toaster.build(
                                                it.root,
                                                "Please check your wearable device",
                                                Toaster.LENGTH_SHORT,
                                                Toaster.TYPE_NORMAL
                                            ).show()
                                        }
                                    }

                                }
                                dialog.setSecondaryCTAClickListener {
                                    dialog.dismiss()
                                }
                                dialog.show()
                            }
                        } else {
                            this@TokopediaWatchActivity.run {
                                val dialog = DialogUnify(this, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
                                dialog.setTitle("Sekarang ada app tokopedia untuk Wear OS lho!")
                                dialog.setDescription("Coba deh buka playstore dan install di device kamu ini: ${connectedNearbyDevice.displayName}")
                                dialog.setPrimaryCTAText("Mengerti")
                                dialog.setPrimaryCTAClickListener {
                                    dialog.dismiss()
                                }
                                dialog.show()
                            }
                        }
                    }
                }
            } catch (cancellationException: CancellationException) {
                // Request was cancelled normally
                Log.d("DevaraTest", "Cancelled")

            } catch (throwable: Throwable) {
                Log.d("DevaraTest", throwable.message?:"")

            }
        }
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

    private fun checkIfPhoneHasApp() {
        lifecycleScope.launch {
            try {
                val capabilityInfo = capabilityClient
                    .getCapability(CAPABILITY_WEAR_APP, CapabilityClient.FILTER_ALL)
                    .await()

                withContext(Dispatchers.Main) {
                    // There should only ever be one phone in a node set (much less w/ the correct
                    // capability), so I am just grabbing the first one (which should be the only one).
                    val nodes = capabilityInfo.nodes
                    val androidPhoneNodeWithApp =
                        nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
                    tv_companion_app.setText((androidPhoneNodeWithApp != null).toString())
                }
            } catch (cancellationException: CancellationException) {
                // Request was cancelled normally
            } catch (throwable: Throwable) {

            }
        }
    }
}