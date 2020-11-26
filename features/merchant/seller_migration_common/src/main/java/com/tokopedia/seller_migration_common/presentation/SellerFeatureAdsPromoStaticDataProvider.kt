package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureAdsPromoStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel.VoucherCashbackFeatureUiModel(),
                SellerFeatureUiModel.TopAdsFeatureUiModel(),
                SellerFeatureUiModel.BroadcastChatUiModel()
        )
    }
}