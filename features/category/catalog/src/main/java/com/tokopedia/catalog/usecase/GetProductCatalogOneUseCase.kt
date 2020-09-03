package com.tokopedia.catalog.usecase

import com.tokopedia.catalog.model.ProductCatalogResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import raw.GQL_PRODUCT_CATALOG_QUERY
import rx.Observable
import javax.inject.Inject

class GetProductCatalogOneUseCase
@Inject constructor(private val graphqlUseCase: GraphqlUseCase)
    : UseCase<ProductCatalogResponse>() {

    private val KEY_CATALOG_ID = "catalog_id"

    fun createRequestParams(catalogId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(KEY_CATALOG_ID, catalogId)
        return requestParams
    }


    override fun createObservable(requestParams: RequestParams?): Observable<ProductCatalogResponse> {

        val graphqlRequest = GraphqlRequest(GQL_PRODUCT_CATALOG_QUERY, ProductCatalogResponse::class.java, requestParams!!.parameters, false)
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(ProductCatalogResponse::class.java)) as ProductCatalogResponse
        }

    }

}