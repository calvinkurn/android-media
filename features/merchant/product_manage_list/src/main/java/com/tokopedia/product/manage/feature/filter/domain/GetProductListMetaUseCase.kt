package com.tokopedia.product.manage.feature.filter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductListMetaUseCase @Inject constructor(
        repository: GraphqlRepository) : GraphqlUseCase<ProductListMetaResponse>(repository) {

    companion object {
        const val PARAM_SHOP_ID = "shopID"
        private val query by lazy {
            val shopID = "\$shopID"
            """
            query ProductListMeta($shopID : String!){
                ProductListMeta(shopID:$shopID){
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
                }
        """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductListMetaResponse::class.java)
    }

    fun setParams(shopId: String) {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_SHOP_ID, shopId)
        setRequestParams(requestParams.parameters)
    }
}