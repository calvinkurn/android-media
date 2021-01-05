package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.query.ShopLocationQuery
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocationWhitelistResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class ShopLocationRepository @Inject constructor(private val gql: GraphqlRepository) {

    suspend fun getShopLocationWhitelist(shopId: Int): ShopLocationWhitelistResponse {
        val param = mapOf("shop_id" to shopId)
        val request = GraphqlRequest(ShopLocationQuery.shopLocationWhitelist,
                ShopLocationWhitelistResponse::class.java, param)
        return gql.getResponse(request)
    }
}