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
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.common.topupbills.data.TopupBillsBanner
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.constant.GeneralCategoryType
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoFavoriteNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.MenuDetailModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.LOADER_DIALOG_TEXT
import com.tokopedia.digital_product_detail.data.model.param.GeneralExtraParam
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpTagihanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.bottomsheet.MoreInfoPDPBottomsheet
import com.tokopedia.digital_product_detail.presentation.delegate.DigitalKeyboardDelegate
import com.tokopedia.digital_product_detail.presentation.delegate.DigitalKeyboardDelegateImpl
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPAnalytics
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPWidgetMapper
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPTagihanViewModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.listener.ClientNumberSortFilterListener
import com.tokopedia.recharge_component.listener.RechargeSimplifyWidgetListener
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.model.client_number.InputFieldType
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class DigitalPDPTagihanFragment : BaseDaggerFragment(),
    RechargeSimplifyWidgetListener,
    DigitalHistoryIconListener,
    ClientNumberInputFieldListener,
    ClientNumberFilterChipListener,
    ClientNumberAutoCompleteListener,
    ClientNumberSortFilterListener,
    DigitalKeyboardDelegate by DigitalKeyboardDelegateImpl()
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPTagihanViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPAnalytics: DigitalPDPAnalytics

    private var binding by autoClearedNullable<FragmentDigitalPdpTagihanBinding>()

    private var clientNumber = ""
    private var loyaltyStatus = ""
    private var operatorId = ""
    private var productId = 0
    private var menuId = 0
    private var categoryId = GeneralCategoryType.CATEGORY_LISTRIK_PLN
    private var inputNumberActionType = InputNumberActionType.MANUAL
    private var actionTypeTrackingJob: Job? = null
    private var loader: LoaderDialog? = null

    private val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPTagihanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDigitalPdpTagihanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromBundle()
        setupKeyboardWatcher()
        initClientNumberWidget()
        observeData()
        getCatalogMenuDetail()
    }

    private fun setupKeyboardWatcher() {
        binding?.root?.let {
            registerLifecycleOwner(viewLifecycleOwner)
            registerKeyboard(WeakReference(it))
        }
    }


    private fun observeData() {
        viewModel.menuDetailData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetMenuDetail(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetMenuDetail(it.error)
            }
        })

        viewModel.favoriteChipsData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetFavoriteNumber(it.data)
                is RechargeNetworkResult.Loading -> {
                    binding?.rechargePdpTagihanListrikClientNumberWidget?.setFilterChipShimmer(true)
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

        viewModel.catalogSelectGroup.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetOperatorSelectGroup(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetOperatorSelectGroup(it.error)
            }
        })

        viewModel.tagihanProduct.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> {
                    productId = it.data.id.toIntOrZero()
                }
            }
        })

        viewModel.addToCartResult.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> {
                    hideLoadingDialog()
                    digitalPDPAnalytics.addToCart(
                        categoryId.toString(),
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        viewModel.operatorData.attributes.name,
                        userSession.userId,
                        it.data.cartId,
                        viewModel.digitalCheckoutPassData.productId.toString(),
                        viewModel.operatorData.attributes.name,
                        it.data.priceProduct,
                        it.data.channelId,
                    )
                    navigateToCart(it.data.categoryId)
                }
                is RechargeNetworkResult.Fail -> {
                    hideLoadingDialog()
                    showErrorToaster(it.error)
                }
                is RechargeNetworkResult.Loading -> {
                    showLoadingDialog()
                }
            }
        })


        viewModel.clientNumberValidatorMsg.observe(viewLifecycleOwner, { msg ->
            binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
                setLoading(false)
                showClearIcon()
                if (msg.first.isEmpty()) {
                    clearErrorState()
                } else {
                    setErrorInputField(msg.first)
                    if (msg.second) {
                        showErrorToaster(MessageErrorException(msg.first))
                        hideLoadingDialog()
                    }
                }
            }
        })
    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            setInputFieldStaticLabel(
                getString(
                    com.tokopedia.recharge_component.R.string.label_recharge_client_number_token_listrik
                )
            )
            setInputFieldType(InputFieldType.Listrik)
            setListener(
                this@DigitalPDPTagihanFragment,
                this@DigitalPDPTagihanFragment,
                this@DigitalPDPTagihanFragment,
                this@DigitalPDPTagihanFragment
            )
        }
    }

    private fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(DigitalPDPConstant.EXTRA_PARAM)
                ?: GeneralExtraParam()
            clientNumber = digitalTelcoExtraParam.clientNumber
            productId = digitalTelcoExtraParam.productId.toIntOrZero()
            if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                categoryId = digitalTelcoExtraParam.categoryId.toIntOrZero()
            }
            if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                menuId = digitalTelcoExtraParam.menuId.toIntOrZero()
            }
            operatorId = digitalTelcoExtraParam.operatorId
        }
        if (!clientNumber.isNullOrEmpty()) {
            binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
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

    private fun onSuccessGetOperatorSelectGroup(operatorGroup: DigitalCatalogOperatorSelectGroup) {
        viewModel.run {
            setTagihanProductLoading()
            getTagihanProduct(
                menuId,
                binding?.rechargePdpTagihanListrikClientNumberWidget?.getInputNumber() ?: "",
                getString(R.string.selection_null_product_error)
            )
        }
        getFavoriteNumbers(
            listOf(
                FavoriteNumberType.PREFILL,
                FavoriteNumberType.CHIP,
                FavoriteNumberType.LIST
            )
        )
        renderProduct()
        renderOperatorChipsAndTitle(operatorGroup)
    }

    private fun renderGreenBox() {
        val listInfo = viewModel.getListInfo()
        if (!listInfo.isNullOrEmpty()) {
            onShowGreenBox(listInfo)
        } else {
            onHideGreenBox()
        }
    }

    private fun onFailedGetOperatorSelectGroup(throwable: Throwable) {
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

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        initEmptyState(data.banners)
        renderTicker(data.tickers)
        getOperatorSelectGroup()
        onShowBuyWidget()
    }

    private fun onSuccessGetFavoriteNumber(favoriteChips: List<FavoriteChipModel>) {
        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            setFilterChipShimmer(false, favoriteChips.isEmpty())
            if (favoriteChips.isNotEmpty()) {
                setFavoriteNumber(
                    DigitalPDPWidgetMapper.mapFavoriteChipsToWidgetModels(favoriteChips)
                )

                val extendedPadding = getDimens(unifyDimens.layout_lvl8)
                binding?.rechargePdpTagihanListrikSvContainer?.setPadding(0, extendedPadding, 0, 0)
            }
        }
    }

    private fun onSuccessGetAutoComplete(autoComplete: List<AutoCompleteModel>) {
        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            if (autoComplete.isNotEmpty()) {
                setAutoCompleteList(
                    DigitalPDPWidgetMapper.mapAutoCompletesToWidgetModels(autoComplete)
                )
            }
        }
    }

    private fun onSuccessGetPrefill(prefill: PrefillModel) {
        inputNumberActionType = InputNumberActionType.NOTHING
        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            if (clientNumber.isNotEmpty()) {
                setInputNumber(clientNumber)
            } else {
                if (isInputFieldEmpty()) {
                    setContactName(prefill.clientName)
                    setInputNumber(prefill.clientNumber)
                }
            }
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
            startActivityForResult(intent, BaseTopupBillsFragment.REQUEST_CODE_CART_DIGITAL)
        }
    }

    private fun onHideGreenBox() {
        binding?.rechargePdpTickerWidgetProductDesc?.hide()
    }

    private fun onShowGreenBox(listInfo: List<String>) {
        binding?.let {
            val mainInfo = listInfo.first()
            val title = getString(R.string.bottom_sheet_more_info)
            it.rechargePdpTickerWidgetProductDesc.apply {
                show()
                setText(mainInfo)
                setOnClickListener {
                    digitalPDPAnalytics.clickTransactionDetailInfo(
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        viewModel.operatorData.attributes.name,
                        loyaltyStatus,
                        userSession.userId,
                    )
                    showMoreInfoBottomSheet(listInfo, title)
                }
            }
        }
    }

    private fun showMoreInfoBottomSheet(listInfo: List<String>, title: String) {
        digitalPDPAnalytics.impressionGreenBox(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            viewModel.operatorData.attributes.name,
            loyaltyStatus,
            userSession.userId,
            title
        )
        fragmentManager?.let {
            MoreInfoPDPBottomsheet(listInfo, title).show(it, "")
        }
    }

    private fun getOperatorSelectGroup() {
        viewModel.run {
            setOperatorSelectGroupLoading()
            getOperatorSelectGroup(menuId)
        }
    }

    private fun getFavoriteNumbers(favoriteNumberTypes: List<FavoriteNumberType>) {
        viewModel.run {
            setFavoriteNumberLoading()
            getFavoriteNumbers(
                listOf(categoryId),
                viewModel.operatorList.map { it.id.toIntOrZero() },
                favoriteNumberTypes
            )
        }
    }

    private fun initEmptyState(banners: List<TopupBillsBanner>) {
        binding?.rechargePdpTagihanListrikEmptyStateWidget?.imageUrl =
            banners.firstOrNull()?.imageUrl ?: ""
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
            binding?.rechargePdpTagihanListrikTicker?.run {
                addPagerView(
                    TickerPagerAdapter(
                        this@DigitalPDPTagihanFragment.requireContext(), messages
                    ), messages
                )
                show()
            }
        } else {
            binding?.rechargePdpTagihanListrikTicker?.hide()
        }
    }

    private fun onShowBuyWidget() {
        binding?.let {
            it.rechargePdpTagihanListrikBuyWidget.show()
            it.rechargePdpTagihanListrikBuyWidget.renderSimplifyBuyWidget(this)
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

    private fun renderOperatorChipsAndTitle(catalogOperators: DigitalCatalogOperatorSelectGroup) {
        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            val operators = catalogOperators.response.operatorGroups?.firstOrNull()?.operators
            if (!operators.isNullOrEmpty()) {
                setChipOperators(operators, viewModel.operatorData)
            }
            setTitleGeneral(catalogOperators.response.text)
        }

        if (operatorId.isNotEmpty()) {
            renderOperatorChipsByAutoSelect(operatorId)
        }
    }

    private fun renderOperatorChipsByAutoSelect(operatorId: String) {
        val position = viewModel.operatorList.indexOfFirst { it.id == operatorId }
        if (!position.isLessThanZero()) {
            binding?.rechargePdpTagihanListrikClientNumberWidget?.selectChipOperatorByPosition(
                position
            )
            viewModel.setOperatorDataById(operatorId)
            getTagihanProduct()
        }
    }

    private fun renderProduct() {
        binding?.run {
            if (rechargePdpTagihanListrikClientNumberWidget.getInputNumber().length >= DigitalPDPConstant.MINIMUM_OPERATOR_PREFIX_LISTRIK) {

                /* validate client number */
                viewModel.run {
                    cancelValidatorJob()
                    validateClientNumber(rechargePdpTagihanListrikClientNumberWidget.getInputNumber())
                }

                hitTrackingForInputNumber(
                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                    viewModel.operatorData.attributes.name
                )

                if (productId <= 0) {
                    productId = viewModel.operatorData.attributes.defaultProductId.toIntOrZero()
                }

                hideEmptyState()
            } else {
                viewModel.isEligibleToBuy = false
                rechargePdpTagihanListrikClientNumberWidget.resetContactName()
                showEmptyState()
            }
        }
    }

    private fun showEmptyState() {
        binding?.run {
            if (!rechargePdpTagihanListrikEmptyStateWidget.isVisible) {
                /** hide empty state when imageUrl is empty*/
                if (rechargePdpTagihanListrikEmptyStateWidget.imageUrl.isNotEmpty()) {
                    digitalPDPAnalytics.impressionBannerEmptyState(
                        rechargePdpTagihanListrikEmptyStateWidget.imageUrl,
                        categoryId.toString(),
                        DigitalPDPCategoryUtil.getCategoryName(categoryId),
                        loyaltyStatus,
                        userSession.userId
                    )
                    rechargePdpTagihanListrikEmptyStateWidget.show()
                } else {
                    rechargePdpTagihanListrikEmptyStateWidget.hide()
                }

                rechargePdpTickerWidgetProductDesc.hide()
            }
        }
    }

    private fun hideEmptyState() {
        binding?.run {
            if (rechargePdpTagihanListrikEmptyStateWidget.isVisible) {
                rechargePdpTagihanListrikEmptyStateWidget.hide()
                renderGreenBox()
            }
        }
    }

    private fun hitTrackingForInputNumber(categoryName: String, operatorName: String) {
        actionTypeTrackingJob?.cancel()
        actionTypeTrackingJob = lifecycleScope.launch {
            delay(DigitalPDPConstant.INPUT_ACTION_TRACKING_DELAY)
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
                else -> {
                }
            }
        }
    }

    private fun navigateToContact(
        clientNumber: String,
        dgCategoryIds: ArrayList<String>,
        dgOperatorIds: ArrayList<String>,
        categoryName: String
    ) {
        context?.let {
            val intent = TopupBillsPersoFavoriteNumberActivity.createInstance(
                it, clientNumber, dgCategoryIds, dgOperatorIds, categoryName, loyaltyStatus
            )

            val requestCode = DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER
            startActivityForResult(intent, requestCode)
        }
    }

    private fun handleCallbackSavedNumber(
        clientName: String,
        clientNumber: String,
        operatorId: String,
        inputNumberActionTypeIndex: Int
    ) {
        if (!inputNumberActionTypeIndex.isLessThanZero()) {
            inputNumberActionType = InputNumberActionType.values()[inputNumberActionTypeIndex]
        }

        renderOperatorChipsByAutoSelect(operatorId)

        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            setContactName(clientName)
            setInputNumber(clientNumber)
        }
    }

    private fun handleCallbackAnySavedNumberCancel() {
        binding?.rechargePdpTagihanListrikClientNumberWidget?.clearFocusAutoComplete()
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
                rechargePdpTagihanListrikPageContainer,
                errMsg,
                errMsgSub,
                null,
                DigitalPDPConstant.DEFAULT_ICON_RES
            ) {
                getCatalogMenuDetail()
            }
        }
    }

    private fun getCatalogMenuDetail() {
        viewModel.run {
            setMenuDetailLoading()
            getMenuDetail(menuId)
        }
    }

    private fun getTagihanProduct() {
        viewModel.run {
            setTagihanProductLoading()
            getTagihanProduct(
                menuId,
                binding?.rechargePdpTagihanListrikClientNumberWidget?.getInputNumber()
                    ?: "",
                getString(R.string.selection_null_product_error)
            )
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

    private fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, DigitalPDPConstant.REQUEST_CODE_LOGIN)
    }

    //region InputFieldListener
    override fun onRenderOperator(isDelayed: Boolean, isManualInput: Boolean) {
        viewModel.operatorData.id.isEmpty().let {
            if (it) {
                getOperatorSelectGroup()
            } else {
                if (isManualInput) inputNumberActionType = InputNumberActionType.MANUAL
                renderProduct()
            }
        }
    }

    override fun onClearInput() {
        showEmptyState()
        digitalPDPAnalytics.eventClearInputNumber(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            userSession.userId
        )
    }

    override fun onClickNavigationIcon() {
        digitalPDPAnalytics.clickScanBarcode(
            DigitalPDPCategoryUtil.getCategoryName(categoryId),
            loyaltyStatus,
            userSession.userId
        )

        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.QR_SCANNEER,
            DigitalPDPConstant.PARAM_NEED_RESULT
        )
        intent.putExtra(
            DigitalPDPConstant.EXTRA_UPDATED_TITLE,
            getString(com.tokopedia.digital_product_detail.R.string.qr_scanner_title_scan_barcode)
        )
        startActivityForResult(intent, DigitalPDPConstant.RESULT_CODE_QR_SCAN)
    }

    override fun isKeyboardShown(): Boolean = isSoftKeyboardShown()

    //endregion

    //region AutoCompleteListener

    override fun onClickAutoComplete(favorite: TopupBillsAutoCompleteContactModel) {
        inputNumberActionType = InputNumberActionType.AUTOCOMPLETE
        if (favorite.name.isNotEmpty()) {
            digitalPDPAnalytics.clickFavoriteContactAutoComplete(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                viewModel.operatorData.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        } else {
            digitalPDPAnalytics.clickFavoriteNumberAutoComplete(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                viewModel.operatorData.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        }
        renderOperatorChipsByAutoSelect(operatorId)
    }
    //endregion

    //region FilterChipListener
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
            digitalPDPAnalytics.clickFavoriteContactChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                viewModel.operatorData.attributes.name,
                loyaltyStatus,
                userSession.userId,
            )
        } else {
            digitalPDPAnalytics.clickFavoriteNumberChips(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                viewModel.operatorData.attributes.name,
                loyaltyStatus,
                userSession.userId
            )
        }
        renderOperatorChipsByAutoSelect(operatorId)
    }

    override fun onClickIcon(isSwitchChecked: Boolean) {
        binding?.run {
            digitalPDPAnalytics.clickListFavoriteNumber(
                DigitalPDPCategoryUtil.getCategoryName(categoryId),
                viewModel.operatorData.attributes.name,
                userSession.userId
            )
            val clientNumber =
                rechargePdpTagihanListrikClientNumberWidget.getInputNumber()
            val dgCategoryIds = arrayListOf(categoryId.toString())
            val dgOperatorIds: ArrayList<String> =
                ArrayList(viewModel.operatorList.map { it.id })
            navigateToContact(
                clientNumber, dgCategoryIds, dgOperatorIds,
                DigitalPDPCategoryUtil.getCategoryName(categoryId)
            )
        }
    }
    //endregion

    //region SortFilterListener
    override fun getSelectedChipOperator(operator: CatalogOperator) {
        viewModel.run {
            operatorData = operator
            getTagihanProduct()
        }
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
                        orderClientNumber.operatorId,
                        orderClientNumber.inputNumberActionTypeIndex,
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
                binding?.rechargePdpTagihanListrikClientNumberWidget?.clearFocusAutoComplete()
            } else if (requestCode == DigitalPDPConstant.REQUEST_CODE_LOGIN) {
                addToCart()
            } else if (requestCode == DigitalPDPConstant.RESULT_CODE_QR_SCAN) {
                if (data != null) {
                    val scanResult = data.getStringExtra(DigitalPDPConstant.EXTRA_QR_PARAM)
                    if (!scanResult.isNullOrEmpty()) {
                        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
                            setInputNumber(scanResult)
                        }
                    }
                }
            }
        }
    }

    override fun onClickedButton() {
        if (viewModel.isEligibleToBuy) {
            viewModel.updateCheckoutPassData(
                userSession.userId.generateRechargeCheckoutToken(),
                binding?.rechargePdpTagihanListrikClientNumberWidget?.getInputNumber() ?: ""
            )
            if (userSession.isLoggedIn) {
                addToCart()
            } else {
                navigateToLoginPage()
            }
        } else {
            binding?.rechargePdpTagihanListrikClientNumberWidget?.startShakeAnimation()
        }
    }

    companion object {
        fun newInstance(generalExtraParam: GeneralExtraParam) = DigitalPDPTagihanFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(DigitalPDPConstant.EXTRA_PARAM, generalExtraParam)
            it.arguments = bundle
        }
    }
}
