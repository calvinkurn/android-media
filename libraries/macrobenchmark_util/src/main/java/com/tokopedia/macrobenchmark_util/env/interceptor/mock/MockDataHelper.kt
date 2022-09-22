package com.tokopedia.macrobenchmark_util.env.interceptor.mock

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tokopedia.macrobenchmark_util.env.mock.MockModelConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object MockDataHelper {
    fun setMock(context: Context, responseConfigs: List<MockModelConfig>) {
        runBlocking {
            resetMock(context)
            responseConfigs.forEach {
                it.getResponseList().forEach {
                    addMock(context, it.key, it.value.value)
                }
            }
        }
    }

    suspend fun resetMock(context: Context) {
        context.dataStore.edit { mock_response ->
            mock_response.clear()
        }
    }
    suspend fun addMock(context: Context, gqlKey: String, response: String) {
        context.dataStore.edit { mock_response ->
            mock_response[stringPreferencesKey(gqlKey)] = response
            Log.d("devfik", "Mock set: "+gqlKey)
        }
        context.dataStore.data
            .map { preferences ->
                // No type safety.
            }.first()
    }
}