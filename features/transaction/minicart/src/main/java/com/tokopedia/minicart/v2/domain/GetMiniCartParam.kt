package com.tokopedia.minicart.v2.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.minicart.common.domain.usecase.MiniCartSourceValue

data class GetMiniCartParam(
    @SerializedName("shop_ids")
    val shopIds: List<String>,
    @SerializedName("source")
    val source: MiniCartSourceValue,
    @SerializedName("usecase")
    val useCase: String = "minicart",
    @SerializedName("is_shop_direct_purchase")
    val isShopDirectPurchase: Boolean = false,
    @SerializedName("promo")
    val promo: GetMiniCartPromoParam? = null,
    @SerializedName("bmgm")
    val bmgm: GetMiniCartBmgmParam? = null,
    @SerializedName("chosen_address")
    internal var chosenAddress: ChosenAddress = ChosenAddress(),
    @Transient
    val delay: Long = 0
) {

    data class GetMiniCartPromoParam(
        @SerializedName("promo_id")
        val promoId: Long = 0,
        @SerializedName("promo_code")
        val promoCode: String = ""
    )

    data class GetMiniCartBmgmParam(
        @SerializedName("offer_ids")
        val offerIds: List<Long> = emptyList(),
        @SerializedName("offer_json_data")
        val offerJsonData: String = "{}",
        @SerializedName("warehouse_ids")
        val warehouseIds: List<Long> = emptyList()
    )
}
