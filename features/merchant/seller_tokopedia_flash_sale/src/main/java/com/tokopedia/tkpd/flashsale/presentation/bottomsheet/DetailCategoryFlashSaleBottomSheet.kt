package com.tokopedia.tkpd.flashsale.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsBottomsheetDetailCategoryFlashSaleTkpdBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class DetailCategoryFlashSaleBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_DESC_CATEGORY = "desc_category"
        private const val BUNDLE_QUANTITY_PRODUCT = "quantity_product"
        private const val BUNDLE_QUANTITY_MAX_PRODUCT = "quantity_max_product"

        @JvmStatic
        fun newInstance(
            descCategory: String,
            quantityProduct: Int,
            maxQuantityProduct: Int
        ): DetailCategoryFlashSaleBottomSheet {
            val args = Bundle()
            args.putString(BUNDLE_DESC_CATEGORY, descCategory)
            args.putInt(BUNDLE_QUANTITY_PRODUCT, quantityProduct)
            args.putInt(BUNDLE_QUANTITY_MAX_PRODUCT, maxQuantityProduct)
            val bottomSheet =  DetailCategoryFlashSaleBottomSheet().apply {
                arguments = args
            }

            return bottomSheet
        }
    }

    private val descriptionCategory by lazy {
        arguments?.getString(BUNDLE_DESC_CATEGORY).orEmpty()
    }

    private val quantityProduct by lazy {
        arguments?.getInt(BUNDLE_QUANTITY_PRODUCT).orZero()
    }

    private val maxQuantityProduct by lazy {
        arguments?.getInt(BUNDLE_QUANTITY_MAX_PRODUCT).orZero()
    }

    private var binding by autoClearedNullable<StfsBottomsheetDetailCategoryFlashSaleTkpdBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding =
            StfsBottomsheetDetailCategoryFlashSaleTkpdBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.title_detail_category))
        binding?.tgpCategory?.text = descriptionCategory
        binding?.tgpQuantity?.text = getString(R.string.quantity_product, quantityProduct, maxQuantityProduct)
    }

}
