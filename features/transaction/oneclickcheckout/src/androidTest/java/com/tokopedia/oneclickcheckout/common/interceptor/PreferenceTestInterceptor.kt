package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PreferenceTestInterceptor : BaseOccInterceptor() {

    var customGetPreferenceListResponsePath: String? = null
    var customSetDefaultPreferenceResponsePath: String? = null

    var customGetPreferenceListThrowable: IOException? = null
    var customSetDefaultPreferenceThrowable: IOException? = null

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
        } else if (requestString.contains(SET_DEFAULT_PROFILE_QUERY)) {
            if (customSetDefaultPreferenceThrowable != null) {
                throw customSetDefaultPreferenceThrowable!!
            } else if (customSetDefaultPreferenceResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customSetDefaultPreferenceResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(SET_DEFAULT_PROFILE_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }
}

const val GET_PREFERENCE_LIST_QUERY = "get_preference_list"
const val SET_DEFAULT_PROFILE_QUERY = "set_default_profile_occ"

const val GET_PREFERENCE_LIST_DEFAULT_RESPONSE_PATH = "preference/get_preference_list_default_response.json"
const val GET_PREFERENCE_LIST_CHANGED_RESPONSE_PATH = "preference/get_preference_list_switched_status_response.json"

const val SET_DEFAULT_PROFILE_DEFAULT_RESPONSE_PATH = "preference/set_default_profile_success_response.json"