package com.tokopedia.merchantvoucher.common.gql.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherAmountTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherOwnerTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherTypeDef


data class MerchantVoucherModel(
        @SerializedName("voucher_id")
        @Expose
        var voucherId: Int,
        @SerializedName("voucher_name")
        @Expose
        var voucherName: String? = "",
        @SerializedName("voucher_code")
        @Expose
        var voucherCode: String? = "",
        @SerializedName("voucher_type")
        @Expose
        var merchantVoucherType: MerchantVoucherType? = null,
        @SerializedName("amount")
        @Expose
        var merchantVoucherAmount: MerchantVoucherAmount? = null,
        @SerializedName("minimum_spend")
        @Expose
        var minimumSpend: Int = 0,
        @SerializedName("owner")
        @Expose
        var merchantVoucherOwner: MerchantVoucherOwner,
        @SerializedName("valid_thru")
        @Expose
        var validThru: String = "",
        @SerializedName("tnc")
        @Expose
        var tnc: String? = "",
        @SerializedName("banner")
        @Expose
        var merchantVoucherBanner: MerchantVoucherBanner? = null,
        @SerializedName("status")
        @Expose
        var merchantVoucherStatus: MerchantVoucherStatus? = null,
        @SerializedName("restricted_for_liquid_product")
        @Expose
        var restrictedForLiquidProduct: Boolean = false
)

data class MerchantVoucherType(
        @SerializedName("voucher_type")
        @Expose
        var type: Int = MerchantVoucherTypeDef.TYPE_FREE_ONGKIR,
        @SerializedName("identifier")
        @Expose
        var identifier: String? = ""
)

data class MerchantVoucherAmount(
        @SerializedName("amount_type")
        @Expose
        var type: Int = MerchantVoucherAmountTypeDef.TYPE_FIXED,
        @SerializedName("amount")
        @Expose
        var amount: Float? = 0f
)

data class MerchantVoucherOwner(
        @SerializedName("owner_id")
        @Expose
        var ownerId: Int = MerchantVoucherOwnerTypeDef.TYPE_MERCHANT,
        @SerializedName("identifier")
        @Expose
        var identifier: String = ""
)

data class MerchantVoucherBanner(
        @SerializedName("mobile_url")
        @Expose
        var mobileUrl: String = ""
)

data class MerchantVoucherStatus(
        @SerializedName("status")
        @Expose
        var status: Int = MerchantVoucherStatusTypeDef.TYPE_AVAILABLE
)


