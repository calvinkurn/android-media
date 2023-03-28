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
    private var brandName: String = ""

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
            brandName = requireArguments().getString(ARG_EXTRA_BRAND_NAME, "")
        }
        setTitle("Kategori")
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().replace(
                R.id.frame_content,
                CatalogLihatSemuaPageFragment.newInstance(true, categoryId, brandId, brandName)
            ).commit()
        }

        setCloseClickListener {
            (parentFragment as? CatalogBrandLandingPageFragment)?.dismissKategoriBottomSheet()
            this.dismiss()
        }
    }

    override fun onChangeCategory(categoryName: String, categoryId: String) {
        super.onChangeCategory(categoryName, categoryId)
        (parentFragment as? CatalogBrandLandingPageFragment)?.onChangeCategory(categoryName, categoryId)
        this.dismiss()
    }

    companion object {
        private const val ARG_EXTRA_CATEGORY_ID = "ARG_EXTRA_CATEGORY_ID"
        private const val ARG_EXTRA_BRAND_ID = "ARG_EXTRA_BRAND_ID"
        private const val ARG_EXTRA_BRAND_NAME = "ARG_EXTRA_BRAND_NAME"
        fun newInstance(categoryId: String, brandId: String): CatalogLibraryComponentBottomSheet {
            return CatalogLibraryComponentBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXTRA_CATEGORY_ID, categoryId)
                    putString(ARG_EXTRA_BRAND_ID, brandId)
                    putString(ARG_EXTRA_BRAND_NAME, brandName)
                }
            }
        }
    }
}
