package com.tokopedia.settingnotif.usersetting.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.usecase.SetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.util.SingleLiveEvent
import com.tokopedia.settingnotif.usersetting.util.load
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.view.state.UserSettingErrorState
import com.tokopedia.track.TrackApp
import javax.inject.Inject
import com.tokopedia.settingnotif.usersetting.domain.usecase.SetUserSettingUseCase.Companion.params as settingParams

class UserSettingViewModel @Inject constructor(
        private val getUserSettingUseCase: GetUserSettingUseCase,
        private val setUserSettingUseCase: SetUserSettingUseCase
) : ViewModel(), SettingFieldContract.UserSetting {

    private val _userSetting = SingleLiveEvent<UserSettingDataView>()
    val userSetting: LiveData<UserSettingDataView> get() = _userSetting

    private val _setUserSetting = MutableLiveData<SetUserSettingResponse>()
    val setUserSetting: LiveData<SetUserSettingResponse> get() = _setUserSetting

    private val _errorState = MutableLiveData<UserSettingErrorState>()
    val errorErrorState: LiveData<UserSettingErrorState> get() = _errorState

    override fun requestUpdateUserSetting(
            notificationType: String,
            updatedSettingIds: List<Map<String, Any>>
    ) {
        val params = settingParams(notificationType, updatedSettingIds)
        setUserSettingUseCase.load(requestParams = params, onSuccess = { data ->
            if (data != null) {
                _setUserSetting.value = data
            }
        }, onError = {
            _errorState.postValue(UserSettingErrorState.SetSettingError)
        })
    }

    override fun loadUserSettings() {
        getUserSettingUseCase.load(onSuccess = { data ->
            if (data != null) {
                _userSetting.setValue(data)
            }
        }, onError = {
            _errorState.postValue(UserSettingErrorState.GetSettingError)
        })
    }

    override fun requestUpdateMoengageUserSetting(
            updatedSettingIds: List<Map<String, Any>>
    ) {
        for (setting in updatedSettingIds) {
            val name = setting[SettingViewHolder.PARAM_SETTING_KEY]
            val value = setting[SettingViewHolder.PARAM_SETTING_VALUE]

            if (name !is String || value !is Boolean) continue

            when (name) {
                SETTING_PUSH_NOTIFICATION_PROMO -> setMoengagePromoPreference(value)
                SETTING_EMAIL_BULLETIN -> setMoengageEmailPreference(value)
            }
        }
    }

    private fun setMoengageEmailPreference(checked: Boolean) {
        TrackApp.getInstance().moEngage.setNewsletterEmailPref(checked)
    }

    private fun setMoengagePromoPreference(checked: Boolean) {
        TrackApp.getInstance().moEngage.setPushPreference(checked)
    }

    companion object {
        private const val SETTING_EMAIL_BULLETIN = "bulletin_newsletter"
        private const val SETTING_PUSH_NOTIFICATION_PROMO = "promo"
    }

}