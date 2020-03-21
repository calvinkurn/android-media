package com.tokopedia.sellerhome.view.fragment

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

    private val handler = Handler()
    private val homeFragment: SellerHomeFragment by lazy { SellerHomeFragment.newInstance() }
    private val productManageFragment: Fragment? by lazy { sellerHomeRouter?.getProductManageFragment() }
    private val chatFragment: Fragment? by lazy { sellerHomeRouter?.getChatListFragment() }
    private val somListFragment: Fragment? by lazy { sellerHomeRouter?.getSomListFragment(SomTabConst.STATUS_ALL_ORDER) }

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

        setupView()
        observeCurrentSelectedPage()
        setupDefaultPage()
    }

    private fun setupView() = view?.run {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(sahToolbar)
        }

        val statusBarHeight = StatusbarHelper.getStatusBarHeight(context)
        val layoutParams = statusBarBackground.layoutParams
        if (layoutParams is LinearLayout.LayoutParams) {
            layoutParams.height = statusBarHeight
            statusBarBackground.layoutParams = layoutParams
            statusBarBackground.requestLayout()
        }
    }

    private fun setupDefaultPage() {
        sharedViewModel?.setCurrentSelectedPage(PageFragment(FragmentType.HOME))
    }

    private fun observeCurrentSelectedPage() {
        sharedViewModel?.currentSelectedPage?.observe(this, Observer { page ->
            currentFragmentType = page.type
            when (page.type) {
                FragmentType.HOME -> showFragment(homeFragment, page, homeFragmentTitle)
                FragmentType.PRODUCT -> showFragment(productManageFragment, page, getString(R.string.sah_product_list))
                FragmentType.CHAT -> showFragment(chatFragment, page, getString(R.string.sah_chat))
                FragmentType.ORDER -> showFragment(somListFragment, page, getString(R.string.sah_sale))
            }
        })
    }

    private fun showFragment(fragment: Fragment?, page: PageFragment, title: String) {
        if (null == fragment) {
            return
        }

        handler.post {
            val fragmentName = fragment.javaClass.name
            val manager = childFragmentManager
            val transaction = manager.beginTransaction()
            val isFragmentAttached = null != manager.findFragmentByTag(fragmentName)

            if (isFragmentAttached && manager.fragments.isNotEmpty()) {
                manager.fragments.forEach { fmt ->
                    if (fragmentName == fmt.javaClass.name) {
                        if (page.type == FragmentType.ORDER) {
                            showSomPage(fmt, transaction, page, fragmentName)
                        } else {
                            transaction.show(fmt)
                        }
                    } else {
                        transaction.hide(fmt)
                    }
                }
            } else {
                transaction.add(R.id.sahFragmentContainer, fragment, fragmentName)
            }

            transaction.commitNowAllowingStateLoss()
            view?.sahToolbar?.title = title
        }
    }

    private fun showSomPage(fmt: Fragment, transaction: FragmentTransaction, page: PageFragment, fragmentName: String) {
        if (page.tabPage.isNotBlank() && SomTabConst.STATUS_ALL_ORDER != page.tabPage) {
            val mSomListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage)
            if (null != mSomListFragment) {
                transaction.remove(fmt)
                transaction.add(R.id.sahFragmentContainer, mSomListFragment, fragmentName)
                transaction.show(mSomListFragment)
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
}