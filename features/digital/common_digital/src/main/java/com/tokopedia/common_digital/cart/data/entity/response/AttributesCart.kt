package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.product.data.response.PostPaidPopup

/**
 * @author anggaprasetiyo on 2/27/17.
 */

class AttributesCart {

    @SerializedName("client_number")
    @Expose
    var clientNumber: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
    @SerializedName("operator_name")
    @Expose
    var operatorName: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
    @SerializedName("price_plain")
    @Expose
    var pricePlain: Long = 0
    @SerializedName("instant_checkout")
    @Expose
    var isInstantCheckout: Boolean = false
    @SerializedName("need_otp")
    @Expose
    var isNeedOtp: Boolean = false
    @SerializedName("enable_voucher")
    @Expose
    var isEnableVoucher: Boolean = false
    @SerializedName("is_coupon_active")
    @Expose
    var isCouponActive: Int = 0
    @SerializedName("sms_state")
    @Expose
    var smsState: String? = null
    @SerializedName("user_input_price")
    @Expose
    var userInputPrice: UserInputPrice? = null
    @SerializedName("main_info")
    @Expose
    var mainInfo: List<MainInfo>? = null
    @SerializedName("additional_info")
    @Expose
    var additionalInfo: List<AdditionalInfo>? = null
    @SerializedName("autoapply")
    @Expose
    var autoApply: AutoApplyVoucher? = null
    @SerializedName("voucher_autocode")
    @Expose
    var voucherAutoCode: String? = null
    @SerializedName("default_promo_dialog_tab")
    @Expose
    var defaultPromoTab: String? = null
    @SerializedName("cross_selling_type")
    @Expose
    var crossSellingType: Int = 0
    @SerializedName("cross_selling_config")
    @Expose
    var crossSellingConfig: CrossSellingConfig? = null
    @SerializedName("pop_up")
    @Expose
    var postPaidPopUp: PostPaidPopup? = null
}
