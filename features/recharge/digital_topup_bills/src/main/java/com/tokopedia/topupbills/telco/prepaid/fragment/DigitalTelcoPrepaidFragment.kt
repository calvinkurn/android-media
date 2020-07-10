package com.tokopedia.topupbills.telco.prepaid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment.InputNumberActionType
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
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
import com.tokopedia.showcase.ShowCaseObject
import com.tokopedia.showcase.ShowCasePreference
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.searchnumber.view.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.common.adapter.TelcoTabAdapter
import com.tokopedia.topupbills.telco.common.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.common.generateRechargeCheckoutToken
import com.tokopedia.topupbills.telco.common.viewmodel.TelcoTabViewModel
import com.tokopedia.topupbills.telco.data.RechargePrefix
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.prepaid.viewmodel.SharedTelcoPrepaidViewModel
import com.tokopedia.topupbills.telco.prepaid.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.prepaid.widget.TelcoNestedCoordinatorLayout
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_digital_telco_prepaid.*
import java.util.*

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var telcoClientNumberWidget: DigitalClientNumberWidget
    private lateinit var mainContainer: TelcoNestedCoordinatorLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var buyWidget: TopupBillsCheckoutWidget
    private lateinit var sharedModelPrepaid: SharedTelcoPrepaidViewModel
    private lateinit var telcoTabViewModel: TelcoTabViewModel
    private lateinit var loadingShimmering: LinearLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var separator: View
    private lateinit var performanceMonitoring: PerformanceMonitoring

    override var menuId = TelcoComponentType.TELCO_PREPAID
    private var inputNumberActionType = InputNumberActionType.MANUAL

    private var clientNumber = ""
    private var operatorId = ""
    private var traceStop = false
    private var showProducts = false
    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_PREPAID_TRACE)

            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            sharedModelPrepaid = viewModelProvider.get(SharedTelcoPrepaidViewModel::class.java)
            telcoTabViewModel = viewModelProvider.get(TelcoTabViewModel::class.java)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    private fun isProductExist(telcoProduct: TelcoProduct): Boolean {
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
                    generateCheckoutPassData(telcoClientNumberWidget.getInputNumber(),
                            if (it.attributes.productPromo != null) "1" else "0",
                            it.attributes.categoryId.toString(),
                            it.attributes.operatorId.toString(),
                            it.id)
                }
            }
        })
        sharedModelPrepaid.showTotalPrice.observe(this, Observer {
            it?.run {
                buyWidget.setVisibilityLayout(it)
            }
        })

        sharedModelPrepaid.selectedFilter.observe(this, Observer {
            if (operatorId.isNotEmpty()) {
                sharedModelPrepaid.getCatalogProductList(GraphqlHelper.loadRawString(activity?.resources,
                        R.raw.query_catalog_product_telco), menuId, operatorId, it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        pageContainer = view.findViewById(R.id.page_container)
        mainContainer = view.findViewById(R.id.main_container)
        appBarLayout = view.findViewById(R.id.appbar_input_number_telco)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        buyWidget = view.findViewById(R.id.buy_widget)
        tickerView = view.findViewById(R.id.ticker_view)
        nestedScrollView = view.findViewById(R.id.nested_scroll_view)
        loadingShimmering = view.findViewById(R.id.loading_telco_shimmering)
        separator = view.findViewById(R.id.separator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        getPrefixOperatorData()
        renderInputNumber()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
    }

    private fun initViewPager() {
        val pagerAdapter = TelcoTabAdapter(this, object : TelcoTabAdapter.Listener {
            override fun getTabList(): List<TelcoTabItem> {
                return telcoTabViewModel.getAll()
            }
        })
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = false
        viewPager.registerOnPageChangeCallback(viewPagerCallback)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                //do nothing
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                //do nothing
            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                viewPager.setCurrentItem(p0.position, true)
            }
        })
    }

    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val tabs = telcoTabViewModel.getAll()
            if (showProducts) {
                nestedScrollView.fling(0)
                nestedScrollView.smoothScrollTo(0, 0)
                categoryId = getIdCategoryCurrentItem()
                topupAnalytics.eventClickTelcoPrepaidCategory(tabs[position].title)
                sharedModelPrepaid.setShowTotalPrice(false)
                sharedModelPrepaid.setProductCatalogSelected(getEmptyProduct())
            } else {
                setTrackingOnTabMenu(tabs[position].title)
            }
        }
    }

    override fun onCollapseAppBar() {
        telcoClientNumberWidget.setVisibleResultNumber(true)
    }

    override fun onExpandAppBar() {
        telcoClientNumberWidget.setVisibleResultNumber(false)
    }

    /**
     * handle reset selected item every move to other tab product
     */
    private fun getEmptyProduct(): TelcoProduct {
        return TelcoProduct(id = ID_PRODUCT_EMPTY)
    }

    override fun getTelcoMenuId(): Int {
        return menuId
    }

    override fun getTelcoCategoryId(): Int {
        return categoryId
    }

    private fun getCatalogMenuDetail() {
        onLoadingMenuDetail(true)
        getMenuDetail(TelcoComponentType.TELCO_PREPAID)
        getFavoriteNumbers(TelcoComponentType.FAV_NUMBER_PREPAID)
    }

    //region Promo and Recommendation
    override fun renderPromoAndRecommendation() {
        tabLayout.removeAllTabs()
        for (i in 0 until listMenu.size) {
            tabLayout.addTab(tabLayout.newTab().setText(listMenu[i].title))
        }
        changeDataSet { telcoTabViewModel.addAll(listMenu) }
        if (!showProducts) {
            if (listMenu.size > 1) {
                tabLayout.show()
                separator.show()
                (viewPager.getChildAt(0) as? TopupBillsWidgetInterface)?.toggleTitle(false)
                (viewPager.getChildAt(1) as? TopupBillsWidgetInterface)?.toggleTitle(false)

            } else {
                separator.hide()
                tabLayout.hide()
            }
        }
    }
    // endregion Promo and Recommendation

    private fun changeDataSet(performChange: () -> Unit) {
        val oldItems = telcoTabViewModel.createIdSnapshot()
        performChange()
        val newItems = telcoTabViewModel.createIdSnapshot()
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldItems[oldItemPosition] == newItems[newItemPosition]

            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    areItemsTheSame(oldItemPosition, newItemPosition)
        }, true).dispatchUpdatesTo(viewPager.adapter!!)
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
                if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                    menuId = digitalTelcoExtraParam.menuId.toInt()
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

    override fun renderProductFromCustomData() {
        try {
            if (telcoClientNumberWidget.getInputNumber().isNotEmpty()) {
                showProducts = true
                val selectedOperator = this.operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                    telcoClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                operatorId = selectedOperator.operator.id
                telcoClientNumberWidget.setIconOperator(selectedOperator.operator.attributes.imageUrl)

                validatePhoneNumber(this.operatorData, telcoClientNumberWidget)
                hitTrackingForInputNumber(selectedOperator)
                renderProductViewPager()
                getProductListData()
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
                operatorId = ""
            }

            override fun onClientNumberHasFocus(clientNumber: String) {
                operatorId = ""
                productId = 0
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

    private fun getProductListData() {
        if (operatorId.isNotEmpty()) {
            sharedModelPrepaid.getCatalogProductList(GraphqlHelper.loadRawString(activity?.resources,
                    R.raw.query_catalog_product_telco), menuId, operatorId, null)
        }
    }

    private fun renderProductViewPager() {
        var idProductTab = 6L
        val listProductTab = mutableListOf<TelcoTabItem>()
        tabLayout.removeAllTabs()
        listProductTab.add(
                TelcoTabItem(generateBundleProduct(TelcoComponentName.PRODUCT_PULSA,
                        operatorName, TelcoProductType.PRODUCT_GRID, productId),
                        TelcoComponentName.PRODUCT_PULSA,
                        idProductTab++))
        listProductTab.add(
                TelcoTabItem(generateBundleProduct(TelcoComponentName.PRODUCT_PAKET_DATA,
                        operatorName, TelcoProductType.PRODUCT_LIST, productId),
                        TelcoComponentName.PRODUCT_PAKET_DATA,
                        idProductTab++))
        listProductTab.add(
                TelcoTabItem(generateBundleProduct(TelcoComponentName.PRODUCT_ROAMING,
                        operatorName, TelcoProductType.PRODUCT_LIST, productId),
                        TelcoComponentName.PRODUCT_ROAMING,
                        idProductTab++))

        for (i in 0 until listProductTab.size) {
            tabLayout.addTab(tabLayout.newTab().setText(listProductTab[i].title))
        }

        changeDataSet { telcoTabViewModel.addAll(listProductTab) }

        tabLayout.show()
        separator.show()
        setTabFromProductSelected()
    }

    private fun generateBundleProduct(titlePage: String, operatorName: String, productType: Int,
                                      selectedProduct: Int): Bundle {
        val bundle = Bundle()
        bundle.putString(DigitalTelcoProductFragment.TITLE_PAGE, titlePage)
        bundle.putInt(DigitalTelcoProductFragment.PRODUCT_TYPE, productType)
        bundle.putInt(DigitalTelcoProductFragment.SELECTED_PRODUCT, selectedProduct)
        bundle.putString(DigitalTelcoProductFragment.OPERATOR_NAME, operatorName)
        return bundle
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

    private fun getLabelActiveCategory(): String {
        return when (getIdCategoryCurrentItem()) {
            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA
            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA
            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING
            else -> ""
        }
    }

    private fun getIdCategoryCurrentItem(): Int {
        return when (viewPager.currentItem) {
            0 -> TelcoCategoryType.CATEGORY_PULSA
            1 -> TelcoCategoryType.CATEGORY_PAKET_DATA
            2 -> TelcoCategoryType.CATEGORY_ROAMING
            else -> TelcoCategoryType.CATEGORY_PULSA
        }
    }

    override fun showErrorCartDigital(message: String) {
        view?.run {
            Toaster.showError(this, message, Snackbar.LENGTH_LONG)
        }
    }

    // region Favorite Numbers
    override fun setFavNumbers(data: TopupBillsFavNumber) {
        performanceMonitoringStopTrace()
        val favNumbers = data.favNumberList
        favNumberList.addAll(favNumbers)
        if (clientNumber.isEmpty() && favNumbers.isNotEmpty() && ::viewPager.isInitialized) {
            telcoClientNumberWidget.setInputNumber(favNumbers[0].clientNumber)
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

    override fun handleCallbackSearchNumber(orderClientNumber: TopupBillsFavNumberItem, inputNumberActionTypeIndex: Int) {
        inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]
        productId = 0
        if (orderClientNumber.productId.isNotEmpty() &&
                orderClientNumber.categoryId.toIntOrNull() ?: 0 == getIdCategoryCurrentItem()) {
            productId = orderClientNumber.productId.toIntOrNull() ?: 0
        }

        telcoClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        telcoClientNumberWidget.clearFocusAutoComplete()
        sharedModelPrepaid.setSelectedCategoryViewPager(getLabelActiveCategory())
    }

    override fun handleCallbackSearchNumberCancel() {
        telcoClientNumberWidget.clearFocusAutoComplete()
    }
    // endregion Favorite Numbers

    //region Recent Numbers
    override fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation) {
        inputNumberActionType = InputNumberActionType.LATEST_TRANSACTION
        if (operatorName.isNotEmpty()) {
            topupAnalytics.clickEnhanceCommerceRecentTransaction(topupBillsRecommendation, operatorName,
                    topupBillsRecommendation.position)
        }

        generateCheckoutPassData(
                topupBillsRecommendation.clientNumber,
                "0",
                topupBillsRecommendation.categoryId.toString(),
                topupBillsRecommendation.operatorId.toString(),
                topupBillsRecommendation.productId.toString())

        if (userSession.isLoggedIn) {
            navigateToCart()
        } else {
            navigateToLoginPage()
        }
    }
    //endregion Recent Numbers

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

            val showCaseDialog = ShowCaseBuilder()
                    .backgroundContentColorRes(com.tokopedia.design.R.color.black)
                    .shadowColorRes(com.tokopedia.showcase.R.color.shadow)
                    .textColorRes(com.tokopedia.design.R.color.grey_400)
                    .textSizeRes(com.tokopedia.design.R.dimen.sp_12)
                    .titleTextSizeRes(com.tokopedia.design.R.dimen.sp_16)
                    .finishStringRes(R.string.telco_showcase_finish)
                    .clickable(true)
                    .useArrow(true)
                    .build()
            val showCaseList = ArrayList<ShowCaseObject>()
            showCaseList.add(ShowCaseObject(telcoClientNumberWidget, getString(R.string.Telco_title_showcase_client_number),
                    getString(R.string.telco_label_showcase_client_number)))
            showCaseList.add(ShowCaseObject(viewPager, getString(R.string.telco_title_showcase_promo),
                    getString(R.string.telco_label_showcase_promo)))
            showCaseDialog.show(activity, showcaseTag, showCaseList)
        }
    }

    private fun generateCheckoutPassData(inputNumber: String, promoStatus: String,
                                         categoryId: String, operatorId: String, productId: String) {
        checkoutPassData = DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                .categoryId(categoryId)
                .clientNumber(inputNumber)
                .instantCheckout("0")
                .isPromo(promoStatus)
                .operatorId(operatorId)
                .productId(productId)
                .utmCampaign(categoryId)
                .utmContent(GlobalConfig.VERSION_NAME)
                .idemPotencyKey(userSession.userId.generateRechargeCheckoutToken())
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied("")
                .build()
    }

    override fun onDestroy() {
        viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
        viewPager.adapter = null
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
