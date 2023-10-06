package com.tokopedia.product.detail.view.viewmodel.product_detail

import androidx.lifecycle.LiveData
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel

/**
 * Created by yovi.putra on 06/09/23"
 * Project name: tokopedia-app-wg
 **/

interface IThumbnailVariantSubViewModel {

    val onThumbnailVariantSelectedData: LiveData<ProductSingleVariantDataModel?>

    fun onThumbnailVariantSelected(
        uiData: ProductSingleVariantDataModel?,
        variantId: String,
        categoryKey: String
    )
}
