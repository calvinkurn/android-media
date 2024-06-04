package com.tokopedia.feedplus.browse.presentation.factory

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.feedplus.browse.presentation.FeedSearchResultViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by Jonathan Darwin on 25 March 2024
 */
internal class FeedSearchResultViewModelFactory @AssistedInject constructor(
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted private val searchKeyword: String,
    private val feedSearchResultViewModelFactory: FeedSearchResultViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            owner: SavedStateRegistryOwner,
            searchKeyword: String,
        ): FeedSearchResultViewModelFactory
    }

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return feedSearchResultViewModelFactory.create(searchKeyword) as T
    }
}
