package com.tokopedia.hotel.booking.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

/**
 * @author by resakemal on 13/05/19
 */

class GetHotelCartDataUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    fun createRequestParam(cartId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_CART_ID, cartId)
        return requestParams
    }

    suspend fun execute(rawQuery: String, cartId: String,
                        fromCloud: Boolean = true): Result<HotelCart> {
        val requestParams = createRequestParam(cartId)
        val params = mapOf(PARAM_CART_PROPERTY to requestParams)

        if (fromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        try {
            val graphqlRequest = GraphqlRequest(rawQuery, HotelCart::class.java, params)
            useCase.addRequest(graphqlRequest)

            val hotelCartData = async {
                val response =  withContext(Dispatchers.IO) {
                    useCase.executeOnBackground().getSuccessData<HotelCart>()
                }
                response
            }
            return Success(hotelCartData.await())
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        const val PARAM_CART_ID = "cartID"
        const val PARAM_CART_PROPERTY = "data"
    }

}