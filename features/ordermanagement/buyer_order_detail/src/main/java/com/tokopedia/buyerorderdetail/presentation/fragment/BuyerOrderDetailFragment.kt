package com.tokopedia.buyerorderdetail.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalShare
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.performance.BuyerOrderDetailLoadMonitoring
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.analytic.tracker.RecommendationWidgetTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailCommonIntentParamKey
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailIntentCode
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailIntentParamKey
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant.MAX_PRODUCT_PRICE_AFFILIATE_LINK_ELIGIBILITY
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant.PRODUCT_STATUS_AFFILIATE_LINK_ELIGIBILITY
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant.SHOP_STATUS_AFFILIATE_LINK_ELIGIBILITY
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant.SITE_ID_AFFILIATE_LINK_ELIGIBILITY
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant.VERTICAL_ID_AFFILIATE_LINK_ELIGIBILITY
import com.tokopedia.buyerorderdetail.common.extension.collectLatestWhenResumed
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailShareUtils
import com.tokopedia.buyerorderdetail.databinding.FragmentBuyerOrderDetailBinding
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderDetailActivity
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.CourierButtonListener
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.CourierInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.DigitalRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OrderResolutionViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.OwocInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PartialProductItemViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PgRecommendationViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.PofRefundInfoViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductListToggleViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.TickerViewHolder
import com.tokopedia.buyerorderdetail.presentation.animator.BuyerOrderDetailToolbarMenuAnimator
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.OwocBottomSheet
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.PofDetailRefundedBottomSheet
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.PofEstimateRefundInfoBottomSheet
import com.tokopedia.buyerorderdetail.presentation.coachmark.CoachMarkManager
import com.tokopedia.buyerorderdetail.presentation.dialog.RequestCancelResultDialog
import com.tokopedia.buyerorderdetail.presentation.helper.BuyerOrderDetailStickyActionButtonHandler
import com.tokopedia.buyerorderdetail.presentation.mapper.ProductListUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.model.EstimateInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.MultiATCState
import com.tokopedia.buyerorderdetail.presentation.model.OrderOneTimeEvent
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundSummaryUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailStickyActionButton
import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailToolbarMenu
import com.tokopedia.buyerorderdetail.presentation.partialview.WidgetBrcCsat
import com.tokopedia.buyerorderdetail.presentation.scroller.BuyerOrderDetailRecyclerViewScroller
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.SavingsWidgetUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.WidgetBrcCsatUiState
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.digital.digital_recommendation.utils.DigitalRecommendationData
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.linker.model.LinkerData.PRODUCT_TYPE
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.logisticCommon.ui.DelayedEtaBottomSheetFragment
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmSectionViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.SCP_REWARDS_MEDALI_TOUCH_POINT
import com.tokopedia.scp_rewards_touchpoints.common.BUYER_ORDER_DETAIL_PAGE
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.touchpoints.ScpToasterHelper
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.viewholder.ScpRewardsMedalTouchPointWidgetViewHolder
import com.tokopedia.scp_rewards_touchpoints.touchpoints.analytics.ScpRewardsCelebrationWidgetAnalytics
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model.AnalyticsData
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.model.ScpToasterModel
import com.tokopedia.scp_rewards_touchpoints.touchpoints.viewmodel.ScpRewardsMedalTouchPointViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.universal_sharing.view.model.Shop
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.text.currency.StringUtils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.resources.common.R as resourcescommonR

