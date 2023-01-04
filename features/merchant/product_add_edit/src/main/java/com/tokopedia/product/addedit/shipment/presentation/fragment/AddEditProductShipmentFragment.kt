package com.tokopedia.product.addedit.shipment.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.logisticCommon.data.model.CustomProductLogisticModel
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_SHIPMENT_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.AddEditProductFragment
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_INPUT_MODEL
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISADDING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISDRAFTING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISEDITING
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.KEY_SAVE_INSTANCE_ISFIRSTMOVED
import com.tokopedia.product.addedit.common.util.*
import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
import com.tokopedia.product.addedit.databinding.FragmentAddEditProductShipmentBinding
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.BUNDLE_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.ON_BOARDING_ITEMS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_CODE_CPL
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_ADD_MODE
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.REQUEST_KEY_SHIPMENT
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
import com.tokopedia.product.addedit.shipment.presentation.adapter.ShipmentAdapter
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.EXTRA_CPL_PARAM
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.EXTRA_SHIPPER_SERVICES
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.EXTRA_SHOP_ID
import com.tokopedia.product.addedit.shipment.presentation.constant.AddEditProductShipmentConstants.Companion.EXTRA_SHOW_ONBOARDING_CPL
import com.tokopedia.product.addedit.shipment.presentation.dialog.ShipmentInsuranceBottomSheet
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel
import com.tokopedia.product.addedit.shipment.presentation.viewmodel.AddEditProductShipmentViewModel
import com.tokopedia.product.addedit.tracking.ProductAddShippingTracking
import com.tokopedia.product.addedit.tracking.ProductEditShippingTracking
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

