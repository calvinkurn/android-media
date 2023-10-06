package com.tokopedia.feedplus.presentation.receiver

import androidx.lifecycle.Observer
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
class ShortsUploadReceiver @Inject constructor(
    private val shortsUploader: PlayShortsUploader,
) : UploadReceiver {

    override fun observe(): Flow<UploadInfo> {
        return callbackFlow {
            val uploadLiveData = shortsUploader.getUploadLiveData()

            val observer = Observer<PlayShortsUploadResult> {
                if (it is PlayShortsUploadResult.Success) {

                    val progress = it.progress
                    val data = it.data

                    val info = when {
                        progress < 0 -> {
                            UploadInfo(
                                UploadType.Shorts,
                                UploadStatus.Failed(data.uploadImageUrl) { shortsUploader.upload(data) },
                            )
                        }
                        progress >= FULL_PROGRESS -> {
                            UploadInfo(
                                UploadType.Shorts,
                                UploadStatus.Finished(
                                    data.shortsId,
                                    data.authorId,
                                    data.authorType,
                                ),
                            )
                        }
                        else -> {
                            UploadInfo(
                                UploadType.Shorts,
                                UploadStatus.Progress(progress, data.uploadImageUrl),
                            )
                        }
                    }

                    trySendBlocking(info)
                }
            }

            uploadLiveData.observeForever(observer)

            awaitClose { uploadLiveData.removeObserver(observer) }
        }
    }

    private val PlayShortsUploadModel.uploadImageUrl: String
        get() = coverUri.ifEmpty { mediaUri }

    companion object {
        private const val FULL_PROGRESS = 100
    }
}
