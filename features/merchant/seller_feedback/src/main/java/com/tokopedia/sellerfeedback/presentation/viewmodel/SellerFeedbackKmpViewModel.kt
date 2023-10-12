package com.tokopedia.sellerfeedback.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql.Result
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.feedback.data.param.FeedbackParam
import com.tokopedia.seller.feedback.domain.SubmitFeedbackUseCase
import com.tokopedia.sellerfeedback.data.SubmitResultKmp
import com.tokopedia.sellerfeedback.error.SubmitThrowable
import com.tokopedia.sellerfeedback.error.UploadThrowable
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import java.io.IOException
import javax.inject.Inject

class SellerFeedbackKmpViewModel @Inject constructor(
    dispatcherProviders: CoroutineDispatchers,
    private val uploaderUseCase: UploaderUseCase,
    private val submitFeedbackUseCase: SubmitFeedbackUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProviders.main) {

    companion object {
        private const val SOURCE_ID = "ukrIHD"
    }

    private val feedbackImageList = mutableListOf<ImageFeedbackUiModel>()

    private val feedbackImages = MutableLiveData<List<ImageFeedbackUiModel>>()
    fun getFeedbackImages(): LiveData<List<ImageFeedbackUiModel>> = feedbackImages

    private val submitResultKmp = MutableLiveData<SubmitResultKmp>()
    fun getSubmitResultKmp(): LiveData<SubmitResultKmp> = submitResultKmp

    fun setImages(images: List<ImageFeedbackUiModel>) {
        feedbackImageList.clear()
        feedbackImageList.addAll(images)
        feedbackImages.value = feedbackImageList
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

            val submitGlobalFeedbackError = (result as? Result.Failure)
            throwSubmitFeedbackExceptionIfAny(submitGlobalFeedbackError)

            val submitGlobalFeedback = (result as? Result.Success)?.data
            if (submitGlobalFeedback?.isError == true) {
                throw SubmitThrowable(submitGlobalFeedback.errorMessage)
            }

            submitResultKmp.postValue(SubmitResultKmp.SubmitFeedbackSuccess(submitGlobalFeedback))
        }, onError = {
                val result = when (it) {
                    is UploadThrowable -> SubmitResultKmp.UploadFail(it)
                    is SubmitThrowable -> SubmitResultKmp.SubmitFail(it)
                    else -> SubmitResultKmp.NetworkFail(it)
                }
                submitResultKmp.postValue(result)
            })
    }

    private fun throwSubmitFeedbackExceptionIfAny(submitGlobalFeedbackError: Result.Failure?) {
        when (submitGlobalFeedbackError) {
            is Result.Failure.HttpFailure -> {
                throw MessageErrorException(submitGlobalFeedbackError.errorBody)
            }

            is Result.Failure.NetworkFailure -> {
                throw IOException(submitGlobalFeedbackError.exception)
            }

            is Result.Failure.UnknownFailure -> {
                throw SubmitThrowable(submitGlobalFeedbackError.exception.localizedMessage.orEmpty())
            }

            else -> {
                // no op
            }
        }
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
