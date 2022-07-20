package com.tokopedia.inactivephone_otp.testing.inactivephone

import android.content.Context
import com.tokopedia.inactivephone_otp.testing.common.BaseInactivePhoneOtpInterceptor
import com.tokopedia.inactivephone_otp.testing.R
import com.tokopedia.test.application.util.InstrumentationMockHelper
import okhttp3.Interceptor
import okhttp3.Response

class InactivePhoneInterceptor constructor(
    private val context: Context
): BaseInactivePhoneOtpInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        return when {
            requestString.contains(QUERY_GET_ACCOUNT_LIST) -> {
                mockResponse(copy, getResponseFromRaw(RAW_GET_ACCOUNT_LIST))
            }
            requestString.contains(QUERY_GET_STATUS_INACTIVE_PHONE_NUMBER) -> {
                mockResponse(copy, getResponseFromRaw(RAW_GET_STATUS_INACTIVE_PHONE_NUMBER))
            }
            requestString.contains(QUERY_PHONE_VALIDATION) -> {
                mockResponse(copy, getResponseFromRaw(RAW_PHONE_VALIDATION))
            }
            requestString.contains(QUERY_SUBMIT_DATA_EXPEDITED) -> {
                mockResponse(copy, getResponseFromRaw(RAW_SUBMIT_DATA_EXPEDITED))
            }
            requestString.contains(QUERY_VERIFY_NEW_PHONE_NUMBER) -> {
                mockResponse(copy, getResponseFromRaw(RAW_VERIFY_NEW_PHONE_NUMBER))
            }
            else -> {
                return chain.proceed(chain.request())
            }
        }
    }

    private fun getResponseFromRaw(raw: Int) : String {
        return InstrumentationMockHelper.getRawString(context, raw)
    }

    companion object {
        const val QUERY_GET_ACCOUNT_LIST = "accountsGetAccountsList"
        const val QUERY_GET_STATUS_INACTIVE_PHONE_NUMBER = "GetStatusInactivePhoneNumber"
        const val QUERY_PHONE_VALIDATION = "validateInactivePhoneUser"
        const val QUERY_SUBMIT_DATA_EXPEDITED = "SubmitExpeditedInactivePhone"
        const val QUERY_VERIFY_NEW_PHONE_NUMBER = "verifyNewPhoneInactivePhoneUser"

        val RAW_GET_ACCOUNT_LIST = R.raw.get_account_list_response
        val RAW_GET_STATUS_INACTIVE_PHONE_NUMBER = R.raw.get_status_inactive_phone_number_response
        val RAW_PHONE_VALIDATION = R.raw.validate_inactive_phone_number_response
        val RAW_SUBMIT_DATA_EXPEDITED = R.raw.submit_expedited_inactive_phone_response
        val RAW_VERIFY_NEW_PHONE_NUMBER = R.raw.verify_new_phone_inactive_phone_user_response
    }
}