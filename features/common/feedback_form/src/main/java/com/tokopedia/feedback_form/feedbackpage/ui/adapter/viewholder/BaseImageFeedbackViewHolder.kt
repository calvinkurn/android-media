package com.tokopedia.feedback_form.feedbackpage.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseImageFeedbackViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T)
}