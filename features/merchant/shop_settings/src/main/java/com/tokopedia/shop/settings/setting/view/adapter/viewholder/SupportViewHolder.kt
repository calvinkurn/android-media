package com.tokopedia.shop.settings.setting.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.settings.databinding.ItemShopPageSettingSupportBinding
import com.tokopedia.shop.settings.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.utils.view.binding.viewBinding

class SupportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemShopPageSettingSupportBinding? by viewBinding()

    fun bind(clickListener: ShopPageSettingAdapter.SupportItemClickListener) {
        binding?.apply {
            supportLayout.setOnClickListener { clickListener.onGetSupportClicked() }
            sellerLayout.setOnClickListener { clickListener.onGetTipsClicked() }
        }
    }
}