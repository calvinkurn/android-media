package com.tokopedia.digital_product_detail.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberPageConfig
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common.topupbills.utils.CommonTopupBillsUtil
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment.Companion.REQUEST_CODE_CART_DIGITAL
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common_digital.atc.data.response.AtcErrorButton
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.FAVNUM_PERMISSION_CHECKER_IS_DENIED
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.FIXED_PADDING_ADJUSTMENT
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.INPUT_ACTION_TRACKING_DELAY
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.LOADER_DIALOG_TEXT
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MAXIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_OPERATOR_PREFIX
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_LOGIN
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_LOGIN_ALT
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_VERIFY_PHONE_NUMBER
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.TELCO_PREFERENCES_NAME
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpDataPlanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.bottomsheet.FilterPDPBottomsheet
import com.tokopedia.digital_product_detail.presentation.bottomsheet.ProductDescBottomSheet
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryTelcoBottomSheet
import com.tokopedia.digital_product_detail.presentation.delegate.DigitalKeyboardDelegate
import com.tokopedia.digital_product_detail.presentation.delegate.DigitalKeyboardDelegateImpl
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPAnalytics
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPWidgetMapper
import com.tokopedia.digital_product_detail.presentation.utils.setupDynamicScrollListener
import com.tokopedia.digital_product_detail.presentation.utils.toggle
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.model.client_number.InputFieldType
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
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
import java.lang.ref.WeakReference
import javax.inject.Inject

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
    DigitalHistoryIconListener,
    DigitalKeyboardDelegate by DigitalKeyboardDelegateImpl()
{
    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPDataPlanViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPAnalytics: DigitalPDPAnalytics

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
    private var loader: LoaderDialog? = null

    private lateinit var localCacheHandler: LocalCacheHandler
    private lateinit var productDescBottomSheet: ProductDescBottomSheet

    private val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

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
        setupDynamicScrollViewPadding()
        initClientNumberWidget()
        observeData()
        getCatalogMenuDetail()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        if (childFragment is ProductDescBottomSheet) {
            childFragment.setListener(this)
        } else if (childFragment is FilterPDPBottomsheet) {
            childFragment.setListener(this)
        }
    }

    private fun setupKeyboardWatcher() {
        binding?.root?.let {
            registerLifecycleOwner(viewLifecycleOwner)
            registerKeyboard(WeakReference(it))
        }
    }


    private fun setupDynamicScrollListener() {
        binding?.run {
            rechargePdpPaketDataSvContainer.setupDynamicScrollListener(
                { !viewModel.isEligibleToBuy },
                { rechargePdpPaketDataClientNumberWidget.getInputNumber().isEmpty() },
                { viewModel.runThrottleJob { onCollapseAppBar() } },
                { viewModel.runThrottleJob { onExpandAppBar() } }
            )
        }
    }

    private fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(DigitalPDPConstant.EXTRA_PARAM)
                ?: TopupBillsExtraParam()
            clientNumber = digitalTelcoExtraParam.clientNumber
            productIdFromApplink = digitalTelcoExtraParam.productId.toIntOrZero()
            if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                categoryId = digitalTelcoExtraParam.categoryId.toIntOrZero()
            }
            if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                menuId = digitalTelcoExtraParam.menuId.toIntOrZero()
            }
        }
        if (!clientNumber.isNullOrEmpty()) {
            binding?.rechargePdpPaketDataClientNumberWidget?.run {
                inputNumberActionType = InputNumberActionType.NOTHING
                setInputNumber(clientNumber)
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

        viewModel.favoriteChipsData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetFavoriteChips(it.data)
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

        viewModel.prefillData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetPrefill(it.data)
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

                    if (viewModel.isEmptyDenomMCCM(
                            denomData.data.denomFull.listDenomData,
                            denomData.data.denomMCCMFull.listDenomData
                        )
                    ) {
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
                    if (::productDescBottomSheet.isInitialized) productDescBottomSheet.dismiss()
                    hideLoadingDialog()
                    digitalPDPAnalytics.addToCart(
                        categoryId.toString(),
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        operator.attributes.name,
                        userSession.userId,
                        atcData.data.cartId,
                        viewModel.digitalCheckoutPassData.productId.toString(),
                        viewModel.selectedFullProduct.denomData.title,
                        atcData.data.priceProduct,
                        atcData.data.channelId,
                    )
                    navigateToCart(atcData.data.categoryId)
                }

                is RechargeNetworkResult.Fail -> {
                    hideLoadingDialog()
                    showErrorToaster(atcData.error)
                }

                is RechargeNetworkResult.Loading -> {
                    showLoadingDialog()
                }
            }
        })

        viewModel.errorAtc.observe(viewLifecycleOwner){
            hideLoadingDialog()
            if (it.atcErrorPage.isShowErrorPage){
                redirectToCart(viewModel.digitalCheckoutPassData.categoryId ?: "")
            } else{
                showErrorUnverifiedPhoneNumber(it)
            }
        }

        viewModel.clientNumberValidatorMsg.observe(viewLifecycleOwner, { msg ->
            binding?.rechargePdpPaketDataClientNumberWidget?.run {
                setLoading(false)
                showClearIcon()
                if (msg.isEmpty()) {
                    clearErrorState()
                } else {
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
            if (isOperatorChanged) resetFilter()
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
        val clientNumbers =
            listOf(binding?.rechargePdpPaketDataClientNumberWidget?.getInputNumber() ?: "")
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

    private fun getFavoriteNumbers(favoriteNumberTypes: List<FavoriteNumberType>) {
        viewModel.run {
            setFavoriteNumberLoading()
            getFavoriteNumbers(
                listOf(
                    TelcoCategoryType.CATEGORY_PULSA,
                    TelcoCategoryType.CATEGORY_PAKET_DATA,
                    TelcoCategoryType.CATEGORY_ROAMING
                ),
                favoriteNumberTypes
            )
        }
    }

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        getFavoriteNumbers(
            listOf(
                FavoriteNumberType.CHIP,
                FavoriteNumberType.LIST,
                FavoriteNumberType.PREFILL
            )
        )
        initEmptyState(data.banners)
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
    }

    private fun onSuccessGetFavoriteChips(favoriteChips: List<FavoriteChipModel>) {
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            setFilterChipShimmer(false, favoriteChips.isEmpty())
            if (favoriteChips.isNotEmpty()) {
                setFavoriteNumber(
                    DigitalPDPWidgetMapper.mapFavoriteChipsToWidgetModels(
                        favoriteChips
                    )
                )
                setupDynamicScrollViewPadding(FIXED_PADDING_ADJUSTMENT)
            } else setupDynamicScrollViewPadding()
        }
    }

    private fun onSuccessGetAutoComplete(autoComplete: List<AutoCompleteModel>) {
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            if (autoComplete.isNotEmpty()) {
                setAutoCompleteList(
                    DigitalPDPWidgetMapper.mapAutoCompletesToWidgetModels(
                        autoComplete
                    )
                )
            }
        }
    }

    private fun onSuccessGetPrefill(prefill: PrefillModel) {
        binding?.rechargePdpPaketDataClientNumberWidget?.run {
            if (clientNumber.isNotEmpty()) {
                setInputNumber(clientNumber)
            } else {
                if (isInputFieldEmpty()) {
                    setContactName(prefill.clientName)
                    setInputNumber(prefill.clientNumber)
                    inputNumberActionType = InputNumberActionType.NOTHING
                }
            }
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
        binding?.rechargePdpPaketDataRecommendationWidget?.hide()
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
                        if (chipItems[index].isSelected) {
                            sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                        }

                        sortFilterItem.listener = {
                            sortFilterItem.toggle()
                            if (!chipItems[index].isSelected) {
                                digitalPDPAnalytics.clickFilterChip(
                                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                                    operator.attributes.name,
                                    sortFilterItem.title.toString(),
                                    userSession.userId,
                                )
                            }
                            if (filterItems[index].type == ChipsUnify.TYPE_SELECTED) {
                                chipItems[index].isSelected = true
                                selectedChipsCounter++
                            } else {
                                chipItems[index].isSelected = false
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
                        childFragmentManager?.let {
                            val filterPDPBottomsheet = FilterPDPBottomsheet.getInstance()
                            filterPDPBottomsheet.setTitleAndAction(
                                getString(R.string.bottom_sheet_filter_title),
                                getString(R.string.bottom_sheet_filter_reset)
                            )
                            filterPDPBottomsheet.setFilterTagDataComponent(filterData)
                            filterPDPBottomsheet.setListener(this@DigitalPDPDataPlanFragment)
                            filterPDPBottomsheet.show(it, "")
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
                viewModel.updateSelectedPositionId(selectedPosition)
                onShowBuyWidget(viewModel.selectedFullProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            if (denomData.listDenomData.isNotEmpty()) {
                it.rechargePdpPaketDataDenomFullWidget.renderDenomFullLayout(
                    this,
                    denomData,
                    selectedInitialPosition
                )
                it.rechargePdpPaketDataDenomFullWidget.show()
            } else {
                it.rechargePdpPaketDataDenomFullWidget.hide()
            }
        }
    }

    private fun onFailedDenomFull() {
        binding?.let {
            it.rechargePdpPaketDataDenomFullWidget.hide()
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

    private fun onClearSelectedDenomFull(position: Int) {
        binding?.let {
            it.rechargePdpPaketDataDenomFullWidget.clearSelectedProduct(position)
        }
    }

    private fun onSuccessMCCM(denomFull: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_FULL_TYPE)) {
                viewModel.updateSelectedPositionId(selectedPosition)
                onShowBuyWidget(viewModel.selectedFullProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            if (denomFull.listDenomData.isNotEmpty()) {
                val colorHexInt = ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
                val colorHexString = "#${Integer.toHexString(colorHexInt)}"

                it.rechargePdpPaketDataPromoWidget.show()
                it.rechargePdpPaketDataPromoWidget.renderMCCMFull(
                    this,
                    denomFull,
                    colorHexString,
                    selectedInitialPosition
                )
            } else {
                it.rechargePdpPaketDataPromoWidget.hide()
            }
        }
    }

    private fun onLoadingAndFailMCCM() {
        binding?.let {
            it.rechargePdpPaketDataPromoWidget.hide()
        }
    }

    private fun onClearSelectedMCCM(position: Int) {
        binding?.let {
            it.rechargePdpPaketDataPromoWidget.clearSelectedProduct(position)
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
            if (data.recommendations.isNotEmpty()) {
                it.rechargePdpPaketDataRecommendationWidget.show()
                it.rechargePdpPaketDataRecommendationWidget.renderRecommendationLayout(
                    this,
                    data.title,
                    data.recommendations
                )
            } else {
                it.rechargePdpPaketDataRecommendationWidget.hide()
            }
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
            it.rechargePdpPaketDataBuyWidget.show()
            it.rechargePdpPaketDataBuyWidget.renderBuyWidget(denomFull, this)
        }
    }

    private fun onHideBuyWidget() {
        binding?.let {
            it.rechargePdpPaketDataBuyWidget.hide()
        }
    }

    private fun showLoadingDialog() {
        loader = LoaderDialog(requireContext()).apply {
            setLoadingText(LOADER_DIALOG_TEXT)
        }
        loader?.show()
    }

    private fun hideLoadingDialog() {
        loader?.dialog?.dismiss()
    }

    private fun initEmptyState(banners: List<TopupBillsBanner>) {
        binding?.rechargePdpPaketDataEmptyStateWidget?.imageUrl =
            banners.firstOrNull()?.imageUrl ?: ""
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
            }
        }
    }

    private fun showGlobalErrorState() {
        binding?.globalErrorPaketData?.run {
            show()
            val categoryName = DigitalPDPCategoryUtil.getCategoryName(categoryId)
            errorTitle.text = getString(R.string.empty_state_paket_data_title, categoryName)
            errorDescription.text = getString(R.string.empty_state_paket_data_desc, categoryName)
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
            setInputNumber(clientNumber)
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

    // this function used to redirect to cart if getting error from atc Response
    private fun redirectToCart(categoryId: String){
        context?.let {
            RouteManager.route(it, CommonTopupBillsUtil.buildRedirectAppLinkToCheckout(
                viewModel.digitalCheckoutPassData.productId ?: "",
                viewModel.digitalCheckoutPassData.clientNumber ?: "",
                categoryId
            ))
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
                loyaltyStatus,
                FavoriteNumberPageConfig.TELCO,
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

    private fun showErrorUnverifiedPhoneNumber(error: ErrorAtc){
        view?.let {

            fun redirectError(error: ErrorAtc){
                if (error.atcErrorPage.buttons.first().actionType == AtcErrorButton.TYPE_PHONE_VERIFICATION) {
                    RouteManager.getIntent(context, error.appLinkUrl).apply {
                        startActivityForResult(this, REQUEST_CODE_VERIFY_PHONE_NUMBER)
                    }
                } else {
                    RouteManager.route(context, error.appLinkUrl)
                }
            }

            if (error.appLinkUrl.isNotEmpty()){
                Toaster.build(
                    it,
                    error.title,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    getString(com.tokopedia.common_digital.R.string.digital_common_button_toaster)
                ) {
                    redirectError(error)
                }.show()
            }else{
                Toaster.build(
                    it,
                    error.title,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                ).show()
            }
        }
    }

    private fun addToCart() {
        viewModel.run {
            setAddToCartLoading()
            addToCart(
                DeviceUtil.getDigitalIdentifierParam(requireActivity()),
                DigitalSubscriptionParams(),
                userSession.userId,
                remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_RECHARGE_ATC_CHECKOUT_GQL, true)
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

    private fun setupDynamicScrollViewPadding(extraPadding: Int = 0) {
        binding?.rechargePdpPaketDataClientNumberWidget
            ?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding?.rechargePdpPaketDataClientNumberWidget?.viewTreeObserver?.removeOnGlobalLayoutListener(
                        this
                    )
                    binding?.run {
                        val defaultPadding: Int = context?.resources?.displayMetrics?.let {
                            rechargePdpPaketDataClientNumberWidget.height.pxToDp(it)
                        } ?: 0
                        val dynamicPadding = defaultPadding + extraPadding
                        rechargePdpPaketDataSvContainer.setPadding(0, dynamicPadding, 0, 0)
                    }
                }
            })
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

    override fun isKeyboardShown(): Boolean = isSoftKeyboardShown()
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

    override fun onClickFilterChip(isLabeled: Boolean, favorite: RechargeClientNumberChipModel) {
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
    override fun onClickAutoComplete(favorite: TopupBillsAutoCompleteContactModel) {
        inputNumberActionType = InputNumberActionType.AUTOCOMPLETE
        if (favorite.name.isNotEmpty()) {
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
        if (binding?.rechargePdpPaketDataClientNumberWidget?.isErrorMessageShown() == true) {
            binding?.rechargePdpPaketDataClientNumberWidget?.startShakeAnimation()
            productDescBottomSheet?.dismiss()
        } else {
            if (userSession.isLoggedIn) {
                addToCart()
            } else {
                navigateToLoginPage()
            }
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
        childFragmentManager?.let {
            val summaryTelcoBottomSheet = SummaryTelcoBottomSheet.getInstance()
            summaryTelcoBottomSheet.setDenomData(denom)
            summaryTelcoBottomSheet.setTitleBottomSheet(getString(R.string.summary_transaction))
            summaryTelcoBottomSheet.show(it, "")
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
            if (viewModel.selectedFullProduct.denomWidgetEnum == DenomWidgetEnum.FULL_TYPE)
                onClearSelectedDenomFull(viewModel.selectedFullProduct.position)

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
            if (viewModel.selectedFullProduct.denomWidgetEnum == DenomWidgetEnum.MCCM_FULL_TYPE ||
                viewModel.selectedFullProduct.denomWidgetEnum == DenomWidgetEnum.FLASH_FULL_TYPE
            )
                onClearSelectedMCCM(viewModel.selectedFullProduct.position)
            digitalPDPAnalytics.clickProductCluster(
                productListTitle,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                position
            )
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
        if (layoutType == DenomWidgetEnum.MCCM_FULL_TYPE || layoutType == DenomWidgetEnum.FLASH_FULL_TYPE) {
            digitalPDPAnalytics.impressionProductMCCM(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.FULL_TYPE) {
            digitalPDPAnalytics.impressionProductCluster(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomFull,
                position
            )
        }
    }

    override fun onChevronDenomClicked(
        denomFull: DenomData,
        position: Int,
        layoutType: DenomWidgetEnum
    ) {
        childFragmentManager?.let {
            productDescBottomSheet = ProductDescBottomSheet.getInstance()
            productDescBottomSheet.setDenomData(denomFull)
            productDescBottomSheet.setListener(this)
            productDescBottomSheet.show(it, "")
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
                getFavoriteNumbers(
                    listOf(
                        FavoriteNumberType.CHIP,
                        FavoriteNumberType.LIST
                    )
                )
                binding?.rechargePdpPaketDataClientNumberWidget?.clearFocusAutoComplete()
            } else if (requestCode == REQUEST_CODE_LOGIN || requestCode == REQUEST_CODE_VERIFY_PHONE_NUMBER) {
                addToCart()
            } else if (requestCode == REQUEST_CODE_LOGIN_ALT) {
                addToCartFromUrl()
            } else if (requestCode == REQUEST_CODE_CART_DIGITAL) {
                showErrorFromCheckout(data)
            }
        }
    }

    private fun showErrorFromCheckout(data: Intent?) {
        if (data?.hasExtra(DigitalExtraParam.EXTRA_MESSAGE) == true) {
            val throwable = data.getSerializableExtra(DigitalExtraParam.EXTRA_MESSAGE)
                as Throwable
            if (!throwable.message.isNullOrEmpty())
                showErrorToaster(throwable)
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
