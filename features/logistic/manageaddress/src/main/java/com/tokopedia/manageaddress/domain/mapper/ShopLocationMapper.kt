package com.tokopedia.manageaddress.domain.mapper

import com.tokopedia.logisticCommon.data.entity.shoplocation.*
import com.tokopedia.logisticCommon.data.response.shoplocation.GeneralTicker
import com.tokopedia.logisticCommon.data.response.shoplocation.GetShopLocationResponse
import javax.inject.Inject

class ShopLocationMapper @Inject constructor(){

    fun mapLocationResponse(response: GetShopLocationResponse): ShopLocationModel {
        return ShopLocationModel().apply {
            generalTicker = mapGeneralTicker(response.shopLocations.data.generalTicker)
            listWarehouse = mapWarehouses(response)
        }
    }

    private fun mapGeneralTicker(response: GeneralTicker): GeneralTickerModel {
        return GeneralTickerModel().apply {
            header = response.header
            body = response.body
            bodyLinkText = response.bodyLinkText
            bodyLinkUrl = response.bodyLinkUrl
        }
    }

    private fun mapWarehouses(response: GetShopLocationResponse) : List<Warehouse> {
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


