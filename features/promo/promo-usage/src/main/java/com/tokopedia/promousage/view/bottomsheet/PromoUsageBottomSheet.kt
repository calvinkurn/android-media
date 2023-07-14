package com.tokopedia.promousage.view.bottomsheet

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.splitByThousand
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageBottomshetBinding
import com.tokopedia.promousage.di.DaggerPromoUsageComponent
import com.tokopedia.promousage.domain.entity.EntryPoint
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.view.adapter.VoucherRecommendationDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherAccordionDelegateAdapter
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.view.adapter.TermAndConditionDelegateAdapter
import com.tokopedia.promousage.view.adapter.VoucherCodeDelegateAdapter
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PromoUsageBottomSheet: BottomSheetDialogFragment() {

    companion object {
        private const val BUNDLE_KEY_ENTRY_POINT = "entry_point"
        private const val BOTTOM_SHEET_MARGIN_TOP_IN_DP = 64

        @JvmStatic
        fun newInstance(entryPoint: EntryPoint): PromoUsageBottomSheet {
            return PromoUsageBottomSheet().apply {
                arguments = Bundle().apply {
                    putSerializable(BUNDLE_KEY_ENTRY_POINT, entryPoint)
                }
            }
        }
    }

    private val recyclerViewAdapter by lazy {
        CompositeAdapter.Builder()
            .add(VoucherRecommendationDelegateAdapter(onVoucherClick))
            .add(VoucherAccordionDelegateAdapter(onVoucherSectionClick, onVoucherClick, onViewAllVoucherClick))
            .add(TermAndConditionDelegateAdapter(onTermAndConditionHyperlinkClick))
            .add(VoucherCodeDelegateAdapter(onApplyVoucherCodeCtaClick))
            .build()
    }

    private var binding by autoClearedNullable<PromoUsageBottomshetBinding>()
    private lateinit var frameDialogView: View
    private var bottomSheet = BottomSheetBehavior<View>()
    private var isDragable: Boolean = false
    private var isHideable: Boolean = false
    private var isFullpage: Boolean = false
    private var customPeekHeight: Int = 200
    private var showKnob: Boolean = true
    private var showHeader: Boolean = true
    private var showCloseIcon: Boolean = true
    private var overlayClickDismiss: Boolean = true
    private var clearContentPadding: Boolean = false
    private var isKeyboardOverlap: Boolean = true
    private var isSkipCollapseState: Boolean = false
    private var displayMetrix = DisplayMetrics()
    private var whiteContainerBackground: Drawable? = null
    private var bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_COLLAPSED
    private var child: View? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[PromoUsageViewModel::class.java] }
    private val entryPoint by lazy {
        arguments?.getSerializable(BUNDLE_KEY_ENTRY_POINT) as? EntryPoint ?: EntryPoint.CART_PAGE
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

        dialog?.setOnShowListener {
            showListener(it)
        }


        (requireContext() as Activity).windowManager.defaultDisplay.getMetrics(displayMetrix)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeVouchers()
        viewModel.getVouchers()
    }

    private fun applyBottomSheetHeightMaxRule() {
        val screenHeight = getScreenHeight()
        val maxPeekHeight: Int = screenHeight - BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx()
    }

    private fun showListener(dialogInterface: DialogInterface?) {
        //dialog?.setCanceledOnTouchOutside(overlayClickDismiss)

        //bottomSheetClose.setOnClickListener(closeListener)

        //BottomSheetUnify.actionLayout(bottomSheetAction, actionText, actionIcon, actionListener)

        val frameDialogView = binding?.bottomSheetWrapper?.parent as View
        frameDialogView.setBackgroundColor(Color.TRANSPARENT)

        frameDialogView.bringToFront()

        bottomSheet = BottomSheetBehavior.from(frameDialogView)

        /**
         * set peekheight so user cant drag down the bottomsheet
         */
        if (isFullpage && !isDragable) {    // full page & not dragable
            frameDialogView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

            bottomSheet.peekHeight = displayMetrix.heightPixels
        } else if (!isFullpage && !isDragable) { // not full page & not dragable
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {

                }

                override fun onStateChanged(p0: View, p1: Int) {
                    /**
                     * Set peekheight here because need to get view height, view height can be get after rendered
                     * peek height obtained from wrapper parent height because parent background have another padding from 9patch
                     */
                    bottomSheet.peekHeight = (binding?.bottomSheetWrapper?.parent as View).height

                    if (isHideable && p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        } else { // dragable
            var mPeekHeight = customPeekHeight.toPx()
            if (isSkipCollapseState) {
                if (isFullpage) {
                    mPeekHeight = displayMetrix.heightPixels
                } else {
                    mPeekHeight = (binding?.bottomSheetWrapper?.parent as View).height
                }
            }
            bottomSheet.peekHeight = mPeekHeight
            bottomSheet.state = bottomSheetBehaviorDefaultState

            if (isFullpage) {
                frameDialogView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }

            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {

                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }

        bottomSheet.isHideable = isHideable
        bottomSheet.state = if (!isDragable || isFullpage) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_COLLAPSED
        }

        // ======================================

        //showDialogListener()
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

    private fun observeVouchers() {
        viewModel.items.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showContent()

                    handleBottomCardViewAppearance(entryPoint)

                    showTotalSavingsSection(3, 40_000)

                    recyclerViewAdapter.submit(result.data)
                }

                is Fail -> {
                    binding?.cardViewTotalPrice?.gone()
                    hideContent()
                }
            }
        }
    }


    private fun setupView() {
        setupRecyclerView()

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
            scrollView.visible()
            layoutTotalSavings.visible()
            promoShimmer.root.gone()
        }
    }

    private fun hideContent() {
        binding?.run {
            scrollView.gone()
            layoutTotalSavings.gone()
            promoShimmer.root.gone()
        }
    }



    private val onVoucherClick = { selectedVoucher : Voucher ->

    }

    private val onVoucherSectionClick = { index : Int ->

    }

    private val onViewAllVoucherClick = { index : Int ->

    }


    private val onButtonUseVoucherClick = {

    }

    private val onApplyVoucherCodeCtaClick = {

    }

    private val onTermAndConditionHyperlinkClick = { showTermAndConditionBottomSheet() }
}


interface Listener {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
