package com.tokopedia.buyerorderdetail.domain.mapper

import androidx.annotation.StringRes
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailTickerType
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.common.utils.Utils.toCurrencyFormatted
import com.tokopedia.buyerorderdetail.domain.models.AddonInfo
import com.tokopedia.buyerorderdetail.domain.models.Details
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailResponse
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.CopyableKeyValueUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PaymentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PlainHeaderUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ShipmentInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.kotlin.extensions.orFalse
import java.lang.StringBuilder
import javax.inject.Inject

class GetBuyerOrderDetailMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {
    fun mapDomainModelToUiModel(
        buyerOrderDetail: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ): BuyerOrderDetailUiModel {
        return BuyerOrderDetailUiModel(
            actionButtonsUiModel = mapActionButtons(
                buyerOrderDetail.button,
                buyerOrderDetail.dotMenu
            ),
            orderStatusUiModel = mapOrderStatusUiModel(
                buyerOrderDetail.orderStatus,
                buyerOrderDetail.tickerInfo,
                buyerOrderDetail.preOrder,
                buyerOrderDetail.invoice,
                buyerOrderDetail.invoiceUrl,
                buyerOrderDetail.deadline,
                buyerOrderDetail.paymentDate,
                buyerOrderDetail.orderId
            ),
            paymentInfoUiModel = mapPaymentInfoUiModel(
                buyerOrderDetail.payment,
                buyerOrderDetail.cashbackInfo
            ),
            productListUiModel = mapProductListUiModel(
                buyerOrderDetail.details,
                buyerOrderDetail.details?.bundleIcon.orEmpty(),
                buyerOrderDetail.shop,
                buyerOrderDetail.addonInfo,
                buyerOrderDetail.orderId,
                buyerOrderDetail.orderStatus.id
            ),
            shipmentInfoUiModel = mapShipmentInfoUiModel(
                buyerOrderDetail.shipment,
                buyerOrderDetail.meta,
                buyerOrderDetail.orderId,
                buyerOrderDetail.orderStatus.id,
                buyerOrderDetail.dropship,
                buyerOrderDetail.getDriverTippingInfo()
            ),
            pgRecommendationWidgetUiModel = mapToRecommendationWidgetUiModel(
                buyerOrderDetail.adsPageName,
                buyerOrderDetail.products
            )
        )
    }

    private fun getAddonsSectionProductLevel(
        details: Details,
        addonSummary: Details.NonBundle.AddonSummary?
    ): AddonsListUiModel {
        return AddonsListUiModel(
            addonsTitle = details.addonLabel,
            addonsLogoUrl = details.addonIcon,
            totalPriceText = addonSummary?.totalPriceStr.orEmpty(),
            addonsItemList = addonSummary?.addons?.map {
                val message = it.metadata?.addonNote?.notes.orEmpty()
                AddonsListUiModel.AddonItemUiModel(
                    priceText = it.priceStr,
                    addOnsName = it.name,
                    addonsId = it.id,
                    quantity = it.quantity,
                    addOnsThumbnailUrl = it.imageUrl,
                    isCustomNote = it.metadata?.addonNote?.isCustomNote.orFalse(),
                    toStr = it.metadata?.addonNote?.to.orEmpty(),
                    fromStr = it.metadata?.addonNote?.from.orEmpty(),
                    message = StringBuilder(message).append(message).append(message).append(message)
                        .append(message).append(message).append(message).append(message)
                        .append(message).toString()
                )
            }.orEmpty()
        )
    }

