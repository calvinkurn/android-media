package com.tokopedia.sellerfeedback.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql.Result
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.seller.feedback.data.param.FeedbackParam
import com.tokopedia.seller.feedback.domain.SubmitFeedbackUseCase
import com.tokopedia.sellerfeedback.data.SubmitResult
import com.tokopedia.sellerfeedback.domain.SubmitGlobalFeedbackUseCase
import com.tokopedia.sellerfeedback.error.SubmitThrowable
import com.tokopedia.sellerfeedback.error.UploadThrowable
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.shared.domain.GetHostPolicyUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class SellerFeedbackKmpViewModel @Inject constructor(
    private val dispatcherProviders: CoroutineDispatchers,
    private val uploaderUseCase: UploaderUseCase,
    private val submitGlobalFeedbackUseCase: SubmitGlobalFeedbackUseCase,
    private val submitFeedbackUseCase: SubmitFeedbackUseCase,
    private val getHostPolicyUseCase: GetHostPolicyUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProviders.main) {

    companion object {
        private const val SOURCE_ID = "ukrIHD"
    }

    private val feedbackImageList = mutableListOf<ImageFeedbackUiModel>()

    private val feedbackImages = MutableLiveData<List<ImageFeedbackUiModel>>()
    fun getFeedbackImages(): LiveData<List<ImageFeedbackUiModel>> = feedbackImages

    private val submitResult = MutableLiveData<SubmitResult>()
    fun getSubmitResult(): LiveData<SubmitResult> = submitResult

    private val _getHostPolicy = MutableLiveData<Result<String>>()
    val getHostPolicy: LiveData<Result<String>>
        get() = _getHostPolicy

    fun setImages(images: List<ImageFeedbackUiModel>) {
        feedbackImageList.clear()
        feedbackImageList.addAll(images)
        feedbackImages.value = feedbackImageList
    }

    fun fetchHostPolicy() {
        launch {
            val getHostPolicyResponse = withContext(dispatcherProviders.io) {
                getHostPolicyUseCase.result("")
            }
            _getHostPolicy.value = getHostPolicyResponse
        }
    }

    fun submitFeedback(sellerFeedback: SellerFeedback) {
        launchCatchError(block = {
            val uploadIds = feedbackImageList.map {
                uploadImage(it.imageUrl)
            }

            sellerFeedback.uploadId1 = uploadIds.getOrNull(0)
            sellerFeedback.uploadId2 = uploadIds.getOrNull(1)
            sellerFeedback.uploadId3 = uploadIds.getOrNull(2)

            sellerFeedback.shopId = userSession.shopId.toLongOrZero()

            val result = submitGlobalFeedbackUseCase.executeOnBackground()

            val submitGlobalFeedback = result.submitGlobalFeedback
            if (submitGlobalFeedback.error) {
                throw SubmitThrowable(submitGlobalFeedback.errorMsg)
            }

            submitResult.postValue(SubmitResult.Success)
        }, onError = {
                val result = when (it) {
                    is UploadThrowable -> SubmitResult.UploadFail(it)
                    is SubmitThrowable -> SubmitResult.SubmitFail(it)
                    else -> SubmitResult.NetworkFail(it)
                }
                submitResult.postValue(result)
            })
    }

    fun submitFeedbackKmp(sellerFeedback: SellerFeedback) {
        launchCatchError(block = {
            val uploadIds = feedbackImageList.map {
                uploadImage(it.imageUrl)
            }

            val submitFeedbackParam = FeedbackParam(
                shopId = userSession.shopId.toLongOrZero(),
                score = sellerFeedback.feedbackScore,
                type = sellerFeedback.feedbackType,
                page = sellerFeedback.feedbackPage,
                detail = sellerFeedback.feedbackDetail,
                uploadId1 = uploadIds.getOrNull(0),
                uploadId2 = uploadIds.getOrNull(1),
                uploadId3 = uploadIds.getOrNull(2)
            )

            val result = submitFeedbackUseCase.execute(submitFeedbackParam)

            val successResult = (result as? Result.Success)?.data

            if (successResult?.isError == true) {
                throw SubmitThrowable(successResult.errorMessage)
            }

            submitResult.postValue(SubmitResult.SubmitFeedbackSuccess(successResult))
        }, onError = {
                val result = when (it) {
                    is UploadThrowable -> SubmitResult.UploadFail(it)
                    is SubmitThrowable -> SubmitResult.SubmitFail(it)
                    else -> SubmitResult.NetworkFail(it)
                }
                submitResult.postValue(result)
            })
    }

    private suspend fun uploadImage(imagePath: String): String {
        val filePath = File(imagePath)
        val params = uploaderUseCase.createParams(
            sourceId = SOURCE_ID,
            filePath = filePath
        )
        return when (val result = uploaderUseCase(params)) {
            is UploadResult.Success -> result.uploadId
            is UploadResult.Error -> throw UploadThrowable(result.message)
        }
    }
}
