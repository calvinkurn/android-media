package com.tokopedia.topads.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopadsShopInfoV2Model(
  @Expose
  @SerializedName("topadsGetShopInfoV2_1")
  val topadsGetShopInfo : TopadsGetShopInfoV2 = TopadsGetShopInfoV2()
){
    data class TopadsGetShopInfoV2(
        @Expose
        @SerializedName("data")
      val data:Data = Data()

    ){
        data class Data(
            @Expose
            @SerializedName("ads")
            val ads : List<Ads> = listOf(),
            @Expose
            @SerializedName("errors")
            val errors:List<Error> = listOf()
        ){
            data class Ads(
                @Expose
                @SerializedName("type")
                val type:String = "",
                @Expose
                @SerializedName("is_used")
                val isUsed:Boolean = false
            )
        }

        data class Error(
            @Expose
            @SerializedName("code")
            val code:String = "",
            @Expose
            @SerializedName("detail")
            val detail:String = "",
            @Expose
            @SerializedName("title")
            val title:String = ""
        )
    }
}
