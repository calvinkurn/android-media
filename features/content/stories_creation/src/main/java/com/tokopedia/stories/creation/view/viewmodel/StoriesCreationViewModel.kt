package com.tokopedia.stories.creation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.state.StoriesCreationUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
class StoriesCreationViewModel @Inject constructor(
    private val repo: StoriesCreationRepository,
    private val creationUploader: CreationUploader,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoriesCreationUiState.Empty)
    val uiState: StateFlow<StoriesCreationUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<StoriesCreationUiEvent>()
    val uiEvent: Flow<StoriesCreationUiEvent> = _uiEvent

    val maxStoriesConfig: StoriesCreationConfiguration.MaxStoriesConfig
        get() = _uiState.value.config.maxStoriesConfig

    private val storyId: String
        get() = _uiState.value.config.storiesId

    private val selectedAccount: ContentAccountUiModel
        get() = _uiState.value.selectedAccount

    private val productTag: List<String>
        get() = _uiState.value.productTags

    fun submitAction(action: StoriesCreationAction) {
        when (action) {
            is StoriesCreationAction.Prepare -> handlePrepare()
            is StoriesCreationAction.SetMedia -> handleSetMedia(action.mediaFilePath, action.mediaType)
            is StoriesCreationAction.ClickAddProduct -> handleClickAddProduct(action.productTags)
            is StoriesCreationAction.ClickUpload -> handleClickUpload()
        }
    }

    private fun handlePrepare() {
        viewModelScope.launchCatchError(block = {
            val accountList = repo.getCreationAccountList()

            /**
             * Stories is only available for shop
             * need FE development to support UGC account
             * */
            val selectedAccount = accountList.firstOrNull { it.isShop && it.enable }

            if (selectedAccount != null) {
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

                if (config.maxStoriesConfig.isLimitReached) {
                    _uiEvent.emit(StoriesCreationUiEvent.ShowTooManyStoriesReminder)
                } else {
                    _uiEvent.emit(StoriesCreationUiEvent.OpenMediaPicker)
                }
            }
            else {
                /** TODO JOE: handle this */
            }
        }) { throwable ->
            _uiEvent.emit(
                StoriesCreationUiEvent.ErrorPreparePage(throwable)
            )
        }
    }

    private fun handleSetMedia(
        mediaFilePath: String,
        mediaType: ContentMediaType,
    ) {
        _uiState.update {
            it.copy(
                mediaFilePath = mediaFilePath,
                mediaType = mediaType,
            )
        }
    }

    private fun handleClickAddProduct(
        productTags: List<String>,
    ) {
        viewModelScope.launchCatchError(block = {
            repo.setActiveProductTag(
                storyId = storyId,
                productIds = productTags.map { it }
            )

            _uiState.update {
                it.copy(
                    productTags = productTags
                )
            }
        }) {
            /** TODO JOE: handle this */
        }
    }

    private fun handleClickUpload() {
        viewModelScope.launch {
            val state = _uiState.value

            val data = CreationUploadData.buildForStories(
                creationId = state.config.storiesId,
                mediaUriList = listOf(state.mediaFilePath),
                mediaTypeList = listOf(state.mediaType.code),
                coverUri = "",
                authorId = state.selectedAccount.id,
                authorType = state.selectedAccount.type,
                imageSourceId = state.config.imageSourceId,
                videoSourceId = state.config.videoSourceId,
            )

            creationUploader.upload(data)
        }
    }
}
