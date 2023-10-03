package com.tokopedia.statistic.common.delegates

import android.content.Context
import android.view.View

/**
 * Created by @ilhamsuaib on 03/10/23.
 */

interface RejectedOrderRateCoachMark {

    fun iniCoachMark(context: Context)

    fun setCoachMarkAnchor(dataKey: String, view: View)

    fun showCoachMark()

    fun dismissCoachMark()

    fun destroyCoachMark()
}