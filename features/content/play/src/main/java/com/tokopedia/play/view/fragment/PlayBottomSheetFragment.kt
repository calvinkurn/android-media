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
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.analytic.ProductAnalyticHelper
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.extensions.isCouponSheetsShown
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.extensions.isProductSheetsShown
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.action.AtcProductAction
import com.tokopedia.play.view.uimodel.action.AtcProductVariantAction
import com.tokopedia.play.view.uimodel.action.BuyProductAction
import com.tokopedia.play.view.uimodel.action.BuyProductVariantAction
import com.tokopedia.play.view.uimodel.action.ClickCloseLeaderboardSheetAction
import com.tokopedia.play.view.uimodel.action.RefreshLeaderboard
import com.tokopedia.play.view.uimodel.action.SelectVariantOptionAction
import com.tokopedia.play.view.uimodel.action.SendUpcomingReminder
import com.tokopedia.play.view.uimodel.action.RetryGetTagItemsAction
import com.tokopedia.play.view.viewcomponent.*
import com.tokopedia.play.view.uimodel.recom.PlayEmptyBottomSheetInfoUiModel
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VariantUiModel
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayInteractiveLeaderboardViewComponent
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by jegul on 06/03/20
 */
class PlayBottomSheetFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val analytic: PlayAnalytic,
    private val newAnalytic: PlayNewAnalytic,
) : TkpdBaseV4Fragment(),
    PlayFragmentContract,
    ProductSheetViewComponent.Listener,
    VariantSheetViewComponent.Listener,
    PlayInteractiveLeaderboardViewComponent.Listener,
    ShopCouponSheetViewComponent.Listener
{

    companion object {
        private const val REQUEST_CODE_LOGIN = 191

        private const val PERCENT_VARIANT_SHEET_HEIGHT = 0.6
    }

    private val productSheetView by viewComponent {
        ProductSheetViewComponent(it, this)
    }
    private val variantSheetView by viewComponent { VariantSheetViewComponent(it, this) }
    private val leaderboardSheetView by viewComponent { PlayInteractiveLeaderboardViewComponent(it, this) }
    private val couponSheetView by viewComponent { ShopCouponSheetViewComponent(it, this) }

    private val offset16 by lazy { context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4) ?: 0 }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayBottomSheetViewModel

    private val variantSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_VARIANT_SHEET_HEIGHT).toInt()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    private lateinit var productAnalyticHelper: ProductAnalyticHelper

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
        analytic.getTrackingQueue().sendAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoadingView()
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return getLoadingDialogFragment()?.isVisible == true
    }

    /**
     * ProductSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: ProductSheetViewComponent) {
        closeProductSheet()
    }

    override fun onBuyButtonClicked(
        view: ProductSheetViewComponent,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section
    ) {
        shouldCheckProductVariant(product, sectionInfo, ProductAction.Buy)
    }

    override fun onAtcButtonClicked(
        view: ProductSheetViewComponent,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section
    ) {
        shouldCheckProductVariant(product, sectionInfo, ProductAction.AddToCart)
    }

    override fun onProductCardClicked(
        view: ProductSheetViewComponent,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int
    ) {
        shouldOpenProductDetail(product, sectionInfo, position)
    }

    override fun onEmptyButtonClicked(view: ProductSheetViewComponent) {
        dismissSheets()
    }

    override fun onProductsImpressed(
        view: ProductSheetViewComponent,
        products: List<Pair<PlayProductUiModel.Product, Int>>,
        sectionInfo: ProductSectionUiModel.Section
    ) {
        if(playViewModel.bottomInsets.isProductSheetsShown) {
            if(sectionInfo.config.type == ProductSectionType.TokoNow) newAnalytic.impressProductBottomSheetNow(products, sectionInfo)
            else analytic.impressBottomSheetProducts(products, sectionInfo)
        }
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

    override fun onReminderImpressed(
        view: ProductSheetViewComponent,
        section: ProductSectionUiModel.Section
    ) {
        playViewModel.sendUpcomingReminderImpression(section)
    }

    /**
     * VariantSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: VariantSheetViewComponent) {
        closeVariantSheet()
    }

    override fun onActionClicked(variant: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, action: ProductAction) {
        playViewModel.submitAction(
            if (action == ProductAction.Buy) BuyProductVariantAction(variant.id, sectionInfo)
            else AtcProductVariantAction(variant.id, sectionInfo)
        )
    }

    override fun onVariantOptionClicked(option: VariantOptionWithAttribute) {
        playViewModel.submitAction(SelectVariantOptionAction(option))
    }

    /**
     * LeaderboardSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        playViewModel.submitAction(ClickCloseLeaderboardSheetAction)
    }

    override fun onRefreshButtonClicked(view: PlayInteractiveLeaderboardViewComponent) {
        newAnalytic.clickRefreshLeaderBoard(interactiveId = playViewModel.interactiveData.id, shopId = playViewModel.partnerId.toString(), channelId = playViewModel.channelId)
        playViewModel.submitAction(RefreshLeaderboard)
    }

    override fun onRefreshButtonImpressed(view: PlayInteractiveLeaderboardViewComponent) {
        newAnalytic.impressRefreshLeaderBoard(shopId = playViewModel.partnerId.toString(), interactiveId = playViewModel.interactiveData.id, channelId = playViewModel.channelId)
    }

    override fun onLeaderBoardImpressed(
        view: PlayInteractiveLeaderboardViewComponent,
        leaderboard: PlayLeaderboardUiModel
    ) {
        if (leaderboard.leaderBoardType != LeadeboardType.Quiz) return
        newAnalytic.impressLeaderBoard(shopId = playViewModel.partnerId.toString(), interactiveId = leaderboard.id, channelId = playViewModel.channelId)
    }

    /**
     * CouponSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: ShopCouponSheetViewComponent) {
        playViewModel.hideCouponSheet()
    }

    override fun onVouchersImpressed(view: ShopCouponSheetViewComponent, vouchers: List<PlayVoucherUiModel.Merchant>) {
        trackImpressedVoucher(vouchers)
    }

    override fun onCopyVoucherCodeClicked(view: ShopCouponSheetViewComponent, voucher: PlayVoucherUiModel.Merchant) {
        copyToClipboard(content = voucher.code)
        doShowToaster(
            bottomSheetType = BottomInsetsType.CouponSheet,
            toasterType = Toaster.TYPE_NORMAL,
            message = getString(R.string.play_voucher_code_copied),
            actionText = getString(R.string.play_action_lihat),
            actionClickListener = {
                RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
            }
        )
        analytic.clickCopyVoucher(voucher)
    }

    override fun onVoucherItemClicked(
        view: ShopCouponSheetViewComponent,
        voucher: PlayVoucherUiModel.Merchant
    ) {
        doShowToaster(
            bottomSheetType = BottomInsetsType.CouponSheet,
            toasterType = Toaster.TYPE_NORMAL,
            message = getString(R.string.play_voucher_public),
        )
    }

    override fun onVoucherScrolled(view: ShopCouponSheetViewComponent, lastPositionViewed: Int) {
        analytic.scrollMerchantVoucher(lastPositionViewed)
    }

    fun showVariantSheet(
        action: ProductAction,
        product: PlayProductUiModel.Product,
    ) {
        variantSheetView.setAction(action)
        playViewModel.onShowVariantSheet(variantSheetMaxHeight, product, action)
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
        observeBottomInsetsState()

        observeUiState()
        observeUiEvent()
    }

    private fun initAnalytic() {
        productAnalyticHelper = ProductAnalyticHelper(analytic, newAnalytic)
    }

    private fun closeProductSheet() {
        playViewModel.onHideProductSheet()
    }

    private fun shouldCheckProductVariant(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, action: ProductAction) {
        if (product.isVariantAvailable) {
            showVariantSheet(action, product)
            analytic.clickActionProductWithVariant(product.id, action)
        }

        playViewModel.submitAction(
            if (action == ProductAction.Buy) BuyProductAction(sectionInfo, product)
            else AtcProductAction(sectionInfo, product)
        )
    }

    private fun closeVariantSheet() {
        playViewModel.onHideVariantSheet()
    }

    private fun showLoadingView() {
        getOrCreateLoadingDialogFragment()
            .showNow(childFragmentManager)
    }

    private fun hideLoadingView() {
        val loadingDialog = getLoadingDialogFragment()
        loadingDialog?.dismiss()
    }

    private fun shouldOpenProductDetail(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, position: Int) {
        viewModel.doInteractionEvent(InteractionEvent.OpenProductDetail(product = product, sectionInfo = sectionInfo, position = position))
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
            is InteractionEvent.OpenProductDetail -> doOpenProductDetail(event.product, event.sectionInfo, event.position)
        }
    }

    private fun doOpenProductDetail(product: PlayProductUiModel.Product, configUiModel: ProductSectionUiModel.Section, position: Int) {
        if (product.applink != null && product.applink.isNotEmpty()) {
            if(configUiModel.config.type == ProductSectionType.TokoNow)
                newAnalytic.clickProductBottomSheetNow(product, configUiModel, position)
            else analytic.clickProduct(product, configUiModel, position)
            openPageByApplink(product.applink, pipMode = true)
        }
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

    private fun dismissSheets(){
        playFragment.hideKeyboard()
        playViewModel.hideInsets(isKeyboardHandled = true)
    }

    /**
     * Observe
     */
    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            val productSheetState = it[BottomInsetsType.ProductSheet]
            val variantSheetState = it[BottomInsetsType.VariantSheet]
            val leaderboardSheetState = it[BottomInsetsType.LeaderboardSheet]

            if (productSheetState != null && !productSheetState.isPreviousStateSame) {
                when (productSheetState) {
                    is BottomInsetsState.Hidden -> if (!it.isAnyShown) playFragment.onBottomInsetsViewHidden()
                    is BottomInsetsState.Shown -> pushParentPlayBySheetHeight(productSheetState.estimatedInsetsHeight)
                }
            } else if (variantSheetState != null && !variantSheetState.isPreviousStateSame) {
                when (variantSheetState) {
                    is BottomInsetsState.Hidden -> if (!it.isAnyShown) playFragment.onBottomInsetsViewHidden()
                    is BottomInsetsState.Shown -> pushParentPlayBySheetHeight(variantSheetState.estimatedInsetsHeight)
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

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.uiState.withCache().collectLatest { (prevState, state) ->
                when(state.winnerBadge.leaderboards.state) {
                    ResultState.Success ->
                        leaderboardSheetView.setData(
                            state.winnerBadge.leaderboards.data.leaderboardWinners
                        )
                    is ResultState.Fail ->
                        leaderboardSheetView.setError()
                    ResultState.Loading ->
                        leaderboardSheetView.setLoading()
                }

                if (state.status.channelStatus.statusType.isFreeze ||
                    state.status.channelStatus.statusType.isBanned) {
                    viewModel.onFreezeBan()
                    hideLoadingView()
                }

                renderProductSheet(
                    prevState?.tagItems,
                    state.tagItems,
                    state.tagItems.bottomSheetTitle,
                    state.channel.emptyBottomSheetInfo
                )

                renderVoucherSheet(state.tagItems)

                renderVariantSheet(state.selectedVariant)

                if (state.isLoadingBuy) showLoadingView()
                else hideLoadingView()
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.uiEvent.collect { event ->
                when (event) {
                        is BuySuccessEvent -> {
                            RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)


                            val bottomInsetsType = if (event.isVariant) {
                                BottomInsetsType.VariantSheet
                            } else BottomInsetsType.ProductSheet //TEMPORARY

                            val sectionInfo = event.sectionInfo ?: ProductSectionUiModel.Section.Empty

                            if(sectionInfo.config.type == ProductSectionType.TokoNow)
                                newAnalytic.clickBeliNowProduct(
                                    product = event.product,
                                    cartId = event.cartId,
                                    shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                    sectionInfo = sectionInfo,
                                )
                            else if (playViewModel.bottomInsets.isProductSheetsShown) {
                                analytic.clickProductAction(
                                    product = event.product,
                                    cartId = event.cartId,
                                    productAction = ProductAction.Buy,
                                    bottomInsetsType = bottomInsetsType,
                                    shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                    sectionInfo = sectionInfo,
                                )
                            }
                        }
                        is ShowInfoEvent -> {
                            doShowToaster(
                                bottomSheetType = BottomInsetsType.ProductSheet,
                                toasterType = Toaster.TYPE_NORMAL,
                                message = getTextFromUiString(event.message)
                            )
                        }
                        is ShowErrorEvent -> {
                            doShowToaster(
                                bottomSheetType = BottomInsetsType.ProductSheet,
                                toasterType = Toaster.TYPE_ERROR,
                                message = ErrorHandler.getErrorMessage(requireContext(), event.error))
                        }
                        is AtcSuccessEvent -> {
                            val bottomInsetsType = if (event.isVariant) {
                                BottomInsetsType.VariantSheet
                            } else BottomInsetsType.ProductSheet //TEMPORARY

                            val partnerTokoNow = playViewModel.latestCompleteChannelData.partnerInfo.type == PartnerType.TokoNow
                            val (wording, route, toaster) = if(event.product.isTokoNow && partnerTokoNow) {
                                newAnalytic.impressNowToaster()
                                Triple(
                                    getString(R.string.play_add_to_cart_message_success_mixed),
                                    ApplinkConst.TokopediaNow.HOME +getString(R.string.play_tokonow_minicart_applink),
                                    getString(R.string.play_toaster_tokonow_wording)
                                )
                            } else if (event.product.isTokoNow && !partnerTokoNow) Triple(getString(R.string.play_add_to_cart_message_success_tokonow), ApplinkConstInternalMarketplace.CART, getString(R.string.play_toaster_tokonow_wording))
                            else {
                                newAnalytic.impressGlobalToaster()
                                Triple(
                                    getString(R.string.play_add_to_cart_message_success_tokonow),
                                    ApplinkConstInternalMarketplace.CART,
                                    getString(R.string.play_toaster_tokonow_wording)
                                )
                            }

                            doShowToaster(
                                bottomSheetType = bottomInsetsType,
                                toasterType = Toaster.TYPE_NORMAL,
                                message = wording,
                                actionText = toaster,
                                actionClickListener = {
                                    RouteManager.route(requireContext(), route)
                                    if (event.product.isPinned &&
                                        !playViewModel.bottomInsets.isProductSheetsShown) {

                                        newAnalytic.clickLihatToasterAtcPinnedProductCarousel(
                                            channelId = playViewModel.channelId,
                                            channelType = playViewModel.channelType,
                                        )
                                    }

                                    if (event.product.isTokoNow && partnerTokoNow) {
                                        newAnalytic.clickLihatNowToaster()
                                        analytic.clickSeeToasterAfterAtc()
                                    }
                                    else analytic.clickSeeToasterAfterAtc()
                                }
                            )

                            if (event.isVariant) closeVariantSheet()

                            val sectionInfo = event.sectionInfo ?: ProductSectionUiModel.Section.Empty

                            if(sectionInfo.config.type == ProductSectionType.TokoNow)
                                newAnalytic.clickAtcNowProduct(
                                    product = event.product,
                                    cartId = event.cartId,
                                    shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                    sectionInfo = sectionInfo,
                                )
                            else if (playViewModel.bottomInsets.isProductSheetsShown) {
                                analytic.clickProductAction(
                                    product = event.product,
                                    cartId = event.cartId,
                                    productAction = ProductAction.AddToCart,
                                    bottomInsetsType = bottomInsetsType,
                                    shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                    sectionInfo = sectionInfo,
                                )
                            }
                        }
                        else -> {}
                    }
                }
            }
    }

    private fun getTextFromUiString(uiString: UiString): String {
        return when (uiString) {
            is UiString.Text -> uiString.text
            is UiString.Resource -> getString(uiString.resource)
        }
    }

    override fun onReminderClicked(
        view: ProductSheetViewComponent,
        productSectionUiModel: ProductSectionUiModel.Section
    ) {
        playViewModel.submitAction(SendUpcomingReminder(productSectionUiModel))
    }

    private fun trackImpressedVoucher(vouchers: List<PlayVoucherUiModel.Merchant> = couponSheetView.getVisibleVouchers()) {
        if (playViewModel.bottomInsets.isCouponSheetsShown) productAnalyticHelper.trackImpressedVouchers(vouchers)
    }

    override fun onInformationClicked(
        view: ProductSheetViewComponent
    ) {
        newAnalytic.clickInfoNow()
        val appLink = "${ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO}?source=play&channel_id=${playViewModel.channelId}&state=${playViewModel.channelType}"
        openPageByApplink(appLink , pipMode = false)
    }

    override fun onInformationImpressed(view: ProductSheetViewComponent) {
        newAnalytic.impressInfoNow()
    }

    /**
     * Render View
     */
    private fun renderProductSheet(
        prevTagItem: TagItemUiModel?,
        tagItem: TagItemUiModel,
        bottomSheetTitle: String,
        emptyBottomSheetInfoUi: PlayEmptyBottomSheetInfoUiModel,
    ) {
        if (tagItem.resultState.isLoading && tagItem.product.productSectionList.isEmpty()) {
            productSheetView.showPlaceholder()
        } else if (tagItem.resultState is ResultState.Fail) {
            productSheetView.showError(
                isConnectionError = tagItem.resultState.error is ConnectException ||
                        tagItem.resultState.error is UnknownHostException,
                onError = { playViewModel.submitAction(RetryGetTagItemsAction) }
            )
        } else if (tagItem.product.productSectionList.isNotEmpty()) {
            productSheetView.setProductSheet(
                sectionList = tagItem.product.productSectionList,
                voucherList = tagItem.voucher.voucherList,
                title = bottomSheetTitle,
            )
            productAnalyticHelper.sendImpressedProductSheets()
        } else {
            productSheetView.showEmpty(emptyBottomSheetInfoUi)
        }
    }

    private fun renderVoucherSheet(tagItem: TagItemUiModel) {
        if (tagItem.voucher.voucherList.isNotEmpty()) {
            couponSheetView.setVoucherList(
                voucherList = tagItem.voucher.voucherList
            )
        }
    }

    private fun renderVariantSheet(variant: NetworkResult<VariantUiModel>) {
        when (variant) {
            NetworkResult.Loading -> variantSheetView.showPlaceholder()
            is NetworkResult.Success -> variantSheetView.setVariantSheet(variant.data)
            is NetworkResult.Fail -> variantSheetView.showError(
                isConnectionError = variant.error is ConnectException || variant.error is UnknownHostException,
                onError = { }
            )
        }
    }

    private fun getLoadingDialogFragment(): PlayLoadingDialogFragment? {
        return PlayLoadingDialogFragment.get(childFragmentManager)
    }

    private fun getOrCreateLoadingDialogFragment(): PlayLoadingDialogFragment {
        return PlayLoadingDialogFragment.getOrCreate(
            childFragmentManager,
            requireActivity().classLoader
        )
    }
}