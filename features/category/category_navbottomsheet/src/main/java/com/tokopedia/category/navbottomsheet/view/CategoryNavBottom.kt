package com.tokopedia.category.navbottomsheet.view

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView.OnGroupExpandListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.category.navbottomsheet.CategoryNavViewModel
import com.tokopedia.category.navbottomsheet.R
import com.tokopedia.category.navbottomsheet.di.DaggerCategoryNavigationBottomSheetComponent
import com.tokopedia.category.navbottomsheet.model.CategoriesItem
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryLevelTwoExpandableAdapter
import com.tokopedia.category.navbottomsheet.view.adapter.CategoryNavLevelOneAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_cat_nav.*
import javax.inject.Inject


class CategoryNavBottomSheet : BottomSheetUnify(), CategoryNavLevelOneAdapter.CategorySelectListener {

    private var selectedLevelOnePosition: Int = 0
    private lateinit var categoryLevelOneAdapter: CategoryNavLevelOneAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var categoryNavViewModel: CategoryNavViewModel
    private val categoryList = ArrayList<CategoriesItem>()
    private var selectedLevelOneID = "-1";
    private var lastExpandedPosition = -1


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
        categoryNavViewModel = ViewModelProvider(this, viewModelFactory).get(CategoryNavViewModel::class.java)
        categoryNavViewModel.getCategoriesFromServer()

        categoryNavViewModel.getCategoryList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    categoryList.clear()
                    categoryList.addAll(it.data.categories as ArrayList<CategoriesItem>)
                    var selectedPosition = 0
                    if (selectedLevelOneID != "-1") {
                        selectedPosition = getPositionFromCategoryId(categoryList)
                        categoryList[selectedPosition].isSelected = true
                    } else {
                        categoryList[selectedPosition].isSelected = true
                    }
                    master_list.adapter?.notifyDataSetChanged()
                    master_list.layoutManager?.scrollToPosition(selectedPosition)

                    initiateLevelTwoView(selectedPosition)
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

    private fun getPositionFromCategoryId(categoryList: ArrayList<CategoriesItem>): Int {
        for (i in 0 until categoryList.size) {
            categoryList[i].id?.let {
                if (it == selectedLevelOneID) {
                    selectedLevelOnePosition = i
                    return i
                }
            }
        }
        return 0
    }

    private fun initView() {
        context?.let {
            setTitle(it.resources.getString(R.string.kategori_title))
            val linearLayoutManager = LinearLayoutManager(it)
            master_list.layoutManager = linearLayoutManager
        }
//        addShimmerItems(categoryList)
        categoryLevelOneAdapter = CategoryNavLevelOneAdapter(categoryList, this)
        master_list.adapter = categoryLevelOneAdapter
        slave_list.setOnGroupExpandListener(OnGroupExpandListener { groupPosition ->
            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                slave_list.collapseGroup(lastExpandedPosition)
            }
            lastExpandedPosition = groupPosition
        })
    }

    override fun onItemClicked(id: String, position: Int, categoryName: String, applink: String?) {
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
}
