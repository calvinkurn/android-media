package com.tokopedia.category.navbottomsheet.view

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView.OnGroupExpandListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.category.navbottomsheet.CategoryNavBottomViewModel
import com.tokopedia.category.navbottomsheet.R
import com.tokopedia.category.navbottomsheet.di.DaggerCategoryNavigationBottomSheetComponent
import com.tokopedia.category.navbottomsheet.model.CategoriesItem
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryLevelTwoExpandableAdapter
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryNavLevelOneAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_cat_nav.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CategoryNavBottomSheet(private val listener: CategorySelected, private val catId: String, private val shouldHideL1: Boolean = false) : BottomSheetUnify(), CategoryNavLevelOneAdapter.CategorySelectListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var categoryNavBottomViewModel: CategoryNavBottomViewModel
    private val categoryList = LinkedList<CategoriesItem>()
    private var selectedLevelOneID = "-1"
    private var selectedLevelOnePosition: Int = 0
    private var lastExpandedPosition = -1
    private var gtmProviderListener: GtmProviderListener? = null

    init {
        isDragable = true
        isHideable = true
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
        initView()
        categoryNavBottomViewModel = ViewModelProvider(this, viewModelFactory).get(CategoryNavBottomViewModel::class.java)
        categoryNavBottomViewModel.getCategoriesFromServer(catId)

        categoryNavBottomViewModel.getCategoryList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    categoryList.clear()
                    categoryList.addAll(it.data.categories as ArrayList<CategoriesItem>)
                    if (selectedLevelOneID != "-1") {
                        if (shouldHideL1)
                            selectedLevelOnePosition = getPositionFromCategoryId(categoryList, selectedLevelOneID)
                        else
                            moveSelectedCatToFirst(categoryList, getPositionFromCategoryId(categoryList, selectedLevelOneID))
                    }
                    categoryList[selectedLevelOnePosition].isSelected = true
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

    private fun initiateLevelTwoView(selectedPosition: Int) {
        if (slave_list.expandableListAdapter == null) {
            val adapter = CategoryLevelTwoExpandableAdapter(categoryList[selectedPosition].child)
            slave_list.setAdapter(adapter)
        } else {
            if (lastExpandedPosition != -1) {
                slave_list.collapseGroup(lastExpandedPosition)
                lastExpandedPosition = -1
            }
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).levelTwoList = categoryList[selectedPosition].child
            (slave_list.expandableListAdapter as CategoryLevelTwoExpandableAdapter).notifyDataSetChanged()
        }
    }

    private fun getPositionFromCategoryId(categoryList: LinkedList<CategoriesItem>, selectedLevelOneID: String): Int {
        for (i in 0 until categoryList.size) {
            categoryList[i].id?.let {
                if (it == selectedLevelOneID) {
                    return i
                }
            }
        }
        return 0
    }

    private fun moveSelectedCatToFirst(categoryList: LinkedList<CategoriesItem>, positionToMove: Int) {
        if (positionToMove != 0 && positionToMove < categoryList.size) {
            val item = categoryList.removeAt(positionToMove)
            categoryList.addFirst(item)
        }
    }

    private fun initView() {
        context?.let {
            setTitle(it.resources.getString(R.string.kategori_title))
            master_list.layoutManager = LinearLayoutManager(it)
        }
//        addShimmerItems(categoryList)
        if (shouldHideL1) {
            master_list.hide()
            separator_guideline.setGuidelinePercent(0.0f)
        } else {
            master_list.adapter = CategoryNavLevelOneAdapter(categoryList, this)
        }
        slave_list.setOnGroupExpandListener(OnGroupExpandListener { groupPosition ->
            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                slave_list.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = groupPosition
        })

        slave_list.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            if (childPosition == 0)
                categoryList[selectedLevelOnePosition].child?.get(groupPosition)?.id?.let {
                    listener.onCategorySelected(it, 2)
                }
            else
                categoryList[selectedLevelOnePosition].child?.get(groupPosition)?.child?.get(childPosition)?.id?.let {
                    listener.onCategorySelected(it, 3)
                }
            return@setOnChildClickListener true
        }
    }

    override fun onItemClicked(id: String, position: Int, categoryName: String) {
        if (selectedLevelOnePosition != position) {
            categoryList[selectedLevelOnePosition].isSelected = false
            categoryList[position].isSelected = true
            master_list.adapter?.notifyItemChanged(selectedLevelOnePosition)
            master_list.adapter?.notifyItemChanged(position)
            initiateLevelTwoView(position)
            selectedLevelOneID = id
            selectedLevelOnePosition = position
        }
    }

    interface CategorySelected {
        fun onCategorySelected(catId: String, depth: Int)
    }

    fun setGtmProviderListener(listener: GtmProviderListener) {
        this.gtmProviderListener = listener;
    }

    /**
     * Client interface to add gtm
     */
    interface GtmProviderListener {
        fun onBottomSheetOpen();
        fun onBottomSheetClosed();
        fun onL1Clicked(id: Int, name: String);
        fun onL2Clicked(id: Int, name: String);
        fun onL3Clicked(id: Int, name: String);
        fun onL2Expand(id: Int, name: String);
        fun onL2Collapsed(id: Int, name: String);
    }

}
