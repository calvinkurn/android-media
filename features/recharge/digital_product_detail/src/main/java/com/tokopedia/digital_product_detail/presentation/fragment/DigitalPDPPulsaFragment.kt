package com.tokopedia.digital_product_detail.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.favorite_number_perso.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment.Companion.REQUEST_CODE_CART_DIGITAL
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common.topupbills.view.activity.TopupBillsSavedNumberActivity
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.model.TopupBillsSavedNumber
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DEFAULT_ICON_RES
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.DEFAULT_SPACE_HEIGHT
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.EXTRA_PARAM
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.FAVNUM_PERMISSION_CHECKER_IS_DENIED
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.INPUT_ACTION_TRACKING_DELAY
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MAXIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_OPERATOR_PREFIX
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.MINIMUM_VALID_NUMBER_LENGTH
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.PREFERENCES_NAME
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.REQUEST_CODE_LOGIN
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpPulsaBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoUtil
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryPulsaBottomsheet
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoAnalytics
import com.tokopedia.digital_product_detail.presentation.utils.setupDynamicAppBar
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPPulsaViewModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget.InputNumberActionType
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
    RechargeRecommendationCardListener
{

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPPulsaViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPTelcoAnalytics: DigitalPDPTelcoAnalytics

    private var binding by autoClearedNullable<FragmentDigitalPdpPulsaBinding>()

    private var dynamicSpacerHeightRes = R.dimen.dynamic_banner_space
    private var operator = TelcoOperator()
    private var loyaltyStatus = ""
    private var clientNumber = ""
    private var productId =  0
    private var menuId = 0
    private var categoryId = TelcoCategoryType.CATEGORY_PULSA
    private var inputNumberActionType = InputNumberActionType.MANUAL

    private lateinit var localCacheHandler: LocalCacheHandler
    private var actionTypeTrackingJob: Job? = null

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPPulsaViewModel::class.java)
        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
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
        initClientNumberWidget()
        initEmptyState()
        setAnimationAppBarLayout()
        observeData()

        getCatalogMenuDetail()
        getPrefixOperatorData()
    }

    private fun renderProduct() {
        binding?.run {
            try {
                if (rechargePdpPulsaClientNumberWidget.getInputNumber().length >= MINIMUM_OPERATOR_PREFIX) {

                    /* operator check */
                    val selectedOperator =
                        viewModel.operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                            rechargePdpPulsaClientNumberWidget.getInputNumber().startsWith(it.value)
                        }

                    /* validate client number */
                    viewModel.validateClientNumber(rechargePdpPulsaClientNumberWidget.getInputNumber())
                    hitTrackingForInputNumber(
                        DigitalPDPTelcoUtil.getCategoryName(categoryId),
                        selectedOperator.operator.attributes.name
                    )

                    if (operator.id != selectedOperator.operator.id || rechargePdpPulsaClientNumberWidget.getInputNumber()
                            .length in MINIMUM_VALID_NUMBER_LENGTH .. MAXIMUM_VALID_NUMBER_LENGTH
                    ) {
                        operator = selectedOperator.operator
                        rechargePdpPulsaClientNumberWidget.run {
                            showOperatorIcon(selectedOperator.operator.attributes.imageUrl)
                        }
                        hideEmptyState()
                        getCatalogProductInput(selectedOperator.key)
                    } else {
                        onHideBuyWidget()
                    }

                } else {
                    showEmptyState()
                }
            } catch (exception: NoSuchElementException) {
                operator = TelcoOperator()
                rechargePdpPulsaClientNumberWidget.setErrorInputField(
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
                    binding?.rechargePdpPulsaClientNumberWidget?.setFilterChipShimmer(true)
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
                    val selectedPositionDenom = viewModel.getSelectedPositionId(denomData.data.denomWidgetModel.listDenomData)
                    val selectedPositionMCCM = viewModel.getSelectedPositionId(denomData.data.mccmFlashSaleModel.listDenomData)

                    onSuccessDenomGrid(denomData.data.denomWidgetModel, selectedPositionDenom)
                    onSuccessMCCM(denomData.data.mccmFlashSaleModel, selectedPositionMCCM)

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
            binding?.rechargePdpPulsaClientNumberWidget?.run {
                setLoading(false)
                if (msg.isEmpty()) {
                    showCheckIcon()
                    clearErrorState()
                } else {
                    hideCheckIcon()
                    setErrorInputField(msg)
                    onHideBuyWidget()
                }
            }
        })
    }

    private fun getCatalogProductInput(selectedOperatorKey: String) {
        viewModel.getRechargeCatalogInputMultiTab(menuId, selectedOperatorKey,
            binding?.rechargePdpPulsaClientNumberWidget?.getInputNumber() ?: "")
    }

    private fun getCatalogMenuDetail() {
        viewModel.getMenuDetail(menuId)
    }

    private fun getPrefixOperatorData() {
        viewModel.getPrefixOperator(menuId)
    }

    private fun getFavoriteNumber(categoryId: Int = this.categoryId) {
        viewModel.getFavoriteNumber(listOf(categoryId))
    }

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        getFavoriteNumber()

        renderPrefill(data.userPerso)
        renderRecommendation(data.recommendations)
        renderTicker(data.tickers)
    }

    private fun renderPrefill(data: TopupBillsUserPerso) {
        binding?.rechargePdpPulsaClientNumberWidget?.setInputNumber(data.prefill)
    }

    private fun onFailedRecommendation(){
        binding?.rechargePdpPulsaRecommendationWidget?.renderFailRecommendation()
    }

    private fun onSuccessGetFavoriteNumber(favoriteNumber: List<TopupBillsPersoFavNumberItem>) {
        binding?.rechargePdpPulsaClientNumberWidget?.run {
        setFilterChipShimmer(false, favoriteNumber.isEmpty())
            if (favoriteNumber.isNotEmpty()){
                setFilterChipShimmer(false, favoriteNumber.isEmpty())
                setFavoriteNumber(favoriteNumber)
                setAutoCompleteList(favoriteNumber)
                dynamicSpacerHeightRes = R.dimen.dynamic_banner_space_extended
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
        onFailedRecommendation()
    }

    private fun onFailedGetFavoriteNumber(throwable: Throwable) {
        binding?.run {
            rechargePdpPulsaClientNumberWidget.setFilterChipShimmer(false, true)
        }
    }

    private fun onFailedGetPrefixOperator(throwable: Throwable) {
        showEmptyState()
        showErrorToaster(throwable)
    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpPulsaClientNumberWidget?.run {

            setInputFieldStaticLabel(
                getString(
                    com.tokopedia.recharge_component.R.string.label_recharge_client_number
                )
            )
            setInputFieldType(RechargeClientNumberWidget.InputFieldType.Telco)
            setListener(
                inputFieldListener = object :
                    RechargeClientNumberWidget.ClientNumberInputFieldListener {
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
                            DigitalPDPTelcoUtil.getCategoryName(categoryId),
                            userSession.userId
                        )
                    }

                    override fun onClickContact() {
                        binding?.run {
                            val clientNumber = rechargePdpPulsaClientNumberWidget.getInputNumber()
                            val dgCategoryIds = arrayListOf(categoryId.toString())
                            navigateToContact(
                                clientNumber, dgCategoryIds,
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                false
                            )
                            digitalPDPTelcoAnalytics.clickOnContactIcon(
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                userSession.userId
                            )
                        }
                    }
                },
                autoCompleteListener = object :
                    RechargeClientNumberWidget.ClientNumberAutoCompleteListener {
                    override fun onClickAutoComplete(isFavoriteContact: Boolean) {
                        inputNumberActionType = InputNumberActionType.AUTOCOMPLETE
                        if (isFavoriteContact) {
                            digitalPDPTelcoAnalytics.clickFavoriteContactAutoComplete(
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                operator.attributes.name,
                                loyaltyStatus,
                                userSession.userId
                            )
                        } else {
                            digitalPDPTelcoAnalytics.clickFavoriteNumberAutoComplete(
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                operator.attributes.name,
                                loyaltyStatus,
                                userSession.userId
                            )
                        }
                    }
                },
                filterChipListener = object :
                    RechargeClientNumberWidget.ClientNumberFilterChipListener {
                    override fun onShowFilterChip(isLabeled: Boolean) {
                        if (isLabeled) {
                            digitalPDPTelcoAnalytics.impressionFavoriteContactChips(
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                loyaltyStatus,
                                userSession.userId
                            )
                        } else {
                            digitalPDPTelcoAnalytics.impressionFavoriteNumberChips(
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
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
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                operator.attributes.name,
                                loyaltyStatus,
                                userSession.userId,
                            )
                        } else {
                            digitalPDPTelcoAnalytics.clickFavoriteNumberChips(
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                operator.attributes.name,
                                loyaltyStatus,
                                userSession.userId
                            )
                        }
                    }

                    override fun onClickIcon(isSwitchChecked: Boolean) {
                        binding?.run {
                            val clientNumber = rechargePdpPulsaClientNumberWidget.getInputNumber()
                            val dgCategoryIds = arrayListOf(categoryId.toString())
                            navigateToContact(
                                clientNumber, dgCategoryIds,
                                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                                isSwitchChecked
                            )
                        }
                    }
                }
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

    private fun initEmptyState() {
        // [Misael] replace with catalogMenuDetail.banners
        binding?.rechargePdpPulsaEmptyStateWidget?.setImageUrl(
            "https://images.tokopedia.net/img/ULHhFV/2022/1/7/8324919c-fa15-46d9-84f7-426adb6994e0.jpg"
        )
    }

    private fun onSuccessDenomGrid(denomData: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)){
                onShowBuyWidget(viewModel.selectedGridProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            it.rechargePdpPulsaDenomGridWidget.renderDenomGridLayout(this, denomData, selectedInitialPosition)
            it.rechargePdpPulsaDenomGridWidget.show()
        }
    }

    private fun onFailedDenomGrid() {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.renderFailDenomGrid()
        }
    }

    private fun onShimmeringDenomGrid() {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.renderDenomGridShimmering()
        }
    }

    private fun onClearSelectedDenomGrid(){
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.clearSelectedProduct()
        }
    }

    fun renderRecommendation(recommendations: List<RecommendationCardWidgetModel>) {
        binding?.let {
            it.rechargePdpPulsaRecommendationWidget.show()
            it.rechargePdpPulsaRecommendationWidget.renderRecommendationLayout(this,
                getString(R.string.digital_pdp_recommendation_title),
                recommendations
            )
        }
    }

    private fun onShimmeringRecommendation(){
        binding?.let {
            it.rechargePdpPulsaRecommendationWidget.show()
            it.rechargePdpPulsaRecommendationWidget.renderShimmering()
        }
    }

    private fun onSuccessMCCM(denomGrid: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.MCCM_GRID_TYPE)){
                onShowBuyWidget(viewModel.selectedGridProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            it.rechargePdpPulsaPromoWidget.show()
            it.rechargePdpPulsaPromoWidget.renderMCCMGrid(this, denomGrid,
                getString(com.tokopedia.unifyprinciples.R.color.Unify_N0), selectedInitialPosition)
        }
    }

    private fun onLoadingAndFailMCCM(){
        binding?.let {
            it.rechargePdpPulsaPromoWidget.renderFailMCCMGrid()
        }
    }

    private fun onClearSelectedMCCM(){
        binding?.let {
            it.rechargePdpPulsaPromoWidget.clearSelectedProduct()
        }
    }

    private fun onShowBuyWidget(denomGrid: DenomData){
        binding?.let {
            it.rechargePdpPulsaBuyWidget.showBuyWidget(denomGrid, this)
        }
    }

    private fun onHideBuyWidget(){
        binding?.let {
            it.rechargePdpPulsaBuyWidget.hideBuyWidget()
        }
    }

    private fun onLoadingBuyWidget(isLoading: Boolean){
        binding?.let {
            it.rechargePdpPulsaBuyWidget.isLoadingButton(isLoading)
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
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }
                    )
                )
            }
            binding?.rechargePdpPulsaTicker?.run {
                addPagerView(TickerPagerAdapter(
                    this@DigitalPDPPulsaFragment.requireContext(), messages), messages)
                show()
            }
        } else {
            binding?.rechargePdpPulsaTicker?.hide()
        }
    }

    private fun showEmptyState() {
        binding?.run {
            if (!rechargePdpPulsaEmptyStateWidget.isVisible) {
                digitalPDPTelcoAnalytics.impressionBannerEmptyState(
                    "TODO Creative Link",
                    categoryId.toString(),
                    DigitalPDPTelcoUtil.getCategoryName(categoryId),
                    loyaltyStatus,
                    userSession.userId
                )
                rechargePdpPulsaEmptyStateWidget.show()
                rechargePdpPulsaPromoWidget.hide()
                rechargePdpPulsaRecommendationWidget.hide()
                rechargePdpPulsaDenomGridWidget.hide()
                rechargePdpPulsaClientNumberWidget.hideOperatorIcon()
            }
        }
    }

    private fun hideEmptyState() {
        binding?.run {
            if (rechargePdpPulsaEmptyStateWidget.isVisible) {
                rechargePdpPulsaEmptyStateWidget.hide()
                rechargePdpPulsaRecommendationWidget.show()
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

        binding?.rechargePdpPulsaClientNumberWidget?.run {
            setContactName(clientName)
            setInputNumber(clientNumber)
        }
    }

    private fun handleCallbackAnySavedNumberCancel() {
        binding?.rechargePdpPulsaClientNumberWidget?.clearFocusAutoComplete()
    }

    private fun setAnimationAppBarLayout() {
        binding?.run {
            rechargePdpPulsaAppbar.setupDynamicAppBar(
                { !viewModel.isEligibleToBuy },
                { rechargePdpPulsaClientNumberWidget.getInputNumber().isEmpty() },
                { onCollapseAppBar() },
                { onExpandAppBar() }
            )
        }
    }

    private fun showDynamicSpacer() {
        binding?.rechargePdpPulsaDynamicBannerSpacer?.layoutParams?.height =
            context?.resources?.getDimensionPixelSize(dynamicSpacerHeightRes)
                ?: DEFAULT_SPACE_HEIGHT
        binding?.rechargePdpPulsaDynamicBannerSpacer?.requestLayout()
    }

    private fun hideDynamicSpacer() {
        binding?.rechargePdpPulsaDynamicBannerSpacer?.layoutParams?.height = 0
        binding?.rechargePdpPulsaDynamicBannerSpacer?.requestLayout()
    }

    private fun onCollapseAppBar() {
        binding?.run {
            rechargePdpPulsaClientNumberWidget.setVisibleSimplifiedLayout(true)
            showDynamicSpacer()
        }
    }

    private fun onExpandAppBar() {
        binding?.run {
            rechargePdpPulsaClientNumberWidget.setVisibleSimplifiedLayout(false)
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

    private fun getDataFromBundle(){
            arguments?.run {
                val digitalTelcoExtraParam = this.getParcelable(EXTRA_PARAM)
                    ?: TopupBillsExtraParam()
                clientNumber = digitalTelcoExtraParam.clientNumber
                productId = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
                if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                    categoryId = digitalTelcoExtraParam.categoryId.toInt()
                }
                if (digitalTelcoExtraParam.menuId.isNotEmpty()){
                    menuId = digitalTelcoExtraParam.menuId.toIntOrNull() ?: 0
                }
            }

        if (!clientNumber.isNullOrEmpty()) {
            binding?.rechargePdpPulsaClientNumberWidget?.run {
                setInputNumber(clientNumber)
            }
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
     * RechargeDenomGridListener
     */
    override fun onDenomGridClicked(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int,
                                    productListTitle: String,
                                    isShowBuyWidget: Boolean) {
        if (layoutType == DenomWidgetEnum.MCCM_GRID_TYPE || layoutType == DenomWidgetEnum.FLASH_GRID_TYPE){
            onClearSelectedDenomGrid()
            digitalPDPTelcoAnalytics.clickMCCMProduct(
                productListTitle,
                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.GRID_TYPE){
            digitalPDPTelcoAnalytics.clickProductCluster(
                productListTitle,
                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                position
            )
            onClearSelectedMCCM()
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
            digitalPDPTelcoAnalytics.impressionProductMCCM(
                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                layoutType,
                position
            )
        } else if (layoutType == DenomWidgetEnum.GRID_TYPE){
            digitalPDPTelcoAnalytics.impressionProductCluster(
                DigitalPDPTelcoUtil.getCategoryName(categoryId),
                operator.attributes.name,
                loyaltyStatus,
                userSession.userId,
                denomGrid,
                position
            )
        }
    }

    /**
     * RechargeBuyWidgetListener
     */
    override fun onClickedButtonLanjutkan(denom: DenomData) {
        viewModel.updateCheckoutPassData(
            denom, userSession.userId.generateRechargeCheckoutToken(),
            binding?.rechargePdpPulsaClientNumberWidget?.getInputNumber() ?:"",
            operator.id
        )
        if (userSession.isLoggedIn){
            addToCart()
        } else {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        }
    }

    override fun onClickedChevron(denom: DenomData) {
        digitalPDPTelcoAnalytics.clickChevronBuyWidget(
            DigitalPDPTelcoUtil.getCategoryName(denom.categoryId.toInt()),
            operator.attributes.name,
            denom.price,
            denom.slashPrice,
            userSession.userId
        )
        fragmentManager?.let {
            SummaryPulsaBottomsheet(getString(R.string.summary_transaction), denom).show(it, "")
        }
    }

    /**
     * RechargeRecommendationCardListener
     */

    override fun onProductRecommendationCardClicked(recommendation: RecommendationCardWidgetModel, position: Int) {
        digitalPDPTelcoAnalytics.clickLastTransactionIcon(
            getString(R.string.digital_pdp_recommendation_title),
            DigitalPDPTelcoUtil.getCategoryName(categoryId),
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
            DigitalPDPTelcoUtil.getCategoryName(categoryId),
            operator.attributes.name,
            loyaltyStatus,
            userSession.userId,
            recommendation,
            position
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
            } else if( requestCode == REQUEST_CODE_LOGIN ) {
                addToCart()
            }
        }
    }

    companion object {

        fun newInstance(telcoExtraParam: TopupBillsExtraParam)  = DigitalPDPPulsaFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PARAM, telcoExtraParam)
            it.arguments = bundle
        }

    }
}