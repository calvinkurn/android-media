package com.tokopedia.topupbills.telco.postpaid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.widget.TopupBillsCheckoutWidget
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.adapter.TelcoTabAdapter
import com.tokopedia.topupbills.telco.common.fragment.DigitalBaseTelcoFragment
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import com.tokopedia.topupbills.telco.common.viewmodel.TelcoTabViewModel
import com.tokopedia.topupbills.telco.common.widget.DigitalClientNumberWidget
import com.tokopedia.topupbills.telco.postpaid.listener.ClientNumberPostpaidListener
import com.tokopedia.topupbills.telco.postpaid.widget.DigitalSignalClientNumberWidget
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by misael on 14/07/22
 * Use the same template as telco postpaid and copied from on [DigitalTelcoPostpaidFragment].
 * */
class DigitalSignalFragment : DigitalBaseTelcoFragment() {

    private lateinit var signalClientNumberWidget: DigitalSignalClientNumberWidget
    private lateinit var mainContainer: CoordinatorLayout
    private lateinit var telcoTabViewModel: TelcoTabViewModel
    private lateinit var loadingShimmering: ConstraintLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabsUnify
    private lateinit var separator: View
    private lateinit var appBarSpacer: View
    private var operatorSelected: RechargePrefix? = null
        set(value) {
            field = value
            value?.run {
                productId = operator.attributes.defaultProductId.toIntSafely()
            }
        }

