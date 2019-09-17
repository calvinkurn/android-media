package com.tokopedia.settingnotif.usersetting.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.usecase.SetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import com.tokopedia.track.TrackApp
import rx.Subscriber
import javax.inject.Inject

class SettingFieldPresenter @Inject constructor(
        private val getUserSettingUseCase: GetUserSettingUseCase,
        private val setUserSettingUseCase: SetUserSettingUseCase
) : BaseDaggerPresenter<SettingFieldContract.View>(), SettingFieldContract.Presenter {

    private val SETTING_EMAIL_BULLETIN = "bulletin_newsletter"
    private val SETTING_PUSH_NOTIFICATION_PROMO = "promo"

    override fun loadUserSettings() {
        return getUserSettingUseCase.execute(object : Subscriber<UserSettingViewModel>() {
            override fun onNext(data: UserSettingViewModel?) {
                if (data == null) return
                view?.onSuccessGetUserSetting(data)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view?.onErrorGetUserSetting()
            }

        })
    }

    override fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>) {
        val params = setUserSettingUseCase.createParams(notificationType, updatedSettingIds)
        setUserSettingUseCase.execute(params, object : Subscriber<SetUserSettingResponse>() {
            override fun onNext(data: SetUserSettingResponse?) {
                if (data == null) return
                view?.onSuccessSetUserSetting(data)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view?.onErrorSetUserSetting()
            }

        })
    }

    override fun requestUpdateMoengageUserSetting(updatedSettingIds: List<Map<String, Any>>) {
        for (setting in updatedSettingIds) {
            val name = setting[SettingViewHolder.PARAM_SETTING_KEY]
            val value = setting[SettingViewHolder.PARAM_SETTING_VALUE]

            if (name !is String || value !is Boolean) continue

            when (name) {
                SETTING_PUSH_NOTIFICATION_PROMO -> setMoengagePushNotificationPromoPreference(value)
                SETTING_EMAIL_BULLETIN -> setMoengageEmailPreference(value)
            }
        }
    }

    private fun setMoengageEmailPreference(checked: Boolean) {
        TrackApp.getInstance().moEngage.setNewsletterEmailPref(checked)
    }

    private fun setMoengagePushNotificationPromoPreference(checked: Boolean) {
        TrackApp.getInstance().moEngage.setPushPreference(checked)
    }
}