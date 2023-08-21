package com.tokopedia.shop.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeItemImageColumnViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

class ShopHomeMultipleImageColumnAdapter(
    val listener: ShopHomeDisplayWidgetListener,
    private val imageColumnRoundedSize: Float= 0f
) : ListAdapter<ShopHomeDisplayWidgetUiModel.DisplayWidgetItem, ShopHomeItemImageColumnViewHolder>(AdapterDiffCallback.WidgetDiffCallback) {

    private var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    private var parentPosition: Int = 0
    private var heightRatio: Float = 0.0F

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeItemImageColumnViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_shop_home_page_image_column, parent, false)
        return ShopHomeItemImageColumnViewHolder(view, listener, heightRatio, imageColumnRoundedSize)
    }

    override fun onBindViewHolder(holder: ShopHomeItemImageColumnViewHolder, position: Int) {
        getItem(position)?.let {
            holder.displayWidgetUiModel = displayWidgetUiModel
            holder.parentPosition = parentPosition
            holder.bind(it)
        }
    }

    fun setShopHomeDisplayWidgetUiModelData(displayWidgetUiModel: ShopHomeDisplayWidgetUiModel) {
        this.displayWidgetUiModel = displayWidgetUiModel
    }

    fun setParentPosition(adapterPosition: Int) {
        this.parentPosition = adapterPosition
    }

    fun setHeightRatio(heightRatio: Float) {
        this.heightRatio = heightRatio
    }
}
