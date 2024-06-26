package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.databinding.SettingTitleBinding
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleUiModel

class SettingTitleViewHolder(itemView: View) : AbstractViewHolder<SettingTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_title
    }

    private val binding = SettingTitleBinding.bind(itemView)

    override fun bind(element: SettingTitleUiModel) {
        binding.settingTitle.text = element.title

        element.verticalSpacing?.let {
            val paddingTop = itemView.context.resources.getDimensionPixelSize(it)
            val paddingBottom = itemView.context.resources.getDimensionPixelSize(R.dimen.setting_title_padding_bottom)
            binding.settingTitle.setPadding(0, paddingTop, 0, paddingBottom)
        }
    }


}