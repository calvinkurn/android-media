package com.tokopedia.product.detail.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.util.SparseArrayCompat
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.detail.view.fragment.VideoPictureFragment

class VideoPicturePagerAdapter(val context: Context,
                               private val pictures: MutableList<Pair<String, String>> = mutableListOf(),
                               val urlTemp: String? = null,
                               val onPictureClickListener: ((Int) -> Unit)?,
                               val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    val registeredFragment = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment {
        val media = pictures[position]
        return VideoPictureFragment.createInstance(media.first, media.second)
    }

    override fun getCount(): Int = pictures.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragment.put(position , o as Fragment)
        return o
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        registeredFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(pos: Int): Fragment? = registeredFragment.get(pos)

}