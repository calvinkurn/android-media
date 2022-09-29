package com.tokopedia.macrobenchmark_util.env.interceptor.mock

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig

object MockDataHelper {
    fun setMock(context: Context, responseConfigs: List<MockModelConfig>) {
        val sharedPref = context.getSharedPreferences(
            "mock_response", Context.MODE_PRIVATE)
        resetMock(sharedPref)
        responseConfigs.forEach {
            it.getResponseList().forEach {
                addMock(it.key, it.value.value, sharedPref)
            }
        }
    }

    fun resetMock(sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
    fun addMock(gqlKey: String, response: String, sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            putString(gqlKey, response)
            apply()
        }
    }
}
