package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPotentialRegularMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusRMUiModel
import kotlinx.android.synthetic.main.item_potential_eligible_status_regular_merchant.view.*

class ItemStatusRMViewHolder(view: View,
                             private val itemPotentialPowerMerchantListener: ItemPotentialRegularMerchantListener):
        AbstractViewHolder<ItemStatusRMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_eligible_status_regular_merchant
    }

    override fun bind(element: ItemStatusRMUiModel?) {
        with(itemView) {
            tvTitleEligiblePowerMerchant?.text = getString(R.string.title_eligible_status_power_merchant,
                    element?.statusGradePM)
            tvDescEligiblePowerMerchant?.text = getString(R.string.desc_potential_eligible_power_merchant,
                    getShopScoreDate(context))
            containerEligiblePowerMerchant?.background = element?.bgGradePM?.let { ContextCompat.getDrawable(context, it) }
            //temporary (dummy)
            iv_pm_badge_eligible_status?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pm_gold_badge))
            tv_pm_potential_value?.text = MethodChecker.fromHtml(getString(R.string.desc_pm_potential))
            tv_see_all_benefit_pm?.setOnClickListener {
                itemPotentialPowerMerchantListener.onItemClickedBenefitPotentialRM()
            }
        }
    }
}