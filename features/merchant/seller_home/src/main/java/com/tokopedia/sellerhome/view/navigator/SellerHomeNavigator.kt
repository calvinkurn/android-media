package com.tokopedia.sellerhome.view.navigator

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.seller_migration_common.listener.SellerHomeFragmentListener
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.SellerHomeConst
import com.tokopedia.sellerhome.common.SomTabConst
import com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.shop.common.util.sellerfeedbackutil.SellerFeedbackUtil
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SellerHomeNavigator(
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val fm: FragmentManager,
    private val sellerHomeRouter: SellerHomeRouter?,
    private val userSession: UserSessionInterface,
    private var navigationHomeMenu: View?
) {

    private var homeFragment: Fragment? = null
    private var productManageFragment: Fragment? = null
    private var chatFragment: Fragment? = null
    private var somListFragment: Fragment? = null
    private var otherSettingsFragment: Fragment? = null

    @FragmentType
    private var currentSelectedPage: Int? = null
    private val pages: MutableMap<Fragment?, String?> = mutableMapOf()

    init {
        initFragments()
    }

    fun start(@FragmentType page: Int) {
        val transaction = fm.beginTransaction()
        val fragment = getPageFragment(page)

        fragment?.let {
            showFragment(it, transaction)
            setSelectedPage(page)
        }

        updateFragmentVisibilityHint(fragment)
    }

    fun showPage(@FragmentType page: Int) {
        if (isActivityResumed()) {
            val transaction = fm.beginTransaction()
            val fragment = getPageFragment(page)

            fragment?.let {
                showFragment(it, transaction)
                setSelectedPage(page)
            }

            updateFragmentVisibilityHint(fragment)
        }
    }

    fun navigateFromAppLink(page: PageFragment) {
        val type = page.type

        getPageFragment(type)?.let { currentPage ->
            val fragment = setupPageFromAppLink(page)

            fragment?.let { selectedPage ->
                val transaction = fm.beginTransaction()
                val currentTag = currentPage::class.java.canonicalName
                val currentFragment = fm.findFragmentByTag(currentTag)

                if (currentFragment != null && currentFragment != selectedPage) {
                    transaction
                        .remove(currentFragment)
                        .add(R.id.sahContainer, selectedPage, currentTag)
                        .commitNowAllowingStateLoss()
                } else {
                    showFragment(selectedPage, transaction)
                }

                setSelectedPage(type)
                updateFragmentVisibilityHint(selectedPage)
            }
        }
    }

    fun getPageTitle(@FragmentType pageType: Int): String? {
        return when (pageType) {
            FragmentType.HOME -> getHomeTitle()
            FragmentType.PRODUCT -> pages[productManageFragment]
            FragmentType.CHAT -> pages[chatFragment]
            FragmentType.ORDER -> pages[somListFragment]
            else -> pages[otherSettingsFragment]
        }
    }

    fun cleanupNavigator() {
        val transaction = fm.beginTransaction()
        fm.fragments.forEach {
            if (it.isAdded) {
                transaction.remove(it)
            }
        }
        transaction.commitAllowingStateLoss()
    }

    fun navigateOnRestored(outState: Bundle, lifecycleScope: LifecycleCoroutineScope) {
        val page = outState.getInt(SellerHomeActivity.LAST_FRAGMENT_TYPE_KEY, FragmentType.HOME)
        lifecycleScope.launchWhenResumed {
            showPage(page)
        }
    }

    private fun setupPageFromAppLink(selectedPage: PageFragment?): Fragment? {
        return selectedPage?.let {
            val pageType = it.type
            val title = getPageTitle(pageType)
            val fragment = getPageFragment(pageType)
            pages.remove(fragment)

            val page = when (pageType) {
                FragmentType.HOME -> homeFragment
                FragmentType.PRODUCT -> setupProductManagePage(it)
                FragmentType.ORDER -> setupSellerOrderPage(it)
                else -> fragment
            }

            pages[page] = title
            page
        }
    }

    fun setHomeTitle(title: String) {
        homeFragment?.let {
            pages[it] = title
        }
    }

    private fun getHomeFragment(pageFragment: PageFragment? = null): Fragment? {
        return homeFragment
    }

    fun getHomeFragment(): SellerHomeFragment? {
        return homeFragment as? SellerHomeFragment
    }

    fun isHomePageSelected(): Boolean {
        return currentSelectedPage == FragmentType.HOME
    }

    fun getCurrentSelectedPage(): Int {
        return currentSelectedPage ?: FragmentType.HOME
    }

    private fun initFragments() {
        clearFragments()
        lifecycleScope.launch(Dispatchers.Default) {

            val homeFragmentAsync = async { SellerHomeFragment.newInstance() }
            val productManageFragmentAsync = async(Dispatchers.Main) {
                sellerHomeRouter?.getProductManageFragment(
                    arrayListOf(), String.EMPTY, String.EMPTY, navigationHomeMenu
                )
            }
            val chatFragmentAsync = async { sellerHomeRouter?.getChatListFragment() }
            val somListFragmentAsync = async(Dispatchers.Main) {
                sellerHomeRouter?.getSomListFragment(
                    "",
                    SomTabConst.DEFAULT_ORDER_TYPE_FILTER,
                    "",
                    ""
                )
            }
            val otherSettingsFragmentAsync = async { OtherMenuFragment.createInstance() }

            homeFragment = homeFragmentAsync.await()
            productManageFragment = productManageFragmentAsync.await()
            chatFragment = chatFragmentAsync.await()
            somListFragment = somListFragmentAsync.await()
            otherSettingsFragment = otherSettingsFragmentAsync.await()

            addPage(homeFragment, context.getString(R.string.sah_home))
            addPage(productManageFragment, context.getString(R.string.sah_product_list))
            addPage(chatFragment, context.getString(R.string.sah_chat))
            addPage(somListFragment, context.getString(R.string.sah_som_list))
            addPage(otherSettingsFragment, context.getString(R.string.sah_others))
        }
    }

    private fun clearFragments() {
        if (fm.fragments.isEmpty()) return
        val transaction = fm.beginTransaction()
        for (fragment in fm.fragments) {
            transaction.remove(fragment)
        }
        transaction.commitNowAllowingStateLoss()

        homeFragment = null
        productManageFragment = null
        chatFragment = null
        somListFragment = null
        otherSettingsFragment = null
    }

    private fun showFragment(fragment: Fragment, transaction: FragmentTransaction) {
        val tag = fragment::class.java.canonicalName
        val fragmentByTag = fm.findFragmentByTag(tag)

        if (fragmentByTag == null || fm.fragments.isEmpty()) {
            transaction.add(R.id.sahContainer, fragment, tag)
        }

        showOnlySelectedFragment(transaction, fragmentByTag)

        transaction.commitNowAllowingStateLoss()
    }

    private fun getPageFragment(@FragmentType type: Int): Fragment? {
        return when (type) {
            FragmentType.HOME -> homeFragment ?: SellerHomeFragment.newInstance()
            FragmentType.PRODUCT -> productManageFragment
            FragmentType.CHAT -> chatFragment
            FragmentType.ORDER -> somListFragment
            FragmentType.OTHER -> otherSettingsFragment
            else -> null
        }
    }

    @Suppress("DEPRECATION")
    private fun updateFragmentVisibilityHint(visibleFragment: Fragment?) {
        if (visibleFragment == null) {
            homeFragment?.userVisibleHint = false
            productManageFragment?.userVisibleHint = false
            chatFragment?.userVisibleHint = false
            somListFragment?.userVisibleHint = false
        } else {
            homeFragment?.userVisibleHint = visibleFragment == homeFragment
            productManageFragment?.userVisibleHint = visibleFragment == productManageFragment
            chatFragment?.userVisibleHint = visibleFragment == chatFragment
            somListFragment?.userVisibleHint = visibleFragment == somListFragment
        }
    }

    private fun setupProductManagePage(page: PageFragment): Fragment? {
        val filterOptions: ArrayList<String> = if (page.tabPage.isNotBlank()) {
            arrayListOf(page.tabPage)
        } else {
            arrayListOf()
        }

        productManageFragment = sellerHomeRouter?.getProductManageFragment(
            filterOptions,
            page.keywordSearch,
            page.productManageTab,
            navigationHomeMenu
        )

        return productManageFragment
    }

    private fun setupSellerOrderPage(page: PageFragment): Fragment? {
        somListFragment = sellerHomeRouter?.getSomListFragment(
            page.tabPage,
            page.orderType,
            page.keywordSearch,
            page.orderId
        )
        return somListFragment
    }

    private fun addPage(fragment: Fragment?, title: String?) {
        fragment?.let { pages[it] = title }
    }

    private fun showOnlySelectedFragment(
        transaction: FragmentTransaction,
        fragment: Fragment? = null
    ) {
        hideAllPages(transaction)
        fragment?.let {
            scrollFragmentToTop(it)
            transaction.show(it)
        }
    }

    private fun scrollFragmentToTop(fragment: Fragment) {
        if (fragment.isVisible) {
            (fragment as? SellerHomeFragmentListener)?.onScrollToTop()
        }
    }

    private fun hideAllPages(transaction: FragmentTransaction) {
        fm.fragments.forEach { transaction.hide(it) }
    }

    private fun setSelectedPage(@FragmentType page: Int) {
        currentSelectedPage = page
        setSelectedPageSellerFeedback()
    }

    fun setSelectedPageSellerFeedback(shouldNavigate: Boolean = false) {
        lifecycleScope.launch {
            val selectedPage = when (currentSelectedPage) {
                FragmentType.HOME -> SellerFeedbackUtil.SELLER_HOME_PAGE
                FragmentType.PRODUCT -> SellerFeedbackUtil.PRODUCT_MANAGE_PAGE
                FragmentType.ORDER -> SellerFeedbackUtil.SOM_PAGE
                FragmentType.CHAT -> SellerFeedbackUtil.CHAT_PAGE
                else -> SellerHomeConst.EMPTY_STRING
            }
            SellerFeedbackUtil(context.applicationContext)
                .setSelectedPage(selectedPage)
            if (shouldNavigate) {
                showPage(currentSelectedPage ?: FragmentType.HOME)
            }
        }
    }

    private fun isActivityResumed(): Boolean {
        val state = (context as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.RESUMED || state == Lifecycle.State.STARTED
    }

    private fun getHomeTitle(): String? {
        val shopName = userSession.shopName
        return if (shopName.isNullOrEmpty()) {
            pages[homeFragment]
        } else {
            shopName
        }
    }
}
