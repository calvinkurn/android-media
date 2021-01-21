package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
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
            if (element.iconUnify != null) {
                settingTitleIcon.visible()
                settingTitleIcon.setImage(element.iconUnify)
            } else {
                settingTitleIcon.invisible()
            }
            settingTitleText.text = element.settingTitle
        }
    }
}