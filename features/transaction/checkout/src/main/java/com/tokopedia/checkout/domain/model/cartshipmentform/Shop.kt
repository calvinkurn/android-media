package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
        var shopId: Int = 0,
        var userId: Int = 0,
        var shopName: String = "",
        var shopImage: String = "",
        var shopUrl: String = "",
        var shopStatus: Int = 0,
        var isGold: Boolean = false,
        var isGoldBadge: Boolean = false,
        var isOfficial: Boolean = false,
        var isFreeReturns: Boolean = false,
        var shopBadge: String = "",
        var addressId: Int = 0,
        var postalCode: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var districtId: Int = 0,
        var districtName: String = "",
        var origin: Int = 0,
        var addressStreet: String = "",
        var provinceId: Int = 0,
        var cityId: Int = 0,
        var cityName: String = "",
        var shopAlertMessage: String = ""
) : Parcelable