package com.tokopedia.atc_variant.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 13/12/18.
 */
@Parcelize
data class ProductChild(
        var productId: Int,
        var productName: String,
        var productPrice: Int,
        var productImageUrl: String,
        var isAvailable: Boolean,
        var isSelected: Boolean,
        var stockWording: String,
        var stock: Int,
        var minOrder: Int,
        var maxOrder: Int,
        var optionsId: ArrayList<Int>
) : Parcelable