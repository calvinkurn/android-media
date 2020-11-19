package com.tokopedia.inbox.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.tokopedia.inbox.view.binder.BadgeCounterBinder
import com.tokopedia.inbox.view.custom.InboxBottomNavigationView
import com.tokopedia.inbox.view.dialog.AccountSwitcherBottomSheet
import com.tokopedia.inbox.view.ext.getRoleName
import com.tokopedia.inbox.view.navigator.InboxFragmentFactoryImpl
import com.tokopedia.inbox.view.navigator.InboxNavigator
import com.tokopedia.inbox.viewmodel.InboxViewModel
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InboxActivity : BaseActivity(), InboxConfig.ConfigListener, InboxFragmentContainer {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var switcher: AccountSwitcherBottomSheet? = null
    private var navigator: InboxNavigator? = null
    private var roleNotificationCounter: Typography? = null
    private var bottomNav: InboxBottomNavigationView? = null
    private var currentRole: ConstraintLayout? = null
    private var fragmentContainer: FrameLayout? = null

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
        setupSwitcher()
        setupNavigator()
        setupBackground()
        setupNotificationBar()
        setupBottomNav()
        setupObserver()
        setupInitialPage()
    }

    private fun setupInjector() {
        DaggerInboxComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        roleNotificationCounter = findViewById(R.id.unread_counter)
        bottomNav = findViewById(R.id.inbox_bottom_nav)
        currentRole = findViewById(R.id.cl_current_role_container)
        fragmentContainer = findViewById(R.id.fragment_contaier)
    }

    override fun onDestroy() {
        super.onDestroy()
        InboxConfig.removeListener(this)
    }

    override fun onRoleChanged(@RoleType role: Int) {
        navigator?.notifyRoleChanged(role)
        showNotificationRoleChanged(role)
    }

    private fun showNotificationRoleChanged(@RoleType role: Int) {
        val name = userSession.getRoleName(role)
        val message = getString(R.string.title_change_role, name)
        fragmentContainer?.let {
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
        currentRole?.setOnClickListener {
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
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(
                    this, com.tokopedia.unifyprinciples.R.color.Unify_N0
            )
        }
    }

    private fun setupObserver() {
        viewModel.notifications.observe(this, Observer { result ->
            if (result is Success) {
                updateBottomNavNotificationCounter(result.data)
                updateToolbarNotificationCounter(result.data)
            }
        })
    }

    private fun updateBottomNavNotificationCounter(notification: InboxCounter) {
        val notificationRole = notification.getByRole(InboxConfig.role)
        bottomNav?.setBadgeCount(InboxFragmentType.NOTIFICATION, notificationRole?.notifcenterInt)
        bottomNav?.setBadgeCount(InboxFragmentType.CHAT, notificationRole?.chatInt)
        bottomNav?.setBadgeCount(InboxFragmentType.DISCUSSION, notificationRole?.talkInt)
    }

    private fun updateToolbarNotificationCounter(notification: InboxCounter) {
        // TODO: to be implemented on global nav custom view
        val notificationRole = notification.getByRole(InboxConfig.role)
        BadgeCounterBinder.bindBadgeCounter(roleNotificationCounter, notificationRole?.totalInt)
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
                        Toast.makeText(context, "discussion", Toast.LENGTH_SHORT).show()
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
}