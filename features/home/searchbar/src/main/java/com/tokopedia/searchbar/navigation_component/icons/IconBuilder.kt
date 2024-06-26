package com.tokopedia.searchbar.navigation_component.icons

import android.os.Bundle
import com.tokopedia.searchbar.navigation_component.NavSource

class IconBuilder(val builderFlags: IconBuilderFlag = IconBuilderFlag()) {
    private val listIcon = mutableListOf<IconToolbar>()
    private val useCentralizedIconNotification = mutableMapOf<Int, Boolean>()

    internal fun build(): IconConfig {
        return IconConfig(listIcon, useCentralizedIconNotification)
    }

    //image icon
    fun addIcon(iconId: Int, disableRouteManager: Boolean = false, disableDefaultGtmTracker: Boolean = false, onClick: ()->Unit): IconBuilder {
        when(iconId) {
            //image
            IconList.ID_MESSAGE -> listIcon.add(IconList.MessageIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_INBOX -> listIcon.add(
                    IconList.InboxIcon.get(
                            builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager,
                            disableDefaultGtmTracker, onClick
                    )
            )
            IconList.ID_WISHLIST -> listIcon.add(IconList.WishlistIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_SHARE -> listIcon.add(IconList.ShareIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_CART -> listIcon.add(IconList.CartIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_NOTIFICATION -> listIcon.add(IconList.NotificationIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_NAV_GLOBAL -> listIcon.add(IconList.NavGlobalIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_SEARCH -> listIcon.add(IconList.SearchGlobalIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_INFORMATION -> listIcon.add(IconList.InformationGlobalIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_BILL -> listIcon.add(IconList.BillGlobalIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_LIST_TRANSACTION -> listIcon.add(IconList.ListTransactionIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_NOTEBOOK -> listIcon.add(IconList.NotebookIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            IconList.ID_SETTING -> listIcon.add(IconList.SettingIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))
            //lottiee
            IconList.ID_NAV_LOTTIE_WISHLIST -> listIcon.add(IconList.LottieWishlistIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))

            //Animated vector drawable
            IconList.ID_NAV_ANIMATED_WISHLIST -> listIcon.add(IconList.AnimatedWishlistIcon.get(builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick))

            // icon share for A/B testing
            IconList.ID_SHARE_AB_TEST -> listIcon.add(
                IconList.ShareAbTestIcon.get(
                    builderFlags.pageSource, builderFlags.pageSourcePath, disableRouteManager, disableDefaultGtmTracker, onClick
                )
            )
        }
        useCentralizedIconNotification[iconId] = true
        return this
    }

    fun disableIconNotification(iconId: Int) {
        useCentralizedIconNotification[iconId] = false
    }
}

internal data class IconConfig(val iconList: MutableList<IconToolbar>, val useCentralizedIconNotification: Map<Int, Boolean>)

internal data class IconToolbar(val id: Int, val name: String = "", val bundle: Bundle? = Bundle(), val imageRes: Int? = null, val applink: String, var nonLoginApplink: String = applink, var badgeCounter: Int = 0, var disableRouteManager: Boolean = false, var disableDefaultGtmTracker: Boolean = false, val iconType: Int = TYPE_IMAGE, val paddingEndRes: Int = 0, val onIconClicked: ()->Unit) {
    companion object {
        val TYPE_IMAGE = 0
        val TYPE_LOTTIE = 1
        val TYPE_ANIMATED = 2
    }
}

data class IconBuilderFlag(
    val pageSource: NavSource = NavSource.DEFAULT,
    val pageSourcePath: String? = null
)
