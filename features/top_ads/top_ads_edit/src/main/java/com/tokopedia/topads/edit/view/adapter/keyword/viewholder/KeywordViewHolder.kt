package com.tokopedia.topads.edit.view.adapter.keyword.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class KeywordViewHolder<in T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
