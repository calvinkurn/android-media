package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureProductStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel.SetVariantFeatureUiModel(),
                SellerFeatureUiModel.MultiEditFeatureUiModel(),
                SellerFeatureUiModel.StockReminderFeatureUiModel(),
                SellerFeatureUiModel.FeaturedProductFeatureUiModel()
        )
    }
}