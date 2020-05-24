package com.tokopedia.topads.edit.view.adapter.product.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class ProductViewHolder<in T>(view: View): RecyclerView.ViewHolder(view) {
    open fun bind(item: T){}
}
