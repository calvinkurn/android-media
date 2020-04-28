package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment.InputNumberActionType
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_OPERATOR_ID
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler.getErrorMessage
import com.tokopedia.showcase.ShowCaseBuilder
import com.tokopedia.showcase.ShowCaseDialog
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.showcase.ShowCasePreference
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.data.TelcoCustomComponentData
import com.tokopedia.topupbills.telco.data.TelcoCustomData
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_digital_telco_prepaid.*
import java.util.*

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var telcoClientNumberWidget: DigitalClientNumberWidget
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var buyWidget: TopupBillsCheckoutWidget
    private lateinit var sharedModel: SharedProductTelcoViewModel
    private lateinit var layoutProgressBar: ProgressBar
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private var inputNumberActionType = InputNumberActionType.MANUAL
    private var clientNumber = ""
    private var traceStop = false

    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()
    private var operatorData: TelcoCustomComponentData =
            TelcoCustomComponentData(TelcoCustomData(mutableListOf()))
    override var menuId = TelcoComponentType.TELCO_PREPAID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_PREPAID_TRACE)

            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
            sharedModel.setShowTotalPrice(false)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.productItem.observe(this, Observer {
            it?.run {
                buyWidget.setTotalPrice(it.product.attributes.price)
                it.product.attributes.productPromo?.run {
                    if (this.newPrice.isNotEmpty()) {
                        buyWidget.setTotalPrice(this.newPrice)
                    }
                }

                productId = it.product.id.toIntOrZero()
                price = it.product.attributes.pricePlain
                checkVoucherWithDelay()

                checkoutPassData = DigitalCheckoutPassData.Builder()
                        .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                        .categoryId(it.product.attributes.categoryId.toString())
                        .clientNumber(telcoClientNumberWidget.getInputNumber())
                        .instantCheckout("0")
                        .isPromo(if (it.product.attributes.productPromo != null) "1" else "0")
                        .operatorId(it.product.attributes.operatorId.toString())
                        .productId(it.product.id)
                        .utmCampaign(it.product.attributes.categoryId.toString())
                        .utmContent(GlobalConfig.VERSION_NAME)
                        .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                        .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                        .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                        .voucherCodeCopied("")
                        .build()
            }
        })
        sharedModel.showTotalPrice.observe(this, Observer {
            it?.run {
                buyWidget.setVisibilityLayout(it)
            }
        })
        sharedModel.promoItem.observe(this, Observer {
            it?.run {
                promoListWidget.notifyPromoItemChanges(this)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        recentNumbersWidget = view.findViewById(R.id.recent_numbers)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        promoListWidget = view.findViewById(R.id.promo_widget)
        viewPager = view.findViewById(R.id.product_view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        layoutProgressBar = view.findViewById(R.id.layout_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInputFilterDataCollections()
        renderInputNumber()
        handleFocusClientNumber()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
    }

    fun getInputFilterDataCollections() {
        customViewModel.getCustomDataPrepaid(GraphqlHelper.loadRawString(resources,
                R.raw.query_custom_digital_telco),
                this::onSuccessCustomData, this::onErrorCustomData)
    }

    private fun getCatalogMenuDetail() {
        getMenuDetail(TelcoComponentType.TELCO_PREPAID)
        getFavoriteNumbers(TelcoComponentType.FAV_NUMBER_PREPAID)
    }

    private fun getDataFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM) as TopupBillsExtraParam
                clientNumber = digitalTelcoExtraParam.clientNumber
                productId = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
                if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                    categoryId = digitalTelcoExtraParam.categoryId.toInt()
                }
            }
        } else {
            clientNumber = savedInstanceState.getString(CACHE_CLIENT_NUMBER) ?: ""
        }
        telcoClientNumberWidget.setInputNumber(clientNumber)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CACHE_CLIENT_NUMBER, telcoClientNumberWidget.getInputNumber())
    }

    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return buy_widget
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        super.processMenuDetail(data)
        showOnBoarding()
    }

    override fun onSuccessCustomData(telcoData: TelcoCustomComponentData) {
        this.operatorData = telcoData
        renderProductFromCustomData()
    }

    fun renderProductFromCustomData() {
        try {
            if (telcoClientNumberWidget.getInputNumber().isNotEmpty()) {
                val selectedOperator = this.operatorData.rechargeCustomData.customDataCollections.single {
                    telcoClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                operatorName = selectedOperator.operator.attributes.name
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
                }

                renderViewPager(selectedOperator.operator.id)
                telcoClientNumberWidget.setIconOperator(selectedOperator.operator.attributes.imageUrl)

                recentNumbersWidget.visibility = View.GONE
                promoListWidget.visibility = View.GONE
                tabLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                separator.visibility = View.VISIBLE
            }
        } catch (exception: Exception) {
            view?.run {
                telcoClientNumberWidget.setErrorInputNumber(
                        getString(R.string.telco_number_error_not_found))
            }
        }
    }

    override fun onErrorCustomData(throwable: Throwable) {
        view?.run {
            Toaster.showError(this, getErrorMessage(activity, throwable), Snackbar.LENGTH_LONG)
        }
    }

    fun onLoadingMenuDetail(showLoading: Boolean) {
        if (showLoading) {
            layoutProgressBar.visibility = View.VISIBLE
            recentNumbersWidget.visibility = View.GONE
            promoListWidget.visibility = View.GONE
        } else {
            layoutProgressBar.visibility = View.GONE
            // If client number auto fill is done (product tabs are visible), do not show recent numbers & promo list
            if (tabLayout.visibility == View.GONE && viewPager.visibility == View.GONE) {
                recentNumbersWidget.visibility = View.VISIBLE
                promoListWidget.visibility = View.VISIBLE
            }
        }
    }

    fun renderInputNumber() {
        telcoClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
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
            }

            override fun onClearAutoComplete() {
                topupAnalytics.eventClearInputNumber()

                separator.visibility = View.GONE
                recentNumbersWidget.visibility = View.VISIBLE
                promoListWidget.visibility = View.VISIBLE
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
                sharedModel.setShowTotalPrice(false)
                productId = 0
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                telcoClientNumberWidget.clearFocusAutoComplete()
                startActivityForResult(activity?.let {
                    DigitalSearchNumberActivity.newInstance(it,
                            ClientNumberType.TYPE_INPUT_TEL, clientNumber, favNumberList)
                },
                        REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
            }
        })
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        inputNumberActionType = InputNumberActionType.CONTACT_HOMEPAGE
        telcoClientNumberWidget.setInputNumber(contactNumber)
    }

    override fun clickCopyOnPromoCode(promoId: Int) {
        sharedModel.setPromoSelected(promoId)
    }

    private fun renderViewPager(operatorId: String) {
        val listProductTab = mutableListOf<TopupBillsTabItem>()
        listProductTab.add(TopupBillsTabItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PULSA, TelcoComponentName.PRODUCT_PULSA, operatorId, operatorName,
                TelcoProductType.PRODUCT_GRID, productId), TelcoComponentName.PRODUCT_PULSA))
        listProductTab.add(TopupBillsTabItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_PAKET_DATA, TelcoComponentName.PRODUCT_PAKET_DATA, operatorId, operatorName,
                TelcoProductType.PRODUCT_LIST, productId), TelcoComponentName.PRODUCT_PAKET_DATA))
        listProductTab.add(TopupBillsTabItem(DigitalTelcoProductFragment.newInstance(
                TelcoComponentType.PRODUCT_ROAMING, TelcoComponentName.PRODUCT_ROAMING, operatorId, operatorName,
                TelcoProductType.PRODUCT_LIST, productId), TelcoComponentName.PRODUCT_ROAMING))
        val pagerAdapter = TopupBillsProductTabAdapter(listProductTab, childFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)
        setTabFromProductSelected()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(pos: Int) {
                topupAnalytics.eventClickTelcoPrepaidCategory(listProductTab[pos].title)
            }
        })
    }

    private fun setTabFromProductSelected() {
        var itemId = 0
        when (categoryId) {
            TelcoCategoryType.CATEGORY_PULSA -> itemId = 0
            TelcoCategoryType.CATEGORY_PAKET_DATA -> itemId = 1
            TelcoCategoryType.CATEGORY_ROAMING -> itemId = 2
        }
        viewPager.setCurrentItem(itemId, true)
    }

    override fun showErrorCartDigital(message: String) {
        view?.run {
            Toaster.showError(this, message, Snackbar.LENGTH_LONG)
        }
    }

    override fun handleCallbackSearchNumber(orderClientNumber: TopupBillsFavNumberItem, inputNumberActionTypeIndex: Int) {
        inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]

        if (orderClientNumber.productId.isNotEmpty() && orderClientNumber.categoryId.isNotEmpty()) {
            productId = orderClientNumber.productId.toIntOrNull() ?: 0
        }
        telcoClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        telcoClientNumberWidget.clearFocusAutoComplete()

        if (::viewPager.isInitialized) {
            setTabFromProductSelected()
        }
    }

    override fun handleCallbackSearchNumberCancel() {
        telcoClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation) {
        inputNumberActionType = InputNumberActionType.LATEST_TRANSACTION
        productId = topupBillsRecommendation.productId
        categoryId = topupBillsRecommendation.categoryId
        telcoClientNumberWidget.setInputNumber(topupBillsRecommendation.clientNumber)

        if (operatorName.isNotEmpty()) {
            topupAnalytics.clickEnhanceCommerceRecentTransaction(topupBillsRecommendation, operatorName,
                    topupBillsRecommendation.position)
        }
    }

    override fun setFavNumbers(data: TopupBillsFavNumber) {
        performanceMonitoringStopTrace()
        val favNumbers = data.favNumberList
        favNumberList.addAll(favNumbers)
        if (clientNumber.isEmpty() && favNumbers.isNotEmpty() && ::viewPager.isInitialized) {
            telcoClientNumberWidget.setInputNumber(favNumbers[0].clientNumber)
            setTabFromProductSelected()
        }
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

    override fun setupCheckoutData() {
        val inputs = mutableMapOf<String, String>()
        inputs[EXPRESS_PARAM_CLIENT_NUMBER] = telcoClientNumberWidget.getInputNumber()
        val operatorId = checkoutPassData.operatorId ?: ""
        if (operatorId.isNotEmpty()) inputs[EXPRESS_PARAM_OPERATOR_ID] = operatorId
        inputFields = inputs
    }

    private fun showOnBoarding() {
        activity?.run {
            val showcaseTag = javaClass.name + ".BroadcastMessage"
            if (ShowCasePreference.hasShown(this, showcaseTag)) {
                return
            }

            val showCaseDialog = generateShowcaseDialog()
            val showCaseList = ArrayList<ShowCaseObject>()
            showCaseList.add(ShowCaseObject(telcoClientNumberWidget, getString(R.string.Telco_title_showcase_client_number),
                    getString(R.string.telco_label_showcase_client_number)))
            showCaseList.add(ShowCaseObject(promoListWidget, getString(R.string.telco_title_showcase_promo),
                    getString(R.string.telco_label_showcase_promo)))
            showCaseDialog.show(activity, showcaseTag, showCaseList)
        }
    }

    private fun generateShowcaseDialog(): ShowCaseDialog {
        return ShowCaseBuilder()
                .backgroundContentColorRes(com.tokopedia.design.R.color.black)
                .shadowColorRes(com.tokopedia.showcase.R.color.shadow)
                .textColorRes(com.tokopedia.design.R.color.grey_400)
                .textSizeRes(com.tokopedia.design.R.dimen.sp_12)
                .titleTextSizeRes(com.tokopedia.design.R.dimen.sp_16)
                .finishStringRes(com.tokopedia.showcase.R.string.finish)
                .clickable(true)
                .useArrow(true)
                .build()
    }

    override fun onDestroy() {
        sharedModel.flush()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        telcoClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onBackPressed() {
        topupAnalytics.eventClickBackButton(categoryId)
    }

    companion object {
        private const val CACHE_CLIENT_NUMBER = "cache_client_number"

        private const val EXTRA_PARAM = "extra_param"

        const val DG_TELCO_PREPAID_TRACE = "dg_telco_prepaid_pdp"

        fun newInstance(telcoExtraParam: TopupBillsExtraParam): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}