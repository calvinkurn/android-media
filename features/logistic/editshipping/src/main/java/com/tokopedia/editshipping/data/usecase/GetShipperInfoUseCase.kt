package com.tokopedia.editshipping.data.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editshipping.domain.mapper.ShippingEditorMapper
import com.tokopedia.editshipping.domain.model.shippingEditor.CourierTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperListModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperTickerModel
import com.tokopedia.editshipping.domain.param.ShippingEditorParam
import com.tokopedia.editshipping.domain.param.ShippingEditorShopMultiLocInput
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetShipperInfoUseCase @Inject constructor(
    private val getShipperListUseCase: GetShipperListUseCase,
    private val getShipperTickerUseCase: GetShipperTickerUseCase,
    private val mapper: ShippingEditorMapper,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<Long, ShipperListModel>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: Long): ShipperListModel {
        return coroutineScope {
            val param = ShippingEditorParam(input = ShippingEditorShopMultiLocInput(params))
            val shipperList = async { getShipperListUseCase(param) }
            val shipperTicker = async { getShipperTickerUseCase(param) }
            val shipperModel = mapper.mapShipperList(shipperList.await())
            val tickerModel = mapper.mapShipperTickerList(shipperTicker.await())
            return@coroutineScope combineTickerAndShipperList(shipperModel, tickerModel)
        }
    }

    private fun combineTickerAndShipperList(
        shipperList: ShipperListModel,
        tickerModel: ShipperTickerModel
    ): ShipperListModel {
        tickerModel.courierTicker.forEach { tickerShipper ->
            shipperList.findShipperById(tickerShipper.shipperId)?.run {
                setTickerDataToShipper(tickerShipper)
            }
        }
        shipperList.tickerHeader = tickerModel.headerTicker
        return shipperList
    }

    private fun ShipperListModel.findShipperById(shipperId: Long): ShipperModel? {
        return shippers.conventional.find { shipper -> shipperId == shipper.shipperId }
            ?: shippers.onDemand.find { shipper -> shipperId == shipper.shipperId }
    }

    private fun ShipperModel.setTickerDataToShipper(tickerShipper: CourierTickerModel) {
        tickerState = tickerShipper.tickerState
        isAvailable = tickerState != EditShippingConstant.TICKER_STATE_UNAVAILABLE
        warehouseModel = tickerShipper.warehouses
        if (!isAvailable) {
            isActive = false
        }
        setTickerDataToShipperProduct(tickerShipper.shipperProduct)
    }

    private fun ShipperModel.setTickerDataToShipperProduct(
        tickerShipperProducts: List<ShipperProductTickerModel>
    ) {
        var shouldReCheckActiveState = false
        shipperProduct.forEach { productModel ->
            val tickerShipperProductData =
                tickerShipperProducts.find { tickerShipperProduct -> tickerShipperProduct.shipperProductId.toString() == productModel.shipperProductId }
            val shipperProductAvailable =
                tickerShipperProductData?.isAvailable ?: isAvailable
            productModel.isAvailable = shipperProductAvailable
            if (shipperProductAvailable.not()) {
                productModel.isActive = false
                shouldReCheckActiveState = true
            }
        }

        if (shouldReCheckActiveState) {
            setActiveState()
        }
    }

    private fun ShipperModel.setActiveState() {
        isActive = shipperProduct.any { shipperProductModel -> shipperProductModel.isActive }
    }
}
