package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.constant.PeriodType
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.view.model.WidgetShopGradeUiModel
import kotlinx.android.synthetic.main.widget_pm_shop_grade.view.*

/**
 * Created By @ilhamsuaib on 03/03/21
 */

class ShopGradeWidget(itemView: View) : AbstractViewHolder<WidgetShopGradeUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_shop_grade
    }

    override fun bind(element: WidgetShopGradeUiModel) {
        setupShopGrade(element)
        setupShopScore(element)
    }

    private fun setupShopScore(element: WidgetShopGradeUiModel) = with(itemView) {
        val labelStringId = if (element.isNewSeller) {
            R.string.pm_shop_performance_sum_new_seller
        } else {
            R.string.pm_shop_performance_sum
        }

        tvPmShopGradeScore.text = context.getString(labelStringId, getShopScoreTextColor(element), getShopScoreFmt(element.shopScore)).parseAsHtml()
        tvPmShopGradeScoreTotal.text = context.getString(R.string.power_merchant_max_score)
        val thresholdInfo = if (element.periodType == PeriodType.TRANSITION_PERIOD) {
            context.getString(R.string.pm_shop_score_threshold_description, element.threshold, element.nextPmCalculationDate)
        } else {
            context.getString(R.string.pm_shop_score_threshold_description_final_period, element.threshold)
        }
        tvPmShopGradeThreshold.text = thresholdInfo.parseAsHtml()

        val days60ofTenure = 60
        val shopScoreTipsVisibility = if (element.shopScore < element.threshold && element.shopAge >= days60ofTenure) {
            View.VISIBLE
        } else {
            View.GONE
        }
        tvPmShopScoreTips.visibility = shopScoreTipsVisibility
        icPmShopScoreTips.visibility = shopScoreTipsVisibility
        tvPmShopScoreTips.setOnClickListener {
            RouteManager.route(context, Constant.Url.URL_SHOP_PERFORMANCE_TIPS)
        }
    }

    private fun getShopScoreTextColor(element: WidgetShopGradeUiModel): String {
        return when (element.shopScore) {
            in 1..element.threshold -> {
                PMCommonUtils.getHexColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R600)
            }
            else -> PMCommonUtils.getHexColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        }
    }

    private fun getShopScoreFmt(shopScore: Int): String {
        return when {
            (shopScore <= 0) -> "-"
            else -> shopScore.toString()
        }
    }

    private fun setupShopGrade(element: WidgetShopGradeUiModel) = with(itemView) {
        tvPmShopGrade.text = context.getString(R.string.pm_your_shop_grade, element.shopGrade.asCamelCase())
        imgPmShopGradeBackground.loadImage(element.gradeBackgroundUrl, R.drawable.bg_pm_registration_header)
        imgPmShopGrade.loadImageWithoutPlaceholder(element.gradeBadgeImgUrl)
        tvPmShopGradeStatus.text = getPMStatusLabel(element.pmStatus)
        tvPmShopGradeMessage.text = if (element.isNewSeller) {
            context.getString(R.string.pm_shop_grade_message_new_seller, element.newSellerTenure)
        } else {
            context.getString(R.string.pm_shop_grade_message)
        }
    }

    private fun getPMStatusLabel(pmStatus: String): String {
        return when (pmStatus) {
            PMStatusConst.INACTIVE -> itemView.context.getString(R.string.pm_inactive)
            else -> itemView.context.getString(R.string.pm_active)
        }
    }
}