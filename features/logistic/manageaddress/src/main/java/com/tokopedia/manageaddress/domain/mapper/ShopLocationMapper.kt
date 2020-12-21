package com.tokopedia.manageaddress.domain.mapper

import com.tokopedia.logisticCommon.data.entity.shoplocation.PartnerId
import com.tokopedia.logisticCommon.data.entity.shoplocation.ShopId
import com.tokopedia.logisticCommon.data.entity.shoplocation.Ticker
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.logisticCommon.data.response.shoplocation.GetShopLocationResponse
import javax.inject.Inject

class ShopLocationMapper @Inject constructor(){

    fun mapShopLocation(response: GetShopLocationResponse) : List<Warehouse> {
        val data = response.shopLocations.data.warehouse
        return data.map {
            Warehouse(
                    it.warehouseId,
                    it.warehouseName,
                    it.warehouseType,
                    mapShopId(it.shopId),
                    mapPartnerId(it.partnerId),
                    it.addressDetail,
                    it.postalCode,
                    it.latLon,
                    it.districtId,
                    it.districtName,
                    it.cityId,
                    it.cityName,
                    it.provinceId,
                    it.provinceName,
                    it.country,
                    it.status,
                    it.isCoveredByCouriers,
                    mapTicker(it.ticker)
            )
        }
    }

    private fun mapShopId(response: com.tokopedia.logisticCommon.data.response.shoplocation.ShopId) : ShopId {
        return ShopId().apply {
            int64 = response.int64
            valid = response.valid
        }
    }

    private fun mapPartnerId(response: com.tokopedia.logisticCommon.data.response.shoplocation.PartnerId) : PartnerId {
        return PartnerId().apply {
            int64 = response.int64
            valid = response.valid
        }
    }

    private fun mapTicker(response: com.tokopedia.logisticCommon.data.response.shoplocation.Ticker) : Ticker {
        return Ticker().apply {
            textInactive = response.textInactive
            textCourierSetting = response.textCourierSetting
            linkCourierSetting = response.linkCourierSetting
        }
    }
}


