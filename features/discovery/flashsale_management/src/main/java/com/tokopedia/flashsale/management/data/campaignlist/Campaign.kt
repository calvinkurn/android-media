package com.tokopedia.flashsale.management.data.campaignlist

data class Campaign(
        var campaign_id: Int = 0,
        var name: String = "",
        var campaign_period: String = "",
        var submission_start_date: String = "",
        var submission_end_date: String = "",
        var status: String = "",
        var campaign_type: String = "",
        var cover: String = "",
        var is_joined: Boolean = false,
        var dashboard_url: String = "",
        var product_number: Int = 0,
        var seller_number: Int = 0,
        var seller_info: SellerInfo = SellerInfo()
)