package com.tokopedia.catalog.ui.bottomsheet

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.CatalogDetailsAndSpecsPagerAdapter
import com.tokopedia.catalog.analytics.CatalogDetailAnalytics
import com.tokopedia.catalog.model.raw.FullSpecificationsComponentData
import com.tokopedia.catalog.ui.fragment.CatalogSpecsAndDetailFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_bottomsheet_catalog_specifications.*

class CatalogSpecsAndDetailBottomSheet : BottomSheetUnify() {

    var list: ArrayList<Fragment> = ArrayList()
    private var openPage : String? = DESCRIPTION
    var catalogId : String = ""

    init {
        isFullpage = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        isSkipCollapseState = true
    }

    companion object {
        const val ARG_CATALOG_ID = "ARG_CATALOG_ID"
        const val DESCRIPTION = "DESCRIPTION"
        const val SPECIFICATION = "SPECIFICATION"
        const val OPEN_PAGE = "OPEN_PAGE"
        const val PAGE_DESCRIPTION = 0
        const val PAGE_SPECIFICATIONS = 1
        fun newInstance(catalogId :String, description: String, specifications: ArrayList<FullSpecificationsComponentData>,
                        openPage : String): CatalogSpecsAndDetailBottomSheet {
            return CatalogSpecsAndDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATALOG_ID, catalogId)
                    putString(DESCRIPTION, description)
                    putParcelableArrayList(SPECIFICATION, specifications)
                    putString(OPEN_PAGE,openPage)
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
        if(openPage == SPECIFICATION)
            view_pager_specs.currentItem = PAGE_SPECIFICATIONS
        else
            view_pager_specs.currentItem = PAGE_DESCRIPTION
    }

    private fun initData(){
        var description: String? = null
        var specifications: ArrayList<FullSpecificationsComponentData>? = null
        if(arguments!=null){
            description = arguments?.getString(DESCRIPTION)
            specifications = arguments?.getParcelableArrayList(SPECIFICATION)
            openPage = arguments?.getString(OPEN_PAGE)
            catalogId = arguments?.getString(ARG_CATALOG_ID) ?: ""
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
            val session = UserSession(it)
            val adapter = CatalogDetailsAndSpecsPagerAdapter(it, context, list)
            viewPager?.adapter = adapter
            if(tabLayout != null && viewPager != null){
                TabLayoutMediator(tabLayout, viewPager) { _, _ ->

                }.attach()
            }
            tabLayout?.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.position?.let { position -> adapter.setUnSelectView(tabLayout,position) }
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { position ->
                        adapter.setOnSelectView(tabLayout,position)
                        if(position == PAGE_DESCRIPTION){
                            CatalogDetailAnalytics.sendEvent(
                                    CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                                    CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                                    CatalogDetailAnalytics.ActionKeys.CLICK_TAB_DESCRIPTION,
                                    catalogId,session.userId)
                            openPage = DESCRIPTION
                        } else if (position == PAGE_SPECIFICATIONS){
                            if(openPage != SPECIFICATION) {
                                CatalogDetailAnalytics.sendEvent(
                                        CatalogDetailAnalytics.EventKeys.EVENT_NAME_CATALOG_CLICK,
                                        CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                                        CatalogDetailAnalytics.ActionKeys.CLICK_TAB_SPECIFICATIONS,
                                        catalogId, session.userId)
                            }
                        }
                    }
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
                setTextColor(MethodChecker.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_G500))
            }

            val tabTwo = Typography(context)
            tabTwo.apply {
                text = context.getString(R.string.catalog_spesification)
                setType(Typography.HEADING_5)
                gravity = Gravity.CENTER
                setTextColor(MethodChecker.getColor(context,com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            }
            tabLayout.getTabAt(0)?.customView = tabOne
            tabLayout.getTabAt(1)?.customView = tabTwo
        }
    }

}
