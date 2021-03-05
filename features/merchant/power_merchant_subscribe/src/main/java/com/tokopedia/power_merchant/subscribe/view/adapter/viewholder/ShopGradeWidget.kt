package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.ShopGrade
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
        with(itemView) {
            containerPmShopGrade.setBackgroundResource(R.drawable.tmp_bg_silver)

            tvPmShopGrade.text = getShopGradeStr(element.shopGrade)
            imgPmShopGrade.loadImageDrawable(R.drawable.tmp_pm_badge_silver)
        }
    }

    private fun getShopGradeStr(shopGrade: Int): String {
        val gradeResId = when (shopGrade) {
            ShopGrade.SILVER -> R.string.pm_silver
            ShopGrade.GOLD -> R.string.pm_gold
            ShopGrade.DIAMOND -> R.string.pm_diamond
            else -> R.string.pm_bronze
        }
        val gradeStr = itemView.context.getString(gradeResId)
        return itemView.context.getString(R.string.pm_your_shop_grade, gradeStr)
    }
}