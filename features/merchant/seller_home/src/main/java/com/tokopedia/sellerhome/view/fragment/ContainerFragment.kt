package com.tokopedia.sellerhome.view.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.SomTabConst
import com.tokopedia.sellerhome.common.StatusbarHelper
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.model.NotificationCenterUnreadUiModel
import com.tokopedia.sellerhome.view.viewmodel.SharedViewModel
import com.tokopedia.sellerhome.view.widget.toolbar.NotificationDotBadge
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import kotlinx.android.synthetic.main.fragment_sah_container.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class ContainerFragment : Fragment() {

    companion object {
        fun newInstance(): ContainerFragment {
            return ContainerFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val sharedViewModel: SharedViewModel? by lazy {
        return@lazy if (null != activity) {
            ViewModelProvider(activity!!, viewModelFactory).get(SharedViewModel::class.java)
        } else
            null
    }
    private val sellerHomeRouter: SellerHomeRouter? by lazy {
        val applicationContext = activity?.applicationContext
        return@lazy if (applicationContext is SellerHomeRouter)
            applicationContext
        else
            null
    }

    private var sellerHomeListener: SellerHomeFragment.Listener? = null
    private val handler = Handler()
    private val homeFragment: SellerHomeFragment by lazy { SellerHomeFragment.newInstance() }
    private val productManageFragment: Fragment? by lazy { sellerHomeRouter?.getProductManageFragment(arrayListOf(), "") }
    private val chatFragment: Fragment? by lazy { sellerHomeRouter?.getChatListFragment() }
    private val somListFragment: Fragment? by lazy { sellerHomeRouter?.getSomListFragment(SomTabConst.STATUS_NEW_ORDER) }

    @FragmentType
    private var currentFragmentType: Int = 0
    private var homeFragmentTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sah_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeFragmentTitle = context?.getString(R.string.sah_home).orEmpty()
        homeFragment.bindListener(sellerHomeListener)

        setupView()
        observeCurrentSelectedPage()
    }

    private fun setupView() = view?.run {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(sahToolbar)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val statusBarHeight = StatusbarHelper.getStatusBarHeight(context)
            val layoutParams = statusBarBackground?.layoutParams
            layoutParams?.let {
                if (it is LinearLayout.LayoutParams) {
                    it.height = statusBarHeight
                    statusBarBackground?.layoutParams = it
                    statusBarBackground?.requestLayout()
                }
            }
        }
    }

    private fun observeCurrentSelectedPage() {
        sharedViewModel?.currentSelectedPage?.observe(this, Observer { page ->
            currentFragmentType = page.type
            when (page.type) {
                FragmentType.HOME -> showFragment(homeFragment, page, homeFragmentTitle)
                FragmentType.PRODUCT -> showFragment(productManageFragment, page, getString(R.string.sah_product_list))
                FragmentType.CHAT -> showFragment(chatFragment, page, getString(R.string.sah_chat))
                FragmentType.ORDER -> showFragment(somListFragment, page, getString(R.string.sah_sale))
                else -> updateFragmentVisibilityHint(null)
            }
        })
    }

    private fun showFragment(fragment: Fragment?, page: PageFragment, title: String) {
        handler.post {
            if (null == fragment || !isAdded) {
                return@post
            }

            val fragmentName = fragment.javaClass.name
            val manager = childFragmentManager
            val transaction = manager.beginTransaction()
            val isFragmentAttached = null != manager.findFragmentByTag(fragmentName)

            if (isFragmentAttached && manager.fragments.isNotEmpty()) {
                manager.fragments.forEach { fmt ->
                    if (fragmentName == fmt.javaClass.name) {
                        when (page.type) {
                            FragmentType.PRODUCT -> showProductMangePage(fmt, transaction, page, fragmentName)
                            FragmentType.ORDER -> showSomPage(fmt, transaction, page, fragmentName)
                            else -> transaction.show(fmt)
                        }
                    } else {
                        transaction.hide(fmt)
                    }
                }
            } else {
                manager.fragments.forEach {
                    transaction.hide(it)
                }
                when (page.type) {
                    FragmentType.PRODUCT -> addProductFragment(fragment, transaction, page, fragmentName)
                    FragmentType.ORDER -> addSomFragment(fragment, transaction, page, fragmentName)
                    else -> addFragmentToTransaction(transaction, fragment, fragmentName)
                }
            }

            transaction.commitNowAllowingStateLoss()

            view?.sahToolbar?.title = title
            if (fragment == homeFragment) {
                context?.let {
                    val menuItem = view?.sahToolbar?.menu?.findItem(SellerHomeFragment.NOTIFICATION_MENU_ID) ?: return@let
                    NotificationDotBadge(it).showBadge(menuItem)
                }
            }
            updateFragmentVisibilityHint(fragment)
        }
    }

    @Suppress("DEPRECATION")
    private fun updateFragmentVisibilityHint(visibleFragment: Fragment?) {
        if (visibleFragment == null) {
            homeFragment.userVisibleHint = false
            productManageFragment?.userVisibleHint = false
            chatFragment?.userVisibleHint = false
            somListFragment?.userVisibleHint = false
        } else {
            homeFragment.userVisibleHint = visibleFragment == homeFragment
            productManageFragment?.userVisibleHint = visibleFragment == productManageFragment
            chatFragment?.userVisibleHint = visibleFragment == chatFragment
            somListFragment?.userVisibleHint = visibleFragment == somListFragment
        }
    }

    private fun addFragmentToTransaction(transaction: FragmentTransaction, fragment: Fragment, fragmentName: String) {
        transaction.add(R.id.sahFragmentContainer, fragment, fragmentName).show(fragment)
    }

    private fun addProductFragment(fragment: Fragment, transaction: FragmentTransaction, page: PageFragment, fragmentName: String) {
        val filterOptionEmptyStock = FilterOption.FilterByCondition.EmptyStockOnly.id
        val searchKeyword = page.keywordSearch
        if (page.tabPage.isNotBlank() && page.tabPage == filterOptionEmptyStock) {
            val productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(filterOptionEmptyStock), searchKeyword)
            if (null != productManageFragment) {
                addFragmentToTransaction(transaction, productManageFragment, fragmentName)
            } else {
                addFragmentToTransaction(transaction, fragment, fragmentName)
            }
        } else if(page.tabPage.isBlank() && searchKeyword.isNotBlank()) {
            val productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), searchKeyword)
            if (null != productManageFragment) {
                transaction.remove(fragment)
                addFragmentToTransaction(transaction, productManageFragment, fragmentName)
            } else {
                transaction.show(fragment)
            }
        } else if (page.needToRecreate) {
            val productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), "")
            if (null != productManageFragment) {
                transaction.remove(fragment)
                addFragmentToTransaction(transaction, productManageFragment, fragmentName)
            } else {
                transaction.show(fragment)
            }
        } else {
            addFragmentToTransaction(transaction, fragment, fragmentName)
        }
    }

    private fun addSomFragment(fragment: Fragment, transaction: FragmentTransaction, page: PageFragment, fragmentName: String) {
        if (page.tabPage.isNotBlank() && SomTabConst.STATUS_ALL_ORDER != page.tabPage) {
            val mSomListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage)
            if (null != mSomListFragment) {
                addFragmentToTransaction(transaction, mSomListFragment, fragmentName)
            } else {
                addFragmentToTransaction(transaction, fragment, fragmentName)
            }
        } else {
            addFragmentToTransaction(transaction, fragment, fragmentName)
        }
    }

    private fun showProductMangePage(fmt: Fragment, transaction: FragmentTransaction, page: PageFragment, fragmentName: String) {
        val filterOptionEmptyStock = FilterOption.FilterByCondition.EmptyStockOnly.id
        val searchKeyword = page.keywordSearch
        if (page.tabPage.isNotBlank() && page.tabPage == filterOptionEmptyStock) {
            val productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(filterOptionEmptyStock), searchKeyword)
            if (null != productManageFragment) {
                transaction.remove(fmt)
                addFragmentToTransaction(transaction, productManageFragment, fragmentName)
            } else {
                transaction.show(fmt)
            }
        } else if(page.tabPage.isBlank() && searchKeyword.isNotBlank()) {
            val productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), searchKeyword)
            if (null != productManageFragment) {
                transaction.remove(fmt)
                addFragmentToTransaction(transaction, productManageFragment, fragmentName)
            } else {
                transaction.show(fmt)
            }
        } else if (page.needToRecreate) {
            val productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), "")
            if (null != productManageFragment) {
                transaction.remove(fmt)
                addFragmentToTransaction(transaction, productManageFragment, fragmentName)
            } else {
                transaction.show(fmt)
            }
        } else {
            transaction.show(fmt)
        }
    }

    private fun showSomPage(fmt: Fragment, transaction: FragmentTransaction, page: PageFragment, fragmentName: String) {
        if (page.tabPage.isNotBlank() && SomTabConst.STATUS_ALL_ORDER != page.tabPage) {
            val mSomListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage)
            if (null != mSomListFragment) {
                transaction.remove(fmt)
                addFragmentToTransaction(transaction, mSomListFragment, fragmentName)
            } else {
                transaction.show(fmt)
            }
        } else {
            transaction.show(fmt)
        }
    }

    fun showNotifCenterBadge(notif: NotificationCenterUnreadUiModel) {
        homeFragment.setNotifCenterCounter(notif.notifUnreadInt)
    }

    fun showShopName(shopName: String) {
        homeFragmentTitle = if (shopName.isBlank()) homeFragmentTitle else shopName
        if (currentFragmentType == FragmentType.HOME)
            view?.sahToolbar?.title = homeFragmentTitle
    }

    fun setSellerHomeListener(listener: SellerHomeFragment.Listener) {
        this.sellerHomeListener = listener
    }
}