package com.tokopedia.navigation.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.navigation.GlobalNavConstant
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.activity.MainParentActivity
import com.tokopedia.navigation.presentation.activity.NewMainParentActivity
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokopediaShortcutManager @Inject constructor(
    private val context: Context,
    private val userSession: UserSessionInterface,
) {

    fun addShortcuts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
        runCatching {
            val shortcutManager = context.getSystemService(ShortcutManager::class.java) ?: return@runCatching
            shortcutManager.removeAllDynamicShortcuts()

            val args = Bundle().apply {
                putBoolean(GlobalNavConstant.EXTRA_APPLINK_FROM_PUSH, true)
                putBoolean(GlobalNavConstant.FROM_APP_SHORTCUTS, true)
            }

            val homeIntent = MainParentActivity.start(context)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setAction(RouteManager.INTERNAL_VIEW)

            val shortcutInfoList = buildList {
                add(buildSearchAutoCompleteShortcut(args, homeIntent))
                if (userSession.isLoggedIn) {
                    add(buildWishlistShortcut(args, homeIntent))
                }
                add(buildPayShortcut(args, homeIntent))
                if (userSession.isLoggedIn) {
                    add(buildSellShortcut(args, homeIntent))
                }
            }
            shortcutManager.addDynamicShortcuts(shortcutInfoList)
        }.onFailure {
            it.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildSearchAutoCompleteShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val searchIntent = RouteManager.getIntent(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
            .setAction(RouteManager.INTERNAL_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(context, Shortcut.Search.id)
            .setShortLabel(context.getString(R.string.navigation_home_label_shortcut_search))
            .setLongLabel(context.getString(R.string.navigation_home_label_shortcut_search))
            .setIcon(Icon.createWithResource(context, R.drawable.main_parent_navigation_ic_search_shortcut))
            .setIntents(arrayOf(homeIntent, searchIntent))
            .setRank(Shortcut.Search.ordinal)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildWishlistShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val wishlistIntent = RouteManager.getIntent(context, ApplinkConst.NEW_WISHLIST)
            .setAction(Intent.ACTION_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(context, Shortcut.Wishlist.id)
            .setShortLabel(context.getString(R.string.navigation_home_label_shortcut_wishlist))
            .setLongLabel(context.getString(R.string.navigation_home_label_shortcut_wishlist))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_wishlist_shortcut))
            .setIntents(arrayOf(homeIntent, wishlistIntent))
            .setRank(Shortcut.Wishlist.ordinal)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildPayShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val digitalIntent = RouteManager.getIntent(context, ApplinkConst.RECHARGE_SUBHOMEPAGE_HOME_NEW)
            .setAction(Intent.ACTION_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(context, Shortcut.Pay.id)
            .setShortLabel(context.getString(R.string.navigation_home_label_shortcut_pay))
            .setLongLabel(context.getString(R.string.navigation_home_label_shortcut_pay))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_pay_shortcut))
            .setIntents(arrayOf(homeIntent, digitalIntent))
            .setRank(Shortcut.Pay.ordinal)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildSellShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val shopIntent = if (userSession.hasShop()) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_PAGE, userSession.shopId)
        } else {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.OPEN_SHOP)
        }.setAction(Intent.ACTION_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(context, Shortcut.Sell.id)
            .setShortLabel(context.getString(R.string.navigation_home_label_shortcut_sell))
            .setLongLabel(context.getString(R.string.navigation_home_label_shortcut_sell))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_sell_shortcut))
            .setIntents(arrayOf(homeIntent, shopIntent))
            .setRank(Shortcut.Sell.ordinal)
            .build()
    }

    enum class Shortcut(val id: String) {
        Search("Cari"),
        Wishlist("Wishlist"),
        Pay("Tagihan"),
        Sell("Jual")
    }
}
