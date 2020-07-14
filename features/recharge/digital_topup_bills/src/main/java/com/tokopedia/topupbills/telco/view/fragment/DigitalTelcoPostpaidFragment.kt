package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment.InputNumberActionType
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.data.TelcoCustomComponentData
import com.tokopedia.topupbills.telco.data.TelcoCustomData
import com.tokopedia.topupbills.telco.data.TelcoCustomDataCollection
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalPostpaidClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_digital_telco_postpaid.*

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoPostpaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var postpaidClientNumberWidget: DigitalPostpaidClientNumberWidget
    private lateinit var buyWidget: TopupBillsCheckoutWidget
    private lateinit var enquiryViewModel: DigitalTelcoEnquiryViewModel
    private lateinit var layoutProgressBar: ProgressBar
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private var traceStop = false

    private var operatorSelected: TelcoCustomDataCollection? = null
        set(value) {
            field = value
            value?.run {
                productId = operator.attributes.defaultProductId
            }
        }
    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))
    override var menuId = TelcoComponentType.TELCO_POSTPAID
    override var categoryId = TelcoCategoryType.CATEGORY_PASCABAYAR

    private var inputNumberActionType = InputNumberActionType.MANUAL
    private lateinit var sharedModel: SharedProductTelcoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_POSTPAID_TRACE)

            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            enquiryViewModel = viewModelProvider.get(DigitalTelcoEnquiryViewModel::class.java)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_postpaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        recentNumbersWidget = view.findViewById(R.id.recent_numbers)
        postpaidClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListWidget = view.findViewById(R.id.promo_widget)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        layoutProgressBar = view.findViewById(R.id.layout_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInputFilterDataCollections()
        renderClientNumber()
        handleFocusClientNumber()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
    }

    override fun setupCheckoutData() {
        val inputs = mutableMapOf<String, String>()
        checkoutPassData.clientNumber?.let { clientNumber ->
            inputs[TopupBillsViewModel.EXPRESS_PARAM_CLIENT_NUMBER] = clientNumber
        }
        checkoutPassData.operatorId?.let { operatorId ->
            inputs[TopupBillsViewModel.EXPRESS_PARAM_OPERATOR_ID] = operatorId
        }
        inputFields = inputs
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.promoItem.observe(this, Observer {
            it?.run {
                promoListWidget.notifyPromoItemChanges(this)
            }
        })
        sharedModel.enquiryResult.observe(this, Observer {
            it?.run {
                operatorSelected?.run {
                    checkoutPassData = DigitalCheckoutPassData.Builder()
                            .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                            .categoryId(categoryId.toString())
                            .clientNumber(postpaidClientNumberWidget.getInputNumber())
                            .instantCheckout("0")
                            .isPromo("0")
                            .operatorId(operator.id)
                            .productId(operator.attributes.defaultProductId.toString())
                            .utmCampaign(categoryId.toString())
                            .utmContent(GlobalConfig.VERSION_NAME)
                            .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                            .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                            .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                            .voucherCodeCopied("")
                            .build()
                }
            }
        })
    }

    fun getInputFilterDataCollections() {
        customViewModel.getCustomDataPostpaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_custom_digital_telco),
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    fun getCatalogMenuDetail() {
        getMenuDetail(TelcoComponentType.TELCO_POSTPAID)
        getFavoriteNumbers(TelcoComponentType.FAV_NUMBER_POSTPAID)
    }

    private fun getDataFromBundle(savedInstanceState: Bundle?) {
        var clientNumber = ""
        if (savedInstanceState == null) {
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM) as TopupBillsExtraParam
                clientNumber = digitalTelcoExtraParam.clientNumber
            }
        } else {
            clientNumber = savedInstanceState.getString(CACHE_CLIENT_NUMBER)
        }
        postpaidClientNumberWidget.setInputNumber(clientNumber)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(CACHE_CLIENT_NUMBER, postpaidClientNumberWidget.getInputNumber())
    }

    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return buy_widget
    }

    private fun renderClientNumber() {
        postpaidClientNumberWidget.resetClientNumberPostpaid()
        postpaidClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun onNavigateToContact() {
                inputNumberActionType = InputNumberActionType.CONTACT
                navigateContact()
            }

            override fun onRenderOperator() {
                operatorData.rechargeCustomData.customDataCollections.isEmpty()?.let {
                    if (it) {
                        getInputFilterDataCollections()
                    } else {
                        renderProductFromCustomData()
                    }
                }
                postpaidClientNumberWidget.setButtonEnquiryEnable()
            }

            override fun onClearAutoComplete() {
                topupAnalytics.eventClearInputNumber()

                postpaidClientNumberWidget.resetClientNumberPostpaid()
                recentNumbersWidget.visibility = View.VISIBLE
                promoListWidget.visibility = View.VISIBLE
                buyWidget.setVisibilityLayout(false)
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                postpaidClientNumberWidget.clearFocusAutoComplete()
                startActivityForResult(activity?.let {
                    DigitalSearchNumberActivity.newInstance(it,
                            ClientNumberType.TYPE_INPUT_TEL, clientNumber, favNumberList)
                },
                        REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
            }
        })
        postpaidClientNumberWidget.setPostpaidListener(object : ClientNumberPostpaidListener {
            override fun enquiryNumber() {
                if (userSession.isLoggedIn) {
                    getEnquiryNumber()
                } else {
                    navigateToLoginPage()
                }
            }
        })
    }

    fun getEnquiryNumber() {
        operatorSelected?.let { selectedOperator ->
            var mapParam = HashMap<String, Any>()
            mapParam.put(KEY_CLIENT_NUMBER, postpaidClientNumberWidget.getInputNumber())
            mapParam.put(KEY_PRODUCT_ID, selectedOperator.operator.attributes.defaultProductId.toString())

            enquiryViewModel.getEnquiry(GraphqlHelper.loadRawString(resources, com.tokopedia.common.topupbills.R.raw.query_enquiry_digital),
                    mapParam, this::onSuccessEnquiry, this::onErrorEnquiry)
        }
    }

    override fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.operatorData = telcoData
        renderProductFromCustomData()
    }

    fun renderProductFromCustomData() {
        try {
            if (postpaidClientNumberWidget.getInputNumber().isNotEmpty()) {
                operatorSelected = this.operatorData.rechargeCustomData.customDataCollections.single {
                    postpaidClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                operatorSelected?.run {
                    operatorName = operator.attributes.name
                    when (inputNumberActionType) {
                        InputNumberActionType.MANUAL -> {
                            topupAnalytics.eventInputNumberManual(categoryId, operatorName)
                        }
                        InputNumberActionType.CONTACT -> {
                            topupAnalytics.eventInputNumberContactPicker(categoryId, operatorName)
                        }
                        InputNumberActionType.FAVORITE -> {
                            topupAnalytics.eventInputNumberFavorites(categoryId, operatorName)
                        }
                        InputNumberActionType.CONTACT_HOMEPAGE -> {
                            topupAnalytics.eventInputNumberContactPicker(categoryId, operatorName)
                        }
                        else -> {

                        }
                    }

                    postpaidClientNumberWidget.setIconOperator(operator.attributes.imageUrl)
                }
            }
        } catch (exception: Exception) {
            view?.run {
                postpaidClientNumberWidget.setErrorInputNumber(
                        getString(R.string.telco_number_error_not_found))
            }
        }
    }

    override fun onErrorCustomData(throwable: Throwable) {
        view?.run {
            Toaster.showError(this, ErrorHandler.getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG)
        }
    }

    fun onLoadingMenuDetail(showLoading: Boolean) {
        if (showLoading) {
            layoutProgressBar.visibility = View.VISIBLE
            recentNumbersWidget.visibility = View.GONE
            promoListWidget.visibility = View.GONE
        } else {
            layoutProgressBar.visibility = View.GONE
            recentNumbersWidget.visibility = View.VISIBLE
            promoListWidget.visibility = View.VISIBLE
        }
    }

    fun onSuccessEnquiry(telcoEnquiryData: TelcoEnquiryData) {
        sharedModel.setEnquiryResult(telcoEnquiryData)
        postpaidClientNumberWidget.showEnquiryResultPostpaid(telcoEnquiryData)
        recentNumbersWidget.visibility = View.GONE
        promoListWidget.visibility = View.GONE

        buyWidget.setTotalPrice(telcoEnquiryData.enquiry.attributes.price)
        buyWidget.setVisibilityLayout(true)
    }

    fun onErrorEnquiry(throwable: Throwable) {
        view?.run {
            Toaster.showError(this, ErrorHandler.getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG)
        }
    }

    override fun clickCopyOnPromoCode(promoId: Int) {
        sharedModel.setPromoSelected(promoId)
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        inputNumberActionType = InputNumberActionType.CONTACT_HOMEPAGE
        postpaidClientNumberWidget.setInputNumber(contactNumber)
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TopupBillsFavNumberItem, inputNumberActionTypeIndex: Int) {
        inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]
        postpaidClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        postpaidClientNumberWidget.clearFocusAutoComplete()
    }

    override fun handleCallbackSearchNumberCancel() {
        postpaidClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation) {
        inputNumberActionType = InputNumberActionType.LATEST_TRANSACTION
        postpaidClientNumberWidget.setInputNumber(topupBillsRecommendation.clientNumber)

        if (operatorName.isNotEmpty()) {
            topupAnalytics.clickEnhanceCommerceRecentTransaction(topupBillsRecommendation,
                    operatorName, topupBillsRecommendation.position)
        }
    }

    override fun setFavNumbers(data: TopupBillsFavNumber) {
        performanceMonitoringStopTrace()
        favNumberList.addAll(data.favNumberList)
    }

    override fun errorSetFavNumbers() {
        performanceMonitoringStopTrace()
    }

    private fun performanceMonitoringStopTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace()
            traceStop = true
        }
    }

    override fun showErrorCartDigital(message: String) {
        view?.run {
            Toaster.showError(this, message, Snackbar.LENGTH_LONG)
        }
    }

    override fun onDestroy() {
        enquiryViewModel.flush()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        postpaidClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onBackPressed() {
        topupAnalytics.eventClickBackButton(categoryId)
    }

    companion object {

        private const val CACHE_CLIENT_NUMBER = "cache_client_number"
        private const val EXTRA_PARAM = "extra_param"
        private const val DG_TELCO_POSTPAID_TRACE = "dg_telco_postpaid_pdp"
        const val KEY_CLIENT_NUMBER = "clientNumber"
        const val KEY_PRODUCT_ID = "productId"


        fun newInstance(telcoExtraParam: TopupBillsExtraParam): Fragment {
            val fragment = DigitalTelcoPostpaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}
