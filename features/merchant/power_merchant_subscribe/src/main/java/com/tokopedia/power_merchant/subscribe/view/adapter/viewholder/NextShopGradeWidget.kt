package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.gm.common.constant.PMShopGrade
import com.tokopedia.kotlin.extensions.view.asCamelCase
import com.tokopedia.kotlin.extensions.view.asUpperCase
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.common.constant.ShopGrade
import com.tokopedia.power_merchant.subscribe.common.utils.PowerMerchantSpannableUtil
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
            tvPmNextGradeInfo.setBackgroundResource(getBackgroundResByGrade(element.gradeName))

            val ctaTextColor = com.tokopedia.unifyprinciples.R.color.Unify_G500
            val clickableText = getString(R.string.power_merchant_learn_more)
            val infoDescription = PowerMerchantSpannableUtil.createSpannableString(
                    text = context.getString(R.string.pm_next_pm_grade_info, element.shopLevel, element.gradeName.asUpperCase()).parseAsHtml(),
                    highlightText = clickableText,
                    colorId = context.getResColor(ctaTextColor),
                    isBold = true
            ) {
                RouteManager.route(itemView.context, Constant.Url.PM_PRO_BENEFIT_PACKAGE_EDU)
            }
            tvPmNextGradeInfo.movementMethod = LinkMovementMethod.getInstance()
            tvPmNextGradeInfo.text = infoDescription
        }
    }

    @DrawableRes
    private fun getBackgroundResByGrade(@ShopGrade gradeName: String): Int {
        return when {
            PMShopGrade.ULTIMATE.equals(gradeName, true) -> R.drawable.bg_pm_next_grade_advanced
            else -> R.drawable.bg_pm_next_grade_expert
        }
    }

    private fun setupBenefitList(element: WidgetNextShopGradeUiModel) {
        val benefitAdapter = NextShopGradeBenefitAdapter(element.benefitList)
        with(itemView.rvPmNextGradeBenefit) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = benefitAdapter
        }
    }
}