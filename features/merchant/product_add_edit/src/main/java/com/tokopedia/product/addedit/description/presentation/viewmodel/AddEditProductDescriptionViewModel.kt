package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_YOUTUBE_VIDEO_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.WEB_PREFIX_HTTP
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.WEB_PREFIX_HTTPS
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.domain.usecase.ValidateProductDescriptionUseCase
import com.tokopedia.product.addedit.description.presentation.constant.AddEditProductDescriptionConstants.Companion.ENABLED_HAMPERS_CATEGORY_ID
import com.tokopedia.product.addedit.description.presentation.constant.AddEditProductDescriptionConstants.Companion.VIDEO_DEBOUNCE_TIME
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AddEditProductDescriptionViewModel @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatchers,
    private val resource: ResourceProvider,
    private val getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase,
    private val validateProductDescriptionUseCase: ValidateProductDescriptionUseCase
) : BaseViewModel(coroutineDispatcher.main) {

    private var _productInputModel = MutableLiveData(ProductInputModel())
    val productInputModel: LiveData<ProductInputModel> get() = _productInputModel

    private var _descriptionValidationMessage = MutableLiveData<String>()
    val descriptionValidationMessage: LiveData<String> get() = _descriptionValidationMessage

    private val _videoYoutubeNew = MutableLiveData<Pair<Int, Result<YoutubeVideoDetailModel>>>()
    val videoYoutube: MediatorLiveData<Pair<Int, Result<YoutubeVideoDetailModel>>> = MediatorLiveData()

    private val videoYoutubeStateFlow = MutableLiveData<Pair<Int, String>>()

    var isFetchingVideoData: MutableMap<Int, Boolean> = mutableMapOf()
    var urlToFetch: MutableMap<Int, String> = mutableMapOf()
    var fetchedUrl: MutableMap<Int, String> = mutableMapOf()

    var isEditMode: Boolean = false
    var isAddMode: Boolean = false
    var isDraftMode: Boolean = false
    var isFirstMoved: Boolean = false

    val descriptionInputModel: DescriptionInputModel? get() {
        return productInputModel.value?.descriptionInputModel
    }

    val variantInputModel: VariantInputModel? get() {
        return productInputModel.value?.variantInputModel
    }

    val hasVariant: Boolean get() {
        productInputModel.value?.apply {
            return variantInputModel.products.isNotEmpty()
        }
        return false
    }

    val isHampersProduct = Transformations.map(productInputModel) { productInputModel ->
        ENABLED_HAMPERS_CATEGORY_ID.any {
            it == productInputModel.detailInputModel.categoryId
        }
    }

    init {
        initVideoYoutube()
    }

    private fun initVideoYoutube() = launch {
        videoYoutubeStateFlow
                .asFlow()
                .debounce(VIDEO_DEBOUNCE_TIME)
                .filter {
                    return@filter it.second.isNotBlank()
                }
                .distinctUntilChanged()
                .flatMapLatest { model ->
                    val url = model.second
                    val position = model.first
                    getYoutubeDataFlow(position, url)
                            .catch {
                                _videoYoutubeNew.value = Pair(position, Fail(it))
                            }
                }
                .flowOn(coroutineDispatcher.io)
                .collect { model ->
                    model.run {
                        _videoYoutubeNew.value = this
                    }
                }
    }

    private fun getYoutubeDataFlow(position: Int, url: String): Flow<Pair<Int, Result<YoutubeVideoDetailModel>>> {
        return flow {
            getIdYoutubeUrl(url)?.let { youtubeId  ->
                getYoutubeVideoUseCase.setVideoId(youtubeId)
                val result = withContext(coroutineDispatcher.io) {
                    convertToYoutubeResponse(getYoutubeVideoUseCase.executeOnBackground())
                }
                emit(Pair(position, Success(result)))
            }
        }
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

    fun urlYoutubeChanged(position: Int, url: String) {
        videoYoutubeStateFlow.value = Pair(position, url)
    }

    fun updateProductInputModel(productInputModel: ProductInputModel) {
        _productInputModel.value = productInputModel
    }

    fun validateProductDescriptionInput(productDescriptionInput: String) {
        launchCatchError(block = {
            val response = withContext(coroutineDispatcher.io) {
                validateProductDescriptionUseCase.setParams(productDescriptionInput)
                validateProductDescriptionUseCase.executeOnBackground()
            }
            val validationMessage = response.productValidateV3.data.validationResults
                    .joinToString("\n")
            _descriptionValidationMessage.value = validationMessage
        }, onError = {
            // log error
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        })
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
        variantInputModel?.apply {
            selections.getOrNull(position)?.let {
                return it.variantName
            }
        }
        return ""
    }

    fun getVariantCountMessage(position: Int): String {
        variantInputModel?.apply {
            selections.getOrNull(position)?.let {
                // generate count of variant eg. 4 Varian
                return "${it.options.size} ${resource.getVariantCountSuffix().orEmpty()}"
            }
        }
        return ""
    }

    fun getIsAddMode(): Boolean {
        return isAddMode && !isDraftMode
    }

    private fun convertToYoutubeResponse(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoDetailModel {
        return typeRestResponseMap[YoutubeVideoDetailModel::class.java]?.getData() as YoutubeVideoDetailModel
    }
}