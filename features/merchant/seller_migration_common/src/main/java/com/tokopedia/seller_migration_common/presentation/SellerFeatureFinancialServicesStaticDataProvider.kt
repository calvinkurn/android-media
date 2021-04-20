package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

object SellerFeatureFinancialServicesStaticDataProvider: StaticDataProvider {
    override fun getData(): List<SellerFeatureUiModel> {
        return listOf(
                SellerFeatureUiModel.ShopCapitalDataUiModel(),
                SellerFeatureUiModel.PriorityBalanceDataUiModel()
        )
    }
}