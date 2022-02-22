package com.tokopedia.statistic.view.viewhelper

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.Const
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 22/02/22.
 */

class StatisticCoachMarkHelper @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    fun saveCoachMarkHasShownByTitle(title: String) {
        when {
            getIsProductInsightTab(title) -> {
                setCoachMarkHasShown(Const.SHOW_PRODUCT_INSIGHT_COACH_MARK_KEY)
            }
            getIsOperationalInsightTab(title) -> {
                setCoachMarkHasShown(Const.HAS_SHOWN_OPERATIONAL_INSIGHT_COACH_MARK_KEY)
            }
            getIsTrafficInsightTab(title) -> {
                setCoachMarkHasShown(Const.HAS_SHOWN_TRAFFIC_INSIGHT_COACH_MARK_KEY)
            }
        }
    }

    fun getProductInsightCoachMark(title: String, itemView: View): CoachMark2Item? {
        if (getIsProductInsightTab(title)) {
            if (!CoachMarkPreference.hasShown(context, Const.SHOW_PRODUCT_INSIGHT_COACH_MARK_KEY)) {
                return CoachMark2Item(
                    itemView,
                    context.getString(R.string.stc_product_coachmark_title),
                    context.getString(R.string.stc_product_coachmark_desc)
                )
            }
        }
        return null
    }

    fun getOperationalInsightCoachMark(title: String, view: View): CoachMark2Item? {
        if (getIsOperationalInsightTab(title)) {
            if (!CoachMarkPreference.hasShown(
                    context,
                    Const.HAS_SHOWN_OPERATIONAL_INSIGHT_COACH_MARK_KEY
                )
            ) {
                return CoachMark2Item(
                    view,
                    context.getString(R.string.stc_operational_coachmark_title),
                    context.getString(R.string.stc_operational_coachmark_desc)
                )
            }
        }
        return null
    }

    fun getTrafficInsightCoachMark(title: String, view: View): CoachMark2Item? {
        if (getIsTrafficInsightTab(title)) {
            if (!CoachMarkPreference.hasShown(
                    context,
                    Const.HAS_SHOWN_TRAFFIC_INSIGHT_COACH_MARK_KEY
                )
            ) {
                return CoachMark2Item(
                    view,
                    context.getString(R.string.stc_traffic_coachmark_title),
                    context.getString(R.string.stc_traffic_coachmark_desc)
                )
            }
        }
        return null
    }

    private fun getIsProductInsightTab(title: String): Boolean {
        return title == context.getString(R.string.stc_product) ||
                title == context.getString(R.string.stc_product_coachmark_title)
    }

    private fun getIsTrafficInsightTab(title: String): Boolean {
        return title == context.getString(R.string.stc_traffic) ||
                title == context.getString(R.string.stc_traffic_coachmark_title)
    }

    private fun getIsOperationalInsightTab(title: String): Boolean {
        return title == context.getString(R.string.stc_operational) ||
                title == context.getString(R.string.stc_operational_coachmark_title)
    }

    private fun setCoachMarkHasShown(tag: String) {
        CoachMarkPreference.setShown(context, tag, true)
    }
}