package com.tokopedia.vouchercreation.product.list.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductUiModel(
        var isEnabled:Boolean = true,
        var isSelectAll: Boolean = false,
        var isSelected: Boolean = false,
        var isVariantSelected: Boolean = false,
        var isError: Boolean = false,
        var errorMessage: String = "",
        var imageUrl: String = "",
        var id: String = "",
        var productName: String = "",
        var sku: String = "",
        var price: String = "",
        var sold: Int = 0,
        var soldNStock: String = "",
        var isVariantHeaderExpanded: Boolean = false,
        var hasVariant: Boolean = false,
        var variants: List<VariantUiModel> = listOf()
) : Parcelable {
    fun getSelectedVariants(): List<VariantUiModel> {
        return variants.filter { it.isSelected }
    }
}