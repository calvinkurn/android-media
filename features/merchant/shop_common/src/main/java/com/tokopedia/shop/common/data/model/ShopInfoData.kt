package com.tokopedia.shop.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopInfoData(
        val shopId: String,
        val name: String,
        val description: String,
        val url: String,
        val location: String,
        val imageCover: String,
        val tagLine: String,
        val isOfficial: Int,
        val isGold: Int,
        val openSince: String,
        val shipments: List<ShopShipmentData>,
        val shopSnippetUrl: String
): Parcelable