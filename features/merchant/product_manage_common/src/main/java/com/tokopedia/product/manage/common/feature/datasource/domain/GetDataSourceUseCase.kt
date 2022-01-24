package com.tokopedia.product.manage.common.feature.datasource.domain

import com.tokopedia.product.manage.common.feature.datasource.model.UserPreferences
import com.tokopedia.product.manage.common.feature.datasource.repository.DataSourceRepository
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataSourceUseCase @Inject constructor(
    private val repository: DataSourceRepository
): UseCase<Flow<UserPreferences>>() {

    override suspend fun executeOnBackground(): Flow<UserPreferences> {
        return repository.observeStatus()
    }
}
