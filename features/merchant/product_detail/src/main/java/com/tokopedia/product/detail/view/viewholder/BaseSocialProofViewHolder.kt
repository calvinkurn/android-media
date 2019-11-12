package com.tokopedia.product.detail.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseSocialProofViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(element: T, position: Int)
}