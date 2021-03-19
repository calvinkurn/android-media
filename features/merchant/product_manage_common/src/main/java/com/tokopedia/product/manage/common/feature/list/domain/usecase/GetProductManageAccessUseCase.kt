package com.tokopedia.product.manage.common.feature.list.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccessResponse.*
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/***
 * See GQL documentation for more information about ProductListMeta.
 * Docs: https://tokopedia.atlassian.net/wiki/spaces/MC/pages/669877876/GQL+ProductListMeta
 */
class GetProductManageAccessUseCase @Inject constructor(
    repository: GraphqlRepository
) : GraphqlUseCase<ProductManageAccessResponse>(repository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_EXTRA_INFO = "extraInfo"
        private const val EXTRA_INFO_ACCESS = "access"
        private const val EXTRA_INFO_RBAC = "rbac"

        private val QUERY = """
            query ProductListMeta(${'$'}shopID:String!, ${'$'}extraInfo:[String]){
                ProductListMeta(shopID:${'$'}shopID, extraInfo:${'$'}extraInfo){
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
        val extraInfo = listOf(EXTRA_INFO_ACCESS, EXTRA_INFO_RBAC)
        requestParams.putString(PARAM_SHOP_ID, shopId)
        requestParams.putObject(PARAM_EXTRA_INFO, extraInfo)
        setRequestParams(requestParams.parameters)
        return executeOnBackground().response
    }
}