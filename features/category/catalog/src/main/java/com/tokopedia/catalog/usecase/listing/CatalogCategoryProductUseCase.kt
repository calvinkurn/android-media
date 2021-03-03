package com.tokopedia.catalog.usecase.listing

import com.tokopedia.catalog.model.raw.CatalogSearchProductResponse
import com.tokopedia.catalog.model.raw.gql.CATALOG_GQL_SEARCH_PRODUCT
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class CatalogCategoryProductUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<CatalogSearchProductResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<CatalogSearchProductResponse> {
        val graphqlRequest = GraphqlRequest(CATALOG_GQL_SEARCH_PRODUCT, CatalogSearchProductResponse::class.java,requestParams?.parameters,false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            it.getData(CatalogSearchProductResponse::class.java) as CatalogSearchProductResponse
        }
    }
}
