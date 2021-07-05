package com.tokopedia.topads.dashboard.view.adapter.keyword.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Pika on 7/6/20.
 */

abstract class KeywordViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T, selectMode: Boolean, fromSearch: Boolean) {}
}