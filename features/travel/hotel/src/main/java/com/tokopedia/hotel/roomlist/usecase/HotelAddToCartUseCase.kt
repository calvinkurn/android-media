package com.tokopedia.hotel.roomlist.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.data.HotelErrorException
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartData
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.util.HotelUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 08/05/19
 */

class HotelAddToCartUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(rawQuery: GqlQueryInterface, hotelAddCartParam: HotelAddCartParam): Result<HotelAddCartData.Response> {

        if (hotelAddCartParam.rooms.isNotEmpty())
            hotelAddCartParam.idempotencyKey = generateIdEmpotency(hotelAddCartParam.rooms.first().roomId)
        val param = mapOf(PARAM_ADD_TO_CART to hotelAddCartParam)

        useCase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(rawQuery, HotelAddCartData.Response::class.java, param)
            useCase.addRequest(graphqlRequest)
            val graphqlResponse = useCase.executeOnBackground()
            val errors = graphqlResponse.getError(HOTEL_ADD_TO_CART_RESPONSE_TYPE)

            if (errors != null && errors.isNotEmpty() && errors[0].extensions != null) {
                Fail(HotelErrorException(errors[0].extensions.code, errors[0].message))
            } else {
                val hotelAddToCartData = graphqlResponse.getData<HotelAddCartData.Response>(HOTEL_ADD_TO_CART_RESPONSE_TYPE)
                Success(hotelAddToCartData)
            }
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateIdEmpotency(roomId: String): String {
        val timeMillis = System.currentTimeMillis().toString()
        val token = HotelUtil.md5(timeMillis)
        return if (token.isEmpty()) "${roomId}_$timeMillis" else "${roomId}_$token"
    }

    companion object {
        const val PARAM_ADD_TO_CART = "data"
        val HOTEL_ADD_TO_CART_RESPONSE_TYPE = HotelAddCartData.Response::class.java
    }
}