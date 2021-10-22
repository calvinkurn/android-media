package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.databinding.SettingTitleMenuBinding
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleMenuUiModel

class SettingTitleMenuViewHolder(itemView: View) : AbstractViewHolder<SettingTitleMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_title_menu
    }

    private val binding = SettingTitleMenuBinding.bind(itemView)

    override fun bind(element: SettingTitleMenuUiModel) {
        with(binding.settingTitleIcon) {
            if (element.iconUnify == null) {
                invisible()
            } else {
                visible()
                setImage(element.iconUnify)
            }
        }
        binding.settingTitleText.text = element.settingTitle
    }

}