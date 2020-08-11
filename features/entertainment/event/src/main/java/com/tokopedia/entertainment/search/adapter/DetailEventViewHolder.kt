package com.tokopedia.entertainment.search.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DetailEventViewHolder<T : DetailEventItem<*>>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(element : T)
}