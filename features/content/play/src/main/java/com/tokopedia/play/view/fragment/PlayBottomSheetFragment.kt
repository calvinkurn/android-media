package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.ProductAnalyticHelper
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.extensions.isCouponSheetsShown
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.extensions.isProductSheetsShown
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.action.ClickCloseLeaderboardSheetAction
import com.tokopedia.play.view.uimodel.action.RefreshLeaderboard
import com.tokopedia.play.view.viewcomponent.*
import com.tokopedia.play.view.uimodel.action.RetryGetTagItemsAction
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.viewcomponent.ProductSheetViewComponent
import com.tokopedia.play.view.viewcomponent.ShopCouponSheetViewComponent
import com.tokopedia.play.view.viewcomponent.VariantSheetViewComponent
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.PlayLeaderboardWrapperUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.flow.collectLatest
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by jegul on 06/03/20
 */
class PlayBottomSheetFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        private val analytic: PlayAnalytic
): TkpdBaseV4Fragment(),
        PlayFragmentContract,
        ProductSheetViewComponent.Listener,
        VariantSheetViewComponent.Listener,
        PlayInteractiveLeaderboardViewComponent.Listener,
        ShopCouponSheetViewComponent.Listener
{

    //TODO = remove user report component

    companion object {
        private const val REQUEST_CODE_LOGIN = 191

        private const val PERCENT_VARIANT_SHEET_HEIGHT = 0.6
        private const val PERCENT_FULL_SHEET_HEIGHT = 0.9

        private const val TO_SECONDS_DIVIDER = 1000L
    }

    private val productSheetView by viewComponent { ProductSheetViewComponent(it, this) }
    private val variantSheetView by viewComponent { VariantSheetViewComponent(it, this) }
    private val leaderboardSheetView by viewComponent { PlayInteractiveLeaderboardViewComponent(it, this) }
    private val couponSheetView by viewComponent { ShopCouponSheetViewComponent(it, this) }

    private val offset16 by lazy { resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayBottomSheetViewModel

    private val variantSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_VARIANT_SHEET_HEIGHT).toInt()

    private val userReportSheetHeight: Int
        get() = (requireView().height * PERCENT_FULL_SHEET_HEIGHT).toInt()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    private val generalErrorMessage: String
        get() = getString(R.string.play_general_err_message)

    private lateinit var loadingDialog: PlayLoadingDialogFragment

    private lateinit var productAnalyticHelper: ProductAnalyticHelper

    private var userReportTimeMillis: Long = 0L

    override fun getScreenName(): String = "Play Bottom Sheet"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(
            requireParentFragment(), (requireParentFragment() as PlayFragment).viewModelProviderFactory
        ).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayBottomSheetViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupObserve()
        initAnalytic()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        productAnalyticHelper.sendImpressedProductSheets()
        analytic.getTrackingQueue().sendAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoadingView()
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return ::loadingDialog.isInitialized && loadingDialog.isVisible
    }

    /**
     * ProductSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: ProductSheetViewComponent) {
        closeProductSheet()
    }

    override fun onBuyButtonClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product) {
        shouldCheckProductVariant(product, ProductAction.Buy)
    }

    override fun onAtcButtonClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product) {
        shouldCheckProductVariant(product, ProductAction.AddToCart)
    }

    override fun onProductCardClicked(view: ProductSheetViewComponent, product: PlayProductUiModel.Product, position: Int) {
        shouldOpenProductDetail(product, position)
    }

    override fun onEmptyButtonClicked(view: ProductSheetViewComponent, partnerId: Long) {
        openShopPage(partnerId)
    }

    override fun onProductsImpressed(view: ProductSheetViewComponent, products: List<Pair<PlayProductUiModel.Product, Int>>) {
        trackImpressedProduct(products)
    }

    override fun onProductCountChanged(view: ProductSheetViewComponent) {
        if (playViewModel.bottomInsets.isKeyboardShown) return

        doShowToaster(
                bottomSheetType = BottomInsetsType.ProductSheet,
                toasterType = Toaster.TYPE_NORMAL,
                message = getString(R.string.play_product_updated)
        )
    }

    override fun onInfoVoucherClicked(
        view: ProductSheetViewComponent) {
        playViewModel.showCouponSheet(variantSheetMaxHeight)
    }

    /**
     * VariantSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: VariantSheetViewComponent) {
        closeVariantSheet()
    }

    override fun onAddToCartClicked(view: VariantSheetViewComponent, productModel: PlayProductUiModel.Product) {
        shouldDoActionProduct(productModel, ProductAction.AddToCart, BottomInsetsType.VariantSheet)
    }

    override fun onBuyClicked(view: VariantSheetViewComponent, productModel: PlayProductUiModel.Product) {
        shouldDoActionProduct(productModel, ProductAction.Buy, BottomInsetsType.VariantSheet)
    }

    /**
     * LeaderboardSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        playViewModel.submitAction(ClickCloseLeaderboardSheetAction)
    }

    override fun onRefreshButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        playViewModel.submitAction(RefreshLeaderboard)
    }

    private fun onSubmitUserReport(reasonId: Int, description: String) {
        analytic.clickUserReportSubmissionDialogSubmit()
        val channelData = playViewModel.latestCompleteChannelData

        viewModel.submitUserReport(
            channelId = channelData.id.toLongOrZero(),
            shopId = channelData.partnerInfo.id,
            mediaUrl = getMediaUrl(channelData.id),
            timestamp = getTimestampVideo(channelData.channelDetail.channelInfo.startTime),
            reportDesc = description,
            reasonId = reasonId
        )
    }

    private fun getMediaUrl(channelId: String) : String = "${TokopediaUrl.getInstance().WEB}play/channel/$channelId"

    private fun getTimestampVideo(startTime: String): Long{
        return if(playViewModel.channelType.isLive){
            val startTimeInSecond = startTime.toLongOrZero()
            val duration = (userReportTimeMillis - startTimeInSecond) / TO_SECONDS_DIVIDER
            duration
        }else{
            playViewModel.getVideoTimestamp()
        }
    }


    /**
     * CouponSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: ShopCouponSheetViewComponent) {
        playViewModel.hideCouponSheet()
    }

    override fun onVouchersImpressed(view: ShopCouponSheetViewComponent, vouchers: List<MerchantVoucherUiModel>) {
        trackImpressedVoucher(vouchers)
    }

    override fun onCopyVoucherCodeClicked(view: ShopCouponSheetViewComponent, voucher: MerchantVoucherUiModel) {
        copyToClipboard(content = voucher.code)
        doShowToaster(
            bottomSheetType = BottomInsetsType.CouponSheet,
            toasterType = Toaster.TYPE_NORMAL,
            message = getString(R.string.play_voucher_code_copied),
            actionText = getString(R.string.play_action_ok),
        )
        analytic.clickCopyVoucher(voucher)
    }

    override fun onVoucherScrolled(view: ShopCouponSheetViewComponent, lastPositionViewed: Int) {
        analytic.scrollMerchantVoucher(lastPositionViewed)
    }

    /**
     * Private methods
     */
    private fun setupView(view: View) {
        productSheetView.hide()
        variantSheetView.hide()
        couponSheetView.hide()
        leaderboardSheetView.hide()
    }

    private fun setupObserve() {
        observeLoggedInInteractionEvent()
        observeVariantSheetContent()
        observeBottomInsetsState()
        observeBuyEvent()

        observeUiState()
    }

    private fun initAnalytic() {
        productAnalyticHelper = ProductAnalyticHelper(analytic)
    }

    private fun openShopPage(partnerId: Long) {
        openPageByApplink(ApplinkConst.SHOP, partnerId.toString(), pipMode = true)
    }

    private fun closeProductSheet() {
        playViewModel.onHideProductSheet()
    }

    private fun shouldCheckProductVariant(product: PlayProductUiModel.Product, action: ProductAction) {
        if (product.isVariantAvailable) {
            openVariantSheet(product, action)
            analytic.clickActionProductWithVariant(product.id, action)
        } else {
            shouldDoActionProduct(product, action, BottomInsetsType.ProductSheet)
        }
    }

    private fun openVariantSheet(product: PlayProductUiModel.Product, action: ProductAction) {
        playViewModel.onShowVariantSheet(variantSheetMaxHeight, product, action)
        viewModel.getProductVariant(product, action)
    }

    private fun closeVariantSheet() {
        playViewModel.onHideVariantSheet()
    }

    private fun showLoadingView() {
        if (!::loadingDialog.isInitialized) {
            loadingDialog = PlayLoadingDialogFragment.newInstance()
        }
        loadingDialog.show(childFragmentManager)
    }

    private fun hideLoadingView() {
        if (::loadingDialog.isInitialized) loadingDialog.dismiss()
    }

    private fun shouldDoActionProduct(product: PlayProductUiModel.Product, action: ProductAction, type: BottomInsetsType) {
        viewModel.doInteractionEvent(InteractionEvent.DoActionProduct(product, action, type))
    }

    private fun shouldOpenProductDetail(product: PlayProductUiModel.Product, position: Int) {
        viewModel.doInteractionEvent(InteractionEvent.OpenProductDetail(product, position))
    }

    private fun shouldOpenUserReport() {
        viewModel.doInteractionEvent(InteractionEvent.OpenUserReport)
    }

    private fun doShowToaster(
            bottomSheetType: BottomInsetsType,
            toasterType: Int,
            message: String,
            actionText: String = "",
            actionClickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        when (bottomSheetType) {
            BottomInsetsType.ProductSheet, BottomInsetsType.CouponSheet ->
                Toaster.build(
                        view = requireView(),
                        text = message,
                        type = toasterType,
                        actionText = actionText,
                        clickListener = actionClickListener
                ).show()
            BottomInsetsType.VariantSheet ->
                variantSheetView.showToaster(
                        toasterType = toasterType,
                        message = message,
                        actionText = actionText,
                        actionListener = actionClickListener
                )
            else -> {
                // nothing
            }
        }
    }

    private fun showDialog(title: String, description: String, primaryCTAText: String, secondaryCTAText: String, primaryAction: () -> Unit, secondaryAction: () -> Unit = {}){
        activity?.let {
            val dialog = DialogUnify(context = it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.apply {
                setTitle(title)
                setDescription(description)
                setPrimaryCTAText(primaryCTAText)
                setPrimaryCTAClickListener {
                    primaryAction()
                    dismiss()
                }
                setSecondaryCTAText(secondaryCTAText)
                setSecondaryCTAClickListener {
                    secondaryAction()
                    dismiss()
                }
            }.show()
        }
    }

    private fun pushParentPlayBySheetHeight(productSheetHeight: Int) {
        val statusBarHeight = view?.let { DisplayMetricUtils.getStatusBarHeight(it.context) }.orZero()
        val requiredMargin = offset16
        playFragment.onBottomInsetsViewShown(requireView().height - (productSheetHeight + statusBarHeight + requiredMargin))
    }

    private fun handleLoginInteractionEvent(loginInteractionEvent: LoginStateEvent) {
        when (loginInteractionEvent) {
            is LoginStateEvent.InteractionAllowed -> handleInteractionEvent(loginInteractionEvent.event)
            is LoginStateEvent.NeedLoggedIn -> openLoginPage()
        }
    }

    private fun handleInteractionEvent(event: InteractionEvent) {
        when (event) {
            is InteractionEvent.DoActionProduct -> doActionProduct(event.product, event.action, event.type)
            is InteractionEvent.OpenProductDetail -> doOpenProductDetail(event.product, event.position)
            is InteractionEvent.OpenUserReport -> doActionUserReport()
        }
    }

    private fun doOpenProductDetail(product: PlayProductUiModel.Product, position: Int) {
        if (product.applink != null && product.applink.isNotEmpty()) {
            analytic.clickProduct(product, position)
            openPageByApplink(product.applink, pipMode = true)
        }
    }

    private fun doActionProduct(product: PlayProductUiModel.Product, productAction: ProductAction, type: BottomInsetsType) {
        viewModel.addToCart(product, action = productAction, type = type)
    }

    private fun doActionUserReport(){
        analytic.clickUserReport()
        playViewModel.onShowUserReportSheet(userReportSheetHeight)
        viewModel.getUserReportList()
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = REQUEST_CODE_LOGIN)
    }

    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false, pipMode: Boolean = false) {
        if (pipMode && playViewModel.isPiPAllowed && !playViewModel.isFreezeOrBanned) {
            playViewModel.requestPiPBrowsingPage(
                    OpenApplinkUiModel(applink = applink, params = params.toList(), requestCode, shouldFinish)
            )
        } else {
            openApplink(applink, *params, requestCode = requestCode, shouldFinish = shouldFinish)
        }
    }

    private fun openApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
        if (requestCode == null) {
            RouteManager.route(context, applink, *params)
        } else {
            val intent = RouteManager.getIntent(context, applink, *params)
            startActivityForResult(intent, requestCode)
        }
        activity?.overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) activity?.finish()
    }

    private fun copyToClipboard(content: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("play-room-bottom-sheet", content))
    }

    /**
     * Observe
     */
    private fun observeVariantSheetContent() {
        viewModel.observableProductVariant.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Loading -> if (it.showPlaceholder) variantSheetView.showPlaceholder()
                is PlayResult.Success -> variantSheetView.setVariantSheet(it.data)
                is PlayResult.Failure -> variantSheetView.showError(
                        isConnectionError = it.error is ConnectException || it.error is UnknownHostException,
                        onError = it.onRetry
                )
            }
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            val productSheetState = it[BottomInsetsType.ProductSheet]
            val leaderboardSheetState = it[BottomInsetsType.LeaderboardSheet]

            if (productSheetState != null && !productSheetState.isPreviousStateSame) {
                when (productSheetState) {
                    is BottomInsetsState.Hidden -> if (!it.isAnyShown) playFragment.onBottomInsetsViewHidden()
                    is BottomInsetsState.Shown -> pushParentPlayBySheetHeight(productSheetState.estimatedInsetsHeight)
                }
            } else if (leaderboardSheetState != null && !leaderboardSheetState.isPreviousStateSame) {
                when (leaderboardSheetState) {
                    is BottomInsetsState.Hidden -> if (!it.isAnyShown) playFragment.onBottomInsetsViewHidden()
                    is BottomInsetsState.Shown -> pushParentPlayBySheetHeight(leaderboardSheetState.estimatedInsetsHeight)
                }
            }

            it[BottomInsetsType.ProductSheet]?.let { state ->
                if (state is BottomInsetsState.Shown) productSheetView.showWithHeight(state.estimatedInsetsHeight)
                else productSheetView.hide()
            }

            it[BottomInsetsType.VariantSheet]?.let { state ->
                if (state is BottomInsetsState.Shown) variantSheetView.showWithHeight(state.estimatedInsetsHeight)
                else variantSheetView.hide()
            }
            it[BottomInsetsType.CouponSheet]?.let { state ->
                if (state is BottomInsetsState.Shown) couponSheetView.showWithHeight(state.estimatedInsetsHeight)
                else couponSheetView.hide()
            }

            it[BottomInsetsType.LeaderboardSheet]?.let { state ->
                if (state is BottomInsetsState.Shown) {
                    leaderboardSheetView.showWithHeight(state.estimatedInsetsHeight)
                    playViewModel.submitAction(RefreshLeaderboard)
                }
                else leaderboardSheetView.hide()
            }
        })
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeBuyEvent() {
        viewModel.observableAddToCart.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Loading -> showLoadingView()
                is PlayResult.Success -> {
                    hideLoadingView()
                    val data = it.data.getContentIfNotHandled() ?: return@DistinctObserver

                    if (data.isSuccess) {
                        when (data.action) {
                            ProductAction.Buy -> RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
                            ProductAction.AddToCart -> doShowToaster(
                                    bottomSheetType = data.bottomInsetsType,
                                    toasterType = Toaster.TYPE_NORMAL,
                                    message = getString(R.string.play_add_to_cart_message_success),
                                    actionText = getString(R.string.play_action_view),
                                    actionClickListener = {
                                        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
                                        analytic.clickSeeToasterAfterAtc()
                                    }
                            )
                        }
                        if (data.bottomInsetsType == BottomInsetsType.VariantSheet) {
                            closeVariantSheet()
                        }
                        analytic.clickProductAction(data.product, data.cartId, data.action, data.bottomInsetsType, playViewModel.latestCompleteChannelData.partnerInfo)
                    }
                    else {
                        val errMsg = ErrorHandler.getErrorMessage(requireContext(), data.errorMessage)
                        doShowToaster(
                                bottomSheetType = data.bottomInsetsType,
                                toasterType = Toaster.TYPE_ERROR,
                                message = errMsg
                        )
                    }
                }
            }
        })
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.uiState.withCache().collectLatest { (prevState, state) ->
                when(state.winnerBadge.leaderboards) {
                    is PlayLeaderboardWrapperUiModel.Success ->
                        leaderboardSheetView.setData(state.winnerBadge.leaderboards.data.leaderboardWinners)
                    PlayLeaderboardWrapperUiModel.Error ->
                        leaderboardSheetView.setError()
                    PlayLeaderboardWrapperUiModel.Loading ->
                        leaderboardSheetView.setLoading()
                    PlayLeaderboardWrapperUiModel.Unknown -> {}
                }

                if (state.status.channelStatus.statusType.isFreeze ||
                    state.status.channelStatus.statusType.isBanned) {
                    viewModel.onFreezeBan()
                    hideLoadingView()
                }

                renderProductSheet(
                    prevState?.tagItems,
                    state.tagItems,
                    state.channel.bottomSheetTitle,
                    state.partner.id
                )

                renderVoucherSheet(state.tagItems)
            }
        }
    }

    private fun trackImpressedProduct(products: List<Pair<PlayProductUiModel.Product, Int>> = productSheetView.getVisibleProducts()) {
        if (playViewModel.bottomInsets.isProductSheetsShown) productAnalyticHelper.trackImpressedProducts(products)
    }

    private fun trackImpressedVoucher(vouchers: List<MerchantVoucherUiModel> = couponSheetView.getVisibleVouchers()) {
        if (playViewModel.bottomInsets.isCouponSheetsShown) productAnalyticHelper.trackImpressedVouchers(vouchers)
    }

    /**
     * Render View
     */
    private fun renderProductSheet(
        prevTagItem: TagItemUiModel?,
        tagItem: TagItemUiModel,
        bottomSheetTitle: String,
        partnerId: Long,
    ) {
        if (tagItem.resultState.isLoading && tagItem.product.productList.isEmpty()) {
            productSheetView.showPlaceholder()
        } else if (tagItem.resultState is ResultState.Fail) {
            productSheetView.showError(
                isConnectionError = tagItem.resultState.error is ConnectException ||
                        tagItem.resultState.error is UnknownHostException,
                onError = { playViewModel.submitAction(RetryGetTagItemsAction) }
            )
        } else if (tagItem.product.productList.isNotEmpty()) {
            productSheetView.setProductSheet(
                productList = tagItem.product.productList,
                voucherList = tagItem.voucher.voucherList,
                title = bottomSheetTitle,
            )

            if (tagItem.product.productList != prevTagItem?.product?.productList) trackImpressedProduct()
        } else {
            productSheetView.showEmpty(partnerId)
        }
    }

    private fun renderVoucherSheet(tagItem: TagItemUiModel) {
        if (tagItem.voucher.voucherList.isNotEmpty()) {
            couponSheetView.setVoucherList(
                voucherList = tagItem.voucher.voucherList
            )
        }
    }
}