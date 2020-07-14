package com.tokopedia.test.application.environment

import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

interface MockResponseInterface {
    fun reInitMockResponse(mapMockResponse:HashMap<String, String>)
    fun setupMockResponse(mockModelConfig: MockModelConfig?)
}