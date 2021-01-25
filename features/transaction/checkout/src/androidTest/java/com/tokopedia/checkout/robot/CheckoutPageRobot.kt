package com.tokopedia.checkout.robot

import android.content.Context
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import org.junit.Assert

fun checkoutPage(func: CheckoutPageRobot.() -> Unit) = CheckoutPageRobot().apply(func)

class CheckoutPageRobot {

    fun assertMainContent() {

    }

    fun assertTickerAnnouncementViewHolder(position: Int) {

    }

    infix fun choosePayment(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }

}

class ResultRobot {

    fun hasPassedAnalytics(gtmLogDBSource: GtmLogDBSource, context: Context, queryFileName: String) {
        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, queryFileName), hasAllSuccess())
    }

}