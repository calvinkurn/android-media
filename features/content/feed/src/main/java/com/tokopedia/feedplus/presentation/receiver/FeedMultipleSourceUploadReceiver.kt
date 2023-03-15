package com.tokopedia.feedplus.presentation.receiver

import androidx.fragment.app.Fragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private var mCurrentFlow: Flow<UploadInfo>? = null

    private val mutex = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<UploadInfo> = channelFlow {
        listOf(shortsUploadReceiver, postUploadReceiver).forEach { receiver ->
            launch {
                val flow = receiver.observe()
                flow.collectLatest { info ->
                    when (info) {
                        is UploadInfo.Progress, is UploadInfo.Failed -> setCurrentFlow(flow)
                        is UploadInfo.Finished -> clearFlow(flow)
                    }

                    send(info)
                }
            }
        }
    }

    private suspend fun setCurrentFlow(flow: Flow<UploadInfo>) = mutex.withLock {
        if (mCurrentFlow == null) mCurrentFlow = flow
    }

    private suspend fun clearFlow(flow: Flow<UploadInfo>) = mutex.withLock {
        if (mCurrentFlow != null) mCurrentFlow = null
    }
}
