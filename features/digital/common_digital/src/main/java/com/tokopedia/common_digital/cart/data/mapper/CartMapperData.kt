package com.tokopedia.common_digital.cart.data.mapper

import com.tokopedia.common_digital.cart.data.entity.response.AdditionalInfo
import com.tokopedia.common_digital.cart.data.entity.response.AutoApplyVoucher
import com.tokopedia.common_digital.cart.data.entity.response.Detail
import com.tokopedia.common_digital.cart.data.entity.response.MainInfo
import com.tokopedia.common_digital.cart.data.entity.response.RelationshipsCart
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData
import com.tokopedia.common_digital.cart.data.entity.response.ResponseCheckoutData
import com.tokopedia.common_digital.cart.domain.model.PostPaidPopupAttribute
import com.tokopedia.common_digital.cart.view.model.cart.AttributesDigital
import com.tokopedia.common_digital.cart.view.model.cart.CartAdditionalInfo
import com.tokopedia.common_digital.cart.view.model.cart.CartAutoApplyVoucher
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData
import com.tokopedia.common_digital.cart.view.model.cart.CartItemDigital
import com.tokopedia.common_digital.cart.view.model.cart.CrossSellingConfig
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData
import com.tokopedia.common_digital.cart.view.model.cart.Relation
import com.tokopedia.common_digital.cart.view.model.cart.RelationData
import com.tokopedia.common_digital.cart.view.model.cart.Relationships
import com.tokopedia.common_digital.cart.view.model.cart.UserInputPriceDigital
import com.tokopedia.common_digital.common.MapperDataException
import com.tokopedia.common_digital.product.data.response.PostPaidPopup

import java.util.ArrayList

/**
 * Created by Rizky on 27/08/18.
 */
class CartMapperData : ICartMapperData {

    @Throws(MapperDataException::class)
    override fun transformCartInfoData(responseCartData: ResponseCartData): CartDigitalInfoData {
        try {
            val cartDigitalInfoData = CartDigitalInfoData()
            val cartItemDigitalList = ArrayList<CartItemDigital>()
            val cartAdditionalInfoList = ArrayList<CartAdditionalInfo>()
            for (mainInfo in responseCartData.attributes!!.mainInfo!!) {
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

            if (responseCartData.attributes!!.crossSellingConfig != null) {
                val crossSellingConfig = CrossSellingConfig()
                crossSellingConfig.isSkipAble = responseCartData.attributes!!.crossSellingConfig!!.isSkipAble
                crossSellingConfig.headerTitle = responseCartData.attributes!!.crossSellingConfig!!.wording!!.headerTitle
                crossSellingConfig.bodyTitle = responseCartData.attributes!!.crossSellingConfig!!.wording!!.bodyTitle
                crossSellingConfig.bodyContentBefore = responseCartData.attributes!!.crossSellingConfig!!.wording!!.bodyContentBefore
                crossSellingConfig.bodyContentAfter = responseCartData.attributes!!.crossSellingConfig!!.wording!!.bodyContentAfter
                crossSellingConfig.checkoutButtonText = responseCartData.attributes!!.crossSellingConfig!!.wording!!.checkoutButtonText
                cartDigitalInfoData.crossSellingConfig = crossSellingConfig
            }

            cartDigitalInfoData.crossSellingType = responseCartData.attributes!!.crossSellingType
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
    override fun transformInstantCheckoutData(
            responseCheckoutData: ResponseCheckoutData
    ): InstantCheckoutData {
        try {
            val checkoutDigitalData = InstantCheckoutData()
            checkoutDigitalData.failedCallbackUrl = responseCheckoutData.attributes!!.callbackUrlFailed
            checkoutDigitalData.successCallbackUrl = responseCheckoutData.attributes!!.callbackUrlSuccess
            checkoutDigitalData.redirectUrl = responseCheckoutData.attributes!!.redirectUrl
            checkoutDigitalData.stringQuery = responseCheckoutData.attributes!!.queryString
            if (responseCheckoutData.attributes!!.parameter != null) {
                checkoutDigitalData.transactionId = responseCheckoutData.attributes!!.parameter!!.transactionId
            }
            return checkoutDigitalData
        } catch (e: Exception) {
            throw MapperDataException(e.message, e)
        }

    }

}
