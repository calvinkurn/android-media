package com.tokopedia.stories.creation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.stories.StoriesStatus
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.creation.common.util.StoriesAppLinkBuilder
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.exception.NotEligibleException
import com.tokopedia.stories.creation.view.model.state.StoriesCreationUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
class StoriesCreationViewModel @Inject constructor(
    private val repo: StoriesCreationRepository,
    private val creationUploader: CreationUploader,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoriesCreationUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<StoriesCreationUiEvent>()
    val uiEvent: Flow<StoriesCreationUiEvent> = _uiEvent

    val mediaFilePath: String
        get() = _uiState.value.media.filePath

    val maxStoriesConfig: StoriesCreationConfiguration.MaxStoriesConfig
        get() = _uiState.value.config.maxStoriesConfig

    val minVideoDuration: Int
        get() = _uiState.value.config.minVideoDuration

    val maxVideoDuration: Int
        get() = _uiState.value.config.maxVideoDuration

    val storyId: String
        get() = _uiState.value.config.storiesId

    val selectedAccount: ContentAccountUiModel
        get() = _uiState.value.selectedAccount

    val productTagSection: List<ProductTagSectionUiModel>
        get() = _uiState.value.productTags

    val maxProductTag: Int
        get() = _uiState.value.config.maxProductTag

    fun submitAction(action: StoriesCreationAction) {
        when (action) {
            is StoriesCreationAction.Prepare -> handlePrepare()
            is StoriesCreationAction.SetMedia -> handleSetMedia(action.media)
            is StoriesCreationAction.SetProduct -> handleSetProduct(action.productTags)
            is StoriesCreationAction.ClickUpload -> handleClickUpload()
        }
    }

    private fun handlePrepare() {
        viewModelScope.launchCatchError(block = {
            /**
             * Stories is only available for shop
             * need FE development to support UGC account
             * */
            val accountList = repo.getCreationAccountList().filter { it.isShop }
            val selectedAccount = accountList.firstOrNull { it.enable }
                ?: throw NotEligibleException()

            val config = repo.getStoryPreparationInfo(selectedAccount)
            val storyId = config.storiesId.ifEmpty { repo.createStory(selectedAccount) }

            _uiState.update {
                it.copy(
                    config = config.copy(
                        storiesId = storyId
                    ),
                    accountList = accountList,
                    selectedAccount = selectedAccount,
                )
            }

            if (config.maxStoriesConfig.isMaxStoryReached) {
                _uiEvent.emit(StoriesCreationUiEvent.ShowTooManyStoriesReminder)
            } else {
                _uiEvent.emit(StoriesCreationUiEvent.OpenMediaPicker)
            }
        }) { throwable ->
            _uiEvent.emit(
                StoriesCreationUiEvent.ErrorPreparePage(throwable)
            )
        }
    }

    private fun handleSetMedia(media: StoriesMedia) {
        _uiState.update {
            it.copy(media = media)
        }
    }

    private fun handleSetProduct(
        productTags: List<ProductTagSectionUiModel>,
    ) {
        _uiState.update {
            it.copy(
                productTags = productTags
            )
        }
    }

    private fun handleClickUpload() {
        viewModelScope.launchCatchError(block = {
            val state = _uiState.value

            repo.updateStoryStatus(
                storyId = state.config.storiesId,
                status = StoriesStatus.Queue,
            )

            val data = CreationUploadData.buildForStories(
                creationId = state.config.storiesId,
                mediaUriList = listOf(state.media.filePath),
                mediaTypeList = listOf(state.media.type.code),
                coverUri = "",
                authorId = state.selectedAccount.id,
                authorType = state.selectedAccount.type,
                imageSourceId = state.config.imageSourceId,
                videoSourceId = state.config.videoSourceId,
                applink = StoriesAppLinkBuilder.buildForShareLink(
                    state.config.storiesApplinkTemplate,
                    state.config.storiesId,
                )
            )

            creationUploader.upload(data)

            _uiEvent.emit(StoriesCreationUiEvent.StoriesUploadQueued)
        }) { throwable ->
            _uiEvent.emit(StoriesCreationUiEvent.ShowError(throwable))
        }
    }
}
