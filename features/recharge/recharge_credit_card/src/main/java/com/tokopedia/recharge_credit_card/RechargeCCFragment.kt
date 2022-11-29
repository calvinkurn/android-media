package com.tokopedia.recharge_credit_card

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoFavoriteNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberPageConfig
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.util.DigitalKeyboardWatcher
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberAutoCompleteListener
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
import com.tokopedia.recharge_component.listener.ClientNumberInputFieldListener
import com.tokopedia.recharge_component.model.client_number.InputFieldType
import com.tokopedia.recharge_component.model.client_number.RechargeClientNumberChipModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_credit_card.RechargeCCActivity.Companion.PARAM_CLIENT_NUMBER
import com.tokopedia.recharge_credit_card.RechargeCCActivity.Companion.PARAM_IDENTIFIER
import com.tokopedia.recharge_credit_card.RechargeCCActivity.Companion.PARAM_OPERATOR_ID
import com.tokopedia.recharge_credit_card.RechargeCCActivity.Companion.PARAM_PRODUCT_ID
import com.tokopedia.recharge_credit_card.RechargeCCActivity.Companion.PARAM_SIGNATURE
import com.tokopedia.recharge_credit_card.analytics.CreditCardAnalytics
import com.tokopedia.recharge_credit_card.bottomsheet.CCBankListBottomSheet
import com.tokopedia.recharge_credit_card.datamodel.RechargeCreditCard
import com.tokopedia.recharge_credit_card.datamodel.TickerCreditCard
import com.tokopedia.recharge_credit_card.di.RechargeCCInstance
import com.tokopedia.recharge_credit_card.util.RechargeCCConst
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.DEFAULT_MAX_CC_LENGTH
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.DEFAULT_MIN_CC_LENGTH
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.REQUEST_CODE_FAVORITE_NUMBER
import com.tokopedia.recharge_credit_card.util.RechargeCCGqlQuery
import com.tokopedia.recharge_credit_card.util.RechargeCCUtil
import com.tokopedia.recharge_credit_card.viewmodel.RechargeCCViewModel
import com.tokopedia.recharge_credit_card.viewmodel.RechargeSubmitCCViewModel
import com.tokopedia.recharge_credit_card.widget.RechargeCCClientNumberWidget
import com.tokopedia.recharge_credit_card.widget.util.RechargeCCMapper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_recharge_cc.*
import javax.inject.Inject

