package com.tokopedia.loginregister.registerinitial.stub

import android.content.Context
import com.tokopedia.loginregister.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

class RegisterMockResponse(private val mockResponses: HashMap<String, Int>): MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        for(response in mockResponses) {
            addMockResponse(response.key,
                    InstrumentationMockHelper.getRawString(context, response.value),
                    FIND_BY_CONTAINS)
        }
        return this
    }

    companion object{
        const val KEY_REGISTER_CHECK = "register_check"
        const val KEY_LOGIN_TOKEN_CHECK = "login_token"
        const val KEY_PROFILE_CHECK = "profile"
    }
}