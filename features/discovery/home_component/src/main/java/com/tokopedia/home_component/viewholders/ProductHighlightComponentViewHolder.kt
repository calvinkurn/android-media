package com.tokopedia.home_component.viewholders

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.ProductHighlightListener
import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.*
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import kotlinx.android.synthetic.main.layout_product_highlight.view.*
import kotlinx.android.synthetic.main.layout_product_highlight.view.home_component_divider_footer
import kotlinx.android.synthetic.main.layout_product_highlight.view.home_component_divider_header
import java.util.*

class ProductHighlightComponentViewHolder(
        val view: View,
        val listener: HomeComponentListener?,
        private val productHighlightListener: ProductHighlightListener?
): AbstractViewHolder<ProductHighlightDataModel>(view) {

    private var isCacheData = false
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_product_highlight
    }

    override fun bind(element: ProductHighlightDataModel?) {
        isCacheData = element?.isCache ?: false
        element?.let {
            setDealsChannelInfo(it)
            setDealsProductGrid(it.channelModel)
        }
    }

    override fun bind(element: ProductHighlightDataModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setDealsChannelInfo(productHighlightDataModel: ProductHighlightDataModel) {
        setDealsChannelTitle(productHighlightDataModel.channelModel.channelHeader)
        setDealsCountDownTimer(productHighlightDataModel)
        setDealsChannelBackground(productHighlightDataModel.channelModel.channelBanner)
        setChannelDivider(productHighlightDataModel)
    }

    private fun setChannelDivider(element: ProductHighlightDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun setDealsCountDownTimer(dataModel: ProductHighlightDataModel) {
        itemView.deals_channel_subtitle.text = dataModel.channelModel.channelHeader.subtitle
        if (dataModel.channelModel.channelHeader.textColor.isNotEmpty()) {
            val textColor = Color.parseColor(dataModel.channelModel.channelHeader.textColor)
            itemView.deals_channel_subtitle.setTextColor(textColor)
        }

        if (dataModel.channelModel.channelHeader.expiredTime.isNotEmpty()) {
            val expiredTime = DateHelper.getExpiredTime(dataModel.channelModel.channelHeader.expiredTime)
            if (!DateHelper.isExpired(dataModel.channelModel.channelConfig.serverTimeOffset, expiredTime)) {
                itemView.deals_count_down?.run {
                    val defaultColor = "#${Integer.toHexString(ContextCompat.getColor(itemView.context, R.color.Unify_Static_White))}"
                    timerVariant = if(dataModel.channelModel.channelBanner.gradientColor.firstOrNull() != defaultColor || dataModel.channelModel.channelBanner.gradientColor.size > 1){
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visibility = View.VISIBLE

                    // calculate date diff
                    targetDate = Calendar.getInstance().apply {
                        val currentDate = Date()
                        val currentMillisecond: Long = currentDate.time + dataModel.channelModel.channelConfig.serverTimeOffset
                        val timeDiff = expiredTime.time - currentMillisecond
                        add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                        add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                        add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                    }
                    onFinish = {
                        listener?.onChannelExpired(dataModel.channelModel, adapterPosition, dataModel)
                    }

                }
            }
        } else {
            itemView.deals_count_down.visibility = View.GONE
        }
    }

    private fun setDealsChannelBackground(it: ChannelBanner) {
        itemView.deals_background.setGradientBackground(it.gradientColor)
    }

    private fun setDealsChannelTitle(it: ChannelHeader) {
        itemView.deals_channel_title.text = it.name
        if (it.textColor.isNotEmpty()) {
            val textColor = Color.parseColor(it.textColor)
            itemView.deals_channel_title.setTextColor(textColor)
        }
    }

    private fun setDealsProductGrid(channel: ChannelModel) {
        val grid = channel.channelGrids.firstOrNull()
        grid?.let {
            setDealsProductCard(channel, it)
            setDealsProductName(it.name)
            setDealsProductPrice(it.price)
            setDealsProductSlashedPrice(it.slashedPrice)
            setDealsProductImage(it.imageUrl)
            setDealsProductDiscountLabel(it.discount)
            setDealsProductStockbar(it.soldPercentage, it.label)
            setDealsProductFreeOngkir(it.isFreeOngkirActive, it.freeOngkirImageUrl)
            setDealsProductViewCount(it.productViewCountFormatted)
        }

    }

    private fun setDealsProductFreeOngkir(isFreeOngkir: Boolean, imageFreeOngkirUrl: String) {
        if (isFreeOngkir) {
            itemView.imageFreeOngkirPromo?.visibility = View.VISIBLE
            itemView.imageFreeOngkirPromo?.loadImage(imageFreeOngkirUrl)
        }
    }

    private fun setDealsProductCard(channel: ChannelModel, grid: ChannelGrid) {
        if (!isCacheData) {
            itemView.deals_product_card.addOnImpressionListener(channel) {
                productHighlightListener?.onProductCardImpressed(channel, grid, adapterPosition)
            }
        }
        itemView.deals_product_card.setOnClickListener {
            productHighlightListener?.onProductCardClicked(channel, grid, adapterPosition, grid.applink)
        }
    }

    private fun setDealsProductStockbar(soldPercentage: Int, label: String) {
        if (label.isNotEmpty()) {
            itemView.deals_stockbar.setValue(soldPercentage)
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
