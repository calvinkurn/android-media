package com.tokopedia.minicart.bmgm.presentation.bottomsheet

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartDetailAdapter
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.minicart.databinding.BottomSheetBmgmMiniCartDetailBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

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

    private var binding: BottomSheetBmgmMiniCartDetailBinding? = null
    private val listAdapter = BmgmMiniCartDetailAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBmgmMiniCartDetailBinding.inflate(inflater).apply {
            setChild(root)
            setTitle(getString(R.string.mini_cart_bmgm_bottom_sheet_title))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductList()
    }

    private fun setupProductList() {
        binding?.rvBmgmMiniCartBottomSheet?.run {
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
            val nonDiscountTier = data.tiersApplied.firstOrNull { it.isNonDiscountProducts() }
            val productList = getDiscountedProductList(data.tiersApplied).plus(
                getNormalProductList(nonDiscountTier)
            )
            listAdapter.clearAllElements()
            listAdapter.addElement(productList)

            setupCartEntryPoint(data)
        }
    }

    private fun setupCartEntryPoint(model: BmgmCommonDataModel) {
        binding?.let {
            if (!model.showMiniCartFooter) {
                it.containerSubTotal.gone()
                return
            }
            it.containerSubTotal.visible()
            val miniCartFooterView = ViewBmgmMiniCartSubTotalBinding.bind(it.root)
            with(miniCartFooterView) {
                tvBmgmFinalPrice.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(model.finalPrice, false)
                tvBmgmPriceBeforeDiscount.text =
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(model.priceBeforeBenefit, false)
                tvBmgmPriceBeforeDiscount.paintFlags =
                    tvBmgmPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    private fun getDiscountedProductList(products: List<BmgmCommonDataModel.TierModel>): List<MiniCartDetailUiModel> {
        val items = mutableListOf<MiniCartDetailUiModel>()
        products.forEach {
            items.add(getDiscountSectionText(it))
            items.addAll(mapToProductList(it.products))
        }
        return items.toList()
    }

    private fun getNormalProductList(tier: BmgmCommonDataModel.TierModel?): List<MiniCartDetailUiModel> {
        val products = tier?.products
        if (products.isNullOrEmpty()) return emptyList()

        val items = mutableListOf<MiniCartDetailUiModel>()
        items.add(
            MiniCartDetailUiModel.Section(
                sectionText = tier.tierMessage,
                isDiscountSection = false
            )
        )
        tier.products.forEach { product ->
            items.add(
                MiniCartDetailUiModel.Product(isDiscountedProduct = false, product = product)
            )
        }
        return emptyList()
    }

    private fun mapToProductList(products: List<BmgmCommonDataModel.ProductModel>): List<MiniCartDetailUiModel.Product> {
        return products.mapIndexed { i, product ->
            val isFirstItem = i == Int.ZERO
            val isLastItem = i == products.size.minus(Int.ONE)
            MiniCartDetailUiModel.Product(
                isDiscountedProduct = true,
                product = product,
                showTopSpace = !isFirstItem,
                showBottomSpace = !isLastItem
            )
        }
    }

    private fun getDiscountSectionText(model: BmgmCommonDataModel.TierModel): MiniCartDetailUiModel {
        return MiniCartDetailUiModel.Section(
            sectionText = getString(
                R.string.bmgm_mini_cart_detail_discount_section, model.tierDiscountStr
            ), isDiscountSection = true
        )
    }
}