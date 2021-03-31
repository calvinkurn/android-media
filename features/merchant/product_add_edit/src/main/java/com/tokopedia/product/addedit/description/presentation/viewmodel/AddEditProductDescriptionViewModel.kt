package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.FULL_YOUTUBE_URL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_YOUTUBE_VIDEO_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.YOUTUBE_URL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.YOUTU_BE_URL
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.domain.usecase.ValidateProductDescriptionUseCase
import com.tokopedia.product.addedit.description.presentation.constant.AddEditProductDescriptionConstants.Companion.ENABLED_HAMPERS_CATEGORY_ID
import com.tokopedia.product.addedit.description.presentation.constant.AddEditProductDescriptionConstants.Companion.INPUT_DEBOUNCE
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

    private val _videoYoutubeNew = MutableLiveData<Result<YoutubeVideoDetailModel>>()
    val videoYoutube: LiveData<Result<YoutubeVideoDetailModel>> = _videoYoutubeNew

    private val videoYoutubeLiveData = MutableLiveData<String>()
    private val descriptionFlow = MutableStateFlow("")

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
        initDescription()
    }

    private fun initVideoYoutube() = launch {
        videoYoutubeLiveData
                .asFlow()
                .filter {
                    return@filter it.isNotBlank()
                }
                .debounce(INPUT_DEBOUNCE)
                .flatMapLatest { url ->
                    getYoutubeVideo(url)
                            .catch {
                                emit(Fail(it))
                            }
                }
                .flowOn(coroutineDispatcher.io)
                .collect {
                    _videoYoutubeNew.value = it
                }
    }

    private fun initDescription() = launch {
        descriptionFlow
                .filter {
                    return@filter it.isNotBlank()
                }
                .debounce(INPUT_DEBOUNCE)
                .flatMapLatest { desc ->
                    validateDescription(desc)
                            .catch {
                                AddEditProductErrorHandler.logExceptionToCrashlytics(it)
                            }
                }
                .flowOn(coroutineDispatcher.io)
                .collect {
                    _descriptionValidationMessage.value = it
                }
    }

    private fun getYoutubeVideo(url: String): Flow<Result<YoutubeVideoDetailModel>> {
        return flow {
            getIdYoutubeUrl(url)?.let { youtubeId  ->
                getYoutubeVideoUseCase.setVideoId(youtubeId)
            }
            emit(Success(convertToYoutubeResponse(getYoutubeVideoUseCase.executeOnBackground())))
        }
    }

    private fun validateDescription(desc: String): Flow<String> {
        return flow {
            validateProductDescriptionUseCase.setParams(desc)
            val response = validateProductDescriptionUseCase.executeOnBackground()
            val validationMessage = response.productValidateV3.data.validationResults
                    .joinToString("\n")
            emit(validationMessage)
        }
    }

    private fun getIdYoutubeUrl(url: String): String? {
        val videoUrl = url.replace("(www\\.|m\\.)".toRegex(), "")
        val uri = Uri.parse(videoUrl)
        return when (uri.host) {
            YOUTU_BE_URL -> uri.lastPathSegment
            YOUTUBE_URL -> uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID)
            FULL_YOUTUBE_URL -> uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID)
            else -> throw MessageErrorException("")
        }
    }

    fun urlYoutubeChanged(url: String) {
        videoYoutubeLiveData.value = url
    }

    fun validateDescriptionChanged(desc: String) {
        descriptionFlow.value = desc
    }

    fun updateProductInputModel(productInputModel: ProductInputModel) {
        _productInputModel.value = productInputModel
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