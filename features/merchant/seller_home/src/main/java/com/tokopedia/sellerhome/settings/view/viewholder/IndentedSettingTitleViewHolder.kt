package com.tokopedia.sellerhome.settings.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.IndentedSettingTitleUiModel
import kotlinx.android.synthetic.main.setting_title.view.*

class IndentedSettingTitleViewHolder(itemView: View) : AbstractViewHolder<IndentedSettingTitleUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.setting_title_indented
    }

    override fun bind(element: IndentedSettingTitleUiModel) {
        itemView.settingTitle.text = element.settingTitle
    }
}