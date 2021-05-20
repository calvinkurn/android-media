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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.ProductAnalyticHelper
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.extensions.isProductSheetsShown
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPinnedUiModel
import com.tokopedia.play.view.uimodel.recom.PlayProductTagsUiModel
import com.tokopedia.play.view.viewcomponent.ProductSheetViewComponent
import com.tokopedia.play.view.viewcomponent.VariantSheetViewComponent
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import java.net.ConnectException
import java.net.UnknownHostException
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
        VariantSheetViewComponent.Listener {

    companion object {
        private const val REQUEST_CODE_LOGIN = 191

        private const val PERCENT_VARIANT_SHEET_HEIGHT = 0.6
    }

    private val productSheetView by viewComponent { ProductSheetViewComponent(it, this) }
    private val variantSheetView by viewComponent { VariantSheetViewComponent(it, this) }

    private val offset16 by lazy { resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayBottomSheetViewModel

    private val variantSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_VARIANT_SHEET_HEIGHT).toInt()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    private val generalErrorMessage: String
        get() = getString(R.string.play_general_err_message)

    private lateinit var loadingDialog: PlayLoadingDialogFragment

    private lateinit var productAnalyticHelper: ProductAnalyticHelper

    override fun getScreenName(): String = "Play Bottom Sheet"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
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

    override fun onVoucherScrolled(view: ProductSheetViewComponent, lastPositionViewed: Int) {
        analytic.scrollMerchantVoucher(lastPositionViewed)
    }

    override fun onCopyVoucherCodeClicked(view: ProductSheetViewComponent, voucher: MerchantVoucherUiModel) {
        copyToClipboard(content = voucher.code)
        doShowToaster(
                bottomSheetType = BottomInsetsType.ProductSheet,
                toasterType = Toaster.TYPE_NORMAL,
                message = getString(R.string.play_voucher_code_copied),
                actionText = getString(R.string.play_action_ok),
        )
        analytic.clickCopyVoucher(voucher)
    }

    override fun onProductsImpressed(view: ProductSheetViewComponent, products: List<Pair<PlayProductUiModel.Product, Int>>) {
        trackImpressedProduct(products)
    }

    override fun onVouchersImpressed(view: ProductSheetViewComponent, vouchers: List<MerchantVoucherUiModel>) {
        trackImpressedVoucher(vouchers)
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
     * Private methods
     */
    private fun setupView(view: View) {
        productSheetView.hide()
        variantSheetView.hide()
    }

    private fun setupObserve() {
        observeLoggedInInteractionEvent()
        observeProductSheetContent()
        observePinned()
        observeVariantSheetContent()
        observeBottomInsetsState()
        observeBuyEvent()
        observeStatusInfo()
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

    private fun doShowToaster(
            bottomSheetType: BottomInsetsType,
            toasterType: Int,
            message: String,
            actionText: String = "",
            actionClickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        when (bottomSheetType) {
            BottomInsetsType.ProductSheet ->
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
            is InteractionEvent.DoActionProduct -> doActionProduct(event.product, event.action, event.type)
            is InteractionEvent.OpenProductDetail -> doOpenProductDetail(event.product, event.position)
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
    private fun observePinned() {
        playViewModel.observablePinned.observe(viewLifecycleOwner, Observer {
            if (it is PlayPinnedUiModel.PinnedProduct && it.productTags is PlayProductTagsUiModel.Complete) {
                if (it.productTags.productList.isNotEmpty()) {
                    productSheetView.setProductSheet(it.productTags)

                    trackImpressedProduct()
                    trackImpressedVoucher()
                } else {
                    productSheetView.showEmpty(it.productTags.basicInfo.partnerId)
                }
            }
        })
    }

    private fun observeProductSheetContent() {
        playViewModel.observableProductSheetContent.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Loading -> if (it.showPlaceholder) productSheetView.showPlaceholder()
                is PlayResult.Failure -> productSheetView.showError(
                        isConnectionError = it.error is ConnectException || it.error is UnknownHostException,
                        onError = it.onRetry
                )
            }
        })
    }

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

            if (productSheetState != null && !productSheetState.isPreviousStateSame) {
                when (productSheetState) {
                    is BottomInsetsState.Hidden -> if (!it.isAnyShown) playFragment.onBottomInsetsViewHidden()
                    is BottomInsetsState.Shown -> pushParentPlayBySheetHeight(productSheetState.estimatedInsetsHeight)
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
        })
    }

    private fun observeStatusInfo() {
        playViewModel.observableStatusInfo.observe(viewLifecycleOwner, DistinctObserver {
            if (it.statusType.isFreeze || it.statusType.isBanned) {
                viewModel.onFreezeBan()
                hideLoadingView()
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
                        playViewModel.updateBadgeCart()
                        when (data.action) {
                            ProductAction.Buy -> RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
                            ProductAction.AddToCart -> doShowToaster(
                                    bottomSheetType = data.bottomInsetsType,
                                    toasterType = Toaster.TYPE_NORMAL,
                                    message = getString(R.string.play_add_to_cart_message_success),
                                    actionText = getString(R.string.play_action_view),
                                    actionClickListener = View.OnClickListener {
                                        RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
                                        analytic.clickSeeToasterAfterAtc()
                                    }
                            )
                        }
                        if (data.bottomInsetsType == BottomInsetsType.VariantSheet) {
                            closeVariantSheet()
                        }
                        analytic.clickProductAction(data.product, data.cartId, data.action, data.bottomInsetsType)
                    }
                    else {
                        doShowToaster(
                                bottomSheetType = data.bottomInsetsType,
                                toasterType = Toaster.TYPE_ERROR,
                                message = if (data.errorMessage.isNotEmpty() && data.errorMessage.isNotBlank()) data.errorMessage else generalErrorMessage
                        )
                    }
                }
            }
        })
    }

    private fun trackImpressedProduct(products: List<Pair<PlayProductUiModel.Product, Int>> = productSheetView.getVisibleProducts()) {
        if (playViewModel.bottomInsets.isProductSheetsShown) productAnalyticHelper.trackImpressedProducts(products)
    }

    private fun trackImpressedVoucher(vouchers: List<MerchantVoucherUiModel> = productSheetView.getVisibleVouchers()) {
        if (playViewModel.bottomInsets.isProductSheetsShown) productAnalyticHelper.trackImpressedVouchers(vouchers)
    }

}