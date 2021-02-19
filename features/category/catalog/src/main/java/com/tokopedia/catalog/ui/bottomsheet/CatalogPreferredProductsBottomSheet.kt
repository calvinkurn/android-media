package com.tokopedia.catalog.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.catalog.R
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogPreferredProductsBottomSheet : BottomSheetUnify() {

    init {
        isFullpage = true
        isDragable = true
        isHideable = false
        clearContentPadding = true
        isSkipCollapseState = false
        showKnob = true
        showCloseIcon = false
        showHeader = false
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_HALF_EXPANDED
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
        childFragmentManager.beginTransaction().replace(R.id.frame_content,
                CatalogDetailProductListingFragment.newInstance("52191","","","")).commit()

    }

    companion object {
        fun newInstance(): CatalogPreferredProductsBottomSheet {
            return CatalogPreferredProductsBottomSheet().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }
}