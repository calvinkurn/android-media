package com.tokopedia.deals.home.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.home.listener.DealsCategoryListener
import com.tokopedia.deals.home.ui.adapter.viewholder.DealsHomeCategoriesViewHolder
import com.tokopedia.deals.home.ui.dataview.CategoriesDataView

/**
 * @author by jessica on 17/06/20
 */

class DealsCategoriesAdapterDelegate(private val listener: DealsCategoryListener):
        TypedAdapterDelegate<CategoriesDataView, Any, DealsHomeCategoriesViewHolder>(DealsHomeCategoriesViewHolder.LAYOUT)  {
    override fun onBindViewHolder(item: CategoriesDataView, holder: DealsHomeCategoriesViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsHomeCategoriesViewHolder {
        return DealsHomeCategoriesViewHolder(basicView, listener)
    }


}