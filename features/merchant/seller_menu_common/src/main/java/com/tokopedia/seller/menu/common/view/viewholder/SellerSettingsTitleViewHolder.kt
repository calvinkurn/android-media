package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.SellerSettingsTitleUiModel
import com.tokopedia.unifyprinciples.Typography

class SellerSettingsTitleViewHolder(itemView: View) : AbstractViewHolder<SellerSettingsTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.seller_menu_settings_title
    }

    private val settingTitleIcon: IconUnify? = itemView.findViewById(R.id.settingTitleIcon)
    private val settingTitleText: Typography? = itemView.findViewById(R.id.settingTitleText)

    override fun bind(data: SellerSettingsTitleUiModel) {
        data.iconUnify?.let { settingTitleIcon?.setImage(it) }
        settingTitleText?.text = data.settingTitle
    }
}