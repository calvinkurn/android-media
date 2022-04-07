package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingStatusInfoSectionBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.RecyclerViewPollerListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.StepperStatusAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.partialview.OrderTrackingStatusInfoWidget
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel

class OrderTrackingStatusInfoViewHolder(
    itemView: View,
    private val recyclerViewPollerListener: RecyclerViewPollerListener
) : BaseOrderTrackingViewHolder<OrderTrackingStatusInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_status_info_section

        const val SPAN_WIDTH_DEFAULT = 2
        const val SPAN_WIDTH_LAST_ITEM = 1
        const val MAX_SPAN_COUNT = 7
    }

    private val binding = ItemTokofoodOrderTrackingStatusInfoSectionBinding.bind(itemView)

    private var stepperStatusAdapter: StepperStatusAdapter? = null

    override fun bind(element: OrderTrackingStatusInfoUiModel) {
        with(binding) {
            orderStatusInfoWidget.setupStatusInfoWidget(element)
            setupStepperStatusAdapter(element)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { payload ->
            val (oldItem, newItem) = payload

            if (oldItem is OrderTrackingStatusInfoUiModel && newItem is OrderTrackingStatusInfoUiModel) {
                updateStatusLottieUrl(oldItem, newItem)
                updateStatusTitle(oldItem, newItem)
                updateStatusSubTitle(oldItem, newItem)
                updateStepperStatus(oldItem, newItem)
            }
        }
    }

    private fun updateStatusLottieUrl(
        oldItem: OrderTrackingStatusInfoUiModel,
        newItem: OrderTrackingStatusInfoUiModel
    ) {
        if (oldItem.lottieUrl != newItem.lottieUrl) {
            binding.orderStatusInfoWidget.updateLottie(newItem.statusKey, newItem.lottieUrl)
        }
    }

    private fun updateStatusTitle(
        oldItem: OrderTrackingStatusInfoUiModel,
        newItem: OrderTrackingStatusInfoUiModel
    ) {
        if (oldItem.orderStatusTitle != newItem.orderStatusTitle) {
            binding.orderStatusInfoWidget.setOrderTrackingStatusTitle(newItem.orderStatusTitle)
        }
    }

    private fun updateStatusSubTitle(
        oldItem: OrderTrackingStatusInfoUiModel,
        newItem: OrderTrackingStatusInfoUiModel
    ) {
        if (oldItem.orderStatusSubTitle != newItem.orderStatusSubTitle) {
            binding.orderStatusInfoWidget.setOrderTrackingStatusTitle(newItem.orderStatusSubTitle)
        }
    }

    private fun updateStepperStatus(
        oldItem: OrderTrackingStatusInfoUiModel,
        newItem: OrderTrackingStatusInfoUiModel
    ) {
        if (oldItem.stepperStatusList != newItem.stepperStatusList) {
            stepperStatusAdapter?.setStepperList(newItem.stepperStatusList)
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
            setHasFixedSize(true)
            setRecycledViewPool(recyclerViewPollerListener.parentPool)
            layoutManager = gridLayoutManager
            adapter = stepperStatusAdapter
        }

        stepperStatusAdapter?.setStepperList(item.stepperStatusList)
    }

    interface Listener {
        fun getRecyclerViewPoolStatusInfo(): RecyclerView.RecycledViewPool
    }


}