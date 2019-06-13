package com.tokopedia.browse.categoryNavigation.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent

import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.adapters.CategoryLevelOneAdapter
import com.tokopedia.browse.categoryNavigation.data.model.category.CategoriesItem
import com.tokopedia.browse.categoryNavigation.di.CategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.di.DaggerCategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.view.CategoryChangeListener
import com.tokopedia.browse.categoryNavigation.viewmodel.CategoryLevelOneViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_categorylevel_one.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CategorylevelOneFragment : Fragment(), HasComponent<CategoryNavigationComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var categoryBrowseViewModel: CategoryLevelOneViewModel
    private val categoryList = ArrayList<CategoriesItem>()

    var selectedPosition = 0

    companion object {
        @JvmStatic
        fun newInstance() = CategorylevelOneFragment()
    }

    override fun getComponent(): CategoryNavigationComponent = DaggerCategoryNavigationComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_categorylevel_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        initView()
        setUpObserver()
    }

    private fun initView() {
        val linearLayoutManager = LinearLayoutManager(context)
        master_list.layoutManager = linearLayoutManager
        val categoryLevelOneAdapter = CategoryLevelOneAdapter(categoryList, activity!!.applicationContext, listener)
        master_list.adapter = categoryLevelOneAdapter

        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            categoryBrowseViewModel = viewModelProvider.get(CategoryLevelOneViewModel::class.java)
            categoryBrowseViewModel.bound()
        }
    }

    private fun setUpObserver() {
        categoryBrowseViewModel.getCategoryList().observe(this, Observer {

            when (it) {
                is Success -> {
                    categoryList.clear()
                    categoryList.addAll(it.data.categories as ArrayList<CategoriesItem>)
                    categoryList[0].isSelected = true
                    master_list.adapter.notifyDataSetChanged()
                    initiate()
                }
                is Fail -> {
                    (activity as CategoryChangeListener).onError()
                }
            }

        })
    }

    fun reloadData() {
        categoryBrowseViewModel.bound()
    }

    private fun initiate() {
        if (activity != null) {
            (activity as CategoryChangeListener).onCategoryChanged(categoryList[0].id!!, categoryList[0].name!!, categoryList[0].applinks)
        }
    }

    private var listener = object : CategorySelectListener {
        override fun onItemClicked(id: String, position: Int, categoryName: String, applink: String?) {
            if (position != selectedPosition) {
                categoryList[selectedPosition].isSelected = false
                categoryList[position].isSelected = true
                master_list.adapter.notifyItemChanged(position)
                master_list.adapter.notifyItemChanged(selectedPosition)
                selectedPosition = position
                if (activity != null)
                    (activity as CategoryChangeListener).onCategoryChanged(id, categoryName, applink)
            }
        }
    }


    interface CategorySelectListener {
        fun onItemClicked(id: String, position: Int, categoryName: String, applink: String?)
    }
}


