package com.tokopedia.developer_options.presentation.feedbackpage.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseImageFeedbackViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T)
}