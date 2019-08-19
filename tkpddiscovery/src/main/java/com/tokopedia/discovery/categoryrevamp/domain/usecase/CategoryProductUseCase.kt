package com.tokopedia.discovery.categoryrevamp.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants.Companion.KEY_PARAMS
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.categoryrevamp.utils.ParamMapToUrl
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.HashMap


import javax.inject.Inject

class CategoryProductUseCase @Inject constructor(private val context: Context,
                                                 private val graphqlUseCase: GraphqlUseCase) : UseCase<ProductListResponse>() {

    override fun createObservable(requestParams: RequestParams?): Observable<ProductListResponse> {
        val variables = createParametersForQuery(requestParams!!.parameters)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.gql_nav_search_product), ProductListResponse::class.java)
        graphqlRequest.variables = variables
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            it.getData(ProductListResponse::class.java) as ProductListResponse
        }
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): Map<String, Any> {
        val variables = HashMap<String, Any>()
        variables[KEY_PARAMS] = ParamMapToUrl.generateUrlParamString(parameters)
        return variables
    }

}
