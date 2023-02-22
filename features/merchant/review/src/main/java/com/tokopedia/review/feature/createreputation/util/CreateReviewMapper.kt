package com.tokopedia.review.feature.createreputation.util

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.picker.common.utils.isVideoFormat
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewMediaUploadResult
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.MediaUploadJobMap
import com.tokopedia.review.feature.createreputation.presentation.viewmodel.MediaUploadResultMap
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.uimodel.StringRes
import java.util.concurrent.TimeUnit

object CreateReviewMapper {

    private const val UPLOADED_ALL_MEDIA_POEM_EXPIRY_DURATION = 3
    private const val UPLOADED_MEDIA_POEM_EXPIRY_DURATION = 5
    private const val MAX_MEDIA_COUNT = 5

    fun mapPoem(
        currentValue: Pair<String, StringRes>,
        mediaItems: List<CreateReviewMediaUiModel>,
        uploadBatchNumber: Int
    ): Pair<String, StringRes> {
        val filteredMediaItems = mediaItems.filter {
            (it.isImage() || it.isVideo()) && it.uploadBatchNumber == uploadBatchNumber
        }
        val uploadingMedia = filteredMediaItems.firstOrNull {
            it.state == CreateReviewMediaUiModel.State.UPLOADING
        }
        val hasSucceedUpload = filteredMediaItems.any {
            it.state == CreateReviewMediaUiModel.State.UPLOADED
        }
        val lastFinishedUploadMedia = filteredMediaItems.lastOrNull {
            it.state != CreateReviewMediaUiModel.State.UPLOADING
        }
        val (nextMediaUri, stringResId) = if (uploadingMedia != null) {
            if (lastFinishedUploadMedia == null || !hasSucceedUpload) {
                uploadingMedia.uri to R.string.review_form_waiting_upload_poem
            } else {
                val timeDiffMillis = System.currentTimeMillis() - lastFinishedUploadMedia.finishUploadTimestamp
                if (TimeUnit.MILLISECONDS.toSeconds(timeDiffMillis) > UPLOADED_MEDIA_POEM_EXPIRY_DURATION) {
                    uploadingMedia.uri to R.string.review_form_waiting_upload_poem
                } else {
                    uploadingMedia.uri to R.string.review_form_on_progress_upload_poem
                }
            }
        } else if (filteredMediaItems.any { it.state == CreateReviewMediaUiModel.State.UPLOAD_FAILED }) {
            currentValue.first to Int.ZERO
        } else {
            filteredMediaItems.lastOrNull()?.let { lastMediaItem ->
                val timeDiffMillis = System.currentTimeMillis() - lastMediaItem.finishUploadTimestamp
                if (TimeUnit.MILLISECONDS.toSeconds(timeDiffMillis) > UPLOADED_ALL_MEDIA_POEM_EXPIRY_DURATION || lastMediaItem.uri != currentValue.first) {
                    currentValue.first to Int.ZERO
                } else {
                    currentValue.first to R.string.review_form_success_upload_poem
                }
            } ?: (currentValue.first to Int.ZERO)
        }
        return nextMediaUri to StringRes(stringResId)
    }

    fun mapMediaItems(
        reviewItemMediaUris: List<String>,
        reviewItemsMediaUploadResults: MediaUploadResultMap,
        reviewItemMediaUploadJobs: MediaUploadJobMap,
        existingMediaItems: List<CreateReviewMediaUiModel>,
        uploadBatchNumber: Int,
        showLargeAddMediaItem: Boolean
    ): List<CreateReviewMediaUiModel> {
        return mutableListOf<CreateReviewMediaUiModel>().apply {
            includeMediaItems(reviewItemMediaUris, reviewItemsMediaUploadResults, reviewItemMediaUploadJobs, existingMediaItems, uploadBatchNumber)
            includeAddMediaUiModel(showLargeAddMediaItem, reviewItemMediaUris)
        }
    }

