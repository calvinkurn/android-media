package com.tokopedia.affiliate.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver.onFragmentSelected
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver.onFragmentUnSelected
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.affiliate.AFFILIATE_PROMOTE_HOME
import com.tokopedia.affiliate.AFFILIATE_PROMO_WEBVIEW
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.FIRST_TAB
import com.tokopedia.affiliate.FOURTH_TAB
import com.tokopedia.affiliate.PAGE_SEGMENT_DISCO_PAGE_LIST
import com.tokopedia.affiliate.PAGE_SEGMENT_EDU_PAGE
import com.tokopedia.affiliate.PAGE_SEGMENT_HELP
import com.tokopedia.affiliate.PAGE_SEGMENT_ONBOARDING
import com.tokopedia.affiliate.PAGE_SEGMENT_PERFORMA
import com.tokopedia.affiliate.PAGE_SEGMENT_PROMO_PAGE
import com.tokopedia.affiliate.PAGE_SEGMENT_SSA_SHOP_LIST
import com.tokopedia.affiliate.PAGE_SEGMENT_TRANSACTION_HISTORY
import com.tokopedia.affiliate.SECOND_TAB
import com.tokopedia.affiliate.THIRD_TAB
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavbar
import com.tokopedia.affiliate.ui.custom.IBottomClickListener
import com.tokopedia.affiliate.ui.fragment.AffiliateAdpFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateIncomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoWebViewFragment
import com.tokopedia.affiliate.ui.fragment.education.AffiliateEducationLandingPage
import com.tokopedia.affiliate.viewmodel.AffiliateViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class AffiliateActivity :
    BaseSimpleActivity(),
    HasComponent<AffiliateComponent>,
    IBottomClickListener,
    AffiliateBottomNavBarInterface {
    private val affiliateComponent: AffiliateComponent by lazy(LazyThreadSafetyMode.NONE) { initInject() }

    @Inject
    @JvmField
    var userSessionInterface: UserSessionInterface? = null

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    private var affiliateVM: AffiliateViewModel? = null
    private var fragmentStack = Stack<String>()
    private var affiliateBottomNavigation: AffiliateBottomNavbar? = null

    private var fromHelpAppLink = false
    private var fromAppLink = false
    private var isLandingOnWebview = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        affiliateComponent.injectActivity(this)
        affiliateVM = viewModelProvider?.let { ViewModelProvider(this, it) }
            ?.get(AffiliateViewModel::class.java)
        intent?.data?.let { data ->
            fromAppLink = data.pathSegments.contains(PAGE_SEGMENT_EDU_PAGE)
            fromHelpAppLink = data.pathSegments.contains(PAGE_SEGMENT_HELP)
            if (data.pathSegments?.contains(PAGE_SEGMENT_ONBOARDING) == true) {
                if (data.queryParameterNames.isNotEmpty()) {
                    showLoginPortal(
                        data.queryParameterNames.first() to intent?.data?.getQueryParameter(
                            data.queryParameterNames.first()
                        )
                    )
                } else {
                    showLoginPortal()
                }
            } else {
                afterViewCreated()
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.affiliate_layout

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Uri.parse(intent?.data?.path ?: "").pathSegments.firstOrNull()?.let {
            when {
                it.equals(PAGE_SEGMENT_SSA_SHOP_LIST, true) ->
                    startActivity(Intent(this, AffiliateSSAShopListActivity::class.java))

                it.equals(PAGE_SEGMENT_DISCO_PAGE_LIST, true) ->
                    startActivity(Intent(this, AffiliateDiscoPromoListActivity::class.java))

                it.equals(PAGE_SEGMENT_PROMO_PAGE, true) -> {
                    isLandingOnWebview = true
                    selectItem(PROMO_MENU, R.id.menu_promo_affiliate, true)
                }

                it.equals(PAGE_SEGMENT_PERFORMA, true) ->
                    selectItem(ADP_MENU, R.id.menu_performa_affiliate, true)

                it.equals(PAGE_SEGMENT_EDU_PAGE, true) || it.equals(PAGE_SEGMENT_HELP, true) -> {
                    fromAppLink = it.contains(PAGE_SEGMENT_EDU_PAGE)
                    fromHelpAppLink = it.contains(PAGE_SEGMENT_HELP)
                    selectItem(EDUKASI_MENU, R.id.menu_edukasi_affiliate, true)
                }

                it.equals(PAGE_SEGMENT_TRANSACTION_HISTORY, true) ->
                    selectItem(INCOME_MENU, R.id.menu_withdrawal_affiliate, true)

                it.equals(PAGE_SEGMENT_ONBOARDING, true) ->
                    if (intent?.data?.queryParameterNames.isNullOrEmpty()) {
                        showLoginPortal()
                    } else {
                        showLoginPortal(
                            intent?.data?.queryParameterNames?.first()
                                .orEmpty() to intent?.data?.getQueryParameter(
                                intent.data?.queryParameterNames?.first()
                            )
                        )
                    }

                else -> {}
            }
        }
    }

    private fun initInject() = DaggerAffiliateComponent.builder()
        .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()

    override fun getComponent(): AffiliateComponent = affiliateComponent

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun afterViewCreated() {
        if (userSessionInterface?.isLoggedIn == true) {
            isLandingOnWebview = Uri.parse(intent?.data?.path ?: "").pathSegments.firstOrNull()
                ?.equals(PAGE_SEGMENT_PROMO_PAGE, true).orTrue()
            setObservers()
            affiliateVM?.getAffiliateValidateUser()
        } else {
            showLoginPortal()
        }
    }

    private fun showLoginPortal(queryData: Pair<String, String?>? = null) {
        AffiliateRegistrationActivity.newInstance(this, queryData = queryData)
        finish()
    }

    private fun isAffiliatePromoWebViewEnabled() =
        RemoteConfigInstance.getInstance()?.abTestPlatform?.getString(
            AFFILIATE_PROMO_WEBVIEW,
            ""
        ) == AFFILIATE_PROMO_WEBVIEW

    private fun showAffiliatePortal() {
        updateHomeTab()
        initBottomNavigationView()
        findViewById<ImageUnify>(R.id.affiliate_background_image)?.show()
        pushOpenScreenEvent()
    }

    private fun pushOpenScreenEvent() {
        AffiliateAnalytics.sendOpenScreenEvent(
            AffiliateAnalytics.EventKeys.OPEN_SCREEN,
            AffiliateAnalytics.ScreenKeys.AFFILIATE_HOME_SCREEN_NAME,
            userSessionInterface?.isLoggedIn.orFalse(),
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun initBottomNavigationView() {
        var selectedTab = if (isAffiliatePromoteHomeEnabled()) {
            PROMO_MENU
        } else {
            ADP_MENU
        }
        Uri.parse(intent?.data?.path ?: "").pathSegments.firstOrNull()?.let {
            when {
                it.equals(PAGE_SEGMENT_HELP, true) || it.equals(
                    PAGE_SEGMENT_EDU_PAGE,
                    true
                ) -> selectedTab = EDUKASI_MENU

                it.equals(PAGE_SEGMENT_TRANSACTION_HISTORY, true) -> selectedTab = INCOME_MENU

                it.equals(PAGE_SEGMENT_PROMO_PAGE, true) -> selectedTab = PROMO_MENU

                it.equals(PAGE_SEGMENT_PERFORMA, true) -> selectedTab = ADP_MENU

                it.equals(PAGE_SEGMENT_SSA_SHOP_LIST, true) ->
                    startActivity(Intent(this, AffiliateSSAShopListActivity::class.java))

                it.equals(PAGE_SEGMENT_DISCO_PAGE_LIST, true) ->
                    startActivity(Intent(this, AffiliateDiscoPromoListActivity::class.java))
            }
        }
        affiliateBottomNavigation = AffiliateBottomNavbar(
            findViewById(R.id.bottom_navbar),
            this,
            this,
            selectedTab
        )
        if (selectedTab == PROMO_MENU) {
            isLandingOnWebview = true
            affiliateBottomNavigation?.hideBottomNav()
        } else {
            affiliateBottomNavigation?.showBottomNav()
        }
        affiliateBottomNavigation?.populateBottomNavigationView()
    }

    override fun menuClicked(position: Int, id: Int, isNotFromBottom: Boolean): Boolean {
        when (position) {
            ADP_MENU -> {
                affiliateBottomNavigation?.showBottomNav()
                openFragment(AffiliateAdpFragment.getFragmentInstance(this))
            }

            PROMO_MENU -> if (isAffiliatePromoWebViewEnabled()) {
                openFragment(AffiliatePromoWebViewFragment.getFragmentInstance())
                affiliateBottomNavigation?.hideBottomNav()
            } else {
                openFragment(AffiliatePromoFragment.getFragmentInstance())
                affiliateBottomNavigation?.showBottomNav()
            }

            INCOME_MENU -> {
                affiliateBottomNavigation?.showBottomNav()
                openFragment(
                    AffiliateIncomeFragment.getFragmentInstance(
                        userSessionInterface?.name.orEmpty(),
                        userSessionInterface?.profilePicture.orEmpty(),
                        this
                    )
                )
            }

            EDUKASI_MENU -> {
                affiliateBottomNavigation?.showBottomNav()
                openFragment(
                    AffiliateEducationLandingPage.getFragmentInstance(
                        fromAppLink = fromAppLink,
                        fromHelpAppLink = fromHelpAppLink
                    )
                )
            }
        }
        if (!isNotFromBottom) sendBottomNavClickEvent(position)
        return true
    }

    override fun menuReselected(position: Int, id: Int) = Unit

    private fun setObservers() {
        affiliateVM?.getValidateUserdata()?.observe(this) { validateUserdata ->
            if (validateUserdata.validateAffiliateUserStatus.userStatusData?.isRegistered == true &&
                validateUserdata.validateAffiliateUserStatus.userStatusData?.isEligible == true
            ) {
                showAffiliatePortal()
            } else {
                showLoginPortal()
            }
        }

        affiliateVM?.progressBar()?.observe(this) {
            if (it) {
                if (isLandingOnWebview) {
                    findViewById<View>(R.id.webview_shimmer)?.show()
                } else {
                    findViewById<LoaderUnify>(R.id.affiliate_home_progress_bar)?.show()
                }
            } else {
                findViewById<View>(R.id.webview_shimmer)?.hide()
                findViewById<LoaderUnify>(R.id.affiliate_home_progress_bar)?.hide()
            }
        }

        affiliateVM?.getErrorMessage()?.observe(this) { error ->
            when (error) {
                is UnknownHostException, is SocketTimeoutException ->
                    Toaster.build(
                        findViewById<FrameLayout>(R.id.parent_view),
                        getString(R.string.affiliate_internet_error),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()

                else -> showLoginPortal()
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val backStackName = fragment.javaClass.name
        val ft = supportFragmentManager.beginTransaction()
        val currentFrag: Fragment? = supportFragmentManager.findFragmentByTag(backStackName)
        if (currentFrag != null && supportFragmentManager.fragments.size > 0) {
            showSelectedFragment(fragment.javaClass.name, supportFragmentManager, ft)
            fragmentStack.add(fragment.javaClass.name)
        } else {
            fragmentStack.add(fragment.javaClass.name)
            ft.add(R.id.parent_view, fragment, backStackName)
            showSelectedFragment(fragment.javaClass.name, supportFragmentManager, ft)
            onFragmentSelected(fragment)
        }
        ft.commitNowAllowingStateLoss()
    }

    private fun sendBottomNavClickEvent(position: Int) {
        var eventAction = ""
        when (position) {
            ADP_MENU -> eventAction = AffiliateAnalytics.ActionKeys.HOME_NAV_BAR_CLICK
            PROMO_MENU -> eventAction = AffiliateAnalytics.ActionKeys.PROMOSIKAN_NAV_BAR_CLICK
            EDUKASI_MENU -> eventAction = AffiliateAnalytics.ActionKeys.BANUTAN_NAV_BAR_CLICK
            INCOME_MENU -> eventAction = AffiliateAnalytics.ActionKeys.PENDAPATAN_NAV_BAR_CLICK
        }
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            "",
            userSessionInterface?.userId.orEmpty()
        )
    }

    private fun showSelectedFragment(
        fragmentName: String,
        manager: FragmentManager,
        ft: FragmentTransaction
    ) {
        for (i in manager.fragments.indices) {
            val frag = manager.fragments[i]
            if (frag.javaClass.name.equals(fragmentName, ignoreCase = true)) {
                ft.show(frag)
                onFragmentSelected(frag)
            } else {
                ft.hide(frag)
                onFragmentUnSelected(frag)
            }
        }
    }

    private fun isAffiliatePromoteHomeEnabled() =
        RemoteConfigInstance.getInstance()?.abTestPlatform?.getString(
            AFFILIATE_PROMOTE_HOME,
            ""
        ) == AFFILIATE_PROMOTE_HOME

    private fun updateHomeTab() {
        if (isAffiliatePromoteHomeEnabled()) {
            PROMO_MENU = FIRST_TAB
            ADP_MENU = SECOND_TAB
        } else {
            ADP_MENU = FIRST_TAB
            PROMO_MENU = SECOND_TAB
        }
    }

    companion object MenuItems {

        var ADP_MENU = FIRST_TAB
        var PROMO_MENU = SECOND_TAB
        const val INCOME_MENU = THIRD_TAB
        const val EDUKASI_MENU = FOURTH_TAB
    }

    override fun selectItem(position: Int, id: Int, isNotFromBottom: Boolean) {
        affiliateBottomNavigation?.setSelected(position, isNotFromBottom)
        if (position == PROMO_MENU) {
            affiliateBottomNavigation?.hideBottomNav()
        } else {
            affiliateBottomNavigation?.showBottomNav()
        }
    }

    override fun onBackPressed() {
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.parent_view)
        if (currentFragment != null && currentFragment.isVisible) {
            when (currentFragment) {
                is AffiliatePromoFragment -> (currentFragment as? AffiliatePromoFragment)?.handleBack()

                is AffiliateEducationLandingPage -> (currentFragment as? AffiliateEducationLandingPage)?.handleBack()

                is AffiliatePromoWebViewFragment -> (currentFragment as? AffiliatePromoWebViewFragment)?.handleBack()

                else -> finish()
            }
            setBottomState(currentFragment::class.java.name)
        } else {
            finish()
        }
    }

    private fun setBottomState(peek: String?) {
        when (peek) {
            AffiliateAdpFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                ADP_MENU
            )

            AffiliatePromoFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                PROMO_MENU
            )

            AffiliatePromoWebViewFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                PROMO_MENU
            )

            AffiliateIncomeFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                INCOME_MENU
            )

            AffiliateEducationLandingPage::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                EDUKASI_MENU
            )
        }
    }

    fun getValidateUserData() = affiliateVM?.getValidateUserdata()

    fun refreshValidateUserData() = affiliateVM?.getAffiliateValidateUser()
}
