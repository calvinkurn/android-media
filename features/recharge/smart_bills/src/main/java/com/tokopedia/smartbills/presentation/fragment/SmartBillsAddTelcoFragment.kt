package com.tokopedia.smartbills.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.prefix_select.RechargeValidation
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.CategoryTelcoType
import com.tokopedia.smartbills.data.RechargeProduct
import com.tokopedia.smartbills.data.RechargeSBMAddBillRequest
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsAddTelcoActivity
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsAddTelcoViewModel
import com.tokopedia.smartbills.presentation.widget.SmartBillAddInquiryCallback
import com.tokopedia.smartbills.presentation.widget.SmartBillsGetNominalCallback
import com.tokopedia.smartbills.presentation.widget.SmartBillsInquiryBottomSheet
import com.tokopedia.smartbills.presentation.widget.SmartBillsNominalBottomSheet
import com.tokopedia.smartbills.util.SmartBillsGlobalError.errorSBMHandlerGlobalError
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_smart_bills_add_telco.*
import java.util.regex.Pattern
import javax.inject.Inject

class SmartBillsAddTelcoFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: SmartBillsAddTelcoViewModel

    private var templateTelco: String? = null
    private var categoryId: String? = null
    private var menuId: String? = null
    private var validationsPhoneNumber: List<RechargeValidation> = emptyList()
    private var operatorActive: TelcoOperator = TelcoOperator()
    private var selectedProduct: RechargeProduct? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SmartBillsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        templateTelco = (activity as SmartBillsAddTelcoActivity).getTemplateTelco()
        categoryId = (activity as SmartBillsAddTelcoActivity).getCategoryId()
        menuId = (activity as SmartBillsAddTelcoActivity).getMenuId()
        return inflater.inflate(R.layout.fragment_smart_bills_add_telco, container,
                false)
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLayout()
    }

    private fun loadData(){
        getMenuDetailTicker()
        getPrefixTelco()
    }

    private fun showLayout(){
        observeTicker()
        observePrefix()
        observeSelectedPrefix()
        observeInquiry()
        observeAddBill()
    }

    private fun getMenuDetailTicker(){
        showLoader()
        hideTicker()
        viewModel.getMenuDetailAddTelco(viewModel.createMenuDetailAddTelcoParams(menuId.toIntOrZero()))
    }

    private fun getPrefixTelco(){
        viewModel.getPrefixAddTelco(menuId.toIntOrZero())
    }

    private fun onInputNumberChanged(inputNumber: String){
        viewModel.getSelectedOperator(inputNumber)
    }

    private fun getInquiryData(){
        viewModel.getInquiryData(viewModel.createInquiryParam(operatorActive.attributes.defaultProductId, getNumber()))
    }

    private fun addBills(productId: Int, clientNumber: String){
        viewModel.addBill(viewModel.createAddBillsParam(RechargeSBMAddBillRequest(productId, clientNumber)))
    }

    private fun observeTicker(){
        observe(viewModel.listTicker){
            when(it){
                is Success -> {
                    if (!it.data.isNullOrEmpty()) {
                        showTicker(it.data)
                    } else {
                        hideTicker()
                    }
                }

                is Fail -> {
                    hideTicker()
                }
            }
        }
    }

    private fun observePrefix(){
        observe(viewModel.catalogPrefixSelect){
            when(it){
                is Fail -> {
                    showErrorState(it.throwable)
                }
                is Success -> {
                    validationsPhoneNumber = it.data.rechargeCatalogPrefixSelect.validations.toMutableList()
                    showMainLayouts()
                    hideLoader()
                }
            }
        }
    }

    private fun observeSelectedPrefix(){
        observe(viewModel.selectedOperator){
            operatorActive = it.operator
            renderIconOperator(it.operator.attributes.imageUrl)
        }
    }

    private fun observeInquiry(){
        observe(viewModel.inquiryData){
            isButtonTelcoLoading(false)
            when(it){
                is Fail -> {
                    val throwable = it.throwable
                    view?.let {
                        Toaster.build(it, ErrorHandler.getErrorMessage(context, throwable), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                    }

                }
                is Success -> {
                    showInquiryBottomSheet(it.data)
                }
           }
        }
    }

    private fun observeAddBill(){
        observe(viewModel.rechargeAddBills){
            when(it){
                is Fail -> {
                    val throwable = it.throwable
                    view?.let {
                        Toaster.build(it, ErrorHandler.getErrorMessage(context, throwable), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                    }

                }
                is Success -> {
                    val errorMessage = it.data.rechargeSBMAddBill.errorMessage
                    val message = it.data.rechargeSBMAddBill.message
                    if(!errorMessage.isNullOrEmpty()){
                        view?.let {
                            Toaster.build(it, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR,
                                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                        }
                    } else {
                        if (!message.isNullOrEmpty()){
                            view?.let {
                                Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL,
                                        getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                            }
                        }
                        //todo add finish() and activity result
                        RouteManager.route(context, ApplinkConsInternalDigital.SMART_BILLS)
                    }
                }
            }
        }
    }

    private fun hideTicker(){
        ticker_sbm_add_telco.hide()
    }

    private fun showTicker(listTicker: List<TopupBillsTicker>){
        ticker_sbm_add_telco.show()
        val messages = ArrayList<TickerData>()
        for (item in listTicker) {
            messages.add(TickerData(item.name, item.content,
                    when (item.type) {
                        "warning" -> Ticker.TYPE_WARNING
                        "info" -> Ticker.TYPE_INFORMATION
                        "success" -> Ticker.TYPE_ANNOUNCEMENT
                        "error" -> Ticker.TYPE_ERROR
                        else -> Ticker.TYPE_INFORMATION
                    }, isFromHtml = true))
        }
        context?.run {
            val tickerAdapter = TickerPagerAdapter(this, messages)
            tickerAdapter.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.route(context, linkUrl.toString())
                }

                override fun onDismiss() {}
            })

            ticker_sbm_add_telco.addPagerView(tickerAdapter, messages)
        }
    }

    private fun showLoader(){
        loader_sbm_add_telco.show()
    }

    private fun hideLoader(){
        loader_sbm_add_telco.hide()
    }

    private fun hideMainLayouts(){
        text_field_sbm_product_type.hide()
        text_field_sbm_product_number.hide()
        text_field_sbm_product_nominal.hide()
        btn_sbm_add_telco.hide()
    }

    private fun showMainLayouts(){
        if(isPrepaid()) {
            text_field_sbm_product_type.apply {
                show()
                textFieldInput.setText(CategoryTelcoType.getCategoryString(categoryId))
                isEnabled = false
            }

            text_field_sbm_product_nominal.apply {
                show()
                textFieldInput.keyListener = null
                textFieldInput.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                        when (event?.action) {
                            MotionEvent.ACTION_DOWN -> {
                                if (getNumber().length >= SmartBillsAddTelcoViewModel.NUMBER_MIN_VALUE
                                        && getNumber().length <= SmartBillsAddTelcoViewModel.NUMBER_MAX_CHECK_VALUE
                                        && !operatorActive.id.isNullOrEmpty()
                                        && !menuId.isNullOrEmpty()
                                ) {
                                    SmartBillsNominalBottomSheet.newInstance(menuId.toIntOrZero(),
                                            categoryId.orEmpty(), operatorActive.id, getNumber(), object : SmartBillsGetNominalCallback {
                                        override fun onProductClicked(rechargeProduct: RechargeProduct) {
                                            renderSelectedProduct(rechargeProduct)
                                        }
                                    }).show(childFragmentManager)
                                } else validationNumber()
                            }
                        }
                        return v?.onTouchEvent(event) ?: true
                    }
                })
            }
        }
        text_field_sbm_product_number.apply {
            show()
            textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    text_field_sbm_product_number.getFirstIcon().hide()
                    onInputNumberChanged(getNumber())
                    validationNumber()
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
        }

        btn_sbm_add_telco.apply {
            show()
            setOnClickListener {
                hideKeyBoard()
                if (isPostaid()) {
                    if (getNumber().length >= SmartBillsAddTelcoViewModel.NUMBER_MIN_VALUE
                            && getNumber().length <= SmartBillsAddTelcoViewModel.NUMBER_MAX_CHECK_VALUE
                            && !operatorActive.id.isNullOrEmpty()){
                                isButtonTelcoLoading(true)
                                getInquiryData()
                            } else {
                                validationNumber()
                            }

                } else if (isPrepaid()) {
                    if (getNumber().length >= SmartBillsAddTelcoViewModel.NUMBER_MIN_VALUE
                            && getNumber().length <= SmartBillsAddTelcoViewModel.NUMBER_MAX_CHECK_VALUE
                            && !selectedProduct?.id.isNullOrEmpty()){
                        isButtonTelcoLoading(true)
                        addBills(selectedProduct?.id.toIntOrZero(), getNumber())
                    } else {
                        validationNumber()
                    }
                }
            }
        }
    }

    private fun renderIconOperator(imageUrl: String){
        text_field_sbm_product_number.setFirstIcon(imageUrl)
        text_field_sbm_product_number.getFirstIcon().show()
    }

    private fun renderSelectedProduct(rechargeProduct: RechargeProduct){
        selectedProduct = rechargeProduct
        text_field_sbm_product_nominal.textFieldInput.setText(selectedProduct?.attributes?.price)
    }

    private fun getNumber(): String = text_field_sbm_product_number.textFieldInput.text.toString()

    private fun validationNumber(){
        if (!validationsPhoneNumber.isNullOrEmpty()) {
            for (validation in validationsPhoneNumber) {
                val phoneIsValid = Pattern.compile(validation.rule)
                        .matcher(getNumber()).matches()
                if (!phoneIsValid) {
                    setErrorNumber(true, validation.message)
                    break
                }
                setErrorNumber(false, "")
            }
        }
    }

    private fun showInquiryBottomSheet(inquiry: TopupBillsEnquiryData){
        val attribute = inquiry.enquiry.attributes
        val inquiryBottomSheet = SmartBillsInquiryBottomSheet(object : SmartBillAddInquiryCallback{
            override fun onInquiryClicked() {
                addBills(attribute.productId.toIntOrZero(), attribute.clientNumber)
            }
        })
        inquiryBottomSheet.addSBMInquiry(attribute.mainInfoList)
        inquiryBottomSheet.show(requireFragmentManager(), "")
    }

    private fun setErrorNumber(isError: Boolean, message: String){
        text_field_sbm_product_number.apply {
            setError(isError)
            setMessage(message)
        }
    }

    private fun showErrorState(throwable: Throwable){
        hideLoader()
        hideTicker()
        hideMainLayouts()
        context?.let {
            errorSBMHandlerGlobalError(it, throwable, container_error_sbm_add_telco,
                    global_error_sbm_add_telco) {
                loadData()
            }
        }
    }

    private fun isPrepaid(): Boolean {
        return !templateTelco.isNullOrEmpty() &&
                templateTelco.equals(DeeplinkMapperDigitalConst.TEMPLATE_PREPAID_TELCO)
    }

    private fun isPostaid(): Boolean {
        return !templateTelco.isNullOrEmpty() &&
                templateTelco.equals(DeeplinkMapperDigitalConst.TEMPLATE_POSTPAID_TELCO)
    }

    private fun isButtonTelcoLoading(isLoading: Boolean){
        btn_sbm_add_telco.isLoading = isLoading
    }

    private fun hideKeyBoard(){
        val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    companion object {
        fun newInstance() = SmartBillsAddTelcoFragment()
    }
}