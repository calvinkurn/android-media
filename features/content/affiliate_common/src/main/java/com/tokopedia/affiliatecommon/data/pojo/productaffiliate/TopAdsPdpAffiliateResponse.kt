package com.tokopedia.affiliatecommon.data.pojo.productaffiliate

import com.google.gson.annotations.SerializedName

class TopAdsPdpAffiliateResponse {

    @SerializedName("topAdsPDPAffiliate")
    var topAdsPDPAffiliate: TopAdsPdpAffiliate? = null

    class TopAdsPdpAffiliate {
        @SerializedName("data")
        var data: Data? = null

        class Data {
            @SerializedName("affiliate")
            var affiliate: List<PdpAffiliate>? = null

            class PdpAffiliate {
                @SerializedName("AdId")
                var adId: Int = 0
                @SerializedName("ProductId")
                var productId: Int = 0
                @SerializedName("AffiliatedByUser")
                var isAffiliatedByUser: Boolean = false
                @SerializedName("CommissionPercent")
                var commissionPercent: Int = 0
                @SerializedName("CommissionPercentDispay")
                var commissionPercentDispay: String? = ""
                @SerializedName("CommissionValue")
                var commissionValue: Int = 0
                @SerializedName("CommissionValueDisplay")
                var commissionValueDisplay: String? = ""
                @SerializedName("UniqueURL")
                var uniqueURL: String? = ""
                @SerializedName("AdTitle")
                var adTitle: String? = ""
                @SerializedName("Image")
                var image: String? = ""
            }
        }
    }
}
