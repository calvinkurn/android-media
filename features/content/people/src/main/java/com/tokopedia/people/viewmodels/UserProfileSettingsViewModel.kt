package com.tokopedia.people.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileSettingsAction
import com.tokopedia.people.views.uimodel.event.UserProfileSettingsEvent
import com.tokopedia.people.views.uimodel.getReviewSettings
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
class UserProfileSettingsViewModel @AssistedInject constructor(
    @Assisted val userID: String,
    private val repo: UserProfileRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(userID: String): UserProfileSettingsViewModel
    }

    val isAnySettingsChanged: Boolean
        get() = _oldReviewSettings.value != _reviewSettings.value

    private val _uiEvent = MutableSharedFlow<UserProfileSettingsEvent>()
    val uiEvent: Flow<UserProfileSettingsEvent>
        get() = _uiEvent

    private val _oldReviewSettings = MutableStateFlow(ProfileSettingsUiModel.Empty)
    private val _reviewSettings = MutableStateFlow(ProfileSettingsUiModel.Empty)
    val reviewSettings: StateFlow<ProfileSettingsUiModel>
        get() = _reviewSettings

    init {
        submitAction(UserProfileSettingsAction.GetProfileSettings)
    }

    fun submitAction(action: UserProfileSettingsAction) {
        when (action) {
            is UserProfileSettingsAction.GetProfileSettings -> handleGetProfileSettings()
            is UserProfileSettingsAction.SetShowReview -> handleSetShowReview(action.isShow)
        }
    }

    private fun handleGetProfileSettings() {
        viewModelScope.launchCatchError(block = {
            val reviewSettings = repo.getProfileSettings(userID).getReviewSettings()

            _oldReviewSettings.update { reviewSettings }
            _reviewSettings.update { reviewSettings }
        }) {

        }
    }

    private fun handleSetShowReview(isShow: Boolean) {
        viewModelScope.launchCatchError(block = {
            val isSuccess = repo.setShowReview(
                userID = userID,
                settingID = _reviewSettings.value.settingID,
                isShow = isShow
            )

            if (isSuccess) {
                _reviewSettings.update { it.copy(isEnabled = isShow) }
            } else {
                throw Exception()
            }
        }) {
            _uiEvent.emit(UserProfileSettingsEvent.ErrorSetShowReview(it))
        }
    }
}
