package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPotentialRmToPmProBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMProBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ItemRMPotentialPMProListener
import com.tokopedia.shop.score.performance.presentation.model.SectionRMPotentialPMProUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemRMPotentialPMProViewHolder(
    view: View,
    private val itemStatusPMProListener: ItemRMPotentialPMProListener
) :
    AbstractViewHolder<SectionRMPotentialPMProUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_rm_to_pm_pro
    }

    private var itemPMProBenefitAdapter: ItemPMProBenefitAdapter? = null
    private val binding: ItemPotentialRmToPmProBinding? by viewBinding()

    override fun bind(element: SectionRMPotentialPMProUiModel?) {
        itemPMProBenefitAdapter = ItemPMProBenefitAdapter()
        binding?.run {
            tvSeeAllBenefitPmPro.setOnClickListener {
                itemStatusPMProListener.onGotoPMProPage()
            }
            icChevronRightBenefitPmPro.setOnClickListener {
                itemStatusPMProListener.onGotoPMProPage()
            }
        }
        setPotentialPMProBenefitAdapter(element)
    }

    private fun setPotentialPMProBenefitAdapter(element: SectionRMPotentialPMProUiModel?) {
        element?.potentialPMProPMBenefitList?.let {
            binding?.run {
                rvShopPmProPotentialBenefit.run {
                    layoutManager = if (DeviceScreenInfo.isTablet(context)) {
                        GridLayoutManager(context, it.size)
                    } else {
                        LinearLayoutManager(context)
                    }
                    adapter = itemPMProBenefitAdapter
                }
                itemPMProBenefitAdapter?.setPotentialPMProBenefit(it)
            }
        }
    }
}