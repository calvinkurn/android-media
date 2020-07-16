package com.tokopedia.deals.home.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.home.listener.DealsFavouriteCategoriesListener
import com.tokopedia.deals.home.ui.adapter.viewholder.DealsFavouriteCategoriesViewHolder
import com.tokopedia.deals.home.ui.adapter.viewholder.VoucherPlacePopularViewHolder
import com.tokopedia.deals.home.ui.dataview.FavouritePlacesDataView

/**
 * @author by jessica on 24/06/20
 */

class DealsFavouriteCategoriesAdapterDelegate(private val listener: DealsFavouriteCategoriesListener)
    : TypedAdapterDelegate<FavouritePlacesDataView, Any,
        DealsFavouriteCategoriesViewHolder>(VoucherPlacePopularViewHolder.LAYOUT) {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): DealsFavouriteCategoriesViewHolder {
        return DealsFavouriteCategoriesViewHolder(basicView, listener)
    }

    override fun onBindViewHolder(item: FavouritePlacesDataView, holder: DealsFavouriteCategoriesViewHolder) {
       holder.bind(item)
    }

}