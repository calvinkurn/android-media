package com.tokopedia.test.application.environment.interceptor.size

import android.content.Context

abstract class SizeModelConfig {
    companion object {
        const val FIND_BY_CONTAINS = 0
        const val FIND_BY_QUERY_NAME = 1
    }

    private val responseList = mutableMapOf<String, SizeModel>()

    abstract fun createModelConfig(context: Context): SizeModelConfig

    fun addSizeModel(key: String, findType: Int) {
        responseList[key] = SizeModel(key, findType)
    }

    fun getResponseList() = responseList.toMap()
}