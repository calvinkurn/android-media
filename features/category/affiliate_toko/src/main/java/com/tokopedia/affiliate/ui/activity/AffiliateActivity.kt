package com.tokopedia.affiliate.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver.onFragmentSelected
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver.onFragmentUnSelected
import com.tokopedia.affiliate.AFFILIATE_HELP_URL
import com.tokopedia.affiliate.PAGE_SEGMENT_HELP
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavBarInterface
import com.tokopedia.affiliate.ui.custom.AffiliateBottomNavbar
import com.tokopedia.affiliate.ui.custom.IBottomClickListener
import com.tokopedia.affiliate.ui.fragment.AffiliateHelpFragment
import com.tokopedia.affiliate.ui.fragment.AffiliateHomeFragment
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoFragment
import com.tokopedia.affiliate.viewmodel.AffiliateViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.webview.BaseSessionWebViewFragment
import java.util.Stack


class AffiliateActivity : BaseViewModelActivity<AffiliateViewModel>(), IBottomClickListener,
    AffiliateBottomNavBarInterface {

    private lateinit var affiliateVM: AffiliateViewModel
    private var fragmentStack = Stack<String>()
    private var affiliateBottomNavigation: AffiliateBottomNavbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomNavigationView()
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

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateViewModel
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun initBottomNavigationView() {
        affiliateBottomNavigation = AffiliateBottomNavbar(
            findViewById(R.id.bottom_navbar),
            this, this
        ).apply {
            populateBottomNavigationView()
        }
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
        val currentFragment=supportFragmentManager.findFragmentByTag(AffiliatePromoFragment::class.java.simpleName)
        if(currentFragment != null && currentFragment.isVisible){
            (currentFragment as? AffiliatePromoFragment)?.handleBack()
        }
        else {
           handleBackButton()
        }
    }

    fun handleBackButton(){
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

    /*
    override fun onBackPressed() {
        val currentFragment=supportFragmentManager.findFragmentByTag(AffiliatePromoFragment::class.java.simpleName)
        if(currentFragment!=null && currentFragment.isVisible){
            (currentFragment as? AffiliatePromoFragment)?.handleBack()
        }
        else super.onBackPressed()
    }
     */
}
