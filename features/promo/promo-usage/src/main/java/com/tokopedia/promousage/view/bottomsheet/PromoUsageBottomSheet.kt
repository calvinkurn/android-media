package com.tokopedia.promousage.view.bottomsheet

import android.animation.Animator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageBottomshetBinding
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoPageState
import com.tokopedia.promousage.domain.entity.PromoSavingInfo
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoAccordionViewAllItem
import com.tokopedia.promousage.domain.entity.list.PromoInputItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.domain.entity.list.PromoTncItem
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.extension.foregroundDrawable
import com.tokopedia.promousage.view.adapter.PromoAccordionHeaderDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionItemDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoAccordionViewAllDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoInputCodeDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoRecommendationDelegateAdapter
import com.tokopedia.promousage.view.adapter.PromoTncDelegateAdapter
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import javax.inject.Inject

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

    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(
                PromoRecommendationDelegateAdapter(
                    onClickPromoItem,
                    onClickPromoRecommendation
                )
            )
            .add(PromoAccordionHeaderDelegateAdapter(onClickPromoAccordionHeader))
            .add(PromoAccordionItemDelegateAdapter(onClickPromoItem))
            .add(PromoAccordionViewAllDelegateAdapter(onClickPromoAccordionViewAll))
            .add(PromoTncDelegateAdapter(onTermAndConditionHyperlinkClick))
            .add(PromoInputCodeDelegateAdapter(onApplyPromoInputCode))
            .build()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PromoUsageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PromoUsageViewModel::class.java]
    }

    private var binding by autoClearedNullable<PromoUsageBottomshetBinding>()
    private val entryPoint by lazy {
        arguments?.getSerializable(BUNDLE_KEY_ENTRY_POINT) as? PromoPageEntryPoint
            ?: PromoPageEntryPoint.CART_PAGE
    }
    private val registerGopayLaterCicilLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val promoCode = result.data?.getStringExtra(BUNDLE_KEY_PROMO_CODE)
            if (!promoCode.isNullOrBlank()) {
                val promoRequest =
                    arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST) ?: PromoRequest()
                val chosenAddress =
                    arguments?.getParcelable<ChosenAddress>(BUNDLE_KEY_CHOSEN_ADDRESS)
                viewModel.loadPromoListWithPreSelectedPromo(promoRequest, chosenAddress, promoCode)
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
    ): View? {
        binding = PromoUsageBottomshetBinding.inflate(inflater, container, false)

        binding?.bottomSheetTitle?.text = context?.getString(R.string.promo_voucher_promo)
        binding?.bottomSheetClose?.setOnClickListener { dismissAllowingStateLoss() }
        binding?.layoutBottomSheetHeader?.foregroundDrawable(R.drawable.promo_usage_bg_confetti)

        dialog?.setOnShowListener { applyBottomSheetMaxHeightRule() }

        return binding?.root
    }

    private fun applyBottomSheetMaxHeightRule() {
        val frameDialogView = binding?.bottomSheetWrapper?.parent as View
        frameDialogView.setBackgroundColor(Color.TRANSPARENT)

        frameDialogView.bringToFront()

        val screenHeight = getScreenHeight()
        val maxPeekHeight: Int = screenHeight - BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx()
        frameDialogView.layoutParams.height = maxPeekHeight

        val bottomSheetBehavior = BottomSheetBehavior.from(frameDialogView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.peekHeight = maxPeekHeight
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalAmount = arguments?.getDouble(BUNDLE_KEY_TOTAL_AMOUNT) ?: 0.0
        setupView(entryPoint, totalAmount)
        setupObservers()

        val promoRequest = arguments?.getParcelable(BUNDLE_KEY_PROMO_REQUEST) ?: PromoRequest()
        val chosenAddress = arguments?.getParcelable<ChosenAddress>(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.loadPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress
        )
    }

    private fun setupView(entryPoint: PromoPageEntryPoint, totalAmount: Double) {
        binding?.run {
            cardViewTotalPrice.visible()

            tpgTotalPrice.isVisible = entryPoint == PromoPageEntryPoint.SHIPMENT_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE
            tpgTotalPriceLabel.isVisible = entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE
            buttonBuy.isVisible = entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE

            buttonBackToShipment.isVisible = entryPoint == PromoPageEntryPoint.SHIPMENT_PAGE
        }


        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context ?: return)
            adapter = recyclerViewAdapter
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        useWhiteHeaderColor()
                    } else {
                        useGradientHeaderColor()
                    }
                }
            })
        }
        binding?.tpgTotalPrice?.text = context?.getString(
            R.string.promo_voucher_placeholder_total_price,
            totalAmount.splitByThousand()
        )
        when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> {
                binding?.buttonBuy?.text = context?.getString(R.string.promo_voucher_buy)
            }
            PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE -> {
                binding?.buttonBuy?.text = context?.getString(R.string.promo_voucher_pay)
                val icon = ContextCompat.getDrawable(
                    context ?: return, R.drawable.promo_usage_ic_protection_check
                )
                binding?.buttonBuy?.setDrawable(icon)
            }
            PromoPageEntryPoint.SHIPMENT_PAGE-> {
                binding?.buttonBackToShipment?.text = context?.getString(R.string.promo_voucher_back_to_shipment)
            }
        }
        binding?.buttonBuy?.setOnClickListener {
            viewModel.onClickBuyButton(entryPoint)
            dismiss()
        }
        binding?.buttonBackToShipment?.setOnClickListener {
            viewModel.onClickBackToCheckoutButton()
            dismiss()
        }
        observeKeyboardVisibility()
    }

    private fun useWhiteHeaderColor() {
        binding?.run {
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
            bottomSheetClose.background = closeIconDrawable
        }
    }

    private fun useGradientHeaderColor() {
        binding?.run {
            layoutBottomSheetHeader.background = ContextCompat.getDrawable(
                layoutBottomSheetHeader.context ?: return,
                R.drawable.promo_usage_bg_bottomsheet_header
            )
            layoutBottomSheetHeader.foregroundDrawable(R.drawable.promo_usage_bg_confetti)
            bottomSheetTitle.setTextColorCompat(R.color.promo_dms_white)
            bottomSheetClose.background = ContextCompat.getDrawable(
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
        binding?.recyclerView?.smoothScrollToPosition(itemCount)

        // Add padding to make voucher code text field displayed above keyboard
        binding?.recyclerView?.setPadding(0, 0, 0, keyboardHeight.toDp())
    }

    private fun resetFocusFromVoucherCodeTextField() {
        binding?.recyclerView?.setPadding(0, 0, 0, 0)
    }

    private fun setupObservers() {
        observePromoPageState()
        observeTncUpdate()
    }

    private fun observePromoPageState() {
        viewModel.promoPageState.observe(viewLifecycleOwner) { state ->
            when (state) {

                is PromoPageState.Success -> {
                    showContent()
                    renderSavingInfo(state.savingInfo)
                    recyclerViewAdapter.submit(state.items)
                }

                is PromoPageState.Error -> {
                    hideContent()
                }

                else -> {
                    // TODO: Handle other states
                }
            }
        }
    }

    private fun showContent() {
        binding?.run {
            shimmer.root.gone()
            layoutBottomSheetHeader.visible()
            recyclerView.visible()
            layoutSavingInfo.visible()
        }
    }

    private fun hideContent() {
        binding?.run {
            shimmer.root.gone()
            layoutBottomSheetOverlay.gone()
            layoutBottomSheetHeader.gone()
            recyclerView.gone()
            layoutSavingInfo.gone()
            binding?.cardViewTotalPrice?.gone()
        }
    }

    private fun renderSavingInfo(promoSavingInfo: PromoSavingInfo) {
        binding?.run {
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

    private fun observeTncUpdate() {
        viewModel.promoTncAction.observe(viewLifecycleOwner) { item ->
            showPromoTncBottomSheet(item)
        }
    }

    private fun showPromoTncBottomSheet(item: PromoTncItem) {
        PromoUsageTncBottomSheet.newInstance(item.selectedPromoCodes).also {
            it.show(childFragmentManager)
        }
    }

    private val onClickPromoRecommendation: (PromoRecommendationItem) -> Unit =
        { recommendationItem: PromoRecommendationItem ->
            showLottieConfettiAnimation(recommendationItem)
            viewModel.onButtonUseRecommendationVoucherClick()
        }

    private val onClickPromoItem = { clickedItem: PromoItem ->
        val isRegisterGoPayLaterCicil =
            clickedItem.couponType.contains(PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL)
                && clickedItem.cta.type == PromoCta.TYPE_REGISTER_GOPAY_LATER_CICIL
        if (isRegisterGoPayLaterCicil) {
            val appLink = clickedItem.cta.appLink
            if (appLink.isNotBlank()) {
                goToRegisterGoPayLaterCicilRegistration(appLink)
            }
        } else {
            viewModel.onClickPromo(clickedItem)
        }
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
            binding?.lottieAnimationView?.visible()
            binding?.lottieAnimationView?.setComposition(result)
            binding?.lottieAnimationView?.playAnimation()
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
        }
    }

    private val onTermAndConditionHyperlinkClick: () -> Unit = {

    }

    private val onApplyPromoInputCode: () -> Unit = {
        viewModel.onCtaUseVoucherCodeClick()
    }

//    private fun observeVouchers() {
//        viewModel.vouchers.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Success -> {
//                    handleBottomSheetHeaderBackground(result.data)
//                    showContent()
//                    handlePromoRecommendationInfo()
//                    handleTotalPriceSection(entryPoint)
//                    recyclerViewAdapter.submit(result.data)
//                }
//                is Fail -> {
//                    hideContent()
//                }
//            }
//        }
//    }
//
//    private fun observeValidatedVoucher() {
//        viewModel.validatedVoucher.observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Success -> {
//                    binding?.root?.hideKeyboard()
//
//                }
//                is Fail -> {
//                    //TODO display toaster if voucher code is not found / invalid
//                }
//            }
//        }
//    }
//
//
//
//    private fun handlePromoRecommendationInfo() {
//        val showRecommendationInfoOverlay = true //TODO Hardcoded for testing purpose. Replace with real values
//
//        binding?.layoutBottomSheetOverlay?.isVisible = showRecommendationInfoOverlay
//
//        if (!showRecommendationInfoOverlay) {
//
//            val layoutParams = binding?.layoutBottomSheetHeader?.layoutParams as? RelativeLayout.LayoutParams
//            layoutParams?.setMargins(0,0,0,0)
//
//            binding?.layoutBottomSheetHeader?.layoutParams = layoutParams
//            binding?.layoutBottomSheetHeader?.requestLayout()
//        }
//    }
//
//    private fun handleTotalPriceSection(entryPoint: EntryPoint) {

//
//    }
//
//    private fun showTermAndConditionBottomSheet() {
//
//    }
//
//
//
//    private fun handleBottomSheetHeaderBackground(items: List<DelegateAdapterItem>) {
//        val recommendationVoucherWidget = items.filterIsInstance<VoucherRecommendation>()
//        val recommendationVouchers = recommendationVoucherWidget.getOrNull(0)?.vouchers
//        val recommendationVoucherCount = recommendationVouchers?.size.orZero()
//        val hasRecommendationVoucher = recommendationVoucherCount.isMoreThanZero()
//
//        if (hasRecommendationVoucher) {
//            useGradientHeaderColor()
//            addRecyclerViewScrollListener()
//        } else {
//            useWhiteHeaderColor()
//        }
//    }
//
//    private fun addRecyclerViewScrollListener() {
//        binding?.recyclerView?.addOnScrollListener(object : OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy > 0) {
//                    //Scroll down
//                    useWhiteHeaderColor()
//                } else {
//                    //Scroll up
//                    useGradientHeaderColor()
//                }
//            }
//        })
//    }
//
//    private fun refreshTotalSavings(selectedVouchers: Set<Voucher>, totalBenefits: Long) {
//        val selectedVoucherCount = selectedVouchers.size
//        binding?.run {
//            cardViewTotalPrice.visible()
//            tpgTotalPrice.isVisible =
//                entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE
//            tpgTotalPriceLabel.isVisible =
//                entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE
//            buttonBuy.isVisible =
//                entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE
//            buttonBackToShipment.isVisible = entryPoint == PromoPageEntryPoint.SHIPMENT_PAGE
//        }
//        when (entryPoint) {
//

//
//    private fun refreshTotalPrice(originalTotalPrice: Long, totalBenefits: Long) {
//        val totalPrice = originalTotalPrice - totalBenefits
//        binding?.tpgTotalPrice?.text = context?.getString(
//            R.string.promo_voucher_placeholder_total_price,
//            totalPrice.splitByThousand()
//        )
//    }
//

//
//<<<<<<< HEAD
//
//    private val onHyperlinkClick = { appLink : String ->
//        //TODO Route to gopay later cicil page
//    }
//

//
//

//
//    private val onTermAndConditionHyperlinkClick = {
//        viewModel.onClickTnc()
//    }
//
//
//    private val onVoucherCodeClearIconClick = { viewModel.onVoucherCodeClearIconClick() }
//        private val onTermAndConditionHyperlinkClick = { showTermAndConditionBottomSheet() }
//
}
