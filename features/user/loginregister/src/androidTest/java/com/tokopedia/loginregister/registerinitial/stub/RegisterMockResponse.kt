package com.tokopedia.loginregister.registerinitial.stub

import android.content.Context
import com.tokopedia.loginregister.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class RegisterMockResponse(private val mockResponse: Int): MockModelConfig() {
    companion object{
        const val KEY_REGISTER_CHECK = "register_check"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_REGISTER_CHECK,
                InstrumentationMockHelper.getRawString(context, mockResponse),
                FIND_BY_CONTAINS)

        return this
    }
}