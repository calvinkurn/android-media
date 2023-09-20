package com.tokopedia.feedplus.presentation.receiver

import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
class ShortsUploadReceiver @Inject constructor(
    private val creationUploader: CreationUploader,
) : UploadReceiver {

    override fun observe(): Flow<UploadInfo> {
        return creationUploader
            .observe()
            .map {
                when (it) {
                    is CreationUploadResult.Progress -> {
                        UploadInfo(
                            UploadType.Shorts,
                            UploadStatus.Progress(it.progress, it.uploadImageUrl),
                        )
                    }
                    is CreationUploadResult.Success -> {
                        UploadInfo(
                            UploadType.Shorts,
                            UploadStatus.Finished(
                                it.data.creationId,
                                it.data.authorId,
                                it.data.authorType,
                            ),
                        )
                    }
                    is CreationUploadResult.Failed -> {
                        UploadInfo(
                            UploadType.Shorts,
                            UploadStatus.Failed(it.uploadImageUrl) {
                                creationUploader.retry()
                            },
                        )
                    }
                    else -> {
                        UploadInfo(
                            type = UploadType.Shorts,
                            status = UploadStatus.Unknown,
                        )
                    }
                }
            }
    }

    private val CreationUploadResult.uploadImageUrl: String
        get() {
            return if (this is CreationUploadResult.Success) {
                data.coverUri.ifEmpty { data.mediaUri }
            } else if (this is CreationUploadResult.Failed) {
                data.coverUri.ifEmpty { data.mediaUri }
            } else if (this is CreationUploadResult.Progress) {
                data.coverUri.ifEmpty { data.mediaUri }
            } else {
                ""
            }
        }

    companion object {
        private const val FULL_PROGRESS = 100
    }
}
