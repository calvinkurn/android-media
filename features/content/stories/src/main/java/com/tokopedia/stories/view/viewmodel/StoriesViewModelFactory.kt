package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.stories.view.model.StoriesArgsModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 16/08/23
 */
class StoriesViewModelFactory @AssistedInject constructor(
    @Assisted private val args: StoriesArgsModel,
    private val factory: StoriesViewModel.Factory,
) : ViewModelProvider.Factory {

    @AssistedFactory
    interface Creator {
        fun create(@Assisted args: StoriesArgsModel): StoriesViewModelFactory
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = factory.create(args) as T
}
