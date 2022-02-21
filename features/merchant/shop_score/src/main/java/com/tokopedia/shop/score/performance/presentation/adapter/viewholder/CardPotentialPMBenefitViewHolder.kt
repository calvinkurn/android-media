package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemNonEligibleStatusPowerMerchantBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPotentialPMBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ItemRegularMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.SectionRMPotentialPMBenefitUiModel
import com.tokopedia.utils.view.binding.viewBinding

class CardPotentialPMBenefitViewHolder(
    view: View,
    private val itemRegularMerchantListener: ItemRegularMerchantListener
) : AbstractViewHolder<SectionRMPotentialPMBenefitUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_non_eligible_status_power_merchant
    }

    private val binding: ItemNonEligibleStatusPowerMerchantBinding? by viewBinding()

    private var itemPotentialPMBenefitAdapter: ItemPotentialPMBenefitAdapter? = null

    override fun bind(element: SectionRMPotentialPMBenefitUiModel?) {
        itemPotentialPMBenefitAdapter = ItemPotentialPMBenefitAdapter(itemRegularMerchantListener)
        binding?.run {
            setPotentialPMBenefitAdapter(element)
            root.setOnClickListener {
                itemRegularMerchantListener.onRMSectionToPMPage()
            }
        }
    }

    private fun setPotentialPMBenefitAdapter(element: SectionRMPotentialPMBenefitUiModel?) {
        element?.potentialPMBenefitList?.let {
            binding?.run {
                rvShopPmPotentialBenefit.run {
                    layoutManager = if (DeviceScreenInfo.isTablet(context)) {
                        GridLayoutManager(context, it.size)
                    } else {
                        LinearLayoutManager(context)
                    }
                    adapter = itemPotentialPMBenefitAdapter
                }
                itemPotentialPMBenefitAdapter?.setPotentialPowerMerchantBenefit(it)
            }
        }
    }
}