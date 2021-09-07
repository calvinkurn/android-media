package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPotentialPmToPmProBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMPotentialPMProListener
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMProBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.model.SectionPMPotentialPMProUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPMPotentialPMProViewHolder(
    view: View,
    private val itemStatusPMProListener: ItemPMPotentialPMProListener
): AbstractViewHolder<SectionPMPotentialPMProUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_pm_to_pm_pro
    }

    private val binding: ItemPotentialPmToPmProBinding? by viewBinding()
    private var itemPMProBenefitAdapter: ItemPMProBenefitAdapter? = null

    override fun bind(element: SectionPMPotentialPMProUiModel?) {
        itemPMProBenefitAdapter = ItemPMProBenefitAdapter()
        binding?.apply {
            tvPmReputationValue.text = getString(R.string.title_pm_value)
            tvSeeAllBenefitPmToPmPro.setOnClickListener {
                itemStatusPMProListener.onGotoBenefitPMPro()
            }
            icChevronRightBenefitPmToPmPro.setOnClickListener {
                itemStatusPMProListener.onGotoBenefitPMPro()
            }
        }
        setPotentialPMProBenefitAdapter(element)
    }

    private fun setPotentialPMProBenefitAdapter(element: SectionPMPotentialPMProUiModel?) {
        binding?.apply {
            rvPmToPmProPotentialBenefit.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemPMProBenefitAdapter
            }
        }
        element?.potentialPMProPMBenefitList?.let {
            itemPMProBenefitAdapter?.setPotentialPMProBenefit(
                it
            )
        }
    }
}