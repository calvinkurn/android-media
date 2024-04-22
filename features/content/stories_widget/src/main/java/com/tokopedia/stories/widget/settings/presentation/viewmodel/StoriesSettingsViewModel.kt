package com.tokopedia.stories.widget.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.stories.widget.settings.data.repository.StoriesSettingsRepository
import com.tokopedia.stories.widget.settings.presentation.StoriesSettingsEntryPoint
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingOpt
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsPageUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author by astidhiyaa on 3/22/24
 */
class StoriesSettingsViewModel @AssistedInject constructor(
    @Assisted private val entryPoint: StoriesSettingsEntryPoint,
    private val repository: StoriesSettingsRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted entryPoint: StoriesSettingsEntryPoint): StoriesSettingsViewModel
    }

    private val _pageInfo = MutableStateFlow(StoriesSettingsPageUiModel.Empty)
    val pageInfo: Flow<StoriesSettingsPageUiModel>
        get() = _pageInfo

    private val _event = MutableSharedFlow<StoriesSettingEvent>()

    val uiEvent: Flow<StoriesSettingEvent>
        get() = _event

    fun onEvent(action: StoriesSettingsAction) {
        when (action) {
            StoriesSettingsAction.FetchPageInfo-> getList()
            is StoriesSettingsAction.SelectOption -> updateOption(action.option)
            else -> {}
        }
    }

    private fun getList() {
        viewModelScope.launchCatchError(block = {
            val data = repository.getOptions(entryPoint = entryPoint)
            _pageInfo.update { data }
        }) {}
    }

    private fun updateOption(option: StoriesSettingOpt) {
        viewModelScope.launchCatchError(block = {
            val response = repository.updateOption(entryPoint = entryPoint, option)
            if (response) {
                _pageInfo.update { page ->
                    page.copy(options = page.options.map { opt ->
                        if (option == opt) opt.copy(isSelected = !opt.isSelected) else opt
                    })
                }
                _event.emit(StoriesSettingEvent.ClickTrack(option)) //need to update latest
            } else throw Exception()
        }) {
            _event.emit(StoriesSettingEvent.ShowErrorToaster(it) {
                updateOption(option)
            })
        }
    }
}
