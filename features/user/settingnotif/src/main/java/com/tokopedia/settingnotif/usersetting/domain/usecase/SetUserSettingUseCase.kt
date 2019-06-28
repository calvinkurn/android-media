package com.tokopedia.settingnotif.usersetting.domain.usecase

import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SetUserSettingUseCase : UseCase<SetUserSettingResponse>() {

    override fun createObservable(p0: RequestParams?): Observable<SetUserSettingResponse> {
        return Observable.empty()
    }

}