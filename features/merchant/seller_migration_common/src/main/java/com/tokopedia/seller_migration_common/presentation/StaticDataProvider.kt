package com.tokopedia.seller_migration_common.presentation

import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel

interface StaticDataProvider {
    fun getData(): List<SellerFeatureUiModel>
}