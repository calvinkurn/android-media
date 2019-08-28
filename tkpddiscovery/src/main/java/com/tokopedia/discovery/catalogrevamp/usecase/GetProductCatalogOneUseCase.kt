package com.tokopedia.discovery.catalogrevamp.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.model.ProductCatalogResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetProductCatalogOneUseCase
@Inject constructor(private val context: Context,
                    private val graphqlUseCase: GraphqlUseCase)
    : UseCase<ProductCatalogResponse>() {

    private val KEY_CATALOG_ID = "catalog_id"

    fun createRequestParams(catalogId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(KEY_CATALOG_ID, catalogId)
        return requestParams
    }


    override fun createObservable(requestParams: RequestParams?): Observable<ProductCatalogResponse> {

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_product_catalog_query), ProductCatalogResponse::class.java, requestParams!!.parameters, false)
        graphqlUseCase.clearRequest()

        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            (it.getData(ProductCatalogResponse::class.java)) as ProductCatalogResponse
        }

    }

}