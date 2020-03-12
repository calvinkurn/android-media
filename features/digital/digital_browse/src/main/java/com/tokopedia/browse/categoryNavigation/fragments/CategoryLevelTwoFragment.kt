package com.tokopedia.browse.categoryNavigation.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager

import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.adapters.CategoryLevelTwoAdapter
import com.tokopedia.browse.categoryNavigation.data.model.newcategory.CategoryChildItem
import com.tokopedia.browse.categoryNavigation.di.CategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.di.DaggerCategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.utils.Constants
import com.tokopedia.browse.categoryNavigation.view.ActivityStateListener
import com.tokopedia.browse.categoryNavigation.viewmodel.CategoryLevelTwoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_category_level_two.*
import javax.inject.Inject

class CategoryLevelTwoFragment : Fragment(), Listener, HasComponent<CategoryNavigationComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var categoryLevelTwoViewModel: CategoryLevelTwoViewModel

    private lateinit var categoryLevelTwoAdapter: CategoryLevelTwoAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    private val childList = ArrayList<CategoryChildItem>()
    private val defaultCaseId = "0"

    private var currentPosition = "0"

    private var categoryApplink: String? = null
    private var currentCategoryName: String = ""

    var activityStateListener: ActivityStateListener? = null

    companion object {
        @JvmStatic
        fun newInstance() = CategoryLevelTwoFragment()
    }


    override fun getComponent(): CategoryNavigationComponent = DaggerCategoryNavigationComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()


    override fun refreshView(id: String, categoryName: String, applink: String?) {
        currentPosition = id
        currentCategoryName = categoryName
        categoryLevelTwoViewModel.refresh(id)
        categoryApplink = applink
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        component.inject(this)
        return inflater.inflate(R.layout.fragment_category_level_two, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        setUpObserver()
    }

    private fun setUpObserver() {
        categoryLevelTwoViewModel.getCategoryChildren().observe(viewLifecycleOwner, Observer {

            when (it) {
                is Success -> {
                    childList.clear()
                    childList.addAll(it.data)
                    slave_list.adapter = CategoryLevelTwoAdapter(childList, activityStateListener?.getActivityTrackingQueue())
                }

                is Fail -> {

                }
            }

        })

    }

    private fun initView() {
        addShimmerItems(childList)
        categoryLevelTwoAdapter = CategoryLevelTwoAdapter(childList, activityStateListener?.getActivityTrackingQueue())
        gridLayoutManager = GridLayoutManager(context, 6, GridLayoutManager.VERTICAL, false)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (slave_list.adapter?.getItemViewType(position)) {
                    Constants.ProductHeaderView -> 6
                    Constants.YangLagiHitsView -> 3
                    Constants.ProductView -> 2
                    Constants.HeaderShimmer -> 6
                    Constants.ProductShimmer -> 2
                    else -> 6
                }

            }
        }
        slave_list.apply {
            layoutManager = gridLayoutManager
            adapter = categoryLevelTwoAdapter
        }
    }

    private fun addShimmerItems(childList: ArrayList<CategoryChildItem>) {
        val item1 = CategoryChildItem()
        item1.itemType = Constants.HeaderShimmer
        childList.add(item1)
        val item2 = CategoryChildItem()
        item2.itemType = Constants.ProductShimmer
        for (i in 1..12) {
            childList.add(item2)
        }
    }

    private fun initViewModel() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        categoryLevelTwoViewModel = viewModelProvider.get(CategoryLevelTwoViewModel::class.java)
    }

}

interface Listener {
    fun refreshView(id: String, categoryName: String, applink: String?)
}
