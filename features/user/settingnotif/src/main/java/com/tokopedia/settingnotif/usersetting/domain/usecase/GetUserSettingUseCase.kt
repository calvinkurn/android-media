package com.tokopedia.settingnotif.usersetting.domain.usecase

import com.tokopedia.settingnotif.usersetting.domain.mapper.UserSettingFieldMapper
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingHelper
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetUserSettingUseCase : UseCase<UserSettingViewModel>() {

    override fun createObservable(p0: RequestParams?): Observable<UserSettingViewModel> {
        return Observable.just(SettingHelper.createDummyResponse()).map(UserSettingFieldMapper())
    }

}