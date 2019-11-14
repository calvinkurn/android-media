package com.tokopedia.topads.view.adapter.product.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author errysuprayogi on 11,November,2019
 */

abstract class ProductViewHolder<in T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
