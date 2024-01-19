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
