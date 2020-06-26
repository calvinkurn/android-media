package com.tokopedia.catalog.ui.fragment

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogDetailsAndSpecsPagerAdapter
import com.tokopedia.catalog.model.ProductCatalogResponse

class CatalogSpecsAndDetailBottomSheet : BottomSheetDialogFragment() {
    var list: ArrayList<Fragment> = ArrayList()

    companion object {
        const val DESCRIPTION = "DESCRIPTION"
        const val SPECIFICATION = "SPECIFICATION"
        fun newInstance(description: String, specifications: ArrayList<ProductCatalogResponse.ProductCatalogQuery.Data.Catalog.Specification>): CatalogSpecsAndDetailBottomSheet {
            return CatalogSpecsAndDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(DESCRIPTION, description)
                    putParcelableArrayList(SPECIFICATION, specifications)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottomsheet_catalog_specifications, container, false)
        var description: String? = null
        var specifications: ArrayList<ProductCatalogResponse.ProductCatalogQuery.Data.Catalog.Specification>? = null
        if(arguments!=null){
            description = arguments?.getString(DESCRIPTION)
            specifications = arguments?.getParcelableArrayList(SPECIFICATION)
        }
        val tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout_specs)
        val viewPager = view?.findViewById<ViewPager>(R.id.view_pager_specs)
        val closeButton = view?.findViewById<ImageView>(R.id.close_button)
        list.add(CatalogSpecsAndDetailFragment.newInstance(CatalogSpecsAndDetailFragment.SPECIFICATION_TYPE, description, specifications))
        list.add(CatalogSpecsAndDetailFragment.newInstance(CatalogSpecsAndDetailFragment.DESCRIPTION_TYPE, description, specifications))
        val adapter = CatalogDetailsAndSpecsPagerAdapter(childFragmentManager, context, list)
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)
        closeButton?.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet: FrameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return bottomSheetDialog
    }

}
