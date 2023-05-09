package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.ProductOptionAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.decoration.ProductOptionDividerDecoration
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel

class ProductAddOnViewHolder(
        private val binding: TokofoodItemAddOnLayoutBinding,
        private val selectListener: OnAddOnSelectListener
) : RecyclerView.ViewHolder(binding.root), ProductOptionViewHolder.Listener {

    interface OnAddOnSelectListener {
        fun onAddOnSelected(isSelected: Boolean, addOnPrice: Double, addOnPositions: Pair<Int, Int>)
    }

    private var context: Context? = null
    private var optionItems: List<OptionUiModel> = listOf()
    private var optionAdapter: ProductOptionAdapter? = null

    private val linearLayoutManager by lazy {
        LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
    }

    private val itemDecorator by lazy {
        ProductOptionDividerDecoration(itemView.context)
    }

    init {
        context = binding.root.context
    }

    fun bindData(addOnUiModel: AddOnUiModel, dataSetPosition: Int) {
        binding.addOnsTypeLabel.text = addOnUiModel.name
        binding.requiredAddOnsLabel.isVisible = addOnUiModel.isRequired
        binding.tpgOptionalWording.isVisible = !addOnUiModel.isRequired

        context?.run {
            if (addOnUiModel.isError) {
                val redColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                binding.tpgMandatoryLabel.setTextColor(redColor)
            } else {
                val greenColor = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                binding.tpgMandatoryLabel.setTextColor(greenColor)
            }
        }

        // setup add on rules wording e.g harus dipilih.pilih 2
        if (addOnUiModel.isRequired) {
            // setup mandatory add on wording
            context?.run {
                // single mandatory add on selection
                var mandatoryAmountWording = this.getString(
                        R.string.text_single_mandatory_add_on_amount,
                        addOnUiModel.minQty
                )
                // multiple mandatory add on selection
                if (addOnUiModel.isMultipleMandatory) {
                    mandatoryAmountWording = this.getString(
                            R.string.text_multiple_mandatory_add_on_amount,
                            addOnUiModel.minQty,
                            addOnUiModel.maxQty
                    )
                }
                binding.tpgMandatoryAmount.text = mandatoryAmountWording
            }
        } else {
            // setup optional add on wording
            context?.run {
                val optionalAddOnWording = this.getString(
                        com.tokopedia.tokofood.R.string.text_optional_add_on_wording,
                        addOnUiModel.maxQty
                )
                binding.tpgOptionalWording.text = optionalAddOnWording
            }
        }

        // setup add on items
        optionItems = addOnUiModel.filteredOptions
        optionAdapter = ProductOptionAdapter(this@ProductAddOnViewHolder).apply {
            val totalSelected = optionItems.count { it.isSelected }
            setData(optionItems.onEach {
                it.dataSetPosition = dataSetPosition
                it.maxSelected = addOnUiModel.maxQty
                it.canBeSelected =
                    it.selectionControlType == SelectionControlType.SINGLE_SELECTION || it.isSelected || totalSelected < addOnUiModel.maxQty
            })
        }
        binding.rvAddOnList.run {
            layoutManager = linearLayoutManager
            optionAdapter?.let { optionAdapter ->
                adapter = optionAdapter
            }
            addItemDecoration(itemDecorator)
        }
    }

    override fun onCheckboxClicked(
        isSelected: Boolean,
        price: Double,
        index: Int,
        dataSetPosition: Int
    ) {
        selectListener.onAddOnSelected(isSelected, price, Pair(dataSetPosition, index))
        optionItems.onEachIndexed { optionIndex, optionUiModel ->
            if (optionIndex == index) {
                optionUiModel.isSelected = isSelected
            }
        }
        val totalSelected = optionItems.count { it.isSelected }
        optionItems.onEachIndexed { optionIndex, optionUiModel ->
            optionUiModel.canBeSelected =
                optionUiModel.isSelected || totalSelected < optionUiModel.maxSelected
        }
        optionAdapter?.updateSelectableData(optionItems)
    }

    override fun onRadioButtonClicked(
        isSelected: Boolean,
        price: Double,
        index: Int,
        dataSetPosition: Int
    ) {
        val previousSelectedIndex = optionItems.indexOfFirst { it.isSelected }
        selectListener.onAddOnSelected(isSelected, price, Pair(dataSetPosition, index))
        optionItems.onEachIndexed { optionIndex, optionUiModel ->
            optionUiModel.isSelected = optionIndex == index
        }
        optionAdapter?.updateData(previousSelectedIndex, optionItems)
    }

}