    private fun MutableList<CreateReviewMediaUiModel>.includeMediaItems(
        mediaUris: List<String>,
        mediaUploadResults: MediaUploadResultMap,
        mediaUploadJobs: MediaUploadJobMap,
        existingMediaItems: List<CreateReviewMediaUiModel>,
        uploadBatchNumber: Int
    ) {
        mediaUris.forEach { uri ->
            val uploadResult = mediaUploadResults[uri]
            val uploadJob = mediaUploadJobs[uri]
            if (uploadJob?.isActive != true) {
                when (uploadResult) {
                    is CreateReviewMediaUploadResult.Success -> {
                        val mediaItem = existingMediaItems.find {
                            it.uri == uri
                        }?.let {
                            if (it is CreateReviewMediaUiModel.Image) {
                                it.copy(
                                    uploadId = uploadResult.uploadId,
                                    finishUploadTimestamp = it.finishUploadTimestamp.takeIf {
                                        it.isMoreThanZero()
                                    } ?: System.currentTimeMillis(),
                                    state = CreateReviewMediaUiModel.State.UPLOADED
                                )
                            } else if (it is CreateReviewMediaUiModel.Video) {
                                it.copy(
                                    uploadId = uploadResult.uploadId,
                                    remoteUrl = uploadResult.videoUrl,
                                    finishUploadTimestamp = it.finishUploadTimestamp.takeIf {
                                        it.isMoreThanZero()
                                    } ?: System.currentTimeMillis(),
                                    state = CreateReviewMediaUiModel.State.UPLOADED
                                )
                            } else null
                        } ?: if (isVideoFormat(uri)) {
                            CreateReviewMediaUiModel.Video(
                                uri = uri,
                                uploadId = uploadResult.uploadId,
                                remoteUrl = uploadResult.videoUrl,
                                uploadBatchNumber = uploadBatchNumber,
                                finishUploadTimestamp = System.currentTimeMillis(),
                                state = CreateReviewMediaUiModel.State.UPLOADED
                            )
                        } else {
                            CreateReviewMediaUiModel.Image(
                                uri = uri,
                                uploadId = uploadResult.uploadId,
                                uploadBatchNumber = uploadBatchNumber,
                                finishUploadTimestamp = System.currentTimeMillis(),
                                state = CreateReviewMediaUiModel.State.UPLOADED
                            )
                        }
                        add(mediaItem)
                    }
                    is CreateReviewMediaUploadResult.Error -> {
                        val mediaItem = existingMediaItems.find {
                            it.uri == uri
                        }?.let {
                            if (it is CreateReviewMediaUiModel.Image) {
                                it.copy(
                                    finishUploadTimestamp = it.finishUploadTimestamp.takeIf {
                                        it.isMoreThanZero()
                                    } ?: System.currentTimeMillis(),
                                    state = CreateReviewMediaUiModel.State.UPLOAD_FAILED,
                                    message = uploadResult.message
                                )
                            } else if (it is CreateReviewMediaUiModel.Video) {
                                it.copy(
                                    finishUploadTimestamp = it.finishUploadTimestamp.takeIf {
                                        it.isMoreThanZero()
                                    } ?: System.currentTimeMillis(),
                                    state = CreateReviewMediaUiModel.State.UPLOAD_FAILED,
                                    message = uploadResult.message
                                )
                            } else null
                        } ?: if (isVideoFormat(uri)) {
                            CreateReviewMediaUiModel.Video(
                                uri = uri,
                                uploadBatchNumber = uploadBatchNumber,
                                finishUploadTimestamp = System.currentTimeMillis(),
                                state = CreateReviewMediaUiModel.State.UPLOAD_FAILED,
                                message = uploadResult.message
                            )
                        } else {
                            CreateReviewMediaUiModel.Image(
                                uri = uri,
                                uploadBatchNumber = uploadBatchNumber,
                                finishUploadTimestamp = System.currentTimeMillis(),
                                state = CreateReviewMediaUiModel.State.UPLOAD_FAILED,
                                message = uploadResult.message
                            )
                        }
                        add(mediaItem)
                    }
                    else -> {
                        val mediaItem = existingMediaItems.find {
                            it.uri == uri
                        }?.let {
                            if (it is CreateReviewMediaUiModel.Image) {
                                it.copy(
                                    uploadBatchNumber = uploadBatchNumber,
                                    finishUploadTimestamp = 0L,
                                    state = CreateReviewMediaUiModel.State.UPLOADING
                                )
                            } else if (it is CreateReviewMediaUiModel.Video) {
                                it.copy(
                                    uploadBatchNumber = uploadBatchNumber,
                                    finishUploadTimestamp = 0L,
                                    state = CreateReviewMediaUiModel.State.UPLOADING
                                )
                            } else null
                        } ?: if (isVideoFormat(uri)) {
                            CreateReviewMediaUiModel.Video(
                                uri = uri,
                                uploadBatchNumber = uploadBatchNumber,
                                state = CreateReviewMediaUiModel.State.UPLOADING
                            )
                        } else {
                            CreateReviewMediaUiModel.Image(
                                uri = uri,
                                uploadBatchNumber = uploadBatchNumber,
                                state = CreateReviewMediaUiModel.State.UPLOADING
                            )
                        }
                        add(mediaItem)
                    }
                }
            } else {
                val mediaItem = existingMediaItems.find {
                    it.uri == uri
                }?.let {
                    if (it is CreateReviewMediaUiModel.Image) {
                        it.copy(
                            uploadBatchNumber = uploadBatchNumber,
                            finishUploadTimestamp = 0L,
                            state = CreateReviewMediaUiModel.State.UPLOADING
                        )
                    } else if (it is CreateReviewMediaUiModel.Video) {
                        it.copy(
                            uploadBatchNumber = uploadBatchNumber,
                            finishUploadTimestamp = 0L,
                            state = CreateReviewMediaUiModel.State.UPLOADING
                        )
                    } else null
                } ?: if (isVideoFormat(uri)) {
                    CreateReviewMediaUiModel.Video(
                        uri = uri,
                        uploadBatchNumber = uploadBatchNumber,
                        state = CreateReviewMediaUiModel.State.UPLOADING
                    )
                } else {
                    CreateReviewMediaUiModel.Image(
                        uri = uri,
                        uploadBatchNumber = uploadBatchNumber,
                        state = CreateReviewMediaUiModel.State.UPLOADING
                    )
                }
                add(mediaItem)
            }
        }
    }

    private fun MutableList<CreateReviewMediaUiModel>.includeAddMediaUiModel(
        showLargeAddMediaItem: Boolean,
        mediaUris: List<String>
    ) {
        if (mediaUris.isEmpty()) {
            if (showLargeAddMediaItem) {
                add(CreateReviewMediaUiModel.AddLarge)
            }
        } else if (mediaUris.size < MAX_MEDIA_COUNT) {
            add(
                CreateReviewMediaUiModel.AddSmall(
                    enabled = none { it.state == CreateReviewMediaUiModel.State.UPLOADING }
                )
            )
        }
    }
}
