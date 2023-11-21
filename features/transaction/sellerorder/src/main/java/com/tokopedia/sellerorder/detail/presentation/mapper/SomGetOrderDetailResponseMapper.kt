package com.tokopedia.sellerorder.detail.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDetailProducts
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory
import com.tokopedia.sellerorder.detail.presentation.model.BaseProductUiModel
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel

object SomGetOrderDetailResponseMapper {

    private fun getBmgmList(
        bmgms: List<SomDetailOrder.GetSomDetail.Bmgm>?,
        orderId: String,
        bmgmIconUrl: String,
        addOnLabel: String,
        addOnIcon: String,
        addOnsExpandableState: List<String>
    ): List<ProductBmgmSectionUiModel> {
        return bmgms?.map { bmgm ->
            ProductBmgmSectionUiModel(
                bmgmId = bmgm.id,
                bmgmName = bmgm.bmgmTierName,
                bmgmIconUrl = bmgmIconUrl,
                totalPrice = bmgm.priceBeforeBenefit,
                totalPriceText = bmgm.priceBeforeBenefitFormatted,
                totalPriceReductionInfoText = bmgm.totalPriceNote,
                bmgmItemList = bmgm.orderDetail.map {
                    val addOnsIdentifier = it.id + it.orderDtlId
                    ProductBmgmSectionUiModel.ProductUiModel(
                        orderId = orderId,
                        orderDetailId = it.orderDtlId,
                        productName = it.productName,
                        price = it.price,
                        productPriceText = it.priceText,
                        quantity = it.quantity,
                        productNote = it.note,
                        thumbnailUrl = it.thumbnail,
                        addOnSummaryUiModel = it.addonSummary?.let { addOnSummary ->
                            com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel(
                                addOnIdentifier = addOnsIdentifier,
                                totalPriceText = addOnSummary.totalPriceStr,
                                addonsLogoUrl = addOnIcon,
                                addonsTitle = addOnLabel,
                                addonItemList = addOnSummary.addons.map { addon ->
                                    val addOnNote = addon.metadata?.addOnNote
                                    val infoLink = addon.metadata?.infoLink
                                    com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel.AddonItemUiModel(
                                        priceText = addon.priceStr,
                                        quantity = addon.quantity,
                                        addonsId = addon.id,
                                        addOnsName = addon.name,
                                        type = addon.type,
                                        addOnsThumbnailUrl = addon.imageUrl,
                                        toStr = addOnNote?.to.orEmpty(),
                                        fromStr = addOnNote?.from.orEmpty(),
                                        message = addOnNote?.notes.orEmpty(),
                                        noteCopyable = true,
                                        providedByShopItself = true,
                                        infoLink = infoLink.orEmpty(),
                                        tips = addOnNote?.tips.orEmpty()
                                    )
                                }
                            ).also {
                                it.isExpand = !addOnsExpandableState.contains(addOnsIdentifier)
                            }
                        }
                    )
                }
            )
        }.orEmpty()
    }

    private fun getProductBundleList(
        bundleList: List<SomDetailOrder.GetSomDetail.Details.Bundle>?,
        bundleIcon: String
    ): List<ProductBundleUiModel> {
        return bundleList?.map { bundle ->
            ProductBundleUiModel(
                bundleId = bundle.bundleId,
                bundleIcon = bundleIcon,
                bundleName = bundle.bundleName,
                bundlePrice = Utils.parseRupiah(bundle.bundlePrice),
                bundleSubTotal = Utils.parseRupiah(bundle.bundleSubtotalPrice),
                orderDetail = bundle.orderDetail.map {
                    SomDetailOrder.GetSomDetail.Details.Product(
                        id = it.id,
                        orderDetailId = it.orderDetailId,
                        name = it.name,
                        thumbnail = it.thumbnail,
                        priceText = it.priceText,
                        quantity = it.quantity,
                        note = it.note
                    )
                }
            )
        }.orEmpty()
    }

    private fun getProductNonBundleList(
        products: List<SomDetailOrder.GetSomDetail.Details.Product>?,
        addOnInfo: SomDetailOrder.GetSomDetail.AddOnInfo?,
        addOnIcon: String,
        addOnLabel: String,
        addOnsExpandableState: List<String>
    ): List<BaseProductUiModel> {
        return arrayListOf<BaseProductUiModel>().apply {
            includeProducts(products, addOnIcon, addOnLabel, addOnsExpandableState)
            includeOrderAddOn(addOnInfo, addOnsExpandableState)
        }
    }

