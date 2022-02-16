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
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso
import com.tokopedia.common.topupbills.data.constant.GeneralCategoryType
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.favorite.view.activity.TopupBillsPersoFavoriteNumberActivity
import com.tokopedia.common.topupbills.favorite.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.EXTRA_QR_PARAM
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.EXTRA_UPDATED_TITLE
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.PARAM_NEED_RESULT
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.RESULT_CODE_QR_SCAN
import com.tokopedia.digital_product_detail.data.model.data.SelectedProduct
import com.tokopedia.digital_product_detail.data.model.param.GeneralExtraParam
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpTokenListrikBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.bottomsheet.MoreInfoPDPBottomsheet
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryTelcoBottomSheet
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoAnalytics
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.utils.DigitalKeyboardWatcher
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPTokenListrikViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.InputFieldType
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget
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

class DigitalPDPTokenListrikFragment: BaseDaggerFragment(),
    RechargeDenomGridListener,
    RechargeBuyWidgetListener,
    RechargeRecommendationCardListener,
    DigitalHistoryIconListener
{

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPTokenListrikViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPTelcoAnalytics: DigitalPDPTelcoAnalytics

    private val keyboardWatcher = DigitalKeyboardWatcher()

    private var binding by autoClearedNullable<FragmentDigitalPdpTokenListrikBinding>()

    private var loyaltyStatus = ""
    private var clientNumber = ""
    private var productId =  0
    private var operatorId = ""
    private var menuId = 0
    private var categoryId = GeneralCategoryType.CATEGORY_LISTRIK_PLN
    private lateinit var localCacheHandler: LocalCacheHandler
    private var actionTypeTrackingJob: Job? = null
    private var inputNumberActionType = InputNumberActionType.MANUAL

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPTokenListrikViewModel::class.java)
        localCacheHandler = LocalCacheHandler(context, DigitalPDPConstant.TOKEN_LISTRIK_PREFERENCES_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDigitalPdpTokenListrikBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromBundle()
        setupKeyboardWatcher()
        initClientNumberWidget()
        initEmptyState()
        observeData()

        getCatalogMenuDetail()
        getPrefixOperatorData()
        onShowGreenBox()
    }

    fun setupKeyboardWatcher() {
        binding?.root?.let {
            keyboardWatcher.listen(it, object : DigitalKeyboardWatcher.Listener {
                override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                    // do nothing
                }

                override fun onKeyboardHidden() {
                    binding?.rechargePdpTokenListrikClientNumberWidget?.setClearable()
                }
            })
        }
    }

    private fun getDataFromBundle(){
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(DigitalPDPConstant.EXTRA_PARAM)
                ?: GeneralExtraParam()
            clientNumber = digitalTelcoExtraParam.clientNumber
            productId = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
            if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                categoryId = digitalTelcoExtraParam.categoryId.toInt()
            }
            if (digitalTelcoExtraParam.menuId.isNotEmpty()){
                menuId = digitalTelcoExtraParam.menuId.toIntOrNull() ?: 0
            }
            operatorId = digitalTelcoExtraParam.operatorId
        }
        if (!clientNumber.isNullOrEmpty()) {
            binding?.rechargePdpTokenListrikClientNumberWidget?.run {
                setInputNumber(clientNumber)
            }
        }
    }

    private fun initEmptyState() {
        // [Misael] replace with catalogMenuDetail.banners
        binding?.rechargePdpTokenListrikEmptyStateWidget?.setImageUrl(
            "https://images.tokopedia.net/img/ULHhFV/2022/1/7/8324919c-fa15-46d9-84f7-426adb6994e0.jpg"
        )
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
                    binding?.rechargePdpTokenListrikClientNumberWidget?.setFilterChipShimmer(true)
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


        viewModel.observableDenomData.observe(viewLifecycleOwner, { denomData ->
            when (denomData) {
                is RechargeNetworkResult.Success -> {
                    val selectedPositionDenom = viewModel.getSelectedPositionId(denomData.data.denomWidgetModel.listDenomData)
                    onSuccessDenomGrid(denomData.data.denomWidgetModel, selectedPositionDenom)

                    if (selectedPositionDenom == null) {
                        onHideBuyWidget()
                    }
                }

                is RechargeNetworkResult.Fail -> {
                    onFailedDenomGrid()
                }

                is RechargeNetworkResult.Loading -> {
                    onShimmeringDenomGrid()
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
            binding?.rechargePdpTokenListrikClientNumberWidget?.run {
                setLoading(false)
                if (msg.isEmpty()) {
                    showIndicatorIcon()
                    clearErrorState()
                } else {
                    hideIndicatorIcon()
                    setErrorInputField(msg)
                    onHideBuyWidget()
                }
            }
        })
    }

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        getFavoriteNumber()

        renderPrefill(data.userPerso)
        renderRecommendation(data.recommendations)
        renderTicker(data.tickers)
    }

    private fun onShowGreenBox(){
        binding?.let {
            //TODO Firman change to real data
            val dummyInfo = "Transaksi selama <b>23:40-00:20 WIB</b> baru akan diproses pada <b>00:45 WIB.</b> <b>Selengkapnya</b>"
            val clickableInfo = "Selengkapnya"
            val dummyListInfo = listOf<String>(
                "Transaksi selama 23:40-00:20 WIB baru akan <b>diproses pada 00:45 WIB.</b>",
                "Proses verifikasi transaksi membutuhkan <b>maksimal 2x24 jam</b> hari kerja",
                "Harap cek <b>limit kWh</b> anda sebelum membeli token listrik ya"
            )
            it.rechargePdpTickerWidgetProductDesc.apply {
                setText(dummyInfo)
                setLinks(clickableInfo, View.OnClickListener {
                    digitalPDPTelcoAnalytics.clickTransactionDetailInfo(
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        DigitalPDPCategoryUtil.getOperatorName(operatorId),
                        loyaltyStatus,
                        userSession.userId
                    )
                    showMoreInfoBottomSheet(dummyListInfo)
                })
            }

        }
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
                rechargePdpTokenListrikPageContainer,
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

    private fun getCatalogMenuDetail() {
        viewModel.getMenuDetail(menuId)
    }

    private fun getPrefixOperatorData() {
        viewModel.getPrefixOperator(menuId)
    }

    private fun getFavoriteNumber() {
        viewModel.getFavoriteNumber(listOf(categoryId))
    }

    private fun renderPrefill(data: TopupBillsUserPerso) {
        binding?.rechargePdpTokenListrikClientNumberWidget?.setInputNumber(data.prefill, true)
    }

    private fun onFailedRecommendation(){
        binding?.rechargePdpTokenListrikRecommendationWidget?.renderFailRecommendation()
    }

    fun renderRecommendation(recommendations: List<RecommendationCardWidgetModel>) {
        binding?.let {
            it.rechargePdpTokenListrikRecommendationWidget.show()
            it.rechargePdpTokenListrikRecommendationWidget.renderRecommendationLayout(this,
                getString(R.string.digital_pdp_recommendation_title),
                recommendations
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
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }
                    )
                )
            }
            binding?.rechargePdpTokenListrikTicker?.run {
                addPagerView(
                    TickerPagerAdapter(
                    this@DigitalPDPTokenListrikFragment.requireContext(), messages), messages)
                show()
            }
        } else {
            binding?.rechargePdpTokenListrikTicker?.hide()
        }
    }

    private fun onShimmeringRecommendation(){
        binding?.let {
            it.rechargePdpTokenListrikRecommendationWidget.show()
            it.rechargePdpTokenListrikRecommendationWidget.renderShimmering()
        }
    }

    private fun onSuccessDenomGrid(denomData: DenomWidgetModel, selectedPosition: Int?) {
        binding?.let {
            var selectedInitialPosition = selectedPosition
            if (viewModel.isAutoSelectedProduct(DenomWidgetEnum.GRID_TYPE)){
                onShowBuyWidget(viewModel.selectedGridProduct.denomData)
            } else {
                selectedInitialPosition = null
            }
            it.rechargePdpTokenListrikDenomGridWidget.renderDenomGridLayout(this, denomData, selectedInitialPosition)
            it.rechargePdpTokenListrikDenomGridWidget.show()
        }
    }

    private fun onFailedDenomGrid() {
        binding?.let {
            it.rechargePdpTokenListrikDenomGridWidget.renderFailDenomGrid()
        }
    }

    private fun onShimmeringDenomGrid() {
        binding?.let {
            it.rechargePdpTokenListrikDenomGridWidget.renderDenomGridShimmering()
        }
    }

    private fun onShowBuyWidget(denomGrid: DenomData){
        binding?.let {
            it.rechargePdpTokenListrikBuyWidget.showBuyWidget(denomGrid, this)
        }
    }

    private fun onHideBuyWidget(){
        binding?.let {
            it.rechargePdpTokenListrikBuyWidget.hideBuyWidget()
        }
    }

    private fun onLoadingBuyWidget(isLoading: Boolean){
        binding?.let {
            it.rechargePdpTokenListrikBuyWidget.isLoadingButton(isLoading)
        }
    }

    private fun onSuccessGetFavoriteNumber(favoriteNumber: List<TopupBillsPersoFavNumberItem>) {
        binding?.rechargePdpTokenListrikClientNumberWidget?.run {
            setFilterChipShimmer(false, favoriteNumber.isEmpty())
            if (favoriteNumber.isNotEmpty()){
                setFilterChipShimmer(false, favoriteNumber.isEmpty())
                setFavoriteNumber(favoriteNumber)
                setAutoCompleteList(favoriteNumber)
            }
        }
    }

    private fun onFailedGetFavoriteNumber(throwable: Throwable) {
        binding?.run {
            rechargePdpTokenListrikClientNumberWidget.setFilterChipShimmer(false, true)
        }
    }

    private fun onSuccessGetPrefixOperator() {
        renderProduct()
    }

    private fun onFailedGetPrefixOperator(throwable: Throwable) {
        showEmptyState()
        showErrorToaster(throwable)
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

    private fun renderProduct() {
        binding?.run {
            if (rechargePdpTokenListrikClientNumberWidget.getInputNumber().length >= DigitalPDPConstant.MINIMUM_OPERATOR_PREFIX) {

                /* operator check */
                // TODO: [Misael] operator prefix check

                /* validate client number */
                viewModel.validateClientNumber(rechargePdpTokenListrikClientNumberWidget.getInputNumber())
                // TODO: [Misael] hit tracking if needed

                if (rechargePdpTokenListrikClientNumberWidget.getInputNumber()
                        .length in DigitalPDPConstant.MINIMUM_VALID_NUMBER_LENGTH..DigitalPDPConstant.MAXIMUM_VALID_NUMBER_LENGTH
                ) {
                    getCatalogProductInput(operatorId)
                    hideEmptyState()

                } else {
                    onHideBuyWidget()
                }

            } else {
                //viewModel.cancelCatalogProductJob()
                showEmptyState()
            }
        }
    }

    private fun hitTrackingForInputNumber(categoryName: String, operatorName: String) {
        actionTypeTrackingJob?.cancel()
        actionTypeTrackingJob = lifecycleScope.launch {
            delay(DigitalPDPConstant.INPUT_ACTION_TRACKING_DELAY)
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

    private fun hideEmptyState() {
        binding?.run {
            if (rechargePdpTokenListrikEmptyStateWidget.isVisible) {
                rechargePdpTokenListrikEmptyStateWidget.hide()
                rechargePdpTokenListrikRecommendationWidget.show()
                rechargePdpTickerWidgetProductDesc.show()
            }
        }
    }

    private fun getCatalogProductInput(selectedOperatorKey: String) {
        viewModel.getRechargeCatalogInput(menuId, selectedOperatorKey)
    }

    private fun showEmptyState() {
        binding?.run {
            if (!rechargePdpTokenListrikEmptyStateWidget.isVisible) {
                digitalPDPTelcoAnalytics.impressionBannerEmptyState(
                    "TODO Creative Link",
                    categoryId.toString(),
                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                    loyaltyStatus,
                    userSession.userId
                )
                rechargePdpTokenListrikEmptyStateWidget.show()
                rechargePdpTokenListrikRecommendationWidget.hide()
                rechargePdpTokenListrikDenomGridWidget.hide()
                rechargePdpTickerWidgetProductDesc.hide()
            }
        }
    }

    private fun navigateToCart(categoryId: String) {
        context?.let { context ->
            val intent = RouteManager.getIntent(context, ApplinkConsInternalDigital.CHECKOUT_DIGITAL)
            viewModel.updateCategoryCheckoutPassData(categoryId)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, viewModel.digitalCheckoutPassData)
            startActivityForResult(intent, BaseTopupBillsFragment.REQUEST_CODE_CART_DIGITAL)
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

    private fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, DigitalPDPConstant.REQUEST_CODE_LOGIN)
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

        binding?.rechargePdpTokenListrikClientNumberWidget?.run {
            setContactName(clientName)
            setInputNumber(clientNumber, true)
        }
    }

    private fun handleCallbackAnySavedNumberCancel() {
        binding?.rechargePdpTokenListrikClientNumberWidget?.clearFocusAutoComplete()
    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpTokenListrikClientNumberWidget?.run {
            setInputFieldStaticLabel(
                getString(
                    com.tokopedia.recharge_component.R.string.label_recharge_client_number_token_listrik
                )
            )
            setInputFieldType(InputFieldType.Listrik)
            setListener(
                inputFieldListener = object : ClientNumberInputFieldListener {
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
                        showEmptyState()
                        digitalPDPTelcoAnalytics.eventClearInputNumber(
                            DigitalPDPCategoryUtil.getCategoryName(categoryId),
                            userSession.userId
                        )
                        onHideBuyWidget()
                    }

                    override fun onClickNavigationIcon() {
                        digitalPDPTelcoAnalytics.clickScanBarcode(
                            DigitalPDPCategoryUtil.getCategoryName(categoryId),
                            loyaltyStatus,
                            userSession.userId
                        )

                        val intent = RouteManager.getIntent(context,
                            ApplinkConstInternalMarketplace.QR_SCANNEER, PARAM_NEED_RESULT)
                        intent.putExtra(EXTRA_UPDATED_TITLE,
                            getString(com.tokopedia.digital_product_detail.R.string.qr_scanner_title_scan_barcode))
                        startActivityForResult(intent, RESULT_CODE_QR_SCAN)
                    }

                    override fun isKeyboardShown(): Boolean {
                        context?.let {
                            val inputMethodManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            return inputMethodManager.isAcceptingText
                        }
                        return false
                    }
                },
                autoCompleteListener = object :
                    ClientNumberAutoCompleteListener {
                    override fun onClickAutoComplete(isFavoriteContact: Boolean) {
                        inputNumberActionType = InputNumberActionType.AUTOCOMPLETE
                        if (isFavoriteContact) {
                            digitalPDPTelcoAnalytics.clickFavoriteContactAutoComplete(
                                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                                DigitalPDPCategoryUtil.getOperatorName(operatorId),
                                loyaltyStatus,
                                userSession.userId
                            )
                        } else {
                            digitalPDPTelcoAnalytics.clickFavoriteNumberAutoComplete(
                                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                                DigitalPDPCategoryUtil.getOperatorName(operatorId),
                                loyaltyStatus,
                                userSession.userId
                            )
                        }
                    }
                },
                filterChipListener = object : ClientNumberFilterChipListener {
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
                                DigitalPDPCategoryUtil.getOperatorName(operatorId),
                                loyaltyStatus,
                                userSession.userId,
                            )
                        } else {
                            digitalPDPTelcoAnalytics.clickFavoriteNumberChips(
                                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                                DigitalPDPCategoryUtil.getOperatorName(operatorId),
                                loyaltyStatus,
                                userSession.userId
                            )
                        }
                    }

                    override fun onClickIcon(isSwitchChecked: Boolean) {
                        binding?.run {
                            digitalPDPTelcoAnalytics.clickListFavoriteNumber(
                                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                                DigitalPDPCategoryUtil.getOperatorName(operatorId),
                                userSession.userId
                            )
                            val clientNumber = rechargePdpTokenListrikClientNumberWidget.getInputNumber()
                            val dgCategoryIds = arrayListOf(categoryId.toString())
                            navigateToContact(
                                clientNumber, dgCategoryIds,
                                DigitalPDPCategoryUtil.getCategoryName(categoryId)
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
        categoryName: String
    ) {
        context?.let {
            val intent = TopupBillsPersoFavoriteNumberActivity.createInstance(
                it, clientNumber, dgCategoryIds, categoryName, viewModel.operatorData, loyaltyStatus
            )

            val requestCode = DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER
            startActivityForResult(intent, requestCode)
        }
    }

    private fun showMoreInfoBottomSheet(listInfo: List<String>){
        fragmentManager?.let {
            MoreInfoPDPBottomsheet(listInfo).show(it, "")
        }
    }

    /**
     * RechargeDenomGridListener
     */
    override fun onDenomGridClicked(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int,
                                    productListTitle: String,
                                    isShowBuyWidget: Boolean) {
        if (layoutType == DenomWidgetEnum.GRID_TYPE){
            digitalPDPTelcoAnalytics.clickProductCluster(
                productListTitle,
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                DigitalPDPCategoryUtil.getOperatorName(operatorId),
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
        if (layoutType == DenomWidgetEnum.GRID_TYPE){
            digitalPDPTelcoAnalytics.impressionProductCluster(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                DigitalPDPCategoryUtil.getOperatorName(operatorId),
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
            binding?.rechargePdpTokenListrikClientNumberWidget?.getInputNumber() ?:"",
            operatorId
        )
        if (userSession.isLoggedIn){
            addToCart()
        } else {
            navigateToLoginPage()
        }
    }

    override fun onClickedChevron(denom: DenomData) {
        digitalPDPTelcoAnalytics.clickChevronBuyWidget(
            DigitalPDPCategoryUtil.getCategoryName(denom.categoryId.toInt()),
            DigitalPDPCategoryUtil.getOperatorName(operatorId),
            denom.price,
            denom.slashPrice,
            userSession.userId
        )
        fragmentManager?.let {
            SummaryTelcoBottomSheet(getString(R.string.summary_transaction), denom).show(it, "")
        }
    }

    /**
     * RechargeRecommendationCardListener
     */

    override fun onProductRecommendationCardClicked(recommendation: RecommendationCardWidgetModel, position: Int) {
        digitalPDPTelcoAnalytics.clickLastTransactionIcon(
            getString(R.string.digital_pdp_recommendation_title),
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            DigitalPDPCategoryUtil.getOperatorName(operatorId),
            loyaltyStatus,
            userSession.userId,
            recommendation,
            position
        )

        viewModel.updateCheckoutPassData(
            recommendation,
            userSession.userId.generateRechargeCheckoutToken()
        )

        if (userSession.isLoggedIn) {
            addToCart()
        } else {
            navigateToLoginPage()
        }
    }

    override fun onProductRecommendationCardImpression(recommendation: RecommendationCardWidgetModel, position: Int) {
        digitalPDPTelcoAnalytics.impressionLastTransactionIcon(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            DigitalPDPCategoryUtil.getOperatorName(operatorId),
            loyaltyStatus,
            userSession.userId,
            recommendation,
            position
        )
    }

    /** DigitalHistoryIconListener */

    override fun onClickDigitalIconHistory() {
        digitalPDPTelcoAnalytics.clickTransactionHistoryIcon(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            loyaltyStatus,
            userSession.userId
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

    override fun onDestroyView() {
        binding?.root?.let {
            keyboardWatcher.unlisten(it)
        }
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER) {
                if (data != null) {
                    val orderClientNumber =
                        data.getParcelableExtra<Parcelable>(EXTRA_CALLBACK_CLIENT_NUMBER) as TopupBillsSavedNumber

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
            } else if (requestCode == DigitalPDPConstant.REQUEST_CODE_LOGIN) {
                addToCart()
            } else if (requestCode == DigitalPDPConstant.RESULT_CODE_QR_SCAN) {
                if (data != null){
                    val scanResult = data.getStringExtra(EXTRA_QR_PARAM)
                    if (!scanResult.isNullOrEmpty()) {
                        binding?.rechargePdpTokenListrikClientNumberWidget?.run {
                            setInputNumber(scanResult, true)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(generalExtraParam: GeneralExtraParam) = DigitalPDPTokenListrikFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(DigitalPDPConstant.EXTRA_PARAM, generalExtraParam)
            it.arguments = bundle
        }
    }
}