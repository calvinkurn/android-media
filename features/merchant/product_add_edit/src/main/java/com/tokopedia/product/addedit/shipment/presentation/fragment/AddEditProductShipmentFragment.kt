package com.tokopedia.product.addedit.shipment.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.unifycomponents.TextFieldUnify
import kotlin.collections.ArrayList

class AddEditProductShipmentFragment : BaseDaggerFragment() {
    private var tfWeightAmount: TextFieldUnify? = null
    private var tfWeightUnit: TextFieldUnify? = null
    private var selectedWeightPosition: Int = -1
    private var selectedWeightText = ""

    companion object {
        fun createInstance(): Fragment {
            return AddEditProductShipmentFragment()
        }

        const val GRAM = "Gram"
        const val KILOGRAM = "Kilogram"
        const val MIN_WEIGHT = 1
        const val MAX_WEIGHT_GRAM = 500000
        const val MAX_WEIGHT_KILOGRAM = 500
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_product_shipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tfWeightUnit = view.findViewById(R.id.tf_weight_unit)
        tfWeightAmount = view.findViewById(R.id.tf_weight_amount)
        tfWeightUnit?.apply {
            textFieldInput.setText(GRAM)
            textFieldInput.isFocusable = false // disable focus
            textFieldInput.isActivated = false // disable focus
            textFieldInput.setOnClickListener {
                showUnitWeightOption()
            }
        }
        tfWeightAmount?.textFieldInput?.afterTextChanged{
            isWeightValid()
        }
    }

    private fun showUnitWeightOption() {
        fragmentManager?.let {
            val optionPicker = OptionPicker()
            val title = getString(R.string.label_weight)
            val options: ArrayList<String> = ArrayList()
            options.add(GRAM)
            options.add(KILOGRAM)

            optionPicker.apply {
                setSelectedPosition(selectedWeightPosition)
                setDividerVisible(true)
                setTitle(title)
                setItemMenuList(options)
                show(it, null)
            }

            optionPicker.setOnItemClickListener{ selectedText: String, selectedPosition: Int ->
                tfWeightUnit?.textFieldInput?.setText(selectedText)
                tfWeightAmount?.textFieldInput?.setText("")
                selectedWeightPosition = selectedPosition
                selectedWeightText = selectedText
            }
        }
    }

    private fun getWeight() = tfWeightAmount?.textFieldInput?.text.toString().toIntOrZero()

    private fun isWeightValid(): Boolean {
        var isValid = true
        val minWeight = MIN_WEIGHT
        val maxWeight = if (selectedWeightText == KILOGRAM) MAX_WEIGHT_KILOGRAM else MAX_WEIGHT_GRAM
        val errorMessage = getString(R.string.error_weight_not_valid, minWeight, maxWeight)
        if (minWeight > getWeight() || getWeight() > maxWeight) {
            isValid = false
        }
        tfWeightAmount?.setError(!isValid)
        tfWeightAmount?.setMessage(if (isValid) "" else errorMessage)
        return isValid
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {

    }

}
