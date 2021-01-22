package com.tokopedia.editshipping.domain.mapper

import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.logisticCommon.data.response.shippingeditor.*
import javax.inject.Inject

class ValidateShippingNewMapper @Inject constructor() {

    fun mapShippingEditorData(response: DataShippingEditorPopup): ValidateShippingEditorModel {
        return ValidateShippingEditorModel().apply {
            state = response.state
            uiContent = mapUiContent(response.uiContent)
            featureId = response.featureId
        }
    }

    private fun mapUiContent(response: UiContent): UiContentModel {
        return UiContentModel().apply {
            header = response.header
            body = response.body
            ticker = mapTickerContent(response.ticker)
            warehouses = mapWarehouses(response.warehouses)
            warehouseId = response.warehouseId
        }
    }

    private fun mapTickerContent(response: TickerContent): TickerContentModel {
        return TickerContentModel().apply {
            header = response.header
            body = response.body
            textLink = response.textLink
            urlLink = response.urlLink
        }
    }

    private fun mapWarehouses(response: Warehouses): WarehousesModel {
        return WarehousesModel().apply {
            warehouseId = response.warehouseId
            warehouseName = response.warehouseName
            districtId = response.districtId
            districtName = response.districtName
            cityId = response.cityId
            cityName = response.cityName
            provinceId = response.provinceId
            provinceName = response.provinceName
            status = response.status
            postalCode = response.postalCode
            isDefault = response.isDefault
            latLon = response.latLon
            latitude = response.latitude
            longitude = response.longitude
            addressDetail = response.addressDetail
            country = response.country
            isFulfillment = response.isFulfillment
            warehouseType = response.warehouseType
            email = response.email
            shopId = mapShopId(response.shopId)
            partnerId = mapPartnerId(response.partnerId)
        }
    }

    private fun mapShopId(response: ShopId) : ShopIdModel {
        return ShopIdModel().apply {
            int64 = response.int64
            valid = response.valid
        }
    }

    private fun mapPartnerId(response: PartnerId) : PartnerIdModel {
        return PartnerIdModel().apply {
            int64 = response.int64
            valid = response.valid
        }
    }

}