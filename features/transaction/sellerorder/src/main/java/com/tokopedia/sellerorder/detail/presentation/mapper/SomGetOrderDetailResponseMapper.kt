package com.tokopedia.sellerorder.detail.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.order_management_common.domain.data.ProductBenefit
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel.AddonItemUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder.GetSomDetail.AddOnInfo
import com.tokopedia.sellerorder.detail.data.model.SomDetailProducts
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory
import com.tokopedia.sellerorder.detail.presentation.model.BaseProductUiModel
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.SomDetailAddOnOrderLevelUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse.Data.InfoRequestPartialOrderFulfillment.Companion.STATUS_INITIAL
import com.tokopedia.order_management_common.R as order_management_commonR

object SomGetOrderDetailResponseMapper {

    private fun getBmgmList(
        bmgms: List<SomDetailOrder.GetSomDetail.Bmgm>?,
        orderId: String,
        bmgmIconUrl: String,
        addOnLabel: String,
        addOnIcon: String,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: MutableList<String>
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
                                totalPriceText = if (addOnSummary.totalPriceStr.isNotBlank()) {
                                    StringRes(
                                        order_management_commonR.string.om_add_on_collapsed_title_format,
                                        listOf(addOnSummary.totalPriceStr)
                                    )
                                } else {
                                    StringRes(Int.ZERO)
                                },
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
                                        tips = addOnNote?.tips.orEmpty(),
                                        orderId = "",
                                        orderDetailId = ""
                                    )
                                },
                                canExpandCollapse = true
                            ).also {
                                it.isExpand = !addOnsExpandableState.contains(addOnsIdentifier)
                            }
                        }
                    )
                },
                productBenefits = mapBmgmProductBenefit(
                    productBenefit = bmgm.productBenefit,
                    orderId = orderId,
                    bmgmId = bmgm.id,
                    expanded = !bmgmProductBenefitExpandableState.contains(bmgm.id)
                )
            )
        }.orEmpty()
    }

    private fun mapBmgmProductBenefit(
        productBenefit: ProductBenefit?,
        orderId: String,
        bmgmId: String,
        expanded: Boolean
    ): AddOnSummaryUiModel? {
        return productBenefit?.let {
            if (productBenefit.isValid()) {
                AddOnSummaryUiModel(
                    addOnIdentifier = bmgmId,
                    totalPriceText = StringRes(order_management_commonR.string.om_gwp_collapsed_title_format, listOf(productBenefit.orderDetail?.count().orZero())),
                    addonsLogoUrl = productBenefit.iconUrl,
                    addonsTitle = productBenefit.label,
                    addonItemList = mapBmgmProductBenefitItems(productBenefit.orderDetail, orderId),
                    canExpandCollapse = true
                ).apply { isExpand = expanded }
            } else null
        }
    }

    private fun mapBmgmProductBenefitItems(
        orderDetails: List<ProductBenefit.OrderDetail>?,
        orderId: String
    ): List<AddOnSummaryUiModel.AddonItemUiModel> {
        return orderDetails?.map { orderDetail ->
            AddOnSummaryUiModel.AddonItemUiModel(
                priceText = orderDetail.totalPriceText,
                quantity = orderDetail.quantity,
                addonsId = orderDetail.productId.toString(),
                addOnsName = orderDetail.productName,
                type = String.EMPTY,
                addOnsThumbnailUrl = orderDetail.thumbnail,
                toStr = String.EMPTY,
                fromStr = String.EMPTY,
                message = String.EMPTY,
                descriptionExpanded = false,
                noteCopyable = false,
                providedByShopItself = true,
                infoLink = String.EMPTY,
                tips = String.EMPTY,
                orderId = orderId,
                orderDetailId = orderDetail.orderDetailId.toString()
            )
        }.orEmpty()
    }

    private fun getProductBundleList(
        bundleList: List<SomDetailOrder.GetSomDetail.Details.Bundle>?,
        bundleIcon: String,
        addOnLabel: String,
        addOnIcon: String,
        addOnsExpandableState: List<String>
    ): List<ProductBundleUiModel> {
        return bundleList?.map { bundle ->
            ProductBundleUiModel(
                bundleId = bundle.bundleId,
                bundleIcon = bundleIcon,
                bundleName = bundle.bundleName,
                bundlePrice = Utils.parseRupiah(bundle.bundlePrice),
                bundleSubTotal = Utils.parseRupiah(bundle.bundleSubtotalPrice),
                products = bundle.orderDetail.map {
                    val addOnsIdentifier = it.id + it.orderDetailId
                    ProductBundleUiModel.ProductUiModel(
                        detail = SomDetailOrder.GetSomDetail.Details.Product(
                            id = it.id,
                            orderDetailId = it.orderDetailId,
                            name = it.name,
                            thumbnail = it.thumbnail,
                            priceText = it.priceText,
                            quantity = it.quantity,
                            note = it.note
                        ),
                        addOnSummaryUiModel = it.addOnSummary?.let { addOnSummary ->
                            com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel(
                                addOnIdentifier = addOnsIdentifier,
                                totalPriceText = if (addOnSummary.totalPriceStr.isNotBlank()) {
                                    StringRes(
                                        order_management_commonR.string.om_add_on_collapsed_title_format,
                                        listOf(addOnSummary.totalPriceStr)
                                    )
                                } else {
                                    StringRes(Int.ZERO)
                                },
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
                                        tips = addOnNote?.tips.orEmpty(),
                                        orderId = "",
                                        orderDetailId = ""
                                    )
                                },
                                canExpandCollapse = true
                            ).also {
                                it.isExpand = !addOnsExpandableState.contains(addOnsIdentifier)
                            }
                        }
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
            includeOrderAddOn(addOnInfo, addOnIcon, addOnLabel)
        }
    }

    private fun MutableList<Visitable<SomDetailAdapterFactory>>.includeProductBmgms(
        bmgms: List<SomDetailOrder.GetSomDetail.Bmgm>?,
        orderId: String,
        bmgmIcon: String,
        addonIcon: String,
        addonLabel: String,
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: MutableList<String>
    ) {
        val bmgmList = getBmgmList(
            bmgms = bmgms,
            orderId = orderId,
            bmgmIconUrl = bmgmIcon,
            addOnLabel = addonLabel,
            addOnIcon = addonIcon,
            addOnsExpandableState = addOnsExpandableState,
            bmgmProductBenefitExpandableState = bmgmProductBenefitExpandableState
        )
        (bmgmList as? List<Visitable<SomDetailAdapterFactory>>)?.let { addAll(it) }
    }

    private fun MutableList<Visitable<SomDetailAdapterFactory>>.includeProductBundles(
        bundle: List<SomDetailOrder.GetSomDetail.Details.Bundle>?,
        bundleIcon: String,
        details: SomDetailOrder.GetSomDetail.Details,
        addOnsExpandableState: List<String>
    ) {
        addAll(getProductBundleList(bundle, bundleIcon, details.addOnLabel, details.addOnIcon, addOnsExpandableState))
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
                            totalPriceText = if (addOnSummary.totalPriceStr.isNotBlank()) {
                                StringRes(
                                    order_management_commonR.string.om_add_on_collapsed_title_format,
                                    listOf(addOnSummary.totalPriceStr)
                                )
                            } else {
                                StringRes(Int.ZERO)
                            },
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
                                    tips = addOnNote?.tips.orEmpty(),
                                    orderId = "",
                                    orderDetailId = ""
                                )
                            },
                            canExpandCollapse = true
                        ).also {
                            it.isExpand = !addOnsExpandableState.contains(addOnsIdentifier)
                        }
                    }
                )
            )
        }
    }

    private fun ArrayList<BaseProductUiModel>.includeOrderAddOn(
        addOnInfo: AddOnInfo?,
        addOnIcon: String,
        addOnLabel: String
    ) {
        addOnInfo?.orderLevelAddOnSummary?.let { addOnSummary ->
            add(
                SomDetailAddOnOrderLevelUiModel(
                    addOnSummaryUiModel = AddOnSummaryUiModel(
                        addOnIdentifier = addOnSummary.label,
                        totalPriceText = if (addOnSummary.totalPriceStr.isNotBlank()) {
                            StringRes(
                                order_management_commonR.string.om_add_on_collapsed_title_format,
                                listOf(addOnSummary.totalPriceStr)
                            )
                        } else {
                            StringRes(Int.ZERO)
                        },
                        addonsLogoUrl = addOnIcon,
                        addonsTitle = addOnLabel,
                        addonItemList = addOnSummary.addons.map { addon ->
                            val addOnNote = addon.metadata?.addOnNote
                            val infoLink = addon.metadata?.infoLink
                            AddonItemUiModel(
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
                                tips = addOnNote?.tips.orEmpty(),
                                orderId = "",
                                orderDetailId = ""
                            )
                        },
                        canExpandCollapse = false
                    )
                )
            )
        }
    }

    private fun SomDetailOrder.GetSomDetail.mapToHeaderUiModel(): SomDetailHeader {
        return SomDetailHeader(
            statusCode = statusCode,
            statusText = statusText,
            statusIndicatorColor = statusIndicatorColor,
            isBuyerRequestCancel = buyerRequestCancel.isRequestCancel,
            invoice = invoice,
            invoiceUrl = invoiceUrl,
            paymentDate = paymentDate,
            custName = customer.name,
            deadlineText = deadline.text,
            deadlineColor = deadline.color,
            deadlineStyle = deadline.style,
            listLabelOrder = listLabelInfo,
            orderId = orderId,
            awbUploadUrl = shipment.awbUploadUrl,
            awbUploadProofText = shipment.awbUploadProofText,
            onlineBookingCode = bookingInfo.onlineBooking.bookingCode,
            onlineBookingState = bookingInfo.onlineBooking.state,
            onlineBookingType = bookingInfo.onlineBooking.barcodeType,
            fullFillBy = warehouse.fullFillBy,
            isWarehouse = flagOrderMeta.isWareHouse,
            tickerInfo = tickerInfo,
            pofStatus = pofData?.pofStatus ?: STATUS_INITIAL
        )
    }

    private fun SomDetailOrder.GetSomDetail.mapToProductsHeaderUiModel(): SomDetailProducts {
        return SomDetailProducts(flagOrderMeta.isTopAds, flagOrderMeta.isBroadcastChat, flagOrderMeta.isAffiliate)
    }

    private fun SomDetailOrder.GetSomDetail.mapToProductsUiModel(
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: MutableList<String>
    )
            : List<Visitable<SomDetailAdapterFactory>> {
        return mutableListOf<Visitable<SomDetailAdapterFactory>>().apply {
            includeProductBmgms(
                details.bmgms,
                orderId,
                details.bmgmIcon,
                details.addOnIcon,
                details.addOnLabel,
                addOnsExpandableState,
                bmgmProductBenefitExpandableState
            )
            includeProductBundles(details.bundle, details.bundleIcon, details, addOnsExpandableState)
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
            shippingTitle = shipment.title,
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
        addOnsExpandableState: List<String>,
        bmgmProductBenefitExpandableState: MutableList<String>
    ): List<Visitable<SomDetailAdapterFactory>> {
        return response?.mapToProductsUiModel(addOnsExpandableState, bmgmProductBenefitExpandableState).orEmpty()
    }

    fun mapResponseToShipmentUiModel(
        response: SomDetailOrder.GetSomDetail?
    ): SomDetailData? {
        return response?.mapToShipmentUiModel()?.let {
            SomDetailData(it, SomConsts.DETAIL_SHIPPING_TYPE)
        }
    }
}
