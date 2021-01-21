package com.tokopedia.digital_checkout.utils

import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.digital_checkout.data.DigitalCartCrossSellingType
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData.PostPaidPopupAttribute
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.request.CheckoutRelationships
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.request.RequestBodyCheckout
import com.tokopedia.digital_checkout.data.response.ResponseCheckout
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import com.tokopedia.track.TrackApp

/**
 * @author by jessica on 11/01/21
 */

object DigitalCheckoutMapper {
    fun mapToCartDigitalInfoData(responseCartData: ResponseCartData): CartDigitalInfoData {
        try {
            val cartDigitalInfoData = CartDigitalInfoData()

            responseCartData.attributes?.mainInfo?.let { mainInfos ->
                cartDigitalInfoData.mainInfo = mainInfos.map {
                    CartItemDigital(it.label ?: "", it.value ?: "")
                }
            }

            responseCartData.attributes?.additionalInfo?.let { additionalInfos ->
                cartDigitalInfoData.additionalInfos = additionalInfos.map {
                    CartItemDigitalWithTitle(it.title ?: "",
                            it.detail?.map { detail ->
                                CartItemDigital(detail.label ?: "", detail.value ?: "")
                            } ?: listOf())
                }
            }

            val attributesDigital = AttributesDigitalData()
            responseCartData.attributes?.let { attributes ->
                attributesDigital.categoryName = attributes.categoryName
                attributesDigital.operatorName = attributes.operatorName
                attributesDigital.clientNumber = attributes.clientNumber
                attributesDigital.icon = attributes.icon
                attributesDigital.isInstantCheckout = attributes.isInstantCheckout
                attributesDigital.isNeedOtp = attributes.isNeedOtp
                attributesDigital.smsState = attributes.smsState
                attributesDigital.price = attributes.price
                attributesDigital.pricePlain = attributes.pricePlain
                attributesDigital.isEnableVoucher = attributes.isEnableVoucher
                attributesDigital.isCouponActive = attributes.isCouponActive
                attributesDigital.voucherAutoCode = attributes.voucherAutoCode

                if (attributes.userInputPrice != null) {
                    val userInputPriceDigital = AttributesDigitalData.UserInputPriceDigital()
                    userInputPriceDigital.maxPaymentPlain = attributes.userInputPrice?.maxPaymentPlain
                            ?: 0
                    userInputPriceDigital.minPaymentPlain = attributes.userInputPrice?.minPaymentPlain
                            ?: 0
                    userInputPriceDigital.minPayment = attributes.userInputPrice?.minPayment
                    userInputPriceDigital.maxPayment = attributes.userInputPrice?.maxPayment
                    attributesDigital.userInputPrice = userInputPriceDigital
                }

                if (attributes.autoApply != null) {
                    val entity = attributes.autoApply
                    val applyVoucher = AttributesDigitalData.CartAutoApplyVoucher()
                    applyVoucher.code = entity!!.code
                    applyVoucher.isSuccess = entity.isSuccess
                    applyVoucher.discountAmount = entity.discountAmount
                    applyVoucher.isCoupon = entity.isCoupon
                    applyVoucher.promoId = entity.promoId
                    applyVoucher.title = entity.title
                    applyVoucher.messageSuccess = entity.messageSuccess
                    attributesDigital.autoApplyVoucher = applyVoucher
                }

                if (attributes.postPaidPopUp != null &&
                        attributes.postPaidPopUp!!.action != null &&
                        attributes.postPaidPopUp!!.action!!.confirmAction != null) {
                    val postPaidPopup = responseCartData.attributes?.postPaidPopUp
                    val postPaidPopupAttribute = PostPaidPopupAttribute()
                    postPaidPopupAttribute.title = postPaidPopup?.title
                    postPaidPopupAttribute.content = postPaidPopup?.content
                    postPaidPopupAttribute.imageUrl = postPaidPopup?.imageUrl
                    postPaidPopupAttribute.confirmButtonTitle = postPaidPopup?.action?.confirmAction?.title
                    attributesDigital.postPaidPopupAttribute = postPaidPopupAttribute
                }

                attributesDigital.defaultPromoTab = attributes.defaultPromoTab
                attributesDigital.userId = attributes.userId
            }

            responseCartData.relationships?.let { relationship ->
                cartDigitalInfoData.relationProduct?.type = relationship.product?.data?.type
                cartDigitalInfoData.relationProduct?.id = relationship.product?.data?.id

                cartDigitalInfoData.relationCategory?.type = relationship.category?.data?.type
                cartDigitalInfoData.relationCategory?.id = relationship.category?.data?.id

                cartDigitalInfoData.relationOperator?.type = relationship.operator?.data?.type
                cartDigitalInfoData.relationOperator?.id = relationship.operator?.data?.id
            }

            responseCartData.attributes?.run {
                cartDigitalInfoData.crossSellingType = crossSellingType
                crossSellingConfig?.run {
                    val crossSellingConfig = CartDigitalInfoData.CrossSellingConfig()
                    crossSellingConfig.isSkipAble = isSkipAble
                    crossSellingConfig.isChecked = isChecked

                    val crossSellingWording = if (cartDigitalInfoData.crossSellingType
                            == DigitalCartCrossSellingType.SUBSCRIBED.id) {
                        wordingIsSubscribed
                    } else {
                        wording
                    }
                    crossSellingWording?.run {
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
            cartDigitalInfoData.id = responseCartData.id
            cartDigitalInfoData.isInstantCheckout = responseCartData.attributes!!.isInstantCheckout
            cartDigitalInfoData.isNeedOtp = responseCartData.attributes!!.isNeedOtp
            cartDigitalInfoData.smsState = responseCartData.attributes!!.smsState
            cartDigitalInfoData.title = responseCartData.attributes!!.title
            cartDigitalInfoData.type = responseCartData.type

            return cartDigitalInfoData

        } catch (e: Exception) {
            throw Throwable(e.message, e)
        }
    }

    fun mapGetCartToCartDigitalInfoData(responseRechargeGetCart: RechargeGetCart.Response): CartDigitalInfoData {
        try {
            val cartDigitalInfoData = CartDigitalInfoData()

            responseRechargeGetCart.response.mainnInfo.let { mainInfos ->
                cartDigitalInfoData.mainInfo = mainInfos.map {
                    CartItemDigital(it.label ?: "", it.value ?: "")
                }
            }

            responseRechargeGetCart.response.additionalInfo.let { additionalInfos ->
                cartDigitalInfoData.additionalInfos = additionalInfos.map {
                    CartItemDigitalWithTitle(it.title ?: "",
                            it.detail?.map { detail ->
                                CartItemDigital(detail.label ?: "", detail.value ?: "")
                            } ?: listOf())
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

                val userInputPrice = responseRechargeGetCart.response.openPaymentConfig
                val userInputPriceDigital = AttributesDigitalData.UserInputPriceDigital()
                userInputPriceDigital.maxPaymentPlain = userInputPrice.maxPayment
                userInputPriceDigital.minPaymentPlain = userInputPrice.minPayment
                userInputPriceDigital.minPayment = userInputPrice.minPaymentText
                userInputPriceDigital.maxPayment = userInputPrice.maxPaymentText
                attributesDigital.userInputPrice = userInputPriceDigital


                if (attributes.autoApply != null) {
                    val entity = attributes.autoApply
                    val applyVoucher = AttributesDigitalData.CartAutoApplyVoucher()
                    applyVoucher.code = entity.code
                    applyVoucher.isSuccess = entity.success
                    applyVoucher.discountAmount = entity.discountAmount
                    applyVoucher.isCoupon = entity.isCoupon
                    applyVoucher.promoId = entity.promoId.toLong()
                    applyVoucher.title = entity.titleDescription
                    applyVoucher.messageSuccess = entity.messageSuccess
                    attributesDigital.autoApplyVoucher = applyVoucher
                }

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
                attributesDigital.userId = attributes.userId.toString()
            }

            responseRechargeGetCart.response.run {
                cartDigitalInfoData.crossSellingType = crossSellingType
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
                    crossSellingWording?.run {
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
        responseCheckoutData.attributes?.run {
            paymentPassData.callbackFailedUrl = callbackUrlFailed ?: ""
            paymentPassData.callbackSuccessUrl = callbackUrlSuccess ?: ""
            paymentPassData.redirectUrl = redirectUrl ?: ""
            paymentPassData.queryString = queryString ?: ""
            paymentPassData.transactionId = parameter?.transactionId ?: ""
        }
        return paymentPassData
    }

    fun buildCheckoutData(cartDigitalInfoData: CartDigitalInfoData, accessToken: String?): DigitalCheckoutDataParameter {
        val digitalCheckoutDataParameter = DigitalCheckoutDataParameter()
        digitalCheckoutDataParameter.cartId = cartDigitalInfoData.id
        digitalCheckoutDataParameter.accessToken = accessToken
        digitalCheckoutDataParameter.walletRefreshToken = ""
        digitalCheckoutDataParameter.ipAddress = DeviceUtil.localIpAddress
        digitalCheckoutDataParameter.relationId = cartDigitalInfoData.id
        digitalCheckoutDataParameter.relationType = cartDigitalInfoData.type
        digitalCheckoutDataParameter.transactionAmount = cartDigitalInfoData.attributes?.pricePlain
                ?: 0
        digitalCheckoutDataParameter.userAgent = DeviceUtil.userAgentForApiCall
        digitalCheckoutDataParameter.isNeedOtp = cartDigitalInfoData.isNeedOtp
        return digitalCheckoutDataParameter
    }

    fun getRequestBodyCheckout(checkoutData: DigitalCheckoutDataParameter,
                               digitalIdentifierParam: RequestBodyIdentifier): RequestBodyCheckout {
        val requestBodyCheckout = RequestBodyCheckout()
        requestBodyCheckout.type = "checkout"

        val attributes = RequestBodyCheckout.AttributesCheckout()
        attributes.voucherCode = checkoutData.voucherCode
        attributes.transactionAmount = checkoutData.transactionAmount
        attributes.ipAddress = checkoutData.ipAddress
        attributes.userAgent = checkoutData.userAgent
        attributes.identifier = digitalIdentifierParam
        val getTrackClientId: String = TrackApp.getInstance().gtm.clientIDString
        attributes.clientId = getTrackClientId
        attributes.appsFlyer = DeviceUtil.getAppsFlyerIdentifierParam(
                TrackApp.getInstance().appsFlyer.uniqueId,
                "")
        requestBodyCheckout.attributes = attributes
        requestBodyCheckout.relationships = CheckoutRelationships(
                CheckoutRelationships.Cart(
                        CheckoutRelationships.Cart.Data(
                                checkoutData.relationType, checkoutData.relationId
                        )))
        return requestBodyCheckout
    }

}