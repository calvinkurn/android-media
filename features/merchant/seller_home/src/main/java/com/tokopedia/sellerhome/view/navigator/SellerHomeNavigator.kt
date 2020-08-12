package com.tokopedia.sellerhome.view.navigator

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
        private val fm: FragmentManager,
        private val sellerHomeRouter: SellerHomeRouter?
) {

    @FragmentType
    private var currentSelectedPage: Int? = null
    private val pages: MutableMap<Int, Fragment?> = mutableMapOf()
    private val pagesTitle: MutableMap<Int, String> = mutableMapOf()

    init {
        setupPagesTitle()
    }

    fun start(@FragmentType page: Int) {
        showPage(page)
    }

    fun showPage(@FragmentType page: Int) {
        createFragmentIfNotExist(page)
        val title = pagesTitle[page].orEmpty()
        val fragment = pages[page]
        if (title.isNotBlank() && fragment != null) {
            val transaction = fm.beginTransaction()
            val currentPage = pages[currentSelectedPage]
            val isFragmentAdded = fm.findFragmentByTag(title) != null
            currentPage?.run { transaction.hide(this) }
            if (isFragmentAdded) {
                transaction.show(fragment)
            } else {
                transaction.add(R.id.sahContainer, fragment, title).show(fragment)
            }
            transaction.commit()
            setSelectedPage(page)
        }
    }

    private fun createFragmentIfNotExist(page: Int) {
        if (pages[page] == null) {
            setupPagesTitle()
            setupPage(PageFragment(page))
        }
    }

    fun navigateFromAppLink(page: PageFragment) {
        val type = page.type
        createFragmentIfNotExist(type)
        val title = pagesTitle[type].orEmpty()
        val previousFragment = pages[type]

        if (title.isNotBlank() && previousFragment != null) {
            val newFragment = setupPageFromAppLink(page)
            val transaction = fm.beginTransaction()
            val currentPage = pages[currentSelectedPage]
            currentPage?.run { transaction.hide(this) }
            when {
                newFragment == null -> {
                    val isFragmentAdded = fm.findFragmentByTag(title) != null
                    if (isFragmentAdded) {
                        transaction.show(previousFragment)
                    } else {
                        transaction.add(R.id.sahContainer, previousFragment, title)
                                .show(previousFragment)
                    }
                }
                previousFragment != newFragment -> {
                    transaction.remove(previousFragment)
                            .add(R.id.sahContainer, newFragment, title)
                            .show(newFragment)
                }
                else -> {
                    val isFragmentAdded = fm.findFragmentByTag(title) != null
                    if (isFragmentAdded) {
                        transaction.show(previousFragment)
                    } else {
                        transaction.add(R.id.sahContainer, previousFragment, title)
                                .show(previousFragment)
                    }
                }
            }
            transaction.commit()
            setSelectedPage(type)
        }
    }

    fun getPageTitle(@FragmentType pageType: Int): String? {
        return pagesTitle[pageType].orEmpty()
    }

    private fun setupPageFromAppLink(selectedPage: PageFragment?): Fragment? {
        return selectedPage?.let {
            val pageType = it.type

            when (pageType) {
                FragmentType.PRODUCT -> setupProductManagePage(it)
                FragmentType.ORDER -> setupSellerOrderPage(it)
            }

            pages[pageType]
        }
    }

    fun setHomeTitle(title: String) {
        pagesTitle[FragmentType.HOME] = title
    }

    fun isHomePageSelected(): Boolean {
        return currentSelectedPage == FragmentType.HOME
    }

    private fun setupPagesTitle() {
        pagesTitle[FragmentType.HOME] = context.getString(R.string.sah_home)
        pagesTitle[FragmentType.PRODUCT] = context.getString(R.string.sah_product_list)
        pagesTitle[FragmentType.CHAT] = context.getString(R.string.sah_chat)
        pagesTitle[FragmentType.ORDER] = context.getString(R.string.sah_sale)
        pagesTitle[FragmentType.OTHER] = context.getString(R.string.sah_others)
    }

    private fun setupPage(page: PageFragment) {
        pages[page.type] = when (page.type) {
            FragmentType.HOME -> SellerHomeFragment.newInstance()
            FragmentType.PRODUCT -> sellerHomeRouter?.getProductManageFragment(arrayListOf(), "")
            FragmentType.CHAT -> sellerHomeRouter?.getChatListFragment()
            FragmentType.ORDER -> sellerHomeRouter?.getSomListFragment(SomTabConst.STATUS_NEW_ORDER)
            FragmentType.OTHER -> OtherMenuFragment.createInstance()
            else -> SellerHomeFragment.newInstance()
        }
    }

    private fun setupProductManagePage(page: PageFragment) {
        val searchKeyword = page.keywordSearch
        val filterOptionEmptyStock = FilterOption.FilterByCondition.EmptyStockOnly.id

        when {
            page.tabPage.isNotBlank() && page.tabPage == filterOptionEmptyStock -> {
                val filterOptions = arrayListOf(filterOptionEmptyStock)
                pages[page.type] = sellerHomeRouter?.getProductManageFragment(filterOptions, searchKeyword)
            }
            page.tabPage.isBlank() && searchKeyword.isNotBlank() -> {
                pages[page.type] = sellerHomeRouter?.getProductManageFragment(arrayListOf(), searchKeyword)
            }
        }
    }

    private fun setupSellerOrderPage(page: PageFragment) {
        pages[page.type] = sellerHomeRouter?.getSomListFragment(page.tabPage)
    }

    private fun setSelectedPage(@FragmentType page: Int) {
        currentSelectedPage = page
    }

    fun getHomeFragment(): SellerHomeFragment? {
        return pages[FragmentType.HOME] as? SellerHomeFragment
    }
}