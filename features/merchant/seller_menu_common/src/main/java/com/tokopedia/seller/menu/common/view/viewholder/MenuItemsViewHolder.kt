package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerMenuItemUiModel
import kotlinx.android.synthetic.main.setting_menu_list.view.*

class MenuItemsViewHolder(
    itemView: View,
    private val trackingListener: SettingTrackingListener,
    private val sellerMenuTracker: SellerMenuTracker?
) : AbstractViewHolder<MenuItemUiModel>(itemView) {

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
                    sendTracker(this)
                    if (onClickApplink.isNullOrEmpty()) {
                        clickAction.invoke()
                    } else {
                        RouteManager.route(context, onClickApplink)
                    }
                }
            }
        }
    }

    private fun sendTracker(menuItem: MenuItemUiModel) {
        when(menuItem) {
            is SellerMenuItemUiModel -> sendTrackerForMainApp(menuItem)
            else -> menuItem.sendSettingShopInfoClickTracking()
        }
    }

    private fun sendTrackerForMainApp(menuItem: SellerMenuItemUiModel) {
        when (menuItem.type) {
            MenuItemType.REVIEW -> sellerMenuTracker?.sendEventClickReview()
            MenuItemType.DISCUSSION -> sellerMenuTracker?.sendEventClickDiscussion()
            MenuItemType.COMPLAIN -> sellerMenuTracker?.sendEventClickComplain()
            MenuItemType.SELLER_EDU -> sellerMenuTracker?.sendEventClickSellerEdu()
            MenuItemType.TOKOPEDIA_CARE -> sellerMenuTracker?.sendEventClickTokopediaCare()
            MenuItemType.SHOP_SETTINGS -> sellerMenuTracker?.sendEventClickShopSettings()
        }
    }
}