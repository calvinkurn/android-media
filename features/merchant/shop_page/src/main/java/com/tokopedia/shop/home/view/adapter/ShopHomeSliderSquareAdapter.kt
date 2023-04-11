package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeItemSliderSquareViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class ShopHomeSliderSquareAdapter(
    private val listener: ShopHomeDisplayWidgetListener
) : ListAdapter<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem, ShopHomeItemSliderSquareViewHolder>(AdapterDiffCallback.WidgetDiffCallback) {

    var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    var parentPosition: Int = 0
    var heightRatio = 0.0F

    override fun onBindViewHolder(holderItem: ShopHomeItemSliderSquareViewHolder, position: Int) {
        getItem(position)?.let {
            holderItem.displayWidgetUiModel = displayWidgetUiModel
            holderItem.parentPosition = parentPosition
            holderItem.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeItemSliderSquareViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_shop_page_home_slider_square,
                parent,
                false
            )
        return ShopHomeItemSliderSquareViewHolder(view, listener, heightRatio)
    }
}
