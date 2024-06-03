package com.tokopedia.shop.settings.setting.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.settings.databinding.ItemShopPageSettingsContentBinding
import com.tokopedia.shop.settings.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by astidhiyaa on 4/22/24
 */
class ContentSettingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemShopPageSettingsContentBinding? by viewBinding()

    fun bind(clickListener: ShopPageSettingAdapter.ContentItemClickListener) {
        binding?.tvEditContent?.setOnClickListener {
            clickListener.onClickContent()
        }
    }
}
