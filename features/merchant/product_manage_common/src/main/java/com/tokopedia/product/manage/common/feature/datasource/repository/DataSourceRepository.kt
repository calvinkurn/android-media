package com.tokopedia.product.manage.common.feature.datasource.repository

import com.tokopedia.product.manage.common.feature.datasource.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface DataSourceRepository {
    fun getDataSourceFlow(): Flow<UserPreferences>

    suspend fun updateDataSource(status: Int, productId: String)

    suspend fun clearDataSource()
}

