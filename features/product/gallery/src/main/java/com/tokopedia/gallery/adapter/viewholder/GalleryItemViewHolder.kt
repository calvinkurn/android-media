package com.tokopedia.gallery.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.gallery.GalleryView
import com.tokopedia.gallery.R
import com.tokopedia.gallery.viewmodel.ImageReviewItem

class GalleryItemViewHolder(itemView: View, private val galleryView: GalleryView) : AbstractViewHolder<ImageReviewItem>(itemView) {

    private val galleryImage: ImageView

    init {
        galleryImage = itemView.findViewById(R.id.galleryImage)
    }

    override fun bind(imageReviewItem: ImageReviewItem) {
        ImageHandler.LoadImage(galleryImage, imageReviewItem.imageUrlThumbnail)
        galleryImage.setOnClickListener { galleryView.onGalleryItemClicked(adapterPosition) }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.gallery_item
    }
}