    private fun MutableList<Visitable<SomDetailAdapterFactory>>.includeProductBmgms(
        bmgms: List<SomDetailOrder.GetSomDetail.Bmgm>?,
        orderId: String,
        bmgmIcon: String,
        addonIcon: String,
        addonLabel: String,
        addOnsExpandableState: List<String>
    ) {
        val bmgmList =
            getBmgmList(bmgms, orderId, bmgmIcon, addonLabel, addonIcon, addOnsExpandableState)
        (bmgmList as? List<Visitable<SomDetailAdapterFactory>>)?.let { addAll(it) }
    }

    private fun MutableList<Visitable<SomDetailAdapterFactory>>.includeProductBundles(
        bundle: List<SomDetailOrder.GetSomDetail.Details.Bundle>?,
        bundleIcon: String
    ) {
        addAll(getProductBundleList(bundle, bundleIcon))
    }

    private fun MutableList<Visitable<SomDetailAdapterFactory>>.includeProductNonBundles(
        nonBundle: List<SomDetailOrder.GetSomDetail.Details.Product>?,
        addOnInfo: SomDetailOrder.GetSomDetail.AddOnInfo?,
        addOnIcon: String,
        addOnLabel: String,
        addOnsExpandableState: List<String>
    ) {
        addAll(
            getProductNonBundleList(
                nonBundle,
                addOnInfo,
                addOnIcon,
                addOnLabel,
                addOnsExpandableState
            )
        )
    }

    private fun ArrayList<BaseProductUiModel>.includeProducts(
        products: List<SomDetailOrder.GetSomDetail.Details.Product>?,
        addOnIcon: String,
        addOnLabel: String,
        addOnsExpandableState: List<String>
    ) {
        products?.forEach { product ->
            val addOnsIdentifier = product.id + product.orderDetailId
            add(
                NonProductBundleUiModel(
                    product = product,
                    addOnSummaryUiModel = product.addOnSummary?.let { addOnSummary ->
                        com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel(
                            addOnIdentifier = addOnsIdentifier,
                            totalPriceText = addOnSummary.totalPriceStr,
                            addonsLogoUrl = addOnIcon,
                            addonsTitle = addOnLabel,
                            addonItemList = addOnSummary.addons.map { addon ->
                                val addOnNote = addon.metadata?.addOnNote
                                val infoLink = addon.metadata?.infoLink
                                com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel.AddonItemUiModel(
                                    priceText = addon.priceStr,
                                    quantity = addon.quantity,
                                    addonsId = addon.id,
                                    addOnsName = addon.name,
                                    type = addon.type,
                                    addOnsThumbnailUrl = addon.imageUrl,
                                    toStr = addOnNote?.to.orEmpty(),
                                    fromStr = addOnNote?.from.orEmpty(),
                                    message = addOnNote?.notes.orEmpty(),
                                    noteCopyable = true,
                                    providedByShopItself = true,
                                    infoLink = infoLink.orEmpty(),
                                    tips = addOnNote?.tips.orEmpty()
                                )
                            }
                        ).also {
                            it.isExpand = !addOnsExpandableState.contains(addOnsIdentifier)
                        }
                    }
                )
            )
        }
    }

    private fun ArrayList<BaseProductUiModel>.includeOrderAddOn(
        addOnInfo: SomDetailOrder.GetSomDetail.AddOnInfo?,
        addOnsExpandableState: List<String>
    ) {
        addOnInfo?.orderLevelAddOnSummary?.let { addOnSummary ->
            add(
                NonProductBundleUiModel(
                    addOnSummaryUiModel = com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel(
                        addOnIdentifier = addOnSummary.label,
                        totalPriceText = addOnSummary.totalPriceStr,
                        addonsLogoUrl = "",
                        addonsTitle = "",
                        addonItemList = addOnSummary.addons.map { addon ->
                            val addOnNote = addon.metadata?.addOnNote
                            val infoLink = addon.metadata?.infoLink
                            com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel.AddonItemUiModel(
                                priceText = addon.priceStr,
                                quantity = addon.quantity,
                                addonsId = addon.id,
                                addOnsName = addon.name,
                                type = addon.type,
                                addOnsThumbnailUrl = addon.imageUrl,
                                toStr = addOnNote?.to.orEmpty(),
                                fromStr = addOnNote?.from.orEmpty(),
                                message = addOnNote?.notes.orEmpty(),
                                noteCopyable = true,
                                providedByShopItself = true,
                                infoLink = infoLink.orEmpty(),
                                tips = addOnNote?.tips.orEmpty()
                            )
                        }
                    ).also {
                        it.isExpand = !addOnsExpandableState.contains(addOnSummary.label)
                    }
                )
            )
        }
    }

