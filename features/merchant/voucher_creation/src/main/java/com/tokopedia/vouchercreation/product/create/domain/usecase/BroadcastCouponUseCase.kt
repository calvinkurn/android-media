package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.product.create.domain.entity.BroadcastMetadata
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class BroadcastCouponUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase
) {

    suspend fun execute(scope: CoroutineScope, couponId : Int): BroadcastMetadata {
        val shopDataDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }
        val broadcastMetadata = scope.async { getBroadCastMetaDataUseCase.executeOnBackground() }

        val shopData = shopDataDeferred.await()
        val broadcastData = broadcastMetadata.await()

        return BroadcastMetadata(
            couponId,
            shopData.shopDomain,
            shopData.shopName,
            broadcastData.promo,
            broadcastData.status
        )
    }

}