package com.tokopedia.content.product.preview.view.uimodel.product

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductUiModel(
    val productId: String,
    val content: List<ProductContentUiModel>
) : Parcelable {
    companion object {
        val Empty
            get() = ProductUiModel(
                productId = "",
                content = emptyList()
            )
    }
}