open class BuyerOrderDetailFragment :
    BaseDaggerFragment(),
    PartialProductItemViewHolder.ProductViewListener,
    ProductBundlingViewHolder.Listener,
    TickerViewHolder.TickerViewHolderListener,
    DigitalRecommendationViewHolder.ActionListener,
    CourierInfoViewHolder.CourierInfoViewHolderListener,
    PgRecommendationViewHolder.BuyerOrderDetailBindRecomWidgetListener,
    OrderResolutionViewHolder.OrderResolutionListener,
    ProductListToggleViewHolder.Listener,
    PofRefundInfoViewHolder.Listener,
    PartialProductItemViewHolder.ShareProductBottomSheetListener,
    ScpRewardsMedalTouchPointWidgetViewHolder.ScpRewardsMedalTouchPointWidgetListener,
    OwocInfoViewHolder.Listener,
    BmgmSectionViewHolder.Listener,
    CourierButtonListener {

    companion object {
        @JvmStatic
        fun newInstance(extras: Bundle): BuyerOrderDetailFragment {
            return BuyerOrderDetailFragment().apply {
                arguments = extras
            }
        }

        private const val SOURCE_NAME_FOR_MEDAL_TOUCH_POINT = "order_detail_page"
        private const val BREADCRUMB_BOM_DETAIL_SHOWING_DATA = "Order detail page is showing data"
        private const val BREADCRUMB_BOM_DETAIL_SHOWING_ERROR = "Order detail page is showing error"
        private const val BREADCRUMB_BOM_DETAIL_FULL_SCREEN_LOADING = "Order detail page is showing fullscreen loading"
        private const val BREADCRUMB_BOM_DETAIL_PULL_REFRESH_LOADING = "Order detail page is showing pull refresh loading"

        const val RESULT_CODE_INSTANT_CANCEL_BUYER_REQUEST = 100
        const val RESULT_CODE_CANCEL_ORDER_DISABLE = 102
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var stickyActionButton: BuyerOrderDetailStickyActionButton? = null
    private var swipeRefreshBuyerOrderDetail: SwipeRefreshLayout? = null
    private var rvBuyerOrderDetail: RecyclerView? = null
    private var toolbarBuyerOrderDetail: HeaderUnify? = null
    private var globalErrorBuyerOrderDetail: GlobalError? = null
    protected var loaderBuyerOrderDetail: LoaderUnify? = null

    private var brcCsatShowToasterRunnable: Runnable? = null

    private var binding by autoClearedNullable<FragmentBuyerOrderDetailBinding>()

    private val viewModel: BuyerOrderDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BuyerOrderDetailViewModel::class.java]
    }

    private val scpMedalTouchPointViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ScpRewardsMedalTouchPointViewModel::class.java]
    }

    private val toolbarMenuAnimator by lazy {
        toolbarMenuIcons?.let {
            BuyerOrderDetailToolbarMenuAnimator(it)
        }
    }
    private val cacheManager: SaveInstanceCacheManager by lazy {
        SaveInstanceCacheManager(requireContext(), true)
    }
    protected open val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactory(
            productBundlingViewListener = this,
            tickerViewHolderListener = this,
            digitalRecommendationData = digitalRecommendationData,
            digitalRecommendationListener = this,
            courierInfoViewHolderListener = this,
            productListToggleListener = this,
            pofRefundInfoListener = this,
            scpRewardsMedalTouchPointWidgetListener = this,
            owocInfoListener = this,
            bmgmListener = this,
            productBenefitListener = ProductBenefitListener(),
            orderResolutionListener = this,
            recyclerViewSharedPool = recyclerViewSharedPool,
            productViewListener = this,
            bottomSheetListener = this,
            navigator = navigator,
            buyerOrderDetailBindRecomWidgetListener = this,
            courierButtonListener = this,
            addOnListener = AddOnListener()
        )
    }
    protected open val adapter: BuyerOrderDetailAdapter by lazy {
        BuyerOrderDetailAdapter(typeFactory)
    }
    private val requestCancelResultDialog: RequestCancelResultDialog by lazy {
        RequestCancelResultDialog(navigator)
    }
    protected val navigator: BuyerOrderDetailNavigator by lazy {
        BuyerOrderDetailNavigator(requireActivity(), this)
    }
    private val remoteConfig: FirebaseRemoteConfigImpl by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    protected val recyclerViewSharedPool = RecyclerView.RecycledViewPool()

    protected val digitalRecommendationData: DigitalRecommendationData
        get() = DigitalRecommendationData(
            viewModelFactory,
            viewLifecycleOwner,
            DigitalRecommendationAdditionalTrackingData(
                userType = "",
                widgetPosition = "",
                pgCategories = viewModel.getCategoryId()
            ),
            DigitalRecommendationPage.PHYSICAL_GOODS
        )

    private val bottomSheetManager: BuyerOrderDetailBottomSheetManager by lazy {
        BuyerOrderDetailBottomSheetManager(requireContext(), childFragmentManager)
    }
    private val stickyActionButtonHandler: BuyerOrderDetailStickyActionButtonHandler by lazy {
        BuyerOrderDetailStickyActionButtonHandler(
            bottomSheetManager,
            cacheManager,
            navigator,
            viewModel
        )
    }

    // show this chat icon only if there's no `Tanya Penjual` button on the sticky button
    private val toolbarMenuIcons: BuyerOrderDetailToolbarMenu? by lazy {
        createToolbarMenuIcons(requireContext())
    }
    private val smoothScroller by lazy {
        rvBuyerOrderDetail?.let {
            BuyerOrderDetailRecyclerViewScroller(
                it
            )
        }
    }
    private val coachMarkManager by lazy { view?.let { CoachMarkManager(it, smoothScroller) } }

    private val buyerOrderDetailLoadMonitoring: BuyerOrderDetailLoadMonitoring?
        get() {
            return activity?.let {
                if (it is BuyerOrderDetailActivity) it.buyerOrderDetailLoadMonitoring else null
            }
        }

    private fun createToolbarMenuIcons(context: Context): BuyerOrderDetailToolbarMenu? {
        return (
            View.inflate(
                context,
                R.layout.partial_buyer_order_detail_toolbar_menu,
                null
            ) as? BuyerOrderDetailToolbarMenu
            )?.apply {
            setViewModel(viewModel)
            setNavigator(navigator)
        }
    }

    override fun getScreenName() = BuyerOrderDetailFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBuyerOrderDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        buyerOrderDetailLoadMonitoring?.startNetworkPerformanceMonitoring()
        if (savedInstanceState == null) {
            loadInitialData(false)
        } else {
            restoreFragmentState(savedInstanceState)
        }
        observeBuyerOrderDetail()
        observeReceiveConfirmation()
        observeAddSingleToCart()
        observeAddMultipleToCart()
        observeMedalTouchPoint()
        observeGroupBooking()
        observeChatUnreadCounter()
        observeOneTimeEvent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activity?.overridePendingTransition(
            resourcescommonR.anim.slide_left_in_medium,
            resourcescommonR.anim.slide_right_out_medium
        )
        when (requestCode) {
            BuyerOrderDetailIntentCode.REQUEST_CODE_REQUEST_CANCEL_ORDER -> handleRequestCancelResult(
                resultCode,
                data
            )
            BuyerOrderDetailIntentCode.REQUEST_CODE_CREATE_RESOLUTION -> handleComplaintResult()
            BuyerOrderDetailIntentCode.REQUEST_CODE_REFRESH_ONLY -> handleResultRefreshOnly()
            BuyerOrderDetailIntentCode.REQUEST_CODE_ORDER_EXTENSION -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleResultOrderExtension(data)
                }
            }
            BuyerOrderDetailIntentCode.REQUEST_CODE_PARTIAL_ORDER_FULFILLMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleResultPartialOrderFulfillment(data)
                }
            }
            BuyerOrderDetailIntentCode.REQUEST_CODE_BRC_CSAT_FORM -> handleBrcCsatFormResult(data)
            BuyerOrderDetailIntentCode.REQUEST_CODE_SHARE -> handleShareResult(resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coachMarkManager?.dismissCoachMark()
        binding?.widgetBrcBom?.removeCallbacks(brcCsatShowToasterRunnable)
    }

    override fun onBuyAgainButtonClicked(product: ProductListUiModel.ProductUiModel) {
        val productCopy = product.copy(isProcessing = true)
        viewModel.addSingleToCart(productCopy)
        trackBuyAgainProduct()
    }

    override fun onProductImpressed(product: ProductListUiModel.ProductUiModel) {
        viewModel.impressProduct(product)
    }

    override fun onPurchaseAgainButtonClicked(uiModel: ProductListUiModel.ProductUiModel) {
        onBuyAgainButtonClicked(uiModel)
    }

    override fun onClickShipmentInfoTnC() {
        BuyerOrderDetailTracker.eventClickSeeShipmentInfoTNC(
            viewModel.getOrderStatusId(),
            viewModel.getOrderId()
        )
    }

    override fun hideDigitalRecommendation() {
        rvBuyerOrderDetail?.post {
            adapter.removeDigitalRecommendation()
        }
    }

    override fun onResolutionWidgetClicked() {
        BuyerOrderDetailTracker.sendClickOnResolutionWidgetEvent(
            viewModel.getOrderStatusId(),
            viewModel.getOrderId()
        )
    }

    private fun restoreFragmentState(savedInstanceState: Bundle) {
        val cacheManagerId = savedInstanceState.getString(
            BuyerOrderDetailCommonIntentParamKey.CACHE_ID
        ).orEmpty()
        cacheManager.id = cacheManagerId
        loadInitialData(true)
    }

    private fun loadInitialData(shouldCheckCache: Boolean) {
        loadBuyerOrderDetail(shouldCheckCache)
    }

    private fun setupViews() {
        bindViews()
        setupToolbar()
        setupGlobalError()
        setupSwipeRefreshLayout()
        setupStickyActionButtons()
        setupBrcCsatWidget()
    }

    private fun bindViews() {
        stickyActionButton = view?.findViewById(R.id.containerActionButtons)
        swipeRefreshBuyerOrderDetail = view?.findViewById(R.id.swipeRefreshBuyerOrderDetail)
        rvBuyerOrderDetail = view?.findViewById(R.id.rvBuyerOrderDetail)
        toolbarBuyerOrderDetail = view?.findViewById(R.id.toolbarBuyerOrderDetail)
        globalErrorBuyerOrderDetail = view?.findViewById(R.id.globalErrorBuyerOrderDetail)
        loaderBuyerOrderDetail = view?.findViewById(R.id.loaderBuyerOrderDetail)
    }

    private fun setupToolbar() {
        setupToolbarMenuIcon()
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolbarBuyerOrderDetail)
        }
    }

    private fun setupGlobalError() {
        globalErrorBuyerOrderDetail?.setActionClickListener { loadBuyerOrderDetail(false) }
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshBuyerOrderDetail?.setOnRefreshListener {
            loadBuyerOrderDetail(false)
        }
    }

    private fun setupRecyclerView() {
        if (rvBuyerOrderDetail?.adapter != adapter) {
            rvBuyerOrderDetail?.adapter = adapter
        }
        recyclerViewSharedPool.setMaxRecycledViews(AddOnViewHolder.RES_LAYOUT, AddOnViewHolder.MAX_RECYCLED_VIEWS)
    }

    private fun setupStickyActionButtons() {
        stickyActionButton?.run {
            setViewModel(viewModel)
            setStickyActionButtonClickHandler(stickyActionButtonHandler)
            setBottomSheetManager(bottomSheetManager)
        }
    }

    private fun setupBrcCsatWidget() {
        binding?.widgetBrcBom?.setup(navigator)
    }

    private fun loadBuyerOrderDetail(shouldCheckCache: Boolean) {
        coachMarkManager?.resetCoachMarkState()
        val orderId =
            arguments?.getString(BuyerOrderDetailCommonIntentParamKey.ORDER_ID, "").orEmpty()
        val paymentId =
            arguments?.getString(BuyerOrderDetailIntentParamKey.PARAM_PAYMENT_ID, "").orEmpty()
        val cart =
            arguments?.getString(BuyerOrderDetailIntentParamKey.PARAM_CART_STRING, "").orEmpty()
        viewModel.getBuyerOrderDetailData(orderId, paymentId, cart, shouldCheckCache)
        getMedalTouchPoint(
            orderId = orderId.toLongOrZero(),
            initialLoad = true
        )
    }

    private fun observeBuyerOrderDetail() {
        collectLatestWhenResumed(viewModel.buyerOrderDetailUiState) { uiState ->
            when (uiState) {
                is BuyerOrderDetailUiState.HasData.Showing -> onSuccessGetBuyerOrderDetail(uiState)
                is BuyerOrderDetailUiState.Error -> onFailedGetBuyerOrderDetail(uiState.throwable)
                is BuyerOrderDetailUiState.FullscreenLoading -> onFullscreenLoadingBuyerOrderDetail()
                is BuyerOrderDetailUiState.HasData.PullRefreshLoading -> onPullRefreshLoadingBuyerOrderDetail(
                    uiState
                )
            }
        }
    }

    private fun observeReceiveConfirmation() {
        viewModel.finishOrderResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> onSuccessReceiveConfirmation(result.data)
                is Fail -> onFailedReceiveConfirmation(result.throwable)
            }
        }
    }

    private fun observeAddSingleToCart() {
        viewModel.singleAtcResult.observe(viewLifecycleOwner) { result ->
            when (val requestResult = result.second) {
                is Success -> {
                    trackSuccessATC(listOf(result.first), requestResult.data)
                    onSuccessAddToCart(requestResult.data)
                }

                is Fail -> onFailedSingleAddToCart(requestResult.throwable)
            }
        }
    }

    private fun observeAddMultipleToCart() {
        viewModel.multiAtcResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is MultiATCState.Success -> {
                    trackSuccessATC(viewModel.getProducts(), result.data)
                    onSuccessAddToCart(result.data)
                }

                is MultiATCState.Fail -> onFailedMultiAddToCart(result)
            }
            stickyActionButton?.finishPrimaryActionButtonLoading()
        }
    }

    private fun observeOneTimeEvent() {
        collectLatestWhenResumed(viewModel.oneTimeMethodState) {
            when (it.event) {
                is OrderOneTimeEvent.ImpressSavingsWidget -> {
                    BuyerOrderDetailTracker.SavingsWidget.impressSavingsWidget(
                        orderId = it.event.orderId,
                        isPlus = it.event.isPlus,
                        isMixPromo = it.event.isMixPromo
                    )
                }

                else -> {
                    // no op
                }
            }
        }
    }

    private fun observeMedalTouchPoint() {
        scpMedalTouchPointViewModel.medalTouchPointData.observe(viewLifecycleOwner) {
            when (val result = it.result) {
                is com.tokopedia.scp_rewards_touchpoints.common.Success -> {
                    val data = result.data
                    if (data.scpRewardsMedaliTouchpointOrder.isShown) {
                        view?.let { view ->
                            if (!it.initialLoad) {
                                ScpToasterHelper.showToaster(
                                    view = view,
                                    data = ScpToasterModel(
                                        AnalyticsData(
                                            orderId = viewModel.getOrderId(),
                                            pagePath = BUYER_ORDER_DETAIL_PAGE,
                                            pageType = BUYER_ORDER_DETAIL_PAGE
                                        ),
                                        responseData = data
                                    ),
                                    customBottomHeight = getStickyActionButtonHeight(),
                                    ctaClickListener = {
                                        viewModel.hideScpRewardsMedalTouchPointWidget()
                                    }
                                )
                            }
                            ScpRewardsCelebrationWidgetAnalytics.impressionCelebrationWidget(
                                badgeId = data.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.medaliID.toString(),
                                orderId = viewModel.getOrderId(),
                                pagePath = BUYER_ORDER_DETAIL_PAGE,
                                pageType = BUYER_ORDER_DETAIL_PAGE
                            )
                            viewModel.updateScpRewardsMedalTouchPointWidgetState(
                                data = data.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder,
                                marginLeft = context?.resources?.getDimension(R.dimen.buyer_order_detail_scp_rewards_medal_touch_point_margin_left).toIntSafely(),
                                marginTop = context?.resources?.getDimension(R.dimen.buyer_order_detail_scp_rewards_medal_touch_point_margin_top).toIntSafely(),
                                marginRight = context?.resources?.getDimension(R.dimen.buyer_order_detail_scp_rewards_medal_touch_point_margin_right).toIntSafely()
                            )
                        }
                    } else {
                        if (!it.initialLoad) {
                            val message = (viewModel.finishOrderResult.value as? Success<FinishOrderResponse.Data.FinishOrderBuyer>)?.data?.message?.firstOrNull().orEmpty()
                            showCommonToaster(message)
                        }
                    }
                }
                is Error -> {
                    if (!it.initialLoad) {
                        val message = (viewModel.finishOrderResult.value as? Success<FinishOrderResponse.Data.FinishOrderBuyer>)?.data?.message?.firstOrNull().orEmpty()
                        showCommonToaster(message)
                    }
                }
                else -> {}
            }
        }
    }

    private fun getStickyActionButtonHeight(): Int = stickyActionButton?.height.orZero()

    private fun onSuccessGetBuyerOrderDetail(
        uiState: BuyerOrderDetailUiState.HasData.Showing
    ) {
        hideLoader()
        showRecyclerView()
        buyerOrderDetailLoadMonitoring?.startRenderPerformanceMonitoring()
        updateToolbarMenu(uiState)
        updateContent(uiState)
        updateStickyButtons(uiState)
        updateSavingsWidget(uiState)
        updateBrcCsatWidget(uiState)
        swipeRefreshBuyerOrderDetail?.isRefreshing = false
        stopLoadTimeMonitoring()
        EmbraceMonitoring.logBreadcrumb(BREADCRUMB_BOM_DETAIL_SHOWING_DATA)
    }

    private fun showGlobalErrorState() {
        globalErrorBuyerOrderDetail?.show()
        swipeRefreshBuyerOrderDetail?.hide()
        stickyActionButton?.hide()
    }

    private fun showRecyclerView() {
        globalErrorBuyerOrderDetail?.hide()
        swipeRefreshBuyerOrderDetail?.show()
    }

    private fun updateToolbarMenu(uiState: BuyerOrderDetailUiState.HasData) {
        val orderId = viewModel.getOrderId()
        setupToolbarMenu(
            !containsAskSellerButton(uiState.actionButtonsUiState.data) &&
                orderId.isNotBlank() &&
                orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID
        )
    }

    private fun updateContent(
        uiState: BuyerOrderDetailUiState.HasData
    ) {
        setupRecyclerView()
        adapter.updateItems(context, uiState)
        coachMarkManager?.notifyUpdatedAdapter()
    }

    private fun showLoader() {
        binding?.loaderBuyerOrderDetail?.show()
    }
    private fun hideLoader() {
        binding?.loaderBuyerOrderDetail?.hide()
    }

    private fun updateStickyButtons(uiState: BuyerOrderDetailUiState.HasData) {
        stickyActionButton?.setupActionButtons(
            actionButtonsUiModel = uiState.actionButtonsUiState.data,
            hasCsatWidgetShowing = uiState.brcCsatUiState is WidgetBrcCsatUiState.HasData.Showing
        )
    }

    private fun updateSavingsWidget(uiState: BuyerOrderDetailUiState.HasData) {
        when (uiState.savingsWidgetUiState) {
            is SavingsWidgetUiState.Success -> {
                onSuccessGetSavingWidget(uiState)
            }
            is SavingsWidgetUiState.Error -> {
                stickyActionButton?.hideSavingWidget()
            }
            is SavingsWidgetUiState.Hide -> {
                stickyActionButton?.hideSavingWidget()
            }
            else -> {
                // noop
            }
        }
    }

    private fun updateBrcCsatWidget(uiState: BuyerOrderDetailUiState.HasData) {
        binding?.widgetBrcBom?.setup(uiState.brcCsatUiState)
    }

    private fun onSuccessGetSavingWidget(uiState: BuyerOrderDetailUiState.HasData) {
        val savingWidgetData = (
            uiState.savingsWidgetUiState as?
                SavingsWidgetUiState.Success
            )?.data

        if (savingWidgetData == null) {
            stickyActionButton?.hideSavingWidget()
            return
        }

        viewModel.changeOneTimeMethod(
            OrderOneTimeEvent.ImpressSavingsWidget(
                orderId = viewModel.getOrderId(),
                isPlus = savingWidgetData.isPlus,
                isMixPromo = savingWidgetData.plusTicker.rightText.isNotEmpty()
            )
        )

        stickyActionButton?.setupSavingWidget(
            savingWidgetData
        )
    }

    private fun setupToolbarMenu(showChatIcon: Boolean) {
        if (showChatIcon) {
            toolbarMenuAnimator?.transitionToShowChatIcon()
        }
    }

    private fun containsAskSellerButton(actionButtonsUiModel: ActionButtonsUiModel): Boolean {
        return actionButtonsUiModel.primaryActionButton.key == BuyerOrderDetailActionButtonKey.ASK_SELLER ||
            actionButtonsUiModel.secondaryActionButtons.any { it.key == BuyerOrderDetailActionButtonKey.ASK_SELLER }
    }

    private fun onSuccessReceiveConfirmation(data: FinishOrderResponse.Data.FinishOrderBuyer) {
        bottomSheetManager.finishReceiveConfirmationBottomSheetLoading()
        bottomSheetManager.dismissBottomSheets()
        loadBuyerOrderDetail(false)
        getMedalTouchPoint(viewModel.getOrderId().toLongOrZero()) {
            showCommonToaster(data.message.firstOrNull().orEmpty())
        }
    }

    private fun getMedalTouchPoint(orderId: Long, initialLoad: Boolean = false, backupAction: (() -> Unit)? = null) {
        if (isScpRewardTouchPointEnabled()) {
            scpMedalTouchPointViewModel.getMedalTouchPoint(
                orderId = orderId,
                sourceName = SOURCE_NAME_FOR_MEDAL_TOUCH_POINT,
                initialLoad = initialLoad
            )
        } else {
            backupAction?.invoke()
        }
    }

    private fun onFailedReceiveConfirmation(throwable: Throwable) {
        bottomSheetManager.finishReceiveConfirmationBottomSheetLoading()
        throwable.showErrorToaster()
    }

    private fun onSuccessAddToCart(data: AtcMultiData) {
        val msg = StringUtils.convertListToStringDelimiter(data.atcMulti.buyAgainData.message, ",")
        if (data.atcMulti.buyAgainData.success == 1) {
            showCommonToaster(msg, getString(R.string.label_see)) {
                navigator.openAppLink(ApplinkConst.CART, false)
            }
        } else {
            showErrorToaster(msg)
        }
    }

    private fun onFailedSingleAddToCart(throwable: Throwable) {
        val errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information)
            .orEmpty()
        showErrorToaster(errorMessage)
    }

    private fun onFailedMultiAddToCart(result: MultiATCState.Fail) {
        if (result.throwable == null) {
            showErrorToaster(result.message.getString(context))
        } else {
            val errorMessage = context?.let {
                ErrorHandler.getErrorMessage(it, result.throwable)
            } ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information)
                .orEmpty()
            showErrorToaster(errorMessage)
        }
    }

    private fun onFailedGetBuyerOrderDetail(throwable: Throwable?) {
        hideLoader()
        showGlobalErrorState()
        buyerOrderDetailLoadMonitoring?.startRenderPerformanceMonitoring()
        val errorType = when (throwable) {
            is MessageErrorException -> null
            is SocketTimeoutException, is UnknownHostException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }

        if (errorType == null) {
            globalErrorBuyerOrderDetail?.showMessageExceptionError(throwable)
        } else {
            globalErrorBuyerOrderDetail?.setType(errorType)
        }
        toolbarMenuAnimator?.transitionToEmpty()
        swipeRefreshBuyerOrderDetail?.isRefreshing = false
        stickyActionButton?.hideSavingWidget()
        binding?.widgetBrcBom?.setup(WidgetBrcCsatUiState.getDefaultState())
        stopLoadTimeMonitoring()
        EmbraceMonitoring.logBreadcrumb(BREADCRUMB_BOM_DETAIL_SHOWING_ERROR)
    }

    private fun onFullscreenLoadingBuyerOrderDetail() {
        showLoader()
        toolbarMenuAnimator?.transitionToEmpty()
        binding?.widgetBrcBom?.setup(WidgetBrcCsatUiState.getDefaultState())
        EmbraceMonitoring.logBreadcrumb(BREADCRUMB_BOM_DETAIL_FULL_SCREEN_LOADING)
    }

    private fun onPullRefreshLoadingBuyerOrderDetail(
        uiState: BuyerOrderDetailUiState.HasData.PullRefreshLoading
    ) {
        swipeRefreshBuyerOrderDetail?.isRefreshing = true
        updateToolbarMenu(uiState)
        updateContent(uiState)
        updateStickyButtons(uiState)
        updateSavingsWidget(uiState)
        updateBrcCsatWidget(uiState)
        EmbraceMonitoring.logBreadcrumb(BREADCRUMB_BOM_DETAIL_PULL_REFRESH_LOADING)
    }

    private fun GlobalError.showMessageExceptionError(
        throwable: Throwable?
    ) {
        this.apply {
            setType(GlobalError.SERVER_ERROR)
            val errorMessage = context?.let {
                ErrorHandler.getErrorMessage(it, throwable)
            }
                ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information)
                    .orEmpty()
            errorDescription.show()
            errorDescription.text = errorMessage
        }
    }

    private fun setupToolbarMenuIcon() {
        toolbarBuyerOrderDetail?.apply {
            toolbarMenuIcons?.let {
                addCustomRightContent(it)
            }
        }
    }

    private fun showCommonToaster(
        message: String,
        actionText: String = "",
        onActionClicked: () -> Unit = {}
    ) {
        if (message.isNotBlank()) {
            view?.let { view ->
                Toaster.build(
                    view,
                    message,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    actionText
                ) {
                    onActionClicked()
                }.show()
            }
        }
    }

    private fun showErrorToaster(
        message: String,
        actionText: String = "",
        onActionClicked: () -> Unit = {}
    ) {
        if (message.isNotBlank()) {
            view?.let { view ->
                Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, actionText) {
                    onActionClicked()
                }.show()
            }
        }
    }

    private fun Throwable.showErrorToaster() {
        val errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, this)
        } ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information)
            .orEmpty()
        showErrorToaster(errorMessage)
    }

    private fun handleRequestCancelResult(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_CODE_INSTANT_CANCEL_BUYER_REQUEST) {
            val resultMessage =
                data?.getStringExtra(BuyerOrderDetailMiscConstant.RESULT_MSG_INSTANT_CANCEL)
                    .orEmpty()
            val result =
                data?.getIntExtra(BuyerOrderDetailMiscConstant.RESULT_CODE_INSTANT_CANCEL, 1)
                    ?: 1
            if (result == BuyerOrderDetailMiscConstant.RESULT_BUYER_REQUEST_CANCEL_STATUS_SHOULD_SHOW_TOASTER) {
                if (resultMessage.isNotBlank()) {
                    showCommonToaster(resultMessage)
                }
            } else if (result == BuyerOrderDetailMiscConstant.RESULT_BUYER_REQUEST_CANCEL_STATUS_SHOULD_SHOW_DIALOG) {
                val popupTitle =
                    data?.getStringExtra(BuyerOrderDetailMiscConstant.RESULT_POPUP_TITLE_INSTANT_CANCEL)
                        .orEmpty()
                val popupBody =
                    data?.getStringExtra(BuyerOrderDetailMiscConstant.RESULT_POPUP_BODY_INSTANT_CANCEL)
                        .orEmpty()
                if (popupTitle.isNotBlank() && popupBody.isNotBlank()) {
                    context?.let { context ->
                        requestCancelResultDialog.apply {
                            setTitle(popupTitle)
                            setBody(popupBody)
                            show(context)
                        }
                    }
                }
            }
            if (result != BuyerOrderDetailMiscConstant.RESULT_BUYER_REQUEST_CANCEL_STATUS_FAILED) {
                loadBuyerOrderDetail(false)
            }
        } else if (resultCode == RESULT_CODE_CANCEL_ORDER_DISABLE) {
            loadBuyerOrderDetail(false)
        }
        bottomSheetManager.dismissBottomSheets()
    }

    private fun handleComplaintResult() {
        loadBuyerOrderDetail(false)
        bottomSheetManager.dismissBottomSheets()
    }

    private fun handleResultPartialOrderFulfillment(data: Intent?) {
        handleResultRefreshOnly()
        val toasterMessage =
            data?.getStringExtra(ApplinkConstInternalOrder.PartialOrderFulfillmentKey.TOASTER_MESSAGE)

        if (!toasterMessage.isNullOrBlank()) {
            view?.run {
                Toaster.build(
                    view = this,
                    text = toasterMessage,
                    type = Toaster.TYPE_NORMAL,
                    duration = Toaster.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleResultOrderExtension(data: Intent?) {
        val isOrderExtend = data?.getBooleanExtra(
            ApplinkConstInternalOrder.OrderExtensionKey.IS_ORDER_EXTENDED,
            true
        )
        val toasterMessage =
            data?.getStringExtra(ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_MESSAGE)

        if (isOrderExtend == true) {
            handleResultRefreshOnly()
        } else {
            bottomSheetManager.dismissBottomSheets()
        }

        if (!toasterMessage.isNullOrBlank()) {
            val toasterType = data.getIntExtra(
                ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_TYPE,
                Toaster.TYPE_NORMAL
            )
            view?.run {
                Toaster.build(
                    view = this,
                    text = toasterMessage,
                    type = toasterType,
                    duration = Toaster.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleResultRefreshOnly() {
        loadBuyerOrderDetail(false)
        bottomSheetManager.dismissBottomSheets()
    }

    private fun handleBrcCsatFormResult(data: Intent?) {
        val message = data?.getStringExtra("message")
        if (!message.isNullOrBlank()) {
            showToasterOnRefreshed(message, WidgetBrcCsat.ANIMATION_DURATION)
            loadBuyerOrderDetail(false)
        }
    }

    private fun handleShareResult(resultCode: Int, data: Intent?) {
        when (resultCode) {
            ApplinkConstInternalShare.ActivityResult.RESULT_CODE_COPY_LINK -> {
                val message: String =
                    data?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_MESSAGE_SUCCESS_COPY_LINK)
                        .orEmpty()
                showCommonToaster(message)
            }
            ApplinkConstInternalShare.ActivityResult.RESULT_CODE_FAIL_GENERATE_AFFILIATE_LINK -> {
                val messageFailGenerateAffiliateLink: String =
                    data?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_MESSAGE_FAIL_GENERATE_AFFILIATE_LINK).orEmpty()
                val messageCopyLink: String =
                    data?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_MESSAGE_SUCCESS_COPY_LINK).orEmpty()
                val ctaCopyLink: String =
                    data?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_CTA_COPY_LINK).orEmpty()

                showErrorToaster(messageFailGenerateAffiliateLink, ctaCopyLink) {
                    showCommonToaster(messageCopyLink)
                }
            }
        }
    }

    private fun stopLoadTimeMonitoring() {
        rvBuyerOrderDetail?.post {
            buyerOrderDetailLoadMonitoring?.stopRenderPerformanceMonitoring()
        }
    }

    private fun trackBuyAgainProduct() {
        BuyerOrderDetailTracker.eventClickBuyAgain(
            viewModel.getOrderId(),
            viewModel.getUserId()
        )
    }

    private fun trackSuccessATC(
        products: List<ProductListUiModel.ProductUiModel>,
        result: AtcMultiData
    ) {
        BuyerOrderDetailTracker.eventSuccessATC(
            products,
            result.atcMulti.buyAgainData.listProducts,
            viewModel.getOrderId(),
            viewModel.getShopId(),
            viewModel.getShopName(),
            viewModel.getShopType().toString(),
            viewModel.getUserId()
        )
    }

    override fun onEtaChangedClicked(delayedInfo: String) {
        showEtaBottomSheet(delayedInfo)
    }

    override fun onClickPodPreview() {
        BuyerOrderDetailTracker.eventClickSeePodPreview(
            orderId = viewModel.getOrderId(),
            orderStatusCode = viewModel.getOrderStatusId()
        )
    }

    private fun showEtaBottomSheet(etaChangedDescription: String) {
        val delayedEtaBottomSheetFragment =
            DelayedEtaBottomSheetFragment.newInstance(etaChangedDescription)
        parentFragmentManager?.run {
            delayedEtaBottomSheetFragment.show(this, "")
        }
    }

    override fun onProductCardClick(recommendationItem: RecommendationItem, applink: String) {
        RecommendationWidgetTracker.sendClickTracker(recommendationItem, userSession.userId)
        RouteManager.route(context, applink)
    }

    override fun onProductCardImpress(recommendationItem: RecommendationItem) {
        context?.let {
            TrackingQueue(it).putEETracking(
                RecommendationWidgetTracker.getImpressionTracker(
                    recommendationItem,
                    userSession.userId
                ) as HashMap<String, Any>
            )
        }
    }

    override fun onSeeAllProductCardClick(appLink: String) {
        RouteManager.route(context, appLink)
    }

    override fun setViewToLifecycleOwner(observer: LifecycleObserver) {
        viewLifecycleOwner.lifecycle.addObserver(observer)
    }

    override fun hidePgRecommendation() {
        rvBuyerOrderDetail?.post { adapter.removePgRecommendation() }
    }

    override fun onCollapseProductList() {
        BuyerOrderDetailTracker.eventClickSeeLessProduct()
        viewModel.collapseProductList()
    }

    override fun onExpandProductList() {
        BuyerOrderDetailTracker.eventClickSeeAllProduct()
        viewModel.expandProductList()
    }

    override fun estimateRefundInfoClicked(estimateInfoUiModel: EstimateInfoUiModel) {
        BuyerOrderDetailTracker.eventClickEstimateIconInBom()
        val bottomSheet = PofEstimateRefundInfoBottomSheet.newInstance(
            estimateInfoUiModel.title,
            estimateInfoUiModel.info
        )
        bottomSheet.show(childFragmentManager)
    }

    override fun refundSummaryClicked(refundSummaryRefundUiModel: PofRefundSummaryUiModel) {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(
            PofDetailRefundedBottomSheet.KEY_DETAIL_REFUNDED_UI_MODEL,
            refundSummaryRefundUiModel
        )
        val bottomSheet =
            PofDetailRefundedBottomSheet.newInstance(cacheManager?.id.orEmpty())
        bottomSheet.show(childFragmentManager)
    }

    override fun onClickWidgetListener(appLink: String) {
        val data = (scpMedalTouchPointViewModel.medalTouchPointData.value?.result as? com.tokopedia.scp_rewards_touchpoints.common.Success)?.data
        ScpRewardsCelebrationWidgetAnalytics.clickCelebrationWidget(
            badgeId = data?.scpRewardsMedaliTouchpointOrder?.medaliTouchpointOrder?.medaliID?.orZero().toString(),
            orderId = viewModel.getOrderId(),
            pagePath = BUYER_ORDER_DETAIL_PAGE,
            pageType = BUYER_ORDER_DETAIL_PAGE
        )
        viewModel.hideScpRewardsMedalTouchPointWidget()
        context?.let {
            RouteManager.route(it, appLink)
        }
    }

    private fun isScpRewardTouchPointEnabled(): Boolean = remoteConfig.getBoolean(SCP_REWARDS_MEDALI_TOUCH_POINT, true)

    override fun onOwocInfoClicked(txId: String) {
        BuyerOrderDetailTracker.sendClickOnOrderGroupWidget(viewModel.getOrderId())
        val owocBottomSheet = OwocBottomSheet.newInstance(viewModel.getOrderId(), txId)
        owocBottomSheet.show(childFragmentManager)
    }

    override fun onShareButtonClicked(element: ProductListUiModel.ProductUiModel) {
        if (BuyerOrderDetailShareUtils.isUsingShareEx()) {
            // Validate archived product
            if (element.productUrl.isBlank()) {
                showCommonToaster(
                    getString(R.string.buyer_order_detail_share_product_archived),
                    getString(R.string.buyer_order_detail_share_product_archived_cta)
                )
            } else {
                shareProduct(element)
            }
            return
        }

        BuyerOrderDetailTracker.sendClickOnShareButton(element.orderId, element.productId, element.orderStatusId, userSession.userId)
        val universalShareBottomSheet = UniversalShareBottomSheet.createInstance(view).apply {
            init(object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    BuyerOrderDetailTracker.sendClickOnSelectionOfSharingChannels(shareModel.channel ?: "", element.orderId, element.productId, element.orderStatusId, userSession.userId)
                }

                override fun onCloseOptionClicked() {
                    BuyerOrderDetailTracker.sendClickOnClosingBottomSheet(element.orderId, element.productId, element.orderStatusId, userSession.userId)
                    dismiss()
                }
            })

            enableDefaultShareIntent()
            imageSaved(element.productThumbnailUrl)
            setUtmCampaignData("Order", userSession.userId, listOf(element.productId, element.orderId), "share")

            val shareString = this@BuyerOrderDetailFragment.getString(R.string.buyer_order_detail_share_text, element.priceText)
            setShareText("$shareString%s")

            setLinkProperties(
                LinkProperties(
                    linkerType = PRODUCT_TYPE,
                    ogTitle = "${element.productName} - ${element.priceText}",
                    ogImageUrl = element.productThumbnailUrl,
                    desktopUrl = element.productUrl,
                    deeplink = Uri.parse(UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, element.productId)).toString(),
                    id = element.productId
                )
            )
            setMetaData(
                element.productName,
                element.productThumbnailUrl,
                imageList = arrayListOf(element.productThumbnailUrl)
            )
            val inputShare = AffiliateInput().apply {
                pageDetail = PageDetail(
                    pageId = element.shopId ?: "",
                    pageType = PageType.PDP.value,
                    siteId = SITE_ID_AFFILIATE_LINK_ELIGIBILITY,
                    verticalId = VERTICAL_ID_AFFILIATE_LINK_ELIGIBILITY
                )
                pageType = PageType.PDP.value
                product = Product(
                    element.productId,
                    element.categoryId,
                    productPrice = element.price.toString(),
                    productStatus = PRODUCT_STATUS_AFFILIATE_LINK_ELIGIBILITY,
                    maxProductPrice = MAX_PRODUCT_PRICE_AFFILIATE_LINK_ELIGIBILITY
                )
                shop = Shop(shopID = element.shopId, shopStatus = SHOP_STATUS_AFFILIATE_LINK_ELIGIBILITY, isOS = false, isPM = false)
                affiliateLinkType = AffiliateLinkType.PDP
            }
            enableAffiliateCommission(AffiliateInput())
        }
        universalShareBottomSheet.show(
            childFragmentManager,
            this@BuyerOrderDetailFragment
        )
        BuyerOrderDetailTracker.eventImpressionShareBottomSheet(element.orderId, element.productId, element.orderStatusId, userSession.userId)
    }

    private fun shareProduct(element: ProductListUiModel.ProductUiModel) {
        val label = "{share_id} - ${element.productId} - ${element.orderId} - ${element.orderStatusId}"
        val affiliateLabel = "{share_id} - ${element.productId} - ${element.orderId}"
        val shareApplink = "${ApplinkConstInternalShare.SHARE}?" +
            "${ApplinkConstInternalShare.Param.PRODUCT_ID}=${element.productId}" +
            "&${ApplinkConstInternalShare.Param.SHOP_ID}=${element.shopId}" +
            "&${ApplinkConstInternalShare.Param.PAGE_TYPE}=${ApplinkConstInternalShare.PageType.ORDER_DETAIL}" +
            "&${ApplinkConstInternalShare.Param.DEFAULT_URL}=${element.productUrl}" +
            "&${ApplinkConstInternalShare.Param.LABEL_ACTION_CLICK_SHARE_ICON}=$label" +
            "&${ApplinkConstInternalShare.Param.LABEL_ACTION_CLICK_CLOSE_ICON}=$label" +
            "&${ApplinkConstInternalShare.Param.LABEL_ACTION_CLICK_CHANNEL}={channel} - $label" +
            "&${ApplinkConstInternalShare.Param.LABEL_IMPRESSION_BOTTOMSHEET}=$label" +
            "&${ApplinkConstInternalShare.Param.LABEL_IMPRESSION_AFFILIATE_REGISTRATION}=$affiliateLabel" +
            "&${ApplinkConstInternalShare.Param.LABEL_ACTION_CLICK_AFFILIATE_REGISTRATION}=$affiliateLabel" +
            "&${ApplinkConstInternalShare.Param.UTM_CAMPAIGN}=Order-{share_id}-${element.productId}-${element.orderId}"

        val intent = RouteManager.getIntent(context, shareApplink)
        startActivityForResult(intent, BuyerOrderDetailIntentCode.REQUEST_CODE_SHARE)
    }

    override fun onBmgmItemClicked(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        val productUrl = uiModel.productUrl
        if (productUrl.isNotBlank()) {
            navigator.openProductUrl(productUrl)
            BuyerOrderDetailTracker.eventClickProduct(uiModel.orderStatusId, uiModel.orderId)
        } else {
            showToaster(
                getString(R.string.buyer_order_detail_error_message_cant_open_snapshot_when_waiting_invoice),
                getString(R.string.buyer_order_detail_oke)
            )
        }
    }

    override fun onBmgmItemAddToCart(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        val productUiModel = ProductListUiStateMapper.mapToProductListProductUiModel(uiModel)
        onBuyAgainButtonClicked(productUiModel)
    }

    override fun onBmgmItemSeeSimilarProducts(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        navigator.openAppLink(uiModel.button?.url.orEmpty(), false)
        BuyerOrderDetailTracker.eventClickSimilarProduct(
            uiModel.orderStatusId,
            uiModel.orderId
        )
    }

    override fun onBmgmItemWarrantyClaim(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        navigator.openAppLink(uiModel.button?.url.orEmpty(), true)
        BuyerOrderDetailTracker.eventClickWarrantyClaim(uiModel.orderId)
    }

    override fun onBmgmItemImpressed(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        viewModel.impressBmgmProduct(uiModel)
    }

    private fun showToaster(message: String, actionText: String) {
        view?.let {
            Toaster.buildWithAction(
                view = it,
                text = message,
                duration = Toaster.LENGTH_SHORT,
                type = Toaster.TYPE_NORMAL,
                actionText = actionText
            ).show()
        }
    }

    override fun initGroupBooking(gojekOrderId: String, source: String) {
        viewModel.initGroupBooking(gojekOrderId, source)
    }

    override fun onChatButtonClicked(gojekOrderId: String, source: String, counter: String) {
        val value = viewModel.buyerOrderDetailUiState.value
        if (value is BuyerOrderDetailUiState.HasData) {
            val orderStatus = value.orderStatusUiState.data.orderStatusHeaderUiModel.orderStatus
            val tokopediaOrderId = viewModel.getOrderId()
            BuyerOrderDetailTracker.sendClickChatButton(
                orderStatus,
                tokopediaOrderId,
                gojekOrderId,
                source,
                counter
            )
        }
    }

    private fun observeGroupBooking() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.groupBookingUiState.collectLatest {
                    if (it.error == null) {
                        viewModel.fetchUnReadChatCount()
                    } else {
                        adapter.updateCourierCounter(0)
                    }
                }
            }
        }
    }

    private fun observeChatUnreadCounter() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatCounterUiState.collectLatest {
                    if (it.error == null) {
                        adapter.updateCourierCounter(it.counter)
                    } else {
                        adapter.updateCourierCounter(0)
                    }
                }
            }
        }
    }

    private fun showToasterOnRefreshed(message: String, delay: Long = 0L) {
        var previousState = viewModel.buyerOrderDetailUiState.value
        var toasterShowed = false
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel
                .buyerOrderDetailUiState
                .takeWhile { !toasterShowed }
                .collectLatest { newState ->
                    val isCurrentlyShowingData = newState is BuyerOrderDetailUiState.HasData.Showing
                    val isPreviouslyLoadingData = previousState is BuyerOrderDetailUiState.FullscreenLoading || previousState is BuyerOrderDetailUiState.HasData.PullRefreshLoading
                    previousState = newState
                    if (isCurrentlyShowingData && isPreviouslyLoadingData) {
                        brcCsatShowToasterRunnable = Runnable {
                            showCommonToaster(message, getString(R.string.bom_brc_csat_toaster_action_text))
                            brcCsatShowToasterRunnable = null
                        }.also { binding?.widgetBrcBom?.postDelayed(it, delay) }
                        toasterShowed = true
                    }
                }
        }
    }

    inner class AddOnListener : AddOnViewHolder.Listener {
        override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
            // noop, buyer add on doesn't have copy function
        }

        override fun onAddOnsExpand(isExpand: Boolean, addOnsIdentifier: String) {
            viewModel.expandCollapseAddOn(addOnsIdentifier, isExpand)
        }

        override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
            BuyerOrderDetailTracker.AddOnsInformation.clickAddOnsInfo(
                orderId = viewModel.getOrderId(),
                addOnsType = type
            )
            navigator.openAppLink(infoLink, false)
        }

        override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {
            // noop, add on is not clickable
        }
    }

    inner class ProductBenefitListener : AddOnViewHolder.Listener {
        override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
            // noop, product benefit doesn't have copyable description
        }

        override fun onAddOnsExpand(isExpand: Boolean, addOnsIdentifier: String) {
            viewModel.expandCollapseBmgmProductBenefit(addOnsIdentifier, isExpand)
        }

        override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
            // noop, product benefit doesn't have clickable info
        }

        override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {
            if (addOn.addOnsUrl.isNotBlank()) {
                navigator.openProductUrl(addOn.addOnsUrl)
            } else {
                showToaster(
                    context?.getString(R.string.buyer_order_detail_error_message_cant_open_snapshot_when_waiting_invoice).orEmpty(),
                    context?.getString(R.string.buyer_order_detail_oke).orEmpty()
                )
            }
        }
    }
}
