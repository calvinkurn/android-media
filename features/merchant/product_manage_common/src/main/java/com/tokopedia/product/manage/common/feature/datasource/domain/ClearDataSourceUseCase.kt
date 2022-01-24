package com.tokopedia.product.manage.common.feature.datasource.domain

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.common.feature.datasource.repository.DataSourceRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ClearDataSourceUseCase @Inject constructor(
    private val repository: DataSourceRepository
): UseCase<Unit>() {

    companion object {
        private const val STATUS = "status"

        fun createRequestParams(status: Int) = HashMap<String, Int>().apply {
            put(STATUS, status)
        }
    }

    var params = HashMap<String, Int>()

    override suspend fun executeOnBackground() {
        repository.updateDataSource(params[STATUS].orZero())
    }
}