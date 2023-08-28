package com.tokopedia.recharge_pdp_emoney.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.RechargePrefix
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.bottomsheet.DigitalDppoConsentBottomSheet
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.showUnifyError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.FragmentEmoneyPdpBinding
import com.tokopedia.recharge_pdp_emoney.databinding.ItemEmoneyProductBinding
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivity
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPdpFragmentPagerAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder
import com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet.EmoneyMenuBottomSheets
import com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet.EmoneyProductDetailBottomSheet
import com.tokopedia.recharge_pdp_emoney.presentation.viewmodel.EmoneyPdpViewModel
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyPdpBottomCheckoutWidget
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyPdpHeaderViewWidget
import com.tokopedia.recharge_pdp_emoney.presentation.widget.EmoneyPdpInputCardNumberWidget
import com.tokopedia.recharge_pdp_emoney.utils.EmoneyPdpAnalyticsUtils
import com.tokopedia.recharge_pdp_emoney.utils.EmoneyPdpMapper
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject
import kotlin.math.abs

/**
 * @author by jessica on 29/03/21
 */

open class EmoneyPdpFragment :
    BaseDaggerFragment(),
    EmoneyPdpHeaderViewWidget.ActionListener,
    EmoneyPdpInputCardNumberWidget.ActionListener,
    EmoneyPdpProductViewHolder.ActionListener,
    EmoneyPdpBottomCheckoutWidget.ActionListener,
    EmoneyMenuBottomSheets.MenuListener
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )
    }
    private val topUpBillsViewModel by lazy { viewModelFragmentProvider.get(TopupBillsViewModel::class.java) }
    protected val emoneyPdpViewModel by lazy { viewModelFragmentProvider.get(EmoneyPdpViewModel::class.java) }
    private val addToCartViewModel by lazy { viewModelFragmentProvider.get(DigitalAddToCartViewModel::class.java) }

    @Inject
    lateinit var rechargeAnalytics: RechargeAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var emoneyCardNumber = ""
    private var issuerId = ""
    lateinit var detailPassData: DigitalCategoryDetailPassData

    private lateinit var localCacheHandler: LocalCacheHandler

    private val coachMark by lazy { CoachMark2(requireContext()) }
    private val coachMarks = arrayListOf<CoachMark2Item>()

    val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            detailPassData = it.getParcelable(EXTRA_PARAM_DIGITAL_CATEGORY_DETAIL_PASS_DATA)
                ?: DigitalCategoryDetailPassData.Builder().build()
            issuerId = detailPassData.operatorId ?: ""
        }
        activity?.let {
            localCacheHandler = LocalCacheHandler(context, EMONEY_PDP_PREFERENCES_NAME)
        }
    }

    private var binding by autoCleared<FragmentEmoneyPdpBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmoneyPdpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initInjector() {
        getComponent(EmoneyPdpComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            emoneyCardNumber = savedInstanceState.getString(EXTRA_USER_INPUT_EMONEY_NUMBER) ?: ""
            detailPassData = savedInstanceState.getParcelable(EXTRA_EMONEY_DETAIL_PASS_DATA)
                ?: DigitalCategoryDetailPassData.Builder().build()
            issuerId = detailPassData.operatorId ?: ""
        }

        emoneyPdpViewModel.getDppoConsent()
        loadData()
        renderCardState(detailPassData)

        binding.emoneyPdpHeaderView.actionListener = this
        binding.emoneyPdpInputCardWidget.initView(this)

        binding.emoneyPdpProductWidget.setListener(this)
        binding.emoneyBuyWidget.listener = this

        setAnimationAppBarLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        val dppoConsentData = emoneyPdpViewModel.dppoConsent.value
        inflater.inflate(R.menu.menu_emoney, menu)
        if (dppoConsentData is Success && dppoConsentData.data.description.isNotEmpty()) {
            menu.showConsentIcon()
            menu.setupConsentIcon(dppoConsentData.data.description)
            menu.setupKebabIcon()
        } else {
            menu.hideConsentIcon()
            menu.setupKebabIcon()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        topUpBillsViewModel.menuDetailData.observe(
            viewLifecycleOwner,
            Observer {
                binding.emoneyGlobalError.hide()
                when (it) {
                    is Success -> {
                        trackEventViewPdp(it.data.catalog.label)
                        renderRecommendationsAndPromoList(it.data.recommendations, it.data.promos)
                        renderTicker(EmoneyPdpMapper.mapTopUpBillsTickersToTickersData(it.data.tickers))
                    }
                    is Fail -> {
                        renderFullPageError(it.throwable)
                    }
                }
            }
        )

        topUpBillsViewModel.favNumberData.observe(
            viewLifecycleOwner,
            Observer {
                emoneyPdpViewModel.getPrefixOperator(detailPassData.menuId.toIntSafely())
            }
        )

        emoneyPdpViewModel.inputViewError.observe(
            viewLifecycleOwner,
            Observer {
                binding.emoneyPdpInputCardWidget.renderError(it)
                if (it.isNotEmpty()) showRecentNumberAndPromo()
            }
        )

        emoneyPdpViewModel.catalogPrefixSelect.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Fail -> renderErrorMessage(it.throwable)
                    is Success -> {
                        if (detailPassData.clientNumber != null && detailPassData.clientNumber?.isNotEmpty() == true) {
                            renderClientNumber(
                                TopupBillsSearchNumberDataModel(
                                    clientNumber = detailPassData.clientNumber ?: ""
                                )
                            )
                        } else if (emoneyCardNumber.isNotEmpty()) {
                            renderClientNumber(
                                TopupBillsSearchNumberDataModel(
                                    clientNumber = emoneyCardNumber
                                )
                            )
                        } else {
                            topUpBillsViewModel.favNumberData.value?.let { favNumber ->
                                if (favNumber is Success) {
                                    favNumber.data.firstOrNull()?.let { num -> renderClientNumber(num) }
                                }
                            }
                        }
                    }
                }
            }
        )

        emoneyPdpViewModel.selectedOperator.observe(
            viewLifecycleOwner,
            Observer {
                renderOperatorIcon(it)
                loadProducts(it)
            }
        )

        emoneyPdpViewModel.selectedRecentNumber.observe(
            viewLifecycleOwner,
            Observer {
                binding.emoneyFullPageLoadingLayout.show()
                proceedAddToCart(
                    emoneyPdpViewModel.generateCheckoutPassData(
                        (requireActivity() as EmoneyPdpActivity).promoCode,
                        it.clientNumber,
                        it.productId,
                        it.operatorId,
                        categoryIdFromPDP = detailPassData.categoryId
                    )
                )
            }
        )

        emoneyPdpViewModel.catalogData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        showProducts()
                        renderProducts(
                            it.data.product.dataCollections.firstOrNull()?.products
                                ?: listOf()
                        )
                        showOnBoarding()
                    }
                    is Fail -> renderErrorMessage(it.throwable)
                }
            }
        )

        addToCartViewModel.addToCartResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        navigateToCart(it.data)
                    }
                    is Fail -> {
                        renderErrorMessage(it.throwable)
                        binding.emoneyFullPageLoadingLayout.hide()
                    }
                }
                binding.emoneyFullPageLoadingLayout.hide()
                binding.emoneyBuyWidget.onBuyButtonLoading(false)
            }
        )

        addToCartViewModel.errorAtc.observe(viewLifecycleOwner) {
            renderErrorMessage(MessageErrorException(it.title))
            binding.emoneyFullPageLoadingLayout.hide()
            binding.emoneyBuyWidget.onBuyButtonLoading(false)
        }

        emoneyPdpViewModel.dppoConsent.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    activity?.invalidateOptionsMenu()
                }
                is Fail -> {}
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // save userInputView value for don't keep activities
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_USER_INPUT_EMONEY_NUMBER, emoneyCardNumber)
        outState.putParcelable(EXTRA_EMONEY_DETAIL_PASS_DATA, detailPassData)
    }

    private fun loadData() {
        binding.emoneyGlobalError.hide()
        binding.emoneyPdpShimmeringLayout.root.show()
        topUpBillsViewModel.getMenuDetail(
            CommonTopupBillsGqlQuery.catalogMenuDetail,
            topUpBillsViewModel.createMenuDetailParams(detailPassData.menuId.toIntSafely())
        )
        topUpBillsViewModel.getFavoriteNumbers(listOf(detailPassData.categoryId.toIntSafely()))
    }

    private fun setAnimationAppBarLayout() {
        binding.appBarLayout.addOnOffsetChangedListener(object :
                AppBarLayout.OnOffsetChangedListener {
                var lastOffset = -1
                var lastIsCollapsed = false

                override fun onOffsetChanged(layoutAppBar: AppBarLayout?, verticalOffSet: Int) {
                    if (lastOffset == verticalOffSet || layoutAppBar == null) return

                    lastOffset = verticalOffSet
                    if (abs(verticalOffSet) >= layoutAppBar.totalScrollRange && !lastIsCollapsed) {
                        // Collapsed
                        lastIsCollapsed = true
                        (activity as EmoneyPdpActivity).findViewById<HeaderUnify>(R.id.emoney_toolbar)?.isShowShadow =
                            true
                    } else if (verticalOffSet == 0 && lastIsCollapsed) {
                        // Expanded
                        lastIsCollapsed = false
                        (activity as EmoneyPdpActivity).findViewById<HeaderUnify>(R.id.emoney_toolbar)?.isShowShadow =
                            false
                        showCoachMark(true)
                    } else {
                        showCoachMark(false)
                    }
                }
            })
    }

    private fun trackEventViewPdp(categoryName: String) {
        rechargeAnalytics.eventViewPdpPage(categoryName, userSession.userId)
    }

    private fun renderRecommendationsAndPromoList(
        recommendations: List<TopupBillsRecommendation>,
        promoList: List<TopupBillsPromo>
    ) {
        binding.emoneyPdpShimmeringLayout.root.hide()
        if (recommendations.isEmpty() && promoList.isEmpty()) {
            binding.emoneyPdpViewPager.hide()
            return
        }
        binding.emoneyPdpViewPager.show()

        if (recommendations.isNotEmpty() && promoList.isNotEmpty()) {
            binding.emoneyPdpTab.addNewTab(getString(R.string.recharge_pdp_emoney_recents_tab))
            binding.emoneyPdpTab.addNewTab(getString(R.string.recharge_pdp_emoney_promo_tab))
            binding.emoneyPdpTab.show()
        } else {
            binding.emoneyPdpTab.hide()
        }

        val adapter = EmoneyPdpFragmentPagerAdapter(this, recommendations, promoList)
        binding.emoneyPdpViewPager.adapter = adapter
        adapter.notifyDataSetChanged()

        binding.emoneyPdpTab.tabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    tab.select()
                    trackSelectedTab(tab.position)
                    binding.emoneyPdpViewPager.currentItem = tab.position
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {}
                override fun onTabReselected(p0: TabLayout.Tab?) {}
            })

        binding.emoneyPdpViewPager?.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val tab = binding.emoneyPdpTab?.getUnifyTabLayout()?.getTabAt(position)
                    tab?.select()
                }
            })
    }

    private fun trackSelectedTab(tabPosition: Int) {
        when (tabPosition) {
            0 -> EmoneyPdpAnalyticsUtils.clickRecentTransactionTab(
                userSession.userId,
                getIssuerName(issuerId)
            )
            1 -> EmoneyPdpAnalyticsUtils.clickPromoTab(userSession.userId, getIssuerName(issuerId))
        }
    }

    private fun renderTicker(tickers: List<TickerData>) {
        if (tickers.isEmpty()) {
            binding.emoneyPdpTicker.hide()
            return
        }

        if (tickers.size == 1) {
            setUpSingleTicker(tickers.first())
        } else {
            setUpMultipleTicker(tickers)
        }
        binding.emoneyPdpTicker.show()
    }

    private fun setUpSingleTicker(ticker: TickerData) {
        binding.emoneyPdpTicker.tickerTitle = ticker.title
        binding.emoneyPdpTicker.setHtmlDescription(ticker.description)
        binding.emoneyPdpTicker.tickerType = ticker.type
        binding.emoneyPdpTicker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
            }

            override fun onDismiss() {}
        })
    }

    private fun setUpMultipleTicker(tickers: List<TickerData>) {
        context?.let { context ->
            val tickerAdapter = TickerPagerAdapter(context, tickers)
            tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$linkUrl")
                }
            })
            binding.emoneyPdpTicker.addPagerView(tickerAdapter, tickers)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_EMONEY_PDP_DIGITAL_SEARCH_NUMBER -> {
                    val favNumber = data?.getParcelableExtra<TopupBillsSearchNumberDataModel>(
                        TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER
                    )
                    favNumber?.let {
                        renderClientNumber(it)

                        // to handle don't keep activities case, so displayed client number wont be override with client number on detailPassData
                        detailPassData.clientNumber = it.clientNumber
                        detailPassData.additionalETollBalance = ""
                    }
                }

                REQUEST_CODE_EMONEY_PDP_CAMERA_OCR -> {
                    val clientNumber =
                        data?.getStringExtra(DigitalExtraParam.EXTRA_NUMBER_FROM_CAMERA_OCR)

                    clientNumber?.let {
                        showToastMessage(getString(R.string.recharge_pdp_success_message_scan_ocr))
                        renderClientNumber(TopupBillsSearchNumberDataModel(clientNumber = it))

                        // to handle don't keep activities case, so displayed client number wont be override with client number on detailPassData
                        detailPassData.clientNumber = it
                        detailPassData.additionalETollBalance = ""
                    }
                }

                REQUEST_CODE_EMONEY_PDP_CHECK_SALDO -> {
                    val checkSaldoData =
                        data?.getParcelableExtra<DigitalCategoryDetailPassData>(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA)
                    checkSaldoData?.run {
                        val clientNumberData = TopupBillsSearchNumberDataModel(
                            clientNumber = clientNumber ?: "",
                            productId = productId ?: "",
                            operatorId = operatorId ?: "",
                            categoryId = categoryId ?: ""
                        )

                        renderClientNumber(clientNumberData)
                        // renderProduct
                        detailPassData = this
                        renderCardState(this)
                    }
                }

                REQUEST_CODE_CART_DIGITAL -> {
                    if (data?.hasExtra(DigitalExtraParam.EXTRA_MESSAGE) == true) {
                        val throwable = data.getSerializableExtra(DigitalExtraParam.EXTRA_MESSAGE)
                            as Throwable
                        if (!throwable.message.isNullOrEmpty()) {
                            renderErrorMessage(throwable)
                        }
                    }
                }

                REQUEST_CODE_LOGIN -> {
                    proceedAddToCart(emoneyPdpViewModel.digitalCheckoutPassData)
                }
            }
        }
    }

    private fun showToastMessage(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
    }

    private fun renderClientNumber(item: TopupBillsSearchNumberDataModel) {
        emoneyCardNumber = item.clientNumber
        if (item.clientNumber.length > MAX_CHAR_EMONEY_CARD_NUMBER) {
            emoneyCardNumber = item.clientNumber.substring(0, MAX_CHAR_EMONEY_CARD_NUMBER)
        }
        binding.emoneyPdpInputCardWidget.setNumber(emoneyCardNumber)
    }

    private fun renderErrorMessage(error: Throwable) {
        var errorThrowable = error
        if ((error.message ?: "").contains(EmoneyPdpViewModel.ERROR_GRPC_TIMEOUT, true)) {
            errorThrowable = MessageErrorException(
                getString(
                    com.tokopedia.common_digital.R.string.digital_common_grpc_toaster
                )
            )
        }
        Toaster.build(
            requireView(),
            ErrorHandler.getErrorMessage(requireContext(), errorThrowable),
            Toaster.LENGTH_LONG,
            Toaster.TYPE_ERROR
        ).show()
    }

    private fun renderFullPageError(throwable: Throwable) {
        binding.emoneyGlobalError.show()
        binding.emoneyGlobalError.showUnifyError(throwable, { loadData() }, {
            binding.emoneyGlobalError.hide()
            renderErrorMessage(throwable)
            binding.emoneyPdpShimmeringLayout.root.hide()
        })
        binding.emoneyGlobalError.findViewById<GlobalError>(com.tokopedia.globalerror.R.id.globalerror_view)
            ?.apply {
                gravity = Gravity.CENTER
            }
    }

    override fun onClickCheckBalance() {
        EmoneyPdpAnalyticsUtils.clickCheckSaldoButton(userSession.userId, getIssuerName(issuerId))
        val intent = RouteManager.getIntent(
            activity,
            ApplinkConsInternalDigital.SMARTCARD,
            DigitalExtraParam.EXTRA_NFC_FROM_PDP,
            "false"
        )
        startActivityForResult(intent, REQUEST_CODE_EMONEY_PDP_CHECK_SALDO)
    }

    override fun onClickCameraIcon() {
        EmoneyPdpAnalyticsUtils.clickCameraIcon(userSession.userId, getIssuerName(issuerId))
        val intent = RouteManager.getIntent(activity, ApplinkConsInternalDigital.CAMERA_OCR)
        startActivityForResult(intent, REQUEST_CODE_EMONEY_PDP_CAMERA_OCR)
    }

    override fun onClickInputView(inputNumber: String) {
        if (topUpBillsViewModel.favNumberData.value is Success) {
            showFavoriteNumbersPage((topUpBillsViewModel.favNumberData.value as Success).data)
        } else {
            showFavoriteNumbersPage(arrayListOf())
        }
    }

    override fun onRemoveNumberIconClick() {
        EmoneyPdpAnalyticsUtils.clickClearCardNumber(userSession.userId, getIssuerName(issuerId))
        showCoachMark(false)
        emoneyCardNumber = ""
        showRecentNumberAndPromo()
    }

    override fun onInputNumberChanged(inputNumber: String) {
        // call be to get operator name
        EmoneyPdpAnalyticsUtils.clickChangeCardNumber(
            inputNumber,
            userSession.userId,
            getIssuerName(issuerId)
        )
        if (inputNumber.length == MAX_CHAR_EMONEY_CARD_NUMBER) {
            emoneyPdpViewModel.getSelectedOperator(
                inputNumber,
                getString(R.string.recharge_pdp_emoney_number_error_not_found)
            )
        } else if (inputNumber.isNotEmpty()) {
            binding.emoneyPdpInputCardWidget.renderError(getString(R.string.recharge_pdp_emoney_number_length_minimal_16_char))
        }
    }

    private fun showFavoriteNumbersPage(favoriteNumbers: List<TopupBillsSearchNumberDataModel>) {
        startActivityForResult(
            TopupBillsSearchNumberActivity.getCallingIntent(
                requireContext(),
                ClientNumberType.TYPE_INPUT_NUMERIC.value,
                binding.emoneyPdpInputCardWidget.getNumber(),
                favoriteNumbers
            ),
            REQUEST_CODE_EMONEY_PDP_DIGITAL_SEARCH_NUMBER
        )
    }

    private fun renderOperatorIcon(selectedOperator: RechargePrefix) {
        if (selectedOperator.operator != null) {
            binding.emoneyPdpInputCardWidget.setOperator(selectedOperator.operator.attributes.imageUrl)
        }
    }

    private fun renderCardState(detailPassData: DigitalCategoryDetailPassData) {
        if (detailPassData.additionalETollBalance != null && detailPassData.additionalETollBalance?.isNotEmpty() == true) {
            binding.emoneyPdpHeaderView.configureUpdateBalanceWithCardNumber(
                detailPassData.clientNumber
                    ?: "",
                detailPassData.additionalETollBalance ?: ""
            )
        } else {
            binding.emoneyPdpHeaderView.configureCheckBalanceView()
        }
    }

    private fun loadProducts(prefix: RechargePrefix) {
        // to be changed to operator.id // NEED ACTION
        showProducts()
        issuerId = prefix.key
        binding.emoneyPdpProductWidget.showShimmering()
        binding.emoneyBuyWidgetLayout.hide()
        emoneyPdpViewModel.getProductFromOperator(detailPassData.menuId.toIntSafely(), prefix.key)
    }

    private fun renderProducts(productList: List<CatalogProduct>) {
        binding.emoneyPdpProductWidget.setTitle(getString(R.string.recharge_pdp_emoney_products_title))
        binding.emoneyPdpProductWidget.setProducts(productList)
    }

    private fun showProducts() {
        binding.emoneyPdpShimmeringLayout.root.hide()
        binding.emoneyPdpTab.hide()
        binding.emoneyPdpViewPager.hide()
        binding.emoneyPdpProductWidget.show()
    }

    private fun showRecentNumberAndPromo() {
        binding.emoneyPdpShimmeringLayout.root.hide()
        binding.emoneyPdpProductWidget.hide()
        (binding.emoneyPdpViewPager.adapter)?.let {
            if ((it as EmoneyPdpFragmentPagerAdapter).itemCount > TAB_COUNT_THRESHOLD_NUMBER) {
                binding.emoneyPdpTab.show()
            }
        }
        binding.emoneyPdpViewPager.show()
        binding.emoneyPdpProductWidget.showPaddingBottom(
            context?.resources?.getDimensionPixelOffset(com.tokopedia.unifycomponents.R.dimen.spacing_lvl6)
                ?: 0
        )
        binding.emoneyBuyWidgetLayout.hide()
    }

    override fun onClickProduct(product: CatalogProduct, position: Int) {
        // atc
        emoneyPdpViewModel.setSelectedProduct(product)
        coachMark.dismissCoachMark()

        if (product.attributes.price.isNotEmpty()) {
            binding.emoneyBuyWidget.setTotalPrice(product.attributes.price)
        } else {
            binding.emoneyBuyWidget.setTotalPrice(
                CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(
                    product.attributes.pricePlain.toIntSafely()
                )
            )
        }

        binding.emoneyBuyWidgetLayout.show()
        binding.emoneyBuyWidget.setVisibilityLayout(true)
        binding.emoneyBuyWidgetLayout.invalidate()
        binding.emoneyPdpProductWidget.showPaddingBottom(
            kotlin.math.max(
                context?.resources?.getDimensionPixelOffset(com.tokopedia.unifycomponents.R.dimen.unify_space_64)
                    ?: 0,
                binding.emoneyBuyWidgetLayout.measuredHeight
            ) + (
                context?.resources?.getDimensionPixelOffset(com.tokopedia.unifycomponents.R.dimen.spacing_lvl6)
                    ?: 0
                )
        )
    }

    override fun onClickSeeDetailProduct(product: CatalogProduct) {
        EmoneyPdpAnalyticsUtils.clickSeeProductDetail(
            getIssuerName(issuerId),
            product.attributes.pricePlain,
            userSession.userId
        )
        val bottomSheet = EmoneyProductDetailBottomSheet.newBottomSheet(product)
        bottomSheet.show(childFragmentManager, TAG)
    }

    override fun onClickNextBuyButton() {
        binding.emoneyBuyWidget.onBuyButtonLoading(true)
        proceedAddToCart(
            emoneyPdpViewModel.generateCheckoutPassData(
                (requireActivity() as EmoneyPdpActivity).promoCode,
                binding.emoneyPdpInputCardWidget.getNumber(),
                categoryIdFromPDP = detailPassData.categoryId
            )
        )
    }

    private fun proceedAddToCart(digitalCheckoutData: DigitalCheckoutPassData) {
        if (userSession.isLoggedIn) {
            addToCartViewModel.addToCart(
                digitalCheckoutData,
                DeviceUtil.getDigitalIdentifierParam(requireActivity())
            )
        } else {
            navigateToLoginPage()
        }
    }

    protected open fun navigateToCart(categoryId: String) {
        context?.let { context ->
            val intent =
                RouteManager.getIntent(context, ApplinkConsInternalDigital.CHECKOUT_DIGITAL)
            emoneyPdpViewModel.digitalCheckoutPassData.categoryId = categoryId
            intent.putExtra(
                DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA,
                emoneyPdpViewModel.digitalCheckoutPassData
            )
            startActivityForResult(intent, REQUEST_CODE_CART_DIGITAL)
        }
    }

    private fun navigateToLoginPage() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
    }

    private fun showOnBoarding() {
        context?.run {
            if (getCoachMarkHasShown()) return
            binding.emoneyPdpProductWidget.binding.emoneyProductListRecyclerView.let { rv ->
                rv.post {
                    try {
                        val itemView =
                            (rv.findViewHolderForAdapterPosition(0) as EmoneyPdpProductViewHolder).itemView
                        val bind = ItemEmoneyProductBinding.bind(itemView)
                        with(bind) {
                            emoneyProductPrice.let { firstNominalView ->
                                coachMarks.add(
                                    CoachMark2Item(
                                        firstNominalView,
                                        getString(R.string.recharge_pdp_emoney_coachmark_title),
                                        getString(R.string.recharge_pdp_emoney_coachmark_subtitle),
                                        CoachMark2.POSITION_BOTTOM
                                    )
                                )

                                showCoachMark(true)

                                localCacheHandler.apply {
                                    putBoolean(EMONEY_PDP_COACH_MARK_HAS_SHOWN, true)
                                    applyEditor()
                                }
                            }
                        }
                    } catch (e: Throwable) {
                        // do nothing, don't show coachmark then.
                    }
                }
            }
        }
    }

    private fun getCoachMarkHasShown(): Boolean {
        return localCacheHandler.getBoolean(EMONEY_PDP_COACH_MARK_HAS_SHOWN, false)
    }

    private fun showCoachMark(show: Boolean) {
        try {
            if (coachMarks.isNotEmpty()) {
                if (show) {
                    coachMark.showCoachMark(coachMarks)
                } else {
                    coachMark.hideCoachMark()
                }
            }
        } catch (e: Throwable) {
            // do nothing. don't show coachmark.
        }
    }

    private fun getIssuerName(issuerId: String): String {
        return when (issuerId) {
            ISSUER_ID_EMONEY -> ISSUER_NAME_EMONEY
            ISSUER_ID_BRIZZI -> ISSUER_NAME_BRIZZI
            ISSUER_ID_TAP_CASH -> ISSUER_NAME_TAPCASH
            else -> ""
        }
    }

    override fun onOrderListClicked() {
        context?.let {
            if (userSession.isLoggedIn) {
                RouteManager.route(it, ApplinkConst.DIGITAL_ORDER)
            } else {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, EmoneyPdpActivity.REQUEST_CODE_LOGIN_EMONEY)
            }
        }
    }

    override fun onHelpClicked() {
        context?.let {
            RouteManager.route(it, ApplinkConst.CONTACT_US_NATIVE)
        }
    }

    private fun showBottomMenus() {
        val menuBottomSheet = EmoneyMenuBottomSheets.newInstance()
        menuBottomSheet.listener = this
        menuBottomSheet.setShowListener {
            menuBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        menuBottomSheet.show(childFragmentManager, EmoneyPdpActivity.TAG_EMONEY_MENU)
    }

    private fun Menu.hideConsentIcon() {
        findItem(R.id.emoney_action_dppo_consent).isVisible = false
    }

    private fun Menu.showConsentIcon() {
        findItem(R.id.emoney_action_dppo_consent).isVisible = true
    }

    private fun Menu.setupConsentIcon(description: String) {
        if (description.isNotEmpty()) {
            context?.let { ctx ->
                val iconUnify = getIconUnifyDrawable(
                    ctx,
                    IconUnify.INFORMATION,
                    ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
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
                IconUnify.MENU_KEBAB_VERTICAL,
                ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            )
            iconUnify?.toBitmap()?.let {
                getItem(1).setOnMenuItemClickListener {
                    showBottomMenus()
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
        private const val TAG = "EmoneyProductDetailBottomSheet"

        const val EMONEY_PDP_PREFERENCES_NAME = "emoney_pdp_preferences"
        const val EMONEY_PDP_COACH_MARK_HAS_SHOWN = "emoney_pdp_show_coach_mark"

        private const val TAB_COUNT_THRESHOLD_NUMBER = 1
        private const val REQUEST_CODE_EMONEY_PDP_CHECK_SALDO = 1007
        private const val REQUEST_CODE_EMONEY_PDP_CAMERA_OCR = 1008
        const val REQUEST_CODE_CART_DIGITAL = 1090
        private const val REQUEST_CODE_EMONEY_PDP_DIGITAL_SEARCH_NUMBER = 1004
        private const val REQUEST_CODE_LOGIN = 1010

        private const val MAX_CHAR_EMONEY_CARD_NUMBER = 16

        const val EXTRA_PARAM_DIGITAL_CATEGORY_DETAIL_PASS_DATA = "EXTRA_PARAM_PASS_DATA"
        private const val EXTRA_USER_INPUT_EMONEY_NUMBER = "EXTRA_USER_INPUT_EMONEY_NUMBER"
        private const val EXTRA_EMONEY_DETAIL_PASS_DATA = "EXTRA_EMONEY_DETAIL_PASS_DATA"

        const val ISSUER_ID_EMONEY = "578"
        const val ISSUER_ID_BRIZZI = "1015"
        const val ISSUER_ID_TAP_CASH = "2606"

        const val ISSUER_NAME_EMONEY = "emoney"
        const val ISSUER_NAME_BRIZZI = "brizzi"
        const val ISSUER_NAME_TAPCASH = "tapcash"

        private const val TOOLBAR_ICON_SIZE = 64

        fun newInstance(digitalCategoryDetailPassData: DigitalCategoryDetailPassData): EmoneyPdpFragment {
            val fragment = EmoneyPdpFragment()
            val bundle = Bundle()
            bundle.putParcelable(
                EXTRA_PARAM_DIGITAL_CATEGORY_DETAIL_PASS_DATA,
                digitalCategoryDetailPassData
            )
            fragment.arguments = bundle
            return fragment
        }
    }
}
