package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.google.gson.Gson
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PaidSectionInfo
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PreOrder
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.PromoStacking
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesDetailData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.RatesTickerData
import com.tokopedia.logisticcart.shipping.model.DynamicPriceModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherModel
import com.tokopedia.logisticcart.shipping.model.PaidSectionInfoUiModel
import com.tokopedia.logisticcart.shipping.model.PreOrderModel
import com.tokopedia.logisticcart.shipping.model.ProductShipmentDetailModel
import com.tokopedia.logisticcart.shipping.model.ShipmentTickerActionType
import com.tokopedia.logisticcart.shipping.model.ShipmentTickerModel
import com.tokopedia.logisticcart.shipping.model.ShipmentTickerPosition
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.unifycomponents.ticker.Ticker
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
class ShippingDurationConverter @Inject constructor() {
    companion object {
        private const val COD_TRUE_VAL = 1
        private const val TICKER_INFO_TYPE = "info"
        private const val TICKER_WARNING_TYPE = "warning"
        private const val TICKER_ERROR_TYPE = "danger"
        private const val TICKER_ACTION_WEB = "link"
        private const val TICKER_ACTION_APPLINK = "applink"
    }

    fun convertModel(ratesData: RatesData?): ShippingRecommendationData {
        val shippingRecommendationData = ShippingRecommendationData()

        // Check response not null
        if (ratesData?.ratesDetailData != null) {
            // Check if has error
            if (ratesData.ratesDetailData.error.errorMessage.isNotEmpty()) {
                shippingRecommendationData.errorMessage =
                    ratesData.ratesDetailData.error.errorMessage
                shippingRecommendationData.errorId = ratesData.ratesDetailData.error.errorId
            }

            // Check has service / duration list
            if (ratesData.ratesDetailData.services.isNotEmpty()) {
                // Setting up for Logistic Promo
                shippingRecommendationData.logisticPromo = convertToPromoModel(
                    ratesData.ratesDetailData.listPromoStacking.firstOrNull(),
                    true
                )

                // Setting up for List of Logistic Promo
                shippingRecommendationData.listLogisticPromo =
                    ratesData.ratesDetailData.listPromoStacking.mapIndexedNotNull { index, promo -> convertToPromoModel(promo, index == ratesData.ratesDetailData.listPromoStacking.size - 1) }

                // Setting up for Logistic Pre Order
                shippingRecommendationData.productShipmentDetailModel =
                    convertShipmentDetailData(ratesData.ratesDetailData)

                // Has service / duration list
                shippingRecommendationData.shippingDurationUiModels =
                    convertShippingDuration(ratesData.ratesDetailData)

                // paid section title section
                shippingRecommendationData.paidSectionInfoUiModel = convertPaidSectionModel(ratesData.ratesDetailData.paidSectionInfo)

                // bottom ticker
                shippingRecommendationData.tickers = convertShipmentTicker(ratesData.ratesDetailData.tickers)
            }
        }
        return shippingRecommendationData
    }

    private fun convertShipmentTicker(tickers: List<RatesTickerData>): List<ShipmentTickerModel> {
        return tickers.map {
            val type: Int = when (it.tickerType) {
                TICKER_INFO_TYPE -> Ticker.TYPE_ANNOUNCEMENT
                TICKER_WARNING_TYPE -> Ticker.TYPE_WARNING
                TICKER_ERROR_TYPE -> Ticker.TYPE_ERROR
                else -> Ticker.TYPE_ANNOUNCEMENT
            }
            val action: ShipmentTickerActionType? = when (it.tickerAction.type) {
                TICKER_ACTION_WEB -> {
                    ShipmentTickerActionType.WebUrl(it.tickerAction.webUrl)
                }
                TICKER_ACTION_APPLINK -> {
                    ShipmentTickerActionType.AppUrl(it.tickerAction.appUrl)
                }
                else -> {
                    null
                }
            }
            val position: ShipmentTickerPosition? = when (it.position) {
                ShipmentTickerPosition.FREE_SHIPPING.key -> {
                    ShipmentTickerPosition.FREE_SHIPPING
                }
                else -> {
                    null
                }
            }
            ShipmentTickerModel(
                type = type,
                actionType = action,
                position = position,
                actionLabel = it.tickerAction.label,
                description = it.content,
                title = it.title
            )
        }
    }

    private fun convertPaidSectionModel(paidSectionInfo: PaidSectionInfo): PaidSectionInfoUiModel {
        return PaidSectionInfoUiModel(
            showCollapsableIcon = paidSectionInfo.isCollapsed,
            isCollapsed = paidSectionInfo.isCollapsed,
            title = paidSectionInfo.labelText
        )
    }

    private fun convertShipmentDetailData(ratesDetailData: RatesDetailData): ProductShipmentDetailModel? {
        return if (ratesDetailData.weight.isEmpty() && ratesDetailData.originData.cityName.isEmpty() && !ratesDetailData.preOrder.display) {
            null
        } else {
            ProductShipmentDetailModel(
                weight = ratesDetailData.weight,
                originCity = ratesDetailData.originData.cityName,
                preOrderModel = convertToPreOrderModel(ratesDetailData.preOrder)
            )
        }
    }

