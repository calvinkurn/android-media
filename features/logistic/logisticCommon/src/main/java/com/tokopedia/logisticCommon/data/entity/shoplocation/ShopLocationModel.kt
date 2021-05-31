package com.tokopedia.logisticCommon.data.entity.shoplocation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class ShopLocationModel (
        var generalTicker: GeneralTickerModel = GeneralTickerModel(),
        var listWarehouse: List<Warehouse> = emptyList()
)

data class GeneralTickerModel(
        var header: String = "",
        var body: String = "",
        var bodyLinkText: String = "",
        var bodyLinkUrl: String = ""
)

@Parcelize
data class Warehouse(
        var warehouseId: Int = 0,
        var warehouseName: String = "",
        var warehouseType: Int = 0,
        var shopId: ShopId = ShopId(),
        var partnerId: PartnerId = PartnerId(),
        var addressDetail: String = "",
        var postalCode: String = "",
        var latLon: String = "",
        var districtId: Int = 0,
        var districtName: String = "",
        var cityId: Int = 0,
        var cityName: String = "",
        var provinceId: Int = 0,
        var provinceName: String = "",
        var country: String = "",
        var status: Int = 0,
        var isCoveredByCouriers: Boolean = false,
        var ticker: Ticker = Ticker(),
        var zipCodes: List<String> = ArrayList()
) : Parcelable

@Parcelize
data class ShopId(
        var int64: Long = 0,
        var valid: Boolean = false
) : Parcelable

@Parcelize
data class PartnerId(
        var int64: Long = 0,
        var valid: Boolean = false
) : Parcelable

@Parcelize
data class Ticker(
        var textInactive: String = "",
        var textCourierSetting: String = "",
        var linkCourierSetting: String = ""
) : Parcelable