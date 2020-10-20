package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.PaddingItemDecorationShopPage
import com.tokopedia.shop.home.view.adapter.ShopHomeMultipleImageColumnAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.widget_shop_home_multiple_image_column.view.*

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeMultipleImageColumnViewHolder(
        itemView: View,
        private val previousViewHolder: AbstractViewHolder<*>?,
        private val listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_home_multiple_image_column
        private const val SPAN_SIZE_SINGLE = 6
        private const val SPAN_SIZE_DOUBLE = 3
        private const val SPAN_SIZE_TRIPLE = 2
    }

    private var shopHomeMultipleImageColumnAdapter: ShopHomeMultipleImageColumnAdapter? = null

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        shopHomeMultipleImageColumnAdapter = ShopHomeMultipleImageColumnAdapter(listener)
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
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecorationShopPage(element.name))
            }
            adapter = shopHomeMultipleImageColumnAdapter
        }
        itemView.textViewTitle?.apply {
            if (element.header.title.isEmpty()) {
                hide()
                if (previousViewHolder is ShopHomeSliderSquareViewHolder || previousViewHolder is ShopHomeCarousellProductViewHolder) {
                    (itemView.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                        setMargins(leftMargin, 16.toPx(), rightMargin, bottomMargin)
                    }
                }
            } else {
                text = element.header.title
                show()
            }
        }

        shopHomeMultipleImageColumnAdapter?.setShopHomeDisplayWidgetUiModelData(element)
        shopHomeMultipleImageColumnAdapter?.setParentPosition(adapterPosition)
        shopHomeMultipleImageColumnAdapter?.setHeightRatio(getHeightRatio(element))
        shopHomeMultipleImageColumnAdapter?.submitList(element.data)
    }

    private fun getIndexRatio(data: ShopHomeDisplayWidgetUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index).toIntOrZero()
    }

    private fun getHeightRatio(element: ShopHomeDisplayWidgetUiModel): Float {
        val indexZero = getIndexRatio(element, 0).toFloat()
        val indexOne = getIndexRatio(element, 1).toFloat()
        return (indexOne / indexZero)
    }
}