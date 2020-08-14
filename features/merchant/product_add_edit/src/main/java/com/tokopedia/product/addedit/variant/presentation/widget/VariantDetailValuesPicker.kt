package com.tokopedia.product.addedit.variant.presentation.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.add_edit_product_variant_value_picker_layout.view.*

class VariantDetailValuesPicker(context: Context?) : LinearLayout(context) {

    private var layoutPosition: Int? = null

    private var variantDetail: VariantDetail? = null

    private var selectedVariantUnit: Unit = Unit()

    private var selectedVariantUnitValues = mutableListOf<UnitValue>()

    private var onVariantUnitPickerClickListener: OnVariantUnitPickerClickListener? = null

    private var onVariantUnitValuePickListener: OnVariantUnitValuePickListener? = null

    private var onAddCustomVariantUnitValueListener: OnAddCustomVariantUnitValueListener? = null

    private var onButtonSaveClickListener: OnButtonSaveClickListener? = null

    interface OnVariantUnitPickerClickListener {
        fun onVariantUnitPickerClicked(selectedVariantUnit: Unit, layoutPosition: Int)
    }

    interface OnVariantUnitValuePickListener {
        fun onVariantUnitValuePickListener(variantType: String, variantUnitValue: String)
    }

    interface OnAddCustomVariantUnitValueListener {
        fun onAddButtonClicked(layoutPosition: Int, variantId: Int, unitName: String)
    }

    interface OnButtonSaveClickListener {
        fun onVariantUnitValueSaveButtonClicked(selectedVariantUnitValues: List<UnitValue>, layoutPosition: Int, variantId: Int, variantTypeName: String)
    }

    init {
        context?.run {
            inflateVariantValuePickerLayout(this)
        }
    }

    fun setLayoutPosition(layoutPosition: Int) {
        this.layoutPosition = layoutPosition
    }

    fun setSelectedVariantUnit(selectedVariantUnit: Unit) {
        this.selectedVariantUnit = selectedVariantUnit
    }

    fun setOnButtonSaveClickListener(onButtonSaveClickListener: OnButtonSaveClickListener) {
        this.onButtonSaveClickListener = onButtonSaveClickListener
    }

    fun setOnVariantUnitPickerClickListener(onVariantUnitPickerClickListener: OnVariantUnitPickerClickListener) {
        this.onVariantUnitPickerClickListener = onVariantUnitPickerClickListener
    }

    fun setOnVariantUnitValuePickListener(onVariantUnitValuePickListener: OnVariantUnitValuePickListener) {
        this.onVariantUnitValuePickListener = onVariantUnitValuePickListener
    }

    fun setOnAddCustomVariantUnitValueListener(onAddCustomVariantUnitValueListener: OnAddCustomVariantUnitValueListener) {
        this.onAddCustomVariantUnitValueListener = onAddCustomVariantUnitValueListener
    }

    fun setSelectedVariantUnitValues(selectedVariantUnitValues: MutableList<UnitValue>) {
        this.selectedVariantUnitValues = selectedVariantUnitValues
    }

    private fun inflateVariantValuePickerLayout(context: Context) {
        View.inflate(context, R.layout.add_edit_product_variant_value_picker_layout, this)
    }

    fun setupVariantDetailValuesPicker(variantDetail: VariantDetail) {
        this.variantDetail = variantDetail
        var selectedUnit = variantDetail.units[0]
        if (selectedVariantUnit.unitName.isNotBlank()) selectedUnit = this.selectedVariantUnit
        // setup unit picker
        setupVariantUnitPicker(variantDetail.name, variantDetail.units)
        // setup unit value picker
        setupVariantUnitValuePicker(variantDetail.variantID, variantDetail.name, selectedUnit.unitValues)
        // setup and configure save button
        setupSaveButton(selectedVariantUnitValues, layoutPosition, variantDetail.variantID, variantDetail.name)
        configureSaveButton(selectedVariantUnitValues)
    }

