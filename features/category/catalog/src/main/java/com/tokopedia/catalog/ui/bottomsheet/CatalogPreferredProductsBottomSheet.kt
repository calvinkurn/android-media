package com.tokopedia.catalog.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogPreferredProductsBottomSheet : BottomSheetUnify() {

    private var catalogId: String = ""

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
        }
        childFragmentManager.beginTransaction().replace(R.id.frame_content,
                CatalogDetailProductListingFragment.newInstance(catalogId)).commit()

    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"

        fun newInstance(catalogId : String): CatalogPreferredProductsBottomSheet {
            return CatalogPreferredProductsBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXTRA_CATALOG_ID, catalogId)
                }
            }
        }
    }
}