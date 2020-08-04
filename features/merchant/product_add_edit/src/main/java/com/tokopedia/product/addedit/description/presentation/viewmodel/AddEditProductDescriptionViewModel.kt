package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_YOUTUBE_VIDEO_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.WEB_PREFIX_HTTP
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.WEB_PREFIX_HTTPS
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class AddEditProductDescriptionViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val resource: ResourceProvider,
        private val getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase
) : BaseViewModel(coroutineDispatcher) {

    private var _productInputModel = MutableLiveData(ProductInputModel())
    val productInputModel: LiveData<ProductInputModel> get() = _productInputModel
    var isEditMode: Boolean = false
    var isAddMode: Boolean = false
    val categoryId: String get() {
        return productInputModel.value?.detailInputModel?.categoryId.orEmpty()
    }
    val descriptionInputModel: DescriptionInputModel get() {
        return productInputModel.value?.descriptionInputModel ?: DescriptionInputModel()
    }
    val variantInputModel: VariantInputModel get() {
        return productInputModel.value?.variantInputModel ?: VariantInputModel()
    }
    val hasVariant: Boolean get() {
        productInputModel.value?.apply {
            return variantInputModel.products.isNotEmpty()
        }
        return false
    }
    val hasWholesale: Boolean get() {
        productInputModel.value?.apply {
            return detailInputModel.wholesaleList.isNotEmpty()
        }
        return false
    }

    private val _videoYoutubeNew = MutableLiveData<Pair<Int, Result<YoutubeVideoDetailModel>>>()
    val videoYoutube: MediatorLiveData<Pair<Int, Result<YoutubeVideoDetailModel>>> = MediatorLiveData()
    var isFetchingVideoData: MutableMap<Int, Boolean> = mutableMapOf()
    var urlToFetch: MutableMap<Int, String> = mutableMapOf()
    var fetchedUrl: MutableMap<Int, String> = mutableMapOf()

    init {
        videoYoutube.addSource(_videoYoutubeNew) { pair ->
            val position = pair.first
            when (val result = pair.second) {
                is Success -> videoYoutube.value = Pair(position, result)
                is Fail -> videoYoutube.value = Pair(position, result)
            }
        }
    }

    fun updateProductInputModel(productInputModel: ProductInputModel) {
        _productInputModel.value = productInputModel
    }

    fun getVideoYoutube(videoUrl: String, position: Int) {
        launchCatchError( block = {
            getIdYoutubeUrl(videoUrl)?.let { youtubeId  ->
                getYoutubeVideoUseCase.setVideoId(youtubeId)
                val result = withContext(Dispatchers.IO) {
                    convertToYoutubeResponse(getYoutubeVideoUseCase.executeOnBackground())
                }
                _videoYoutubeNew.value = Pair(position, Success(result))
            }
        }, onError = {
            _videoYoutubeNew.value = Pair(position, Fail(it))
        })
    }

    private fun convertToYoutubeResponse(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoDetailModel {
        return typeRestResponseMap[YoutubeVideoDetailModel::class.java]?.getData() as YoutubeVideoDetailModel
    }

    private fun getIdYoutubeUrl(videoUrl: String): String? {
        return try {
            // add https:// prefix to videoUrl
            var webVideoUrl = if (videoUrl.startsWith(WEB_PREFIX_HTTP) || videoUrl.startsWith(WEB_PREFIX_HTTPS)) {
                videoUrl
            } else {
                WEB_PREFIX_HTTPS + videoUrl
            }
            webVideoUrl = webVideoUrl.replace("(www\\.|m\\.)".toRegex(), "")

            val uri = Uri.parse(webVideoUrl)
            when (uri.host) {
                "youtu.be" -> uri.lastPathSegment
                "youtube.com" -> uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID)
                "www.youtube.com" -> uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID)
                else -> throw MessageErrorException("")
            }
        } catch (e: NullPointerException) {
            throw MessageErrorException(e.message)
        }
    }

    fun validateDuplicateVideo(inputUrls: MutableList<VideoLinkModel>, url: String): String {
        var errorMessage = ""
        val videoLinks = inputUrls.filter {
            it.inputUrl == url
        }
        if (videoLinks.size > 1) errorMessage = resource.getDuplicateProductVideoErrorMessage() ?: ""
        return errorMessage
    }

    fun validateInputVideo(inputUrls: MutableList<VideoLinkModel>): Boolean {
        val videoLinks = inputUrls.filter {
            it.errorMessage.isNotEmpty()
        }
        return videoLinks.isEmpty()
    }

    fun getVariantSelectedMessage(): String {
        return if (hasVariant) {
            resource.getVariantAddedMessage().orEmpty()
        } else {
            resource.getVariantEmptyMessage().orEmpty()
        }
    }

    fun getVariantTypeMessage(position: Int): String {
        variantInputModel.selections.getOrNull(position)?.let {
            return it.variantName
        }
        return ""
    }

    fun getVariantCountMessage(position: Int): String {
        variantInputModel.selections.getOrNull(position)?.let {
            // generate count of variant eg. 4 Varian
            return "${it.options.size} ${resource.getVariantCountSuffix().orEmpty()}"
        }
        return ""
    }
}