package com.tokopedia.addon.presentation.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnParam(
    val productId: String = "",
    val warehouseId: String = "",
    val isTokocabang: Boolean = false,
    val categoryID: String = "",
    val shopID: String = "",
    val quantity: Long = 0,
    val price: Long = 0,
    val discountedPrice: Long = 0,
    val condition: String = ""
) : Parcelable
