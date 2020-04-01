package com.tokopedia.product.addedit.description.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.presentation.model.youtube.YoutubeResponse
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductDescriptionViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val getProductVariantUseCase: GetProductVariantUseCase
) : BaseViewModel(coroutineDispatcher) {

    private val _homeTicker = MutableLiveData<Success<List<ProductVariantByCatModel>>>()
    val homeTicker: LiveData<Success<List<ProductVariantByCatModel>>>
        get() = _homeTicker

    private val _videoYoutube = MutableLiveData<Result<YoutubeResponse>>()
    val youtubeVideo: LiveData<Result<YoutubeResponse>> = _videoYoutube

    fun getVariants(categoryId: String) {
        launchCatchError(block = {
            _homeTicker.value = Success(withContext(Dispatchers.IO) {
                getProductVariantUseCase.params =
                        GetProductVariantUseCase.createRequestParams(categoryId)
                getProductVariantUseCase.executeOnBackground()
            })
        }, onError = {
            // no-op
        })
    }

    fun getVideoYoutube(videoId: String) {
        launchCatchError( block = {

        }, onError = {

        })
    }
}