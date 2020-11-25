package com.tokopedia.test.application.util

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.test.application.environment.InstrumentationTestApp
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor


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
 * Use this method if your test can switch between real network response and mock response
 * Add "-e mockTest true" when running the test to use mock response
 * */
fun setupGraphqlMockResponseWithCheck(createMockModel: MockModelConfig.() -> Unit) {
    val mockModelConfig = createMockModelConfig(createMockModel)

    setupGraphqlMockResponseWithCheck(mockModelConfig)
}

private fun createMockModelConfig(createMockModel: MockModelConfig.() -> Unit): MockModelConfig {
    return object : MockModelConfig() {
        override fun createMockModel(context: Context): MockModelConfig {
            createMockModel()
            return this
        }
    }
}

/**
 * Use this method if your test ONLY uses mock response Graphql API
 * */
fun setupGraphqlMockResponse(mockModelConfig: MockModelConfig) {
    val context = getInstrumentation().targetContext
    val application = context.applicationContext as InstrumentationTestApp

    mockModelConfig.createMockModel(context)
    application.setInterceptor(MockInterceptor(mockModelConfig))
}

/**
 * Use this method if your test ONLY uses mock response REST API
 * with custom interceptor
 **/
fun setupRestMockResponse(mockModelConfig: MockModelConfig) {
    val context = getInstrumentation().targetContext
    val application = context.applicationContext as InstrumentationTestApp

    mockModelConfig.createMockModel(context)
    application.addRestSupportInterceptor(MockInterceptor(mockModelConfig))
}

fun setupRestMockResponse(createMockModel: MockModelConfig.() -> Unit) {
    val mockModelConfig = createMockModelConfig(createMockModel)

    setupRestMockResponse(mockModelConfig)
}

fun setupGraphqlMockResponse(createMockModel: MockModelConfig.() -> Unit) {
    val mockModelConfig = createMockModelConfig(createMockModel)

    setupGraphqlMockResponse(mockModelConfig)
}

fun setupGraphqlMockResponseWithCheckAndTotalSizeInterceptor(
        mockModelConfig: MockModelConfig,
        listToAnalyze: List<String>
) {
    val context = getInstrumentation().targetContext
    val application = context.applicationContext as InstrumentationTestApp
    if (isMockTest()) {
        mockModelConfig.createMockModel(context)
        application.addInterceptor(MockInterceptor(mockModelConfig))
    }
    GqlNetworkAnalyzerInterceptor.reset()
    GqlNetworkAnalyzerInterceptor.addGqlQueryListToAnalyze(listToAnalyze)
    application.addInterceptor(GqlNetworkAnalyzerInterceptor())
}