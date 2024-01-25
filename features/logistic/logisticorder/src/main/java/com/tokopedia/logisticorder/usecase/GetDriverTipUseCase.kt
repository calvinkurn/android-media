package com.tokopedia.logisticorder.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticorder.domain.response.GetDriverTipResponse
import javax.inject.Inject

class GetDriverTipUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String?, GetDriverTipResponse>(dispatcher.io) {

    override suspend fun execute(orderId: String?): GetDriverTipResponse {
        val param = mapOf(
            "input" to mapOf(
                "order_id" to orderId
            )
        )
        return gql.request(graphqlQuery(), param)
    }

    override fun graphqlQuery() = """
        mutation mpLogisticDriverTipInfo (${'$'}input: MPLogisticDriverTipInfoInputs! ){
          mpLogisticDriverTipInfo(input: ${'$'}input) {
             status
             last_driver {
               photo
               name
               phone
               license_number
               is_changed
             }
             prepayment {
               info
               preset_amount
               max_amount
               min_amount
               payment_link
             }
             payment {
               amount
               amount_formatted
               method
               method_icon
             }
           }
         }
    """.trimIndent()
}
