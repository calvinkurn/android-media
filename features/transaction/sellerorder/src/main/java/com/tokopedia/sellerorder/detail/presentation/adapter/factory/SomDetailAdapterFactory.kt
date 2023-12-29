package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.sellerorder.detail.presentation.model.DividerUiModel
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel

interface SomDetailAdapterFactory {
    // for old view types
    fun type(typeLayout: String): Int

    fun type(model: ProductBundleUiModel): Int

    fun type(model: NonProductBundleUiModel): Int

    fun type(dividerUiModel: DividerUiModel): Int
}
