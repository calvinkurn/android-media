package com.tokopedia.officialstore.category.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment.Companion.KEY_CATEGORY
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment
import com.tokopedia.officialstore.official.presentation.OfficialHomeFragment.Companion.BUNDLE_CATEGORY

class OfficialHomeContainerAdapter(
        val context: Context?,
        fragmentManager: FragmentManager,
        val categoryIdSelected: String
) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragment = SparseArrayCompat<Fragment>()

    var categoryList: MutableList<Category> = mutableListOf()

    private fun getBundle(position: Int): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(BUNDLE_CATEGORY,
                categoryList[position])
        bundle.putString(KEY_CATEGORY, categoryIdSelected)
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