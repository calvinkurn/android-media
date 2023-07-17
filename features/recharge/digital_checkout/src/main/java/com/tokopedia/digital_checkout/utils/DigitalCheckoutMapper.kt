package com.tokopedia.digital_checkout.utils

import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData.PostPaidPopupAttribute
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.response.checkout.RechargeCheckoutResponse
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView

/**
 * @author by jessica on 11/01/21
 */

object DigitalCheckoutMapper {

    const val COUPON_NOT_ACTIVE = 0
    const val VOUCHER_IS_COUPON = 1

    fun mapToPromoData(cartInfo: CartDigitalInfoData): PromoData? {
        var promoData: PromoData? = null
        val isEnableVoucher = cartInfo.attributes.isEnableVoucher

        cartInfo.attributes.autoApplyVoucher.let {
            if (!isEnableVoucher) {
                promoData = PromoData(
                    description = it.discountAmountLabel,
                    amount = it.discountAmount.toLong(),
                    state = TickerCheckoutView.State.INACTIVE
                )
            } else if (it.isSuccess && !(cartInfo.attributes.isCouponActive == COUPON_NOT_ACTIVE && it.isCoupon == VOUCHER_IS_COUPON)) {
                promoData = PromoData(
                    title = it.title,
                    description = it.messageSuccess,
                    promoCode = it.code,
                    typePromo = it.isCoupon,
                    amount = it.discountAmount.toLong(),
                    state = TickerCheckoutView.State.ACTIVE
                )
            }
        }
        return promoData
    }

    fun mapGetCartToCartDigitalInfoData(
        responseRechargeGetCart: RechargeGetCart.Response,
        isSpecialProduct: Boolean
    ): CartDigitalInfoData {
        try {
            val cartDigitalInfoData = CartDigitalInfoData()

            cartDigitalInfoData.isSpecialProduct = isSpecialProduct

            responseRechargeGetCart.response.mainnInfo.let { mainInfos ->
                cartDigitalInfoData.mainInfo = mainInfos.map {
                    CartItemDigital(it.label, it.value)
                }
            }

            responseRechargeGetCart.response.additionalInfo.let { additionalInfos ->
                cartDigitalInfoData.additionalInfos = additionalInfos.map {
                    CartItemDigitalWithTitle(
                        it.title,
                        it.detail.map { detail ->
                            CartItemDigital(detail.label, detail.value)
                        }
                    )
                }
            }

            val attributesDigital = AttributesDigitalData()
            responseRechargeGetCart.response.let { attributes ->
                attributesDigital.categoryName = attributes.categoryName
                attributesDigital.operatorName = attributes.operatorName
                attributesDigital.clientNumber = attributes.clientNumber
                attributesDigital.icon = attributes.icon
                attributesDigital.isInstantCheckout = attributes.isInstantCheckout
                attributesDigital.isNeedOtp = attributes.isOtpRequired
                attributesDigital.smsState = attributes.sms_state
                attributesDigital.price = attributes.priceText
                attributesDigital.pricePlain = attributes.price
                attributesDigital.isEnableVoucher = attributes.enableVoucher
                if (attributes.isCouponActive) {
                    attributesDigital.isCouponActive =
                        1
                } else {
                    attributesDigital.isCouponActive = 0
                }
                attributesDigital.voucherAutoCode = attributes.voucher
                attributesDigital.adminFee = attributes.adminFee
                attributesDigital.isAdminFeeIncluded = attributes.isAdminFeeIncluded

                val userInputPrice = responseRechargeGetCart.response.openPaymentConfig
                if (userInputPrice.maxPayment != 0.0 && userInputPrice.minPayment != 0.0) {
                    val userInputPriceDigital = AttributesDigitalData.UserInputPriceDigital()
                    userInputPriceDigital.maxPaymentPlain = userInputPrice.maxPayment
                    userInputPriceDigital.minPaymentPlain = userInputPrice.minPayment
                    userInputPriceDigital.minPayment = userInputPrice.minPaymentText
                    userInputPriceDigital.maxPayment = userInputPrice.maxPaymentText
                    attributesDigital.userInputPrice = userInputPriceDigital
                    attributesDigital.isOpenAmount = true
                }

                val entity = attributes.autoApply
                val applyVoucher = AttributesDigitalData.CartAutoApplyVoucher()
                applyVoucher.code = entity.code
                applyVoucher.isSuccess = entity.success
                applyVoucher.discountAmount = entity.discountAmount
                applyVoucher.discountAmountLabel = entity.discountAmountLabel
                applyVoucher.isCoupon = entity.isCoupon
                applyVoucher.promoId = entity.promoId.toLongOrZero()
                applyVoucher.title = entity.titleDescription
                applyVoucher.messageSuccess = entity.messageSuccess
                attributesDigital.autoApplyVoucher = applyVoucher

                if (responseRechargeGetCart.response.popUp.content.isNotEmpty() &&
                    responseRechargeGetCart.response.popUp.action.yesButtonTitle.isNotEmpty()
                ) {
                    val postPaidPopup = responseRechargeGetCart.response.popUp
                    val postPaidPopupAttribute = PostPaidPopupAttribute()
                    postPaidPopupAttribute.title = postPaidPopup.title
                    postPaidPopupAttribute.content = postPaidPopup.content
                    postPaidPopupAttribute.imageUrl = postPaidPopup.imageUrl
                    postPaidPopupAttribute.confirmButtonTitle = postPaidPopup.action.yesButtonTitle
                    attributesDigital.postPaidPopupAttribute = postPaidPopupAttribute
                }

                attributesDigital.defaultPromoTab = attributes.defaultPromo
                attributesDigital.userId = attributes.userId
            }

            responseRechargeGetCart.response.run {
                val subscriptions = fintechProduct.filter {
                    it.transactionType == DigitalCheckoutConst.FintechProduct.AUTO_DEBIT
                }
                cartDigitalInfoData.isSubscribed =
                    subscriptions.isNotEmpty() && subscriptions[0].checkBoxDisabled
                attributesDigital.fintechProduct = fintechProduct
            }

            cartDigitalInfoData.attributes = attributesDigital
            cartDigitalInfoData.id = responseRechargeGetCart.response.id
            cartDigitalInfoData.productId = responseRechargeGetCart.response.productId
            cartDigitalInfoData.isInstantCheckout =
                responseRechargeGetCart.response.isInstantCheckout
            cartDigitalInfoData.isNeedOtp = responseRechargeGetCart.response.isOtpRequired
            cartDigitalInfoData.smsState = responseRechargeGetCart.response.sms_state
            cartDigitalInfoData.title = responseRechargeGetCart.response.title
            cartDigitalInfoData.channelId = responseRechargeGetCart.response.channelId
            cartDigitalInfoData.collectionPointId =
                responseRechargeGetCart.response.collectionPointId
            cartDigitalInfoData.collectionPointVersion =
                responseRechargeGetCart.response.collectionPointVersion

            return cartDigitalInfoData
        } catch (e: Exception) {
            throw Throwable(e.message, e)
        }
    }

