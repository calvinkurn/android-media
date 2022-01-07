package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.AutoFillResponse
import com.tokopedia.logisticCommon.data.query.KeroLogisticQuery
import com.tokopedia.logisticCommon.data.request.AddAddressParam
import com.tokopedia.logisticCommon.data.response.*
import com.tokopedia.logisticCommon.data.utils.getResponse
import javax.inject.Inject

class KeroRepository @Inject constructor(@ApplicationContext private val gql: GraphqlRepository) {

    suspend fun getAutoComplete(keyword: String, latlng: String): AutoCompleteResponse {
        val param = mapOf("param" to keyword, "latlng" to latlng)
        val request = GraphqlRequest(
            KeroLogisticQuery.autoComplete,
            AutoCompleteResponse::class.java, param
        )
        return gql.getResponse(request)
    }

    suspend fun getDistrict(placeId: String): GetDistrictResponse {
        val param = mapOf("param" to placeId)
        val request = GraphqlRequest(KeroLogisticQuery.placesGetDistrict,
                GetDistrictResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getDefaultAddress(source: String): GetDefaultAddressResponse {
        val param = mapOf("source" to source)
        val request = GraphqlRequest(KeroLogisticQuery.kero_addr_get_default,
                GetDefaultAddressResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getDistrictBoundaries(districtId: Long): GetDistrictBoundaryResponse {
        val param = mapOf("districtId" to districtId)
        val request = GraphqlRequest(KeroLogisticQuery.kero_district_boundary,
                GetDistrictBoundaryResponse::class.java, param)
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

    suspend fun getZipCode(districtId: String): GetDistrictDetailsResponse {
        val param = mapOf(
                "query" to districtId,
                "page" to "1"
        )
        val request = GraphqlRequest(KeroLogisticQuery.getDistrictDetails,
                GetDistrictDetailsResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun getDistrictGeocode(latlong: String?): AutoFillResponse {
        val param = mapOf("latlng" to latlong)
        val request = GraphqlRequest(KeroLogisticQuery.keroMapsAutofill,
                AutoFillResponse::class.java, param)
        return gql.getResponse(request)
    }

    suspend fun saveAddress(model: SaveAddressDataModel): AddAddressResponse {
        val param = AddAddressParam(
                model.addressName, model.receiverName, model.address1, model.address2,
                model.postalCode, model.phone, model.provinceId.toString(), model.cityId.toString(),
                model.districtId.toString(), model.latitude, model.longitude, model.isAnaPositive)
        val gqlParam = mapOf("input" to param.toMap())
        val request = GraphqlRequest(KeroLogisticQuery.kero_add_address_query,
                AddAddressResponse::class.java, gqlParam)
        return gql.getResponse(request)

    }

    suspend fun eligibleForAddressFeature(featureId: Int): EligibleForAddressFeatureResponse {
        val gqlParam = mapOf("feature_id" to featureId, "device" to "android", "device_version" to GlobalConfig.VERSION_NAME)
        val request = GraphqlRequest(KeroLogisticQuery.eligible_for_address_feature,
                EligibleForAddressFeatureResponse::class.java, gqlParam)
        return gql.getResponse(request)
    }


}