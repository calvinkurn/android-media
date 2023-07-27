package com.tokopedia.promousage.view.bottomsheet

import android.content.Intent
import android.content.res.Configuration
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
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageBottomshetBinding
import com.tokopedia.promousage.di.DaggerPromoUsageComponent
import com.tokopedia.promousage.domain.entity.EntryPoint
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.domain.entity.list.VoucherAccordion
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.extension.foregroundDrawable
import com.tokopedia.promousage.view.adapter.TermAndConditionDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherAccordionDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherCodeDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherRecommendationDelegateAdapter
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PromoUsageBottomSheet: BottomSheetDialogFragment() {

    companion object {
        private const val BUNDLE_KEY_ENTRY_POINT = "entry_point"
        private const val BUNDLE_KEY_ORIGINAL_TOTAL_PRICE = "original_total_price"
        private const val BOTTOM_SHEET_MARGIN_TOP_IN_DP = 64

        @JvmStatic
        fun newInstance(entryPoint: EntryPoint, originalTotalPrice: Long): PromoUsageBottomSheet {
            return PromoUsageBottomSheet().apply {
                arguments = Bundle().apply {
                    putSerializable(BUNDLE_KEY_ENTRY_POINT, entryPoint)
                    putLong(BUNDLE_KEY_ORIGINAL_TOTAL_PRICE, originalTotalPrice)
                }
            }
        }
    }

    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(VoucherRecommendationDelegateAdapter(onVoucherClick, onButtonUseRecommendedVoucherClick))
            .add(VoucherAccordionDelegateAdapter(onVoucherAccordionClick, onVoucherClick, onHyperlinkClick, onViewAllVoucherCtaClick))
            .add(TermAndConditionDelegateAdapter(onTermAndConditionHyperlinkClick))
            .add(VoucherCodeDelegateAdapter(onApplyVoucherCodeCtaClick, onVoucherCodeClearIconClick))
            .build()
    }

    private var binding by autoClearedNullable<PromoUsageBottomshetBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[PromoUsageViewModel::class.java] }
    private val entryPoint by lazy {
        arguments?.getSerializable(BUNDLE_KEY_ENTRY_POINT) as? EntryPoint ?: EntryPoint.CART_PAGE
    }
    private val originalTotalPrice by lazy { arguments?.getLong(BUNDLE_KEY_ORIGINAL_TOTAL_PRICE).orZero() }

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
        setStyle(DialogFragment.STYLE_NORMAL, com.tokopedia.unifycomponents.R.style.UnifyBottomSheetOverlapStyle)
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
        observeVouchers()
        observeSelectedVouchers()
        observeValidatedVoucher()
        viewModel.getVouchers()
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

    private fun observeVouchers() {
        viewModel.vouchers.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showContent()
                    handleBottomCardViewAppearance(entryPoint)
                    recyclerViewAdapter.submit(result.data)
                }
                is Fail -> {
                    hideContent()
                }
            }
        }
    }

    private fun observeSelectedVouchers() {
        viewModel.selectedVouchers.observe(viewLifecycleOwner) { selectedVouchers ->
            val totalBenefits = selectedVouchers.sumOf { it.benefitAmount }
            refreshTotalSavings(selectedVouchers, totalBenefits)
            refreshTotalPrice(originalTotalPrice, totalBenefits)
        }
    }

    private fun observeValidatedVoucher() {
        viewModel.validatedVoucher.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {

                }
                is Fail -> {
                    //TODO display toaster if voucher code is not found / invalid
                }
            }
        }
    }

    private fun setupView() {
        binding?.run {
            tpgTotalPrice.text = originalTotalPrice.splitByThousand()
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

    private fun handleBottomCardViewAppearance(entryPoint: EntryPoint) {
        binding?.run {
            cardViewTotalPrice.visible()

            tpgTotalPrice.isVisible = entryPoint == EntryPoint.CART_PAGE || entryPoint == EntryPoint.ONE_CLICK_CHECKOUT_PAGE
            tpgTotalPriceLabel.isVisible = entryPoint == EntryPoint.CART_PAGE || entryPoint == EntryPoint.ONE_CLICK_CHECKOUT_PAGE
            buttonBuy.isVisible = entryPoint == EntryPoint.CART_PAGE || entryPoint == EntryPoint.ONE_CLICK_CHECKOUT_PAGE

            buttonBackToShipment.isVisible = entryPoint == EntryPoint.SHIPMENT_PAGE
        }

        when (entryPoint) {
            EntryPoint.CART_PAGE -> {
                binding?.buttonBuy?.text = context?.getString(R.string.promo_voucher_buy)
            }
            EntryPoint.ONE_CLICK_CHECKOUT_PAGE -> {
                binding?.buttonBuy?.text = context?.getString(R.string.promo_voucher_pay)
                val icon = ContextCompat.getDrawable(
                    context ?: return,
                    R.drawable.promo_usage_ic_protection_check
                )
                binding?.buttonBuy?.setDrawable(icon)
            }
            EntryPoint.SHIPMENT_PAGE -> {
                binding?.buttonBackToShipment?.text = context?.getString(R.string.promo_voucher_back_to_shipment)
            }
        }

    }

    private fun showTermAndConditionBottomSheet() {

    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context ?: return)
            adapter = recyclerViewAdapter
            addOnScrollListener(object : OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) handleScrollDownEvent() else handleScrollUpEvent()
                }
            })
        }
    }

    private fun refreshTotalSavings(selectedVouchers: Set<Voucher>, totalBenefits: Long) {
        val selectedVoucherCount = selectedVouchers.size

        binding?.run {

            layoutTotalSavings.isVisible = selectedVoucherCount.isMoreThanZero()

            val formattedTotalVoucherAmount = totalBenefits.splitByThousand()
            val text = if (selectedVoucherCount == 1) {
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

    private fun refreshTotalPrice(originalTotalPrice: Long, totalBenefits: Long) {
        val totalPrice = originalTotalPrice - totalBenefits
        binding?.tpgTotalPrice?.text = context?.getString(
            R.string.promo_voucher_placeholder_total_price,
            totalPrice.splitByThousand()
        )
    }

    private fun showContent() {
        binding?.run {
            shimmer.root.gone()
            layoutBottomSheetOverlay.visible()
            layoutBottomSheetHeader.visible()
            recyclerView.visible()
        }
    }

    private fun hideContent() {
        binding?.run {
            shimmer.root.gone()
            layoutBottomSheetOverlay.gone()
            layoutBottomSheetHeader.gone()
            recyclerView.gone()
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
            val closeIconDrawable = if (isDarkMode()) {
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

    private fun isDarkMode() : Boolean {
        val nightModeFlags = context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
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


    private val onVoucherClick = { selectedVoucher : Voucher ->
        viewModel.onVoucherClick(selectedVoucher)
    }

    private val onHyperlinkClick = { appLink : String ->
        //TODO Route to gopay later cicil page
    }

    private val onVoucherAccordionClick = { accordion : VoucherAccordion ->
        viewModel.onClickVoucherAccordion(accordion)
    }

    private val onViewAllVoucherCtaClick = { accordion : VoucherAccordion ->
        viewModel.onClickViewAllVoucher(accordion)
    }

    private val onButtonUseRecommendedVoucherClick = {

    }

    private val onApplyVoucherCodeCtaClick = { voucherCode : String ->
        viewModel.onCtaUseVoucherCodeClick(voucherCode)
    }

    private val onVoucherCodeClearIconClick = { viewModel.onVoucherCodeClearIconClick() }

    private val onTermAndConditionHyperlinkClick = { showTermAndConditionBottomSheet() }
}


interface Listener {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
