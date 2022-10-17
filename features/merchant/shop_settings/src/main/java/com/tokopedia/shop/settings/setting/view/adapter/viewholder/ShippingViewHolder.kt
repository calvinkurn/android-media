package com.tokopedia.shop.settings.setting.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.settings.databinding.ItemShopPageSettingShippingBinding
import com.tokopedia.shop.settings.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.utils.view.binding.viewBinding

class ShippingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemShopPageSettingShippingBinding? by viewBinding()

    fun bind(isEligibleForMultiLocation: Boolean, clickListener: ShopPageSettingAdapter.ShippingItemClickListener) {
        binding?.apply {
            tvEditLocation.apply {
                showWithCondition(isEligibleForMultiLocation)
                setOnClickListener {
                    clickListener.onEditLocationClicked()
                }
            }
            tvManageShippingService.setOnClickListener { clickListener.onManageShippingServiceClicked() }
        }
    }
}