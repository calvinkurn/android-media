package com.tokopedia.play.broadcaster.view.viewmodel.factory

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by kenny.hadisaputra on 24/02/22
 */
class PlayBroadcastViewModelFactory @AssistedInject constructor(
    @Assisted activity: FragmentActivity,
    private val playBroViewModelFactory: PlayBroadcastViewModel.Factory,
) : AbstractSavedStateViewModelFactory(activity, null) {

    @AssistedFactory
    interface Creator {
        fun create(activity: FragmentActivity): PlayBroadcastViewModelFactory
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return playBroViewModelFactory.create(handle) as T
    }
}