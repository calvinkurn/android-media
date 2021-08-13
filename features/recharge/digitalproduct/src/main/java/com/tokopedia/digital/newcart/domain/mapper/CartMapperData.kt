package com.tokopedia.digital.newcart.domain.mapper

import com.tokopedia.digital.common.exception.MapperDataException
import com.tokopedia.digital.newcart.constant.DigitalCartCrossSellingType
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCancelVoucherData
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCartData
import com.tokopedia.digital.newcart.data.entity.response.cart.ResponseCheckoutData
import com.tokopedia.digital.newcart.data.entity.response.voucher.ResponseVoucherData
import com.tokopedia.digital.newcart.domain.model.CheckoutDigitalData
import com.tokopedia.digital.newcart.domain.model.VoucherAttributeDigital
import com.tokopedia.digital.newcart.domain.model.VoucherDigital
import com.tokopedia.digital.newcart.presentation.model.cart.*
import java.util.*

/**
 * Created by Rizky on 27/08/18.
 */
class CartMapperData : ICartMapperData {

    @Throws(MapperDataException::class)
    override fun transformCartInfoData(responseCartData: ResponseCartData?): CartDigitalInfoData {
        try {
            val cartDigitalInfoData = CartDigitalInfoData()
            val cartItemDigitalList = ArrayList<CartItemDigital>()
            val cartAdditionalInfoList = ArrayList<CartAdditionalInfo>()
            for (mainInfo in responseCartData?.attributes!!.mainInfo!!) {
                cartItemDigitalList.add(
                        CartItemDigital(mainInfo.label, mainInfo.value)
                )
            }
            for (additionalInfo in responseCartData.attributes!!.additionalInfo!!) {
                val cartItemDigitalList1 = ArrayList<CartItemDigital>()
                for (detail in additionalInfo.detail!!) {
                    cartItemDigitalList1.add(CartItemDigital(detail.label, detail.value))
                }
                cartAdditionalInfoList.add(
                        CartAdditionalInfo(additionalInfo.title, cartItemDigitalList1)
                )
            }
            val attributesDigital = AttributesDigital()
            attributesDigital.categoryName = responseCartData.attributes!!.categoryName
            attributesDigital.operatorName = responseCartData.attributes!!.operatorName
            attributesDigital.clientNumber = responseCartData.attributes!!.clientNumber
            attributesDigital.icon = responseCartData.attributes!!.icon
            attributesDigital.isInstantCheckout = responseCartData.attributes!!.isInstantCheckout
            attributesDigital.isNeedOtp = responseCartData.attributes!!.isNeedOtp
            attributesDigital.smsState = responseCartData.attributes!!.smsState
            attributesDigital.price = responseCartData.attributes!!.price
            attributesDigital.pricePlain = responseCartData.attributes!!.pricePlain
            attributesDigital.isEnableVoucher = responseCartData.attributes!!.isEnableVoucher
            attributesDigital.isCouponActive = responseCartData.attributes!!.isCouponActive
            attributesDigital.voucherAutoCode = responseCartData.attributes!!.voucherAutoCode
            if (responseCartData.attributes!!.userInputPrice != null) {
                val userInputPriceDigital = UserInputPriceDigital()
                userInputPriceDigital.maxPaymentPlain = responseCartData.attributes!!.userInputPrice!!.maxPaymentPlain
                userInputPriceDigital.minPaymentPlain = responseCartData.attributes!!.userInputPrice!!.minPaymentPlain
                userInputPriceDigital.minPayment = responseCartData.attributes!!
                        .userInputPrice!!.minPayment
                userInputPriceDigital.maxPayment = responseCartData.attributes!!
                        .userInputPrice!!.maxPayment
                attributesDigital.userInputPrice = userInputPriceDigital
            }

            if (responseCartData.attributes!!.autoApply != null) {
                val entity = responseCartData.attributes!!.autoApply
                val applyVoucher = CartAutoApplyVoucher()
                applyVoucher.code = entity!!.code
                applyVoucher.isSuccess = entity.isSuccess
                applyVoucher.discountAmount = entity.discountAmount
                applyVoucher.isCoupon = entity.isCoupon
                applyVoucher.promoId = entity.promoId
                applyVoucher.title = entity.title
                applyVoucher.messageSuccess = entity.messageSuccess
                attributesDigital.autoApplyVoucher = applyVoucher
            }

            if (responseCartData.attributes!!.postPaidPopUp != null &&
                    responseCartData.attributes!!.postPaidPopUp!!.action != null &&
                    responseCartData.attributes!!.postPaidPopUp!!.action!!.confirmAction != null) {
                val postPaidPopup = responseCartData.attributes!!.postPaidPopUp
                val postPaidPopupAttribute = PostPaidPopupAttribute()
                postPaidPopupAttribute.title = postPaidPopup!!.title
                postPaidPopupAttribute.content = postPaidPopup.content
                postPaidPopupAttribute.imageUrl = postPaidPopup.imageUrl
                postPaidPopupAttribute.confirmButtonTitle = postPaidPopup.action!!.confirmAction!!.title
                attributesDigital.postPaidPopupAttribute = postPaidPopupAttribute
            }

            attributesDigital.defaultPromoTab = responseCartData.attributes!!.defaultPromoTab

            attributesDigital.userId = responseCartData.attributes!!.userId

            val relationshipsResponse = responseCartData.relationships

            val relationDataProduct = RelationData()
            relationDataProduct.type = relationshipsResponse!!.product!!.data!!.type
            relationDataProduct.id = relationshipsResponse.product!!.data!!.id

            val relationDataCategory = RelationData()
            relationDataCategory.type = relationshipsResponse.category!!.data!!.type
            relationDataCategory.id = relationshipsResponse.category!!.data!!.id

            val relationDataOperator = RelationData()
            relationDataOperator.type = relationshipsResponse.operator!!.data!!.type
            relationDataOperator.id = relationshipsResponse.operator!!.data!!.id

            val relationships = Relationships()
            relationships.relationCategory = Relation(relationDataCategory)
            relationships.relationOperator = Relation(relationDataOperator)
            relationships.relationProduct = Relation(relationDataProduct)

            responseCartData.attributes?.run {
                cartDigitalInfoData.crossSellingType = crossSellingType
                crossSellingConfig?.run {
                    val crossSellingConfig = CrossSellingConfig()
                    crossSellingConfig.isSkipAble = isSkipAble
                    crossSellingConfig.isChecked = isChecked

                    val crossSellingWording = if (cartDigitalInfoData.crossSellingType == DigitalCartCrossSellingType.SUBSCRIBED) {
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

            cartDigitalInfoData.additionalInfos = cartAdditionalInfoList
            cartDigitalInfoData.attributes = attributesDigital
            cartDigitalInfoData.id = responseCartData.id
            cartDigitalInfoData.mainInfo = cartItemDigitalList
            cartDigitalInfoData.isInstantCheckout = responseCartData.attributes!!.isInstantCheckout
            cartDigitalInfoData.isNeedOtp = responseCartData.attributes!!.isNeedOtp
            cartDigitalInfoData.smsState = responseCartData.attributes!!.smsState
            cartDigitalInfoData.title = responseCartData.attributes!!.title
            cartDigitalInfoData.type = responseCartData.type
            cartDigitalInfoData.relationships = relationships

            return cartDigitalInfoData
        } catch (e: Exception) {
            throw MapperDataException(e.message, e)
        }
    }

    @Throws(MapperDataException::class)
    override fun transformCancelVoucherData(responseCancelVoucherData: ResponseCancelVoucherData?): Boolean {
        try {
            responseCancelVoucherData?.let { response ->
                response.attributes?.let {
                    return it.success ?: false
                }
            }
            return false
        } catch (e: Exception) {
            throw MapperDataException(e.message, e)
        }
    }

    @Throws(MapperDataException::class)
    override fun transformVoucherDigitalData(responseVoucherData: ResponseVoucherData): VoucherDigital? {
        return try {
            val voucherDigital = VoucherDigital()
            val voucherAttributeDigital = VoucherAttributeDigital()
            voucherAttributeDigital.message = responseVoucherData.attributes.message
            voucherAttributeDigital.discountAmountPlain = responseVoucherData.attributes.discountAmountPlain
                    .toLong()
            voucherAttributeDigital.cashbackAmpountPlain = responseVoucherData.attributes.cashbackAmountPlain
                    .toLong()
            voucherAttributeDigital.userId = responseVoucherData.attributes.userId.toLong()
            voucherAttributeDigital.voucherCode = responseVoucherData.attributes.voucherCode
            voucherDigital.attributeVoucher = voucherAttributeDigital
            voucherDigital
        } catch (e: java.lang.Exception) {
            throw MapperDataException(e.message, e)
        }
    }

    @Throws(MapperDataException::class)
    override fun transformCheckoutData(responseCheckoutData: ResponseCheckoutData): CheckoutDigitalData? {
        return try {
            val checkoutDigitalData = CheckoutDigitalData()
            checkoutDigitalData.failedCallbackUrl = responseCheckoutData.attributes!!.callbackUrlFailed
            checkoutDigitalData.successCallbackUrl = responseCheckoutData.attributes!!.callbackUrlSuccess
            checkoutDigitalData.redirectUrl = responseCheckoutData.attributes!!.redirectUrl
            checkoutDigitalData.stringQuery = responseCheckoutData.attributes!!.queryString
            checkoutDigitalData.thanksUrl = responseCheckoutData.attributes!!.thanksUrl
            checkoutDigitalData.transactionId = responseCheckoutData.attributes!!.parameter!!.transactionId
            checkoutDigitalData
        } catch (e: java.lang.Exception) {
            throw MapperDataException(e.message, e)
        }
    }
}
