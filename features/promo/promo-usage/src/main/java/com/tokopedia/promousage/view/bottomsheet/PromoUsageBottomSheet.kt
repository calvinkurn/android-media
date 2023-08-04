package com.tokopedia.promousage.view.bottomsheet

import android.animation.Animator
import android.graphics.Color
import android.os.Build
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageBottomsheetBinding
import com.tokopedia.promousage.di.DaggerPromoUsageComponent
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoPageState
import com.tokopedia.promousage.domain.entity.PromoPageTickerInfo
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.composite.DelegateAdapterItem
import com.tokopedia.promousage.util.extension.foregroundDrawable
import com.tokopedia.promousage.view.adapter.PromoAccordionHeaderDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionItemDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionViewAllDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAttemptCodeDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoRecommendationDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoTncDelegateAdapter
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
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
        private const val BUNDLE_KEY_PROMO_CODE = "promo_code"

        private const val BOTTOM_SHEET_MARGIN_TOP_IN_DP = 64

        @JvmStatic
        fun newInstance(
            entryPoint: PromoPageEntryPoint,
            promoRequest: PromoRequest,
            totalAmount: Double,
            chosenAddress: ChosenAddress? = null
        ): PromoUsageBottomSheet {
            return PromoUsageBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_ENTRY_POINT, entryPoint)
                    putParcelable(BUNDLE_KEY_PROMO_REQUEST, promoRequest)
                    putParcelable(BUNDLE_KEY_CHOSEN_ADDRESS, chosenAddress)
                    putDouble(BUNDLE_KEY_TOTAL_AMOUNT, totalAmount)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PromoUsageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PromoUsageViewModel::class.java]
    }

    private var _binding by autoClearedNullable<PromoUsageBottomsheetBinding>()
    private val binding: PromoUsageBottomsheetBinding
        get() = _binding!!
    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(PromoRecommendationDelegateAdapter(onClickPromoItem, onClickApplyPromoRecommendation))
            .add(PromoAccordionHeaderDelegateAdapter(onClickPromoAccordionHeader))
            .add(PromoAccordionItemDelegateAdapter(onClickPromoItem))
            .add(PromoAccordionViewAllDelegateAdapter(onClickPromoAccordionViewAll))
            .add(PromoTncDelegateAdapter(onClickPromoTnc))
            .add(PromoAttemptCodeDelegateAdapter(onAttemptPromoCode))
            .build()
    }
    private var loaderDialog: LoaderDialog? = null

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

        binding.bottomSheetTitle.text = context?.getString(R.string.promo_voucher_promo)
        binding.buttonClose.setOnClickListener { dismissAllowingStateLoss() }
        binding.layoutBottomSheetHeader.foregroundDrawable(R.drawable.promo_usage_bg_confetti)

        dialog?.setOnShowListener { applyBottomSheetMaxHeightRule() }

        return binding.root
    }

    private fun applyBottomSheetMaxHeightRule() {
        val frameDialogView = binding.bottomSheetWrapper.parent as View
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

        val entryPoint =
            arguments?.getParcelable(BUNDLE_KEY_ENTRY_POINT) ?: PromoPageEntryPoint.CART_PAGE
        val totalAmount = arguments?.getDouble(BUNDLE_KEY_TOTAL_AMOUNT) ?: 0.0
        setupView(entryPoint, totalAmount)
        setupObservers()

        val promoRequest: PromoRequest? = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress: ChosenAddress? = arguments?.getParcelable(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress
        )
    }

    private fun setupView(entryPoint: PromoPageEntryPoint, totalAmount: Double) {
        binding.buttonClose.setOnClickListener {
            dismiss()
        }
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
                        onSuccess = {
                            dismiss()
                        }
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
                        onSuccess = {
                            dismiss()
                        }
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
                    viewModel.onClickBackToCheckout(
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
        binding.run {
            layoutBottomSheetHeader.background = ContextCompat.getDrawable(
                layoutBottomSheetHeader.context ?: return,
                R.drawable.promo_usage_bg_bottomsheet_header_scrolled
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layoutBottomSheetHeader.foreground = null
            }

            bottomSheetTitle.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            val isDarkMode = context?.isDarkMode() ?: false
            val closeIconDrawable = if (isDarkMode) {
                ContextCompat.getDrawable(
                    layoutBottomSheetHeader.context ?: return,
                    R.drawable.promo_usage_ic_close_white
                )
            } else {
                ContextCompat.getDrawable(
                    layoutBottomSheetHeader.context ?: return,
                    R.drawable.promo_usage_ic_close_black
                )
            }
            buttonClose.background = closeIconDrawable
        }
    }

    private fun useGradientHeaderColor() {
        binding.run {
            layoutBottomSheetHeader.background = ContextCompat.getDrawable(
                layoutBottomSheetHeader.context ?: return,
                R.drawable.promo_usage_bg_bottomsheet_header
            )
            layoutBottomSheetHeader.foregroundDrawable(R.drawable.promo_usage_bg_confetti)
            bottomSheetTitle.setTextColorCompat(R.color.promo_dms_white)
            buttonClose.background = ContextCompat.getDrawable(
                layoutBottomSheetHeader.context ?: return,
                R.drawable.promo_usage_ic_close_white
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
        observePromoRecommendation()
        observePromoPageState()
        observeCtaAction()
    }

    private fun observePromoRecommendation() {
        viewModel.promoRecommendation.observe(viewLifecycleOwner) { item ->
            val hasRecommendedItems = item != null && item.promos.isNotEmpty()
            if (hasRecommendedItems) {
                addHeaderScrollListener()
                useGradientHeaderColor()
            } else {
                useWhiteHeaderColor()
            }
        }
    }

    private fun observePromoPageState() {
        viewModel.promoPageState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is PromoPageState.Initial -> {
                    showShimmer()
                }

                is PromoPageState.Success -> {
                    updateTickerInfo(state.tickerInfo)
                    updateRecyclerView(state.items)
                    updateSavingInfo(state.savingInfo)
                    hideLoading()
                    showContent()
                }

                is PromoPageState.Error -> {
                    // TODO: Handle error state
                    hideLoading()
                    hideContent()
                }

                is PromoPageState.Loading -> {
                    showLoading()
                }

                else -> {
                    // TODO: Handle other states
                }
            }
        }
    }

    private fun updateTickerInfo(tickerInfo: PromoPageTickerInfo) {
        val hasTickerInfo = tickerInfo.message.isNotBlank() && tickerInfo.iconUrl.isNotBlank()
        //&& tickerInfo.backgroundUrl.isNotBlank()
        if (hasTickerInfo) {
            // TODO: Handle tickerInfo.backgroundUrl
            binding.iconTickerInfo.loadImage(tickerInfo.iconUrl)
            binding.tpgTickerInfoMessage.text = tickerInfo.message
            binding.layoutTickerInfo.isVisible = true
        } else {
            val layoutParams =
                binding.layoutBottomSheetHeader.layoutParams as? RelativeLayout.LayoutParams
            layoutParams?.setMargins(0, 0, 0, 0)
            binding.layoutBottomSheetHeader.layoutParams = layoutParams
            binding.layoutBottomSheetHeader.requestLayout()
            binding.layoutTickerInfo.isVisible = false
        }
    }

    private fun updateRecyclerView(items: List<DelegateAdapterItem>) {
        recyclerViewAdapter.submit(items)
    }

    private fun showShimmer() {
        binding.shimmer.root.visible()
        binding.rvPromo.gone()
        binding.cvTotalAmount.gone()
    }

    private fun showContent() {
        binding.shimmer.root.gone()
        binding.layoutBottomSheetHeader.visible()
        binding.rvPromo.visible()
        binding.cvTotalAmount.visible()
    }

    private fun hideContent() {
        binding.shimmer.root.gone()
        binding.layoutBottomSheetHeader.gone()
        binding.rvPromo.gone()
        binding.cvTotalAmount.gone()
    }

    private fun updateSavingInfo(promoSavingInfo: PromoSavingInfo) {
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

    private fun observeCtaAction() {
        viewModel.promoCtaAction.observe(viewLifecycleOwner) { item ->
            val ctaAppLink = item.appLink
            if (ctaAppLink.isNotBlank()) {
                goToRegisterGoPayLaterCicilRegistration(ctaAppLink)
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
            viewModel.onClickApplyPromoRecommendation(
                onSuccess = {
                    hideLoading()
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

    private fun showLoading() {
        if (loaderDialog == null) {
            loaderDialog = LoaderDialog(requireContext())
            loaderDialog?.show()
        }
    }

    private fun hideLoading() {
        if (loaderDialog != null) {
            loaderDialog?.dismiss()
            loaderDialog = null
        }
    }
}
