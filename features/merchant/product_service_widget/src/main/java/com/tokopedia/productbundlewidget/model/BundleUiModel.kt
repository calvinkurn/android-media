package com.tokopedia.productbundlewidget.model

import com.tokopedia.kotlin.model.ImpressHolder

data class BundleUiModel(
        var bundleName: String = "",
        var bundleType: BundleTypes = BundleTypes.MULTIPLE_BUNDLE,
        var actionButtonText: String? = null,
        var bundleDetails: List<BundleDetailUiModel> = listOf(),
        var selectedBundleId: String = "0",
        var selectedBundleApplink: String = "",
): ImpressHolder()
