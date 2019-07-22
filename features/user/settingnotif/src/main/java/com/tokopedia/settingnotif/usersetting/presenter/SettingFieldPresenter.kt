package com.tokopedia.settingnotif.usersetting.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.domain.usecase.SetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import rx.Subscriber
import javax.inject.Inject

class SettingFieldPresenter @Inject constructor(
        private val getUserSettingUseCase: GetUserSettingUseCase,
        private val setUserSettingUseCase: SetUserSettingUseCase
) : BaseDaggerPresenter<SettingFieldContract.View>(), SettingFieldContract.Presenter {

    override fun loadUserSettings() {
        return getUserSettingUseCase.execute(object : Subscriber<UserSettingViewModel>() {
            override fun onNext(data: UserSettingViewModel?) {
                if (data == null) return
                view?.onSuccessGetUserSetting(data)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

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
}