    private fun convertShippingDuration(ratesDetailData: RatesDetailData): List<ShippingDurationUiModel> {
        val serviceDataList = ratesDetailData.services
        val ratesId = ratesDetailData.ratesId
        // Check if has blackbox info
        var blackboxInfo = ""
        if (ratesDetailData.info.blackboxInfo.textInfo.isNotEmpty()) {
            blackboxInfo = ratesDetailData.info.blackboxInfo.textInfo
        }
        val shippingDurationUiModels: MutableList<ShippingDurationUiModel> = ArrayList()
        for (serviceData in serviceDataList) {
            val shippingDurationUiModel = ShippingDurationUiModel()
            shippingDurationUiModel.serviceData = serviceData
            shippingDurationUiModel.isShowShippingInformation =
                isCourierInstantOrSameday(serviceData.serviceId)
            shippingDurationUiModel.etaErrorCode = serviceData.texts.errorCode
            val shippingCourierUiModels = convertToShippingCourierViewModel(
                shippingDurationUiModel,
                serviceData.products,
                ratesId,
                blackboxInfo,
                convertShipmentDetailData(ratesDetailData)
            )
            shippingDurationUiModel.serviceData.isUiRatesHidden =
                shippingDurationUiModel.serviceData.isUiRatesHidden || (shippingDurationUiModel.serviceData.selectedShipperProductId == 0 && shippingCourierUiModels.all { it.productData.isUiRatesHidden })
            shippingDurationUiModel.shippingCourierViewModelList = shippingCourierUiModels
            if (shippingCourierUiModels.isNotEmpty()) {
                shippingDurationUiModels.add(shippingDurationUiModel)
            }
            if (serviceData.error.errorMessage.isNotEmpty()) {
                shippingDurationUiModel.errorMessage = serviceData.error.errorMessage
            }
            shippingDurationUiModel.isCodAvailable = serviceData.codData.isCod == COD_TRUE_VAL
            shippingDurationUiModel.codText = serviceData.codData.codText
            val merchantVoucherData = serviceData.merchantVoucherData
            val merchantVoucherModel = MerchantVoucherModel(
                merchantVoucherData.isMvc,
                merchantVoucherData.mvcTitle,
                merchantVoucherData.mvcLogo,
                merchantVoucherData.mvcErrorMessage
            )
            shippingDurationUiModel.merchantVoucherModel = merchantVoucherModel
            val featuresData = serviceData.features
            val dynamicPriceModel = DynamicPriceModel(
                featuresData.dynamicPricing.textLabel
            )
            shippingDurationUiModel.dynamicPriceModel = dynamicPriceModel
        }
        return shippingDurationUiModels
    }

    private fun isCourierInstantOrSameday(shipperId: Int): Boolean {
        val ids = CourierConstant.INSTANT_SAMEDAY_DURATION
        for (id in ids) {
            if (shipperId == id) return true
        }
        return false
    }

    private fun convertToShippingCourierViewModel(
        shippingDurationUiModel: ShippingDurationUiModel,
        productDataList: List<ProductData>,
        ratesId: String,
        blackboxInfo: String,
        productShipmentDetailModel: ProductShipmentDetailModel?
    ): List<ShippingCourierUiModel> {
        val shippingCourierUiModels: MutableList<ShippingCourierUiModel> = ArrayList()
        for (productData in productDataList) {
            addShippingCourierViewModel(
                shippingDurationUiModel,
                ratesId,
                shippingCourierUiModels,
                productData,
                blackboxInfo,
                productShipmentDetailModel
            )
        }
        return shippingCourierUiModels
    }

    private fun addShippingCourierViewModel(
        shippingDurationUiModel: ShippingDurationUiModel,
        ratesId: String,
        shippingCourierUiModels: MutableList<ShippingCourierUiModel>,
        productData: ProductData,
        blackboxInfo: String,
        productShipmentDetailModel: ProductShipmentDetailModel?
    ) {
        val shippingCourierUiModel = ShippingCourierUiModel()
        shippingCourierUiModel.productData = productData
        shippingCourierUiModel.blackboxInfo = blackboxInfo
        shippingCourierUiModel.serviceData = shippingDurationUiModel.serviceData
        shippingCourierUiModel.ratesId = ratesId
        shippingCourierUiModel.productShipmentDetailModel = productShipmentDetailModel
        shippingCourierUiModel.productData.isRecommend =
            productData.isRecommend && (productData.error.errorMessage.isEmpty())
        shippingCourierUiModels.add(shippingCourierUiModel)
    }

    private fun convertToPromoModel(
        promo: PromoStacking?,
        isLast: Boolean
    ): LogisticPromoUiModel? {
        if (promo == null || promo.isPromo != 1) return null
        val applied = promo.isApplied == 1
        val gson = Gson()
        return LogisticPromoUiModel(
            promo.promoCode,
            promo.title,
            promo.benefitDesc,
            promo.shipperName,
            promo.serviceId,
            promo.shipperId,
            promo.shipperProductId,
            promo.shipperDesc,
            promo.shipperDisableText,
            promo.promoTncHtml,
            applied,
            promo.imageUrl,
            promo.discontedRate,
            promo.shippingRate,
            promo.benefitAmount,
            promo.isDisabled,
            promo.isHideShipperName,
            promo.cod,
            promo.eta,
            promo.texts.bottomSheet,
            promo.texts.chosenCourier,
            promo.texts.tickerCourier,
            promo.isBebasOngkirExtra,
            promo.texts.bottomSheetDescription,
            promo.texts.promoMessage,
            promo.texts.titlePromoMessage,
            gson.toJson(promo.freeShippingMetadata),
            promo.freeShippingMetadata.benefitClass,
            promo.freeShippingMetadata.shippingSubsidy,
            promo.boCampaignId,
            promo.quotaMessage,
            promo.imageUrlChosen,
            promo.texts.orderMessage,
            isLast
        )
    }

    private fun convertToPreOrderModel(preOrder: PreOrder?): PreOrderModel? {
        return if (preOrder == null) {
            null
        } else {
            PreOrderModel(
                preOrder.header,
                preOrder.label,
                preOrder.display
            )
        }
    }
}
