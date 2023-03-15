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
                val info = when {
                    progress < 0 -> UploadInfo.Failed(data.coverUri) {
                        shortsUploader.upload(data)
                    }
                    progress >= FULL_PROGRESS -> UploadInfo.Finished
                    else -> UploadInfo.Progress(progress, data.coverUri)
                }
                trySendBlocking(info)
            }

            awaitClose { shortsUploader.cancelObserve(observer) }
        }
//        return flow {
//            emit(UploadInfo.Progress(10, ""))
//            delay(500)
//            emit(UploadInfo.Progress(30, ""))
//            delay(500)
//            emit(UploadInfo.Progress(40, ""))
//            delay(500)
//            emit(UploadInfo.Progress(80, ""))
//            delay(500)
//            emit(UploadInfo.Progress(100, ""))
//            delay(500)
//            emit(UploadInfo.Failed("") {
//                Log.d("UPLOAD FAILED", "Retry")
//            })
//        }
//        return flow {}
    }

    companion object {
        private const val FULL_PROGRESS = 100
    }
}
