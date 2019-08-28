package com.tokopedia.search.result.presentation.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.*
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter
import com.tokopedia.search.result.presentation.view.fragment.CatalogListFragment
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.presentation.view.fragment.ProfileListFragment
import com.tokopedia.search.result.presentation.view.fragment.ShopListFragment

class SearchSectionPagerAdapter(
        fragmentManager: FragmentManager,
        private val searchParameter: SearchParameter
) : FragmentStatePagerAdapter(fragmentManager) {

    private var productListFragment: ProductListFragment? = null
    private var catalogListFragment: CatalogListFragment? = null
    private var shopListFragment: ShopListFragment? = null
    private var profileListFragment: ProfileListFragment? = null
    private val titleList = mutableListOf<String>()

    fun setTitleList(titleList: List<String>) {
        this.titleList.addAll(titleList)
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            TAB_FIRST_POSITION -> {
                createProductFragment()
            }
            TAB_SECOND_POSITION -> {
                createCatalogFragment()
            }
            TAB_THIRD_POSITION -> {
                createShopFragment()
            }
            else -> {
                createProfileFragment()
            }
        }
    }

    private fun createProductFragment(): ProductListFragment {
        return ProductListFragment.newInstance(searchParameter, TAB_FIRST_POSITION)
    }

    private fun createCatalogFragment(): CatalogListFragment {
        return CatalogListFragment.newInstance(searchParameter, TAB_SECOND_POSITION)
    }

    private fun createShopFragment(): ShopListFragment {
        return ShopListFragment.newInstance(searchParameter, TAB_THIRD_POSITION)
    }

    private fun createProfileFragment(): ProfileListFragment {
        return ProfileListFragment.newInstance(searchParameter.getSearchQuery(), TAB_FORTH_POSITION)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)

        castFragmentsInstance(fragment)

        return fragment
    }

    private fun castFragmentsInstance(fragment: Any) {
        when (fragment) {
            is ProductListFragment -> productListFragment = fragment
            is CatalogListFragment -> catalogListFragment = fragment
            is ShopListFragment -> shopListFragment = fragment
            is ProfileListFragment -> profileListFragment = fragment
        }
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (titleList.size > position) titleList[position] else ""
    }

    fun getProductListFragment(): ProductListFragment? = productListFragment

    fun getCatalogListFragment(): CatalogListFragment? = catalogListFragment

    fun getShopListFragment(): ShopListFragment? = shopListFragment

    fun getProfileListFragment(): ProfileListFragment? = profileListFragment
}
