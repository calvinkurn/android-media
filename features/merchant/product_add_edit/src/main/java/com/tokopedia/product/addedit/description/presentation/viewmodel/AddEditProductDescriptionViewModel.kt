package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.domain.usecase.GetYoutubeVideoUseCase
import com.tokopedia.product.addedit.description.presentation.model.youtube.YoutubeVideoModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class AddEditProductDescriptionViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val getYoutubeVideoUseCase: GetYoutubeVideoUseCase
) : BaseViewModel(coroutineDispatcher) {

    var categoryId: String = ""
    var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()
    var isEditMode: Boolean = false

    private val _productVariant = MutableLiveData<Result<List<ProductVariantByCatModel>>>()
    val productVariant: LiveData<Result<List<ProductVariantByCatModel>>>
        get() = _productVariant

    private val _videoYoutube = MutableLiveData<Result<YoutubeVideoModel>>()
    val videoYoutube: LiveData<Result<YoutubeVideoModel>> = _videoYoutube

    fun getVariants(categoryId: String) {
        launchCatchError(block = {
            _productVariant.value = Success(withContext(Dispatchers.IO) {
                getProductVariantUseCase.params =
                        GetProductVariantUseCase.createRequestParams(categoryId)
                getProductVariantUseCase.executeOnBackground()
            })
        }, onError = {
            _productVariant.value = Fail(it)
        })
    }

    fun getVideoYoutube(videoUrl: String) {
        launchCatchError( block = {
            getYoutubeVideoUseCase.setVideoId(getIdYoutubeUrl(videoUrl))
            val result = withContext(Dispatchers.IO) {
                convertToYoutubeResponse(getYoutubeVideoUseCase.executeOnBackground())
            }
            _videoYoutube.value = Success(result)
        }, onError = {
            _videoYoutube.value = Fail(it)
        })
    }

    private fun convertToYoutubeResponse(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoModel {
        return typeRestResponseMap[YoutubeVideoModel::class.java]?.getData() as YoutubeVideoModel
    }

    private fun getIdYoutubeUrl(videoUrl: String): String {
        return try {
            val uri = Uri.parse(videoUrl)
            uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID) ?: ""
        } catch (e: NullPointerException) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        const val KEY_YOUTUBE_VIDEO_ID = "v"
    }
}