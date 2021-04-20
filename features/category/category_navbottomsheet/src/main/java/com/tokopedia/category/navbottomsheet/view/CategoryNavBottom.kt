package com.tokopedia.category.navbottomsheet.view

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.category.navbottomsheet.R
import com.tokopedia.category.navbottomsheet.di.DaggerCategoryNavigationBottomSheetComponent
import com.tokopedia.category.navbottomsheet.model.CategoriesItem
import com.tokopedia.category.navbottomsheet.model.CategoryNavStateModel
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryLevelTwoExpandableAdapter
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryNavLevelOneAdapter
import com.tokopedia.category.navbottomsheet.viewmodel.CategoryNavBottomViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_cat_nav.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject


class CategoryNavBottomSheet : BottomSheetUnify(), CategoryNavLevelOneAdapter.CategorySelectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var categoryNavBottomViewModel: CategoryNavBottomViewModel
    private val categoryList = LinkedList<CategoriesItem?>()
    private var listener: CategorySelected? = null
    private var gtmListener: GtmProviderListener? = null
    private var catId: String = ""
    private var shouldHideL1: Boolean = false
    private val model = CategoryNavStateModel()
    private var selectedLevelOnePosition: Int = 0
    private var expandedLevelTwoPosition = NOTHING_SELECTED

    init {
        isFullpage = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        isFullpage = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(getContentView())
    }


    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.bottom_sheet_cat_nav, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gtmListener?.onBottomSheetOpen()
        val component = DaggerCategoryNavigationBottomSheetComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
        component.inject(this)
        initData()
        categoryNavBottomViewModel = ViewModelProvider(this, viewModelFactory).get(CategoryNavBottomViewModel::class.java)
        initView()
        getCategories()
    }

    private fun initData() {
        arguments?.let {
            catId = it.getString(CATEGORY_ID, "")
            shouldHideL1 = it.getBoolean(SHOULD_HIDE_L1, false)
        }
    }

    private fun getCategories() {
        categoryNavBottomViewModel.getCategoriesFromServer(catId)
        categoryNavBottomViewModel.getCategoryList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    it.data.categories?.let { list ->
                        categoryList.clear()
                        categoryList.addAll(list)
                    }
                    it.data.categoryDetailData?.let { categoryDetailData ->
                        categoryNavBottomViewModel.setupStateModel(categoryDetailData, model)
                    }
                    if (model.selectedLevelOneID.isNotBlank()) {
                        if (shouldHideL1)
                            selectedLevelOnePosition = categoryNavBottomViewModel.getPositionFromCategoryId(categoryList, model.selectedLevelOneID)
                        else
                            categoryNavBottomViewModel.moveSelectedCatToFirst(categoryList, categoryNavBottomViewModel.getPositionFromCategoryId(categoryList, model.selectedLevelOneID))
                    }
                    categoryList[selectedLevelOnePosition]?.isSelected = true
                    master_list.adapter?.notifyDataSetChanged()
                    master_list.layoutManager?.scrollToPosition(selectedLevelOnePosition)
                    initiateLevelTwoView(selectedLevelOnePosition, true)
                }
                is Fail -> {
                    onError(it.throwable)
                }
            }

        })
    }

    private fun onError(e: Throwable) {
        slave_list.hide()
        master_list.hide()

        if (e is UnknownHostException
                || e is SocketTimeoutException) {
            global_error.setType(GlobalError.NO_CONNECTION)
        } else {
            global_error.setType(GlobalError.SERVER_ERROR)
        }

        global_error.show()
        global_error.setOnClickListener {
            if (!shouldHideL1)
                master_list.show()
            slave_list.show()
            global_error.hide()
            categoryNavBottomViewModel.getCategoriesFromServer(catId)
        }
    }

    private fun initiateLevelTwoView(selectedPosition: Int, createNewAdapter: Boolean = false) {
        if (createNewAdapter) {
            val adapter = CategoryLevelTwoExpandableAdapter(categoryList[selectedPosition]?.child)
            slave_list.setAdapter(adapter)
            if (model.selectedLevelTwoID.isNotBlank())
                expandedLevelTwoPosition = categoryNavBottomViewModel.getPositionFromL2L3CategoryId(categoryList[selectedPosition]?.child, model.selectedLevelTwoID).also { expandedLevelTwoPosition ->
                    if (expandedLevelTwoPosition != NOTHING_SELECTED) {
                        adapter.selectedL3Position =
                                categoryNavBottomViewModel.getSelectedL3PositionWithSemua(categoryList[selectedPosition]?.child?.get(expandedLevelTwoPosition)?.child, model.selectedLevelThreeID)
                        slave_list.post {
                            slave_list.expandGroup(expandedLevelTwoPosition)
                            slave_list.setSelectedGroup(expandedLevelTwoPosition)
                        }
                    }
                }
        } else {
            if (expandedLevelTwoPosition != NOTHING_SELECTED) {
                val collapsePosition = expandedLevelTwoPosition
                expandedLevelTwoPosition = NOTHING_SELECTED
                slave_list.collapseGroup(collapsePosition)
            }
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).levelTwoList = categoryList[selectedPosition]?.child
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).selectedL3Position = NOTHING_SELECTED
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).notifyDataSetChanged()
        }
    }

    private fun initView() {
        context?.let {
            setTitle(it.resources.getString(R.string.nbs_kategori_title))
            master_list.layoutManager = LinearLayoutManager(it)
        }
        if (shouldHideL1) {
            master_list.hide()
            separator_guideline.setGuidelinePercent(0.0f)
        } else {
            categoryNavBottomViewModel.addShimmerItems(categoryList)
            master_list.adapter = CategoryNavLevelOneAdapter(categoryList, this)
        }
        val adapter = CategoryLevelTwoExpandableAdapter(categoryNavBottomViewModel.addShimmerItemsToL2())
        slave_list.setAdapter(adapter)
        setL2L3Listeners()
    }

    private fun setL2L3Listeners() {
        slave_list.setOnGroupExpandListener { groupPosition ->
            val newL2Expanded = (groupPosition != expandedLevelTwoPosition)
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).selectedL3Position =
                    if (model.selectedLevelTwoID == categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.id)
                        categoryNavBottomViewModel.getSelectedL3PositionWithSemua(categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.child, model.selectedLevelThreeID)
                    else
                        NOTHING_SELECTED
            if (expandedLevelTwoPosition != NOTHING_SELECTED && newL2Expanded) {
                val collapsePosition = expandedLevelTwoPosition
                expandedLevelTwoPosition = groupPosition
                slave_list.collapseGroup(collapsePosition)
            }
            expandedLevelTwoPosition = groupPosition
            if (newL2Expanded)
                gtmListener?.onL2Expanded(categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.id, categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.name)
        }

        slave_list.setOnGroupCollapseListener { groupPosition ->
            if (groupPosition == expandedLevelTwoPosition) {
                gtmListener?.onL2Collapsed(categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.id, categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.name)
                expandedLevelTwoPosition = NOTHING_SELECTED
            }
        }

        slave_list.setOnGroupClickListener { _, _, groupPosition, _ ->
            gtmListener?.onL2Clicked(categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.id, categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.name)
            return@setOnGroupClickListener false
        }

        slave_list.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            (slave_list?.expandableListAdapter as CategoryLevelTwoExpandableAdapter).apply {
                selectedL3Position = childPosition
                notifyDataSetChanged()
            }
            Handler().postDelayed({
                if (childPosition == 0)
                    categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.id?.let {
                        listener?.onCategorySelected(it,
                                categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.appLink,
                                2,
                                categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.name
                                        ?: "")
                    }
                else
                    categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.child?.get(childPosition - 1)?.id?.let {
                        listener?.onCategorySelected(it,
                                categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.child?.get(childPosition - 1)?.appLink,
                                3,
                                categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.child?.get(childPosition - 1)?.name
                                        ?: "")
                        gtmListener?.onL3Clicked(it, categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.child?.get(childPosition - 1)?.name)
                    }
                dismiss()
            }, DELAY_TO_SHOW_CHANGING_CHECKBOXES)

            return@setOnChildClickListener true
        }
    }

    override fun onItemClicked(id: String, position: Int, categoryName: String) {
        if (selectedLevelOnePosition != position) {
            categoryList[selectedLevelOnePosition]?.isSelected = false
            categoryList[position]?.isSelected = true
            master_list.adapter?.notifyItemChanged(selectedLevelOnePosition)
            master_list.adapter?.notifyItemChanged(position)
            initiateLevelTwoView(position)
            model.selectedLevelOneID = id
            selectedLevelOnePosition = position
            gtmListener?.onL1Clicked(id, categoryName)
        }
    }

    interface CategorySelected {
        fun onCategorySelected(catId: String, appLink: String?, depth: Int, catName : String)
    }

    /**
     * Client interface to add gtm
     */
    interface GtmProviderListener {
        fun onBottomSheetOpen() {}
        fun onBottomSheetClosed() {}
        fun onL1Clicked(id: String?, name: String?) {}
        fun onL2Clicked(id: String?, name: String?) {}
        fun onL3Clicked(id: String?, name: String?) {}
        fun onL2Expanded(id: String?, name: String?) {}
        fun onL2Collapsed(id: String?, name: String?) {}
    }

    companion object {
        const val CATEGORY_ID = "catID"
        const val SHOULD_HIDE_L1 = "shouldHideL1"
        const val NOTHING_SELECTED = -1
        const val DELAY_TO_SHOW_CHANGING_CHECKBOXES:Long = 200
        fun getInstance(selectedCatId: String, categoryListener: CategorySelected? = null, gtmProviderListener: GtmProviderListener? = null, shouldHideL1View: Boolean = false): CategoryNavBottomSheet {
            return CategoryNavBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_ID, selectedCatId)
                    putBoolean(SHOULD_HIDE_L1, shouldHideL1View)
                }
                listener = categoryListener
                gtmListener = gtmProviderListener
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        gtmListener?.onBottomSheetClosed()
        super.onDismiss(dialog)
    }

}
