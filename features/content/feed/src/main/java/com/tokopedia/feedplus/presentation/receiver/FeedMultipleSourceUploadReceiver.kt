package com.tokopedia.feedplus.presentation.receiver

import android.util.Log
import androidx.fragment.app.Fragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by kenny.hadisaputra on 15/03/23
 */
class FeedMultipleSourceUploadReceiver @AssistedInject constructor(
    @Assisted fragment: Fragment,
    shortsUploadReceiverFactory: ShortsUploadReceiver.Factory,
    postUploadReceiverFactory: PostUploadReceiver.Factory,
) : UploadReceiver {

    @AssistedFactory
    interface Factory {
        fun create(fragment: Fragment): FeedMultipleSourceUploadReceiver
    }

    private val shortsUploadReceiver = shortsUploadReceiverFactory.create(fragment.viewLifecycleOwner)
    private val postUploadReceiver = postUploadReceiverFactory.create(fragment.requireContext())

    private val mutex = Mutex()

    private val receiverList = listOf(shortsUploadReceiver, postUploadReceiver)

    private var mReceiver: UploadReceiver? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<UploadInfo> = channelFlow {
        receiverList.forEach { receiver ->
            launch {
                val flow = receiver.observe()
                flow.collectLatest { info ->
                    if (info != UploadInfo.Finished) {
                        setCurrentReceiver(receiver)

                        Log.d("Upload Info", "From Receiver: $receiver, Info: $info")
                        send(receiver, info)
                    } else {
                        send(receiver, info)
                        clearReceiver(receiver)
                    }
                }
            }
        }
    }

    suspend fun releaseCurrent() {
        Log.d("Upload Info", "Release Current, Receiver: $mReceiver")
        mReceiver?.let { clearReceiver(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun ProducerScope<UploadInfo>.send(receiver: UploadReceiver, info: UploadInfo) = mutex.withLock {
        if (mReceiver != receiver) return@withLock
        Log.d("Upload Info", "Sending from Receiver: $receiver, Info: $info")
        send(info)
    }

    private suspend fun setCurrentReceiver(receiver: UploadReceiver) = mutex.withLock {
        if (mReceiver != null) return@withLock
        mReceiver = receiver
    }

    private suspend fun clearReceiver(receiver: UploadReceiver) = mutex.withLock {
        if (mReceiver != receiver) return@withLock
        Log.d("Upload Info", "Clearing Receiver: $receiver")
        mReceiver = null
    }
}
