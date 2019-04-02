package com.tokopedia.flashsale.management.data.campaignlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataCampaignList(
        @SerializedName("shop_id")
        @Expose
        var shopID: Int = 0,
        @SerializedName("total_data")
        @Expose
        var totalData: Int = 0,
        @SerializedName("list")
        @Expose
        var list: List<Campaign> = listOf()
){
    data class ResponseData(@SerializedName("data")
                            @Expose val data: DataCampaignList = DataCampaignList())

    data class Response(@SerializedName("getCampaignList")
                        @Expose val result: ResponseData = ResponseData())
}