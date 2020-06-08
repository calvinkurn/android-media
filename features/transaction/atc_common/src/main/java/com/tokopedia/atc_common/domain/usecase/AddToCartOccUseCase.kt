package com.tokopedia.atc_common.domain.usecase

import com.tokopedia.atc_common.AtcConstant.MUTATION_ATC_OCC
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.response.AddToCartOccGqlResponse
import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class AddToCartOccUseCase @Inject constructor(@Named(MUTATION_ATC_OCC) private val queryString: String,
                                              private val graphqlUseCase: GraphqlUseCase,
                                              private val addToCartDataMapper: AddToCartDataMapper) : UseCase<AddToCartDataModel>() {

    companion object {
        const val REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST = "REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST"

        private const val PARAM = "param"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddToCartDataModel> {
        val addToCartRequest = requestParams?.getObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST) as AddToCartOccRequestParams
        val graphqlRequest = GraphqlRequest(queryString, AddToCartOccGqlResponse::class.java, getParams(addToCartRequest))
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val addToCartOccGqlResponse = it.getData<AddToCartOccGqlResponse>(AddToCartOccGqlResponse::class.java)
            addToCartDataMapper.mapAddToCartOccResponse(addToCartOccGqlResponse)
        }

    }

    private fun getParams(addToCartRequest: AddToCartOccRequestParams): Map<String, Any> {
        return mapOf(PARAM to addToCartRequest)
    }

}