package com.tokopedia.localizationchooseaddress.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.data.query.ChooseAddressQuery
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.util.getResponse
import javax.inject.Inject

class ChooseAddressRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    suspend fun getDefaultChosenAddress(latLong: String?, source: String, isTokonow: Boolean): GetDefaultChosenAddressGqlResponse {
        val param = mapOf(
            "lat_long" to latLong,
            "source" to source,
            "is_tokonow_request" to isTokonow
        )
        val request = GraphqlRequest(
            ChooseAddressQuery.getDefaultChosenAddress,
            GetDefaultChosenAddressGqlResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }
}
