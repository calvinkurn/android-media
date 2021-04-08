package com.tokopedia.product.manage.common.feature.list.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/***
 * See GQL documentation for more information about ProductListMeta.
 * Docs: https://tokopedia.atlassian.net/wiki/spaces/MC/pages/669877876/GQL+ProductListMeta
 */
class GetProductListMetaUseCase @Inject constructor(
        repository: GraphqlRepository) : GraphqlUseCase<ProductListMetaResponse>(repository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_EXTRA_INFO = "extraInfo"
        private const val EXTRA_INFO_RBAC = "rbac"

        private val query by lazy {
            """
            query ProductListMeta(${"$"}shopID:String!, ${"$"}extraInfo:[String]){
                ProductListMeta(shopID:${"$"}shopID, extraInfo:${"$"}extraInfo){
                    header{
                        processTime
                        messages
                        reason
                      errorCode
                    }
                    data{
                      tab{
                        id
                        name
                        value
                      }
                      filter{
                        id
                        name
                        value
                      }
                      sort{
                        id
                        name
                        value
                      }
                    }
                  }
                  status
                }
        """.trimIndent() //Don't remove `status` field since it's necessary for refresh token flow
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductListMetaResponse::class.java)
    }

    fun setParams(shopId: String) {
        val requestParams = RequestParams.create()
        val extraInfo = listOf(EXTRA_INFO_RBAC)
        requestParams.putString(PARAM_SHOP_ID, shopId)
        requestParams.putObject(PARAM_EXTRA_INFO, extraInfo)
        setRequestParams(requestParams.parameters)
    }
}