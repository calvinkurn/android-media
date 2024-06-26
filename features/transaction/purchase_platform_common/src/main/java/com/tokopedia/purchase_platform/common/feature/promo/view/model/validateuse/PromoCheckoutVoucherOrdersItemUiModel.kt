package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoCheckoutVoucherOrdersItemUiModel(
    var code: String = "",
    var uniqueId: String = "",
    var cashbackWalletAmount: Int = -1,
    var discountAmount: Long = -1,
    var addressId: Int = -1,
    var titleDescription: String = "",
    var isPo: Int = -1,
    var shippingId: Int = 0,
    var spId: Int = 0,
    var type: String = "",
    var messageUiModel: MessageUiModel = MessageUiModel(),
    var duration: String = "",
    var cartId: Int = -1,
    var shopId: Int = -1,
    var benefitDetailUiModels: List<BenefitDetailsItemUiModel> = listOf(),
    var success: Boolean = false,
    var invoiceDescription: String = "",
    var orderId: Int = -1,
    var warehouseId: Int = -1,
    var cartStringGroup: String = ""
) : Parcelable {
    companion object {
        private const val TYPE_LOGISTIC = "logistic"
    }

    fun isTypeLogistic(): Boolean {
        return type == TYPE_LOGISTIC
    }
}
