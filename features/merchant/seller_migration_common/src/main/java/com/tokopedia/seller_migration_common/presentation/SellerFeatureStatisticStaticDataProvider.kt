package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureStatisticStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_insight,
                        titleId = R.string.seller_migration_fragment_statistic_tab_shop_insight_title,
                        descriptionId = R.string.seller_migration_fragment_statistic_tab_shop_insight_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_insight,
                        titleId = R.string.seller_migration_fragment_statistic_tab_market_insight_title,
                        descriptionId = R.string.seller_migration_fragment_statistic_tab_market_insight_description
                )
        )
    }
}