    private fun getAddonsSectionOrderLevel(
        addonInfo: AddonInfo?
    ): AddonsListUiModel? {
        return if (addonInfo != null) {
            AddonsListUiModel(
                addonsTitle = addonInfo.label,
                addonsLogoUrl = addonInfo.iconUrl,
                totalPriceText = addonInfo.orderLevel?.totalPriceStr.orEmpty(),
                addonsItemList = addonInfo.orderLevel?.addons?.map {
                    AddonsListUiModel.AddonItemUiModel(
                        priceText = it.priceStr,
                        addOnsName = it.name,
                        addonsId = it.id,
                        quantity = it.quantity,
                        addOnsThumbnailUrl = it.imageUrl,
                        isCustomNote = it.metadata?.addonNote?.isCustomNote.orFalse(),
                        toStr = it.metadata?.addonNote?.to.orEmpty(),
                        fromStr = it.metadata?.addonNote?.from.orEmpty(),
                        message = it.metadata?.addonNote?.notes.orEmpty()
                    )
                }.orEmpty()
            )
        } else null
    }

    private fun mapToRecommendationWidgetUiModel(
        adsPageName: String,
        productsList: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Product>
    ): PGRecommendationWidgetUiModel {
        val productIdList = arrayListOf<String>()
        productsList.forEach { product ->
            productIdList.add(product.productId)
        }
        return PGRecommendationWidgetUiModel(adsPageName, productIdList)
    }

    private fun mapActionButtons(
        button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button,
        dotMenu: List<GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.DotMenu>
    ): ActionButtonsUiModel {
        return ActionButtonsUiModel(
            primaryActionButton = mapActionButton(button),
            secondaryActionButtons = mapSecondaryActionButtons(dotMenu)
        )
    }

    private fun mapOrderStatusUiModel(
        orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus,
        tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo,
        preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder,
        invoice: String,
        invoiceUrl: String,
        deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline,
        paymentDate: String,
        orderId: String
    ): OrderStatusUiModel {
        return OrderStatusUiModel(
            orderStatusHeaderUiModel = mapOrderStatusHeaderUiModel(orderStatus, preOrder, orderId),
            orderStatusInfoUiModel = mapOrderStatusInfoUiModel(
                invoice,
                invoiceUrl,
                deadline,
                paymentDate,
                orderStatus.id,
                orderId
            ),
            ticker = mapTicker(tickerInfo)
        )
    }

    private fun mapPaymentInfoUiModel(
        payment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Payment,
        cashbackInfo: String
    ): PaymentInfoUiModel {
        return PaymentInfoUiModel(
            headerUiModel = mapPlainHeader(resourceProvider.getPaymentInfoSectionHeader()),
            paymentMethodInfoItem = mapPaymentMethodInfoItem(payment.paymentMethod),
            paymentInfoItems = mapPaymentInfoItems(payment.paymentDetails),
            paymentGrandTotal = mapPaymentGrandTotal(payment.paymentAmount),
            ticker = mapPaymentTicker(cashbackInfo)
        )
    }

    private fun mapProductListUiModel(
        details: Details?,
        bundleIcon: String,
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop,
        addonInfo: AddonInfo?,
        orderId: String,
        orderStatusId: String
    ): ProductListUiModel {
        val productList =
            details?.let { mapProductList(it, orderId, orderStatusId) }.orEmpty()
        val productBundlingList =
            mapProductBundle(details?.bundles, bundleIcon, orderId, orderStatusId)
        return ProductListUiModel(
            productList = productList,
            productListHeaderUiModel = mapProductListHeaderUiModel(shop, orderId, orderStatusId),
            productBundlingList = productBundlingList,
            addonsListUiModel = getAddonsSectionOrderLevel(addonInfo)
        )
    }

    private fun mapShipmentInfoUiModel(
        shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment,
        meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta,
        orderId: String,
        orderStatusId: String,
        dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship,
        driverTippingInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?
    ): ShipmentInfoUiModel {
        return ShipmentInfoUiModel(
            awbInfoUiModel = mapAwbInfoUiModel(shipment.shippingRefNum, orderStatusId, orderId),
            courierDriverInfoUiModel = mapCourierDriverInfoUiModel(shipment.driver),
            driverTippingInfoUiModel = mapDriverTippingInfoUiModel(driverTippingInfo),
            courierInfoUiModel = mapCourierInfoUiModel(shipment, meta),
            dropShipperInfoUiModel = mapDropShipperInfoUiModel(dropship),
            headerUiModel = mapPlainHeader(resourceProvider.getShipmentInfoSectionHeader()),
            receiverAddressInfoUiModel = mapReceiverAddressInfoUiModel(shipment.receiver),
            ticker = mapTicker(
                shipment.shippingInfo,
                BuyerOrderDetailMiscConstant.TICKER_KEY_SHIPPING_INFO
            )
        )
    }

