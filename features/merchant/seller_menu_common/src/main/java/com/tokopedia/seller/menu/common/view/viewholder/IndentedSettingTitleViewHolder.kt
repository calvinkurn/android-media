package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel
import kotlinx.android.synthetic.main.setting_title_indented.view.*

class IndentedSettingTitleViewHolder(itemView: View) : AbstractViewHolder<IndentedSettingTitleUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.setting_title_indented
    }

    override fun bind(element: IndentedSettingTitleUiModel) {
        itemView.settingTitle.text = element.settingTitle
    }
}