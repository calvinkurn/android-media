package com.tokopedia.product.detail.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.collection.SparseArrayCompat
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.detail.common.data.model.product.Media
import com.tokopedia.product.detail.view.fragment.VideoPictureFragment

class VideoPicturePagerAdapter(val context: Context,
                               private val media: MutableList<Media> = mutableListOf(),
                               val urlTemp: String? = null,
                               val onPictureClickListener: ((Int) -> Unit)?,
                               val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val registeredFragment = SparseArrayCompat<Fragment>()

    override fun getItem(position: Int): Fragment {
        val mediaItem = media[position]
        val urlMedia = if (mediaItem.type == "image") {
            mediaItem.urlOriginal
        } else {
            mediaItem.videoUrl
        }
        val f = VideoPictureFragment.createInstance(urlMedia, mediaItem.type, position)
        f.onPictureClickListener = onPictureClickListener
        return f
    }

    override fun getCount(): Int = media.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o = super.instantiateItem(container, position)
        registeredFragment.put(position, o as Fragment)
        return o
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        registeredFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(pos: Int): Fragment? = registeredFragment.get(pos)

}