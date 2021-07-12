package com.tokopedia.product.addedit.productlimitation.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductLimitationModel (
        var isEligible: Boolean = false,
        var isUnlimited: Boolean = false,
        var limitAmount: Int = 0,
        var actionItems: List<ProductLimitationActionItemModel> = emptyList()
) : Parcelable

@Parcelize
data class ProductLimitationActionItemModel (
        var imageUrl: String = "",
        var title: String = "",
        var description: String = "",
        var actionText: String = "",
        var actionUrl: String = ""
) : Parcelable

