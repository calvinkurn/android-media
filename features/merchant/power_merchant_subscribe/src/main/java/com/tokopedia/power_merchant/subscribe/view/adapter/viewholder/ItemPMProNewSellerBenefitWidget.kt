package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.PMProBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.model.WidgetPmProNewSellerBenefitUiModel
import kotlinx.android.synthetic.main.item_benefit_power_merchant_new_seller.view.*

class ItemPMProNewSellerBenefitWidget(view: View, private val pmWidgetListener: PMWidgetListener) :
    AbstractViewHolder<WidgetPmProNewSellerBenefitUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_benefit_power_merchant_new_seller
    }

    override fun bind(element: WidgetPmProNewSellerBenefitUiModel?) {
        with(itemView) {
            tvLearnMoreBenefitExclusive?.setOnClickListener {
                pmWidgetListener.onPMProNewSellerLearnMore()
            }
        }
        setupAdapter(element)
    }

    private fun setupAdapter(element: WidgetPmProNewSellerBenefitUiModel?) {
        with(itemView) {
            rvBenefitExclusivePmPro?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = PMProBenefitAdapter(element?.items.orEmpty())
            }
        }
    }

    interface Listener {
        fun onPMProNewSellerLearnMore()
    }
}