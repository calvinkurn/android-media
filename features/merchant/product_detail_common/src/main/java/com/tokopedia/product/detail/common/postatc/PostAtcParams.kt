package com.tokopedia.product.detail.common.postatc

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostAtcParams(
    val cartId: String = "",
    val layoutId: String = "",
    val pageSource: String = "",
    val session: String = "",
    val addons: Addons? = null
) : Parcelable {

    @Parcelize
    data class Addons(
        val deselectedAddonsIds: List<String>,
        @Deprecated("Please use `isFulfillment` from PostAtcInfo")
        val isFulfillment: Boolean,
        val selectedAddonsIds: List<String>,
        @Deprecated("Please use `warehouseId` from PostAtcInfo.warehouseInfo")
        val warehouseId: String,
        val quantity: Int
    ) : Parcelable

    @Parcelize
    sealed class Source(
        open val name: String
    ) : Parcelable {
        object PDP : Source("product detail page")
        object Default : Source("")
    }
}
