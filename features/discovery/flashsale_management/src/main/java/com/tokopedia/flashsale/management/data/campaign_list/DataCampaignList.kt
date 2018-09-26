package com.tokopedia.flashsale.management.data.campaign_list

data class DataCampaignList(
        var shop_id: Int = 0,
        var total_data: Int = 0,
        var list: List<Campaign> = listOf()
)