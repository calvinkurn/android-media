package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 16/08/23
 */
class StoriesViewModelFactory @AssistedInject constructor(
    @Assisted activity: FragmentActivity,
    @Assisted private val authorId: String,
    private val factory: StoriesViewModel.Factory,
) : AbstractSavedStateViewModelFactory(activity, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            @Assisted activity: FragmentActivity,
            @Assisted authorId: String,
        ): StoriesViewModelFactory
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return factory.create(authorId, handle) as T
    }
}
