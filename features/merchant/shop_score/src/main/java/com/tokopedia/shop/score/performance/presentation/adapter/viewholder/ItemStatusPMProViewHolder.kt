package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMProBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPMProListener
import com.tokopedia.shop.score.performance.presentation.model.SectionPotentialPMProUiModel
import kotlinx.android.synthetic.main.item_potential_pm_pro.view.*

class ItemStatusPMProViewHolder(view: View, private val itemStatusPMProListener: ItemStatusPMProListener):
        AbstractViewHolder<SectionPotentialPMProUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_pm_pro
    }

    private var itemPMProBenefitAdapter: ItemPMProBenefitAdapter? = null

    override fun bind(element: SectionPotentialPMProUiModel?) {
        itemPMProBenefitAdapter = ItemPMProBenefitAdapter()
        with(itemView) {
            itemStatusPMProListener.onItemPMProListener(this)
            tvDescPMPro?.text = getString(R.string.desc_potential_pm_pro,
                    element?.transitionEndDate)
            tv_see_all_benefit_pm_pro?.setOnClickListener {
                itemStatusPMProListener.onGotoPMProPage()
            }
            ic_chevron_right_benefit_pm_pro?.setOnClickListener {
                itemStatusPMProListener.onGotoPMProPage()
            }
        }
        setPotentialPMProBenefitAdapter(element)
    }

    private fun setPotentialPMProBenefitAdapter(element: SectionPotentialPMProUiModel?) {
        with(itemView) {
            rv_shop_pm_pro_potential_benefit?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemPMProBenefitAdapter
            }
        }
        element?.potentialPMProPMBenefitList?.let { itemPMProBenefitAdapter?.setPotentialPMProBenefit(it) }
    }
}