package com.tokopedia.test.application.environment.interceptor.mock

import android.content.Context

abstract class MockModelConfig {
    companion object {
        const val FIND_BY_CONTAINS = 0
        const val FIND_BY_QUERY_NAME = 1
        /* Proto #1a & #2a */
        const val FIND_BY_CONTAINS_ALL = 2
        /* Proto #1b & 2b */
        const val FIND_BY_CONTAINS_ALL_REGEX = 3
    }

    private val responseList = mutableListOf<MockModel>()

    abstract fun createMockModel(context: Context): MockModelConfig

    fun addMockResponse(key: String, value: String, findType: Int) {
        responseList.add(MockModel(listOf(key), value, findType))
    }

    /* Proto #1 */
    fun addMockResponse(keys: List<String>, value: String, findType: Int) {
        responseList.add(MockModel(keys, value, findType))
    }

    /* Proto #2 */
    fun addMockResponse(keys: MockKey, value: String, findType: Int) {
        responseList.add(MockModel(keys.inList(), value, findType))
    }

    fun getResponseList() = responseList.toList()
}