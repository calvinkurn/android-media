package com.tokopedia.product.detail.data.model.datamodel

sealed class ProductMiniSocialProofTokoNowItemType {
    object ProductMiniSocialProofChip : ProductMiniSocialProofTokoNowItemType()
    object ProductMiniSocialProofText : ProductMiniSocialProofTokoNowItemType()
    object ProductMiniSocialProofTextWithDivider : ProductMiniSocialProofTokoNowItemType()
    object ProductMiniSocialProofSingleText : ProductMiniSocialProofTokoNowItemType()
}
