package com.tokopedia.shareexperience.ui

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shareexperience.domain.ShareExConstants
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkProperties
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorArgRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetShortLinkUseCase
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.uistate.ShareExBottomSheetUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExChannelIntentUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExImageGeneratorUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExShortLinkUiState
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
    private val generateShortLinkUseCase: ShareExGetShortLinkUseCase,
    private val getDownloadedImageUseCase: ShareExGetDownloadedImageUseCase,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _actionFlow =
        MutableSharedFlow<ShareExAction>(extraBufferCapacity = 16)

    // Cache the args from initializer & fetcher
    var bottomSheetArgs: ShareExBottomSheetArg? = null

    /**
     * Ui state for bottom sheet views
     */
    private val _bottomSheetUiState = MutableStateFlow(ShareExBottomSheetUiState())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    /**
     * Ui state for image generator
     */
    private val _imageGeneratorUiState = MutableStateFlow(ShareExImageGeneratorUiState())

    /**
     * Ui state for cache short link
     */
    private val _shortLinkUiState = MutableStateFlow(ShareExShortLinkUiState())

    /**
     * Ui state for open channel (app) intent
     */
    private val _channelIntentUiState = MutableStateFlow(ShareExChannelIntentUiState())
    val channelIntentUiState = _channelIntentUiState.asStateFlow()

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
                is ShareExAction.InitializePage -> {
                    getShareBottomSheetData()
                }
                is ShareExAction.UpdateShareBody -> {
                    updateShareBottomSheetBody(it.position, it.text)
                }
                is ShareExAction.UpdateShareImage -> {
                    handleUpdateShareImage(it.imageUrl)
                }
                is ShareExAction.GenerateLink -> {
                    generateLink(it.channelItemModel)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getShareBottomSheetData() {
        viewModelScope.launch {
            try {
                bottomSheetArgs?.let {
                    when {
                        (it.throwable != null) -> {
                            getDefaultBottomSheetModel(
                                it.throwable,
                                it.defaultUrl
                            )
                        }
                        (it.bottomSheetModel != null) -> {
                            handleFirstLoadBottomSheetModel(
                                it.bottomSheetModel,
                                it.selectedChip
                            )
                        }
                    }
                } ?: run {
                    throw IllegalArgumentException("BottomSheetArgs is null")
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun handleFirstLoadBottomSheetModel(
        bottomSheetModel: ShareExBottomSheetModel,
        selectedChip: String
    ) {
        val chipPosition = bottomSheetModel.getSelectedChipPosition(selectedChip).orZero()
        val uiResult = bottomSheetModel.map(chipPosition)
        updateBottomSheetUiState(
            title = bottomSheetModel.title,
            uiModelList = uiResult,
            bottomSheetModel = bottomSheetModel,
            chipPosition = chipPosition
        )
    }

    private suspend fun getDefaultBottomSheetModel(throwable: Throwable, defaultUrl: String) {
        getSharePropertiesUseCase.getDefaultData()
            .collectLatest {
                when (it) {
                    is ShareExResult.Success -> {
                        val uiResult = it.data.mapError(defaultUrl, throwable)
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

    private fun updateShareBottomSheetBody(
        position: Int,
        text: String
    ) {
        bottomSheetArgs = bottomSheetArgs?.copy(
            selectedChip = text
        )
        bottomSheetArgs?.bottomSheetModel?.let { bottomSheetModel ->
            val updatedUiResult = bottomSheetModel.map(position = position)
            updateBottomSheetUiState(
                title = bottomSheetModel.title,
                uiModelList = updatedUiResult,
                bottomSheetModel = bottomSheetModel,
                chipPosition = position
            )
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
        val imageGeneratorProperty = bottomSheetArgs?.bottomSheetModel?.getImageGeneratorProperty(imageUrl)
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

    private fun generateLink(channelItemModel: ShareExChannelItemModel) {
        viewModelScope.launch {
            try {
                bottomSheetArgs?.let {
                    val bottomSheetModel = it.bottomSheetModel
                    if (bottomSheetModel != null) {
                        val channelEnum = channelItemModel.channelEnum
                        val chipPosition = bottomSheetModel.getSelectedChipPosition(it.selectedChip).orZero()
                        val shareProperty = bottomSheetModel.bottomSheetPage.listShareProperty[chipPosition]
                        val campaign = generateCampaign(shareProperty, it.identifier, it.pageTypeEnum)
                        val linkPropertiesWithCampaign = shareProperty.linkProperties.copy(
                            androidUrl = generateUrlWithUTM(shareProperty.linkProperties.androidUrl, channelEnum, campaign),
                            iosUrl = generateUrlWithUTM(shareProperty.linkProperties.iosUrl, channelEnum, campaign),
                            desktopUrl = generateUrlWithUTM(shareProperty.linkProperties.desktopUrl, channelEnum, campaign),
                            campaign = campaign
                        )
                        // Get generated image first
                        generateImageFlow(channelEnum).collectLatest { imageUrl ->
                            val isAffiliate = bottomSheetModel.bottomSheetPage.listShareProperty[chipPosition].affiliate.eligibility.isEligible
                            val finalLinkProperties = linkPropertiesWithCampaign.copy(
                                ogImageUrl = imageUrl
                            )
                            val shortLinkRequest = generateShortLinkRequest(
                                it.identifier,
                                it.defaultUrl,
                                it.pageTypeEnum,
                                channelEnum,
                                finalLinkProperties,
                                isAffiliate
                            )
                            generateShortLink(shortLinkRequest, channelItemModel)
                        }
                    } else {
                        updateWithDefaultUrl(
                            channelItemModel,
                            it.defaultUrl,
                            null
                        )
                    }
                } ?: run {
                    throw IllegalArgumentException("BottomSheetArgs is null")
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                updateWithDefaultUrl(
                    channelItemModel,
                    bottomSheetArgs?.defaultUrl ?: "",
                    throwable
                )
            }
        }
    }

    private fun updateWithDefaultUrl(
        channelItemModel: ShareExChannelItemModel,
        defaultUrl: String,
        throwable: Throwable?
    ) {
        updateIntentUiState(
            intent = getAppIntent(channelItemModel, defaultUrl, null),
            message = defaultUrl,
            shortLink = defaultUrl,
            channelEnum = channelItemModel.channelEnum,
            isLoading = false,
            error = throwable,
            isAffiliateError = false
        )
    }

    private fun generateUrlWithUTM(
        url: String,
        channelEnum: ShareExChannelEnum,
        campaign: String
    ): String {
        val utmSource = ShareExConstants.ShortLinkValue.SOURCE
        val uri = Uri.parse(url)
        val newUri = Uri.Builder()
            .scheme(uri.scheme)
            .authority(uri.authority)
            .path(uri.path)
        if (uri.query != null) {
            newUri.appendQueryParameter(ShareExConstants.UTM.SOURCE_KEY, utmSource)
            newUri.appendQueryParameter(ShareExConstants.UTM.MEDIUM_KEY, channelEnum.label)
            newUri.appendQueryParameter(ShareExConstants.UTM.CAMPAIGN_KEY, campaign)
        } else {
            newUri.query("${ShareExConstants.UTM.SOURCE_KEY}=$utmSource&${ShareExConstants.UTM.MEDIUM_KEY}=${channelEnum.label}&${ShareExConstants.UTM.CAMPAIGN_KEY}=$campaign")
        }
        return newUri.build().toString()
    }

    private fun generateCampaign(
        shareProperty: ShareExPropertyModel,
        identifier: String,
        pageTypeEnum: ShareExPageTypeEnum
    ): String {
        var campaign = "${pageTypeEnum.name}-$identifier"
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
        identifier: String,
        defaultUrl: String,
        pageTypeEnum: ShareExPageTypeEnum,
        channelEnum: ShareExChannelEnum,
        finalLinkProperties: ShareExLinkProperties,
        isAffiliate: Boolean
    ): ShareExShortLinkRequest {
        return ShareExShortLinkRequest(
            identifierId = identifier,
            channelEnum = channelEnum,
            linkerPropertiesRequest = finalLinkProperties,
            fallbackPriorityEnumList = getFallbackPriorityEnumList(isAffiliate),
            defaultUrl = defaultUrl,
            pageTypeEnum = pageTypeEnum
        )
    }

    private suspend fun generateShortLink(
        shortLinkRequest: ShareExShortLinkRequest,
        channelItemModel: ShareExChannelItemModel
    ) {
        generateShortLinkUseCase.getShortLink(shortLinkRequest).collectLatest { (apiType, result) ->
            when (result) {
                is ShareExResult.Success -> {
                    downloadImageAndShare(apiType, shortLinkRequest, channelItemModel, result.data)
                }
                is ShareExResult.Error -> {
                    if (apiType == ShareExShortLinkFallbackPriorityEnum.AFFILIATE) {
                        updateIntentUiState(
                            intent = null,
                            message = "",
                            shortLink = "",
                            channelEnum = shortLinkRequest.channelEnum,
                            isLoading = false,
                            error = result.throwable,
                            isAffiliateError = true
                        )
                    } else {
                        updateIntentUiState(
                            intent = null,
                            message = "",
                            shortLink = "",
                            channelEnum = shortLinkRequest.channelEnum,
                            isLoading = false,
                            error = result.throwable
                            // do not update affiliate error
                        )
                    }
                }
                ShareExResult.Loading -> setLoadingIntentUiState(channelItemModel.channelEnum)
            }
        }
    }

    private fun resetIntentUiState() {
    }

    private fun setLoadingIntentUiState(channelEnum: ShareExChannelEnum?) {
        updateIntentUiState(
            intent = null,
            message = "",
            shortLink = "",
            channelEnum = channelEnum,
            isLoading = true,
            error = null,
            isAffiliateError = false
        )
    }

    private fun updateIntentUiState(
        intent: Intent?,
        message: String,
        shortLink: String,
        channelEnum: ShareExChannelEnum?,
        isLoading: Boolean,
        error: Throwable?,
        isAffiliateError: Boolean? = null
    ) {
        if (isAffiliateError != null) {
            _channelIntentUiState.update {
                it.copy(
                    intent = intent,
                    message = message,
                    shortLink = shortLink,
                    channelEnum = channelEnum,
                    isLoading = isLoading,
                    error = error,
                    isAffiliateError = isAffiliateError
                )
            }
        } else {
            _channelIntentUiState.update {
                it.copy(
                    intent = intent,
                    message = message,
                    shortLink = shortLink,
                    channelEnum = channelEnum,
                    isLoading = isLoading,
                    error = error
                )
            }
        }
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
                    ShareExResult.Loading -> setLoadingIntentUiState(null)
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

    private fun getAppIntent(
        channelItemModel: ShareExChannelItemModel,
        messageWithUrl: String,
        imageUri: Uri?
    ): Intent {
        val intent = Intent().apply {
            action = channelItemModel.actionIntent
            type = channelItemModel.mimeType.textType
            if (channelItemModel.packageName.isNotBlank()) {
                setPackage(channelItemModel.packageName)
            }
            putExtra(Intent.EXTRA_TEXT, messageWithUrl)
            if (imageUri != null) {
                when (channelItemModel.mimeType) {
                    ShareExMimeTypeEnum.IMAGE -> {
                        setDataAndType(imageUri, channelItemModel.mimeType.textType)
                        putExtra(Intent.EXTRA_STREAM, imageUri)
                    }
                    ShareExMimeTypeEnum.ALL -> putExtra(Intent.EXTRA_STREAM, imageUri)
                    else -> Unit
                }
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        return intent
    }

    private suspend fun downloadImageAndShare(
        apiTypeEnum: ShareExShortLinkFallbackPriorityEnum,
        shortLinkRequest: ShareExShortLinkRequest,
        channelItemModel: ShareExChannelItemModel,
        shortLink: String
    ) {
        val imageUrl = shortLinkRequest.linkerPropertiesRequest.ogImageUrl
        val message = shortLinkRequest.linkerPropertiesRequest.message
        val messageWithUrl = if (message.isNotBlank()) "$message $shortLink" else shortLink
        getDownloadedImageUseCase.downloadImage(imageUrl).collectLatest {
            when (it) {
                is ShareExResult.Success -> {
                    updateIntentUiState(
                        intent = getAppIntent(channelItemModel, messageWithUrl, it.data),
                        message = messageWithUrl,
                        shortLink = shortLink,
                        channelEnum = channelItemModel.channelEnum,
                        isLoading = false,
                        error = null
                        // do not update affiliate error flag
                    )
                }
                is ShareExResult.Error -> {
                    updateIntentUiState(
                        intent = getAppIntent(channelItemModel, messageWithUrl, null),
                        message = messageWithUrl,
                        shortLink = shortLink,
                        channelEnum = channelItemModel.channelEnum,
                        isLoading = false,
                        error = it.throwable
                        // do not update affiliate error flag
                    )
                }
                ShareExResult.Loading -> Unit
            }
        }
    }
}
