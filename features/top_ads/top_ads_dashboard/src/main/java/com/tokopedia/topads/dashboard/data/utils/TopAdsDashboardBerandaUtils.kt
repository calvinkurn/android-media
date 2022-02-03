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

object TopAdsDashboardBerandaUtils {

    fun showDialogWithCoachMark(context: Context, cmv1: View, cmv2: View, cmv3: View) {
        DialogUnify(
            context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION
        ).apply {
            setTitle(context.resources.getString(R.string.topads_dashboard_home_dialog_title))
            setDescription(context.resources.getString(R.string.topads_dashboard_home_dialog_description))
            setPrimaryCTAText(context.resources.getString(R.string.topads_dashboard_home_dialog_button_text))
            setImageDrawable(R.drawable.topads_dashboard_dialog_img)
            setPrimaryCTAClickListener {
                dismiss()
                showCoachMark(context, cmv1, cmv2, cmv3)
            }
        }.show()
    }

    private fun showCoachMark(context: Context, view1: View, view2: View, view3: View) {
        val coachMarkItems = arrayListOf(
            CoachMark2Item(
                view1,
                context.resources.getString(R.string.topads_dashboard_home_coachmark_1_title),
                context.resources.getString(R.string.topads_dashboard_home_coachmark_1_desc),
                CoachMark2.POSITION_TOP
            ), CoachMark2Item(
                view2,
                context.resources.getString(R.string.topads_dashboard_home_coachmark_2_title),
                context.resources.getString(R.string.topads_dashboard_home_coachmark_2_desc),
                CoachMark2.POSITION_TOP
            ),
            CoachMark2Item(
                view3,
                context.resources.getString(R.string.topads_dashboard_home_coachmark_4_title),
                context.resources.getString(R.string.topads_dashboard_home_coachmark_4_desc),
                CoachMark2.POSITION_TOP
            )
        )

        val coachMark = CoachMark2(context)
        coachMark.showCoachMark(coachMarkItems)
    }

    fun getSummaryAdTypes(resources: Resources) = listOf(
        Chip(
            resources.getString(R.string.topads_dashboard_all_promo_menu),
            TopAdsSummaryType.ALL, true
        ),
        Chip(resources.getString(R.string.topads_dash_iklan_produck), TopAdsSummaryType.PRODUCT),
        Chip(resources.getString(R.string.topads_dash_headline_title), TopAdsSummaryType.SHOP),
        Chip(resources.getString(R.string.topads_dashboard_iklan_google), TopAdsSummaryType.GOOGLE),
        Chip(resources.getString(R.string.topads_dashboard_iklan_banner), TopAdsSummaryType.BANNER),
        Chip(
            resources.getString(R.string.topads_dashboard_iklan_tanpa_modal),
            TopAdsSummaryType.NO_MODAL
        ),
    )
}