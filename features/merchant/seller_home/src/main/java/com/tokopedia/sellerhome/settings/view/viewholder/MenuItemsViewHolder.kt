package com.tokopedia.sellerhome.settings.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingListener
import com.tokopedia.sellerhome.settings.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.sellerhome.settings.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import kotlinx.android.synthetic.main.setting_menu_list.view.*

class MenuItemsViewHolder(itemView: View,
                          private val trackingListener: SettingTrackingListener) : AbstractViewHolder<MenuItemUiModel>(itemView) {

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
            if (element.isNoIcon) {
                element.trackingAlias?.let {
                    sendSettingShopInfoImpressionTracking(element, trackingListener::sendImpressionDataIris)
                }
            } else {
                sendSettingShopInfoImpressionTracking(element, trackingListener::sendImpressionDataIris)
            }
            setOnClickListener {
                element.run {
                    sendSettingShopInfoClickTracking()
                    if (onClickApplink.isNullOrEmpty()) {
                        clickAction.invoke()
                    } else {
                        RouteManager.route(context, onClickApplink)
                    }
                }
            }
        }
    }
}