package com.tokopedia.search.result.presentation.view.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION
import com.tokopedia.search.result.mps.MPSFragment
import com.tokopedia.search.result.mps.bottomsheet.MPSShimmeringFragment

internal class MPSPagerAdapter(
    fragmentManager: FragmentManager,
    private val titleList: List<String>,
    private val classLoader: ClassLoader,
    private val fragmentFactory: FragmentFactory,
): FragmentStatePagerAdapter(fragmentManager), SearchViewPagerAdapter {

    private var mpsShimmeringFragment: MPSShimmeringFragment? = null
    private var mpsFragment: MPSFragment? = null
    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun asViewPagerAdapter() = this

    override fun getItem(position: Int): Fragment {
        return when (position) {
            TAB_FIRST_POSITION -> createMPSBottomSheetFragment()
            else -> createMPSFragment()
        }
    }

    private fun createMPSBottomSheetFragment(): MPSShimmeringFragment {
        return MPSShimmeringFragment.newInstance(classLoader, fragmentFactory)
    }

    private fun createMPSFragment(): MPSFragment {
        return MPSFragment.newInstance(classLoader, fragmentFactory)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)

        castFragmentsInstance(fragment)
        registerFragments(position, fragment)

        return fragment
    }

    private fun castFragmentsInstance(fragment: Any) {
        when (fragment) {
            is MPSShimmeringFragment -> mpsShimmeringFragment = fragment
            is MPSFragment -> mpsFragment = fragment
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

    override fun getPageTitle(position: Int): CharSequence {
        return if (titleList.size > position) titleList[position] else ""
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getFirstPageFragment(): Fragment? = mpsShimmeringFragment

    override fun getSecondPageFragment(): Fragment? = mpsFragment

    override fun getRegisteredFragmentAtPosition(position: Int): Fragment? =
        registeredFragments.get(position)
}
