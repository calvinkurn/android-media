package com.tokopedia.topads.data

data class AdGroupCompleteData(
    val groupId:String = "",
    val groupName:String = "",
    var totalImpressionStats:String = "",
    var totalClickStats:String = "",
    var totalConversionStats:String = "",
    var productBrowse:Float = 0f,
    var productSearch:Float = 0f,
)

data class AdGroupSettingData(
    val productBrowse:Float = 0f,
    val productSearch:Float = 0f,
    val loading:Boolean = false
)

data class AdGroupStatsData(
    val totalImpressionStats:String = "0",
    val totalClickStats:String = "1",
    val totalConversionStats:String = "0",
    val loading:Boolean = false
)
