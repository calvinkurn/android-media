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
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.TopupBillsUserPerso
import com.tokopedia.common.topupbills.data.constant.GeneralCategoryType
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.prefix_select.TelcoOperator
import com.tokopedia.common.topupbills.data.product.CatalogOperator
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.common.topupbills.utils.generateRechargeCheckoutToken
import com.tokopedia.common.topupbills.view.activity.TopupBillsFavoriteNumberActivity
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.common.topupbills.view.model.TopupBillsSavedNumber
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogOperatorSelectGroup
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpTagihanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.bottomsheet.MoreInfoPDPBottomsheet
import com.tokopedia.digital_product_detail.presentation.utils.DigitalKeyboardWatcher
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoAnalytics
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPTagihanViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.listener.ClientNumberSortFilterListener
import com.tokopedia.recharge_component.listener.RechargeSimplifyWidgetListener
import com.tokopedia.recharge_component.model.InputFieldType
import com.tokopedia.recharge_component.model.InputNumberActionType
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DigitalPDPTagihanFragment: BaseDaggerFragment(),
    RechargeSimplifyWidgetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPTagihanViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var digitalPDPTelcoAnalytics: DigitalPDPTelcoAnalytics

    private val keyboardWatcher = DigitalKeyboardWatcher()

    private var binding by autoClearedNullable<FragmentDigitalPdpTagihanBinding>()

    private var clientNumber = ""
    private var loyaltyStatus = ""
    private var productId =  0
    private var operator = TelcoOperator()
    private var menuId = 0
    private var categoryId = GeneralCategoryType.CATEGORY_LISTRIK_PLN
    private var inputNumberActionType = InputNumberActionType.MANUAL

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
        initEmptyState()
        observeData()
        getCatalogMenuDetail()
        getOperatorSelectGroup()
        onShowGreenBox()
    }

    fun setupKeyboardWatcher() {
        binding?.root?.let {
            keyboardWatcher.listen(it, object : DigitalKeyboardWatcher.Listener {
                override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                    // do nothing
                }

                override fun onKeyboardHidden() {
                    binding?.rechargePdpTagihanListrikClientNumberWidget?.setClearable()
                }
            })
        }
    }


    private fun observeData() {
        viewModel.menuDetailData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetMenuDetail(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetMenuDetail(it.error)
                is RechargeNetworkResult.Loading -> { }
            }
        })

        viewModel.favoriteNumberData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetFavoriteNumber(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetFavoriteNumber(it.error)
                is RechargeNetworkResult.Loading -> {
                    binding?.rechargePdpTagihanListrikClientNumberWidget?.setFilterChipShimmer(true)
                }
            }
        })

        viewModel.catalogSelectGroup.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetPrefixOperator(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetPrefixOperator(it.error)
                is RechargeNetworkResult.Loading -> {}
            }
        })

        viewModel.dynamicInput.observe(viewLifecycleOwner, {
            when(it) {
                is RechargeNetworkResult.Success -> {
                    productId = it.data.id.toIntOrZero()
                }
                is RechargeNetworkResult.Fail -> {}
                is RechargeNetworkResult.Loading -> {}
            }
        })

        viewModel.inquiry.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> {}
                is RechargeNetworkResult.Fail -> {}
                is RechargeNetworkResult.Loading -> {}
            }
        })

        viewModel.addToCartResult.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> {
                    onLoadingBuyWidget(false)
                    navigateToCart()
                }
                is RechargeNetworkResult.Fail -> {
                    onLoadingBuyWidget(false)
                    showErrorToaster(it.error)
                }
                is RechargeNetworkResult.Loading -> {
                    onLoadingBuyWidget(true)
                }
            }
        })


        viewModel.clientNumberValidatorMsg.observe(viewLifecycleOwner, { msg ->
            binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
                setLoading(false)
                if (msg.isEmpty()) {
                    showIndicatorIcon()
                    clearErrorState()
                } else {
                    hideIndicatorIcon()
                    setErrorInputField(msg)
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
                inputFieldListener = object : ClientNumberInputFieldListener {
                    override fun onRenderOperator(isDelayed: Boolean) {
                        viewModel.operatorData.attributes.prefix.isEmpty().let {
                            if (it) {
                                getOperatorSelectGroup()
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
                    }

                    override fun onClickNavigationIcon() {
                        digitalPDPTelcoAnalytics.clickOnContactIcon(
                            DigitalPDPCategoryUtil.getCategoryName(categoryId),
                            userSession.userId
                        )

                        val intent = RouteManager.getIntent(context,
                            ApplinkConstInternalMarketplace.QR_SCANNEER,
                            DigitalPDPConstant.PARAM_NEED_RESULT
                        )
                        intent.putExtra(
                            DigitalPDPConstant.EXTRA_UPDATED_TITLE,
                            getString(com.tokopedia.digital_product_detail.R.string.qr_scanner_title_scan_barcode))
                        startActivityForResult(intent, DigitalPDPConstant.RESULT_CODE_QR_SCAN)
                    }

                    override fun isKeyboardShown(): Boolean {
                        context?.let {
                            val inputMethodManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            return inputMethodManager.isAcceptingText
                        }
                        return false
                    }
                },

                autoCompleteListener = object : ClientNumberAutoCompleteListener {
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
                            val clientNumber = rechargePdpTagihanListrikClientNumberWidget.getInputNumber()
                            val dgCategoryIds = arrayListOf(categoryId.toString())
                            navigateToContact(
                                clientNumber, dgCategoryIds,
                                DigitalPDPCategoryUtil.getCategoryName(categoryId)
                            )
                        }
                    }
                },
                sortFilterListener = object : ClientNumberSortFilterListener {
                    override fun getSelectedChipOperator(operator: CatalogOperator) {
                        viewModel.operatorData = operator
                        viewModel.getDynamicInput(menuId, getString(R.string.selection_null_product_error))
                    }
                }
            )
        }
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
            binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
                setInputNumber(clientNumber, true)
            }
        }
    }

    private fun onSuccessGetPrefixOperator(operatorGroup: DigitalCatalogOperatorSelectGroup) {
        viewModel.getDynamicInput(menuId, getString(R.string.selection_null_product_error))
        renderChipsAndTitle(operatorGroup)
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

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        loyaltyStatus = data.userPerso.loyaltyStatus
        getFavoriteNumber()

        renderPrefill(data.userPerso)
        renderTicker(data.tickers)
        onShowBuyWidget()
    }

    private fun onSuccessGetFavoriteNumber(favoriteNumber: List<TopupBillsPersoFavNumberItem>) {
        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            setFilterChipShimmer(false, favoriteNumber.isEmpty())
            if (favoriteNumber.isNotEmpty()){
                setFilterChipShimmer(false, favoriteNumber.isEmpty())
                setFavoriteNumber(favoriteNumber)
                setAutoCompleteList(favoriteNumber)
            }
        }
    }

    private fun navigateToCart() {
        context?.let { context ->
            val intent = RouteManager.getIntent(context, ApplinkConsInternalDigital.CHECKOUT_DIGITAL)
            viewModel.updateCategoryCheckoutPassData(categoryId.toString())
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, viewModel.digitalCheckoutPassData)
            startActivityForResult(intent, BaseTopupBillsFragment.REQUEST_CODE_CART_DIGITAL)
        }
    }

    private fun onFailedGetFavoriteNumber(throwable: Throwable) {
        binding?.run {
            rechargePdpTagihanListrikClientNumberWidget.setFilterChipShimmer(false, true)
        }
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
                    showMoreInfoBottomSheet(dummyListInfo, dummyInfo)
                })
            }

        }
    }

    private fun showMoreInfoBottomSheet(listInfo: List<String>, tickerInfo: String){
        fragmentManager?.let {
            MoreInfoPDPBottomsheet(tickerInfo, listInfo).show(it, "")
        }
    }

    private fun renderPrefill(data: TopupBillsUserPerso) {
        binding?.rechargePdpTagihanListrikClientNumberWidget?.setInputNumber(data.prefill, true)
    }

    private fun getOperatorSelectGroup() {
        viewModel.getOperatorSelectGroup(menuId)
    }

    private fun getFavoriteNumber() {
        viewModel.getFavoriteNumber(listOf(categoryId))
    }

    private fun initEmptyState() {
        // [Misael] replace with catalogMenuDetail.banners
        binding?.rechargePdpTagihanListrikEmptyStateWidget?.setImageUrl(
            "https://images.tokopedia.net/img/ULHhFV/2022/1/7/8324919c-fa15-46d9-84f7-426adb6994e0.jpg"
        )
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
            binding?.rechargePdpTagihanListrikTicker?.run {
                addPagerView(
                    TickerPagerAdapter(
                        this@DigitalPDPTagihanFragment.requireContext(), messages), messages)
                show()
            }
        } else {
            binding?.rechargePdpTagihanListrikTicker?.hide()
        }
    }

    private fun onShowBuyWidget(){
        binding?.let {
            it.rechargePdpTagihanListrikBuyWidget.showSimplifyBuyWidget(this)
        }
    }

    private fun onLoadingBuyWidget(isLoading: Boolean){
        binding?.let {
            it.rechargePdpTagihanListrikBuyWidget.isLoadingButton(isLoading)
        }
    }

    private fun renderChipsAndTitle(catalogOperators: DigitalCatalogOperatorSelectGroup){
        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            val operators = catalogOperators.response.operatorGroups?.firstOrNull()?.operators
            if (!operators.isNullOrEmpty()){
                setChipOperators(operators, viewModel.operatorData)
            }
            setTitleGeneral(catalogOperators.response.text)
        }
    }

    private fun renderProduct() {
        binding?.run {
            if (rechargePdpTagihanListrikClientNumberWidget.getInputNumber().length >= DigitalPDPConstant.MINIMUM_OPERATOR_PREFIX) {

                /* operator check */
                // TODO: [Misael] operator prefix check

                /* validate client number */
                viewModel.validateClientNumber(rechargePdpTagihanListrikClientNumberWidget.getInputNumber())
                // TODO: [Misael] hit tracking if needed

                if (rechargePdpTagihanListrikClientNumberWidget.getInputNumber()
                        .length in DigitalPDPConstant.MINIMUM_VALID_NUMBER_LENGTH..DigitalPDPConstant.MAXIMUM_VALID_NUMBER_LENGTH
                ) {
                    hideEmptyState()
                }

            } else {
                //viewModel.cancelCatalogProductJob()
                showEmptyState()
            }
        }
    }

    private fun showEmptyState() {
        binding?.run {
            if (!rechargePdpTagihanListrikEmptyStateWidget.isVisible) {
                digitalPDPTelcoAnalytics.impressionBannerEmptyState(
                    "TODO Creative Link",
                    categoryId.toString(),
                    DigitalPDPCategoryUtil.getCategoryName(categoryId),
                    loyaltyStatus,
                    userSession.userId
                )
                rechargePdpTagihanListrikEmptyStateWidget.show()
                rechargePdpTickerWidgetProductDesc.hide()
            }
        }
    }

    private fun hideEmptyState() {
        binding?.run {
            if (rechargePdpTagihanListrikEmptyStateWidget.isVisible) {
                rechargePdpTagihanListrikEmptyStateWidget.hide()
                rechargePdpTickerWidgetProductDesc.show()
            }
        }
    }

    private fun navigateToContact(
        clientNumber: String,
        dgCategoryIds: ArrayList<String>,
        categoryName: String
    ) {
        context?.let {
            val intent = TopupBillsFavoriteNumberActivity.createInstance(
                it, clientNumber, dgCategoryIds, categoryName
            )

            val requestCode = DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER
            startActivityForResult(intent, requestCode)
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

        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
            setContactName(clientName)
            setInputNumber(clientNumber, true)
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
        viewModel.getMenuDetail(menuId)
    }

    private fun addToCart(){
        viewModel.addToCart(DeviceUtil.getDigitalIdentifierParam(requireActivity()),
            DigitalSubscriptionParams(),
            userSession.userId
        )
    }

    private fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, DigitalPDPConstant.REQUEST_CODE_LOGIN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == DigitalPDPConstant.REQUEST_CODE_DIGITAL_SAVED_NUMBER) {
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
            } else if (requestCode == DigitalPDPConstant.REQUEST_CODE_LOGIN) {
                //inquiry or atc
            } else if (requestCode == DigitalPDPConstant.RESULT_CODE_QR_SCAN) {
                if (data != null){
                    val scanResult = data.getStringExtra(DigitalPDPConstant.EXTRA_QR_PARAM)
                    if (!scanResult.isNullOrEmpty()) {
                        binding?.rechargePdpTagihanListrikClientNumberWidget?.run {
                            setInputNumber(scanResult, true)
                        }
                    }
                }
            }
        }
    }

    override fun onClickedButton() {
        viewModel.updateCheckoutPassData(userSession.userId.generateRechargeCheckoutToken(),
            binding?.rechargePdpTagihanListrikClientNumberWidget?.getInputNumber() ?:"")
        if (userSession.isLoggedIn){
            addToCart()
        } else {
            navigateToLoginPage()
        }
    }


    override fun onDestroyView() {
        binding?.root?.let {
            keyboardWatcher.unlisten(it)
        }
        super.onDestroyView()
    }

    companion object {
        fun newInstance(telcoExtraParam: TopupBillsExtraParam) = DigitalPDPTagihanFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(DigitalPDPConstant.EXTRA_PARAM, telcoExtraParam)
            it.arguments = bundle
        }
    }
}