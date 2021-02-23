package com.tokopedia.catalog.usecase.listing

import com.tokopedia.catalog.model.raw.gql.CATALOG_GQL_DYNAMIC_FILTER
import com.tokopedia.common_category.model.filter.FilterResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject


class CatalogDynamicFilterUseCase @Inject constructor() : UseCase<DynamicFilterModel>() {
    override fun createObservable(requestParams: RequestParams?): Observable<DynamicFilterModel> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(CATALOG_GQL_DYNAMIC_FILTER, FilterResponse::class.java, requestParams?.parameters, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(FilterResponse::class.java) as FilterResponse).dynamicAttribute
        }

    }
}