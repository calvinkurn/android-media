package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Pika on 12/4/20.
 */

abstract class EditNegKeywordViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}