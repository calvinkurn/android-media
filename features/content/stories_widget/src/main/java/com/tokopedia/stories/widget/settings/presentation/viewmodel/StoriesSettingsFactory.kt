package com.tokopedia.stories.widget.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.stories.widget.settings.presentation.StoriesSettingsEntryPoint
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 4/18/24
 */
class StoriesSettingsFactory @AssistedInject constructor(
    @Assisted private val entryPoint: StoriesSettingsEntryPoint,
    private val factory: StoriesSettingsViewModel.Factory
) : ViewModelProvider.Factory {

    @AssistedFactory
    interface Creator {
        fun create(
            @Assisted source: StoriesSettingsEntryPoint
        ): StoriesSettingsFactory
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(entryPoint) as T
}
