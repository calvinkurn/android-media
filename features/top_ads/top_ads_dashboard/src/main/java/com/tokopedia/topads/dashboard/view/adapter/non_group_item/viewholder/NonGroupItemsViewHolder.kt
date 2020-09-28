package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem

/**
 * Created by Pika on 2/6/20.
 */

abstract class NonGroupItemsViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<WithoutGroupDataItem>){}
}