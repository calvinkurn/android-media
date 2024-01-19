package com.tokopedia.recharge_credit_card

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoFavoriteNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberPageConfig
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common.topupbills.view.adapter.TopupBillsProductTabAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsTabItem
import com.tokopedia.common.topupbills.view.model.TopupBillsTrackRecentTransaction
import com.tokopedia.common.topupbills.widget.TopupBillsRecentNumberListener
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.bottomsheet.DigitalDppoConsentBottomSheet
import com.tokopedia.common_digital.common.util.DigitalKeyboardWatcher
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_component.listener.ClientNumberFilterChipListener
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
import com.tokopedia.recharge_credit_card.databinding.FragmentRechargeCcBinding
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCPromo
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCRecommendation
import com.tokopedia.recharge_credit_card.datamodel.TickerCreditCard
import com.tokopedia.recharge_credit_card.di.RechargeCCInstance
import com.tokopedia.recharge_credit_card.pcidss.model.PcidssOperator
import com.tokopedia.recharge_credit_card.pcidss.model.PcidssPrefix
import com.tokopedia.recharge_credit_card.pcidss.model.PcidssPrefixSelect
import com.tokopedia.recharge_credit_card.pcidss.model.PcidssPrefixValidatorModel
import com.tokopedia.recharge_credit_card.pcidss.model.PrefixAttributes
import com.tokopedia.recharge_credit_card.pcidss.model.Validation
import com.tokopedia.recharge_credit_card.pcidss.widgets.PcidssInputFieldWidget
import com.tokopedia.recharge_credit_card.util.RechargeCCConst
import com.tokopedia.recharge_credit_card.util.RechargeCCConst.REQUEST_CODE_FAVORITE_NUMBER
import com.tokopedia.recharge_credit_card.util.RechargeCCGqlQuery
import com.tokopedia.recharge_credit_card.util.RechargeCCMapper
import com.tokopedia.recharge_credit_card.util.RechargeCCUtil
import com.tokopedia.recharge_credit_card.viewmodel.RechargeCCViewModel
import com.tokopedia.recharge_credit_card.viewmodel.RechargeSubmitCCViewModel
import com.tokopedia.recharge_credit_card.widget.RechargeCCBankListWidget
import com.tokopedia.recharge_credit_card.widget.RechargeCCClientNumberWidget
import com.tokopedia.recharge_credit_card.widget.util.RechargeCCWidgetMapper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class RechargeCCFragment :
    BaseDaggerFragment(),
    MenuProvider,
    ClientNumberFilterChipListener,
    RechargeCCClientNumberWidget.CreditCardActionListener,
    RechargeCCBankListWidget.RechargeCCBankListListener,
    TopupBillsRecentNumberListener,
    RechargeCCPromoFragment.RechargeCCPromoItemListener,
    PcidssInputFieldWidget.PcidssInputFieldWidgetListener {
    private lateinit var rechargeCCViewModel: RechargeCCViewModel
    private lateinit var rechargeSubmitCCViewModel: RechargeSubmitCCViewModel
    private lateinit var saveInstanceManager: SaveInstanceCacheManager
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private val keyboardWatcher = DigitalKeyboardWatcher()

    private var binding by autoClearedNullable<FragmentRechargeCcBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var creditCardAnalytics: CreditCardAnalytics

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var operatorIdSelected: String = ""
    private var productIdSelected: String = ""
    private var categoryId: String = ""
    private var menuId: String = ""
    private var checkoutPassDataState: DigitalCheckoutPassData? = null

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
            checkoutPassDataState = saveInstanceManager.get(
                EXTRA_STATE_CHECKOUT_PASS_DATA,
                DigitalCheckoutPassData::class.java,
                null
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        checkoutPassDataState?.let {
            saveInstanceManager.put(EXTRA_STATE_CHECKOUT_PASS_DATA, it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRechargeCcBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun setupToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializedViewModel()
        setupKeyboardWatcher()
        setupToolbarMenu()
        getDataBundle()
        getTickerData()
        getFavoriteNumbers(
            listOf(
                FavoriteNumberType.PREFILL,
                FavoriteNumberType.CHIP,
                FavoriteNumberType.LIST
            )
        )
        rechargeCCViewModel.getDppoConsent(categoryId.toIntOrZero())

        binding?.ccWidgetClientNumber?.run {
            setInputFieldType(InputFieldType.CreditCard, isGoogleWalletAutofillEnabled())
            setInputFieldStaticLabel(context.getString(R.string.cc_label_input_number))
            setInputFieldListener(this@RechargeCCFragment)
            setFilterChipListener(this@RechargeCCFragment)
            setCreditCardATCListener(this@RechargeCCFragment)
        }

        binding?.ccWidgetBankList?.setListener(this@RechargeCCFragment)
        observeData()
        creditCardAnalytics.impressionInitialPage(userSession.userId)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        if (childFragment is RechargeCCRecommendationFragment) {
            childFragment.setListener(this)
        } else if (childFragment is RechargeCCPromoFragment) {
            childFragment.setListener(this)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        try {
            val dppoConsentData = rechargeCCViewModel.dppoConsent.value
            menuInflater.inflate(R.menu.menu, menu)
            if (dppoConsentData is Success && dppoConsentData.data.description.isNotEmpty()) {
                menu.showConsentIcon()
                menu.setupConsentIcon(dppoConsentData.data.description)
                menu.setupKebabIcon()
            } else {
                menu.hideConsentIcon()
                menu.setupKebabIcon()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    private fun setupKeyboardWatcher() {
        binding?.ccWidgetClientNumber?.rootView?.let {
            keyboardWatcher.listen(
                it,
                object : DigitalKeyboardWatcher.Listener {
                    override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                        // do nothing
                    }

                    override fun onKeyboardHidden() {
                        // do nothing
                    }
                }
            )
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
        rechargeCCViewModel.menuDetail.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is RechargeNetworkResult.Success -> {
                        renderTicker(it.data.tickers)
                        if (isEnablePromoRecomSection()) {
                            renderPromoAndRecommendation(
                                it.data.recommendations,
                                it.data.promos
                            )
                        }
                        performanceMonitoring.stopTrace()
                    }
                    is RechargeNetworkResult.Fail -> {
                        // TODO: show global error
                    }
                    else -> {
                        // no op
                    }
                }
            }
        )

        rechargeCCViewModel.prefixSelect.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is RechargeNetworkResult.Success -> {
                        binding?.ccWidgetClientNumber?.setPrefixValidator(
                            PcidssPrefixValidatorModel(
                                prefixSelect = PcidssPrefixSelect(
                                    it.data.prefixSelect.text,
                                    it.data.prefixSelect.validations.map { validation ->
                                        Validation(
                                            title = validation.title,
                                            message = validation.message,
                                            rule = validation.rule
                                        )
                                    },
                                    it.data.prefixSelect.prefixes.map { prefix ->
                                        PcidssPrefix(
                                            key = prefix.key,
                                            value = prefix.value,
                                            operator = PcidssOperator(
                                                id = prefix.operator.id,
                                                attribute = PrefixAttributes(
                                                    imageUrl = prefix.operator.attribute.imageUrl,
                                                    defaultProductId = prefix.operator.attribute.defaultProductId,
                                                    name = prefix.operator.attribute.name
                                                )
                                            )
                                        )
                                    }
                                )
                            )
                        )
                    }
                    is RechargeNetworkResult.Fail -> {
                        showErrorToaster(it.error)
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )

        rechargeSubmitCCViewModel.signature.observe(viewLifecycleOwner) { signature ->
            binding?.ccWidgetClientNumber?.submitCheckout(
                rechargeCCViewModel.getPcidssCustomHeaders(),
                operatorIdSelected,
                productIdSelected,
                signature,
                userSession.userId
            )
        }

        rechargeSubmitCCViewModel.errorSignature.observe(
            viewLifecycleOwner,
            Observer {
                hideLoading()
                showErrorToaster(it)
            }
        )

        rechargeCCViewModel.favoriteChipsData.observe(viewLifecycleOwner) {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetFavoriteChips(it.data)
                is RechargeNetworkResult.Loading -> {
                    binding?.ccWidgetClientNumber?.setFilterChipShimmer(true)
                }
                else -> {
                    // no-op
                }
            }
        }

        rechargeCCViewModel.autoCompleteData.observe(viewLifecycleOwner) {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetAutoComplete(it.data)
                else -> {
                    // no-op
                }
            }
        }

        rechargeCCViewModel.prefillData.observe(viewLifecycleOwner) {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetPrefill(it.data)
                else -> {
                    // no-op
                }
            }
        }

        rechargeCCViewModel.dppoConsent.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    activity?.invalidateOptionsMenu()
                }
                is Fail -> {}
            }
        }
    }

    private fun getTickerData() {
        rechargeCCViewModel.getMenuDetail(RechargeCCGqlQuery.catalogMenuDetail, menuId)
    }

    private fun renderTicker(tickers: List<TickerCreditCard>) {
        binding?.run {
            if (tickers.isNotEmpty()) {
                val messages = ArrayList<TickerData>()
                for (item in tickers) {
                    messages.add(
                        TickerData(
                            item.name,
                            item.content,
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
                context?.run {
                    ccTickerView.addPagerView(TickerPagerAdapter(this, messages), messages)
                }
                ccTickerView.visibility = View.VISIBLE
            } else {
                ccTickerView.visibility = View.GONE
            }
        }
    }

    private fun renderPromoAndRecommendation(recommendations: List<RechargeCCRecommendation>, promos: List<RechargeCCPromo>) {
        val menuTab = mutableListOf<TopupBillsTabItem>()
        val isShowTitle = false

        RechargeCCRecommendationFragment.newInstance(
            RechargeCCMapper.mapRechargeCCRecomToTopupRecom(recommendations),
            isShowTitle
        ).also {
            menuTab.add(TopupBillsTabItem(it, RECENT_TRANSACTION_LABEL))
        }

        RechargeCCPromoFragment.newInstance(
            RechargeCCMapper.mapRechargeCCPromoToTopupPromo(promos),
            isShowTitle
        ).also {
            menuTab.add(TopupBillsTabItem(it, PROMO_LIST_LABEL))
        }

        binding?.run {
            val pagerAdapter = TopupBillsProductTabAdapter(this@RechargeCCFragment, menuTab)
            ccViewPager.adapter = pagerAdapter
            ccViewPager.offscreenPageLimit = menuTab.size
            ccTabLayout.customTabMode = TabLayout.MODE_FIXED
            ccTabLayout.customTabGravity = TabLayout.GRAVITY_FILL

            ccTabLayout.getUnifyTabLayout().removeAllTabs()
            for (item in menuTab) {
                ccTabLayout.addNewTab(item.title)
            }
            ccTabLayout.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    if (tab.position == RECENT_TRANSACTION_POSITION) {
                        creditCardAnalytics.sendClickLastTransactionTabEvent(
                            rechargeCCViewModel.categoryName,
                            rechargeCCViewModel.loyaltyStatus
                        )
                    } else if (tab.position == PROMO_LIST_POSITION) {
                        creditCardAnalytics.sendClickPromoTabEvent(
                            rechargeCCViewModel.categoryName,
                            rechargeCCViewModel.loyaltyStatus
                        )
                    }
                    ccViewPager.setCurrentItem(tab.position, true)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // do nothing
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // do nothing
                }
            })
            ccViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    ccTabLayout.getUnifyTabLayout().getTabAt(position)?.let {
                        it.select()
                    }
                    super.onPageSelected(position)
                    val myFragment = childFragmentManager.findFragmentByTag("f$position")
                    myFragment?.view?.let { updatePagerHeightForChild(it, ccViewPager) }
                }

                private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
                    view.post {
                        if (view != null) {
                            val wMeasureSpec =
                                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                            view.measure(wMeasureSpec, hMeasureSpec)

                            if (pager.layoutParams.height != view.measuredHeight) {
                                pager.layoutParams = (pager.layoutParams)
                                    .also { lp ->
                                        lp.height = view.measuredHeight
                                    }
                            }
                        }
                    }
                }
            })

            ccTabLayout.show()
            ccViewPager.show()
        }
    }

    override fun onClickRecentNumber(
        topupBillsRecommendation: TopupBillsRecommendation,
        categoryId: String,
        position: Int
    ) {
        topupBillsRecommendation.position = position
        binding?.ccWidgetClientNumber?.run {
            setInputNumber(topupBillsRecommendation.clientNumber, topupBillsRecommendation.token)
            operatorIdSelected = topupBillsRecommendation.operatorId
            productIdSelected = topupBillsRecommendation.productId
            showDialogConfirmation()
        }
        creditCardAnalytics.sendClickLastTransactionListEvent(
            rechargeCCViewModel.categoryName,
            rechargeCCViewModel.loyaltyStatus,
            position
        )
    }

    override fun onTrackImpressionRecentList(topupBillsTrackRecentList: List<TopupBillsTrackRecentTransaction>) {
        // do nothing
    }

    override fun onCopiedPromoCode(promoId: String, voucherCode: String, position: Int) {
        creditCardAnalytics.sendClickSalinPromoDigitalEvent(
            rechargeCCViewModel.categoryName,
            rechargeCCViewModel.loyaltyStatus,
            voucherCode,
            position
        )
    }

    private fun showErrorToaster(error: Throwable) {
        KeyboardHandler.hideSoftKeyboard(activity)
        view?.run {
            Toaster.build(
                this,
                ErrorHandler.getErrorMessage(requireContext(), error),
                Snackbar.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
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

                binding?.ccWidgetClientNumber?.submitInstantCheckout(
                    rechargeCCViewModel.getPcidssCustomHeaders(),
                    clientNumber,
                    operatorId,
                    productId,
                    userSession.userId,
                    signature,
                    identifier
                )
            }
        } else {
            val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN_INSTANT_CHECKOUT)
        }
    }

    private fun postCreditCard(categoryId: String) {
        showLoading()
        if (userSession.isLoggedIn) {
            if (operatorIdSelected.isNotEmpty() && productIdSelected.isNotEmpty()) {
                rechargeSubmitCCViewModel.postCreditCard(RechargeCCGqlQuery.rechargeCCSignature, categoryId)
            } else {
                showErrorToaster(MessageErrorException(getString(R.string.cc_error_default_message)))
            }
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
        binding?.ccWidgetClientNumber?.run {
            setFilterChipShimmer(false, favoriteChips.isEmpty())
            setFavoriteNumber(
                RechargeCCWidgetMapper.mapFavoriteChipsToWidgetModels(favoriteChips)
            )
        }
    }

    private fun onSuccessGetAutoComplete(autoComplete: List<AutoCompleteModel>) {
        binding?.ccWidgetClientNumber?.setAutoCompleteList(
            RechargeCCWidgetMapper.mapAutoCompletesToWidgetModels(autoComplete)
        )
    }

    private fun onSuccessGetPrefill(prefill: PrefillModel) {
        binding?.run {
            if (ccWidgetClientNumber.isInputFieldEmpty()) {
                ccWidgetClientNumber.run {
                    setContactName(prefill.clientName)
                    setInputNumber(prefill.clientNumber, prefill.token)
                }
            }
        }
    }

    private fun navigateUserLogin() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun hideLoading() {
        binding?.ccProgressBar?.visibility = View.GONE
    }

    private fun showLoading() {
        binding?.ccProgressBar?.visibility = View.VISIBLE
    }

    private fun navigateToCart(passData: DigitalCheckoutPassData, operatorName: String) {
        creditCardAnalytics.addToCart(
            userSession.userId,
            rechargeCCViewModel.categoryName,
            categoryId,
            operatorName,
            passData.operatorId.toEmptyStringIfNull()
        )

        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConsInternalDigital.CHECKOUT_DIGITAL)
            intent.putExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA, passData)
            startActivityForResult(intent, REQUEST_CODE_CART)
        }
    }

    //region RechargeCCBankListListener
    override fun onClickBankList() {
        activity?.let {
            val bottomSheetBankList = CCBankListBottomSheet.newBottomSheet(categoryId)
            bottomSheetBankList.show(it.supportFragmentManager, "Bank list")
        }
    }
    //endregion

    //region CreditCardActionListener
    override fun onClickNextButton() {
        creditCardAnalytics.clickToConfirmationPage(
            categoryId,
            operatorIdSelected,
            userSession.userId
        )
        binding?.ccWidgetClientNumber?.showDialogConfirmation()
    }

    override fun onNavigateTokoCardWebView() {
        // TODO: [Misael] Add tracker here
    }

    //endregion

    //region FilterChipListener
    override fun onClickIcon(isSwitchChecked: Boolean) {
        // isSwitchChecked is not used here
        binding?.run {
            navigateToFavoriteNumberPage(
                arrayListOf(categoryId),
                rechargeCCViewModel.categoryName,
                rechargeCCViewModel.loyaltyStatus
            )
        }
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
        binding?.ccWidgetClientNumber?.setInputNumber(favorite.clientNumber, favorite.token)
        if (isLabeled) {
            creditCardAnalytics.clickFavoriteContactChips(
                rechargeCCViewModel.categoryName,
                favorite.operatorName,
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        } else {
            creditCardAnalytics.clickFavoriteNumberChips(
                rechargeCCViewModel.categoryName,
                favorite.operatorName,
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        }
    }
    //endregion

    // region PcidssInputFieldWidgetListener
    override fun onTextChanged(isManualInput: Boolean) {
    }

    override fun onClickClearIcon() {
        operatorIdSelected = ""
        productIdSelected = ""
        binding?.ccWidgetClientNumber?.disablePrimaryButton()
    }

    override fun onClickAutoCompleteItem(name: String, operatorName: String) {
        binding?.ccWidgetClientNumber?.setContactName(name)
        if (name.isNotEmpty()) {
            creditCardAnalytics.clickFavoriteContactAutoComplete(
                rechargeCCViewModel.categoryName,
                operatorName,
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        } else {
            creditCardAnalytics.clickFavoriteNumberAutoComplete(
                rechargeCCViewModel.categoryName,
                operatorName,
                rechargeCCViewModel.loyaltyStatus,
                userSession.userId
            )
        }
    }

    override fun isSoftKeyboardShown(): Boolean {
        return keyboardWatcher.isKeyboardOpened
    }

    override fun onDialogSubmitCreditCard() {
        showLoading()
        creditCardAnalytics.clickToContinueCheckout(
            categoryId,
            operatorIdSelected,
            userSession.userId
        )
        postCreditCard(categoryId)
    }

    override fun onDialogClickBack() {
        creditCardAnalytics.clickBackOnConfirmationPage(
            categoryId,
            operatorIdSelected,
            userSession.userId
        )
    }

    override fun onSuccessSubmitCreditCard(productId: String, operatorId: String, operatorName: String) {
        hideLoading()
        val passData = DigitalCheckoutPassData.Builder()
            .action(DigitalCheckoutPassData.DEFAULT_ACTION)
            .categoryId(categoryId)
            .instantCheckout("0")
            .isPromo("0")
            .operatorId(operatorId)
            .productId(productId)
            .idemPotencyKey(RechargeCCUtil.generateIdemPotencyCheckout(userSession.userId))
            .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
            .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
            .isFromPDP(true)
            .build()
        checkoutPassDataState = passData

        navigateToCart(passData, operatorName)
    }

    override fun onFailSubmitCreditCard(errorMessage: String) {
        hideLoading()
        showErrorToaster(MessageErrorException(errorMessage))
    }

    override fun onSelectedCreditCardNotSupported() {
        binding?.ccWidgetClientNumber?.hideOperatorIcon()
        productIdSelected = ""
        operatorIdSelected = ""
    }

    override fun onSelectedCreditCardSupported(
        operatorId: String,
        productId: String,
        imageUrl: String
    ) {
        binding?.ccWidgetClientNumber?.showOperatorIcon(imageUrl)
        productIdSelected = productId
        operatorIdSelected = operatorId
    }

    override fun onCreditCardPrefixValidated() {
        binding?.ccWidgetClientNumber?.enablePrimaryButton()
    }

    override fun onCheckPrefixEmpty() {
        rechargeCCViewModel.getPrefixes(RechargeCCGqlQuery.catalogPrefix, menuId)
    }

    override fun onCreditCardNumberBelowMinLength() {
        binding?.ccWidgetClientNumber?.hideOperatorIcon()
    }
    //endregion

    private fun navigateToFavoriteNumberPage(
        dgCategoryIds: ArrayList<String>,
        categoryName: String,
        loyaltyStatus: String
    ) {
        context?.let {
            val intent = TopupBillsPersoFavoriteNumberActivity.createInstance(
                it,
                "",
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
                    binding?.run {
                        ccWidgetClientNumber.showDialogConfirmation()
                    }
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
                            orderClientNumber.token
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
                }
            }
        }
    }

    private fun handleCallbackFavoriteNumber(
        clientName: String,
        clientNumber: String,
        token: String
    ) {
        binding?.ccWidgetClientNumber?.run {
            setContactName(clientName)
            setInputNumber(clientNumber, token)
        }
    }

    private fun handleCallbackAnySavedNumberCancel() {
        binding?.ccWidgetClientNumber?.clearFocusAutoComplete()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(RECHARGE_CC_PAGE_PERFORMANCE)
    }

    private fun isEnablePromoRecomSection(): Boolean {
        return remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_RECHARGE_CC_PROMO_RECOM, true)
    }

    private fun isGoogleWalletAutofillEnabled(): Boolean {
        return remoteConfig.getBoolean(
            RemoteConfigKey.ANDROID_CREDIT_CARD_ENABLE_AUTOFILL_GOOGLE_WALLET,
            true
        )
    }

    private fun Menu.hideConsentIcon() {
        findItem(R.id.action_dppo_consent).isVisible = false
    }

    private fun Menu.showConsentIcon() {
        findItem(R.id.action_dppo_consent).isVisible = true
    }

    private fun Menu.setupConsentIcon(description: String) {
        if (description.isNotEmpty()) {
            context?.let { ctx ->
                val iconUnify = getIconUnifyDrawable(
                    ctx,
                    IconUnify.INFORMATION,
                    ContextCompat.getColor(ctx, unifyprinciplesR.color.Unify_NN0)
                )
                iconUnify?.toBitmap()?.let {
                    getItem(0).setOnMenuItemClickListener {
                        val bottomSheet = DigitalDppoConsentBottomSheet(description)
                        bottomSheet.show(childFragmentManager)
                        true
                    }
                    getItem(0).icon = BitmapDrawable(
                        ctx.resources,
                        Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true)
                    )
                }
            }
        }
    }

    private fun Menu.setupKebabIcon() {
        context?.let { ctx ->
            val iconUnify = getIconUnifyDrawable(
                ctx,
                IconUnify.LIST_TRANSACTION,
                ContextCompat.getColor(ctx, unifyprinciplesR.color.Unify_NN0)
            )
            iconUnify?.toBitmap()?.let {
                getItem(1).setOnMenuItemClickListener {
                    RouteManager.route(context, ApplinkConst.DIGITAL_ORDER)
                    true
                }
                getItem(1).icon = BitmapDrawable(
                    ctx.resources,
                    Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true)
                )
            }
        }
    }

    companion object {

        const val EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA"

        private const val CATEGORY_ID = "category_id"
        private const val MENU_ID = "menu_id"

        const val RECHARGE_CC_PAGE_PERFORMANCE = "dg_tagihan_cc_pdp"
        const val PROMO_LIST_LABEL = "Promo"
        const val RECENT_TRANSACTION_LABEL = "Transaksi Terakhir"
        const val RECENT_TRANSACTION_POSITION = 0
        const val PROMO_LIST_POSITION = 1

        const val REQUEST_CODE_CART = 1000
        const val REQUEST_CODE_LOGIN = 1001
        const val REQUEST_CODE_LOGIN_INSTANT_CHECKOUT = 1020

        private const val TOOLBAR_ICON_SIZE = 64

        fun newInstance(
            categoryId: String,
            menuId: String,
            operatorId: String,
            productId: String,
            signature: String,
            identifier: String,
            clientNumber: String
        ): Fragment {
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
