package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureAdsPromoStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_attach_voucher,
                        titleId = R.string.seller_migration_fragment_ads_and_promo_tab_shop_voucher_title,
                        descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_shop_voucher_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_topads,
                        titleId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_title,
                        descriptionId = R.string.seller_migration_fragment_ads_and_promo_tab_topads_description
                )
        )
    }
}