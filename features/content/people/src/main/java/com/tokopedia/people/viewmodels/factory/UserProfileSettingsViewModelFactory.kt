package com.tokopedia.people.viewmodels.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.people.viewmodels.UserProfileSettingsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
class UserProfileSettingsViewModelFactory @AssistedInject constructor(
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted private val userID: String,
    private val userProfileSettingsViewModelFactory: UserProfileSettingsViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            owner: SavedStateRegistryOwner,
            userID: String,
        ): UserProfileSettingsViewModelFactory
    }

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        return userProfileSettingsViewModelFactory.create(userID) as T
    }
}
