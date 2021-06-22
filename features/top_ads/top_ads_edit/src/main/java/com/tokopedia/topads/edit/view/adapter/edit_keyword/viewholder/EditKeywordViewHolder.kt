package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Pika on 9/4/20.
 */

abstract class EditKeywordViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T, added: MutableList<Boolean>, minBid: String)
}