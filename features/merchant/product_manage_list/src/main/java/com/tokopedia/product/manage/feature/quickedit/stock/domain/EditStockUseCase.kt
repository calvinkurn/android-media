package com.tokopedia.product.manage.feature.quickedit.stock.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.ProductEditStockParam
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class EditStockUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductUpdateV3Response>() {

    companion object {

        const val PARAM_EDIT_STOCK = "param_edit_stock"
        const val PARAM_INPUT = "input"

        @JvmStatic
        fun createRequestParams(shopId: String, productId: String, stock: Int, status: ProductStatus): RequestParams {
            val requestParams = RequestParams.create()
            val productEditStockParam = ProductEditStockParam()
            productEditStockParam.shop.shopId = shopId
            productEditStockParam.productId = productId
            productEditStockParam.stock = stock
            productEditStockParam.status = status.name
            requestParams.putObject(PARAM_EDIT_STOCK, productEditStockParam)
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
        variables[PARAM_INPUT] = params.getObject(PARAM_EDIT_STOCK)
        val gqlRequest = GraphqlRequest(query, ProductUpdateV3Response::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<ProductUpdateV3Response>(ProductUpdateV3Response::class.java)
        }
    }


}