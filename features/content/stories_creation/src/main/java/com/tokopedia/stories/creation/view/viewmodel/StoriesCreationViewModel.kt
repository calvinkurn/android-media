package com.tokopedia.stories.creation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.StoriesMediaType
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.exception.AccountNotEligibleException
import com.tokopedia.stories.creation.view.model.state.StoriesCreationUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
class StoriesCreationViewModel @Inject constructor(
    private val repo: StoriesCreationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoriesCreationUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<StoriesCreationUiEvent>()
    val uiEvent: Flow<StoriesCreationUiEvent> = _uiEvent

    val maxStoriesConfig: StoriesCreationConfiguration.MaxStoriesConfig
        get() = _uiState.value.config.maxStoriesConfig

    val minVideoDuration: Int
        get() = _uiState.value.config.minVideoDuration

    val maxVideoDuration: Int
        get() = _uiState.value.config.maxVideoDuration

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
                ?: throw AccountNotEligibleException()

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
        }) { throwable ->
            _uiEvent.emit(
                StoriesCreationUiEvent.ErrorPreparePage(throwable)
            )
        }
    }

    private fun handleSetMedia(
        mediaFilePath: String,
        mediaType: StoriesMediaType,
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
        /** TODO JOE: handle this */
    }
}
