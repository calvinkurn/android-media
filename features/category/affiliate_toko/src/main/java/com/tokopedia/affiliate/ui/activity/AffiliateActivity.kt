package com.tokopedia.affiliate.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver.onFragmentSelected
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver.onFragmentUnSelected
import com.tokopedia.affiliate.*
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.ui.custom.*
import com.tokopedia.affiliate.ui.fragment.AffiliateHelpFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateHomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateIncomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateLoginFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliatePortfolioFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.viewmodel.AffiliateViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseSessionWebViewFragment
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject


class AffiliateActivity : BaseViewModelActivity<AffiliateViewModel>(), IBottomClickListener,
    AffiliateBottomNavBarInterface, AffiliateActivityInterface {

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateVM: AffiliateViewModel
    private var fragmentStack = Stack<String>()
    private var affiliateBottomNavigation: AffiliateBottomNavbar? = null
    private var userActionRequiredForRegister = true

    private var isUserBlackListed = false
    private var isAffiliateWalletEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        afterViewCreated()
    }

    override fun getLayoutRes(): Int = R.layout.affiliate_layout

    override fun getViewModelType(): Class<AffiliateViewModel> {
        return AffiliateViewModel::class.java
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Uri.parse(intent?.data?.path ?: "").pathSegments.firstOrNull()?.let {
            if (it.contains(PAGE_SEGMENT_HELP)) {
                selectItem(HELP_MENU, R.id.menu_help_affiliate, true)
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateViewModel
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun afterViewCreated() {
        setObservers()
        if (userSessionInterface.isLoggedIn)
            affiliateVM.getAffiliateValidateUser()
        else
            showLoginPortal()
    }

    private fun initRollence() {
        isAffiliateWalletEnabled =
            when (RemoteConfigInstance.getInstance().abTestPlatform.getString(
                AFFILIATE_TRX_ENABLED,
                ""
            )) {
                AFFILIATE_TRX_ENABLED -> true
                else -> false
            }
    }

    private fun showLoginPortal() {
        if (fragmentStack.isEmpty() || (fragmentStack.peek() != (AffiliateLoginFragment::class.java.canonicalName)))
            openFragment(AffiliateLoginFragment.getFragmentInstance())
    }

    private fun showAffiliatePortal() {
        initRollence()
        initBottomNavigationView()
        findViewById<ImageUnify>(R.id.affiliate_background_image)?.show()
        if(findViewById<LottieBottomNavbar>(R.id.bottom_navbar)?.visibility !=  View.VISIBLE)
            affiliateBottomNavigation?.populateBottomNavigationView()
        affiliateBottomNavigation?.showBottomNav()
    }

    private val coachMarkItemList = ArrayList<CoachMark2Item>()
    private var coachMark: CoachMark2? = null
    private var currentCoachIndex: Int = 0
    private var viewFound: Boolean = false
    override fun showCoachMarker() {
        disableTouch()
        coachMark = CoachMark2(this)
        getHomeFragmentView()?.let { firstView ->
            coachMarkItemList.add(
                CoachMark2Item(
                    firstView,
                    getString(R.string.affiliate_coacher_title1),
                    getString(R.string.affiliate_coacher_desc1)
                )
            )
        }
        findViewById<LottieBottomNavbar>(R.id.bottom_navbar)?.getView(PROMO_MENU)
            ?.let { secondView ->
                coachMarkItemList.add(
                    CoachMark2Item(
                        secondView,
                        getString(R.string.affiliate_coacher_title2),
                        getString(R.string.affiliate_coacher_desc2)
                    )
                )
                coachMarkItemList.add(
                    CoachMark2Item(
                        secondView,
                        getString(R.string.affiliate_coacher_title3),
                        getString(R.string.affiliate_coacher_desc3)
                    )
                )
            }

        coachMark?.showCoachMark(coachMarkItemList, null)
        coachMark?.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                disableTouch()
                when (currentIndex) {
                    1 -> {
                        if (currentCoachIndex == 2) handleBackButton(true)
                    }
                    2 -> {
                        getPromoFragmentView()?.let {
                            if (!viewFound) {
                                coachMarkItemList[currentIndex].anchorView = it
                                viewFound = true
                            }
                            setBottomState(AffiliatePromoFragment::class.java.name)
                        }
                    }
                }
                currentCoachIndex = currentIndex
            }
        })
        coachMark?.setOnDismissListener{
            CoachMarkPreference.setShown(this, COACHMARK_TAG,true)
            enableTouch()
        }

    }

    private fun disableTouch() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun enableTouch(){
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private fun getPromoFragmentView(): AffiliateLinkTextField? {
        openFragment(AffiliatePromoFragment.getFragmentInstance())
        val currentFragment =
            supportFragmentManager.findFragmentByTag(AffiliatePromoFragment::class.java.name)
        currentFragment?.let { fragment ->
            return (fragment as? AffiliatePromoFragment)?.view?.findViewById<AffiliateLinkTextField>(
                R.id.product_link_et
            )
        }
        return null
    }

    private fun getHomeFragmentView(): Typography? {
        val currentFragment =
            supportFragmentManager.findFragmentByTag(AffiliateHomeFragment::class.java.name)
        currentFragment?.let { fragment ->
            return (fragment as? AffiliateHomeFragment)?.view?.findViewById<Typography>(R.id.user_name)
        }
        return null
    }

    private fun clearBackStack() {
        for (frag in supportFragmentManager.fragments) {
            supportFragmentManager.popBackStack()
        }
        fragmentStack.clear()
    }

    private fun initBottomNavigationView() {
        affiliateBottomNavigation = AffiliateBottomNavbar(
            findViewById(R.id.bottom_navbar),
            this, this, isAffiliateWalletEnabled
        )
        if (isAffiliateWalletEnabled) {
            INCOME_MENU = 2
            HELP_MENU = 3
        } else {
            INCOME_MENU = 3
            HELP_MENU = 2
        }
    }

    override fun menuClicked(position: Int, id: Int, isNotFromBottom: Boolean): Boolean {
        when (position) {
            HOME_MENU -> openFragment(AffiliateHomeFragment.getFragmentInstance(this, this))
            PROMO_MENU -> openFragment(AffiliatePromoFragment.getFragmentInstance())
            INCOME_MENU -> openFragment(
                AffiliateIncomeFragment.getFragmentInstance(
                    userSessionInterface.name,
                    userSessionInterface.profilePicture, this
                )
            )
            HELP_MENU -> openFragment(AffiliateHelpFragment.getFragmentInstance(AFFILIATE_HELP_URL))
        }
        if (!isNotFromBottom) sendBottomNavClickEvent(position)
        return true
    }

    override fun menuReselected(position: Int, id: Int) {

    }

    private fun setObservers() {
        affiliateVM.getValidateUserdata().observe(this, { validateUserdata ->
            if (validateUserdata.validateAffiliateUserStatus.data?.isRegistered == true) {
                clearBackStack()
                showAffiliatePortal()
            } else if (validateUserdata.validateAffiliateUserStatus.data?.isEligible == false &&
                validateUserdata.validateAffiliateUserStatus.data?.isRegistered == false
            ) {
                showFraudTicker()
            } else {
                if (!userActionRequiredForRegister)
                    navigateToPortfolioFragment()
                else {
                    showLoginPortal()
                }
            }
        })

        affiliateVM.getErrorMessage().observe(this, { error ->
            when (error) {
                is UnknownHostException, is SocketTimeoutException -> {
                    Toaster.build(
                        findViewById<FrameLayout>(R.id.parent_view),
                        getString(com.tokopedia.affiliate_toko.R.string.affiliate_internet_error),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_ERROR
                    ).show()
                }
                else -> {
                    showLoginPortal()
                }
            }
        })
    }

    private fun showFraudTicker() {
        var currentFragment =
            supportFragmentManager.findFragmentByTag(AffiliateLoginFragment::class.java.name)
        if (currentFragment == null) {
            showLoginPortal()
            currentFragment = supportFragmentManager.findFragmentByTag(AffiliateLoginFragment::class.java.name)
        }
        currentFragment?.let { fragment ->
            (fragment as? AffiliateLoginFragment)?.showFraudTicker()
        }

    }

    override fun onRegistrationSuccessful() {
        showSplashScreen()
    }

    var splashHandler: Handler? = null
    var isBackEnabled = true

    private val splashRunnable = Runnable {
        isBackEnabled = true
        findViewById<Group>(R.id.splash_group)?.hide()
        showAffiliatePortal()
    }

    private fun showSplashScreen() {
        clearBackStack()
        findViewById<Typography>(R.id.splash_title).text =
            getString(R.string.affiliate_hai_ana_selamat_bergabung_di_tokopedia_affiliate).replace(
                "{name}",
                userSessionInterface.name
            )
        findViewById<Group>(R.id.splash_group)?.show()
        isBackEnabled = false
        splashHandler = Handler(Looper.getMainLooper())
        splashHandler?.postDelayed(splashRunnable, AFFILIATE_SPLASH_TIME)
    }

    override fun onDestroy() {
        splashHandler?.removeCallbacks(splashRunnable)
        super.onDestroy()
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
            HOME_MENU -> eventAction = AffiliateAnalytics.ActionKeys.HOME_NAV_BAR_CLICK
            PROMO_MENU -> eventAction = AffiliateAnalytics.ActionKeys.PROMOSIKAN_NAV_BAR_CLICK
            HELP_MENU -> eventAction = AffiliateAnalytics.ActionKeys.BANUTAN_NAV_BAR_CLICK
            INCOME_MENU -> eventAction = AffiliateAnalytics.ActionKeys.PENDAPATAN_NAV_BAR_CLICK
        }
        AffiliateAnalytics.sendEvent(
            AffiliateAnalytics.EventKeys.CLICK_PG,
            eventAction,
            AffiliateAnalytics.CategoryKeys.AFFILIATE_HOME_PAGE,
            "",
            userSessionInterface.userId
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

    companion object MenuItems {
        var HOME_MENU = 0
        var PROMO_MENU = 1
        var INCOME_MENU = 2
        var HELP_MENU = 3
    }

    override fun selectItem(position: Int, id: Int, isNotFromBottom: Boolean) {
        affiliateBottomNavigation?.setSelected(position, isNotFromBottom)
    }

    override fun onBackPressed() {
        if(!isBackEnabled) return
        val currentFragment =
            supportFragmentManager.findFragmentByTag(AffiliatePromoFragment::class.java.name)
        if (currentFragment != null && currentFragment.isVisible) {
            (currentFragment as? AffiliatePromoFragment)?.handleBack()
        } else {
            handleBackButton(false)
        }
    }

    override fun handleBackButton(fromCoacher:Boolean) {
        if (!fromCoacher) {
            coachMark?.isDismissed = true
            coachMark?.dismiss()
        }
        if (!fragmentStack.empty()) {
            fragmentStack.pop()
            if (!fragmentStack.empty()) {
                handleBackStack()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun handleBackStack() {
        val ft = supportFragmentManager.beginTransaction()
        showSelectedFragment(
            fragmentStack.peek(),
            supportFragmentManager,
            ft
        )
        setBottomState(fragmentStack.peek())
        ft.commitNowAllowingStateLoss()
    }

    private fun setBottomState(peek: String?) {
        when (peek) {
            AffiliateHomeFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                HOME_MENU
            )
            AffiliatePromoFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                PROMO_MENU
            )
            AffiliateIncomeFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                INCOME_MENU
            )
            BaseSessionWebViewFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                HELP_MENU
            )
        }
    }

    fun setBlackListedStatus(blackListed: Boolean) {
        isUserBlackListed = blackListed
    }

    fun getBlackListedStatus(): Boolean {
        return isUserBlackListed
    }

    override fun navigateToTermsFragment(channels: ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>) {
        openFragment(AffiliateTermsAndConditionFragment.getFragmentInstance().apply {
            setChannels(channels)
        })
        val currentFragment =
            supportFragmentManager.findFragmentByTag(AffiliateTermsAndConditionFragment::class.java.name)
        if (currentFragment != null) {
            (currentFragment as? AffiliateTermsAndConditionFragment)?.setChannels(channels)
        }
    }

    override fun navigateToPortfolioFragment() {
        openFragment(AffiliatePortfolioFragment.getFragmentInstance())
    }

    override fun validateUserStatus() {
        userActionRequiredForRegister = true
        affiliateVM.getAffiliateValidateUser()
    }
}
