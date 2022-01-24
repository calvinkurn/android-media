package com.tokopedia.product.manage.common.feature.datasource.repository

import com.tokopedia.product.manage.common.feature.datasource.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface DataSourceRepository {
    fun observeStatus(): Flow<UserPreferences>

    suspend fun updateDataSource(status: Int)

    suspend fun clearDataSource()
}

