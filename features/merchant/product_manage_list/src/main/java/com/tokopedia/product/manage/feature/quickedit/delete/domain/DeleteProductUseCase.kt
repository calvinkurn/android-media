package com.tokopedia.product.manage.feature.quickedit.delete.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductParam
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(repository: GraphqlRepository)
    : GraphqlUseCase<ProductUpdateV3Response>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
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

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductUpdateV3Response::class.java)
    }

    fun setParams(shopId: String, productId: String) {
        val requestParams = RequestParams.create()
        val deleteProductParam = DeleteProductParam()
        deleteProductParam.productId = productId
        deleteProductParam.shop.shopId = shopId
        requestParams.putObject(PARAM_INPUT, deleteProductParam)
        setRequestParams(requestParams.parameters)
    }
}