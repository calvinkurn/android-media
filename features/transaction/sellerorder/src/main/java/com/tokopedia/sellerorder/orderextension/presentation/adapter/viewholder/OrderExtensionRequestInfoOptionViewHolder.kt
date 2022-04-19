package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.view.MotionEvent
import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemOrderExtensionRequestInfoOptionBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.utils.view.binding.viewBinding

class OrderExtensionRequestInfoOptionViewHolder(
    itemView: View?,
    private val listener: SomRequestExtensionOptionListener
) : BaseOrderExtensionRequestInfoViewHolder<OrderExtensionRequestInfoUiModel.OptionUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_order_extension_request_info_option
    }

    private val binding by viewBinding<ItemOrderExtensionRequestInfoOptionBinding>()

    override fun bind(element: OrderExtensionRequestInfoUiModel.OptionUiModel?) {
        super.bind(element)
        element?.run {
            setupOptionName(name)
            setupStatusWithoutAnimation(selected)
        }
    }

    override fun bind(
        element: OrderExtensionRequestInfoUiModel.OptionUiModel?,
        payloads: MutableList<Any>
    ) {
        super.bind(element, payloads)
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OrderExtensionRequestInfoUiModel.OptionUiModel && newItem is OrderExtensionRequestInfoUiModel.OptionUiModel) {
                    if (oldItem.name != newItem.name) {
                        setupOptionName(newItem.name)
                    }
                    if (oldItem.selected != newItem.selected) {
                        setupStatusWithAnimation(newItem.selected)
                    }
                    return
                }
            }
        }
    }

    override fun onTap(event: MotionEvent?): Boolean {
        super.onTap(event)
        listener.onOptionChecked(element)
        return true
    }

    private fun setupOptionName(name: String) {
        binding?.root?.text = name
    }

    private fun setupStatusWithAnimation(selected: Boolean) {
        binding?.root?.isChecked = selected
    }

    private fun setupStatusWithoutAnimation(selected: Boolean) {
        binding?.root?.run {
            isChecked = selected
            skipAnimation()
        }
    }

    interface SomRequestExtensionOptionListener {
        fun onOptionChecked(element: OrderExtensionRequestInfoUiModel.OptionUiModel?)
    }
}