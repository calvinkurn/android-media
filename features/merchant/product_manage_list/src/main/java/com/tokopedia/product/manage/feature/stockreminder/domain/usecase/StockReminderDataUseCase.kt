package com.tokopedia.product.manage.feature.stockreminder.domain.usecase

import javax.inject.Inject

class StockReminderDataUseCase @Inject constructor(private val getStockReminderDataUseCase: GetStockReminderDataUseCase,
                                                   private val createStockReminderDataUseCase: CreateStockReminderDataUseCase,
                                                   private val updateStockReminderDataUseCase: UpdateStockReminderDataUseCase) {

    fun setGetStockParams(productId: String) = getStockReminderDataUseCase.setParams(productId)

    fun setCreateStockParams(shopId: String, productId: String, warehouseId: String, threshold: String) = createStockReminderDataUseCase.setParams(shopId, productId, warehouseId, threshold)

    fun setUpdateStockParams(shopId: String, productId: String, warehouseId: String, threshold: String) = updateStockReminderDataUseCase.setParams(shopId, productId, warehouseId, threshold)

    suspend fun executeGetStockReminder() = getStockReminderDataUseCase.executeOnBackground()

    suspend fun executeCreateStockReminder() = createStockReminderDataUseCase.executeOnBackground()

    suspend fun executeUpdateStockReminder() = updateStockReminderDataUseCase.executeOnBackground()
}