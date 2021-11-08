package com.tokopedia.localizationchooseaddress.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.data.query.ChooseAddressQuery
import com.tokopedia.localizationchooseaddress.domain.model.StateChooseAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.localizationchooseaddress.util.getResponse
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import javax.inject.Inject

class ChooseAddressRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository){

    suspend fun getChosenAddressList(source: String, isTokonow: Boolean): GetChosenAddressListQglResponse {
        val param = mapOf("source" to source,
            "is_tokonow_request" to isTokonow)
        val request = GraphqlRequest(ChooseAddressQuery.getChosenAddressList,
                GetChosenAddressListQglResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun setStateChosenAddress(model: StateChooseAddressParam): SetStateChosenAddressQqlResponse {
        val gqlParam = mapOf("input" to model.toMap() )
        val request = GraphqlRequest(ChooseAddressQuery.setStateChosenAddress,
            SetStateChosenAddressQqlResponse::class.java, gqlParam)
        return gql.getResponse(request)
    }

    suspend fun setStateChosenAddressFromAddress(model: RecipientAddressModel): SetStateChosenAddressQqlResponse {
        val param = StateChooseAddressParam(
                model.addressStatus, model.id.toLong(), model.recipientName,
                model.addressName, model.latitude, model.longitude,
                model.destinationDistrictId.toLong(), model.postalCode,
                true
        )
        val gqlParam = mapOf("input" to param.toMap() )
        val request = GraphqlRequest(ChooseAddressQuery.setStateChosenAddress,
                SetStateChosenAddressQqlResponse::class.java, gqlParam)
        return gql.getResponse(request)
    }

    suspend fun getStateChosenAddress(source: String, isTokonow: Boolean): GetStateChosenAddressQglResponse {
        val param = mapOf("source" to source, "is_tokonow_request" to isTokonow)
        val request = GraphqlRequest(ChooseAddressQuery.getStateChosenAddress,
                GetStateChosenAddressQglResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getDefaultChosenAddress(latLong: String?, source: String, isTokonow: Boolean): GetDefaultChosenAddressGqlResponse {
        val param = mapOf("lat_long" to latLong, "source" to source,
                "is_tokonow_request" to isTokonow)
        val request = GraphqlRequest(ChooseAddressQuery.getDefaultChosenAddress,
                GetDefaultChosenAddressGqlResponse::class.java, param)
        return gql.getResponse(request)
    }
}