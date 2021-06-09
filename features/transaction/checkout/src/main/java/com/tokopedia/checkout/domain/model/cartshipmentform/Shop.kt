package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.ShopTypeInfoData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
        var shopId: Long = 0,
        var shopName: String = "",
        var shopImage: String = "",
        var shopUrl: String = "",
        var shopStatus: Int = 0,
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
        var shopAlertMessage: String = "",
        var shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData()
) : Parcelable