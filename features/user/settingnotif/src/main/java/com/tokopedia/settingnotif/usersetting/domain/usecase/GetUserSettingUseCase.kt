package com.tokopedia.settingnotif.usersetting.domain.usecase

import com.tokopedia.settingnotif.usersetting.domain.pojo.UserSettingPojo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetUserSettingUseCase : UseCase<UserSettingPojo>(){

    override fun createObservable(p0: RequestParams?): Observable<UserSettingPojo> {
        return Observable.empty()
    }

}