package com.tokopedia.product.manage.common.feature.list.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductManageAccessUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<ProductManageAccessResponse>(repository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val shopID = "\$shopID"
        private const val extraInfo = "access"

        private val QUERY = """
            query ProductListMeta($shopID:String!){
                ProductListMeta(shopID:$shopID, extraInfo:["$extraInfo"]){
                    header{
                        processTime
                        messages
                        reason
                        errorCode
                    }
                    data{
                      access{
                        id
                      }
                    }
                  }
                }
        """.trimIndent()
    }

    init {
        setGraphqlQuery(QUERY)
        setTypeClass(ProductManageAccessResponse::class.java)
    }

    suspend fun execute(shopId: String): Response {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_SHOP_ID, shopId)
        setRequestParams(requestParams.parameters)
        return executeOnBackground().response
    }
}