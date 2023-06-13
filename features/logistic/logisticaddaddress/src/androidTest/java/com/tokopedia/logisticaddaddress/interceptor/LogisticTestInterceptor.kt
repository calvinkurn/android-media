package com.tokopedia.logisticaddaddress.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class LogisticTestInterceptor : BaseLogisticInterceptor() {

    var autoCompleteResponsePath: String = ""

    var getDistrictResponsePath: String = ""

    var getDistrictRecommendationResponsePath: String = ""

    var getAddressResponsePath: String = ""

    var editAddressResponsePath: String = ""

    var saveAddressResponsePath: String = ""

    var pinPointValidationResponsePath: String = ""

    var getCollectionPointResponsePath: String = ""

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
    }
}

const val AUTOCOMPLETE_KEY = "KeroMapsAutoComplete"
const val GET_DISTRICT_KEY = "KeroPlacesGetDistrict"
const val SAVE_ADDRESS_KEY = "kero_add_address"
const val GET_DISTRICT_RECOMMENDATION_KEY = "GetDistrictRecommendation"
const val GET_ADDRESS_KEY = "kero_get_address"
const val EDIT_ADDRESS_KEY = "kero_edit_address"
const val PINPOINT_VALIDATION_KEY = "pinpoint_validation"
const val GET_COLLECTION_POINT = "GetCollectionPoint"
