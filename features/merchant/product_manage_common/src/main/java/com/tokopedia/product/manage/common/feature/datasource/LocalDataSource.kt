package com.tokopedia.product.manage.common.feature.datasource

import com.tokopedia.product.manage.common.feature.datasource.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getDataSourceFlow(): Flow<UserPreferences>

    suspend fun updateDataSource(status: Int, productId: String)

    suspend fun clearDataStore()
}
