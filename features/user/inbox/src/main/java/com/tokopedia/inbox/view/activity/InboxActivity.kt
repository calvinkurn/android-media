package com.tokopedia.inbox.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inbox.common.config.InboxConfig
import com.tokopedia.inbox.di.DaggerInboxComponent
import com.tokopedia.inbox.domain.data.notification.InboxCounter
import com.tokopedia.inbox.view.custom.InboxBottomNavigationView
import com.tokopedia.inbox.view.custom.NavigationHeader
import com.tokopedia.inbox.view.dialog.AccountSwitcherBottomSheet
import com.tokopedia.inbox.view.ext.getRoleName
import com.tokopedia.inbox.view.navigator.InboxFragmentFactoryImpl
import com.tokopedia.inbox.view.navigator.InboxNavigator
import com.tokopedia.inbox.viewmodel.InboxViewModel
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InboxActivity : BaseActivity(), InboxConfig.ConfigListener, InboxFragmentContainer {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var navHeader: NavigationHeader

    private var switcher: AccountSwitcherBottomSheet? = null
    private var navigator: InboxNavigator? = null
    private var bottomNav: InboxBottomNavigationView? = null
    private var navHeaderContainer: ConstraintLayout? = null
    private var container: CoordinatorLayout? = null
    private var fragmentContainer: FrameLayout? = null
    private var toolbar: NavToolbar? = null
    private var inboxBadgeCounter: InboxCounter = InboxCounter()

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxViewModel::class.java)
    }

    override val role: Int get() = InboxConfig.role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjector()
        setContentView(R.layout.activity_inbox)
        setupView()
        setupConfig()
        setupNavigator()
        setupBackground()
        setupNotificationBar()
        setupBottomNav()
        setupObserver()
        setupInitialPage()
        setupToolbar()
        setupSwitcher()
    }

    override fun clearNotificationCounter() {
        inboxBadgeCounter.getByRole(InboxConfig.role)?.notifcenterInt = 0
        bottomNav?.setBadgeCount(InboxFragmentType.NOTIFICATION, 0)
    }

    private fun setupToolbar() {
        toolbar?.switchToLightToolbar()
        toolbar?.setIcon(
                IconBuilder()
                        .addIcon(IconList.ID_CART) {
                            Toast.makeText(this, "Cart clicked", Toast.LENGTH_SHORT).show()
                        }
                        .addIcon(IconList.ID_NAV_GLOBAL) {
                            Toast.makeText(this, "ID_NAV_GLOBAL clicked", Toast.LENGTH_SHORT).show()
                        }
        )
        val view = View.inflate(
                this, R.layout.partial_inbox_nav_content_view, null
        ).also {
            navHeader.bindNavHeaderView(it)
            navHeader.bindValue()
            navHeaderContainer = it.findViewById(R.id.inbox_toolbar)
        }
        toolbar?.setCustomViewContentView(view)
        toolbar?.setToolbarContentType(TOOLBAR_TYPE_CUSTOM)
    }

    private fun setupInjector() {
        DaggerInboxComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        bottomNav = findViewById(R.id.inbox_bottom_nav)
        container = findViewById(R.id.coor_container)
        fragmentContainer = findViewById(R.id.fragment_contaier)
        toolbar = findViewById(R.id.inbox_nav_toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        InboxConfig.removeListener(this)
    }

    override fun onRoleChanged(@RoleType role: Int) {
        navigator?.notifyRoleChanged(role)
        navHeader.bindValue()
        showNotificationRoleChanged(role)
        updateBadgeCounter()
    }

    private fun showNotificationRoleChanged(@RoleType role: Int) {
        val name = userSession.getRoleName(role)
        val message = getString(R.string.title_change_role, name)
        container?.let {
            Toaster.toasterCustomBottomHeight = 50.toPx()
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                    .show()
        }
    }

    private fun setupConfig() {
        InboxConfig.cleanListener()
        InboxConfig.addConfigListener(this)
    }

    private fun setupSwitcher() {
        switcher = AccountSwitcherBottomSheet.create()
        navHeaderContainer?.setOnClickListener {
            switcher?.show(supportFragmentManager, switcher?.javaClass?.simpleName)
        }
    }

    private fun setupNavigator() {
        navigator = InboxNavigator(
                this,
                R.id.fragment_contaier,
                supportFragmentManager,
                InboxFragmentFactoryImpl()
        )
    }

    private fun setupBackground() {
        val whiteColor = ContextCompat.getColor(
                this, com.tokopedia.unifyprinciples.R.color.Unify_N0
        )
        window.decorView.setBackgroundColor(whiteColor)
    }

    private fun setupNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isDarkMode()) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(
                    this, com.tokopedia.unifyprinciples.R.color.Unify_N0
            )
        }
    }

    private fun setupObserver() {
        viewModel.notifications.observe(this, Observer { result ->
            if (result is Success) {
                inboxBadgeCounter = result.data
                updateBadgeCounter()
            }
        })
    }

    private fun updateBadgeCounter() {
        val notificationRole = inboxBadgeCounter.getByRole(InboxConfig.role) ?: return
        bottomNav?.setBadgeCount(InboxFragmentType.NOTIFICATION, notificationRole.notifcenterInt)
        bottomNav?.setBadgeCount(InboxFragmentType.CHAT, notificationRole.chatInt)
        bottomNav?.setBadgeCount(InboxFragmentType.DISCUSSION, notificationRole.talkInt)
        navHeader.setBadgeCount(notificationRole.totalInt)
    }

    private fun setupBottomNav() {
        bottomNav?.apply {
            setBackgroundColor(Color.TRANSPARENT)
            itemIconTintList = null
            setOnNavigationItemSelectedListener { menu ->
                when (menu.itemId) {
                    R.id.menu_inbox_notification -> {
                        onBottomNavSelected(InboxFragmentType.NOTIFICATION)
                    }
                    R.id.menu_inbox_chat -> {
                        onBottomNavSelected(InboxFragmentType.CHAT)
                    }
                    R.id.menu_inbox_discussion -> {
                        onBottomNavSelected(InboxFragmentType.DISCUSSION)
                    }
                }
                return@setOnNavigationItemSelectedListener true
            }
        }
    }

    private fun setupInitialPage() {
        navigator?.start(InboxFragmentType.NOTIFICATION)
        bottomNav?.setSelectedPage(InboxFragmentType.NOTIFICATION)
        viewModel.getNotifications()
    }

    private fun onBottomNavSelected(@InboxFragmentType page: Int) {
        navigator?.onPageSelected(page)
    }

    private fun isDarkMode(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }
}