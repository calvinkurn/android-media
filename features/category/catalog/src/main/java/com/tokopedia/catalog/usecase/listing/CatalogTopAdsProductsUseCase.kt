package com.tokopedia.catalog.usecase.listing

import com.tokopedia.catalog.model.raw.gql.CATALOG_GQL_TOP_ADS
import com.tokopedia.common_category.model.topAds.TopAdsResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CatalogTopAdsProductsUseCase @Inject constructor() : UseCase<TopAdsResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<TopAdsResponse> {

        val graphqlUseCase = GraphqlUseCase()
        val graphqlRequest = GraphqlRequest(CATALOG_GQL_TOP_ADS, TopAdsResponse::class.java,requestParams?.parameters,false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(TopAdsResponse::class.java) as TopAdsResponse)
        }

    }
}