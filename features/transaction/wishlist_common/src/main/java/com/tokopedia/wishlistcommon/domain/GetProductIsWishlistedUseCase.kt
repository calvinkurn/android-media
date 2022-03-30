package com.tokopedia.wishlistcommon.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wishlistcommon.data.response.GetIsWishlistedV2Response

class GetProductIsWishlistedUseCase(private val rawQuery: String, private val gqlUseCase: GraphqlUseCase) : UseCase<Boolean>() {
    override fun createObservable(requestParams: RequestParams): rx.Observable<Boolean> {
        gqlUseCase.clearRequest()
        val gqlRequest = GraphqlRequest(rawQuery, GetIsWishlistedV2Response::class.java,
                requestParams.parameters)
        gqlUseCase.addRequest(gqlRequest)
        return gqlUseCase.createObservable(RequestParams.EMPTY)
                .map { graphqlResponse: GraphqlResponse ->
                    val errors = graphqlResponse.getError(GetIsWishlistedV2Response::class.java)
                    if (errors != null && errors.isNotEmpty()) {
                        return@map false
                    } else {
                        val resp: GetIsWishlistedV2Response = graphqlResponse.getData(GetIsWishlistedV2Response::class.java)
                        return@map resp.isWishlisted
                    }
                }
    }

    override fun unsubscribe() {
        gqlUseCase.unsubscribe()
        super.unsubscribe()
    }

    companion object {
        private const val ARG_PRODUCT_ID = "productID"
        fun createParams(productId: String?): RequestParams {
            val params = RequestParams.create()
            params.putString(ARG_PRODUCT_ID, productId)
            return params
        }
    }
}
