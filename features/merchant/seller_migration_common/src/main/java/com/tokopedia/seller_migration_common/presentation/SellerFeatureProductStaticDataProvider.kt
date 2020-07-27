package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureProductStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_set_variant,
                        titleId = R.string.seller_migration_fragment_product_tab_set_variant_title,
                        descriptionId = R.string.seller_migration_fragment_product_tab_set_variant_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_multi_edit,
                        titleId = R.string.seller_migration_fragment_product_tab_multi_edit_title,
                        descriptionId = R.string.seller_migration_fragment_product_tab_multi_edit_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_import_instagram,
                        titleId = R.string.seller_migration_fragment_product_tab_instagram_import_title,
                        descriptionId = R.string.seller_migration_fragment_product_tab_instagram_import_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_featured_product,
                        titleId = R.string.seller_migration_fragment_product_tab_featured_product_title,
                        descriptionId = R.string.seller_migration_fragment_product_tab_featured_product_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_voucher,
                        titleId = R.string.seller_migration_fragment_product_tab_set_cashback_title,
                        descriptionId = R.string.seller_migration_fragment_product_tab_set_cashback_description
                )
        )
    }
}