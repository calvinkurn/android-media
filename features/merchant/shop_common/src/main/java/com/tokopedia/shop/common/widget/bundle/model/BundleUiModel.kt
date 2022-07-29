package com.tokopedia.shop.common.widget.bundle.model

import com.tokopedia.kotlin.model.ImpressHolder

data class BundleUiModel(
        var bundleName: String = "",
        var bundleType: String = "",
        var bundleDetails: List<BundleDetailUiModel> = listOf()
): ImpressHolder()