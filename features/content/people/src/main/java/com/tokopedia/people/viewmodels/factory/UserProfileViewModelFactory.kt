package com.tokopedia.people.viewmodels.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.people.viewmodels.UserProfileViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
class UserProfileViewModelFactory @AssistedInject constructor(
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted private val username: String,
    private val userProfileViewModelFactory: UserProfileViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            owner: SavedStateRegistryOwner,
            username: String,
        ): UserProfileViewModelFactory
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        return userProfileViewModelFactory.create(username) as T
    }
}
