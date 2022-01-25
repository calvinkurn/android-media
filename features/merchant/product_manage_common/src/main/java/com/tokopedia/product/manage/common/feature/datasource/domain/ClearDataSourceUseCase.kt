package com.tokopedia.product.manage.common.feature.datasource.domain

import com.tokopedia.product.manage.common.feature.datasource.repository.DataSourceRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ClearDataSourceUseCase @Inject constructor(
    private val repository: DataSourceRepository
): UseCase<Unit>() {

    override suspend fun executeOnBackground() {
        repository.clearDataSource()
    }
}