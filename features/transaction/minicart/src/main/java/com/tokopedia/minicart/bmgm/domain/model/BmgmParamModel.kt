package com.tokopedia.minicart.bmgm.domain.model

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
    val warehouseIds: List<String> = emptyList()
)