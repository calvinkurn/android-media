package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PreferenceTestInterceptor : BaseOccInterceptor() {

    var customGetPreferenceListResponsePath: String? = null
    var customGetPreferenceListThrowable: IOException? = null

    var customSetDefaultPreferenceResponsePath: String? = null
    var customSetDefaultPreferenceThrowable: IOException? = null

    var customGetPreferenceByIdResponsePath: String? = null
    var customGetPreferenceByIdThrowable: IOException? = null

    var customCreatePreferenceResponsePath: String? = null
    var customCreatePreferenceThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_PREFERENCE_LIST_QUERY)) {
            if (customGetPreferenceListThrowable != null) {
                throw customGetPreferenceListThrowable!!
            } else if (customGetPreferenceListResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetPreferenceListResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_PREFERENCE_LIST_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(SET_DEFAULT_PROFILE_QUERY)) {
            if (customSetDefaultPreferenceThrowable != null) {
                throw customSetDefaultPreferenceThrowable!!
            } else if (customSetDefaultPreferenceResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customSetDefaultPreferenceResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(SET_DEFAULT_PROFILE_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(GET_PREFERENCE_BY_ID_QUERY)) {
            if (customGetPreferenceByIdThrowable != null) {
                throw customGetPreferenceByIdThrowable!!
            } else if (customGetPreferenceByIdResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetPreferenceByIdResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_PREFERENCE_BY_ID_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(CREATE_PREFERENCE_QUERY)) {
            if (customCreatePreferenceThrowable != null) {
                throw customCreatePreferenceThrowable!!
            } else if (customCreatePreferenceResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customCreatePreferenceResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(CREATE_PREFERENCE_SUCCESS_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetPreferenceListResponsePath = null
        customGetPreferenceListThrowable = null
        customSetDefaultPreferenceResponsePath = null
        customSetDefaultPreferenceThrowable = null
        customGetPreferenceByIdResponsePath = null
        customGetPreferenceByIdThrowable = null
        customCreatePreferenceResponsePath = null
        customCreatePreferenceThrowable = null
    }
}

const val GET_PREFERENCE_LIST_QUERY = "get_preference_list"
const val SET_DEFAULT_PROFILE_QUERY = "set_default_profile_occ"
const val GET_PREFERENCE_BY_ID_QUERY = "get_profile_by_id_occ"
const val CREATE_PREFERENCE_QUERY = "insert_profile_occ"

const val GET_PREFERENCE_LIST_DEFAULT_RESPONSE_PATH = "preference/get_preference_list_default_response.json"
const val GET_PREFERENCE_LIST_CHANGED_RESPONSE_PATH = "preference/get_preference_list_switched_status_response.json"
const val GET_PREFERENCE_LIST_WITH_ETA_RESPONSE_PATH = "preference/get_preference_list_with_eta_response.json"

const val GET_PREFERENCE_LIST_REVAMP_HALF_DISABLED_RESPONSE_PATH = "preference/get_preference_list_revamp_half_disabled_response.json"

const val SET_DEFAULT_PROFILE_DEFAULT_RESPONSE_PATH = "preference/set_default_profile_success_response.json"

const val GET_PREFERENCE_BY_ID_DEFAULT_RESPONSE_PATH = "preference/get_preference_by_id_default_response.json"

const val CREATE_PREFERENCE_SUCCESS_RESPONSE_PATH = "preference/insert_profile_success_response.json"