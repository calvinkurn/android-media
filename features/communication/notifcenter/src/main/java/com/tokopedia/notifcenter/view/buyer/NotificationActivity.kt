package com.tokopedia.notifcenter.view.buyer

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentFactory
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst.Inbox.*
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationNavAnalytic
import com.tokopedia.notifcenter.di.NotificationActivityComponentFactory
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.view.NotificationFragment
import com.tokopedia.notifcenter.view.NotificationViewModel
import com.tokopedia.notifcenter.view.buyer.bottomsheet.NotifCenterAccountSwitcherBottomSheet
import com.tokopedia.notifcenter.view.buyer.customview.NotifCenterNavigationHeader
import com.tokopedia.notifcenter.view.listener.NotificationFragmentContainer
import com.tokopedia.notifcenter.util.NotifCenterConfig
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheState
import com.tokopedia.notifcenter.util.getRoleName
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

/**
 * How to go to this page
 * Applink: [com.tokopedia.applink.ApplinkConst.INBOX]
 *
 * This page accept 4 optional query parameters:
 * - [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_PAGE]
 * - [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_ROLE]
 * - [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_SOURCE]
 * - [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_SHOW_BOTTOM_NAV]
 * the value you can use are as follows
 *
 * param [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_PAGE]:
 * - [com.tokopedia.applink.ApplinkConst.Inbox.VALUE_PAGE_NOTIFICATION]
 * - [com.tokopedia.applink.ApplinkConst.Inbox.VALUE_PAGE_CHAT]
 * - [com.tokopedia.applink.ApplinkConst.Inbox.VALUE_PAGE_TALK]
 * - [com.tokopedia.applink.ApplinkConst.Inbox.VALUE_PAGE_REVIEW]
 *
 * param [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_ROLE]:
 * - [com.tokopedia.applink.ApplinkConst.Inbox.VALUE_ROLE_BUYER]
 * - [com.tokopedia.applink.ApplinkConst.Inbox.VALUE_ROLE_SELLER]
 *
 * param [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_SOURCE]:
 * - you can put any value to this param
 *
 * param [com.tokopedia.applink.ApplinkConst.Inbox.PARAM_SHOW_BOTTOM_NAV]:
 * - boolean value true/false, the default value is true
 *
 * If the query parameters is not provided it will use recent/last opened page & role
 *
 * example form of applinks:
 * - tokopedia://inbox
 * - tokopedia://inbox?page=notification&role=buyer&show_bottom_nav=true
 * - tokopedia://inbox?page=notification&role=buyer
 * - tokopedia://inbox?page=notification
 * - tokopedia://inbox?role=buyer
 * - tokopedia://inbox?source=uoh
 *
 * How to construct the applink with query parameters:
 * ```
 * val applinkUri = Uri.parse(ApplinkConstInternalMarketplace.INBOX).buildUpon().apply {
 *      appendQueryParameter(
 *          ApplinkConst.Inbox.PARAM_PAGE,
 *          ApplinkConst.Inbox.VALUE_PAGE_CHAT
 *      )
 * }
 * ```
 *
 * note: Do not hardcode applink.
 * use variables provided in [com.tokopedia.applink.ApplinkConst]
 */
