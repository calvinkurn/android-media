package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel

/**
 * Created By @ilhamsuaib on 05/07/21
 */

interface ProductAdapterFactory {

    fun type(model: ProductBundleUiModel): Int

    fun type(model: NonProductBundleUiModel): Int
}