package com.tokopedia.checkout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.saveshipmentstate.SaveShipmentStateRequest
import com.tokopedia.checkout.data.model.response.saveshipmentstate.SaveShipmentStateGqlResponse
import com.tokopedia.checkout.domain.model.saveshipmentstate.SaveShipmentStateData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.purchase_platform.common.constant.CartConstant.CART_ERROR_GLOBAL
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import javax.inject.Inject

class SaveShipmentStateGqlUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SaveShipmentStateRequest, SaveShipmentStateData>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return SAVE_SHIPMENT_MUTATION
    }

    override suspend fun execute(params: SaveShipmentStateRequest): SaveShipmentStateData {
        val gqlResponse = graphqlRepository.response(
            listOf(
                GraphqlRequest(
                    graphqlQuery(),
                    SaveShipmentStateGqlResponse::class.java,
                    mapOf("carts" to params.requestDataList)
                )
            )
        ).getData<SaveShipmentStateGqlResponse>(SaveShipmentStateGqlResponse::class.java)
        if (gqlResponse != null) {
            return SaveShipmentStateData().apply {
                isSuccess = gqlResponse.saveShipmentStateResponse.success == 1
                error = gqlResponse.saveShipmentStateResponse.error
                message = gqlResponse.saveShipmentStateResponse.message
            }
        } else {
            throw CartResponseErrorException(CART_ERROR_GLOBAL)
        }
    }
}

const val SAVE_SHIPMENT_MUTATION = """
    mutation save_shipment(${"$"}carts: [SaveShipmentParam]) {
        save_shipment(carts: ${"$"}carts) {
            error_message
            status
            data {
                success
                error
                message
            }
        }
    }
"""
