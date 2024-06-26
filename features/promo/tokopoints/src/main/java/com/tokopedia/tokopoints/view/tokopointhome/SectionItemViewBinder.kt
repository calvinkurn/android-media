package com.tokopedia.tokopoints.view.tokopointhome

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class SectionItemViewBinder<M, in VH : RecyclerView.ViewHolder>(
        val modelClass: Class<M>) {

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindViewHolder(model: M, viewHolder: VH)
    abstract fun getSectionItemType(): Int

    open fun onViewRecycled(viewHolder: VH) = Unit
    open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
}
