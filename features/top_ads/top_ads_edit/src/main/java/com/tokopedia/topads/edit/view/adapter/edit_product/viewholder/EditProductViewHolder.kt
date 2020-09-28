package com.tokopedia.topads.edit.view.adapter.edit_product.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Pika on 8/4/20.
 */

abstract class EditProductViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T){}
}
