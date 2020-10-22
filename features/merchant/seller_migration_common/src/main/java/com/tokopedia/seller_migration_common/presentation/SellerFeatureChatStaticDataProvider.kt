package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureChatStaticDataProvider : StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel.TemplateChatFeatureUiModel(),
                SellerFeatureUiModel.AttachVoucherFeatureUiModel(),
                SellerFeatureUiModel.AutoReplyFeatureUiModel()
        )
    }
}