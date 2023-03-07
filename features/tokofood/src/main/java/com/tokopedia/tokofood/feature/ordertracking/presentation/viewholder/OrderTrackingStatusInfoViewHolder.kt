package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingStatusInfoSectionBinding
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.partialview.OrderTrackingStatusInfoWidget
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StepperStatusUiModel

class OrderTrackingStatusInfoViewHolder(
    itemView: View
) : CustomPayloadViewHolder<OrderTrackingStatusInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_status_info_section
    }

    private val binding = ItemTokofoodOrderTrackingStatusInfoSectionBinding.bind(itemView)

    override fun bind(element: OrderTrackingStatusInfoUiModel) {
        with(binding) {
            orderStatusInfoWidget.setupStatusInfoWidget(element)
            setStepperListView(element.stepperStatusList)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { payload ->
            val (oldItem, newItem) = payload

            if (oldItem is OrderTrackingStatusInfoUiModel && newItem is OrderTrackingStatusInfoUiModel) {
                updateStatusLottieUrl(oldItem, newItem)
                updateStatusTitle(oldItem, newItem)
                updateStatusSubTitle(oldItem, newItem)
                updateStepperListView(oldItem, newItem)
            }
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        clearStatusLottie()
    }

    private fun updateStatusLottieUrl(
        oldItem: OrderTrackingStatusInfoUiModel,
        newItem: OrderTrackingStatusInfoUiModel
    ) {
        if (oldItem.lottieUrl != newItem.lottieUrl) {
            binding.orderStatusInfoWidget.updateLottie(newItem.statusKey, newItem.lottieUrl)
        }
    }

    private fun clearStatusLottie() {
        binding.orderStatusInfoWidget.clearLottie()
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
            binding.orderStatusInfoWidget.setOrderTrackingStatusSubTitle(newItem.orderStatusSubTitle)
        }
    }

    private fun OrderTrackingStatusInfoWidget.setupStatusInfoWidget(item: OrderTrackingStatusInfoUiModel) {
        setupLottie(item.lottieUrl)
        setOrderTrackingStatusTitle(item.orderStatusTitle)
        setOrderTrackingStatusSubTitle(item.orderStatusSubTitle)
    }

    private fun setStepperListView(stepperStatusList: List<StepperStatusUiModel>) {
        binding.orderStatusStepperListView.setStepperStatusListView(stepperStatusList)
    }

    private fun updateStepperListView(
        oldItem: OrderTrackingStatusInfoUiModel,
        newItem: OrderTrackingStatusInfoUiModel
    ) {
        if (oldItem.stepperStatusList != newItem.stepperStatusList) {
            binding.orderStatusStepperListView.updateStepperStatus(newItem.stepperStatusList)
        }
    }

}