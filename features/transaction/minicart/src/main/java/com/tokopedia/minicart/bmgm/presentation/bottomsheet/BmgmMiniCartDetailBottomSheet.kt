package com.tokopedia.minicart.bmgm.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.minicart.R
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartDetailAdapter
import com.tokopedia.minicart.bmgm.presentation.model.MiniCartDetailUiModel
import com.tokopedia.minicart.databinding.BottomSheetBmgmMiniCartDetailBinding
import com.tokopedia.minicart.databinding.ViewBmgmMiniCartSubTotalBinding
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        val data = PersistentCacheManager.instance.get<BmgmCommonDataUiModel>(
            BmgmCommonDataUiModel.PARAM_KEY_BMGM_DATA,
            BmgmCommonDataUiModel::class.java,
            null
        )
        data?.let {
            val productList = getDiscountedProductList(data.bundledProducts).plus(
                getNormalProductList(data.nonBundledProducts)
            )
            listAdapter.clearAllElements()
            listAdapter.addElement(productList)

            setupCartEntryPoint(it)
        }
    }

    private fun setupCartEntryPoint(model: BmgmCommonDataUiModel) {
        binding?.run {
            if (!model.showCartFooter) {
                containerSubTotal.gone()
                return
            }
            containerSubTotal.visible()
            val miniCartSubTotal = ViewBmgmMiniCartSubTotalBinding.bind(root)
        }
    }

    private fun getDiscountedProductList(products: List<BmgmCommonDataUiModel.BundledProductUiModel>): List<MiniCartDetailUiModel> {
        val items = mutableListOf<MiniCartDetailUiModel>()
        products.forEach {
            items.add(getDiscountSectionText(it))
            items.addAll(mapToProductList(it.products))
        }
        return items.toList()
    }

    private fun getNormalProductList(products: List<BmgmCommonDataUiModel.SingleProductUiModel>): List<MiniCartDetailUiModel> {
        val items = mutableListOf<MiniCartDetailUiModel>()
        items.add(getNormalSectionText())
        products.forEach {
            items.add(
                MiniCartDetailUiModel.Product(isDiscountedProduct = false, product = it)
            )
        }
        return items.toList()
    }

    private fun mapToProductList(products: List<BmgmCommonDataUiModel.SingleProductUiModel>): List<MiniCartDetailUiModel.Product> {
        return products.map {
            MiniCartDetailUiModel.Product(isDiscountedProduct = true, product = it)
        }
    }

    private fun getDiscountSectionText(model: BmgmCommonDataUiModel.BundledProductUiModel): MiniCartDetailUiModel {
        return MiniCartDetailUiModel.Section(
            sectionText = getString(
                R.string.bmgm_mini_cart_detail_discount_section,
                model.tierDiscountStr
            ),
            isDiscountSection = true
        )
    }

    private fun getNormalSectionText(): MiniCartDetailUiModel {
        return MiniCartDetailUiModel.Section(
            sectionText = getString(R.string.bmgm_mini_cart_detail_normal_section),
            isDiscountSection = false
        )
    }
}