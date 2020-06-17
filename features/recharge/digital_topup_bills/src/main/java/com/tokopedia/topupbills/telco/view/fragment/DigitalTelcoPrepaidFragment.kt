package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.tokopedia.common.topupbills.widget.TopupBillsWidgetInterface
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.showcase.ShowCaseBuilder
import com.tokopedia.showcase.ShowCaseDialog
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.showcase.ShowCasePreference
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.data.RechargePrefix
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.activity.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.view.viewmodel.SharedTelcoPrepaidViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_digital_telco_prepaid.*
import java.util.*

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var telcoClientNumberWidget: DigitalClientNumberWidget
    private lateinit var buyWidget: TopupBillsCheckoutWidget
    private lateinit var sharedModelPrepaid: SharedTelcoPrepaidViewModel
    private lateinit var loadingShimmering: LinearLayout
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var separator: View

    override var menuId = TelcoComponentType.TELCO_PREPAID
    private var inputNumberActionType = InputNumberActionType.MANUAL
    private val listProductTab = mutableListOf<TopupBillsTabItem>()

    private var clientNumber = ""
    private var traceStop = false
    private var showProducts = false
    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_PREPAID_TRACE)

            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            sharedModelPrepaid = viewModelProvider.get(SharedTelcoPrepaidViewModel::class.java)
            sharedModelPrepaid.setShowTotalPrice(false)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    private fun isProductExist(telcoProduct: TelcoProduct): Boolean{
        return telcoProduct.id != ID_PRODUCT_EMPTY
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModelPrepaid.productCatalogItem.observe(this, Observer {
            if (isProductExist(it)) {
                it?.run {
                    buyWidget.setTotalPrice(it.attributes.price)
                    it.attributes.productPromo?.run {
                        if (this.newPrice.isNotEmpty()) {
                            buyWidget.setTotalPrice(this.newPrice)
                        }
                    }

                    productId = it.id.toIntOrZero()
                    price = it.attributes.pricePlain
                    checkVoucherWithDelay()

                    checkoutPassData = DigitalCheckoutPassData.Builder()
                            .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                            .categoryId(it.attributes.categoryId.toString())
                            .clientNumber(telcoClientNumberWidget.getInputNumber())
                            .instantCheckout("0")
                            .isPromo(if (it.attributes.productPromo != null) "1" else "0")
                            .operatorId(it.attributes.operatorId.toString())
                            .productId(it.id)
                            .utmCampaign(it.attributes.categoryId.toString())
                            .utmContent(GlobalConfig.VERSION_NAME)
                            .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                            .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                            .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                            .voucherCodeCopied("")
                            .build()
                }
            }
        })
        sharedModelPrepaid.showTotalPrice.observe(this, Observer {
            it?.run {
                buyWidget.setVisibilityLayout(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        pageContainer = view.findViewById(R.id.page_container)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        loadingShimmering = view.findViewById(R.id.loading_telco_shimmering)
        separator = view.findViewById(R.id.separator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPrefixOperatorData()
        renderInputNumber()
        handleFocusClientNumber()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
        sendOpenScreenTracking()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(pos: Int) {
                if (showProducts) {
                    topupAnalytics.eventClickTelcoPrepaidCategory(listProductTab[pos].title)
                    sharedModelPrepaid.setShowTotalPrice(false)
                    sharedModelPrepaid.setProductCatalogSelected(getEmptyProduct())
                } else {
                    setTrackingOnTabMenu(listMenu[pos].title)
                }
            }
        })
    }

    /**
     * handle reset selected item every move to other tab product
     */
    private fun getEmptyProduct(): TelcoProduct {
        return TelcoProduct(id= ID_PRODUCT_EMPTY)
    }

    override fun getTelcoMenuId(): Int {
        return menuId
    }

    override fun getTelcoCategoryId(): Int {
        return categoryId
    }

    private fun getCatalogMenuDetail() {
        getMenuDetail(TelcoComponentType.TELCO_PREPAID)
        getFavoriteNumbers(TelcoComponentType.FAV_NUMBER_PREPAID)
    }

    override fun renderPromoAndRecommendation() {
        if (listMenu.size > 0 && !showProducts) {
            viewPager.adapter = null
            val pagerAdapter = TopupBillsProductTabAdapter(listMenu, childFragmentManager)
            viewPager.adapter = pagerAdapter
            viewPager.offscreenPageLimit = listMenu.size

            if (listMenu.size > 1) {
                tabLayout.show()
                separator.show()
                tabLayout.setupWithViewPager(viewPager)
                if (viewPager.getChildAt(0) != null) {
                    (viewPager.getChildAt(0) as TopupBillsWidgetInterface).toggleTitle(false)
                }
                if (viewPager.getChildAt(1) != null) {
                    (viewPager.getChildAt(1) as TopupBillsWidgetInterface).toggleTitle(false)
                }
            } else {
                tabLayout.hide()
                separator.hide()
            }
        }
    }

    private fun getDataFromBundle(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM) as TopupBillsExtraParam
                clientNumber = digitalTelcoExtraParam.clientNumber
                productId = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
                if (digitalTelcoExtraParam.categoryId.isNotEmpty()) { categoryId = digitalTelcoExtraParam.categoryId.toInt() }
                if (digitalTelcoExtraParam.menuId.isNotEmpty()) { menuId = digitalTelcoExtraParam.menuId.toInt() }
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

    override fun renderProductFromCustomData() {
        try {
            if (telcoClientNumberWidget.getInputNumber().isNotEmpty()) {
                showProducts = true
                val selectedOperator = this.operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                    telcoClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                telcoClientNumberWidget.setIconOperator(selectedOperator.operator.attributes.imageUrl)

                validatePhoneNumber(this.operatorData, telcoClientNumberWidget)
                hitTrackingForInputNumber(selectedOperator)
                renderProductViewPager()
                getProductListData(selectedOperator.operator.id)
            }
        } catch (exception: Exception) {
            telcoClientNumberWidget.setErrorInputNumber(
                    getString(R.string.telco_number_error_not_found))
        }
    }

    private fun hitTrackingForInputNumber(selectedOperator: RechargePrefix) {
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
    }

    override fun onLoadingMenuDetail(showLoading: Boolean) {
        if (showLoading) {
            loadingShimmering.show()
            mainContainer.hide()
        } else {
            loadingShimmering.hide()
            mainContainer.show()
        }
    }

    private fun renderInputNumber() {
        telcoClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
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
                topupAnalytics.eventClearInputNumber()
                showProducts = false
                renderPromoAndRecommendation()
                sharedModelPrepaid.setShowTotalPrice(false)
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

    private fun getProductListData(operatorId: String) {
        sharedModelPrepaid.getCatalogProductList(GraphqlHelper.loadRawString(activity?.resources,
                R.raw.query_catalog_product_telco), menuId, operatorId)
    }

    private fun renderProductViewPager() {
        viewPager.adapter = null
        listProductTab.clear()

        listProductTab.add(
                TopupBillsTabItem(DigitalTelcoProductFragment.newInstance(TelcoComponentName.PRODUCT_PULSA,
                        operatorName, TelcoProductType.PRODUCT_GRID, productId), TelcoComponentName.PRODUCT_PULSA))
        listProductTab.add(
                TopupBillsTabItem(DigitalTelcoProductFragment.newInstance(TelcoComponentName.PRODUCT_PAKET_DATA,
                        operatorName, TelcoProductType.PRODUCT_LIST, productId), TelcoComponentName.PRODUCT_PAKET_DATA))
        listProductTab.add(
                TopupBillsTabItem(DigitalTelcoProductFragment.newInstance(TelcoComponentName.PRODUCT_ROAMING,
                        operatorName, TelcoProductType.PRODUCT_LIST, productId), TelcoComponentName.PRODUCT_ROAMING))

        val pagerAdapter = TopupBillsProductTabAdapter(listProductTab, childFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 3

        tabLayout.show()
        separator.show()
        tabLayout.setupWithViewPager(viewPager)
        setTabFromProductSelected()
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

        setTabFromProductSelected()
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
            showCaseList.add(ShowCaseObject(viewPager, getString(R.string.telco_title_showcase_promo),
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
                .finishStringRes(R.string.telco_showcase_finish)
                .clickable(true)
                .useArrow(true)
                .build()
    }

    override fun onDestroy() {
        sharedModelPrepaid.flush()
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
        private const val DG_TELCO_PREPAID_TRACE = "dg_telco_prepaid_pdp"
        private const val ID_PRODUCT_EMPTY = "-1"

        fun newInstance(telcoExtraParam: TopupBillsExtraParam): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}
