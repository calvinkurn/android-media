package com.tokopedia.officialstore.presentation.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.officialstore.presentation.OfficialHomeFragment
import com.tokopedia.officialstore.presentation.RecyclerViewScrollListener
import com.tokopedia.officialstore.presentation.model.Category

class OfficialHomeContainerAdapter(fragmentManager: FragmentManager,
                                   val categoryList: MutableList<Category>) :
        FragmentStatePagerAdapter(fragmentManager) {

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

}