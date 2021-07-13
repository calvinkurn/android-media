package com.tokopedia.home_component.viewholders

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.ProductHighlightListener
import com.tokopedia.home_component.mapper.ProductHighlightModelMapper
import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight
import kotlinx.android.synthetic.main.layout_product_highlight.view.*
import java.util.*

class ProductHighlightComponentViewHolder(
        val view: View,
        val listener: HomeComponentListener?,
        private val productHighlightListener: ProductHighlightListener?
): AbstractViewHolder<ProductHighlightDataModel>(view) {

    private var isCacheData = false
    private var masterProductCardListView: ProductCardListView? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_product_highlight
    }

    override fun bind(element: ProductHighlightDataModel?) {
        isCacheData = element?.isCache ?: false
        initView()
        element?.let {
            setDealsChannelInfo(it)
            setDealsProductGrid(it.channelModel)
        }
    }

    override fun bind(element: ProductHighlightDataModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun initView() {
        masterProductCardListView = itemView.findViewById(R.id.master_product_card_deals)
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
                        TimerUnifyHighlight.VARIANT_ALTERNATE
                    } else {
                        TimerUnifyHighlight.VARIANT_DARK_RED
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
        val channelDataModel = grid?.let { ProductHighlightModelMapper.mapToProductCardModel(it) }
        if (channelDataModel != null) {
            masterProductCardListView?.setProductModel(channelDataModel)
        }
        grid?.let { setDealsProductCard(channel, it) }
    }

    private fun setDealsProductCard(channel: ChannelModel, grid: ChannelGrid) {
        if (!isCacheData) {
            masterProductCardListView?.addOnImpressionListener(channel) {
                productHighlightListener?.onProductCardImpressed(channel, grid, adapterPosition)
            }
        }
        masterProductCardListView?.setOnClickListener {
            productHighlightListener?.onProductCardClicked(channel, grid, adapterPosition, grid.applink)
        }
    }

}