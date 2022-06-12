package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.query.param.ProductWarehouseParam
import javax.inject.Inject

class StockReminderDataUseCase @Inject constructor(private val getStockReminderDataUseCase: GetStockReminderDataUseCase,
                                                   private val createStockReminderDataUseCase: CreateStockReminderDataUseCase,
                                                   private val updateStockReminderDataUseCase: UpdateStockReminderDataUseCase) {

    fun setGetStockParams(productId: String) = getStockReminderDataUseCase.setParams(productId)

    fun setCreateStockParams(shopId: String, listProductWarehouseParam : ArrayList<ProductWarehouseParam>) = createStockReminderDataUseCase.setParams(shopId, listProductWarehouseParam)

    fun setUpdateStockParams(shopId: String, listProductWarehouseParam : ArrayList<ProductWarehouseParam>) = updateStockReminderDataUseCase.setParams(shopId, listProductWarehouseParam)

    suspend fun executeGetStockReminder() = getStockReminderDataUseCase.executeOnBackground()

    suspend fun executeCreateStockReminder() = createStockReminderDataUseCase.executeOnBackground()

    suspend fun executeUpdateStockReminder() = updateStockReminderDataUseCase.executeOnBackground()
}