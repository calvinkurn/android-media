package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

data class ShopLevelResponse(
    @Expose
    @SerializedName("shopLevel")
    val shopLevel: ShopLevelModel = ShopLevelModel()
) {
    data class ShopLevelModel(
        @Expose
        @SerializedName("result")
        val result: ResultModel = ResultModel()
    ) {
        data class ResultModel(
            @Expose
            @SerializedName("itemSold")
            val itemSold: Int? = 0,
            @Expose
            @SerializedName("nextUpdate")
            val nextUpdate: String? = "",
            @Expose
            @SerializedName("niv")
            val niv: Int? = 0,
            @Expose
            @SerializedName("period")
            val period: String? = "",
            @Expose
            @SerializedName("shopLevel")
            val shopLevel: Int? = 0
        )
    }
}