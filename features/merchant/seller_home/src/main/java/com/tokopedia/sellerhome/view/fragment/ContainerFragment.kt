package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.analytic.NavigationTracking
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
    private val homeFragment by lazy { SellerHomeFragment.newInstance() }
    private val productManageFragment: Fragment? by lazy {
        sellerHomeRouter?.getProductManageFragment()
    }
    private var chatFragment: Fragment = ChatFragmentTemp()
    private var somListFragment: Fragment? = null

    private var currentFragment: Fragment? = null
    private val fragmentManger: FragmentManager by lazy { childFragmentManager }

    @FragmentType
    private var currentFragmentType: Int = 0
    private var homeFragmentTitle = ""
    private var hasAttachHomeFragment = false
    private var hasAttachProductManagerFragment = false
    private var hasAttachChatFragment = false
    private var hasAttachSomFragment = false

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
        sahStatusBar?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusbarHelper.getStatusBarHeight(context))
    }

    private fun setupDefaultPage() {
        currentFragment = homeFragment
        sharedViewModel?.setCurrentSelectedPage(PageFragment(FragmentType.HOME))
    }

    private fun observeCurrentSelectedPage() {
        sharedViewModel?.currentSelectedPage?.observe(this, Observer { page ->
            currentFragmentType = page.type
            when (page.type) {
                FragmentType.HOME -> setupSellerHomeFragment()
                FragmentType.PRODUCT -> setupProductManagerFragment()
                FragmentType.CHAT -> setupChatFragment()
                FragmentType.ORDER -> setupSomListFragment(page)
            }
        })
    }

    private fun setupSellerHomeFragment() {
        if (!hasAttachHomeFragment) {
            addFragment(homeFragment)
            hasAttachHomeFragment = true
        }
        showFragment(homeFragment, homeFragmentTitle)

        view?.sahToolbar?.showNotificationActionMenu {
            RouteManager.route(requireContext(), ApplinkConst.SELLER_INFO)
            NavigationTracking.sendClickNotificationEvent()
        }
    }

    private fun setupProductManagerFragment() {
        productManageFragment?.let { fragment ->
            if (!hasAttachProductManagerFragment) {
                addFragment(fragment)
                hasAttachProductManagerFragment = true
            }
            showFragment(fragment, getString(R.string.sah_product_list))
        }

        view?.sahToolbar?.showAddProductActionMenu {
            RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM)
        }
    }

    private fun setupChatFragment() {
        if (!hasAttachChatFragment) {
            addFragment(chatFragment)
            hasAttachChatFragment = true
        }
        showFragment(chatFragment, getString(R.string.sah_chat))

        view?.sahToolbar?.showChatSettingsActionMenu {
            RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CHAT_SETTING_TEMPLATE)
        }
    }

    private fun setupSomListFragment(page: PageFragment) {
        if (null == somListFragment || (page.tabPage.isNotBlank() && SomTabConst.STATUS_ALL_ORDER != page.tabPage)) {
            somListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage)
            hasAttachSomFragment = false
        }

        somListFragment?.let {
            if (!hasAttachSomFragment) {
                addFragment(it)
                hasAttachSomFragment = true
            }
            showFragment(it, getString(R.string.sah_sale))
        }

        view?.sahToolbar?.hideAllActionMenu()
    }

    private fun <T : Fragment> addFragment(fragment: T) {
        fragmentManger.beginTransaction()
                .add(R.id.sahFragmentContainer, fragment, fragment.tag)
                .hide(fragment)
                .commit()
    }

    private fun showFragment(fragment: Fragment, title: String) {
        currentFragment?.let {
            fragmentManger.beginTransaction()
                    .hide(it)
                    .show(fragment)
                    .commit()
            currentFragment = fragment
        }
        view?.sahToolbar?.title = title
    }

    fun showNotifCenterBadge(chat: NotificationCenterUnreadUiModel) {
        if (chat.notifUnreadInt > 0)
            view?.sahToolbar?.showBadge()
        else
            view?.sahToolbar?.removeBadge()
    }

    fun showShopName(shopName: String) {
        homeFragmentTitle = if (shopName.isBlank()) homeFragmentTitle else shopName
        if (currentFragmentType == FragmentType.HOME)
            view?.sahToolbar?.title = homeFragmentTitle
    }
}