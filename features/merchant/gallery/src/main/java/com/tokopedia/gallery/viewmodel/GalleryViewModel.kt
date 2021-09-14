package com.tokopedia.gallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gallery.domain.GetReviewImagesUseCase
import com.tokopedia.gallery.networkmodel.ProductrevGetReviewImage
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val getReviewImagesUseCase: GetReviewImagesUseCase,
    coroutineDispatchers: CoroutineDispatchers
): BaseViewModel(coroutineDispatchers.io) {

    private var productId = ""
    private val currentPage = MutableLiveData<Int>()

    private val _reviewImages = MediatorLiveData<Result<ProductrevGetReviewImage>>()
    val reviewImages: LiveData<Result<ProductrevGetReviewImage>>
        get() = _reviewImages

    init {
        _reviewImages.addSource(currentPage) {
            getReviewImages(it)
        }
    }

    fun setPage(productId: String, page: Int) {
        this.productId = productId
        currentPage.value = page
    }

    private fun getReviewImages(page: Int) {
        launchCatchError(block = {
            getReviewImagesUseCase.setParams(productId, page)
            val data = getReviewImagesUseCase.executeOnBackground()
            _reviewImages.postValue(Success(data.productrevGetReviewImage))
        }) {
            _reviewImages.postValue(Fail(it))
        }
    }
}