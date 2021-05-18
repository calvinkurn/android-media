package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.*
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.PrintingMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerMenuItemUiModel
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.seller.menu.common.view.uimodel.StatisticMenuItemUiModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.setting_menu_list.view.*

class MenuItemsViewHolder(
    itemView: View,
    private val userSession: UserSessionInterface?,
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

    private var itemLabel: Label? = null

    init {
        itemLabel = itemView.findViewById(R.id.label_seller_menu_item)
    }

    override fun bind(element: MenuItemUiModel) {
        with(itemView) {
            element.drawableReference?.let { settingMenuIcon?.setImageDrawable(ContextCompat.getDrawable(context, it)) }
            element.iconUnify?.let { settingMenuIcon?.setImage(it) }
            settingMenuTitle.text = element.title
            if (element.isNoIcon) {
                element.trackingAlias?.let {
                    sendSettingShopInfoImpressionTracking(element, trackingListener::sendImpressionDataIris)
                }
            } else {
                sendSettingShopInfoImpressionTracking(element, trackingListener::sendImpressionDataIris)
            }
            bindNotificationCounter(element.notificationCount)
            itemLabel?.showWithCondition(element.isNewItem)
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

    private fun bindNotificationCounter(notificationCount: Int) {
        with(itemView) {
            if (notificationCount > 0) {
                settingMenuCounterIcon.setNotification(
                        notificationCount.toString(),
                        NotificationUnify.COUNTER_TYPE,
                        NotificationUnify.COLOR_PRIMARY)
                settingMenuCounterIcon.show()
            } else {
                settingMenuCounterIcon.gone()
            }
        }
    }

    private fun sendTracker(menuItem: MenuItemUiModel) {
        when(menuItem) {
            is SellerMenuItemUiModel -> sendClickSellerMenuEvent(menuItem)
            is StatisticMenuItemUiModel -> sendEventClickStatisticMenuItem(userSession?.userId.orEmpty())
            is PrintingMenuItemUiModel -> sendEventClickPrintingMenuItem(userSession?.userId.orEmpty())
            else -> menuItem.sendSettingShopInfoClickTracking()
        }
    }

    private fun sendClickSellerMenuEvent(menuItem: SellerMenuItemUiModel) {
        when (menuItem.type) {
            MenuItemType.REVIEW -> sellerMenuTracker?.sendEventClickReview()
            MenuItemType.DISCUSSION -> sellerMenuTracker?.sendEventClickDiscussion()
            MenuItemType.COMPLAIN -> sellerMenuTracker?.sendEventClickComplain()
            MenuItemType.SELLER_EDU -> sellerMenuTracker?.sendEventClickSellerEdu()
            MenuItemType.TOKOPEDIA_CARE -> sellerMenuTracker?.sendEventClickTokopediaCare()
            MenuItemType.BASIC_INFO -> sellerMenuTracker?.sendEventClickBasicInformation()
            MenuItemType.NOTES -> sellerMenuTracker?.sendEventClickShopNotes()
            MenuItemType.SCHEDULE -> sellerMenuTracker?.sendEventClickSchedule()
            MenuItemType.LOCATION -> sellerMenuTracker?.sendEventClickLocation()
            MenuItemType.SHIPPING -> sellerMenuTracker?.sendEventClickShipping()
            MenuItemType.NOTIFICATION -> sellerMenuTracker?.sendEventClickNotificationSettings()
            MenuItemType.SHOP_SETTINGS -> sellerMenuTracker?.sendEventClickShopSettings()
        }
    }
}