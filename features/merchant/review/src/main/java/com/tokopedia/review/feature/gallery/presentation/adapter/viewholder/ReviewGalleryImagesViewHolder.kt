package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.ImageUnify

class ReviewGalleryImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var image: ImageUnify? = null

    init {
        image = view.findViewById(R.id.review_gallery_image)
    }

    fun bind(imageUrl: String) {
        image?.loadImage(imageUrl)
    }
}