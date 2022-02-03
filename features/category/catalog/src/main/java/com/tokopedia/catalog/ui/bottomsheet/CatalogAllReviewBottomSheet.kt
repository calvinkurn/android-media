package com.tokopedia.catalog.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.ui.fragment.CatalogAllReviewFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogAllReviewBottomSheet : BottomSheetUnify() {

    private var catalogId: String = ""
    private var catalogDetailListener: CatalogDetailListener? = null
    private var catalogName = ""

    init {
        isFullpage = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        isSkipCollapseState = true
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
        setTitle(getString(com.tokopedia.catalog.R.string.catalog_ulasan_produk))
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
            catalogName = requireArguments().getString(ARG_EXTRA_CATALOG_NAME, "")
        }
        if(savedInstanceState == null) {
            childFragmentManager.beginTransaction().replace(R.id.frame_content,
                    CatalogAllReviewFragment.newInstance(catalogName, catalogId, catalogDetailListener)).commit()
        }
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_NAME = "ARG_EXTRA_CATALOG_NAME"

        fun newInstance(catalogName : String, catalogId : String, listener: CatalogDetailListener?): CatalogAllReviewBottomSheet {
            return CatalogAllReviewBottomSheet().apply {
                catalogDetailListener = listener
                arguments = Bundle().apply {
                    putString(ARG_EXTRA_CATALOG_ID, catalogId)
                    putString(ARG_EXTRA_CATALOG_NAME, catalogName)
                }
            }
        }
    }
}