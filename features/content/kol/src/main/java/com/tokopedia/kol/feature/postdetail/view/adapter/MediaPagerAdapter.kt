package com.tokopedia.kol.feature.postdetail.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.kol.feature.video.view.fragment.MediaHolderFragment
import android.support.v4.util.SparseArrayCompat
import android.view.ViewGroup


class MediaPagerAdapter(private val media: MutableList<MediaItem>, fm: FragmentManager?)
    : FragmentStatePagerAdapter(fm) {

    private val registeredFragment = SparseArrayCompat<Fragment>()

    override fun getItem(positon: Int): Fragment {
        val url = if (media[positon].type == MediaHolderFragment.TYPE_VIDEO){
            media[positon].videos.firstOrNull()?.url ?: ""
        } else {
            media[positon].thumbnail
        }
        return MediaHolderFragment.createInstance(url, media[positon].type)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Fragment {
        val f = super.instantiateItem(container, position) as Fragment
        registeredFragment.put(position, f)
        return f
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragment.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(pos: Int): Fragment? = registeredFragment.get(pos)

    override fun getCount(): Int = media.size
}