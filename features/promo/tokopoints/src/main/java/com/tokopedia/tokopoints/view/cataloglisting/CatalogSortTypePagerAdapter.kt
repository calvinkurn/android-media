package com.tokopedia.tokopoints.view.cataloglisting

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.tokopoints.view.model.CatalogSubCategory
import java.util.*

class CatalogSortTypePagerAdapter(fm: FragmentManager?, categoryId: Int, items: MutableList<CatalogSubCategory>?) : FragmentStatePagerAdapter(fm!!) {
    private val categoryId: Int
    private var isPointsAvailable = false
    private val mItems: MutableList<CatalogSubCategory>
    private val mrRegisteredFragments = SparseArray<Fragment>()
    fun setPointsAvailable(isPointsAvailable: Boolean) {
        this.isPointsAvailable = isPointsAvailable
    }

    override fun getItem(position: Int): Fragment {
        return CatalogListItemFragment.newInstance(categoryId,
                mItems[position].id, isPointsAvailable)
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mItems[position].name
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        mrRegisteredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        mrRegisteredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment? {
        return mrRegisteredFragments[position]
    }

    init {
        if (items == null || items.isEmpty()) {
            mItems = ArrayList()
            mItems.add(CatalogSubCategory())
        } else {
            mItems = items
        }
        this.categoryId = categoryId
    }
}