    private fun mapTicker(
        tickerInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.TickerInfo,
        actionKey: String? = null
    ): TickerUiModel {
        return TickerUiModel(
            actionKey = actionKey ?: tickerInfo.actionKey,
            actionText = tickerInfo.actionText,
            actionUrl = tickerInfo.actionUrl,
            description = tickerInfo.text,
            type = tickerInfo.type
        )
    }

    private fun mapPlainHeader(@StringRes headerStringResId: Int): PlainHeaderUiModel {
        return PlainHeaderUiModel(
            header = mapStringRes(headerStringResId)
        )
    }

    private fun mapStringRes(@StringRes resId: Int): com.tokopedia.buyerorderdetail.presentation.model.StringRes {
        return com.tokopedia.buyerorderdetail.presentation.model.StringRes(resId)
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

    private fun mapOrderStatusHeaderUiModel(
        orderStatus: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.OrderStatus,
        preOrder: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.PreOrder,
        orderId: String
    ): OrderStatusUiModel.OrderStatusHeaderUiModel {
        return OrderStatusUiModel.OrderStatusHeaderUiModel(
            indicatorColor = orderStatus.indicatorColor,
            orderId = orderId,
            orderStatus = orderStatus.statusName,
            orderStatusId = orderStatus.id,
            preOrder = mapPreOrderUiModel(preOrder)
        )
    }

    private fun mapOrderStatusInfoUiModel(
        invoice: String,
        invoiceUrl: String,
        deadline: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Deadline,
        paymentDate: String,
        orderStatusId: String,
        orderId: String
    ): OrderStatusUiModel.OrderStatusInfoUiModel {
        return OrderStatusUiModel.OrderStatusInfoUiModel(
            deadline = mapDeadlineUiModel(deadline),
            invoice = mapInvoiceUiModel(invoice, invoiceUrl),
            purchaseDate = paymentDate,
            orderId = orderId,
            orderStatusId = orderStatusId
        )
    }

    private fun mapInvoiceUiModel(
        invoice: String,
        invoiceUrl: String
    ): OrderStatusUiModel.OrderStatusInfoUiModel.InvoiceUiModel {
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

    private fun mapProductListHeaderUiModel(
        shop: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shop,
        orderId: String,
        orderStatusId: String
    ): ProductListUiModel.ProductListHeaderUiModel {
        return ProductListUiModel.ProductListHeaderUiModel(
            orderId = orderId,
            shopBadgeUrl = shop.badgeUrl,
            shopName = shop.shopName,
            shopType = shop.shopType,
            shopId = shop.shopId,
            orderStatusId = orderStatusId
        )
    }

    private fun mapProductList(
        details: Details,
        orderId: String,
        orderStatusId: String
    ): List<ProductListUiModel.ProductUiModel> {
        return details.nonBundles?.map {
            mapProduct(
                details,
                it,
                it.addonSummary,
                it.button ?: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button(),
                orderId,
                orderStatusId
            )
        }.orEmpty()
    }

    private fun mapProductBundleItem(
        product: Details.Bundle.OrderDetail,
        orderId: String,
        orderStatusId: String
    ): ProductListUiModel.ProductUiModel {
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

    private fun mapProduct(
        details: Details,
        product: Details.NonBundle,
        addonSummary: Details.NonBundle.AddonSummary?,
        button: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Button,
        orderId: String,
        orderStatusId: String
    ): ProductListUiModel.ProductUiModel {
        return ProductListUiModel.ProductUiModel(
            button = mapActionButton(button),
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
            totalPriceText = product.totalPriceText,
            addonsListUiModel = getAddonsSectionProductLevel(details, addonSummary)
        )
    }

    private fun mapProductBundle(
        bundleDetail: List<Details.Bundle>?,
        bundleIcon: String,
        orderId: String,
        orderStatusId: String
    ): List<ProductListUiModel.ProductBundlingUiModel> {
        return bundleDetail?.map { bundle ->
            ProductListUiModel.ProductBundlingUiModel(
                bundleName = bundle.bundleName,
                bundleIconUrl = bundleIcon,
                totalPrice = bundle.bundleSubtotalPrice,
                totalPriceText = bundle.bundleSubtotalPrice.toCurrencyFormatted(),
                bundleItemList = bundle.orderDetail.map { bundleDetail ->
                    mapProductBundleItem(bundleDetail, orderId, orderStatusId)
                }
            )
        }.orEmpty()
    }

    private fun mapDropShipperInfoUiModel(dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship): CopyableKeyValueUiModel {
        return CopyableKeyValueUiModel(
            copyableText = formatDropshipperValue(dropship),
            label = mapStringRes(resourceProvider.getDropshipLabel())
        )
    }

    private fun mapCourierInfoUiModel(
        shipment: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment,
        meta: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Meta
    ): ShipmentInfoUiModel.CourierInfoUiModel {
        return ShipmentInfoUiModel.CourierInfoUiModel(
            arrivalEstimation = composeETA(shipment.eta),
            courierNameAndProductName = shipment.shippingDisplayName,
            isFreeShipping = meta.isBebasOngkir,
            boBadgeUrl = meta.boImageUrl,
            etaChanged = shipment.etaIsUpdated,
            etaUserInfo = shipment.userUpdatedInfo
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

    private fun mapDriverTippingInfoUiModel(driverTippingInfo: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.LogisticSectionInfo?): ShipmentInfoUiModel.DriverTippingInfoUiModel {
        return ShipmentInfoUiModel.DriverTippingInfoUiModel(
            imageUrl = driverTippingInfo?.imageUrl.orEmpty(),
            title = driverTippingInfo?.title.orEmpty(),
            description = resourceProvider.composeDriverTippingInfoDescription(driverTippingInfo)
        )
    }

    private fun mapAwbInfoUiModel(
        shippingRefNum: String,
        orderStatusId: String,
        orderId: String
    ): ShipmentInfoUiModel.AwbInfoUiModel {
        return ShipmentInfoUiModel.AwbInfoUiModel(
            orderId = orderId,
            orderStatusId = orderStatusId,
            copyableText = shippingRefNum,
            copyLabel = mapStringRes(resourceProvider.getCopyLabelAwb()),
            copyMessage = mapStringRes(resourceProvider.getCopyMessageAwb()),
            label = mapStringRes(resourceProvider.getAwbLabel()),
        )
    }

    private fun mapReceiverAddressInfoUiModel(receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver): CopyableKeyValueUiModel {
        return CopyableKeyValueUiModel(
            copyableText = formatReceiverAddressValue(receiver),
            copyLabel = mapStringRes(resourceProvider.getCopyLabelReceiverAddress()),
            copyMessage = mapStringRes(resourceProvider.getCopyMessageReceiverAddress()),
            label = mapStringRes(resourceProvider.getReceiverAddressLabel())
        )
    }

    private fun formatReceiverAddressValue(receiver: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Shipment.Receiver): String {
        return resourceProvider.composeReceiverAddressValue(receiver)
    }

    private fun formatDropshipperValue(dropship: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail.Dropship): String {
        return resourceProvider.composeDropshipperValue(dropship)
    }

    private fun composeETA(eta: String): String {
        return if (eta.isBlank() || eta.startsWith("(") || eta.endsWith(")")) eta else "($eta)"
    }
}
