package com.tokopedia.topads.dashboard.view.adapter.product.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem

/**
 * Created by Pika on 7/6/20.
 */

abstract class ProductViewHolder<in T>(view: View): RecyclerView.ViewHolder(view) {
    open fun bind(item: T, selectMode: Boolean, statsData: MutableList<WithoutGroupDataItem>){}
}