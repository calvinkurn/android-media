package com.tokopedia.product.addedit.variant.presentation.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_UNIT_VALUES
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.add_edit_product_variant_value_picker_layout.view.*

class VariantDataValuePicker : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, layoutPosition: Int, variantData: VariantDetail,
                onVariantUnitPickerClickListener: OnVariantUnitPickerClickListener,
                onVariantUnitValuePickListener: OnVariantUnitValuePickListener,
                onAddCustomVariantUnitValueListener: OnAddCustomVariantUnitValueListener,
                onButtonSaveClickListener: OnButtonSaveClickListener) : super(context) {
        inflateVariantValuePickerLayout(context)
        this.layoutPosition = layoutPosition
        this.variantData = variantData
        this.onVariantUnitPickerClickListener = onVariantUnitPickerClickListener
        this.onVariantUnitValuePickListener = onVariantUnitValuePickListener
        this.onAddCustomVariantUnitValueListener = onAddCustomVariantUnitValueListener
        this.onButtonSaveClickListener = onButtonSaveClickListener
    }

    private var layoutPosition: Int = 0
    private var variantData: VariantDetail = VariantDetail()
    private var onVariantUnitPickerClickListener: OnVariantUnitPickerClickListener? = null
    private var onVariantUnitValuePickListener: OnVariantUnitValuePickListener? = null
    private var onAddCustomVariantUnitValueListener: OnAddCustomVariantUnitValueListener? = null
    private var onButtonSaveClickListener: OnButtonSaveClickListener? = null

    private fun inflateVariantValuePickerLayout(context: Context) {
        View.inflate(context, R.layout.add_edit_product_variant_value_picker_layout, this)
    }

    fun setupVariantDataValuePicker(selectedVariantUnit: Unit,
                                    selectedVariantUnitValues: MutableList<UnitValue>,
                                    addedCustomVariantUnitValue: UnitValue,
                                    unConfirmedSelection: List<UnitValue>) {
        // setup unit picker
        setupVariantUnitPicker(layoutPosition, variantData.name, selectedVariantUnit, selectedVariantUnitValues)
        // setup unit value picker
        setupVariantUnitValuePicker(layoutPosition, variantData, selectedVariantUnit, selectedVariantUnitValues, addedCustomVariantUnitValue, unConfirmedSelection)
        // setup and configure save button
        setupSaveButton(layoutPosition, selectedVariantUnit, selectedVariantUnitValues)
        // count and enable/disable save button
        configureSaveButton(selectedVariantUnitValues.isEmpty(), selectedVariantUnitValues.size)
    }

    private fun setupVariantUnitPicker(layoutPosition: Int,
                                       typeName: String,
                                       selectedVariantUnit: Unit,
                                       selectedVariantUnitValues: MutableList<UnitValue>) {
        if (selectedVariantUnit.unitName.isNotBlank()) {
            // setup variant unit picker ui
            textFieldUnifyVariantUnit.textFiedlLabelText.text = typeName
            textFieldUnifyVariantUnit.textFieldInput.setText(selectedVariantUnit.unitName)
            textFieldUnifyVariantUnit.textFieldInput.isFocusable = false
            textFieldUnifyVariantUnit.textFieldInput.isActivated = false
            // setup variant unit picker click listener
            textFieldUnifyVariantUnit.textFieldInput.setOnClickListener {
                onVariantUnitPickerClickListener?.onVariantUnitPickerClicked(layoutPosition, selectedVariantUnit, selectedVariantUnitValues)
            }
            variantUnitLayout.show()
        } else variantUnitLayout.hide()
    }

    private fun setupVariantUnitValuePicker(layoutPosition: Int,
                                            variantData: VariantDetail,
                                            selectedVariantUnit: Unit,
                                            selectedVariantUnitValues: MutableList<UnitValue>,
                                            addedCustomVariantUnitValue: UnitValue,
                                            unConfirmedSelection: List<UnitValue>) {

        // list of listItemUnifyList
        val listItemUnifyList = ArrayList<ListItemUnify>()

        val variantUnitValues = variantData.units.find { unit ->
            unit.variantUnitID == selectedVariantUnit.variantUnitID
        }?.unitValues ?: mutableListOf()

        // populate the listItemUnifyList with data
        variantUnitValues.forEach {
            val listItemUnify = ListItemUnify(it.value, "")
            listItemUnify.setVariant(null, ListItemUnify.CHECKBOX, "")
            listItemUnifyList.add(listItemUnify)
        }

        // add the add custom button at the last index
        val addCustomValueTitle = context.getString(R.string.action_variant_add_button, variantData.name)
        val addCustomVariantUnitValueButton = ListItemUnify(addCustomValueTitle, "")
        listItemUnifyList.add(addCustomVariantUnitValueButton)

        // populate the ListUnify with data
        listUnifyVariantUnitValues.setData(listItemUnifyList)

        listUnifyVariantUnitValues.onLoadFinish {
            // set selected values
            setSelectedValues(listItemUnifyList, selectedVariantUnitValues)
            // setup add custom button
            setupAddCustomVariantUnitValueButton(addCustomVariantUnitValueButton)
            // setup item click handler
            setupListUnifyItemClickHandler(listItemUnifyList, layoutPosition, selectedVariantUnit, variantUnitValues, selectedVariantUnitValues)
            // setup checked change listener
            setupCheckBoxCheckedChangeListener(listItemUnifyList, variantUnitValues, selectedVariantUnitValues)
            // set added custom variant unit value
            setAddedCustomVariantUnitValue(listItemUnifyList, addedCustomVariantUnitValue)
            // set unconfirmed selection
            setUnconfirmedSelection(listItemUnifyList, unConfirmedSelection)

            listItemUnifyList.forEach {
                it.listRightCheckbox?.setPadding(0, 0, 0, 0)
                it.listRightCheckbox?.setMargin(0, 0, 0, 0)
            }
        }
    }

    private fun setSelectedValues(listItemUnifyList: List<ListItemUnify>, selectedVariantUnitValues: MutableList<UnitValue>) {
        // set selected values to check box
        selectedVariantUnitValues.forEach { unitValue ->
            // comparing the title with unit value name to find the selected item(s)
            val selectedListItemUnify = listItemUnifyList.find { listItemUnify ->
                listItemUnify.listTitleText == unitValue.value
            }
            // set the ListItemUnify state
            selectedListItemUnify?.listRightCheckbox?.isChecked = true
            selectedListItemUnify?.listRightCheckbox?.isEnabled = false
        }
    }

    private fun setupAddCustomVariantUnitValueButton(addCustomVariantUnitValueButton: ListItemUnify) {
        addCustomVariantUnitValueButton.isBold = false
        addCustomVariantUnitValueButton.listTitle?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
    }

    private fun setupListUnifyItemClickHandler(listItemUnifyList: List<ListItemUnify>,
                                               layoutPosition: Int,
                                               selectedVariantUnit: Unit,
                                               variantUnitValues: List<UnitValue>,
                                               selectedVariantUnitValues: MutableList<UnitValue>) {
        // setup ListUnify item click handler
        listUnifyVariantUnitValues.setOnItemClickListener { _, _, position, _ ->
            // last position is reserved for add custom variant unit value button
            if (position != listItemUnifyList.lastIndex) {
                val selectedItem = listItemUnifyList[position]
                // check if the checkbox is disabled then ignore the click event
                val isEnabled = selectedItem.listRightCheckbox?.isEnabled ?: true
                if (!isEnabled) return@setOnItemClickListener
                // handle check / uncheck condition
                val isChecked = selectedItem.listRightCheckbox?.isChecked ?: false
                selectedItem.listRightCheckbox?.isChecked = !isChecked
                // track select variant unit value event, when list item is clicked
                val selectedUnitValue = variantUnitValues[position]
                if (!isChecked) {
                    onVariantUnitValuePickListener?.onVariantUnitValuePickListener(variantData.name, selectedUnitValue.value)
                }
            } else {
                // add custom variant unit value
                onAddCustomVariantUnitValueListener?.onAddCustomButtonClicked(
                        layoutPosition,
                        selectedVariantUnit,
                        variantUnitValues,
                        selectedVariantUnitValues)

            }
        }
        // track select variant unit value event, when right checkbox is clicked
        listItemUnifyList.forEachIndexed { index, listItemUnify ->
            listItemUnify.listRightCheckbox?.setOnClickListener {
                val selectedUnitValue = variantUnitValues[index]
                val isChecked = listItemUnify.listRightCheckbox?.isChecked ?: false
                if (isChecked) {
                    onVariantUnitValuePickListener?.onVariantUnitValuePickListener(variantData.name, selectedUnitValue.value)
                }
            }
        }

    }

    private fun setupCheckBoxCheckedChangeListener(listItemUnifyList: List<ListItemUnify>,
                                                   variantUnitValues: List<UnitValue>,
                                                   selectedVariantUnitValues: MutableList<UnitValue>) {
        // setup listRightCheckbox checked change listener
        listItemUnifyList.forEachIndexed { index, listItemUnify ->
            if (index != listItemUnifyList.lastIndex) {
                listItemUnify.listRightCheckbox?.setOnCheckedChangeListener { _, isChecked ->
                    val selectedUnitValue = variantUnitValues[index]
                    if (isChecked) {
                        selectedVariantUnitValues.add(selectedUnitValue)
                    } else selectedVariantUnitValues.remove(selectedUnitValue)
                    configureSaveButton(selectedVariantUnitValues.isEmpty(), selectedVariantUnitValues.size)
                }
            }
        }
    }

    private fun setAddedCustomVariantUnitValue(listItemUnifyList: List<ListItemUnify>, addedCustomVariantUnitValue: UnitValue) {
        val customListItemUnify = listItemUnifyList.find { listItemUnify ->
            listItemUnify.listTitleText == addedCustomVariantUnitValue.value
        }
        customListItemUnify?.run {
            customListItemUnify.listRightCheckbox?.performClick()
            // scroll to the bottom
            val top = listUnifyVariantUnitValues.getChildAt(listItemUnifyList.lastIndex).top
            scrollViewVariantUnitValues.smoothScrollTo(0, top)
        }
    }

    private fun setUnconfirmedSelection(listItemUnifyList: List<ListItemUnify>, unConfirmedSelection: List<UnitValue>) {
        // set unconfirmed selection to check box
        unConfirmedSelection.forEach { unitValue ->
            // comparing the title with unit value name to find the selected item(s)
            val selectedListItemUnify = listItemUnifyList.find { listItemUnify ->
                listItemUnify.listTitleText == unitValue.value
            }
            selectedListItemUnify?.listRightCheckbox?.isChecked = true
        }
    }

    private fun setupSaveButton(layoutPosition: Int, selectedVariantUnit: Unit, selectedVariantUnitValues: MutableList<UnitValue>) {
        buttonSave.setOnClickListener {
            if (selectedVariantUnitValues.size > MAX_SELECTED_VARIANT_UNIT_VALUES) {
                Toaster.make(this, context.getString(R.string.error_variant_unit_value_selection_exceed), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                return@setOnClickListener
            }
            onButtonSaveClickListener?.onVariantUnitValueSaveButtonClicked(selectedVariantUnit, selectedVariantUnitValues, layoutPosition)
        }
    }

    private fun configureSaveButton(isSelectedUnitValuesEmpty: Boolean, size: Int) {
        var enableSaveButton = true
        // disable save button when selection is empty
        if (isSelectedUnitValuesEmpty) {
            buttonSave.text = context.getText(R.string.action_variant_save).toString()
            buttonSave.isEnabled = false
            enableSaveButton = false
        }
        // show "Simpan (maks. 10)" for 10 or more selection
        if (size >= MAX_SELECTED_VARIANT_UNIT_VALUES) {
            val stringSave = context.getText(R.string.action_variant_save).toString()
            val stringMax = context.getText(R.string.label_variant_max).toString()
            buttonSave.text = "$stringSave($MAX_SELECTED_VARIANT_UNIT_VALUES)$stringMax"
        } else {
            buttonSave.text = context.getText(R.string.action_variant_save).toString() + "(" + size.toString() + ")"
        }
        // disable save button when selection beyond max limit
        if (size > MAX_SELECTED_VARIANT_UNIT_VALUES) enableSaveButton = false
        // configure save button status
        buttonSave.isEnabled = enableSaveButton
    }

    interface OnVariantUnitPickerClickListener {
        fun onVariantUnitPickerClicked(layoutPosition: Int, selectedVariantUnit: Unit, selectedVariantUnitValues: MutableList<UnitValue>)
    }

    interface OnVariantUnitValuePickListener {
        fun onVariantUnitValuePickListener(variantType: String, variantUnitValue: String)
    }

    interface OnAddCustomVariantUnitValueListener {
        fun onAddCustomButtonClicked(layoutPosition: Int, selectedVariantUnit: Unit, variantUnitValues: List<UnitValue>, selectedVariantUnitValues: MutableList<UnitValue>)
    }

    interface OnButtonSaveClickListener {
        fun onVariantUnitValueSaveButtonClicked(selectedVariantUnit: Unit, selectedVariantUnitValues: MutableList<UnitValue>, layoutPosition: Int)
    }
}