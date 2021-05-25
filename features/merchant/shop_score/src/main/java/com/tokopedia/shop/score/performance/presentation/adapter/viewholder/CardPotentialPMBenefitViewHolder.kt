package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPotentialPMBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.model.SectionPotentialPMBenefitUiModel
import kotlinx.android.synthetic.main.item_non_eligible_status_power_merchant.view.*

class CardPotentialPMBenefitViewHolder(view: View):
        AbstractViewHolder<SectionPotentialPMBenefitUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_non_eligible_status_power_merchant
    }

    private var itemPotentialPMBenefitAdapter: ItemPotentialPMBenefitAdapter? = null

    override fun bind(element: SectionPotentialPMBenefitUiModel?) {
        itemPotentialPMBenefitAdapter = ItemPotentialPMBenefitAdapter()
        with(itemView) {
            tvDescNonEligiblePowerMerchant.text = getString(R.string.desc_non_eligible_power_merchant,
                    element?.transitionEndDate)
            setPotentialPMBenefitAdapter(element)
        }
    }

    private fun setPotentialPMBenefitAdapter(element: SectionPotentialPMBenefitUiModel?) {
        with(itemView) {
            rv_shop_pm_potential_benefit?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemPotentialPMBenefitAdapter
            }
        }
        element?.potentialPMBenefitList?.let { itemPotentialPMBenefitAdapter?.setPotentialPowerMerchantBenefit(it) }
    }
}