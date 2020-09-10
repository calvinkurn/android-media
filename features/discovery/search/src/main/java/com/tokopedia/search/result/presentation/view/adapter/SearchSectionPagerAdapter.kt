package com.tokopedia.search.result.presentation.view.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.shop.presentation.fragment.ShopListFragment

internal class SearchSectionPagerAdapter(
        fragmentManager: FragmentManager,
        private val searchParameter: SearchParameter
) : FragmentStatePagerAdapter(fragmentManager) {

    private var productListFragment: ProductListFragment? = null
    private var shopListFragment: ShopListFragment? = null
    private val titleList = mutableListOf<String>()
    private val registeredFragments = SparseArrayCompat<Fragment>()

    fun updateData(titleList: List<String>) {
        this.titleList.clear()
        this.titleList.addAll(titleList)
        this.registeredFragments.clear()
        this.notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            TAB_FIRST_POSITION -> {
                createProductFragment()
            }
            else -> {
                createShopFragment()
            }
        }
    }

    private fun createProductFragment(): ProductListFragment {
        return ProductListFragment.newInstance(searchParameter)
    }

    private fun createShopFragment(): ShopListFragment {
        return ShopListFragment.newInstance()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)

        castFragmentsInstance(fragment)
        registerFragments(position, fragment)

        return fragment
    }

    private fun castFragmentsInstance(fragment: Any) {
        when (fragment) {
            is ProductListFragment -> productListFragment = fragment
            is ShopListFragment -> shopListFragment = fragment
        }
    }

    private fun registerFragments(position: Int, fragment: Any) {
        if (fragment is Fragment) {
            registeredFragments.put(position, fragment)
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (titleList.size > position) titleList[position] else ""
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun getProductListFragment(): ProductListFragment? = productListFragment

    fun getShopListFragment(): ShopListFragment? = shopListFragment

    fun getRegisteredFragmentAtPosition(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}
