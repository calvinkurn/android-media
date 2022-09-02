package com.tokopedia.tokofood.common.minicartwidget.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
        var id: String = "",
        var price: Long = 0L,
        var quantity: Int = 0,
        var minQuantity: Int = 0,
        var maxQuantity: Int = 0
): Parcelable