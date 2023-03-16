package com.tokopedia.unifyorderhistory.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohListParam
import com.tokopedia.unifyorderhistory.util.UohConsts.PARAM_INPUT
import kotlinx.coroutines.delay
import javax.inject.Inject

@GqlQuery("GetOrderHistoryQuery", UohListUseCase.query)
class UohListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<Pair<UohListParam, Boolean>, UohListOrder>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: Pair<UohListParam, Boolean>): UohListOrder {
        if (params.second) {
            delay(DELAY_LOAD)
        }
        return repository.request(GetOrderHistoryQuery(), createVariables(params.first))
    }

    private fun createVariables(param: UohListParam): Map<String, Any> {
        return mapOf(PARAM_INPUT to param)
    }

    companion object {
        const val DELAY_LOAD = 2000L
        const val query = """
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
            """
    }
}
