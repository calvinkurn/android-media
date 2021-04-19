package com.tokopedia.otp.verification.phone

import android.content.Context
import com.tokopedia.otp.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class OTPMethodPhoneMockResponse: MockModelConfig() {
    companion object{
        const val KEY_OTP_MODE_LIST = "otp_mode_list"
        const val KEY_OTP_REQUEST = "otp_request"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_OTP_MODE_LIST,
                InstrumentationMockHelper.getRawString(context, R.raw.success_get_otp_method_phone),
                FIND_BY_CONTAINS)
        addMockResponse(
                KEY_OTP_REQUEST,
                InstrumentationMockHelper.getRawString(context, R.raw.success_otp_request_phone),
                FIND_BY_CONTAINS)
        return this
    }
}