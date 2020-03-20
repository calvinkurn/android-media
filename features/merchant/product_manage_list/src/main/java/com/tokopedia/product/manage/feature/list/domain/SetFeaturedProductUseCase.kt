package com.tokopedia.product.manage.feature.list.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.oldlist.constant.GQL_FEATURED_PRODUCT
import com.tokopedia.product.manage.oldlist.data.model.featuredproductresponse.FeaturedProductResponseModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class SetFeaturedProductUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQL_FEATURED_PRODUCT) private val gqlMutation: String
) : UseCase<FeaturedProductResponseModel>() {

    companion object {
        private const val PARAM_PRODUCT_ID = "productId"
        private const val PARAM_STATUS = "status"

        @JvmStatic
        fun createRequestParams(paramProductId: Int, paramStatus: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_PRODUCT_ID, paramProductId)
            requestParams.putObject(PARAM_STATUS, paramStatus)
            return requestParams
        }
    }

    var params: RequestParams? = RequestParams.EMPTY

    override suspend fun executeOnBackground(): FeaturedProductResponseModel {
        val variables = HashMap<String, Any>()
        params?.let {
            variables[PARAM_PRODUCT_ID] = it.getObject(PARAM_PRODUCT_ID)
            variables[PARAM_STATUS] = it.getObject(PARAM_STATUS)
        }
        val graphqlRequest = GraphqlRequest(gqlMutation, FeaturedProductResponseModel::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData(FeaturedProductResponseModel::class.java)
        }
    }
}