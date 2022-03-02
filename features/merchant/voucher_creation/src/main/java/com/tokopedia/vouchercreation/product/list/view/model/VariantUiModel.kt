package com.tokopedia.vouchercreation.product.list.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VariantUiModel(
        var isEditing: Boolean = false,
        var isViewing: Boolean = false,
        var isSelected: Boolean = false,
        var isSelectAll: Boolean = false,
        var isClicked: Boolean = false,
        var variantId: String = "",
        var variantName: String = "",
        var sku: String = "",
        var price: String = "",
        var priceTxt: String = "",
        var soldNStock: String = "",
        var isError: Boolean = false,
        var errorMessage: String = ""
) : Parcelable