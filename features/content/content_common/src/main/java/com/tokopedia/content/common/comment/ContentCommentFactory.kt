package com.tokopedia.content.common.comment

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentFactory @AssistedInject constructor(
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted private val source: PageSource,
    private val factory: ContentCommentViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = factory.create(source) as T

    @AssistedFactory
    interface Creator {
        fun create(
            owner: SavedStateRegistryOwner,
            @Assisted source: PageSource,
        ): ContentCommentFactory
    }
}
