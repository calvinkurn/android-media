package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.ProductHighlightTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.FreeOngkir
import com.tokopedia.home.beranda.helper.DateHelper
import com.tokopedia.home.beranda.helper.glide.FPM_DEALS_WIDGET_PRODUCT_IMAGE
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import kotlinx.android.synthetic.main.home_dc_deals.view.*

class ProductHighlightViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<DynamicChannelDataModel>(view) {

    companion object {
        @LayoutRes val LAYOUT = R.layout.home_dc_deals
    }

    override fun bind(element: DynamicChannelDataModel?) {
        element?.let {
            setDealsChannelInfo(it)
            setDealsProductGrid(it)
        }
    }

    override fun bind(element: DynamicChannelDataModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setDealsChannelInfo(channel: DynamicChannelDataModel) {
        channel.channel?.header?.let {
            setDealsChannelTitle(it)
            setDealsCountDownTimer(it, channel)
        }

        channel.channel?.banner?.let {
            setDealsChannelBackground(it)
        }
    }

    private fun setDealsCountDownTimer(it: DynamicHomeChannel.Header, channel: DynamicChannelDataModel) {
        itemView.deals_channel_subtitle.text = it.subtitle
        if (it.textColor.isNotEmpty()) {
            val textColor = Color.parseColor(it.textColor)
            itemView.deals_channel_subtitle.setTextColor(textColor)
        }

        if (it.expiredTime.isNotEmpty()) {
            val expiredTime = DateHelper.getExpiredTime(it.expiredTime)
            if (!DateHelper.isExpired(channel.serverTimeOffset, expiredTime)) {
                itemView.deals_count_down.setup(
                        channel.serverTimeOffset,
                        expiredTime
                ){
                    listener.updateExpiredChannel(channel, adapterPosition)
                }
                itemView.deals_count_down.visibility = View.VISIBLE
            }
        } else {
            itemView.deals_count_down.visibility = View.GONE
        }
    }

    private fun setDealsChannelBackground(it: DynamicHomeChannel.Banner) {
        itemView.deals_background.setGradientBackground(it.gradientColor)
    }

    private fun setDealsChannelTitle(it: DynamicHomeChannel.Header) {
        itemView.deals_channel_title.text = it.name
        if (it.textColor.isNotEmpty()) {
            val textColor = Color.parseColor(it.textColor)
            itemView.deals_channel_title.setTextColor(textColor)
        }
    }

    private fun setDealsProductGrid(channel: DynamicChannelDataModel) {
        channel.channel?.let { channel ->
            val grid = channel.grids.firstOrNull()
            grid?.let {
                setDealsProductCard(channel, it)
                setDealsProductName(it.name)
                setDealsProductPrice(it.price)
                setDealsProductSlashedPrice(it.slashedPrice)
                setDealsProductImage(it.imageUrl)
                setDealsProductDiscountLabel(it.discount)
                setDealsProductStockbar(it.soldPercentage, it.label)
                setDealsProductFreeOngkir(it.freeOngkir)
                setDealsProductViewCount(it.productViewCountFormatted)
            }
        }
    }

    private fun setDealsProductFreeOngkir(freeOngkir: FreeOngkir) {
        if (freeOngkir.isActive) {
            itemView.imageFreeOngkirPromo?.visibility = View.VISIBLE
            itemView.imageFreeOngkirPromo?.loadImage(freeOngkir.imageUrl)
        }
    }

    private fun setDealsProductCard(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid) {
        itemView.deals_product_card.setOnClickListener {
            listener.onSectionItemClicked(grid.applink)
            ProductHighlightTracking.sendRecommendationListClick(
                    channelId = channel.id,
                    headerName = channel.header.name,
                    campaignCode = channel.campaignCode,
                    persoType = channel.persoType,
                    categoryId = channel.categoryID,
                    gridFreeOngkirIsActive = grid.freeOngkir.isActive,
                    gridId = grid.id,
                    gridName = grid.name,
                    gridPrice = grid.price,
                    position = adapterPosition
            )
        }
    }

    private fun setDealsProductStockbar(soldPercentage: Int, label: String) {
        if (label.isNotEmpty()) {
            itemView.deals_stockbar.progress = soldPercentage
            itemView.deals_stockbar_label.text = label
        } else {
            itemView.deals_stockbar.visibility = View.GONE
            itemView.deals_stockbar_label.visibility = View.GONE
        }
    }

    private fun setDealsProductViewCount(productViewCount: String) {
        itemView.deals_product_view_count.displayTextOrHide(productViewCount)
    }

    private fun setDealsProductDiscountLabel(label: String) {
        itemView.deals_product_discount_label.setLabel(label)
    }

    private fun setDealsProductImage(imageUrl: String) {
        itemView.deals_product_image.loadImageRounded(
                imageUrl,
                16,
                FPM_DEALS_WIDGET_PRODUCT_IMAGE)
    }

    private fun setDealsProductSlashedPrice(slashedPrice: String) {
        itemView.deals_product_slashed_price.displayTextOrHide(slashedPrice)
        itemView.deals_product_slashed_price.paintFlags = itemView.deals_product_slashed_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun setDealsProductPrice(price: String) {
        itemView.deals_product_price.displayTextOrHide(price)
    }

    private fun setDealsProductName(productName: String) {
        itemView.deals_product_name.displayTextOrHide(productName)
    }
}
