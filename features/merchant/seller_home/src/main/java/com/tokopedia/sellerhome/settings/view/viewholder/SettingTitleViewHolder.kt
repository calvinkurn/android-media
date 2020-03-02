package com.tokopedia.sellerhome.settings.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleUiModel
import kotlinx.android.synthetic.main.setting_title.view.*

class SettingTitleViewHolder(itemView: View) : AbstractViewHolder<SettingTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_title
    }

    override fun bind(element: SettingTitleUiModel) {
        itemView.settingTitle.text = element.title

    }


}