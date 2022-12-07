package com.tokopedia.shop.common.widget.bundle.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes

data class BundleUiModel(
        var bundleName: String = "",
        var bundleType: BundleTypes = BundleTypes.MULTIPLE_BUNDLE,
        var actionButtonText: String? = null,
        var bundleDetails: List<BundleDetailUiModel> = listOf(),
        var selectedBundleId: String = "0",
        var selectedBundleApplink: String = "",
): ImpressHolder()
