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
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.fragment.ContainerFragment
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_sah_seller_home.*
import javax.inject.Inject

class SellerHomeActivity : BaseActivity() {

    companion object {
        private const val MENU_ORDER_POSITION = 2

        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val homeViewModel: SellerHomeActivityViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeActivityViewModel::class.java)
    }

    private val containerFragment by lazy { ContainerFragment.newInstance() }

    private var currentFragment: Fragment? = null
    private val fragmentManger: FragmentManager by lazy { supportFragmentManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_seller_home)

        initInjector()
        setupView()
        setupDefaultFragment()
        UpdateCheckerHelper.checkAppUpdate(this)
        observeNotificationsLiveData()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getNotifications()
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
                R.id.menu_sah_home -> {
                    val title = getString(R.string.sah_home)
                    containerFragment.showFragment(ContainerFragment.FRAGMENT_HOME, title)
                }
                R.id.menu_sah_product -> {
                    val title = getString(R.string.sah_product)
                    containerFragment.showFragment(ContainerFragment.FRAGMENT_PRODUCT, title)
                }
                //R.id.menu_sah_chat -> showFragment(sellerHomeFragment)
                R.id.menu_sah_order -> {
                    val title = getString(R.string.sah_sale)
                    containerFragment.showFragment(ContainerFragment.FRAGMENT_ORDER, title)
                }
                R.id.menu_sah_other -> {
                    val title = getString(R.string.sah_others)
                    containerFragment.showFragment(ContainerFragment.FRAGMENT_OTHER, title)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setupDefaultFragment() {
        addFragment(containerFragment)
        currentFragment = containerFragment
        showFragment(containerFragment)
    }

    private fun <T : Fragment> addFragment(fragment: T) {
        fragmentManger.beginTransaction()
                .add(R.id.sahContainer, fragment, fragment.tag)
                .hide(fragment)
                .commit()
    }

    private fun showFragment(fragment: Fragment) {
        currentFragment?.let {
            fragmentManger.beginTransaction()
                    .hide(it)
                    .show(fragment)
                    .commit()
            currentFragment = fragment
        }
    }

    private fun observeNotificationsLiveData() {
        homeViewModel.notifications.observe(this, Observer {
            if (it is Success) {
                showOrderNotificationCounter(it.data.sellerOrderStatus)
            }
        })
    }

    private fun showOrderNotificationCounter(orderStatus: NotificationSellerOrderStatusUiModel) {
        sahBottomNav.setNotification(orderStatus.newOrder.plus(10), MENU_ORDER_POSITION)
    }
}