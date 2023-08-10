package com.tokopedia.tkpd.flashsale.util

import com.tokopedia.campaign.utils.constant.TickerType
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.ticker.Ticker

object TickerUtil {

    fun getTickerType(tickerType: String): Int {
        return when (tickerType.lowercase()) {
            TickerType.INFO -> Ticker.TYPE_ANNOUNCEMENT
            TickerType.WARNING -> Ticker.TYPE_WARNING
            TickerType.DANGER -> Ticker.TYPE_ERROR
            else -> Ticker.TYPE_INFORMATION
        }
    }

    fun getRollenceValues(): List<String> {
        val listOfFilteredRollenceKeys: List<String> = getFilteredRollenceKeys()
        val listOfFilteredRollenceValues: List<String> = getFilteredRollenceValues(listOfFilteredRollenceKeys)
        return listOfFilteredRollenceValues
    }

    private fun getFilteredRollenceKeys(): List<String> {
        val prefixKey = "CT_"
        val filteredRollenceKeys = RemoteConfigInstance.getInstance().abTestPlatform
            .getKeysByPrefix(prefix = prefixKey)
        return filteredRollenceKeys.toList()
    }

    private fun getFilteredRollenceValues(keyList: List<String>): List<String> {
        var valueList: MutableList<String> = mutableListOf()
        for (key in keyList) {
            val rollenceValue: String = RemoteConfigInstance.getInstance().abTestPlatform.getString(key)
            if (rollenceValue.isNotEmpty()) {
                valueList.add(rollenceValue)
            }
        }
        return valueList
    }

}
