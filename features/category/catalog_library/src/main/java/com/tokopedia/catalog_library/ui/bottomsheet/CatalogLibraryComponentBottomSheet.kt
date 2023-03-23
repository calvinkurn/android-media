package com.tokopedia.catalog_library.ui.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.ui.fragment.CatalogBrandLandingPageFragment
import com.tokopedia.catalog_library.ui.fragment.CatalogLihatSemuaPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class CatalogLibraryComponentBottomSheet : BottomSheetUnify(), CatalogLibraryListener {

    private var categoryId: String = ""
    private var brandId: String = ""

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
        return View.inflate(requireContext(), R.layout.catalog_component_bottomsheet, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            categoryId = requireArguments().getString(ARG_EXTRA_CATEGORY_ID, "")
            brandId = requireArguments().getString(ARG_EXTRA_BRAND_ID, "")
        }
        setTitle("Kategori")
        if(savedInstanceState == null) {
            childFragmentManager.beginTransaction().replace(R.id.frame_content,
                CatalogLihatSemuaPageFragment.newInstance(true,categoryId,brandId)).commit()
        }
    }

    private fun dismissCatalogComponentBottomSheet() {
        this.dismiss()
    }

    override fun onChangeCategory(categoryId: String) {
        super.onChangeCategory(categoryId)
        (parentFragment as? CatalogBrandLandingPageFragment)?.onChangeCategory(categoryId)
        dismissCatalogComponentBottomSheet()
    }

    companion object {
        private const val ARG_EXTRA_CATEGORY_ID = "ARG_EXTRA_CATEGORY_ID"
        private const val ARG_EXTRA_BRAND_ID = "ARG_EXTRA_BRAND_ID"
        fun newInstance(categoryId : String,brandId : String): CatalogLibraryComponentBottomSheet {
            return CatalogLibraryComponentBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXTRA_CATEGORY_ID, categoryId)
                    putString(ARG_EXTRA_BRAND_ID, brandId)
                }
            }
        }
    }
}
