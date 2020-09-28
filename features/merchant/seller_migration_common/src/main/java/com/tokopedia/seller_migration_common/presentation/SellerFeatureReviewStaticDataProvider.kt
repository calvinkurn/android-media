package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureReviewStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel.TemplateReviewFeatureUiModel(),
                SellerFeatureUiModel.ReviewStatisticsFeatureUiModel()
        )
    }
}