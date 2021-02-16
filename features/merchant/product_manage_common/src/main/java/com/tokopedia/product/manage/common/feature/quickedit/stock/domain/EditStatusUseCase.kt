package com.tokopedia.product.manage.common.feature.quickedit.stock.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.quickedit.common.data.model.ProductUpdateV3Response
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.ProductEditStockParam
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class EditStatusUseCase @Inject constructor(repository: GraphqlRepository) : GraphqlUseCase<ProductUpdateV3Response>(repository) {

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

    fun setParams(shopId: String, productId: String, status: ProductStatus) {
        val requestParams = RequestParams.create()
        val productEditStockParam = ProductEditStockParam()
        productEditStockParam.shop.shopId = shopId
        productEditStockParam.productId = productId
        productEditStockParam.status = status.name
        requestParams.putObject(PARAM_INPUT, productEditStockParam)
        setRequestParams(requestParams.parameters)
    }

}