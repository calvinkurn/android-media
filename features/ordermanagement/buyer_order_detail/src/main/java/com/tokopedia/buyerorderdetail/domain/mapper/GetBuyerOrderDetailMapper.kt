package com.tokopedia.buyerorderdetail.domain.mapper

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailTickerType
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.common.utils.Utils.toCurrencyFormatted
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.*
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class GetBuyerOrderDetailMapper @Inject constructor(
        private val resourceProvider: ResourceProvider
) {
    fun mapDomainModelToUiModel(buyerOrderDetail: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail): BuyerOrderDetailUiModel {
        return BuyerOrderDetailUiModel(
                actionButtonsUiModel = mapActionButtons(buyerOrderDetail.button, buyerOrderDetail.dotMenu),
                orderStatusUiModel = mapOrderStatusUiModel(buyerOrderDetail.orderStatus, buyerOrderDetail.tickerInfo, buyerOrderDetail.preOrder, buyerOrderDetail.invoice, buyerOrderDetail.invoiceUrl, buyerOrderDetail.deadline, buyerOrderDetail.paymentDate, buyerOrderDetail.orderId),
                paymentInfoUiModel = mapPaymentInfoUiModel(buyerOrderDetail.payment, buyerOrderDetail.cashbackInfo),
                productListUiModel = mapProductListUiModel(buyerOrderDetail.products, buyerOrderDetail.bundleDetail, buyerOrderDetail.shop, buyerOrderDetail.orderId, buyerOrderDetail.orderStatus.id),
                shipmentInfoUiModel = mapShipmentInfoUiModel(buyerOrderDetail.shipment, buyerOrderDetail.meta, buyerOrderDetail.orderId, buyerOrderDetail.orderStatus.id, buyerOrderDetail.dropship)
        )
    }

    private fun mapActionButtons(button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button, dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>): ActionButtonsUiModel {
        return ActionButtonsUiModel(
                primaryActionButton = mapActionButton(button),
                secondaryActionButtons = mapSecondaryActionButtons(dotMenu)
        )
    }

    private fun mapOrderStatusUiModel(orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus, tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo, preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder, invoice: String, invoiceUrl: String, deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline, paymentDate: String, orderId: String): OrderStatusUiModel {
        return OrderStatusUiModel(
                orderStatusHeaderUiModel = mapOrderStatusHeaderUiModel(orderStatus, preOrder, orderId),
                orderStatusInfoUiModel = mapOrderStatusInfoUiModel(invoice, invoiceUrl, deadline, paymentDate, orderStatus.id, orderId),
                ticker = mapTicker(tickerInfo)
        )
    }

    private fun mapPaymentInfoUiModel(payment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment, cashbackInfo: String): PaymentInfoUiModel {
        return PaymentInfoUiModel(
                headerUiModel = mapPlainHeader(resourceProvider.getPaymentInfoSectionHeader()),
                paymentMethodInfoItem = mapPaymentMethodInfoItem(payment.paymentMethod),
                paymentInfoItems = mapPaymentInfoItems(payment.paymentDetails),
                paymentGrandTotal = mapPaymentGrandTotal(payment.paymentAmount),
                ticker = mapPaymentTicker(cashbackInfo)
        )
    }

    private fun mapProductListUiModel(products: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Product>, bundleDetail: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail?, shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop, orderId: String, orderStatusId: String): ProductListUiModel {
        return ProductListUiModel(
                productList = mapProductList(products, orderId, orderStatusId),
                productListHeaderUiModel = mapProductListHeaderUiModel(shop, orderId, orderStatusId),
                productBundlingList = mapProductBundle(bundleDetail, orderStatusId)
        )
    }

    private fun mapShipmentInfoUiModel(shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment, meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta, orderId: String, orderStatusId: String, dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship): ShipmentInfoUiModel {
        return ShipmentInfoUiModel(
                awbInfoUiModel = mapAwbInfoUiModel(shipment.shippingRefNum, orderStatusId, orderId),
                courierDriverInfoUiModel = mapCourierDriverInfoUiModel(shipment.driver),
                courierInfoUiModel = mapCourierInfoUiModel(shipment, meta),
                dropShipperInfoUiModel = mapDropShipperInfoUiModel(dropship),
                headerUiModel = mapPlainHeader(resourceProvider.getShipmentInfoSectionHeader()),
                receiverAddressInfoUiModel = mapReceiverAddressInfoUiModel(shipment.receiver),
                ticker = mapTicker(shipment.shippingInfo, BuyerOrderDetailMiscConstant.TICKER_KEY_SHIPPING_INFO)
        )
    }

    private fun mapTicker(tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo, actionKey: String? = null): TickerUiModel {
        return TickerUiModel(
                actionKey = actionKey ?: tickerInfo.actionKey,
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
                popUp = mapPopUp(button.popup),
                variant = button.variant,
                type = button.type,
                url = button.url
        )
    }

    private fun mapPopUp(popup: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup): ActionButtonsUiModel.ActionButton.PopUp {
        return ActionButtonsUiModel.ActionButton.PopUp(
                actionButton = mapPopUpButtons(popup.actionButton),
                body = popup.body,
                title = popup.title
        )
    }

    private fun mapPopUpButtons(popUpButtons: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton>): List<ActionButtonsUiModel.ActionButton.PopUp.PopUpButton> {
        return popUpButtons.map {
            mapPopUpButton(it)
        }
    }

    private fun mapPopUpButton(popUpButton: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button.Popup.PopUpButton): ActionButtonsUiModel.ActionButton.PopUp.PopUpButton {
        return ActionButtonsUiModel.ActionButton.PopUp.PopUpButton(
                key = popUpButton.key,
                displayName = popUpButton.displayName,
                color = popUpButton.color,
                type = popUpButton.type,
                uriType = popUpButton.uriType,
                uri = popUpButton.uri
        )
    }

    private fun mapSecondaryActionButtons(dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>): List<ActionButtonsUiModel.ActionButton> {
        return dotMenu.map {
            ActionButtonsUiModel.ActionButton(
                    key = it.key,
                    label = it.displayName,
                    popUp = mapPopUp(it.popup),
                    variant = "",
                    type = "",
                    url = it.url
            )
        }
    }

    private fun mapOrderStatusHeaderUiModel(orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus, preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder, orderId: String): OrderStatusUiModel.OrderStatusHeaderUiModel {
        return OrderStatusUiModel.OrderStatusHeaderUiModel(
                indicatorColor = orderStatus.indicatorColor,
                orderId = orderId,
                orderStatus = orderStatus.statusName,
                orderStatusId = orderStatus.id,
                preOrder = mapPreOrderUiModel(preOrder)
        )
    }

    private fun mapOrderStatusInfoUiModel(invoice: String, invoiceUrl: String, deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline, paymentDate: String, orderStatusId: String, orderId: String): OrderStatusUiModel.OrderStatusInfoUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel(
                deadline = mapDeadlineUiModel(deadline),
                invoice = mapInvoiceUiModel(invoice, invoiceUrl),
                purchaseDate = paymentDate,
                orderId = orderId,
                orderStatusId = orderStatusId
        )
    }

    private fun mapInvoiceUiModel(invoice: String, invoiceUrl: String): OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel(
                invoice = invoice,
                url = invoiceUrl
        )
    }

    private fun mapPreOrderUiModel(preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder): OrderStatusUiModel.OrderStatusHeaderUiModel.PreOrderUiModel {
        return OrderStatusUiModel.OrderStatusHeaderUiModel.PreOrderUiModel(
                isPreOrder = preOrder.isPreOrder,
                label = preOrder.label,
                value = preOrder.value
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
                type = BuyerOrderDetailTickerType.INFO
        )
    }

    private fun mapProductListHeaderUiModel(shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop, orderId: String, orderStatusId: String): ProductListUiModel.ProductListHeaderUiModel {
        return ProductListUiModel.ProductListHeaderUiModel(
                header = resourceProvider.getProductListSectionHeader(),
                orderId = orderId,
                shopBadgeUrl = shop.badgeUrl,
                shopName = shop.shopName,
                shopType = shop.shopType,
                shopId = shop.shopId,
                orderStatusId = orderStatusId
        )
    }

    private fun mapProductList(products: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Product>, orderId: String, orderStatusId: String): List<ProductListUiModel.ProductUiModel> {
        return products.map {
            mapProduct(it, orderId, orderStatusId)
        }
    }

    private fun mapProduct(product: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Product, orderId: String, orderStatusId: String): ProductListUiModel.ProductUiModel {
        return ProductListUiModel.ProductUiModel(
                button = mapActionButton(product.button),
                category = product.category,
                categoryId = product.categoryId,
                orderDetailId = product.orderDetailId,
                orderId = orderId,
                orderStatusId = orderStatusId,
                price = product.price,
                priceText = product.priceText,
                productId = product.productId,
                productName = product.productName,
                productNote = product.notes,
                productThumbnailUrl = product.thumbnail,
                quantity = product.quantity,
                totalPrice = product.totalPrice,
                totalPriceText = product.totalPriceText
        )
    }

    private fun mapProductBundle(bundleDetail: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.BundleDetail?, orderStatusId: String): List<ProductListUiModel.ProductBundlingUiModel>? {
        return bundleDetail?.bundleList?.map { bundle ->
            ProductListUiModel.ProductBundlingUiModel(
                    // TODO: get actual action button
                    bundleName = bundle.bundleName,
                    totalPrice = bundle.bundleSubtotalPrice.toCurrencyFormatted(),
                    totalPriceText = bundle.bundleSubtotalPrice.toCurrencyFormatted(),
                    bundleItemList = bundle.orderDetailList.map { bundleDetail ->
                        ProductListUiModel.ProductBundlingItemUiModel(
                                orderId = bundleDetail.orderId.toString(),
                                orderDetailId = bundleDetail.orderDetailId.toString(),
                                orderStatusId = orderStatusId,
                                productId = bundleDetail.productId,
                                productName = bundleDetail.productName,
                                productNote = bundleDetail.notes,
                                productThumbnailUrl = bundleDetail.thumbnail,
                                quantity = bundleDetail.quantity,
                                productPrice = bundleDetail.productPrice
                        )
                    }
            )
        }
    }

    private fun mapDropShipperInfoUiModel(dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship): CopyableKeyValueUiModel {
        return CopyableKeyValueUiModel(
                copyableText = formatDropshipperValue(dropship),
                copyLabel = "",
                copyMessage = "",
                label = resourceProvider.getDropshipLabel()
        )
    }

    private fun mapCourierInfoUiModel(shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment, meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta): ShipmentInfoUiModel.CourierInfoUiModel {
        return ShipmentInfoUiModel.CourierInfoUiModel(
                arrivalEstimation = composeETA(shipment.eta),
                courierNameAndProductName = shipment.shippingDisplayName,
                isFreeShipping = meta.isBebasOngkir,
                boBadgeUrl = meta.boImageUrl
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

    private fun mapAwbInfoUiModel(shippingRefNum: String, orderStatusId: String, orderId: String): ShipmentInfoUiModel.AwbInfoUiModel {
        return ShipmentInfoUiModel.AwbInfoUiModel(
                orderId = orderId,
                orderStatusId = orderStatusId,
                copyableText = SpannableString(shippingRefNum),
                copyLabel = resourceProvider.getCopyLabelAwb(),
                copyMessage = resourceProvider.getCopyMessageAwb(),
                label = resourceProvider.getAwbLabel(),
        )
    }

    private fun mapReceiverAddressInfoUiModel(receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver): CopyableKeyValueUiModel {
        val receiverAddress = composeReceiverAddress(receiver.street, receiver.district, receiver.city, receiver.province, receiver.postal)
        return CopyableKeyValueUiModel(
                copyableText = formatReceiverAddressValue(receiver, receiverAddress),
                copyLabel = resourceProvider.getCopyLabelReceiverAddress(),
                copyMessage = resourceProvider.getCopyMessageReceiverAddress(),
                label = resourceProvider.getReceiverAddressLabel()
        )
    }

    private fun formatReceiverAddressValue(receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver, receiverAddress: String): Spannable {
        return SpannableStringBuilder().apply {
            if (receiver.name.isNotBlank()) append(createBoldText(receiver.name))
            if (receiver.phone.isNotBlank()) {
                if (isNotBlank()) appendLine()
                append(receiver.phone)
            }
            if (receiverAddress.isNotBlank()) {
                if (isNotBlank()) appendLine()
                append(receiverAddress)
            }
        }
    }

    private fun formatDropshipperValue(dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship): Spannable {
        return SpannableStringBuilder().apply {
            if (dropship.name.isNotBlank()) append(createBoldText(dropship.name))
            if (dropship.phoneNumber.isNotBlank()) {
                if (isNotBlank()) appendLine()
                append(dropship.phoneNumber)
            }
        }
    }

    private fun composeReceiverAddress(vararg chunks: String): String {
        return StringBuilder().apply {
            chunks.forEach {
                if (isNotBlank()) append(", ")
                if (it.isNotBlank()) append(it)
            }
        }.toString()
    }

    private fun createBoldText(text: String): Spannable {
        return SpannableString(text).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun composeETA(eta: String): String {
        return if (eta.isBlank() || eta.startsWith("(") || eta.endsWith(")")) eta else "($eta)"
    }
}
