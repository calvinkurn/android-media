package com.tokopedia.product.manage.feature.quickedit.delete.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductParam
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase) : UseCase<DeleteProductResponse>() {

    companion object {

        const val PARAM_DELETE_PRODUCT = "param_delete_product"
        const val PARAM_INPUT = "input"

        fun createParams(shopId: String, productId: String): RequestParams {
            val requestParams = RequestParams.create()
            val deleteProductParam = DeleteProductParam()
            deleteProductParam.productId = productId
            deleteProductParam.shop.shopId = shopId
            requestParams.putObject(PARAM_DELETE_PRODUCT, deleteProductParam)
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

    override suspend fun executeOnBackground(): DeleteProductResponse {
        val variables = HashMap<String, Any>()
        variables[PARAM_INPUT] = params.getObject(PARAM_DELETE_PRODUCT)
        val gqlRequest = GraphqlRequest(query, DeleteProductResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<DeleteProductResponse>(DeleteProductResponse::class.java)
        }
    }
}