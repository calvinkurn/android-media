package com.tokopedia.catalog.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogPreferredProductsBottomSheet : BottomSheetUnify() {

    private var catalogId: String = ""
    private var catalogUrl: String = ""

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
            catalogUrl = requireArguments().getString(ARG_EXTRA_CATALOG_URL, "")
        }
        if(savedInstanceState == null)
            childFragmentManager.beginTransaction().replace(R.id.frame_content,
                CatalogDetailProductListingFragment.newInstance(catalogId,catalogUrl)).commit()

    }

    fun setCatalogUrl(catalogUrl: String) {
        childFragmentManager.fragments.firstOrNull()?.let { topFragment ->
            if(topFragment is CatalogDetailProductListingFragment && catalogUrl.isNotEmpty()){
                topFragment.viewModel.catalogUrl = catalogUrl
            }
        }
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_URL = "ARG_EXTRA_CATALOG_URL"
        const val PREFFERED_PRODUCT_BOTTOMSHEET_TAG = "PREFFERED_PRODUCT_BOTTOMSHEET_TAG"

        fun newInstance(catalogId : String, catalogUrl : String?): CatalogPreferredProductsBottomSheet {
            return CatalogPreferredProductsBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXTRA_CATALOG_ID, catalogId)
                    putString(ARG_EXTRA_CATALOG_URL, catalogUrl)
                }
            }
        }
    }
}