package com.tokopedia.top_ads_headline.data

data class TopAdsCategoryDataModel(val count: Int = 0,
                                   val id: String = "",
                                   val name: String = "",
                                   val type: Int = 0,
                                   var isSelected: Boolean = false)