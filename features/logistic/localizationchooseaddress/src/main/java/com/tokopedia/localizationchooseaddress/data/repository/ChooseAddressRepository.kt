package com.tokopedia.localizationchooseaddress.data.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.data.query.ChooseAddressQuery
import com.tokopedia.localizationchooseaddress.domain.model.StateChooseAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.localizationchooseaddress.util.getResponse
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import javax.inject.Inject

class ChooseAddressRepository @Inject constructor(private val gql: GraphqlRepository){

    suspend fun getChosenAddressList(source: String): GetChosenAddressListQglResponse {
        val param = mapOf("source" to source)
        val request = GraphqlRequest(ChooseAddressQuery.getChosenAddressList,
                GetChosenAddressListQglResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun setStateChosenAddress(status: Int?, addressId: Int?, receiverName: String?, addressName: String?, latitude: String?, longitude: String?, districtId: Int?, postalCode: String?): SetStateChosenAddressQqlResponse {
        val param = mapOf("input" to mapOf(
                "status" to status,
                "addr_id" to addressId,
                "addr_name" to addressName,
                "receiver_name" to receiverName,
                "district" to districtId,
                "latitude" to latitude,
                "longitude" to longitude,
                "postal_code" to postalCode))
        val request = GraphqlRequest(ChooseAddressQuery.setStateChosenAddress,
                SetStateChosenAddressQqlResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun setStateChosenAddressFromAddress(model: RecipientAddressModel): SetStateChosenAddressQqlResponse {
        val param = StateChooseAddressParam(
                model.addressStatus, model.id.toInt(), model.recipientName,
                model.addressName, model.latitude, model.longitude,
                model.destinationDistrictId.toInt(), model.postalCode
        )
        val gqlParam = mapOf("input" to param.toMap() )
        val request = GraphqlRequest(ChooseAddressQuery.setStateChosenAddress,
                SetStateChosenAddressQqlResponse::class.java, gqlParam)
        return gql.getResponse(request)
    }

    suspend fun getStateChosenAddress(source: String): GetStateChosenAddressQglResponse {
        val param = mapOf("source" to source)
        val request = GraphqlRequest(ChooseAddressQuery.getStateChosenAddress,
                GetStateChosenAddressQglResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getDefaultChosenAddress(latLong: String?, source: String): GetDefaultChosenAddressGqlResponse {
        val param = mapOf("lat_long" to latLong, "source" to source)
        val request = GraphqlRequest(ChooseAddressQuery.getDefaultChosenAddress,
                GetDefaultChosenAddressGqlResponse::class.java, param)
        return gql.getResponse(request)
    }
}