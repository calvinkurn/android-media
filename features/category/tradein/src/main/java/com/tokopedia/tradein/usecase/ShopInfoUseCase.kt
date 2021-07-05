package com.tokopedia.tradein.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tradein.TradeinConstants.UseCase.KEY_FIELDS
import com.tokopedia.tradein.TradeinConstants.UseCase.KEY_SHOP_IDS
import com.tokopedia.tradein.TradeinConstants.UseCase.SHIPMENT
import com.tokopedia.tradein.model.ShopInfoDataModel
import com.tokopedia.tradein.raw.GQL_GET_SHOP_INFO
import com.tokopedia.tradein.repository.TradeInRepository
import javax.inject.Inject

@GqlQuery("GqlGetShopInfo",GQL_GET_SHOP_INFO)
class ShopInfoUseCase @Inject constructor(
        private val repository: TradeInRepository) {

    suspend fun getShopInfo(shopID: Int): ShopInfoDataModel {
        return repository.getGQLData(GqlGetShopInfo.GQL_QUERY, ShopInfoDataModel::class.java, createRequestParams(shopID))
    }

    fun createRequestParams(shopID: Int): Map<String, Any> {
        return mapOf(KEY_SHOP_IDS to shopID,
                KEY_FIELDS to SHIPMENT)
    }
}