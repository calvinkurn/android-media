package com.tokopedia.gallery.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gallery.R

class LoadingSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.review_image_slider_loading
    }
}