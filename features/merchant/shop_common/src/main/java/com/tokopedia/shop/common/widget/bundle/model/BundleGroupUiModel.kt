package com.tokopedia.shop.common.widget.bundle.model

import com.tokopedia.kotlin.model.ImpressHolder

data class BundleGroupUiModel(
        var groupName: String = "",
        var shopInfo: BundleShopUiModel? = null,
        var bundles: List<ShopHomeProductBundleDetailUiModel> = listOf()
): ImpressHolder()