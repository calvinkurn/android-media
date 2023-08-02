package com.tokopedia.stories.view.viewmodel.factory

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesViewModelFactory @AssistedInject constructor(
    @Assisted activity: FragmentActivity,
    private val storiesViewModel: StoriesViewModel.Factory,
) : AbstractSavedStateViewModelFactory(activity, null) {

    @AssistedFactory
    interface Creator {
        fun create(activity: FragmentActivity): StoriesViewModelFactory
    }

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return storiesViewModel.create(handle) as T
    }
}
