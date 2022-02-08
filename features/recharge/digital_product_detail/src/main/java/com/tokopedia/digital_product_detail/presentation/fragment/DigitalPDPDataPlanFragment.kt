package com.tokopedia.digital_product_detail.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.utils.CommonTopupBillsUtil
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.activity.TopupBillsSavedNumberActivity
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment.Companion.REQUEST_CODE_CART_DIGITAL
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.model.TopupBillsSavedNumber
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.FAVNUM_PERMISSION_CHECKER_IS_DENIED
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.INPUT_ACTION_TRACKING_DELAY
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MAXIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_OPERATOR_PREFIX
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.TELCO_PREFERENCES_NAME
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_LOGIN
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpDataPlanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.bottomsheet.FilterPDPBottomsheet
import com.tokopedia.digital_product_detail.presentation.bottomsheet.ProductDescBottomSheet
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryTelcoBottomSheet
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoAnalytics
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.utils.setupDynamicAppBar
import com.tokopedia.digital_product_detail.presentation.utils.toggle
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget.InputNumberActionType
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by firmanda on 04/01/21
 */

class DigitalPDPDataPlanFragment :
    BaseDaggerFragment(),
    RechargeBuyWidgetListener,
    RechargeRecommendationCardListener,
    RechargeDenomFullListener,
    RechargeClientNumberWidget.ClientNumberInputFieldListener,
    RechargeClientNumberWidget.ClientNumberFilterChipListener,
    RechargeClientNumberWidget.ClientNumberAutoCompleteListener,
    FilterPDPBottomsheet.FilterBottomSheetListener
{
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPDataPlanViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPTelcoAnalytics: DigitalPDPTelcoAnalytics

    private var binding by autoClearedNullable<FragmentDigitalPdpDataPlanBinding>()

    private var dynamicSpacerHeightRes = R.dimen.dynamic_banner_space
    private var operator = TelcoOperator()
    private var loyaltyStatus = ""
    private var clientNumber = ""
    private var productId =  0
    private var menuId = 0
    private var categoryId = TelcoCategoryType.CATEGORY_PAKET_DATA
    private var inputNumberActionType = InputNumberActionType.MANUAL

    private lateinit var localCacheHandler: LocalCacheHandler
    private var actionTypeTrackingJob: Job? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPDataPlanViewModel::class.java)
        localCacheHandler = LocalCacheHandler(context, TELCO_PREFERENCES_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDigitalPdpDataPlanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromBundle()
        initClientNumberWidget()
        initEmptyState()
        setAnimationAppBarLayout()
        observeData()

        getCatalogMenuDetail()
        getPrefixOperatorData()
    }

    private fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(DigitalPDPConstant.EXTRA_PARAM)
                ?: TopupBillsExtraParam()
            clientNumber = digitalTelcoExtraParam.clientNumber
            productId = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
            if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                categoryId = digitalTelcoExtraParam.categoryId.toInt()
            }
            if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                menuId = digitalTelcoExtraParam.menuId.toIntOrNull() ?: 0
            }
        }
        if (!clientNumber.isNullOrEmpty()) {
            binding?.rechargePdpPaketDataClientNumberWidget?.run {
                setInputNumber(clientNumber, true)
            }
        }
    }

    private fun renderProduct() {
        binding?.run {
            try {
                val selectedClientNumber = rechargePdpPaketDataClientNumberWidget.getInputNumber()
                if (selectedClientNumber.length >= MINIMUM_OPERATOR_PREFIX) {

                    /* operator check */
                    val selectedOperator =
                        viewModel.operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                            selectedClientNumber.startsWith(it.value)
                        }

                    /* validate client number */
                    viewModel.validateClientNumber(selectedClientNumber)
                    hitTrackingForInputNumber(
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        selectedOperator.operator.attributes.name
                    )

                    if (operator.id != selectedOperator.operator.id || selectedClientNumber
                            .length in MINIMUM_VALID_NUMBER_LENGTH..MAXIMUM_VALID_NUMBER_LENGTH
                    ) {
                        operator = selectedOperator.operator
                        rechargePdpPaketDataClientNumberWidget.run {
                            showOperatorIcon(selectedOperator.operator.attributes.imageUrl)
                        }
                        hideEmptyState()
                        getCatalogProductInputMultiTab(selectedOperator.key, selectedClientNumber)
                    } else {
                        onHideBuyWidget()
                    }

                } else {
                    operator = TelcoOperator()
                    viewModel.cancelCatalogProductJob()
                    showEmptyState()
                }
            } catch (exception: NoSuchElementException) {
                operator = TelcoOperator()
                viewModel.cancelCatalogProductJob()
                binding?.rechargePdpPaketDataClientNumberWidget?.setLoading(false)
                rechargePdpPaketDataClientNumberWidget.setErrorInputField(
                    getString(com.tokopedia.recharge_component.R.string.client_number_prefix_error),
                    true
                )
            }
        }
    }

    private fun observeData() {
        viewModel.menuDetailData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetMenuDetail(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetMenuDetail(it.error)
                is RechargeNetworkResult.Loading -> {
                    onShimmeringRecommendation()
                }
            }
        })

        viewModel.favoriteNumberData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetFavoriteNumber(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetFavoriteNumber(it.error)
                is RechargeNetworkResult.Loading -> {
                    binding?.rechargePdpPaketDataClientNumberWidget?.setFilterChipShimmer(true)
                }
            }
        })

        viewModel.catalogPrefixSelect.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetPrefixOperator()
                is RechargeNetworkResult.Fail -> onFailedGetPrefixOperator(it.error)
                is RechargeNetworkResult.Loading -> {}
            }
        })

        viewModel.observableDenomMCCMData.observe(viewLifecycleOwner, { denomData ->
            when (denomData) {
                is RechargeNetworkResult.Success -> {
                    val selectedPositionDenom = viewModel.getSelectedPositionId(denomData.data.denomFull.listDenomData)
                    val selectedPositionMCCM = viewModel.getSelectedPositionId(denomData.data.denomMCCMFull.listDenomData)

                    if (denomData.data.isFilterRefreshed) {
                        digitalPDPTelcoAnalytics.impressionFilterChip(
                            DigitalPDPCategoryUtil.getCategoryName(categoryId),
                            operator.attributes.name,
                            userSession.userId,
                        )
                        onSuccessSortFilter()
                    }
                    onSuccessDenomFull(denomData.data.denomFull, selectedPositionDenom)
                    onSuccessMCCM(denomData.data.denomMCCMFull, selectedPositionMCCM)

                    if (selectedPositionDenom == null && selectedPositionMCCM == null) {
                        onHideBuyWidget()
                    }
                }

                is RechargeNetworkResult.Fail -> {
                    onFailedDenomFull()
                    onLoadingAndFailMCCM()
                }

                is RechargeNetworkResult.Loading -> {
                    onShimmeringDenomFull()
                    onLoadingAndFailMCCM()
                }
            }
        })

        viewModel.addToCartResult.observe(viewLifecycleOwner,{ atcData ->
            when(atcData) {
                is RechargeNetworkResult.Success -> {
                    onLoadingBuyWidget(false)
                    navigateToCart(atcData.data)
                }

                is RechargeNetworkResult.Fail -> {
                    onLoadingBuyWidget(false)
                    showErrorToaster(atcData.error)
                }

                is RechargeNetworkResult.Loading -> {
                    onLoadingBuyWidget(true)
                }
            }
        })

        viewModel.clientNumberValidatorMsg.observe(viewLifecycleOwner, { msg ->
            binding?.rechargePdpPaketDataClientNumberWidget?.run {
                setLoading(false)
                if (msg.isEmpty()) {
                    showIndicatorIcon()
                    clearErrorState()
                } else {
                    hideIndicatorIcon(shouldShowClearIcon = true)
                    setErrorInputField(msg)
                    onHideBuyWidget()
                }
            }
        })
    }

    private fun getCatalogProductInputMultiTab(
        selectedOperatorKey: String,
        clientNumber: String
    ) {
        viewModel.getRechargeCatalogInputMultiTab(menuId, selectedOperatorKey, clientNumber)
    }

    private fun getCatalogMenuDetail() {
        viewModel.getMenuDetail(menuId)
    }

    private fun getPrefixOperatorData() {
        viewModel.getPrefixOperator(menuId)
    }

    private fun getFavoriteNumber() {
        viewModel.getFavoriteNumber(listOf(
            TelcoCategoryType.CATEGORY_PULSA,
            TelcoCategoryType.CATEGORY_PAKET_DATA,
            TelcoCategoryType.CATEGORY_ROAMING
        ))
    }

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        getFavoriteNumber()

        renderPrefill(data.userPerso)
        renderRecommendation(data.recommendations)
        renderTicker(data.tickers)
    }

    private fun onFailedGetMenuDetail(throwable: Throwable) {
        val (errMsg, errCode) = ErrorHandler.getErrorMessagePair(
            activity, throwable, ErrorHandler.Builder().build()
        )
        val errMsgSub = getString(
            R.string.error_message_with_code,
            getString(com.tokopedia.abstraction.R.string.msg_network_error_2),
            errCode
        )
        binding?.run {
            NetworkErrorHelper.showEmptyState(
                activity,
                rechargePdpPaketDataPageContainer,
                errMsg,
                errMsgSub,
                null,
                DigitalPDPConstant.DEFAULT_ICON_RES
            ) {
                getCatalogMenuDetail()
            }
        }
        onFailedRecommendation()
    }

    private fun onFailedRecommendation() {
        binding?.rechargePdpPaketDataRecommendationWidget?.renderFailRecommendation()
    }

    private fun onSuccessGetFavoriteNumber(favoriteNumber: List<TopupBillsPersoFavNumberItem>) {
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            setFilterChipShimmer(false, favoriteNumber.isEmpty())
            if (favoriteNumber.isNotEmpty()) {
                setFilterChipShimmer(false, favoriteNumber.isEmpty())
                setFavoriteNumber(favoriteNumber)
                setAutoCompleteList(favoriteNumber)
                dynamicSpacerHeightRes = R.dimen.dynamic_banner_space_extended
            }
        }
    }

    private fun onFailedGetFavoriteNumber(throwable: Throwable) {
        binding?.run {
            rechargePdpPaketDataClientNumberWidget.setFilterChipShimmer(false, true)
        }
    }

    private fun onSuccessGetPrefixOperator() {
        renderProduct()
    }

    private fun onFailedGetPrefixOperator(throwable: Throwable) {
        showEmptyState()
        showErrorToaster(throwable)
    }

    private fun onSuccessSortFilter(initialSelectedCounter: Int = 0){
        binding?.let {
            if (!viewModel.filterData.isNullOrEmpty()){
                it.sortFilterPaketData.run {
                    show()
                    val filterItems = arrayListOf<SortFilterItem>()
                    val chipItems = viewModel.filterData.first().filterTagDataCollections
                    chipItems.forEach {
                        val item = SortFilterItem(it.value)
                        filterItems.add(item)
                    }

                    var selectedChipsCounter = initialSelectedCounter

                    filterItems.forEachIndexed{ index, sortFilterItem ->
                        if (chipItems.get(index).isSelected) {
                            sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                        }

                        sortFilterItem.listener = {
                            sortFilterItem.toggle()
                            if (!chipItems.get(index).isSelected) {
                                digitalPDPTelcoAnalytics.clickFilterChip(
                                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                                    operator.attributes.name,
                                    sortFilterItem.title.toString(),
                                    userSession.userId,
                                )
                            }
                            if (filterItems[index].type == ChipsUnify.TYPE_SELECTED){
                                chipItems.get(index).isSelected = true
                                selectedChipsCounter++
                            } else {
                                chipItems.get(index).isSelected = false
                                selectedChipsCounter--
                            }

                            viewModel.filterData.first().filterTagDataCollections = chipItems
                            onChipClicked()
                        }
                    }

                    addItem(filterItems)
                    val filterData = viewModel.filterData
                    sortFilterPrefix.setOnClickListener {
                        digitalPDPTelcoAnalytics.clickFilterChip(
                            DigitalPDPCategoryUtil.getCategoryName(categoryId),
                            operator.attributes.name,
                            getString(R.string.bottom_sheet_filter_title),
                            userSession.userId,
                        )
                        fragmentManager?.let {
                            FilterPDPBottomsheet(getString(R.string.bottom_sheet_filter_title),
                                getString(R.string.bottom_sheet_filter_reset),
                                filterData, this@DigitalPDPDataPlanFragment)
                                .show(it, "")
                        }
                    }

                    indicatorCounter = selectedChipsCounter
                }
            }
        }
    }

    private fun onChipClicked(){
        viewModel.updateFilterData()
        viewModel.getRechargeCatalogInputMultiTab(menuId, operator.id, binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?: "", false)
    }

    private fun onSuccessDenomFull(denomData: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.FULL_TYPE)){
                onShowBuyWidget(viewModel.selectedFullProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            it.rechargePdpPaketDataDenomFullWidget.renderDenomFullLayout(this, denomData, selectedInitialPosition)
            it.rechargePdpPaketDataDenomFullWidget.show()
        }
    }

    private fun onFailedDenomFull() {
        binding?.let {
            it.rechargePdpPaketDataDenomFullWidget.renderFailDenomFull()
        }
    }


    private fun onShimmeringDenomFull() {
        binding?.let {
            it.rechargePdpPaketDataDenomFullWidget.renderDenomFullShimmering()
        }
    }

    private fun onClearSelectedDenomFull(){
        binding?.let {
            it.rechargePdpPaketDataDenomFullWidget.clearSelectedProduct()
        }
    }

    private fun onSuccessMCCM(denomFull: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_FULL_TYPE)){
                onShowBuyWidget(viewModel.selectedFullProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            it.rechargePdpPaketDataPromoWidget.show()
            it.rechargePdpPaketDataPromoWidget.renderMCCMFull(this, denomFull,
                getString(com.tokopedia.unifyprinciples.R.color.Unify_N0), selectedInitialPosition)
        }
    }

    private fun onLoadingAndFailMCCM(){
        binding?.let {
            it.rechargePdpPaketDataPromoWidget.renderFailMCCMFull()
        }
    }

    private fun onClearSelectedMCCM(){
        binding?.let {
            it.rechargePdpPaketDataPromoWidget.clearSelectedProduct()
        }
    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            setCustomInputNumberFormatter { inputNumber ->
                CommonTopupBillsUtil.formatPrefixClientNumber(inputNumber) }
            setInputFieldStaticLabel(
                getString(
                    com.tokopedia.recharge_component.R.string.label_recharge_client_number_telco
                )
            )
            setInputFieldType(RechargeClientNumberWidget.InputFieldType.Telco)
            setListener(
                this@DigitalPDPDataPlanFragment,
                this@DigitalPDPDataPlanFragment,
                this@DigitalPDPDataPlanFragment)
        }
    }

    private fun renderPrefill(data: TopupBillsUserPerso) {
        binding?.rechargePdpPaketDataClientNumberWidget?.setInputNumber(data.prefill, true)
    }

    private fun renderTicker(tickers: List<TopupBillsTicker>) {
        if (tickers.isNotEmpty()) {
            val messages = ArrayList<TickerData>()
            for (item in tickers) {
                messages.add(
                    TickerData(
                        item.name, item.content,
                        when (item.type) {
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }
                    )
                )
            }
            binding?.rechargePdpPaketDataTicker?.run {
                addPagerView(
                    TickerPagerAdapter(
                    this@DigitalPDPDataPlanFragment.requireContext(), messages), messages)
                show()
            }
        } else {
            binding?.rechargePdpPaketDataTicker?.hide()
        }
    }

    private fun renderRecommendation(recommendations: List<RecommendationCardWidgetModel>) {
        binding?.let {
            it.rechargePdpPaketDataRecommendationWidget.show()
            it.rechargePdpPaketDataRecommendationWidget.renderRecommendationLayout(this,
                getString(R.string.digital_pdp_recommendation_title),
                recommendations
            )
        }
    }

    private fun onShimmeringRecommendation() {
        binding?.let {
            it.rechargePdpPaketDataRecommendationWidget.show()
            it.rechargePdpPaketDataRecommendationWidget.renderShimmering()
        }
    }

    private fun onShowBuyWidget(denomFull: DenomData) {
        binding?.let {
            it.rechargePdpPaketDataBuyWidget.showBuyWidget(denomFull, this)
        }
    }

    private fun onHideBuyWidget() {
        binding?.let {
            it.rechargePdpPaketDataBuyWidget.hideBuyWidget()
        }
    }

    private fun onLoadingBuyWidget(isLoading: Boolean) {
        binding?.let {
            it.rechargePdpPaketDataBuyWidget.isLoadingButton(isLoading)
        }
    }

    private fun initEmptyState() {
        // [Misael] replace with catalogMenuDetail.banners
        binding?.rechargePdpPaketDataEmptyStateWidget?.setImageUrl(
            "https://images.tokopedia.net/img/ULHhFV/2022/1/7/8324919c-fa15-46d9-84f7-426adb6994e0.jpg"
        )
    }

    private fun showEmptyState() {
        binding?.run {
            if (!rechargePdpPaketDataEmptyStateWidget.isVisible) {
                digitalPDPTelcoAnalytics.impressionBannerEmptyState(
                    "TODO Creative Link",
                    categoryId.toString(),
                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                    loyaltyStatus,
                    userSession.userId
                )
                rechargePdpPaketDataEmptyStateWidget.show()
                rechargePdpPaketDataPromoWidget.hide()
                sortFilterPaketData.hide()
                rechargePdpPaketDataRecommendationWidget.hide()
                rechargePdpPaketDataDenomFullWidget.hide()
                rechargePdpPaketDataClientNumberWidget.hideOperatorIcon()
            }
        }
    }

    private fun hideEmptyState() {
        binding?.run {
            if (rechargePdpPaketDataEmptyStateWidget.isVisible) {
                rechargePdpPaketDataEmptyStateWidget.hide()
                rechargePdpPaketDataRecommendationWidget.show()
            }
        }
    }

    private fun hitTrackingForInputNumber(categoryName: String, operatorName: String) {
        actionTypeTrackingJob?.cancel()
        actionTypeTrackingJob = lifecycleScope.launch {
            delay(INPUT_ACTION_TRACKING_DELAY)
            when (inputNumberActionType) {
                InputNumberActionType.MANUAL -> {
                    digitalPDPTelcoAnalytics.eventInputNumberManual(
                        categoryName,
                        operatorName,
                        userSession.userId
                    )
                }
                InputNumberActionType.CONTACT -> {
                    digitalPDPTelcoAnalytics.eventInputNumberContact(
                        categoryName,
                        operatorName,
                        userSession.userId
                    )
                }
                InputNumberActionType.FAVORITE -> {
                    digitalPDPTelcoAnalytics.eventInputNumberFavorite(
                        categoryName,
                        operatorName,
                        userSession.userId
                    )
                }
                else -> {}
            }
        }
    }

    private fun handleCallbackSavedNumber(
        clientName: String,
        clientNumber: String,
        productId: String,
        categoryId: String,
        inputNumberActionTypeIndex: Int
    ) {
        if (!inputNumberActionTypeIndex.isLessThanZero()) {
            inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]
        }

        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            setContactName(clientName)
            setInputNumber(clientNumber, true)
        }
    }

    private fun handleCallbackAnySavedNumberCancel() {
        binding?.rechargePdpPaketDataClientNumberWidget?.clearFocusAutoComplete()
    }
    private fun setAnimationAppBarLayout() {
        binding?.run {
            rechargePdpPaketDataAppbar.setupDynamicAppBar(
                { !viewModel.isEligibleToBuy },
                { rechargePdpPaketDataClientNumberWidget.getInputNumber().isEmpty() },
                { onCollapseAppBar() },
                { onExpandAppBar() }
            )
        }
    }

    private fun showDynamicSpacer() {
        binding?.rechargePdpPaketDataDynamicBannerSpacer?.layoutParams?.height =
            context?.resources?.getDimensionPixelSize(dynamicSpacerHeightRes)
                ?: DigitalPDPConstant.DEFAULT_SPACE_HEIGHT
        binding?.rechargePdpPaketDataDynamicBannerSpacer?.requestLayout()
    }

    private fun hideDynamicSpacer() {
        binding?.rechargePdpPaketDataDynamicBannerSpacer?.layoutParams?.height = 0
        binding?.rechargePdpPaketDataDynamicBannerSpacer?.requestLayout()
    }

    private fun onCollapseAppBar() {
        binding?.run {
            rechargePdpPaketDataClientNumberWidget.setVisibleSimplifiedLayout(true)
            showDynamicSpacer()
        }
    }

    private fun onExpandAppBar() {
        binding?.run {
            rechargePdpPaketDataClientNumberWidget.setVisibleSimplifiedLayout(false)
            hideDynamicSpacer()
        }
    }

    private fun navigateToCart(categoryId: String) {
        context?.let { context ->
            val intent = RouteManager.getIntent(context, ApplinkConsInternalDigital.CHECKOUT_DIGITAL)
            viewModel.updateCategoryCheckoutPassData(categoryId)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, viewModel.digitalCheckoutPassData)
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
        }
    }

    private fun navigateToContact(
        clientNumber: String,
        dgCategoryIds: ArrayList<String>,
        categoryName: String,
        isSwitchChecked: Boolean
    ) {
        val isDeniedOnce = localCacheHandler.getBoolean(FAVNUM_PERMISSION_CHECKER_IS_DENIED, false)
        if (!isDeniedOnce && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.checkPermission(this,
                PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        navigateSavedNumber(clientNumber, dgCategoryIds, categoryName, isSwitchChecked)
                        localCacheHandler.run {
                            putBoolean(FAVNUM_PERMISSION_CHECKER_IS_DENIED, true)
                            applyEditor()
                        }
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        permissionCheckerHelper.onNeverAskAgain(requireContext(), permissionText)
                    }

                    override fun onPermissionGranted() {
                        navigateSavedNumber(clientNumber, dgCategoryIds, categoryName, isSwitchChecked)
                    }
                }
            )
        } else {
            navigateSavedNumber(clientNumber, dgCategoryIds, categoryName, isSwitchChecked)
        }
    }

    private fun navigateSavedNumber(
        clientNumber: String,
        dgCategoryIds: ArrayList<String>,
        categoryName: String,
        isSwitchChecked: Boolean = false
    ) {
        context?.let {
            val intent = TopupBillsSavedNumberActivity.createInstance(
                it, clientNumber, mutableListOf(), dgCategoryIds, categoryName, viewModel.operatorData, isSwitchChecked
            )

            val requestCode = REQUEST_CODE_DIGITAL_SAVED_NUMBER
            startActivityForResult(intent, requestCode)
        }
    }

    private fun showErrorToaster(throwable: Throwable) {
        val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
            requireContext(),
            throwable,
            ErrorHandler.Builder()
                .className(this::class.java.simpleName)
                .build()
        )
        view?.run {
            Toaster.build(
                this,
                errorMessage.orEmpty(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun addToCart(){
        viewModel.addToCart(
            viewModel.digitalCheckoutPassData,
            DeviceUtil.getDigitalIdentifierParam(requireActivity()),
            DigitalSubscriptionParams(),
            userSession.userId
        )
    }

    /**
     * Input Field Listener
     * */
    override fun onRenderOperator(isDelayed: Boolean) {
        viewModel.operatorData.rechargeCatalogPrefixSelect.prefixes.isEmpty().let {
            if (it) {
                getPrefixOperatorData()
            } else {
                renderProduct()
            }
        }
    }

    override fun onClearInput() {
        operator = TelcoOperator()
        showEmptyState()
        digitalPDPTelcoAnalytics.eventClearInputNumber(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            userSession.userId
        )
    }

    override fun onClickNavigationIcon() {
        binding?.run {
            val clientNumber = rechargePdpPaketDataClientNumberWidget.getInputNumber()
            val dgCategoryIds = arrayListOf(
                TelcoCategoryType.CATEGORY_PULSA.toString(),
                TelcoCategoryType.CATEGORY_PAKET_DATA.toString(),
                TelcoCategoryType.CATEGORY_ROAMING.toString()
            )
            navigateToContact(
                clientNumber, dgCategoryIds,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                false
            )
            digitalPDPTelcoAnalytics.clickOnContactIcon(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                userSession.userId
            )
        }
    }

    override fun isKeyboardShown(): Boolean {
        context?.let {
            val inputMethodManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.isAcceptingText
        }
        return false
    }

    /**
     * Filter Chip Listener
     * */
    override fun onShowFilterChip(isLabeled: Boolean) {
        if (isLabeled) {
            digitalPDPTelcoAnalytics.impressionFavoriteContactChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                loyaltyStatus,
                userSession.userId
            )
        } else {
            digitalPDPTelcoAnalytics.impressionFavoriteNumberChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                loyaltyStatus,
                userSession.userId
            )
        }
    }

    override fun onClickFilterChip(isLabeled: Boolean) {
        inputNumberActionType = InputNumberActionType.CHIP
        if (isLabeled) {
            onHideBuyWidget()
            digitalPDPTelcoAnalytics.clickFavoriteContactChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
            )
        } else {
            digitalPDPTelcoAnalytics.clickFavoriteNumberChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        }
    }

    override fun onClickIcon(isSwitchChecked: Boolean) {
        binding?.run {
            val clientNumber = rechargePdpPaketDataClientNumberWidget.getInputNumber()
            val dgCategoryIds = arrayListOf(
                TelcoCategoryType.CATEGORY_PULSA.toString(),
                TelcoCategoryType.CATEGORY_PAKET_DATA.toString(),
                TelcoCategoryType.CATEGORY_ROAMING.toString()
            )
            navigateToContact(
                clientNumber, dgCategoryIds,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                isSwitchChecked
            )
        }
    }

    /**
     * Auto Complete Listener
     * */
    override fun onClickAutoComplete(isFavoriteContact: Boolean) {
        inputNumberActionType = InputNumberActionType.AUTOCOMPLETE
        if (isFavoriteContact) {
            digitalPDPTelcoAnalytics.clickFavoriteContactAutoComplete(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        } else {
            digitalPDPTelcoAnalytics.clickFavoriteNumberAutoComplete(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        }
    }

    /**
     * Buy Widget Listener
     * */
    override fun onClickedButtonLanjutkan(denom: DenomData) {
        viewModel.updateCheckoutPassData(
            denom, userSession.userId.generateRechargeCheckoutToken(),
            binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?:"",
            operator.id
        )
        if (userSession.isLoggedIn) {
            addToCart()
        } else {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
    }

    override fun onClickedChevron(denom: DenomData) {
        digitalPDPTelcoAnalytics.clickChevronBuyWidget(
            DigitalPDPCategoryUtil.getCategoryName(denom.categoryId.toInt()),
            operator.attributes.name,
            denom.price,
            denom.slashPrice,
            userSession.userId
        )
        fragmentManager?.let {
            SummaryTelcoBottomSheet(getString(R.string.summary_transaction), denom).show(it, "")
        }
    }

    /**
     * Recommendation Card Listener
     * */

    override fun onProductRecommendationCardClicked(recommendation: RecommendationCardWidgetModel, position: Int) {
        digitalPDPTelcoAnalytics.clickLastTransactionIcon(
            getString(R.string.digital_pdp_recommendation_title),
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            operator.attributes.name,
            loyaltyStatus,
            userSession.userId,
            recommendation,
            position
        )
        context?.let {
            RouteManager.route(it, recommendation.appUrl)
        }
    }

    override fun onProductRecommendationCardImpression(recommendation: RecommendationCardWidgetModel, position: Int) {
        digitalPDPTelcoAnalytics.impressionLastTransactionIcon(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            operator.attributes.name,
            loyaltyStatus,
            userSession.userId,
            recommendation,
            position
        )
    }

    /**
     * Denom Full Listener
     * */

    override fun onDenomFullClicked(denomFull: DenomData, layoutType: DenomWidgetEnum, position: Int,
                                    productListTitle: String,
                                    isShowBuyWidget: Boolean
    ) {
        if (layoutType == DenomWidgetEnum.MCCM_FULL_TYPE || layoutType == DenomWidgetEnum.FLASH_FULL_TYPE){
            onClearSelectedDenomFull()
            digitalPDPTelcoAnalytics.clickMCCMProduct(
                productListTitle,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.FULL_TYPE){
            digitalPDPTelcoAnalytics.clickProductCluster(
                productListTitle,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                position
            )
            onClearSelectedMCCM()
        }

        viewModel.selectedFullProduct = SelectedProduct(denomFull, layoutType, position)

        if (isShowBuyWidget && viewModel.isEligibleToBuy) {
            onShowBuyWidget(denomFull)
        } else {
            viewModel.onResetSelectedProduct()
            onHideBuyWidget()
        }
    }

    override fun onDenomFullImpression(
        denomFull: DenomData,
        layoutType: DenomWidgetEnum,
        position: Int
    ) {
        if (layoutType == DenomWidgetEnum.MCCM_FULL_TYPE || layoutType == DenomWidgetEnum.FLASH_FULL_TYPE){
            digitalPDPTelcoAnalytics.impressionProductMCCM(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.FULL_TYPE){
            digitalPDPTelcoAnalytics.impressionProductCluster(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                position
            )
        }
    }

    override fun onChevronDenomClicked(denomFull: DenomData, position: Int, layoutType: DenomWidgetEnum) {
        fragmentManager?.let {
            ProductDescBottomSheet(denomFull, this).show(it, "")
        }
        if (layoutType == DenomWidgetEnum.FULL_TYPE) {
            digitalPDPTelcoAnalytics.clickFullDenomChevron(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
            )
        } else {
            digitalPDPTelcoAnalytics.clickPromoFullDenomChevron(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                layoutType
            )
        }
    }

    /** FilterBottomSheetListener */

    override fun onClickSaveFilter(filterTagComponents: List<TelcoFilterTagComponent>, initialSelectedCounter: Int) {
        viewModel.updateFilterData(filterTagComponents)
        onSuccessSortFilter(initialSelectedCounter)
        viewModel.getRechargeCatalogInputMultiTab(menuId, operator.id, binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?: "", false)
    }

    override fun onChipClicked(chipName: String) {
        digitalPDPTelcoAnalytics.clickFilterChip(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            operator.attributes.name,
            chipName,
            userSession.userId,
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        context?.run {
            permissionCheckerHelper.onRequestPermissionsResult(
                this,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_DIGITAL_SAVED_NUMBER) {
                if (data != null) {
                    val orderClientNumber =
                        data.getParcelableExtra<Parcelable>(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER) as TopupBillsSavedNumber

                    handleCallbackSavedNumber(
                        orderClientNumber.clientName,
                        orderClientNumber.clientNumber,
                        orderClientNumber.productId,
                        orderClientNumber.categoryId,
                        orderClientNumber.inputNumberActionTypeIndex
                    )
                } else {
                    handleCallbackAnySavedNumberCancel()
                }
                getFavoriteNumber()
            } else if (requestCode == REQUEST_CODE_LOGIN ) {
                addToCart()
            }
        }
    }

    companion object {
        fun newInstance(telcoExtraParam: TopupBillsExtraParam) = DigitalPDPDataPlanFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(DigitalPDPConstant.EXTRA_PARAM, telcoExtraParam)
            it.arguments = bundle
        }
    }
}