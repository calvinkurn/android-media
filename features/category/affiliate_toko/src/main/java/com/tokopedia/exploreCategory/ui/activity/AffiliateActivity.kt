package com.tokopedia.exploreCategory.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.ui.bottomnav.BottomMenu
import com.tokopedia.exploreCategory.ui.bottomnav.IBottomClickListener
import com.tokopedia.exploreCategory.ui.bottomnav.LottieBottomNavbar
import com.tokopedia.exploreCategory.ui.fragment.AffiliateHomeFragment
import com.tokopedia.exploreCategory.viewmodel.AffiliateViewModel

class AffiliateActivity : BaseViewModelActivity<AffiliateViewModel>() , IBottomClickListener{

    private lateinit var affiliateVM: AffiliateViewModel

    var menu: ArrayList<BottomMenu> = ArrayList()
    private val isNewNavigation = false
    private var bottomNavigation: LottieBottomNavbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomNavigationView()
    }

    override fun getLayoutRes(): Int = R.layout.affiliate_layout

    override fun getViewModelType(): Class<AffiliateViewModel> {
        return AffiliateViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateViewModel
    }

    override fun getNewFragment(): Fragment? {
        return AffiliateHomeFragment.getFragmentInstance()
    }

    private fun initBottomNavigationView() {
        bottomNavigation = findViewById(R.id.bottom_navbar)
        populateBottomNavigationView()
        bottomNavigation?.setMenuClickListener(this)
    }

    private fun populateBottomNavigationView(){
        menu.add(BottomMenu(R.id.menu_home, resources.getString(R.string.home), R.raw.bottom_nav_home, R.raw.bottom_nav_home_to_enabled, R.drawable.ic_bottom_nav_home_active, R.drawable.ic_bottom_nav_home_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f))
        menu.add(BottomMenu(R.id.menu_feed, resources.getString(R.string.feed), R.raw.bottom_nav_feed, R.raw.bottom_nav_feed_to_enabled, R.drawable.ic_bottom_nav_feed_active, R.drawable.ic_bottom_nav_feed_enabled, com.tokopedia.unifyprinciples.R.color.Unify_G500, true, 1f, 3f))
        menu.add(BottomMenu(R.id.menu_os, resources.getString(R.string.official), R.raw.bottom_nav_official, R.raw.bottom_nav_os_to_enabled, R.drawable.ic_bottom_nav_os_active, R.drawable.ic_bottom_nav_os_enabled, com.tokopedia.unifyprinciples.R.color.Unify_P500, true, 1f, 3f))
        bottomNavigation?.setMenu(menu, isNewNavigation)
    }

    override fun menuClicked(position: Int, id: Int): Boolean {
        return true
    }

    override fun menuReselected(position: Int, id: Int) {

    }
}
