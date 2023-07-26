package com.tokopedia.promousage.view.bottomsheet

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.promousage.R
import com.tokopedia.promousage.data.DummyData
import com.tokopedia.promousage.databinding.PromoUsageBottomshetBinding
import com.tokopedia.promousage.di.DaggerPromoUsageComponent
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.PromoItem
import com.tokopedia.promousage.domain.entity.PromoCta
import com.tokopedia.promousage.domain.entity.PromoPageState
import com.tokopedia.promousage.domain.entity.list.PromoAccordionItem
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.extension.foregroundDrawable
import com.tokopedia.promousage.view.adapter.TermAndConditionDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherAccordionDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherCodeDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherRecommendationDelegateAdapter
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PromoUsageBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val BUNDLE_KEY_ENTRY_POINT = "entry_point"
        private const val BUNDLE_KEY_PROMO_REQUEST = "promo_request"
        private const val BUNDLE_KEY_CHOSEN_ADDRESS = "chosen_address"
        private const val BOTTOM_SHEET_MARGIN_TOP_IN_DP = 64

        @JvmStatic
        fun newInstance(
            entryPoint: PromoPageEntryPoint,
            promoRequest: PromoRequest,
            chosenAddress: ChosenAddress? = null
        ): PromoUsageBottomSheet {
            return PromoUsageBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_ENTRY_POINT, entryPoint)
                    putParcelable(BUNDLE_KEY_PROMO_REQUEST, promoRequest)
                    putParcelable(BUNDLE_KEY_CHOSEN_ADDRESS, chosenAddress)
                }
            }
        }
    }

    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(
                VoucherRecommendationDelegateAdapter(
                    onVoucherClick,
                    onButtonUseRecommendedVoucherClick
                )
            )
            .add(
                VoucherAccordionDelegateAdapter(
                    onVoucherAccordionClick,
                    onVoucherClick,
                    onViewAllVoucherCtaClick
                )
            )
            .add(TermAndConditionDelegateAdapter(onTermAndConditionHyperlinkClick))
            .add(VoucherCodeDelegateAdapter(onApplyVoucherCodeCtaClick))
            .build()
    }

    private var binding by autoClearedNullable<PromoUsageBottomshetBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[PromoUsageViewModel::class.java] }
    private val entryPoint by lazy {
        arguments?.getSerializable(BUNDLE_KEY_ENTRY_POINT) as? PromoPageEntryPoint ?: PromoPageEntryPoint.CART_PAGE
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupRecyclerView()
        observeBottomSheetContent()

        val promoRequest = arguments?.getParcelable<PromoRequest>(BUNDLE_KEY_PROMO_REQUEST)
        val chosenAddress = arguments?.getParcelable<ChosenAddress>(BUNDLE_KEY_CHOSEN_ADDRESS)
        viewModel.getPromoList(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress
        )
    }

    private fun applyBottomSheetMaxHeightRule() {
        val frameDialogView = binding?.bottomSheetWrapper?.parent as View
        frameDialogView.setBackgroundColor(Color.TRANSPARENT)

        frameDialogView.bringToFront()

        val screenHeight = getScreenHeight()
        val maxPeekHeight: Int = screenHeight - BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx()
        frameDialogView.layoutParams.height = maxPeekHeight

        val bottomSheet = BottomSheetBehavior.from(frameDialogView)
        bottomSheet.peekHeight = maxPeekHeight
    }

    private fun observeBottomSheetContent() {
        viewModel.promoPageState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PromoPageState.InitialShimmer -> {

                }

                is PromoPageState.Success -> {
                    showContent()
                    handleBottomCardViewAppearance(entryPoint)
                    showTotalSavingsSection(3, 40_000)
                    recyclerViewAdapter.submit(state.sections)
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

    private fun setupView() {
        binding?.run {
            val formattedTotalPrice = 15_000.splitByThousand()
            tpgTotalPrice.text = context?.getString(
                R.string.promo_voucher_placeholder_total_price,
                formattedTotalPrice
            )

            buttonBuy.setOnClickListener {
                viewModel.onButtonBuyClick(entryPoint)
                dismiss()
            }

            buttonBackToShipment.setOnClickListener {
                viewModel.onButtonBackToShipmentClick()
                dismiss()
            }
        }
    }

    private fun handleBottomCardViewAppearance(entryPoint: PromoPageEntryPoint) {
        binding?.run {
            cardViewTotalPrice.visible()

            tpgTotalPrice.isVisible =
                entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE
            tpgTotalPriceLabel.isVisible =
                entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE
            buttonBuy.isVisible =
                entryPoint == PromoPageEntryPoint.CART_PAGE || entryPoint == PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE

            buttonBackToShipment.isVisible = entryPoint == PromoPageEntryPoint.SHIPMENT_PAGE
        }

        when (entryPoint) {
            PromoPageEntryPoint.CART_PAGE -> {
                binding?.buttonBuy?.text = context?.getString(R.string.promo_voucher_buy)
            }

            PromoPageEntryPoint.ONE_CLICK_CHECKOUT_PAGE -> {
                binding?.buttonBuy?.text = context?.getString(R.string.promo_voucher_pay)
                val icon = ContextCompat.getDrawable(
                    context ?: return,
                    R.drawable.promo_usage_ic_protection_check
                )
                binding?.buttonBuy?.setDrawable(icon)
            }

            PromoPageEntryPoint.SHIPMENT_PAGE -> {
                binding?.buttonBackToShipment?.text =
                    context?.getString(R.string.promo_voucher_back_to_shipment)
            }
        }
    }

    private fun showPromoTncBottomSheet() {
        val tncBottomSheet = PromoUsageTncBottomSheet.newInstance(DummyData.tncPromoCodes)
        tncBottomSheet.show(childFragmentManager)
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context ?: return)
            adapter = recyclerViewAdapter
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) handleScrollDownEvent() else handleScrollUpEvent()
                }
            })
        }
    }

    private fun showTotalSavingsSection(selectedVoucherCount: Int, totalVoucherAmount: Long) {
        binding?.run {

            tpgTotalSavings.isVisible = selectedVoucherCount.isMoreThanZero()

            val formattedTotalVoucherAmount = totalVoucherAmount.splitByThousand()
            val text = if (selectedVoucherCount > 1) {
                context?.getString(
                    R.string.promo_voucher_placeholder_total_savings,
                    formattedTotalVoucherAmount
                )
            } else {
                context?.getString(
                    R.string.promo_voucher_placeholder_total_savings_multi_voucher,
                    formattedTotalVoucherAmount,
                    selectedVoucherCount
                )
            }

            tpgTotalSavings.text = MethodChecker.fromHtml(text)
        }
    }

    private fun showContent() {
        binding?.run {
            shimmer.root.gone()
            layoutBottomSheetOverlay.visible()
            layoutBottomSheetHeader.visible()
            recyclerView.visible()
            layoutTotalSavings.visible()
        }
    }

    private fun hideContent() {
        binding?.run {
            shimmer.root.gone()
            layoutBottomSheetOverlay.gone()
            layoutBottomSheetHeader.gone()
            recyclerView.gone()
            layoutTotalSavings.gone()
            binding?.cardViewTotalPrice?.gone()
        }
    }

    private fun handleScrollDownEvent() {
        binding?.run {
            layoutBottomSheetHeader.background = ContextCompat.getDrawable(
                layoutBottomSheetHeader.context ?: return,
                R.drawable.promo_usage_bg_bottomsheet_header_scrolled
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                layoutBottomSheetHeader.foreground = null
            }

            bottomSheetTitle.setTextColorCompat(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
            bottomSheetClose.background = ContextCompat.getDrawable(
                layoutBottomSheetHeader.context ?: return,
                R.drawable.promo_usage_ic_close_black
            )
        }
    }

    private fun handleScrollUpEvent() {
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

    private val onVoucherClick = { promo: PromoItem ->
        val isRegisterGoPayLaterCicil =
            promo.couponType.contains(PromoItem.COUPON_TYPE_GOPAY_LATER_CICIL)
                && promo.cta.type == PromoCta.TYPE_REGISTER_GOPAY_LATER_CICIL
        if (isRegisterGoPayLaterCicil) {
            // TODO: Handle GoPay Later Cicil Registration
        } else {
            // TODO: Handle regular promo selected
        }
    }

    private val onVoucherAccordionClick = { accordion: PromoAccordionItem ->
        viewModel.onClickVoucherAccordion(accordion)
    }

    private val onViewAllVoucherCtaClick = { accordion: PromoAccordionItem ->
        viewModel.onClickViewAllVoucher(accordion)
    }

    private val onButtonUseRecommendedVoucherClick = {

    }

    private val onApplyVoucherCodeCtaClick = {

    }

    private val onTermAndConditionHyperlinkClick = {
        showPromoTncBottomSheet()
    }
}
