package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.product.manage.common.feature.variant.data.model.response.GetProductVariantResponse
import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import javax.inject.Inject

class StockReminderDataUseCase @Inject constructor(
    private val getProductUseCase: GetProductStockReminderUseCase,
    private val getStockReminderDataUseCase: GetStockReminderDataUseCase,
    private val createStockReminderDataUseCase: CreateStockReminderDataUseCase
) {

    fun setGetStockParams(productId: String) = getStockReminderDataUseCase.setParams(productId)

    fun setCreateStockParams(
        shopId: String,
        listProductWarehouseParam: ArrayList<ProductWarehouseParam>
    ) = createStockReminderDataUseCase.setParams(shopId, listProductWarehouseParam)

    suspend fun executeGetStockReminder() = getStockReminderDataUseCase.executeOnBackground()

    suspend fun executeCreateStockReminder() = createStockReminderDataUseCase.executeOnBackground()

    suspend fun executeGetProductStockReminder(
        productId: String,
        warehouseId: String
    ): GetProductVariantResponse {
        val requestParams =
            GetProductStockReminderUseCase.createRequestParams(productId, false, warehouseId)
        return getProductUseCase.execute(requestParams)
    }

}
