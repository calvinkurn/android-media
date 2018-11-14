package com.tokopedia.kol.feature.report.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable

/**
 * @author by milhamj on 14/11/18.
 */
class SendReportUseCase: GraphqlUseCase() {
    override fun createObservable(params: RequestParams?): Observable<GraphqlResponse> {

        return super.createObservable(params)
    }
}