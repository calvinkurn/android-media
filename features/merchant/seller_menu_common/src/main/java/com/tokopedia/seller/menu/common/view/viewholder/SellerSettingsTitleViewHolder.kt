package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.databinding.SellerMenuSettingsTitleBinding
import com.tokopedia.seller.menu.common.view.uimodel.SellerSettingsTitleUiModel

class SellerSettingsTitleViewHolder(itemView: View) : AbstractViewHolder<SellerSettingsTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.seller_menu_settings_title
    }

    private val binding = SellerMenuSettingsTitleBinding.bind(itemView)

    override fun bind(data: SellerSettingsTitleUiModel) {
        data.iconUnify?.let {
            binding.settingTitleIcon.setImage(it)
        }
        binding.settingTitleText.text = data.settingTitle
    }
}