package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemOrderExtensionRequestInfoPickTimeBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.view.binding.viewBinding

class OrderExtensionRequestInfoPickTimeViewHolder(
    itemView: View?,
    val onPickTimeListener: SomRequestExtensionPickTimeListener
) : AbstractViewHolder<OrderExtensionRequestInfoUiModel.PickTimeUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_order_extension_request_info_pick_time
    }

    private val binding by viewBinding<ItemOrderExtensionRequestInfoPickTimeBinding>()

    override fun bind(element: OrderExtensionRequestInfoUiModel.PickTimeUiModel?) {
        if (element?.timeText.orEmpty().isNotEmpty()) {
            binding?.pickTimeText?.text = element?.timeText
        }
        binding?.pickTimeText?.setOnClickListener {
            onPickTimeListener.onShowCalendarPicker()
        }
        binding?.pickTimeText?.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.som_calendar,
            0,
            R.drawable.som_chevron_down,
            0
        )
        binding?.ivInformation?.setOnClickListener {
            onPickTimeListener.onShowTooltip()
        }

        binding?.tvDescription?.text =
            HtmlLinkHelper(
                itemView.context,
                getString(R.string.bottomsheet_order_extension_request_pick_time_rules)
            ).spannedString ?: ""

    }

    interface SomRequestExtensionPickTimeListener {
        fun onShowCalendarPicker()
        fun onShowTooltip()
    }
}
