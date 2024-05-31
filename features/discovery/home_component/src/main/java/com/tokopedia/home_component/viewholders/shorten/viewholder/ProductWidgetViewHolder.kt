package com.tokopedia.home_component.viewholders.shorten.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponent2squareProductWidgetBinding
import com.tokopedia.home_component.util.ShortenUtils
import com.tokopedia.home_component.util.ShortenUtils.TWO_SQUARE_MAX_PRODUCT_LIMIT
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.item.ItemContentCardAdapter
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.home_component.widget.card.timer.CountdownTimer
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.util.DateHelper
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel.Type as ItemTwoSquareType

class ProductWidgetViewHolder(
    view: View,
    pool: RecyclerView.RecycledViewPool?,
    private val listener: ContainerMultiTwoSquareListener
) : AbstractViewHolder<ProductWidgetUiModel>(view) {

    private val binding: GlobalComponent2squareProductWidgetBinding? by viewBinding()
    private var mAdapter: ItemContentCardAdapter? = null

    init {
        if (pool != null) {
            binding?.lstProductCard?.setRecycledViewPool(pool)
        }

        setupRecyclerView()
    }

    override fun bind(element: ProductWidgetUiModel?) {
        if (element == null) return

        setupWidgetHeader(element)
        widgetImpressionListener(element)
        mAdapter?.submitList(element.data)
    }

    private fun setupWidgetHeader(model: ProductWidgetUiModel) {
        val header = model.header
        handleCountdownTimer(header, ::shouldShowRetryWhenCampaignTimeout)

        binding?.txtHeader?.text = header.name
        binding?.txtHeader?.setOnClickListener { listener.productChannelHeaderClicked(model) }
        binding?.timerCountdown?.setOnClickListener { listener.productChannelHeaderClicked(model) }
    }

    private fun setupRecyclerView() {
        mAdapter = ItemContentCardAdapter(ItemTwoSquareType.Product, listener)
        binding?.lstProductCard?.layoutManager = GridLayoutManager(
            itemView.context,
            TWO_SQUARE_MAX_PRODUCT_LIMIT
        )
        binding?.lstProductCard?.adapter = mAdapter
        binding?.lstProductCard?.setHasFixedSize(true)
    }

    private fun handleCountdownTimer(header: ChannelHeader, onTimerFinished: () -> Unit) {
        if (header.expiredTime.isEmpty()) {
            binding?.timerCountdown?.gone()
            return
        }

        val expiredTime = DateHelper.getExpiredTime(header.expiredTime)
        val hasExpired = DateHelper.isExpired(header.serverTimeOffset, expiredTime)
        if (!hasExpired) {
            binding?.timerCountdown?.show()
            binding?.timerCountdown?.run {
                isShowClockIcon = false
                timerVariant = CountdownTimer.VARIANT_MAIN
                targetDate = ShortenUtils.createFormatTargetDate(
                    expiredTime = expiredTime,
                    serverTimeOffset = header.serverTimeOffset
                )
                onFinish = { onTimerFinished() }
            }
        }
    }

    private fun shouldShowRetryWhenCampaignTimeout() {
        binding?.retryContainer?.root?.show()

        binding?.retryContainer?.root?.setOnClickListener {
            it.gone()
            listener.retryWidget()
        }
    }

    private fun widgetImpressionListener(model: ProductWidgetUiModel) {
        itemView.addOnImpressionListener(model.impression, object : ViewHintListener {
            override fun onViewHint() {
                listener.productImpressed(model, bindingAdapterPosition)
            }
        })
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_component_2square_product_widget
    }
}
