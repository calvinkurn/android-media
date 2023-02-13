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
        val shopSnippetUrl: String,
        val isGoApotik: Boolean,
        val siaNumber: String,
        val sipaNumber: String,
        val apj: String,
        val partnerLabel: String,
        val fsType: Int,
        val partnerName: String
) : Parcelable
