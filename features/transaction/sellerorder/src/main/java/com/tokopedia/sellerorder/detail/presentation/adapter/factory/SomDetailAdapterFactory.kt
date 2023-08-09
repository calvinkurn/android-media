package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.order_management_common.presentation.factory.BmgmAdapterTypeFactory
import com.tokopedia.sellerorder.detail.presentation.model.DividerUiModel
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel

interface SomDetailAdapterFactory: BmgmAdapterTypeFactory {
    // for old view types
    fun type(typeLayout: String): Int

    fun type(model: ProductBundleUiModel): Int

    fun type(model: NonProductBundleUiModel): Int

    fun type(dividerUiModel: DividerUiModel): Int
}
