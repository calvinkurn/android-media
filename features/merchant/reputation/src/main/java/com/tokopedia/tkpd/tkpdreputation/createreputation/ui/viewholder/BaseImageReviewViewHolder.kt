package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder
import android.support.v7.widget.RecyclerView
import android.view.View


abstract class BaseImageReviewViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T)
}