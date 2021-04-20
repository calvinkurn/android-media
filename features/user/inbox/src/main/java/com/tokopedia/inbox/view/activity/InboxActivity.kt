package com.tokopedia.inbox.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.inbox.R
import com.tokopedia.inbox.analytic.InboxAnalytic
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inbox.common.config.InboxConfig
import com.tokopedia.inbox.di.DaggerInboxComponent
import com.tokopedia.inbox.domain.cache.InboxCacheState
import com.tokopedia.inbox.domain.listener.InboxOnBoardingListener
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
import com.tokopedia.searchbar.navigation_component.NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

class InboxActivity : BaseActivity(), InboxConfig.ConfigListener, InboxFragmentContainer {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var navHeader: NavigationHeader

    @Inject
    lateinit var cacheState: InboxCacheState

    @Inject
    lateinit var analytic: InboxAnalytic

    private var switcher: AccountSwitcherBottomSheet? = null
    private var navigator: InboxNavigator? = null
    private var bottomNav: InboxBottomNavigationView? = null
    private var navHeaderContainer: ConstraintLayout? = null
    private var container: CoordinatorLayout? = null
    private var fragmentContainer: FrameLayout? = null
    private var toolbar: NavToolbar? = null
    private var onBoardingCoachMark: CoachMark2? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(InboxViewModel::class.java)
    }

    override val role: Int get() = InboxConfig.role

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)
        setupInjector()
        setupLastPreviousState()
        trackOpenInbox()
        setupView()
        setupConfig()
        setupNavigator()
        setupBackground()
        setupNotificationBar()
        setupObserver()
        setupToolbar()
        setupInitialPage()
        setupInitialToolbar()
        setupBottomNav()
        setupSwitcher()
        setupOnBoarding()
    }

    override fun onResume() {
        super.onResume()
        analytic.trackOpenInboxPage(InboxConfig.page, InboxConfig.role)
    }

    private fun setupInjector() {
        DaggerInboxComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupLastPreviousState() {
        InboxConfig.setRole(cacheState.role)
        InboxConfig.page = cacheState.initialPage
    }

    private fun trackOpenInbox() {
        if (!viewModel.hasBeenVisited()) {
            analytic.trackOpenInbox(InboxConfig.page, InboxConfig.role)
            viewModel.markAsVisited()
        }
    }

    override fun clearNotificationCounter() {
        val notificationRole = InboxConfig.inboxCounter.getByRole(
                InboxConfig.role
        ) ?: return
        notificationRole.notifcenterInt = 0
        bottomNav?.setBadgeCount(InboxFragmentType.NOTIFICATION, 0)
    }

    override fun decreaseChatUnreadCounter() {
        val notificationRole = InboxConfig.inboxCounter.getByRole(
                InboxConfig.role
        ) ?: return
        notificationRole.chatInt -= 1
        bottomNav?.setBadgeCount(InboxFragmentType.CHAT, notificationRole.chatInt)
    }

    override fun increaseChatUnreadCounter() {
        val notificationRole = InboxConfig.inboxCounter.getByRole(
                InboxConfig.role
        ) ?: return
        notificationRole.chatInt += 1
        bottomNav?.setBadgeCount(InboxFragmentType.CHAT, notificationRole.chatInt)
    }

    override fun refreshNotificationCounter() {
        viewModel.getNotifications()
    }

    override fun decreaseDiscussionUnreadCounter() {
        val notificationRole = InboxConfig.inboxCounter.getByRole(
                InboxConfig.role
        ) ?: return
        notificationRole.talkInt -= 1
        bottomNav?.setBadgeCount(InboxFragmentType.DISCUSSION, notificationRole.talkInt)
    }

    private fun setupToolbar() {
        toolbar?.switchToLightToolbar()
        val view = View.inflate(
                this, R.layout.partial_inbox_nav_content_view, null
        ).also {
            navHeader.bindNavHeaderView(it)
            navHeader.bindValue()
            navHeaderContainer = it.findViewById(R.id.inbox_toolbar)
        }
        if (userSession.hasShop()) {
            toolbar?.setCustomViewContentView(view)
            toolbar?.setToolbarContentType(TOOLBAR_TYPE_CUSTOM)
        } else {
            val title = getString(R.string.inbox)
            toolbar?.setToolbarContentType(TOOLBAR_TYPE_TITLE)
            toolbar?.setToolbarTitle(title)
        }
    }

    private fun updateToolbarIcon(hasChatSearch: Boolean = false) {
        val icon = IconBuilder()
        if (hasChatSearch) {
            icon.addIcon(IconList.ID_SEARCH) { }
        }
        icon.addIcon(IconList.ID_CART) { }
        icon.addIcon(IconList.ID_NAV_GLOBAL) { }
        toolbar?.setIcon(icon)
        toolbar?.setBadgeCounter(IconList.ID_CART, InboxConfig.notifications.totalCart)
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
        navigator?.cleanupNavigator()
    }

    override fun onRoleChanged(@RoleType role: Int) {
        analytic.trackRoleChanged(role)
        navigator?.notifyRoleChanged(role)
        navHeader.bindValue()
        cacheState.saveRoleCache(role)
        onBoardingCoachMark?.dismissCoachMark()
        showNotificationRoleChanged(role)
        updateBadgeCounter()
    }

    private fun showNotificationRoleChanged(@RoleType role: Int) {
        val name = userSession.getRoleName(role)
        val message = getString(R.string.title_change_role, name)
        container?.let {
            Toaster.toasterCustomBottomHeight = 50.toPx()
            Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL)
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
            if (onBoardingCoachMark?.isShowing == true) {
                onBoardingCoachMark?.stepNext?.performClick()
            } else {
                switcher?.show(supportFragmentManager, switcher?.javaClass?.simpleName)
            }
            analytic.trackClickSwitchAccount()
        }
    }

    private fun setupOnBoarding() {
        if (!viewModel.hasShowOnBoarding()) {
            onBoardingCoachMark = CoachMark2(this)
            if (userSession.hasShop()) {
                showOnBoardingSeller()
            } else {
                showOnBoardingBuyer()
            }
        }
    }

    private fun showOnBoardingSeller() {
        if (bottomNav == null || navHeaderContainer == null || switcher == null) return
        val anchors = ArrayList<CoachMark2Item>()
        anchors.add(
                CoachMark2Item(
                        bottomNav!!,
                        getString(R.string.inbox_title_onboarding_1),
                        getString(R.string.inbox_desc_onboarding_1)
                )
        )
        anchors.add(
                CoachMark2Item(
                        navHeaderContainer!!,
                        getString(R.string.inbox_title_onboarding_2),
                        getString(R.string.inbox_desc_onboarding_2)
                )
        )
        anchors.add(
                CoachMark2Item(
                        navHeaderContainer!!,
                        getString(R.string.inbox_title_onboarding_3),
                        getString(R.string.inbox_desc_onboarding_3),
                        CoachMark2.POSITION_TOP
                )
        )
        onBoardingCoachMark?.showCoachMark(anchors)
        onBoardingCoachMark?.onFinishListener = {
            viewModel.markFinishedSellerOnBoarding()
            switcher?.setShowListener { }
            analytic.trackClickOnBoardingCta(role, 2, "selesai")
        }
        onBoardingCoachMark?.onDismissListener = {
            viewModel.markFinishedSellerOnBoarding()
            switcher?.setShowListener { }
            analytic.trackDismissOnBoarding(role, onBoardingCoachMark?.currentIndex)
        }
        analytic.trackShowOnBoardingOnStep(role, 0)
        onBoardingCoachMark?.setStepListener(InboxOnBoardingListener(
                onStepCoach = { currentIndex: Int,
                                _: CoachMark2Item,
                                direction: String,
                                previousIndex: Int ->
                    analytic.trackShowOnBoardingOnStep(role, currentIndex)
                    analytic.trackClickOnBoardingCta(role, previousIndex, direction)
                    onChangeOnBoardingStep(currentIndex, anchors)
                }
        ))
    }

    private fun onChangeOnBoardingStep(currentIndex: Int, anchors: ArrayList<CoachMark2Item>) {
        if (currentIndex == 2) {
            onBoardingCoachMark?.isDismissed = true
            switcher?.show(supportFragmentManager, switcher?.javaClass?.simpleName)
            switcher?.setShowListener {
                switcher?.let {
                    anchors.last().anchorView = it.bottomSheetWrapper
                    Handler().postDelayed({
                        showDelayedOnBoarding(anchors, 2)
                    }, 250)
                }
            }
        } else if (currentIndex == 1) {
            switcher?.dialog?.let {
                onBoardingCoachMark?.isDismissed = true
                if (it.isShowing) {
                    switcher?.dismiss()
                }
                Handler().postDelayed({
                    showDelayedOnBoarding(anchors, 1)
                }, 250)
            }
        }
    }

    private fun showDelayedOnBoarding(
            anchors: ArrayList<CoachMark2Item>,
            index: Int
    ) {
        onBoardingCoachMark?.isDismissed = false
        onBoardingCoachMark?.showCoachMark(anchors, index = index)
    }

    private fun showOnBoardingBuyer() {
        if (bottomNav == null) return
        val anchors = ArrayList<CoachMark2Item>()
        anchors.add(
                CoachMark2Item(
                        bottomNav!!,
                        getString(R.string.inbox_title_onboarding_1),
                        getString(R.string.inbox_desc_onboarding_1)
                )
        )
        onBoardingCoachMark?.showCoachMark(anchors)
        onBoardingCoachMark?.setOnDismissListener {
            viewModel.markFinishedBuyerOnBoarding()
            analytic.trackDismissOnBoarding(role, onBoardingCoachMark?.currentIndex)
        }
        analytic.trackShowOnBoardingOnStep(role, 0)
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
                InboxConfig.notifications = result.data
                updateBadgeCounter()
            }
        })
    }

    private fun updateBadgeCounter() {
        val notificationRole = InboxConfig.inboxCounter.getByRole(
                InboxConfig.role
        ) ?: return
        val oppositeRole = InboxConfig.inboxCounter.getByRoleOpposite(InboxConfig.role)
        bottomNav?.setBadgeCount(InboxFragmentType.NOTIFICATION, notificationRole.notifcenterInt)
        bottomNav?.setBadgeCount(InboxFragmentType.CHAT, notificationRole.chatInt)
        bottomNav?.setBadgeCount(InboxFragmentType.DISCUSSION, notificationRole.talkInt)
        toolbar?.setBadgeCounter(IconList.ID_CART, InboxConfig.notifications.totalCart)
        oppositeRole?.let {
            navHeader.setBadgeCount(oppositeRole.totalInt)
        }
    }

    private fun setupBottomNav() {
        bottomNav?.apply {
            setBackgroundColor(Color.TRANSPARENT)
            itemIconTintList = null
            setOnNavigationItemSelectedListener { menu ->
                when (menu.itemId) {
                    R.id.menu_inbox_notification -> {
                        cacheState.saveInitialPageCache(InboxFragmentType.NOTIFICATION)
                        onBottomNavSelected(InboxFragmentType.NOTIFICATION)
                        updateToolbarIcon()
                        InboxConfig.page = InboxFragmentType.NOTIFICATION
                    }
                    R.id.menu_inbox_chat -> {
                        cacheState.saveInitialPageCache(InboxFragmentType.CHAT)
                        onBottomNavSelected(InboxFragmentType.CHAT)
                        updateToolbarIcon(true)
                        InboxConfig.page = InboxFragmentType.CHAT
                    }
                    R.id.menu_inbox_discussion -> {
                        cacheState.saveInitialPageCache(InboxFragmentType.DISCUSSION)
                        onBottomNavSelected(InboxFragmentType.DISCUSSION)
                        updateToolbarIcon()
                        InboxConfig.page = InboxFragmentType.DISCUSSION
                    }
                }
                analytic.trackOpenInboxPage(InboxConfig.page, InboxConfig.role)
                analytic.trackClickBottomNaveMenu(InboxConfig.page, InboxConfig.role)
                return@setOnNavigationItemSelectedListener true
            }
        }
    }

    private fun setupInitialPage() {
        navigator?.start(InboxConfig.page)
        bottomNav?.setSelectedPage(InboxConfig.page)
        viewModel.getNotifications()
    }

    private fun setupInitialToolbar() {
        val isChatPage = InboxConfig.page == InboxFragmentType.CHAT
        updateToolbarIcon(isChatPage)
    }

    private fun onBottomNavSelected(@InboxFragmentType page: Int) {
        navigator?.onPageSelected(page)
    }

}