package com.tokopedia.product.addedit.description.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.description.domain.usecase.GetYoutubeVideoUseCase
import com.tokopedia.product.addedit.description.presentation.model.*
import com.tokopedia.product.addedit.description.presentation.model.youtube.YoutubeVideoModel
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.util.ArrayList
import javax.inject.Inject

class AddEditProductDescriptionViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val resource: ResourceProvider,
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val getYoutubeVideoUseCase: GetYoutubeVideoUseCase,
        private val saveProductDraftUseCase: SaveProductDraftUseCase
) : BaseViewModel(coroutineDispatcher) {

    var categoryId: String = ""
    var variantCountList: ArrayList<Int> = arrayListOf(0, 0)
    var variantNameList: ArrayList<String> = arrayListOf("", "")
    var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()
    var isEditMode: Boolean = false

    private val _productVariant = MutableLiveData<Result<List<ProductVariantByCatModel>>>()
    val productVariant: LiveData<Result<List<ProductVariantByCatModel>>>
        get() = _productVariant

    private val _videoYoutube = MutableLiveData<Result<YoutubeVideoModel>>()
    val videoYoutube: LiveData<Result<YoutubeVideoModel>> = _videoYoutube

    private val _saveProductDraftResult = MutableLiveData<Result<Long>>()
    val saveProductDraftResult: LiveData<Result<Long>>
        get() = _saveProductDraftResult

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
            getIdYoutubeUrl(videoUrl)?.let { youtubeId  ->
                getYoutubeVideoUseCase.setVideoId(youtubeId)
                val result = withContext(Dispatchers.IO) {
                    convertToYoutubeResponse(getYoutubeVideoUseCase.executeOnBackground())
                }
                _videoYoutube.value = Success(result)
            }
        }, onError = {
            _videoYoutube.value = Fail(it)
        })
    }

    private fun convertToYoutubeResponse(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoModel {
        return typeRestResponseMap[YoutubeVideoModel::class.java]?.getData() as YoutubeVideoModel
    }

    private fun getIdYoutubeUrl(videoUrl: String): String? {
        return try {
            // add https:// prefix to videoUrl
            val webVideoUrl =
                    if (videoUrl.startsWith(WEB_PREFIX_HTTP) ||
                            videoUrl.startsWith(WEB_PREFIX_HTTPS)) videoUrl else WEB_YOUTUBE_PREFIX + videoUrl
            val uri = Uri.parse(webVideoUrl)
            when {
                uri.host == "youtu.be" -> uri.lastPathSegment
                uri.host == "www.youtube.com" -> uri.getQueryParameter(KEY_YOUTUBE_VIDEO_ID)
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

    fun saveProductDraft(productDraft: ProductDraft, productId: Long, isUploading: Boolean) {
        launchCatchError(block = {
            saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(productDraft, productId, isUploading)
            _saveProductDraftResult.value = withContext(Dispatchers.IO) {
                saveProductDraftUseCase.executeOnBackground()
            }.let { Success(it) }
        }, onError = {
            _saveProductDraftResult.value = Fail(it)
        })
    }

    fun setVariantInput(productVariant: ArrayList<ProductVariantCombinationViewModel>,
                        variantOptionParent: ArrayList<ProductVariantOptionParent>) {
        variantInputModel.variantOptionParent = mapVariantOption(variantOptionParent)
        variantInputModel.productVariant = mapProductVariant(productVariant, variantOptionParent)
        setVariantNamesAndCount(productVariant, variantOptionParent)
    }

    private fun mapProductVariant(productVariant: ArrayList<ProductVariantCombinationViewModel>,
                                  variantOptionParent: ArrayList<ProductVariantOptionParent>
    ): ArrayList<ProductVariantCombinationViewModel> {
        productVariant.forEach { variant ->
            val options: ArrayList<Int> = ArrayList()
            val level1Id = getVariantOptionIndex(variant.level1String,
                    variantOptionParent)
            val level2Id = getVariantOptionIndex(variant.level2String,
                    variantOptionParent)
            level1Id?.let { options.add(it) }
            level2Id?.let { options.add(it) }
            variant.opt = options
        }
        return productVariant
    }

    private fun getVariantOptionIndex(variantValue: String?,
                                       variantOptionParent: List<ProductVariantOptionParent>): Int? {
        variantOptionParent.forEach { productVariantOptionParent ->
            productVariantOptionParent.productVariantOptionChild?.let {
                for (outputIndex in it.indices){
                    if (it[outputIndex].value == variantValue) return outputIndex
                }
            }
        }
        return null
    }

    private fun mapVariantOption(variantOptionParent: ArrayList<ProductVariantOptionParent>):
            ArrayList<ProductVariantOptionParent> = variantOptionParent.map {
                it.productVariantOptionChild?.forEachIndexed { index, productVariantOptionChild ->
                    productVariantOptionChild.pvo = index
                }
                it
            } as ArrayList<ProductVariantOptionParent>

    private fun setVariantNamesAndCount(productVariant: ArrayList<ProductVariantCombinationViewModel>,
                                        variantOptionParent: ArrayList<ProductVariantOptionParent>) {
        productVariant.firstOrNull().let { variant ->
            // process level 1
            setVariantName(0, variant?.level1String, variantOptionParent)
            // process level 2
            setVariantName(1, variant?.level2String, variantOptionParent)
        }

        setVariantCountLevel1(productVariant)
        setVariantCountLevel2(productVariant)
    }

    private fun setVariantCountLevel1(productVariant: ArrayList<ProductVariantCombinationViewModel>) {
        val distictOptionList = productVariant.distinctBy {
            it.level1String
        }
        variantCountList[0] = distictOptionList.size
    }

    private fun setVariantCountLevel2(productVariant: ArrayList<ProductVariantCombinationViewModel>) {
        val distictOptionList = productVariant.distinctBy {
            it.level2String
        }
        variantCountList[1] = distictOptionList.size
    }

    private fun setVariantName(
            changedIndex: Int,
            optionChildValue: String?,
            variantOptionParent: ArrayList<ProductVariantOptionParent>) {
        variantOptionParent.forEach { optionParent ->
            optionParent.productVariantOptionChild?.forEach { optionChild ->
                if (optionChildValue == optionChild.value) {
                    variantNameList[changedIndex] = optionParent.name ?: ""
                }
            }
        }
    }

    fun getVariantSelectedMessage(): String {
        var level1Message = ""
        var level2Message = ""
        if (variantNameList[0].isNotEmpty()) {
            level1Message = variantCountList[0].toString() + " " + variantNameList[0]
        }

        if (variantNameList[1].isNotEmpty()) {
            level2Message = ", " + variantCountList[1].toString() + " " + variantNameList[1]
        }

        return if (level1Message.isNotEmpty()) {
            resource.getVariantAddedMessage() + level1Message + level2Message
        } else {
            resource.getVariantEmptyMessage() ?: ""
        }
    }

    fun getVariantButtonMessage(): String {
        return if (variantNameList[0].isNotEmpty()) {
            resource.getVariantButtonAddedMessage()
        } else {
            resource.getVariantButtonEmptyMessage()
        } ?: ""
    }

    companion object {
        const val KEY_YOUTUBE_VIDEO_ID = "v"
        const val WEB_PREFIX_HTTP = "http://"
        const val WEB_PREFIX_HTTPS = "https://"
        const val WEB_YOUTUBE_PREFIX = "https://"
    }
}