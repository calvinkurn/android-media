package com.tokopedia.attachcommon.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Hendri on 19/02/18.
 */
@Parcelize
class ResultProduct constructor(
    val productId: String = "",
    val productUrl: String = "",
    val productImageThumbnail: String = "",
    val price: String = "",
    val name: String = "",
    val priceBefore: String = "",
    val dropPercentage: String = "",
    val isFreeOngkirActive: Boolean = false,
    val imgUrlFreeOngkir: String = "",
    val stock: Int = 1,
    val isSupportVariant: Boolean = false,
    val campaignId: Long = 0,
    val isPreorder: Boolean = false,
    val priceInt: Long = 0,
    val categoryId: Long = 0
) : Parcelable