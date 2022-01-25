package com.tokopedia.product.manage.common.feature.datasource.repository

import com.tokopedia.product.manage.common.feature.datasource.LocalDataSource
import com.tokopedia.product.manage.common.feature.datasource.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataSourceRepositoryImpl @Inject constructor(
    private val dataSource: LocalDataSource
) : DataSourceRepository {
    override fun getDataSourceFlow(): Flow<UserPreferences> {
        return dataSource.getDataSourceFlow()
    }

    override suspend fun updateDataSource(status: Int, productId: String) {
        dataSource.updateDataSource(status, productId)
    }

    override suspend fun clearDataSource() {
        dataSource.clearDataStore()
    }
}
