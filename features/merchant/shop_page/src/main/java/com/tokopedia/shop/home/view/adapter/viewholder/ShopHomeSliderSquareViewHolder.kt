package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.WidgetSliderSquareBanner
import com.tokopedia.shop.home.view.adapter.PaddingItemDecorationShopPage
import com.tokopedia.shop.home.view.adapter.ShopHomeSliderSquareAdapter
import com.tokopedia.shop.home.view.model.WidgetModel
import kotlinx.android.synthetic.main.widget_shop_page_home_slider_banner.view.*

/**
 * Created by rizqiaryansa on 2020-02-25.
 */

class ShopHomeSliderSquareViewHolder(itemView: View) : AbstractViewHolder<WidgetModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_page_home_slider_banner
    }

    private val shopHomeSliderSquareAdapter by lazy { ShopHomeSliderSquareAdapter() }

    override fun bind(element: WidgetModel?) {
        val linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        itemView.rvCarouselShopPageHome.apply {
            layoutManager = linearLayoutManager
            addItemDecoration(PaddingItemDecorationShopPage(WidgetSliderSquareBanner))
            adapter = shopHomeSliderSquareAdapter
        }

        val listMultipleCarousel = element?.takeIf { it -> it.name == WidgetSliderSquareBanner }
        shopHomeSliderSquareAdapter.submitList(listMultipleCarousel?.data)

        itemView.tgHeaderSliderBanner.text = "New Axe Apollo"
    }
}