package com.tokopedia.topupbills.telco.prepaid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment.InputNumberActionType
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel.Companion.EXPRESS_PARAM_OPERATOR_ID
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.util.DigitalTopupBillsGqlQuery
import com.tokopedia.topupbills.searchnumber.view.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity.Companion.RECHARGE_PRODUCT_EXTRA
import com.tokopedia.topupbills.telco.common.adapter.TelcoTabAdapter
import com.tokopedia.topupbills.telco.common.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.common.viewmodel.TelcoTabViewModel
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.prepaid.viewmodel.SharedTelcoPrepaidViewModel
import com.tokopedia.topupbills.telco.prepaid.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_digital_telco_prepaid.*

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class DigitalTelcoPrepaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var dynamicSpacer: View
    private lateinit var telcoClientNumberWidget: DigitalClientNumberWidget
    private lateinit var mainContainer: CoordinatorLayout
    private lateinit var buyWidget: TopupBillsCheckoutWidget
    private lateinit var sharedModelPrepaid: SharedTelcoPrepaidViewModel
    private lateinit var telcoTabViewModel: TelcoTabViewModel
    private lateinit var loadingShimmering: LinearLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabsUnify
    private lateinit var separator: View
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var localCacheHandler: LocalCacheHandler

    override var menuId = TelcoComponentType.TELCO_PREPAID
    private var inputNumberActionType = InputNumberActionType.MANUAL

    private var rechargeProductFromSlice: String = ""
    private var clientNumber = ""
    private var operatorId = ""
    private var autoSelectTabProduct = false
    private var traceStop = false
    private var showProducts = false
    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()

    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_PREPAID_TRACE)
            localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)

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

    private fun subscribeUi() {
        sharedModelPrepaid.productCatalogItem.observe(viewLifecycleOwner, Observer {
            if (isProductExist(it)) {
                sharedModelPrepaid.setVisibilityTotalPrice(true)
                telco_buy_widget.setTotalPrice(it.attributes.price)
                it.attributes.productPromo?.run {
                    if (this.newPrice.isNotEmpty()) {
                        telco_buy_widget.setTotalPrice(this.newPrice)
                    }
                }
                prepareProductForCheckout(it)
            }
        })

        sharedModelPrepaid.productAutoCheckout.observe(viewLifecycleOwner, Observer {
            if (!isExpressCheckout) {
                setupCheckoutData()
                processTransaction()
            }
        })

        sharedModelPrepaid.showTotalPrice.observe(viewLifecycleOwner, Observer {
            it?.run {
                buyWidget.setVisibilityLayout(it)
            }
        })

        sharedModelPrepaid.selectedFilter.observe(viewLifecycleOwner, Observer {
            if (operatorId.isNotEmpty()) {
                sharedModelPrepaid.getCatalogProductList(DigitalTopupBillsGqlQuery.catalogProductTelco, menuId, operatorId, it)
            }
        })

        sharedModelPrepaid.expandView.observe(viewLifecycleOwner, Observer {
            if (it) {
                telcoClientNumberWidget.setVisibleResultNumber(false)
                hideDynamicSpacer()
            } else {
                telcoClientNumberWidget.setVisibleResultNumber(true)
                if (telcoClientNumberWidget.getInputNumber().isNotEmpty()) showDynamicSpacer() else hideDynamicSpacer()
            }
        })
    }

    private fun showDynamicSpacer() {
        val defaultSpaceHeight = 81
        dynamicSpacer.layoutParams.height =
                context?.resources?.getDimensionPixelSize(R.dimen.telco_dynamic_banner_space)
                        ?: defaultSpaceHeight
        dynamicSpacer.requestLayout()
    }

    private fun hideDynamicSpacer() {
        dynamicSpacer.layoutParams.height = 0
        dynamicSpacer.requestLayout()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_prepaid, container, false)
        pageContainer = view.findViewById(R.id.telco_page_container)
        mainContainer = view.findViewById(R.id.telco_main_container)
        appBarLayout = view.findViewById(R.id.telco_appbar_input_number)
        telcoClientNumberWidget = view.findViewById(R.id.telco_input_number)
        dynamicSpacer = view.findViewById(R.id.digital_telco_dynamic_banner_spacer)
        bannerImage = view.findViewById(R.id.telco_bg_img_banner)
        viewPager = view.findViewById(R.id.telco_view_pager)
        tabLayout = view.findViewById(R.id.telco_tab_layout)
        buyWidget = view.findViewById(R.id.telco_buy_widget)
        tickerView = view.findViewById(R.id.telco_ticker_view)
        loadingShimmering = view.findViewById(R.id.telco_loading_shimmering)
        separator = view.findViewById(R.id.separator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
        initViewPager()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
        if (rechargeProductFromSlice.isNotEmpty()) {
            rechargeAnalytics.onClickSliceRecharge(userSession.userId, rechargeProductFromSlice)
            rechargeAnalytics.onOpenPageFromSlice(TITLE_PAGE)
        }
    }

    private fun prepareProductForCheckout(telcoProduct: TelcoProduct) {
        productId = telcoProduct.id.toIntOrZero()
        price = telcoProduct.attributes.pricePlain
        checkVoucherWithDelay()
        generateCheckoutPassData(telco_input_number.getInputNumber(),
                if (telcoProduct.attributes.productPromo != null) "1" else "0",
                telcoProduct.attributes.categoryId.toString(),
                telcoProduct.attributes.operatorId.toString(),
                telcoProduct.id)
    }

    private fun initViewPager() {
        val pagerAdapter = TelcoTabAdapter(this, object : TelcoTabAdapter.Listener {
            override fun getTabList(): List<TelcoTabItem> {
                return telcoTabViewModel.getAll()
            }
        })
        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(viewPagerCallback)
        tabLayout.customTabMode = TabLayout.MODE_FIXED
        tabLayout.customTabGravity = TabLayout.GRAVITY_FILL
        tabLayout.getUnifyTabLayout().addOnTabSelectedListener(tabLayoutCallback)

    }

    private val tabLayoutCallback = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(p0: TabLayout.Tab?) {
            //do nothing
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
            //do nothing
        }

        override fun onTabSelected(p0: TabLayout.Tab) {
            if (!autoSelectTabProduct) {
                viewPager.setCurrentItem(p0.position, true)
            }
        }
    }

    private val viewPagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            tabLayout.getUnifyTabLayout().getTabAt(position)?.let {
                it.select()
            }
            val tabs = telcoTabViewModel.getAll()
            if (showProducts) {
                sharedModelPrepaid.setPositionScrollToItem(0)
                categoryId = getIdCategoryCurrentItem()
                topupAnalytics.eventClickTelcoPrepaidCategory(tabs[position].title)
                sharedModelPrepaid.setVisibilityTotalPrice(false)
                sharedModelPrepaid.setProductCatalogSelected(getEmptyProduct())
                sharedModelPrepaid.setSelectedCategoryViewPager(getLabelActiveCategory())
            } else {
                setTrackingOnTabMenu(tabs[position].title)
                if (tabs[position].title == TelcoComponentName.PROMO) sendImpressionPromo()
                else sendImpressionRecents()
            }
        }
    }

    override fun onCollapseAppBar() {
        sharedModelPrepaid.setExpandInputNumberView(false)
    }

    override fun onExpandAppBar() {
        sharedModelPrepaid.setExpandInputNumberView(true)
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
        tabLayout.getUnifyTabLayout().removeAllTabs()
        for (i in 0 until listMenu.size) {
            tabLayout.addNewTab(listMenu[i].title)
        }
        changeDataSet { telcoTabViewModel.addAll(listMenu) }
        if (!showProducts) {
            if (listMenu.size > 1) {
                tabLayout.show()
                separator.show()
            } else {
                separator.hide()
                tabLayout.hide()
            }
            //initiate impression promo
            sendImpressionPromo()
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
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM)
                        ?: TopupBillsExtraParam()
                clientNumber = digitalTelcoExtraParam.clientNumber
                productId = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
                if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                    categoryId = digitalTelcoExtraParam.categoryId.toInt()
                    sharedModelPrepaid.setSelectedCategoryViewPager(getLabelActiveCategory())
                }
                if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                    menuId = digitalTelcoExtraParam.menuId.toInt()
                }
                rechargeProductFromSlice = this.getString(RECHARGE_PRODUCT_EXTRA, "")
            }
        } else {
            clientNumber = savedInstanceState.getString(CACHE_CLIENT_NUMBER) ?: ""
        }

        telcoClientNumberWidget.setListener(clientNumberCallback)
        if (clientNumber.isNotEmpty()) {
            autoSelectTabProduct = true
            telcoClientNumberWidget.setInputNumber(clientNumber)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CACHE_CLIENT_NUMBER, telcoClientNumberWidget.getInputNumber())
    }

    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return telco_buy_widget
    }

    override fun initAddToCartViewModel() {
        addToCartViewModel = viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java)
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

    override fun onLoadingAtc(showLoading: Boolean) {
        buyWidget.onBuyButtonLoading(showLoading)
    }

    private val clientNumberCallback = object : DigitalClientNumberWidget.ActionListener {
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

            productId = 0
            operatorId = ""
            sharedModelPrepaid.setVisibilityTotalPrice(false)
        }

        override fun onClientNumberHasFocus(clientNumber: String) {
            operatorId = ""
            productId = 0
            sharedModelPrepaid.setVisibilityTotalPrice(false)

            telcoClientNumberWidget.clearFocusAutoComplete()
            startActivityForResult(activity?.let {
                DigitalSearchNumberActivity.newInstance(it,
                        ClientNumberType.TYPE_INPUT_TEL, clientNumber, favNumberList)
            },
                    REQUEST_CODE_DIGITAL_SEARCH_NUMBER)
        }
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        inputNumberActionType = InputNumberActionType.CONTACT_HOMEPAGE
        telcoClientNumberWidget.setInputNumber(contactNumber)
    }

    private fun getProductListData() {
        if (operatorId.isNotEmpty()) {
            sharedModelPrepaid.getCatalogProductList(DigitalTopupBillsGqlQuery.catalogProductTelco, menuId, operatorId, null,
                    productId)
        }
    }

    private fun renderProductViewPager() {
        var idProductTab = 6L
        val listProductTab = mutableListOf<TelcoTabItem>()
        tabLayout.getUnifyTabLayout().removeAllTabs()
        listProductTab.add(
                TelcoTabItem(generateBundleProduct(TelcoComponentName.PRODUCT_PULSA,
                        operatorName, TelcoProductType.PRODUCT_GRID),
                        TelcoComponentName.PRODUCT_PULSA,
                        idProductTab++))
        listProductTab.add(
                TelcoTabItem(generateBundleProduct(TelcoComponentName.PRODUCT_PAKET_DATA,
                        operatorName, TelcoProductType.PRODUCT_LIST),
                        TelcoComponentName.PRODUCT_PAKET_DATA,
                        idProductTab++))
        listProductTab.add(
                TelcoTabItem(generateBundleProduct(TelcoComponentName.PRODUCT_ROAMING,
                        operatorName, TelcoProductType.PRODUCT_LIST),
                        TelcoComponentName.PRODUCT_ROAMING,
                        idProductTab++))

        for (i in 0 until listProductTab.size) {
            tabLayout.addNewTab(listProductTab[i].title)
        }

        changeDataSet { telcoTabViewModel.addAll(listProductTab) }

        tabLayout.show()
        separator.show()
        setTabFromProductSelected()
    }

    private fun generateBundleProduct(titlePage: String, operatorName: String, productType: Int): Bundle {
        val bundle = Bundle()
        bundle.putString(DigitalTelcoProductFragment.TITLE_PAGE, titlePage)
        bundle.putInt(DigitalTelcoProductFragment.PRODUCT_TYPE, productType)
        bundle.putString(DigitalTelcoProductFragment.OPERATOR_NAME, operatorName)
        bundle.putInt(DigitalTelcoProductFragment.CATEGORY_ID, categoryId)
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

        if (autoSelectTabProduct) {
            tabLayout.getUnifyTabLayout().getTabAt(itemId)?.let {
                it.select()
            }
            autoSelectTabProduct = false
        }
    }

    private fun getLabelActiveCategory(): String {
        return when (categoryId) {
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
            autoSelectTabProduct = true
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
        autoSelectTabProduct = true
        if (orderClientNumber.productId.isNotEmpty() &&
                orderClientNumber.categoryId.toIntOrNull() ?: 0 == categoryId) {
            sharedModelPrepaid.setFavNumberSelected(orderClientNumber)
            sharedModelPrepaid.setSelectedCategoryViewPager(getLabelActiveCategory())
        }

        telcoClientNumberWidget.setInputNumber(orderClientNumber.clientNumber)
        telcoClientNumberWidget.clearFocusAutoComplete()
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
            addToCart()
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
        context?.run {
            val coachMarkHasShown = localCacheHandler.getBoolean(TELCO_COACH_MARK_HAS_SHOWN, false)
            if (coachMarkHasShown) {
                return
            }

            val coachMarks = ArrayList<CoachMark2Item>()
            coachMarks.add(CoachMark2Item(telcoClientNumberWidget,
                    getString(R.string.Telco_title_showcase_client_number),
                    getString(R.string.telco_label_showcase_client_number)))
            coachMarks.add(CoachMark2Item(viewPager,
                    getString(R.string.telco_title_showcase_promo),
                    getString(R.string.telco_label_showcase_promo)))

            val coachMark = CoachMark2(this)
            coachMark.showCoachMark(coachMarks)

            localCacheHandler.apply {
                putBoolean(TELCO_COACH_MARK_HAS_SHOWN, true)
                applyEditor()
            }
        }
    }

    private fun generateCheckoutPassData(inputNumber: String, promoStatus: String,
                                         categoryId: String, operatorId: String, productId: String) {
        checkoutPassData = getDefaultCheckoutPassDataBuilder()
                .categoryId(categoryId)
                .clientNumber(inputNumber)
                .isPromo(promoStatus)
                .operatorId(operatorId)
                .productId(productId)
                .utmCampaign(categoryId)
                .build()
    }

    override fun onDestroy() {
        viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
        viewPager.adapter = null
        tabLayout.getUnifyTabLayout().removeOnTabSelectedListener(tabLayoutCallback)
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
        const val PREFERENCES_NAME = "telco_prepaid_preferences"
        const val TELCO_COACH_MARK_HAS_SHOWN = "telco_show_coach_mark"
        const val ID_PRODUCT_EMPTY = "-1"

        private const val CACHE_CLIENT_NUMBER = "cache_client_number"
        private const val EXTRA_PARAM = "extra_param"
        private const val DG_TELCO_PREPAID_TRACE = "dg_telco_prepaid_pdp"

        private const val TITLE_PAGE = "telco prepaid"

        fun newInstance(telcoExtraParam: TopupBillsExtraParam, rechargeProductFromSlice: String = ""): Fragment {
            val fragment = DigitalTelcoPrepaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            bundle.putString(RECHARGE_PRODUCT_EXTRA, rechargeProductFromSlice)
            fragment.arguments = bundle
            return fragment
        }
    }
}
