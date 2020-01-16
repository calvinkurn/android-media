package com.tokopedia.sellerhomedrawer.view.helper

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.TaskStackBuilder
import androidx.core.view.GravityCompat
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.core.drawer2.view.DrawerAdapter
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.analytics.*
import com.tokopedia.sellerhomedrawer.constant.SellerBaseUrl
import com.tokopedia.sellerhomedrawer.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.view.adapter.SellerDrawerAdapter
import com.tokopedia.sellerhomedrawer.view.adapter.SellerDrawerAdapterTypeFactory
import com.tokopedia.sellerhomedrawer.view.dashboard.SellerDashboardActivity
import com.tokopedia.sellerhomedrawer.view.listener.*
import com.tokopedia.sellerhomedrawer.view.viewmodel.SellerDrawerGroup
import com.tokopedia.sellerhomedrawer.view.viewmodel.SellerDrawerItem
import com.tokopedia.sellerhomedrawer.view.viewmodel.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.view.webview.SellerHomeWebViewActivity
import com.tokopedia.sellerhomedrawer.view.webview.SellerSimpleWebViewActivity
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.sh_drawer_activity.*
import kotlinx.android.synthetic.main.sh_drawer_layout.*
import kotlinx.android.synthetic.main.sh_drawer_shop.*

