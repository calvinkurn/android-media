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
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
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
import com.tokopedia.buyerorderdetail.common.extension.collectLatestWhenResumed
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.FragmentBuyerOrderDetailBinding
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderDetailActivity
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
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
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.EstimateInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.MultiATCState
import com.tokopedia.buyerorderdetail.presentation.model.PofRefundSummaryUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailStickyActionButton
import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailToolbarMenu
import com.tokopedia.buyerorderdetail.presentation.scroller.BuyerOrderDetailRecyclerViewScroller
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
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
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse
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
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

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
    OwocInfoViewHolder.Listener {

    companion object {
        @JvmStatic
        fun newInstance(extras: Bundle): BuyerOrderDetailFragment {
            return BuyerOrderDetailFragment().apply {
                arguments = extras
            }
        }

        private const val SOURCE_NAME_FOR_MEDAL_TOUCH_POINT = "order_detail_page"

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
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

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
            this,
            this,
            digitalRecommendationData,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            navigator,
            this,
            this
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activity?.overridePendingTransition(
            com.tokopedia.resources.common.R.anim.slide_left_in_medium,
            com.tokopedia.resources.common.R.anim.slide_right_out_medium
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coachMarkManager?.dismissCoachMark()
    }

    override fun onBuyAgainButtonClicked(product: ProductListUiModel.ProductUiModel) {
        val productCopy = product.copy(isProcessing = true)
        viewModel.addSingleToCart(productCopy)
        trackBuyAgainProduct()
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
    }

    private fun setupStickyActionButtons() {
        stickyActionButton?.run {
            setViewModel(viewModel)
            setStickyActionButtonClickHandler(stickyActionButtonHandler)
            setBottomSheetManager(bottomSheetManager)
        }
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

    private fun observeMedalTouchPoint() {
        scpMedalTouchPointViewModel.medalTouchPointData.observe(viewLifecycleOwner) {
            when (it.result) {
                is com.tokopedia.scp_rewards_touchpoints.common.Success<*> -> {
                    val data = ((it.result as com.tokopedia.scp_rewards_touchpoints.common.Success<*>).data as ScpRewardsMedalTouchPointResponse)
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
                                marginLeft = resources.getDimension(R.dimen.buyer_order_detail_scp_rewards_medal_touch_point_margin_left).toIntSafely(),
                                marginTop = resources.getDimension(R.dimen.buyer_order_detail_scp_rewards_medal_touch_point_margin_top).toIntSafely(),
                                marginRight = resources.getDimension(R.dimen.buyer_order_detail_scp_rewards_medal_touch_point_margin_right).toIntSafely()
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
        swipeRefreshBuyerOrderDetail?.isRefreshing = false
        stopLoadTimeMonitoring()
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
            actionButtonsUiModel = uiState.actionButtonsUiState.data
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
            showErrorToaster(result.message.getStringValue(context))
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
        stopLoadTimeMonitoring()
    }

    private fun onFullscreenLoadingBuyerOrderDetail() {
        showLoader()
        toolbarMenuAnimator?.transitionToEmpty()
    }

    private fun onPullRefreshLoadingBuyerOrderDetail(
        uiState: BuyerOrderDetailUiState.HasData.PullRefreshLoading
    ) {
        swipeRefreshBuyerOrderDetail?.isRefreshing = true
        updateToolbarMenu(uiState)
        updateContent(uiState)
        updateStickyButtons(uiState)
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
        val data = ((scpMedalTouchPointViewModel.medalTouchPointData.value?.result as? com.tokopedia.scp_rewards_touchpoints.common.Success<*>)?.data as ScpRewardsMedalTouchPointResponse)
        ScpRewardsCelebrationWidgetAnalytics.clickCelebrationWidget(
            badgeId = data.scpRewardsMedaliTouchpointOrder.medaliTouchpointOrder.medaliID.toString(),
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
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance(view).apply {
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
                    siteId = "1",
                    verticalId = "1"
                )
                pageType = PageType.PDP.value
                product = Product(
                    element.productId,
                    element.categoryId,
                    productPrice = element.price.toString(),
                    productStatus = "active",
                    maxProductPrice = "0"
                )
                shop = Shop(shopID = element.shopId, shopStatus = 1, isOS = false, isPM = false)
                affiliateLinkType = AffiliateLinkType.PDP
            }
            enableAffiliateCommission(AffiliateInput())
        }
        universalShareBottomSheet?.show(
            requireActivity().supportFragmentManager,
            this@BuyerOrderDetailFragment
        )
        BuyerOrderDetailTracker.eventImpressionShareBottomSheet(element.orderId, element.productId, element.orderStatusId, userSession.userId)
    }
}
