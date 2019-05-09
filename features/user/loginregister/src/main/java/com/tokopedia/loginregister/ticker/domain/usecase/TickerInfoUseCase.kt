package com.tokopedia.loginregister.ticker.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.ticker.domain.pojo.TickerInfoData
import com.tokopedia.loginregister.ticker.domain.pojo.TickerInfoPojo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by ade on 8/5/2019
 */
class TickerInfoUseCase @Inject constructor(
        val resources: Resources,
        val graphqlUseCase: GraphqlUseCase
): UseCase<List<TickerInfoPojo>>(){

    override fun createObservable(requestParams: RequestParams?): Observable<List<TickerInfoPojo>> {
        val graphqlRequest = GraphqlRequest(
            GraphqlHelper.loadRawString(resources, R.raw.query_ticker_login_register),
            TickerInfoData::class.java,
            false
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            return@map it.getData<TickerInfoData>(TickerInfoData::class.java).tickersInfoPojo.listTickerInfoPojo
        }
    }

}