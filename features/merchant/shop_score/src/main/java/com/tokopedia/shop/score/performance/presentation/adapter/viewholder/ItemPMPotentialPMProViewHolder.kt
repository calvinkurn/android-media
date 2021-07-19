package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMPotentialPMProListener
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMProBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.model.SectionRMPotentialPMProUiModel
import kotlinx.android.synthetic.main.item_potential_rm_to_pm_pro.view.*

class ItemPMPotentialPMProViewHolder(view: View, private val itemStatusPMProListener: ItemPMPotentialPMProListener):
    AbstractViewHolder<SectionRMPotentialPMProUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_pm_to_pm_pro
    }

    private var itemPMProBenefitAdapter: ItemPMProBenefitAdapter? = null

    override fun bind(element: SectionRMPotentialPMProUiModel?) {
        itemPMProBenefitAdapter = ItemPMProBenefitAdapter()
        with(itemView) {
            tv_see_all_benefit_pm_pro?.setOnClickListener {
                itemStatusPMProListener.onGotoBenefitPMPro()
            }
            ic_chevron_right_benefit_pm_pro?.setOnClickListener {
                itemStatusPMProListener.onGotoBenefitPMPro()
            }
        }
        setPotentialPMProBenefitAdapter(element)
    }

    private fun setPotentialPMProBenefitAdapter(element: SectionRMPotentialPMProUiModel?) {
        with(itemView) {
            rv_shop_pm_pro_potential_benefit?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemPMProBenefitAdapter
            }
        }
        element?.potentialPMProPMBenefitList?.let { itemPMProBenefitAdapter?.setPotentialPMProBenefit(it) }
    }
}