package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureReviewStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_template_review,
                        titleId = R.string.seller_migration_fragment_review_tab_template_review_title,
                        descriptionId = R.string.seller_migration_fragment_review_tab_template_review_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_review_statistic,
                        titleId = R.string.seller_migration_fragment_review_tab_review_statistic_title,
                        descriptionId = R.string.seller_migration_fragment_review_tab_review_statistic_description
                )
        )
    }
}