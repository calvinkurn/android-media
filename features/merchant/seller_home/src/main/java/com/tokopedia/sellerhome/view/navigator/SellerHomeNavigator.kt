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

    companion object {
        const val FRAGMENT_TAG = "BOTTOM_NAV_FRAGMENT_TAG"
    }

    @FragmentType
    private var currentSelectedPage: Int? = null
    private val pages: MutableMap<Int, FragmentPage> = mutableMapOf()

    init {
        initFragments()
    }

    fun start(@FragmentType page: Int) {
        showPage(page)
    }

    fun showPage(@FragmentType page: Int) {
        pages[page]?.lazyFragment?.value?.let { fragment ->
            replaceFragment(fragment)
            setSelectedPage(page)
        }
    }

    fun navigateFromAppLink(page: PageFragment) {
        val type = page.type

        pages[type]?.lazyFragment?.value?.let { currentPage ->
            val fragment = setupPageFromAppLink(page)

            fragment?.let { selectedPage ->
                when {
                    currentPage != selectedPage -> {
                        replaceFragment(selectedPage)
                    }
                    else -> {
                        replaceFragment(currentPage)
                    }
                }
                setSelectedPage(type)
            }
        }
    }

    fun getPageTitle(@FragmentType pageType: Int): String? {
        return pages[pageType]?.title
    }

    private fun setupPageFromAppLink(selectedPage: PageFragment?): Fragment? {
        return selectedPage?.let {
            val pageType = it.type

            when (pageType) {
                FragmentType.PRODUCT -> setupProductManagePage(it)
                FragmentType.ORDER -> setupSellerOrderPage(it)
            }

            pages[pageType]?.lazyFragment?.value
        }
    }

    fun setHomeTitle(title: String) {
        pages[FragmentType.HOME]?.title = title
    }

    fun isHomePageSelected(): Boolean {
        return currentSelectedPage == FragmentType.HOME
    }

    private fun initFragments() {
        val homeFragment = FragmentPage(context.getString(R.string.sah_home), lazy { SellerHomeFragment.newInstance() })
        val productManageFragment = FragmentPage(context.getString(R.string.sah_product_list), lazy { sellerHomeRouter?.getProductManageFragment(arrayListOf(), "") })
        val chatFragment = FragmentPage(context.getString(R.string.sah_chat), lazy { sellerHomeRouter?.getChatListFragment() })
        val somListFragment = FragmentPage(context.getString(R.string.sah_sale), lazy { sellerHomeRouter?.getSomListFragment(SomTabConst.STATUS_NEW_ORDER) })
        val otherSettingsFragment = FragmentPage(context.getString(R.string.sah_others), lazy { OtherMenuFragment.createInstance() })

        addPage(FragmentType.HOME, homeFragment)
        addPage(FragmentType.PRODUCT, productManageFragment)
        addPage(FragmentType.CHAT, chatFragment)
        addPage(FragmentType.ORDER, somListFragment)
        addPage(FragmentType.OTHER, otherSettingsFragment)
    }

    private fun setupProductManagePage(page: PageFragment) {
        val searchKeyword = page.keywordSearch
        val filterOptionEmptyStock = FilterOption.FilterByCondition.EmptyStockOnly.id

        when {
            page.tabPage.isNotBlank() && page.tabPage == filterOptionEmptyStock -> {
                val filterOptions = arrayListOf(filterOptionEmptyStock)
                pages[page.type]?.lazyFragment = lazy { sellerHomeRouter?.getProductManageFragment(filterOptions, searchKeyword) }
            }
            page.tabPage.isBlank() && searchKeyword.isNotBlank() -> {
                pages[page.type]?.lazyFragment = lazy { sellerHomeRouter?.getProductManageFragment(arrayListOf(), searchKeyword) }
            }
        }
    }

    private fun setupSellerOrderPage(page: PageFragment) {
        pages[page.type]?.lazyFragment = lazy { sellerHomeRouter?.getSomListFragment(page.tabPage) }
    }

    private fun addPage(@FragmentType key: Int, fragmentPage: FragmentPage) {
        pages[key] = fragmentPage
    }

    private fun setSelectedPage(@FragmentType page: Int) {
        currentSelectedPage = page
    }

    fun getHomeFragment(): SellerHomeFragment? {
        return pages[FragmentType.HOME]?.lazyFragment?.value as? SellerHomeFragment
    }

    private fun replaceFragment(fragment: Fragment) {
        fm.beginTransaction()
                .replace(R.id.sahContainer, fragment, FRAGMENT_TAG)
                .commit()
    }

    inner class FragmentPage(var title: String, var lazyFragment: Lazy<Fragment?>)
}