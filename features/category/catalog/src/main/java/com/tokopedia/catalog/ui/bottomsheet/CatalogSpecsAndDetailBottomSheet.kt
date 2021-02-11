package com.tokopedia.catalog.ui.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogDetailsAndSpecsPagerAdapter
import com.tokopedia.catalog.model.raw.SpecificationsComponentData
import com.tokopedia.catalog.ui.fragment.CatalogSpecsAndDetailFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.fragment_bottomsheet_catalog_specifications.*

class CatalogSpecsAndDetailBottomSheet : BottomSheetUnify() {

    var list: ArrayList<Fragment> = ArrayList()

    init {
        isFullpage = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        isSkipCollapseState = true
    }

    companion object {
        const val DESCRIPTION = "DESCRIPTION"
        const val SPECIFICATION = "SPECIFICATION"
        fun newInstance(description: String, specifications: ArrayList<SpecificationsComponentData>): CatalogSpecsAndDetailBottomSheet {
            return CatalogSpecsAndDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(DESCRIPTION, description)
                    putParcelableArrayList(SPECIFICATION, specifications)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(getContentView())
    }

    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.fragment_bottomsheet_catalog_specifications, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initViews()
        setCustomTabText(context,tab_layout_specs)
        view_pager_specs.setCurrentItem(1)
    }

    private fun initData(){
        var description: String? = null
        var specifications: ArrayList<SpecificationsComponentData>? = null
        if(arguments!=null){
            description = arguments?.getString(DESCRIPTION)
            specifications = arguments?.getParcelableArrayList(SPECIFICATION)
        }
        list.add(CatalogSpecsAndDetailFragment.newInstance(CatalogSpecsAndDetailFragment.DESCRIPTION_TYPE, description, specifications))
        list.add(CatalogSpecsAndDetailFragment.newInstance(CatalogSpecsAndDetailFragment.SPECIFICATION_TYPE, description, specifications))
    }

    private fun initViews() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout_specs)
        val viewPager = view?.findViewById<ViewPager2>(R.id.view_pager_specs)
        val closeButton = view?.findViewById<ImageView>(R.id.close_button)
        activity?.let {
            setTitle(it.resources.getString(R.string.catalog_detail_product))
            val adapter = CatalogDetailsAndSpecsPagerAdapter(it, context, list)
            viewPager?.adapter = adapter
            if(tabLayout != null && viewPager != null){
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->

                }.attach()
            }
            tabLayout?.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.position?.let { adapter.setUnSelectView(tabLayout,it) }
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { adapter.setOnSelectView(tabLayout,it) }
                }

            })
        }
        closeButton?.setOnClickListener {
            dismiss()
        }
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

    private fun setCustomTabText(context : Context?, tabLayout: TabLayout?){
        if(context != null && tabLayout != null){
            val tabOne = Typography(context)
            tabOne.apply {
                text = context.getString(R.string.catalog_description)
                setType(Typography.HEADING_5)
                gravity = Gravity.CENTER
                setTextColor(MethodChecker.getColor(context,R.color.catalog_N700_44))
            }

            val tabTwo = Typography(context)
            tabTwo.apply {
                text = context.getString(R.string.catalog_spesification)
                setType(Typography.HEADING_5)
                gravity = Gravity.CENTER
                setTextColor(MethodChecker.getColor(context,R.color.catalog_N700_44))
            }
            tabLayout.getTabAt(0)?.customView = tabOne
            tabLayout.getTabAt(1)?.customView = tabTwo
        }
    }

}
