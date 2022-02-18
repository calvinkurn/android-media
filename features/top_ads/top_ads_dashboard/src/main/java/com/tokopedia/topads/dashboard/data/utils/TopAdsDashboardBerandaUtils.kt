package com.tokopedia.topads.dashboard.data.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsSummaryType
import com.tokopedia.topads.dashboard.data.model.beranda.Chip
import com.tokopedia.topads.dashboard.data.model.beranda.SummaryBeranda
import com.tokopedia.topads.dashboard.data.model.beranda.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.topads.dashboard.data.utils.TopAdsPrefsUtil.berandaDialogShown
import com.tokopedia.topads.dashboard.data.utils.TopAdsPrefsUtil.showBerandaDialog
import com.tokopedia.topads.dashboard.data.utils.Utils.asPercentage
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsDashboardBerandaBaseBinding

internal object TopAdsDashboardBerandaUtils {

    private const val BERANDA_DIALOG_IMAGE =
        "https://images.tokopedia.net/img/android/res/singleDpi/topads_dashboard_dialog_img.png"

    fun TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Summary.mapToSummary(
        context: Context
    ) = listOf(
        SummaryBeranda(
            context.resources.getString(R.string.label_top_ads_impression),
            TopAdsDashboardConstant.CONST_TAMPIL, impressionSum, impressionPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_P400)
        ),
        SummaryBeranda(
            context.resources.getString(R.string.label_top_ads_click),
            TopAdsDashboardConstant.CONST_KLIK, clickSum, clickPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y400)
        ),
        SummaryBeranda(
            context.resources.getString(R.string.label_top_ads_sold),
            TopAdsDashboardConstant.CONST_TERJUAL, totalSoldSum, totalSoldPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_B400)
        ),
        SummaryBeranda(
            context.resources.getString(com.tokopedia.topads.common.R.string.topads_common_pendapatan),
            TopAdsDashboardConstant.CONST_PENDAPATAN, incomeSum, incomePercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
        ),
        SummaryBeranda(
            context.resources.getString(com.tokopedia.topads.common.R.string.topads_common_pengeluaran),
            TopAdsDashboardConstant.CONST_PENGELURAN, spendingSum, spendingPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R400)
        ),
        SummaryBeranda(
            context.resources.getString(R.string.topads_dashboard_efektivitas_iklan),
            TopAdsDashboardConstant.CONST_EFECTIVITAS_IKLAN, roasSum, roasPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_T500)
        ),
    )

    fun Activity.showDialogWithCoachMark(
        binding: FragmentTopadsDashboardBerandaBaseBinding,
        toolbarIcon: View
    ) {
        if (!showBerandaDialog()) return
        DialogUnify(
            this, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION
        ).apply {
            setTitle(resources.getString(R.string.topads_dashboard_home_dialog_title))
            setDescription(resources.getString(R.string.topads_dashboard_home_dialog_description))
            setPrimaryCTAText(resources.getString(R.string.topads_dashboard_home_dialog_button_text))
            setImageUrl(BERANDA_DIALOG_IMAGE)
            setPrimaryCTAClickListener {
                dismiss()
                showCoachMark(
                    binding.layoutRingkasan.rvSummary,
                    binding.layoutRingkasan.graphLayout, binding.layoutRecommendasi.root,
                    binding.layoutLatestReading.rvLatestReading, toolbarIcon
                )
            }
        }.show()
        berandaDialogShown()
    }

    private fun Context.showCoachMark(vararg views: View) {
        val coachMarkItems = arrayListOf(
            CoachMark2Item(
                views[0],
                resources.getString(R.string.topads_dashboard_home_coachmark_1_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_1_desc),
                CoachMark2.POSITION_TOP
            ), CoachMark2Item(
                views[1],
                resources.getString(R.string.topads_dashboard_home_coachmark_2_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_2_desc),
                CoachMark2.POSITION_TOP
            ), CoachMark2Item(
                views[2],
                resources.getString(R.string.topads_dashboard_home_coachmark_3_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_3_desc),
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                views[3],
                resources.getString(R.string.topads_dashboard_home_coachmark_4_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_4_desc),
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                views[4],
                resources.getString(R.string.topads_dashboard_home_coachmark_5_title),
                resources.getString(R.string.topads_dashboard_home_coachmark_5_desc),
                CoachMark2.POSITION_BOTTOM
            )
        )

        val coachMark = CoachMark2(this)
        coachMark.showCoachMark(coachMarkItems)
        coachMark.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                views[currentIndex].parent.requestChildFocus(
                    views[currentIndex], views[currentIndex]
                )
            }
        })
    }

    fun Resources.getSummaryAdTypes() = listOf(
        Chip(getString(R.string.topads_dashboard_all_promo_menu), TopAdsSummaryType.ALL),
        Chip(getString(R.string.topads_dash_iklan_produck), TopAdsSummaryType.PRODUCT),
        Chip(getString(R.string.topads_dash_headline_title), TopAdsSummaryType.SHOP)
    )
}