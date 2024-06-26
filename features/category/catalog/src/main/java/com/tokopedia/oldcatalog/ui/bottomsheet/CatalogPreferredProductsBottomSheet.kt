package com.tokopedia.oldcatalog.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogPreferredProductsBottomSheet : BottomSheetUnify() {

    private var catalogId: String = ""
    private var catalogName: String = ""
    private var catalogUrl: String = ""
    private var categoryId: String = ""
    private var catalogBrand: String = ""

    init {
        isDragable = true
        clearContentPadding = true
        showKnob = true
        showCloseIcon = false
        showHeader = false
        customPeekHeight = 0
        isFullpage = true
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(getContentView())
    }

    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.bottomsheet_catalog_products, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
            catalogName = requireArguments().getString(ARG_EXTRA_CATALOG_NAME, "")
            catalogUrl = requireArguments().getString(ARG_EXTRA_CATALOG_URL, "")
            categoryId = requireArguments().getString(ARG_EXTRA_CATALOG_CATEGORY_ID, "")
            catalogBrand = requireArguments().getString(ARG_EXTRA_CATALOG_BRAND, "")

        }
        if(savedInstanceState == null)
            childFragmentManager.beginTransaction().replace(R.id.frame_content,
                CatalogDetailProductListingFragment.newInstance(catalogId,catalogName,
                    catalogUrl,categoryId, catalogBrand)).commit()

    }

    fun setData(catalogName : String, catalogUrl: String,catalogId : String,categoryId : String,brand : String) {
        childFragmentManager.fragments.firstOrNull()?.let { topFragment ->
            if(topFragment is CatalogDetailProductListingFragment && catalogUrl.isNotEmpty()){
                topFragment.viewModel.catalogName = catalogName
                topFragment.viewModel.catalogUrl = catalogUrl
                topFragment.viewModel.catalogId = catalogId
                topFragment.viewModel.categoryId = categoryId
                topFragment.viewModel.brand = brand
            }
        }
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_NAME = "ARG_EXTRA_CATALOG_NAME"
        private const val ARG_EXTRA_CATALOG_URL = "ARG_EXTRA_CATALOG_URL"
        private const val ARG_EXTRA_CATALOG_CATEGORY_ID = "ARG_EXTRA_CATALOG_CATEGORY_ID"
        private const val ARG_EXTRA_CATALOG_BRAND = "ARG_EXTRA_CATALOG_BRAND"
        const val PREFFERED_PRODUCT_BOTTOMSHEET_TAG = "PREFFERED_PRODUCT_BOTTOMSHEET_TAG"

        fun newInstance(catalogName : String, catalogId : String, catalogUrl : String?,
                        categoryId : String?, catalogBrand : String?): CatalogPreferredProductsBottomSheet {
            return CatalogPreferredProductsBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXTRA_CATALOG_ID, catalogId)
                    putString(ARG_EXTRA_CATALOG_NAME, catalogName)
                    putString(ARG_EXTRA_CATALOG_URL, catalogUrl)
                    putString(ARG_EXTRA_CATALOG_CATEGORY_ID, categoryId)
                    putString(ARG_EXTRA_CATALOG_BRAND, catalogBrand)
                }
            }
        }
    }
}
