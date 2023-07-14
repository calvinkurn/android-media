package com.tokopedia.promousage.view.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.promousage.domain.entity.Voucher
import com.tokopedia.promousage.view.adapter.VoucherRecommendationAdapter
import com.tokopedia.promousage.view.adapter.VoucherSectionAdapter
import com.tokopedia.promousage.domain.entity.VoucherSource
import com.tokopedia.promousage.domain.entity.VoucherState
import com.tokopedia.promousage.domain.entity.VoucherType
import com.tokopedia.promousage.util.extension.applyPaddingToLastItem
import com.tokopedia.promousage.util.extension.setHyperlinkText
import com.tokopedia.promousage.view.custom.PromoBottomSheet
import com.tokopedia.promousage.view.viewmodel.PromoUsageViewModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class PromoUsageBottomSheet: PromoBottomSheet() {

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


    private var binding by autoClearedNullable<PromoUsageBottomshetBinding>()


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[PromoUsageViewModel::class.java] }
    private val voucherRecommendationAdapter = VoucherRecommendationAdapter()
    private val voucherSectionAdapter = VoucherSectionAdapter()
    private val entryPoint by lazy {
        arguments?.getSerializable(BUNDLE_KEY_ENTRY_POINT) as? EntryPoint ?: EntryPoint.CART_PAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PromoUsageBottomshetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeVouchers()
        observeRecommendationVouchers()
        viewModel.getVouchers()
    }

    private fun applyBottomSheetHeightMaxRule() {
        val screenHeight = getScreenHeight()
        val maxPeekHeight: Int = screenHeight - BOTTOM_SHEET_MARGIN_TOP_IN_DP.toPx()
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
        viewModel.sections.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    showContent()

                    handleBottomCardViewAppearance(entryPoint)

                    voucherSectionAdapter.submit(result.data)
                    showTotalSavingsSection(3, 40_000)
                    handleVoucherFound()
                }

                is Fail -> {
                    binding?.cardViewTotalPrice?.gone()
                    hideContent()
                }
            }
        }
    }

    private fun observeRecommendationVouchers() {
        viewModel.recommendationVouchers.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {

                }

                is Fail -> {

                }
            }
        }
    }

    private fun setupView() {

        binding?.run {
            tpgRecommendationTitle.text = "Kamu bisa hemat Rp30.000 dari 2 promo"
            tpgTnc.setHyperlinkText(
                fullText = context?.getString(R.string.promo_voucher_view_tnc).orEmpty(),
                hyperlinkSubstring = context?.getString(R.string.promo_voucher_tnc).orEmpty(),
                ignoreCase = true,
                onHyperlinkClick = { showTermAndConditionBottomSheet() }
            )

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
            btnRecommendationUseVoucher.setOnClickListener {
                viewModel.onButtonBackToShipmentClick()
                dismiss()
            }
        }

        setupVoucherRecommendationRecyclerView()
        setupVoucherSectionRecyclerView()
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

    private fun setupVoucherSectionRecyclerView() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context ?: return)
            adapter = voucherSectionAdapter
        }

        voucherSectionAdapter.setOnSectionClick { selectedItemPosition ->
            val allSections = voucherSectionAdapter.snapshot()
            val selectedVoucherSection = voucherSectionAdapter.getItemAtOrNull(selectedItemPosition) ?: return@setOnSectionClick
            viewModel.onClickChevron(allSections, selectedVoucherSection)
        }

        voucherSectionAdapter.setOnVoucherClick { selectedVoucher ->
            viewModel.onVoucherSelected(selectedVoucher)
        }

        voucherSectionAdapter.setOnViewAllVoucherClick { selectedItemPosition ->
            val allSections = voucherSectionAdapter.snapshot()
            val selectedVoucherSection = voucherSectionAdapter.getItemAtOrNull(selectedItemPosition) ?: return@setOnViewAllVoucherClick
            viewModel.onClickViewAllVoucher(allSections, selectedVoucherSection)
        }
    }

    private fun setupVoucherRecommendationRecyclerView() {
        binding?.recyclerViewVoucherRecommendation?.apply {
            layoutManager = LinearLayoutManager(context ?: return)
            adapter = voucherRecommendationAdapter
            applyPaddingToLastItem()
        }

        voucherRecommendationAdapter.setOnVoucherClick { selectedItemPosition ->
            val allVouchers = voucherRecommendationAdapter.snapshot()
            val selectedVoucher = voucherRecommendationAdapter.getItemAtOrNull(selectedItemPosition) ?: return@setOnVoucherClick
            viewModel.onVoucherSelected(selectedVoucher)
        }

        voucherRecommendationAdapter.submit(listOf(
            Voucher(
                300,
                100_000,
                "Cashback - Loading",
                "2 hari",
                "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
                VoucherType.CASHBACK,
                VoucherState.Normal,
                VoucherSource.Promo,
                true
            ),

            Voucher(
                400,
                100_000,
                "Cashback - Normal",
                "2 hari",
                "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
                "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
                VoucherType.CASHBACK,
                VoucherState.Normal,
                VoucherSource.Promo,
                true
            ),
        ))
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

    private fun handleVoucherFound() {
        val voucher = Voucher(
            100,
            100_000,
            "Cashback - Voucher Code User Input",
            "2 hari",
            "https://images.tokopedia.net/img/android/promo/ic_voucher_cashback/ic_voucher_cashback.png",
            "https://images.tokopedia.net/img/android/promo/bg_supergraphic_cashback/bg_supergraphic_cashback.png",
            VoucherType.CASHBACK,
            VoucherState.Selected,
            VoucherSource.UserInput("TOKOPEDIAXBCA"),
            false
        )

        binding?.run {
            userInputVoucherView.visible()
            userInputVoucherView.bind(voucher)
        }
    }

    private fun handleVoucherNotFound() {
        binding?.userInputVoucherView?.gone()
    }


}


interface Listener {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}
