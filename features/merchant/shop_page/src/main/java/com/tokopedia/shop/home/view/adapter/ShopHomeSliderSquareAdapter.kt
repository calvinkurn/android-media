package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeItemSliderSquareViewHolder
import com.tokopedia.shop.home.view.model.DisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class ShopHomeSliderSquareAdapter: ListAdapter<DisplayWidgetUiModel.WidgetItem, ShopHomeItemSliderSquareViewHolder>(AdapterDiffCallback.WidgetDiffCallback) {

    override fun onBindViewHolder(holderItem: ShopHomeItemSliderSquareViewHolder, position: Int) {
        getItem(position)?.let { holderItem.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeItemSliderSquareViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shop_page_home_slider_square,
                        parent,
                        false)
        return ShopHomeItemSliderSquareViewHolder(view)
    }
}