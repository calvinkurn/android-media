package com.tokopedia.topads.headline.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.data.model.CountDataItem

/**
 * Created by Pika on 16/10/20.
 */

abstract class HeadLineAdItemsViewHolder<in T>(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: T, selectedMode: Boolean, fromSearch: Boolean, statsData: MutableList<DataItem>, countList: MutableList<CountDataItem>, selectedText: String = ""){}
}