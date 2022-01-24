package com.tokopedia.product.manage.common.feature.datasource

import com.tokopedia.product.manage.common.feature.datasource.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun userPreferencesFlow(): Flow<UserPreferences>

    suspend fun updateStatus(status: Int)

    suspend fun clearDataStore()
}
