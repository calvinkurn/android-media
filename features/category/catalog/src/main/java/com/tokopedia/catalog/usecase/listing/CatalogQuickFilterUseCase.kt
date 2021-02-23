package com.tokopedia.catalog.usecase.listing

import com.tokopedia.catalog.model.raw.gql.CATALOG_GQL_QUICK_FILTER
import com.tokopedia.catalog.model.raw.SearchFilterResponse
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CatalogQuickFilterUseCase @Inject constructor() : UseCase<List<Filter>>() {
    override fun createObservable(requestParams: RequestParams?): Observable<List<Filter>> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(CATALOG_GQL_QUICK_FILTER, SearchFilterResponse::class.java, requestParams?.parameters, false)
        val filterParams = RequestParams.create()
        filterParams.putString("params", requestParams?.getString("params", ""))

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(filterParams).map {
            (it.getData(SearchFilterResponse::class.java) as SearchFilterResponse).dynamicAttribute?.data?.filter
        }
    }
}