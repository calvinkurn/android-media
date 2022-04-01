package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingStatusInfoSectionBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.StepperStatusAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.partialview.OrderTrackingStatusInfoWidget
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel

class OrderTrackingStatusInfoViewHolder(itemView: View) :
    AbstractViewHolder<OrderTrackingStatusInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_status_info_section

        const val SPAN_WIDTH_DEFAULT = 2
        const val SPAN_WIDTH_LAST_ITEM = 1
        const val MAX_SPAN_COUNT = 7
    }

    private val binding = ItemTokofoodOrderTrackingStatusInfoSectionBinding.bind(itemView)

    private var stepperStatusAdapter: StepperStatusAdapter? = null

    override fun bind(element: OrderTrackingStatusInfoUiModel?) {
        if (element == null) return
        with(binding) {
            orderStatusInfoWidget.setupStatusInfoWidget(element)
            setupStepperStatusAdapter(element)
        }
    }

    override fun bind(element: OrderTrackingStatusInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let { payload ->
            if (payload is Pair<*, *>) {
                val (oldItem, newItem) = payload

                if (oldItem is OrderTrackingStatusInfoUiModel && newItem is OrderTrackingStatusInfoUiModel) {
                    updateStatusLottieUrl(oldItem, newItem)
                    updateStatusTitle(oldItem, newItem)
                    updateStatusSubTitle(oldItem, newItem)
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun updateStatusLottieUrl(oldItem: OrderTrackingStatusInfoUiModel, newItem: OrderTrackingStatusInfoUiModel) {
        if (oldItem.lottieUrl != newItem.lottieUrl) {
            binding.orderStatusInfoWidget.updateLottie(newItem.statusKey, newItem.lottieUrl)
        }
    }

    private fun updateStatusTitle(oldItem: OrderTrackingStatusInfoUiModel, newItem: OrderTrackingStatusInfoUiModel) {
        if (oldItem.orderStatusTitle != newItem.orderStatusTitle) {
            binding.orderStatusInfoWidget.setOrderTrackingStatusTitle(newItem.orderStatusTitle)
        }
    }

    private fun updateStatusSubTitle(oldItem: OrderTrackingStatusInfoUiModel, newItem: OrderTrackingStatusInfoUiModel) {
        if (oldItem.orderStatusSubTitle != newItem.orderStatusSubTitle) {
            binding.orderStatusInfoWidget.setOrderTrackingStatusTitle(newItem.orderStatusSubTitle)
        }
    }

    private fun OrderTrackingStatusInfoWidget.setupStatusInfoWidget(item: OrderTrackingStatusInfoUiModel) {
        setupLottie(item.lottieUrl)
        setOrderTrackingStatusTitle(item.orderStatusTitle)
        setOrderTrackingStatusSubTitle(item.orderStatusSubTitle)
    }

    private fun setupStepperStatusAdapter(item: OrderTrackingStatusInfoUiModel) {
        stepperStatusAdapter = StepperStatusAdapter()

        val gridLayoutManager = GridLayoutManager(itemView.context, MAX_SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (item.stepperStatusList.size == position + Int.ONE) {
                    SPAN_WIDTH_LAST_ITEM
                } else SPAN_WIDTH_DEFAULT
            }
        }

        binding.rvOrderStatusStepper.run {
            layoutManager = gridLayoutManager
            adapter = stepperStatusAdapter
        }

        stepperStatusAdapter?.setStepperList(item.stepperStatusList)
    }
}