    private fun SomDetailOrder.GetSomDetail.mapToHeaderUiModel(): SomDetailHeader {
        return SomDetailHeader(
            statusCode,
            statusText,
            statusIndicatorColor,
            buyerRequestCancel.isRequestCancel,
            invoice,
            invoiceUrl,
            paymentDate,
            customer.name,
            deadline.text,
            deadline.color,
            deadline.style,
            listLabelInfo,
            orderId,
            shipment.awbUploadUrl,
            shipment.awbUploadProofText,
            bookingInfo.onlineBooking.bookingCode,
            bookingInfo.onlineBooking.state,
            bookingInfo.onlineBooking.barcodeType,
            warehouse.fullFillBy,
            flagOrderMeta.isWareHouse,
            tickerInfo
        )
    }

    private fun SomDetailOrder.GetSomDetail.mapToProductsHeaderUiModel(): SomDetailProducts {
        return SomDetailProducts(flagOrderMeta.isTopAds, flagOrderMeta.isBroadcastChat, flagOrderMeta.isAffiliate)
    }

    private fun SomDetailOrder.GetSomDetail.mapToProductsUiModel(addOnsExpandableState: List<String>)
            : List<Visitable<SomDetailAdapterFactory>> {
        return mutableListOf<Visitable<SomDetailAdapterFactory>>().apply {
            includeProductBmgms(
                details.bmgms,
                orderId,
                details.bmgmIcon,
                details.addOnIcon,
                details.addOnLabel,
                addOnsExpandableState
            )
            includeProductBundles(details.bundle, details.bundleIcon)
            includeProductNonBundles(
                details.nonBundle,
                addOnInfo,
                details.addOnIcon,
                details.addOnLabel,
                addOnsExpandableState
            )
        }
    }

    private fun SomDetailOrder.GetSomDetail.mapToShipmentUiModel(): SomDetailShipping {
        return SomDetailShipping(
            shippingName = shipment.name + " - " + shipment.productName,
            receiverName = receiver.name,
            receiverPhone = receiver.phone,
            receiverStreet = receiver.street,
            receiverDistrict = receiver.district + ", " + receiver.city + " " + receiver.postal,
            receiverProvince = receiver.province,
            isFreeShipping = flagOrderMeta.flagFreeShipping,
            driverPhoto = bookingInfo.driver.photo,
            driverName = bookingInfo.driver.name,
            driverPhone = bookingInfo.driver.phone,
            dropshipperName = dropshipper.name,
            dropshipperPhone = dropshipper.phone,
            driverLicense = bookingInfo.driver.licenseNumber,
            onlineBookingCode = bookingInfo.onlineBooking.bookingCode,
            onlineBookingState = bookingInfo.onlineBooking.state,
            onlineBookingMsg = bookingInfo.onlineBooking.message,
            onlineBookingMsgArray = bookingInfo.onlineBooking.messageArray,
            onlineBookingType = bookingInfo.onlineBooking.barcodeType,
            isRemoveAwb = onlineBooking.isRemoveInputAwb,
            awb = shipment.awb,
            awbTextColor = shipment.awbTextColor,
            isShippingPrinted = flagOrderMeta.isShippingPrinted,
            logisticInfo = logisticInfo,
            shipmentLogo = flagOrderMeta.shipmentLogo,
            courierInfo = shipment.courierInfo,
            shipmentTickerInfo = shipment.tickerInfo
        )
    }

    fun mapResponseToHeaderUiModel(
        response: SomDetailOrder.GetSomDetail?
    ): SomDetailData? {
        return response?.mapToHeaderUiModel()?.let {
            SomDetailData(it, SomConsts.DETAIL_HEADER_TYPE)
        }
    }

    fun mapResponseToProductsHeaderUiModel(
        response: SomDetailOrder.GetSomDetail?
    ): SomDetailData? {
        return response?.mapToProductsHeaderUiModel()?.let {
            SomDetailData(it, SomConsts.DETAIL_PRODUCTS_TYPE)
        }
    }

    fun mapResponseToProductsUiModels(
        response: SomDetailOrder.GetSomDetail?,
        addOnsExpandableState: List<String>
    ): List<Visitable<SomDetailAdapterFactory>> {
        return response?.mapToProductsUiModel(addOnsExpandableState).orEmpty()
    }

    fun mapResponseToShipmentUiModel(
        response: SomDetailOrder.GetSomDetail?
    ): SomDetailData? {
        return response?.mapToShipmentUiModel()?.let {
            SomDetailData(it, SomConsts.DETAIL_SHIPPING_TYPE)
        }
    }
}
