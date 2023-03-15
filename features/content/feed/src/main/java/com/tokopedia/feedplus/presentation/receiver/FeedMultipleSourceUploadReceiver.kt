package com.tokopedia.feedplus.presentation.receiver

import androidx.fragment.app.Fragment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex

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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<UploadInfo> = channelFlow {
        listOf(shortsUploadReceiver, postUploadReceiver).map { receiver ->
            receiver.observe().stateIn(this)
        }.forEach { infoFlow ->
            infoFlow.collectLatest {
                send(it)
            }
        }
    }
}
