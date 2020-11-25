package com.tokopedia.orderhistory.view.adapter

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.orderhistory.view.adapter.viewholder.OrderHistoryViewHolder

class OrderHistoryAdapter(private val baseListAdapterTypeFactory: OrderHistoryTypeFactory)
    : BaseListAdapter<Visitable<*>, OrderHistoryTypeFactory>(baseListAdapterTypeFactory) {

}