    private var inputNumberActionType = TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }

    override var menuId = 0
    override var categoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
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
        val view = inflater.inflate(R.layout.fragment_digital_signal, container, false)
        mainContainer = view.findViewById(R.id.signal_main_container)
        pageContainer = view.findViewById(R.id.signal_page_container)
        appBarLayout = view.findViewById(R.id.signal_appbar_input_number)
        bannerImage = view.findViewById(R.id.signal_bg_img_banner)
        signalClientNumberWidget = view.findViewById(R.id.signal_input_number)
        tickerView = view.findViewById(R.id.signal_ticker_view)
        loadingShimmering = view.findViewById(R.id.telco_loading_shimmering)
        viewPager = view.findViewById(R.id.signal_view_pager)
        tabLayout = view.findViewById(R.id.signal_tab_layout)
        separator = view.findViewById(R.id.separator)
        appBarSpacer = view.findViewById(R.id.signal_appbar_spacer)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        renderClientNumber()
        getDataFromBundle(savedInstanceState)
        initViewPager()
        getPrefixOperatorData()
        getCatalogMenuDetail()
        initSignalPage()
    }

    private fun initSignalPage() {
        signalClientNumberWidget.run {
            hideContactIcon()
            setTextFieldStaticLabel(context.getString(R.string.digital_client_label_signal))
        }
        appBarSpacer.hide()
    }

    override fun getClientInputNumber(): DigitalClientNumberWidget = signalClientNumberWidget

    private fun initViewPager() {
        val pagerAdapter = TelcoTabAdapter(
            this,
            object : TelcoTabAdapter.Listener {
                override fun getTabList(): List<TelcoTabItem> {
                    return telcoTabViewModel.getAll()
                }
            }
        )
        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(viewPagerCallback)
        tabLayout.customTabMode = TabLayout.MODE_FIXED
        tabLayout.customTabGravity = TabLayout.GRAVITY_FILL
        tabLayout.getUnifyTabLayout()
            .addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    // do nothing
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                    // do nothing
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
            if (tabs[position].title == TelcoComponentName.PROMO) {
                sendImpressionPromo()
            } else {
                sendImpressionRecents()
            }
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

            if (listMenu.size > 1) {
                tabLayout.show()
                separator.show()
            } else {
                separator.hide()
                tabLayout.hide()
            }
            // initiate impression promo
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
        viewPager.adapter?.let {
            DiffUtil.calculateDiff(
                object : DiffUtil.Callback() {
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
                },
                true
            ).dispatchUpdatesTo(it)
        }
    }

    override fun setupCheckoutData() {
        // do nothing
    }

    private fun getCatalogMenuDetail() {
        getMenuDetail(menuId)
    }

    private fun getDataFromBundle(savedInstanceState: Bundle?) {
        var clientNumber = ""
        if (savedInstanceState == null) {
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM)
                    ?: TopupBillsExtraParam()
                clientNumber = digitalTelcoExtraParam.clientNumber
                if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                    menuId = digitalTelcoExtraParam.menuId.toIntSafely()
                }
                if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                    categoryId = digitalTelcoExtraParam.categoryId.toIntSafely()
                }
            }
        } else {
            clientNumber = savedInstanceState.getString(CACHE_CLIENT_NUMBER, "")
        }
        signalClientNumberWidget.setInputNumber(clientNumber)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(CACHE_CLIENT_NUMBER, signalClientNumberWidget.getInputNumber())
    }

    override fun getCheckoutView(): TopupBillsCheckoutWidget? {
        return null
    }

    override fun initAddToCartViewModel() {
        addToCartViewModel = viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java)
    }

    override fun redirectErrorUnVerifiedNumber(error: ErrorAtc) {
        /*no op*/
    }

    private fun renderClientNumber() {
        signalClientNumberWidget.resetClientNumberPostpaid()
        signalClientNumberWidget.setListener(object : DigitalClientNumberWidget.ActionListener {
            override fun onNavigateToContact(isSwitchChecked: Boolean) {
                // do nothing
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

                signalClientNumberWidget.resetClientNumberPostpaid()
            }

            override fun onShowFilterChip(isLabeled: Boolean) {
                // do nothing
            }

            override fun onClickFilterChip(isLabeled: Boolean) {
                // do nothing
            }

            override fun onClickClearInput() {
                topupAnalytics.eventClearInputNumber()
            }

            override fun onUserManualType() {
                if (inputNumberActionType != TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL) {
                    inputNumberActionType = TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL
                }
            }

            override fun onClickAutoComplete(isFavoriteContact: Boolean) {
                // do nothing
            }
        })

        signalClientNumberWidget.setPostpaidListener(object : ClientNumberPostpaidListener {
            override fun enquiryNumber() {
                if (signalClientNumberWidget.getInputNumber().isEmpty()) {
                    signalClientNumberWidget.setErrorInputNumber(getString(R.string.telco_number_invalid_empty_string))
                } else if (userSession.isLoggedIn) {
                    setCheckoutPassData()
                    processTransaction()
                } else {
                    navigateToLoginPage()
                }
            }
        })
    }

    private fun setCheckoutPassData() {
        operatorSelected?.run {
            checkoutPassData = getDefaultCheckoutPassDataBuilder()
                .categoryId(categoryId.toString())
                .clientNumber(signalClientNumberWidget.getInputNumber())
                .isPromo("0")
                .operatorId(operator.id)
                .productId(operator.attributes.defaultProductId)
                .utmCampaign(categoryId.toString())
                .build()
        }
    }

    override fun renderProductFromCustomData(isDelayed: Boolean) {
        try {
            if (signalClientNumberWidget.getInputNumber().length >= MINIMUM_OPERATOR_PREFIX) {
                operatorSelected = operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                    signalClientNumberWidget.getInputNumber().startsWith(it.value)
                }
                operatorSelected?.run {
                    operatorName = operator.attributes.name
                    productName = operatorName

                    val isInputValid = validatePhoneNumber(operatorData, signalClientNumberWidget)

                    if (isInputValid) {
                        hitTrackingForInputNumber()
                        signalClientNumberWidget.clearErrorState()
                        signalClientNumberWidget.setButtonEnquiry(true)
                    } else {
                        signalClientNumberWidget.setButtonEnquiry(false)
                    }
                    signalClientNumberWidget.showOperatorResult(
                        getString(R.string.signal_operator_result_label) to operatorName
                    )
                }
            } else {
                signalClientNumberWidget.resetClientNumberPostpaid()
                operatorName = ""
                productName = ""
            }
        } catch (exception: NoSuchElementException) {
            signalClientNumberWidget.setErrorInputNumber(
                getString(R.string.signal_number_error_prefix_not_found)
            )
        }
    }

    private fun hitTrackingForInputNumber() {
        actionTypeTrackingJob?.cancel()
        actionTypeTrackingJob = lifecycleScope.launch {
            delay(INPUT_ACTION_TYPE_TRACKING_DELAY)
            when (inputNumberActionType) {
                TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL -> {
                    topupAnalytics.eventInputNumberManual(categoryId, operatorName)
                }
                else -> {
                    // no op
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
        // do nothing
    }

    override fun setInputNumberFromContact(contactNumber: String) {
        // do nothing
    }

    override fun setContactNameFromContact(contactName: String) {
        // do nothing
    }

    override fun handleCallbackAnySearchNumber(
        clientName: String,
        clientNumber: String,
        productId: String,
        categoryId: String,
        inputNumberActionTypeIndex: Int
    ) {
        // do nothing
    }

    override fun handleCallbackAnySearchNumberCancel() {
        // do nothing
    }

    override fun onClickItemRecentNumber(topupBillsRecommendation: TopupBillsRecommendation) {
        signalClientNumberWidget.setInputNumber(topupBillsRecommendation.clientNumber)
        inputNumberActionType = TopupBillsSearchNumberFragment.InputNumberActionType.LATEST_TRANSACTION

        if (operatorName.isNotEmpty()) {
            topupAnalytics.clickEnhanceCommerceRecentTransaction(
                topupBillsRecommendation,
                operatorName,
                topupBillsRecommendation.position
            )
        }
    }

    override fun errorSetFavNumbers() {
        // do nothing
    }

    override fun setSeamlessFavNumbers(
        data: TopupBillsSeamlessFavNumber,
        shouldRefreshInputNumber: Boolean
    ) {
        // do nothing
    }

    override fun reloadSortFilterChip() {
        // do nothing
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
        signalClientNumberWidget.clearFocusAutoComplete()
    }

    override fun onBackPressed() {
        topupAnalytics.eventClickBackButton(categoryId)
    }

    companion object {

        private const val CACHE_CLIENT_NUMBER = "cache_client_number"
        private const val EXTRA_PARAM = "extra_param"

        private const val MINIMUM_OPERATOR_PREFIX = 2

        private const val INPUT_ACTION_TYPE_TRACKING_DELAY = 1000L

        fun newInstance(
            extraParam: TopupBillsExtraParam
        ): Fragment {
            val fragment = DigitalSignalFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, extraParam)
            fragment.arguments = bundle
            return fragment
        }
    }
}
