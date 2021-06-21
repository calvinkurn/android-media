package com.tokopedia.entertainment.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery.getEventSearchCategory
import com.tokopedia.entertainment.search.activity.EventCategoryActivity
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.entertainment.search.analytics.EventCategoryPageTracking
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.viewmodel.EventDetailViewModel
import com.tokopedia.entertainment.search.viewmodel.factory.EventDetailViewModelFactory
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_search_category_emptystate.*
import kotlinx.android.synthetic.main.ent_search_category_text.*
import kotlinx.android.synthetic.main.ent_search_detail_activity.*
import kotlinx.android.synthetic.main.ent_search_detail_shimmer.*
import kotlinx.android.synthetic.main.ent_search_fragment.recycler_viewParent
import kotlinx.android.synthetic.main.ent_search_fragment.swipe_refresh_layout
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,March,2020
 */

class EventCategoryFragment : BaseDaggerFragment(), EventGridAdapter.EventGridListener {

    lateinit var categoryTextAdapter: CategoryTextBubbleAdapter
    lateinit var eventGridAdapter : EventGridAdapter
    lateinit var performanceMonitoring: PerformanceMonitoring
    private var QUERY_TEXT : String = ""
    private var CITY_ID : String = ""
    private var CATEGORY_ID: String = ""
    private val gridLayoutManager = GridLayoutManager(context,2)
    private val endlessScroll = getScrollListener()

    @Inject
    lateinit var factory: EventDetailViewModelFactory
    lateinit var viewModel: EventDetailViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object{
        fun newInstance() = EventCategoryFragment()
        val TAG = EventCategoryFragment::class.java.simpleName
        const val ENT_CATEGORY_PERFORMANCE = "et_event_category"
    }


    override fun getScreenName(): String {
        return TAG
    }

