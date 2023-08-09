package com.tokopedia.promousage.view.bottomsheet

import android.animation.Animator
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.airbnb.lottie.LottieCompositionFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
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
import com.tokopedia.promousage.util.BottomSheetUtil
import com.tokopedia.promousage.util.analytics.PromoUsageAnalytics
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.logger.PromoErrorException
import com.tokopedia.promousage.view.adapter.PromoAccordionHeaderDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionItemDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionViewAllDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAttemptCodeDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoRecommendationDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoTncDelegateAdapter
import com.tokopedia.promousage.view.viewmodel.ApplyPromoUiAction
import com.tokopedia.promousage.view.viewmodel.PromoAttemptUiAction
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.promousage.view.viewmodel.getRecommendationItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
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

        private const val BOTTOM_SHEET_MARGIN_TOP_IN_DP = 64

        @JvmStatic
        fun newInstance(
            entryPoint: PromoPageEntryPoint,
            promoRequest: PromoRequest,
            validateUsePromoRequest: ValidateUsePromoRequest,
            boPromoCodes: List<String>,
            totalAmount: Double,
            chosenAddress: ChosenAddress? = null,
            listener: Listener? = null
        ): PromoUsageBottomSheet {
            return PromoUsageBottomSheet().apply {
                this.arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_ENTRY_POINT, entryPoint)
                    putParcelable(BUNDLE_KEY_PROMO_REQUEST, promoRequest)
                    putParcelable(BUNDLE_KEY_VALIDATE_USE, validateUsePromoRequest)
                    putStringArrayList(BUNDLE_KEY_BO_PROMO_CODES, ArrayList(boPromoCodes))
                    putParcelable(BUNDLE_KEY_CHOSEN_ADDRESS, chosenAddress)
                    putDouble(BUNDLE_KEY_TOTAL_AMOUNT, totalAmount)
                }
                this.listener = listener
            }
        }
    }

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
    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(
                PromoRecommendationDelegateAdapter(
                    onClickPromoItem,
                    onClickApplyPromoRecommendation
                )
            )
            .add(PromoAccordionHeaderDelegateAdapter(onClickPromoAccordionHeader))
            .add(PromoAccordionItemDelegateAdapter(onClickPromoItem))
            .add(PromoAccordionViewAllDelegateAdapter(onClickPromoAccordionViewAll))
            .add(PromoTncDelegateAdapter(onClickPromoTnc))
            .add(PromoAttemptCodeDelegateAdapter(onAttemptPromoCode))
            .build()
    }
    private var loaderDialog: LoaderDialog? = null
    private val defaultErrorMessage: String
        get() = binding.root.context.getString(R.string.promo_usage_global_error_promo)

    @Suppress("DEPRECATION")
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

        binding.tpgBottomSheetHeaderTitle.text = context?.getString(R.string.promo_voucher_promo)
        binding.btnBottomSheetHeaderClose.setOnClickListener { dismissAllowingStateLoss() }
