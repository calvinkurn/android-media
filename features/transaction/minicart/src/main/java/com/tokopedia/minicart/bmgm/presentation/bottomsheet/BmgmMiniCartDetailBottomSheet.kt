package com.tokopedia.minicart.bmgm.presentation.bottomsheet

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.common.di.DaggerBmgmComponent
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartDetailAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.itemdecoration.BmgmMiniCartDetailItemDecoration
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.minicart.bmgm.presentation.viewmodel.BmgmMiniCartDetailViewModel
import com.tokopedia.minicart.databinding.BottomSheetBmgmMiniCartDetailBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailBottomSheet : BottomSheetUnify() {

    companion object {

        const val TAG = "BmgmCartBottomSheet"

        fun getInstance(fm: FragmentManager): BmgmMiniCartDetailBottomSheet {
            return (fm.findFragmentByTag(TAG) as? BmgmMiniCartDetailBottomSheet)
                ?: BmgmMiniCartDetailBottomSheet().apply {
                    showCloseIcon = false
                    showKnob = true
                    isDragable = true
                    clearContentPadding = true
                    isHideable = true
                    isSkipCollapseState = true
                }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var binding: BottomSheetBmgmMiniCartDetailBinding? = null
    private var footerBinding: ViewBmgmMiniCartSubTotalBinding? = null
    private val listAdapter = BmgmMiniCartDetailAdapter()
    private val viewModel by lazy {
        ViewModelProvider(
            this, viewModelFactory
        )[BmgmMiniCartDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBmgmMiniCartDetailBinding.inflate(inflater).apply {
            footerBinding = ViewBmgmMiniCartSubTotalBinding.bind(root)
            setChild(root)
            setTitle(getString(R.string.mini_cart_bmgm_bottom_sheet_title))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductList()
        observeCartListState()
    }

    private fun observeCartListState() {
        viewModel.setCheckListState.observe(viewLifecycleOwner) {
            when (it) {
                is BmgmState.Loading -> showLoadingButton()
                is BmgmState.Success, is BmgmState.Error -> openCartPage()
            }
        }
    }

    private fun openCartPage() {
        dismissLoadingButton()
        RouteManager.route(context, ApplinkConst.CART)
    }

    private fun showLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = true
    }

    private fun dismissLoadingButton() {
        footerBinding?.btnBmgmOpenCart?.isLoading = false
    }

    private fun setupProductList() {
        binding?.rvBmgmMiniCartBottomSheet?.run {
            if (itemDecorationCount == Int.ZERO) {
                addItemDecoration(BmgmMiniCartDetailItemDecoration())
            }
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }

        showProducts()
    }

    private fun showProducts() {
        val commonData = PersistentCacheManager.instance.get<BmgmCommonDataModel>(
            BmgmCommonDataModel.PARAM_KEY_BMGM_DATA, BmgmCommonDataModel::class.java, null
        )

        commonData?.let { data ->
            val productList = getDiscountedProductList(data.tiersApplied)
            listAdapter.clearAllElements()
            listAdapter.addElement(productList)

            setupCartEntryPoint(data)
        }
    }

    private fun setupCartEntryPoint(model: BmgmCommonDataModel) {
        if (!model.showMiniCartFooter) {
            binding?.containerSubTotal?.gone()
            return
        }
        binding?.containerSubTotal?.visible()

        footerBinding?.run {
            tvBmgmFinalPrice.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(model.finalPrice, false)
            tvBmgmPriceBeforeDiscount.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(model.priceBeforeBenefit, false)
            tvBmgmPriceBeforeDiscount.paintFlags =
                tvBmgmPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            btnBmgmOpenCart.isEnabled = true
            btnBmgmOpenCart.setOnClickListener {
                viewModel.setCartListCheckboxState(getCartIds(model.tiersApplied))
            }
        }
    }

    private fun getCartIds(tiersApplied: List<BmgmCommonDataModel.TierModel>): List<String> {
        return tiersApplied.map { it.products }.flatten().map { it.cartId }
    }

    private fun getDiscountedProductList(tiers: List<BmgmCommonDataModel.TierModel>): List<MiniCartDetailUiModel> {
        val items = mutableListOf<MiniCartDetailUiModel>()
        tiers.forEach { tier ->
            val isDiscountedTier = !tier.isNonDiscountProducts()
            if (isDiscountedTier) {
                items.add(getDiscountSectionText(tier, isDiscountedTier))
                items.addAll(mapToProductList(tier.products, isDiscountedTier))
            } else {
                items.add(getDiscountSectionText(tier, isDiscountedTier))
                items.addAll(mapToProductList(tier.products, isDiscountedTier))
            }
        }
        return items.toList()
    }

    private fun mapToProductList(
        products: List<BmgmCommonDataModel.ProductModel>,
        isDiscountedTier: Boolean
    ): List<MiniCartDetailUiModel.Product> {
        return products.mapIndexed { i, product ->
            val isFirstItem = i == Int.ZERO
            val isLastItem = i == products.size.minus(Int.ONE)
            return@mapIndexed MiniCartDetailUiModel.Product(
                isDiscountedProduct = isDiscountedTier,
                product = product,
                showTopSpace = !isFirstItem,
                showBottomSpace = !isLastItem
            )
        }
    }

    private fun getDiscountSectionText(
        model: BmgmCommonDataModel.TierModel,
        isDiscountedTier: Boolean
    ): MiniCartDetailUiModel {
        return MiniCartDetailUiModel.Section(
            sectionText = model.tierMessage, isDiscountSection = isDiscountedTier
        )
    }

    private fun initInjector() {
        context?.let {
            DaggerBmgmComponent.builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
        }
    }
}