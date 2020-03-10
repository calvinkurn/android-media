package com.tokopedia.product.manage.feature.quickedit.price.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.price.data.model.ProductEditPriceParam
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class EditPriceUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<ProductUpdateV3Response>() {

    companion object {

        const val PARAM_EDIT_PRICE = "param_edit_price"
        const val PARAM_INPUT = "input"

        @JvmStatic
        fun createRequestParams(shopId: String, productId: String, price: Float): RequestParams {
            val requestParams = RequestParams.create()
            val productEditPriceParam = ProductEditPriceParam()
            productEditPriceParam.shop.shopId = shopId
            productEditPriceParam.productId = productId
            productEditPriceParam.price = price
            requestParams.putObject(PARAM_EDIT_PRICE, productEditPriceParam)
            return requestParams
        }

        private val query = """
            mutation productUpdateV3(${'$'}input: ProductInputV3!){
                ProductUpdateV3(input:${'$'}input) {
                    header {
                        messages
                        reason
                        errorCode
                    }
                    isSuccess
                }
            }
        """.trimIndent()
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductUpdateV3Response {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = params.getObject(PARAM_EDIT_PRICE)
        val gqlRequest = GraphqlRequest(query, ProductUpdateV3Response::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<ProductUpdateV3Response>(ProductUpdateV3Response::class.java)
        }
    }

}