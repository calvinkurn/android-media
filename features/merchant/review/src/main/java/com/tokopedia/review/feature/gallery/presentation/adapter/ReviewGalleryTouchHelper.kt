package com.tokopedia.review.feature.gallery.presentation.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryImageSwipeListener

class ReviewGalleryTouchHelper(private val listener: ReviewGalleryImageSwipeListener): ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return 0
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onImageSwiped(viewHolder.adapterPosition)
    }
}