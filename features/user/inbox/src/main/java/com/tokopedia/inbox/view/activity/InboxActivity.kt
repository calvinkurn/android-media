package com.tokopedia.inbox.view.activity

import android.graphics.Color
import android.net.Uri
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
import com.tokopedia.applink.ApplinkConst.Inbox.*
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
import com.tokopedia.inbox.view.navigator.InboxFragmentFactory
import com.tokopedia.inbox.view.navigator.InboxFragmentFactoryImpl
import com.tokopedia.inbox.view.navigator.InboxNavigator
import com.tokopedia.inbox.viewmodel.InboxViewModel
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
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
 * val applinkUri = Uri.parse(ApplinkConst.INBOX).buildUpon().apply {
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
open class InboxActivity : BaseActivity(), InboxConfig.ConfigListener, InboxFragmentContainer {

    private var source = ""
    private var isShowBottomNav = true

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
    private var bottomNavShadow: View? = null
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
        setupOptionalParameter()
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
        createDaggerComponent()
            .inject(this)
    }

    protected open fun createDaggerComponent() = DaggerInboxComponent.builder()
        .baseAppComponent((application as BaseMainApplication).baseAppComponent)
        .build()

    private fun setupLastPreviousState() {
        InboxConfig.setRole(cacheState.role)
        InboxConfig.page = cacheState.initialPage
    }

    private fun setupOptionalParameter() {
        val data = intent?.data ?: return
        setupInitialPageFromParams(data)
        setupInitialRoleFromParams(data)
        setupPageSource(data)
        setupPageIsShowBottomNav(data)
    }

    private fun setupInitialPageFromParams(data: Uri) {
        val pageInt = when (data.getQueryParameter(PARAM_PAGE)) {
            VALUE_PAGE_NOTIFICATION -> InboxFragmentType.NOTIFICATION
            VALUE_PAGE_CHAT -> InboxFragmentType.CHAT
            VALUE_PAGE_TALK -> InboxFragmentType.DISCUSSION
            VALUE_PAGE_REVIEW -> InboxFragmentType.REVIEW
            else -> null
        }
        pageInt?.let {
            InboxConfig.page = it
        }
    }

    private fun setupInitialRoleFromParams(data: Uri) {
        val roleInt = when (data.getQueryParameter(PARAM_ROLE)) {
            VALUE_ROLE_BUYER -> RoleType.BUYER
            VALUE_ROLE_SELLER -> RoleType.SELLER
            else -> null
        }
        roleInt?.let {
            InboxConfig.setRole(it)
        }
    }

    private fun setupPageSource(data: Uri) {
        val source = data.getQueryParameter(PARAM_SOURCE)
        source?.let {
            this.source = it
        }
    }

    private fun setupPageIsShowBottomNav(data: Uri) {
        isShowBottomNav = data.getBooleanQueryParameter(PARAM_SHOW_BOTTOM_NAV, true)
    }

    private fun trackOpenInbox() {
        analytic.trackOpenInbox(InboxConfig.page, InboxConfig.role)
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

    override fun decreaseReviewUnreviewedCounter() {
        val notificationRole = InboxConfig.inboxCounter.getByRole(
            InboxConfig.role
        ) ?: return
        notificationRole.reviewInt -= 1
        bottomNav?.setBadgeCount(InboxFragmentType.REVIEW, notificationRole.reviewInt)
    }

    override fun hideReviewCounter() {
        bottomNav?.setBadgeCount(InboxFragmentType.REVIEW, 0)
    }

    override fun showReviewCounter() {
        val notificationRole = InboxConfig.inboxCounter.getByRole(RoleType.BUYER) ?: return
        bottomNav?.setBadgeCount(InboxFragmentType.REVIEW, notificationRole.reviewInt)
    }

    override fun getPageSource(): String {
        return source
    }

    private fun setupToolbar() {
        setupToolbarLifecycle()
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

    protected open fun setupToolbarLifecycle() {
        toolbar?.let { this.lifecycle.addObserver(it) }
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
        bottomNavShadow = findViewById(R.id.bottom_nav_top_shadow)
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
            if (bottomNav?.isVisible == true) {
                Toaster.toasterCustomBottomHeight = 50.toPx()
            }
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
        if (isShowBottomNav) {
            anchors.add(
                CoachMark2Item(
                    bottomNav!!,
                    getString(R.string.inbox_title_onboarding_1),
                    getString(R.string.inbox_desc_onboarding_1)
                )
            )
        }
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
            analytic.trackClickOnBoardingCta(role, anchors.lastIndex, "selesai")
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
        val coachMarkItem = anchors.getOrNull(currentIndex) ?: return
        val delayMillis = 250L
        if (coachMarkItem.title == getString(R.string.inbox_title_onboarding_3)) {
            onBoardingCoachMark?.isDismissed = true
            switcher?.show(supportFragmentManager, switcher?.javaClass?.simpleName)
            switcher?.setShowListener {
                switcher?.let {
                    anchors.last().anchorView = it.bottomSheetWrapper
                    Handler().postDelayed({
                        showDelayedOnBoarding(anchors, currentIndex)
                    }, delayMillis)
                }
            }
        } else if (coachMarkItem.title == getString(R.string.inbox_title_onboarding_2)) {
            switcher?.dialog?.let {
                onBoardingCoachMark?.isDismissed = true
                if (it.isShowing) {
                    switcher?.dismiss()
                }
                Handler().postDelayed({
                    showDelayedOnBoarding(anchors, currentIndex)
                }, delayMillis)
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
        if (bottomNav == null || !isShowBottomNav) return
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
            createFragmentFactory()
        )
    }

    protected open fun createFragmentFactory(): InboxFragmentFactory {
        return InboxFragmentFactoryImpl(InboxConfig.page, isShowBottomNav)
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
        bottomNav?.setBadgeCount(InboxFragmentType.REVIEW, notificationRole.reviewInt)
        toolbar?.setBadgeCounter(IconList.ID_CART, InboxConfig.notifications.totalCart)
        oppositeRole?.let {
            navHeader.setBadgeCount(oppositeRole.totalInt)
        }
    }

    private fun setupBottomNav() {
        if (isShowBottomNav) {
            bottomNav?.show()
            bottomNavShadow?.show()
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
                        R.id.menu_inbox_review -> {
                            cacheState.saveInitialPageCache(InboxFragmentType.REVIEW)
                            onBottomNavSelected(InboxFragmentType.REVIEW)
                            updateToolbarIcon()
                            InboxConfig.page = InboxFragmentType.REVIEW
                        }
                    }
                    analytic.trackOpenInboxPage(InboxConfig.page, InboxConfig.role)
                    analytic.trackClickBottomNaveMenu(InboxConfig.page, InboxConfig.role)
                    return@setOnNavigationItemSelectedListener true
                }
            }
        } else {
            bottomNav?.hide()
            bottomNavShadow?.hide()
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