class AddEditProductShipmentFragment :
    AddEditProductFragment(),
    AddEditProductPerformanceMonitoringListener {

    private var binding by autoCleared<FragmentAddEditProductShipmentBinding>()
    private var mainLayout: ViewGroup? = null

    private var tfWeightAmount: TextFieldUnify? = null
    private var tickerShipmentDescription: Ticker? = null
    private var shipperServicesIds: ArrayList<Long>? = arrayListOf()

    private var radiosInsurance: RadioGroup? = null
    private var radioRequiredInsurance: RadioButtonUnify? = null
    private var radioOptionalInsurance: RadioButtonUnify? = null
    private var tickerInsurance: Ticker? = null

    private var shipmentInputLayout: ConstraintLayout? = null
    private var radioStandarShipment: RadioButtonUnify? = null
    private var radioCustomShipment: RadioButtonUnify? = null
    private var rvCpl: RecyclerView? = null
    private var btnCpl: Typography? = null
    private var cplLayout: CardUnify2? = null
    private val cplShipmentGroupAdapter: ShipmentAdapter by lazy { ShipmentAdapter() }

    private var btnEnd: UnifyButton? = null
    private var btnSave: UnifyButton? = null

    // private var productInputModel: ProductInputModel? = null
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
            .addEditProductComponent(
                AddEditProductComponentBuilder
                    .getComponent(requireContext().applicationContext as BaseMainApplication)
            )
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        shopId = userSession.shopId

        arguments?.let {
            val cacheManagerId = AddEditProductShipmentFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager =
                SaveInstanceCacheManager(requireContext(), cacheManagerId)

            cacheManagerId.run {
                val productInputModel = saveInstanceCacheManager.get(
                    EXTRA_PRODUCT_INPUT_MODEL,
                    ProductInputModel::class.java
                ) ?: ProductInputModel()
                shipmentViewModel.setProductInputModel(productInputModel)
                shipmentViewModel.isEditMode = saveInstanceCacheManager.get(
                    EXTRA_IS_EDITING_PRODUCT,
                    Boolean::class.java,
                    false
                ) ?: false
                shipmentViewModel.isAddMode = saveInstanceCacheManager.get(
                    EXTRA_IS_ADDING_PRODUCT,
                    Boolean::class.java,
                    false
                ) ?: false
                shipmentViewModel.isDraftMode =
                    saveInstanceCacheManager.get(EXTRA_IS_DRAFTING_PRODUCT, Boolean::class.java)
                        ?: false
                shipmentViewModel.isFirstMoved =
                    saveInstanceCacheManager.get(EXTRA_IS_FIRST_MOVED, Boolean::class.java) ?: false
            }
            if (shipmentViewModel.isAddMode) {
                ProductAddShippingTracking.trackScreen()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditProductShipmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        setFragmentToUnifyBgColor()

        // set navigation highlight
        highlightNavigationButton(PageIndicator.INDICATOR_SHIPMENT_PAGE)

        // to check whether current fragment is visible or not
        isFragmentVisible = true

        setupViews()
        hideKeyboardWhenTouchOutside()

        setupInsuranceTicker()
        setupInsuranceRadios()

        setupSubmitButton()
        setupOnBackPressed()

        if (GlobalConfig.isSellerApp()) {
            initShipmentData()
        } else {
            hideShipment()
        }

        setupShipmentDescriptionTicker()

        initObservers()

        // PLT monitoring
        stopNetworkRequestPerformanceMonitoring()
        stopPerformanceMonitoring()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (isFragmentVisible) {
            inputAllDataInProductInputModel()
            outState.putString(
                KEY_SAVE_INSTANCE_INPUT_MODEL,
                mapObjectToJson(shipmentViewModel.productInputModel)
            )
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
            shipmentViewModel.isEditMode =
                savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISEDITING)
            shipmentViewModel.isDraftMode =
                savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISDRAFTING)
            shipmentViewModel.isFirstMoved =
                savedInstanceState.getBoolean(KEY_SAVE_INSTANCE_ISFIRSTMOVED)

            if (!productInputModelJson.isNullOrBlank()) {
                // set product input model and and ui of the page
                mapJsonToObject(productInputModelJson, ProductInputModel::class.java).apply {
                    shipmentViewModel.setProductInputModel(this)
                }
            }
        }
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CPL) {
                val shipperServicesIdsInt = data?.getLongArrayExtra(EXTRA_SHIPPER_SERVICES)
                if (shipperServicesIdsInt != null) {
                    shipperServicesIds = arrayListOf()
                    shipperServicesIdsInt.forEach { ids ->
                        shipperServicesIds?.add(ids)
                    }
                    shipperServicesIds?.let {
                        shipmentViewModel.setProductActiveState(it)
                    }
                }
            }
        }
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

    private fun setupViews() {
        tfWeightAmount = binding.weightInputLayout.tfWeightAmount
        tickerShipmentDescription = binding.weightInputLayout.tickerWeight

        radiosInsurance = binding.insuranceInputLayout.radiosInsurance
        radioRequiredInsurance = binding.insuranceInputLayout.radioRequiredInsurance
        radioOptionalInsurance = binding.insuranceInputLayout.radioOptionalInsurance
        tickerInsurance = binding.insuranceInputLayout.tickerInsurance

        shipmentInputLayout = binding.shipmentInputLayout.root
        radioStandarShipment = binding.shipmentInputLayout.radioStandardShipment
        radioCustomShipment = binding.shipmentInputLayout.radioCustomShipment
        rvCpl = binding.shipmentInputLayout.rvShipperGroup
        btnCpl = binding.shipmentInputLayout.btnCpl
        cplLayout = binding.shipmentInputLayout.layoutCpl

        btnSave = binding.btnSave
        btnEnd = binding.btnEnd
        mainLayout = binding.mainLayout
    }

    private fun initShipmentData() {
        if (shipmentViewModel.isAddMode) {
            getCplList(null)
        } else {
            getCplList(shipmentViewModel.productInputModel?.productId ?: 0L)
        }
    }

    private fun getCplList(productId: Long?) {
        shipmentViewModel.getCPLList(
            shopId = shopId.toLong(),
            productId = productId,
            shipmentServicesIds = shipmentViewModel.productInputModel?.shipmentInputModel?.cplModel?.shipmentServicesIds,
            cplParam = shipmentViewModel.productInputModel?.shipmentInputModel?.cplModel?.cplParam
        )
    }

    private fun initObservers() {
        shipmentViewModel.cplList.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    applyShipmentValue(it.data)
                }
                is Fail -> {
                    hideShipment()
                }
            }
        })
        shipmentViewModel.shipmentInputModel.observe(viewLifecycleOwner, {
            applyShipmentInputModel(it)
        })
        shipmentViewModel.hasVariant.observe(viewLifecycleOwner, {
            binding.weightInputLayout.root.isVisible = !it
            setupWeightInput()
        })
    }

    private fun setupWeightInput() {
        tfWeightAmount.setModeToNumberInput()
        tfWeightAmount?.textFieldInput?.imeOptions = EditorInfo.IME_ACTION_DONE
        tfWeightAmount?.textFieldInput?.afterTextChanged {
            validateInputWeight(it)
        }
    }

    private fun setupInsuranceRadios() {
        radioRequiredInsurance.setTitle(getString(R.string.title_shipment_required))
        radioOptionalInsurance.setTitle(getString(R.string.title_shipment_optional))
        radiosInsurance?.setOnCheckedChangeListener { _, checkedId ->
            val isRequired = checkedId == R.id.radio_required_insurance
            if (isRequired) {
                if (shipmentViewModel.isEditMode) {
                    ProductEditShippingTracking.clickInsurance(shopId)
                } else {
                    ProductAddShippingTracking.clickInsurance(shopId)
                }
            }

            tickerInsurance?.isVisible = !isRequired
        }
    }

    private fun setupShipment() {
        shipmentInputLayout?.visible()
        setupShipmentRadios()
    }

    private fun goToCustomProductLogistic(isOnBoarding: Boolean = false) {
        startActivityForResult(
            RouteManager.getIntent(
                context,
                ApplinkConstInternalLogistic.CUSTOM_PRODUCT_LOGISTIC
            ).apply {
                putExtra(EXTRA_SHOP_ID, shopId.toLong())
                if (!shipmentViewModel.isAddMode) {
                    putExtra(EXTRA_PRODUCT_ID, shipmentViewModel.productInputModel?.productId)
                }
                putExtra(EXTRA_SHOW_ONBOARDING_CPL, isOnBoarding)
                putExtra(EXTRA_SHIPPER_SERVICES, shipperServicesIds?.toLongArray())
                putExtra(
                    EXTRA_CPL_PARAM,
                    shipmentViewModel.productInputModel?.shipmentInputModel?.cplModel?.cplParam?.toLongArray()
                )
            },
            REQUEST_CODE_CPL
        )
    }

    private fun setupShipmentRadios() {
        radioStandarShipment?.setOnClickListener {
            showDialogStandardShipment()
        }

        radioCustomShipment?.setOnClickListener {
            changeShipmentType(false)
        }
    }

    private fun setShipmentLayout(isStandardShipment: Boolean, data: CustomProductLogisticModel) {
        if (isStandardShipment) {
            cplLayout?.gone()
        } else {
            cplLayout?.visible()
            rvCpl?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = cplShipmentGroupAdapter
            }

            btnCpl?.setOnClickListener {
                goToCustomProductLogistic()
            }
            cplShipmentGroupAdapter.updateData(
                data.shipperList.filter {
                    it.getActiveServiceName().isNotEmpty()
                }
            )
            if (data.shouldShowOnBoarding) {
                doOnBoardingCpl()
            }
        }
    }

    private fun doOnBoardingCpl() {
        activity?.let {
            if (SharedPreferencesUtil.shouldShowCPLWhitelabelOnBoarding(it)) {
                showOnBoardingCoachmark()
            }
        }
    }

    private fun showOnBoardingCoachmark() {
        context?.let {
            val coachMarkItem = generateOnBoardingCoachmark()
            CoachMark2(it).apply {
                setOnBoardingListener()
                showCoachMark(coachMarkItem)
            }
        }
    }

    private fun generateOnBoardingCoachmark(): ArrayList<CoachMark2Item> {
        val coachMarkItems = ArrayList<CoachMark2Item>()
        cplLayout?.let { card ->
            (0 until ON_BOARDING_ITEMS).forEach { _ ->
                coachMarkItems.add(
                    CoachMark2Item(
                        card,
                        getString(R.string.cpl_onboarding_title),
                        getString(R.string.cpl_onboarding_description)
                    )
                )
            }
        }
        return coachMarkItems
    }

    private fun CoachMark2.setOnBoardingListener() {
        this.setStepListener(object : CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                if (currentIndex > 0) {
                    this@setOnBoardingListener.isDismissed = true
                    this@setOnBoardingListener.dismissCoachMark()
                    shipmentViewModel.setAlreadyShowOnBoarding()
                    activity?.let { SharedPreferencesUtil.setCPLWhitelabelOnBoarding(it, false) }
                    goToCustomProductLogistic(isOnBoarding = true)
                }
            }
        })
    }

    private fun changeShipmentType(isStandardShipment: Boolean) {
        shipmentViewModel.setAllCPLProductActiveState(!isStandardShipment)
    }

    private fun showDialogStandardShipment() {
        var isStandardShipment = false
        DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.title_standard_shipment))
            setDefaultMaxWidth()
            setDescription(getString(R.string.description_standard_shipment))
            setPrimaryCTAText(getString(R.string.primary_button_standard_shipment))
            setSecondaryCTAText(getString(R.string.secondary_button_standard_shipment))
            setPrimaryCTAClickListener {
                isStandardShipment = true
                dismiss()
            }
            setSecondaryCTAClickListener {
                isStandardShipment = false
                dismiss()
            }
            setOnDismissListener {
                if (isStandardShipment) {
                    changeShipmentType(true)
                } else {
                    setCplRadioButtonState(true)
                }
            }
        }.show()
    }

    private fun setupSubmitButton() {
        btnEnd?.setOnClickListener {
            val productLimitationModel = SharedPreferencesUtil
                .getProductLimitationModel(requireActivity()) ?: ProductLimitationModel()
            val isEligible = productLimitationModel.isEligible

            if (isEligible) {
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
        tickerInsurance?.setDescriptionClick {
            ShipmentInsuranceBottomSheet().show(childFragmentManager)
        }
    }

    private fun setupShipmentDescriptionTicker() {
        tickerShipmentDescription?.setTextDescription(
            context?.resources?.getString(R.string.label_shipment_weight_ticker).toString()
        )
    }

    fun sendDataBack() {
        if (shipmentViewModel.isAddMode && !shipmentViewModel.isDraftMode) {
            var dataBackPressed = NO_DATA
            if (shipmentViewModel.isFirstMoved) {
                inputAllDataInProductInputModel()
                dataBackPressed = SHIPMENT_DATA
                shipmentViewModel.productInputModel?.requestCode =
                    arrayOf(DETAIL_DATA, DESCRIPTION_DATA, SHIPMENT_DATA)
            }
            setFragmentResultWithBundle(REQUEST_KEY_ADD_MODE, dataBackPressed)
        } else {
            setFragmentResultWithBundle(REQUEST_KEY_SHIPMENT)
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    sendDataBack()

                    if (shipmentViewModel.isEditMode) {
                        ProductEditShippingTracking.clickBack(shopId)
                    } else {
                        ProductAddShippingTracking.clickBack(shopId)
                    }
                }
            }
        )
    }

    private fun inputAllDataInProductInputModel() {
        shipmentViewModel.productInputModel?.isDataChanged = true
        shipmentViewModel.productInputModel?.shipmentInputModel?.apply {
            isMustInsurance = radioRequiredInsurance?.isChecked == true
            weight = tfWeightAmount.getTextIntOrZero()
            cplModel.shipmentServicesIds = shipperServicesIds
            isUsingParentWeight = binding.weightInputLayout.root.isVisible
        }
    }

    private fun applyShipmentInputModel(inputModel: ShipmentInputModel) {
        tfWeightAmount.setText(if (inputModel.weight.isZero()) "" else inputModel.weight.getNumberFormatted())

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

    private fun applyShipmentValue(data: CustomProductLogisticModel) {
        val activatedSpIds = data.getActivatedSpIds()
        val isCplActivated = activatedSpIds.isNotEmpty()
        setupShipment()
        setSelectedShipperService(activatedSpIds)
        setCplRadioButtonState(isCplActivated)
        setShipmentLayout(!isCplActivated, data)
    }

    private fun setCplRadioButtonState(cplActivated: Boolean) {
        radioStandarShipment?.isChecked = !cplActivated
        radioCustomShipment?.isChecked = cplActivated
    }

    private fun setSelectedShipperService(activatedSpIds: List<Long>) {
        shipperServicesIds = if (activatedSpIds.isEmpty()) {
            arrayListOf()
        } else {
            ArrayList(activatedSpIds)
        }
    }

    private fun hideShipment() {
        shipmentInputLayout?.gone()
    }

    private fun showProductLimitationBottomSheet(productLimitationModel: ProductLimitationModel) {
        val actionItems = productLimitationModel.actionItems
        val bottomSheet = ProductLimitationBottomSheet(
            actionItems,
            productLimitationModel.isEligible,
            productLimitationModel.limitAmount
        )

        bottomSheet.setOnBottomSheetResult { urlResult ->
            when {
                urlResult.startsWith(ProductLimitationBottomSheet.RESULT_FINISH_ACTIVITY) -> {
                    activity?.finish()
                }
                urlResult.startsWith(ProductLimitationBottomSheet.RESULT_SAVING_DRAFT) -> {
                    val intent = RouteManager.getIntent(
                        context,
                        ApplinkConstInternalMechant.MERCHANT_PRODUCT_DRAFT
                    )
                    startActivity(intent)
                    shipmentViewModel.productInputModel?.let { shipmentViewModel.saveProductDraft(it) }
                    activity?.finish()
                }
                urlResult.startsWith(HTTP_PREFIX) -> {
                    RouteManager.route(
                        context,
                        "${ApplinkConst.WEBVIEW}?url=$urlResult"
                    )
                }
                else -> {
                    val intent = RouteManager.getIntent(context, urlResult)
                    startActivity(intent)
                }
            }
        }
        bottomSheet.setIsSavingToDraft(true)
        bottomSheet.setSubmitButtonText(getString(R.string.label_product_limitation_bottomsheet_button_draft))
        bottomSheet.show(childFragmentManager, context)
    }

    private fun validateInputWeight(inputText: String): Boolean {
        val errorMessage = shipmentViewModel.validateWeightInput(inputText)
        val isValid = errorMessage.isEmpty()
        tfWeightAmount?.setError(!isValid)
        tfWeightAmount?.setMessage(errorMessage)
        btnEnd?.isEnabled = isValid
        btnSave?.isEnabled = isValid
        if (!isValid) {
            btnEnd?.isLoading = false
            btnSave?.isLoading = false
        }
        return isValid
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboardWhenTouchOutside() {
        mainLayout?.setOnTouchListener { _, _ ->
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

    private fun setFragmentResultWithBundle(
        requestKey: String,
        dataBackPressed: Int = SHIPMENT_DATA
    ) {
        arguments?.let {
            val cacheManagerId = AddEditProductShipmentFragmentArgs.fromBundle(it).cacheManagerId
            SaveInstanceCacheManager(requireContext(), cacheManagerId)
                .put(EXTRA_PRODUCT_INPUT_MODEL, shipmentViewModel.productInputModel)

            val bundle = Bundle().apply {
                putString(BUNDLE_CACHE_MANAGER_ID, cacheManagerId)
                putInt(BUNDLE_BACK_PRESSED, dataBackPressed)
            }
            setNavigationResult(bundle, requestKey)
            findNavController().navigateUp()
        }
    }
}
