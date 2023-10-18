package com.tokopedia.minicart.common.widget.viewmodel.utils

import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel

object ProductUtils {

    fun MiniCartListUiModel.getBundleProductList(bundleId: String): List<MiniCartProductUiModel> {
        return visitables.filterIsInstance<MiniCartProductUiModel>()
            .filter { it.bundleId == bundleId }
    }
}
