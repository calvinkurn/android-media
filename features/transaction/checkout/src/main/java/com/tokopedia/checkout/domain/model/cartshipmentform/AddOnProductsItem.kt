package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductsItem(
   var productImageUrl: String = "",
   var productName: String = ""
): Parcelable
