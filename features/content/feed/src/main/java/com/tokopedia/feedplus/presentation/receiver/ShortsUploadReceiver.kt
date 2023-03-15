package com.tokopedia.feedplus.presentation.receiver

import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
class ShortsUploadReceiver @AssistedInject constructor(
    private val shortsUploader: PlayShortsUploader,
    @Assisted private val owner: LifecycleOwner,
) : UploadReceiver {

    @AssistedFactory
    interface Factory {
        fun create(owner: LifecycleOwner): ShortsUploadReceiver
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observe(): Flow<UploadInfo> {
        return callbackFlow {
            val observer = shortsUploader.observe(owner) { progress, data ->
                trySendBlocking(
                    UploadInfo(progress, data.coverUri)
                )
            }

            awaitClose { shortsUploader.cancelObserve(observer) }
        }
    }
}
