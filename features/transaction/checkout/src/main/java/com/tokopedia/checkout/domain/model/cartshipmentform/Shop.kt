package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
        var shopId: Long = 0,
        var userId: String = "",
        var shopName: String = "",
        var shopImage: String = "",
        var shopUrl: String = "",
        var shopStatus: Int = 0,
        var isGold: Boolean = false,
        var isGoldBadge: Boolean = false,
        var isOfficial: Boolean = false,
        var isFreeReturns: Boolean = false,
        var shopBadge: String = "",
        var addressId: String = "",
        var postalCode: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var districtId: String = "",
        var districtName: String = "",
        var origin: Int = 0,
        var addressStreet: String = "",
        var provinceId: String = "",
        var cityId: String = "",
        var cityName: String = "",
        var shopAlertMessage: String = ""
) : Parcelable