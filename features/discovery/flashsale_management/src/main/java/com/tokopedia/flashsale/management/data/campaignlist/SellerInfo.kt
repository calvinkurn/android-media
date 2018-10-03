package com.tokopedia.flashsale.management.data.campaignlist

data class SellerInfo(
        var submitted_product: Int = 0,
        var accepted_product: Int = 0,
        var rejected_product: Int = 0,
        var max_submission: Int = 0,
        var total_item: Int = 0,
        var sold_item: Int = 0
)