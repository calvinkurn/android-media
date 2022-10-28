package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.BaseCreateReviewVisitable

abstract class BaseCreateReviewViewHolder<VB : ViewBinding, V : BaseCreateReviewVisitable<*>>(
    itemView: View,
) : AbstractViewHolder<V>(itemView) {
    abstract val binding: VB
}