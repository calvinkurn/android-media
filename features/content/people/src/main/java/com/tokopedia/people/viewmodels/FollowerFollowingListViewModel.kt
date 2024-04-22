package com.tokopedia.people.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FollowerFollowingListUiState(
    val profileName: String,
    val totalFollowersFmt: String,
    val totalFollowingsFmt: String
) {
    companion object {
        val Empty
            get() = FollowerFollowingListUiState(
                profileName = "",
                totalFollowersFmt = "",
                totalFollowingsFmt = ""
            )
    }
}

sealed interface FollowerFollowingListAction {

    object FetchData : FollowerFollowingListAction
}

internal class FollowerFollowingListViewModel @AssistedInject constructor(
    @Assisted private val profileIdentifier: String,
    private val profileRepo: UserProfileRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(profileIdentifier: String): FollowerFollowingListViewModel
    }

    private val _profile = MutableStateFlow(ProfileUiModel.Empty)

    val uiState get() = _profile.map {
        FollowerFollowingListUiState(it.name, it.stats.totalFollowerFmt, it.stats.totalFollowingFmt)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FollowerFollowingListUiState.Empty
    )

    infix fun onAction(action: FollowerFollowingListAction) {
        when (action) {
            FollowerFollowingListAction.FetchData -> onInit()
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            runCatching {
                profileRepo.getProfile(profileIdentifier)
            }.onSuccess {
                _profile.value = it
            }
        }
    }
}
