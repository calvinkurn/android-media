package com.tokopedia.product.addedit.shipment.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.InputPriceUtil.formatProductPriceInput
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MIN_WEIGHT
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
  
    companion object {
        const val REQUEST_CODE_SHIPMENT = 0x04

        fun createInstance(cacheManagerId: String?): Fragment {
            return AddEditProductShipmentFragment().apply {
                arguments = Bundle().apply {
                    putString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
        fun getWeightTypeTitle(type: Int) =
                when (type) {
                    UNIT_GRAM -> com.tokopedia.product.addedit.R.string.label_weight_gram
                    UNIT_KILOGRAM -> com.tokopedia.product.addedit.R.string.label_weight_kilogram
                    else -> com.tokopedia.product.addedit.R.string.label_weight_gram
                }
    }

    private var mainLayout: ConstraintLayout? = null
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
        val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {
            productInputModel = saveInstanceCacheManager.get(AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()
            shipmentViewModel.shipmentInputModel = productInputModel?.shipmentInputModel ?: ShipmentInputModel()
            shipmentViewModel.isEditMode = saveInstanceCacheManager.get(AddEditProductPreviewConstants.EXTRA_IS_EDITING_PRODUCT, Boolean::class.java, false) ?: false
            shipmentViewModel.isAddMode = saveInstanceCacheManager.get(AddEditProductPreviewConstants.EXTRA_IS_ADDING_PRODUCT, Boolean::class.java, false) ?: false
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
        mainLayout = view.findViewById(R.id.main_layout)

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
        hideKeyboardWhenTouchOutside()
        tfWeightAmount?.textFieldInput?.afterTextChanged {
            validateInputWeight(it)
        }
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
        switchInsurance?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)  {
                if (shipmentViewModel.isEditMode && !shipmentViewModel.isAddMode) {
                    ProductEditShippingTracking.clickInsurance(shopId)
                } else {
                    ProductAddShippingTracking.clickInsurance(shopId)
                }
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
            val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL, productInputModel)

            val intent = Intent()
            intent.putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
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
        val minWeight = formatProductPriceInput(MIN_WEIGHT.toString())
        val maxWeightGram = formatProductPriceInput(MAX_WEIGHT_GRAM.toString())
        val maxWeightKilogram = formatProductPriceInput(MAX_WEIGHT_KILOGRAM.toString())
        val errorMessage = if (selectedWeightPosition == UNIT_GRAM) {
            getString(R.string.error_weight_not_valid, minWeight, maxWeightGram)
        } else {
            getString(R.string.error_weight_not_valid, minWeight, maxWeightKilogram)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboardWhenTouchOutside() {
        mainLayout?.setOnTouchListener{ _, _ ->
            activity?.apply {
                KeyboardHandler.hideSoftKeyboard(this)
            }
            true
        }
    }

    private fun submitInput() {
        if (validateInputWeight(tfWeightAmount.getText())) {
            val shipmentInputModel = ShipmentInputModel(
                tfWeightAmount.getTextIntOrZero(),
                selectedWeightPosition,
                switchInsurance?.isChecked == true
            )
            productInputModel?.shipmentInputModel = shipmentInputModel
            val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL, productInputModel)

            val intent = Intent()
            intent.putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun submitInputEdit() {
        if (validateInputWeight(tfWeightAmount.getText())) {
            inputAllDataInInputDraftModel()
            val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL, productInputModel)

            val intent = Intent()
            intent.putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
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
