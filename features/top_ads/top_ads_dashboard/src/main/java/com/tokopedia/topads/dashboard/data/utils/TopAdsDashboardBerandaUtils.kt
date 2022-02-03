package com.tokopedia.topads.dashboard.data.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsSummaryType
import com.tokopedia.topads.dashboard.data.model.beranda.Chip
import com.tokopedia.topads.dashboard.data.model.beranda.SummaryBeranda
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.utils.Utils.asPercentage

object TopAdsDashboardBerandaUtils {

    fun TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Summary.mapToSummary(
        resources: Resources
    ) = listOf(
        SummaryBeranda(
            resources.getString(R.string.label_top_ads_impression),
            impressionSum, impressionPercent.asPercentage()
        ),
        SummaryBeranda(
            resources.getString(R.string.label_top_ads_click),
            clickSum, clickPercent.asPercentage()
        ),
        SummaryBeranda(
            resources.getString(R.string.label_top_ads_sold),
            totalSoldSum, totalSoldPercent.asPercentage()
        ),
        SummaryBeranda(
            resources.getString(com.tokopedia.topads.common.R.string.topads_common_pendapatan),
            incomeSum, incomePercent.asPercentage()
        ),
        SummaryBeranda(
            resources.getString(com.tokopedia.topads.common.R.string.topads_common_pengeluaran),
            spendingSum, spendingPercent.asPercentage()
        ),
        SummaryBeranda(
            resources.getString(R.string.topads_dashboard_efektivitas_iklan),
            roasSum, roasPercent.asPercentage()
        ),
    )

    fun Context.showDialogWithCoachMark(cmv1: View, cmv2: View, cmv3: View) {
        DialogUnify(
            this, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION
        ).apply {
            setTitle(resources.getString(R.string.topads_dashboard_home_dialog_title))
            setDescription(resources.getString(R.string.topads_dashboard_home_dialog_description))
            setPrimaryCTAText(resources.getString(R.string.topads_dashboard_home_dialog_button_text))
            setImageDrawable(R.drawable.topads_dashboard_dialog_img)
            setPrimaryCTAClickListener {
                dismiss()
                showCoachMark(context, cmv1, cmv2, cmv3)
            }
        }.show()
    }

    private fun Context.showCoachMark(context: Context, view1: View, view2: View, view3: View) {
        val coachMarkItems = arrayListOf(
            CoachMark2Item(
                view1,
                resources.getString(R.string.topads_dashboard_home_coachmark_1_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_1_desc),
                CoachMark2.POSITION_TOP
            ), CoachMark2Item(
                view2,
                resources.getString(R.string.topads_dashboard_home_coachmark_2_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_2_desc),
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                view3,
                resources.getString(R.string.topads_dashboard_home_coachmark_4_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_4_desc),
                CoachMark2.POSITION_TOP
            )
        )

        val coachMark = CoachMark2(context)
        coachMark.showCoachMark(coachMarkItems)
    }

    fun Resources.getSummaryAdTypes() = listOf(
        Chip(getString(R.string.topads_dashboard_all_promo_menu), TopAdsSummaryType.ALL),
        Chip(getString(R.string.topads_dash_iklan_produck), TopAdsSummaryType.PRODUCT),
        Chip(getString(R.string.topads_dash_headline_title), TopAdsSummaryType.SHOP),
        Chip(getString(R.string.topads_dashboard_iklan_google), TopAdsSummaryType.GOOGLE),
        Chip(getString(R.string.topads_dashboard_iklan_banner), TopAdsSummaryType.BANNER),
        Chip(getString(R.string.topads_dashboard_iklan_tanpa_modal), TopAdsSummaryType.NO_MODAL),
    )
}