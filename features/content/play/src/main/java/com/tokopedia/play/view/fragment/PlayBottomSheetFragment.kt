package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.productsheet.ProductSheetComponent
import com.tokopedia.play.ui.productsheet.interaction.ProductSheetInteractionEvent
import com.tokopedia.play.ui.variantsheet.VariantSheetComponent
import com.tokopedia.play.ui.variantsheet.interaction.VariantSheetInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.ProductSheetUiModel
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 06/03/20
 */
class PlayBottomSheetFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {
        private const val REQUEST_CODE_LOGIN = 191

        private const val PERCENT_VARIANT_SHEET_HEIGHT = 0.6

        fun newInstance(channelId: String?): PlayBottomSheetFragment {
            return PlayBottomSheetFragment().apply {
                val args = Bundle()
                args.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = args
            }
        }
    }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + dispatchers.main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    @Inject
    lateinit var trackingQueue: TrackingQueue

    private val offset16 by lazy { resources.getDimensionPixelOffset(R.dimen.spacing_lvl4) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayBottomSheetViewModel

    private lateinit var productSheetComponent: UIComponent<*>
    private lateinit var variantSheetComponent: UIComponent<*>

    private var channelId: String = ""

    private val variantSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_VARIANT_SHEET_HEIGHT).toInt()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    private lateinit var loadingDialog: PlayLoadingDialogFragment

    override fun getScreenName(): String = "Play Bottom Sheet"

    override fun initInjector() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .playModule(PlayModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayBottomSheetViewModel::class.java)
        channelId  = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_bottom_sheet, container, false)
        initComponents(view.findViewById(R.id.coordl_bottom_sheet) as ViewGroup)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeLoggedInInteractionEvent()
        observeProductSheetContent()
        observeVariantSheetContent()
        observeBottomInsetsState()
        observeBuyEvent()
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

    private fun observeProductSheetContent() {
        playViewModel.observableProductSheetContent.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetProductSheet(it)
                        )
            }
            sendTrackerImpression(it)
        })
    }

    private fun sendTrackerImpression(playResult: PlayResult<ProductSheetUiModel>) {
        if (playResult is PlayResult.Success) {
            if (playResult.data.productList.isNotEmpty()
                    && playResult.data.productList[0] is ProductLineUiModel) {
                with(PlayAnalytics) { impressionProductList(
                        trackingQueue,
                        channelId,
                        playResult.data.productList as List<ProductLineUiModel>,
                        playViewModel.channelType
                ) }
            }
        }

    }

    private fun observeVariantSheetContent() {
        viewModel.observableProductVariant.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVariantSheet(it)
                        )
            }
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(it, it.isAnyShown, it.isAnyHidden))

                val productSheetState = it[BottomInsetsType.ProductSheet]

                if (productSheetState != null && !productSheetState.isPreviousStateSame) {
                    when (productSheetState) {
                        is BottomInsetsState.Hidden -> playFragment.onBottomInsetsViewHidden()
                        is BottomInsetsState.Shown -> pushParentPlayBySheetHeight(productSheetState.estimatedInsetsHeight)
                    }
                }
            }
        })
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeBuyEvent() {
        viewModel.observableAddToCart.observe(viewLifecycleOwner, Observer {
            hideLoadingView()
            if (it.isSuccess) {
                playViewModel.updateBadgeCart()
                if (it.action == ProductAction.BuyInVariant && it.action == ProductAction.Buy) {
                    RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
                }
                if (it.action == ProductAction.AddToCart && it.action == ProductAction.AddToCartInVariant) {
                    Toaster.make(requireView(),
                            getString(R.string.play_add_to_cart_message_success),
                            Snackbar.LENGTH_LONG,
                            actionText = getString(R.string.play_add_to_cart_action_success),
                            clickListener = View.OnClickListener {
                                RouteManager.route(requireContext(), ApplinkConstInternalMarketplace.CART)
                                PlayAnalytics.clickSeeToasterAfterAtc(channelId, playViewModel.channelType)
                            })
                }
                if (it.action == ProductAction.BuyInVariant && it.action == ProductAction.AddToCartInVariant) {
                    closeVariantSheet()
                }
                PlayAnalytics.clickProductAction(trackingQueue, channelId, it.product, it.cartId, playViewModel.channelType, it.action)
            }
            else Toaster.make(requireView(), it.errorMessage, Snackbar.LENGTH_LONG, type = Toaster.TYPE_ERROR)
        })
    }
    private fun setupView(view: View) {}

    private fun initComponents(container: ViewGroup) {
        productSheetComponent = initProductSheetComponent(container)
        variantSheetComponent = initVariantSheetComponent(container)

        sendInitState()
    }

    private fun sendInitState() {
        launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init
            )
        }
    }

    private fun initProductSheetComponent(container: ViewGroup): UIComponent<ProductSheetInteractionEvent> {
        val productSheetComponent = ProductSheetComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            productSheetComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            ProductSheetInteractionEvent.OnCloseProductSheet -> closeProductSheet()
                            is ProductSheetInteractionEvent.OnBuyProduct -> checkProductVariant(it.product, ProductAction.Buy)
                            is ProductSheetInteractionEvent.OnAtcProduct -> checkProductVariant(it.product, ProductAction.AddToCart)
                            is ProductSheetInteractionEvent.OnProductCardClicked -> onProductCardClicked(it.product)
                            is ProductSheetInteractionEvent.OnVoucherScrolled -> onVoucherScrolled(it.lastPositionViewed)
                        }
                    }
        }

        return productSheetComponent
    }

    private fun onVoucherScrolled(lastPositionViewed: Int) {
        PlayAnalytics.scrollMerchantVoucher(channelId, lastPositionViewed)
    }

    private fun onProductCardClicked(product: ProductLineUiModel) {
        if (product.applink != null && product.applink.isNotEmpty()) {
            PlayAnalytics.clickProduct(trackingQueue, channelId, product, playViewModel.channelType)
            openPageByApplink(product.applink)
        }
    }

    private fun initVariantSheetComponent(container: ViewGroup): UIComponent<VariantSheetInteractionEvent> {
        val variantSheetComponent = VariantSheetComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            variantSheetComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            VariantSheetInteractionEvent.OnCloseVariantSheet -> closeVariantSheet()
                            is VariantSheetInteractionEvent.OnBuyProduct -> doActionProduct(it.product, ProductAction.BuyInVariant)
                            is VariantSheetInteractionEvent.OnAddProductToCart -> doActionProduct(it.product, ProductAction.AddToCartInVariant)
                            is VariantSheetInteractionEvent.OnClickVariantGuideline -> {
                                startActivity(ImagePreviewActivity.getCallingIntent(requireContext(), arrayListOf(it.url)))
                            }
                        }
                    }
        }

        return variantSheetComponent
    }

    private fun closeProductSheet() {
        playViewModel.onHideProductSheet()
    }

    private fun checkProductVariant(product: ProductLineUiModel, action: ProductAction) {
        if (product.isVariantAvailable) {
            openVariantSheet(product, action)
            PlayAnalytics.clickActionProductWithVariant(channelId, product.id, playViewModel.channelType, action)
        } else {
            shouldDoActionProduct(product, action)
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
        loadingDialog.dismiss()
    }

    private fun shouldDoActionProduct(product: ProductLineUiModel, action: ProductAction) {
        viewModel.doInteractionEvent(InteractionEvent.DoActionProduct(product, action))
    }

    private fun pushParentPlayBySheetHeight(productSheetHeight: Int) {
        val requiredMargin = offset16
        playFragment.onBottomInsetsViewShown(getScreenHeight() - (productSheetHeight + requiredMargin))
    }

    private fun handleLoginInteractionEvent(loginInteractionEvent: LoginStateEvent) {
        when (loginInteractionEvent) {
            is LoginStateEvent.InteractionAllowed -> handleInteractionEvent(loginInteractionEvent.event)
            is LoginStateEvent.NeedLoggedIn -> openLoginPage()
        }
    }

    private fun handleInteractionEvent(event: InteractionEvent) {
        if(event is InteractionEvent.DoActionProduct) doActionProduct(event.product, event.action)
    }

    private fun doActionProduct(product: ProductLineUiModel, productAction: ProductAction) {
        showLoadingView()
        viewModel.addToCart(product, action = productAction)
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
}