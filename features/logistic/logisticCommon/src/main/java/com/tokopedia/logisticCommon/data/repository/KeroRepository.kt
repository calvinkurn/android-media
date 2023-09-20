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
import com.tokopedia.logisticCommon.data.response.AddressResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictDetailsResponse
import com.tokopedia.logisticCommon.data.response.KeroAddrGetDistrictCenterResponse
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.data.response.PinpointValidationResponse
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class KeroRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    companion object {
        private const val LIMIT_ADDRESS_LIST = 5
        private const val PARAM_ADDR_IDS = "addr_ids"
        private const val PARAM_EXTRACT_ADDRESS_DETAIL = "extract_address_detail"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_TRACK_ACTIVITY = "track_activity"
        private const val PARAM_LATLNG = "latlng"
        private const val PARAM_IS_MANAGE_ADDRESS_FLOW = "is_manage_address_flow"
    }

    suspend fun getAddressDetail(addressId: String, source: String, needToTrack: Boolean = false): KeroGetAddressResponse.Data {
        val param = mapOf(
            "input" to mapOf(
                PARAM_ADDR_IDS to addressId,
                PARAM_EXTRACT_ADDRESS_DETAIL to true,
                PARAM_SOURCE to source,
                PARAM_TRACK_ACTIVITY to needToTrack
            )
        )
        val request = GraphqlRequest(
            KeroLogisticQuery.kero_get_address_detail,
            KeroGetAddressResponse.Data::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun getAddress(): AddressResponse {
        val param = mapOf(
            "input" to mapOf(
                "show_address" to true,
                "page" to 1,
                "limit" to LIMIT_ADDRESS_LIST
            )
        )
        val request = GraphqlRequest(
            KeroLogisticQuery.addressCorner,
            AddressResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun getZipCode(districtId: String): GetDistrictDetailsResponse {
        val param = mapOf(
            "query" to districtId,
            "page" to "1"
        )
        val request = GraphqlRequest(
            KeroLogisticQuery.getDistrictDetails,
            GetDistrictDetailsResponse::class.java,
            param
        )
        return gql.getResponse(request)
    }

    suspend fun getDistrictCenter(districtId: Long): KeroAddrGetDistrictCenterResponse.Data {
        val param = mapOf("districtId" to districtId)
        val request = GraphqlRequest(
            KeroLogisticQuery.kero_addr_get_district_center,
            KeroAddrGetDistrictCenterResponse.Data::class.java,
            param
        )
        return gql.getResponse(request)
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
