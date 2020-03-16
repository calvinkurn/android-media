package com.tokopedia.product.addedit.shipment.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MIN_WEIGHT
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.viewmodel.AddEditProductViewModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class AddEditProductShipmentFragment : BaseDaggerFragment() {
    private var tfWeightAmount: TextFieldUnify? = null
    private var tfWeightUnit: TextFieldUnify? = null
    private var btnEnd: UnifyButton? = null
    private var selectedWeightPosition: Int = 0
    private lateinit var viewModel: AddEditProductViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun createInstance(): Fragment {
            return AddEditProductShipmentFragment()
        }

        fun getWeightTypeTitle(type: Int) =
                when (type) {
                    UNIT_GRAM ->  R.string.label_weight_gram
                    UNIT_KILOGRAM ->  R.string.label_weight_kilogram
                    else -> -1
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
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
        btnEnd = view.findViewById(R.id.btn_end)
        tfWeightUnit?.apply {
            textFieldInput.setText(getWeightTypeTitle(0))
            textFieldInput.isFocusable = false // disable focus
            textFieldInput.isActivated = false // disable focus
            textFieldInput.setOnClickListener {
                showUnitWeightOption()
            }
        }
        tfWeightAmount?.textFieldInput?.afterTextChanged{
            validateInputWeight(it)
        }
        btnEnd?.setOnClickListener {
            activity?.finish()
        }
    }

    private fun initViewModel() {
        activity?.run {
            viewModel = ViewModelProviders.of(this, viewModelFactory)
                    .get(AddEditProductViewModel::class.java)
        }
    }

    private fun showUnitWeightOption() {
        fragmentManager?.let {
            val optionPicker = OptionPicker()
            val title = getString(R.string.label_weight)
            val options: ArrayList<String> = ArrayList()
            options.add(getString(getWeightTypeTitle(UNIT_GRAM)))
            options.add(getString(getWeightTypeTitle(UNIT_KILOGRAM)))

            optionPicker.apply {
                setSelectedPosition(selectedWeightPosition)
                setDividerVisible(true)
                setTitle(title)
                setItemMenuList(options)
                show(it, null)
            }

            optionPicker.setOnItemClickListener{ selectedText: String, selectedPosition: Int ->
                tfWeightUnit?.textFieldInput?.setText(selectedText)
                selectedWeightPosition = selectedPosition
                resetTfWeightAmount()
            }
        }
    }

    private fun validateInputWeight(inputText: String) {
        val errorMessage: String
        val isValid = viewModel.isWeightValid(inputText, selectedWeightPosition, MIN_WEIGHT)
        if (selectedWeightPosition == UNIT_GRAM) {
            errorMessage = getString(R.string.error_weight_not_valid, MIN_WEIGHT, MAX_WEIGHT_GRAM)
        } else {
            errorMessage = getString(R.string.error_weight_not_valid, MIN_WEIGHT, MAX_WEIGHT_KILOGRAM)
        }
        tfWeightAmount?.setError(!isValid)
        tfWeightAmount?.setMessage(if (isValid) "" else errorMessage)
    }

    private fun resetTfWeightAmount() {
        tfWeightAmount?.apply {
            textFieldInput.setText("")
            setError(false)
            setMessage("")
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductShipmentComponent::class.java).inject(this)
    }

}
