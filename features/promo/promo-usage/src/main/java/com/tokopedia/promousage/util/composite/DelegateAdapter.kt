package com.tokopedia.promousage.util.composite

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateAdapter<M, in VH : RecyclerView.ViewHolder>(val modelClass: Class<out M>) {

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindViewHolder(item: M, viewHolder: VH)
}
