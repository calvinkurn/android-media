package com.tokopedia.topads.dashboard.view.adapter.movetogroup.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class MovetoGroupViewHolder<in T>(view: View): RecyclerView.ViewHolder(view) {
    open fun bind(item: T, lastSelected: Int){}
}
