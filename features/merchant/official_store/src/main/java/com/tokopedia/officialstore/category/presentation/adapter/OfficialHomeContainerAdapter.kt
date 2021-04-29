package com.tokopedia.officialstore.category.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment.Companion.KEY_CATEGORY
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment.Companion.BUNDLE_CATEGORY

class OfficialHomeContainerAdapter(
        val context: Context?,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragment = SparseArrayCompat<Fragment>()

    var categoryList: MutableList<Category> = mutableListOf()

    private var categoryTabPosMap: MutableMap<String, Int> = mutableMapOf()

    private fun getBundle(position: Int): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_CATEGORY, categoryList[position])
        categoryTabPosMap.put(categoryList[position].categoryId, position)
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

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
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