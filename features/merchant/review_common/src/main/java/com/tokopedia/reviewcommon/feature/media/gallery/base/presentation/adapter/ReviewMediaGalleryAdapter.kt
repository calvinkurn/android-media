package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.adapter.diffutil.ReviewMediaGalleryDiffUtilCallback
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.fragment.ReviewMediaGalleryLoadStateFragment
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.fragment.ReviewImagePlayerFragment
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uimodel.ImageMediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.fragment.ReviewVideoPlayerFragment
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.model.VideoMediaItemUiModel

class ReviewMediaGalleryAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val mediaItems = arrayListOf<MediaItemUiModel>()

    override fun getItemCount(): Int {
        return mediaItems.size
    }

    override fun getItemId(position: Int): Long {
        return mediaItems[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return mediaItems.any { it.hashCode().toLong() == itemId }
    }

    override fun createFragment(position: Int): Fragment {
        return mediaItems[position].let { mediaItem ->
            when (mediaItem) {
                is ImageMediaItemUiModel -> {
                    ReviewImagePlayerFragment.createInstance(mediaItem.uri)
                }
                is VideoMediaItemUiModel -> {
                    ReviewVideoPlayerFragment.createInstance(mediaItem.uri)
                }
                is LoadingStateItemUiModel -> {
                    ReviewMediaGalleryLoadStateFragment()
                }
                else -> {
                    throw IllegalArgumentException("Unknown media item type of ${mediaItem::class.java.name}")
                }
            }
        }
    }

    fun updateItems(mediaItemUiModels: List<MediaItemUiModel>) {
        val diffUtilCallback = ReviewMediaGalleryDiffUtilCallback(
            mediaItems.toMutableList(),
            mediaItemUiModels
        )
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        mediaItems.clear()
        mediaItems.addAll(mediaItemUiModels)
        result.dispatchUpdatesTo(this)
    }

    fun restoreUiState(mediaItemUiModels: List<MediaItemUiModel>) {
        mediaItems.clear()
        mediaItems.addAll(mediaItemUiModels)
    }
}