    override fun initInjector() {
        getComponent(EventSearchComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        QUERY_TEXT = (activity as EventCategoryActivity).getQueryText() ?: ""
        CITY_ID = (activity as EventCategoryActivity).getCityId() ?: ""
        CATEGORY_ID = (activity as EventCategoryActivity).getCategoryId() ?: ""
        return inflater.inflate(R.layout.ent_search_event_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        activity?.run {
            viewModel = ViewModelProviders.of(this,factory).get(EventDetailViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.txt_search?.searchBarTextField?.setText(QUERY_TEXT)
        observeLiveData()
        observeErrorReport()
        setInitData()
        setupView()
    }

    private fun setupView(){
        setupCategoryAdapter()
        setupGridAdapter()
        setupRefreshLayout()
        setupResetFilterButton()
        performanceMonitoring.stopTrace()
    }

    private fun setupGridAdapter(){
        eventGridAdapter = EventGridAdapter(this)
        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                if(eventGridAdapter.getItemViewType(position) == eventGridAdapter.VIEW_TYPE_LOADING) return gridLayoutManager.spanCount
                else return 1
            }
        }

        recycler_viewParent.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            addOnScrollListener(endlessScroll)
            adapter = eventGridAdapter
            setPadding(context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2), context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0)
                    , context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2), context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0))
        }
    }


    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(ENT_CATEGORY_PERFORMANCE)
    }

    private fun setupRefreshLayout(){
        swipe_refresh_layout.apply {
            setOnRefreshListener {
                recycler_viewParent.addOnScrollListener(endlessScroll)
                viewModel.page = "1"
                viewModel.getData(CacheType.ALWAYS_CLOUD,query = getQueryCategory())
            }
        }
    }

    private fun setupResetFilterButton(){
        activity?.resetFilterButton?.apply { setOnClickListener{ resetFilter() } }
    }

    private fun getScrollListener(): EndlessRecyclerViewScrollListener{
        return object: EndlessRecyclerViewScrollListener(gridLayoutManager){
            override fun onLoadMore(page: Int, p1: Int) {
                viewModel.page = (page+1).toString()
                viewModel.getData(query=getQueryCategory())
            }
        }
    }

    private fun setInitData(){
        viewModel.setData(cityID = CITY_ID)
        if(CATEGORY_ID.isNotBlank()){
            val cat = CATEGORY_ID.split(",")
            cat.forEach{
                viewModel.putCategoryToQuery(it,getQueryCategory())
            }
            viewModel.initCategory = true
        } else viewModel.getData(query=getQueryCategory())
    }

    private fun observeErrorReport(){
        viewModel.errorReport.observe(viewLifecycleOwner, Observer {
            NetworkErrorHelper.createSnackbarRedWithAction(activity, resources.getString(R.string.ent_search_error_message)) {
                recycler_viewParent.addOnScrollListener(endlessScroll)
                viewModel.page = "1"
                viewModel.getData(CacheType.ALWAYS_CLOUD,getQueryCategory())
            }.showRetrySnackbar()
        })
    }

    private fun resetFilter() {
        setupCategoryAdapter()
        viewModel.clearFilter(getQueryCategory())
    }

    private fun setupCategoryAdapter(){
        categoryTextAdapter = CategoryTextBubbleAdapter(::onCategoryClicked)

        activity?.recycler_view_category!!.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = categoryTextAdapter
        }

    }


    private fun onCategoryClicked(id : Any?){
        viewModel.putCategoryToQuery(id.toString(),getQueryCategory())
    }

    private fun observeLiveData(){
        observeViewState()

        viewModel.catLiveData.observe(viewLifecycleOwner, Observer {
            categoryTextAdapter.listCategory = it.listCategory
            categoryTextAdapter.hashSet = it.hashSet
            categoryTextAdapter.notifyDataSetChanged()

            if(it.position != -1 && it.position < it.listCategory.size) {
                activity?.recycler_view_category!!.scrollToPosition(it.position)
            }

        })

        viewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){

                if(viewModel.page == "1") eventGridAdapter.listEvent = it
                else{ it.forEach { eventGridAdapter.listEvent.add(it) } }

                eventGridAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun observeViewState(){
        viewModel.isItRefreshing.observe(viewLifecycleOwner, Observer { swipe_refresh_layout.isRefreshing = it })
        viewModel.isItShimmering.observe(viewLifecycleOwner, Observer { showOrHideShimmer(it) })
        viewModel.showParentView.observe(viewLifecycleOwner, Observer { showOrHideParentView(it) })
        viewModel.showResetFilter.observe(viewLifecycleOwner, Observer { showOrHideResetFilter(it) })
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer { showOrHideProgressBar(it) })
    }

    private fun showOrHideProgressBar(state: Boolean){
        eventGridAdapter.isLoading = state
        eventGridAdapter.notifyDataSetChanged()
    }

    private fun showOrHideResetFilter(state: Boolean) { activity?.resetFilter?.visibility = if(state) View.VISIBLE else View.GONE }

    private fun showOrHideParentView(state: Boolean){ activity?.parent_view?.visibility = if(state) View.VISIBLE else View.GONE }

    private fun showOrHideShimmer(state: Boolean){ activity?.shimering_layout?.visibility = if(state) View.VISIBLE else View.GONE }

    private fun getEventData(){
        viewModel.getData(query=getQueryCategory())
    }

    private fun getQueryCategory():String{
        return getEventSearchCategory()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    override fun impressionCategory(event: EventGridAdapter.EventGrid, listsEvent: List<EventGridAdapter.EventGrid>, position: Int) {
        EventCategoryPageTracking.getInstance().impressionGridViewProduct(event, listsEvent, position + 1, userSession.userId)
    }

    override fun clickCategory(event: EventGridAdapter.EventGrid, listsEvent: List<EventGridAdapter.EventGrid>, position: Int) {
        EventCategoryPageTracking.getInstance().onClickGridViewProduct(event, listsEvent, categoryTextAdapter.listCategory,
                position + 1, userSession.userId)
    }
}