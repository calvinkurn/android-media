package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.product.create.domain.entity.ShareMetadata
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetShareMetadataFacadeUseCase @Inject constructor(
    private val getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase,
    private val getShopBasicDataUseCase: ShopBasicDataUseCase
) {

    suspend fun execute(scope: CoroutineScope): ShareMetadata {
        val broadCastMetaDataDeferred =
            scope.async { getBroadCastMetaDataUseCase.executeOnBackground() }
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }

        val broadcastMetadata = broadCastMetaDataDeferred.await()
        val shop = shopDeferred.await()

        return ShareMetadata(broadcastMetadata.promo, broadcastMetadata.status, shop.shopName)
    }
}