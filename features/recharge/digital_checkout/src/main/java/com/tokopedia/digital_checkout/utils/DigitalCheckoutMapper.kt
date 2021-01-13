package com.tokopedia.digital_checkout.utils

import com.tokopedia.digital_checkout.data.DigitalCartCrossSellingType
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData
import com.tokopedia.digital_checkout.data.model.AttributesDigitalData.PostPaidPopupAttribute
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigital
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData.CartItemDigitalWithTitle
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData

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
}