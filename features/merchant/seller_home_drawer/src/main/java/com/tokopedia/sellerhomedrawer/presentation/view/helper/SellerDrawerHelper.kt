package com.tokopedia.sellerhomedrawer.presentation.view.helper

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.TaskStackBuilder
import androidx.core.view.GravityCompat
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.core.ManageGeneral
import com.tokopedia.core.drawer2.view.DrawerAdapter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.analytics.*
import com.tokopedia.sellerhomedrawer.constant.SellerBaseUrl
import com.tokopedia.sellerhomedrawer.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerProfile
import com.tokopedia.sellerhomedrawer.presentation.listener.*
import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapter
import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapterTypeFactory
import com.tokopedia.sellerhomedrawer.presentation.view.dashboard.SellerDashboardActivity
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerGroup
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.sellerheader.SellerDrawerHeader
import com.tokopedia.sellerhomedrawer.presentation.view.webview.SellerHomeWebViewActivity
import com.tokopedia.sellerhomedrawer.presentation.view.webview.SellerSimpleWebViewActivity
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.sh_drawer_activity.*
import kotlinx.android.synthetic.main.sh_drawer_layout.*
import kotlinx.android.synthetic.main.sh_drawer_shop.*

class SellerDrawerHelper(val context: Activity,
                         val userSession: UserSession,
                         val drawerCache: LocalCacheHandler,
                         val remoteConfig: FirebaseRemoteConfigImpl) :
        SellerDrawerItemListener, SellerDrawerHeaderListener, SellerDrawerGroupListener ,
        DrawerHeaderListener, RetryTokoCashListener {

    companion object {
        @JvmStatic
        val DIGITAL_WEBSITE_DOMAIN = TokopediaUrl.getInstance().PULSA
        @JvmStatic
        val DIGITAL_PATH_MITRA = "mitra"
        @JvmStatic
        val CONTACT_US = "Contact_Us"
        @JvmStatic
        val DRAWER_CACHE = "DRAWER_CACHE"
        @JvmStatic
        val REQUEST_LOGIN = 345
    }

    var sellerDrawerAdapter: SellerDrawerAdapter? = null
    var powerMerchantInstance: SellerDrawerItem? = null

    var selectedPosition = -1

    override fun onItemClicked(drawerItem: SellerDrawerItem) {
        if (drawerItem.id == selectedPosition) closeDrawer()
        else {
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
                    moveActivityInternalApplink(ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                }
                SellerHomeState.DrawerPosition.SHOP_NEW_ORDER -> {
                    eventDrawerClick(EventLabel.NEW_ORDER)
                    moveActivityInternalApplink(
                            ApplinkConstInternalMarketplace.SELLING_TRANSACTION,
                            SellerHomeState.SellingTransaction.TAB_POSITION_SELLING_NEW_ORDER)
                }
                SellerHomeState.DrawerPosition.SHOP_CONFIRM_SHIPPING -> {
                    eventDrawerClick(EventLabel.DELIVERY_CONFIRMATION)
                    moveActivityInternalApplink(
                            ApplinkConstInternalMarketplace.SELLING_TRANSACTION,
                            SellerHomeState.SellingTransaction.TAB_POSITION_SELLING_CONFIRM_SHIPPING)
                }
                SellerHomeState.DrawerPosition.SHOP_SHIPPING_STATUS -> {
                    eventDrawerClick(EventLabel.DELIVERY_CONFIRMATION)
                    moveActivityInternalApplink(
                            ApplinkConstInternalMarketplace.SELLING_TRANSACTION,
                            SellerHomeState.SellingTransaction.TAB_POSITION_SELLING_SHIPPING_STATUS)
                }
                SellerHomeState.DrawerPosition.SHOP_TRANSACTION_LIST -> {
                    eventDrawerClick(EventLabel.SALES_LIST)
                    moveActivityInternalApplink(
                            ApplinkConstInternalMarketplace.SELLING_TRANSACTION,
                            SellerHomeState.SellingTransaction.TAB_POSITION_SELLING_TRANSACTION_LIST)
                }
                SellerHomeState.DrawerPosition.SHOP_OPPORTUNITY_LIST -> {
                    moveActivityInternalApplink(
                            ApplinkConstInternalMarketplace.SELLING_TRANSACTION,
                            SellerHomeState.SellingTransaction.TAB_POSITION_SELLING_OPPORTUNITY)
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
                    RouteManager.route(context, ApplinkConstInternalMarketplace.GOLD_MERCHANT_STATISTIC_DASHBOARD)
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
                SellerHomeState.DrawerPosition.SETTINGS -> {
                    val settingsIntent = Intent(context, ManageGeneral::class.java)
                    val settingsCanonicalName = ManageGeneral::class.java.canonicalName
                    eventDrawerClick(EventLabel.SETTING)
                    if (settingsCanonicalName != null)
                        SellerAnalyticsEventTrackingHelper.hamburgerOptionClicked(context, settingsCanonicalName, EventLabel.SETTING)
                    context.startActivity(settingsIntent)
                }
                SellerHomeState.DrawerPosition.CONTACT_US -> {
                    val contactUsIntent = RouteManager.getIntent(context, ApplinkConst.CONTACT_US_NATIVE)
                    val contactUsClassName = contactUsIntent.component?.className
                    if (contactUsClassName != null)
                        SellerAnalyticsEventTrackingHelper.hamburgerOptionClicked(context, contactUsClassName, CONTACT_US)
                    context.startActivity(contactUsIntent)
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
        sellerDrawerAdapter?.notifyDataSetChanged()
    }

    override fun onGoToDeposit() {
        //TODO: SellerModuleRouter is needed ?
        startSaldoDepositIntent()
        eventDrawerClick(EventLabel.SHOP_EN)
    }

    override fun onGoToProfile() {
        //TODO: Add internal applink to ProfileActivity
    }

    override fun onGoToProfileCompletion() {
        //TODO: Add internal applink to ProfileCompletionActivity
    }

    override fun onGroupClicked(sellerDrawerGroup: SellerDrawerGroup, position: Int) {

        val list = sellerDrawerGroup.list

        if (sellerDrawerGroup.isExpanded)
            sellerDrawerAdapter?.removeAllElements(list)
        else {
            sellerDrawerAdapter?.addAllElements(position + 1, list)
        }

        list.forEach{item ->
            item.isExpanded = !sellerDrawerGroup.isExpanded
        }
        sellerDrawerGroup.isExpanded = sellerDrawerGroup.isExpanded.not()

        setExpandCache(sellerDrawerGroup, sellerDrawerGroup.isExpanded)

        notifyDataSetChanged()

    }

    private fun SellerDrawerAdapter.addAllElements(position: Int, elementList: List<SellerDrawerItem>) {
        var incrementalPosition = position
        elementList.forEach{
            this.addElement(position, it)
            incrementalPosition++
        }
    }

    private fun SellerDrawerAdapter.removeAllElements(elementList: List<SellerDrawerItem>) {
        elementList.forEach{
            this.removeElement(it)
        }
    }

    private fun setExpandCache(group: SellerDrawerGroup, isExpand: Boolean) {
        when(group.id) {
            SellerHomeState.DrawerPosition.INBOX ->
                drawerCache.putBoolean(SellerDrawerAdapter.IS_INBOX_OPENED, isExpand)
            SellerHomeState.DrawerPosition.PEOPLE ->
                drawerCache.putBoolean(SellerDrawerAdapter.IS_PEOPLE_OPENED, isExpand)
            SellerHomeState.DrawerPosition.SHOP ->
                drawerCache.putBoolean(SellerDrawerAdapter.IS_PRODUCT_OPENED, isExpand)
            SellerHomeState.DrawerPosition.SELLER_PRODUCT_EXTEND ->
                drawerCache.putBoolean(SellerDrawerAdapter.IS_PRODUCT_OPENED, isExpand)
            SellerHomeState.DrawerPosition.SELLER_GM_SUBSCRIBE ->
                drawerCache.putBoolean(SellerDrawerAdapter.IS_GM_OPENED, isExpand)
            SellerHomeState.DrawerPosition.RESOLUTION_CENTER ->
                drawerCache.putBoolean(SellerDrawerAdapter.IS_RESO_OPENED, isExpand)
        }
        drawerCache.applyEditor()
    }


    override fun onGoToTopPoints(topPointsUrl: String?) {

    }

    override fun onWalletBalanceClicked(redirectUrlBalance: String?, appLinkBalance: String?) {

    }

    override fun onWalletActionButtonClicked(redirectUrlActionButton: String?, appLinkActionButton: String?) {

    }

    override fun onTokoPointActionClicked(mainPageUrl: String?, title: String?) {

    }

    override fun onGotoTokoCard() {

    }

    override fun onRetryTokoCash() {
        //TODO: Looks like this method is not called anymore
    }

    fun createDrawerData(): MutableList<SellerDrawerItem> {
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

            if (adapter != null && !adapter.isOfficialStore && powerMerchantDrawerItem != null)
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
        val visitableList = mutableListOf<Visitable<*>>().apply {
            add(SellerDrawerHeader())
            addAll(createDrawerData())
        }

        sellerDrawerAdapter = SellerDrawerAdapter(SellerDrawerAdapterTypeFactory(this, this, this, this, this, context), visitableList, drawerCache)
        sellerDrawerAdapter?.drawerItemData = createDrawerData()
        activity.left_drawer.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = sellerDrawerAdapter
        }
        setExpand()
        closeDrawer()
    }

    fun setFooterData(profile: SellerDrawerProfile) {
        context.shop_label.text = profile.shopName
        context.shop_icon.visibility = View.VISIBLE
        context.shop_sublabel.visibility = View.VISIBLE
        ImageHandler.LoadImage(context.shop_icon, profile.shopAvatar)
        context.drawer_shop.setOnClickListener {
            onGoToShop()
        }
    }

    private fun onGoToShop() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_PAGE_DOMAIN, userSession.shopId)
    }

    private fun setExpand() {
        checkExpand(SellerDrawerAdapter.IS_INBOX_OPENED, SellerHomeState.DrawerPosition.INBOX)
        checkExpand(SellerDrawerAdapter.IS_PEOPLE_OPENED, SellerHomeState.DrawerPosition.PEOPLE)
        checkExpand(SellerDrawerAdapter.IS_SHOP_OPENED, SellerHomeState.DrawerPosition.SHOP)
        checkExpand(SellerDrawerAdapter.IS_PRODUCT_DIGITAL_OPENED, SellerHomeState.DrawerPosition.SELLER_PRODUCT_DIGITAL_EXTEND)
        checkExpand(SellerDrawerAdapter.IS_PRODUCT_OPENED, SellerHomeState.DrawerPosition.SELLER_PRODUCT_EXTEND)
        checkExpand(SellerDrawerAdapter.IS_GM_OPENED, SellerHomeState.DrawerPosition.SELLER_GM_SUBSCRIBE)
        notifyDataSetChanged()
    }

    private fun checkExpand(key: String, idPos: Int) {
        if (drawerCache.getBoolean(key, false)) {
            val sellerDrawerGroup = findGroup(idPos)
            if (sellerDrawerGroup != null) {
                val drawerPosition = sellerDrawerGroup.position?.plus(1)
                if (drawerPosition != null)
                    sellerDrawerAdapter?.addAllElements(drawerPosition, sellerDrawerGroup.list)
            }
        }
    }

    private fun findGroup(id: Int): SellerDrawerGroup? {
        sellerDrawerAdapter?.list?.forEachIndexed { index, drawerItem ->
            if (drawerItem is SellerDrawerGroup && drawerItem.id == id) {
                drawerItem.position = index
                return drawerItem
            }
        }
        return null
    }

    fun isOpened() = context.drawer_layout_nav.isDrawerOpen(GravityCompat.START)

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

    private fun moveActivityInternalApplink(applink: String) {
        RouteManager.route(context, applink)
    }

    private fun moveActivityInternalApplink(applink: String, vararg params: String) {
        val intent = RouteManager.getIntent(context, applink, *params)
        context.startActivity(intent)
    }

    private fun startSaldoDepositIntent() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            moveActivityInternalApplink(ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else context.startActivity(SellerHomeWebViewActivity.createIntent(context, ApplinkConst.WebViewUrl.SALDO_DETAIL))
    }

}