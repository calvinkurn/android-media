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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryQuery
import com.tokopedia.common.topupbills.data.TopupBillsFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment.InputNumberActionType
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.searchnumber.view.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity.Companion.RECHARGE_PRODUCT_EXTRA
import com.tokopedia.topupbills.telco.common.adapter.TelcoTabAdapter
import com.tokopedia.topupbills.telco.common.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.common.viewmodel.TelcoTabViewModel
import com.tokopedia.topupbills.telco.data.RechargePrefix
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
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
                productId = operator.attributes.defaultProductId
            }
        }

    private val favNumberList = mutableListOf<TopupBillsFavNumberItem>()

    override var menuId = TelcoComponentType.TELCO_POSTPAID
    override var categoryId = TelcoCategoryType.CATEGORY_PASCABAYAR
    private var inputNumberActionType = InputNumberActionType.MANUAL

    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            performanceMonitoring = PerformanceMonitoring.start(DG_TELCO_POSTPAID_TRACE)

            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            enquiryViewModel = viewModelProvider.get(DigitalTelcoEnquiryViewModel::class.java)
            telcoTabViewModel = viewModelProvider.get(TelcoTabViewModel::class.java)
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        tabLayout.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
        getMenuDetail(TelcoComponentType.TELCO_POSTPAID)
        getFavoriteNumbers(TelcoComponentType.FAV_NUMBER_POSTPAID)
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
            DigitalTelcoEnquiryViewModel.NULL_RESPONSE -> error = MessageErrorException(getString(com.tokopedia.common.topupbills.R.string.common_topup_enquiry_error))
            DigitalTelcoEnquiryViewModel.GRPC_ERROR_MSG_RESPONSE -> error = MessageErrorException(getString(com.tokopedia.common.topupbills.R.string.common_topup_enquiry_grpc_error_msg))
        }

        postpaidClientNumberWidget.setLoadingButtonEnquiry(false)
        view?.run {
            Toaster.build(this, ErrorHandler.getErrorMessage(context, error), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
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

    override fun renderProductFromCustomData() {
        try {
            if (postpaidClientNumberWidget.getInputNumber().isNotEmpty()) {
                operatorSelected = operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                    postpaidClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                operatorSelected?.run {
                    operatorName = operator.attributes.name
                    productName = operatorName
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
                        onInputNewNumberUpdateLayout()
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


        fun newInstance(telcoExtraParam: TopupBillsExtraParam, rechargeProductFromSlice: String = ""): Fragment {
            val fragment = DigitalTelcoPostpaidFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            bundle.putString(RECHARGE_PRODUCT_EXTRA, rechargeProductFromSlice)
            fragment.arguments = bundle
            return fragment
        }
    }
}
