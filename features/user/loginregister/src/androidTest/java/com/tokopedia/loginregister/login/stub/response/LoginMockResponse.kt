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
        const val KEY_REGISTER_CHECK = "register_check"
    }

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                KEY_REGISTER_CHECK,
                InstrumentationMockHelper.getRawString(context, R.raw.register_check),
                FIND_BY_CONTAINS)

        return this
    }
}