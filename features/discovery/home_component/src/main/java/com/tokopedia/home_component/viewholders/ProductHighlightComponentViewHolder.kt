package com.tokopedia.home_component.viewholders

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.LayoutProductHighlightBinding
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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class ProductHighlightComponentViewHolder(
        val view: View,
        val listener: HomeComponentListener?,
        private val productHighlightListener: ProductHighlightListener?
): AbstractViewHolder<ProductHighlightDataModel>(view) {
    private var binding: LayoutProductHighlightBinding? by viewBinding()
    private var isCacheData = false
    private var masterProductCardListView: ProductCardListView? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_product_highlight
        const val TITLE_LENGTH = 22
        const val START_INDEX = 0
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
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setDealsCountDownTimer(dataModel: ProductHighlightDataModel) {
        binding?.dealsChannelSubtitle?.text = dataModel.channelModel.channelHeader.subtitle
        if (dataModel.channelModel.channelHeader.textColor.isNotEmpty()) {
            val textColor = Color.parseColor(dataModel.channelModel.channelHeader.textColor)
            binding?.dealsChannelSubtitle?.setTextColor(textColor)
        }

        if (dataModel.channelModel.channelHeader.expiredTime.isNotEmpty()) {
            val expiredTime = DateHelper.getExpiredTime(dataModel.channelModel.channelHeader.expiredTime)
            if (!DateHelper.isExpired(dataModel.channelModel.channelConfig.serverTimeOffset, expiredTime)) {
                binding?.dealsCountDown?.run {
                    val defaultColor = "#${Integer.toHexString(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))}"
                    timerVariant = if(dataModel.channelModel.channelBanner.gradientColor.firstOrNull() != defaultColor || dataModel.channelModel.channelBanner.gradientColor.size > 1){
                        TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        TimerUnifySingle.VARIANT_MAIN
                    }

                    visible()

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
            binding?.dealsCountDown?.gone()
            binding?.dealsChannelSubtitle?.gone()
        }
    }

    private fun setDealsChannelBackground(it: ChannelBanner) {
        binding?.dealsBackground?.setGradientBackground(it.gradientColor)
    }

    private fun setDealsChannelTitle(it: ChannelHeader) {
        val title = if (it.name.length > TITLE_LENGTH) "${
            it.name.substring(
                START_INDEX,
                TITLE_LENGTH
            )
        }${getString(R.string.discovery_home_product_highlight_ellipsize_character)}" else it.name
        binding?.dealsChannelTitle?.text = title
        if (it.textColor.isNotEmpty()) {
            val textColor = Color.parseColor(it.textColor)
            binding?.dealsChannelTitle?.setTextColor(textColor)
        }
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

}