package com.tokopedia.shop.home.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.home.WidgetName.SHOWCASE_GRID_BIG
import com.tokopedia.shop.home.WidgetName.SHOWCASE_GRID_MEDIUM
import com.tokopedia.shop.home.WidgetName.SHOWCASE_GRID_SMALL
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_MEDIUM
import com.tokopedia.shop.home.WidgetName.SHOWCASE_SLIDER_SMALL
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeShowcaseListGridViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeShowcaseListSliderMediumViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeShowcaseListSliderSmallViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeShowcaseListWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel

class ShopHomeShowcaseListWidgetAdapter(
        showcaseListItemData: List<ShopHomeShowcaseListItemUiModel> = listOf(),
        private val showcaseListWidgetListener: ShopHomeShowcaseListWidgetListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showcaseListItem: List<ShopHomeShowcaseListItemUiModel> = showcaseListItemData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ShopHomeShowcaseListSliderSmallViewHolder.ITEM_SLIDER_SMALL_LAYOUT -> {
                ShopHomeShowcaseListSliderSmallViewHolder(
                        View.inflate(
                                parent.context,
                                ShopHomeShowcaseListSliderSmallViewHolder.ITEM_SLIDER_SMALL_LAYOUT,
                                null
                        ),
                        showcaseListWidgetListener
                )
            }
            ShopHomeShowcaseListSliderMediumViewHolder.ITEM_SLIDER_MEDIUM_LAYOUT -> {
                ShopHomeShowcaseListSliderMediumViewHolder(
                        View.inflate(
                                parent.context,
                                ShopHomeShowcaseListSliderMediumViewHolder.ITEM_SLIDER_MEDIUM_LAYOUT,
                                null
                        ),
                        showcaseListWidgetListener
                )
            }
            ShopHomeShowcaseListGridViewHolder.ITEM_GRID_LAYOUT -> {
                ShopHomeShowcaseListGridViewHolder(
                        View.inflate(
                                parent.context,
                                ShopHomeShowcaseListGridViewHolder.ITEM_GRID_LAYOUT,
                                null
                        ),
                        showcaseListWidgetListener
                )
            }
            else -> {
                // default type is slider small viewholder
                ShopHomeShowcaseListSliderSmallViewHolder(
                        View.inflate(
                                parent.context,
                                ShopHomeShowcaseListSliderSmallViewHolder.ITEM_SLIDER_SMALL_LAYOUT,
                                null
                        ),
                        showcaseListWidgetListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShopHomeShowcaseListSliderSmallViewHolder -> holder.bind(showcaseListItem[position])
            is ShopHomeShowcaseListSliderMediumViewHolder -> holder.bind(showcaseListItem[position])
            is ShopHomeShowcaseListGridViewHolder -> holder.bind(showcaseListItem[position])
        }
    }

    override fun getItemCount(): Int {
        return showcaseListItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (showcaseListItem[position].viewType) {
            SHOWCASE_GRID_SMALL,
            SHOWCASE_GRID_MEDIUM,
            SHOWCASE_GRID_BIG -> ShopHomeShowcaseListGridViewHolder.ITEM_GRID_LAYOUT
            SHOWCASE_SLIDER_SMALL -> ShopHomeShowcaseListSliderSmallViewHolder.ITEM_SLIDER_SMALL_LAYOUT
            SHOWCASE_SLIDER_MEDIUM -> ShopHomeShowcaseListSliderMediumViewHolder.ITEM_SLIDER_MEDIUM_LAYOUT
            else -> ShopHomeShowcaseListSliderSmallViewHolder.ITEM_SLIDER_SMALL_LAYOUT
        }
    }

    fun updateDataSet(newList: List<ShopHomeShowcaseListItemUiModel>) {
        showcaseListItem = newList
        notifyDataSetChanged()
    }

}