package com.tokopedia.loginregister.login.stub.response

import android.content.Context
import com.tokopedia.loginregister.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper

/**
 * Created by Yoris Prayogo on 07/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class LoginMockResponse: MockModelConfig() {

    companion object{
        const val KEY_REGISTER_CHECK_EMAIL_SUCCESS = "registerCheck"
        const val KEY_LOGIN_SUCCESS = "login_token"
        const val KEY_GET_PROFILE = "profile"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_REGISTER_CHECK_EMAIL_SUCCESS,
                InstrumentationMockHelper.getRawString(context, R.raw.register_check_email_success),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_LOGIN_SUCCESS,
                InstrumentationMockHelper.getRawString(context, R.raw.login_token_success),
                FIND_BY_CONTAINS)

        addMockResponse(
                KEY_GET_PROFILE,
                InstrumentationMockHelper.getRawString(context, R.raw.get_profile_success),
                FIND_BY_CONTAINS)

        return this
    }
}