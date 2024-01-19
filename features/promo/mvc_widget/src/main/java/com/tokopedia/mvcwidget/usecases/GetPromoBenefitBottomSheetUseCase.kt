package com.tokopedia.mvcwidget.usecases

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mvcwidget.data.entity.PromoCatalogResponse
import javax.inject.Inject

class GetPromoBenefitBottomSheetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, PromoCatalogResponse>(dispatcher.io) {

    override suspend fun execute(params: String): PromoCatalogResponse {
        val param = mapOf("metaData" to params)
        return repository.request(graphqlQuery(), param)
    }

    override fun graphqlQuery(): String = """
        query getPromoBenefit(${'$'}metaData: String!){
          promoCatalogGetPDEBottomSheet(source: "pdp", jsonMetadata: ${'$'}metaData) {
            resultStatus {
              code
              message
              status
            }
            resultList {
              productID
              widgetList {
                id
                widgetType
                componentList {
                  id
                  componentType
                  attributeList {
                    type
                    value
                  }
                }
              }
            }
          }
        }
    """.trimIndent()

}

val metaDataSample = """
        {"request_list":[{"product_id":1,"is_low_benefit":false,"additional_data":[{"field":"background_color","value":"#FFF5F6"},{"field":"background_image","value":"https://images.tokopedia.net/img/bs_background_regular.png"},{"field":"nett_price","value":"9000000"},{"field":"price","value":"9500000"},{"field":"benefit_cashback","value":"300000"},{"field":"benefit_cashback_currency","value":"GoPay Coins"},{"field":"benefit_discount","value":"200000"}]}]}
    """.trimIndent()
