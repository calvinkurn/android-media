package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeItemImageColumnViewHolder
import com.tokopedia.shop.home.view.model.DisplayWidgetUiModel

class ShopHomeMultipleImageColumnAdapter: ListAdapter<DisplayWidgetUiModel.WidgetItem, ShopHomeItemImageColumnViewHolder>(AdapterDiffCallback.WidgetDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeItemImageColumnViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_shop_home_page_image_column, parent, false)
        return ShopHomeItemImageColumnViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopHomeItemImageColumnViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

}