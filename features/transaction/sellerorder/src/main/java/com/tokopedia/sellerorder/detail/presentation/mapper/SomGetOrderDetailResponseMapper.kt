package com.tokopedia.sellerorder.detail.presentation.mapper

import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomDetailProducts
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.model.AddOnSummaryUiModel
import com.tokopedia.sellerorder.detail.presentation.model.AddOnUiModel
import com.tokopedia.sellerorder.detail.presentation.model.BaseProductUiModel
import com.tokopedia.sellerorder.detail.presentation.model.NonProductBundleUiModel
import com.tokopedia.sellerorder.detail.presentation.model.ProductBundleUiModel

object SomGetOrderDetailResponseMapper {

    private fun getProductBundleList(
        bundleList: List<SomDetailOrder.Data.GetSomDetail.Details.Bundle>?,
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
                    SomDetailOrder.Data.GetSomDetail.Details.Product(
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
        products: List<SomDetailOrder.Data.GetSomDetail.Details.Product>?,
        addOnInfo: SomDetailOrder.Data.GetSomDetail.AddOnInfo?,
        addOnIcon: String,
        addOnLabel: String
    ): List<BaseProductUiModel> {
        return arrayListOf<BaseProductUiModel>().apply {
            includeProducts(products, addOnIcon, addOnLabel)
            includeOrderAddOn(addOnInfo)
        }
    }

    private fun MutableList<BaseProductUiModel>.includeProductBundles(
        bundle: List<SomDetailOrder.Data.GetSomDetail.Details.Bundle>?,
        bundleIcon: String
    ) {
        addAll(getProductBundleList(bundle, bundleIcon))
    }

    private fun MutableList<BaseProductUiModel>.includeProductNonBundles(
        nonBundle: List<SomDetailOrder.Data.GetSomDetail.Details.Product>?,
        addOnInfo: SomDetailOrder.Data.GetSomDetail.AddOnInfo?,
        addOnIcon: String,
        addOnLabel: String
    ) {
        addAll(getProductNonBundleList(nonBundle, addOnInfo, addOnIcon, addOnLabel))
    }

    private fun ArrayList<BaseProductUiModel>.includeProducts(
        products: List<SomDetailOrder.Data.GetSomDetail.Details.Product>?,
        addOnIcon: String,
        addOnLabel: String
    ) {
        products?.forEach { product ->
            add(
                NonProductBundleUiModel(
                    product = product,
                    addOnSummary = product.addOnSummary?.let { addOnSummary ->
                        AddOnSummaryUiModel(
                            addons = addOnSummary.addons.map {
                                AddOnUiModel(addOn = it, providedByBranchShop = false)
                            },
                            total = addOnSummary.total,
                            totalPrice = addOnSummary.totalPrice,
                            totalPriceStr = addOnSummary.totalPriceStr,
                            totalQuantity = addOnSummary.totalQuantity,
                            iconUrl = addOnIcon,
                            label = addOnLabel
                        )
                    }
                )
            )
        }
    }

    private fun ArrayList<BaseProductUiModel>.includeOrderAddOn(
        addOnInfo: SomDetailOrder.Data.GetSomDetail.AddOnInfo?
    ) {
        addOnInfo?.orderLevelAddOnSummary?.let { addOnSummary ->
            add(
                NonProductBundleUiModel(
                    addOnSummary = AddOnSummaryUiModel(
                        addons = addOnSummary.addons.map {
                            AddOnUiModel(addOn = it, providedByBranchShop = true)
                        },
                        total = addOnSummary.total,
                        totalPrice = addOnSummary.totalPrice,
                        totalPriceStr = addOnSummary.totalPriceStr,
                        totalQuantity = addOnSummary.totalQuantity,
                        iconUrl = addOnInfo.iconUrl,
                        label = addOnInfo.label
                    )
                )
            )
        }
    }

    private fun SomDetailOrder.Data.GetSomDetail.mapToHeaderUiModel(): SomDetailHeader {
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

    private fun SomDetailOrder.Data.GetSomDetail.mapToProductsHeaderUiModel(): SomDetailProducts {
        return SomDetailProducts(flagOrderMeta.isTopAds, flagOrderMeta.isBroadcastChat)
    }

    private fun SomDetailOrder.Data.GetSomDetail.mapToProductsUiModel(): List<BaseProductUiModel> {
        return mutableListOf<BaseProductUiModel>().apply {
            includeProductBundles(details.bundle, details.bundleIcon)
            includeProductNonBundles(details.nonBundle, addOnInfo, details.addOnIcon, details.addOnLabel)
        }
    }

    private fun SomDetailOrder.Data.GetSomDetail.mapToShipmentUiModel(): SomDetailShipping {
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
            logisticInfo = logisticInfo
        )
    }

    fun mapResponseToHeaderUiModel(
        response: SomDetailOrder.Data.GetSomDetail?
    ): SomDetailData? {
        return response?.mapToHeaderUiModel()?.let {
            SomDetailData(it, SomConsts.DETAIL_HEADER_TYPE)
        }
    }

    fun mapResponseToProductsHeaderUiModel(
        response: SomDetailOrder.Data.GetSomDetail?
    ): SomDetailData? {
        return response?.mapToProductsHeaderUiModel()?.let {
            SomDetailData(it, SomConsts.DETAIL_PRODUCTS_TYPE)
        }
    }

    fun mapResponseToProductsUiModels(
        response: SomDetailOrder.Data.GetSomDetail?
    ): List<BaseProductUiModel> {
        return response?.mapToProductsUiModel().orEmpty()
    }

    fun mapResponseToShipmentUiModel(
        response: SomDetailOrder.Data.GetSomDetail?
    ): SomDetailData? {
        return response?.mapToShipmentUiModel()?.let {
            SomDetailData(it, SomConsts.DETAIL_SHIPPING_TYPE)
        }
    }
}