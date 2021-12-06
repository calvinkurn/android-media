package com.tokopedia.affiliate.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.tokopedia.affiliate.AFFILIATE_HELP_URL
import com.tokopedia.affiliate.AFFILIATE_SPLASH_TIME
import com.tokopedia.affiliate.PAGE_SEGMENT_HELP
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AffiliateActivityInterface
import com.tokopedia.affiliate.model.request.OnBoardingRequest
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavbar
import com.tokopedia.affiliate.ui.custom.IBottomClickListener
import com.tokopedia.affiliate.ui.fragment.AffiliateHelpFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateHomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateLoginFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliatePortfolioFragment
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.viewmodel.AffiliateViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseSessionWebViewFragment
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject


class AffiliateActivity : BaseViewModelActivity<AffiliateViewModel>(), IBottomClickListener,
    AffiliateBottomNavBarInterface , AffiliateActivityInterface{

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateVM: AffiliateViewModel
    private var fragmentStack = Stack<String>()
    private var affiliateBottomNavigation: AffiliateBottomNavbar? = null
    private var userActionRequiredForRegister = false

    private var isUserBlackListed = false

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
                selectItem(HELP_MENU, R.id.menu_help_affiliate)
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
        initBottomNavigationView()
        setObservers()
        showAffiliatePortal()
//        if(userSessionInterface.isLoggedIn)
//            affiliateVM.getAffiliateValidateUser()
    }

    private fun showLoginPortal() {
        openFragment(AffiliateLoginFragment.getFragmentInstance(this))
    }

    fun showAffiliatePortal() {
        clearBackStack()
        findViewById<ImageUnify>(R.id.affiliate_background_image)?.show()
        affiliateBottomNavigation?.showBottomNav()
        affiliateBottomNavigation?.populateBottomNavigationView()
    }

    private fun clearBackStack() {
        for(frag in supportFragmentManager.fragments){
            supportFragmentManager.popBackStack()
        }
        fragmentStack.clear()
    }

    private fun initBottomNavigationView() {
        affiliateBottomNavigation = AffiliateBottomNavbar(
            findViewById(R.id.bottom_navbar),
            this, this
        )
    }

    override fun menuClicked(position: Int, id: Int): Boolean {
        when (position) {
            HOME_MENU -> openFragment(AffiliateHomeFragment.getFragmentInstance(this))
            PROMO_MENU -> openFragment(AffiliatePromoFragment.getFragmentInstance())
            HELP_MENU -> openFragment(AffiliateHelpFragment.getFragmentInstance(AFFILIATE_HELP_URL))
        }
        return true
    }

    override fun menuReselected(position: Int, id: Int) {

    }

    private fun setObservers() {
        affiliateVM.getValidateUserdata().observe(this, { validateUserdata ->
            if (validateUserdata.validateAffiliateUserStatus.data?.isRegistered == true) {
                showAffiliatePortal()
            }else if(validateUserdata.validateAffiliateUserStatus.data?.isEligible == false &&
                    validateUserdata.validateAffiliateUserStatus.data?.isRegistered == false){
                showFraudTicker()
            }else {
                if(!userActionRequiredForRegister)
                    navigateToPortfolioFragment()
            }
        })

        affiliateVM.getErrorMessage().observe(this , { error ->
            when(error) {
                is UnknownHostException, is SocketTimeoutException -> {
                    Toaster.build(findViewById<FrameLayout>(R.id.parent_view), getString(com.tokopedia.affiliate_toko.R.string.affiliate_internet_error),
                            Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                }
                else -> {
                    showLoginPortal()
                }
            }
        })
    }

    private fun showFraudTicker() {
        val currentFragment = supportFragmentManager.findFragmentByTag(AffiliateLoginFragment::class.java.simpleName)
        if(currentFragment != null){
            (currentFragment as? AffiliateLoginFragment)?.showFraudTicker()
        }
    }

    override fun onRegistrationSuccessful() {
        showSplashScreen()
    }

    private fun showSplashScreen() {
        findViewById<Group>(R.id.splash_group)?.show()
        Handler().postDelayed({
            findViewById<Group>(R.id.splash_group)?.hide()
            showAffiliatePortal()
        }, AFFILIATE_SPLASH_TIME)
    }

    private fun openFragment(fragment: Fragment) {
        val backStackName = fragment.javaClass.simpleName
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
        const val HOME_MENU = 0
        const val PROMO_MENU = 1
        const val HELP_MENU = 2
    }

    override fun selectItem(position: Int, id: Int) {
        affiliateBottomNavigation?.setSelected(position)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentByTag(AffiliatePromoFragment::class.java.simpleName)
        if(currentFragment != null && currentFragment.isVisible){
            (currentFragment as? AffiliatePromoFragment)?.handleBack()
        }
        else {
           handleBackButton()
        }
    }

    override fun handleBackButton(){
        if(!fragmentStack.empty()) {
            fragmentStack.pop()
            if (!fragmentStack.empty()) {
                handleBackStack()
            } else{
                super.onBackPressed()
            }
        }
        else{
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
            BaseSessionWebViewFragment::class.java.name -> affiliateBottomNavigation?.selectBottomTab(
                HELP_MENU
            )
        }
    }

    fun setBlackListedStatus(blackListed : Boolean) {
        isUserBlackListed = blackListed
    }

    fun getBlackListedStatus() : Boolean{
        return  isUserBlackListed
    }

    override fun navigateToTermsFragment(channels : ArrayList<OnBoardingRequest.Channel>) {
        openFragment(AffiliateTermsAndConditionFragment.getFragmentInstance(this).apply {
            setChannels(channels)
        })
        val currentFragment = supportFragmentManager.findFragmentByTag(AffiliateTermsAndConditionFragment::class.java.simpleName)
        if(currentFragment != null){
            (currentFragment as? AffiliateTermsAndConditionFragment)?.setChannels(channels)
        }
    }

    override fun navigateToPortfolioFragment() {
        openFragment(AffiliatePortfolioFragment.getFragmentInstance(this))
    }

    override fun validateUserStatus() {
        userActionRequiredForRegister = true
        affiliateVM.getAffiliateValidateUser()
    }
}
