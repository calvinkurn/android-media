package com.tokopedia.otp.verification.email.stub

import android.content.Context
import com.tokopedia.otp.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class OTPEmailMockResponse: MockModelConfig() {
    companion object{
        const val KEY_OTP_REQUEST = "otp_request"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_OTP_REQUEST,
                InstrumentationMockHelper.getRawString(context, R.raw.success_otp_request_email),
                FIND_BY_CONTAINS)
        return this
    }
}