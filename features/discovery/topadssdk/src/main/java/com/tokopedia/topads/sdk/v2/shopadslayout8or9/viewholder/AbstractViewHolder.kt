package com.tokopedia.topads.sdk.v2.shopadslayout8or9.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
