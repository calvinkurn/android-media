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
            headerLocation = response.headerLocation
            ticker = mapTickerContent(response.ticker)
            warehouses = mapWarehouseModelBasedOnWarehouseId(response.warehouseId, response.warehouses)
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

    private fun mapWarehouseModelBasedOnWarehouseId(response: List<Int>, warehouses: List<Warehouses>): List<WarehousesModel> {
        return warehouses.filter {
            response.any { id ->
                it.warehouseId == id
            }
        }.map {
            mapWarehouses(it)
        }
    }

    private fun mapWarehouses(it: Warehouses): WarehousesModel {
        return WarehousesModel(
                    it.warehouseId,
                    it.warehouseName,
                    it.districtId,
                    it.districtName,
                    it.cityId,
                    it.cityName,
                    it.provinceId,
                    it.provinceName,
                    it.status,
                    it.postalCode,
                    it.isDefault,
                    it.latLon,
                    it.latitude,
                    it.longitude,
                    it.addressDetail,
                    it.country,
                    it.isFulfillment,
                    it.warehouseType,
                    it.email,
                    mapShopId(it.shopId),
                    mapPartnerId(it.partnerId)
            )
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