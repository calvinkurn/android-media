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
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.extensions.isCouponSheetsShown
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.extensions.isProductSheetsShown
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.custom.activityresult.OpenLogin
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.viewcomponent.ProductSheetViewComponent
import com.tokopedia.play.view.viewcomponent.ShopCouponSheetViewComponent
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.ui.leaderboard.PlayGameLeaderboardViewComponent
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
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
    private val router: Router,
    private val resultRegistry: ActivityResultRegistry,
) : TkpdBaseV4Fragment(),
    PlayFragmentContract,
    ProductSheetViewComponent.Listener,
    PlayGameLeaderboardViewComponent.Listener,
    ShopCouponSheetViewComponent.Listener {

    companion object {
        private const val REQUEST_CODE_LOGIN = 191

        private const val PERCENT_VARIANT_SHEET_HEIGHT = 0.6
    }

    private val productSheetView by viewComponent {
        ProductSheetViewComponent(it, this, viewLifecycleOwner.lifecycleScope)
    }
    private val leaderboardSheetView by viewComponent { PlayGameLeaderboardViewComponent(it, this) }
    private val couponSheetView by viewComponent { ShopCouponSheetViewComponent(it, this) }

    private val offset16 by lazy { context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4) ?: 0 }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayBottomSheetViewModel

    private val variantSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_VARIANT_SHEET_HEIGHT).toInt()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    override fun getScreenName(): String = "Play Bottom Sheet"

    private val openLoginForCart = registerForActivityResult(OpenLogin(), resultRegistry) { success ->
        if (success) router.route(context, ApplinkConst.CART)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(
            requireParentFragment(),
            (requireParentFragment() as PlayFragment).viewModelProviderFactory
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoadingView(allowStateLoss = true)
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

    override fun onCartClicked(view: ProductSheetViewComponent) {
        analytic.clickCartFromSheet()
        if (playViewModel.isLoggedIn) {
            router.route(context, ApplinkConst.CART)
        } else {
            openLoginForCart.launch(Unit)
        }
    }

    override fun onImpressedCart(view: ProductSheetViewComponent) {
        analytic.impressCartFromBottomSheet()
    }

    override fun onButtonTransactionClicked(
        view: ProductSheetViewComponent,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        action: ProductAction
    ) {
        shouldCheckProductVariant(product, sectionInfo, action)
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

    override fun onProductImpressed(
        view: ProductSheetViewComponent,
        products: Map<ProductSheetAdapter.Item.Product, Int>
    ) {
        if (!playViewModel.bottomInsets.isProductSheetsShown) return

        if (playViewModel.latestCompleteChannelData.partnerInfo.type == PartnerType.TokoNow) {
            newAnalytic.impressProductBottomSheetNow(products)
        } else {
            analytic.impressBottomSheetProduct(products)
        }
    }

    private fun onProductCountChanged() {
        if (playViewModel.bottomInsets.isKeyboardShown) return

        doShowToaster(
            toasterType = Toaster.TYPE_NORMAL,
            message = getString(R.string.play_product_updated)
        )
    }

    override fun onInfoVoucherClicked(
        view: ProductSheetViewComponent
    ) {
        playViewModel.showCouponSheet(variantSheetMaxHeight)
        newAnalytic.clickInfoVoucher()
    }

    override fun onInfoVoucherImpressed(
        view: ProductSheetViewComponent,
        voucher: PlayVoucherUiModel.Merchant
    ) {
        newAnalytic.impressInfoVoucher(voucher)
    }

    override fun onReminderImpressed(
        view: ProductSheetViewComponent,
        section: ProductSectionUiModel.Section
    ) {
        playViewModel.sendUpcomingReminderImpression(section)
    }

    /**
     * LeaderboardSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: PlayGameLeaderboardViewComponent) {
        playViewModel.submitAction(ClickCloseLeaderboardSheetAction)
    }

    override fun onRefreshButtonClicked(view: PlayGameLeaderboardViewComponent) {
        newAnalytic.clickRefreshLeaderBoard(
            interactiveId = playViewModel.gameData.id,
            shopId = playViewModel.partnerId.toString(),
            channelId = playViewModel.channelId
        )
        playViewModel.submitAction(RefreshLeaderboard)
    }

    override fun onRefreshButtonImpressed(view: PlayGameLeaderboardViewComponent) {
        newAnalytic.impressRefreshLeaderBoard(shopId = playViewModel.partnerId.toString(), interactiveId = playViewModel.gameData.id, channelId = playViewModel.channelId)
    }

    override fun onLeaderBoardImpressed(
        view: PlayGameLeaderboardViewComponent,
        leaderboard: LeaderboardGameUiModel.Header
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

    override fun onVouchersImpressed(view: ShopCouponSheetViewComponent, voucherId: String) {
        if (playViewModel.bottomInsets.isCouponSheetsShown) newAnalytic.impressVoucherBottomSheet(voucherId)
    }

    override fun onCopyVoucherCodeClicked(view: ShopCouponSheetViewComponent, voucher: PlayVoucherUiModel.Merchant) {
        copyToClipboard(content = voucher.code)
        doShowToaster(
            toasterType = Toaster.TYPE_NORMAL,
            message = getString(R.string.play_voucher_code_copied),
            actionText = getString(R.string.play_action_lihat),
            actionClickListener = {
                newAnalytic.clickToasterPrivate(voucherId = voucher.id)
                playViewModel.submitAction(OpenCart)
            }
        )
        newAnalytic.impressToasterPrivate(voucher.id)
        newAnalytic.clickCopyVoucher(voucher.id)
    }

    override fun onVoucherItemClicked(
        view: ShopCouponSheetViewComponent,
        voucher: PlayVoucherUiModel.Merchant
    ) {
        doShowToaster(
            toasterType = Toaster.TYPE_NORMAL,
            message = getString(R.string.play_voucher_public)
        )
        newAnalytic.impressToasterPublic()
        newAnalytic.clickToasterPublic()
    }

    /**
     * Private methods
     */
    private fun setupView(view: View) {
        productSheetView.hide()
        couponSheetView.hide()
        leaderboardSheetView.hide()
    }

    private fun setupObserve() {
        observeLoggedInInteractionEvent()
        observeBottomInsetsState()

        observeUiState()
        observeUiEvent()
    }

    private fun closeProductSheet() {
        playViewModel.onHideProductSheet()
    }

    private fun shouldCheckProductVariant(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, action: ProductAction) {
        if (product.isVariantAvailable) {
            playViewModel.submitAction(ShowVariantAction(product, false))
            analytic.clickActionProductWithVariant(product.id, action)
        } else {
            playViewModel.submitAction(
                when (action) {
                    ProductAction.Buy -> BuyProductAction(sectionInfo, product)
                    ProductAction.AddToCart -> AtcProductAction(sectionInfo, product)
                    ProductAction.OCC -> OCCProductAction(sectionInfo, product)
                }
            )
        }
    }

    private fun showLoadingView() {
        getOrCreateLoadingDialogFragment()
            .showNow(childFragmentManager)
    }

    private fun hideLoadingView(allowStateLoss: Boolean = false) {
        val loadingDialog = getLoadingDialogFragment()

        if (!allowStateLoss) {
            loadingDialog?.dismiss()
        } else {
            loadingDialog?.dismissAllowingStateLoss()
        }
    }

    private fun shouldOpenProductDetail(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, position: Int) {
        viewModel.doInteractionEvent(InteractionEvent.OpenProductDetail(product = product, sectionInfo = sectionInfo, position = position))
    }

    private fun doShowToaster(
        toasterType: Int,
        message: String,
        actionText: String = "",
        actionClickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        Toaster.build(
            view = requireView(),
            text = message,
            type = toasterType,
            actionText = actionText,
            clickListener = actionClickListener
        ).show()
    }

    private fun pushParentPlayBySheetHeight(productSheetHeight: Int) {
        val closeIcon = playFragment.getCloseIconView() ?: return
        val videoOrientation = playViewModel.videoOrientation
        val dstHeight = if (videoOrientation is VideoOrientation.Horizontal) {
            val dstStart = closeIcon.right + offset16
            val dstEnd = requireView().right - dstStart
            val dstWidth = dstEnd - dstStart
            (1 / (videoOrientation.widthRatio / videoOrientation.heightRatio.toFloat()) * dstWidth)
        } else {
            requireView().height - productSheetHeight -
                    closeIcon.top -
                    offset16
        }
        playFragment.onBottomInsetsViewShown(dstHeight.toInt())
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
            else -> {}
        }
    }

    private fun doOpenProductDetail(product: PlayProductUiModel.Product, configUiModel: ProductSectionUiModel.Section, position: Int) {
        if (product.applink != null && product.applink.isNotEmpty()) {
            if (configUiModel.config.type == ProductSectionType.TokoNow) {
                newAnalytic.clickProductBottomSheetNow(product, configUiModel, position)
            } else {
                analytic.clickProduct(product, configUiModel, position)
            }
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
            router.route(context, applink, *params)
        } else {
            val intent = router.getIntent(context, applink, *params)
            startActivityForResult(intent, requestCode)
        }
        activity?.overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) activity?.finish()
    }

    private fun copyToClipboard(content: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("play-room-bottom-sheet", content))
    }

    private fun dismissSheets() {
        playFragment.hideKeyboard()
        playViewModel.hideInsets(isKeyboardHandled = true)
    }

    /**
     * Observe
     */
    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(
            viewLifecycleOwner,
            DistinctObserver {
                val productSheetState = it[BottomInsetsType.ProductSheet]
                val leaderboardSheetState = it[BottomInsetsType.LeaderboardSheet]
                val couponSheetState = it[BottomInsetsType.CouponSheet]

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
                } else if (couponSheetState != null && !couponSheetState.isPreviousStateSame) {
                    when (couponSheetState) {
                        is BottomInsetsState.Hidden -> if (!it.isAnyShown) playFragment.onBottomInsetsViewHidden()
                        is BottomInsetsState.Shown ->
                            if (!it.isProductSheetsShown) pushParentPlayBySheetHeight(couponSheetState.estimatedInsetsHeight)
                    }
                }

                it[BottomInsetsType.ProductSheet]?.let { state ->
                    if (state is BottomInsetsState.Shown) {
                        productSheetView.showWithHeight(state.estimatedInsetsHeight)
                    } else {
                        productSheetView.hide()
                    }
                }
                it[BottomInsetsType.CouponSheet]?.let { state ->
                    if (state is BottomInsetsState.Shown) {
                        couponSheetView.showWithHeight(state.estimatedInsetsHeight)
                    } else {
                        couponSheetView.hide()
                    }
                }

                it[BottomInsetsType.LeaderboardSheet]?.let { state ->
                    if (state is BottomInsetsState.Shown) {
                        leaderboardSheetView.showWithHeight(state.estimatedInsetsHeight)
                        playViewModel.submitAction(RefreshLeaderboard)
                    } else {
                        leaderboardSheetView.hide()
                    }
                }
            }
        )
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.uiState.withCache().collectLatest { state ->
                when (state.value.winnerBadge.leaderboards.state) {
                    ResultState.Success ->
                        leaderboardSheetView.setData(
                            state.value.winnerBadge.leaderboards.data
                        )
                    is ResultState.Fail ->
                        leaderboardSheetView.setError()
                    ResultState.Loading ->
                        leaderboardSheetView.setLoading()
                }

                if (state.value.status.channelStatus.statusType.isFreeze ||
                    state.value.status.channelStatus.statusType.isBanned
                ) {
                    viewModel.onFreezeBan()
                    hideLoadingView()
                }

                renderProductSheet(state)
                renderVoucherSheet(state.value.tagItems)
                renderFullLoading(state)
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            playViewModel.uiEvent.collect { event ->
                when (event) {
                    is BuySuccessEvent -> {
                        router.route(requireContext(), ApplinkConstInternalMarketplace.CART)

                        if (event.isProductFeatured) return@collect

                        val sectionInfo = event.sectionInfo ?: ProductSectionUiModel.Section.Empty

                        if (sectionInfo.config.type == ProductSectionType.TokoNow) {
                            newAnalytic.clickBeliNowProduct(
                                product = event.product,
                                cartId = event.cartId,
                                shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                sectionInfo = sectionInfo
                            )
                        } else if (playViewModel.bottomInsets.isProductSheetsShown) {
                            analytic.clickProductAction(
                                product = event.product,
                                cartId = event.cartId,
                                productAction = ProductAction.Buy,
                                shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                sectionInfo = sectionInfo
                            )
                        }
                    }
                    is ShowInfoEvent -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getTextFromUiString(event.message)
                        )
                    }
                    is ShowErrorEvent -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_ERROR,
                            message = ErrorHandler.getErrorMessage(requireContext(), event.error)
                        )
                    }
                    is AtcSuccessEvent -> {
                        val partnerTokoNow = playViewModel.latestCompleteChannelData.partnerInfo.type == PartnerType.TokoNow
                        val (wording, route, toaster) = if (event.product.isTokoNow && partnerTokoNow) {
                            newAnalytic.impressNowToaster()
                            Triple(
                                getString(R.string.play_add_to_cart_message_success_mixed),
                                ApplinkConst.TokopediaNow.HOME + getString(R.string.play_tokonow_minicart_applink),
                                getString(R.string.play_toaster_tokonow_wording)
                            )
                        } else if (event.product.isTokoNow && !partnerTokoNow) {
                            Triple(getString(R.string.play_add_to_cart_message_success_tokonow), ApplinkConstInternalMarketplace.CART, getString(R.string.play_toaster_tokonow_wording))
                        } else {
                            newAnalytic.impressGlobalToaster()
                            Triple(
                                getString(R.string.play_add_to_cart_message_success_tokonow),
                                ApplinkConstInternalMarketplace.CART,
                                getString(R.string.play_toaster_tokonow_wording)
                            )
                        }

                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = wording,
                            actionText = toaster,
                            actionClickListener = {
                                router.route(requireContext(), route)
                                if (event.product.isPinned &&
                                    !playViewModel.bottomInsets.isProductSheetsShown
                                ) {
                                    newAnalytic.clickLihatToasterAtcPinnedProductCarousel(
                                        channelId = playViewModel.channelId,
                                        channelType = playViewModel.channelType
                                    )
                                }

                                if (event.product.isTokoNow && partnerTokoNow) {
                                    newAnalytic.clickLihatNowToaster()
                                    analytic.clickSeeToasterAfterAtc()
                                } else {
                                    analytic.clickSeeToasterAfterAtc()
                                }
                            }
                        )

                        if (event.isProductFeatured) return@collect

                        val sectionInfo = event.sectionInfo ?: ProductSectionUiModel.Section.Empty

                        if (sectionInfo.config.type == ProductSectionType.TokoNow) {
                            newAnalytic.clickAtcNowProduct(
                                product = event.product,
                                cartId = event.cartId,
                                shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                sectionInfo = sectionInfo
                            )
                        } else if (playViewModel.bottomInsets.isProductSheetsShown) {
                            analytic.clickProductAction(
                                product = event.product,
                                cartId = event.cartId,
                                productAction = ProductAction.AddToCart,
                                shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                                sectionInfo = sectionInfo
                            )
                        }
                    }
                    is ChangeCampaignReminderSuccess -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = event.message.ifBlank {
                                getString(R.string.play_product_upcoming_reminder_success)
                            }
                        )
                    }
                    is ChangeCampaignReminderFailed -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_ERROR,
                            message = event.error.localizedMessage.ifBlank {
                                getString(R.string.play_product_upcoming_reminder_error)
                            }
                        )
                    }
                    is OCCSuccessEvent -> {
                        router.route(requireContext(), ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)

                        if (event.isProductFeatured) return@collect

                        analytic.clickProductAction(
                            product = event.product,
                            cartId = event.cartId,
                            productAction = ProductAction.OCC,
                            shopInfo = playViewModel.latestCompleteChannelData.partnerInfo,
                            sectionInfo = event.sectionInfo ?: ProductSectionUiModel.Section.Empty
                        )
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
        newAnalytic.clickUpcomingReminder(productSectionUiModel, playViewModel.channelId, playViewModel.channelType)
        playViewModel.submitAction(SendUpcomingReminder(productSectionUiModel))
    }

    override fun onInformationClicked(
        view: ProductSheetViewComponent
    ) {
        newAnalytic.clickInfoNow()
        val appLink = "${ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO}?source=play&channel_id=${playViewModel.channelId}&state=${playViewModel.channelType}"
        val intent = router.getIntent(context, appLink)
        startActivity(intent)
    }

    override fun onInformationImpressed(view: ProductSheetViewComponent) {
        newAnalytic.impressInfoNow()
    }

    /**
     * Render View
     */
    private fun renderProductSheet(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged(
                { it.tagItems },
                { it.channel.showCart },
                { it.combinedState.cartCount },
                { it.channel.emptyBottomSheetInfo }
            )
        ) return

        productSheetView.showCart(state.value.channel.showCart)
        productSheetView.setCartCount(state.value.combinedState.cartCount)

        val prevTagItems = state.prevValue?.tagItems
        val tagItems = state.value.tagItems

        if (tagItems.resultState.isLoading && tagItems.product.productSectionList.isEmpty()) {
            productSheetView.showPlaceholder()
        } else if (tagItems.resultState is ResultState.Fail) {
            productSheetView.showError(
                isConnectionError = tagItems.resultState.error is ConnectException ||
                    tagItems.resultState.error is UnknownHostException,
                onError = { playViewModel.submitAction(RetryGetTagItemsAction) }
            )
        } else if (tagItems.product.productSectionList.isNotEmpty()) {
            productSheetView.setProductSheet(
                sectionList = tagItems.product.productSectionList,
                voucherList = tagItems.voucher.voucherList,
                title = tagItems.bottomSheetTitle,
            )
        } else {
            productSheetView.showEmpty(state.value.channel.emptyBottomSheetInfo)
        }

        if (prevTagItems?.product?.productSectionList == tagItems.product.productSectionList) return

        val prevSum = prevTagItems?.product?.productSectionList?.productsSum().orZero()
        val sum = tagItems.product.productSectionList.productsSum()

        if (prevSum != sum &&
            prevTagItems?.resultState?.isLoading != true &&
            sum != 0
        ) {
            onProductCountChanged()
        }
    }

    private fun renderVoucherSheet(tagItem: TagItemUiModel) {
        if (tagItem.voucher.voucherList.isNotEmpty()) {
            couponSheetView.setVoucherList(
                voucherList = tagItem.voucher.voucherList
            )
        }
    }

    private fun renderFullLoading(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged { it.combinedState.isLoadingBuy }) return

        if (state.value.combinedState.isLoadingBuy) {
            showLoadingView()
        } else {
            hideLoadingView()
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

    private fun List<ProductSectionUiModel>.productsSum(): Int {
        return sumOf {
            if (it is ProductSectionUiModel.Section) {
                it.productList.size
            } else {
                0
            }
        }
    }
}
