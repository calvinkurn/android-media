package com.tokopedia.topupbills.telco.postpaid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.common.topupbills.data.*
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment.InputNumberActionType
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity.Companion.RECHARGE_PRODUCT_EXTRA
import com.tokopedia.topupbills.telco.common.adapter.TelcoTabAdapter
import com.tokopedia.topupbills.telco.common.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.common.viewmodel.TelcoTabViewModel
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.postpaid.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.postpaid.viewmodel.DigitalTelcoEnquiryViewModel
import com.tokopedia.topupbills.telco.postpaid.widget.DigitalPostpaidClientNumberWidget
import com.tokopedia.topupbills.telco.prepaid.widget.DigitalClientNumberWidget
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_telco_postpaid.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoPostpaidFragment : DigitalBaseTelcoFragment() {

    private lateinit var postpaidClientNumberWidget: DigitalPostpaidClientNumberWidget
    private lateinit var buyWidget: TopupBillsCheckoutWidget
    private lateinit var mainContainer: CoordinatorLayout
    private lateinit var enquiryViewModel: DigitalTelcoEnquiryViewModel
    private lateinit var telcoTabViewModel: TelcoTabViewModel
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var loadingShimmering: LinearLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabsUnify
    private lateinit var separator: View
    private var rechargeProductFromSlice: String = ""
    private var traceStop = false
    private var operatorSelected: RechargePrefix? = null
        set(value) {
            field = value
            value?.run {
                productId = operator.attributes.defaultProductId.toIntOrZero()
            }
        }

    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()
    private val seamlessFavNumberList = mutableListOf<TopupBillsSeamlessFavNumberItem>()

    override var menuId = TelcoComponentType.TELCO_POSTPAID
    override var categoryId = TelcoCategoryType.CATEGORY_PASCABAYAR
    private var inputNumberActionType = InputNumberActionType.MANUAL

    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_POSTPAID_TRACE)

            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            enquiryViewModel = viewModelProvider.get(DigitalTelcoEnquiryViewModel::class.java)
            telcoTabViewModel = viewModelProvider.get(TelcoTabViewModel::class.java)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_postpaid, container, false)
        mainContainer = view.findViewById(R.id.telco_main_container)
        pageContainer = view.findViewById(R.id.telco_page_container)
        appBarLayout = view.findViewById(R.id.telco_appbar_input_number)
        bannerImage = view.findViewById(R.id.telco_bg_img_banner)
        postpaidClientNumberWidget = view.findViewById(R.id.telco_input_number)
        buyWidget = view.findViewById(R.id.telco_buy_widget)
        tickerView = view.findViewById(R.id.telco_ticker_view)
        loadingShimmering = view.findViewById(R.id.telco_loading_shimmering)
        viewPager = view.findViewById(R.id.telco_view_pager)
        tabLayout = view.findViewById(R.id.telco_tab_layout)
        separator = view.findViewById(R.id.separator)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        getPrefixOperatorData()
        renderClientNumber()
        getCatalogMenuDetail()
        getDataFromBundle(savedInstanceState)
        if (rechargeProductFromSlice.isNotEmpty()) {
            rechargeAnalytics.onClickSliceRecharge(userSession.userId, rechargeProductFromSlice)
            rechargeAnalytics.onOpenPageFromSlice(TITLE_PAGE)
        }
    }

    override fun getClientInputNumber(): DigitalClientNumberWidget = postpaidClientNumberWidget

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
        tabLayout.getUnifyTabLayout()
            .addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
            tabLayout.getUnifyTabLayout().getTabAt(position)?.let {
                it.select()
            }
            setTrackingOnTabMenu(listMenu[position].title)

            val tabs = telcoTabViewModel.getAll()
            if (tabs[position].title == TelcoComponentName.PROMO) sendImpressionPromo()
            else sendImpressionRecents()
        }
    }

    override fun getTelcoMenuId(): Int {
        return menuId
    }

    override fun getTelcoCategoryId(): Int {
        return categoryId
    }

    override fun renderPromoAndRecommendation() {
        if (listMenu.size > 0) {
            tabLayout.getUnifyTabLayout().removeAllTabs()
            for (i in 0 until listMenu.size) {
                tabLayout.addNewTab(listMenu[i].title)
            }
            changeDataSet { telcoTabViewModel.addAll(listMenu) }
            viewPager.show()

            if (listMenu.size > 1) {
                tabLayout.show()
                separator.show()
            } else {
                separator.hide()
                tabLayout.hide()
            }
            //initiate impression promo
            sendImpressionPromo()
        } else {
            separator.hide()
            tabLayout.hide()
        }
    }

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
        postpaidClientNumberWidget.setFilterChipShimmer(true)
        getMenuDetail(TelcoComponentType.TELCO_POSTPAID)
        getFavoriteNumber(
            categoryIds = listOf(TelcoComponentType.FAV_NUMBER_POSTPAID.toString()),
            oldCategoryId = TelcoComponentType.FAV_NUMBER_POSTPAID
        )
    }

    private fun getDataFromBundle(savedInstanceState: Bundle?) {
        var clientNumber = ""
        if (savedInstanceState == null) {
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM)
                    ?: TopupBillsExtraParam()
                clientNumber = digitalTelcoExtraParam.clientNumber
                if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                    menuId = digitalTelcoExtraParam.menuId.toInt()
                }
                if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                    categoryId = digitalTelcoExtraParam.categoryId.toInt()
                }
                rechargeProductFromSlice = this.getString(RECHARGE_PRODUCT_EXTRA, "")
            }
        } else {
            clientNumber = savedInstanceState.getString(CACHE_CLIENT_NUMBER, "")
        }
        postpaidClientNumberWidget.setInputNumber(clientNumber)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(CACHE_CLIENT_NUMBER, postpaidClientNumberWidget.getInputNumber())
    }

    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return telco_buy_widget
    }

    override fun initAddToCartViewModel() {
        addToCartViewModel = viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java)
    }

    private fun renderClientNumber() {
        postpaidClientNumberWidget.resetClientNumberPostpaid()
        postpaidClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun onNavigateToContact(isSwitchChecked: Boolean) {
                val clientNumber = postpaidClientNumberWidget.getInputNumber()
                navigateContact(
                    clientNumber, favNumberList,
                    arrayListOf(categoryId.toString()), topupAnalytics.getCategoryName(categoryId),
                    isSwitchChecked
                )
            }

            override fun onRenderOperator(isDelayed: Boolean) {
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

                postpaidClientNumberWidget.resetClientNumberPostpaid()
                buyWidget.setVisibilityLayout(false)
            }

            override fun onShowFilterChip(isLabeled: Boolean) {
                if (isLabeled) {
                    topupAnalytics.impressionFavoriteContactChips(categoryId, userSession.userId)
                } else {
                    topupAnalytics.impressionFavoriteNumberChips(categoryId, userSession.userId)
                }
            }

            override fun onClickFilterChip(isLabeled: Boolean) {
                if (isLabeled) {
                    topupAnalytics.clickFavoriteContactChips(categoryId, userSession.userId)
                } else {
                    topupAnalytics.clickFavoriteNumberChips(categoryId, userSession.userId)
                }
            }

            override fun onClickClearInput() {
                topupAnalytics.eventClearInputNumber()
            }

            override fun onUserManualType() {
                if (inputNumberActionType != InputNumberActionType.MANUAL) {
                    inputNumberActionType = InputNumberActionType.MANUAL
                }
            }

            override fun onClickAutoComplete(isFavoriteContact: Boolean) {
                inputNumberActionType = InputNumberActionType.AUTOCOMPLETE
                if (isFavoriteContact) {
                    topupAnalytics.clickFavoriteContactAutoComplete(
                        categoryId, operatorName, userSession.userId
                    )
                } else {
                    topupAnalytics.clickFavoriteNumberAutoComplete(
                        categoryId, operatorName, userSession.userId
                    )
                }
            }
        })

        enquiryViewModel.enquiryResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> enquirySuccess(it.data)
                is Fail -> {
                    enquiryFailed(it.throwable)
                }
            }
        })

        postpaidClientNumberWidget.setPostpaidListener(object : ClientNumberPostpaidListener {
            override fun enquiryNumber() {
                if (postpaidClientNumberWidget.getInputNumber().isEmpty()) {
                    postpaidClientNumberWidget.setErrorInputNumber(getString(R.string.telco_number_invalid_empty_string))
                } else if (userSession.isLoggedIn) {
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
            postpaidClientNumberWidget.setLoadingButtonEnquiry(true)
            enquiryViewModel.getEnquiry(
                CommonTopupBillsGqlQuery.rechargeInquiry,
                selectedOperator.operator.attributes.defaultProductId.toString(),
                postpaidClientNumberWidget.getInputNumber()
            )
        }
    }

    private fun enquirySuccess(enquiryData: TelcoEnquiryData) {
        postpaidClientNumberWidget.setLoadingButtonEnquiry(false)
        tabLayout.hide()
        separator.hide()
        viewPager.hide()
        setCheckoutPassData(enquiryData)
        postpaidClientNumberWidget.showEnquiryResultPostpaid(enquiryData)

        price = enquiryData.enquiry.attributes.pricePlain
        buyWidget.setTotalPrice(enquiryData.enquiry.attributes.price)
        buyWidget.setVisibilityLayout(true)
    }

    private fun enquiryFailed(throwable: Throwable) {
        var error = throwable

        when (error.message) {
            DigitalTelcoEnquiryViewModel.NULL_RESPONSE -> error =
                MessageErrorException(getString(com.tokopedia.common.topupbills.R.string.common_topup_enquiry_error))
            DigitalTelcoEnquiryViewModel.GRPC_ERROR_MSG_RESPONSE -> error =
                MessageErrorException(getString(com.tokopedia.common.topupbills.R.string.common_topup_enquiry_grpc_error_msg))
        }

        val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
            requireContext(),
            error,
            ErrorHandler.Builder()
                .className(this::class.java.simpleName)
                .build()
        )

        postpaidClientNumberWidget.setLoadingButtonEnquiry(false)
        view?.run {
            Toaster.build(
                this,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun onInputNewNumberUpdateLayout() {
        viewPager.show()
        buyWidget.setVisibilityLayout(false)
        postpaidClientNumberWidget.resetEnquiryResult()
    }

    private fun setCheckoutPassData(telcoEnquiryData: TelcoEnquiryData) {
        telcoEnquiryData?.run {
            operatorSelected?.run {
                checkoutPassData = getDefaultCheckoutPassDataBuilder()
                    .categoryId(categoryId.toString())
                    .clientNumber(postpaidClientNumberWidget.getInputNumber())
                    .isPromo("0")
                    .operatorId(operator.id)
                    .productId(operator.attributes.defaultProductId.toString())
                    .utmCampaign(categoryId.toString())
                    .build()
            }
        }
    }

    override fun renderProductFromCustomData(isDelayed: Boolean) {
        try {
            if (postpaidClientNumberWidget.getInputNumber().length >= MINIMUM_OPERATOR_PREFIX) {
                operatorSelected = operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                    postpaidClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                operatorSelected?.run {
                    operatorName = operator.attributes.name
                    productName = operatorName

                    val isInputvalid = validatePhoneNumber(operatorData, postpaidClientNumberWidget)

                    if (isInputvalid) {
                        hitTrackingForInputNumber(this)
                        postpaidClientNumberWidget.clearErrorState()
                    }
                    postpaidClientNumberWidget.setIconOperator(operator.attributes.imageUrl)

                    if (postpaidClientNumberWidget.getInputNumber().length in VALID_MIN_INPUT_NUMBER..VALID_MAX_INPUT_NUMBER) {
                        onInputNewNumberUpdateLayout()
                        postpaidClientNumberWidget.setButtonEnquiry(true)
                    } else {
                        postpaidClientNumberWidget.setButtonEnquiry(false)
                    }
                }
            }
        } catch (exception: NoSuchElementException) {
            postpaidClientNumberWidget.setErrorInputNumber(
                getString(R.string.telco_number_error_prefix_not_found)
            )
        }
    }

    private fun hitTrackingForInputNumber(selectedOperator: RechargePrefix) {
        actionTypeTrackingJob?.cancel()
        actionTypeTrackingJob = lifecycleScope.launch {
            delay(INPUT_ACTION_TYPE_TRACKING_DELAY)
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

    override fun onLoadingAtc(showLoading: Boolean) {
        buyWidget.onBuyButtonLoading(showLoading)
    }

    override fun onCollapseAppBar() {
        //do nothing
    }

    override fun onExpandAppBar() {
        //do nothing
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        postpaidClientNumberWidget.setInputNumber(contactNumber)
        inputNumberActionType = InputNumberActionType.CONTACT_HOMEPAGE
    }

    override fun setContactNameFromContact(contactName: String) {
        postpaidClientNumberWidget.setContactName(contactName)
    }

    override fun handleCallbackAnySearchNumber(
        clientName: String,
        clientNumber: String,
        productId: String,
        categoryId: String,
        inputNumberActionTypeIndex: Int
    ) {
        if (!inputNumberActionTypeIndex.isLessThanZero()) {
            inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]
        }
        postpaidClientNumberWidget.run {
            setContactName(clientName)
            setInputNumber(clientNumber)
            clearFocusAutoComplete()
        }
    }

    override fun handleCallbackAnySearchNumberCancel() {
        postpaidClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation) {
        postpaidClientNumberWidget.setInputNumber(topupBillsRecommendation.clientNumber)
        inputNumberActionType = InputNumberActionType.LATEST_TRANSACTION

        if (operatorName.isNotEmpty()) {
            topupAnalytics.clickEnhanceCommerceRecentTransaction(
                topupBillsRecommendation,
                operatorName, topupBillsRecommendation.position
            )
        }
    }

    override fun setFavNumbers(data: TopupBillsFavNumber) {
        performanceMonitoringStopTrace()
        favNumberList.addAll(data.favNumberList)
    }

    override fun errorSetFavNumbers() {
        performanceMonitoringStopTrace()
        postpaidClientNumberWidget.setFilterChipShimmer(false, true)
    }

    override fun setSeamlessFavNumbers(
        data: TopupBillsSeamlessFavNumber,
        shouldRefreshInputNumber: Boolean
    ) {
        performanceMonitoringStopTrace()
        if (data.favoriteNumbers.isNotEmpty() && shouldRefreshInputNumber) {
            postpaidClientNumberWidget.run {
                setInputNumber(data.favoriteNumbers[0].clientNumber)
                setContactName(data.favoriteNumbers[0].clientName)
            }
        }
        seamlessFavNumberList.addAll(data.favoriteNumbers)
        postpaidClientNumberWidget.setFilterChipShimmer(false, data.favoriteNumbers.isEmpty())
        postpaidClientNumberWidget.setFavoriteNumber(data.favoriteNumbers)
        postpaidClientNumberWidget.setAutoCompleteList(data.favoriteNumbers)
    }

    override fun reloadSortFilterChip() {
        getFavoriteNumber(
            categoryIds = listOf(TelcoComponentType.FAV_NUMBER_POSTPAID.toString()),
            oldCategoryId = TelcoComponentType.FAV_NUMBER_POSTPAID,
            false
        )
    }

    private fun performanceMonitoringStopTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace()
            traceStop = true
        }
    }

    override fun showErrorCartDigital(message: String) {
        view?.run {
            Toaster.build(this, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onDestroy() {
        viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
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
        private const val TITLE_PAGE = "telco post paid"

        private const val VALID_MIN_INPUT_NUMBER = 10
        private const val VALID_MAX_INPUT_NUMBER = 14

        private const val INPUT_ACTION_TYPE_TRACKING_DELAY = 1000L

        fun newInstance(
            telcoExtraParam: TopupBillsExtraParam,
            rechargeProductFromSlice: String = ""
        ): Fragment {
            val fragment = DigitalTelcoPostpaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            bundle.putString(RECHARGE_PRODUCT_EXTRA, rechargeProductFromSlice)
            fragment.arguments = bundle
            return fragment
        }
    }
}
