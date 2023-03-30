package com.tokopedia.search.result.presentation.view.adapter

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition.TAB_FIRST_POSITION
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.search.result.mps.DaggerMPSComponent
import com.tokopedia.search.result.mps.MPSFragment
import com.tokopedia.search.result.mps.qbottomsheet.MPSBottomSheetFragment

class MPSPagerAdapter(
    fragmentManager: FragmentManager,
    private val titleList: List<String>,
    private val searchParameter: SearchParameter,
    private val baseAppComponent: BaseAppComponent,
): FragmentStatePagerAdapter(fragmentManager), SearchViewPagerAdapter {

    private var mpsBottomSheetFragment: MPSBottomSheetFragment? = null
    private var mpsFragment: MPSFragment? = null
    private val registeredFragments = SparseArrayCompat<Fragment>()

    override fun asViewPagerAdapter() = this

    override fun getItem(position: Int): Fragment {
        return when (position) {
            TAB_FIRST_POSITION -> createMPSBottomSheetFragment()
            else -> createMPSFragment()
        }
    }

    private fun createMPSBottomSheetFragment(): MPSBottomSheetFragment {
        return MPSBottomSheetFragment()
    }

    private fun createMPSFragment(): MPSFragment {
        return MPSFragment.newInstance().apply {
            DaggerMPSComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)

        castFragmentsInstance(fragment)
        registerFragments(position, fragment)

        return fragment
    }

    private fun castFragmentsInstance(fragment: Any) {
        when (fragment) {
            is MPSBottomSheetFragment -> mpsBottomSheetFragment = fragment
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

    override fun getFirstPageFragment(): Fragment? = mpsBottomSheetFragment

    override fun getSecondPageFragment(): Fragment? = mpsFragment

    override fun getRegisteredFragmentAtPosition(position: Int): Fragment? =
        registeredFragments.get(position)
}
