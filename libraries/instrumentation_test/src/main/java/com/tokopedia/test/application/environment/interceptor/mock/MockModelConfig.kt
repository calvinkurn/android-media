package com.tokopedia.test.application.environment.interceptor.mock

import android.content.Context

abstract class MockModelConfig {
    companion object {
        const val FIND_BY_CONTAINS = 0
        const val FIND_BY_QUERY_NAME = 1
        const val FIND_BY_QUERY_AND_VARIABLES = 2
    }

    private val responseList = mutableMapOf<String, MockModel>()

    abstract fun createMockModel(context: Context): MockModelConfig

    fun addMockResponse(key: String, value: String, findType: Int) {
        responseList[key] = MockModel(listOf(key), value, findType)
    }

    fun addMockResponse(key: MockKey, value: String) {
        responseList[key.toString()] = MockModel(key.inList(), value, FIND_BY_QUERY_AND_VARIABLES)
    }

    fun getResponseList() = responseList
}