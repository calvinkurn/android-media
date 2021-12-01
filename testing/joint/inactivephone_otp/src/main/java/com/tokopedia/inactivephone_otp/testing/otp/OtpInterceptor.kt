package com.tokopedia.inactivephone_otp.testing.otp

import android.content.Context
import com.tokopedia.inactivephone_otp.testing.R
import com.tokopedia.inactivephone_otp.testing.common.BaseInactivePhoneOtpInterceptor
import com.tokopedia.test.application.util.InstrumentationMockHelper
import okhttp3.Interceptor
import okhttp3.Response

class OtpInterceptor constructor(
    private val context: Context
): BaseInactivePhoneOtpInterceptor() {

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        return when {
            requestString.contains(QUERY_OTP_MODE_LIST) -> {
                mockResponse(copy, getResponseFromRaw(RAW_OTP_MODE_LIST))
            }
            requestString.contains(QUERY_OTP_REQUEST) -> {
                mockResponse(copy, getResponseFromRaw(RAW_OTP_REQUEST))
            }
            requestString.contains(QUERY_OTP_VALIDATION) -> {
                mockResponse(copy, getResponseFromRaw(RAW_OTP_VERIFICATION))
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
        const val QUERY_OTP_MODE_LIST = "otp_mode_list"
        const val QUERY_OTP_REQUEST = "otp_request"
        const val QUERY_OTP_VALIDATION = "otp_validate"

        val RAW_OTP_MODE_LIST = R.raw.get_verification_method_response
        val RAW_OTP_REQUEST = R.raw.send_otp_response
        val RAW_OTP_VERIFICATION = R.raw.otp_validate_response
    }
}