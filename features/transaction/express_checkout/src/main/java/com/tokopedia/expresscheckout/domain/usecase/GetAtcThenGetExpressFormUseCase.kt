package com.tokopedia.expresscheckout.domain.usecase

import com.tokopedia.expresscheckout.domain.entity.ExpressCheckoutFormData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class GetAtcThenGetExpressFormUseCase : UseCase<ExpressCheckoutFormData>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ExpressCheckoutFormData> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}