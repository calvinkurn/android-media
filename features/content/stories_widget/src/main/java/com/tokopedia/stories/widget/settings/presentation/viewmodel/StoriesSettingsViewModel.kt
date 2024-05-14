package com.tokopedia.stories.widget.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stories.widget.settings.StoriesSettingsChecker
import com.tokopedia.stories.widget.settings.data.repository.StoriesSettingsRepository
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsPageUiModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by astidhiyaa on 3/22/24
 */
class StoriesSettingsViewModel @Inject constructor(
    private val repository: StoriesSettingsRepository,
    private val storiesChecker: StoriesSettingsChecker,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {

    private val _pageInfo = MutableStateFlow(StoriesSettingsPageUiModel.Empty)
    val pageInfo: Flow<StoriesSettingsPageUiModel>
        get() = _pageInfo

    private val _event = MutableSharedFlow<StoriesSettingEvent>()

    val uiEvent: Flow<StoriesSettingEvent>
        get() = _event

    fun onEvent(action: StoriesSettingsAction) {
        when (action) {
            StoriesSettingsAction.FetchPageInfo -> getList()
            is StoriesSettingsAction.SelectOption -> updateOption(action.option)
            is StoriesSettingsAction.Navigate -> viewModelScope.launch {
                _event.emit(StoriesSettingEvent.Navigate(action.appLink))
            }

            else -> {}
        }
    }

    private val onEligibleError = CoroutineExceptionHandler { _, _ ->
        _pageInfo.update { data ->
            data.copy(
                state = ResultState.Success,
                config = data.config.copy(isEligible = false)
            )
        }
    }

    private fun getList() {
        viewModelScope.launchCatchError(block = {
            val eligibleJob = viewModelScope.launch(onEligibleError) {
                val value = storiesChecker.isEligible()
                _pageInfo.update { data -> data.copy(config = data.config.copy(isEligible = value)) }
            }

            eligibleJob.join()

            if (eligibleJob.isCancelled) return@launchCatchError

            viewModelScope.launchCatchError(block = {
                val data = repository.getOptions()
                _pageInfo.update { result ->
                    result.copy(
                        state = ResultState.Success,
                        options = data.options,
                        config = result.config.copy(
                            articleWebLink = data.config.articleWebLink,
                            articleAppLink = data.config.articleAppLink,
                            articleCopy = data.config.articleCopy
                        ),
                    )
                }
            }) {
                _pageInfo.update { data -> data.copy(state = ResultState.Fail(it)) }
            }
        }) {}
    }

    private fun updateOption(option: StoriesSettingOpt) {
        viewModelScope.launchCatchError(block = {
            val response = repository.updateOption(option)
            if (response) {
                updatePageOption(option)
                _event.emit(StoriesSettingEvent.ClickTrack(option.copy(isSelected = !option.isSelected)))
            } else throw Exception()
        }) {
            updatePageOption(option)
            _event.emit(StoriesSettingEvent.ShowErrorToaster(it) {
                updateOption(option)
            })
        }
    }

    private fun updatePageOption(option: StoriesSettingOpt) {
        _pageInfo.update { page ->
            page.copy(options = page.options.map { opt ->
                if (option.optionType == opt.optionType) opt.copy(isSelected = !opt.isSelected) else opt
            })
        }
    }
}
