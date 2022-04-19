package com.tokopedia.sellerfeedback.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerfeedback.presentation.uimodel.BaseImageFeedbackUiModel

abstract class BaseImageFeedbackViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T)

    interface ImageClickListener {
        fun onClickRemoveImage(item: BaseImageFeedbackUiModel)
        fun onClickAddImage()
    }
}