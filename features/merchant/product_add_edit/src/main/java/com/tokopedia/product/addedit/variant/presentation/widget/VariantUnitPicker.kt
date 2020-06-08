package com.tokopedia.product.addedit.variant.presentation.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.add_edit_product_variant_unit_picker_layout.view.*

class VariantUnitPicker(context: Context?) : LinearLayout(context) {

    private var layoutPosition: Int? = null

    private var onVariantUnitPickListener: OnVariantUnitPickListener? = null

    private var selectedVariantUnit: Unit? = null

    interface OnVariantUnitPickListener {
        fun onVariantUnitPicked(selectedVariantUnit: Unit, layoutPosition: Int)
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

    fun setOnVariantUnitPickListener(onVariantUnitPickListener: OnVariantUnitPickListener) {
        this.onVariantUnitPickListener = onVariantUnitPickListener
    }

    private fun inflateVariantUnitPickerLayout(context: Context) {
        View.inflate(context, R.layout.add_edit_product_variant_unit_picker_layout, this)
    }

    fun setupVariantUnitPicker(variantUnits: List<Unit>) {
        setupPicker(variantUnits)
        setupSaveButton()
    }

    private fun setupPicker(variantUnits: List<Unit>) {
        val variantUnitData = ArrayList<ListItemUnify>()
        variantUnits.forEach {
            val data = ListItemUnify(it.unitName, "")
            data.setVariant(null, ListItemUnify.RADIO_BUTTON, "")
            variantUnitData.add(data)
        }

        val selectedVariantUnitData = variantUnitData.find { listItemUnify ->
            listItemUnify.listTitleText == selectedVariantUnit?.unitName
        }

        selectedVariantUnitData?.listRightRadiobtn?.isChecked = true

        listUnifyVariantUnits.setData(variantUnitData)
        listUnifyVariantUnits.onLoadFinish {

            selectedVariantUnit?.let {
                val unitName = it.unitName
                val selectedListItemUnify = variantUnitData.find { listItemUnify ->
                    listItemUnify.listTitleText == unitName
                }
                selectedListItemUnify?.listRightRadiobtn?.isChecked = true
            }

            listUnifyVariantUnits.setOnItemClickListener { _, _, position, _ ->
                val selectedItem = variantUnitData[position]
                selectedItem.listRightRadiobtn?.performClick()
            }

            variantUnitData.forEachIndexed { position, listItemUnify ->
                listItemUnify.listRightRadiobtn?.setOnClickListener {
                    val isChecked = listItemUnify.listRightRadiobtn?.isChecked
                    isChecked?.run {
                        if (isChecked) selectedVariantUnit = variantUnits[position]
                        variantUnitData.forEachIndexed { index, listItemUnify ->
                            if (position != index) listItemUnify.listRightRadiobtn?.isChecked = false
                        }
                    }
                }
            }
        }
    }

    private fun setupSaveButton() {
        buttonSave.setOnClickListener {
            selectedVariantUnit?.let {
                layoutPosition?.run {
                    onVariantUnitPickListener?.onVariantUnitPicked(it, this)
                }
            }
        }
    }
}