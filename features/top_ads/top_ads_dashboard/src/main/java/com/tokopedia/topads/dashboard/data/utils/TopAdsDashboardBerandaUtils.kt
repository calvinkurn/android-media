package com.tokopedia.topads.dashboard.data.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.beranda.*
import com.tokopedia.topads.dashboard.data.utils.TopAdsPrefsUtil.berandaDialogShown
import com.tokopedia.topads.dashboard.data.utils.TopAdsPrefsUtil.showBerandaDialog
import com.tokopedia.topads.dashboard.data.utils.Utils.asPercentage
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsDashboardBerandaBaseBinding

internal object TopAdsDashboardBerandaUtils {

    private const val BERANDA_DIALOG_IMAGE =
        "https://images.tokopedia.net/img/android/res/singleDpi/topads_dashboard_dialog_img.png"
    private const val PRODUK_BERPOTENSI_LIMIT_IMAGE = 5

    private const val COACH_MARK_1 = 0
    private const val COACH_MARK_2 = 1
    private const val COACH_MARK_3 = 2
    private const val COACH_MARK_4 = 3
    private const val COACH_MARK_5 = 4

    private const val TOPADS_SUMMARY_TYPE_ALL = "0"
    private const val TOPADS_SUMMARY_TYPE_PRODUCT = "1"
    private const val TOPADS_SUMMARY_TYPE_SHOP = "3"

    fun mapImageModel(items: List<ImageModel>): MutableList<ImageModel> {
        val list = mutableListOf<ImageModel>()
        items.forEach {
            if (list.size >= PRODUK_BERPOTENSI_LIMIT_IMAGE) return@forEach
            list.add(it)
        }
        if (items.size - list.size > 0)
            list[list.size - 1].overLappingText = "+${items.size - list.size + 1}"
        return list
    }

    fun mapChipDetailToKataKunci(
        item: RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup,
        resources: Resources,
    ) = listOf(
        KataKunciDetail(
            resources.getString(R.string.topads_dashboard_kata_kunci_baru),
            item.newKeywordCount,
            String.format(
                resources.getString(R.string.topads_dashboard_potensi_tampil_str),
                item.newKeywordTotalImpression
            )
        ),
        KataKunciDetail(
            resources.getString(R.string.new_keyword_subtitle2),
            item.bidCount,
            String.format(
                resources.getString(R.string.topads_dashboard_potensi_tampil_str),
                item.bidTotalImpression
            )
        ),
        KataKunciDetail(
            resources.getString(R.string.topads_dashboard_kata_kunci_neg),
            item.negativeKeywordCount,
            String.format(
                resources.getString(R.string.topads_dashboard_potensi_tampil_str),
                item.newKeywordTotalImpression
            )
        )
    )

    fun TopadsWidgetSummaryStatisticsModel.TopadsWidgetSummaryStatistics.WidgetSummaryStatistics.Summary.mapToSummary(
        context: Context,
    ) = listOf(
        SummaryBeranda(
            context.resources.getString(R.string.label_top_ads_impression),
            TopAdsDashboardConstant.CONST_TAMPIL, impressionSum, impressionPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_PN400)
        ),
        SummaryBeranda(
            context.resources.getString(R.string.label_top_ads_click),
            TopAdsDashboardConstant.CONST_KLIK, clickSum, clickPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400)
        ),
        SummaryBeranda(
            context.resources.getString(R.string.label_top_ads_sold),
            TopAdsDashboardConstant.CONST_TERJUAL, totalSoldSum, totalSoldPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_BN400)
        ),
        SummaryBeranda(
            context.resources.getString(com.tokopedia.topads.common.R.string.topads_common_pendapatan),
            TopAdsDashboardConstant.CONST_PENDAPATAN, incomeSum, incomePercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        ),
        SummaryBeranda(
            context.resources.getString(com.tokopedia.topads.common.R.string.topads_common_pengeluaran),
            TopAdsDashboardConstant.CONST_PENGELURAN, spendingSum, spendingPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_RN400)
        ),
        SummaryBeranda(
            context.resources.getString(R.string.topads_dashboard_efektivitas_iklan),
            TopAdsDashboardConstant.CONST_EFECTIVITAS_IKLAN, roasSum, roasPercent.asPercentage(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_TN500)
        ),
    )

    fun Activity.showDialogWithCoachMark(
        binding: FragmentTopadsDashboardBerandaBaseBinding,
        toolbarIcon: View,
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
                    binding.layoutRingkasan.graphLayoutBeranda, binding.layoutRecommendasi.root,
                    binding.layoutLatestReading.rvLatestReading, toolbarIcon
                )
            }
        }.show()
        berandaDialogShown()
    }

    private fun Context.showCoachMark(vararg views: View) {
        val coachMarkItems = arrayListOf<CoachMark2Item>()
        views.forEachIndexed { index, view ->
            coachMarkItems.add(
                when (index) {
                    COACH_MARK_1 -> CoachMark2Item(
                        view,
                        resources.getString(R.string.topads_dashboard_home_coachmark_1_title),
                        resources.getString(R.string.topads_dashboard_home_coachmark_1_desc),
                        CoachMark2.POSITION_TOP
                    )
                    COACH_MARK_2 -> CoachMark2Item(
                        view,
                        resources.getString(R.string.topads_dashboard_home_coachmark_2_title),
                        resources.getString(R.string.topads_dashboard_home_coachmark_2_desc),
                        CoachMark2.POSITION_TOP
                    )
                    COACH_MARK_3 -> CoachMark2Item(
                        view,
                        resources.getString(R.string.topads_dashboard_home_coachmark_3_title),
                        resources.getString(R.string.topads_dashboard_home_coachmark_3_desc),
                        CoachMark2.POSITION_TOP
                    )
                    COACH_MARK_4 -> CoachMark2Item(
                        view,
                        resources.getString(R.string.topads_dashboard_home_coachmark_4_title),
                        resources.getString(R.string.topads_dashboard_home_coachmark_4_desc),
                        CoachMark2.POSITION_TOP
                    )
                    COACH_MARK_5 -> CoachMark2Item(
                        view,
                        resources.getString(R.string.topads_dashboard_home_coachmark_5_title),
                        resources.getString(R.string.topads_dashboard_home_coachmark_5_desc),
                        CoachMark2.POSITION_BOTTOM
                    )
                    else -> {
                        return@forEachIndexed
                    }
                }
            )
        }
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
        Chip(getString(R.string.topads_dashboard_all_promo_menu), TOPADS_SUMMARY_TYPE_ALL),
        Chip(getString(R.string.topads_dash_iklan_produck), TOPADS_SUMMARY_TYPE_PRODUCT),
        Chip(getString(R.string.topads_dash_headline_title), TOPADS_SUMMARY_TYPE_SHOP)
    )
}