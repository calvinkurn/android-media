package com.tokopedia.reviewseller.feature.reviewreply.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductReplyUiModel(
        var productID: Int? = 0,
        var productImageUrl: String? = "",
        var productName: String? = "",
        var variantName: String? = ""
): Parcelable