open class NotificationActivity :
    BaseActivity(),
    HasComponent<NotificationComponent>,
    NotifCenterConfig.ConfigListener,
    NotificationFragmentContainer {

    private var source = ""

    private var notificationComponent: NotificationComponent? = null

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var navHeader: NotifCenterNavigationHeader

    @Inject
    lateinit var cacheState: NotifCenterCacheState

    @Inject
    lateinit var viewModel: NotificationViewModel

    @Inject
    lateinit var analytic: NotificationNavAnalytic

    private var switcher: NotifCenterAccountSwitcherBottomSheet? = null
    private var navHeaderContainer: ConstraintLayout? = null
    private var container: ConstraintLayout? = null
    private var fragmentContainer: FrameLayout? = null
    private var toolbar: NavToolbar? = null

    override val role: Int get() = NotifCenterConfig.role

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        setupOptionalParameter()
        trackOpenInbox()
        setupView()
        setupConfig()
        setupBackground()
        setupNotificationBar()
        setupObserver()
        setupToolbar()
        setupInitialPage()
        updateToolbarIcon()
        setupSwitcher()
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    override fun onResume() {
        super.onResume()
        analytic.trackOpenInboxPage(NotifCenterConfig.role)
    }

    private fun initializeNotificationComponent(): NotificationComponent {
        return NotificationActivityComponentFactory
            .instance
            .createNotificationComponent(application).also {
                notificationComponent = it
            }
    }

    override fun getComponent(): NotificationComponent {
        return notificationComponent ?: initializeNotificationComponent()
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setupOptionalParameter() {
        val data = intent?.data ?: return
        setupInitialRoleFromParams(data)
        setupPageSource(data)
    }

    private fun setupInitialRoleFromParams(data: Uri) {
        val roleInt = when (data.getQueryParameter(PARAM_ROLE)) {
            VALUE_ROLE_BUYER -> RoleType.BUYER
            VALUE_ROLE_SELLER -> RoleType.SELLER
            else -> null
        }
        if (roleInt != null) {
            NotifCenterConfig.setRole(roleInt)
        } else {
            NotifCenterConfig.setRole(cacheState.role)
        }
    }

    private fun setupPageSource(data: Uri) {
        val source = data.getQueryParameter(PARAM_SOURCE)
        source?.let {
            this.source = it
        }
    }

    private fun trackOpenInbox() {
        analytic.trackOpenInbox(NotifCenterConfig.role)
    }

    override fun clearNotificationCounter() {
        val notificationRole = NotifCenterConfig.inboxCounter.getByRole(
            NotifCenterConfig.role
        ) ?: return
        notificationRole.notifcenterInt = 0
    }

    override fun refreshNotificationCounter() {
        viewModel.getNotifications(userSession.shopId)
    }

    override fun getPageSource(): String {
        return source
    }

    private fun setupToolbar() {
        setupToolbarLifecycle()
        toolbar?.switchToLightToolbar()
        val view = View.inflate(
            this,
            R.layout.partial_notifcenter_nav_content_view,
            null
        ).also {
            navHeader.bindNavHeaderView(it)
            navHeader.bindValue()
            navHeaderContainer = it.findViewById(R.id.notifcenter_layout_toolbar)
        }
        if (userSession.hasShop()) {
            toolbar?.setCustomViewContentView(view)
            toolbar?.setToolbarContentType(TOOLBAR_TYPE_CUSTOM)
        } else {
            val title = getPageTitle()
            toolbar?.setToolbarContentType(TOOLBAR_TYPE_TITLE)
            toolbar?.setToolbarTitle(title)
        }
    }

    private fun getPageTitle(): String = getString(R.string.notifications)

    protected open fun setupToolbarLifecycle() {
        toolbar?.let { this.lifecycle.addObserver(it) }
    }

    private fun updateToolbarIcon() {
        val icon = IconBuilder()
        icon.addIcon(IconList.ID_CART) { }
        toolbar?.setIcon(icon)
        toolbar?.setBadgeCounter(IconList.ID_CART, NotifCenterConfig.notifications.totalCart)
    }

    private fun setupView() {
        container = findViewById(R.id.notifcenter_layout_container)
        fragmentContainer = findViewById(R.id.notifcenter_fragment_container)
        toolbar = findViewById(R.id.notifcenter_nav_toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        NotifCenterConfig.removeListener(this)
    }

    override fun onRoleChanged(@RoleType role: Int) {
        analytic.trackRoleChanged(role)
        supportFragmentManager.fragments.forEach { fragment ->
            if (fragment is InboxFragment) {
                fragment.onRoleChanged(role)
            }
        }
        navHeader.bindValue()
        cacheState.saveRoleCache(role)
        showNotificationRoleChanged(role)
        updateBadgeCounter()
    }

    private fun showNotificationRoleChanged(@RoleType role: Int) {
        val name = userSession.getRoleName(role)
        val message = getString(R.string.notifcenter_title_change_role, name)
        container?.let {
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL)
                .show()
        }
    }

    private fun setupConfig() {
        NotifCenterConfig.cleanListener()
        NotifCenterConfig.addConfigListener(this)
    }

    private fun setupSwitcher() {
        switcher = NotifCenterAccountSwitcherBottomSheet.create()
        navHeaderContainer?.setOnClickListener {
            switcher?.show(supportFragmentManager, switcher?.javaClass?.simpleName)
            analytic.trackClickSwitchAccount()
        }
    }

    private fun setupBackground() {
        val whiteColor = ContextCompat.getColor(
            this,
            com.tokopedia.unifyprinciples.R.color.Unify_Background
        )
        window.decorView.setBackgroundColor(whiteColor)
    }

    private fun setupNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDarkMode()) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(
                this,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        }
    }

    private fun setupObserver() {
        viewModel.notifications.observe(this) { result ->
            if (result is Success) {
                NotifCenterConfig.notifications = result.data
                updateBadgeCounter()
            }
        }
    }

    private fun updateBadgeCounter() {
        val oppositeRole = NotifCenterConfig.inboxCounter.getByRoleOpposite(NotifCenterConfig.role)
        toolbar?.setBadgeCounter(IconList.ID_CART, NotifCenterConfig.notifications.totalCart)
        oppositeRole?.let {
            navHeader.setBadgeCount(oppositeRole.notifcenterInt)
        }
    }

    private fun setupInitialPage() {
        viewModel.getNotifications(userSession.shopId)
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.notifcenter_fragment_container,
                NotificationFragment.getFragment(
                    supportFragmentManager,
                    classLoader
                ),
                NotificationFragment::class.simpleName
            )
            .commit()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}
