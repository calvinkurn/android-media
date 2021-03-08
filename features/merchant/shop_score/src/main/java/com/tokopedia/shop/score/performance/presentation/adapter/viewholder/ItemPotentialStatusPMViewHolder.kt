package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPotentialPowerMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemPotentialStatusPMUiModel
import kotlinx.android.synthetic.main.item_potential_eligible_status_power_merchant.view.*

class ItemPotentialStatusPMViewHolder(view: View,
                                      private val itemPotentialPowerMerchantListener: ItemPotentialPowerMerchantListener):
        AbstractViewHolder<ItemPotentialStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_eligible_status_power_merchant
    }

    override fun bind(element: ItemPotentialStatusPMUiModel?) {
        with(itemView) {
            tvTitleEligiblePowerMerchant?.text = getString(R.string.title_eligible_status_power_merchant,
                    element?.datePotentialStatus)
            tvDescEligiblePowerMerchant?.text = getString(R.string.desc_potential_eligible_power_merchant,
                    getShopScoreDate(context))
            //temporary (dummy)
            iv_pm_badge_eligible_status?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pm_gold_badge))
            tv_pm_potential_value?.text = getString(R.string.desc_pm_potential, element?.upToPotentialPM.orEmpty())
            tv_see_all_benefit_pm?.setOnClickListener {
                itemPotentialPowerMerchantListener.onItemClickedBenefitPotentialPM()
            }
        }
    }
}