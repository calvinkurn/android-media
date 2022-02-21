package com.tokopedia.catalog.adapter.components

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.viewholder.components.CatalogForYouViewHolder

class CatalogForYouAdapter (val list : List<CatalogComparisonProductsResponse.CatalogComparisonList.CatalogComparison?>?,
                            private val catalogProductCardListener: CatalogProductCardListener?
)
    : RecyclerView.Adapter<CatalogForYouViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogForYouViewHolder {
        return CatalogForYouViewHolder(View.inflate(parent.context, R.layout.item_catalog_for_you, null))
    }

    override fun getItemCount(): Int  = list?.size ?: 0

    override fun onBindViewHolder(holder: CatalogForYouViewHolder, position: Int) {
        holder.bind(list?.get(position),catalogProductCardListener)
    }
}