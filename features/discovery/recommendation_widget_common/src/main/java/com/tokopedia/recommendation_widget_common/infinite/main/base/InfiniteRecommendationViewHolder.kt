package com.tokopedia.recommendation_widget_common.infinite.main.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class InfiniteRecommendationViewHolder<T : InfiniteRecommendationUiModel>(
    view: View
) : ViewHolder(view) {
    abstract fun bind(item: T)
}
