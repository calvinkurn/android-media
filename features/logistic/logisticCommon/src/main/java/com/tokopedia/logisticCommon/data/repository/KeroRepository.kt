package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.data.response.PinpointValidationResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class KeroRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    suspend fun pinpointValidation(
        districtId: Int,
        latitude: String,
        longitude: String,
        postalCode: String
    ): PinpointValidationResponse {
        val gqlParam = mapOf(
            "district_id" to districtId,
            "latitude" to latitude,
            "longitude" to longitude,
            "postal_code" to postalCode
        )
        val request = GraphqlRequest(
            KeroLogisticQuery.pinpoint_validation,
            PinpointValidationResponse::class.java,
            gqlParam
        )
        return gql.getResponse(request)
    }
}
