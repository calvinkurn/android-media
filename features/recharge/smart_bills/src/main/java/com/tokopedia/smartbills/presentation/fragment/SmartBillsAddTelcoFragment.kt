package com.tokopedia.smartbills.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common.topupbills.data.RechargeSBMAddBillRequest
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.prefix_select.RechargeValidation
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.view.bottomsheet.AddSmartBillsInquiryBottomSheet
import com.tokopedia.common.topupbills.view.bottomsheet.callback.AddSmartBillsInquiryCallBack
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.CategoryTelcoType
import com.tokopedia.smartbills.data.RechargeCatalogProductInputMultiTabData
import com.tokopedia.smartbills.data.RechargeProduct
import com.tokopedia.smartbills.databinding.FragmentSmartBillsAddTelcoBinding
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsAddTelcoActivity
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsAddTelcoViewModel
import com.tokopedia.smartbills.presentation.widget.SmartBillsGetNominalCallback
import com.tokopedia.smartbills.presentation.widget.SmartBillsNominalBottomSheet
import com.tokopedia.smartbills.util.SmartBillsGlobalError.errorSBMHandlerGlobalError
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.regex.Pattern
import javax.inject.Inject

class SmartBillsAddTelcoFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentSmartBillsAddTelcoBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsAddTelcoViewModel

    @Inject
    lateinit var commonTopUpBillsAnalytic: CommonTopupBillsAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var templateTelco: String? = null
    private var categoryId: String? = null
    private var menuId: String? = null
    private var validationsPhoneNumber: List<RechargeValidation> = emptyList()
    private var operatorActive: TelcoOperator = TelcoOperator()
    private var selectedProduct: RechargeProduct? = null
    private var isRequestNominal: Boolean = false
    private var catalogProduct: RechargeCatalogProductInputMultiTabData = RechargeCatalogProductInputMultiTabData()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SmartBillsComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        templateTelco = (activity as SmartBillsAddTelcoActivity).getTemplateTelco()
        categoryId = (activity as SmartBillsAddTelcoActivity).getCategoryId()
        menuId = (activity as SmartBillsAddTelcoActivity).getMenuId()
        binding = FragmentSmartBillsAddTelcoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(SmartBillsAddTelcoViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        showLayout()
    }

    fun onBackPressed() {
        commonTopUpBillsAnalytic.clickBackTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId))
    }

    private fun loadData() {
        getMenuDetailTicker()
        getPrefixTelco()
    }

    private fun showLayout() {
        observePrefix()
        observeSelectedPrefix()
        observeInquiry()
        observeAddBill()
        observeNumberNotfound()
    }

    private fun getMenuDetailTicker() {
        showLoader()
        hideTicker()
    }

    private fun getPrefixTelco() {
        viewModel.getPrefixAddTelco(menuId.toIntSafely())
    }

    private fun onInputNumberChanged(inputNumber: String) {
        viewModel.getSelectedOperator(inputNumber, context?.resources?.getString(R.string.smart_bills_add_bills_number_not_found).orEmpty())
    }

    private fun getInquiryData() {
        viewModel.getInquiryData(viewModel.createInquiryParam(operatorActive.attributes.defaultProductId, getNumber()))
    }

    private fun addBills(productId: Int, clientNumber: String) {
        viewModel.addBill(viewModel.createAddBillsParam(RechargeSBMAddBillRequest(productId, clientNumber)))
    }

    private fun observePrefix() {
        observe(viewModel.catalogPrefixSelect) {
            when (it) {
                is Fail -> {
                    showErrorState(it.throwable)
                }
                is Success -> {
                    validationsPhoneNumber = it.data.rechargeCatalogPrefixSelect.validations.toMutableList()
                    showMainLayouts()
                    showTicker()
                    hideLoader()
                }
            }
        }
    }

    private fun observeSelectedPrefix() {
        observe(viewModel.selectedOperator) {
            if (operatorActive.id.isNullOrEmpty() ||
                (operatorActive.id != it.operator.id) ||
                (
                    operatorActive.attributes.defaultProductId
                        != it.operator.attributes.defaultProductId
                    )
            ) {
                isRequestNominal = true
            }
            operatorActive = it.operator
            renderIconOperator(it.operator.attributes.imageUrl)
        }
    }

    private fun observeInquiry() {
        observe(viewModel.inquiryData) {
            isButtonTelcoLoading(false)
            when (it) {
                is Fail -> {
                    val throwable = it.throwable
                    view?.let {
                        Toaster.build(
                            it,
                            ErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(com.tokopedia.resources.common.R.string.general_label_ok)
                        ).show()
                    }
                }
                is Success -> {
                    showInquiryBottomSheet(it.data)
                }
            }
        }
    }

    private fun observeNumberNotfound() {
        observe(viewModel.inputNumberNotFound) {
            if (!it.isNullOrEmpty()) {
                setErrorNumber(true, it)
            }
        }
    }

    private fun observeAddBill() {
        observe(viewModel.rechargeAddBills) {
            isButtonTelcoLoading(false)
            when (it) {
                is Fail -> {
                    val throwable = it.throwable
                    val (message, key) = ErrorHandler.getErrorMessagePair(context, throwable, ErrorHandler.Builder())
                    message?.let {
                        commonTopUpBillsAnalytic.clickViewErrorToasterTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId), message)
                        view?.let {
                            Toaster.build(
                                it,
                                getString(com.tokopedia.smartbills.R.string.smart_bills_add_bills_custom_error_handling, message, key),
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)
                            ).show()
                        }
                    }
                }
                is Success -> {
                    val errorMessage = it.data.rechargeSBMAddBill.errorMessage
                    val message = it.data.rechargeSBMAddBill.message
                    if (!errorMessage.isNullOrEmpty()) {
                        commonTopUpBillsAnalytic.clickViewErrorToasterTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId), errorMessage)
                        view?.let {
                            val errorHandler = ErrorHandler.getErrorMessage(context, MessageErrorException(message))
                            Toaster.build(
                                it,
                                errorHandler,
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)
                            ).show()
                        }
                    } else {
                        val intent = RouteManager.getIntent(context, ApplinkConsInternalDigital.SMART_BILLS)
                        intent.putExtra(EXTRA_ADD_BILLS_MESSAGE, message)
                        intent.putExtra(EXTRA_ADD_BILLS_CATEGORY, CategoryTelcoType.getCategoryString(categoryId))
                        activity?.setResult(Activity.RESULT_OK, intent)
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun hideTicker() {
        binding?.tickerSbmAddTelco?.hide()
    }

    private fun showTicker() {
        binding?.tickerSbmAddTelco?.show()
        val messages = ArrayList<TickerData>()
        messages.add(TickerData("", getString(R.string.smart_bills_add_bills_ticker_desc), Ticker.TYPE_ANNOUNCEMENT, isFromHtml = true))
        context?.run {
            val tickerAdapter = TickerPagerAdapter(context, messages)
            tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                }
            })
            tickerAdapter.onDismissListener = {
                commonTopUpBillsAnalytic.clickCloseTickerTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId))
            }
            binding?.tickerSbmAddTelco?.addPagerView(tickerAdapter, messages)
        }
    }

    private fun showLoader() {
        binding?.loaderSbmAddTelco?.show()
    }

    private fun hideLoader() {
        binding?.loaderSbmAddTelco?.hide()
    }

    private fun hideMainLayouts() {
        binding?.run {
            textFieldSbmProductType.hide()
            textFieldSbmProductNumber.hide()
            textFieldSbmProductNominal.hide()
            btnSbmAddTelco.hide()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showMainLayouts() {
        if (isPrepaid()) {
            binding?.textFieldSbmProductNominal?.apply {
                textFieldInput.keyListener = null
                textFieldInput.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        when (event?.action) {
                            MotionEvent.ACTION_DOWN -> {
                                if (isNumberValid() &&
                                    !operatorActive.id.isNullOrEmpty() &&
                                    !menuId.isNullOrEmpty()
                                ) {
                                    val position = "1"
                                    commonTopUpBillsAnalytic.clickDropDownListTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId), textFieldWrapper.hint.toString(), position)
                                    SmartBillsNominalBottomSheet.newInstance(
                                        isRequestNominal,
                                        catalogProduct,
                                        menuId.toIntSafely(),
                                        categoryId.orEmpty(),
                                        operatorActive.id,
                                        getNumber(),
                                        object : SmartBillsGetNominalCallback {
                                            override fun onProductClicked(rechargeProduct: RechargeProduct) {
                                                renderSelectedProduct(rechargeProduct)
                                                setErrorNominal(false, "")
                                                isDisableButton()
                                            }

                                            override fun onNominalLoaded(isRequesting: Boolean, catalogProductRecharge: RechargeCatalogProductInputMultiTabData, products: List<RechargeProduct>) {
                                                commonTopUpBillsAnalytic.viewBottomSheetAddBills(
                                                    userSession.userId,
                                                    CategoryTelcoType.getCategoryString(categoryId),
                                                    textFieldWrapper.hint.toString(),
                                                    viewModel.getProductTracker(products, operatorActive.attributes.name, CategoryTelcoType.getCategoryString(categoryId))
                                                )
                                                isRequestNominal = isRequesting
                                                catalogProduct = catalogProductRecharge
                                            }

                                            override fun onCloseNominal() {
                                                commonTopUpBillsAnalytic.clickCloseDropDownListTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId), textFieldWrapper.hint.toString())
                                            }
                                        }
                                    ).show(childFragmentManager)
                                } else {
                                    validationNumber()
                                }
                            }
                        }
                        return v?.onTouchEvent(event) ?: true
                    }
                })
            }
        }

        binding?.textFieldSbmProductType?.apply {
            show()
            textFieldInput.setText(CategoryTelcoType.getCategoryString(categoryId))
            isEnabled = false
        }

        binding?.textFieldSbmProductNumber?.apply {
            show()
            textFieldInput.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_UP -> {
                            commonTopUpBillsAnalytic.clickInputFieldTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId))
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    binding?.textFieldSbmProductNumber?.getFirstIcon()?.hide()
                    onInputNumberChanged(getNumber())
                    validationNumber()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }

        binding?.btnSbmAddTelco?.apply {
            show()
            isDisableButton()
            text = if (isPostPaid()) {
                context?.resources?.getString(com.tokopedia.smartbills.R.string.smart_bills_add_bills_add_telco_post).orEmpty()
            } else {
                context?.resources?.getString(com.tokopedia.smartbills.R.string.smart_bills_add_bills_product_button_inquiry).orEmpty()
            }
            setOnClickListener {
                hideKeyBoard()
                commonTopUpBillsAnalytic.clickTambahTagihanTelcoAddBills(CategoryTelcoType.getCategoryString(categoryId))
                if (!isNumberValid()) {
                    validationNumber()
                } else if (isPostPaid()) {
                    if (!operatorActive.id.isNullOrEmpty()) {
                        isButtonTelcoLoading(true)
                        getInquiryData()
                    }
                } else if (isPrepaid()) {
                    if (!selectedProduct?.id.isNullOrEmpty()) {
                        isButtonTelcoLoading(true)
                        addBills(selectedProduct?.id.toIntSafely(), getNumber())
                    }
                }
            }
        }
    }

    private fun renderIconOperator(imageUrl: String) {
        binding?.textFieldSbmProductNumber?.run {
            setFirstIcon(imageUrl)
            getFirstIcon().show()
        }
    }

    private fun renderSelectedProduct(rechargeProduct: RechargeProduct) {
        selectedProduct = rechargeProduct
        selectedProduct?.attributes?.desc?.let {
            binding?.textFieldSbmProductNominal?.textFieldInput?.setText(it)
        }
    }

    private fun getNumber(): String = binding?.textFieldSbmProductNumber?.textFieldInput?.text?.toString() ?: EMPTY

    private fun validationNumber() {
        if (!validationsPhoneNumber.isNullOrEmpty()) {
            for (validation in validationsPhoneNumber) {
                val phoneIsValid = Pattern.compile(validation.rule)
                    .matcher(getNumber()).matches()
                if (!phoneIsValid) {
                    setErrorNumber(true, validation.message)
                    break
                }
                setErrorNumber(false, "")
                isDisableButton()
            }
        }
    }

    private fun isNumberValid(): Boolean {
        return getNumber().length >= SmartBillsAddTelcoViewModel.NUMBER_MIN_VALUE &&
            getNumber().length <= SmartBillsAddTelcoViewModel.NUMBER_MAX_CHECK_VALUE
    }

    private fun showInquiryBottomSheet(inquiry: TopupBillsEnquiryData) {
        val attribute = inquiry.enquiry.attributes
        val inquiryBottomSheet = AddSmartBillsInquiryBottomSheet(object : AddSmartBillsInquiryCallBack {
            override fun onInquiryClicked() {
                commonTopUpBillsAnalytic.clickAddInquiry(CategoryTelcoType.getCategoryString(categoryId))
                addBills(attribute.productId.toIntSafely(), attribute.clientNumber)
            }

            override fun onInquiryClose() {
                commonTopUpBillsAnalytic.clickOnCloseInquiry(CategoryTelcoType.getCategoryString(categoryId))
            }
        })
        inquiryBottomSheet.addSBMInquiry(attribute)
        inquiryBottomSheet.show(requireFragmentManager(), "")
    }

    private fun setErrorNumber(isError: Boolean, message: String) {
        showNominal(!isError)
        binding?.textFieldSbmProductNumber?.apply {
            setError(isError)
            setMessage(message)
        }
    }

    private fun setErrorNominal(isError: Boolean, message: String) {
        binding?.textFieldSbmProductNominal?.apply {
            setError(isError)
            setMessage(message)
        }
    }

    private fun showNominal(isShow: Boolean) {
        binding?.run {
            if (isShow && isPrepaid()) {
                textFieldSbmProductNominal.apply {
                    textFieldSbmProductNominal.textFieldInput.setText("")
                    selectedProduct = null
                    show()
                    textFieldWrapper.hint = if (CategoryTelcoType.isCategoryPacketData(categoryId)) {
                        context?.resources?.getString(R.string.smart_bills_add_bills_packet_data).orEmpty()
                    } else {
                        context?.resources?.getString(R.string.smart_bills_add_bills_product_nominal_label).orEmpty()
                    }
                    textFieldInput.ellipsize = TextUtils.TruncateAt.END
                }
            } else {
                textFieldSbmProductNominal.hide()
            }
        }
    }

    private fun showErrorState(throwable: Throwable) {
        hideLoader()
        hideTicker()
        hideMainLayouts()
        binding?.run {
            context?.let {
                errorSBMHandlerGlobalError(
                    it,
                    throwable,
                    containerErrorSbmAddTelco,
                    globalErrorSbmAddTelco
                ) {
                    loadData()
                }
            }
        }
    }

    private fun isPrepaid(): Boolean {
        return !templateTelco.isNullOrEmpty() &&
            templateTelco.equals(DeeplinkMapperDigitalConst.TEMPLATE_OLD_PREPAID_TELCO)
    }

    private fun isPostPaid(): Boolean {
        return !templateTelco.isNullOrEmpty() &&
            templateTelco.equals(DeeplinkMapperDigitalConst.TEMPLATE_POSTPAID_TELCO)
    }

    private fun isButtonTelcoLoading(isLoading: Boolean) {
        binding?.btnSbmAddTelco?.isLoading = isLoading
    }

    private fun isDisableButton() {
        binding?.btnSbmAddTelco?.isEnabled = (
            (isNumberValid() && isPostPaid()) ||
                (isNumberValid() && isPrepaid() && !selectedProduct?.id.isNullOrEmpty())
            )
    }

    private fun hideKeyBoard() {
        val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        fun newInstance() = SmartBillsAddTelcoFragment()

        const val EXTRA_ADD_BILLS_MESSAGE = "MESSAGE"
        const val EXTRA_ADD_BILLS_CATEGORY = "CATEGORY"

        private const val EMPTY = ""
    }
}
