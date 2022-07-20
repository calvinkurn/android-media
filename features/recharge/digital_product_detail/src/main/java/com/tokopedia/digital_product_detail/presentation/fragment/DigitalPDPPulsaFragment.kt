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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.utils.CommonTopupBillsUtil
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment.Companion.REQUEST_CODE_CART_DIGITAL
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DEFAULT_ICON_RES
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.EXTRA_PARAM
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.FAVNUM_PERMISSION_CHECKER_IS_DENIED
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.FIXED_PADDING_ADJUSTMENT
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.INPUT_ACTION_TRACKING_DELAY
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.LOADER_DIALOG_TEXT
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MAXIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_OPERATOR_PREFIX
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.TELCO_PREFERENCES_NAME
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_LOGIN
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_LOGIN_ALT
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpPulsaBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryTelcoBottomSheet
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPAnalytics
import com.tokopedia.common_digital.common.util.DigitalKeyboardWatcher
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPWidgetMapper
import com.tokopedia.digital_product_detail.presentation.utils.setupDynamicScrollListener
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPPulsaViewModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.client_number.InputFieldType
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
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

class DigitalPDPPulsaFragment : BaseDaggerFragment(),
    RechargeDenomGridListener,
    RechargeBuyWidgetListener,
    RechargeRecommendationCardListener,
    DigitalHistoryIconListener,
    ClientNumberInputFieldListener,
    ClientNumberFilterChipListener,
    ClientNumberAutoCompleteListener
{

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPPulsaViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPAnalytics: DigitalPDPAnalytics

    private val keyboardWatcher = DigitalKeyboardWatcher()

    private var binding by autoClearedNullable<FragmentDigitalPdpPulsaBinding>()

    private lateinit var localCacheHandler: LocalCacheHandler

    private var operator = TelcoOperator()
    private var loyaltyStatus = ""
    private var clientNumber = ""
    private var productId = 0
    private var productIdFromApplink = 0
    private var menuId = 0
    private var categoryId = TelcoCategoryType.CATEGORY_PULSA
    private var inputNumberActionType = InputNumberActionType.MANUAL
    private var actionTypeTrackingJob: Job? = null
    private var loader: LoaderDialog? = null

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPPulsaViewModel::class.java)
        localCacheHandler = LocalCacheHandler(context, TELCO_PREFERENCES_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDigitalPdpPulsaBinding.inflate(inflater, container, false)
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

    private fun setupKeyboardWatcher() {
        binding?.root?.let {
            keyboardWatcher.listen(it, object : DigitalKeyboardWatcher.Listener {
                override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                    // do nothing
                }

                override fun onKeyboardHidden() {
                    // do nothing
                }
            })
        }
    }

    private fun setupDynamicScrollListener() {
        binding?.run {
            rechargePdpPulsaSvContainer.setupDynamicScrollListener(
                { !viewModel.isEligibleToBuy },
                { rechargePdpPulsaClientNumberWidget.getInputNumber().isEmpty() },
                { viewModel.runThrottleJob { onCollapseAppBar() }},
                { viewModel.runThrottleJob { onExpandAppBar() }}
            )
        }
    }

    private fun renderProduct() {
        binding?.run {
            val selectedClientNumber = rechargePdpPulsaClientNumberWidget.getInputNumber()
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
                    rechargePdpPulsaClientNumberWidget.run {
                        showOperatorIcon(selectedOperator.operator.attributes.imageUrl)
                    }
                    hideEmptyState()
                    onHideBuyWidget()
                    getRecommendations()
                    getCatalogProductInput(selectedOperator.key)
                } else {
                    onHideBuyWidget()
                }
            } catch (exception: NoSuchElementException) {
                operator = TelcoOperator()
                viewModel.run {
                    cancelRecommendationJob()
                    cancelCatalogProductJob()
                }
                rechargePdpPulsaClientNumberWidget.run {
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
                    binding?.rechargePdpPulsaClientNumberWidget?.setFilterChipShimmer(true)
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
                            denomData.data.denomWidgetModel.listDenomData,
                            productId.toString()
                        )
                    }
                    val selectedPositionDenom =
                        viewModel.getSelectedPositionId(denomData.data.denomWidgetModel.listDenomData)
                    val selectedPositionMCCM =
                        viewModel.getSelectedPositionId(denomData.data.mccmFlashSaleModel.listDenomData)

                    onSuccessDenomGrid(denomData.data.denomWidgetModel, selectedPositionDenom)
                    onSuccessMCCM(denomData.data.mccmFlashSaleModel, selectedPositionMCCM)

                    if (viewModel.isEmptyDenomMCCM(denomData.data.denomWidgetModel.listDenomData, denomData.data.mccmFlashSaleModel.listDenomData)){
                        showEmptyState(false)
                    } else hideEmptyState()

                    if (selectedPositionDenom == null && selectedPositionMCCM == null) {
                        onHideBuyWidget()
                    }
                }

                is RechargeNetworkResult.Fail -> {
                    onFailedDenomGrid()
                    onLoadingAndFailMCCM()
                }

                is RechargeNetworkResult.Loading -> {
                    onShimmeringDenomGrid()
                    onLoadingAndFailMCCM()
                }
            }
        })

        viewModel.addToCartResult.observe(viewLifecycleOwner, { atcData ->
            when (atcData) {
                is RechargeNetworkResult.Success -> {
                    hideLoadingDialog()
                    digitalPDPAnalytics.addToCart(
                        categoryId.toString(),
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        operator.attributes.name,
                        userSession.userId,
                        atcData.data.cartId,
                        viewModel.digitalCheckoutPassData.productId.toString(),
                        viewModel.selectedGridProduct.denomData.title,
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

        viewModel.clientNumberValidatorMsg.observe(viewLifecycleOwner, { msg ->
            binding?.rechargePdpPulsaClientNumberWidget?.run {
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

    private fun getRecommendations() {
        val clientNumbers = listOf(binding?.rechargePdpPulsaClientNumberWidget?.getInputNumber() ?: "")
        viewModel.setRecommendationLoading()
        viewModel.cancelRecommendationJob()
        viewModel.getRecommendations(clientNumbers, listOf(categoryId))
    }

    private fun getCatalogProductInput(selectedOperatorKey: String) {
        viewModel.setRechargeCatalogInputMultiTabLoading()
        viewModel.cancelCatalogProductJob()
        viewModel.getRechargeCatalogInputMultiTab(
            menuId, selectedOperatorKey,
            binding?.rechargePdpPulsaClientNumberWidget?.getInputNumber() ?: ""
        )
    }

    private fun getCatalogMenuDetail() {
        viewModel.setMenuDetailLoading()
        viewModel.getMenuDetail(menuId)
    }

    private fun getPrefixOperatorData() {
        viewModel.setPrefixOperatorLoading()
        viewModel.getPrefixOperator(menuId)
    }

    private fun getFavoriteNumbers(favoriteNumberTypes: List<FavoriteNumberType>) {
        viewModel.setFavoriteNumberLoading()
        viewModel.getFavoriteNumbers(
            listOf(
                TelcoCategoryType.CATEGORY_PULSA,
                TelcoCategoryType.CATEGORY_PAKET_DATA,
                TelcoCategoryType.CATEGORY_ROAMING
            ),
            favoriteNumberTypes
        )
    }

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        getFavoriteNumbers(
            listOf(
                FavoriteNumberType.PREFILL,
                FavoriteNumberType.CHIP,
                FavoriteNumberType.LIST
            )
        )
        initEmptyState(data.banners)
        renderTicker(data.tickers)
    }

    private fun onSuccessGetFavoriteChips(favoriteChips: List<FavoriteChipModel>) {
        binding?.rechargePdpPulsaClientNumberWidget?.run {
            setFilterChipShimmer(false, favoriteChips.isEmpty())
            if (favoriteChips.isNotEmpty()) {
                setFavoriteNumber(DigitalPDPWidgetMapper.mapFavoriteChipsToWidgetModels(favoriteChips))
                setupDynamicScrollViewPadding(FIXED_PADDING_ADJUSTMENT)
            } else setupDynamicScrollViewPadding()
        }
    }

    private fun onSuccessGetAutoComplete(autoComplete: List<AutoCompleteModel>) {
        binding?.rechargePdpPulsaClientNumberWidget?.run {
            if (autoComplete.isNotEmpty()) {
                setAutoCompleteList(DigitalPDPWidgetMapper.mapAutoCompletesToWidgetModels(autoComplete))
            }
        }
    }

    private fun onSuccessGetPrefill(prefill: PrefillModel) {
        binding?.rechargePdpPulsaClientNumberWidget?.run {
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
                rechargePdpPulsaPageContainer,
                errMsg,
                errMsgSub,
                null,
                DEFAULT_ICON_RES
            ) {
                getCatalogMenuDetail()
            }
        }
    }

    private fun onFailedGetPrefixOperator(throwable: Throwable) {
        showEmptyState()
        showErrorToaster(throwable)
    }

    private fun onSuccessGetRecommendations(recommendations: RecommendationWidgetModel) {
        renderRecommendation(recommendations)
    }

    private fun onFailedGetRecommendations() {
        binding?.rechargePdpPulsaRecommendationWidget?.hide()
    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpPulsaClientNumberWidget?.run {
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
                this@DigitalPDPPulsaFragment,
                this@DigitalPDPPulsaFragment,
                this@DigitalPDPPulsaFragment
            )
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

    private fun initEmptyState(banners: List<TopupBillsBanner>) {
        binding?.rechargePdpPulsaEmptyStateWidget?.imageUrl = banners.firstOrNull()?.imageUrl ?: ""
    }

    private fun onSuccessDenomGrid(denomData: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)) {
                viewModel.updateSelectedPositionId(selectedPosition)
                onShowBuyWidget(viewModel.selectedGridProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            if (denomData.listDenomData.isNotEmpty()) {
                it.rechargePdpPulsaDenomGridWidget.renderDenomGridLayout(
                    this,
                    denomData,
                    selectedInitialPosition
                )
                it.rechargePdpPulsaDenomGridWidget.show()
            } else {
                it.rechargePdpPulsaDenomGridWidget.hide()
            }
        }
    }

    private fun onFailedDenomGrid() {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.hide()
        }
    }

    private fun onShimmeringDenomGrid() {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.run {
                show()
                renderDenomGridShimmering()
            }
        }
    }

    private fun onClearSelectedDenomGrid(position: Int) {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.clearSelectedProduct(position)
        }
    }

    private fun renderRecommendation(data: RecommendationWidgetModel) {
        binding?.let {
            it.rechargePdpPulsaRecommendationWidget.show()
            it.rechargePdpPulsaRecommendationWidget.renderRecommendationLayout(
                this,
                data.title,
                data.recommendations
            )
        }
    }

    private fun onShimmeringRecommendation() {
        binding?.let {
            it.rechargePdpPulsaRecommendationWidget.show()
            it.rechargePdpPulsaRecommendationWidget.renderShimmering()
        }
    }

    private fun onSuccessMCCM(denomGrid: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_GRID_TYPE)) {
                viewModel.updateSelectedPositionId(selectedPosition)
                onShowBuyWidget(viewModel.selectedGridProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            if (denomGrid.listDenomData.isNotEmpty()) {
                val colorHexInt = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0)
                val colorHexString = "#${Integer.toHexString(colorHexInt)}"

                it.rechargePdpPulsaPromoWidget.show()
                it.rechargePdpPulsaPromoWidget.renderMCCMGrid(
                    this,
                    denomGrid,
                    colorHexString,
                    selectedInitialPosition
                )
            } else {
                it.rechargePdpPulsaPromoWidget.hide()
            }
        }
    }

    private fun onLoadingAndFailMCCM() {
        binding?.let {
            it.rechargePdpPulsaPromoWidget.hide()
        }
    }

    private fun onClearSelectedMCCM(position: Int) {
        binding?.let {
            it.rechargePdpPulsaPromoWidget.clearSelectedProduct(position)
        }
    }

    private fun onShowBuyWidget(denomGrid: DenomData) {
        binding?.let {
            it.rechargePdpPulsaBuyWidget.show()
            it.rechargePdpPulsaBuyWidget.renderBuyWidget(denomGrid, this)
        }
    }

    private fun onHideBuyWidget() {
        binding?.let {
            it.rechargePdpPulsaBuyWidget.hide()
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
            binding?.rechargePdpPulsaTicker?.run {
                addPagerView(
                    TickerPagerAdapter(
                        this@DigitalPDPPulsaFragment.requireContext(), messages
                    ), messages
                )
                show()
            }
        } else {
            binding?.rechargePdpPulsaTicker?.hide()
        }
    }

    private fun showEmptyState(isHideIndicatorIcon: Boolean = true) {
        binding?.run {
            if (!rechargePdpPulsaEmptyStateWidget.isVisible) {
                /** hide empty state when imageUrl is empty */
                if (rechargePdpPulsaEmptyStateWidget.imageUrl.isNotEmpty()) {
                    rechargePdpPulsaEmptyStateWidget.show()
                    digitalPDPAnalytics.impressionBannerEmptyState(
                        rechargePdpPulsaEmptyStateWidget.imageUrl,
                        categoryId.toString(),
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        loyaltyStatus,
                        userSession.userId
                    )

                } else {
                    rechargePdpPulsaEmptyStateWidget.hide()
                }

                if (isHideIndicatorIcon) rechargePdpPulsaClientNumberWidget.hideOperatorIcon()

                rechargePdpPulsaPromoWidget.hide()
                rechargePdpPulsaRecommendationWidget.hide()
                rechargePdpPulsaDenomGridWidget.hide()
            }
        }
    }

    private fun hideEmptyState() {
        binding?.run {
            if (rechargePdpPulsaEmptyStateWidget.isVisible) {
                rechargePdpPulsaEmptyStateWidget.hide()
            }
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

        binding?.rechargePdpPulsaClientNumberWidget?.run {
            setContactName(clientName)
            setInputNumber(clientNumber)
        }
    }

    private fun handleCallbackAnySavedNumberCancel() {
        binding?.rechargePdpPulsaClientNumberWidget?.clearFocusAutoComplete()
    }

    private fun onCollapseAppBar() {
        binding?.run {
            rechargePdpPulsaClientNumberWidget.setVisibleSimplifiedLayout(true)
        }
    }

    private fun onExpandAppBar() {
        binding?.run {
            rechargePdpPulsaClientNumberWidget.setVisibleSimplifiedLayout(false)
        }
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

    private fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM)
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
            binding?.rechargePdpPulsaClientNumberWidget?.run {
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

    private fun addToCart() {
        viewModel.setAddToCartLoading()
        viewModel.addToCart(
            DeviceUtil.getDigitalIdentifierParam(requireActivity()),
            DigitalSubscriptionParams(),
            userSession.userId
        )
    }

    private fun addToCartFromUrl() {
        context?.let { RouteManager.route(it, viewModel.recomCheckoutUrl) }
    }

    private fun navigateToLoginPage(requestCode: Int = REQUEST_CODE_LOGIN) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, requestCode)
    }

    private fun setupDynamicScrollViewPadding(extraPadding: Int = 0) {
        binding?.rechargePdpPulsaClientNumberWidget
            ?.viewTreeObserver?.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding?.rechargePdpPulsaClientNumberWidget?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    binding?.run {
                        val defaultPadding: Int = context?.resources?.displayMetrics?.let {
                            rechargePdpPulsaClientNumberWidget.height.pxToDp(it)
                        } ?: 0
                        val dynamicPadding = defaultPadding + extraPadding
                        rechargePdpPulsaSvContainer.setPadding(0, dynamicPadding, 0, 0)
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
            val clientNumber = rechargePdpPulsaClientNumberWidget.getInputNumber()
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
        return keyboardWatcher.isKeyboardOpened
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
            val clientNumber = rechargePdpPulsaClientNumberWidget.getInputNumber()
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

    //region RechargeDenomGridListener
    override fun onDenomGridClicked(
        denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int,
        productListTitle: String,
        isShowBuyWidget: Boolean
    ) {
        if (layoutType == DenomWidgetEnum.MCCM_GRID_TYPE || layoutType == DenomWidgetEnum.FLASH_GRID_TYPE) {
            if (viewModel.selectedGridProduct.denomWidgetEnum == DenomWidgetEnum.GRID_TYPE)
                onClearSelectedDenomGrid(viewModel.selectedGridProduct.position)
            digitalPDPAnalytics.clickMCCMProduct(
                productListTitle,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.GRID_TYPE) {
            if (viewModel.selectedGridProduct.denomWidgetEnum == DenomWidgetEnum.MCCM_GRID_TYPE ||
                viewModel.selectedGridProduct.denomWidgetEnum == DenomWidgetEnum.FLASH_GRID_TYPE)
                onClearSelectedMCCM(viewModel.selectedGridProduct.position)
            digitalPDPAnalytics.clickProductCluster(
                productListTitle,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                position
            )
        }

        viewModel.selectedGridProduct = SelectedProduct(denomGrid, layoutType, position)

        if (isShowBuyWidget && viewModel.isEligibleToBuy) {
            onShowBuyWidget(denomGrid)
        } else {
            viewModel.onResetSelectedProduct()
            onHideBuyWidget()
        }
    }

    override fun onDenomGridImpression(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int) {
        if (layoutType == DenomWidgetEnum.MCCM_GRID_TYPE || layoutType == DenomWidgetEnum.FLASH_GRID_TYPE){
            digitalPDPAnalytics.impressionProductMCCM(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.GRID_TYPE){
            digitalPDPAnalytics.impressionProductCluster(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                position
            )
        }
    }
    //endregion

    //region RechargeBuyWidgetListener
    override fun onClickedButtonLanjutkan(denom: DenomData) {
        viewModel.updateCheckoutPassData(
            denom, userSession.userId.generateRechargeCheckoutToken(),
            binding?.rechargePdpPulsaClientNumberWidget?.getInputNumber() ?: "",
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
            DigitalPDPCategoryUtil.getCategoryName(denom.categoryId.toInt()),
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

    //region DigitalHistoryIconListener
    override fun onClickDigitalIconHistory() {
        digitalPDPAnalytics.clickTransactionHistoryIcon(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            loyaltyStatus,
            userSession.userId
        )
    }
    //endregion

    override fun onDestroyView() {
        binding?.root?.let {
            keyboardWatcher.unlisten(it)
        }
        super.onDestroyView()
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
            } else if (requestCode == REQUEST_CODE_LOGIN) {
                addToCart()
            } else if (requestCode == REQUEST_CODE_LOGIN_ALT) {
                addToCartFromUrl()
            }
        }
    }

    companion object {
        fun newInstance(telcoExtraParam: TopupBillsExtraParam) = DigitalPDPPulsaFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            it.arguments = bundle
        }
    }
}