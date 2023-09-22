package com.tokopedia.stories.view.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tokopedia.stories.view.model.StoriesArgsModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesViewModelFactory @AssistedInject constructor(
    @Assisted activity: FragmentActivity,
    @Assisted private val args: StoriesArgsModel,
    private val factory: StoriesViewModel.Factory,
) : AbstractSavedStateViewModelFactory(activity, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            @Assisted activity: FragmentActivity,
            @Assisted args: StoriesArgsModel,
        ): StoriesViewModelFactory
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return factory.create(args, handle) as T
    }
}
