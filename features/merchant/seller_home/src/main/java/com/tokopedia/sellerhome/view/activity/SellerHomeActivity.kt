package com.tokopedia.sellerhome.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.common.ShopStatus
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_sah_seller_home.*
import javax.inject.Inject

class SellerHomeActivity : BaseActivity(), SellerHomeFragment.PageRefreshListener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel: SellerHomeActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeActivityViewModel::class.java)
    }

    private val sellerHomeRouter: SellerHomeRouter? by lazy {
        return@lazy if (applicationContext is SellerHomeRouter)
            (applicationContext as SellerHomeRouter)
        else
            null
    }
    private val sellerHomeFragment by lazy { SellerHomeFragment.newInstance() }
    private val somListFragment: Fragment? by lazy {
        sellerHomeRouter?.getSellerOrderManageFragment()
    }

    private var currentFragment: Fragment = sellerHomeFragment
    private val fragmentManger: FragmentManager by lazy { supportFragmentManager }

    private var hasAttachOrderFragment = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_seller_home)

        sellerHomeFragment.setOnPageRefreshedListener(this)

        initInjector()
        setupView()
        setupHomeFragment()
        UpdateCheckerHelper.checkAppUpdate(this)
        observeShopStatusLiveData()
    }

    private fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupView() {
        sahBottomNav.itemIconTintList = null
        sahBottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        sahBottomNav.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_sah_home -> showFragment(sellerHomeFragment, "Home")
                R.id.menu_sah_product -> showFragment(sellerHomeFragment, "Product")
                R.id.menu_sah_chat -> showFragment(sellerHomeFragment, "Chat")
                R.id.menu_sah_order -> somListFragment?.let {
                    if (!hasAttachOrderFragment) {
                        addFragment(it)
                        hasAttachOrderFragment = true
                    }
                    showFragment(it, "Order")
                }
                R.id.menu_sah_other -> showFragment(sellerHomeFragment, "Lainnya")
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setupHomeFragment() {
        addFragment(sellerHomeFragment)
        showFragment(sellerHomeFragment, "Home")
    }

    private fun <T : Fragment> addFragment(fragment: T) {
        fragmentManger.beginTransaction()
                .add(R.id.sahContainer, fragment, fragment.tag)
                .hide(fragment)
                .commit()
    }

    private fun showFragment(fragment: Fragment, title: String) {
        fragmentManger.beginTransaction()
                .hide(currentFragment)
                .show(fragment)
                .commit()
        currentFragment = fragment
        setToolbarTitle(title)
    }

    override fun onRefreshPage() {
        //update notif counter
    }

    private fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun observeShopStatusLiveData() {
        mViewModel.shopStatus.observe(this, Observer {
            if (it is Success)
                setOnSuccessGetShopStatus(it.data)
        })
        mViewModel.getShopStatus()
    }

    private fun setOnSuccessGetShopStatus(goldPmOsStatus: GetShopStatusResponse) {
        val shopStatus = goldPmOsStatus.result.data
        val mShopStatus: ShopStatus = when {
            shopStatus.isOfficialStore() -> ShopStatus.OFFICIAL_STORE
            shopStatus.isPowerMerchantActive() || shopStatus.isPowerMerchantIdle() -> ShopStatus.POWER_MERCHANT
            else -> ShopStatus.REGULAR_MERCHANT
        }

        sellerHomeFragment.setShopStatus(mShopStatus)
    }
}