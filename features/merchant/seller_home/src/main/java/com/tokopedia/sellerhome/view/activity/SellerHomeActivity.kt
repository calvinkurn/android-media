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
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.fragment.ContainerFragment
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.sellerhome.view.viewmodel.SharedViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_sah_seller_home.*
import javax.inject.Inject

class SellerHomeActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val homeViewModel: SellerHomeActivityViewModel by lazy {
        viewModelProvider.get(SellerHomeActivityViewModel::class.java)
    }
    private val sharedViewModel by lazy {
        viewModelProvider.get(SharedViewModel::class.java)
    }
    private val containerFragment by lazy { ContainerFragment.newInstance() }

    private var currentFragment: Fragment? = null
    private val fragmentManger: FragmentManager by lazy { supportFragmentManager }

    private var homeTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_seller_home)

        homeTitle = getString(R.string.sah_home)

        initInjector()
        setupView()
        setupDefaultFragment()
        UpdateCheckerHelper.checkAppUpdate(this)
        observeNotificationsLiveData()
        observeShopInfoLiveData()
        observeCurrentSelectedPageLiveData()
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

        fun showFragment(@FragmentType type: Int, title: String) {
            if (sahBottomNav.currentItem != type) {
                sharedViewModel.setCurrentSelectedMenu(type)
                sharedViewModel.setToolbarTitle(title)
            }
        }

        sahBottomNav.itemIconTintList = null
        sahBottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        sahBottomNav.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_sah_home -> showFragment(FragmentType.HOME, homeTitle)
                R.id.menu_sah_product -> {
                    val title = getString(R.string.sah_product)
                    showFragment(FragmentType.PRODUCT, title)
                }
                R.id.menu_sah_chat -> {
                    val title = getString(R.string.sah_chat)
                    showFragment(FragmentType.CHAT, title)
                }
                R.id.menu_sah_order -> {
                    val title = getString(R.string.sah_sale)
                    showFragment(FragmentType.ORDER, title)
                }
                R.id.menu_sah_other -> {
                    //show other fragment
                    //sharedViewModel.setCurrentSelectedMenu(type)
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

    private fun observeCurrentSelectedPageLiveData() {
        sharedViewModel.currentSelectedMenu.observe(this, Observer {
            sahBottomNav.currentItem = it
        })
    }

    private fun observeNotificationsLiveData() {
        homeViewModel.notifications.observe(this, Observer {
            if (it is Success) {
                containerFragment.showChatNotificationBadge(it.data.chat)
                showOrderNotificationCounter(it.data.sellerOrderStatus)
            }
        })
    }

    private fun observeShopInfoLiveData() {
        homeViewModel.shopInfo.observe(this, Observer {
            if (it is Success) {
                val shopName = it.data.shopName
                homeTitle = if (shopName.isBlank()) homeTitle else shopName
                println("shop info : ${it.data}")
            }
        })
        homeViewModel.getShopInfo()
    }

    private fun showOrderNotificationCounter(orderStatus: NotificationSellerOrderStatusUiModel) {
        sahBottomNav.setNotification(orderStatus.newOrder.plus(orderStatus.readyToShip), FragmentType.ORDER)
    }
}