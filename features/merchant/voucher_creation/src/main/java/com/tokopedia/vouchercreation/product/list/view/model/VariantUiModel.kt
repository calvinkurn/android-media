package com.tokopedia.vouchercreation.product.list.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VariantUiModel(
        var isSelected: Boolean = false,
        var isClick: Boolean = true,
        var variantId: String = "",
        var variantName: String = "",
        var sku: String = "",
        var price: String = "",
        var priceTxt: String = "",
        var soldNStock: String = "",
        var isError: Boolean = false,
        var errorMessage: String = ""
) : Parcelable