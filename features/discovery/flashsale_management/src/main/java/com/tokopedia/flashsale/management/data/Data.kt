package com.tokopedia.flashsale.management.data

data class Data(
        var shop_id: Int = 0,
        var total_data: Int = 0,
        var list: List<Campaign> = listOf()
)