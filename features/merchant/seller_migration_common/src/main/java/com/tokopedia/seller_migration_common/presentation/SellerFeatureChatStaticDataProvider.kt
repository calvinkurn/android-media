package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureChatStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_template_chat,
                        titleId = R.string.seller_migration_fragment_chat_tab_template_chat_title,
                        descriptionId = R.string.seller_migration_fragment_chat_tab_template_chat_description
                ),
                SellerFeatureUiModel(
                        imageId = R.drawable.ic_seller_migration_attach_voucher,
                        titleId = R.string.seller_migration_fragment_chat_tab_attach_voucher_title,
                        descriptionId = R.string.seller_migration_fragment_chat_tab_attach_voucher_description
                )
        )
    }
}