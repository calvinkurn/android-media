package com.tokopedia.review.feature.reviewreply.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductReplyUiModel(
    var productID: String = "",
    var productImageUrl: String? = "",
    var productName: String? = "",
    var variantName: String? = ""
): Parcelable