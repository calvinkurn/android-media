package com.tokopedia.product.detail.view.adapter

import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.MediaDataModel
import com.tokopedia.product.detail.view.fragment.VideoPictureFragment

class VideoPicturePagerAdapter(var media: List<MediaDataModel>,
                               private val onPictureClickListener: ((Int) -> Unit)?,
                               fragmentManager: FragmentManager,
                               private val componentTrackData: ComponentTrackDataModel,
                               private val onPictureClickTrackListener: ((ComponentTrackDataModel?) -> Unit)?,
                               val lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val registeredFragment = SparseArrayCompat<Fragment>()
    private var mediaId = media.map { it.hashCode().toLong() }

    override fun getItemCount(): Int = media.size

    override fun createFragment(position: Int): Fragment {
        val mediaItem = media[position]
        val urlMedia = if (mediaItem.type == "image") {
            mediaItem.urlOriginal
        } else {
            mediaItem.videoUrl
        }
        val f = VideoPictureFragment.createInstance(urlMedia, mediaItem.type, position)
        f.onPictureClickListener = onPictureClickListener
        f.onPictureClickTrackListener = onPictureClickTrackListener
        f.componentTrackDataModel = componentTrackData
        registeredFragment.put(position, f)
        return f
    }

    override fun getItemId(position: Int): Long = mediaId[position]

    override fun containsItem(itemId: Long): Boolean = mediaId.contains(itemId)

    fun getRegisteredFragment(pos: Int): Fragment? = registeredFragment.get(pos)

    fun setData(data: List<MediaDataModel>) {
        media = data
        mediaId = media.map {
            it.hashCode().toLong()
        }
        notifyItemChanged(0)
    }

}