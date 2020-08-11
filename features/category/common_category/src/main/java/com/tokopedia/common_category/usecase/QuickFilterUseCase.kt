package com.tokopedia.common_category.usecase

import com.tokopedia.common_category.data.raw.GQL_NAV_QUICK_FILTER
import com.tokopedia.common_category.model.filter.FilterResponse
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class QuickFilterUseCase @Inject constructor() : UseCase<List<Filter>>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<Filter>> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(GQL_NAV_QUICK_FILTER, FilterResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(FilterResponse::class.java) as FilterResponse).dynamicAttribute?.data?.filter
        }
    }
}