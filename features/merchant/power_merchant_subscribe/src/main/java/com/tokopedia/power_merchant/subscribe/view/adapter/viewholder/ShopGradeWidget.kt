package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
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
        tvPmShopGradeScore.text = context.getString(R.string.pm_shop_performance_sum, element.shopScore)
        tvPmShopGradeScoreTotal.text = context.getString(R.string.power_merchant_max_score)
        val thresholdInfo = context.getString(R.string.pm_shop_score_threshold_description, element.threshold, element.nextPmCalculationDate)
        tvPmShopGradeThreshold.text = thresholdInfo.parseAsHtml()
    }

    private fun setupShopGrade(element: WidgetShopGradeUiModel) = with(itemView) {
        tvPmShopGrade.text = context.getString(R.string.pm_your_shop_grade, element.shopGrade)
        imgPmShopGradeBackground.loadImage(element.gradeBackgroundUrl, R.drawable.bg_pm_registration_header)
        imgPmShopGrade.loadImageWithoutPlaceholder(element.gradeBadgeImgUrl)
    }
}