    fun mapToPaymentPassData(responseCheckoutData: RechargeCheckoutResponse): PaymentPassData {
        val paymentPassData = PaymentPassData()

        paymentPassData.callbackFailedUrl = responseCheckoutData.data.attributes.callbackUrlFailed
        paymentPassData.callbackSuccessUrl = responseCheckoutData.data.attributes.callbackUrlSuccess
        paymentPassData.redirectUrl = responseCheckoutData.data.attributes.redirectUrl
        paymentPassData.queryString = responseCheckoutData.data.attributes.queryString
        paymentPassData.transactionId = responseCheckoutData.data.attributes.parameter.transactionId

        return paymentPassData
    }

    fun buildCheckoutData(
        cartDigitalInfoData: CartDigitalInfoData,
        accessToken: String?,
        requestCheckoutDataParameter: DigitalCheckoutDataParameter
    ): DigitalCheckoutDataParameter {
        // not override digitalCheckoutDataParameter's fintechproduct, subscription check and input price (for keeping don't keep state)
        requestCheckoutDataParameter.cartId = cartDigitalInfoData.id
        requestCheckoutDataParameter.accessToken = accessToken ?: ""
        requestCheckoutDataParameter.walletRefreshToken = ""
        requestCheckoutDataParameter.ipAddress = DeviceUtil.localIpAddress
        requestCheckoutDataParameter.relationId = cartDigitalInfoData.id
        requestCheckoutDataParameter.relationType = cartDigitalInfoData.type
        requestCheckoutDataParameter.transactionAmount = cartDigitalInfoData.attributes.pricePlain
        requestCheckoutDataParameter.userAgent = DeviceUtil.userAgentForApiCall
        requestCheckoutDataParameter.isNeedOtp = cartDigitalInfoData.isNeedOtp

        return requestCheckoutDataParameter
    }
}
