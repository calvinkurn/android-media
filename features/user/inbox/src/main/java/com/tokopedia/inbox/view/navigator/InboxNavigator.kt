package com.tokopedia.inbox.view.navigator

import android.content.Context
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inboxcommon.InboxCommonFragment
import com.tokopedia.inboxcommon.RoleType

class InboxNavigator constructor(
        private val context: Context,
        @IdRes
        private val containerId: Int,
        private val fm: FragmentManager,
        private val fragmentFactory: InboxFragmentFactory
) {
    private var notificationFragment: Fragment? = null
    private var chatFragment: Fragment? = null

    //    private var discussionFragment: Fragment? = null
    @InboxFragmentType
    private var currentSelectedPage: Int = InboxFragmentType.NONE
    private val pages: MutableMap<Fragment?, String?> = mutableMapOf()

    init {
        initFragments()
    }

    private fun initFragments() {
        chatFragment = fragmentFactory.createChatListFragment()
        notificationFragment = fragmentFactory.createNotificationFragment()

        addPage(notificationFragment, context.getString(R.string.inbox_title_notification))
        addPage(chatFragment, context.getString(R.string.inbox_title_chat))
//        addPage(somListFragment, context.getString(R.string.sah_sale))
    }

    fun start(@InboxFragmentType page: Int) {
        val transaction = fm.beginTransaction()
        val fragment = getPageFragment(page)
        if (fm.fragments.isEmpty()) {
            addAllPages(fragment, transaction)
        }

        fragment?.let {
            showFragment(it, transaction)
            setSelectedPage(page)
        }

//        updateFragmentVisibilityHint(fragment)
    }

    fun showPage(@InboxFragmentType page: Int) {
        if (isActivityResumed()) {
            val transaction = fm.beginTransaction()
            val fragment = getPageFragment(page)
            fragment?.let {
                showFragment(it, transaction)
                setSelectedPage(page)
            }
//            updateFragmentVisibilityHint(fragment)
        }
    }

    fun notifyRoleChanged(@RoleType role: Int) {
        pages.keys.forEach {
            if (it is InboxCommonFragment) {
                it.onRoleChanged(role)
            }
        }
    }

    //
//    fun navigateFromAppLink(page: PageFragment) {
//        val type = page.type
//
//        getPageFragment(type)?.let { currentPage ->
//            val fragment = setupPageFromAppLink(page)
//
//            fragment?.let { selectedPage ->
//                val tag = page::class.java.canonicalName
//                val transaction = fm.beginTransaction()
//                val fragments = fm.fragments
//
//                when {
//                    fragments.isEmpty() -> {
//                        addAllPages(selectedPage, transaction)
//                        showFragment(selectedPage, transaction)
//                    }
//                    currentPage != selectedPage -> {
//                        hideAllPages(transaction)
//
//                        transaction
//                                .remove(currentPage)
//                                .add(R.id.sahContainer, selectedPage, tag)
//                                .commit()
//                    }
//                    else -> showFragment(fragment, transaction)
//                }
//
//                setSelectedPage(type)
//                updateFragmentVisibilityHint(selectedPage)
//            }
//        }
//    }
//
//    fun getPageTitle(@InboxFragmentType pageType: Int): String? {
//        return when (pageType) {
//            FragmentType.HOME -> pages[homeFragment]
//            FragmentType.PRODUCT -> pages[productManageFragment]
//            FragmentType.CHAT -> pages[chatFragment]
//            FragmentType.ORDER -> pages[somListFragment]
//            else -> pages[otherSettingsFragment]
//        }
//    }
//
//    fun cleanupNavigator() {
//        val transaction = fm.beginTransaction()
//        fm.fragments.forEach {
//            if (it.isAdded) {
//                transaction.remove(it)
//            }
//        }
//        transaction.commitAllowingStateLoss()
//    }
//
//    private fun setupPageFromAppLink(selectedPage: PageFragment?): Fragment? {
//        return selectedPage?.let {
//            val pageType = it.type
//            val title = getPageTitle(pageType)
//            val fragment = getPageFragment(pageType)
//            pages.remove(fragment)
//
//            val page = when (pageType) {
//                FragmentType.PRODUCT -> setupProductManagePage(it)
//                FragmentType.ORDER -> setupSellerOrderPage(it)
//                else -> fragment
//            }
//
//            pages[page] = title
//            page
//        }
//    }
//
//    fun setHomeTitle(title: String) {
//        homeFragment?.let {
//            pages[it] = title
//        }
//    }
//
//    fun getHomeFragment(): SellerHomeFragment? {
//        return homeFragment as? SellerHomeFragment
//    }
//
//    fun isHomePageSelected(): Boolean {
//        return currentSelectedPage == FragmentType.HOME
//    }
//
//    fun getCurrentSelectedPage(): Int {
//        return currentSelectedPage ?: FragmentType.HOME
//    }

    private fun addAllPages(selectedPage: Fragment?, transaction: FragmentTransaction) {
        pages.keys.forEach {
            it?.let {
                val tag = it::class.java.canonicalName
                transaction.add(containerId, it, tag)

                if (it != selectedPage) {
                    transaction.setMaxLifecycle(it, Lifecycle.State.CREATED)
                }
            }
        }
    }

    private fun showFragment(fragment: Fragment, transaction: FragmentTransaction) {
        val tag = fragment::class.java.canonicalName
        val fragmentByTag = fm.findFragmentByTag(tag)
        val selectedFragment = fragmentByTag ?: fragment
        val currentState = selectedFragment.lifecycle.currentState
        val isFragmentNotResumed = !currentState.isAtLeast(Lifecycle.State.RESUMED)

        if (isFragmentNotResumed) {
            try {
                transaction.setMaxLifecycle(selectedFragment, Lifecycle.State.RESUMED)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        hideAllPages(transaction)

        transaction
                .show(selectedFragment)
                .commit()
    }

    private fun getPageFragment(@InboxFragmentType type: Int): Fragment? {
        return when (type) {
            InboxFragmentType.NOTIFICATION -> notificationFragment
            InboxFragmentType.CHAT -> chatFragment
//            InboxFragmentType.DISCUSSION -> productManageFragment
            else -> null
        }
    }

//    }

    //
//    @Suppress("DEPRECATION")
//    private fun updateFragmentVisibilityHint(visibleFragment: Fragment?) {
//        if (visibleFragment == null) {
//            homeFragment?.userVisibleHint = false
//            productManageFragment?.userVisibleHint = false
//            chatFragment?.userVisibleHint = false
//            somListFragment?.userVisibleHint = false
//        } else {
//            homeFragment?.userVisibleHint = visibleFragment == homeFragment
//            productManageFragment?.userVisibleHint = visibleFragment == productManageFragment
//            chatFragment?.userVisibleHint = visibleFragment == chatFragment
//            somListFragment?.userVisibleHint = visibleFragment == somListFragment
//        }
//    }
//
//    private fun setupProductManagePage(page: PageFragment): Fragment? {
//        val searchKeyword = page.keywordSearch
//        val filterOptionEmptyStock = FilterOption.FilterByCondition.EmptyStockOnly.id
//
//        when {
//            page.tabPage.isNotBlank() && page.tabPage == filterOptionEmptyStock -> {
//                val filterOptions = arrayListOf(filterOptionEmptyStock)
//                productManageFragment = sellerHomeRouter?.getProductManageFragment(filterOptions, searchKeyword)
//            }
//            page.tabPage.isBlank() && searchKeyword.isNotBlank() -> {
//                productManageFragment = sellerHomeRouter?.getProductManageFragment(arrayListOf(), searchKeyword)
//            }
//        }
//
//        return productManageFragment
//    }
//
//    private fun setupSellerOrderPage(page: PageFragment): Fragment? {
//        somListFragment = sellerHomeRouter?.getSomListFragment(page.tabPage)
//        return somListFragment

    private fun addPage(fragment: Fragment?, title: String?) {
        fragment?.let { pages[it] = title }
    }

    private fun hideAllPages(transaction: FragmentTransaction) {
        fm.fragments.forEach { transaction.hide(it) }
    }

    private fun setSelectedPage(@InboxFragmentType page: Int) {
        currentSelectedPage = page
    }

    private fun isActivityResumed(): Boolean {
        val state = (context as? AppCompatActivity)?.lifecycle?.currentState
        return state == Lifecycle.State.RESUMED || state == Lifecycle.State.STARTED
    }
}