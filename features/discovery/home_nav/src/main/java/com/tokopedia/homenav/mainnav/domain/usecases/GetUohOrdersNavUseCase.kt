package com.tokopedia.homenav.mainnav.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.homenav.mainnav.data.pojo.order.UohData
import com.tokopedia.homenav.mainnav.data.pojo.order.UohOrders
import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by Fikry on 03/11/20.
 */
class GetUohOrdersNavUseCase (
        private val graphqlUseCase: GraphqlUseCase<UohData>
): UseCase<List<NavProductOrder>>(){
    init {
        val query = """
            query GetOrderHistory(${'$'}input:UOHOrdersRequest!){
              uohOrders(input:${'$'}input) {
                orders {
                          orderUUID
                          status
                          metadata {
                            detailURL {
                              appURL
                            }
                            status {
                              label
                              textColor
                              bgColor
                            }
                            products {
                              title
                              imageURL
                              inline1 {
                                label
                                textColor
                                bgColor
                              }
                              inline2 {
                                label
                                textColor
                                bgColor
                                }
                            }
                        }
                    }
                }
            }
        """.trimIndent()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(generateParam(NavUohListParam()))
        graphqlUseCase.setTypeClass(UohData::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    override suspend fun executeOnBackground(): List<NavProductOrder> {
        return try {
            val responseData = Success(graphqlUseCase.executeOnBackground().uohOrders?:UohOrders())
            val navProductList = mutableListOf<NavProductOrder>()
            responseData.data.orders?.map {
                if (it.metadata?.products?.isNotEmpty() == true) {
                    val product = it.metadata.products[0]
                    val additionalProductCount = it.metadata.products.size-1
                    navProductList.add(NavProductOrder(
                            statusText = it.metadata.status?.label?:"",
                            statusTextColor = it.metadata.status?.textColor?:"",
                            productNameText = product.title?:"",
                            additionalProductCount = additionalProductCount,
                            imageUrl = product.imageURL?:"",
                            id = it.orderUUID?:"",
                            applink = it.metadata.detailURL?.appURL?:""
                    ))
                }
            }
            navProductList
        } catch (e: Throwable){
            listOf()
        }
    }

    private fun generateParam(param: NavUohListParam): Map<String, Any?> {
        return mapOf(PARAM_INPUT to param)
    }

    companion object{
        private const val PARAM_INPUT = "input"
    }
}