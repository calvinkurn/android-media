package com.tokopedia.catalog.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.catalog.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogPrefferedProductsBottomSheet : BottomSheetUnify() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(getContentView())
    }

    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.bottomsheet_catalog_products, null)
    }
}