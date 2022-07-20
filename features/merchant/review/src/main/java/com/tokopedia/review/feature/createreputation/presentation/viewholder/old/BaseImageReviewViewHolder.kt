package com.tokopedia.review.feature.createreputation.presentation.viewholder.old

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseImageReviewViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T)
}