package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment.InputNumberActionType
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common.topupbills.widget.TopupBillsWidgetInterface
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.data.RechargePrefix
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.view.widget.DigitalPostpaidClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_telco_postpaid.*

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoPostpaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var postpaidClientNumberWidget: DigitalPostpaidClientNumberWidget
    private lateinit var buyWidget: TopupBillsCheckoutWidget
    private lateinit var mainContainer: NestedScrollView
    private lateinit var enquiryViewModel: DigitalTelcoEnquiryViewModel
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var loadingShimmering: LinearLayout
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var separator: View

    private var traceStop = false
    private var operatorSelected: RechargePrefix? = null
        set(value) {
            field = value
            value?.run {
                productId = operator.attributes.defaultProductId
            }
        }

    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()

    override var menuId = TelcoComponentType.TELCO_POSTPAID
    override var categoryId = TelcoCategoryType.CATEGORY_PASCABAYAR
    private var inputNumberActionType = InputNumberActionType.MANUAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_POSTPAID_TRACE)

            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            enquiryViewModel = viewModelProvider.get(DigitalTelcoEnquiryViewModel::class.java)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_postpaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        pageContainer = view.findViewById(R.id.page_container)
        postpaidClientNumberWidget = view.findViewById(R.id.telco_input_number)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        loadingShimmering = view.findViewById(R.id.loading_telco_shimmering)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        separator = view.findViewById(R.id.separator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPrefixOperatorData()
        renderClientNumber()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
    }

    override fun getTelcoMenuId(): Int {
        return menuId
    }

    override fun getTelcoCategoryId(): Int {
        return categoryId
    }

    override fun renderPromoAndRecommendation() {
        if (listMenu.size > 0) {
            val pagerAdapter = TopupBillsProductTabAdapter(listMenu, childFragmentManager)
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = listMenu.size
            viewPager.show()

            if (listMenu.size > 1) {
                tabLayout.show()
                separator.show()
                tabLayout.setupWithViewPager(viewPager)
                (viewPager.getChildAt(0) as? TopupBillsWidgetInterface)?.toggleTitle(false)
                (viewPager.getChildAt(1) as? TopupBillsWidgetInterface)?.toggleTitle(false)
            } else {
                tabLayout.hide()
                separator.hide()
            }

            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(pos: Int) {
                    setTrackingOnTabMenu(listMenu[pos].title)
                }
            })
        }
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

    private fun getCatalogMenuDetail() {
        getMenuDetail(TelcoComponentType.TELCO_POSTPAID)
        getFavoriteNumbers(TelcoComponentType.FAV_NUMBER_POSTPAID)
    }

    private fun getDataFromBundle(savedInstanceState: Bundle?) {
        var clientNumber = ""
        if (savedInstanceState == null) {
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM) as TopupBillsExtraParam
                clientNumber = digitalTelcoExtraParam.clientNumber
                if (digitalTelcoExtraParam.menuId.isNotEmpty()) { menuId = digitalTelcoExtraParam.menuId.toInt() }
                if (digitalTelcoExtraParam.categoryId.isNotEmpty()) { categoryId = digitalTelcoExtraParam.categoryId.toInt() }
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
                operatorData.rechargeCatalogPrefixSelect.prefixes.isEmpty()?.let {
                    if (it) {
                        getPrefixOperatorData()
                    } else {
                        renderProductFromCustomData()
                    }
                }
            }

            override fun onClearAutoComplete() {
                renderPromoAndRecommendation()
                topupAnalytics.eventClearInputNumber()

                postpaidClientNumberWidget.resetClientNumberPostpaid()
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
            topupAnalytics.eventClickCheckEnquiry(categoryId, operatorName, userSession.userId)
            var mapParam = HashMap<String, Any>()
            mapParam.put(KEY_CLIENT_NUMBER, postpaidClientNumberWidget.getInputNumber())
            mapParam.put(KEY_PRODUCT_ID, selectedOperator.operator.attributes.defaultProductId.toString())

            postpaidClientNumberWidget.setLoadingButtonEnquiry(true)
            enquiryViewModel.getEnquiry(GraphqlHelper.loadRawString(resources,
                    com.tokopedia.common.topupbills.R.raw.query_enquiry_digital), mapParam)

            enquiryViewModel.enquiryResult.observe(this, Observer {
                when (it) {
                    is Success -> enquirySuccess()
                    is Fail -> enquiryFailed()
                }
            })
        }
    }

    private fun enquirySuccess() {
        postpaidClientNumberWidget.setLoadingButtonEnquiry(false)
        tabLayout.hide()
        separator.hide()
        viewPager.hide()
        val enquiryData = (enquiryViewModel.enquiryResult.value as Success).data
        setCheckoutPassData(enquiryData)
        postpaidClientNumberWidget.showEnquiryResultPostpaid(enquiryData)

        buyWidget.setTotalPrice(enquiryData.enquiry.attributes.price)
        buyWidget.setVisibilityLayout(true)
    }

    private fun enquiryFailed() {
        postpaidClientNumberWidget.setLoadingButtonEnquiry(false)
        val errorEnquiry = enquiryViewModel.enquiryResult.value as Fail
        view?.run {
            errorEnquiry.throwable?.let {
                Toaster.make(this, ErrorHandler.getErrorMessage(context, it), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun setCheckoutPassData(telcoEnquiryData: TelcoEnquiryData) {
        telcoEnquiryData?.run {
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
    }

    override fun renderProductFromCustomData() {
        try {
            if (postpaidClientNumberWidget.getInputNumber().isNotEmpty()) {
                operatorSelected = operatorData.rechargeCatalogPrefixSelect.prefixes.single {
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
                    if (postpaidClientNumberWidget.getInputNumber().length in 10..14) {
                        postpaidClientNumberWidget.setButtonEnquiry(true)
                    } else {
                        postpaidClientNumberWidget.setButtonEnquiry(false)
                    }
                    validatePhoneNumber(operatorData, postpaidClientNumberWidget)
                }
            }
        } catch (exception: Exception) {
            postpaidClientNumberWidget.setErrorInputNumber(
                    getString(R.string.telco_number_error_not_found))
        }
    }

    override fun onLoadingMenuDetail(showLoading: Boolean) {
        if (showLoading) {
            loadingShimmering.visibility = View.VISIBLE
            mainContainer.visibility = View.GONE
        } else {
            loadingShimmering.visibility = View.GONE
            mainContainer.visibility = View.VISIBLE
        }
    }

    override fun onCollapseAppBar() {
        //do nothing
    }

    override fun onExpandAppBar() {
        //do nothing
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
            Toaster.make(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
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
