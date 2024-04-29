package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.ItemFeeServiceBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetShopExploreUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopExploreWidget(
    itemView: View,
    private val listener: Listener,
) : AbstractViewHolder<WidgetShopExploreUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.item_explore_shop
    }

    private val binding: ItemFeeServiceBinding? by viewBinding()
    override fun bind(element: WidgetShopExploreUiModel) {
        binding?.run {
            root.setOnClickListener {
                onLearnFeeClicked()
            }
            chevron.setOnClickListener {
                onLearnFeeClicked()
            }
        }
    }

    private fun onLearnFeeClicked() {
        listener.showAdsPromo()
    }

    interface Listener {
        fun showAdsPromo()
    }
}
