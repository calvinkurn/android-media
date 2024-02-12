package com.tokopedia.buy_more_get_more.minicart.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

data class BmgmParamModel(
    @SerializedName("offer_ids")
    val offerIds: List<Long> = emptyList(),
    @SerializedName("offer_json_data")
    val offerJsonData: String = "{}",
    @SerializedName("warehouse_ids")
    val warehouseIds: List<Long> = emptyList()
)