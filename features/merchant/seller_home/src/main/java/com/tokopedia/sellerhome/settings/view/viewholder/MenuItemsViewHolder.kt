package com.tokopedia.sellerhome.settings.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.sellerhome.settings.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import kotlinx.android.synthetic.main.setting_menu_list.view.*

class MenuItemsViewHolder(itemView: View) : AbstractViewHolder<MenuItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_menu_list
        @LayoutRes
        val LAYOUT_NO_ICON = R.layout.setting_menu_list_no_icon

        fun getLayoutRes(isNoIcon: Boolean) =
                if (isNoIcon) LAYOUT_NO_ICON else LAYOUT
    }

    override fun bind(element: MenuItemUiModel) {
        with(itemView) {
            element.drawableReference?.let { settingMenuIcon.setImageDrawable(ContextCompat.getDrawable(context, it)) }
            settingMenuTitle.text = element.title
            sendSettingShopInfoImpressionTracking(element, context)
            setOnClickListener {
                element.sendSettingShopInfoClickTracking()
                if (element.onClickApplink.isNullOrEmpty()) {
                    element.clickAction.invoke()
                } else {
                    RouteManager.route(context, element.onClickApplink)
                }
            }
        }
    }
}