package com.tokopedia.shareexperience.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shareexperience.data.util.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.ShareExConstants
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.domain.model.property.linkproperties.ShareExLinkProperties
import com.tokopedia.shareexperience.domain.model.request.bottomsheet.ShareExProductBottomSheetRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorArgRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGenerateShortLinkUseCase
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.uistate.ShareExBottomSheetUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExImageGeneratorUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExNavigationUiState
import com.tokopedia.shareexperience.ui.util.getImageGeneratorProperty
import com.tokopedia.shareexperience.ui.util.getSelectedChipPosition
import com.tokopedia.shareexperience.ui.util.getSelectedImageUrl
import com.tokopedia.shareexperience.ui.util.map
import com.tokopedia.shareexperience.ui.util.mapError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ShareExViewModel @Inject constructor(
    private val getSharePropertiesUseCase: ShareExGetSharePropertiesUseCase,
    private val getGeneratedImageUseCase: ShareExGetGeneratedImageUseCase,
    private val generateShortLinkUseCase: ShareExGenerateShortLinkUseCase,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _actionFlow =
        MutableSharedFlow<ShareExAction>(extraBufferCapacity = 16)

    // Cache id & page type for short link
    private var _id: String = ""
    private var _pageType: ShareExPageTypeEnum = ShareExPageTypeEnum.PDP

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
                    handleUpdateShareImage(it.imageUrl)
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
                _id = id
                _pageType = pageTypeEnum
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
        // Change image generator property, it wil be used later when channel clicked
        val imageGeneratorProperty = bottomSheetModel.getImageGeneratorProperty(chipPosition)
        updateImageGeneratorUiState(
            imageUrl = bottomSheetModel.getSelectedImageUrl(
                chipPosition = chipPosition,
                imagePosition = 0 // Default first image
            ),
            sourceId = imageGeneratorProperty?.sourceId,
            args = imageGeneratorProperty?.args
        )
    }

    private fun handleUpdateShareImage(imageUrl: String) {
        // Each chip might have different source id and args
        val imageGeneratorProperty = _bottomSheetModel?.getImageGeneratorProperty(imageUrl)
        updateImageGeneratorUiState(
            imageUrl = imageUrl,
            sourceId = imageGeneratorProperty?.sourceId,
            args = imageGeneratorProperty?.args
        )
    }

    private fun updateImageGeneratorUiState(
        imageUrl: String,
        sourceId: String?,
        args: Map<String, String>?
    ) {
        _imageGeneratorUiState.update {
            it.copy(
                selectedImageUrl = imageUrl,
                sourceId = sourceId,
                args = args
            )
        }
    }

    private fun generateLink(channelEnum: ShareExChannelEnum) {
        viewModelScope.launch {
            try {
                // Expected to use !!, should throw error when empty
                val bottomSheetModel = _bottomSheetModel!!
                val chipPosition = bottomSheetModel.getSelectedChipPosition(_selectedIdChip).orZero()
                val shareProperty = bottomSheetModel.bottomSheetPage.listShareProperty[chipPosition]
                val campaign = generateCampaign(shareProperty)
                val linkPropertiesWithCampaign = shareProperty.linkProperties.copy(
                    androidUrl = generateUrlWithUTM(shareProperty.linkProperties.androidUrl, channelEnum, campaign),
                    iosUrl = generateUrlWithUTM(shareProperty.linkProperties.iosUrl, channelEnum, campaign),
                    desktopUrl = generateUrlWithUTM(shareProperty.linkProperties.desktopUrl, channelEnum, campaign),
                    campaign = campaign
                )
                // Get generated image first
                generateImageFlow(channelEnum).collectLatest {
                    val isAffiliate = bottomSheetModel.bottomSheetPage.listShareProperty[chipPosition].affiliate.eligibility.isEligible
                    val shortLinkRequest = generateShortLinkRequest(channelEnum, linkPropertiesWithCampaign, isAffiliate)
                    generateShortLinkUseCase.getShortLink(shortLinkRequest).collectLatest {
                        Log.d("TESTTT", "$it")
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun generateUrlWithUTM(
        url: String,
        channelEnum: ShareExChannelEnum,
        campaign: String
    ): String {
        val utmSource = ShareExConstants.BranchValue.SOURCE
        val uri = Uri.parse(url)
        val newUri = Uri.Builder()
            .scheme(uri.scheme)
            .authority(uri.authority)
            .path(uri.path)
        if (uri.query != null) {
            newUri.appendQueryParameter("utm_source", utmSource)
            newUri.appendQueryParameter("utm_medium", channelEnum.label)
            newUri.appendQueryParameter("utm_campaign", campaign)
        } else {
            newUri.query("utm_source=$utmSource&utm_medium=${channelEnum.label}&utm_campaign=$campaign")
        }
        return newUri.build().toString()
    }

    private fun generateCampaign(shareProperty: ShareExPropertyModel): String {
        var campaign = "${_pageType.name}-$_id"
        campaign += if (userSession.isLoggedIn) {
            "-${userSession.userId}"
        } else {
            "-0"
        }
        campaign += "-${getSimpleDate()}"
        if (shareProperty.imageGenerator?.sourceId?.isNotBlank() == true) {
            campaign += "-${shareProperty.imageGenerator.sourceId}"
        }
        return campaign
    }

    private fun getSimpleDate(): String {
        return SimpleDateFormat("ddMMyy", Locale.getDefault()).format(Date())
    }

    private fun generateShortLinkRequest(
        channelEnum: ShareExChannelEnum,
        finalLinkProperties: ShareExLinkProperties,
        isAffiliate: Boolean
    ): ShareExShortLinkRequest {
        return ShareExShortLinkRequest(
            identifierId = _id,
            channelEnum = channelEnum,
            linkerPropertiesRequest = finalLinkProperties,
            fallbackPriorityEnumList = getFallbackPriorityEnumList(isAffiliate),
            defaultUrl = _defaultUrl
        )
    }

    @OptIn(FlowPreview::class)
    private suspend fun generateImageFlow(channelEnum: ShareExChannelEnum): Flow<String> {
        return try {
            // Source Id and Args are nullable, that means do not use image generator
            val imageGeneratorParam = ShareExImageGeneratorRequest(
                sourceId = _imageGeneratorUiState.value.sourceId,
                args = _imageGeneratorUiState.value.args?.entries?.map { entry ->
                    ShareExImageGeneratorArgRequest(entry.key, entry.value)
                }
            )
            val param = ShareExImageGeneratorWrapperRequest(
                params = imageGeneratorParam,
                originalImageUrl = _imageGeneratorUiState.value.selectedImageUrl
            )
            getGeneratedImageUseCase.getData(param, channelEnum).flatMapConcat {
                var flowResult: Flow<String> = flowOf()
                when (it) {
                    is ShareExResult.Success -> {
                        flowResult = flow {
                            emit(it.data.imageUrl)
                        }
                    }
                    is ShareExResult.Error -> {
                        flowResult = flow {
                            emit(_imageGeneratorUiState.value.selectedImageUrl)
                        }
                    }
                    ShareExResult.Loading -> Unit
                }
                flowResult
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            flow {
                emit(_imageGeneratorUiState.value.selectedImageUrl)
            }
        }
    }

    private fun getFallbackPriorityEnumList(
        isAffiliate: Boolean
    ): List<ShareExShortLinkFallbackPriorityEnum> {
        return if (isAffiliate) {
            listOf(
                ShareExShortLinkFallbackPriorityEnum.AFFILIATE,
                ShareExShortLinkFallbackPriorityEnum.BRANCH,
                ShareExShortLinkFallbackPriorityEnum.DEFAULT
            )
        } else {
            listOf(
                ShareExShortLinkFallbackPriorityEnum.BRANCH,
                ShareExShortLinkFallbackPriorityEnum.DEFAULT
            )
        }
    }

    private fun navigateToPage(appLink: String) {
        viewModelScope.launch {
            _navigationUiState.emit(ShareExNavigationUiState(appLink))
        }
    }
}
