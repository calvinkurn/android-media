package com.tokopedia.settingnotif.usersetting.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.usecase.SetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.util.load
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.track.TrackApp
import javax.inject.Inject
import com.tokopedia.settingnotif.usersetting.domain.usecase.SetUserSettingUseCase.Companion.params as settingParams

class SettingFieldPresenter @Inject constructor(
        private val getUserSettingUseCase: GetUserSettingUseCase,
        private val setUserSettingUseCase: SetUserSettingUseCase
) : BaseDaggerPresenter<SettingFieldContract.View>(), SettingFieldContract.Presenter {

    override fun loadUserSettings() {
        return getUserSettingUseCase.load(onSuccess = { data ->
            if (data != null) {
                view?.onSuccessGetUserSetting(data)
            }
        }, onError = ::onUserSettingError)
    }

    override fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>) {
        val params = settingParams(notificationType, updatedSettingIds)
        setUserSettingUseCase.load(requestParams = params, onSuccess = { data ->
            if (data != null) {
                view?.onSuccessSetUserSetting(data)
            }
        }, onError = ::onUserSettingError)
    }

    override fun requestUpdateMoengageUserSetting(updatedSettingIds: List<Map<String, Any>>) {
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

    private fun onUserSettingError() {
        view?.onErrorSetUserSetting()
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