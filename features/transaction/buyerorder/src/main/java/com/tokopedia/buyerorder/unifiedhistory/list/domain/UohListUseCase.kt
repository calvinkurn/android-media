package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.common.util.BuyerConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/07/20.
 */
class UohListUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(param: UohListParam): Result<UohListOrder.Data.UohOrders> {
        return try {
            val request = GraphqlRequest(QUERY, UohListOrder.Data::class.java, generateParam(param))
            val response = gqlRepository.getReseponse(listOf(request)).getSuccessData<UohListOrder.Data>()
            Success(response.uohOrders)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(param: UohListParam): Map<String, Any?> {
        return mapOf(BuyerConsts.PARAM_INPUT to param)
    }

    companion object {
        val QUERY = """
            query GetOrderHistory(${'$'}input:UOHOrdersRequest!){
                uohOrders(input:${'$'}input) {
                    orders {
                      orderUUID
                      verticalID
                      verticalCategory
                      userID
                      status
                      verticalStatus
                      searchableText
                      metadata {
                        detailURL {
                          appURL
                          appTypeLink
                          webURL
                          webTypeLink
                        }
                        upstream
                        verticalLogo
                        verticalLabel
                        paymentDate
                        paymentDateStr
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
                        otherInfo {
                          actionType
                          appURL
                          webURL
                          label
                          textColor
                          bgColor
                        }
                        totalPrice {
                          value
                          label
                          textColor
                          bgColor
                        }
                        tickers {
                          action {
                            actionType
                            appURL
                            webURL
                            label
                            textColor
                            bgColor
                          }
                          title
                          text
                          type
                          isFull
                        }
                        buttons {
                          Label
                          variantColor
                          type
                          actionType
                          appURL
                          webURL
                        }
                        dotMenus {
                          actionType
                          appURL
                          webURL
                          label
                          textColor
                          bgColor
                        }
                        queryParams
                        listProducts
                      }
                      createTime
                      createBy
                      updateTime
                      updateBy
                    }
                    filters
                    filtersV2 {
                      label
                      value
                      isPrimary
                    }
                    categories {
                      value
                      label
                    }
                    next
                    dateLimit
                    tickers {
                      action {
                        actionType
                        appURL
                        webURL
                        label
                        textColor
                        bgColor
                      }
                      title
                      text
                      type
                      isFull
                    }
                  }
                }
        """.trimIndent()
    }
}