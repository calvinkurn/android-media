package com.tokopedia.sellerhomedrawer.presentation.view.helper

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.TaskStackBuilder
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.config.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.analytics.*
import com.tokopedia.sellerhomedrawer.data.constant.SellerBaseUrl
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeState.SellingTransaction.EXTRA_STATE_TAB_POSITION
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerProfile
import com.tokopedia.sellerhomedrawer.presentation.listener.*
import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapter
import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapterTypeFactory
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerGroup
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.sellerheader.SellerDrawerHeader
import com.tokopedia.track.TrackApp
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.sh_drawer_layout.*
import javax.inject.Inject

class SellerDrawerHelper @Inject constructor(val context: Activity,
                                             override val userSession: UserSessionInterface,
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
        const val APP_LINK_EXTRA_SHOP_ID = "shop_id"
        const val INBOX_TICKET_ACTIVITY = "com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity"
        const val INBOX_REPUTATION_ACTIVITY = "com.tokopedia.tkpd.tkpdreputation.inbox.view.activity"
        const val REPUTATION_DEEPLINK = "tokopedia://review"
        const val URL_KEY = "url"
        const val TAB_POSITION_SELLING_OPPORTUNITY = 1
        const val TAB_POSITION_SELLING_NEW_ORDER = 2
        const val TAB_POSITION_SELLING_CONFIRM_SHIPPING = 3
        const val TAB_POSITION_SELLING_SHIPPING_STATUS = 4
        const val TAB_POSITION_SELLING_TRANSACTION_LIST = 5

    }

    val sellerDrawerHeader = SellerDrawerHeader()
    var sellerDrawerAdapter: SellerDrawerAdapter? = null
    var powerMerchantInstance: SellerDrawerItem? = null

    var selectedPosition = -1

    private val logoutIconDrawable by lazy {
        if (GlobalConfig.isSellerApp()) R.drawable.sah_qc_launcher2
        else R.drawable.sah_qc_launcher
    }

    override fun onItemClicked(drawerItem: SellerDrawerItem) {
        if (drawerItem.id == selectedPosition) closeDrawer()
        else {
            var isNeedToCloseActivity = true
            when(drawerItem.id) {
                SellerHomeState.DrawerPosition.INDEX_HOME -> {
                    eventDrawerClick(EventLabel.SELLER_HOME)
                    moveActivityApplink(ApplinkConstInternalSellerapp.SELLER_HOME)
                }
                SellerHomeState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND -> {
                    if (context.application is AbstractionRouter)
                        sendClickHamburgerMenuEvent(drawerItem.label)
                    eventClickGoldMerchantViaDrawer()
                    moveActivityApplink(ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE)
                }
                SellerHomeState.DrawerPosition.SHOP_NEW_ORDER -> {
                    eventDrawerClick(EventLabel.NEW_ORDER)
                    moveActivityApplinkSellingTransaction(ApplinkConst.SELLER_NEW_ORDER, TAB_POSITION_SELLING_NEW_ORDER)
                }
                SellerHomeState.DrawerPosition.SHOP_CONFIRM_SHIPPING -> {
                    eventDrawerClick(EventLabel.DELIVERY_CONFIRMATION)
                    moveActivityApplinkSellingTransaction(ApplinkConst.SELLER_SHIPMENT, TAB_POSITION_SELLING_CONFIRM_SHIPPING)
                }
                SellerHomeState.DrawerPosition.SHOP_SHIPPING_STATUS -> {
                    eventDrawerClick(EventLabel.DELIVERY_CONFIRMATION)
                    moveActivityApplinkSellingTransaction(ApplinkConst.SELLER_STATUS, TAB_POSITION_SELLING_SHIPPING_STATUS)
                }
                SellerHomeState.DrawerPosition.SHOP_TRANSACTION_LIST -> {
                    eventDrawerClick(EventLabel.SALES_LIST)
                    moveActivityApplinkSellingTransaction(ApplinkConst.SELLER_HISTORY, TAB_POSITION_SELLING_TRANSACTION_LIST)
                }
                SellerHomeState.DrawerPosition.SHOP_OPPORTUNITY_LIST -> {
                    moveActivityApplinkSellingTransaction(ApplinkConst.SELLER_OPPORTUNITY, TAB_POSITION_SELLING_OPPORTUNITY)
                }
                SellerHomeState.DrawerPosition.SELLER_INFO -> {
                    eventSellerInfo(
                            Action.CLICK_HAMBURGER_ICON,
                            EventLabel.SELLER_INFO
                    )
                    moveActivityApplink(ApplinkConst.SELLER_INFO)
                }
                SellerHomeState.DrawerPosition.INBOX_TICKET -> {
                    moveActivityClassNameAndSendTrackingInbox(INBOX_TICKET_ACTIVITY, EventLabel.HELP)
                }
                SellerHomeState.DrawerPosition.INBOX_REVIEW -> {
                    moveActivityDeeplinkAndSendTrackingInbox(REPUTATION_DEEPLINK, EventLabel.REVIEW)
                }
                SellerHomeState.DrawerPosition.INBOX_TALK -> {
                    moveActivityDeeplinkAndSendTrackingInbox(ApplinkConst.TALK, EventLabel.PRODUCT_DISCUSSION)
                }
                SellerHomeState.DrawerPosition.INBOX_MESSAGE -> {
                    moveActivityDeeplinkAndSendTrackingInbox(ApplinkConst.TOPCHAT_IDLESS, EventLabel.MESSAGE)
                    TrackApp.getInstance().gtm.sendGeneralEvent("clickNavigationDrawer",
                            "left navigation",
                            "click on groupchat",
                            "")
                }
                SellerHomeState.DrawerPosition.ADD_PRODUCT -> {
                    val manageProductIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
                    val addProductIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM)
                    TaskStackBuilder.create(context)
                            .addNextIntent(manageProductIntent)
                            .addNextIntent(addProductIntent)
                            .startActivities()
                }
                SellerHomeState.DrawerPosition.MANAGE_PRODUCT -> {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
                }
                SellerHomeState.DrawerPosition.MANAGE_PAYMENT_AND_TOPUP -> {
                    eventClickPaymentAndTopupOnDrawer()
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW,  DIGITAL_WEBSITE_DOMAIN + DIGITAL_PATH_MITRA)
                }
                SellerHomeState.DrawerPosition.MANAGE_TRANSACTION_DIGITAL -> {
                    eventClickDigitalTransactionListOnDrawer()
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, DIGITAL_WEBSITE_DOMAIN + DIGITAL_PATH_MITRA)
                }
                SellerHomeState.DrawerPosition.DRAFT_PRODUCT -> {
                    eventDrawerClick(EventLabel.DRAFT_PRODUCT)
                    RouteManager.route(context, ApplinkConst.PRODUCT_DRAFT)
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
                    RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_DASHBOARD)
                }
                SellerHomeState.DrawerPosition.SELLER_FLASH_SALE -> {
                    RouteManager.route(context, ApplinkConst.SellerApp.FLASHSALE_MANAGEMENT)
                }
                SellerHomeState.DrawerPosition.FEATURED_PRODUCT -> {
                    eventFeaturedProduct(EventLabel.FEATURED_PRODUCT)
                    RouteManager.route(context, ApplinkConstInternalMarketplace.GOLD_MERCHANT_FEATURED_PRODUCT)
                }
                SellerHomeState.DrawerPosition.RESOLUTION_CENTER -> {
                    eventDrawerClick(EventLabel.RESOLUTION_CENTER)
                    val intent = RouteManager.getIntent(context, ApplinkConst.SellerApp.WEBVIEW)
                    intent.putExtra(URL_KEY, SellerBaseUrl.HOSTNAME + SellerBaseUrl.RESO_INBOX_SELLER)
                    context.startActivity(intent)
                }
                SellerHomeState.DrawerPosition.SETTINGS -> {
                    val manageGeneralIntent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.MANAGE_GENERAL)
                    val settingsCanonicalName = manageGeneralIntent.component?.className
                    eventDrawerClick(EventLabel.SETTING)
                    if (settingsCanonicalName != null)
                        SellerAnalyticsEventTrackingHelper.hamburgerOptionClicked(context, settingsCanonicalName, EventLabel.SETTING)
                    context.startActivity(manageGeneralIntent)
                }
                SellerHomeState.DrawerPosition.CONTACT_US -> {
                    val contactUsIntent = RouteManager.getIntent(context, ApplinkConst.CONTACT_US_NATIVE)
                    val contactUsClassName = contactUsIntent.component?.className
                    if (contactUsClassName != null)
                        SellerAnalyticsEventTrackingHelper.hamburgerOptionClicked(context, contactUsClassName, CONTACT_US)
                    context.startActivity(contactUsIntent)
                }
                SellerHomeState.DrawerPosition.LOGOUT -> {
                    logout()
                    eventDrawerClick(EventLabel.SIGN_OUT)
                    SellerAnalyticsEventTrackingHelper.hamburgerOptionClicked(context, "Home", "Logout")
                }
                else -> {

                }
            }

            if (selectedPosition != SellerHomeState.DrawerPosition.INDEX_HOME && drawerItem.id != SellerHomeState.DrawerPosition.LOGOUT && isNeedToCloseActivity)
                context.finish()

            closeDrawer()
        }
    }

    fun closeDrawer() { context.findViewById<DrawerLayout>(R.id.drawer_layout_nav).closeDrawer(GravityCompat.START) }

    fun openDrawer() { context.findViewById<DrawerLayout>(R.id.drawer_layout_nav).openDrawer(GravityCompat.START) }

    override fun notifyDataSetChanged() {
        sellerDrawerAdapter?.notifyDataSetChanged()
    }

    override fun onGoToDeposit() {

    }

    override fun onGoToProfile() {

    }

    override fun onGoToProfileCompletion() {

    }

    override fun onGoToDepositHeader() {
        startSaldoDepositIntent()
        eventDrawerClick(EventLabel.SHOP_EN)
    }

    override fun onGoToProfileHeader() {
        moveActivityApplink(ApplinkConstInternalContent.PROFILE_DETAIL, userSession.userId)
    }

    override fun onGoToProfileCompletionHeader() {
        moveActivityApplink(ApplinkConstInternalGlobal.PROFILE_COMPLETION)
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
            this.addElement(incrementalPosition, it)
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
                drawerCache.putBoolean(SellerDrawerAdapter.IS_SHOP_OPENED, isExpand)
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
        
        powerMerchantInstance = getPowerMerchantDrawerInstance()
        
        val adapter = sellerDrawerAdapter

        drawerItemData.apply {
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_home),
                    iconId = R.drawable.sh_icon_home,
                    id = SellerHomeState.DrawerPosition.SELLER_INDEX_HOME,
                    isExpanded = true,
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.SELLER_INDEX_HOME))
            add(getSellerMenu())
            add(getInboxMenu())
            add(getProductMenu())
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_top_ads),
                    iconId = R.drawable.sh_ic_top_ads,
                    id = SellerHomeState.DrawerPosition.SELLER_TOP_ADS,
                    isExpanded = true,
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.SELLER_TOP_ADS
            ))

            val powerMerchantDrawerItem = powerMerchantInstance
            if (adapter != null && !adapter.isOfficialStore && powerMerchantDrawerItem != null) {
                add(powerMerchantDrawerItem)
            }
            else remove(powerMerchantDrawerItem)

            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_new_reso_seller),
                    iconId = R.drawable.sh_ic_reso,
                    id = SellerHomeState.DrawerPosition.RESOLUTION_CENTER,
                    isExpanded = true,
                    notif = getTotalResoNotif(),
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.RESOLUTION_CENTER
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_mitra_toppers),
                    iconId = R.drawable.sh_ic_mitra_toppers,
                    id = SellerHomeState.DrawerPosition.SELLER_MITRA_TOPPERS,
                    isExpanded = true,
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.SELLER_MITRA_TOPPERS
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_statistic),
                    iconId = R.drawable.sh_statistik_icon,
                    id = SellerHomeState.DrawerPosition.SELLER_GM_STAT,
                    isExpanded = true,
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.SELLER_GM_STAT
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_setting),
                    iconId = R.drawable.sh_icon_setting,
                    id = SellerHomeState.DrawerPosition.SETTINGS,
                    isExpanded = true,
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.SETTINGS
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.title_activity_contact_us),
                    iconId = R.drawable.sh_ic_contactus,
                    id = SellerHomeState.DrawerPosition.CONTACT_US,
                    isExpanded = true,
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.CONTACT_US
            ))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_logout),
                    iconId = R.drawable.sh_ic_menu_logout,
                    id = SellerHomeState.DrawerPosition.LOGOUT,
                    isExpanded = true,
                    isSelected = selectedPosition == SellerHomeState.DrawerPosition.LOGOUT
            ))
        }
        context.findViewById<FrameLayout>(R.id.drawer_shop).visibility = View.VISIBLE
        context.findViewById<TextView>(R.id.drawer_footer_shadow).visibility = View.VISIBLE
        return drawerItemData
    }

    fun initDrawer(activity: Activity) {
        val visitableList = mutableListOf<Visitable<*>>().apply {
            add(sellerDrawerHeader)
            addAll(createDrawerData())
        }

        sellerDrawerAdapter = SellerDrawerAdapter(context, SellerDrawerAdapterTypeFactory(this, this, this, this, this, context), visitableList, drawerCache)
        sellerDrawerAdapter?.drawerItemData = createDrawerData()
        activity.left_drawer.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = sellerDrawerAdapter
        }
        setExpand()
        closeDrawer()
    }

    fun setFooterData(profile: SellerDrawerProfile) {
        context.findViewById<TextView>(R.id.shop_label).text = profile.shopName
        context.findViewById<ImageView>(R.id.shop_icon).visibility = View.VISIBLE
        context.findViewById<TextView>(R.id.shop_sublabel).visibility = View.VISIBLE
        ImageHandler.LoadImage(context.findViewById(R.id.shop_icon), profile.shopAvatar)
        context.findViewById<FrameLayout>(R.id.drawer_shop).setOnClickListener {
            onGoToShop()
        }
    }

    private fun onGoToShop() {
        RouteManager.route(context, ApplinkConst.SHOP, userSession.shopId)
    }

    fun setExpand() {
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

    fun isOpened() = context.findViewById<DrawerLayout>(R.id.drawer_layout_nav).isDrawerOpen(GravityCompat.START)

    private fun getSellerMenu() : SellerDrawerItem {
        val isExpanded = drawerCache.getBoolean(SellerDrawerAdapter.IS_SHOP_OPENED, false)
        val sellerMenu = SellerDrawerGroup(
                context.getString(R.string.drawer_title_seller),
                        R.drawable.sh_icon_penjualan,
                        SellerHomeState.DrawerPosition.SHOP,
                        isExpanded,
                        getTotalSellerNotif())

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
                    id = SellerHomeState.DrawerPosition.INBOX_MESSAGE,
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
        val isExpanded = drawerCache.getBoolean(SellerDrawerAdapter.IS_PRODUCT_OPENED, false)
        val productMenu =  SellerDrawerGroup(
                context.getString(R.string.drawer_title_product),
                R.drawable.sh_ic_manage_produk,
                SellerHomeState.DrawerPosition.SELLER_PRODUCT_EXTEND,
                isExpanded,
                notif = 0)

        productMenu.apply {
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_add_product),
                    id = SellerHomeState.DrawerPosition.ADD_PRODUCT,
                    isExpanded = isExpanded))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_product_list),
                    id = SellerHomeState.DrawerPosition.MANAGE_PRODUCT,
                    isExpanded = isExpanded))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_draft_list),
                    id = SellerHomeState.DrawerPosition.DRAFT_PRODUCT,
                    isExpanded = isExpanded))
            add(SellerDrawerItem(
                    label = context.getString(R.string.featured_product_title),
                    id = SellerHomeState.DrawerPosition.FEATURED_PRODUCT,
                    isExpanded = isExpanded))
            add(SellerDrawerItem(
                    label = context.getString(R.string.drawer_title_etalase_list),
                    id = SellerHomeState.DrawerPosition.MANAGE_ETALASE,
                    isExpanded = isExpanded))
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

    private fun getPowerMerchantDrawerInstance(): SellerDrawerItem? {
        if (powerMerchantInstance == null)
            powerMerchantInstance =  SellerDrawerItem(
                    label = context.getString(R.string.pm_title),
                    iconId = R.drawable.sh_ic_pm_badge_shop_regular,
                    id = SellerHomeState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND,
                    isExpanded = true)
        return powerMerchantInstance
    }

    private fun moveActivityApplink(applink: String) {
        RouteManager.route(context, applink)
    }

    private fun moveActivityApplinkSellingTransaction(applink: String, tabPosition: Int) {
        val intent = RouteManager.getIntent(context, applink)
        intent.putExtra(EXTRA_STATE_TAB_POSITION, tabPosition)
        context.startActivity(intent)
    }

    private fun moveActivityApplink(applink: String, vararg params: String) {
        val intent = RouteManager.getIntent(context, applink, *params)
        context.startActivity(intent)
    }

    private fun moveActivityDeeplinkAndSendTrackingInbox(deeplink: String, subCategory: String) {
        val intent = RouteManager.getIntent(context, deeplink)
        context.startActivity(intent)
        eventDrawerClick(subCategory)
        intent.component?.className?.let { className ->
            SellerAnalyticsEventTrackingHelper.hamburgerOptionClicked(context, className, EventLabel.INBOX, subCategory)
        }
    }

    private fun moveActivityClassNameAndSendTrackingInbox(activityPath: String, subCategory: String) {
        val intent = Intent()
        intent.setClassName(context.packageName, activityPath)
        context.startActivity(intent)
        eventDrawerClick(subCategory)
        intent.component?.className?.let { className ->
            SellerAnalyticsEventTrackingHelper.hamburgerOptionClicked(context, className, EventLabel.INBOX, subCategory)
        }
    }

    private fun startSaldoDepositIntent() {
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_ENABLE_SALDO_SPLIT_FOR_SELLER_APP, false))
            moveActivityApplink(ApplinkConstInternalGlobal.SALDO_DEPOSIT)
        else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, ApplinkConst.WebViewUrl.SALDO_DETAIL)
            context.startActivity(intent)
        }
    }

    private fun logout() {
        if (GlobalConfig.isSellerApp()) {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.apply {
            setIcon(logoutIconDrawable)
            setTitle(context.getString(R.string.seller_home_logout_title))
            setMessage(context.getString(R.string.seller_home_logout_confirm))
            setPositiveButton(context.getString(R.string.seller_home_logout_button)) { dialogInterface, _ ->
                showProgressDialog()
                dialogInterface.dismiss()
                moveActivityApplink(ApplinkConstInternalGlobal.LOGOUT)
            }
            setNegativeButton(context.getString(R.string.seller_home_cancel)) {
                dialogInterface, _ -> dialogInterface.dismiss()
            }
            show()
        }
    }

    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(context)
        progressDialog.apply {
            setMessage(context.resources.getString(R.string.seller_home_loading))
            setTitle("")
            setCancelable(false)
            show()
        }
    }



}