package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.databinding.SettingToggleMenuItemBinding
import com.tokopedia.seller.menu.common.view.uimodel.ToggleMenuItemUiModel

class ToggleMenuItemViewHolder(
    itemView: View
) : AbstractViewHolder<ToggleMenuItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_toggle_menu_item
    }

    private val binding = SettingToggleMenuItemBinding.bind(itemView)

    override fun bind(element: ToggleMenuItemUiModel) {
        with(binding) {
            settingToggleMenuTitle.text = element.title
            settingToggleMenuTag.text = element.tag
            settingToggleSwitch.isChecked = element.isChecked
            settingToggleSwitch.setOnCheckedChangeListener { _, b ->
                element.isChecked = b
                element.onCheckedChanged?.invoke(b)
            }
        }
    }
}
