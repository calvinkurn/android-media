package com.tokopedia.sellerfeedback.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.sellerfeedback.domain.SubmitGlobalFeedbackUseCase
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import javax.inject.Inject

class SellerFeedbackViewModel @Inject constructor(
        dispatcherProviders: CoroutineDispatchers,
        private val uploaderUseCase: UploaderUseCase,
        private val submitGlobalFeedbackUseCase: SubmitGlobalFeedbackUseCase,
        private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProviders.main) {

    companion object {
        private const val SOURCE_ID = "ukrIHD"
    }

    private val feedbackImageList = mutableListOf<ImageFeedbackUiModel>()

    private val feedbackImages = MutableLiveData<List<ImageFeedbackUiModel>>()
    fun getFeedbackImages(): LiveData<List<ImageFeedbackUiModel>> = feedbackImages

    private val submitResult = MutableLiveData<Boolean>()
    fun getSubmitResult(): LiveData<Boolean> = submitResult

    fun setImages(images: List<ImageFeedbackUiModel>) {
        feedbackImageList.clear()
        feedbackImageList.addAll(images)
        feedbackImages.value = feedbackImageList
    }

    fun submitFeedback(sellerFeedback: SellerFeedback) {
        launchCatchError(block = {
            val uploadIds = feedbackImageList.map {
                uploadImage(it.imageUrl)
            }

            sellerFeedback.uploadId1 = uploadIds.getOrNull(0)
            sellerFeedback.uploadId2 = uploadIds.getOrNull(1)
            sellerFeedback.uploadId3 = uploadIds.getOrNull(2)

            sellerFeedback.shopId = userSession.shopId.toIntOrZero()

            submitGlobalFeedbackUseCase.setParams(sellerFeedback)
            val result = submitGlobalFeedbackUseCase.executeOnBackground()
            result.submitGlobalFeedback.let {
                if (it.error) {
                    throw Throwable(it.errorMsg)
                }
            }
            submitResult.postValue(true)
        }, onError = {
            it.message
//            submitResult.postVa
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
            is UploadResult.Error -> throw Throwable(result.message)
        }
    }
}