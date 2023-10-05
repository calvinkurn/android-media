package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticcart.shipping.model.ShopTypeInfoData
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(
    var shopId: Long = 0,
    var shopName: String = "",
    var postalCode: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var districtId: String = "",
    var shopAlertMessage: String = "",
    var shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
    var isTokoNow: Boolean = false,
    var shopTickerTitle: String = "",
    var shopTicker: String = "",
    var enablerLabel: String = ""
) : Parcelable
