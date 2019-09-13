package com.tokopedia.home.account.domain

import com.tokopedia.home.account.presentation.viewmodel.base.PushNotifCheckerViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class GetPushNotifCheckerStatusUseCase: UseCase<PushNotifCheckerViewModel>() {

    override fun createObservable(p0: RequestParams?): Observable<PushNotifCheckerViewModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}