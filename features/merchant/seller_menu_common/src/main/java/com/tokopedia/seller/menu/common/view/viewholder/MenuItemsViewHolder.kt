package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.doOnAttach
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.analytics.sendEventClickStatisticMenuItem
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoClickTracking
import com.tokopedia.seller.menu.common.analytics.sendSettingShopInfoImpressionTracking
import com.tokopedia.seller.menu.common.constant.MenuItemType
import com.tokopedia.seller.menu.common.databinding.SettingMenuListBinding
import com.tokopedia.seller.menu.common.databinding.SettingMenuListNoIconBinding
import com.tokopedia.seller.menu.common.view.typefactory.CoachMarkListener
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerMenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.StatisticMenuItemUiModel
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface

class MenuItemsViewHolder(
    itemView: View,
    private val userSession: UserSessionInterface?,
    private val trackingListener: SettingTrackingListener,
    private val sellerMenuTracker: SellerMenuTracker?,
    private val coachMarkListener: CoachMarkListener? = null
) : AbstractViewHolder<MenuItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.setting_menu_list

        @LayoutRes
        val LAYOUT_NO_ICON = R.layout.setting_menu_list_no_icon

        fun getLayoutRes(isNoIcon: Boolean) =
            if (isNoIcon) LAYOUT_NO_ICON else LAYOUT
    }

    private var settingMenuIcon: IconUnify? = null
    private var settingMenuTitle: Typography? = null
    private var settingMenuCounterIcon: NotificationUnify? = null

    override fun bind(element: MenuItemUiModel) {
        with(itemView) {
            setupBinding(element.isNoIcon)
            element.iconUnify?.let { settingMenuIcon?.setImage(it) }
            settingMenuTitle?.text = element.title
            if (element.isNoIcon) {
                element.trackingAlias?.let {
                    sendSettingShopInfoImpressionTracking(element, trackingListener::sendImpressionDataIris)
                }
            } else {
                sendSettingShopInfoImpressionTracking(element, trackingListener::sendImpressionDataIris)
            }
            bindNotificationCounter(element.notificationCount)
            setOnClickListener {
                element.run {
                    sendTracker(this)
                    if (onClickApplink.isNullOrEmpty()) {
                        clickAction.invoke()
                    } else {
                        RouteManager.route(context, onClickApplink)
                    }

                    clickSendTracker.invoke()
                }
            }
            setupTag(element.tag)
            settingMenuIcon?.doOnAttach {
                coachMarkListener?.onViewReadyForCoachMark(element.title, it)
            }
        }
    }

    private fun setupTag(tag: String) {
        val settingMenuTag = itemView.findViewById<NotificationUnify>(R.id.settingMenuTag)
        settingMenuTag?.isVisible = tag.isNotBlank()
        settingMenuTag?.text = tag
    }

    private fun setupBinding(isNoIcon: Boolean) {
        if (isNoIcon) {
            val binding = SettingMenuListNoIconBinding.bind(itemView)
            settingMenuCounterIcon = binding.settingMenuCounterIcon
            settingMenuTitle = binding.settingMenuTitle
        } else {
            val binding = SettingMenuListBinding.bind(itemView)
            settingMenuIcon = binding.settingMenuIcon
            settingMenuCounterIcon = binding.settingMenuCounterIcon
            settingMenuTitle = binding.settingMenuTitle
        }
    }

    private fun bindNotificationCounter(notificationCount: Int) {
        if (notificationCount > 0) {
            settingMenuCounterIcon?.setNotification(
                notificationCount.toString(),
                NotificationUnify.COUNTER_TYPE,
                NotificationUnify.COLOR_PRIMARY
            )
            settingMenuCounterIcon?.show()
        } else {
            settingMenuCounterIcon?.gone()
        }
    }

    private fun sendTracker(menuItem: MenuItemUiModel) {
        when (menuItem) {
            is SellerMenuItemUiModel -> sendClickSellerMenuEvent(menuItem)
            is StatisticMenuItemUiModel -> sendEventClickStatisticMenuItem(userSession?.userId.orEmpty())
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
