package com.tokopedia.test.application.environment.interceptor.mock

import android.content.Context

abstract class MockModelConfig {
    private val responseList = mutableMapOf<String, MockModel>()
    companion object {
        const val FIND_BY_CONTAINS = 0
        const val FIND_BY_QUERY_NAME = 1
    }
    abstract fun createMockModel(context: Context): MockModelConfig

    fun addMockResponse(key: String, value: String, findType: Int) {
        responseList[key] = MockModel(key, value, findType)
    }

    fun getResponseList() = responseList.toMap()
}