package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemBenefitPowerMerchantNewSellerBinding
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPmProNewSellerBenefitUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPMProNewSellerBenefitWidget(view: View, private val pmWidgetListener: PMWidgetListener) :
    AbstractViewHolder<WidgetPmProNewSellerBenefitUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_power_merchant_new_seller
    }

    private val binding: ItemBenefitPowerMerchantNewSellerBinding? by viewBinding()

    override fun bind(element: WidgetPmProNewSellerBenefitUiModel?) {
        binding?.run {
            tvLearnMoreBenefitExclusive.setOnClickListener {
                pmWidgetListener.onPMProNewSellerLearnMore()
            }
        }
        setupAdapter(element)
    }

    private fun setupAdapter(element: WidgetPmProNewSellerBenefitUiModel?) {
        binding?.run {
            rvBenefitExclusivePmPro.run {
                layoutManager = object: LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
                adapter = PMProBenefitAdapter(element?.items.orEmpty())
            }
        }
    }

    interface Listener {
        fun onPMProNewSellerLearnMore()
    }
}