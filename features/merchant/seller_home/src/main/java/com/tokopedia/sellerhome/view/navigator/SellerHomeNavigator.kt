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

class SellerHomeNavigator(
    private val context: Context,
    private val sellerHomeRouter: SellerHomeRouter?
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

    fun start(@FragmentType page: Int,
              fm: FragmentManager) {
        val transaction = fm.beginTransaction()
        val fragment = getPageFragment(page)
        fm.fragments.forEach {
            transaction.remove(it)
        }
        addAllPages(fragment, transaction)

        fragment?.let {
            showFragment(it, fm, transaction, true)
            setSelectedPage(page)
        }

        updateFragmentVisibilityHint(fragment)
    }

    fun showPage(@FragmentType page: Int,
                 fm: FragmentManager) {
        if(isActivityResumed()) {
            val transaction = fm.beginTransaction()
            val fragment = getPageFragment(page)

            fragment?.let {
                hideCurrentPage(transaction)
                showFragment(it, fm, transaction)
                setSelectedPage(page)
            }

            updateFragmentVisibilityHint(fragment)
        }
    }

    fun navigateFromAppLink(page: PageFragment,
                            fm: FragmentManager) {
        val type = page.type

        getPageFragment(type)?.let { currentPage ->
            val fragment = setupPageFromAppLink(page)

            fragment?.let { selectedPage ->
                val tag = page::class.java.simpleName
                val transaction = fm.beginTransaction()
                val fragments = fm.fragments

                when {
                    fragments.isEmpty() -> {
                        addAllPages(selectedPage, transaction)
                        showFragment(selectedPage, fm, transaction, true)
                    }
                    currentPage != selectedPage -> {
                        transaction
                            .remove(currentPage)
                            .add(R.id.sahContainer, selectedPage, tag)
                            .commit()
                    }
                    else -> {
                        hideCurrentPage(transaction)
                        showFragment(fragment, fm, transaction)
                    }
                }

                setSelectedPage(type)
                updateFragmentVisibilityHint(selectedPage)
            }
        }
    }

    fun getPageTitle(@FragmentType pageType: Int): String? {
        return when(pageType) {
            FragmentType.HOME -> pages[homeFragment]
            FragmentType.PRODUCT -> pages[productManageFragment]
            FragmentType.CHAT -> pages[chatFragment]
            FragmentType.ORDER -> pages[somListFragment]
            else -> pages[otherSettingsFragment]
        }
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

    fun getCurrentSelectedPage() = currentSelectedPage ?: FragmentType.HOME

    private fun initFragments() {
        homeFragment = SellerHomeFragment.newInstance()
        productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), "")
        chatFragment = sellerHomeRouter?.getChatListFragment()
        somListFragment = sellerHomeRouter?.getSomListFragment(SomTabConst.STATUS_NEW_ORDER)
        otherSettingsFragment = OtherMenuFragment.createInstance()

        addPage(homeFragment, context.getString(R.string.sah_home))
        addPage(productManageFragment, context.getString(R.string.sah_product_list))
        addPage(chatFragment, context.getString(R.string.sah_chat))
        addPage(somListFragment, context.getString(R.string.sah_sale))
        addPage(otherSettingsFragment, context.getString(R.string.sah_sale))
    }

    private fun addAllPages(selectedPage: Fragment?, transaction: FragmentTransaction) {
        pages.keys.forEach {
            it?.let {
                val tag = it::class.java.simpleName
                transaction.add(R.id.sahContainer, it, tag)

                if(it != selectedPage) {
                    transaction.setMaxLifecycle(it, Lifecycle.State.CREATED)
                }

                transaction.hide(it)
            }
        }
    }

    private fun showFragment(fragment: Fragment,
                             fm: FragmentManager,
                             transaction: FragmentTransaction,
                             isFromStart: Boolean = false) {
        val currentState = fragment.lifecycle.currentState
        val isFragmentNotResumed = !currentState.isAtLeast(Lifecycle.State.RESUMED)

        if(isFragmentNotResumed && fragment.fragmentManager == fm) {
            try {
                transaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        transaction.showDesignatedFragment(fragment, fm, isFromStart)
    }

    private fun FragmentTransaction.showDesignatedFragment(fragment: Fragment, fm: FragmentManager, isFromStart: Boolean) {
        if (isFromStart) {
            show(fragment)
        } else {
            if (fragment.isAdded) {
                show(fragment)
            } else {
                add(R.id.sahContainer, fragment)
            }
        }

        if (!isFromStart) {
            fm.fragments.forEach {
                if (it != fragment && it.isAdded) {
                    hide(it)
                }
            }
        }

        commit()
    }

    private fun getPageFragment(@FragmentType type: Int): Fragment? {
        return when (type) {
            FragmentType.HOME -> homeFragment
            FragmentType.PRODUCT -> productManageFragment
            FragmentType.CHAT -> chatFragment
            FragmentType.ORDER -> somListFragment
            else -> otherSettingsFragment
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
        somListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage)
        return somListFragment
    }

    private fun addPage(fragment: Fragment?, title: String?) {
        fragment?.let { pages[it] = title }
    }

    private fun hideCurrentPage(transaction: FragmentTransaction) {
        currentSelectedPage?.let {
            getPageFragment(it)?.let { currentPage ->
                transaction.hide(currentPage)
            }
        }
    }

    private fun setSelectedPage(@FragmentType page: Int) {
        currentSelectedPage = page
    }

    private fun isActivityResumed(): Boolean {
        val state = (context as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.RESUMED || state == Lifecycle.State.STARTED
    }
}