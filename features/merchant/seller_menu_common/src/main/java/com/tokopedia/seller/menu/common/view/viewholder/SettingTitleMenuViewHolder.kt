package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleMenuUiModel
import kotlinx.android.synthetic.main.setting_title_menu.view.*

class SettingTitleMenuViewHolder(itemView: View) : AbstractViewHolder<SettingTitleMenuUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_title_menu
    }

    override fun bind(element: SettingTitleMenuUiModel) {
        with(itemView) {
            element.settingDrawable?.let { settingTitleIcon.setImageDrawable(ContextCompat.getDrawable(context, it)) }
            settingTitleText.text = element.settingTitle
        }
    }
}