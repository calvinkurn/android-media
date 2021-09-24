package com.tokopedia.product.addedit.variant.presentation.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

class VariantUnitPicker(context: Context?) : LinearLayout(context) {

    private var listUnifyVariantUnits: ListUnify? = null
    private var buttonSave: UnifyButton? = null

    private var onVariantUnitPickListener: OnVariantUnitPickListener? = null

    private var layoutPosition: Int? = null
    private var selectedVariantUnit: Unit = Unit()
    private var currentVariantUnit: Unit = Unit()
    private var hasSelectedValues: Boolean = false

    interface OnVariantUnitPickListener {
        fun onVariantUnitPicked(selectedVariantUnit: Unit, currentVariantUnit: Unit, layoutPosition: Int, hasSelectedValues: Boolean)
    }

    init {
        context?.run {
            inflateVariantUnitPickerLayout(this)
        }
    }

    fun setLayoutPosition(layoutPosition: Int) {
        this.layoutPosition = layoutPosition
    }

    fun setSelectedVariantUnit(selectedVariantUnit: Unit) {
        this.selectedVariantUnit = selectedVariantUnit
    }

    fun setHasSelectedValues(hasSelectedVariantUnitValues: Boolean) {
        this.hasSelectedValues = hasSelectedVariantUnitValues
    }

    fun setOnVariantUnitPickListener(onVariantUnitPickListener: OnVariantUnitPickListener) {
        this.onVariantUnitPickListener = onVariantUnitPickListener
    }

    private fun inflateVariantUnitPickerLayout(context: Context) {
        View.inflate(context, R.layout.add_edit_product_variant_unit_picker_layout, this)
    }

    fun setupVariantUnitPicker(variantUnits: List<Unit>) {
        setupViews()
        setDefaultSelection(selectedVariantUnit)
        setupPicker(variantUnits)
        setupSaveButton()
    }

    private fun setupViews() {
        listUnifyVariantUnits = findViewById(R.id.listUnifyVariantUnits)
        buttonSave = findViewById(R.id.buttonSave)
    }

    private fun setDefaultSelection(selectedVariantUnit: Unit) {
        this.currentVariantUnit = selectedVariantUnit
    }

    private fun setupPicker(variantUnits: List<Unit>) {
        val variantUnitData = ArrayList<ListItemUnify>()
        variantUnits.forEach {
            val data = ListItemUnify(it.unitName, "")
            data.setVariant(null, ListItemUnify.RADIO_BUTTON, "")
            variantUnitData.add(data)
        }

        listUnifyVariantUnits?.setData(variantUnitData)
        listUnifyVariantUnits?.onLoadFinish {

            val unitName = selectedVariantUnit.unitName
            if (unitName.isNotBlank()) {
                val selectedListItemUnify = variantUnitData.find { listItemUnify ->
                    listItemUnify.listTitleText == unitName
                }
                selectedListItemUnify?.listRightRadiobtn?.isChecked = true
            } else {
                variantUnitData.firstOrNull()?.listRightRadiobtn?.isChecked = true
            }

            listUnifyVariantUnits?.setOnItemClickListener { _, _, position, _ ->
                val selectedItem = variantUnitData[position]
                selectedItem.listRightRadiobtn?.performClick()
            }

            variantUnitData.forEachIndexed { position, listItemUnify ->
                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    val isChecked = listItemUnify.listRightRadiobtn?.isChecked
                    isChecked?.run {
                        if (isChecked) currentVariantUnit = variantUnits[position]
                        variantUnitData.forEachIndexed { index, listItemUnify ->
                            if (position != index) listItemUnify.listRightRadiobtn?.isChecked = false
                        }
                    }
                }
                listItemUnify.listRightRadiobtn?.setPadding(0,0,0,0)
                listItemUnify.listRightRadiobtn?.setMargin(0,0,0,0)
            }
        }
    }

    private fun setupSaveButton() {
        buttonSave?.setOnClickListener {
            if (selectedVariantUnit.unitName.isNotBlank() && currentVariantUnit.unitName.isNotBlank()) {
                layoutPosition?.run {
                    onVariantUnitPickListener?.onVariantUnitPicked(selectedVariantUnit, currentVariantUnit, this, hasSelectedValues)
                }
            }
        }
    }
}