package com.tokopedia.browse.categoryNavigation.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager

import com.tokopedia.browse.R
import com.tokopedia.browse.categoryNavigation.adapters.CategoryLevelTwoAdapter
import com.tokopedia.browse.categoryNavigation.adapters.HotlistAdapter
import com.tokopedia.browse.categoryNavigation.analytics.CategoryAnalytics
import com.tokopedia.browse.categoryNavigation.data.model.category.ChildItem
import com.tokopedia.browse.categoryNavigation.data.model.hotlist.ListItem
import com.tokopedia.browse.categoryNavigation.di.CategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.di.DaggerCategoryNavigationComponent
import com.tokopedia.browse.categoryNavigation.viewmodel.CategoryLevelTwoViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_category_level_two.*
import javax.inject.Inject

class CategoryLevelTwoFragment : Fragment(), Listener, HasComponent<CategoryNavigationComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var categoryLevelTwoViewModel: CategoryLevelTwoViewModel


    private lateinit var categoryLevelTwoAdapter: CategoryLevelTwoAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    private val childList = ArrayList<ChildItem>()

    private val categoryHotlist = ArrayList<ListItem>()


    val default_case_id = "0"

    var current_position = "0"

    var categoryApplink: String? = null
    var currentCategoryName: String = ""


    companion object {
        @JvmStatic
        fun newInstance() = CategoryLevelTwoFragment()
    }


    override fun getComponent(): CategoryNavigationComponent = DaggerCategoryNavigationComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent).build()


    override fun refreshView(id: String, categoryName: String, applink: String?) {
        current_position = id
        currentCategoryName = categoryName
        categoryLevelTwoViewModel.refresh(id)
        categoryLevelTwoViewModel.fetchHotlist(id, currentCategoryName)
        setShimmer(id)
        category_name.text = categoryName
        hotlist_name.text = "Hotlist $categoryName"
        categoryApplink = applink
    }

    private fun setShimmer(id: String) {

        slave_list.visibility = View.GONE
        hotlist.visibility = View.GONE
        label_lihat_semua.visibility = View.GONE
        category_name.visibility = View.GONE
        hotlist_name.visibility = View.GONE

        if (id.equals(default_case_id)) {
            shimmer_layout_default.visibility = View.VISIBLE
            shimmer_layout.visibility = View.GONE
        } else {
            shimmer_layout_default.visibility = View.GONE
            shimmer_layout.visibility = View.VISIBLE
        }

    }

    private fun removeShimmer() {

        if (!current_position.equals("0")) {
            label_lihat_semua.visibility = View.VISIBLE
            hotlist.visibility = View.VISIBLE
            hotlist_name.visibility = View.VISIBLE
        }

        shimmer_layout.visibility = View.GONE
        shimmer_layout_default.visibility = View.GONE
        slave_list.visibility = View.VISIBLE
        category_name.visibility = View.VISIBLE

        if (categoryApplink != null) {

            label_lihat_semua.setOnClickListener {
                routeToCategoryLevelTwo(activity!!, categoryApplink!!)
            }
            category_name.setOnClickListener {
                routeToCategoryLevelTwo(activity!!, categoryApplink!!)
            }
        }
    }
    private fun routeToCategoryLevelTwo(context: Context, categoryApplink: String) {
        RouteManager.route(context, categoryApplink)
        CategoryAnalytics.createInstance().eventClickLihatSemua(context,label_lihat_semua.text.toString())

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
        categoryLevelTwoViewModel.getCategoryChildren().observe(this, Observer {

            when (it) {
                is Success -> {
                    empty_view_second_level.visibility = View.GONE
                    childList.clear()
                    childList.addAll(it.data as List<ChildItem>)
                    removeShimmer()
                    slave_list.adapter = CategoryLevelTwoAdapter(childList)
                }

                is Fail -> {
                    shimmer_layout_default.visibility = View.GONE
                    shimmer_layout.visibility = View.GONE
                    empty_view_second_level.visibility = View.VISIBLE

                }
            }

        })

        categoryLevelTwoViewModel.getCategoryHotlist().observe(this, Observer<List<ListItem>> {
            categoryHotlist.clear()
            categoryHotlist.addAll(it as List<ListItem>)
            hotlist.adapter.notifyDataSetChanged()

        })
    }

    private fun initView() {

        empty_view_second_level.setOnClickListener {
            categoryLevelTwoViewModel.refresh(current_position)

        }


        categoryLevelTwoAdapter = CategoryLevelTwoAdapter(childList)
        gridLayoutManager = GridLayoutManager(context, 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val type = slave_list.adapter.getItemViewType(position)
                return if (type == 1)
                    1
                else
                    2
            }
        }

        slave_list.layoutManager = gridLayoutManager
        slave_list.adapter = categoryLevelTwoAdapter
        slave_list.isNestedScrollingEnabled = false


        val hotlistAdapter = HotlistAdapter(categoryHotlist)
        val horizontalLayout = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        hotlist.layoutManager = horizontalLayout
        hotlist.adapter = hotlistAdapter
        hotlist.isNestedScrollingEnabled = false


    }

    private fun initViewModel() {

        activity?.let { observer ->
            val viewModelProvider = ViewModelProviders.of(observer, viewModelFactory)
            categoryLevelTwoViewModel = viewModelProvider.get(CategoryLevelTwoViewModel::class.java)
        }
    }

    fun startShimmer(isStarted: Boolean) {
        if (isStarted) {
            if (current_position == "0") {
                shimmer_layout_default.visibility = View.VISIBLE
                shimmer_layout.visibility = View.GONE
            } else {
                shimmer_layout_default.visibility = View.GONE
                shimmer_layout.visibility = View.VISIBLE
            }
        } else {
            shimmer_layout_default.visibility = View.GONE
            shimmer_layout.visibility = View.GONE
        }

    }
}

interface Listener {
    fun refreshView(id: String, categoryName: String, applink: String?)
}
