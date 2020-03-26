package com.tokopedia.brandlist.brandlist_category.presentation.adapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.brandlist.brandlist_category.data.model.Category
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment
import com.tokopedia.brandlist.brandlist_page.presentation.fragment.BrandlistPageFragment.Companion.KEY_CATEGORY

class BrandlistContainerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {

    private val brandListFragmentMap = SparseArrayCompat<Fragment>()

    var categories: MutableList<Category> = mutableListOf()

    private fun getBundle(position: Int): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(KEY_CATEGORY, categories[position])
        return bundle
    }

    override fun getItem(position: Int): Fragment {
        return BrandlistPageFragment.newInstance(getBundle(position))
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position].title
    }

    override fun getCount(): Int {
        return categories.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragmentObject = super.instantiateItem(container, position)
        registerFragment(brandListFragmentMap, position, fragmentObject as Fragment)
        return fragmentObject
    }

    private fun registerFragment(fragmentMap: SparseArrayCompat<Fragment>, position: Int, fragment: Fragment) {
        fragmentMap.put(position, fragment)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        deregisterFragment(brandListFragmentMap, position)
        super.destroyItem(container, position, `object`)
    }

    private fun deregisterFragment(fragmentMap: SparseArrayCompat<Fragment>, position: Int) {
        fragmentMap.remove(position)
    }
}