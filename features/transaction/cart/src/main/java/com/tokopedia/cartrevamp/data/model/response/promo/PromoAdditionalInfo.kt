package com.tokopedia.cartrevamp.data.model.response.promo

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.UsageSummaries

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class PromoAdditionalInfo(
    @SerializedName("message_info")
    var messageInfo: PromoMessageInfo = PromoMessageInfo(),

    @SerializedName("error_detail")
    var errorDetail: PromoErrorDetail = PromoErrorDetail(),

    @SerializedName("empty_cart_info")
    var emptyCartInfo: PromoEmptyCartInfo = PromoEmptyCartInfo(),

    @SerializedName("usage_summaries")
    var usageSummaries: List<UsageSummaries> = emptyList(),

    @SerializedName("poml_auto_applied")
    var pomlAutoApplied: Boolean = false,

    @SerializedName("bebas_ongkir_info")
    val bebasOngkirInfo: PromoBebasOngkirInfo = PromoBebasOngkirInfo()
)
