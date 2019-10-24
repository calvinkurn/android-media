package com.tokopedia.officialstore.category.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment

class OfficialHomeContainerAdapter(
        val context: Context?,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragment = SparseArrayCompat<Fragment>()

    var categoryList: MutableList<Category> = mutableListOf()

    private fun getBundle(position: Int): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(OfficialHomeFragment.BUNDLE_CATEGORY,
                categoryList[position])
        return bundle
    }

    override fun getItem(position: Int): Fragment {
        return OfficialHomeFragment.newInstance(getBundle(position))
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categoryList[position].title
    }

    override fun getCount(): Int {
        return categoryList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragment.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }
}