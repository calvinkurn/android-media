package com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.common.data.response.groupitem.DataItem

/**
 * Created by Pika on 2/6/20.
 */

abstract class GroupItemsViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<DataItem>, countList: MutableList<CountDataItem>){}
}