package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 16/08/23
 */
class StoriesViewModelFactory @AssistedInject constructor(
    @Assisted private val groupId: String,
    private val factory: StoriesViewModel.Factory,
) : ViewModelProvider.Factory {

    @AssistedFactory
    interface Creator {
        fun create(
            @Assisted groupId: String,
        ): StoriesViewModelFactory
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(groupId) as T
}
