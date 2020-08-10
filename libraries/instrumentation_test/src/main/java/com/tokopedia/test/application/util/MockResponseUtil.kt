package com.tokopedia.test.application.util

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.test.application.environment.InstrumentationTestApp
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

private const val MOCK_TEST = "mockTest"

/**
 * Use this method if your test can switch between real network response and mock response
 * Add "-e mockTest true" when running the test to use mock response
 * */
fun setupGraphqlMockResponseWithCheck(mockModelConfig: MockModelConfig) {
    if (!isMockTest()) return

    setupGraphqlMockResponse(mockModelConfig)
}

private fun isMockTest(): Boolean {
    val arguments = InstrumentationRegistry.getArguments()
    val isMockTest = arguments.getString(MOCK_TEST) ?: ""

    return isMockTest.toBoolean()
}

/**
 * Use this method if your test ONLY uses mock response
 * */
fun setupGraphqlMockResponse(mockModelConfig: MockModelConfig) {
    val context = getInstrumentation().targetContext
    val application = context.applicationContext as InstrumentationTestApp

    mockModelConfig.createMockModel(context)
    application.addInterceptor(MockInterceptor(mockModelConfig))
}