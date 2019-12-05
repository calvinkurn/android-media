package com.tokopedia.product.manage.list.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.list.constant.GQL_FEATURED_PRODUCT
import com.tokopedia.product.manage.list.data.model.featuredproductresponse.FeaturedProductResponseModel
import com.tokopedia.product.manage.list.view.mapper.FeaturedProductResponseMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class EditFeaturedProductUseCase @Inject constructor(
      private val graphqlUseCase: GraphqlUseCase,
      @Named(GQL_FEATURED_PRODUCT) private val gqlMutation: String,
      private val featuredProductResponseMapper: FeaturedProductResponseMapper
) : UseCase<Nothing>() {

    companion object {
        private const val PARAM_PRODUCT_ID = "productId"
        private const val PARAM_STATUS = "status"

        @JvmStatic
        fun createRequestParams(paramProductId: Int,
                                paramStatus: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_PRODUCT_ID, paramProductId)
            requestParams.putObject(PARAM_STATUS, paramStatus)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<Nothing> {
        var graphqlRequest = GraphqlRequest(gqlMutation, FeaturedProductResponseModel::class.java, requestParams?.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map(featuredProductResponseMapper)
    }
}