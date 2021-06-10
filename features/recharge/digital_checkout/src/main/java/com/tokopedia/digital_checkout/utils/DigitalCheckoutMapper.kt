package com.tokopedia.digital_checkout.utils

import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_checkout.data.DigitalCartCrossSellingType
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData.PostPaidPopupAttribute
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.request.CheckoutRelationships
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.request.RequestBodyCheckout
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.track.TrackApp

/**
 * @author by jessica on 11/01/21
 */

object DigitalCheckoutMapper {

    fun mapToPromoData(cartInfo: CartDigitalInfoData): PromoData? {
        var promoData: PromoData? = null
        cartInfo.attributes.autoApplyVoucher.let {
            if (it.isSuccess && !(cartInfo.attributes.isCouponActive == 0 && it.isCoupon == 1)) {
                promoData = PromoData(title = it.title,
                        description = it.messageSuccess,
                        promoCode = it.code,
                        typePromo = it.isCoupon,
                        amount = it.discountAmount.toInt(),
                        state = TickerCheckoutView.State.ACTIVE)
            }
        }
        return promoData
    }

    fun mapGetCartToCartDigitalInfoData(responseRechargeGetCart: RechargeGetCart.Response): CartDigitalInfoData {
        try {
            val cartDigitalInfoData = CartDigitalInfoData()

            responseRechargeGetCart.response.mainnInfo.let { mainInfos ->
                cartDigitalInfoData.mainInfo = mainInfos.map {
                    CartItemDigital(it.label, it.value)
                }
            }

            responseRechargeGetCart.response.additionalInfo.let { additionalInfos ->
                cartDigitalInfoData.additionalInfos = additionalInfos.map {
                    CartItemDigitalWithTitle(it.title,
                            it.detail.map { detail ->
                                CartItemDigital(detail.label, detail.value)
                            })
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
                if (attributes.isCouponActive) attributesDigital.isCouponActive = 1 else attributesDigital.isCouponActive = 0
                attributesDigital.voucherAutoCode = attributes.voucher
                attributesDigital.adminFee = attributes.adminFee.toFloat()

                val userInputPrice = responseRechargeGetCart.response.openPaymentConfig
                if (userInputPrice.maxPayment != 0.0 && userInputPrice.minPayment != 0.0) {
                    val userInputPriceDigital = AttributesDigitalData.UserInputPriceDigital()
                    userInputPriceDigital.maxPaymentPlain = userInputPrice.maxPayment
                    userInputPriceDigital.minPaymentPlain = userInputPrice.minPayment
                    userInputPriceDigital.minPayment = userInputPrice.minPaymentText
                    userInputPriceDigital.maxPayment = userInputPrice.maxPaymentText
                    attributesDigital.userInputPrice = userInputPriceDigital
                }

                val entity = attributes.autoApply
                val applyVoucher = AttributesDigitalData.CartAutoApplyVoucher()
                applyVoucher.code = entity.code
                applyVoucher.isSuccess = entity.success
                applyVoucher.discountAmount = entity.discountAmount
                applyVoucher.isCoupon = entity.isCoupon
                applyVoucher.promoId = entity.promoId.toLongOrZero()
                applyVoucher.title = entity.titleDescription
                applyVoucher.messageSuccess = entity.messageSuccess
                attributesDigital.autoApplyVoucher = applyVoucher

                if (responseRechargeGetCart.response.popUp.content.isNotEmpty() &&
                        responseRechargeGetCart.response.popUp.action.yesButtonTitle.isNotEmpty()) {
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
                cartDigitalInfoData.crossSellingType = crossSellingType
                cartDigitalInfoData.showSubscriptionsView = crossSellingType == DigitalCartCrossSellingType.MYBILLS.id ||
                        crossSellingType == DigitalCartCrossSellingType.SUBSCRIBED.id
                crossSellingConfig.run {
                    val crossSellingConfig = CartDigitalInfoData.CrossSellingConfig()
                    crossSellingConfig.isSkipAble = canBeSkipped
                    crossSellingConfig.isChecked = isChecked

                    val crossSellingWording = if (cartDigitalInfoData.crossSellingType
                            == DigitalCartCrossSellingType.SUBSCRIBED.id) {
                        wordingIsSubscribe
                    } else {
                        wording
                    }
                    crossSellingWording.run {
                        crossSellingConfig.headerTitle = headerTitle
                        crossSellingConfig.bodyTitle = bodyTitle
                        crossSellingConfig.bodyContentBefore = bodyContentBefore
                        crossSellingConfig.bodyContentAfter = bodyContentAfter
                        crossSellingConfig.checkoutButtonText = checkoutButtonText
                        cartDigitalInfoData.crossSellingConfig = crossSellingConfig
                    }
                }
                attributesDigital.fintechProduct = fintechProduct
            }

            cartDigitalInfoData.attributes = attributesDigital
            cartDigitalInfoData.id = responseRechargeGetCart.response.id
            cartDigitalInfoData.productId = responseRechargeGetCart.response.productId
            cartDigitalInfoData.isInstantCheckout = responseRechargeGetCart.response.isInstantCheckout
            cartDigitalInfoData.isNeedOtp = responseRechargeGetCart.response.isOtpRequired
            cartDigitalInfoData.smsState = responseRechargeGetCart.response.sms_state
            cartDigitalInfoData.title = responseRechargeGetCart.response.title

            return cartDigitalInfoData

        } catch (e: Exception) {
            throw Throwable(e.message, e)
        }
    }

    fun mapToPaymentPassData(responseCheckoutData: ResponseCheckout): PaymentPassData {
        val paymentPassData = PaymentPassData()
        responseCheckoutData.attributes.run {
            paymentPassData.callbackFailedUrl = callbackUrlFailed ?: ""
            paymentPassData.callbackSuccessUrl = callbackUrlSuccess ?: ""
            paymentPassData.redirectUrl = redirectUrl ?: ""
            paymentPassData.queryString = queryString ?: ""
            paymentPassData.transactionId = parameter?.transactionId ?: ""
        }
        return paymentPassData
    }

    fun buildCheckoutData(cartDigitalInfoData: CartDigitalInfoData, accessToken: String?,
                          requestCheckoutDataParameter: DigitalCheckoutDataParameter): DigitalCheckoutDataParameter {
        //not override digitalCheckoutDataParameter's fintechproduct, subscription check and input price (for keeping don't keep state)
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

    fun getRequestBodyCheckout(checkoutData: DigitalCheckoutDataParameter,
                               digitalIdentifierParam: RequestBodyIdentifier,
                               fintechProduct: FintechProduct?): RequestBodyCheckout {
        val requestBodyCheckout = RequestBodyCheckout()
        requestBodyCheckout.type = DigitalCheckoutConst.RequestBodyParams.REQUEST_BODY_CHECKOUT_TYPE

        val attributes = RequestBodyCheckout.AttributesCheckout()
        attributes.voucherCode = checkoutData.voucherCode
        attributes.transactionAmount = checkoutData.transactionAmount.toLong()
        attributes.ipAddress = checkoutData.ipAddress
        attributes.userAgent = checkoutData.userAgent
        attributes.identifier = digitalIdentifierParam

        try {
            val getTrackClientId: String = TrackApp.getInstance().gtm.clientIDString
            attributes.clientId = getTrackClientId

            attributes.appsFlyer = DeviceUtil.getAppsFlyerIdentifierParam(
                    TrackApp.getInstance().appsFlyer.uniqueId,
                    TrackApp.getInstance().appsFlyer.googleAdId)
        } catch (e: Throwable) {
        }

        attributes.subscribe = checkoutData.isSubscriptionChecked

        val fintechProductsCheckout = mutableListOf<RequestBodyCheckout.FintechProductCheckout>()
        checkoutData.fintechProducts.values.forEach { fintech ->
            fintechProductsCheckout.add(RequestBodyCheckout.FintechProductCheckout(
                    transactionType = fintech.transactionType,
                    tierId = fintech.tierId.toIntOrZero(),
                    userId = attributes.identifier.userId?.toLongOrNull() ?: 0,
                    fintechAmount = fintech.fintechAmount.toLong(),
                    fintechPartnerAmount = fintech.fintechPartnerAmount.toLong(),
                    productName = fintech.info.title
            ))
        }
        attributes.fintechProduct = fintechProductsCheckout

        requestBodyCheckout.attributes = attributes
        requestBodyCheckout.relationships = CheckoutRelationships(
                CheckoutRelationships.Cart(
                        CheckoutRelationships.Cart.Data(
                                checkoutData.relationType, checkoutData.relationId
                        )))
        return requestBodyCheckout
    }

}