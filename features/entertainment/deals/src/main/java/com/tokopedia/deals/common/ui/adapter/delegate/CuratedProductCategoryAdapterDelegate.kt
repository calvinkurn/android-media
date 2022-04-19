package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.listener.CuratedProductCategoryListener
import com.tokopedia.deals.common.ui.adapter.viewholder.CuratedProductCategoryViewHolder
import com.tokopedia.deals.common.ui.dataview.CuratedProductCategoryDataView

/**
 * @author by jessica on 16/06/20
 */

class CuratedProductCategoryAdapterDelegate(val listener: CuratedProductCategoryListener)
    : TypedAdapterDelegate<CuratedProductCategoryDataView, Any, CuratedProductCategoryViewHolder>(CuratedProductCategoryViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: CuratedProductCategoryDataView, holder: CuratedProductCategoryViewHolder) {
        holder.bindData(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): CuratedProductCategoryViewHolder {
        return CuratedProductCategoryViewHolder(basicView, listener)
    }

}