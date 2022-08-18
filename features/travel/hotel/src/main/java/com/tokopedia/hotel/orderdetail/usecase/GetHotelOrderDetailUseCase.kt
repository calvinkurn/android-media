package com.tokopedia.hotel.orderdetail.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 16/05/19
 */

class GetHotelOrderDetailUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {
    suspend fun execute(rawQuery: GqlQueryInterface, orderId: String, orderCategory: String,
                        fromCloud: Boolean): Result<HotelOrderDetail>{
        val params = mapOf(PARAM_ORDER_ID to orderId,
                PARAM_ORDER_CATEGORY_STR to orderCategory)

        if (fromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        try {
            val graphqlRequest = GraphqlRequest(rawQuery, HotelOrderDetail.Response::class.java, params)
            useCase.addRequest(graphqlRequest)

            val hotelOrderDetail = useCase.executeOnBackground().getSuccessData<HotelOrderDetail.Response>().response
            return Success(hotelOrderDetail)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        const val PARAM_ORDER_ID = "orderId"
        const val PARAM_ORDER_CATEGORY_STR = "orderCategoryStr"
    }
}