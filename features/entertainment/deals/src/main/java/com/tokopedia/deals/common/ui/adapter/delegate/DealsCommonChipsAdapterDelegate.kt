package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.listener.DealChipsListActionListener
import com.tokopedia.deals.common.ui.adapter.viewholder.DealsChipsViewHolder
import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView

class DealsCommonChipsAdapterDelegate(private val chipListListener: DealChipsListActionListener) :
    TypedAdapterDelegate<DealsChipsDataView, Any, DealsChipsViewHolder>(DealsChipsViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: DealsChipsDataView, holder: DealsChipsViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsChipsViewHolder {
        return DealsChipsViewHolder(basicView, chipListListener)
    }

}