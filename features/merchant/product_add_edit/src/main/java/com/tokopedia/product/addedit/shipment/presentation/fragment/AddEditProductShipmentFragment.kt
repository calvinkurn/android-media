package com.tokopedia.product.addedit.shipment.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_INPUT_MODEL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISADDING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISDRAFTING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISEDITING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISFIRSTMOVED
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.common.util.InputPriceUtil.formatProductPriceInput
import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.BUNDLE_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_SHIPMENT
import com.tokopedia.product.addedit.optionpicker.OptionPicker
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.BUNDLE_BACK_PRESSED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DESCRIPTION_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.DETAIL_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_ADDING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_DRAFTING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_EDITING_PRODUCT
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_IS_FIRST_MOVED
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.NO_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.SHIPMENT_DATA
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.UPLOAD_DATA
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.productlimitation.presentation.dialog.ProductLimitationBottomSheet
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationModel
import com.tokopedia.product.addedit.shipment.di.DaggerAddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MAX_WEIGHT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.MIN_WEIGHT
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_GRAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.UNIT_KILOGRAM
import com.tokopedia.product.addedit.shipment.presentation.dialog.ShipmentInsuranceBottomSheet
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.shipment.presentation.viewmodel.AddEditProductShipmentViewModel
import com.tokopedia.product.addedit.tracking.ProductAddShippingTracking
import com.tokopedia.product.addedit.tracking.ProductEditShippingTracking
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddEditProductShipmentFragment:
        BaseDaggerFragment(),
        AddEditProductPerformanceMonitoringListener {

    private var mainLayout: ViewGroup? = null

    private var tfWeightAmount: TextFieldUnify? = null
    private var tfWeightUnit: TextFieldUnify? = null
    private var selectedWeightPosition: Int = 0

    private var radiosInsurance: RadioGroup? = null
    private var radioRequiredInsurance: RadioButtonUnify? = null
    private var radioOptionalInsurance: RadioButtonUnify? = null
    private var tickerInsurance: Ticker? = null

    private var btnEnd: UnifyButton? = null
    private var btnSave: UnifyButton? = null

    private var productInputModel: ProductInputModel? = null
    private var isFragmentVisible = false

    private lateinit var shopId: String
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var shipmentViewModel: AddEditProductShipmentViewModel

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerAddEditProductShipmentComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder
                        .getComponent(requireContext().applicationContext as BaseMainApplication))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        shopId = userSession.shopId

        arguments?.let {
            val cacheManagerId = AddEditProductShipmentFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

            cacheManagerId.run {
                productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java) ?: ProductInputModel()
                shipmentViewModel.shipmentInputModel = productInputModel?.shipmentInputModel ?: ShipmentInputModel()
                shipmentViewModel.isEditMode = saveInstanceCacheManager.get(EXTRA_IS_EDITING_PRODUCT, Boolean::class.java, false) ?: false
                shipmentViewModel.isAddMode = saveInstanceCacheManager.get(EXTRA_IS_ADDING_PRODUCT, Boolean::class.java, false) ?: false
                shipmentViewModel.isDraftMode = saveInstanceCacheManager.get(EXTRA_IS_DRAFTING_PRODUCT, Boolean::class.java) ?: false
                shipmentViewModel.isFirstMoved = saveInstanceCacheManager.get(EXTRA_IS_FIRST_MOVED, Boolean::class.java) ?: false
            }
            if (shipmentViewModel.isAddMode) {
                ProductAddShippingTracking.trackScreen()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_shipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        requireActivity().window.decorView.setBackgroundColor(ContextCompat.getColor(
                requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))

        // to check whether current fragment is visible or not
        isFragmentVisible = true

        setupViews(view)
        hideKeyboardWhenTouchOutside()
        applyShipmentInputModel()

        setupWeightInput()
        setupInsuranceTicker()
        setupInsuranceRadios()
        setupSubmitButton()
        setupOnBackPressed()

        // PLT monitoring
        stopNetworkRequestPerformanceMonitoring()
        stopPerformanceMonitoring()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isFragmentVisible) {
            inputAllDataInProductInputModel()
            outState.putString(KEY_SAVE_INSTANCE_INPUT_MODEL, mapObjectToJson(productInputModel))
            outState.putBoolean(KEY_SAVE_INSTANCE_ISADDING, shipmentViewModel.isAddMode)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISEDITING, shipmentViewModel.isEditMode)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISDRAFTING, shipmentViewModel.isDraftMode)
            outState.putBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED, shipmentViewModel.isFirstMoved)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val productInputModelJson = savedInstanceState.getString(KEY_SAVE_INSTANCE_INPUT_MODEL)
            shipmentViewModel.isAddMode = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISADDING)
            shipmentViewModel.isEditMode = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISEDITING)
            shipmentViewModel.isDraftMode = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISDRAFTING)
            shipmentViewModel.isFirstMoved = savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED)

            if (!productInputModelJson.isNullOrBlank()) {
                //set product input model and and ui of the page
                mapJsonToObject(productInputModelJson, ProductInputModel::class.java).apply {
                    productInputModel = this
                    shipmentViewModel.shipmentInputModel = shipmentInputModel
                    applyShipmentInputModel()
                }
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        btnEnd?.isLoading = false
        btnSave?.isLoading = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isFragmentVisible = false
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ADD_EDIT_PRODUCT_SHIPMENT_PLT_PREPARE_METRICS,
                ADD_EDIT_PRODUCT_SHIPMENT_PLT_NETWORK_METRICS,
                ADD_EDIT_PRODUCT_SHIPMENT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(ADD_EDIT_PRODUCT_SHIPMENT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun setupViews(view: View) {
        tfWeightUnit = view.findViewById(R.id.tf_weight_unit)
        tfWeightAmount = view.findViewById(R.id.tf_weight_amount)

        radiosInsurance = requireView().findViewById(R.id.radios_insurance)
        radioRequiredInsurance = requireView().findViewById(R.id.radio_required_insurance)
        radioOptionalInsurance = requireView().findViewById(R.id.radio_optional_insurance)
        tickerInsurance = requireView().findViewById(R.id.ticker_insurance)

        btnSave = view.findViewById(R.id.btn_save)
        btnEnd = view.findViewById(R.id.btn_end)
        mainLayout = view.findViewById(R.id.main_layout)
    }

    private fun setupWeightInput() {
        tfWeightAmount.setModeToNumberInput()
        tfWeightUnit?.textFieldInput?.apply {
            isFocusable = false // disable focus
            isActivated = false // disable focus
            setOnClickListener {
                showUnitWeightOption()
            }
        }
        tfWeightAmount?.textFieldInput?.afterTextChanged {
            validateInputWeight(it)
        }
    }

    private fun setupInsuranceRadios() {
        radioRequiredInsurance.setTitle(getString(R.string.title_shipment_required))
        radioOptionalInsurance.setTitle(getString(R.string.title_shipment_optional))
        radiosInsurance?.setOnCheckedChangeListener { _, checkedId ->
            val isRequired = checkedId == R.id.radio_required_insurance
            if (isRequired)  {
                if (shipmentViewModel.isEditMode) {
                    ProductEditShippingTracking.clickInsurance(shopId)
                } else {
                    ProductAddShippingTracking.clickInsurance(shopId)
                }
            }

            tickerInsurance?.isVisible = !isRequired
        }
    }

    private fun setupSubmitButton() {
        btnEnd?.setOnClickListener {
            val productLimitationModel = SharedPreferencesUtil.getProductLimitationModel(requireActivity())
            if (productLimitationModel.isEligible) {
                btnEnd?.isLoading = true
                submitInput(UPLOAD_DATA)
            } else {
                showProductLimitationBottomSheet(productLimitationModel)
            }
        }
        btnSave?.setOnClickListener {
            btnSave?.isLoading = true
            if (shipmentViewModel.isAddMode && !shipmentViewModel.isDraftMode) {
                submitInput(SHIPMENT_DATA)
            } else {
                submitInputEdit()
            }
        }
    }

    private fun setupInsuranceTicker() {
        tickerInsurance?.setHtmlDescription(getString(R.string.label_shipment_ticker))
        tickerInsurance?.setOnClickListener {
            ShipmentInsuranceBottomSheet().show(fragmentManager)
        }
    }

    fun sendDataBack() {
        if(shipmentViewModel.isAddMode && !shipmentViewModel.isDraftMode) {
            var dataBackPressed = NO_DATA
            if(shipmentViewModel.isFirstMoved) {
                inputAllDataInProductInputModel()
                dataBackPressed = SHIPMENT_DATA
                productInputModel?.requestCode = arrayOf(DETAIL_DATA, DESCRIPTION_DATA, SHIPMENT_DATA)
            }
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE, dataBackPressed)
        } else {
            setFragmentResultWithBundle(REQUEST_KEY_SHIPMENT)
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendDataBack()

                if (shipmentViewModel.isEditMode) {
                    ProductEditShippingTracking.clickBack(shopId)
                } else {
                    ProductAddShippingTracking.clickBack(shopId)
                }
            }
        })
    }

    private fun inputAllDataInProductInputModel() {
        productInputModel?.isDataChanged = true
        productInputModel?.shipmentInputModel?.apply {
            isMustInsurance = radioRequiredInsurance?.isChecked == true
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

        applyInsuranceValue(inputModel.isMustInsurance)

        if (!(shipmentViewModel.isAddMode && shipmentViewModel.isFirstMoved)) {
            btnEnd?.visibility = View.GONE
            btnSave?.visibility = View.VISIBLE
        } else {
            btnEnd?.visibility = View.VISIBLE
            btnSave?.visibility = View.GONE
        }
    }

    private fun applyInsuranceValue(mustInsurance: Boolean) {
        radioRequiredInsurance?.isChecked = mustInsurance
        radioOptionalInsurance?.isChecked = !mustInsurance
        tickerInsurance?.isVisible = !mustInsurance
    }

    private fun showProductLimitationBottomSheet(productLimitationModel: ProductLimitationModel) {
        val actionItems = productLimitationModel.actionItems
        val bottomSheet = ProductLimitationBottomSheet(actionItems, productLimitationModel.isEligible,
                productLimitationModel.limitAmount)

        bottomSheet.setOnBottomSheetResult { urlResult ->
            when {
                urlResult.startsWith(ProductLimitationBottomSheet.RESULT_FINISH_ACTIVITY) -> {
                    activity?.finish()
                }
                urlResult.startsWith(ProductLimitationBottomSheet.RESULT_SAVING_DRAFT) -> {
                    val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_PRODUCT_DRAFT)
                    startActivity(intent)
                    productInputModel?.let { shipmentViewModel.saveProductDraft(it) }
                    activity?.finish()
                }
                urlResult.startsWith(HTTP_PREFIX) -> {
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, urlResult))
                }
                else -> {
                    val intent = RouteManager.getIntent(context, urlResult)
                    startActivity(intent)
                }
            }
        }
        bottomSheet.setIsSavingToDraft(true)
        bottomSheet.setSubmitButtonText(getString(R.string.label_product_limitation_bottomsheet_button_draft))
        bottomSheet.show(childFragmentManager)
    }

    private fun showUnitWeightOption() {
        if (shipmentViewModel.isEditMode) {
            ProductEditShippingTracking.clickWeightDropDown(shopId)
        } else {
            ProductAddShippingTracking.clickWeightDropDown(shopId)
        }
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

            optionPicker.setCloseClickListener {
                if (shipmentViewModel.isEditMode) {
                    ProductEditShippingTracking.clickCancelChangeWeight(shopId)
                } else {
                    ProductAddShippingTracking.clickCancelChangeWeight(shopId)
                }
                optionPicker.dismiss()
            }

            optionPicker.setOnItemClickListener { selectedText: String, selectedPosition: Int ->
                if (shipmentViewModel.isEditMode) {
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

    private fun getWeightTypeTitle(type: Int) = when (type) {
        UNIT_GRAM -> com.tokopedia.product.addedit.R.string.label_weight_gram
        UNIT_KILOGRAM -> com.tokopedia.product.addedit.R.string.label_weight_kilogram
        else -> com.tokopedia.product.addedit.R.string.label_weight_gram
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

    private fun submitInput(dataBackPressed: Int) {
        if (validateInputWeight(tfWeightAmount.getText())) {
            inputAllDataInProductInputModel()
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE, dataBackPressed)
        }
    }

    private fun submitInputEdit() {
        if (validateInputWeight(tfWeightAmount.getText())) {
            inputAllDataInProductInputModel()
            setFragmentResultWithBundle(REQUEST_KEY_SHIPMENT)
        }
    }

    private fun setFragmentResultWithBundle(requestKey: String, dataBackPressed: Int = SHIPMENT_DATA) {
        arguments?.let {
            val cacheManagerId = AddEditProductShipmentFragmentArgs.fromBundle(it).cacheManagerId
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel)

            val bundle = Bundle().apply {
                putString(BUNDLE_CACHE_MANAGER_ID, cacheManagerId)
                putInt(BUNDLE_BACK_PRESSED, dataBackPressed)
            }
            setNavigationResult(bundle,requestKey)
            findNavController().navigateUp()
        }
    }
}
