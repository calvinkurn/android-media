package com.tokopedia.logisticaddaddress.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class LogisticTestInterceptor : BaseLogisticInterceptor() {

    var autoCompleteResponsePath: String = ""

    var getDistrictResponsePath: String = ""

    var getDistrictRecommendationResponsePath: String = ""

    var getDistrictCenterResponsePath: String = ""

    var autofillResponsePath: String = ""

    var getAddressResponsePath: String = ""

    var editAddressResponsePath: String = ""

    var saveAddressResponsePath: String = ""

    var pinPointValidationResponsePath: String = ""

    var getCollectionPointResponsePath: String = ""

    var districtBoundaryResponsePath: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(AUTOCOMPLETE_KEY)) {
            return mockResponse(copy, autoCompleteResponsePath)
        } else if (requestString.contains(GET_DISTRICT_KEY)) {
            return mockResponse(copy, getDistrictResponsePath)
        } else if (requestString.contains(GET_DISTRICT_RECOMMENDATION_KEY)) {
            return mockResponse(copy, getDistrictRecommendationResponsePath)
        } else if (requestString.contains(GET_ADDRESS_KEY)) {
            return mockResponse(copy, getAddressResponsePath)
        } else if (requestString.contains(EDIT_ADDRESS_KEY)) {
            return mockResponse(copy, editAddressResponsePath)
        } else if (requestString.contains(SAVE_ADDRESS_KEY)) {
            return mockResponse(copy, saveAddressResponsePath)
        } else if (requestString.contains(PINPOINT_VALIDATION_KEY)) {
            return mockResponse(copy, pinPointValidationResponsePath)
        } else if (requestString.contains(GET_COLLECTION_POINT)) {
            return mockResponse(copy, getCollectionPointResponsePath)
        } else if (requestString.contains(GET_DISTRICT_CENTER)) {
            return mockResponse(copy, getDistrictCenterResponsePath)
        } else if (requestString.contains(AUTO_FILL_KEY)) {
            return mockResponse(copy, autofillResponsePath)
        } else if (requestString.contains(DISTRICT_BOUNDARY_KEY)) {
            return mockResponse(copy, districtBoundaryResponsePath)
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        autoCompleteResponsePath = ""
        getDistrictResponsePath = ""
        getDistrictRecommendationResponsePath = ""
        getAddressResponsePath = ""
        editAddressResponsePath = ""
        saveAddressResponsePath = ""
        pinPointValidationResponsePath = ""
        getCollectionPointResponsePath = ""
        getDistrictCenterResponsePath = ""
        autofillResponsePath = ""
        districtBoundaryResponsePath = ""
    }
}

const val AUTOCOMPLETE_KEY = "KeroMapsAutoComplete"
const val GET_DISTRICT_KEY = "KeroPlacesGetDistrict"
const val SAVE_ADDRESS_KEY = "kero_add_address"
const val GET_DISTRICT_RECOMMENDATION_KEY = "GetDistrictRecommendation"
const val GET_ADDRESS_KEY = "kero_get_address"
const val GET_DISTRICT_CENTER = "kero_addr_get_district_center"
const val EDIT_ADDRESS_KEY = "kero_edit_address"
const val PINPOINT_VALIDATION_KEY = "pinpoint_validation"
const val GET_COLLECTION_POINT = "GetCollectionPoint"
const val AUTO_FILL_KEY = "kero_maps_autofill"
const val DISTRICT_BOUNDARY_KEY = "keroGetDistrictBoundaryArray"
