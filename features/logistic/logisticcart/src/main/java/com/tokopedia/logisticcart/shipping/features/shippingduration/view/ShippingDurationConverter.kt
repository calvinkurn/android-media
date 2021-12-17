package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.*
import com.tokopedia.logisticcart.shipping.model.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
class ShippingDurationConverter @Inject constructor() {
    companion object {
        private const val COD_TRUE_VAL = 1
    }

    fun convertModel(ratesData: RatesData?): ShippingRecommendationData {
        val shippingRecommendationData = ShippingRecommendationData()

        // Check response not null
        if (ratesData != null && ratesData.ratesDetailData != null) {

            // Check if has error
            if (ratesData.ratesDetailData.error != null &&
                    !ratesData.ratesDetailData.error.errorMessage.isNullOrEmpty()) {
                shippingRecommendationData.errorMessage = ratesData.ratesDetailData.error.errorMessage
                shippingRecommendationData.errorId = ratesData.ratesDetailData.error.errorId
            }

            // Check has service / duration list
            if (ratesData.ratesDetailData.services != null &&
                    ratesData.ratesDetailData.services.isNotEmpty()) {

                // Setting up for Logistic Promo
                shippingRecommendationData.logisticPromo = convertToPromoModel(ratesData.ratesDetailData.promoStacking)

                // Setting up for Logistic Pre Order
                shippingRecommendationData.preOrderModel = convertToPreOrderModel(ratesData.ratesDetailData.preOrder)

                // Has service / duration list
                shippingRecommendationData.shippingDurationUiModels = convertShippingDuration(ratesData.ratesDetailData)
            }
        }
        return shippingRecommendationData
    }

    private fun convertShippingDuration(ratesDetailData: RatesDetailData): List<ShippingDurationUiModel> {
        val serviceDataList = ratesDetailData.services
        val ratesId = ratesDetailData.ratesId
        val isPromoStackingApplied = isPromoStackingApplied(ratesDetailData)
        // Check if has blackbox info
        var blackboxInfo = ""
        if (ratesDetailData.info != null && ratesDetailData.info.blackboxInfo != null &&
                !ratesDetailData.info.blackboxInfo.textInfo.isNullOrEmpty()) {
            blackboxInfo = ratesDetailData.info.blackboxInfo.textInfo
        }
        val shippingDurationUiModels: MutableList<ShippingDurationUiModel> = ArrayList()
        for (serviceData in serviceDataList) {
            val shippingDurationUiModel = ShippingDurationUiModel()
            shippingDurationUiModel.serviceData = serviceData
            shippingDurationUiModel.isShowShippingInformation = isCourierInstantOrSameday(serviceData.serviceId)
            shippingDurationUiModel.etaErrorCode = serviceData.texts.errorCode
            val shippingCourierUiModels = convertToShippingCourierViewModel(shippingDurationUiModel,
                    serviceData.products, ratesId, blackboxInfo, convertToPreOrderModel(ratesDetailData.preOrder))
            shippingDurationUiModel.shippingCourierViewModelList = shippingCourierUiModels
            if (shippingCourierUiModels.isNotEmpty()) {
                shippingDurationUiModels.add(shippingDurationUiModel)
            }
            if (serviceData.error != null && !serviceData.error.errorMessage.isNullOrEmpty()) {
                if (serviceData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                    serviceData.texts.textRangePrice = serviceData.error.errorMessage
                } else {
                    shippingDurationUiModel.errorMessage = serviceData.error.errorMessage
                }
            }
            if (serviceData.codData != null) {
                shippingDurationUiModel.isCodAvailable = serviceData.codData.isCod == COD_TRUE_VAL
                shippingDurationUiModel.codText = serviceData.codData.codText
            }
            if (serviceData.merchantVoucherData != null) {
                val merchantVoucherData = serviceData.merchantVoucherData
                val merchantVoucherModel = MerchantVoucherModel(
                        merchantVoucherData.isMvc,
                        merchantVoucherData.mvcTitle,
                        merchantVoucherData.mvcLogo,
                        merchantVoucherData.mvcErrorMessage
                )
                shippingDurationUiModel.merchantVoucherModel = merchantVoucherModel
            }
            if (serviceData.features != null) {
                val featuresData = serviceData.features
                val dynamicPriceModel = DynamicPriceModel(
                        featuresData.dynamicPricing.textLabel
                )
                shippingDurationUiModel.dynamicPriceModel = dynamicPriceModel
            }
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

    private fun convertToShippingCourierViewModel(shippingDurationUiModel: ShippingDurationUiModel,
                                                  productDataList: List<ProductData>,
                                                  ratesId: String,
                                                  blackboxInfo: String,
                                                  preOrderModel: PreOrderModel?): List<ShippingCourierUiModel> {
        val shippingCourierUiModels: MutableList<ShippingCourierUiModel> = ArrayList()
        for (productData in productDataList) {
            addShippingCourierViewModel(shippingDurationUiModel, ratesId,
                    shippingCourierUiModels, productData, blackboxInfo, preOrderModel)
        }
        return shippingCourierUiModels
    }

    private fun addShippingCourierViewModel(shippingDurationUiModel: ShippingDurationUiModel,
                                            ratesId: String,
                                            shippingCourierUiModels: MutableList<ShippingCourierUiModel>,
                                            productData: ProductData, blackboxInfo: String,
                                            preOrderModel: PreOrderModel?) {
        val shippingCourierUiModel = ShippingCourierUiModel()
        shippingCourierUiModel.productData = productData
        shippingCourierUiModel.blackboxInfo = blackboxInfo
        shippingCourierUiModel.serviceData = shippingDurationUiModel.serviceData
        shippingCourierUiModel.ratesId = ratesId
        shippingCourierUiModel.preOrderModel = preOrderModel
        shippingCourierUiModels.add(shippingCourierUiModel)
    }

    private fun convertToPromoModel(promo: PromoStacking?): LogisticPromoUiModel? {
        if (promo == null || promo.isPromo != 1) return null
        val applied = promo.isApplied == 1
        return LogisticPromoUiModel(
                promo.promoCode, promo.title, promo.benefitDesc,
                promo.shipperName, promo.serviceId, promo.shipperId,
                promo.shipperProductId, promo.shipperDesc, promo.shipperDisableText,
                promo.promoTncHtml, applied, promo.imageUrl, promo.discontedRate,
                promo.shippingRate, promo.benefitAmount, promo.isDisabled, promo.isHideShipperName,
                promo.cod, promo.eta, promo.texts.bottomSheet, promo.texts.chosenCourier,
                promo.texts.tickerCourier, promo.isBebasOngkirExtra)
    }

    private fun convertToPreOrderModel(preOrder: PreOrder?): PreOrderModel? {
        return if (preOrder == null) null else PreOrderModel(
                preOrder.header,
                preOrder.label,
                preOrder.display
        )
    }

    private fun isPromoStackingApplied(ratesDetailData: RatesDetailData): Boolean {
        return if (ratesDetailData.promoStacking == null) false else ratesDetailData.promoStacking.isApplied == 1
    }
}