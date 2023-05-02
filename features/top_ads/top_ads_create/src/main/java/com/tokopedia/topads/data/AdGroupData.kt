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
    val totalImpressionStats:String = "",
    val totalClickStats:String = "",
    val totalConversionStats:String = "",
    val loading:Boolean = false
)
