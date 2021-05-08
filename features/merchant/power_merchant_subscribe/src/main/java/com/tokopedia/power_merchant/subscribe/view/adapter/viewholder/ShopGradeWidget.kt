package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.utils.PMCommonUtils
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
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
        val thresholdInfo = context.getString(R.string.pm_shop_score_threshold_description_final_period, element.threshold, getPmTireLabel(element.pmTierType))
        tvPmShopGradeThreshold.text = thresholdInfo.parseAsHtml()

        val isPmShopScoreTipsVisible = element.pmStatus == PMStatusConst.IDLE
        tvPmShopScoreTips.isVisible = isPmShopScoreTipsVisible
        icPmShopScoreTips.isVisible = isPmShopScoreTipsVisible
        tvPmShopScoreTips.setOnClickListener {
            RouteManager.route(context, Constant.Url.URL_SHOP_PERFORMANCE_TIPS)
        }
    }

    private fun getPmTireLabel(pmTierType: Int): String {
        return if (pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO) {
            getString(R.string.pm_power_merchant_pro)
        } else {
            getString(R.string.pm_power_merchant)
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
        val isPmPro = element.pmTierType == PMConstant.PMTierType.POWER_MERCHANT_PRO
        tvPmShopGrade.text = if (isPmPro) {
            getString(R.string.pm_power_merchant_pro)
        } else {
            getString(R.string.pm_power_merchant)
        }
        imgPmShopGradeBackground.loadImage(element.gradeBackgroundUrl, R.drawable.bg_pm_registration_header)
        imgPmShopGrade.loadImageWithoutPlaceholder(element.gradeBadgeImgUrl)
        tvPmShopGradeStatus.text = getPMStatusLabel(element.pmStatus)
    }

    private fun getPMStatusLabel(pmStatus: String): String {
        return when (pmStatus) {
            PMStatusConst.INACTIVE -> itemView.context.getString(R.string.pm_inactive)
            else -> itemView.context.getString(R.string.pm_active)
        }
    }
}