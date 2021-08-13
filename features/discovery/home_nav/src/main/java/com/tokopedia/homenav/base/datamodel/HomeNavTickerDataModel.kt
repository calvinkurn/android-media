package com.tokopedia.homenav.base.datamodel

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * Created by Fikry on 21/10/20.
 */

data class HomeNavTickerDataModel (
        val title: String = "",
        val description: String = "",
        val tickerType: Int = Ticker.TYPE_ANNOUNCEMENT,
        val applink: String = ""
) : HomeNavVisitable {
    override fun id(): Any = title

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean =
            visitable is HomeNavTickerDataModel && visitable.title == title && visitable.description == description && visitable.tickerType == tickerType

    override fun type(factory: HomeNavTypeFactory): Int = factory.type(this)
}