package com.tokopedia.oldcatalog.ui.bottomsheet

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
import com.tokopedia.oldcatalog.adapter.CatalogDetailsAndSpecsPagerAdapter
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics
import com.tokopedia.oldcatalog.model.raw.FullSpecificationsComponentData
import com.tokopedia.oldcatalog.ui.fragment.CatalogSpecsAndDetailFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_bottomsheet_catalog_specifications.*
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.google.android.material.R as materialR

class CatalogSpecsAndDetailBottomSheet : BottomSheetUnify() {

    var list: ArrayList<Fragment> = ArrayList()
    private var openPage : String? = DESCRIPTION
    var catalogId : String = ""
    var catalogName : String = ""
    private var jumpToFullSpecIndex : Int = 0

    private var tabLayout : TabLayout? = null
    private var viewPager : ViewPager2? = null

    init {
        isFullpage = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        isSkipCollapseState = true
    }

    companion object {
        const val ARG_CATALOG_NAME = "ARG_CATALOG_NAME"
        const val ARG_CATALOG_ID = "ARG_CATALOG_ID"
        const val DESCRIPTION = "DESCRIPTION"
        const val SPECIFICATION = "SPECIFICATION"
        const val OPEN_PAGE = "OPEN_PAGE"
        private const val JUMP_TO_FULL_SPEC_INDEX = "JUMP_TO_FULL_SPEC_INDEX"
        const val PAGE_DESCRIPTION = 0
        const val PAGE_SPECIFICATIONS = 1
        const val FIRST_TAB = 0
        const val SECOND_TAB = 1
        fun newInstance(catalogName : String, catalogId :String, description: String, specifications: ArrayList<FullSpecificationsComponentData>,
                        openPage : String, jumpTo : Int = 0): CatalogSpecsAndDetailBottomSheet {
            return CatalogSpecsAndDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATALOG_NAME, catalogName)
                    putString(ARG_CATALOG_ID, catalogId)
                    putString(DESCRIPTION, description)
                    putParcelableArrayList(SPECIFICATION, specifications)
                    putString(OPEN_PAGE,openPage)
                    putInt(JUMP_TO_FULL_SPEC_INDEX , jumpTo)
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
            catalogName = arguments?.getString(ARG_CATALOG_NAME) ?: ""
            jumpToFullSpecIndex = arguments?.getInt(JUMP_TO_FULL_SPEC_INDEX) ?: 0
        }
        list.add(CatalogSpecsAndDetailFragment.newInstance(CatalogSpecsAndDetailFragment.DESCRIPTION_TYPE, description, specifications))
        list.add(CatalogSpecsAndDetailFragment.newInstance(CatalogSpecsAndDetailFragment.SPECIFICATION_TYPE, description, specifications,jumpToFullSpecIndex))
    }

    private fun initViews() {
        tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout_specs)
        viewPager = view?.findViewById<ViewPager2>(R.id.view_pager_specs)
        val closeButton = view?.findViewById<ImageView>(R.id.close_button)
        activity?.let {
            setTitle(it.resources.getString(R.string.catalog_detail_product))
            val session = UserSession(it)
            val adapter = CatalogDetailsAndSpecsPagerAdapter(it, context, list)
            viewPager?.adapter = adapter
            if(tabLayout != null && viewPager != null){
                TabLayoutMediator(tabLayout!!, viewPager!!) { _, _ ->

                }.attach()
            }
            tabLayout?.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.position?.let { position -> adapter.setUnSelectView(tab) }
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { position ->
                        adapter.setOnSelectView(tab)
                        if(position == PAGE_DESCRIPTION){
                            CatalogDetailAnalytics.sendEvent(
                                    CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                                    CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                                    CatalogDetailAnalytics.ActionKeys.CLICK_TAB_DESCRIPTION,
                                    "$catalogName - $catalogId",session.userId,catalogId)
                            openPage = DESCRIPTION
                        } else if (position == PAGE_SPECIFICATIONS){
                            if(openPage != SPECIFICATION) {
                                CatalogDetailAnalytics.sendEvent(
                                        CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                                        CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                                        CatalogDetailAnalytics.ActionKeys.CLICK_TAB_SPECIFICATIONS,
                                        "$catalogName - $catalogId", session.userId,catalogId)
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
            val bottomSheet: FrameLayout = bottomSheetDialog.findViewById(materialR.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return bottomSheetDialog
    }

    override fun onDestroyView() {
        viewPager?.adapter = null
        tabLayout?.addOnTabSelectedListener(null)
        viewPager = null
        tabLayout = null
        super.onDestroyView()
    }

    private fun setCustomTabText(context : Context?, tabLayout: TabLayout?){
        if(context != null && tabLayout != null){
            val tabOne = Typography(context)
            tabOne.apply {
                text = context.getString(R.string.catalog_description)
                setType(Typography.HEADING_5)
                gravity = Gravity.CENTER
                setTextColor(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_GN500))
            }

            val tabTwo = Typography(context)
            tabTwo.apply {
                text = context.getString(R.string.catalog_spesification)
                setType(Typography.HEADING_5)
                gravity = Gravity.CENTER
                setTextColor(MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN950_44))
            }
            tabLayout.getTabAt(FIRST_TAB)?.customView = tabOne
            tabLayout.getTabAt(SECOND_TAB)?.customView = tabTwo
        }
    }

}
