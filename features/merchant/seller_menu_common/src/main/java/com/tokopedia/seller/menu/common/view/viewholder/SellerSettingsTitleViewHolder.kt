package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.SellerSettingsTitleUiModel
import kotlinx.android.synthetic.main.seller_menu_settings_title.view.*

class SellerSettingsTitleViewHolder(itemView: View) : AbstractViewHolder<SellerSettingsTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.seller_menu_settings_title
    }

    override fun bind(data: SellerSettingsTitleUiModel) {
        with(itemView) {
            data.settingDrawable?.let { settingTitleIcon.setImageDrawable(ContextCompat.getDrawable(context, it)) }
            settingTitleText.text = data.settingTitle
        }
    }
}