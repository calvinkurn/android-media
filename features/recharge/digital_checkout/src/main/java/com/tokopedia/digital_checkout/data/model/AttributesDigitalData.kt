package com.tokopedia.digital_checkout.data.model

import android.os.Parcelable
import com.tokopedia.digital_checkout.data.response.getcart.FintechProduct
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AttributesDigitalData(
        var userId: String? = null,

        var clientNumber: String? = null,

        var icon: String? = null,

        var price: String? = null,

        var categoryName: String? = null,

        var operatorName: String? = null,

        var pricePlain: Long = 0,

        var isInstantCheckout: Boolean = false,

        var isNeedOtp: Boolean = false,

        var smsState: String? = null,

        var isEnableVoucher: Boolean = false,

        var voucherAutoCode: String? = null,

        var isCouponActive: Int = 0,

        var userInputPrice: UserInputPriceDigital? = null,

        var autoApplyVoucher: CartAutoApplyVoucher? = null,

        var defaultPromoTab: String? = null,

        var postPaidPopupAttribute: PostPaidPopupAttribute? = null,

        var fintechProduct: List<FintechProduct>? = null
) : Parcelable {

    @Parcelize
    data class UserInputPriceDigital(
            var minPayment: String? = null,

            var maxPayment: String? = null,

            var minPaymentPlain: Long = 0,

            var maxPaymentPlain: Long = 0
    ) : Parcelable

    @Parcelize
    class CartAutoApplyVoucher(
            var isSuccess: Boolean = false,

            var code: String? = null,

            var isCoupon: Int = 0,

            var discountAmount: Long = 0,

            var title: String? = null,

            var messageSuccess: String? = null,

            var promoId: Long = 0
    ) : Parcelable

    @Parcelize
    class PostPaidPopupAttribute(
            var title: String? = null,

            var content: String? = null,

            var imageUrl: String? = null,

            var confirmButtonTitle: String? = null
    ) : Parcelable
}