    private fun setupVariantUnitPicker(unitName: String, variantUnits: List<Unit>) {
        if (variantUnits.size > 1) {
            var selectedUnit = variantUnits[0]
            if (selectedVariantUnit.unitName.isNotBlank()) selectedUnit = this.selectedVariantUnit
            variantUnitLayout.show()
            textFieldUnifyVariantUnit.textFiedlLabelText.text = unitName
            textFieldUnifyVariantUnit.textFieldInput.setText(selectedUnit.unitName)
            textFieldUnifyVariantUnit.textFieldInput.isFocusable = false
            textFieldUnifyVariantUnit.textFieldInput.isActivated = false
            textFieldUnifyVariantUnit.textFieldInput.setOnClickListener {
                layoutPosition?.let {
                    onVariantUnitPickerClickListener?.onVariantUnitPickerClicked(selectedVariantUnit, it)
                }
            }
        } else variantUnitLayout.hide()
    }

    private fun setupVariantUnitValuePicker(variantId: Int, unitName: String, variantUnitValues: List<UnitValue>) {

        val variantUnitData = ArrayList<ListItemUnify>()

        variantUnitValues.forEach {
            val data = ListItemUnify(it.value, "")
            data.setVariant(null, ListItemUnify.CHECKBOX, "")
            variantUnitData.add(data)
        }

        val addCustomValueTitle = context.getString(com.tokopedia.product.addedit.R.string.action_variant_add) + " " + unitName
        val addCustomValueButton = ListItemUnify(addCustomValueTitle, "")
        variantUnitData.add(addCustomValueButton)

        listUnifyVariantUnitValues.setData(variantUnitData)
        listUnifyVariantUnitValues.onLoadFinish {

            // set selected values to check box
            selectedVariantUnitValues.forEach {
                val unitValueName = it.value
                val selectedListItemUnify = variantUnitData.find { listItemUnify ->
                    listItemUnify.listTitleText == unitValueName
                }
                selectedListItemUnify?.listRightCheckbox?.isChecked = true
                selectedListItemUnify?.listRightCheckbox?.isEnabled = false
            }

            // on item click listener
            listUnifyVariantUnitValues.setOnItemClickListener { _, _, position, _ ->
                if (position != variantUnitData.lastIndex) {
                    val selectedItem = variantUnitData[position]
                    val isChecked = selectedItem.listRightCheckbox?.isChecked
                    isChecked?.run {
                        selectedItem.listRightCheckbox?.isChecked = !this
                    }
                    // track select variant unit value event
                    onVariantUnitValuePickListener?.onVariantUnitValuePickListener(variantDetail?.name
                            ?: "", selectedItem.listTitleText)
                } else {
                    // add custom variant unit value
                    layoutPosition?.run {
                        onAddCustomVariantUnitValueListener?.onAddButtonClicked(this, variantId, unitName)
                    }
                }
            }

            //
            variantUnitData.forEachIndexed { index, listItemUnify ->
                if (index != variantUnitData.lastIndex) {
                    listItemUnify.listRightCheckbox?.setOnCheckedChangeListener { _, isChecked ->
                        val selectedVariantUnitValue = variantUnitValues[index]
                        if (isChecked) selectedVariantUnitValues.add(selectedVariantUnitValue)
                        else selectedVariantUnitValues.remove(selectedVariantUnitValue)
                        configureSaveButton(selectedVariantUnitValues)
                    }
                } else {
                    listItemUnify.isBold = false
                    listItemUnify.listTitle?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500))
                }
                listItemUnify.listRightCheckbox?.setPadding(0,0,0,0)
                listItemUnify.listRightCheckbox?.setMargin(0,0,0,0)
            }
        }
    }

    private fun setupSaveButton(selectedVariantUnitValues: List<UnitValue>, layoutPosition: Int?, variantId: Int, variantTypeName: String) {
        buttonSave.setOnClickListener {
            layoutPosition?.let {
                onButtonSaveClickListener?.onVariantUnitValueSaveButtonClicked(selectedVariantUnitValues, it, variantId, variantTypeName)
            }
        }
    }

    private fun configureSaveButton(selectedVariantUnitValues: List<UnitValue>) {
        if (selectedVariantUnitValues.isEmpty()) {
            buttonSave.text = context.getText(R.string.action_variant_save).toString()
            buttonSave.isEnabled = false
        } else {
            buttonSave.text = context.getText(R.string.action_variant_save).toString() + "(" + selectedVariantUnitValues.size.toString() + ")"
            buttonSave.isEnabled = true
        }
    }
}