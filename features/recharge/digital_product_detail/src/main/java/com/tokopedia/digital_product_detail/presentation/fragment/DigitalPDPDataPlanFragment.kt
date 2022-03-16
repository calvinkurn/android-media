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
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.favorite.view.activity.TopupBillsPersoSavedNumberActivity
import com.tokopedia.common.topupbills.favorite.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.utils.CommonTopupBillsUtil
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment.Companion.REQUEST_CODE_CART_DIGITAL
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
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
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_LOGIN_ALT
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpDataPlanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.bottomsheet.FilterPDPBottomsheet
import com.tokopedia.digital_product_detail.presentation.bottomsheet.ProductDescBottomSheet
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryTelcoBottomSheet
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPAnalytics
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.utils.DigitalKeyboardWatcher
import com.tokopedia.digital_product_detail.presentation.utils.setupDynamicScrollListener
import com.tokopedia.digital_product_detail.presentation.utils.toggle
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.InputFieldType
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
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
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

/**
 * @author by firmanda on 04/01/21
 */

class DigitalPDPDataPlanFragment :
    BaseDaggerFragment(),
    RechargeBuyWidgetListener,
    RechargeRecommendationCardListener,
    RechargeDenomFullListener,
    ClientNumberInputFieldListener,
    ClientNumberFilterChipListener,
    ClientNumberAutoCompleteListener,
    FilterPDPBottomsheet.FilterBottomSheetListener,
    DigitalHistoryIconListener {
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPDataPlanViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPAnalytics: DigitalPDPAnalytics

    private val keyboardWatcher = DigitalKeyboardWatcher()

    private var binding by autoClearedNullable<FragmentDigitalPdpDataPlanBinding>()

    private var operator = TelcoOperator()
    private var loyaltyStatus = ""
    private var clientNumber = ""
    private var productId = 0
    private var productIdFromApplink = 0
    private var menuId = 0
    private var categoryId = TelcoCategoryType.CATEGORY_PAKET_DATA
    private var inputNumberActionType = InputNumberActionType.MANUAL
    private var actionTypeTrackingJob: Job? = null

    private lateinit var localCacheHandler: LocalCacheHandler

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
        setupKeyboardWatcher()
        setupDynamicScrollListener()
        initClientNumberWidget()
        observeData()
        getCatalogMenuDetail()
    }

    private fun setupKeyboardWatcher() {
        binding?.root?.let {
            keyboardWatcher.listen(it, object : DigitalKeyboardWatcher.Listener {
                override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                    // do nothing
                }

                override fun onKeyboardHidden() {
                    binding?.rechargePdpPaketDataClientNumberWidget?.setClearable()
                }
            })
        }
    }


    private fun setupDynamicScrollListener() {
        binding?.run {
            rechargePdpPaketDataSvContainer.setupDynamicScrollListener(
                { !viewModel.isEligibleToBuy },
                { rechargePdpPaketDataClientNumberWidget.getInputNumber().isEmpty() },
                { viewModel.runThrottleJob { onCollapseAppBar() }},
                { viewModel.runThrottleJob { onExpandAppBar() }}
            )
        }
    }

    private fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(DigitalPDPConstant.EXTRA_PARAM)
                ?: TopupBillsExtraParam()
            clientNumber = digitalTelcoExtraParam.clientNumber
            productIdFromApplink = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
            if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                categoryId = digitalTelcoExtraParam.categoryId.toInt()
            }
            if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                menuId = digitalTelcoExtraParam.menuId.toIntOrNull() ?: 0
            }
        }
        if (!clientNumber.isNullOrEmpty()) {
            binding?.rechargePdpPaketDataClientNumberWidget?.run {
                inputNumberActionType = InputNumberActionType.NOTHING
                setInputNumber(clientNumber, true)
            }
        }

        digitalPDPAnalytics.openScreenPDPPage(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            userSession.userId,
            userSession.isLoggedIn
        )

        digitalPDPAnalytics.viewPDPPage(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            userSession.userId
        )
    }

    private fun renderProduct() {
        binding?.run {
            val selectedClientNumber = rechargePdpPaketDataClientNumberWidget.getInputNumber()
            try {
                /* operator check */
                val selectedOperator =
                    viewModel.operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                        selectedClientNumber.startsWith(it.value)
                    }

                /* validate client number */
                viewModel.run {
                    cancelValidatorJob()
                    validateClientNumber(selectedClientNumber)
                }
                hitTrackingForInputNumber(
                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                    selectedOperator.operator.attributes.name
                )

                val isOperatorChanged = operator.id != selectedOperator.operator.id
                val productIdFromDefaultPrefix =
                        selectedOperator.operator.attributes.defaultProductId.toIntOrZero()

                /* set default product id when prefix changed */
                if (isOperatorChanged && operator.id.isEmpty() && productIdFromApplink.isMoreThanZero()) {
                    productId = productIdFromApplink
                } else if (isOperatorChanged && productIdFromDefaultPrefix.isMoreThanZero()) {
                    productId = productIdFromDefaultPrefix
                }

                if (isOperatorChanged || selectedClientNumber
                        .length in MINIMUM_VALID_NUMBER_LENGTH..MAXIMUM_VALID_NUMBER_LENGTH
                ) {
                    operator = selectedOperator.operator
                    rechargePdpPaketDataClientNumberWidget.run {
                        showOperatorIcon(selectedOperator.operator.attributes.imageUrl)
                    }
                    hideEmptyState()
                    onHideBuyWidget()
                    getRecommendations()
                    getCatalogProductInputMultiTab(
                            selectedOperator.key, isOperatorChanged,
                        selectedClientNumber
                        )
                } else {
                    onHideBuyWidget()
                }
            } catch (exception: NoSuchElementException) {
                operator = TelcoOperator()
                viewModel.run {
                    cancelRecommendationJob()
                    cancelCatalogProductJob()
                }
                rechargePdpPaketDataClientNumberWidget.run {
                    setLoading(false)
                    resetContactName()
                    if (selectedClientNumber.length >= MINIMUM_OPERATOR_PREFIX) {
                        setErrorInputField(
                            getString(com.tokopedia.recharge_component.R.string.client_number_prefix_error),
                            true
                        )
                    }
                }
                showEmptyState()
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

        viewModel.autoCompleteData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetAutoComplete(it.data)
            }
        })

        viewModel.catalogPrefixSelect.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetPrefixOperator()
                is RechargeNetworkResult.Fail -> onFailedGetPrefixOperator(it.error)
            }
        })

        viewModel.recommendationData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetRecommendations(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetRecommendations()
                is RechargeNetworkResult.Loading -> onShimmeringRecommendation()
            }
        })

        viewModel.observableDenomMCCMData.observe(viewLifecycleOwner, { denomData ->
            when (denomData) {
                is RechargeNetworkResult.Success -> {

                    if (productId >= 0) {
                        viewModel.setAutoSelectedDenom(
                            denomData.data.denomFull.listDenomData,
                            productId.toString()
                        )
                    }
                    val selectedPositionDenom =
                        viewModel.getSelectedPositionId(denomData.data.denomFull.listDenomData)
                    val selectedPositionMCCM =
                        viewModel.getSelectedPositionId(denomData.data.denomMCCMFull.listDenomData)

                    if (denomData.data.isFilterRefreshed) {
                        digitalPDPAnalytics.impressionFilterChip(
                            DigitalPDPCategoryUtil.getCategoryName(categoryId),
                            operator.attributes.name,
                            userSession.userId,
                        )
                        onSuccessSortFilter()
                    }
                    onSuccessDenomFull(denomData.data.denomFull, selectedPositionDenom)
                    onSuccessMCCM(denomData.data.denomMCCMFull, selectedPositionMCCM)

                    if (viewModel.isEmptyDenomMCCM(denomData.data.denomFull.listDenomData,
                            denomData.data.denomMCCMFull.listDenomData)){
                        showGlobalErrorState()
                    } else {
                        hideGlobalErrorState()
                    }

                    if (selectedPositionDenom == null && selectedPositionMCCM == null) {
                        onHideBuyWidget()
                    }
                }

                is RechargeNetworkResult.Fail -> {
                    onFailedDenomFull()
                    onLoadingAndFailMCCM()
                }

                is RechargeNetworkResult.Loading -> {
                    hideGlobalErrorState()
                    hideEmptyState()
                    onShimmeringDenomFull()
                    onLoadingAndFailMCCM()
                }
            }
        })

        viewModel.addToCartResult.observe(viewLifecycleOwner, { atcData ->
            when (atcData) {
                is RechargeNetworkResult.Success -> {
                    onLoadingBuyWidget(false)
                    digitalPDPAnalytics.addToCart(
                        categoryId.toString(),
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        operator.attributes.name,
                        userSession.userId,
                        atcData.data.cartId,
                        viewModel.digitalCheckoutPassData.productId.toString(),
                        operator.attributes.name,
                        atcData.data.priceProduct
                    )
                    navigateToCart(atcData.data.categoryId)
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
        isOperatorChanged: Boolean,
        clientNumber: String
    ) {
        viewModel.run {
            if(isOperatorChanged) resetFilter()
            cancelCatalogProductJob()
            setRechargeCatalogInputMultiTabLoading()
            getRechargeCatalogInputMultiTab(
                menuId,
                selectedOperatorKey,
                clientNumber,
                isOperatorChanged
            )
        }
    }

    private fun getRecommendations() {
        val clientNumbers = listOf(binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?: "")
        viewModel.setRecommendationLoading()
        viewModel.cancelRecommendationJob()
        viewModel.getRecommendations(clientNumbers, listOf(categoryId))
    }

    private fun getCatalogMenuDetail() {
        viewModel.run {
            setMenuDetailLoading()
            getMenuDetail(menuId)
        }
    }

    private fun getPrefixOperatorData() {
        viewModel.run {
            setPrefixOperatorLoading()
            getPrefixOperator(menuId)
        }
    }

    private fun getFavoriteNumber() {
        viewModel.run {
            setFavoriteNumberLoading()
            getFavoriteNumber(
                listOf(
                    TelcoCategoryType.CATEGORY_PULSA,
                    TelcoCategoryType.CATEGORY_PAKET_DATA,
                    TelcoCategoryType.CATEGORY_ROAMING
                )
            )
        }
    }

    private fun getAutoComplete() {
        viewModel.run {
            setAutoCompleteLoading()
            getAutoComplete(
                listOf(
                    TelcoCategoryType.CATEGORY_PULSA,
                    TelcoCategoryType.CATEGORY_PAKET_DATA,
                    TelcoCategoryType.CATEGORY_ROAMING
                )
            )
        }
    }

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        getAutoComplete()
        getFavoriteNumber()
        initEmptyState(data.banners)
        renderPrefill(data.userPerso)
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

                val extendedPadding = getDimens(unifyDimens.layout_lvl8)
                binding?.rechargePdpPaketDataSvContainer?.setPadding(0, extendedPadding, 0, 0)
            }
        }
    }

    private fun onSuccessGetAutoComplete(autoComplete: List<TopupBillsPersoFavNumberItem>) {
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            if (autoComplete.isNotEmpty()) {
                setAutoCompleteList(autoComplete)
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

    private fun onSuccessGetRecommendations(recommendations: RecommendationWidgetModel) {
        renderRecommendation(recommendations)
    }

    private fun onFailedGetRecommendations() {
        binding?.rechargePdpPaketDataRecommendationWidget?.renderFailRecommendation()
    }

    private fun onSuccessSortFilter(initialSelectedCounter: Int = 0) {
        binding?.let {
            if (!viewModel.filterData.isNullOrEmpty()) {
                it.sortFilterPaketData.run {
                    show()
                    val filterItems = arrayListOf<SortFilterItem>()
                    val chipItems = viewModel.filterData.first().filterTagDataCollections
                    chipItems.forEach {
                        val item = SortFilterItem(it.value)
                        filterItems.add(item)
                    }

                    var selectedChipsCounter = initialSelectedCounter

                    filterItems.forEachIndexed { index, sortFilterItem ->
                        if (chipItems.get(index).isSelected) {
                            sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                        }

                        sortFilterItem.listener = {
                            sortFilterItem.toggle()
                            if (!chipItems.get(index).isSelected) {
                                digitalPDPAnalytics.clickFilterChip(
                                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                                    operator.attributes.name,
                                    sortFilterItem.title.toString(),
                                    userSession.userId,
                                )
                            }
                            if (filterItems[index].type == ChipsUnify.TYPE_SELECTED) {
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
                        digitalPDPAnalytics.clickFilterChip(
                            DigitalPDPCategoryUtil.getCategoryName(categoryId),
                            operator.attributes.name,
                            getString(R.string.bottom_sheet_filter_title),
                            userSession.userId,
                        )
                        fragmentManager?.let {
                            FilterPDPBottomsheet(
                                getString(R.string.bottom_sheet_filter_title),
                                getString(R.string.bottom_sheet_filter_reset),
                                filterData, this@DigitalPDPDataPlanFragment
                            ).show(it, "")
                        }
                    }

                    indicatorCounter = selectedChipsCounter
                }
            }
        }
    }

    private fun onChipClicked() {
        viewModel.run {
            updateFilterData()
            cancelCatalogProductJob()
            setRechargeCatalogInputMultiTabLoading()
            getRechargeCatalogInputMultiTab(
                menuId,
                operator.id,
                binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?: "",
                false
            )
        }
    }

    private fun onSuccessDenomFull(denomData: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.FULL_TYPE)) {
                onShowBuyWidget(viewModel.selectedFullProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            it.rechargePdpPaketDataDenomFullWidget.renderDenomFullLayout(
                this,
                denomData,
                selectedInitialPosition
            )
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
            it.rechargePdpPaketDataDenomFullWidget.run {
                show()
                renderDenomFullShimmering()
            }
        }
    }

    private fun onClearSelectedDenomFull() {
        binding?.let {
            it.rechargePdpPaketDataDenomFullWidget.clearSelectedProduct()
        }
    }

    private fun onSuccessMCCM(denomFull: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_FULL_TYPE)) {
                onShowBuyWidget(viewModel.selectedFullProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            it.rechargePdpPaketDataPromoWidget.show()
            it.rechargePdpPaketDataPromoWidget.renderMCCMFull(
                this, denomFull,
                getString(com.tokopedia.unifyprinciples.R.color.Unify_N0), selectedInitialPosition
            )
        }
    }

    private fun onLoadingAndFailMCCM() {
        binding?.let {
            it.rechargePdpPaketDataPromoWidget.renderFailMCCMFull()
        }
    }

    private fun onClearSelectedMCCM() {
        binding?.let {
            it.rechargePdpPaketDataPromoWidget.clearSelectedProduct()
        }
    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            setCustomInputNumberFormatter { inputNumber ->
                CommonTopupBillsUtil.formatPrefixClientNumber(inputNumber)
            }
            setInputFieldStaticLabel(
                getString(
                    com.tokopedia.recharge_component.R.string.label_recharge_client_number_telco
                )
            )
            setInputFieldType(InputFieldType.Telco)
            setListener(
                this@DigitalPDPDataPlanFragment,
                this@DigitalPDPDataPlanFragment,
                this@DigitalPDPDataPlanFragment
            )
        }
    }

    private fun renderPrefill(data: TopupBillsUserPerso) {
        inputNumberActionType = InputNumberActionType.NOTHING
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            if (clientNumber.isNotEmpty()) {
                setInputNumber(clientNumber, true)
            } else {
                setContactName(data.clientName)
                setInputNumber(data.prefill, true)
            }
        }
    }

    private fun renderTicker(tickers: List<TopupBillsTicker>) {
        if (tickers.isNotEmpty()) {
            val messages = ArrayList<TickerData>()
            for (item in tickers) {
                messages.add(
                    TickerData(
                        item.name, item.content,
                        when (item.type) {
                            TopupBillsTicker.TYPE_WARNING -> Ticker.TYPE_WARNING
                            TopupBillsTicker.TYPE_INFO -> Ticker.TYPE_INFORMATION
                            TopupBillsTicker.TYPE_SUCCESS -> Ticker.TYPE_ANNOUNCEMENT
                            TopupBillsTicker.TYPE_ERROR -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }
                    )
                )
            }
            binding?.rechargePdpPaketDataTicker?.run {
                addPagerView(
                    TickerPagerAdapter(
                        this@DigitalPDPDataPlanFragment.requireContext(), messages
                    ), messages
                )
                show()
            }
        } else {
            binding?.rechargePdpPaketDataTicker?.hide()
        }
    }

    private fun renderRecommendation(data: RecommendationWidgetModel) {
        binding?.let {
            it.rechargePdpPaketDataRecommendationWidget.show()
            it.rechargePdpPaketDataRecommendationWidget.renderRecommendationLayout(
                this,
                data.title,
                data.recommendations
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

    private fun initEmptyState(banners: List<TopupBillsBanner>) {
        binding?.rechargePdpPaketDataEmptyStateWidget?.imageUrl = banners.firstOrNull()?.imageUrl ?: ""
    }

    private fun showEmptyState() {
        binding?.run {
            if (!rechargePdpPaketDataEmptyStateWidget.isVisible) {

                /** hide empty state when imageUrl is empty*/
                if (rechargePdpPaketDataEmptyStateWidget.imageUrl.isNotEmpty()) {
                    rechargePdpPaketDataEmptyStateWidget.show()
                    digitalPDPAnalytics.impressionBannerEmptyState(
                        rechargePdpPaketDataEmptyStateWidget.imageUrl,
                        categoryId.toString(),
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        loyaltyStatus,
                        userSession.userId
                    )
                } else {
                    rechargePdpPaketDataEmptyStateWidget.hide()
                }

                sortFilterPaketData.hide()
                rechargePdpPaketDataPromoWidget.hide()
                rechargePdpPaketDataRecommendationWidget.hide()
                rechargePdpPaketDataDenomFullWidget.hide()
                globalErrorPaketData.hide()
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

    private fun showGlobalErrorState() {
        binding?.globalErrorPaketData?.run {
            show()
            errorTitle.text = getString(R.string.empty_state_paket_data_title)
            errorDescription.text = getString(R.string.empty_state_paket_data_desc)
            errorAction.hide()
        }
    }

    private fun hideGlobalErrorState() {
        binding?.globalErrorPaketData?.run {
            hide()
        }
    }

    private fun hitTrackingForInputNumber(categoryName: String, operatorName: String) {
        actionTypeTrackingJob?.cancel()
        actionTypeTrackingJob = lifecycleScope.launch {
            delay(INPUT_ACTION_TRACKING_DELAY)
            when (inputNumberActionType) {
                InputNumberActionType.MANUAL -> {
                    digitalPDPAnalytics.eventInputNumberManual(
                        categoryName,
                        operatorName,
                        userSession.userId
                    )
                }
                InputNumberActionType.CONTACT -> {
                    digitalPDPAnalytics.eventInputNumberContact(
                        categoryName,
                        operatorName,
                        userSession.userId
                    )
                }
                InputNumberActionType.FAVORITE -> {
                    digitalPDPAnalytics.eventInputNumberFavorite(
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

    private fun onCollapseAppBar() {
        binding?.rechargePdpPaketDataClientNumberWidget?.setVisibleSimplifiedLayout(true)
    }

    private fun onExpandAppBar() {
        binding?.rechargePdpPaketDataClientNumberWidget?.setVisibleSimplifiedLayout(false)
    }

    private fun navigateToCart(categoryId: String) {
        context?.let { context ->
            val intent =
                RouteManager.getIntent(context, ApplinkConsInternalDigital.CHECKOUT_DIGITAL)
            viewModel.updateCategoryCheckoutPassData(categoryId)
            intent.putExtra(
                DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA,
                viewModel.digitalCheckoutPassData
            )
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
                        navigateSavedNumber(
                            clientNumber,
                            dgCategoryIds,
                            categoryName,
                            isSwitchChecked
                        )
                        localCacheHandler.run {
                            putBoolean(FAVNUM_PERMISSION_CHECKER_IS_DENIED, true)
                            applyEditor()
                        }
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        permissionCheckerHelper.onNeverAskAgain(requireContext(), permissionText)
                    }

                    override fun onPermissionGranted() {
                        navigateSavedNumber(
                            clientNumber,
                            dgCategoryIds,
                            categoryName,
                            isSwitchChecked
                        )
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
            val intent = TopupBillsPersoSavedNumberActivity.createInstance(
                it,
                clientNumber,
                dgCategoryIds,
                arrayListOf(),
                categoryName,
                isSwitchChecked,
                loyaltyStatus
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

    private fun addToCart() {
        viewModel.run {
            setAddToCartLoading()
            addToCart(
                DeviceUtil.getDigitalIdentifierParam(requireActivity()),
                DigitalSubscriptionParams(),
                userSession.userId
            )
        }
    }

    private fun addToCartFromUrl() {
        context?.let { RouteManager.route(it, viewModel.recomCheckoutUrl) }
    }

    private fun navigateToLoginPage(requestCode: Int = REQUEST_CODE_LOGIN) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, requestCode)
    }

    //region ClientNumberInputFieldListener
    override fun onRenderOperator(isDelayed: Boolean, isManualInput: Boolean) {
        viewModel.operatorData.rechargeCatalogPrefixSelect.prefixes.isEmpty().let {
            if (it) {
                getPrefixOperatorData()
            } else {
                if (isManualInput) inputNumberActionType = InputNumberActionType.MANUAL
                renderProduct()
            }
        }
    }

    override fun onClearInput() {
        operator = TelcoOperator()
        showEmptyState()
        digitalPDPAnalytics.eventClearInputNumber(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            userSession.userId
        )
        onHideBuyWidget()
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
            digitalPDPAnalytics.clickOnContactIcon(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                userSession.userId
            )
        }
    }

    override fun isKeyboardShown(): Boolean {
        context?.let {
            val inputMethodManager =
                it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputMethodManager.isAcceptingText
        }
        return false
    }
    //endregion

    //region ClientNumberFilterChipListener
    override fun onShowFilterChip(isLabeled: Boolean) {
        if (isLabeled) {
            digitalPDPAnalytics.impressionFavoriteContactChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                loyaltyStatus,
                userSession.userId
            )
        } else {
            digitalPDPAnalytics.impressionFavoriteNumberChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                loyaltyStatus,
                userSession.userId
            )
        }
    }

    override fun onClickFilterChip(isLabeled: Boolean, operatorId: String) {
        inputNumberActionType = InputNumberActionType.CHIP
        if (isLabeled) {
            onHideBuyWidget()
            digitalPDPAnalytics.clickFavoriteContactChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
            )
        } else {
            digitalPDPAnalytics.clickFavoriteNumberChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        }
    }

    override fun onClickIcon(isSwitchChecked: Boolean) {
        binding?.run {
            digitalPDPAnalytics.clickListFavoriteNumber(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                userSession.userId
            )
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
    //endregion

    //region ClientNumberAutoCompleteListener
    override fun onClickAutoComplete(isFavoriteContact: Boolean) {
        inputNumberActionType = InputNumberActionType.AUTOCOMPLETE
        if (isFavoriteContact) {
            digitalPDPAnalytics.clickFavoriteContactAutoComplete(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        } else {
            digitalPDPAnalytics.clickFavoriteNumberAutoComplete(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        }
    }
    //endregion

    //region RechargeBuyWidgetListener
    override fun onClickedButtonLanjutkan(denom: DenomData) {
        viewModel.updateCheckoutPassData(
            denom, userSession.userId.generateRechargeCheckoutToken(),
            binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?: "",
            operator.id
        )
        if (userSession.isLoggedIn) {
            addToCart()
        } else {
            navigateToLoginPage()
        }
    }

    override fun onClickedChevron(denom: DenomData) {
        digitalPDPAnalytics.clickChevronBuyWidget(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            operator.attributes.name,
            denom.price,
            denom.slashPrice,
            userSession.userId
        )
        fragmentManager?.let {
            SummaryTelcoBottomSheet(getString(R.string.summary_transaction), denom).show(it, "")
        }
    }
    //endregion

    //region RechargeRecommendationCardListener
    override fun onProductRecommendationCardClicked(
        title: String,
        recommendation: RecommendationCardWidgetModel,
        position: Int
    ) {
        digitalPDPAnalytics.clickLastTransactionIcon(
            title,
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            operator.attributes.name,
            loyaltyStatus,
            userSession.userId,
            recommendation,
            position
        )
        viewModel.recomCheckoutUrl = recommendation.appUrl
        if (userSession.isLoggedIn) {
            addToCartFromUrl()
        } else {
            navigateToLoginPage(REQUEST_CODE_LOGIN_ALT)
        }
    }

    override fun onProductRecommendationCardImpression(
        recommendation: RecommendationCardWidgetModel,
        position: Int
    ) {
        digitalPDPAnalytics.impressionLastTransactionIcon(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            operator.attributes.name,
            loyaltyStatus,
            userSession.userId,
            recommendation,
            position
        )
    }
    //endregion

    //region RechargeDenomFullListener
    override fun onDenomFullClicked(
        denomFull: DenomData, layoutType: DenomWidgetEnum, position: Int,
        productListTitle: String,
        isShowBuyWidget: Boolean
    ) {
        if (layoutType == DenomWidgetEnum.MCCM_FULL_TYPE || layoutType == DenomWidgetEnum.FLASH_FULL_TYPE) {
            onClearSelectedDenomFull()
            digitalPDPAnalytics.clickMCCMProduct(
                productListTitle,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.FULL_TYPE) {
            digitalPDPAnalytics.clickProductCluster(
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
        listDenomFull: List<DenomData>,
        layoutType: DenomWidgetEnum,
    ) {
        if (layoutType == DenomWidgetEnum.MCCM_FULL_TYPE || layoutType == DenomWidgetEnum.FLASH_FULL_TYPE) {
            digitalPDPAnalytics.impressionProductMCCM(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                listDenomFull,
                layoutType,
            )
        } else if (layoutType == DenomWidgetEnum.FULL_TYPE) {
            digitalPDPAnalytics.impressionProductCluster(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                listDenomFull,
            )
        }
    }

    override fun onChevronDenomClicked(
        denomFull: DenomData,
        position: Int,
        layoutType: DenomWidgetEnum
    ) {
        fragmentManager?.let {
            ProductDescBottomSheet(denomFull, this).show(it, "")
        }
        if (layoutType == DenomWidgetEnum.FULL_TYPE) {
            digitalPDPAnalytics.clickFullDenomChevron(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
            )
        } else {
            digitalPDPAnalytics.clickPromoFullDenomChevron(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                layoutType
            )
        }
    }
    //endregion

    //region FilterBottomSheetListener
    override fun onClickSaveFilter(
        filterTagComponents: List<TelcoFilterTagComponent>,
        initialSelectedCounter: Int
    ) {
        if (viewModel.isFilterChanged(filterTagComponents)) {
            viewModel.updateFilterData(filterTagComponents)
            onSuccessSortFilter(initialSelectedCounter)
            viewModel.run {
                cancelCatalogProductJob()
                setRechargeCatalogInputMultiTabLoading()
                getRechargeCatalogInputMultiTab(
                    menuId,
                    operator.id,
                    binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?: "",
                    false
                )
            }
        }
    }

    override fun onChipClicked(chipName: String) {
        digitalPDPAnalytics.clickFilterChip(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            operator.attributes.name,
            chipName,
            userSession.userId,
        )
    }
    //endregion

    //region DigitalHistoryIconListener
    override fun onClickDigitalIconHistory() {
        digitalPDPAnalytics.clickTransactionHistoryIcon(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            loyaltyStatus,
            userSession.userId
        )
    }
    //endregion

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
                        data.getParcelableExtra<Parcelable>(EXTRA_CALLBACK_CLIENT_NUMBER) as TopupBillsSavedNumber

                    handleCallbackSavedNumber(
                        orderClientNumber.clientName,
                        orderClientNumber.clientNumber,
                        orderClientNumber.inputNumberActionTypeIndex
                    )
                } else {
                    handleCallbackAnySavedNumberCancel()
                }
                getAutoComplete()
                getFavoriteNumber()
            } else if (requestCode == REQUEST_CODE_LOGIN) {
                addToCart()
            } else if (requestCode == REQUEST_CODE_LOGIN_ALT) {
                addToCartFromUrl()
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