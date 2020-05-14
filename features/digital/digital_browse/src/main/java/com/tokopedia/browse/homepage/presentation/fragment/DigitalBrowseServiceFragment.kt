package com.tokopedia.browse.homepage.presentation.fragment

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.browse.R
import com.tokopedia.browse.common.data.DigitalBrowseServiceAnalyticsModel
import com.tokopedia.browse.common.util.DigitalBrowseAnalytics
import com.tokopedia.browse.homepage.di.DigitalBrowseHomeComponent
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapter
import com.tokopedia.browse.homepage.presentation.adapter.DigitalBrowseServiceAdapterTypeFactory
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceViewHolder
import com.tokopedia.browse.homepage.presentation.contract.DigitalBrowseServiceContract
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel
import com.tokopedia.browse.homepage.presentation.presenter.DigitalBrowseServicePresenter
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * @author by furqan on 30/08/18.
 */

class DigitalBrowseServiceFragment : BaseDaggerFragment(), DigitalBrowseServiceContract.View,
        DigitalBrowseServiceViewHolder.CategoryListener {

    @Inject
    lateinit var presenter: DigitalBrowseServicePresenter
    @Inject
    lateinit var digitalBrowseAnalytics: DigitalBrowseAnalytics

    private lateinit var tabLayout: TabLayout
    private lateinit var rvCategory: RecyclerView
    private lateinit var smoothScroller: LinearSmoothScroller
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var containerData: LinearLayout
    private lateinit var performanceMonitoring: PerformanceMonitoring

    private var scrollSelected: RecyclerView.OnScrollListener? = null
    private var tabSelectedListener: TabLayout.OnTabSelectedListener? = null

    private var oldTitlePosition = 0
    private var currentTitlePosition = 0
    private var currentScrollIndex = 0
    private var traceStop: Boolean = false;

    private var selectedCategoryId = -1

    private lateinit var viewModel: DigitalBrowseServiceViewModel
    private lateinit var serviceAdapter: DigitalBrowseServiceAdapter

    private lateinit var trackingQueue: TrackingQueue

    override val fragmentContext: Context?
        get() = context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackingQueue = TrackingQueue(activity!!)
        performanceMonitoring = PerformanceMonitoring.start(BROWSE_TRACE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_browse_service, container, false)

        containerData = view.findViewById(R.id.container_data)
        tabLayout = view.findViewById(R.id.tab_layout)
        rvCategory = view.findViewById(R.id.recycler_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        presenter.attachView(this)
        presenter.onInit()

        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(KEY_SERVICE_DATA)
        } else {
            viewModel = DigitalBrowseServiceViewModel(null)
        }

        if (arguments != null && arguments!!.containsKey(EXTRA_CATEGORY_ID)) {
            selectedCategoryId = arguments!!.getInt(EXTRA_CATEGORY_ID)
        }
    }

    override fun onPause() {
        super.onPause()
        if (::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_SERVICE_DATA, viewModel)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(DigitalBrowseHomeComponent::class.java).inject(this)
    }

    private fun initView() {

        val adapterTypeFactory = DigitalBrowseServiceAdapterTypeFactory(this)
        serviceAdapter = DigitalBrowseServiceAdapter(adapterTypeFactory, ArrayList())

        layoutManager = GridLayoutManager(context, COLUMN_NUMBER)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    serviceAdapter.isLoadingObject(position) -> 4
                    viewModel.categoryViewModelList!![position].isTitle -> 4
                    else -> 1
                }
            }
        }
        smoothScroller = object : LinearSmoothScroller(context!!) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text != "") {
                    digitalBrowseAnalytics.eventClickHeaderTabLayanan(tab.text!!.toString())

                    scrollSelected?.let { rvCategory.removeOnScrollListener(it) }

                    currentScrollIndex = viewModel.titleMap!![tab.text]!!.indexPositionInList
                    currentTitlePosition = currentScrollIndex
                    oldTitlePosition = currentTitlePosition

                    smoothScroller.targetPosition = viewModel.titleMap!![tab.text]!!
                            .indexPositionInList
                    layoutManager.startSmoothScroll(smoothScroller)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        }

        rvCategory.layoutManager = layoutManager
        rvCategory.setHasFixedSize(true)
        rvCategory.adapter = serviceAdapter

        serviceAdapter.showLoading()

    }

    override fun renderData(viewModel: DigitalBrowseServiceViewModel) {
        serviceAdapter.clearAllElements()
        tabLayout.removeAllTabs()

        this.viewModel = viewModel

        serviceAdapter.hideLoading()

        presenter.processTabData(viewModel.titleMap!!, viewModel, selectedCategoryId)

        renderDataList(viewModel.categoryViewModelList)

        viewModel.categoryViewModelList?.run {
            sendImpressionAnalytics(viewModel.categoryViewModelList)
        }

        setRecyclerViewListener()
        stopTrace()
    }

    private fun stopTrace() {
        if (!traceStop) {
            performanceMonitoring.stopTrace()
            traceStop = true
        }
    }

    override fun showTab() {
        tabLayout.visibility = View.VISIBLE
    }

    override fun hideTab() {
        tabLayout.visibility = View.GONE
    }

    override fun addTab(key: String) {
        tabLayout.addTab(tabLayout.newTab().setText(key))
    }

    private fun renderDataList(dataList: List<DigitalBrowseServiceCategoryViewModel>?) {
        serviceAdapter.addElement(dataList)
    }

    override fun renderTab(selectedTabIndex: Int) {
        tabLayout.addOnTabSelectedListener(tabSelectedListener!!)
        tabLayout.getTabAt(selectedTabIndex)!!.select()
    }

    override fun showGetDataError(e: Throwable) {
        serviceAdapter.hideLoading()
        containerData.visibility = View.GONE
        NetworkErrorHelper.showEmptyState(activity, activity!!.window.decorView.rootView,
                ErrorHandler.getErrorMessage(context, e)
        ) {
            containerData.visibility = View.VISIBLE
            serviceAdapter.showLoading()
            presenter.getDigitalCategory()
        }
        stopTrace()
    }

    private fun setRecyclerViewListener() {
        scrollSelected = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentScrollIndex = layoutManager.findFirstVisibleItemPosition()
                if (currentScrollIndex > -1 &&
                        viewModel.categoryViewModelList != null &&
                        viewModel.categoryViewModelList!!.isNotEmpty()) {

                    if (viewModel.categoryViewModelList!![currentScrollIndex].isTitle) {
                        currentTitlePosition = currentScrollIndex
                        val indexTab = viewModel.titleMap!![viewModel.categoryViewModelList!![currentScrollIndex].name]!!.indexPositionInTab
                        tabLayout.removeOnTabSelectedListener(tabSelectedListener!!)
                        tabLayout.getTabAt(indexTab)!!.select()
                        tabLayout.addOnTabSelectedListener(tabSelectedListener!!)
                        oldTitlePosition = currentTitlePosition
                    } else {
                        if (currentScrollIndex < oldTitlePosition) {
                            tabLayout.removeOnTabSelectedListener(tabSelectedListener!!)
                            tabLayout.getTabAt(tabLayout.selectedTabPosition - 1)!!.select()
                            tabLayout.addOnTabSelectedListener(tabSelectedListener!!)

                            val indexList = viewModel.titleMap!![tabLayout.getTabAt(tabLayout.selectedTabPosition)!!.text]!!.indexPositionInList
                            oldTitlePosition = indexList
                            currentTitlePosition = oldTitlePosition
                        } else {
                            val indexTab = tabLayout.selectedTabPosition + 1
                            if (indexTab < tabLayout.tabCount) {
                                val indexList = viewModel.titleMap!![tabLayout.getTabAt(indexTab)!!.text]!!.indexPositionInList
                                if (currentScrollIndex > indexList) {
                                    tabLayout.removeOnTabSelectedListener(tabSelectedListener!!)
                                    tabLayout.getTabAt(indexTab)!!.select()
                                    tabLayout.addOnTabSelectedListener(tabSelectedListener!!)

                                    oldTitlePosition = indexList
                                    currentTitlePosition = oldTitlePosition
                                }
                            }
                        }
                    }
                }
            }
        }

        rvCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (currentTitlePosition == layoutManager.findFirstVisibleItemPosition()) {
                    scrollSelected?.let {
                        rvCategory.removeOnScrollListener(it)
                        rvCategory.addOnScrollListener(it)
                    }
                }
            }
        })

        scrollSelected?.let { rvCategory.addOnScrollListener(it) }
    }

    override fun onCategoryItemClicked(viewModel: DigitalBrowseServiceCategoryViewModel?, itemPosition: Int) {
        val analyticsModel = presenter.getItemPositionInGroup(this.viewModel.titleMap!!, itemPosition)
        analyticsModel.iconName = viewModel!!.name!!
        analyticsModel.buIdentifier = viewModel.buIdentifier?:""

        digitalBrowseAnalytics.eventClickIconLayanan(analyticsModel)

        val intent = RouteManager.getIntentNoFallback(context, viewModel.appLinks)

        if (viewModel.appLinks != null && intent != null) {
            startActivity(intent)
        } else {
            RouteManager.route(context, viewModel.url)
        }
    }

    override fun sendImpressionAnalytics(viewModels: List<DigitalBrowseServiceCategoryViewModel>) {
        //create analytics model (combine data objects with their corresponding header name
        var dataObjects: ArrayList<DigitalBrowseServiceAnalyticsModel> =
                arrayListOf<DigitalBrowseServiceAnalyticsModel>()
        var position = 0
        for (item: DigitalBrowseServiceCategoryViewModel in viewModels) {
            if (!item.isTitle) {
                val analyticsModel = presenter.getItemPositionInGroup(
                        this.viewModel.titleMap!!,
                        position)
                analyticsModel.iconName = item.name ?: ""
                analyticsModel.buIdentifier = item.buIdentifier?:""
                dataObjects.add(analyticsModel)
            }
            position++
        }

        //hit impression based on their header name
        var dataObjectsPerHeader: ArrayList<DigitalBrowseServiceAnalyticsModel> =
                arrayListOf()
        var currentHeader = ""
        for (item: DigitalBrowseServiceAnalyticsModel in dataObjects) {
            if (currentHeader.isEmpty()) {
                currentHeader = item.headerName
            }
            if (!currentHeader.equals(item.headerName)) {
                digitalBrowseAnalytics.eventImpressionIconLayanan(
                        trackingQueue,
                        dataObjectsPerHeader,
                        currentHeader)
                currentHeader = item.headerName
                dataObjectsPerHeader.clear()
            }
            dataObjectsPerHeader.add(item)
        }
        //last header section impression
        digitalBrowseAnalytics.eventImpressionIconLayanan(
                trackingQueue,
                dataObjectsPerHeader,
                currentHeader)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }

    override fun getItemCount(): Int = serviceAdapter.itemCount

    companion object {

        private val EXTRA_CATEGORY_ID = "CATEGORY_ID"
        private val COLUMN_NUMBER = 4
        private val KEY_SERVICE_DATA = "KEY_SERVICE_DATA"
        private val BROWSE_TRACE = "dg_browse"

        val fragmentInstance: Fragment
            get() = DigitalBrowseServiceFragment()

        fun getFragmentInstance(categoryId: Int): Fragment {
            val fragment = DigitalBrowseServiceFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_CATEGORY_ID, categoryId)
            fragment.arguments = bundle

            return fragment
        }
    }
}