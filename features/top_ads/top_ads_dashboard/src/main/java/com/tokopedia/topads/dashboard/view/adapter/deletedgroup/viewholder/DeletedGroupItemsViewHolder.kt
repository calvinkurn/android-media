package com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DeletedGroupItemsViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
