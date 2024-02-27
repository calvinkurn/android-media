package com.tokopedia.shareexperience.ui

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkProperties
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorArgRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetShortLinkUseCase
import com.tokopedia.shareexperience.domain.util.ShareExConstants
import com.tokopedia.shareexperience.domain.util.ShareExLogger
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import com.tokopedia.shareexperience.ui.uistate.ShareExBottomSheetUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExChannelIntentUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExImageGeneratorUiState
import com.tokopedia.shareexperience.ui.util.ShareExIntentErrorEnum
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
import javax.inject.Inject

class ShareExViewModel @Inject constructor(
    private val getSharePropertiesUseCase: ShareExGetSharePropertiesUseCase,
    private val getGeneratedImageUseCase: ShareExGetGeneratedImageUseCase,
    private val getShortLinkUseCase: ShareExGetShortLinkUseCase,
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
    val imageGeneratorModel = _imageGeneratorUiState.asStateFlow()

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
                val bottomSheetArgs = bottomSheetArgs!! // Safe !!
                when {
                    (bottomSheetArgs.throwable != null) -> {
                        getDefaultBottomSheetModel(
                            bottomSheetArgs.throwable,
                            bottomSheetArgs.defaultUrl
                        )
                    }
                    (bottomSheetArgs.bottomSheetModel != null) -> {
                        handleFirstLoadBottomSheetModel(
                            bottomSheetArgs.bottomSheetModel,
                            bottomSheetArgs.selectedChip
                        )
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                ShareExLogger.logExceptionToServerLogger(
                    throwable = throwable,
                    deviceId = userSession.deviceId,
                    description = ::getShareBottomSheetData.name
                )
                getDefaultBottomSheetModel(
                    throwable = throwable,
                    defaultUrl = ""
                )
            }
        }
    }

    private fun handleFirstLoadBottomSheetModel(
        bottomSheetModel: ShareExBottomSheetModel, // args model
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

    private fun getDefaultBottomSheetModel(throwable: Throwable, defaultUrl: String) {
        val defaultShareProperties = getSharePropertiesUseCase.getDefaultData()
        bottomSheetArgs = bottomSheetArgs?.copy(
            bottomSheetModel = defaultShareProperties
        )
        val uiResult = defaultShareProperties.mapError(defaultUrl, throwable)
        updateBottomSheetUiState(
            title = defaultShareProperties.title,
            uiModelList = uiResult,
            bottomSheetModel = defaultShareProperties,
            chipPosition = 0 // default
        )
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
                uiModelList = uiModelList,
                chipPosition = chipPosition
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
                // Safe !! because of try catch
                val bottomSheetArgs = bottomSheetArgs!!
                val bottomSheetModel = bottomSheetArgs.bottomSheetModel!!
                val channelEnum = channelItemModel.channelEnum
                val chipPosition = bottomSheetModel.getSelectedChipPosition(bottomSheetArgs.selectedChip).orZero()
                val shareProperty = bottomSheetModel.bottomSheetPage.listShareProperty[chipPosition]
                val campaign = bottomSheetArgs.trackerArg.utmCampaign.replace(ShareExTrackerArg.SHARE_ID_KEY, shareProperty.shareId.toString())
                val linkPropertiesWithCampaign = shareProperty.linkProperties.copy(
                    androidUrl = generateUrlWithUTM(shareProperty.linkProperties.androidUrl, channelEnum, campaign),
                    iosUrl = generateUrlWithUTM(shareProperty.linkProperties.iosUrl, channelEnum, campaign),
                    desktopUrl = generateUrlWithUTM(shareProperty.linkProperties.desktopUrl, channelEnum, campaign),
                    campaign = campaign
                )
                // Get generated image first
                generateImageFlow(channelItemModel).collectLatest { model ->
                    val isAffiliate = bottomSheetModel.bottomSheetPage.listShareProperty[chipPosition].affiliate.eligibility.isEligible
                    val finalLinkProperties = linkPropertiesWithCampaign.copy(
                        ogImageUrl = model.imageUrl
                    )
                    val shortLinkRequest = generateShortLinkRequest(
                        bottomSheetArgs.identifier,
                        bottomSheetArgs.defaultUrl,
                        bottomSheetArgs.pageTypeEnum,
                        channelEnum,
                        finalLinkProperties,
                        isAffiliate
                    )
                    generateShortLink(shortLinkRequest, channelItemModel, model.imageTypeEnum)
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                ShareExLogger.logExceptionToServerLogger(
                    throwable = throwable,
                    deviceId = userSession.deviceId,
                    description = ::generateLink.name
                )
                updateIntentUiStateWithDefaultUrl(
                    channelItemModel,
                    bottomSheetArgs?.defaultUrl.toEmptyStringIfNull(),
                    throwable,
                    ShareExIntentErrorEnum.DEFAULT_URL_ERROR
                )
            }
        }
    }

    private fun updateIntentUiStateWithDefaultUrl(
        channelItemModel: ShareExChannelItemModel,
        defaultUrl: String,
        throwable: Throwable?,
        errorEnum: ShareExIntentErrorEnum?
    ) {
        updateIntentUiState(
            intent = getAppIntent(channelItemModel, defaultUrl, null),
            message = defaultUrl,
            shortLink = defaultUrl,
            channelEnum = channelItemModel.channelEnum,
            isLoading = false,
            error = throwable,
            imageType = ShareExImageTypeEnum.NO_IMAGE,
            errorEnum = errorEnum
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

        // Only set scheme, authority, and path if they are not null
        uri.scheme?.let { newUri.scheme(it) }
        uri.authority?.let { newUri.authority(it) }
        uri.path?.let { newUri.path(it) }

        if (!uri.query.isNullOrEmpty()) {
            newUri.appendQueryParameter(ShareExConstants.UTM.SOURCE_KEY, utmSource)
            newUri.appendQueryParameter(ShareExConstants.UTM.MEDIUM_KEY, channelEnum.label)
            newUri.appendQueryParameter(ShareExConstants.UTM.CAMPAIGN_KEY, campaign)
        } else {
            val query = "${ShareExConstants.UTM.SOURCE_KEY}=$utmSource&${ShareExConstants.UTM.MEDIUM_KEY}=${channelEnum.label}&${ShareExConstants.UTM.CAMPAIGN_KEY}=$campaign"
            newUri.query(query)
        }
        return newUri.build().toString()
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
        channelItemModel: ShareExChannelItemModel,
        imageType: ShareExImageTypeEnum
    ) {
        getShortLinkUseCase.getShortLink(shortLinkRequest).collectLatest { (apiType, result) ->
            when (result) {
                is ShareExResult.Success -> {
                    downloadImageAndShare(shortLinkRequest, channelItemModel, result.data, imageType)
                }
                is ShareExResult.Error -> {
                    val errorEnum = when (apiType) {
                        ShareExShortLinkFallbackPriorityEnum.AFFILIATE -> ShareExIntentErrorEnum.AFFILIATE_ERROR
                        ShareExShortLinkFallbackPriorityEnum.BRANCH -> ShareExIntentErrorEnum.BRANCH_ERROR
                        ShareExShortLinkFallbackPriorityEnum.DEFAULT -> ShareExIntentErrorEnum.DEFAULT_URL_ERROR
                    }
                    updateIntentUiState(
                        intent = null,
                        message = "",
                        shortLink = "",
                        channelEnum = shortLinkRequest.channelEnum,
                        isLoading = false,
                        error = result.throwable,
                        imageType = imageType,
                        errorEnum = errorEnum
                    )
                }
                ShareExResult.Loading -> setLoadingIntentUiState(channelItemModel.channelEnum)
            }
        }
    }

    private fun setLoadingIntentUiState(channelEnum: ShareExChannelEnum?) {
        updateIntentUiState(
            intent = null,
            message = "",
            shortLink = "",
            channelEnum = channelEnum,
            isLoading = true,
            error = null,
            imageType = ShareExImageTypeEnum.NO_IMAGE,
            errorEnum = null
        )
    }

    private fun updateIntentUiState(
        intent: Intent?,
        message: String,
        shortLink: String,
        channelEnum: ShareExChannelEnum?,
        isLoading: Boolean,
        error: Throwable?,
        imageType: ShareExImageTypeEnum,
        errorEnum: ShareExIntentErrorEnum?
    ) {
        _channelIntentUiState.update {
            it.copy(
                intent = intent,
                message = message,
                shortLink = shortLink,
                channelEnum = channelEnum,
                isLoading = isLoading,
                error = error,
                imageType = imageType,
                errorEnum = errorEnum
            )
        }
    }

    @OptIn(FlowPreview::class)
    private suspend fun generateImageFlow(channel: ShareExChannelItemModel): Flow<ShareExImageGeneratorModel> {
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
                originalImageUrl = _imageGeneratorUiState.value.selectedImageUrl,
                platform = channel.platform,
                imageResolution = channel.imageResolution
            )
            // Change to flow with model of generated image url (short link) and image type (tracking)
            getGeneratedImageUseCase.getData(param).flatMapConcat {
                var flowResult: Flow<ShareExImageGeneratorModel> = flowOf()
                when (it) {
                    is ShareExResult.Success -> {
                        flowResult = flow {
                            emit(it.data)
                        }
                    }
                    is ShareExResult.Error -> {
                        flowResult = flow {
                            emit(
                                ShareExImageGeneratorModel(
                                    _imageGeneratorUiState.value.selectedImageUrl,
                                    ShareExImageTypeEnum.DEFAULT
                                )
                            )
                        }
                    }
                    ShareExResult.Loading -> setLoadingIntentUiState(null)
                }
                flowResult
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            ShareExLogger.logExceptionToServerLogger(
                throwable = throwable,
                deviceId = userSession.deviceId,
                description = ::generateImageFlow.name
            )
            flow {
                emit(
                    ShareExImageGeneratorModel(
                        _imageGeneratorUiState.value.selectedImageUrl,
                        ShareExImageTypeEnum.DEFAULT
                    )
                )
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
        val intent = Intent()
        intent.action = channelItemModel.actionIntent
        intent.type = channelItemModel.mimeType.textType
        if (channelItemModel.packageName.isNotBlank()) {
            intent.setPackage(channelItemModel.packageName)
        }
        intent.putExtra(Intent.EXTRA_TEXT, messageWithUrl)
        if (imageUri != null) {
            when (channelItemModel.mimeType) {
                ShareExMimeTypeEnum.IMAGE -> {
                    intent.setDataAndType(imageUri, channelItemModel.mimeType.textType)
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri)
                }
                ShareExMimeTypeEnum.ALL -> intent.putExtra(Intent.EXTRA_STREAM, imageUri)
                else -> Unit
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return intent
    }

    private suspend fun downloadImageAndShare(
        shortLinkRequest: ShareExShortLinkRequest,
        channelItemModel: ShareExChannelItemModel,
        shortLink: String,
        imageType: ShareExImageTypeEnum
    ) {
        val imageUrl = shortLinkRequest.linkerPropertiesRequest.ogImageUrl
        val message = shortLinkRequest.linkerPropertiesRequest.message
        val messageWithUrl = if (message.isNotBlank()) "$message $shortLink" else shortLink
        when(channelItemModel.mimeType) {
            ShareExMimeTypeEnum.ALL, ShareExMimeTypeEnum.IMAGE -> {
                getDownloadedImageUseCase.downloadImageThumbnail(imageUrl).collectLatest {
                    when (it) {
                        is ShareExResult.Success -> {
                            updateIntentUiState(
                                intent = getAppIntent(channelItemModel, messageWithUrl, it.data),
                                message = messageWithUrl,
                                shortLink = shortLink,
                                channelEnum = channelItemModel.channelEnum,
                                isLoading = false,
                                error = null,
                                imageType = imageType,
                                errorEnum = null
                            )
                        }
                        is ShareExResult.Error -> {
                            updateIntentUiState(
                                intent = getAppIntent(channelItemModel, messageWithUrl, null),
                                message = messageWithUrl,
                                shortLink = shortLink,
                                channelEnum = channelItemModel.channelEnum,
                                isLoading = false,
                                error = it.throwable,
                                imageType = imageType,
                                errorEnum = ShareExIntentErrorEnum.IMAGE_DOWNLOADER
                            )
                        }
                        ShareExResult.Loading -> Unit
                    }
                }
            }
            else -> {
                updateIntentUiState(
                    intent = getAppIntent(channelItemModel, messageWithUrl, null),
                    message = messageWithUrl,
                    shortLink = shortLink,
                    channelEnum = channelItemModel.channelEnum,
                    isLoading = false,
                    error = null,
                    imageType = imageType,
                    errorEnum = null
                )
            }
        }
    }
}
