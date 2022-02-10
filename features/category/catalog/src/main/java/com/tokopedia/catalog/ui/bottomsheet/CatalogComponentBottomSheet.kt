package com.tokopedia.catalog.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.ui.fragment.CatalogAllReviewFragment
import com.tokopedia.catalog.ui.fragment.CatalogProductComparisonFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogComponentBottomSheet : BottomSheetUnify() , CatalogComponentBottomSheetListener {

    private var catalogId: String = ""
    private var catalogDetailListener: CatalogDetailListener? = null
    private var catalogName = ""
    private var brand = ""
    private var categoryId = ""
    private var origin : Int  = 0

    init {
        isFullpage = true
        isDragable = false
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
        if (arguments != null) {
            catalogId = requireArguments().getString(ARG_EXTRA_CATALOG_ID, "")
            catalogName = requireArguments().getString(ARG_EXTRA_CATALOG_NAME, "")
            brand = requireArguments().getString(ARG_EXTRA_CATALOG_BRAND, "")
            categoryId = requireArguments().getString(ARG_EXTRA_CATALOG_CATEGORY_ID, "")
            origin = requireArguments().getInt(ARG_SHEET_ORIGIN,0)
        }
        setUpTitle()
        if(savedInstanceState == null) {
            when(origin){

                ORIGIN_ALL_REVIEWS -> {
                    childFragmentManager.beginTransaction().replace(R.id.frame_content,
                        CatalogAllReviewFragment.newInstance(catalogName, catalogId, catalogDetailListener)).commit()
                }

                ORIGIN_ULTIMATE_VERSION -> {
                    childFragmentManager.beginTransaction().replace(R.id.frame_content,
                        CatalogProductComparisonFragment.newInstance(catalogName, catalogId, brand,
                            categoryId, catalogDetailListener ,this)).commit()
                }
            }
        }
    }

    private fun setUpTitle() {
        when(origin){
            ORIGIN_ALL_REVIEWS -> setTitle(getString(com.tokopedia.catalog.R.string.catalog_ulasan_produk))
            ORIGIN_ULTIMATE_VERSION -> setTitle(getString(com.tokopedia.catalog.R.string.catalog_perbandingan_produk))
        }
    }

    override fun dismissCatalogComponentBottomSheet() {
        this.dismiss()
    }

    companion object {
        private const val ARG_EXTRA_CATALOG_ID = "ARG_EXTRA_CATALOG_ID"
        private const val ARG_EXTRA_CATALOG_NAME = "ARG_EXTRA_CATALOG_NAME"
        private const val ARG_EXTRA_CATALOG_BRAND = "ARG_EXTRA_CATALOG_BRAND"
        private const val ARG_EXTRA_CATALOG_CATEGORY_ID = "ARG_EXTRA_CATALOG_CATEGORY_ID"

        private const val ARG_SHEET_ORIGIN = "ARG_SHEET_ORIGIN"
        const val ORIGIN_ALL_REVIEWS = 0
        const val ORIGIN_ULTIMATE_VERSION = 1

        fun newInstance(catalogName : String, catalogId : String, brand : String, categoryId : String,
                        origin : Int ,listener: CatalogDetailListener?): CatalogComponentBottomSheet {
            return CatalogComponentBottomSheet().apply {
                catalogDetailListener = listener
                arguments = Bundle().apply {
                    putString(ARG_EXTRA_CATALOG_ID, catalogId)
                    putString(ARG_EXTRA_CATALOG_NAME, catalogName)
                    putString(ARG_EXTRA_CATALOG_BRAND, brand)
                    putString(ARG_EXTRA_CATALOG_CATEGORY_ID, categoryId)
                    putInt(ARG_SHEET_ORIGIN,origin)
                }
            }
        }
    }
}

interface CatalogComponentBottomSheetListener {

    fun dismissCatalogComponentBottomSheet(){}
}
