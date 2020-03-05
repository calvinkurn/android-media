package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.PaddingItemDecorationShopPage
import com.tokopedia.shop.home.view.adapter.ShopHomeMultipleImageColumnAdapter
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import kotlinx.android.synthetic.main.widget_shop_home_multiple_image_column.view.*

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeMultipleImageColumnViewHolder(
        itemView: View,
        shopHomeMultipleImageColumnListener: ShopHomeMultipleImageColumnListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_home_multiple_image_column

        private const val SPAN_SIZE_SINGLE = 6
        private const val SPAN_SIZE_DOUBLE = 3
        private const val SPAN_SIZE_TRIPLE = 2
    }

    interface ShopHomeMultipleImageColumnListener{
        fun onMultipleImageColumnItemImpression(
                displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
                displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem,
                parentPosition: Int,
                adapterPosition: Int
        )

        fun onMultipleImageColumnItemClicked(
                displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?,
                displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem
        )
    }

    private val shopHomeMultipleImageColumnAdapter by lazy { ShopHomeMultipleImageColumnAdapter(
            shopHomeMultipleImageColumnListener
    ) }

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        val gridLayoutManager = GridLayoutManager(itemView.context, SPAN_SIZE_SINGLE)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (element.data?.size) {
                    2 -> SPAN_SIZE_DOUBLE
                    3 -> SPAN_SIZE_TRIPLE
                    else -> SPAN_SIZE_SINGLE
                }
            }
        }
        itemView.rvShopHomeMultiple.apply {
            layoutManager = gridLayoutManager
            addItemDecoration(PaddingItemDecorationShopPage(element.name))
            adapter = shopHomeMultipleImageColumnAdapter
        }
        shopHomeMultipleImageColumnAdapter.setShopHomeDisplayWidgetUiModelData(element)
        shopHomeMultipleImageColumnAdapter.setParentPosition(adapterPosition)
        shopHomeMultipleImageColumnAdapter.submitList(element.data)
    }
}