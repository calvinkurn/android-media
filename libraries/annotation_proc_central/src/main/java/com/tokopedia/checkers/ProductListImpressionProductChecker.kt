package com.tokopedia.checkers

//import com.tokopedia.config.GlobalConfig

object ProductListImpressionProductChecker {
    fun isPriceNotZero(price: Double) = !(price == 0.0)
    fun isIndexNotZero(index: Long) = !(index > 0)
    fun checkMap(map: Map<String, String>) = map.isNotEmpty()
//    fun isOnlyMainApp(dimension88: String?) = !GlobalConfig.isSellerApp()
}