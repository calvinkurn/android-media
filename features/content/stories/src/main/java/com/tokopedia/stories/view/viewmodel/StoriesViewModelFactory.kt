package com.tokopedia.stories.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StoriesViewModelFactory @AssistedInject constructor(
    @Assisted private val authorId: String,
    private val factory: StoriesViewModel.Factory,
) : ViewModelProvider.Factory {

    @AssistedFactory
    interface Creator {
        fun create(
            @Assisted authorId: String,
        ): StoriesViewModelFactory
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = factory.create(authorId) as T
}
