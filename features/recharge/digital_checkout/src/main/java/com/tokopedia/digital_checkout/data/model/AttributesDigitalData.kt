package com.tokopedia.digital_checkout.data.model

import android.os.Parcelable
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AttributesDigitalData(
        var userId: String = "",

        var clientNumber: String = "",

        var icon: String = "",

        var price: String = "",

        var categoryName: String = "",

        var operatorName: String = "",

        var pricePlain: Double = 0.0,

        var isInstantCheckout: Boolean = false,

        var isNeedOtp: Boolean = false,

        var smsState: String = "",

        var isEnableVoucher: Boolean = false,

        var voucherAutoCode: String = "",

        var isCouponActive: Int = 0,

        var userInputPrice: UserInputPriceDigital = UserInputPriceDigital(),

        var autoApplyVoucher: CartAutoApplyVoucher = CartAutoApplyVoucher(),

        var defaultPromoTab: String = "",

        var postPaidPopupAttribute: PostPaidPopupAttribute = PostPaidPopupAttribute(),

        var fintechProduct: List<FintechProduct> = listOf(),

        var adminFee: Float = 0.0f
) : Parcelable {

    @Parcelize
    data class UserInputPriceDigital(
            var minPayment: String? = null,

            var maxPayment: String? = null,

            var minPaymentPlain: Double = 0.0,

            var maxPaymentPlain: Double = 0.0
    ) : Parcelable

    @Parcelize
    class CartAutoApplyVoucher(
            var isSuccess: Boolean = false,

            var code: String = "",

            var isCoupon: Int = 0,

            var discountAmount: Double = 0.0,

            var title: String = "",

            var messageSuccess: String = "",

            var promoId: Long = 0
    ) : Parcelable

    @Parcelize
    class PostPaidPopupAttribute(
            var title: String = "",

            var content: String = "",

            var imageUrl: String = "",

            var confirmButtonTitle: String = ""
    ) : Parcelable
}
