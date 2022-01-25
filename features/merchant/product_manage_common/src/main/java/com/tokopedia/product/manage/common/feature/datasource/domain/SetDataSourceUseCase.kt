package com.tokopedia.product.manage.common.feature.datasource.domain

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.feature.datasource.repository.DataSourceRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SetDataSourceUseCase @Inject constructor(
    private val repository: DataSourceRepository
): UseCase<Unit>() {

    companion object {
        private const val STATUS = "status"
        private const val PRODUCT_ID = "product_id"

        fun createRequestParams(status: Int, productId: String) = HashMap<String, String>().apply {
            put(STATUS, status.toString())
            put(PRODUCT_ID, productId)
        }
    }

    var params = HashMap<String, String>()

    override suspend fun executeOnBackground() {
        repository.updateDataSource(
            status = params[STATUS].toIntOrZero(),
            productId = params[PRODUCT_ID].orEmpty()
        )
    }
}
