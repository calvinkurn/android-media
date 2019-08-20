package com.tokopedia.discovery.categoryrevamp.view.fragments.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.adapters.BaseCategoryAdapter
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants


abstract class BaseCategorySectionFragment : BaseDaggerFragment() {

    private lateinit var categoryNavigationListener: CategoryNavigationListener

    private var gridLayoutManager: GridLayoutManager? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var spanCount: Int = 0

    private val LANDSCAPE_COLUMN_MAIN = 3
    private val PORTRAIT_COLUMN_MAIN = 2


    protected fun onSwipeToRefresh() {
    }

    protected abstract fun getAdapter(): BaseCategoryAdapter?


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpan()
        initLayoutManager()
        initSwipeToRefresh()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is CategoryNavigationListener) {
            categoryNavigationListener = context
        }
    }

    private fun initSwipeToRefresh() {
        refreshLayout = view?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        refreshLayout?.setOnRefreshListener {
            onSwipeToRefresh()
        }
    }


    protected fun getGridLayoutManager(): GridLayoutManager? {
        return gridLayoutManager
    }

    protected fun getLinearLayoutManager(): LinearLayoutManager? {
        return linearLayoutManager
    }

    protected fun getStaggeredGridLayoutManager(): StaggeredGridLayoutManager? {
        return staggeredGridLayoutManager
    }


    private fun initLayoutManager() {
        linearLayoutManager = LinearLayoutManager(activity)
        gridLayoutManager = GridLayoutManager(activity, spanCount)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initSpan() {
        spanCount = calcColumnSize(resources.configuration.orientation)
    }


    private fun calcColumnSize(orientation: Int): Int {
        var defaultColumnNumber = 1
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> defaultColumnNumber = PORTRAIT_COLUMN_MAIN
            Configuration.ORIENTATION_LANDSCAPE -> defaultColumnNumber = LANDSCAPE_COLUMN_MAIN
        }
        return defaultColumnNumber
    }

    protected fun setUpNavigation() {
        categoryNavigationListener.setupSearchNavigation(object : CategoryNavigationListener.ClickListener {
            override fun onFilterClick() {
            }

            override fun onSortClick() {
            }

            override fun onChangeGridClick() {
                switchLayoutType()

            }
        })
    }

    private fun switchLayoutType() {
        switchCatalogcategory()
    }

    private fun switchCatalogcategory() {

        when (getAdapter()?.getCurrentLayoutType()) {
            CategoryNavConstants.RecyclerView.GridType.GRID_1 -> {
                spanCount = 1
                gridLayoutManager?.spanCount = spanCount
                staggeredGridLayoutManager?.spanCount = spanCount
                getAdapter()?.changeSingleGridView()

            }
            CategoryNavConstants.RecyclerView.GridType.GRID_2 -> {
                spanCount = 1
                gridLayoutManager?.spanCount = spanCount
                staggeredGridLayoutManager?.spanCount = spanCount
                getAdapter()?.changeListView()

            }
            CategoryNavConstants.RecyclerView.GridType.GRID_3 -> {
                spanCount = 2
                gridLayoutManager?.spanCount = spanCount
                staggeredGridLayoutManager?.spanCount = spanCount
                getAdapter()?.changeDoubleGridView()

            }

        }

    }

}