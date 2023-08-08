package com.tokopedia.minicart.bmgm.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

data class BmgmParamModel(
    @SerializedName("offer_id")
    val offerId: Long,
    @SerializedName("offer_json_data")
    val offerJsonData: Long,
    @SerializedName("warehouse_ids")
    val warehouseIds: List<String>
)