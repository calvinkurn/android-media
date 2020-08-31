package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.Tickers
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

/**
 * @author by Lukas on 18/05/2020.
 */

data class TickerDataModel(
    val tickers: List<Tickers> = listOf(),
    private var isCache: Boolean = false,
    private var trackingData: Map<String, Any>? = null,
    private var trackingDataForCombination: List<Any>? = null,
    private var isCombined: Boolean = false
) : HomeVisitable {

    override fun equalsWith(b: Any?): Boolean {
        if (b is TickerDataModel) {
            if (tickers.size != b.tickers.size) return false
            tickers.forEachIndexed { index, ticker ->
                if (ticker.id == b.tickers[index].id) return false
            }
            return true
        }
        return false
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return TICKERS
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }


    override fun setTrackingData(trackingData: Map<String, Any>) {
        this.trackingData = trackingData
    }

    override fun getTrackingData(): Map<String, Any>? {
        return trackingData
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return trackingDataForCombination
    }

    override fun setTrackingDataForCombination(trackingDataForCombination: List<Any>) {
        this.trackingDataForCombination = trackingDataForCombination
    }

    override fun isTrackingCombined(): Boolean {
        return isCombined
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    companion object{
        private const val TICKERS = "Tickers"
    }
}
