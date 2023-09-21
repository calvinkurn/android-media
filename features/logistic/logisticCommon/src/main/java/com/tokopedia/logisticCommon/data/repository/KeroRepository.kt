package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.data.request.AddAddressParam
import com.tokopedia.logisticCommon.data.request.EditAddressParam
import com.tokopedia.logisticCommon.data.response.AddAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.data.response.PinpointValidationResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class KeroRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    companion object {
        private const val PARAM_LATLNG = "latlng"
        private const val PARAM_IS_MANAGE_ADDRESS_FLOW = "is_manage_address_flow"
    }

    suspend fun getDistrictGeocode(
        latlong: String?,
        isManageAddressFlow: Boolean = false
    ): AutoFillResponse {
        val param = mapOf(
            PARAM_LATLNG to latlong,
            PARAM_IS_MANAGE_ADDRESS_FLOW to isManageAddressFlow
        )
        val request = GraphqlRequest(
            KeroLogisticQuery.keroMapsAutofill,
            AutoFillResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun saveAddress(
        model: SaveAddressDataModel,
        source: String,
        consentJson: String
    ): AddAddressResponse {
        val param = AddAddressParam(
            addrName = model.addressName,
            receiverName = model.receiverName,
            address1 = model.address1,
            address1Notes = model.address1Notes,
            address2 = model.address2,
            postalCode = model.postalCode,
            phone = model.phone,
            province = model.provinceId.toString(),
            city = model.cityId.toString(),
            district = model.districtId.toString(),
            latitude = model.latitude,
            longitude = model.longitude,
            isAnaPositive = model.isAnaPositive,
            setAsPrimaryAddress = model.setAsPrimaryAddresss,
            applyNameAsNewUserFullname = model.applyNameAsNewUserFullname,
            source = source,
            isTokonowRequest = model.isTokonowRequest,
            consentJson = consentJson
        )
        val gqlParam = mapOf("input" to param)
        val request = GraphqlRequest(
            KeroLogisticQuery.kero_add_address_query,
            AddAddressResponse::class.java,
            gqlParam
        )
        return gql.getResponse(request)
    }

    suspend fun editAddress(
        model: SaveAddressDataModel,
        source: String
    ): KeroEditAddressResponse.Data {
        val param = EditAddressParam(
            addressId = model.id,
            addressName = model.addressName,
            receiverName = model.receiverName,
            address1 = model.address1,
            address1Notes = model.address1Notes,
            address2 = model.address2,
            postalCode = model.postalCode,
            district = model.districtId.toString(),
            city = model.cityId.toString(),
            province = model.provinceId.toString(),
            phone = model.phone,
            latitude = model.latitude,
            longitude = model.longitude,
            source = source,
            isTokonowRequest = model.isTokonowRequest
        )
        val gqlParam = mapOf("input" to param)
        val request = GraphqlRequest(
            KeroLogisticQuery.kero_edit_address,
            KeroEditAddressResponse.Data::class.java,
            gqlParam
        )
        return gql.getResponse(request)
    }

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
