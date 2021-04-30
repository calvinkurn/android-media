package com.tokopedia.buyerorderdetail.domain.mapper

import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.*
import java.lang.StringBuilder
import javax.inject.Inject

class GetBuyerOrderDetailMapper @Inject constructor() {
    fun mapDomainModelToUiModel(buyerOrderDetail: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail): BuyerOrderDetailUiModel {
        return BuyerOrderDetailUiModel(
                actionButtonsUiModel = mapActionButtons(buyerOrderDetail.button, buyerOrderDetail.dotMenu),
                buyProtectionUiModel = mapBuyProtectionUiModel(),
                orderStatusUiModel = mapOrderStatusUiModel(buyerOrderDetail.orderStatus, buyerOrderDetail.tickerInfo, buyerOrderDetail.invoice, buyerOrderDetail.invoiceUrl, buyerOrderDetail.deadline, buyerOrderDetail.paymentDate),
                paymentInfoUiModel = mapPaymentInfoUiModel(buyerOrderDetail.payment, buyerOrderDetail.cashbackInfo),
                productListUiModel = mapProductListUiModel(buyerOrderDetail.products, buyerOrderDetail.shop, buyerOrderDetail.meta),
                shipmentInfoUiModel = mapShipmentInfoUiModel(buyerOrderDetail.shipment)
        )
    }

    private fun mapActionButtons(button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button, dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>): ActionButtonsUiModel {
        return ActionButtonsUiModel(
                primaryActionButton = mapActionButton(button),
                secondaryActionButtons = mapSecondaryActionButtons(dotMenu)
        )
    }

    private fun mapBuyProtectionUiModel(): BuyProtectionUiModel {
        return BuyProtectionUiModel(
                deadline = 1234567890, //TODO: replace with the one from backend
                description = "12 bulan proteksi diluar cakupan garansi resmi, ganti rugi hingga senilai harga barang", //TODO: replace with the one from backend
                title = "Beli Proteksi?" //TODO: replace with the one from backend
        )
    }

    private fun mapOrderStatusUiModel(orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus, tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo, invoice: String, invoiceUrl: String, deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline, paymentDate: String): OrderStatusUiModel {
        return OrderStatusUiModel(
                orderStatusHeaderUiModel = mapOrderStatusHeaderUiModel(orderStatus),
                orderStatusInfoUiModel = mapOrderStatusInfoUiModel(invoice, invoiceUrl, deadline, paymentDate),
                ticker = mapTicker(tickerInfo)
        )
    }

    private fun mapPaymentInfoUiModel(payment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment, cashbackInfo: String): PaymentInfoUiModel {
        return PaymentInfoUiModel(
                headerUiModel = mapPlainHeader(BuyerOrderDetailConst.SECTION_HEADER_PAYMENT_INFO),
                paymentMethodInfoItem = mapPaymentMethodInfoItem(payment.paymentMethod),
                paymentInfoItems = mapPaymentInfoItems(payment.paymentDetails),
                paymentGrandTotal = mapPaymentGrandTotal(payment.paymentAmount),
                ticker = mapPaymentTicker(cashbackInfo)
        )
    }

    private fun mapProductListUiModel(products: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Product>, shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop, meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta): ProductListUiModel {
        return ProductListUiModel(
                productList = mapProductList(products),
                productListHeaderUiModel = mapProductListHeaderUiModel(shop, meta)
        )
    }

    private fun mapShipmentInfoUiModel(shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment): ShipmentInfoUiModel {
        return ShipmentInfoUiModel(
                awbInfoUiModel = mapAwbInfoUiModel(shipment.shippingRefNum),
                courierDriverInfoUiModel = mapCourierDriverInfoUiModel(shipment.driver),
                courierInfoUiModel = mapCourierInfoUiModel(shipment),
                headerUiModel = mapPlainHeader(BuyerOrderDetailConst.SECTION_HEADER_SHIPMENT_INFO),
                receiverAddressInfoUiModel = mapReceiverAddressInfoUiModel(shipment.receiver),
                ticker = mapShipmentTickerUiModel(shipment.shippingInfo)
        )
    }

    private fun mapTicker(tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo): TickerUiModel {
        return TickerUiModel(
                actionKey = tickerInfo.actionKey,
                actionText = tickerInfo.actionText,
                actionUrl = tickerInfo.actionUrl,
                description = tickerInfo.text,
                type = tickerInfo.type
        )
    }

    private fun mapPlainHeader(header: String): PlainHeaderUiModel {
        return PlainHeaderUiModel(
                header = header
        )
    }

    private fun mapActionButton(button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button): ActionButtonsUiModel.ActionButton {
        return ActionButtonsUiModel.ActionButton(
                key = button.key,
                label = button.displayName,
                popUp = mapButtonPopUp(button.popup),
                style = button.style
        )
    }

    private fun mapActionButtons(actionButton: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button>): List<ActionButtonsUiModel.ActionButton> {
        return actionButton.map {
            mapActionButton(it)
        }
    }

    private fun mapButtonPopUp(popup: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup): ActionButtonsUiModel.ActionButton.PopUp {
        return ActionButtonsUiModel.ActionButton.PopUp(
                actionButton = mapActionButtons(popup.actionButton)
        )
    }

    private fun mapSecondaryActionButtons(dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>): List<ActionButtonsUiModel.ActionButton> {
        return dotMenu.map {
            ActionButtonsUiModel.ActionButton(
                    key = it.key,
                    label = it.displayName,
                    popUp = mapButtonPopUp(it.popup),
                    style = ""
            )
        }
    }

    private fun mapOrderStatusHeaderUiModel(orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus): OrderStatusUiModel.OrderStatusHeaderUiModel {
        return OrderStatusUiModel.OrderStatusHeaderUiModel(
                orderId = orderStatus.id,
                orderStatus = orderStatus.statusName,
                indicatorColor = orderStatus.indicatorColor
        )
    }

