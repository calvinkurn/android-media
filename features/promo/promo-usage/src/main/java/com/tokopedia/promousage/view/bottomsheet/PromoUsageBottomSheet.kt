package com.tokopedia.promousage.view.bottomsheet

import android.animation.Animator
import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
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
import com.tokopedia.promousage.view.viewmodel.ClosePromoPageUiAction
import com.tokopedia.promousage.view.viewmodel.GetPromoRecommendationUiAction
import com.tokopedia.promousage.view.viewmodel.PromoCtaUiAction
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.promousage.view.viewmodel.UsePromoRecommendationUiAction
import com.tokopedia.promousage.view.viewmodel.getRecommendationItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.max
import com.tokopedia.promousage.R as promousageR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.unifycomponents.R as unifycomponentsR


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
        private const val BOTTOM_SHEET_HEADER_HEIGHT_IN_DP = 56

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

    private var binding by autoClearedNullable<PromoUsageBottomsheetBinding>()
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
    private val layoutManager = LinearLayoutManager(context)
    private val registerGopayLaterCicilLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val promoRequest =
                    arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST) ?: PromoRequest()
                val chosenAddress =
                    arguments?.getParcelable<ChosenAddress>(BUNDLE_KEY_CHOSEN_ADDRESS)
                viewModel.loadPromoListWithPreSelectedGopayLaterPromo(
                    promoRequest = promoRequest,
                    chosenAddress = chosenAddress
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
        binding = null
    }

    private fun applyBottomSheetMaxHeightRule() {
        val frameDialogView = binding?.rlBottomSheetWrapper?.parent as FrameLayout
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

        binding?.tpgBottomSheetHeaderTitle?.text = context?.getString(R.string.promo_voucher_promo)
        binding?.btnBottomSheetHeaderClose?.setOnClickListener {
            renderLoadingDialog(true)
            viewModel.onClosePromoPage(
                entryPoint = entryPoint,
                validateUsePromoRequest = validateUsePromoRequest,
                boPromoCodes = boPromoCodes
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
        binding?.ivPromoRecommendationBackground?.invisible()
        binding?.rvPromo?.layoutManager = layoutManager
        binding?.rvPromo?.adapter = recyclerViewAdapter
        when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> {
                binding?.tpgTotalAmountLabel?.visible()
                binding?.tpgTotalAmount?.text = context?.getString(
                    R.string.promo_voucher_placeholder_total_price,
                    totalAmount.splitByThousand()
                )
                binding?.tpgTotalAmount?.visible()
                binding?.cvTotalAmount?.visible()
                binding?.buttonBuy?.setOnClickListener {
                    renderLoadingDialog(true)
                    viewModel.onClickBuy(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes
                    )
                }
                binding?.buttonBuy?.visible()
                binding?.buttonBackToShipment?.gone()
            }

            PromoPageEntryPoint.OCC_PAGE -> {
                binding?.tpgTotalAmountLabel?.visible()
                binding?.tpgTotalAmount?.text = context?.getString(
                    R.string.promo_voucher_placeholder_total_price,
                    totalAmount.splitByThousand()
                )
                binding?.tpgTotalAmount?.visible()
                binding?.cvTotalAmount?.visible()
                val icon = ContextCompat.getDrawable(
                    context ?: return, R.drawable.promo_usage_ic_protection_check
                )
                binding?.buttonBuy?.setDrawable(icon)
                binding?.buttonBuy?.setOnClickListener {
                    renderLoadingDialog(true)
                    viewModel.onClickBuy(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes
                    )
                }
                binding?.buttonBuy?.visible()
                binding?.buttonBackToShipment?.gone()
            }

            PromoPageEntryPoint.CHECKOUT_PAGE -> {
                binding?.tpgTotalAmountLabel?.gone()
                binding?.tpgTotalAmount?.gone()
                binding?.buttonBuy?.gone()
                binding?.buttonBackToShipment?.text =
                    context?.getString(R.string.promo_voucher_back_to_shipment)
                binding?.buttonBackToShipment?.setOnClickListener {
                    viewModel.onClickBackToCheckout(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes
                    )
                }
                binding?.buttonBackToShipment?.visible()
            }
        }
        observeKeyboardVisibility()
    }

    private var dummyBackgroundMaxTranslationY = 0
    private var dummyAnchorPosition = 0
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
                        adjustDummyBackground(viewModel.getCurrentItems())
                    } else {
                        renderWhiteHeader()
                        val bgTranslationY = dummyBackgroundMaxTranslationY - scrollY
                        if (bgTranslationY >= BOTTOM_SHEET_HEADER_HEIGHT_IN_DP.toPx()) {
                            binding?.dummyBackground?.translationY = bgTranslationY
                        } else {
                            binding?.dummyBackground?.translationY =
                                BOTTOM_SHEET_HEADER_HEIGHT_IN_DP.toPx().toFloat()
                        }
                    }
                } else {
                    renderTransparentHeader()
                    adjustDummyBackground(viewModel.getCurrentItems())
                }
            }
        })
    }

    private fun adjustDummyBackground(items: List<DelegateAdapterItem>) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(100L)
            val promoRecommendation = items.getRecommendationItem()
            if (promoRecommendation != null) {
                val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (firstVisibleItemPosition == 0) {
                    val promoRecommendationHeight = List(promoRecommendation.codes.size) { index ->
                        layoutManager.getChildAt(index + 1)?.height ?: 0
                    }.sum().plus(layoutManager.getChildAt(0)?.height ?: 0)
                    val translationY =
                        BOTTOM_SHEET_HEADER_HEIGHT_IN_DP.toPx() + promoRecommendationHeight
                    dummyBackgroundMaxTranslationY = translationY
                    binding?.dummyBackground?.translationY = translationY.toFloat()
                }
            } else {
                binding?.dummyBackground?.translationY =
                    BOTTOM_SHEET_HEADER_HEIGHT_IN_DP.toPx().toFloat()
            }
        }
    }

    private fun clearHeaderScrollListener() {
        binding?.rvPromo?.clearOnScrollListeners()
    }

    private fun renderWhiteHeader() {
        context?.let {
            binding?.clBottomSheetHeader?.background = BottomSheetUtil
                .generateBackgroundDrawableWithColor(
                    it,
                    unifyprinciplesR.color.Unify_Background
                )
        }
        binding?.tpgBottomSheetHeaderTitle
            ?.setTextColorCompat(unifyprinciplesR.color.Unify_NN950)
        context?.let {
            binding?.btnBottomSheetHeaderClose?.setImageDrawable(
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
    }

    private fun renderTransparentHeader() {
        binding?.clBottomSheetHeader?.background = null
        binding?.tpgBottomSheetHeaderTitle
            ?.setTextColorCompat(unifyprinciplesR.color.Unify_Static_White)
        context?.let {
            binding?.btnBottomSheetHeaderClose?.setImageDrawable(
                getIconUnifyDrawable(
                    context = it,
                    iconId = IconUnify.CLOSE,
                    assetColor = ContextCompat.getColor(
                        it,
                        unifyprinciplesR.color.Unify_Static_White
                    )
                )
            )
        }
    }

    private fun observeKeyboardVisibility() {
        activity?.let { activity ->
            ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView) { view: View, insets: WindowInsetsCompat ->
                val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                val keyboardHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                if (isKeyboardVisible) {
                    // isKeyboardVisible means user tap voucher code text field
                    setFocusOnPromoAttemptTextField(keyboardHeight)
                } else {
                    resetFocusOnPromoAttemptTextField()
                }
                return@setOnApplyWindowInsetsListener ViewCompat.onApplyWindowInsets(view, insets)
            }
        }
    }

    private fun setFocusOnPromoAttemptTextField(keyboardHeight: Int) {
        val itemCount = recyclerViewAdapter.itemCount
        binding?.rvPromo?.smoothScrollToPosition(itemCount)

        // Add padding to make voucher code text field displayed above keyboard
        binding?.rvPromo?.setPadding(0, 0, 0, keyboardHeight.toDp())
    }

    private fun resetFocusOnPromoAttemptTextField() {
        binding?.rvPromo?.setPadding(0, 0, 0, 0)
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
                    hidePromoRecommendationBackground()
                    hideSavingInfo()
                    renderError(false)
                    refreshBottomSheetHeight(state)
                }

                is PromoPageUiState.Success -> {
                    renderHeader(true)
                    submitItems(state.items)
                    renderLoadingDialog(false)
                    renderLoadingShimmer(false)
                    renderContent(true)
                    renderSavingInfo(state.savingInfo)
                    renderError(false)
                    renderTickerInfo(state.tickerInfo)
                    refreshBottomSheetHeight(state)
                    adjustDummyBackground(state.items)
                }

                is PromoPageUiState.Error -> {
                    renderHeader(true)
                    renderLoadingShimmer(false)
                    renderLoadingDialog(false)
                    renderContent(false)
                    renderError(true, state.exception)
                    hidePromoRecommendationBackground()
                    hideSavingInfo()
                    refreshBottomSheetHeight(state)
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
        binding?.ivPromoRecommendationBackground?.loadImage(promoRecommendation.backgroundUrl)
        binding?.ivPromoRecommendationBackground?.setBackgroundColor(Color.parseColor(promoRecommendation.backgroundColor))
        binding?.ivPromoRecommendationBackground?.visible()
        dummyAnchorPosition = if (promoRecommendation.codes.isNotEmpty()) {
            promoRecommendation.codes.size + 1
        } else {
            0
        }
    }

    private fun hidePromoRecommendationBackground() {
        binding?.ivPromoRecommendationBackground?.gone()
    }

    private fun renderTickerInfo(tickerInfo: PromoPageTickerInfo) {
        val hasTickerInfo = tickerInfo.message.isNotBlank() && tickerInfo.iconUrl.isNotBlank()
            && tickerInfo.backgroundUrl.isNotBlank()
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

    private fun refreshBottomSheetHeight(state: PromoPageUiState) {
        binding?.root?.addOneTimeGlobalLayoutListener {
            val contentHeight = when (state) {
                is PromoPageUiState.Initial -> {
                    maxPeekHeight
                }

                is PromoPageUiState.Error -> {
                    binding?.layoutGlobalError?.root?.height ?: 0
                }

                is PromoPageUiState.Success -> {
                    binding?.root?.height ?: 0
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
            val frameDialogView = binding?.rlBottomSheetWrapper?.parent as FrameLayout
            frameDialogView.layoutParams.height = currentPeekHeight
            val bottomSheetBehavior = BottomSheetBehavior.from(frameDialogView)
            bottomSheetBehavior.peekHeight = currentPeekHeight
        }
    }

    private fun submitItems(items: List<DelegateAdapterItem>) {
        recyclerViewAdapter.submit(items)
    }

    private fun renderLoadingShimmer(isVisible: Boolean) {
        if (isVisible) {
            binding?.layoutShimmer?.iuCloseIcon?.setOnClickListener {
                dismiss()
            }
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
        binding?.clBottomSheetHeader?.isVisible = isVisible
    }

    private fun renderContent(isVisible: Boolean) {
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
        binding?.rvPromo?.isVisible = isVisible
        binding?.cvTotalAmount?.isVisible = isVisible
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
            binding?.layoutGlobalError?.iuCloseIcon?.setOnClickListener {
                dismiss()
            }
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
                    dismiss()
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
                is ApplyPromoUiAction.SuccessWithApplyPromo -> {
                    renderLoadingDialog(false)
                    listener?.onApplyPromo(
                        entryPoint = uiAction.entryPoint,
                        validateUse = uiAction.validateUse,
                        lastValidateUsePromoRequest = uiAction.lastValidateUsePromoRequest
                    )
                    dismiss()
                }

                is ApplyPromoUiAction.SuccessNoAction -> {
                    renderLoadingDialog(false)
                    dismiss()
                }

                is ApplyPromoUiAction.Failed -> {
                    renderLoadingDialog(false)
                    if (uiAction.shouldReload) {
                        showToastMessage(uiAction.throwable)
                        resetView()
                    } else {
                        listener?.onApplyPromoFailed(uiAction.throwable)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun observeClosePromoPageUiAction() {
        viewModel.closePromoPageUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction) {
                is ClosePromoPageUiAction.SuccessWithApplyPromo -> {
                    renderLoadingDialog(false)
                    dismiss()
                    listener?.onClosePageWithApplyPromo(
                        entryPoint = uiAction.entryPoint,
                        validateUse = uiAction.validateUse,
                        lastValidateUsePromoRequest = uiAction.lastValidateUsePromoRequest
                    )
                }

                is ClosePromoPageUiAction.SuccessWithClearPromo -> {
                    renderLoadingDialog(false)
                    dismiss()
                    listener?.onClosePageWithClearPromo(
                        entryPoint = uiAction.entryPoint,
                        clearPromo = uiAction.clearPromo,
                        lastValidateUsePromoRequest = uiAction.lastValidateUsePromoRequest,
                        isFlowMvcLockToCourier = uiAction.isFlowMvcLockToCourier
                    )
                }

                is ClosePromoPageUiAction.SuccessNoAction -> {
                    renderLoadingDialog(false)
                    dismiss()
                    listener?.onClosePageWithNoAction()
                }

                is ClosePromoPageUiAction.Failed -> {
                    renderLoadingDialog(false)
                    dismiss()
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
                binding?.lottieAnimationView?.visible()
                binding?.lottieAnimationView?.setComposition(result)
                binding?.lottieAnimationView?.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {
                        // no-op
                    }

                    override fun onAnimationEnd(animator: Animator) {
                        binding?.lottieAnimationView?.gone()
                    }

                    override fun onAnimationCancel(animator: Animator) {
                        // no-op
                    }

                    override fun onAnimationRepeat(animator: Animator) {
                        // no-op
                    }
                })
                binding?.lottieAnimationView?.playAnimation()
            }
    }

    private val onClickPromoTnc: (PromoTncItem) -> Unit = { item ->
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
            onSuccess = {

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
        binding?.root?.context?.let { showToastMessage(throwable.getErrorMessage(it)) }
    }

    private fun showToastMessage(message: String) {
        binding?.root?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
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
            isFlowMvcLockToCourier: Boolean
        )

        fun onClosePageWithNoAction()

        fun onApplyPromo(
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
