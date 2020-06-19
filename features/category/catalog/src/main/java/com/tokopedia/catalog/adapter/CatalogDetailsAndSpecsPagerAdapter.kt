package com.tokopedia.catalog.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.catalog.R

class CatalogDetailsAndSpecsPagerAdapter(
        fm: FragmentManager,
        var context: Context?,
        var fragmentList: ArrayList<Fragment>
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if(position == 0){
            context?.getString(R.string.catalog_spesification)
        } else {
            context?.getString(R.string.catalog_description)
        }
    }

}
