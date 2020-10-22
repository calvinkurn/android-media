package com.tokopedia.common_category.usecase

import com.tokopedia.common_category.data.raw.GQL_NAV_SEARCH_PRODUCT
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

import javax.inject.Inject

class CategoryProductUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) : UseCase<ProductListResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ProductListResponse> {
        val graphqlRequest = GraphqlRequest(GQL_NAV_SEARCH_PRODUCT, ProductListResponse::class.java,requestParams?.parameters,false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            it.getData(ProductListResponse::class.java) as ProductListResponse
        }
    }
}
