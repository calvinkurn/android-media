package com.tokopedia.tokofood.feature.merchant.presentation.viewholder

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.TokofoodItemAddOnLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType.MULTIPLE_SELECTION
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType.SINGLE_SELECTION
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class ProductAddOnViewHolder(
        private val binding: TokofoodItemAddOnLayoutBinding,
        private val selectListener: OnAddOnSelectListener
) : RecyclerView.ViewHolder(binding.root) {

    interface OnAddOnSelectListener {
        fun onAddOnSelected(isSelected: Boolean, addOnPositions: Pair<Int, Int>)
    }

    private var context: Context? = null
    private var addOnItems = ArrayList<ListItemUnify>()

    init {
        context = binding.root.context
    }

    fun bindData(addOnUiModel: AddOnUiModel) {

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
        this.addOnItems = ArrayList(addOnUiModel.addOnItems)
        binding.luAddOnList.setData(addOnItems)
        binding.luAddOnList.apply {
            setData(addOnItems)
            onLoadFinish {
                // list item click listener
                setOnItemClickListener { _, _, position, _ ->
                    val selectedItem = this.getItemAtPosition(position) as ListItemUnify
                    when (addOnUiModel.options[position].selectionControlType) {
                        SINGLE_SELECTION -> {
                            selectedItem.listRightRadiobtn?.callOnClick()
                        }
                        MULTIPLE_SELECTION -> {
                            val isChecked = selectedItem.listRightCheckbox?.isChecked ?: false
                            selectedItem.listRightCheckbox?.isChecked = !isChecked
                            selectedItem.listRightCheckbox?.callOnClick()
                        }
                    }
                }

                addOnItems.forEachIndexed { index, listItemUnify ->
                    // radio button click listener
                    listItemUnify.listRightRadiobtn?.setOnClickListener {
                        val type = addOnUiModel.options[index].selectionControlType
                        setSelected(addOnItems, index, type, addOnUiModel.maxQty) {
                            val isSelected = it.listRightCheckbox?.isChecked ?: false
                            selectListener.onAddOnSelected(isSelected, Pair(index, adapterPosition))
                        }
                    }
                    // check box button click listener
                    listItemUnify.listRightCheckbox?.setOnClickListener {
                        val type = addOnUiModel.options[index].selectionControlType
                        setSelected(addOnItems, index, type, addOnUiModel.maxQty) {
                            selectListener.onAddOnSelected(isSelected, Pair(index, adapterPosition))
                        }
                    }
                }

            }
        }
    }

    private fun ListUnify.setSelected(items: List<ListItemUnify>,
                                      position: Int,
                                      type: SelectionControlType,
                                      maxQty: Int,
                                      onChecked: (selectedItem: ListItemUnify) -> Any) = run {
        val selectedItem = this.getItemAtPosition(position) as ListItemUnify
        when (type) {
            SINGLE_SELECTION -> {
                // deselect previously selected item
                items.filter { it.listRightRadiobtn?.isChecked ?: false }
                        .filterNot { it == selectedItem }
                        .onEach { it.listRightRadiobtn?.isChecked = false }
            }
            MULTIPLE_SELECTION -> {
                val selectedItemCount = items.filter { it.listRightCheckbox?.isChecked == true }.size
                if (selectedItemCount == maxQty) {
                    items.filter { it.listRightCheckbox?.isChecked == false }
                            .filterNot { it == selectedItem || it.listRightCheckbox?.isChecked == true }
                            .forEach { it.listRightCheckbox?.isEnabled = false }
                } else {
                    items.forEach { it.listRightCheckbox?.isEnabled = true }
                }
            }
        }
        onChecked(selectedItem)
    }
}