    private fun mapOrderStatusInfoUiModel(invoice: String, invoiceUrl: String, deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline, paymentDate: String): OrderStatusUiModel.OrderStatusInfoUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel(
                deadline = mapDeadlineUiModel(deadline),
                invoice = mapInvoiceUiModel(invoice, invoiceUrl),
                purchaseDate = paymentDate
        )
    }

    private fun mapInvoiceUiModel(invoice: String, invoiceUrl: String): OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel(
                invoice = invoice,
                url = invoiceUrl
        )
    }

    private fun mapDeadlineUiModel(deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline): OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel(
                color = deadline.color,
                label = deadline.label,
                value = deadline.value
        )
    }

    private fun mapPaymentInfoItems(paymentDetails: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentDetail>): List<PaymentInfoUiModel.PaymentInfoItemUiModel> {
        return paymentDetails.map {
            mapPaymentInfoItem(it)
        }
    }

    private fun mapPaymentInfoItem(paymentDetail: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentDetail): PaymentInfoUiModel.PaymentInfoItemUiModel {
        return PaymentInfoUiModel.PaymentInfoItemUiModel(
                label = paymentDetail.label,
                value = paymentDetail.value
        )
    }

    private fun mapPaymentMethodInfoItem(paymentMethod: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentMethod): PaymentInfoUiModel.PaymentInfoItemUiModel {
        return PaymentInfoUiModel.PaymentInfoItemUiModel(
                label = paymentMethod.label,
                value = paymentMethod.value
        )
    }

    private fun mapPaymentGrandTotal(paymentAmount: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment.PaymentAmount): PaymentInfoUiModel.PaymentGrandTotalUiModel {
        return PaymentInfoUiModel.PaymentGrandTotalUiModel(
                label = paymentAmount.label,
                value = paymentAmount.value
        )
    }

    private fun mapPaymentTicker(cashbackInfo: String): TickerUiModel {
        return TickerUiModel(
                actionKey = "",
                actionText = "",
                actionUrl = "",
                description = cashbackInfo,
                type = BuyerOrderDetailConst.TICKER_TYPE_INFO
        )
    }

    private fun mapProductListHeaderUiModel(shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop, meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta): ProductListUiModel.ProductListHeaderUiModel {
        return ProductListUiModel.ProductListHeaderUiModel(
                header = BuyerOrderDetailConst.SECTION_HEADER_PRODUCT_LIST,
                shopBadge = mapShopBadge(meta.isOs, meta.isPm),
                shopName = shop.shopName,
                shopId = shop.shopId
        )
    }

    private fun mapShopBadge(os: Boolean, pm: Boolean): Int {
        return if (os) 2 else if (pm) 1 else 0
    }

    private fun mapProductList(products: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Product>): List<ProductListUiModel.ProductUiModel> {
        return products.map {
            mapProduct(it)
        }
    }

    private fun mapProduct(product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Product): ProductListUiModel.ProductUiModel {
        return ProductListUiModel.ProductUiModel(
                button = mapActionButton(product.button),
                orderDetailId = product.orderDetailId,
                price = product.price,
                priceText = product.priceText,
                productId = product.productId,
                productName = product.productName,
                productNote = product.notes,
                productThumbnailUrl = product.thumbnail,
                quantity = product.quantity,
                totalPrice = product.totalPrice,
                totalPriceText = product.totalPriceText,
                showClaimInsurance = true
        )
    }

    private fun mapShipmentTickerUiModel(shippingInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.ShippingInfo): TickerUiModel {
        return TickerUiModel(
                actionKey = "",
                actionText = shippingInfo.urlText,
                actionUrl = shippingInfo.urlDetail,
                description = composeTickerDescription(shippingInfo.title, shippingInfo.notes),
                type = ""
        )
    }

    private fun mapCourierInfoUiModel(shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment): ShipmentInfoUiModel.CourierInfoUiModel {
        return ShipmentInfoUiModel.CourierInfoUiModel(
                arrivalEstimation = shipment.eta,
                courierNameAndProductName = shipment.shippingDisplayName,
                isFreeShipping = shipment.isBebasOngkir
        )
    }

    private fun mapCourierDriverInfoUiModel(driver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Driver): ShipmentInfoUiModel.CourierDriverInfoUiModel {
        return ShipmentInfoUiModel.CourierDriverInfoUiModel(
                name = driver.name,
                phoneNumber = driver.phone,
                photoUrl = driver.photoUrl,
                plateNumber = driver.licenseNumber
        )
    }

    private fun mapAwbInfoUiModel(shippingRefNum: String): ShipmentInfoUiModel.AwbInfoUiModel {
        return ShipmentInfoUiModel.AwbInfoUiModel(
                awbNumber = shippingRefNum
        )
    }

    private fun mapReceiverAddressInfoUiModel(receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver): ShipmentInfoUiModel.ReceiverAddressInfoUiModel {
        return ShipmentInfoUiModel.ReceiverAddressInfoUiModel(
                receiverAddress = composeReceiverAddress(receiver.street, receiver.district, receiver.city, receiver.province, receiver.postal),
                receiverAddressNote = "Lantai 29", //TODO: replace with the one from backend
                receiverName = receiver.name,
                receiverPhoneNumber = receiver.phone
        )
    }

    private fun composeReceiverAddress(street: String, district: String, city: String, province: String, postal: String): String {
        return StringBuilder().append(street).append(", ").append(district).append(", ").append(city)
                .append(", ").append(province).append(" ").append(postal).toString()
    }

    private fun composeTickerDescription(title: String, notes: String): String {
        return StringBuilder().append(title).append(". ").append(notes).toString()
    }
}