package com.tokopedia.deals.home.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.ui.adapter.viewholder.DealsFavouriteCategoriesViewHolder
import com.tokopedia.deals.home.ui.adapter.viewholder.VoucherPlacePopularViewHolder
import com.tokopedia.deals.home.ui.dataview.CuratedCategoryDataView

/**
 * @author by jessica on 24/06/20
 */

class DealsFavouriteCategoriesAdapterDelegate(private val listener: DealsFavouriteCategoriesListener)
    : TypedAdapterDelegate<CuratedCategoryDataView, Any,
        DealsFavouriteCategoriesViewHolder>(VoucherPlacePopularViewHolder.LAYOUT) {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsFavouriteCategoriesViewHolder {
        return DealsFavouriteCategoriesViewHolder(basicView, listener)
    }

    override fun onBindViewHolder(item: CuratedCategoryDataView, holder: DealsFavouriteCategoriesViewHolder) {
       holder.bind(item)
    }

}