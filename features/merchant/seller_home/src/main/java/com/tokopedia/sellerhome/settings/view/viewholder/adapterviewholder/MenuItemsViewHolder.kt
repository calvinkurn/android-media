package com.tokopedia.sellerhome.settings.view.viewholder.adapterviewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import kotlinx.android.synthetic.main.setting_menu_list.view.*

class MenuItemsViewHolder(itemView: View) : AbstractViewHolder<MenuItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_menu_list
    }

    override fun bind(element: MenuItemUiModel) {
        with(itemView) {
            settingMenuIcon.setImageDrawable(ContextCompat.getDrawable(context, element.drawableReference))
            settingMenuTitle.text = element.title
            element.onClickApplink?.let { routeApplink ->
                setOnClickListener {
                    RouteManager.route(context, routeApplink)
                }
            }
        }
    }
}