package com.tokopedia.promousage.view.bottomsheet

import android.content.Intent
import android.graphics.Color
import android.graphics.Outline
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.media.loader.loadImage
import com.tokopedia.promousage.R
import com.tokopedia.promousage.analytics.PromoUsageAnalytics
import com.tokopedia.promousage.databinding.PromoUsageBottomsheetBinding
import com.tokopedia.promousage.di.DaggerPromoUsageComponent
import com.tokopedia.promousage.di.PromoUsageModule
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.extension.getErrorMessage
import com.tokopedia.promousage.util.extension.toSpannableHtmlString
import com.tokopedia.promousage.util.view.BottomSheetUtil
import com.tokopedia.promousage.view.adapter.PromoAccordionHeaderDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionItemDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionViewAllDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAttemptCodeDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoRecommendationDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoTncDelegateAdapter
import com.tokopedia.promousage.view.viewmodel.ApplyPromoUiAction
import com.tokopedia.promousage.view.viewmodel.AttemptPromoUiAction
import com.tokopedia.promousage.view.viewmodel.ClearPromoUiAction
import com.tokopedia.promousage.view.viewmodel.ClickPromoUiAction
import com.tokopedia.promousage.view.viewmodel.ClickTncUiAction
import com.tokopedia.promousage.view.viewmodel.ClosePromoPageUiAction
import com.tokopedia.promousage.view.viewmodel.GetPromoRecommendationUiAction
import com.tokopedia.promousage.view.viewmodel.PromoCtaUiAction
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.promousage.view.viewmodel.UsePromoRecommendationUiAction
import com.tokopedia.promousage.view.viewmodel.getAttemptItem
import com.tokopedia.promousage.view.viewmodel.getAttemptedPromos
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.revamp.CartCheckoutRevampRollenceManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.max
import com.tokopedia.promousage.R as promousageR
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PromoUsageBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val BUNDLE_KEY_ENTRY_POINT = "entry_point"
        private const val BUNDLE_KEY_PROMO_REQUEST = "promo_request"
        private const val BUNDLE_KEY_TOTAL_AMOUNT = "total_amount"
        private const val BUNDLE_KEY_CHOSEN_ADDRESS = "chosen_address"
        private const val BUNDLE_KEY_VALIDATE_USE = "validate_use"
        private const val BUNDLE_KEY_BO_PROMO_CODES = "bo_promo_codes"
        private const val BUNDLE_KEY_IS_FLOW_MVC_LOCK_TO_COURIER = "is_flow_mvc_lock_to_courier"

        private const val BOTTOM_SHEET_MARGIN_TOP_IN_DP = 64
        private const val BOTTOM_SHEET_TICKER_INFO_HEIGHT_IN_DP = 40
        private const val BOTTOM_SHEET_TICKER_INFO_MARGIN_IN_DP = 12
        private const val BOTTOM_SHEET_HEADER_HEIGHT_IN_DP = 56
        private const val BOTTOM_SHEET_SAVING_INFO_HEIGHT_IN_DP = 56
        private const val BOTTOM_SHEET_TOTAL_AMOUNT_HEIGHT_IN_DP = 56

        private const val AUTO_SCROLL_DELAY = 500L

        @JvmStatic
        fun newInstance(
            entryPoint: PromoPageEntryPoint,
            promoRequest: PromoRequest,
            validateUsePromoRequest: ValidateUsePromoRequest,
            totalAmount: Double,
            listener: Listener? = null,
            boPromoCodes: List<String> = emptyList(),
            chosenAddress: ChosenAddress? = null,
            isFlowMvcLockToCourier: Boolean = false
        ): PromoUsageBottomSheet {
            return PromoUsageBottomSheet().apply {
                this.arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_ENTRY_POINT, entryPoint)
                    putParcelable(BUNDLE_KEY_PROMO_REQUEST, promoRequest)
                    putParcelable(BUNDLE_KEY_VALIDATE_USE, validateUsePromoRequest)
                    putStringArrayList(BUNDLE_KEY_BO_PROMO_CODES, ArrayList(boPromoCodes))
                    putParcelable(BUNDLE_KEY_CHOSEN_ADDRESS, chosenAddress)
                    putDouble(BUNDLE_KEY_TOTAL_AMOUNT, totalAmount)
                    putBoolean(BUNDLE_KEY_IS_FLOW_MVC_LOCK_TO_COURIER, isFlowMvcLockToCourier)
                }
                this.listener = listener
            }
        }
    }

    private val entryPoint: PromoPageEntryPoint
        get() = arguments?.getParcelable(BUNDLE_KEY_ENTRY_POINT)
            ?: PromoPageEntryPoint.CART_PAGE
    private val isFlowMvcLockToCourier: Boolean
        get() = arguments?.getBoolean(BUNDLE_KEY_IS_FLOW_MVC_LOCK_TO_COURIER) ?: false

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var promoUsageAnalytics: PromoUsageAnalytics

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    private val viewModel: PromoUsageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PromoUsageViewModel::class.java]
    }

    private var binding by autoClearedNullable<PromoUsageBottomsheetBinding>()
    var listener: Listener? = null
    private val maxBottomSheetHeight: Int by lazy {
        getScreenHeight() - BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx()
    }

    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(
                PromoRecommendationDelegateAdapter(
                    onClickUsePromoRecommendation,
                    onClickRecommendationPromo,
                    onImpressionPromo,
                    onRecommendationAnimationEnd,
                    onClickClose
                )
            )
            .add(PromoAccordionHeaderDelegateAdapter(onClickPromoAccordionHeader))
            .add(PromoAccordionItemDelegateAdapter(onClickPromoItem, onImpressionPromo))
            .add(PromoAccordionViewAllDelegateAdapter(onClickPromoAccordionViewAll))
            .add(PromoTncDelegateAdapter(onClickPromoTnc))
            .add(PromoAttemptCodeDelegateAdapter(onAttemptPromoCode))
            .build()
    }
    private val layoutManager = LinearLayoutManager(context)
    private val registerGopayLaterCicilLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val promoRequest =
                arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST) ?: PromoRequest()
            val chosenAddress =
                arguments?.getParcelable<ChosenAddress>(BUNDLE_KEY_CHOSEN_ADDRESS)
            viewModel.loadPromoListWithPreSelectedGopayLaterPromo(
                promoRequest = promoRequest,
                chosenAddress = chosenAddress,
                autoApplyImpressionTrackerEnable = getEnableSendAutoApplyTracker()
            )
        }
    private var loaderDialog: LoaderDialog? = null

    private fun setupDependencyInjection() {
        activity?.let {
            val application = it.application
            if (application is BaseMainApplication) {
                DaggerPromoUsageComponent.builder()
                    .baseAppComponent(application.baseAppComponent)
                    .promoUsageModule(PromoUsageModule())
                    .build()
                    .inject(this)
            }
        }
    }

    fun show(manager: FragmentManager) {
        super.show(manager, PromoUsageBottomSheet::class.java.simpleName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setStyle(
            DialogFragment.STYLE_NORMAL,
            unifycomponentsR.style.UnifyBottomSheetOverlapStyle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PromoUsageBottomsheetBinding.inflate(inflater, container, false)
        dialog?.setOnShowListener {
            applyBottomSheetMaxHeightRule()
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        renderLoadingDialog(false)
        removeObserveKeyboardVisibility()
        recyclerViewAdapter.clear()
        binding = null
        listener = null
    }

    private fun applyBottomSheetMaxHeightRule() {
        binding?.run {
            val frameDialogView = rlBottomSheetWrapper.parent as FrameLayout
            frameDialogView.setBackgroundColor(Color.TRANSPARENT)
            frameDialogView.bringToFront()
            rlBottomSheetWrapper.maxHeight = maxBottomSheetHeight
            val bottomSheetBehavior = BottomSheetBehavior.from(frameDialogView)
            bottomSheetBehavior.peekHeight = maxBottomSheetHeight
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.isDraggable = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
        val promoRequest: PromoRequest? = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.reloadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress
        )
    }

    private fun resetView() {
        setupView()
        val promoRequest: PromoRequest? = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.reloadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress
        )
    }

    private fun setupView() {
        renderContent(isVisible = false, hasPromoRecommendationSection = false)
        renderLoadingShimmer(true)

        val totalAmount = arguments?.getDouble(BUNDLE_KEY_TOTAL_AMOUNT) ?: 0.0
        val validateUsePromoRequest = arguments?.getParcelable(BUNDLE_KEY_VALIDATE_USE)
            ?: ValidateUsePromoRequest()
        val boPromoCodes: List<String> = arguments?.getStringArrayList(BUNDLE_KEY_BO_PROMO_CODES)
            ?: emptyList()

        binding?.clBottomSheetHeader?.btnBottomSheetHeaderClose
            ?.setOnClickListener {
                renderLoadingDialog(true)
                viewModel.onClosePromoPage(
                    entryPoint = entryPoint,
                    validateUsePromoRequest = validateUsePromoRequest,
                    boPromoCodes = boPromoCodes,
                    isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(
                        RemoteConfigInstance.getInstance().abTestPlatform
                    ).isRevamp()
                )
            }
        binding?.clTickerInfo?.gone()
        context?.let {
            binding?.clBottomSheetContent?.background = BottomSheetUtil
                .generateBackgroundDrawableWithColor(
                    it,
                    unifyprinciplesR.color.Unify_Background
                )
        }
        binding?.rvPromo?.layoutManager = layoutManager
        binding?.rvPromo?.adapter = recyclerViewAdapter
        when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> {
                binding?.tpgTotalAmountLabel?.visible()
                binding?.tpgTotalAmount?.text = context?.getString(
                    R.string.promo_usage_label_total_price,
                    totalAmount.splitByThousand()
                )
                binding?.tpgTotalAmount?.visible()
                binding?.cvTotalAmount?.visible()
                binding?.buttonBuy?.setOnClickListener {
                    if (binding?.buttonBuy?.isLoading == false) {
                        renderLoadingDialog(true)
                        viewModel.onClickBuy(
                            entryPoint = entryPoint,
                            validateUsePromoRequest = validateUsePromoRequest,
                            boPromoCodes = boPromoCodes,
                            isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(
                                RemoteConfigInstance.getInstance().abTestPlatform
                            ).isRevamp()
                        )
                    }
                }
                binding?.buttonBuy?.visible()
                binding?.buttonBackToShipment?.gone()
            }

            PromoPageEntryPoint.CHECKOUT_PAGE, PromoPageEntryPoint.OCC_PAGE -> {
                binding?.tpgTotalAmountLabel?.gone()
                binding?.tpgTotalAmount?.gone()
                binding?.buttonBuy?.gone()
                binding?.buttonBackToShipment?.setOnClickListener {
                    if (binding?.buttonBackToShipment?.isLoading == false) {
                        renderLoadingDialog(true)
                        viewModel.onClickBackToCheckout(
                            entryPoint = entryPoint,
                            validateUsePromoRequest = validateUsePromoRequest,
                            boPromoCodes = boPromoCodes,
                            isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(
                                RemoteConfigInstance.getInstance().abTestPlatform
                            ).isRevamp()
                        )
                    }
                }
                binding?.buttonBackToShipment?.visible()
            }
        }
        observeKeyboardVisibility()
    }

    private fun addScrollListeners() {
        binding?.rvPromo?.clearOnScrollListeners()
        // Header scroll listener
        binding?.rvPromo?.addOnScrollListener(object : OnScrollListener() {
            var scrollY = 0f
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollY += dy.toFloat()
                scrollY = max(scrollY, 0f)

                // Handle header change
                val firstCompletelyVisibleItemPosition =
                    layoutManager.findFirstCompletelyVisibleItemPosition()
                if (scrollY > 0) {
                    if (firstCompletelyVisibleItemPosition == 0) {
                        scrollY = 0f
                        renderTransparentHeader()
                    } else {
                        renderWhiteHeader()
                    }
                } else {
                    renderTransparentHeader()
                }
            }
        })
    }

    private fun clearHeaderScrollListener() {
        binding?.rvPromo?.clearOnScrollListeners()
    }

    private fun renderWhiteHeader() {
        context?.let {
            binding?.clBottomSheetHeader?.root?.background =
                BottomSheetUtil.generateBackgroundDrawableWithColor(
                    it,
                    unifyprinciplesR.color.Unify_Background
                )
        }
        binding?.clBottomSheetHeader?.tpgBottomSheetHeaderTitle
            ?.setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
        context?.let {
            binding?.clBottomSheetHeader?.btnBottomSheetHeaderClose
                ?.setImageDrawable(
                    if (binding?.root?.context.isDarkMode()) {
                        getIconUnifyDrawable(
                            context = it,
                            iconId = IconUnify.CLOSE,
                            assetColor = ContextCompat.getColor(
                                it,
                                unifyprinciplesR.color.Unify_Static_White
                            )
                        )
                    } else {
                        getIconUnifyDrawable(
                            context = it,
                            iconId = IconUnify.CLOSE,
                            assetColor = ContextCompat.getColor(
                                it,
                                unifyprinciplesR.color.Unify_Static_Black
                            )
                        )
                    }
                )
        }
        binding?.clBottomSheetHeader?.root?.visible()
    }

    private fun renderTransparentHeader() {
        binding?.clBottomSheetHeader?.root?.gone()
    }

    private fun observeKeyboardVisibility() {
        activity?.let { activity ->
            ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView) { view: View, insets: WindowInsetsCompat ->
                val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                if (isKeyboardVisible) {
                    // isKeyboardVisible means user tap voucher code text field
                    setFocusOnPromoAttemptTextField()
                } else {
                    resetFocusOnPromoAttemptTextField()
                }
                return@setOnApplyWindowInsetsListener ViewCompat.onApplyWindowInsets(view, insets)
            }
        }
    }

    private fun removeObserveKeyboardVisibility() {
        activity?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it.window.decorView, null)
        }
    }

    private fun setFocusOnPromoAttemptTextField() {
        binding?.rvPromo?.requestLayout()

        val itemCount = recyclerViewAdapter.itemCount
        binding?.rvPromo?.smoothSnapToPosition(itemCount)
    }

    private fun resetFocusOnPromoAttemptTextField() {
        binding?.rvPromo?.requestLayout()
    }

    private fun setupObservers() {
        observePromoPageState()
        observePromoRecommendationUiAction()
        observeUsePromoRecommendationUiAction()
        observePromoAttemptUiAction()
        observePromoCtaUiAction()
        observeClearPromoUiAction()
        observeApplyPromoUiAction()
        observeClosePromoPageUiAction()
        observeAutoScrollUiAction()
        observeClickTncUiAction()
        observeAutoApplyAction()
    }

    private fun observeAutoApplyAction() {
        viewModel.autoApplyAction.observe(viewLifecycleOwner) { promoItem ->
            processAndSendImpressionAutoApplyGpl(promoItem)
        }
    }

    private fun observePromoRecommendationUiAction() {
        viewModel.getPromoRecommendationUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is GetPromoRecommendationUiAction.NotEmpty -> {
                    renderPromoRecommendationBackground(uiAction.promoRecommendation)
                    addScrollListeners()
                    renderTransparentHeader()
                }

                is GetPromoRecommendationUiAction.Empty -> {
                    clearHeaderScrollListener()
                    renderWhiteHeader()
                }
            }
        }
    }

    private fun observeUsePromoRecommendationUiAction() {
        viewModel.usePromoRecommendationUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is UsePromoRecommendationUiAction.Success -> {
                    if (uiAction.isClickUseRecommendation) {
                        processAndSendClickPakaiPromoNewEvent(
                            uiAction.promoRecommendation,
                            uiAction.items
                        )
                    }
                }

                is UsePromoRecommendationUiAction.Failed -> {
                    // no-op
                }
            }
        }
    }

    private fun observePromoAttemptUiAction() {
        viewModel.attemptPromoUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is AttemptPromoUiAction.Success -> {
                    renderLoadingDialog(false)
                }

                is AttemptPromoUiAction.Failed -> {
                    renderLoadingDialog(false)
                    showToastError(uiAction.errorMessage)
                }
            }
        }
    }

    private fun observePromoPageState() {
        viewModel.promoPageUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PromoPageUiState.Initial -> {
                    refreshBottomSheetHeight(state)
                    renderHeader(false)
                    renderContent(isVisible = false, hasPromoRecommendationSection = false)
                    renderLoadingDialog(false)
                    renderLoadingShimmer(true)
                    hideSavingInfo()
                    renderError(false)
                    renderActionButton(state)
                }

                is PromoPageUiState.Success -> {
                    refreshBottomSheetHeight(state)
                    renderHeader(true)
                    submitItems(state.items)
                    renderLoadingDialog(false)
                    renderLoadingShimmer(false)
                    renderContent(
                        isVisible = true,
                        hasPromoRecommendationSection = state.hasPromoRecommendationSection
                    )
                    renderSavingInfo(state.savingInfo)
                    renderError(false)
                    renderTickerInfo(state.tickerInfo)
                    renderActionButton(state)
                    if (state.isReload) {
                        processAndSendViewAvailablePromoListNewEvent(items = state.items)
                    }
                }

                is PromoPageUiState.Error -> {
                    refreshBottomSheetHeight(state)
                    renderHeader(true)
                    renderLoadingShimmer(false)
                    renderLoadingDialog(false)
                    renderContent(isVisible = false, hasPromoRecommendationSection = false)
                    renderError(true, state.exception)
                    hideSavingInfo()
                    processAndSendViewAvailablePromoListNewEvent(isError = true)
                }

                else -> {
                    // no-op
                }
            }
        }
    }

    private fun renderPromoRecommendationBackground(promoRecommendation: PromoRecommendationItem) {
        binding?.clBottomSheetContent?.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                val cornerRadius = 16.toPx()
                val width = view?.width ?: 0
                val height = view?.height ?: 0
                outline?.setRoundRect(
                    0,
                    0,
                    width,
                    height.plus(cornerRadius),
                    cornerRadius.toFloat()
                )
            }
        }
        binding?.clBottomSheetContent?.clipToOutline = true
        binding?.clBottomSheetContent?.clipChildren = true
    }

    private fun renderTickerInfo(tickerInfo: PromoPageTickerInfo) {
        val hasTickerInfo = tickerInfo.message.isNotBlank() && tickerInfo.iconUrl.isNotBlank() &&
            tickerInfo.backgroundUrl.isNotBlank()
        if (hasTickerInfo) {
            binding?.ivTickerInfoBackground?.loadImage(
                url = tickerInfo.backgroundUrl,
                properties = {
                    centerCrop()
                }
            )
            binding?.iuTickerInfoIcon?.loadImage(tickerInfo.iconUrl)
            binding?.tpgTickerInfoMessage?.text = tickerInfo.message
            binding?.clTickerInfo?.visible()
        } else {
            val layoutParams =
                binding?.clBottomSheetContent?.layoutParams as? ConstraintLayout.LayoutParams
            layoutParams?.setMargins(0, 0, 0, 0)
            if (layoutParams != null) {
                binding?.clBottomSheetContent?.layoutParams = layoutParams
            }
            binding?.clTickerInfo?.gone()
        }
    }

    private fun renderActionButton(state: PromoPageUiState) {
        when (state) {
            is PromoPageUiState.Success -> {
                when (entryPoint) {
                    PromoPageEntryPoint.CART_PAGE -> {
                        setBuyButton(
                            isEnabled = !state.isCalculating,
                            isLoading = state.isCalculating
                        )
                    }

                    PromoPageEntryPoint.CHECKOUT_PAGE, PromoPageEntryPoint.OCC_PAGE -> {
                        setBackToShipmentButton(
                            isEnabled = !state.isCalculating,
                            isLoading = state.isCalculating
                        )
                    }
                }
            }

            else -> {
                // no-op
            }
        }
    }

    private fun setBuyButton(
        isEnabled: Boolean = true,
        isLoading: Boolean = false
    ) {
        binding?.buttonBuy?.apply {
            this.buttonType = UnifyButton.Type.MAIN
            this.buttonVariant = UnifyButton.Variant.FILLED
            this.isEnabled = isEnabled
            this.isLoading = isLoading
        }
    }

    private fun setBackToShipmentButton(
        isEnabled: Boolean = true,
        isLoading: Boolean = false
    ) {
        binding?.buttonBackToShipment?.apply {
            this.buttonType = UnifyButton.Type.MAIN
            this.buttonVariant = UnifyButton.Variant.FILLED
            this.isEnabled = isEnabled
            this.isLoading = isLoading
        }
    }

    private fun refreshBottomSheetHeight(state: PromoPageUiState) {
        when (state) {
            is PromoPageUiState.Initial -> {
                binding?.run {
                    val maxShimmerHeight = getScreenHeight()
                        .minus(BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx())
                        .minus(BOTTOM_SHEET_HEADER_HEIGHT_IN_DP.toPx())
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(layoutShimmer.root)
                    constraintSet.constrainMaxHeight(R.id.clShimmerContainer, maxShimmerHeight)
                    constraintSet.applyTo(layoutShimmer.root)
                }
            }

            is PromoPageUiState.Success -> {
                binding?.run {
                    var maxPromoHeight = getScreenHeight()
                        .minus(BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx())
                        .minus(BOTTOM_SHEET_TOTAL_AMOUNT_HEIGHT_IN_DP.toPx())
                    val hasTickerInfo = state.tickerInfo.message.isNotBlank() &&
                        state.tickerInfo.iconUrl.isNotBlank() &&
                        state.tickerInfo.backgroundUrl.isNotBlank()
                    if (hasTickerInfo) {
                        maxPromoHeight = maxPromoHeight
                            .minus(BOTTOM_SHEET_TICKER_INFO_HEIGHT_IN_DP.toPx())
                    } else {
                        maxPromoHeight = maxPromoHeight
                            .minus(BOTTOM_SHEET_TICKER_INFO_MARGIN_IN_DP.toPx())
                    }
                    val hasSavingInfo = state.savingInfo.isVisible
                    if (hasSavingInfo) {
                        maxPromoHeight = maxPromoHeight
                            .minus(BOTTOM_SHEET_SAVING_INFO_HEIGHT_IN_DP.toPx())
                    }
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(clBottomSheetContent)
                    constraintSet.constrainMaxHeight(R.id.rvPromo, maxPromoHeight)
                    constraintSet.applyTo(clBottomSheetContent)
                }
            }

            is PromoPageUiState.Error -> {
                binding?.run {
                    val maxErrorHeight = getScreenHeight()
                        .minus(BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx())
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(rlBottomSheetWrapper)
                    constraintSet.constrainHeight(R.id.layoutGlobalError, maxErrorHeight)
                    constraintSet.applyTo(rlBottomSheetWrapper)
                }
            }
        }
    }

    private fun submitItems(items: List<DelegateAdapterItem>) {
        recyclerViewAdapter.submit(transformItems(items))
    }

    private fun transformItems(items: List<DelegateAdapterItem>): List<DelegateAdapterItem> {
        return items.map { item ->
            when (item) {
                is PromoRecommendationItem -> {
                    val recommendedPromos = items.filterIsInstance<PromoItem>()
                        .filter { it.isRecommended }
                    return@map item.copy(
                        promos = recommendedPromos
                    )
                }

                else -> {
                    return@map item
                }
            }
        }.filterNot { it is PromoItem && it.isRecommended }
    }

    private fun renderLoadingShimmer(isVisible: Boolean) {
        if (isVisible) {
            binding?.layoutShimmer?.clBottomSheetShimmerHeader
                ?.btnBottomSheetHeaderClose?.setOnClickListener {
                    dismiss()
                }
            binding?.layoutShimmer?.clBottomSheetShimmerHeader
                ?.btnBottomSheetHeaderClose?.setImage(
                    newIconId = IconUnify.CLOSE,
                    newDarkEnable = unifyprinciplesR.color.Unify_NN900,
                    newLightEnable = unifyprinciplesR.color.Unify_NN900
                )
            binding?.layoutShimmer?.clBottomSheetShimmerHeader
                ?.tpgBottomSheetHeaderTitle?.setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
            context?.let {
                binding?.rlBottomSheetWrapper?.background =
                    BottomSheetUtil.generateBackgroundDrawableWithColor(
                        it,
                        unifyprinciplesR.color.Unify_Background
                    )
            }
            binding?.rlBottomSheetWrapper?.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    val cornerRadius = 16.toPx()
                    val width = view?.width ?: 0
                    val height = view?.height ?: 0
                    outline?.setRoundRect(
                        0,
                        0,
                        width,
                        height.plus(cornerRadius),
                        cornerRadius.toFloat()
                    )
                }
            }
            binding?.rlBottomSheetWrapper?.clipToOutline = true
            binding?.clBottomSheetContent?.clipChildren = true
        }
        binding?.layoutShimmer?.root?.isVisible = isVisible
    }

    private fun renderHeader(isVisible: Boolean) {
        binding?.clBottomSheetHeader?.root?.isVisible = isVisible
    }

    private fun renderContent(isVisible: Boolean, hasPromoRecommendationSection: Boolean) {
        context?.let {
            binding?.rlBottomSheetWrapper?.background =
                BottomSheetUtil.generateBackgroundDrawableWithColor(
                    it,
                    unifyprinciplesR.color.Unify_Background
                )
        }
        binding?.rlBottomSheetWrapper?.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                val cornerRadius = 16.toPx()
                val width = view?.width ?: 0
                val height = view?.height ?: 0
                outline?.setRoundRect(
                    0,
                    0,
                    width,
                    height.plus(cornerRadius),
                    cornerRadius.toFloat()
                )
            }
        }
        binding?.rlBottomSheetWrapper?.clipToOutline = true
        binding?.clBottomSheetContent?.clipChildren = true
        binding?.clBottomSheetHeader?.root?.isVisible = isVisible
        binding?.rvPromo?.isVisible = isVisible
        binding?.cvTotalAmount?.isVisible = isVisible
        if (isVisible && hasPromoRecommendationSection) {
            binding?.rvPromo?.setPadding(0, 0, 0, 0)
        } else {
            binding?.rvPromo?.setPadding(0, BOTTOM_SHEET_HEADER_HEIGHT_IN_DP.toPx(), 0, 0)
        }
    }

    private fun renderSavingInfo(promoSavingInfo: PromoSavingInfo) {
        val formattedTotalVoucherAmount =
            promoSavingInfo.totalSelectedPromoBenefitAmount.splitByThousand()
        val text =
            "${promoSavingInfo.message} <b>Rp{{benefit_amount}}</b> ({{promo_count}} promo)"
                .replace("{{benefit_amount}}", formattedTotalVoucherAmount)
                .replace("{{promo_count}}", promoSavingInfo.selectedPromoCount.toString())
        context?.let { binding?.tpgTotalSavings?.text = text.toSpannableHtmlString(it) }
        binding?.clSavingInfo?.isVisible = promoSavingInfo.isVisible
    }

    private fun hideSavingInfo() {
        binding?.clSavingInfo?.gone()
    }

    private fun renderError(isVisible: Boolean, throwable: Throwable? = null) {
        if (isVisible) {
            binding?.layoutGlobalError?.clBottomSheetErrorHeader
                ?.btnBottomSheetHeaderClose?.setOnClickListener {
                    dismiss()
                }
            binding?.layoutGlobalError?.clBottomSheetErrorHeader
                ?.btnBottomSheetHeaderClose?.setImage(
                    newIconId = IconUnify.CLOSE,
                    newDarkEnable = unifyprinciplesR.color.Unify_NN900,
                    newLightEnable = unifyprinciplesR.color.Unify_NN900
                )
            binding?.layoutGlobalError?.clBottomSheetErrorHeader
                ?.tpgBottomSheetHeaderTitle?.setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
            val errorType = if (throwable != null) {
                getGlobalErrorType(throwable)
            } else {
                GlobalError.SERVER_ERROR
            }
            binding?.layoutGlobalError?.globalErrorPromoUsage?.setType(errorType)
            binding?.layoutGlobalError?.globalErrorPromoUsage?.errorAction?.text =
                context?.getText(promousageR.string.promo_usage_error_try_again)
                    ?: ""
            if (errorType == GlobalError.NO_CONNECTION) {
                binding?.layoutGlobalError?.globalErrorPromoUsage?.errorSecondaryAction?.text =
                    context?.getText(promousageR.string.promo_usage_error_to_settings)
                        ?: ""
                binding?.layoutGlobalError?.globalErrorPromoUsage?.setActionClickListener {
                    resetView()
                }
                binding?.layoutGlobalError?.globalErrorPromoUsage?.setSecondaryActionClickListener {
                    openNetworkSettings()
                }
            } else {
                binding?.layoutGlobalError?.globalErrorPromoUsage?.setActionClickListener {
                    resetView()
                }
            }
        }
        binding?.layoutGlobalError?.root?.isVisible = isVisible
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun openNetworkSettings() {
        try {
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
        } catch (_: Exception) {
            // no-op
        }
    }

    private fun observePromoCtaUiAction() {
        viewModel.promoCtaUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is PromoCtaUiAction.RegisterGoPayLaterCicil -> {
                    if (uiAction.cta.appLink.isNotBlank()) {
                        goToRegisterGoPayLaterCicilRegistration(uiAction.cta.appLink)
                    }
                }
            }
        }
    }

    private fun observeClearPromoUiAction() {
        viewModel.clearPromoUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is ClearPromoUiAction.Success -> {
                    renderLoadingDialog(false)
                    listener?.onClearPromoSuccess(
                        entryPoint = uiAction.entryPoint,
                        clearPromo = uiAction.clearPromo,
                        lastValidateUsePromoRequest = uiAction.lastValidateUseRequest,
                        isFlowMvcLockToCourier = isFlowMvcLockToCourier
                    )
                    processAndSendClickCheckoutPromoEvent(true, uiAction.clearedPromos)
                    dismiss()
                }

                is ClearPromoUiAction.Failed -> {
                    renderLoadingDialog(false)
                    listener?.onClearPromoFailed(uiAction.throwable)
                    processAndSendClickCheckoutPromoEvent(false, emptyList())
                    dismiss()
                }
            }
        }
    }

    private fun observeApplyPromoUiAction() {
        viewModel.applyPromoUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is ApplyPromoUiAction.SuccessWithApplyPromo -> {
                    renderLoadingDialog(false)
                    listener?.onApplyPromo(
                        entryPoint = uiAction.entryPoint,
                        validateUse = uiAction.validateUse,
                        lastValidateUsePromoRequest = uiAction.lastValidateUsePromoRequest
                    )
                    processAndSendClickCheckoutPromoEvent(true, uiAction.appliedPromos)
                    dismiss()
                }

                is ApplyPromoUiAction.SuccessNoAction -> {
                    renderLoadingDialog(false)
                    listener?.onApplyPromoNoAction()
                    dismiss()
                }

                is ApplyPromoUiAction.Failed -> {
                    renderLoadingDialog(false)
                    listener?.onApplyPromoFailed(uiAction.throwable)
                    processAndSendClickCheckoutPromoEvent(false, emptyList())
                    dismiss()
                }
            }
        }
    }

    private fun observeClosePromoPageUiAction() {
        viewModel.closePromoPageUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is ClosePromoPageUiAction.SuccessWithApplyPromo -> {
                    renderLoadingDialog(false)
                    listener?.onClosePageWithApplyPromo(
                        entryPoint = uiAction.entryPoint,
                        validateUse = uiAction.validateUse,
                        lastValidateUsePromoRequest = uiAction.lastValidateUsePromoRequest
                    )
                    processAndSendClickExitPromoBottomsheetEvent(uiAction.appliedPromos)
                    dismiss()
                }

                is ClosePromoPageUiAction.SuccessWithClearPromo -> {
                    renderLoadingDialog(false)
                    listener?.onClosePageWithClearPromo(
                        entryPoint = uiAction.entryPoint,
                        clearPromo = uiAction.clearPromo,
                        lastValidateUsePromoRequest = uiAction.lastValidateUsePromoRequest,
                        isFlowMvcLockToCourier = isFlowMvcLockToCourier,
                        clearedPromos = uiAction.clearedPromos
                    )
                    processAndSendClickExitPromoBottomsheetEvent()
                    dismiss()
                }

                is ClosePromoPageUiAction.SuccessNoAction -> {
                    renderLoadingDialog(false)
                    listener?.onClosePageWithNoAction()
                    dismiss()
                }

                is ClosePromoPageUiAction.Failed -> {
                    renderLoadingDialog(false)
                    processAndSendClickExitPromoBottomsheetEvent()
                    dismiss()
                }
            }
        }
    }

    private fun observeAutoScrollUiAction() {
        viewModel.clickPromoUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is ClickPromoUiAction.Updated -> {
                    // Auto scroll to selected promo if promo is selected and outside of user's view
                    if (uiAction.clickedPromo.state is PromoItemState.Selected) {
                        try {
                            val firstVisibleItemPosition =
                                layoutManager.findFirstCompletelyVisibleItemPosition()
                            val lastVisibleItemPosition =
                                layoutManager.findLastCompletelyVisibleItemPosition()
                            val itemPosition = getItemPosition(uiAction.clickedPromo.id)

                            val isVisibleInCurrentScreen =
                                itemPosition in firstVisibleItemPosition until (lastVisibleItemPosition + 1)
                            val isIdle =
                                binding?.rvPromo?.scrollState == RecyclerView.SCROLL_STATE_IDLE
                            if (!isVisibleInCurrentScreen && itemPosition != RecyclerView.NO_POSITION && isIdle) {
                                // Delay scroll until layout is finish updating
                                Handler(Looper.getMainLooper()).postDelayed({
                                    binding?.rvPromo?.smoothSnapToPosition(
                                        position = itemPosition,
                                        topOffset = BOTTOM_SHEET_HEADER_HEIGHT_IN_DP.toPx()
                                    )
                                }, AUTO_SCROLL_DELAY)
                            }
                        } catch (ignored: Exception) {
                            // no-op
                        }
                    }
                    processAndSendClickPromoCardEvent(uiAction.clickedPromo)
                }
            }
        }
    }

    private fun observeClickTncUiAction() {
        viewModel.clickTncUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is ClickTncUiAction.Success -> {
                    processAndSendClickDetailTermAndConditionsEvent(uiAction.selectedPromos)
                }
            }
        }
    }

    private fun getItemPosition(itemId: String): Int {
        return recyclerViewAdapter.items.indexOfFirst { it.id == itemId }
    }

    private fun showPromoTncBottomSheet(item: PromoTncItem) {
        val tncBottomSheet = PromoUsageTncBottomSheet.newInstance(
            promoCodesWithTitle = item.selectedPromoCodesWithTitle,
            source = entryPoint,
            userId = userSession.userId
        )
        tncBottomSheet.show(childFragmentManager)
    }

    private val onClickUsePromoRecommendation: () -> Unit = {
        viewModel.onUsePromoRecommendation()
    }

    private val onClickRecommendationPromo = { clickedItem: PromoItem ->
        viewModel.onClickPromo(clickedItem)
    }

    private val onClickPromoItem = { clickedItem: PromoItem ->
        viewModel.onClickPromo(clickedItem)

        if (clickedItem.isPromoGopayLater && clickedItem.isPromoCtaRegisterGopayLater && clickedItem.isPromoCtaValid) {
            processAndSendClickActivationGoPayLater(clickedItem)
        }

        if (clickedItem.isPromoGopayLater && !clickedItem.isPromoCtaRegisterGopayLater) {
            processAndSendClickGplEligible(clickedItem)
        }
    }

    private val onImpressionPromo = { item: PromoItem ->
        processAndSendImpressionOfPromoCardNewEvent(item)

        if (item.isPromoGopayLater && item.isPromoCtaRegisterGopayLater && item.isPromoCtaValid) {
            processAndSendImpressionActivationGoPayLater(item)
        }

        if (item.isPromoGopayLater && !item.isPromoCtaRegisterGopayLater) {
            processAndSendImpressionGplEligible(item)
        }
    }

    private val onRecommendationAnimationEnd = {
        viewModel.onRecommendationAnimationEnd()
    }

    private val onClickClose = {
        val validateUsePromoRequest = arguments?.getParcelable(BUNDLE_KEY_VALIDATE_USE)
            ?: ValidateUsePromoRequest()
        val boPromoCodes: List<String> = arguments?.getStringArrayList(BUNDLE_KEY_BO_PROMO_CODES)
            ?: emptyList()

        renderLoadingDialog(true)
        viewModel.onClosePromoPage(
            entryPoint = entryPoint,
            validateUsePromoRequest = validateUsePromoRequest,
            boPromoCodes = boPromoCodes,
            isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(
                RemoteConfigInstance.getInstance().abTestPlatform
            ).isRevamp()
        )
    }

    private val onClickPromoAccordionHeader: (PromoAccordionHeaderItem) -> Unit =
        { headerItem: PromoAccordionHeaderItem ->
            viewModel.onClickAccordionHeader(headerItem)
            promoUsageAnalytics.sendClickExpandPromoSectionEvent(
                userId = userSession.userId,
                entryPoint = entryPoint,
                promoHeader = headerItem
            )
        }

    private val onClickPromoAccordionViewAll: (PromoAccordionViewAllItem) -> Unit =
        { viewAllItem: PromoAccordionViewAllItem ->
            viewModel.onClickViewAllAccordion(viewAllItem)
            promoUsageAnalytics.sendClickExpandPromoSectionDetailEvent(
                userId = userSession.userId,
                entryPoint = entryPoint,
                promoViewAll = viewAllItem
            )
        }

    private fun goToRegisterGoPayLaterCicilRegistration(appLink: String) {
        val intent = RouteManager.getIntent(context, appLink)
        registerGopayLaterCicilLauncher.launch(intent)
    }

    private val onClickPromoTnc: (PromoTncItem) -> Unit = { item ->
        viewModel.onClickTnc()
        showPromoTncBottomSheet(item)
    }

    private val onAttemptPromoCode: (View, String) -> Unit = { view, attemptedPromoCode ->
        resetFocusOnPromoAttemptTextField()
        KeyboardHandler.DropKeyboard(view.context, view)
        renderLoadingDialog(true)
        val promoRequest: PromoRequest? = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.onAttemptPromoCode(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode,
            onSuccess = { items ->
                processAndSendClickPakaiPromoPromoCodeEvent(items)
            }
        )
    }

    private fun renderLoadingDialog(isVisible: Boolean) {
        if (isVisible) {
            if (loaderDialog == null) {
                loaderDialog = LoaderDialog(requireContext())
                loaderDialog?.show()
            }
        } else {
            if (loaderDialog != null) {
                loaderDialog?.dismiss()
                loaderDialog = null
            }
        }
    }

    private fun showToastError(throwable: Throwable) {
        activity?.let { showToastError(throwable.getErrorMessage(it)) }
    }

    private fun showToastError(message: String) {
        parentFragment?.view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    interface Listener {

        fun onClosePageWithApplyPromo(
            entryPoint: PromoPageEntryPoint,
            validateUse: ValidateUsePromoRevampUiModel,
            lastValidateUsePromoRequest: ValidateUsePromoRequest
        )

        fun onClosePageWithClearPromo(
            entryPoint: PromoPageEntryPoint,
            clearPromo: ClearPromoUiModel,
            lastValidateUsePromoRequest: ValidateUsePromoRequest,
            isFlowMvcLockToCourier: Boolean,
            clearedPromos: List<PromoItem>
        )

        fun onClosePageWithNoAction()

        fun onApplyPromo(
            entryPoint: PromoPageEntryPoint,
            validateUse: ValidateUsePromoRevampUiModel,
            lastValidateUsePromoRequest: ValidateUsePromoRequest
        )

        fun onApplyPromoNoAction()

        fun onApplyPromoFailed(throwable: Throwable)

        fun onClearPromoSuccess(
            entryPoint: PromoPageEntryPoint,
            clearPromo: ClearPromoUiModel,
            lastValidateUsePromoRequest: ValidateUsePromoRequest,
            isFlowMvcLockToCourier: Boolean
        )

        fun onClearPromoFailed(throwable: Throwable)
    }

    private fun getEnableSendAutoApplyTracker(): Boolean {
        val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context?.applicationContext)
        return remoteConfig.getBoolean(RemoteConfigKey.ANDROID_ENABLE_AUTO_APPLY_PROMO_TRACKER, false)
    }

    // region Tracker
    private fun processAndSendViewAvailablePromoListNewEvent(
        items: List<DelegateAdapterItem>? = null,
        isError: Boolean = false
    ) {
        val promos = items?.filterIsInstance<PromoItem>()
        promoUsageAnalytics.sendViewAvailablePromoListNewEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            items = promos,
            isError = isError
        )
    }

    private fun processAndSendImpressionOfPromoCardNewEvent(viewedPromo: PromoItem) {
        promoUsageAnalytics.sendImpressionOfPromoCardNewEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            viewedPromo = viewedPromo
        )
    }

    private fun processAndSendClickPromoCardEvent(clickedPromo: PromoItem) {
        promoUsageAnalytics.sendClickPromoCardEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            clickedPromo = clickedPromo
        )
    }

    private fun processAndSendClickPakaiPromoPromoCodeEvent(items: List<DelegateAdapterItem>) {
        val attemptItem = items.getAttemptItem()
        val attemptedPromo = if (attemptItem != null) {
            items.getAttemptedPromos().firstOrNull { it.code == attemptItem.attemptedPromoCode }
        } else {
            null
        }
        promoUsageAnalytics.sendClickPakaiPromoPromoCodeEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            attemptItem = attemptItem,
            attemptedPromo = attemptedPromo
        )
    }

    private fun processAndSendClickPakaiPromoNewEvent(
        promoRecommendation: PromoRecommendationItem,
        items: List<DelegateAdapterItem>
    ) {
        val recommendedPromos = items.filterIsInstance<PromoItem>().filter { it.isRecommended }
        promoUsageAnalytics.sendClickPakaiPromoNewEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            promoRecommendation = promoRecommendation,
            recommendedPromos = recommendedPromos
        )
    }

    private fun processAndSendClickDetailTermAndConditionsEvent(selectedPromos: List<PromoItem>) {
        promoUsageAnalytics.sendClickDetailTermAndConditionsEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            selectedPromos = selectedPromos
        )
    }

    private fun processAndSendClickCheckoutPromoEvent(
        isSuccess: Boolean,
        appliedPromos: List<PromoItem> = emptyList()
    ) {
        promoUsageAnalytics.sendClickCheckoutPromoEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            appliedPromos = appliedPromos,
            isSuccess = isSuccess
        )
    }

    private fun processAndSendClickExitPromoBottomsheetEvent(
        appliedPromos: List<PromoItem> = emptyList()
    ) {
        promoUsageAnalytics.sendClickExitPromoBottomsheetEvent(
            userId = userSession.userId,
            entryPoint = entryPoint,
            appliedPromos = appliedPromos
        )
    }

    private fun processAndSendImpressionActivationGoPayLater(viewedPromo: PromoItem) {
        promoUsageAnalytics.sendImpressionActivationGoPayLater(
            userId = userSession.userId,
            entryPoint = entryPoint,
            viewedPromo = viewedPromo
        )
    }

    private fun processAndSendClickActivationGoPayLater(clickedPromo: PromoItem) {
        promoUsageAnalytics.sendClickActivationGoPayLater(
            userId = userSession.userId,
            entryPoint = entryPoint,
            clickedPromo = clickedPromo
        )
    }

    private fun processAndSendImpressionAutoApplyGpl(viewedPromo: PromoItem) {
        promoUsageAnalytics.sendImpressionAutoApplyGpl(
            userId = userSession.userId,
            entryPoint = entryPoint,
            viewedPromo = viewedPromo
        )
    }

    private fun processAndSendImpressionGplEligible(viewedPromo: PromoItem) {
        promoUsageAnalytics.sendImpressionGplEligible(
            userId = userSession.userId,
            entryPoint = entryPoint,
            viewedPromo = viewedPromo
        )
    }

    private fun processAndSendClickGplEligible(clickedPromo: PromoItem) {
        promoUsageAnalytics.sendClickGplEligible(
            userId = userSession.userId,
            entryPoint = entryPoint,
            clickedPromo = clickedPromo
        )
    }

    // endregion
}
