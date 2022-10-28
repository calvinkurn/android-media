package com.tokopedia.review.feature.createreputation.presentation.viewmodel.old

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.LoadingView
import com.tokopedia.review.common.data.ProductrevGetReviewDetail
import com.tokopedia.review.common.data.ProductrevReviewImageAttachment
import com.tokopedia.review.common.data.ProductrevReviewVideoAttachment
import com.tokopedia.review.common.data.ReviewViewState
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.common.domain.usecase.ProductrevGetReviewDetailUseCase
import com.tokopedia.review.common.util.ReviewConstants
import com.tokopedia.review.feature.createreputation.domain.usecase.ProductrevEditReviewUseCase
import com.tokopedia.review.feature.createreputation.model.BaseImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.model.ImageReviewUiModel
import com.tokopedia.review.feature.createreputation.presentation.mapper.CreateReviewImageMapper
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class CreateReviewViewModel @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatchers,
    private val getReviewDetailUseCase: ProductrevGetReviewDetailUseCase,
    private val uploaderUseCase: UploaderUseCase,
    private val editReviewUseCase: ProductrevEditReviewUseCase,
    private val userSessionInterface: UserSessionInterface,
) : BaseViewModel(coroutineDispatcherProvider.io) {

    companion object {
        const val CREATE_REVIEW_SOURCE_ID = "bjFkPX"
        const val HTTP_PREFIX = "http"
        const val MAX_IMAGE_COUNT = 5
    }

    private var mediaData: MutableList<Any> = mutableListOf()
    private var originalImages: MutableList<String> = mutableListOf()
    private var originalVideos: MutableList<String> = mutableListOf()

    private val _reviewDetails = MutableLiveData<ReviewViewState<ProductrevGetReviewDetail>>()
    val reviewDetails: LiveData<ReviewViewState<ProductrevGetReviewDetail>>
        get() = _reviewDetails

    private val _editReviewResult = MutableLiveData<ReviewViewState<Boolean>>()
    val editReviewResult: LiveData<ReviewViewState<Boolean>>
        get() = _editReviewResult

    private fun mergeImagePickerResultWithOriginalImages(
        imagePickerResult: MutableList<String>,
        imagesFedIntoPicker: MutableList<String>
    ): List<String> {
        return imagePickerResult.mapIndexed { index, result ->
            if (result.endsWith(ReviewConstants.TEMP_IMAGE_EXTENSION)) {
                imagesFedIntoPicker[index]
            } else {
                result
            }
        }
    }

    fun editReview(
        feedbackId: String,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int,
        rating: Int,
        reviewText: String,
        isAnonymous: Boolean
    ) {
        _editReviewResult.postValue(LoadingView())
        if (mediaData.isEmpty()) {
            editReviewWithoutImage(
                feedbackId,
                reputationId,
                productId,
                shopId,
                reputationScore,
                rating,
                reviewText,
                isAnonymous
            )
        } else {
            editReviewWithImage(
                feedbackId,
                reputationId,
                productId,
                shopId,
                reputationScore,
                rating,
                reviewText,
                isAnonymous,
                getSelectedImagesUrl()
            )
        }
    }

    fun getReviewDetails(feedbackId: String) {
        _reviewDetails.value = LoadingView()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                getReviewDetailUseCase.setRequestParams(feedbackId)
                getReviewDetailUseCase.executeOnBackground()
            }
            originalImages = response.productrevGetReviewDetail.review.imageAttachments.map {
                it.fullSize
            }.toMutableList()
            originalVideos = response.productrevGetReviewDetail.review.videoAttachments.mapNotNull {
                it.url
            }.toMutableList()
            _reviewDetails.postValue(Success(response.productrevGetReviewDetail))
        }) {
            _reviewDetails.postValue(Fail(it))
        }
    }

    fun getAfterEditImageList(
        imagePickerResult: MutableList<String>,
        imagesFedIntoPicker: MutableList<String>
    ): MutableList<Any> {
        // Remove old image
        val mergedImagePaths = mergeImagePickerResultWithOriginalImages(imagePickerResult, imagesFedIntoPicker)
        val mappedOriginalVideos = originalVideos.map {
            CreateReviewMediaUiModel.Video(
                uri = it,
                uploadBatchNumber = Int.ZERO,
                remoteUrl = it,
                state = CreateReviewMediaUiModel.State.UPLOADED
            )
        }
        originalImages = mergedImagePaths.filter { it.startsWith(HTTP_PREFIX) }.toMutableList()
        when (mergedImagePaths.size + mappedOriginalVideos.size) {
            MAX_IMAGE_COUNT -> {
                mediaData = mappedOriginalVideos.plus(mergedImagePaths.map {
                    ImageReviewUiModel(it)
                }).toMutableList()
            }
            else -> {
                mediaData.addAll(mappedOriginalVideos.plus(mergedImagePaths.map {
                    ImageReviewUiModel(it)
                }))
                mediaData.add(DefaultImageReviewUiModel())
            }
        }
        return mediaData
    }

    fun getImageList(
        selectedImage: List<ProductrevReviewImageAttachment>,
        videoAttachments: List<ProductrevReviewVideoAttachment>
    ): MutableList<Any> {
        val mappedVideoAttachments = videoAttachments.mapNotNull {
            it.url?.let { url ->
                CreateReviewMediaUiModel.Video(
                    uri = url,
                    uploadBatchNumber = Int.ZERO,
                    remoteUrl = url,
                    state = CreateReviewMediaUiModel.State.UPLOADED
                )
            }
        }
        when (selectedImage.size + videoAttachments.size) {
            MAX_IMAGE_COUNT -> {
                mediaData = mappedVideoAttachments.plus(selectedImage.map {
                    ImageReviewUiModel(it.thumbnail, it.fullSize)
                }).toMutableList()
            }
            else -> {
                mediaData.addAll(mappedVideoAttachments.plus(selectedImage.map {
                    ImageReviewUiModel(it.thumbnail, it.fullSize)
                }))
                mediaData.add(DefaultImageReviewUiModel())
            }
        }
        return mediaData
    }

    fun removeImage(image: BaseImageReviewUiModel): MutableList<Any> {
        mediaData.remove(image)
        originalImages = CreateReviewImageMapper.removeImageFromList(image, originalImages)
        mediaData = CreateReviewImageMapper.addDefaultModelIfLessThan(mediaData, MAX_IMAGE_COUNT)
        return mediaData
    }

    fun removeVideo(): MutableList<Any> {
        mediaData.removeAll { it is CreateReviewMediaUiModel.Video }
        originalVideos.clear()
        mediaData = CreateReviewImageMapper.addDefaultModelIfLessThan(mediaData, MAX_IMAGE_COUNT)
        return mediaData
    }

    fun isImageNotEmpty(): Boolean {
        return mediaData.filterIsInstance<ImageReviewUiModel>().isNotEmpty()
    }

    fun clearImageData() {
        mediaData.clear()
    }

    fun getSelectedImagesUrl(): ArrayList<String> {
        val result = arrayListOf<String>()
        mediaData.filterIsInstance<BaseImageReviewUiModel>().forEach {
            val imageUrl = CreateReviewImageMapper.getImageUrl(it)
            if (imageUrl.isNotEmpty()) {
                result.add(imageUrl)
            }
        }
        return result
    }

    fun getMaxImagePickCount(): Int {
        return MAX_IMAGE_COUNT - originalVideos.size
    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserId(): String {
        return userSessionInterface.userId
    }

    fun getMediaCount(): Int {
        return mediaData.count { it is ImageReviewUiModel || it is CreateReviewMediaUiModel.Video }
    }

    private fun editReviewWithoutImage(
        feedbackId: String,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int,
        rating: Int,
        reviewText: String,
        isAnonymous: Boolean
    ) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                editReviewUseCase.setParams(
                    feedbackId = feedbackId,
                    reputationId = reputationId,
                    productId = productId,
                    shopId = shopId,
                    reputationScore = reputationScore,
                    rating = rating,
                    reviewText = reviewText,
                    isAnonymous = isAnonymous,
                    oldVideoAttachmentUrls = originalVideos
                )
                editReviewUseCase.executeOnBackground()
            }
            if (response.productrevSuccessIndicator != null) {
                if (response.productrevSuccessIndicator.success) {
                    _editReviewResult.postValue(Success(response.productrevSuccessIndicator.success))
                } else {
                    _editReviewResult.postValue(Fail(Throwable()))
                }
            }
        }) {
            _editReviewResult.postValue(Fail(it))
        }
    }

    private fun editReviewWithImage(
        feedbackId: String,
        reputationId: String,
        productId: String,
        shopId: String,
        reputationScore: Int,
        rating: Int,
        reviewText: String,
        isAnonymous: Boolean,
        listOfImages: List<String>
    ) {
        val uploadIdList: ArrayList<String> = ArrayList()
        launchCatchError(block = {
            val response = withContext(coroutineDispatcherProvider.io) {
                repeat(listOfImages.size) {
                    if (!originalImages.contains(listOfImages[it])) {
                        when (val uploadImageResult = uploadImage(listOfImages[it])) {
                            is UploadResult.Success -> {
                                uploadIdList.add(uploadImageResult.uploadId)
                            }
                            is UploadResult.Error -> {
                                _editReviewResult.postValue(
                                    Fail(
                                        MessageErrorException(
                                            uploadImageResult.message
                                        )
                                    )
                                )
                                this@launchCatchError.cancel()
                            }
                        }
                    }
                }
                editReviewUseCase.setParams(
                    feedbackId = feedbackId,
                    reputationId = reputationId,
                    productId = productId,
                    shopId = shopId,
                    reputationScore = reputationScore,
                    rating = rating,
                    reviewText = reviewText,
                    isAnonymous = isAnonymous,
                    oldAttachmentUrls = originalImages,
                    attachmentIds = uploadIdList,
                    oldVideoAttachmentUrls = originalVideos
                )
                editReviewUseCase.executeOnBackground()
            }
            if (response.productrevSuccessIndicator != null) {
                if (response.productrevSuccessIndicator.success) {
                    _editReviewResult.postValue(Success(response.productrevSuccessIndicator.success))
                } else {
                    _editReviewResult.postValue(Fail(Throwable()))
                }
            }
        }) {
            _editReviewResult.postValue(Fail(it))
        }
    }

    private suspend fun uploadImage(imagePath: String): UploadResult {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
            sourceId = CREATE_REVIEW_SOURCE_ID,
            filePath = filePath
        )
        return uploaderUseCase(params)
    }
}