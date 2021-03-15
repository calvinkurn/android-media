package com.tokopedia.editshipping.domain.mapper

import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.logisticCommon.data.response.shippingeditor.*
import javax.inject.Inject

class ShippingEditorMapper @Inject constructor() {

    fun mapShipperList(response: GetShipperListResponse): ShipperListModel {
        return ShipperListModel().apply {
             shippers = mapShipper(response.ongkirShippingEditor.data)
             ticker = mapTickerShipperList(response)
        }
    }

    fun mapShipperTickerList(response: GetShipperTickerResponse): ShipperTickerModel {
        val data = response.ongkirShippingEditorGetShipperTicker.data
        return ShipperTickerModel().apply {
            headerTicker = mapHeaderTicker(data.headerTicker, data.warehouses)
            courierTicker = mapCourierTicker(data.courierTicker, data.warehouses)
        }
    }

    private fun mapShipper(response: Data): ShippersModel {
        return ShippersModel().apply {
            onDemand = mapShipperOnDemand(response.shippers.onDemand)
            conventional = mapShipperConventional(response.shippers.conventional)
        }
    }

    private fun mapShipperOnDemand(response: List<OnDemand>): List<OnDemandModel> {
        val onDemandModelList =  ArrayList<OnDemandModel>()
        response.forEach { data ->
            val onDemandUiModel = OnDemandModel().apply {
                shipperId = data.shipperId
                shipperName = data.shipperName
                isActive = data.isActive
                textPromo = data.textPromo
                image = data.image
                featureInfo = mapFeatureInfo(data.featureInfo)
                shipperProduct = mapShipperProduct(data.shipperProduct)
            }
            onDemandModelList.add(onDemandUiModel)
        }
        return onDemandModelList
    }

    private fun mapShipperConventional(response: List<Conventional>): List<ConventionalModel> {
        val conventionalModelList = ArrayList<ConventionalModel>()
        response.forEach { data ->
            val conventionalUiModel = ConventionalModel().apply {
                shipperId = data.shipperId
                shipperName = data.shipperName
                isActive = data.isActive
                textPromo = data.textPromo
                image = data.image
                featureInfo = mapFeatureInfo(data.featureInfo)
                shipperProduct = mapShipperProduct(data.shipperProduct)
            }
            conventionalModelList.add(conventionalUiModel)
        }
        return conventionalModelList
    }

    private fun mapFeatureInfo(response: List<FeatureInfo>): List<FeatureInfoModel> {
       return response.map {
           FeatureInfoModel(
                   it.header,
                   it.body
           )
       }
    }

    private fun mapShipperProduct(response: List<ShipperProduct>): List<ShipperProductModel> {
        return response.map {
            ShipperProductModel(
                    it.shipperProductId,
                    it.shipperProductName,
                    it.isActive
            )
        }
    }

    private fun mapTickerShipperList(response: GetShipperListResponse): List<TickerModel> {
        val data = response.ongkirShippingEditor.data.ticker
        return data.map {
            TickerModel(
                    it.header,
                    it.body,
                    it.textLink,
                    it.urlLink
            )
        }
    }

    private fun mapHeaderTicker(response: HeaderTicker, warehouses: List<Warehouses>): HeaderTickerModel {
        return HeaderTickerModel().apply {
            header = response.header
            body = response.body
            textLink = response.textLink
            urlLink = response.urlLink
            warehouseModel = mapWarehouseModelBasedOnWarehouseId(response.warehouseIds, warehouses)
            isActive = response.isActive
        }
    }

    private fun mapCourierTicker(response: List<CourierTicker>, warehouses: List<Warehouses>): List<CourierTickerModel> {
        return response.map {
            CourierTickerModel(
                    it.shipperId,
                    mapWarehouseModelBasedOnWarehouseId(it.warehouseIds?: emptyList(), warehouses),
                    it.tickerState,
                    it.isAvailable,
                    mapShipperProductTicker(it.shipperProduct)
            )
        }
    }

    private fun mapWarehouseModelBasedOnWarehouseId(response: List<Int>, warehouses: List<Warehouses>): List<WarehousesModel> {
        return warehouses.filter {
            response.any { id ->
                it.warehouseId == id
            }
        }.map {
            mapWarehousesTicker(it)
        }
    }

    private fun mapShipperProductTicker(response: List<ShipperProductTicker>): List<ShipperProductTickerModel> {
        val shipperProductTickerList = ArrayList<ShipperProductTickerModel>()
        response.forEach { data ->
            val shipperProductTickerUiModel = ShipperProductTickerModel().apply {
                shipperProductId = data.shipperProductId
                isAvailable = data.isAvailable
            }
            shipperProductTickerList.add(shipperProductTickerUiModel)
        }
        return shipperProductTickerList
    }

    private fun mapWarehousesTicker(it: Warehouses) : WarehousesModel {
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