package com.tokopedia.search.result.presentation.view.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.result.presentation.view.fragment.ProductListFragment
import com.tokopedia.search.result.shop.presentation.fragment.ShopListFragment

internal class SearchSectionPagerAdapter(
    fragmentManager: FragmentManager,
    private val titleList: List<String>,
    private val searchParameter: SearchParameter,
    private val classLoader: ClassLoader,
    private val fragmentFactory: FragmentFactory,
) : FragmentStatePagerAdapter(fragmentManager), SearchViewPagerAdapter {

    private var productListFragment: ProductListFragment? = null
    private var shopListFragment: ShopListFragment? = null
    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun asViewPagerAdapter(): PagerAdapter = this

    override fun getItem(position: Int): Fragment {
        return when (position) {
            TAB_FIRST_POSITION -> createProductFragment()
            else -> createShopFragment()
        }
    }

    private fun createProductFragment(): ProductListFragment {
        return ProductListFragment.newInstance(searchParameter)
    }

    private fun createShopFragment(): ShopListFragment {
        return ShopListFragment.newInstance(classLoader, fragmentFactory)
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

    override fun getFirstPageFragment(): Fragment? = productListFragment

    override fun getSecondPageFragment(): Fragment? = shopListFragment

    override fun getRegisteredFragmentAtPosition(position: Int): Fragment? {
        return registeredFragments.get(position)
    }
}