class SellerDrawerHelper(val context: Activity,
                         val userSession: UserSession,
                         val drawerCache: LocalCacheHandler) :
        SellerDrawerItemListener, SellerDrawerHeaderListener, SellerDrawerGroupListener ,
        DrawerHeaderListener, RetryTokoCashListener {

    companion object {
        @JvmStatic
        val DIGITAL_WEBSITE_DOMAIN = TokopediaUrl.getInstance().PULSA
        @JvmStatic
        val DIGITAL_PATH_MITRA = "mitra"
    }

    var sellerDrawerAdapter: SellerDrawerAdapter? = null
    var powerMerchantInstance: SellerDrawerItem? = null

    private var selectedPosition = -1

    override fun onItemClicked(drawerItem: SellerDrawerItem) {
        if (drawerItem.id == selectedPosition) closeDrawer()
        else {
            var intent: Intent? = null
            var isNeedToCloseActivity = true
            when(drawerItem.id) {
                SellerHomeState.DrawerPosition.INDEX_HOME -> {
                    eventDrawerClick(EventLabel.SELLER_HOME)
                    //TODO: implement full SellerDashboardActivity feature
                    context.startActivity(SellerDashboardActivity.createInstance(context))
                }
                SellerHomeState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND -> {
                    if (context.application is AbstractionRouter)
                        sendClickHamburgerMenuEvent(drawerItem.label)
                    eventClickGoldMerchantViaDrawer()
                    RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                }
                SellerHomeState.DrawerPosition.SHOP_NEW_ORDER -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SELLING_TRANSACTION)
                    eventDrawerClick(EventLabel.NEW_ORDER)
                }
                SellerHomeState.DrawerPosition.SHOP_CONFIRM_SHIPPING -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SELLING_TRANSACTION)
                    eventDrawerClick(EventLabel.DELIVERY_CONFIRMATION)
                }
                SellerHomeState.DrawerPosition.SHOP_SHIPPING_STATUS -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SELLING_TRANSACTION)
                    eventDrawerClick(EventLabel.DELIVERY_CONFIRMATION)
                }
                SellerHomeState.DrawerPosition.SHOP_TRANSACTION_LIST -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SELLING_TRANSACTION)
                    eventDrawerClick(EventLabel.SALES_LIST)
                }
                SellerHomeState.DrawerPosition.SHOP_OPPORTUNITY_LIST -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SELLING_TRANSACTION)
                }
                SellerHomeState.DrawerPosition.ADD_PRODUCT -> {
                    //TODO : context is TkpdCoreRouter ?
                    val manageProductIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
                    val addProductIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM)
                    TaskStackBuilder.create(context)
                            .addNextIntent(manageProductIntent)
                            .addNextIntent(addProductIntent)
                            .startActivities()
                }
                SellerHomeState.DrawerPosition.MANAGE_PRODUCT -> {
                    //TODO : is this applink correct ?
                    RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
                }
                SellerHomeState.DrawerPosition.MANAGE_PAYMENT_AND_TOPUP -> {
                    //TODO : Check if you can use intent to move between activities in library (not features)
                    eventClickPaymentAndTopupOnDrawer()
                    context.startActivity(SellerSimpleWebViewActivity.createIntent(context, DIGITAL_WEBSITE_DOMAIN + DIGITAL_PATH_MITRA))
                }
                SellerHomeState.DrawerPosition.MANAGE_TRANSACTION_DIGITAL -> {
                    eventClickDigitalTransactionListOnDrawer()
                    context.startActivity(SellerSimpleWebViewActivity.createIntent(context, DIGITAL_WEBSITE_DOMAIN + DIGITAL_PATH_MITRA))
                }
                SellerHomeState.DrawerPosition.DRAFT_PRODUCT -> {
                    //TODO : Check the applink
                    eventDrawerClick(EventLabel.DRAFT_PRODUCT)
                    RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DRAFT_LIST)
                }
                SellerHomeState.DrawerPosition.MANAGE_ETALASE -> {
                    eventDrawerClick(EventLabel.PRODUCT_DISPLAY)
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE)
                }
                SellerHomeState.DrawerPosition.SELLER_GM_STAT -> {
                    eventClickGMStat(Category.HAMBURGER,
                            EventLabel.STATISTIC)
                    RouteManager.route(context, ApplinkConstInternalMarketplace.GOLD_MERCHANT_STATISTIC_DASHBOARD)
                }
                SellerHomeState.DrawerPosition.SELLER_MITRA_TOPPERS -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.MITRA_TOPPERS_DASHBOARD)
                }
                SellerHomeState.DrawerPosition.SELLER_TOP_ADS -> {
                    eventDrawerClick(EventLabel.TOPADS)
                    RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTOADS)
                }
                SellerHomeState.DrawerPosition.SELLER_FLASH_SALE -> {
                    RouteManager.route(context, ApplinkConst.SellerApp.FLASHSALE_MANAGEMENT)
                }
                SellerHomeState.DrawerPosition.FEATURED_PRODUCT -> {
                    eventFeaturedProduct(EventLabel.FEATURED_PRODUCT)
                    RouteManager.route(context, ApplinkConstInternalMarketplace.GOLD_MERCHANT_FEATURED_PRODUCT)
                }
                SellerHomeState.DrawerPosition.SELLER_INFO -> {
                    eventSellerInfo(
                            Action.CLICK_HAMBURGER_ICON,
                            EventLabel.SELLER_INFO
                    )
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SELLER_INFO)
                }
                SellerHomeState.DrawerPosition.RESOLUTION_CENTER -> {
                    eventDrawerClick(EventLabel.RESOLUTION_CENTER)
                    context.startActivity(SellerHomeWebViewActivity.createIntent(context, SellerBaseUrl.HOSTNAME + SellerBaseUrl.RESO_INBOX_SELLER))
                }
                else -> {
                    //TODO: Extends from DrawerHelper ?
                }
            }

            if (selectedPosition != SellerHomeState.DrawerPosition.INDEX_HOME && drawerItem.id != SellerHomeState.DrawerPosition.LOGOUT && isNeedToCloseActivity)
                context.finish()

            closeDrawer()
        }
    }

    fun closeDrawer() { context.drawer_layout_nav.closeDrawer(GravityCompat.START) }

    fun openDrawer() { context.drawer_layout_nav.openDrawer(GravityCompat.START) }

    override fun notifyDataSetChanged() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoToDeposit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoToProfile() {

    }

    override fun onGoToProfileCompletion() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGroupClicked(sellerDrawerGroup: SellerDrawerGroup) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGoToTopPoints(topPointsUrl: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWalletBalanceClicked(redirectUrlBalance: String?, appLinkBalance: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWalletActionButtonClicked(redirectUrlActionButton: String?, appLinkActionButton: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTokoPointActionClicked(mainPageUrl: String?, title: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGotoTokoCard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRetryTokoCash() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun createDrawerData(): List<SellerDrawerItem> {
        val drawerItemData = mutableListOf<SellerDrawerItem>()
        
        if (powerMerchantInstance == null) 
            powerMerchantInstance = getPowerMerchantDrawerInstance()
        
        val adapter = sellerDrawerAdapter
        val powerMerchantDrawerItem = powerMerchantInstance
        
        drawerItemData.apply { 
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_home),
                    iconId = R.drawable.icon_home,
                    id = SellerHomeState.DrawerPosition.SELLER_INDEX_HOME,
                    isExpanded = true))
            add(getSellerMenu())
            add(getInboxMenu())
            add(getProductMenu())
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_top_ads),
                    iconId = R.drawable.sh_ic_top_ads,
                    id = SellerHomeState.DrawerPosition.SELLER_TOP_ADS,
                    isExpanded = true
            ))

            if (adapter != null && adapter.isOfficialStore && powerMerchantDrawerItem != null)
                add(powerMerchantDrawerItem)
            else remove(powerMerchantDrawerItem)

            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_new_reso_seller),
                    iconId = R.drawable.sh_ic_reso,
                    id = SellerHomeState.DrawerPosition.RESOLUTION_CENTER,
                    isExpanded = true,
                    notif = getTotalResoNotif()
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_mitra_toppers),
                    iconId = R.drawable.sh_ic_mitra_toppers,
                    id = SellerHomeState.DrawerPosition.SELLER_MITRA_TOPPERS,
                    isExpanded = true
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_statistic),
                    iconId = R.drawable.sh_statistik_icon,
                    id = SellerHomeState.DrawerPosition.SELLER_GM_STAT,
                    isExpanded = true
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_setting),
                    iconId = R.drawable.sh_icon_setting,
                    id = SellerHomeState.DrawerPosition.SETTINGS,
                    isExpanded = true
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.title_activity_contact_us),
                    iconId = R.drawable.sh_ic_contactus,
                    id = SellerHomeState.DrawerPosition.CONTACT_US,
                    isExpanded = true
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_logout),
                    iconId = R.drawable.sh_ic_menu_logout,
                    id = SellerHomeState.DrawerPosition.LOGOUT,
                    isExpanded = true
            ))
        }
        context.drawer_shop.visibility = View.VISIBLE
        context.drawer_footer_shadow.visibility = View.VISIBLE
        return drawerItemData
    }

    fun initDrawer(activity: Activity) {
        sellerDrawerAdapter = SellerDrawerAdapter(SellerDrawerAdapterTypeFactory(this, this, this, this, this, context), createDrawerData(), drawerCache)
    }

    private fun getSellerMenu() : SellerDrawerItem {
        val sellerMenu = SellerDrawerGroup(
                context.getString(R.string.drawer_title_seller),
                        R.drawable.sh_icon_penjualan,
                        SellerHomeState.DrawerPosition.SHOP,
                        drawerCache.getBoolean(SellerDrawerAdapter.IS_SHOP_OPENED, false),
                        getTotalSellerNotif())

        val isExpanded = drawerCache.getBoolean(SellerDrawerAdapter.IS_INBOX_OPENED, false)

        sellerMenu.apply {
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_opportunity),
                    id = SellerHomeState.DrawerPosition.SHOP_OPPORTUNITY_LIST,
                    isExpanded = isExpanded))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_new_order),
                    id = SellerHomeState.DrawerPosition.SHOP_NEW_ORDER,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_SELLING_NEW_ORDER)))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_confirm_shipping),
                    id = SellerHomeState.DrawerPosition.SHOP_CONFIRM_SHIPPING,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION)))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_shipping_status),
                    id = SellerHomeState.DrawerPosition.SHOP_SHIPPING_STATUS,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_SELLING_SHIPPING_STATUS)))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_list_selling),
                    id = SellerHomeState.DrawerPosition.SHOP_TRANSACTION_LIST,
                    isExpanded = isExpanded))
        }

        return sellerMenu
    }

    private fun getInboxMenu(): SellerDrawerItem {

        val isExpanded = drawerCache.getBoolean(SellerDrawerAdapter.IS_INBOX_OPENED, false)

        val inboxMenu =  SellerDrawerGroup(
                context.getString(R.string.drawer_title_inbox),
                R.drawable.sh_icon_inbox,
                SellerHomeState.DrawerPosition.INBOX,
                isExpanded,
                getTotalInboxNotif())

        inboxMenu.apply {
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_inbox_message),
                    id = SellerHomeState.DrawerPosition.INBOX_TALK,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_MESSAGE)))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_inbox_discussion),
                    id = SellerHomeState.DrawerPosition.INBOX_TALK,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_TALK)))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_inbox_review),
                    id = SellerHomeState.DrawerPosition.INBOX_REVIEW,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_REVIEW)))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_inbox_ticket),
                    id = SellerHomeState.DrawerPosition.INBOX_TICKET,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_TICKET)))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_seller_info),
                    id = SellerHomeState.DrawerPosition.SELLER_INFO,
                    isExpanded = isExpanded,
                    notif = drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_SELLER_INFO)))
        }
        return inboxMenu
    }

    private fun getProductMenu(): SellerDrawerItem {
        val productMenu =  SellerDrawerGroup(
                context.getString(R.string.drawer_title_product),
                R.drawable.sh_ic_manage_produk,
                SellerHomeState.DrawerPosition.SELLER_PRODUCT_EXTEND,
                drawerCache.getBoolean(DrawerAdapter.IS_PRODUCT_OPENED, false),
                notif = 0)

        productMenu.apply {
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_add_product),
                    id = SellerHomeState.DrawerPosition.ADD_PRODUCT,
                    isExpanded = true))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_product_list),
                    id = SellerHomeState.DrawerPosition.MANAGE_PRODUCT,
                    isExpanded = true))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_draft_list),
                    id = SellerHomeState.DrawerPosition.DRAFT_PRODUCT,
                    isExpanded = true))
            add(SellerDrawerItem(
                    label = context.getString(R.string.featured_product_title),
                    id = SellerHomeState.DrawerPosition.FEATURED_PRODUCT,
                    isExpanded = true))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_etalase_list),
                    id = SellerHomeState.DrawerPosition.MANAGE_ETALASE,
                    isExpanded = true))
        }
        return productMenu
    }

    private fun getTotalSellerNotif(): Int = drawerCache.getInt(SellerDrawerNotification.CACHE_SELLING_SHIPPING_STATUS, 0) +
                    drawerCache.getInt(SellerDrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, 0) +
                    drawerCache.getInt(SellerDrawerNotification.CACHE_SELLING_NEW_ORDER, 0)

    private fun getTotalInboxNotif(): Int = drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_MESSAGE, 0)!! +
                    drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_TALK, 0)!! +
                    drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_REVIEW, 0)!! +
                    drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_TICKET, 0)!! +
                    drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_SELLER_INFO, 0)

    private fun getTotalResoNotif(): Int = drawerCache.getInt(SellerDrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, 0)

    private fun getPowerMerchantDrawerInstance(): SellerDrawerItem {
        return SellerDrawerItem(
                    label = context.getString(R.string.pm_title),
                    iconId = R.drawable.ic_pm_badge_shop_regular,
                    id = SellerHomeState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND,
                    isExpanded = true)
    }

}