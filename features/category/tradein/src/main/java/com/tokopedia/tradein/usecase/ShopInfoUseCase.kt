package com.tokopedia.tradein.usecase

import com.tokopedia.tradein.model.ShopInfoDataModel
import com.tokopedia.tradein.raw.GQL_GET_SHOP_INFO
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

class ShopInfoUseCase @Inject constructor(
        private val repository: TradeInRepository) {

    suspend fun getShopInfo(shopID: Int): ShopInfoDataModel {
        return repository.getGQLData(GQL_GET_SHOP_INFO, ShopInfoDataModel::class.java, createRequestParams(shopID))
    }

    fun createRequestParams(shopID: Int): Map<String, Any> {
        return mapOf("shopIds" to shopID,
                "fields" to "shipment")
    }
}