package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.ItemPofProductEditableBinding
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.PofAdapterTypeFactory
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofProductEditableUiModel
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.UiEvent

class PofProductEditableViewHolder(
    view: View,
    private val listener: PofAdapterTypeFactory.Listener
) : AbstractViewHolder<PofProductEditableUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pof_product_editable
    }

    private val binding = ItemPofProductEditableBinding.bind(view)
    private var textWatcher: TextWatcher? = null

    override fun bind(element: PofProductEditableUiModel) {
        setupProductImage(element.productImageUrl)
        setupProductName(element.productName)
        setupProductPriceQuantity(element.productPriceQuantity)
        setupProductQuantityEditor(element.quantityEditorData)
    }

    override fun bind(element: PofProductEditableUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setupProductQuantityEditor(quantityEditorData: PofProductEditableUiModel.QuantityEditorData) {
        binding.qePofProductQuantity.editText.removeTextChangedListener(textWatcher)
        binding.qePofProductQuantity.minValue = Int.ZERO
        binding.qePofProductQuantity.maxValue = quantityEditorData.maxQuantity
        binding.qePofProductQuantity.setValue(quantityEditorData.quantity)
        binding.qePofProductQuantity.isEnabled = quantityEditorData.enabled
        binding.qePofProductQuantity.subtractButton.setOnClickListener {
            onSubtractButtonClicked(binding.qePofProductQuantity.getValue(), quantityEditorData.minQuantity)
        }
        binding.qePofProductQuantity.subtractButtonAnimationView?.setOnClickListener {
            onSubtractButtonClicked(binding.qePofProductQuantity.getValue(), quantityEditorData.minQuantity)
        }
        binding.qePofProductQuantity.editText.addTextChangedListener(initTextWatcher(quantityEditorData))
    }

    private fun setupProductImage(productImageUrl: String) {
        binding.ivPofProductImage.loadImage(productImageUrl)
    }

    private fun setupProductName(productName: String) {
        binding.tvPofProductName.text = productName
    }

    private fun setupProductPriceQuantity(productPriceQuantity: String) {
        binding.tvPofProductPriceQuantity.text = productPriceQuantity
    }

    private fun onSubtractButtonClicked(quantity: Int, minQuantity: Int) {
        if (quantity == minQuantity) {
            listener.onEvent(UiEvent.OnTryChangeProductQuantityBelowMinQuantity)
        } else {
            binding.qePofProductQuantity.setValue(quantity.dec())
        }
    }

    private fun initTextWatcher(
        quantityEditorData: PofProductEditableUiModel.QuantityEditorData
    ): TextWatcher {
        return QuantityChangeListener(quantityEditorData).apply { textWatcher = this }
    }

    private inner class QuantityChangeListener(
        private val quantityEditorData: PofProductEditableUiModel.QuantityEditorData
    ): TextWatcher {
        // Keep track of the previous value because somehow afterTextChanged is called twice
        // when the value changed
        private var previousValue = quantityEditorData.quantity
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // noop
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // noop
        }

        override fun afterTextChanged(text: Editable?) {
            val quantityText = text?.toString().orEmpty().filter { it.isDigit() }
            val quantityNumber = quantityText.toIntOrZero()
            if (quantityText.isNotBlank() && quantityNumber != previousValue) {
                if (quantityNumber < quantityEditorData.minQuantity) {
                    onTryChangeProductQuantityBelowMinQuantity()
                } else if (quantityNumber > quantityEditorData.maxQuantity) {
                    onTryChangeProductQuantityAboveMaxQuantity()
                } else {
                    onProductQuantityChanged(quantityNumber)
                }
            }
        }

        private fun onTryChangeProductQuantityBelowMinQuantity() {
            binding.qePofProductQuantity.editText.removeTextChangedListener(this)
            binding.qePofProductQuantity.setValue(quantityEditorData.quantity)
            previousValue = quantityEditorData.quantity
            binding.qePofProductQuantity.editText.addTextChangedListener(this)
            listener.onEvent(UiEvent.OnTryChangeProductQuantityBelowMinQuantity)
        }

        private fun onTryChangeProductQuantityAboveMaxQuantity() {
            binding.qePofProductQuantity.editText.removeTextChangedListener(this)
            binding.qePofProductQuantity.setValue(quantityEditorData.quantity)
            previousValue = quantityEditorData.quantity
            binding.qePofProductQuantity.editText.addTextChangedListener(this)
            listener.onEvent(UiEvent.OnTryChangeProductQuantityAboveMaxQuantity)
        }

        private fun onProductQuantityChanged(quantity: Int) {
            listener.onEvent(
                UiEvent.ProductQuantityChanged(
                    orderDetailId = quantityEditorData.orderDetailId,
                    newQuantity = quantity,
                    oldQuantity = quantityEditorData.quantity
                )
            )
            previousValue = quantity
        }
    }
}
