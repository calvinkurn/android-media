package com.tokopedia.catalog.utils


import android.content.Context
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess

class CatalogSimulationRobot {

    infix fun assertTest(action: CatalogSimulationRobot.() -> Unit) = CatalogSimulationRobot().apply(action)

    fun validate(gtmLogDbSource: GtmLogDBSource,
                 targetContext: Context,
                 fileName: String) {
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDbSource, targetContext, fileName),
                hasAllSuccess())
    }
}

fun actionTest(action: CatalogSimulationRobot.() -> Unit) = CatalogSimulationRobot().apply(action)