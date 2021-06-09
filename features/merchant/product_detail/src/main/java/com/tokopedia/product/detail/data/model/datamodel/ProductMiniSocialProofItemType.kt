package com.tokopedia.product.detail.data.model.datamodel

sealed class ProductMiniSocialProofItemType {
    object ProductMiniSocialProofChip: ProductMiniSocialProofItemType()
    object ProductMiniSocialProofText: ProductMiniSocialProofItemType()
    object ProductMiniSocialProofSingleText: ProductMiniSocialProofItemType()
    object ProductMiniSocialProofTextDivider: ProductMiniSocialProofItemType()
}