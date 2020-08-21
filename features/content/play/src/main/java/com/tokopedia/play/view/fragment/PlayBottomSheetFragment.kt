package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import com.tokopedia.play.view.viewcomponent.ProductSheetViewComponent
import com.tokopedia.play.view.viewcomponent.VariantSheetViewComponent
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by jegul on 06/03/20
 */
class PlayBottomSheetFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        private val trackingQueue: TrackingQueue
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

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    private lateinit var loadingDialog: PlayLoadingDialogFragment

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return ::loadingDialog.isInitialized && loadingDialog.isVisible
    }

    override fun onInterceptSystemUiVisibilityChanged(): Boolean {
        return false
    }

    /**
     * ProductSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: ProductSheetViewComponent) {
        closeProductSheet()
    }

    override fun onBuyButtonClicked(view: ProductSheetViewComponent, product: ProductLineUiModel) {
        shouldCheckProductVariant(product, ProductAction.Buy)
    }

    override fun onAtcButtonClicked(view: ProductSheetViewComponent, product: ProductLineUiModel) {
        shouldCheckProductVariant(product, ProductAction.AddToCart)
    }

    override fun onProductCardClicked(view: ProductSheetViewComponent, product: ProductLineUiModel, position: Int) {
        shouldOpenProductDetail(product, position)
    }

    override fun onEmptyButtonClicked(view: ProductSheetViewComponent, partnerId: Long) {
        openShopPage(partnerId)
    }

    override fun onVoucherScrolled(view: ProductSheetViewComponent, lastPositionViewed: Int) {
        PlayAnalytics.scrollMerchantVoucher(channelId, lastPositionViewed)
    }

    /**
     * VariantSheet View Component Listener
     */
    override fun onCloseButtonClicked(view: VariantSheetViewComponent) {
        closeVariantSheet()
    }

    override fun onAddToCartClicked(view: VariantSheetViewComponent, productModel: ProductLineUiModel) {
        shouldDoActionProduct(productModel, ProductAction.AddToCart, BottomInsetsType.VariantSheet)
    }

    override fun onBuyClicked(view: VariantSheetViewComponent, productModel: ProductLineUiModel) {
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
        observeVariantSheetContent()
        observeBottomInsetsState()
        observeBuyEvent()
        observeEventUserInfo()
    }

    private fun openShopPage(partnerId: Long) {
        openPageByApplink(ApplinkConst.SHOP, partnerId.toString())
    }

    private fun closeProductSheet() {
        playViewModel.onHideProductSheet()
    }

    private fun shouldCheckProductVariant(product: ProductLineUiModel, action: ProductAction) {
        if (product.isVariantAvailable) {
            openVariantSheet(product, action)
            PlayAnalytics.clickActionProductWithVariant(channelId, product.id, playViewModel.channelType, action)
        } else {
            shouldDoActionProduct(product, action, BottomInsetsType.ProductSheet)
        }
    }

    private fun openVariantSheet(product: ProductLineUiModel, action: ProductAction) {
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

    private fun shouldDoActionProduct(product: ProductLineUiModel, action: ProductAction, type: BottomInsetsType) {
        viewModel.doInteractionEvent(InteractionEvent.DoActionProduct(product, action, type))
    }

    private fun shouldOpenProductDetail(product: ProductLineUiModel, position: Int) {
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
                Toaster.make(
                        view = requireView(),
                        text = message,
                        type = toasterType,
                        actionText = actionText,
                        clickListener = actionClickListener
                )
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

    private fun doOpenProductDetail(product: ProductLineUiModel, position: Int) {
        if (product.applink != null && product.applink.isNotEmpty()) {
            PlayAnalytics.clickProduct(trackingQueue, channelId, product, position, playViewModel.channelType)
            openPageByApplink(product.applink)
        }
    }

    private fun doActionProduct(product: ProductLineUiModel, productAction: ProductAction, type: BottomInsetsType) {
        viewModel.addToCart(product, action = productAction, type = type)
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = REQUEST_CODE_LOGIN)
    }

    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
        if (requestCode == null) {
            RouteManager.route(context, applink, *params)
        } else {
            val intent = RouteManager.getIntent(context, applink, *params)
            startActivityForResult(intent, requestCode)
        }
        activity?.overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) activity?.finish()
    }

    private fun sendTrackerImpression(playResult: PlayResult<ProductSheetUiModel>) {
        if (playResult is PlayResult.Success) {
            if (playResult.data.productList.isNotEmpty()
                    && playResult.data.productList.first() is ProductLineUiModel) {
                with(PlayAnalytics) { impressionProductList(
                        trackingQueue,
                        channelId,
                        playResult.data.productList as List<ProductLineUiModel>,
                        playViewModel.channelType
                ) }
            }
        }
    }

    /**
     * Observe
     */
    private fun observeProductSheetContent() {
        playViewModel.observableProductSheetContent.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Loading -> if (it.showPlaceholder) productSheetView.showPlaceholder()
                is PlayResult.Success -> {
                    if (it.data.productList.isNotEmpty()) {
                        productSheetView.setProductSheet(it.data)
                    } else {
                        productSheetView.showEmpty(it.data.partnerId)
                    }
                }
                is PlayResult.Failure -> productSheetView.showError(
                        isConnectionError = it.error is ConnectException || it.error is UnknownHostException,
                        onError = it.onRetry
                )
            }

            sendTrackerImpression(it)
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

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, DistinctObserver {
            if (it.isFreeze || it.isBanned) {
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
                                        PlayAnalytics.clickSeeToasterAfterAtc(channelId, playViewModel.channelType)
                                    }
                            )
                        }
                        if (data.bottomInsetsType == BottomInsetsType.VariantSheet) {
                            closeVariantSheet()
                        }
                        PlayAnalytics.clickProductAction(trackingQueue, channelId, data.product, data.cartId, playViewModel.channelType, data.action, data.bottomInsetsType)
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
}