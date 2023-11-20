package com.tokopedia.home_component.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.LayoutProductHighlightBinding
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.ProductHighlightListener
import com.tokopedia.home_component.mapper.ProductHighlightModelMapper
import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.home_component_header.util.HomeChannelHeaderRollenceController
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*
import kotlin.collections.ArrayList

class ProductHighlightComponentViewHolder(
    val view: View,
    val listener: HomeComponentListener?,
    private val productHighlightListener: ProductHighlightListener?,
    private val cardInteraction: Boolean = false
) : AbstractViewHolder<ProductHighlightDataModel>(view) {
    private var binding: LayoutProductHighlightBinding? by viewBinding()
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
        masterProductCardListView = itemView.findViewById<ProductCardListView?>(R.id.master_product_card_deals)
    }

    private fun setDealsChannelInfo(productHighlightDataModel: ProductHighlightDataModel) {
        setHeaderComponent(productHighlightDataModel)
        setDealsChannelBackground(productHighlightDataModel.channelModel.channelBanner)
        setChannelDivider(productHighlightDataModel)
    }

    private fun setChannelDivider(element: ProductHighlightDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setDealsChannelBackground(it: ChannelBanner) {
        binding?.dealsBackground?.setGradientBackground(it.gradientColor)
    }

    private fun setHeaderComponent(element: ProductHighlightDataModel) {
        binding?.homeComponentHeaderView?.setChannel(
            element.channelModel,
            object : HeaderListener {
                override fun onChannelExpired(channelModel: ChannelModel) {
                    listener?.onChannelExpired(channelModel, channelModel.verticalPosition, element)
                }
            },
            colorMode = element.channelModel.channelBanner.gradientColor.getColorMode()
        )
        val topPadding = if (!HomeChannelHeaderRollenceController.isHeaderUsingRollenceVariant()) {
            view.context.resources.getDimensionPixelSize(R.dimen.home_product_highlight_content_top_padding)
        } else {
            0
        }
        binding?.masterProductCardDeals?.setMargin(
            view.context.resources.getDimensionPixelSize(R.dimen.home_product_highlight_content_horizontal_padding),
            topPadding,
            view.context.resources.getDimensionPixelSize(R.dimen.home_product_highlight_content_horizontal_padding),
            0
        )
    }

    @SuppressLint("ResourceType")
    private fun ArrayList<String>.getColorMode(): Int {
        val hexWhite = itemView.context.resources.getString(com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        // check if get empty or all white gradient color, then use mode normal
        return if (isEmpty() || get(0).lowercase(Locale.getDefault()).take(7) == hexWhite.lowercase().take(7)) {
            DynamicChannelHeaderView.COLOR_MODE_NORMAL
        } else {
            DynamicChannelHeaderView.COLOR_MODE_INVERTED
        }
    }

    private fun setDealsProductGrid(channel: ChannelModel) {
        val grid = channel.channelGrids.firstOrNull()
        val channelDataModel = grid?.let { ProductHighlightModelMapper.mapToProductCardModel(it, cardInteraction) }
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
}
