package com.tokopedia.promousage.view.bottomsheet

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import android.graphics.Outline
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.airbnb.lottie.LottieCompositionFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.media.loader.loadImage
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageBottomsheetBinding
import com.tokopedia.promousage.di.DaggerPromoUsageComponent
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.analytics.PromoUsageAnalytics
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.extension.getErrorMessage
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
import com.tokopedia.promousage.view.viewmodel.GetPromoRecommendationUiAction
import com.tokopedia.promousage.view.viewmodel.PromoCtaUiAction
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.promousage.view.viewmodel.UsePromoRecommendationUiAction
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.max


class PromoUsageBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val BUNDLE_KEY_ENTRY_POINT = "entry_point"
        private const val BUNDLE_KEY_PROMO_REQUEST = "promo_request"
        private const val BUNDLE_KEY_TOTAL_AMOUNT = "total_amount"
        private const val BUNDLE_KEY_CHOSEN_ADDRESS = "chosen_address"
        private const val BUNDLE_KEY_VALIDATE_USE = "validate_use"
        private const val BUNDLE_KEY_BO_PROMO_CODES = "bo_promo_codes"
        private const val BUNDLE_KEY_PROMO_CODE = "promo_code"
        private const val BUNDLE_KEY_IS_FLOW_MVC_LOCK_TO_COURIER = "is_flow_mvc_lock_to_courier"

        private const val BOTTOM_SHEET_MARGIN_TOP_IN_DP = 64

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
    lateinit var analytics: PromoUsageAnalytics
    private val viewModel: PromoUsageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PromoUsageViewModel::class.java]
    }

    private var _binding by autoClearedNullable<PromoUsageBottomsheetBinding>()
    private val binding: PromoUsageBottomsheetBinding
        get() = _binding!!
    var listener: Listener? = null
    var currentPeekHeight: Int = 0
    val maxPeekHeight: Int
        get() = getScreenHeight() - BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx()

    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(PromoRecommendationDelegateAdapter(onClickUsePromoRecommendation))
            .add(PromoAccordionHeaderDelegateAdapter(onClickPromoAccordionHeader))
            .add(PromoAccordionItemDelegateAdapter(onClickPromoItem))
            .add(PromoAccordionViewAllDelegateAdapter(onClickPromoAccordionViewAll))
            .add(PromoTncDelegateAdapter(onClickPromoTnc))
            .add(PromoAttemptCodeDelegateAdapter(onAttemptPromoCode))
            .build()
    }
    private val registerGopayLaterCicilLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val promoCode = result.data?.getStringExtra(BUNDLE_KEY_PROMO_CODE)
            if (!promoCode.isNullOrBlank()) {
                val promoRequest =
                    arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST) ?: PromoRequest()
                val chosenAddress =
                    arguments?.getParcelable<ChosenAddress>(BUNDLE_KEY_CHOSEN_ADDRESS)
                viewModel.loadPromoListWithPreSelectedPromo(
                    promoRequest = promoRequest,
                    chosenAddress = chosenAddress,
                    preSelectPromoCode = promoCode
                )
            }
        }
    private var loaderDialog: LoaderDialog? = null

    private fun setupDependencyInjection() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerPromoUsageComponent.builder()
                    .baseAppComponent(baseAppComponent.baseAppComponent)
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
            com.tokopedia.unifycomponents.R.style.UnifyBottomSheetOverlapStyle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PromoUsageBottomsheetBinding.inflate(inflater, container, false)
        dialog?.setOnShowListener {
            applyBottomSheetMaxHeightRule()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        renderLoadingDialog(false)
        _binding = null
    }

    private fun applyBottomSheetMaxHeightRule() {
        val frameDialogView = binding.rlBottomSheetWrapper.parent as FrameLayout
        frameDialogView.setBackgroundColor(Color.TRANSPARENT)
        frameDialogView.bringToFront()
        frameDialogView.layoutParams.height = maxPeekHeight
        val bottomSheetBehavior = BottomSheetBehavior.from(frameDialogView)
        bottomSheetBehavior.peekHeight = maxPeekHeight
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
        val promoRequest: PromoRequest? = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.setEntryPoint(entryPoint)
        val defaultErrorMessage = context?.getString(R.string.promo_usage_global_error_promo) ?: ""
        viewModel.setDefaultErrorMessage(defaultErrorMessage)
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
        renderContent(false)
        renderLoadingShimmer(true)

        val totalAmount = arguments?.getDouble(BUNDLE_KEY_TOTAL_AMOUNT) ?: 0.0
        val validateUsePromoRequest = arguments?.getParcelable(BUNDLE_KEY_VALIDATE_USE)
            ?: ValidateUsePromoRequest()
        val boPromoCodes: List<String> = arguments?.getStringArrayList(BUNDLE_KEY_BO_PROMO_CODES)
            ?: emptyList()

        binding.tpgBottomSheetHeaderTitle.text = context?.getString(R.string.promo_voucher_promo)
        binding.btnBottomSheetHeaderClose.setOnClickListener {
            dismiss()
        }
        binding.clTickerInfo.gone()
        binding.clBottomSheetContent.background = BottomSheetUtil
            .generateBackgroundDrawableWithColor(
                binding.root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            )
        binding.ivPromoRecommendationBackground.invisible()
        binding.rvPromo.itemAnimator = null
        binding.rvPromo.layoutManager = LinearLayoutManager(context)
        binding.rvPromo.adapter = recyclerViewAdapter
        when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> {
                binding.tpgTotalAmountLabel.visible()
                binding.tpgTotalAmount.text = context?.getString(
                    R.string.promo_voucher_placeholder_total_price,
                    totalAmount.splitByThousand()
                )
                binding.tpgTotalAmount.visible()
                binding.cvTotalAmount.visible()
                binding.buttonBuy.setOnClickListener {
                    renderLoadingDialog(true)
                    viewModel.onClickBuy(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes
                    )
                }
                binding.buttonBuy.visible()
                binding.buttonBackToShipment.gone()
            }

            PromoPageEntryPoint.OCC_PAGE -> {
                binding.tpgTotalAmountLabel.visible()
                binding.tpgTotalAmount.text = context?.getString(
                    R.string.promo_voucher_placeholder_total_price,
                    totalAmount.splitByThousand()
                )
                binding.tpgTotalAmount.visible()
                binding.cvTotalAmount.visible()
                val icon = ContextCompat.getDrawable(
                    context ?: return, R.drawable.promo_usage_ic_protection_check
                )
                binding.buttonBuy.setDrawable(icon)
                binding.buttonBuy.setOnClickListener {
                    renderLoadingDialog(true)
                    viewModel.onClickBuy(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes
                    )
                }
                binding.buttonBuy.visible()
                binding.buttonBackToShipment.gone()
            }

            PromoPageEntryPoint.CHECKOUT_PAGE -> {
                binding.tpgTotalAmountLabel.gone()
                binding.tpgTotalAmount.gone()
                binding.buttonBuy.gone()
                binding.buttonBackToShipment.text =
                    context?.getString(R.string.promo_voucher_back_to_shipment)
                binding.buttonBackToShipment.setOnClickListener {
                    viewModel.onBackToCheckout(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes
                    )
                }
                binding.buttonBackToShipment.visible()
            }
        }
        observeKeyboardVisibility()
    }

    private fun addHeaderScrollListener() {
        binding.rvPromo.clearOnScrollListeners()
        binding.rvPromo.addOnScrollListener(object : OnScrollListener() {
            var scrollY = 0f
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollY += dy.toFloat()
                scrollY = max(scrollY, 0f)

                if (scrollY > 0) {
                    val layoutManager: LinearLayoutManager? = recyclerView.layoutManager as? LinearLayoutManager
                    if (layoutManager != null) {
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                            scrollY = 0f
                            renderTransparentHeader()
                        } else {
                            renderWhiteHeader()
                        }
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
        binding.rvPromo.clearOnScrollListeners()
    }

    private fun renderWhiteHeader() {
        with(binding) {
            clBottomSheetHeader.background = BottomSheetUtil
                .generateBackgroundDrawableWithColor(
                    binding.root.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            tpgBottomSheetHeaderTitle
                .setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            btnBottomSheetHeaderClose.setImageDrawable(
                if (binding.root.context.isDarkMode()) {
                    getIconUnifyDrawable(
                        context = binding.root.context,
                        iconId = IconUnify.CLOSE,
                        assetColor = ContextCompat.getColor(
                            binding.root.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                        )
                    )
                } else {
                    getIconUnifyDrawable(
                        context = binding.root.context,
                        iconId = IconUnify.CLOSE,
                        assetColor = ContextCompat.getColor(
                            binding.root.context,
                            com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
                        )
                    )
                }
            )
        }
    }

    private fun renderTransparentHeader() {
        with(binding) {
            clBottomSheetHeader.background = null
            tpgBottomSheetHeaderTitle
                .setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
            btnBottomSheetHeaderClose.setImageDrawable(
                getIconUnifyDrawable(
                    context = binding.root.context,
                    iconId = IconUnify.CLOSE,
                    assetColor = ContextCompat.getColor(
                        binding.root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Static_White
                    )
                )
            )
        }
    }

    private fun observeKeyboardVisibility() {
        activity?.let { activity ->
            ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView) { _: View?, insets: WindowInsetsCompat ->
                val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                if (isKeyboardVisible) {
                    // isKeyboardVisible means user tap voucher code text field
                    scrollToVoucherCodeTextField(keyboardHeight)
                } else {
                    resetFocusFromVoucherCodeTextField()
                }
                insets
            }
        }
    }

    private fun scrollToVoucherCodeTextField(keyboardHeight: Int) {
        val itemCount = recyclerViewAdapter.itemCount
        binding.rvPromo.smoothScrollToPosition(itemCount)

        // Add padding to make voucher code text field displayed above keyboard
        binding.rvPromo.setPadding(0, 0, 0, keyboardHeight.toDp())
    }

    private fun resetFocusFromVoucherCodeTextField() {
        binding.rvPromo.setPadding(0, 0, 0, 0)
    }

    private fun setupObservers() {
        observePromoPageState()
        observePromoRecommendationUiAction()
        observeUsePromoRecommendationUiAction()
        observePromoAttemptUiAction()
        observePromoCtaUiAction()
        observeClearPromoUiAction()
        observeApplyPromoUiAction()
    }

    private fun observePromoRecommendationUiAction() {
        viewModel.getPromoRecommendationUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is GetPromoRecommendationUiAction.NotEmpty -> {
                    renderPromoRecommendationBackground(uiAction.promoRecommendation)
                    addHeaderScrollListener()
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
                    showPromoRecommendationAnimation(uiAction.promoRecommendation)
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
                    if (uiAction.errorMessage.isNotBlank()) {
                        showToastMessage(uiAction.errorMessage)
                    }
                }
            }
        }
    }

    private fun observePromoPageState() {
        viewModel.promoPageUiState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is PromoPageUiState.Initial -> {
                    renderHeader(false)
                    renderContent(false)
                    renderLoadingDialog(false)
                    renderLoadingShimmer(true)
                    renderError(false)
                    refreshBottomSheetHeight(state)
                }

                is PromoPageUiState.Success -> {
                    renderHeader(true)
                    renderTickerInfo(state.tickerInfo)
                    submitItems(state.items)
                    renderLoadingDialog(false)
                    renderLoadingShimmer(false)
                    renderContent(true)
                    renderSavingInfo(state.savingInfo)
                    renderError(false)
                    refreshBottomSheetHeight(state)
                }

                is PromoPageUiState.Error -> {
                    renderHeader(true)
                    renderLoadingShimmer(false)
                    renderLoadingDialog(false)
                    renderContent(false)
                    renderError(true, state.exception)
                    refreshBottomSheetHeight(state)
                }

                else -> {
                    // no-op
                }
            }
        }
    }

    private fun renderPromoRecommendationBackground(promoRecommendation: PromoRecommendationItem) {
        with(binding) {
            clBottomSheetContent.background = BottomSheetUtil
                .generateBackgroundDrawableWithColor(
                    root.context,
                    promoRecommendation.backgroundColor
                )
            clBottomSheetContent.outlineProvider = object : ViewOutlineProvider() {
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
            clBottomSheetContent.clipToOutline = true
            clBottomSheetContent.clipChildren = true
            ivPromoRecommendationBackground.loadImage(promoRecommendation.backgroundUrl)
            ivPromoRecommendationBackground.visible()
        }
    }

    private fun renderTickerInfo(tickerInfo: PromoPageTickerInfo) {
        with(binding) {
            val hasTickerInfo = tickerInfo.message.isNotBlank() && tickerInfo.iconUrl.isNotBlank()
                && tickerInfo.backgroundUrl.isNotBlank()
            if (hasTickerInfo) {
                ivTickerInfoBackground.loadImage(
                    url = tickerInfo.backgroundUrl,
                    properties = {
                        centerCrop()
                    }
                )
                iuTickerInfoIcon.loadImage(tickerInfo.iconUrl)
                tpgTickerInfoMessage.text = tickerInfo.message
                clTickerInfo.visible()
            } else {
                val layoutParams =
                    clBottomSheetContent.layoutParams as? RelativeLayout.LayoutParams
                layoutParams?.setMargins(0, 0, 0, 0)
                clBottomSheetContent.layoutParams = layoutParams
                clBottomSheetContent.requestLayout()
                clTickerInfo.gone()
            }
        }
    }

    private fun refreshBottomSheetHeight(state: PromoPageUiState) {
        binding.root.addOneTimeGlobalLayoutListener {
            val contentHeight = when (state) {
                is PromoPageUiState.Initial -> {
                    maxPeekHeight
                }

                is PromoPageUiState.Error -> {
                    binding.layoutGlobalError.root.height
                }

                is PromoPageUiState.Success -> {
                    binding.root.height
                }
            }
            if (currentPeekHeight == contentHeight) {
                return@addOneTimeGlobalLayoutListener
            }
            currentPeekHeight = if (contentHeight < maxPeekHeight) {
                contentHeight
            } else {
                maxPeekHeight
            }
            val frameDialogView = binding.rlBottomSheetWrapper.parent as FrameLayout
            frameDialogView.layoutParams.height = currentPeekHeight
            val bottomSheetBehavior = BottomSheetBehavior.from(frameDialogView)
            bottomSheetBehavior.peekHeight = currentPeekHeight
        }
    }

    private fun submitItems(items: List<DelegateAdapterItem>) {
        recyclerViewAdapter.submit(items)
    }

    private fun renderLoadingShimmer(isVisible: Boolean) {
        with(binding) {
            if (isVisible) {
                layoutShimmer.iuCloseIcon.setOnClickListener {
                    dismiss()
                }
                rlBottomSheetWrapper.background =
                    BottomSheetUtil.generateBackgroundDrawableWithColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Background
                    )
                rlBottomSheetWrapper.outlineProvider = object : ViewOutlineProvider() {
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
                rlBottomSheetWrapper.clipToOutline = true
                clBottomSheetContent.clipChildren = true
            }
            layoutShimmer.root.isVisible = isVisible
        }
    }

    private fun renderHeader(isVisible: Boolean) {
        binding.clBottomSheetHeader.isVisible = isVisible
    }

    private fun renderContent(isVisible: Boolean) {
        with(binding) {
            rlBottomSheetWrapper.background =
                BottomSheetUtil.generateBackgroundDrawableWithColor(
                    binding.root.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            rlBottomSheetWrapper.outlineProvider = object : ViewOutlineProvider() {
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
            rlBottomSheetWrapper.clipToOutline = true
            clBottomSheetContent.clipChildren = true
            rvPromo.isVisible = isVisible
            cvTotalAmount.isVisible = isVisible
        }
    }

    private fun renderSavingInfo(promoSavingInfo: PromoSavingInfo) {
        with(binding) {
            val formattedTotalVoucherAmount =
                promoSavingInfo.totalSelectedPromoBenefitAmount.splitByThousand()
            val text =
                "${promoSavingInfo.message} <b>Rp{{benefit_amount}}</b> ({{promo_count}} promo)"
                    .replace("{{benefit_amount}}", formattedTotalVoucherAmount)
                    .replace("{{promo_count}}", promoSavingInfo.selectedPromoCount.toString())
            tpgTotalSavings.text = HtmlLinkHelper(root.context, text).spannedString
            clSavingInfo.isVisible = promoSavingInfo.selectedPromoCount > 0
                && promoSavingInfo.totalSelectedPromoBenefitAmount > 0
        }
    }

    private fun renderError(isVisible: Boolean, throwable: Throwable? = null) {
        with(binding) {
            if (isVisible) {
                layoutGlobalError.iuCloseIcon.setOnClickListener {
                    dismiss()
                }
                val errorType = if (throwable != null) {
                    getGlobalErrorType(throwable)
                } else {
                    GlobalError.SERVER_ERROR
                }
                layoutGlobalError.globalErrorPromoUsage.setType(errorType)
                layoutGlobalError.globalErrorPromoUsage.errorAction.text =
                    context?.getText(com.tokopedia.promousage.R.string.promo_usage_error_try_again)
                        ?: ""
                if (errorType == GlobalError.NO_CONNECTION) {
                    layoutGlobalError.globalErrorPromoUsage.errorSecondaryAction.text =
                        context?.getText(com.tokopedia.promousage.R.string.promo_usage_error_to_settings)
                            ?: ""
                    layoutGlobalError.globalErrorPromoUsage.setActionClickListener {
                        resetView()
                    }
                    layoutGlobalError.globalErrorPromoUsage.setSecondaryActionClickListener {
                        openNetworkSettings()
                    }
                } else {
                    layoutGlobalError.globalErrorPromoUsage.setActionClickListener {
                        resetView()
                    }
                }
            }
            layoutGlobalError.root.isVisible = isVisible
        }
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun openNetworkSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        try {
            startActivity(intent)
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
                }

                is ClearPromoUiAction.Failed -> {
                    renderLoadingDialog(false)
                    showToastMessage(uiAction.throwable)
                    listener?.onClearPromoFailed(uiAction.throwable)
                }
            }
        }
    }

    private fun observeApplyPromoUiAction() {
        viewModel.applyPromoUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is ApplyPromoUiAction.Success -> {
                    renderLoadingDialog(false)
                    listener?.onApplyPromoSuccess(
                        entryPoint = uiAction.entryPoint,
                        validateUse = uiAction.validateUse,
                        lastValidateUsePromoRequest = uiAction.lastValidateUsePromoRequest
                    )
                }

                is ApplyPromoUiAction.Failed -> {
                    renderLoadingDialog(false)
                    if (uiAction.shouldReload) {
                        resetView()
                    }
                    showToastMessage(uiAction.throwable)
                    listener?.onApplyPromoFailed(uiAction.throwable)
                }
            }
        }
    }

    private fun showPromoTncBottomSheet(item: PromoTncItem) {
        val tncBottomSheet = PromoUsageTncBottomSheet.newInstance(
            promoCodes = item.selectedPromoCodes,
            source = entryPoint,
            userId = userSession.userId
        )
        tncBottomSheet.show(childFragmentManager)
    }

    private val onClickUsePromoRecommendation: () -> Unit = {
        viewModel.onUsePromoRecommendation()
    }

    private val onClickPromoItem = { clickedItem: PromoItem ->
        viewModel.onClickPromo(clickedItem)
    }

    private val onClickPromoAccordionHeader: (PromoAccordionHeaderItem) -> Unit =
        { headerItem: PromoAccordionHeaderItem ->
            viewModel.onClickAccordionHeader(headerItem)
        }

    private val onClickPromoAccordionViewAll: (PromoAccordionViewAllItem) -> Unit =
        { viewAllItem: PromoAccordionViewAllItem ->
            viewModel.onClickViewAllAccordion(viewAllItem)
        }

    private fun goToRegisterGoPayLaterCicilRegistration(appLink: String) {
        val intent = RouteManager.getIntent(context, appLink)
        registerGopayLaterCicilLauncher.launch(intent)
    }

    private fun showPromoRecommendationAnimation(item: PromoRecommendationItem) {
        LottieCompositionFactory.fromUrl(context, item.animationUrl)
            .addListener { result ->
                binding.lottieAnimationView.visible()
                binding.lottieAnimationView.setComposition(result)
                binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {
                        // no-op
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        binding.lottieAnimationView.gone()
                    }

                    override fun onAnimationCancel(animator: Animator) {
                        // no-op
                    }

                    override fun onAnimationRepeat(animator: Animator) {
                        // no-op
                    }
                })
                binding.lottieAnimationView.playAnimation()
            }
    }

    private val onClickPromoTnc: (PromoTncItem) -> Unit = { item ->
        showPromoTncBottomSheet(item)
    }

    private val onAttemptPromoCode: (String) -> Unit = { attemptedPromoCode ->
        renderLoadingDialog(true)
        val promoRequest: PromoRequest? = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.onAttemptPromoCode(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode,
            onSuccess = {
                val itemCount = binding.rvPromo.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    binding.rvPromo.scrollToPosition(itemCount - 1)
                }
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

    private fun showToastMessage(throwable: Throwable) {
        context?.let { showToastMessage(throwable.getErrorMessage(it)) }
    }

    private fun showToastMessage(message: String) {
        Toaster.build(binding.root, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
    }

    interface Listener {

        fun onClosePromo()

        fun onApplyPromoSuccess(
            entryPoint: PromoPageEntryPoint,
            validateUse: ValidateUsePromoRevampUiModel,
            lastValidateUsePromoRequest: ValidateUsePromoRequest
        )

        fun onApplyPromoFailed(throwable: Throwable)

        fun onClearPromoSuccess(
            entryPoint: PromoPageEntryPoint,
            clearPromo: ClearPromoUiModel,
            lastValidateUsePromoRequest: ValidateUsePromoRequest,
            isFlowMvcLockToCourier: Boolean,
        )

        fun onClearPromoFailed(throwable: Throwable)
    }
}
