package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.databinding.SettingTitleIndentedBinding
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel

class IndentedSettingTitleViewHolder(itemView: View) : AbstractViewHolder<IndentedSettingTitleUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.setting_title_indented
    }

    private val binding = SettingTitleIndentedBinding.bind(itemView)

    override fun bind(element: IndentedSettingTitleUiModel) {
        binding.settingTitle.text = element.settingTitle
    }

}