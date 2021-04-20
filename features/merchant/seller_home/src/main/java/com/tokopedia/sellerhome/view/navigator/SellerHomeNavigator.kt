package com.tokopedia.sellerhome.view.navigator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.SomTabConst
import com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.user.session.UserSessionInterface

class SellerHomeNavigator(
    private val context: Context,
    private val fm: FragmentManager,
    private val sellerHomeRouter: SellerHomeRouter?,
    private val userSession: UserSessionInterface
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
        if(isActivityResumed()) {
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
        return when(pageType) {
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

    private fun setupPageFromAppLink(selectedPage: PageFragment?): Fragment? {
        return selectedPage?.let {
            val pageType = it.type
            val title = getPageTitle(pageType)
            val fragment = getPageFragment(pageType)
            pages.remove(fragment)

            val page = when (pageType) {
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
        homeFragment = SellerHomeFragment.newInstance()
        productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), "")
        chatFragment = sellerHomeRouter?.getChatListFragment()
        somListFragment = sellerHomeRouter?.getSomListFragment(SomTabConst.STATUS_ALL_ORDER, 0, "")
        otherSettingsFragment = OtherMenuFragment.createInstance()

        addPage(homeFragment, context.getString(R.string.sah_home))
        addPage(productManageFragment, context.getString(R.string.sah_product_list))
        addPage(chatFragment, context.getString(R.string.sah_chat))
        addPage(somListFragment, context.getString(R.string.sah_sale))
        addPage(otherSettingsFragment, context.getString(R.string.sah_sale))
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
            FragmentType.HOME -> homeFragment
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
        val searchKeyword = page.keywordSearch
        val filterOptionEmptyStock = FilterOption.FilterByCondition.EmptyStockOnly.id

        when {
            page.tabPage.isNotBlank() && page.tabPage == filterOptionEmptyStock -> {
                val filterOptions = arrayListOf(filterOptionEmptyStock)
                productManageFragment = sellerHomeRouter?.getProductManageFragment(filterOptions, searchKeyword)
            }
            page.tabPage.isBlank() && searchKeyword.isNotBlank() -> {
                productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), searchKeyword)
            }
        }

        return productManageFragment
    }

    private fun setupSellerOrderPage(page: PageFragment): Fragment? {
        somListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage, page.orderType, page.keywordSearch)
        return somListFragment
    }

    private fun addPage(fragment: Fragment?, title: String?) {
        fragment?.let { pages[it] = title }
    }

    private fun showOnlySelectedFragment(transaction: FragmentTransaction, fragment: Fragment? = null) {
        hideAllPages(transaction)
        fragment?.let {
            transaction.show(it)
        }
    }

    private fun hideAllPages(transaction: FragmentTransaction) {
        fm.fragments.forEach { transaction.hide(it) }
    }

    private fun setSelectedPage(@FragmentType page: Int) {
        currentSelectedPage = page
    }

    private fun isActivityResumed(): Boolean {
        val state = (context as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.RESUMED || state == Lifecycle.State.STARTED
    }

    private fun getHomeTitle(): String? {
        val shopName = userSession.shopName
        return if(shopName.isNullOrEmpty()) {
            pages[homeFragment]
        } else {
            shopName
        }
    }
}