//        binding.clBottomSheetHeader.foregroundDrawable(R.drawable.promo_usage_bg_confetti)

        dialog?.setOnShowListener { applyBottomSheetMaxHeightRule() }

        return binding.root
    }

    private fun applyBottomSheetMaxHeightRule() {
        val frameDialogView = binding.rlBottomSheetWrapper.parent as View
        frameDialogView.setBackgroundColor(Color.TRANSPARENT)

        frameDialogView.bringToFront()

        val screenHeight = getScreenHeight()
        val maxPeekHeight: Int = screenHeight - BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx()
        frameDialogView.layoutParams.height = maxPeekHeight

        val bottomSheetBehavior = BottomSheetBehavior.from(frameDialogView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.peekHeight = maxPeekHeight
    }

    @Suppress("DEPRECATION")
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

    private fun reloadUi() {
        setupView()
        val promoRequest: PromoRequest? = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.reloadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress
        )
    }

    private fun setupView() {
        hideContent()
        showShimmer()

        binding.clBottomSheetContent.background =
            BottomSheetUtil.generateBackgroundDrawableWithColor(binding.root.context)
        val entryPoint = arguments?.getParcelable(BUNDLE_KEY_ENTRY_POINT)
            ?: PromoPageEntryPoint.CART_PAGE
        val totalAmount = arguments?.getDouble(BUNDLE_KEY_TOTAL_AMOUNT) ?: 0.0
        val validateUsePromoRequest = arguments?.getParcelable(BUNDLE_KEY_VALIDATE_USE)
            ?: ValidateUsePromoRequest()
        val boPromoCodes: List<String> = arguments?.getStringArrayList(BUNDLE_KEY_BO_PROMO_CODES)
            ?: emptyList()

        binding.btnBottomSheetHeaderClose.setOnClickListener {
            dismiss()
        }
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
                    viewModel.onClickBuy(
                        entryPoint = entryPoint,
                        validateUsePromoRequest = validateUsePromoRequest,
                        boPromoCodes = boPromoCodes
                    )
                }
                binding.buttonBuy.visible()
                binding.buttonBackToShipment.gone()
            }

            PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE -> {
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
                        onSuccess = {
                            dismiss()
                        }
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
                    useWhiteHeaderColor()
                } else {
                    useGradientHeaderColor()
                }
            }
        })
    }

    private fun useWhiteHeaderColor() {
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

    private fun useGradientHeaderColor() {
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
        observePromoAttemptUiAction()
        observePromoCtaUiAction()
        observeApplyPromoUiAction()
    }

    private fun observePromoRecommendationUiAction() {
        viewModel.promoRecommendationUiAction.observe(viewLifecycleOwner) { uiAction ->
            val hasRecommendedItems = uiAction.promoRecommendationItem != null
                && uiAction.promoRecommendationItem.promos.isNotEmpty()
            if (hasRecommendedItems) {
                addHeaderScrollListener()
                useGradientHeaderColor()
            } else {
                useWhiteHeaderColor()
            }
        }
    }

    private fun observePromoAttemptUiAction() {
        viewModel.promoAttemptUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction.state) {
                PromoAttemptUiAction.State.SHOW_ERROR_TOAST -> {
                    if (uiAction.errorMessage.isNotBlank()) {
                        showToastMessage(uiAction.errorMessage)
                    }
                }

                else -> {

                }
            }
        }
    }

    private fun observePromoPageState() {
        viewModel.promoPageUiState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is PromoPageUiState.Initial -> {
                    showShimmer()
                }

                is PromoPageUiState.Success -> {
                    val promoRecommendation = state.items.getRecommendationItem()
                    if (promoRecommendation != null) {
                        renderPromoRecommendationBackground(promoRecommendation)
                    }
                    renderTickerInfo(state.tickerInfo)
                    renderRecyclerView(state.items)
                    renderSavingInfo(state.savingInfo)
                    setLoadingDialog(false)
                    showContent()
                }

                is PromoPageUiState.Error -> {
                    // TODO: Handle error state
                    setLoadingDialog(false)
                    hideContent()
                }

                is PromoPageUiState.Loading -> {
                    setLoadingDialog(true)
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
                .generateBackgroundDrawableWithColor(root.context, "#763BD7")
//            iuBottomSheetBackground
//                .setImageDrawable(
//                    ContextCompat.getDrawable(root.context, R.drawable.promo_usage_bg_bottomsheet_voucher_recommendation)
//                )
        }
    }

    private fun renderTickerInfo(tickerInfo: PromoPageTickerInfo) {
        with(binding) {
            // TODO: Remove hardcoded ticker info
            val hasTickerInfo = tickerInfo.message.isNotBlank() && tickerInfo.iconUrl.isNotBlank()
            //&& tickerInfo.backgroundUrl.isNotBlank()
            if (hasTickerInfo) {
                if (tickerInfo.backgroundUrl.isNotBlank()) {
                    iuTickerInfoBackground.setImageUrl(tickerInfo.backgroundUrl)
                } else {
                    iuTickerInfoBackground.setImageDrawable(
                        ContextCompat
                            .getDrawable(root.context, R.drawable.promo_usage_bg_ticker_info)
                    )
                }
                iuTickerInfoIcon.loadImage(tickerInfo.iconUrl)
                tpgTickerInfoMessage.text = tickerInfo.message
            } else {
                val layoutParams =
                    clBottomSheetHeader.layoutParams as? RelativeLayout.LayoutParams
                layoutParams?.setMargins(0, 0, 0, 0)
                clBottomSheetHeader.layoutParams = layoutParams
                clBottomSheetHeader.requestLayout()
            }
            clTickerInfo.isVisible = hasTickerInfo
        }
    }

    private fun renderRecyclerView(items: List<DelegateAdapterItem>) {
        recyclerViewAdapter.submit(items)
    }

    private fun showShimmer() {
        binding.layoutShimmer.root.visible()
        binding.rvPromo.gone()
        binding.cvTotalAmount.gone()
    }

    private fun showContent() {
        binding.layoutShimmer.root.gone()
        binding.clTickerInfo.visible()
        binding.clBottomSheetHeader.visible()
        binding.rvPromo.visible()
        binding.cvTotalAmount.visible()
    }

    private fun hideContent() {
        binding.layoutShimmer.root.gone()
        binding.clTickerInfo.gone()
        binding.clBottomSheetHeader.gone()
        binding.rvPromo.gone()
        binding.cvTotalAmount.gone()
    }

    private fun renderSavingInfo(promoSavingInfo: PromoSavingInfo) {
        binding.run {
            val formattedTotalVoucherAmount =
                promoSavingInfo.totalSelectedPromoBenefitAmount.splitByThousand()
            val text = if (promoSavingInfo.selectedPromoCount > 1) {
                context?.getString(
                    R.string.promo_voucher_placeholder_total_savings_multi_voucher,
                    formattedTotalVoucherAmount,
                    promoSavingInfo.selectedPromoCount
                )
            } else {
                context?.getString(
                    R.string.promo_voucher_placeholder_total_savings,
                    formattedTotalVoucherAmount
                )
            }
            tpgTotalSavings.text = MethodChecker.fromHtml(text)
            layoutSavingInfo.isVisible = promoSavingInfo.selectedPromoCount.isMoreThanZero()
        }
    }

    private fun observePromoCtaUiAction() {
        viewModel.promoCtaUiAction.observe(viewLifecycleOwner) { uiAction ->
            if (uiAction.promoCta != null) {
                val ctaAppLink = uiAction.promoCta.appLink
                if (ctaAppLink.isNotBlank()) {
                    goToRegisterGoPayLaterCicilRegistration(ctaAppLink)
                }
            }
        }
    }

    private fun observeApplyPromoUiAction() {
        viewModel.applyPromoUiAction.observe(viewLifecycleOwner) { uiAction ->
            when (uiAction.state) {

                ApplyPromoUiAction.State.SHOW_ERROR_TOAST_AND_RELOAD -> {
                    if (uiAction.throwable != null) {
                        showToastMessage(uiAction.throwable)
                    }
                    reloadUi()
                }

                ApplyPromoUiAction.State.SHOW_ERROR_TOAST -> {
                    if (uiAction.throwable != null) {
                        showToastMessage(uiAction.throwable)
                    }
                }

                else -> {

                }
            }
        }
    }

    private fun showPromoTncBottomSheet(item: PromoTncItem) {
        PromoUsageTncBottomSheet.newInstance(item.selectedPromoCodes).also {
            it.show(childFragmentManager)
        }
    }

    private val onClickApplyPromoRecommendation: (PromoRecommendationItem) -> Unit =
        { recommendationItem: PromoRecommendationItem ->
            viewModel.onApplyPromoRecommendation(
                onSuccess = {
                    setLoadingDialog(false)
                    showLottieConfettiAnimation(recommendationItem)
                }
            )
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

    private fun showLottieConfettiAnimation(item: PromoRecommendationItem) {
        LottieCompositionFactory.fromUrl(context, item.animationUrl).addListener { result ->
            binding.lottieAnimationView.visible()
            binding.lottieAnimationView.setComposition(result)
            binding.lottieAnimationView.playAnimation()
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
        }
    }

    private val onClickPromoTnc: (PromoTncItem) -> Unit = { item ->
        showPromoTncBottomSheet(item)
    }

    private val onAttemptPromoCode: (String) -> Unit = { attemptedPromoCode ->
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

    private fun setLoadingDialog(isLoading: Boolean) {
        if (isLoading) {
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
        showToastMessage(getErrorMessage(throwable))
    }

    private fun showToastMessage(message: String) {
        Toaster.build(binding.root, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
    }

    private fun getErrorMessage(throwable: Throwable): String {
        var errorMessage = throwable.message
        if (throwable !is PromoErrorException && throwable !is AkamaiErrorException) {
            errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        }
        if (errorMessage.isNullOrBlank()) {
            errorMessage = getString(R.string.promo_usage_global_error_promo)
        }
        return errorMessage
    }

    interface Listener {

    }
}
