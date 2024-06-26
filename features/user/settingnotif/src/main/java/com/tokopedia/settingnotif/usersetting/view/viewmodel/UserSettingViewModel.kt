package com.tokopedia.settingnotif.usersetting.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.settingnotif.usersetting.data.mapper.UserSettingMapper
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.domain.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.SetUserSettingUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.settingnotif.usersetting.analytics.MoengageManager
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.view.state.UserSettingErrorState
import com.tokopedia.settingnotif.usersetting.view.state.UserSettingErrorState.GetSettingError
import com.tokopedia.settingnotif.usersetting.view.state.UserSettingErrorState.SetSettingError
import com.tokopedia.track.TrackApp
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.tokopedia.settingnotif.usersetting.domain.SetUserSettingUseCase.Companion.params as settingParams

class UserSettingViewModel @Inject constructor(
        private val getUserSettingUseCase: GetUserSettingUseCase,
        private val setUserSettingUseCase: SetUserSettingUseCase,
        private val moengageManager: MoengageManager,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io), SettingFieldContract.UserSetting {

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
        setUserSettingUseCase.params = settingParams(
                notificationType = notificationType,
                updatedSettingIds = updatedSettingIds
        )
        launchCatchError(block = {
            val result = setUserSettingUseCase.executeOnBackground()
            withContext(dispatcher.main) {
                _setUserSetting.value = result
            }
        }, onError = {
            _errorState.postValue(SetSettingError)
        })
    }

    override fun loadUserSettings() {
        launchCatchError(block = {
            val result = getUserSettingUseCase.executeOnBackground()
            withContext(dispatcher.main) {
                val mapToDataView = UserSettingMapper.map(result)
                _userSetting.setValue(mapToDataView)
            }
        }, onError = {
            _errorState.postValue(GetSettingError)
        })
    }

    override fun requestUpdateMoengageUserSetting(name: String, value: Boolean) {
        when (name) {
            SETTING_PUSH_NOTIFICATION_PROMO -> moengageManager.setPushPreference(value)
            SETTING_EMAIL_BULLETIN -> moengageManager.setNewsletterEmailPref(value)
        }
    }

    companion object {
        private const val SETTING_EMAIL_BULLETIN = "bulletin_newsletter"
        private const val SETTING_PUSH_NOTIFICATION_PROMO = "promo"
    }

}