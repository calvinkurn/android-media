package com.tokopedia.similarsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal abstract class BaseViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {

    open fun onViewRecycled() {

    }

    abstract fun bind(item: T)

    open fun bind(payload: List<Any>) {

    }
}