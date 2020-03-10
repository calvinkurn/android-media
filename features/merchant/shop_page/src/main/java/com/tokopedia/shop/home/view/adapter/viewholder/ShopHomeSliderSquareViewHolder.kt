package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.PaddingItemDecorationShopPage
import com.tokopedia.shop.home.view.adapter.ShopHomeSliderSquareAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import kotlinx.android.synthetic.main.widget_shop_page_home_slider_square.view.*

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderSquareViewHolder(
        itemView: View,
        listener: ShopHomeDisplayWidgetListener
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_page_home_slider_square
    }

    private val shopHomeSliderSquareAdapter by lazy { ShopHomeSliderSquareAdapter(listener) }

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        val linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        itemView.rvCarouselShopPageHome.apply {
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecorationShopPage(element.name))
            }
            adapter = shopHomeSliderSquareAdapter
        }
        shopHomeSliderSquareAdapter.submitList(element.data)
        itemView.tgHeaderSliderSquare.text = element.header.title
    }
}