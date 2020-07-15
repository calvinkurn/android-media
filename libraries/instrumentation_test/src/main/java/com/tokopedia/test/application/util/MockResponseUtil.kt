package com.tokopedia.test.application.util

private const val MOCK_TEST = "mockTest"

fun setupGraphqlMockResponseWithCheck(mockModelConfig: MockModelConfig) {
    if (!isMockTest()) return

    setupGraphqlMockResponse(mockModelConfig)
}

fun setupGraphqlMockResponse(mockModelConfig: MockModelConfig) {
    val context = getInstrumentation().targetContext

    mockModelConfig.createMockModel(context)

    val testInterceptors = listOf(MockInterceptor(mockModelConfig))
    GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, context)
}

private fun isMockTest(): Boolean {
    val arguments = InstrumentationRegistry.getArguments()
    val isMockTest = arguments.getString(MOCK_TEST) ?: ""

    return isMockTest.toBoolean()
}