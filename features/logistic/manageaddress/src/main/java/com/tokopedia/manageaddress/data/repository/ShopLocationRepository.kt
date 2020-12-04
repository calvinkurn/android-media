package com.tokopedia.manageaddress.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.manageaddress.data.query.ShopLocationQuery
import com.tokopedia.manageaddress.domain.response.shoplocation.GetShopLocationResponse
import com.tokopedia.manageaddress.domain.response.shoplocation.SetShopLocationStatusResponse
import com.tokopedia.manageaddress.util.getResponse
import javax.inject.Inject

class ShopLocationRepository @Inject constructor(private val gql: GraphqlRepository) {

    suspend fun getShopLocation(shopId: Int): GetShopLocationResponse {
        val param = mapOf("param" to shopId)
        val request = GraphqlRequest(ShopLocationQuery.getShopLocation,
                GetShopLocationResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun setShopLocationStatus(): SetShopLocationStatusResponse {
        val request = GraphqlRequest(ShopLocationQuery.setShopLocationStatus,
                GetShopLocationResponse::class.java)
        return gql.getResponse(request)
    }


}