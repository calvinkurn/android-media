package com.tokopedia.shareexperience.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shareexperience.data.util.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorArgRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.uistate.ShareExBottomSheetUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExImageGeneratorUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExNavigationUiState
import com.tokopedia.shareexperience.ui.util.getSelectedChipPosition
import com.tokopedia.shareexperience.ui.util.getSelectedImageUrl
import com.tokopedia.shareexperience.ui.util.map
import com.tokopedia.shareexperience.ui.util.mapError
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ShareExViewModel @Inject constructor(
    private val getSharePropertiesUseCase: ShareExGetSharePropertiesUseCase,
    private val getGeneratedImageUseCase: ShareExGetGeneratedImageUseCase,
    private val dispatchers: CoroutineDispatchers,
    @ApplicationContext private val context: Context
) : BaseViewModel(dispatchers.main) {

    private val _actionFlow =
        MutableSharedFlow<ShareExAction>(extraBufferCapacity = 16)

    // Cache the model from activity and body switching when chip clicked
    private var _bottomSheetModel: ShareExBottomSheetModel? = null
    private var _selectedIdChip: String = ""

    // Cache the default url
    private var _defaultUrl = ""
    private var _defaultImageUrl = ""

    // Cache the throwable for showing error state
    private var _fetchThrowable: Throwable? = null

    /**
     * This ui state only for trigger show bottomsheet
     * Mark the data has been fetched
     */
    private val _fetchedDataState = MutableSharedFlow<Unit>()
    val fetchedDataState = _fetchedDataState.asSharedFlow()

    private val _bottomSheetUiState = MutableStateFlow(ShareExBottomSheetUiState())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    private val _imageGeneratorUiState = MutableStateFlow(ShareExImageGeneratorUiState())

    private val _navigationUiState = MutableSharedFlow<ShareExNavigationUiState>(
        extraBufferCapacity = 16
    )
    val navigationUiState = _navigationUiState.asSharedFlow()

    fun setupViewModelObserver() {
        _actionFlow.process()
    }

    fun processAction(action: ShareExAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    private fun Flow<ShareExAction>.process() {
        onEach {
            when (it) {
                is ShareExAction.FetchShareData -> {
                    getShareData(it.id, it.source, it.defaultUrl, it.defaultImageUrl, it.selectedIdChip)
                }
                is ShareExAction.InitializePage -> {
                    getShareBottomSheetData()
                }
                is ShareExAction.UpdateShareBody -> {
                    updateShareBottomSheetBody(it.position)
                }
                is ShareExAction.UpdateShareImage -> {
                    updateShareImage(it.imageUrl)
                }
                is ShareExAction.GenerateLink -> {
                    generateLink(it.channelEnum)
                }
                is ShareExAction.NavigateToPage -> {
                    navigateToPage(it.appLink)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getShareData(
        id: String,
        pageTypeEnum: ShareExPageTypeEnum,
        defaultUrl: String,
        defaultImageUrl: String,
        selectedIdChip: String
    ) {
        viewModelScope.launch {
            try {
                _defaultUrl = defaultUrl
                _defaultImageUrl = defaultImageUrl
                _selectedIdChip = selectedIdChip
                getSharePropertiesUseCase.getData(
                    ShareExProductBottomSheetRequest(
                        pageType = pageTypeEnum.value,
                        id = id.toLong()
                    )
                )
                    .collectLatest {
                        when (it) {
                            is ShareExResult.Success -> {
                                _bottomSheetModel = it.data
                                _fetchedDataState.emit(Unit)
                            }
                            is ShareExResult.Error -> {
                                _fetchThrowable = it.throwable
                                _fetchedDataState.emit(Unit)
                            }
                            ShareExResult.Loading -> Unit
                        }
                    }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                _fetchThrowable = throwable
                _fetchedDataState.emit(Unit)
                _fetchedDataState.emit(Unit)
            }
        }
    }

    private fun getShareBottomSheetData() {
        viewModelScope.launch {
            try {
                when {
                    (_fetchThrowable != null) -> {
                        getDefaultBottomSheetModel()
                    }
                    (_bottomSheetModel != null) -> {
                        handleBottomSheetModel(_bottomSheetModel!!) // Safe !!
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun handleBottomSheetModel(bottomSheetModel: ShareExBottomSheetModel) {
        val chipPosition = bottomSheetModel.getSelectedChipPosition(_selectedIdChip).orZero()
        val uiResult = bottomSheetModel.map(chipPosition)
        Log.d("TESTT", "${uiResult.joinToString()}")
        updateBottomSheetUiState(
            title = bottomSheetModel.title,
            uiModelList = uiResult,
            bottomSheetModel = bottomSheetModel,
            chipPosition = chipPosition
        )
    }

    private suspend fun getDefaultBottomSheetModel() {
        getSharePropertiesUseCase.getDefaultData(_defaultUrl, _defaultImageUrl)
            .collectLatest {
                when (it) {
                    is ShareExResult.Success -> {
                        val uiResult = it.data.mapError(_defaultUrl, _fetchThrowable ?: Throwable())
                        updateBottomSheetUiState(
                            title = it.data.title,
                            uiModelList = uiResult,
                            bottomSheetModel = it.data,
                            chipPosition = 0 // default
                        )
                    }
                    is ShareExResult.Error, ShareExResult.Loading -> Unit
                }
            }
    }

    private fun updateShareBottomSheetBody(position: Int) {
        viewModelScope.launch {
            try {
                _bottomSheetModel?.let { bottomSheetModel ->
                    val updatedUiResult = bottomSheetModel.map(position = position)
                    updateBottomSheetUiState(
                        title = bottomSheetModel.title,
                        uiModelList = updatedUiResult,
                        bottomSheetModel = bottomSheetModel,
                        chipPosition = position
                    )
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun updateBottomSheetUiState(
        title: String,
        uiModelList: List<Visitable<in ShareExTypeFactory>>,
        bottomSheetModel: ShareExBottomSheetModel,
        chipPosition: Int
    ) {
        _bottomSheetUiState.update { uiState ->
            uiState.copy(
                title = title,
                uiModelList = uiModelList
            )
        }
        _imageGeneratorUiState.update { uiState ->
            uiState.copy(
                selectedImageUrl = bottomSheetModel.getSelectedImageUrl(
                    chipPosition = chipPosition,
                    imagePosition = 0 // Default first image
                )
            )
        }
    }

    private fun updateShareImage(imageUrl: String) {
        viewModelScope.launch {
            try {
                _imageGeneratorUiState.update {
                    it.copy(
                        selectedImageUrl = imageUrl
                    )
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun generateLink(channelEnum: ShareExChannelEnum) {
        viewModelScope.launch {
            try {
                val imageGeneratorParam = ShareExImageGeneratorRequest(
                    sourceId = "wmVUzt",
                    args = listOf(
                        ShareExImageGeneratorArgRequest("product_id", "2150932863"),
                        ShareExImageGeneratorArgRequest("product_price", "600001.000000"),
                        ShareExImageGeneratorArgRequest("product_rating", "0"),
                        ShareExImageGeneratorArgRequest("product_title", "Kitchin Sukēru P2P-001"),
                        ShareExImageGeneratorArgRequest("is_bebas_ongkir", "false"),
                        ShareExImageGeneratorArgRequest("bebas_ongkir_type", "0"),
                        ShareExImageGeneratorArgRequest("has_ribbon", "0"),
                        ShareExImageGeneratorArgRequest("has_campaign", "0"),
                        ShareExImageGeneratorArgRequest("campaign_discount", "0"),
                        ShareExImageGeneratorArgRequest("new_product_price", "0"),
                        ShareExImageGeneratorArgRequest("campaign_info", ""),
                        ShareExImageGeneratorArgRequest("campaign_name", ""),
                        ShareExImageGeneratorArgRequest("product_image_orientation", ""),
                        // TODO: ASK WHEN WE NEED TO USE IMAGE GENERATOR & BE SHOULD PROVIDE platform & product_image_url
                        ShareExImageGeneratorArgRequest("platform", "wa"),
                        ShareExImageGeneratorArgRequest("product_image_url", "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/10/29/74168343-4d42-4798-b1e7-1162ab20e248.jpg") // ASK BE
                    )
                )
                getGeneratedImageUseCase.getData(imageGeneratorParam).collectLatest {
                    when (it) {
                        is ShareExResult.Success -> {
                            Log.d("TESTT", it.data.toString())
                        }
                        is ShareExResult.Error -> {
                            Log.d("TESTT", it.throwable.stackTraceToString())
                        }
                        ShareExResult.Loading -> {
                            Log.d("TESTT", "Loading")
                        }
                    }
                }

                val buo = BranchUniversalObject()
                    .setCanonicalIdentifier("content/12345")
                    .setTitle("My Content Title")
                    .setContentDescription("My Content Description")
                    .setContentImageUrl("https://lorempixel.com/400/400")
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setContentMetadata(ContentMetadata().addCustomMetadata("key1", "value1"))

                // validation all of the value in case error
                val lp = LinkProperties()
                    .setChannel("facebook")
                    .setFeature("sharing")
                    .setCampaign("content 123 launch")
                    .addControlParameter("\$og_title", "Kitchin Sukēru P2P-001 - Rp600.001")
                    .addControlParameter("\$og_description", "Kitchin Sukēru P2P-001 - Rp600.001")
                    .addControlParameter("\$og_url", "https://images.tokopedia.net/img/generator/RKdhUE/c7d0a9e45c851fe4212b5b9845f3697e.jpg")
                    .addControlParameter("\$og_image_url", "https://images.tokopedia.net/img/generator/RKdhUE/c7d0a9e45c851fe4212b5b9845f3697e.jpg")
                    .addControlParameter("an_min_version", "")
                    .addControlParameter("ios_min_version", "")
                    .addControlParameter("\$android_url", "https://www.tokopedia.com/")
                    .addControlParameter("\$ios_url", "https://www.tokopedia.com/")
                    .addControlParameter("\$desktop_url", "https://www.tokopedia.com/")
                    .addControlParameter("\$android_deeplink_path", "tokopedia://home")
                    .addControlParameter("\$ios_deeplink_path", "tokopedia://home")
                    .addControlParameter("source", "android")

                buo.generateShortUrl(context, lp, Branch.BranchLinkCreateListener { url, error ->
                    if (error == null) {
                        Log.d("BRANCH SDK_TESTT", "got my Branch link to share: " + url)
                    } else {
                        Log.d("BRANCH SDK_TESTT", error.message)
                    }
                })

            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun navigateToPage(appLink: String) {
        viewModelScope.launch {
            _navigationUiState.emit(ShareExNavigationUiState(appLink))
        }
    }
}
