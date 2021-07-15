package com.tokopedia.loginregister.login.behaviour.data

import android.content.res.Resources
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.loginregister.common.view.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.loginregister.common.view.ticker.domain.usecase.TickerInfoUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable

class TickerInfoUseCaseStub(
         resources: Resources,
         graphqlUseCase: GraphqlUseCase
): TickerInfoUseCase(resources, graphqlUseCase) {

    var response = listOf<TickerInfoPojo>()

    override fun createObservable(requestParams: RequestParams): Observable<List<TickerInfoPojo>> {
        return Observable.just(response)
    }
}