class RechargeCCFragment :
    BaseDaggerFragment(),
    ClientNumberInputFieldListener,
    ClientNumberFilterChipListener,
    ClientNumberAutoCompleteListener,
    RechargeCCClientNumberWidget.CreditCardActionListener
{
    private lateinit var rechargeCCViewModel: RechargeCCViewModel
    private lateinit var rechargeSubmitCCViewModel: RechargeSubmitCCViewModel
    private lateinit var saveInstanceManager: SaveInstanceCacheManager
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private val keyboardWatcher = DigitalKeyboardWatcher()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var creditCardAnalytics: CreditCardAnalytics

    private var operatorIdSelected: String = ""
    private var productIdSelected: String = ""
    private var categoryId: String = ""
    private var menuId: String = ""
    private var checkoutPassDataState: DigitalCheckoutPassData? = null
    private var token: String = ""

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.let {
            val creditCardComponent = RechargeCCInstance.getComponent(it.application)
            creditCardComponent.inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        activity?.let {
            saveInstanceManager = SaveInstanceCacheManager(it, savedInstanceState)
        }

        if (savedInstanceState != null) {
            checkoutPassDataState = saveInstanceManager.get(EXTRA_STATE_CHECKOUT_PASS_DATA,
                    DigitalCheckoutPassData::class.java, null)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        checkoutPassDataState?.let {
            saveInstanceManager.put(EXTRA_STATE_CHECKOUT_PASS_DATA, it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recharge_cc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializedViewModel()
        setupKeyboardWatcher()
        getDataBundle()
        getTickerData()
        getFavoriteNumbers(
            listOf(
                FavoriteNumberType.PREFILL,
                FavoriteNumberType.CHIP,
                FavoriteNumberType.LIST
            )
        )

        cc_widget_client_number.run {
            setInputFieldType(InputFieldType.CreditCard)
            setInputFieldStaticLabel(context.getString(R.string.cc_label_input_number))
            setInputFieldListener(this@RechargeCCFragment)
            setFilterChipListener(this@RechargeCCFragment)
            setAutoCompleteListener(this@RechargeCCFragment)
            setCreditCardATCListener(this@RechargeCCFragment)
        }

        list_bank_btn.setOnClickListener {
            activity?.let {
                val bottomSheetBankList = CCBankListBottomSheet(categoryId)
                bottomSheetBankList.show(it.supportFragmentManager, "Bank list")
            }
        }
        observeData()
        creditCardAnalytics.impressionInitialPage(userSession.userId)
    }

    private fun setupKeyboardWatcher() {
        cc_widget_client_number.rootView?.let {
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

    private fun getDataBundle() {
        arguments?.let {
            categoryId = it.getString(CATEGORY_ID, "")
            menuId = it.getString(MENU_ID, "")
            val signature = it.getString(PARAM_SIGNATURE) ?: ""
            val identifier = it.getString(PARAM_IDENTIFIER) ?: ""
            if (signature.isNotEmpty() && identifier.isNotEmpty()) {
                instantCheckout()
            }
        }
    }

    private fun initializedViewModel() {
        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            rechargeCCViewModel = viewModelProvider.get(RechargeCCViewModel::class.java)
            rechargeSubmitCCViewModel = viewModelProvider.get(RechargeSubmitCCViewModel::class.java)
        }
    }

    private fun observeData() {
        rechargeCCViewModel.creditCardSelected.observe(viewLifecycleOwner, Observer {
            if (it.defaultProductId.isEmpty() && it.operatorId.isEmpty()) {
                cc_widget_client_number.hideOperatorIcon()
                cc_widget_client_number.setLoading(false)
                cc_widget_client_number.setErrorInputField(
                    getString(R.string.cc_bank_is_not_supported))
            } else {
                cc_widget_client_number.showOperatorIcon(it.imageUrl)
            }
            operatorIdSelected = it.operatorId
            productIdSelected = it.defaultProductId
        })

        rechargeCCViewModel.prefixSelect.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RechargeNetworkResult.Success -> {
                    rechargeCCViewModel.run {
                        checkPrefixNumber(cc_widget_client_number.getInputNumber())
                        cancelValidatorJob()
                        validateCCNumber(cc_widget_client_number.getInputNumber())
                    }
                }
                is RechargeNetworkResult.Fail -> {
                    showErrorToaster(it.error)
                }
            }
        })

        rechargeCCViewModel.prefixValidation.observe(viewLifecycleOwner, Observer { isValid ->
            cc_widget_client_number.run {
                setLoading(false)
                val clientNumber = getInputNumber()
                if (clientNumber.length in DEFAULT_MIN_CC_LENGTH..DEFAULT_MAX_CC_LENGTH) {
                    if (isValid && rechargeCCViewModel.creditCardSelected.value?.operatorId?.isEmpty() == false) {
                        if (!clientNumber.isMasked() &&
                            !RechargeCCUtil.isCreditCardValid(clientNumber)) {
                            setErrorInputField(getString(R.string.cc_error_invalid_number))
                        } else {
                            clearErrorState()
                            enablePrimaryButton()
                        }
                    }
                }
            }
        })

        rechargeCCViewModel.tickers.observe(viewLifecycleOwner, Observer {
            renderTicker(it)
            performanceMonitoring.stopTrace()
        })

        rechargeSubmitCCViewModel.errorSignature.observe(viewLifecycleOwner, Observer {
            hideLoading()
            showErrorToaster(it)
        })

        rechargeSubmitCCViewModel.redirectUrl.observe(viewLifecycleOwner, Observer {
            hideLoading()
            val passData = DigitalCheckoutPassData.Builder()
                    .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                    .categoryId(categoryId)
                    .instantCheckout("0")
                    .isPromo("0")
                    .operatorId(it.operatorId)
                    .productId(it.productId)
                    .idemPotencyKey(RechargeCCUtil.generateIdemPotencyCheckout(userSession.userId))
                    .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                    .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                    .isFromPDP(true)
                    .build()
            checkoutPassDataState = passData

            navigateToCart(passData)
        })

        rechargeSubmitCCViewModel.errorSubmitCreditCard.observe(viewLifecycleOwner, Observer {
            hideLoading()
            showErrorToaster(it)
        })

        rechargeCCViewModel.favoriteChipsData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetFavoriteChips(it.data)
                is RechargeNetworkResult.Loading -> {
                    cc_widget_client_number.setFilterChipShimmer(true)
                }
            }
        })

        rechargeCCViewModel.autoCompleteData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetAutoComplete(it.data)
            }
        })


        rechargeCCViewModel.prefillData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetPrefill(it.data)
            }
        })
    }

    private fun getTickerData() {
        rechargeCCViewModel.getMenuDetail(RechargeCCGqlQuery.catalogMenuDetail, menuId)
    }

    private fun renderTicker(tickers: List<TickerCreditCard>) {
        if (tickers.isNotEmpty()) {
            val messages = ArrayList<TickerData>()
            for (item in tickers) {
                messages.add(TickerData(item.name, item.content,
                        when (item.type) {
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }))
            }
            context?.run {
                cc_ticker_view.addPagerView(TickerPagerAdapter(this, messages), messages)
            }
            cc_ticker_view.visibility = View.VISIBLE
        } else {
            cc_ticker_view.visibility = View.GONE
        }
    }

    private fun checkPrefixCreditCardNumber(clientNumber: String) {
        rechargeCCViewModel.prefixData.prefixSelect.prefixes.isEmpty().let {
            if (it) {
                rechargeCCViewModel.getPrefixes(RechargeCCGqlQuery.catalogPrefix, menuId)
            } else {
                if (clientNumber.length > RechargeCCConst.MIN_VALID_LENGTH) {
                    rechargeCCViewModel.run {
                        checkPrefixNumber(clientNumber)
                        cancelValidatorJob()
                        validateCCNumber(clientNumber)
                    }
                } else {
                    cc_widget_client_number.run {
                        clearErrorState()
                        setLoading(false)
                        hideOperatorIcon()
                    }
                }
            }
        }
    }

    private fun showErrorToaster(error: Throwable) {
        KeyboardHandler.hideSoftKeyboard(activity)
        view?.run {
            Toaster.build(this, ErrorHandler.getErrorMessage(requireContext(), error),
                    Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    private fun dialogConfirmation() {
        if (operatorIdSelected.isNotEmpty() && productIdSelected.isNotEmpty()) {
            context?.let {
                val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.setTitle(getString(R.string.cc_title_dialog))
                dialog.setDescription(getString(R.string.cc_desc_dialog))
                dialog.setPrimaryCTAText(getString(R.string.cc_cta_btn_primary))
                dialog.setSecondaryCTAText(getString(R.string.cc_cta_btn_secondary))
                dialog.setPrimaryCTAClickListener {
                    dialog.dismiss()
                    creditCardAnalytics.clickToContinueCheckout(
                            categoryId, operatorIdSelected, userSession.userId)
                    submitCreditCard(categoryId, operatorIdSelected,
                            productIdSelected, cc_widget_client_number.getInputNumber())
                }
                dialog.setSecondaryCTAClickListener {
                    creditCardAnalytics.clickBackOnConfirmationPage(
                            categoryId, operatorIdSelected, userSession.userId)
                    dialog.dismiss()
                }
                dialog.show()
            }
        } else {
            showErrorToaster(MessageErrorException(getString(R.string.cc_error_default_message)))
        }
    }

    private fun instantCheckout() {
        showLoading()
        if (userSession.isLoggedIn) {
            arguments?.let {
                val signature = it.getString(PARAM_SIGNATURE) ?: ""
                val operatorId = it.getString(PARAM_OPERATOR_ID) ?: ""
                val productId = it.getString(PARAM_PRODUCT_ID) ?: ""
                val identifier = it.getString(PARAM_IDENTIFIER) ?: ""
                val clientNumber = it.getString(PARAM_CLIENT_NUMBER) ?: ""

                val mapParam = rechargeSubmitCCViewModel.createPcidssParamFromApplink(clientNumber, operatorId,
                        productId, userSession.userId, signature, identifier)
                rechargeSubmitCCViewModel.submitCreditCard(mapParam)
            }
        } else {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN_INSTANT_CHECKOUT)
        }
    }

    private fun submitCreditCard(categoryId: String, operatorId: String, productId: String, clientNumber: String) {
        showLoading()
        if (userSession.isLoggedIn) {
            val mapParam = if (token.isNotEmpty() && clientNumber.isMasked()) {
                rechargeSubmitCCViewModel.createMaskedMapParam(clientNumber, operatorId, productId,
                    userSession.userId, token)
            } else {
                rechargeSubmitCCViewModel.createMapParam(clientNumber, operatorId, productId,
                    userSession.userId)
            }

            rechargeSubmitCCViewModel.postCreditCard(RechargeCCGqlQuery.rechargeCCSignature, categoryId, mapParam)
        } else {
            hideLoading()
            navigateUserLogin()
        }
    }

    private fun getFavoriteNumbers(favoriteNumberTypes: List<FavoriteNumberType>) {
        val currCategoryId = categoryId.toIntOrZero()
        if (currCategoryId != 0) {
            rechargeCCViewModel.setFavoriteNumberLoading()
            rechargeCCViewModel.getFavoriteNumbers(
                listOf(currCategoryId),
                favoriteNumberTypes
            )
        }
    }

    private fun onSuccessGetFavoriteChips(favoriteChips: List<FavoriteChipModel>) {
        cc_widget_client_number.run {
            setFilterChipShimmer(false, favoriteChips.isEmpty())
            setFavoriteNumber(
                RechargeCCMapper.mapFavoriteChipsToWidgetModels(favoriteChips))
        }
    }

    private fun onSuccessGetAutoComplete(autoComplete: List<AutoCompleteModel>) {
        cc_widget_client_number.setAutoCompleteList(
            RechargeCCMapper.mapAutoCompletesToWidgetModels(autoComplete))
    }

    private fun onSuccessGetPrefill(prefill: PrefillModel) {
        val clientNumber = cc_widget_client_number.getInputNumber()
        if (clientNumber.isEmpty()) {
            cc_widget_client_number.run {
                setContactName(prefill.clientName)
                setInputNumber(prefill.clientNumber)
                token = prefill.token
            }
        }
    }

    private fun navigateUserLogin() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun hideLoading() {
        cc_progress_bar.visibility = View.GONE
    }

    private fun showLoading() {
        cc_progress_bar.visibility = View.VISIBLE
    }

    private fun navigateToCart(passData: DigitalCheckoutPassData) {
        trackAddToCartEvent()
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConsInternalDigital.CHECKOUT_DIGITAL)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
            startActivityForResult(intent, REQUEST_CODE_CART)
        }
    }

    private fun trackAddToCartEvent() {
        val creditCardSelected = rechargeCCViewModel.creditCardSelected.value
                ?: RechargeCreditCard()
        creditCardAnalytics.addToCart(userSession.userId, rechargeCCViewModel.categoryName, categoryId,
                creditCardSelected.prefixName, creditCardSelected.operatorId)
    }

    //region CreditCardActionListener
    override fun onClickNextButton(clientNumber: String) {
        creditCardAnalytics.clickToConfirmationPage(
            categoryId, operatorIdSelected, userSession.userId
        )
        dialogConfirmation()
    }

    override fun onManualInput() {
        token = ""
    }

    override fun onNavigateTokoCardWebView() {
        // TODO: [Misael] Add tracker here
    }

    //endregion

    //region ClientNumberInputFieldListener
    override fun onRenderOperator(isDelayed: Boolean, isManualInput: Boolean) {
        checkPrefixCreditCardNumber(cc_widget_client_number.getInputNumber())
    }

    override fun onClearInput() {
        operatorIdSelected = ""
        productIdSelected = ""
    }

    override fun onClickNavigationIcon() {
        // do nothing
    }

    override fun isKeyboardShown(): Boolean {
        return keyboardWatcher.isKeyboardOpened
    }
    //endregion

    //region FilterChipListener
    override fun onClickIcon(isSwitchChecked: Boolean) {
        // isSwitchChecked is not used here
        navigateToFavoriteNumberPage(
            cc_widget_client_number.getInputNumber(),
            arrayListOf(categoryId),
            rechargeCCViewModel.categoryName,
            rechargeCCViewModel.loyaltyStatus
        )
    }

    override fun onShowFilterChip(isLabeled: Boolean) {
        if (isLabeled) {
            creditCardAnalytics.impressionFavoriteContactChips(
                rechargeCCViewModel.categoryName,
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        } else {
            creditCardAnalytics.impressionFavoriteNumberChips(
                rechargeCCViewModel.categoryName,
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        }
    }

    override fun onClickFilterChip(isLabeled: Boolean, favorite: RechargeClientNumberChipModel) {
        token = favorite.token
        if (isLabeled) {
            creditCardAnalytics.clickFavoriteContactChips(
                rechargeCCViewModel.categoryName,
                rechargeCCViewModel.creditCardSelected.value?.prefixName ?: "",
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId,
            )
        } else {
            creditCardAnalytics.clickFavoriteNumberChips(
                rechargeCCViewModel.categoryName,
                rechargeCCViewModel.creditCardSelected.value?.prefixName ?: "",
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        }
    }
    //endregion

    //region AutoCompleteListener
    override fun onClickAutoComplete(favorite: TopupBillsAutoCompleteContactModel) {
        token = favorite.token
        if (favorite.name.isNotEmpty()) {
            creditCardAnalytics.clickFavoriteContactAutoComplete(
                rechargeCCViewModel.categoryName,
                rechargeCCViewModel.creditCardSelected.value?.prefixName ?: "",
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        } else {
            creditCardAnalytics.clickFavoriteNumberAutoComplete(
                rechargeCCViewModel.categoryName,
                rechargeCCViewModel.creditCardSelected.value?.prefixName ?: "",
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        }
    }
    //endregion

    private fun navigateToFavoriteNumberPage(
        clientNumber: String,
        dgCategoryIds: ArrayList<String>,
        categoryName: String,
        loyaltyStatus: String,
    ) {
        context?.let {
            val intent = TopupBillsPersoFavoriteNumberActivity.createInstance(
                it,
                clientNumber,
                dgCategoryIds,
                arrayListOf(),
                categoryName,
                loyaltyStatus,
                FavoriteNumberPageConfig.CREDIT_CARD
            )
            val requestCode = RechargeCCConst.REQUEST_CODE_FAVORITE_NUMBER
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CART -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(DigitalExtraParam.EXTRA_MESSAGE)) {
                        val error = data.getSerializableExtra(DigitalExtraParam.EXTRA_MESSAGE) as Throwable
                        if (!TextUtils.isEmpty(error.message)) {
                            showErrorToaster(error)
                        }
                    }
                }
            }
            REQUEST_CODE_LOGIN -> {
                if (userSession.isLoggedIn) {
                    submitCreditCard(categoryId, operatorIdSelected,
                            productIdSelected, cc_widget_client_number.getInputNumber())
                }
            }

            REQUEST_CODE_LOGIN_INSTANT_CHECKOUT -> {
                hideLoading()
                if (userSession.isLoggedIn) {
                    instantCheckout()
                }
            }

            REQUEST_CODE_FAVORITE_NUMBER -> {
                if (data != null) {
                    if (data != null) {
                        val orderClientNumber =
                            data.getParcelableExtra<Parcelable>(EXTRA_CALLBACK_CLIENT_NUMBER) as TopupBillsSavedNumber

                        handleCallbackFavoriteNumber(
                            orderClientNumber.clientName,
                            orderClientNumber.clientNumber,
                            orderClientNumber.operatorId,
                            orderClientNumber.inputNumberActionTypeIndex,
                        )
                        token = orderClientNumber.token
                    } else {
                        handleCallbackAnySavedNumberCancel()
                    }
                    getFavoriteNumbers(
                        listOf(
                            FavoriteNumberType.CHIP,
                            FavoriteNumberType.LIST
                        )
                    )
                }
            }
        }
    }

    private fun handleCallbackFavoriteNumber(
        clientName: String,
        clientNumber: String,
        operatorId: String,
        inputNumberActionTypeIndex: Int
    ) {
        cc_widget_client_number.run {
            setContactName(clientName)
            setInputNumber(clientNumber)
        }
    }

    private fun handleCallbackAnySavedNumberCancel() {
        cc_widget_client_number.clearFocusAutoComplete()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(RECHARGE_CC_PAGE_PERFORMANCE)
    }

    companion object {

        const val EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA"

        private const val CATEGORY_ID = "category_id"
        private const val MENU_ID = "menu_id"

        const val RECHARGE_CC_PAGE_PERFORMANCE = "dg_tagihan_cc_pdp"

        const val REQUEST_CODE_CART = 1000
        const val REQUEST_CODE_LOGIN = 1001
        const val REQUEST_CODE_LOGIN_INSTANT_CHECKOUT = 1020

        fun newInstance(categoryId: String, menuId: String, operatorId: String, productId: String,
                        signature: String, identifier: String, clientNumber: String): Fragment {
            val fragment = RechargeCCFragment()
            val bundle = Bundle()
            bundle.putString(CATEGORY_ID, categoryId)
            bundle.putString(MENU_ID, menuId)
            bundle.putString(PARAM_OPERATOR_ID, operatorId)
            bundle.putString(PARAM_PRODUCT_ID, productId)
            bundle.putString(PARAM_SIGNATURE, signature)
            bundle.putString(PARAM_IDENTIFIER, identifier)
            bundle.putString(PARAM_CLIENT_NUMBER, clientNumber)
            fragment.arguments = bundle
            return fragment
        }
    }
}
