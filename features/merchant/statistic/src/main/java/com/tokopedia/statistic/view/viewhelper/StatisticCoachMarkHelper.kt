package com.tokopedia.statistic.view.viewhelper

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.Const
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 22/02/22.
 */

class StatisticCoachMarkHelper @Inject constructor() {

    fun saveCoachMarkHasShownByTitle(context: Context, title: String) {
        when {
            getIsProductInsightTab(context, title) -> {
                setCoachMarkHasShown(context, Const.SHOW_PRODUCT_INSIGHT_COACH_MARK_KEY)
            }

            getIsOperationalInsightTab(context, title) -> {
                setCoachMarkHasShown(context, Const.HAS_SHOWN_OPERATIONAL_INSIGHT_COACH_MARK_KEY)
            }

            getIsTrafficInsightTab(context, title) -> {
                setCoachMarkHasShown(context, Const.HAS_SHOWN_TRAFFIC_INSIGHT_COACH_MARK_KEY)
            }
        }
    }

    fun getProductInsightCoachMark(title: String, itemView: View): CoachMark2Item? {
        val context = itemView.context
        if (getIsProductInsightTab(context, title)) {
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
        val context = view.context
        if (getIsOperationalInsightTab(context, title)) {
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
        val context = view.context
        if (getIsTrafficInsightTab(context, title)) {
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

    fun getIsTrafficInsightTab(context: Context, title: String): Boolean {
        return title == context.getString(R.string.stc_traffic) ||
                title == context.getString(R.string.stc_traffic_coachmark_title)
    }

    private fun getIsProductInsightTab(context: Context, title: String): Boolean {
        return title == context.getString(R.string.stc_product) ||
                title == context.getString(R.string.stc_product_coachmark_title)
    }

    private fun getIsOperationalInsightTab(context: Context, title: String): Boolean {
        return title == context.getString(R.string.stc_operational) ||
                title == context.getString(R.string.stc_operational_coachmark_title)
    }

    private fun setCoachMarkHasShown(context: Context, tag: String) {
        CoachMarkPreference.setShown(context, tag, true)
    }
}