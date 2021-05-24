package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.kotlin.extensions.view.asLowerCase
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.ShopGrade
import com.tokopedia.power_merchant.subscribe.view.adapter.NextShopGradeBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetNextShopGradeUiModel
import kotlinx.android.synthetic.main.widget_next_shop_grade.view.*

/**
 * Created By @ilhamsuaib on 17/03/21
 */

class NextShopGradeWidget(itemView: View) : AbstractViewHolder<WidgetNextShopGradeUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_next_shop_grade
    }

    override fun bind(element: WidgetNextShopGradeUiModel) {
        setupBenefitList(element)
        with(itemView) {
            layoutPmNextGradeInfo.setBackgroundResource(getBackgroundResByGrade(element.gradeName))
            val nextGradeInfo = context.getString(R.string.pm_next_pm_grade_info, element.shopLevel, element.shopScoreMin, element.gradeName.asCamelCase())
            tvPmNextGradeInfo.text = nextGradeInfo.parseAsHtml()
            imgPmNextGradeShopBadge.loadImageWithoutPlaceholder(element.gradeBadgeUrl)
        }
    }

    @DrawableRes
    private fun getBackgroundResByGrade(@ShopGrade gradeName: String): Int {
        return when (gradeName.asLowerCase()) {
            PMShopGrade.DIAMOND.asLowerCase() -> R.drawable.bg_pm_next_grade_diamond
            PMShopGrade.GOLD.asLowerCase() -> R.drawable.bg_pm_next_grade_gold
            else -> R.drawable.bg_pm_next_grade_silver
        }
    }

    private fun setupBenefitList(element: WidgetNextShopGradeUiModel) {
        val benefitAdapter = NextShopGradeBenefitAdapter(element.benefitList)
        with(itemView.rvPmNextGradeBenefit) {
            layoutManager = LinearLayoutManager(context)
            adapter = benefitAdapter
        }
    }
}