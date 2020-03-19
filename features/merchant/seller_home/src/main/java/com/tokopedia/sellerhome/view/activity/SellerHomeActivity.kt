package com.tokopedia.sellerhome.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.setupStatusBarUnderMarshmallow
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.common.DeepLinkHandler
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.fragment.ContainerFragment
import com.tokopedia.sellerhome.view.model.NotificationCenterUnreadUiModel
import com.tokopedia.sellerhome.view.model.NotificationChatUiModel
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.sellerhome.view.viewmodel.SharedViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_sah_seller_home.*
import javax.inject.Inject

class SellerHomeActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)
    }

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val homeViewModel by lazy { viewModelProvider.get(SellerHomeActivityViewModel::class.java) }
    private val sharedViewModel by lazy { viewModelProvider.get(SharedViewModel::class.java) }
    private val containerFragment by lazy { ContainerFragment.newInstance() }
    private val otherSettingsFragment by lazy { OtherMenuFragment.createInstance() }
    private val fragmentManger: FragmentManager by lazy { supportFragmentManager }

    private var currentSelectedMenu = 0
    private var currentFragment: Fragment? = null
    private var hasAttachSettingsFragment = false
    private var lastSomTab = PageFragment(FragmentType.ORDER) //by default show tab "Semua Pesanan"

    private var statusBarCallback: StatusBarCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_seller_home)

        initInjector()
        setupBottomNav()
        setupDefaultFragment()
        UpdateCheckerHelper.checkAppUpdate(this)
        observeNotificationsLiveData()
        observeShopInfoLiveData()
        observeCurrentSelectedPageLiveData()
        setupStatusBar()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getNotifications()

        if (!userSession.isLoggedIn) {
            RouteManager.route(this, ApplinkConstInternalSellerapp.WELCOME)
            finish()
        } else if (!userSession.hasShop()) {
            RouteManager.route(this, ApplinkConst.CREATE_SHOP)
            finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        DeepLinkHandler.handleAppLink(intent) { page ->
            lastSomTab = page
            sharedViewModel.setCurrentSelectedPage(page)
        }
    }

    fun attachCallback(callback: StatusBarCallback) {
        statusBarCallback = callback
    }

    private fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupBottomNav() {
        sahBottomNav.itemIconTintList = null
        sahBottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        sahBottomNav.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_sah_home -> showContainerFragment(PageFragment(FragmentType.HOME), TrackingConstant.CLICK_HOME)
                R.id.menu_sah_product -> showContainerFragment(PageFragment(FragmentType.PRODUCT), TrackingConstant.CLICK_PRODUCT)
                R.id.menu_sah_chat -> showContainerFragment(PageFragment(FragmentType.CHAT), TrackingConstant.CLICK_CHAT)
                R.id.menu_sah_order -> showContainerFragment(lastSomTab, TrackingConstant.CLICK_ORDER)
                R.id.menu_sah_other -> showOtherSettingsFragment()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun showContainerFragment(page: PageFragment, trackingAction: String) {
        if (currentSelectedMenu == page.type) return
        currentSelectedMenu = page.type

        setupStatusBar()

        sharedViewModel.setCurrentSelectedPage(page)
        showFragment(containerFragment)
        lastSomTab = PageFragment(FragmentType.ORDER)

        NavigationTracking.sendClickBottomNavigationMenuEvent(trackingAction)
    }

    private fun showOtherSettingsFragment() {
        statusBarCallback?.setStatusBar()
        val type = FragmentType.OTHER
        if (currentSelectedMenu == type) return
        currentSelectedMenu = type

        if (!hasAttachSettingsFragment) {
            addFragment(otherSettingsFragment)
            hasAttachSettingsFragment = true
        }
        showFragment(otherSettingsFragment)
        sharedViewModel.setCurrentSelectedPage(PageFragment(type))

        NavigationTracking.sendClickBottomNavigationMenuEvent(TrackingConstant.CLICK_OTHERS)
    }

    private fun setupDefaultFragment() {
        addFragment(containerFragment)
        currentFragment = containerFragment
        showFragment(containerFragment)
    }

    private fun <T : Fragment> addFragment(fragment: T) {
        fragmentManger.beginTransaction()
                .add(R.id.sahContainer, fragment, fragment.tag)
                .hide(fragment)
                .commit()
    }

    private fun showFragment(fragment: Fragment) {
        currentFragment?.let {
            fragmentManger.beginTransaction()
                    .hide(it)
                    .show(fragment)
                    .commit()
            currentFragment = fragment
        }
    }

    private fun observeCurrentSelectedPageLiveData() {
        sharedViewModel.currentSelectedPage.observe(this, Observer {
            sahBottomNav.currentItem = it.type
        })
    }

    private fun observeNotificationsLiveData() {
        homeViewModel.notifications.observe(this, Observer {
            if (it is Success) {
                showNotificationBadge(it.data.notifCenterUnread)
                showChatNotificationCounter(it.data.chat)
                showOrderNotificationCounter(it.data.sellerOrderStatus)
            }
        })
    }

    private fun observeShopInfoLiveData() {
        homeViewModel.shopInfo.observe(this, Observer {
            if (it is Success) {
                containerFragment.showShopName(it.data.shopName)
            }
        })
        homeViewModel.getShopInfo()
    }

    private fun showNotificationBadge(notifCenter: NotificationCenterUnreadUiModel) {
        containerFragment.showNotifCenterBadge(notifCenter)
    }

    private fun showChatNotificationCounter(chat: NotificationChatUiModel) {
        sahBottomNav.setNotification(chat.unreadsSeller, FragmentType.CHAT)
    }

    private fun showOrderNotificationCounter(orderStatus: NotificationSellerOrderStatusUiModel) {
        val notificationCount = orderStatus.newOrder.plus(orderStatus.readyToShip)
        sahBottomNav.setNotification(notificationCount, FragmentType.ORDER)
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestStatusBarDark()
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                sahContainer.requestApplyInsets()
            }
            this.setupStatusBarUnderMarshmallow()
        }
    }
}