package com.tokopedia.product.addedit.shipment.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_SHIPMENT_INPUT
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.shipment.presentation.viewmodel.AddEditProductShipmentViewModel
import com.tokopedia.product.addedit.tracking.ProductAddShippingTracking
import com.tokopedia.product.addedit.tracking.ProductEditShippingTracking
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddEditProductShipmentFragment : BaseDaggerFragment() {
    private var tfWeightAmount: TextFieldUnify? = null
    private var tfWeightUnit: TextFieldUnify? = null
    private var switchInsurance: SwitchUnify? = null
    private var btnEnd: UnifyButton? = null
    private var productInputModel: ProductInputModel? = null
    private var btnSave: UnifyButton? = null
    private var selectedWeightPosition: Int = 0

    private lateinit var userSession: UserSessionInterface
    private lateinit var shopId: String

    @Inject
    lateinit var shipmentViewModel: AddEditProductShipmentViewModel

    companion object {
        fun createInstance(productInputModel: ProductInputModel): Fragment {
            return AddEditProductShipmentFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
                }
            }
        }
        fun createInstanceEditMode(shipmentInputModel: ShipmentInputModel, isAddMode: Boolean): Fragment {
            return AddEditProductShipmentFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SHIPMENT_INPUT_MODEL, shipmentInputModel)
                    putBoolean(EXTRA_IS_EDITMODE, true)
                    putBoolean(EXTRA_IS_ADD_MODE, isAddMode)
                }
            }
        }

        fun getWeightTypeTitle(type: Int) =
                when (type) {
                    UNIT_GRAM -> R.string.label_weight_gram
                    UNIT_KILOGRAM -> R.string.label_weight_kilogram
                    else -> -1
                }

        const val EXTRA_SHIPMENT_INPUT_MODEL = "shipment_input_model"
        const val EXTRA_IS_EDITMODE = "shipment_is_editmode"
        const val EXTRA_IS_ADD_MODE = "shipment_is_add_mode"
        const val REQUEST_CODE_SHIPMENT = 0x04
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductShipmentComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        userSession = UserSession(requireContext())
        shopId = userSession.shopId
        super.onCreate(savedInstanceState)
        arguments?.run {
            val shipmentInputModel: ShipmentInputModel = getParcelable(EXTRA_SHIPMENT_INPUT_MODEL) ?: ShipmentInputModel()
            val isEditMode = getBoolean(EXTRA_IS_EDITMODE, false)
            val isAddMode = getBoolean(EXTRA_IS_ADD_MODE, false)
            productInputModel = getParcelable(EXTRA_PRODUCT_INPUT_MODEL) ?: ProductInputModel()
            shipmentViewModel.shipmentInputModel = shipmentInputModel
            shipmentViewModel.isEditMode = isEditMode
            shipmentViewModel.isAddMode = isAddMode
        }
        if (shipmentViewModel.isAddMode || !shipmentViewModel.isEditMode) {
            ProductAddShippingTracking.trackScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_shipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tfWeightUnit = view.findViewById(R.id.tf_weight_unit)
        tfWeightAmount = view.findViewById(R.id.tf_weight_amount)
        switchInsurance = view.findViewById(R.id.switch_insurance)
        btnSave = view.findViewById(R.id.btn_save)
        btnEnd = view.findViewById(R.id.btn_end)
        tfWeightAmount.setModeToNumberInput()
        tfWeightUnit?.apply {
            textFieldInput.setText(getWeightTypeTitle(0))
            textFieldInput.isFocusable = false // disable focus
            textFieldInput.isActivated = false // disable focus
            textFieldInput.setOnClickListener {
                showUnitWeightOption()
            }
        }
        applyShipmentInputModel()
        tfWeightAmount?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val inputWeight = s?.toString()?.replace(".", "")
                inputWeight?.let { validateInputWeight(it) }
            }

            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val inputWeight = charSequence.toString()
                if (inputWeight == "") {
                    tfWeightAmount?.textFieldInput?.setText("0")
                }
            }

        })
        btnEnd?.setOnClickListener {
            btnEnd?.isLoading = true
            submitInput()
        }
        btnSave?.setOnClickListener {
            btnSave?.isLoading = true
            if(shipmentViewModel.isAddMode) {
                submitInput()
            } else {
                submitInputEdit()
            }
        }
        switchInsurance?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (shipmentViewModel.isEditMode && !shipmentViewModel.isAddMode) {
                ProductEditShippingTracking.clickInsurance(shopId)
            } else {
                ProductAddShippingTracking.clickInsurance(shopId)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        btnEnd?.isLoading = false
        btnSave?.isLoading = false
    }

    fun sendDataBack() {
        if(!shipmentViewModel.isEditMode) {
            inputAllDataInInputDraftModel()
            val intent = Intent()
            intent.putExtra(AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL, productInputModel)
            intent.putExtra(AddEditProductPreviewConstants.EXTRA_BACK_PRESSED, 3)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        } else {
            activity?.finish()
        }
    }

    fun onBackPressed() {
        if (shipmentViewModel.isEditMode && !shipmentViewModel.isAddMode) {
            ProductEditShippingTracking.clickBack(shopId)
        } else {
            ProductAddShippingTracking.clickBack(shopId)
        }
    }

    private fun inputAllDataInInputDraftModel() {
        productInputModel?.shipmentInputModel?.apply {
            isMustInsurance = switchInsurance?.isChecked == true
            weight = tfWeightAmount.getTextIntOrZero()
            weightUnit = selectedWeightPosition
        }
    }

    private fun applyShipmentInputModel() {
        val inputModel = shipmentViewModel.shipmentInputModel
        val weightUnitResId = getWeightTypeTitle(inputModel.weightUnit)
        val weightUnit = getString(weightUnitResId)
        selectedWeightPosition = inputModel.weightUnit
        tfWeightUnit.setText(weightUnit)
        tfWeightAmount.setText(inputModel.weight.toString())
        switchInsurance?.isChecked = inputModel.isMustInsurance
        if (shipmentViewModel.isEditMode || shipmentViewModel.isAddMode) {
            btnEnd?.visibility = View.GONE
            btnSave?.visibility = View.VISIBLE
        }
    }

    private fun showUnitWeightOption() {
        if (shipmentViewModel.isEditMode && !shipmentViewModel.isAddMode) {
            ProductEditShippingTracking.clickWeightDropDown(shopId)
        } else {
            ProductAddShippingTracking.clickWeightDropDown(shopId)
        }
        fragmentManager?.let {
            val optionPicker = OptionPicker()
            optionPicker.setCloseClickListener {
                if (shipmentViewModel.isEditMode && !shipmentViewModel.isAddMode) {
                    ProductEditShippingTracking.clickCancelChangeWeight(shopId)
                } else {
                    ProductAddShippingTracking.clickCancelChangeWeight(shopId)
                }
                optionPicker.dismiss()
            }
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

            optionPicker.setOnItemClickListener { selectedText: String, selectedPosition: Int ->
                if (shipmentViewModel.isEditMode && !shipmentViewModel.isAddMode) {
                    ProductEditShippingTracking.clickChooseWeight(shopId, selectedPosition == 0)
                } else {
                    ProductAddShippingTracking.clickChooseWeight(shopId, selectedPosition == 0)
                }
                tfWeightUnit?.textFieldInput?.setText(selectedText)
                selectedWeightPosition = selectedPosition
                resetTfWeightAmount()
            }
        }
    }

    private fun validateInputWeight(inputText: String): Boolean {
        val inputWeight = inputText.toIntOrZero()
        var errorMessage = ""
        if (inputWeight == 0) {
            errorMessage = getString(R.string.error_weight_is_zero)
        } else if (selectedWeightPosition == UNIT_GRAM && inputWeight > 500000) {
            errorMessage = getString(R.string.error_weight_exceed_in_gram)
        } else if (selectedWeightPosition == UNIT_KILOGRAM && inputWeight > 500) {
            errorMessage = getString(R.string.error_weight_exceed_in_kilogram)
        }
        val isValid = shipmentViewModel.isWeightValid(inputText, selectedWeightPosition)
        tfWeightAmount?.setError(!isValid)
        tfWeightAmount?.setMessage(if (isValid) "" else errorMessage)
        btnEnd?.isEnabled = isValid
        btnSave?.isEnabled = isValid
        return isValid
    }

    private fun resetTfWeightAmount() {
        tfWeightAmount?.apply {
            textFieldInput.setText("")
            setError(false)
            setMessage("")
        }
    }

    private fun submitInput() {
        if (validateInputWeight(tfWeightAmount.getText())) {
            val shipmentInputModel = ShipmentInputModel(
                tfWeightAmount.getTextIntOrZero(),
                selectedWeightPosition,
                switchInsurance?.isChecked == true
            )
            val intent = Intent()
            intent.putExtra(EXTRA_SHIPMENT_INPUT, shipmentInputModel)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun submitInputEdit() {
        if (validateInputWeight(tfWeightAmount.getText())) {
            val shipmentInputModel = ShipmentInputModel(
                    tfWeightAmount.getTextIntOrZero(),
                    selectedWeightPosition,
                    switchInsurance?.isChecked == true
            )
            val intent = Intent()
            intent.putExtra(EXTRA_SHIPMENT_INPUT, shipmentInputModel)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
            if (shipmentViewModel.isEditMode) {
                ProductEditShippingTracking.clickFinish(shopId, true)
            }
        } else {
            if (shipmentViewModel.isEditMode) {
                ProductEditShippingTracking.clickFinish(shopId, false)
            }
        }
    }
}
