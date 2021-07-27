package com.tokopedia.smartbills.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.prefix_select.RechargeValidation
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.CategoryTelcoType
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.activity.SmartBillsAddTelcoActivity
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsAddTelcoViewModel
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
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
        showLoader()
        getMenuDetailTicker()
        getPrefixTelco()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLayout()
    }

    private fun getMenuDetailTicker(){
        viewModel.getMenuDetailAddTelco(viewModel.createMenuDetailAddTelcoParams(getMenuID()))
    }

    private fun getPrefixTelco(){
        viewModel.getPrefixAddTelco(getMenuID())
    }

    private fun getMenuID(): Int{
        return menuId.toIntOrZero()
    }

    private fun showLayout(){
        observeTicker()
        observePrefix()
        observeSelectedPrefix()
        showProductTextField()
    }

    private fun observeTicker(){
        observe(viewModel.listTicker){
            when(it){
                is Success -> {
                    if (!it.data.isNullOrEmpty()){
                        showTicker(it.data)
                    } else {
                        hideTicker()
                    }
                }

                is Fail -> {
                    hideTicker()
                }
            }
            hideLoader()
        }
    }

    private fun observePrefix(){
        observe(viewModel.catalogPrefixSelect){
            when(it){
                is Fail -> {}
                is Success -> {
                    validationsPhoneNumber = it.data.rechargeCatalogPrefixSelect.
                    validations.toMutableList()
                }
            }
        }
    }

    private fun observeSelectedPrefix(){
        observe(viewModel.selectedOperator){
            renderIconOperator(it.operator.attributes.imageUrl)
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
            tickerAdapter.setDescriptionClickEvent(object: TickerCallback{
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
        hideTicker()
    }

    private fun hideLoader(){
        loader_sbm_add_telco.hide()
    }

    private fun showProductTextField(){
        text_field_sbm_product_type.apply {
            show()
            textFieldInput.setText(CategoryTelcoType.getCategoryString(categoryId))
            isEnabled = false
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

        text_field_sbm_product_nominal.apply {
            show()
            textFieldInput.keyListener = null
            textFieldInput.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {

                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })
        }
    }

    private fun onInputNumberChanged(inputNumber: String){
        viewModel.getSelectedOperator(inputNumber)
    }

    private fun renderIconOperator(imageUrl: String){
        text_field_sbm_product_number.setFirstIcon(imageUrl)
        text_field_sbm_product_number.getFirstIcon().show()
    }

    private fun setErrorNumber(isError: Boolean, message: String){
        text_field_sbm_product_number.apply {
            setError(isError)
            setMessage(message)
        }
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

    companion object {
        fun newInstance() = SmartBillsAddTelcoFragment()
    }
}