package com.tokopedia.inbox.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inbox.common.config.InboxConfig
import com.tokopedia.inbox.di.DaggerInboxComponent
import com.tokopedia.inbox.domain.data.notification.BaseNotification
import com.tokopedia.inbox.domain.data.notification.InboxCounter
import com.tokopedia.inbox.view.custom.InboxBottomNavigationView
import com.tokopedia.inbox.view.dialog.AccountSwitcherBottomSheet
import com.tokopedia.inbox.view.navigator.InboxFragmentFactoryImpl
import com.tokopedia.inbox.view.navigator.InboxNavigator
import com.tokopedia.inbox.viewmodel.InboxViewModel
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InboxActivity : BaseActivity(), InboxConfig.ConfigListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var switcher: AccountSwitcherBottomSheet? = null
    private var navigator: InboxNavigator? = null
    private val bottomNav: InboxBottomNavigationView? by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<InboxBottomNavigationView?>(R.id.inbox_bottom_nav)
    }
    private val currentRole: ConstraintLayout? by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<ConstraintLayout?>(R.id.cl_current_role_container)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjector()
        setContentView(R.layout.activity_inbox)
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

    override fun onDestroy() {
        super.onDestroy()
        InboxConfig.removeListener(this)
    }

    override fun onRoleChanged(@RoleType role: Int) {
        navigator?.notifyRoleChanged(role)
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
        navigator = InboxNavigator(this, R.id.fragment_contaier, supportFragmentManager, InboxFragmentFactoryImpl())
    }

    private fun setupBackground() {
        window.decorView.setBackgroundColor(Color.WHITE)
    }

    private fun setupNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    private fun setupObserver() {
        viewModel.notifications.observe(this, Observer { result ->
            if (result is Success) {
                updateNotificationCounter(result.data)
            }
        })
    }

    private fun updateNotificationCounter(notification: InboxCounter) {
        val notificationRole: BaseNotification? = when (InboxConfig.role) {
            RoleType.BUYER -> notification.buyer
            RoleType.SELLER -> notification.seller
            else -> null
        }
        bottomNav?.setBadgeCount(InboxFragmentType.NOTIFICATION, notificationRole?.notifcenterInt)
        bottomNav?.setBadgeCount(InboxFragmentType.CHAT, notificationRole?.chatInt)
        bottomNav?.setBadgeCount(InboxFragmentType.DISCUSSION, notificationRole?.talkInt)
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
        navigator?.showPage(page)
    }
}