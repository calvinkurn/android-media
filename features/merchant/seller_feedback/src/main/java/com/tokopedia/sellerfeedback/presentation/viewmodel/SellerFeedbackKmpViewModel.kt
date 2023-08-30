package com.tokopedia.sellerfeedback.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql.Result
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.sellerfeedback.data.SubmitResult
import com.tokopedia.sellerfeedback.domain.SubmitGlobalFeedbackUseCase
import com.tokopedia.sellerfeedback.error.SubmitThrowable
import com.tokopedia.sellerfeedback.error.UploadThrowable
import com.tokopedia.sellerfeedback.presentation.SellerFeedback
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel
import com.tokopedia.shared.di.FeatureModule
import com.tokopedia.shared.domain.GetProductUseCase
import com.tokopedia.shared.domain.model.ProductModel
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
    private val getProductUseCase: GetProductUseCase,
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

    private val _productList = MutableLiveData<Result<List<ProductModel>>>()
    val getProductList: LiveData<Result<List<ProductModel>>>
        get() = _productList

    fun setImages(images: List<ImageFeedbackUiModel>) {
        feedbackImageList.clear()
        feedbackImageList.addAll(images)
        feedbackImages.value = feedbackImageList
    }

    fun fetchProductList() {
        launch {
            val productListResponse = withContext(dispatcherProviders.main) {
                getProductUseCase.getProductList()
            }
            _productList.value = productListResponse
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

            submitGlobalFeedbackUseCase.setParams(sellerFeedback)
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
