package com.tokopedia.deals.home.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.ui.dataview.DealsTickerDataView
import com.tokopedia.deals.home.ui.adapter.viewholder.DealsTickerViewHolder

/**
 * @author by astidhiyaa on 19/05/21
 */

class DealsTickerAdapterDelegate():
        TypedAdapterDelegate<DealsTickerDataView, Any, DealsTickerViewHolder>(DealsTickerViewHolder.LAYOUT)  {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsTickerViewHolder {
        return DealsTickerViewHolder(basicView)
    }

    override fun onBindViewHolder(item: DealsTickerDataView, holder: DealsTickerViewHolder) {
        holder.bind(item)
    }
}