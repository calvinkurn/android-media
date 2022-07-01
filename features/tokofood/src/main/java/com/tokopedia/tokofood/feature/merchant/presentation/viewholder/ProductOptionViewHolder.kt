package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnItemLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel

class ProductOptionViewHolder(private val binding: TokofoodItemAddOnItemLayoutBinding,
                              private val listener: Listener) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(uiModel: OptionUiModel, index: Int) {
        setTitle(uiModel.name)
        setDescription(uiModel.priceFmt, uiModel.isOutOfStock)
        setSelectionType(uiModel.isSelected, uiModel.selectionControlType)
        setEnabled(uiModel.isOutOfStock, uiModel.canBeSelected)
        setOnClickAction(uiModel.selectionControlType, uiModel.isOutOfStock)
        setSelectionClickListener(uiModel.price, index, uiModel.dataSetPosition, uiModel.isOutOfStock)
        renderAlpha(uiModel.isOutOfStock, uiModel.canBeSelected)
    }

    private fun setTitle(title: String) {
        binding.tvTokofoodAddOnItemTitle.text = title
    }

    private fun setDescription(description: String?,
                               isOutOfStock: Boolean) {
        if (isOutOfStock) {
            binding.tvTokofoodAddOnItemDesc.invisible()
            binding.tvTokofoodAddOnItemOutOfStock.visible()
        } else {
            binding.tvTokofoodAddOnItemDesc.run {
                val priceDesc =
                    description ?: itemView.context?.getString(com.tokopedia.tokofood.R.string.text_free).orEmpty()
                visible()
                text = priceDesc
            }
            binding.tvTokofoodAddOnItemOutOfStock.invisible()
        }
    }

    private fun setSelectionType(isSelected: Boolean,
                                 type: SelectionControlType) {
        if (type == SelectionControlType.SINGLE_SELECTION) {
            binding.radioTokofoodAddOnItem.run {
                visible()
                isChecked = isSelected
            }
            binding.checkboxTokofoodAddOnItem.gone()
        } else {
            binding.radioTokofoodAddOnItem.gone()
            binding.checkboxTokofoodAddOnItem.run {
                visible()
                isChecked = isSelected
            }
        }
    }

    private fun setEnabled(isOutOfStock: Boolean,
                           canBeSelected: Boolean) {
        val isItemEnabled = !isOutOfStock && canBeSelected
        binding.root.isEnabled = isItemEnabled
        binding.checkboxTokofoodAddOnItem.run {
            isEnabled = isItemEnabled
            isClickable = isItemEnabled
        }
        binding.radioTokofoodAddOnItem.run {
            isEnabled = isItemEnabled
            isClickable = isItemEnabled
        }
    }

    private fun setOnClickAction(type: SelectionControlType,
                                 isOutOfStock: Boolean) {
        val onClickAction: () -> Unit =
            when {
                isOutOfStock -> {
                    {}
                }
                type == SelectionControlType.SINGLE_SELECTION -> {
                    {
                        binding.radioTokofoodAddOnItem.callOnClick()
                    }
                }
                else -> {
                    {
                        val isChecked = binding.checkboxTokofoodAddOnItem.isChecked
                        binding.checkboxTokofoodAddOnItem.isChecked = !isChecked
                        binding.checkboxTokofoodAddOnItem.callOnClick()
                    }
                }
            }

        binding.root.setOnClickListener {
            onClickAction.invoke()
        }
    }

    private fun setSelectionClickListener(price: Double,
                                          index: Int,
                                          dataSetPosition: Int,
                                          isOutOfStock: Boolean) {
        binding.radioTokofoodAddOnItem.setOnClickListener {
            if (!isOutOfStock) {
                binding.radioTokofoodAddOnItem.isChecked = true
                val isChecked = binding.radioTokofoodAddOnItem.isChecked
                listener.onRadioButtonClicked(isChecked, price, index, dataSetPosition)
            }
        }
        binding.checkboxTokofoodAddOnItem.setOnClickListener {
            if (!isOutOfStock) {
                val isChecked = binding.checkboxTokofoodAddOnItem.isChecked
                listener.onCheckboxClicked(isChecked, price, index, dataSetPosition)
            }
        }
    }

    private fun renderAlpha(isOutOfStock: Boolean,
                            canBeSelected: Boolean) {
        val alpha =
            if (isOutOfStock || !canBeSelected) {
                DISABLED_ALPHA
            } else {
                ENABLED_ALPHA
            }
        binding.root.alpha = alpha
        binding.tvTokofoodAddOnItemTitle.alpha = alpha
        binding.tvTokofoodAddOnItemDesc.alpha = alpha
        binding.tvTokofoodAddOnItemOutOfStock.alpha = alpha
    }

    interface Listener {
        fun onRadioButtonClicked(isSelected: Boolean, price: Double, index: Int, dataSetPosition: Int)
        fun onCheckboxClicked(isSelected: Boolean, price: Double, index: Int, dataSetPosition: Int)
    }

    companion object {
        private const val ENABLED_ALPHA = 1.0f
        private const val DISABLED_ALPHA = 0.5f
    }

}