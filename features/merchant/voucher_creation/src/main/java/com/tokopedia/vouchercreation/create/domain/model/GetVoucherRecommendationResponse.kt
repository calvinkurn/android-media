package com.tokopedia.vouchercreation.create.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

data class GetVoucherRecommendationResponse(
        @SerializedName("MerchantPromotionGetVoucherRecommendation")
        @Expose
        var voucherRecommendation: MerchantPromotionGetVoucherRecommendation = MerchantPromotionGetVoucherRecommendation()
)

data class MerchantPromotionGetVoucherRecommendation(
        @SerializedName("header")
        @Expose
        var header: Header = Header(),
        @SerializedName("data")
        @Expose
        var data: VoucherRecommendationData = VoucherRecommendationData()
)

data class VoucherRecommendationData(
        @SerializedName("ShopID")
        @Expose
        var shopID: Int = 0,
        @SerializedName("VoucherType")
        @Expose
        var voucherType: Int = 0,
        @SerializedName("VoucherBenefitType")
        @Expose
        var voucherBenefitType: Int = 0,
        @SerializedName("VoucherDiscountAmt")
        @Expose
        var voucherDiscountAmt: Int = 0,
        @SerializedName("VoucherDiscountAmtMax")
        @Expose
        var voucherDiscountAmtMax: Int = 0,
        @SerializedName("VoucherMinimumAmt")
        @Expose
        var voucherMinimumAmt: Int = 0,
        @SerializedName("VoucherQuota")
        @Expose
        var voucherQuota: Int = 0,
        @SerializedName("HaveRecommendation")
        @Expose
        var haveRecommendation: Boolean = false
)