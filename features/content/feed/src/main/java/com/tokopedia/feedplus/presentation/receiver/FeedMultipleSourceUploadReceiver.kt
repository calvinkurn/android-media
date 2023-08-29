package com.tokopedia.feedplus.presentation.receiver

import androidx.fragment.app.Fragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.ProducerScope
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
    shortsUploadReceiver: ShortsUploadReceiver,
    postUploadReceiverFactory: PostUploadReceiver.Factory,
) : UploadReceiver {

    @AssistedFactory
    interface Factory {
        fun create(fragment: Fragment): FeedMultipleSourceUploadReceiver
    }

    private val postUploadReceiver = postUploadReceiverFactory.create(fragment.requireContext())

    private val mutex = Mutex()

    private val receiverList = listOf(shortsUploadReceiver, postUploadReceiver)

    private var mReceiver: UploadReceiver? = null

    override fun observe(): Flow<UploadInfo> = channelFlow {
        receiverList.forEach { receiver ->
            launch {
                val flow = receiver.observe()
                flow.collectLatest { info ->
                    if (info.status !is UploadStatus.Finished) {
                        setCurrentReceiver(receiver)

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
        mReceiver?.let { clearReceiver(it) }
    }

    private suspend fun ProducerScope<UploadInfo>.send(receiver: UploadReceiver, info: UploadInfo) = mutex.withLock {
        if (mReceiver != receiver) return@withLock
        send(info)
    }

    private suspend fun setCurrentReceiver(receiver: UploadReceiver) = mutex.withLock {
        if (mReceiver != null) return@withLock
        mReceiver = receiver
    }

    private suspend fun clearReceiver(receiver: UploadReceiver) = mutex.withLock {
        if (mReceiver != receiver) return@withLock
        mReceiver = null
    }
}
