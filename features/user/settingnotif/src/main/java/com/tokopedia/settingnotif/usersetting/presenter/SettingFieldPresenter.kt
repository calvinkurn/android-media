package com.tokopedia.settingnotif.usersetting.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingnotif.usersetting.domain.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.domain.usecase.GetUserSettingUseCase
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import rx.Subscriber
import javax.inject.Inject

class SettingFieldPresenter @Inject constructor(
        val getUserSettingUseCase: GetUserSettingUseCase
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

}