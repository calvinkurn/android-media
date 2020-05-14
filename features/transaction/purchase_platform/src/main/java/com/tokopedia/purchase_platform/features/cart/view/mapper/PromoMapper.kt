package com.tokopedia.purchase_platform.features.cart.view.mapper

import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-11.
 */

class PromoMapper @Inject constructor() {

    fun convertPromoGlobalModel(responseGetPromoStackUiModel: ResponseGetPromoStackUiModel,
                                currentPromoGlobalDara: PromoStackingData) {
        val typePromo: Int
        if (responseGetPromoStackUiModel.data.isCoupon == PromoStackingData.VALUE_COUPON) {
            typePromo = PromoStackingData.TYPE_COUPON
        } else {
            typePromo = PromoStackingData.TYPE_VOUCHER
        }
        currentPromoGlobalDara.typePromo = typePromo
        currentPromoGlobalDara.promoCode = responseGetPromoStackUiModel.data.codes.get(0)
        currentPromoGlobalDara.description = responseGetPromoStackUiModel.data.message.text
        currentPromoGlobalDara.state = responseGetPromoStackUiModel.data.message.state.mapToStatePromoStackingCheckout()
        currentPromoGlobalDara.title = responseGetPromoStackUiModel.data.titleDescription
        currentPromoGlobalDara.amount = responseGetPromoStackUiModel.data.cashbackWalletAmount
        currentPromoGlobalDara.variant = TickerPromoStackingCheckoutView.Variant.GLOBAL
    }

    fun convertPromoMerchantModel(voucherOrdersItemUiModel: VoucherOrdersItemUiModel,
                                  currentVoucherOrdersItemData: VoucherOrdersItemData) {
        currentVoucherOrdersItemData.code = voucherOrdersItemUiModel.code
        currentVoucherOrdersItemData.isSuccess = voucherOrdersItemUiModel.success
        currentVoucherOrdersItemData.uniqueId = voucherOrdersItemUiModel.uniqueId
        currentVoucherOrdersItemData.cartId = voucherOrdersItemUiModel.cartId
        currentVoucherOrdersItemData.type = voucherOrdersItemUiModel.type
        currentVoucherOrdersItemData.cashbackWalletAmount = voucherOrdersItemUiModel.cashbackWalletAmount
        currentVoucherOrdersItemData.discountAmount = voucherOrdersItemUiModel.discountAmount
        currentVoucherOrdersItemData.invoiceDescription = voucherOrdersItemUiModel.invoiceDescription

        val messageData = MessageData()
        messageData.color = voucherOrdersItemUiModel.message.color
        messageData.state = voucherOrdersItemUiModel.message.state
        messageData.text = voucherOrdersItemUiModel.message.text

        currentVoucherOrdersItemData.messageData = messageData
    }
}