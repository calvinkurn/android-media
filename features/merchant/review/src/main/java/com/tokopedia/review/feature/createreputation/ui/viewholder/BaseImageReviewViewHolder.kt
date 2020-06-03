package com.tokopedia.review.feature.createreputation.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseImageReviewViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T)
}