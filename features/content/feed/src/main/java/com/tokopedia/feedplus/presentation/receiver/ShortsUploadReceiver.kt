package com.tokopedia.feedplus.presentation.receiver

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

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
                    progress < 0 -> {
                        Log.d("Upload Shorts", "Failed")
                        UploadInfo(
                            UploadType.Shorts,
                            UploadStatus.Failed(data.uploadImageUrl) { shortsUploader.upload(data) },
                        )
                    }
                    progress >= FULL_PROGRESS -> {
                        Log.d("Upload Shorts", "Finished")
                        UploadInfo(
                            UploadType.Shorts,
                            UploadStatus.Finished(data.shortsId),
                        )
                    }
                    else -> {
                        Log.d("Upload Shorts", "Progress: $progress")
                        UploadInfo(
                            UploadType.Shorts,
                            UploadStatus.Progress(progress, data.uploadImageUrl),
                        )
                    }
                }
                trySendBlocking(info)
            }

            awaitClose { shortsUploader.cancelObserve(observer) }
        }
    }

    private val PlayShortsUploadModel.uploadImageUrl: String
        get() = coverUri.ifEmpty { mediaUri }

    companion object {
        private const val FULL_PROGRESS = 100
    }
}
