package com.tokopedia.stories.widget.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * @author by astidhiyaa on 3/22/24
 */
class StoriesSettingsViewModel @Inject constructor( //Todo(): parsing author Id & type
    private val repository: StoriesSettingsRepository
) : ViewModel() {


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
