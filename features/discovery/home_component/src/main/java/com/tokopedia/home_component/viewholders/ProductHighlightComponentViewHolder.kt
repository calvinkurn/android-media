package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.ProductHighlightListener
import com.tokopedia.home_component.mapper.ProductHighlightModelMapper
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardListView
import kotlinx.android.synthetic.main.layout_product_highlight.view.*

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
        setChannelDivider(productHighlightDataModel)
        setHeaderComponent(productHighlightDataModel)
    }

    private fun setChannelDivider(element: ProductHighlightDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun setDealsProductGrid(channel: ChannelModel) {
        val grid = channel.channelGrids.firstOrNull()
        val channelDataModel = grid?.let { ProductHighlightModelMapper.mapToProductCardModel(it) }
        channelDataModel?.let {
            masterProductCardListView?.setProductModel(it)
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

    private fun setHeaderComponent(element: ProductHighlightDataModel) {
        element.channelModel.let {
            itemView.home_component_header_view.setChannel(it, object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                }

                override fun onChannelExpired(channelModel: ChannelModel) {
                    listener?.onChannelExpired(channelModel, adapterPosition, element)
                }
            })
        }
    }

}