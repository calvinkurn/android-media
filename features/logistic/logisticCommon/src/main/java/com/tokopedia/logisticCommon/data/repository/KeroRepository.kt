package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.data.response.AddressResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class KeroRepository @Inject constructor(private val gql: GraphqlRepository) {

    suspend fun getAutoComplete(keyword: String): AutoCompleteResponse {
        val param = mapOf("param" to keyword)
        val request = GraphqlRequest(KeroLogisticQuery.autoCompleteGeocode,
                AutoCompleteResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getDistrict(placeId: String): GetDistrictResponse {
        val param = mapOf("param" to placeId)
        val request = GraphqlRequest(KeroLogisticQuery.placesGetDistrict,
                GetDistrictResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getAddress(): AddressResponse {
        val param = mapOf(
                "input" to mapOf(
                        "show_address" to true,
                        "page" to 1,
                        "limit" to 5
                )
        )
        val request = GraphqlRequest(KeroLogisticQuery.addressCorner,
                AddressResponse::class.java, param)
        return gql.getResponse(request)
    }

}