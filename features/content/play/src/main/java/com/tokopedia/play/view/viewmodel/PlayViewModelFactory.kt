package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 24/11/22
 */
class PlayViewModelFactory @AssistedInject constructor(
    @Assisted private val channelId: String,
    @Assisted owner: SavedStateRegistryOwner,
    private val factory: PlayViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            owner: SavedStateRegistryOwner,
            channelId: String,
        ): PlayViewModelFactory
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return factory.create(channelId) as T
    }
}
