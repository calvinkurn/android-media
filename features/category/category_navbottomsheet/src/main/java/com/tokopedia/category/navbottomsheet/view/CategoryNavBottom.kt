package com.tokopedia.category.navbottomsheet.view

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.category.navbottomsheet.CategoryNavBottomViewModel
import com.tokopedia.category.navbottomsheet.R
import com.tokopedia.category.navbottomsheet.di.DaggerCategoryNavigationBottomSheetComponent
import com.tokopedia.category.navbottomsheet.model.CategoriesItem
import com.tokopedia.category.navbottomsheet.model.CategoryDetailData
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryLevelTwoExpandableAdapter
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryNavLevelOneAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_cat_nav.*
import java.util.*
import javax.inject.Inject


class CategoryNavBottomSheet(private val listener: CategorySelected, private val catId: String, private val shouldHideL1: Boolean = false) : BottomSheetUnify(), CategoryNavLevelOneAdapter.CategorySelectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var categoryNavBottomViewModel: CategoryNavBottomViewModel
    private val categoryList = LinkedList<CategoriesItem?>()
    private var selectedLevelOneID = "-1"
    private var selectedLevelTwoID = "-1"
    private var selectedLevelThreeID = "-1"
    private var selectedLevelOnePosition: Int = 0
    private var expandedLevelTwoPosition = -1

    init {
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
        val component = DaggerCategoryNavigationBottomSheetComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()
        component.inject(this)
        categoryNavBottomViewModel = ViewModelProvider(this, viewModelFactory).get(CategoryNavBottomViewModel::class.java)
        initView()
        categoryNavBottomViewModel.getCategoriesFromServer(catId)
        categoryNavBottomViewModel.getCategoryList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    it.data.categories?.let { list ->
                        categoryList.clear()
                        categoryList.addAll(list)
                    }
                    it.data.categoryDetailData?.let { categoryDetailData ->
                        setupRestIds(categoryDetailData)
                    }
                    if (selectedLevelOneID != "-1") {
                        if (shouldHideL1)
                            selectedLevelOnePosition = categoryNavBottomViewModel.getPositionFromCategoryId(categoryList, selectedLevelOneID)
                        else
                            categoryNavBottomViewModel.moveSelectedCatToFirst(categoryList, categoryNavBottomViewModel.getPositionFromCategoryId(categoryList, selectedLevelOneID))
                    }
                    categoryList[selectedLevelOnePosition]?.isSelected = true
                    master_list.adapter?.notifyDataSetChanged()
                    master_list.layoutManager?.scrollToPosition(selectedLevelOnePosition)
                    initiateLevelTwoView(selectedLevelOnePosition)
                }
                is Fail -> {
//                    TODO:: Failure Case Handling
//                    (activity as CategoryChangeListener).onError(it.throwable)
                }
            }

        })
    }

    private fun setupRestIds(categoryDetailData: CategoryDetailData) {
        if (0 != categoryDetailData.id) {
            if (0 != categoryDetailData.parent) {
                if (categoryDetailData.parent == categoryDetailData.rootId) {
//                    Given id is L2.
                    selectedLevelOneID = categoryDetailData.parent.toString()
                    selectedLevelTwoID = categoryDetailData.id.toString()
                    selectedLevelThreeID = "-1"
                } else {
//                    Given Id is L3
                    selectedLevelOneID = categoryDetailData.rootId.toString()
                    selectedLevelTwoID = categoryDetailData.parent.toString()
                    selectedLevelThreeID = categoryDetailData.id.toString()
                }
            } else {
                if (categoryDetailData.id == categoryDetailData.rootId) {
//                    Given id is L1
                    selectedLevelOneID = categoryDetailData.id.toString()
                    selectedLevelTwoID = "-1"
                    selectedLevelThreeID = "-1"
                }
            }
        }
    }

    private fun initiateLevelTwoView(selectedPosition: Int) {
        if (slave_list.expandableListAdapter == null) {
            val adapter = CategoryLevelTwoExpandableAdapter(categoryList[selectedPosition]?.child)
            slave_list.setAdapter(adapter)
            if (selectedLevelTwoID != "-1")
                expandedLevelTwoPosition = categoryNavBottomViewModel.getPositionFromL2L3CategoryId(categoryList[selectedPosition]?.child, selectedLevelTwoID).also { expandedLevelTwoPosition ->
                    if (expandedLevelTwoPosition != -1) {
                        adapter.selectedL3Position = if (selectedLevelThreeID != "-1")
                            categoryNavBottomViewModel.getPositionFromL2L3CategoryId(categoryList[selectedPosition]?.child?.get(expandedLevelTwoPosition)?.child, selectedLevelThreeID).let {
                                if (it != -1)
                                    it + 1
                                else
                                    it
                            }
                        else
                            0
                        slave_list.expandGroup(expandedLevelTwoPosition)
                    }
                }
        } else {
            if (expandedLevelTwoPosition != -1) {
                slave_list.collapseGroup(expandedLevelTwoPosition)
                expandedLevelTwoPosition = -1
            }
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).levelTwoList = categoryList[selectedPosition]?.child
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).selectedL3Position = -1
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).notifyDataSetChanged()
        }
    }

    private fun initView() {
        context?.let {
            setTitle(it.resources.getString(R.string.kategori_title))
            master_list.layoutManager = LinearLayoutManager(it)
        }
        categoryNavBottomViewModel.addShimmerItems(categoryList)
        if (shouldHideL1) {
            master_list.hide()
            separator_guideline.setGuidelinePercent(0.0f)
        } else {
            master_list.adapter = CategoryNavLevelOneAdapter(categoryList, this)
        }
        slave_list.setOnGroupExpandListener { groupPosition ->
            if (expandedLevelTwoPosition != -1 && groupPosition != expandedLevelTwoPosition) {
                slave_list.collapseGroup(expandedLevelTwoPosition)
            }
            expandedLevelTwoPosition = groupPosition
            selectedLevelTwoID = categoryList[selectedLevelOnePosition]?.child?.get(expandedLevelTwoPosition)?.id
                    ?: "-1"
            selectedLevelThreeID = "-1"
        }

        slave_list.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            (slave_list?.expandableListAdapter as CategoryLevelTwoExpandableAdapter).apply {
                selectedL3Position = childPosition
                notifyDataSetChanged()
            }
            Handler().postDelayed(Runnable {
                if (childPosition == 0)
                    categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.id?.let {
                        listener.onCategorySelected(it, categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.appLink, 2)
                    }
                else
                    categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.child?.get(childPosition - 1)?.id?.let {
                        listener.onCategorySelected(it, categoryList[selectedLevelOnePosition]?.child?.get(groupPosition)?.child?.get(childPosition - 1)?.appLink, 3)
                    }
                dismiss()
            },200)

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
            selectedLevelOneID = id
            selectedLevelOnePosition = position
        }
    }

    interface CategorySelected {
        fun onCategorySelected(catId: String, appLink: String?, depth: Int)
    }

}
