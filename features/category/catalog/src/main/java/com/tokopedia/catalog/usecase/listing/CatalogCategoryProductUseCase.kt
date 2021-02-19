package com.tokopedia.catalog.usecase.listing

import com.tokopedia.catalog.model.raw.AceSearchProductResponse
import com.tokopedia.catalog.model.raw.CATALOG_GQL_SEARCH_PRODUCT
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CatalogCategoryProductUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<AceSearchProductResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<AceSearchProductResponse> {
        val graphqlRequest = GraphqlRequest(CATALOG_GQL_SEARCH_PRODUCT, AceSearchProductResponse::class.java,requestParams?.parameters,false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            it.getData(AceSearchProductResponse::class.java) as AceSearchProductResponse
        }
    }
}
