package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupShopV2(
    var cartStringOrder: String = "",
    var shop: Shop = Shop(),
    var products: List<Product> = emptyList()
) : Parcelable
