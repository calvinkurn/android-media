package com.tokopedia.v2.home.model.vo

import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.model.pojo.home.Tickers

class TickerDataModel(
        val tickers: List<Tickers>
): ModelViewType {
    override fun getPrimaryKey(): Int {
        return 2
    }

    override fun isContentsTheSame(other: ModelViewType): Boolean {
        return other is TickerDataModel && tickers.size == other.tickers.size
    }
}