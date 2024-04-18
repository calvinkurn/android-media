package com.tokopedia.stories.widget.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * @author by astidhiyaa on 3/22/24
 */
class StoriesSettingsViewModel @AssistedInject constructor(
    @Assisted entryPoint: StoriesSettingsEntryPoint,
    private val repository: StoriesSettingsRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted entryPoint: StoriesSettingsEntryPoint): StoriesSettingsViewModel
    }

    private val _options = MutableStateFlow<List<StoriesSettingOpt>>(emptyList<StoriesSettingOpt>())
    val options: Flow<List<StoriesSettingOpt>>
        get() = _options

    fun getList() {
        viewModelScope.launchCatchError(block = {
            val data = repository.getOptions("", "")
            _options.update { data }
        }) {}
    }

    fun updateOption(option: StoriesSettingOpt) {
        viewModelScope.launchCatchError(block = {
            repository.updateOption("", "", option)
        }) {}
    }
}
