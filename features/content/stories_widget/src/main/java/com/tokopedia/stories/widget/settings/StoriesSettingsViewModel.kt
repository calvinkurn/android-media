package com.tokopedia.stories.widget.settings

import android.util.Log
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

    fun getList() {
        viewModelScope.launchCatchError(block = {
            val data = repository.getOptions(entryPoint = entryPoint)
            _pageInfo.update { data }
        }) {}
    }

    fun updateOption(option: StoriesSettingOpt) {
        viewModelScope.launchCatchError(block = {
            val response = repository.updateOption(entryPoint = entryPoint, option)

        }) {}
    }

    fun check() {
        viewModelScope.launchCatchError(block = {
            val response = repository.checkEligibility(entryPoint = entryPoint)
        }) {}
    }
}
