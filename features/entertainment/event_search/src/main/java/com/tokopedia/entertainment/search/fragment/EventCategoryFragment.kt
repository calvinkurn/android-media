package com.tokopedia.entertainment.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.activity.EventCategoryActivity
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.viewmodel.EventDetailViewModel
import com.tokopedia.entertainment.search.viewmodel.factory.EventDetailViewModelFactory
import kotlinx.android.synthetic.main.ent_search_category_emptystate.*
import kotlinx.android.synthetic.main.ent_search_category_text.*
import kotlinx.android.synthetic.main.ent_search_detail_activity.*
import kotlinx.android.synthetic.main.ent_search_detail_shimmer.*
import kotlinx.android.synthetic.main.ent_search_fragment.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,March,2020
 */

class EventCategoryFragment : BaseDaggerFragment() {

    lateinit var categoryTextAdapter: CategoryTextBubbleAdapter
    lateinit var eventGridAdapter : EventGridAdapter
    private var QUERY_TEXT : String = ""
    private var CITY_ID : String = ""
    private var CATEGORY_ID: String = ""
    private val gridLayoutManager = GridLayoutManager(context,2)

    @Inject
    lateinit var factory: EventDetailViewModelFactory
    lateinit var viewModel: EventDetailViewModel

    companion object{
        fun newInstance() = EventCategoryFragment()
        val TAG = EventCategoryFragment::class.java.simpleName
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
        activity?.run {
            viewModel = ViewModelProviders.of(this,factory).get(EventDetailViewModel::class.java)
            viewModel.resources = resources
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
    }

    private fun setupGridAdapter(){
        eventGridAdapter = EventGridAdapter()

        recycler_viewParent.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            addOnScrollListener(getScrollListener())
            adapter = eventGridAdapter
        }
    }

    private fun setupRefreshLayout(){
        swipe_refresh_layout.apply {
            setOnRefreshListener {
                recycler_viewParent.addOnScrollListener(getScrollListener())
                viewModel.page = "1"
                getEventData()
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
                viewModel.getData()
            }
        }
    }

    private fun setInitData(){
        viewModel.setData(cityID = CITY_ID)
        if(CATEGORY_ID.isNotBlank()){
            val cat = CATEGORY_ID.split(",")
            cat.forEach{
                viewModel.putCategoryToQuery(it)
            }
            viewModel.initCategory = true
        } else viewModel.getData()
    }

    private fun observeErrorReport(){
        viewModel.errorReport.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun resetFilter() {
        setupCategoryAdapter()
        viewModel.clearFilter()
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
        viewModel.putCategoryToQuery(id.toString())
    }

    private fun observeLiveData(){
        observeViewState()

        viewModel.catLiveData.observe(this, Observer {
            categoryTextAdapter.listCategory = it.listCategory
            categoryTextAdapter.hashSet = it.hashSet
            categoryTextAdapter.notifyDataSetChanged()

            if(it.position != -1 && it.position < it.listCategory.size) {
                activity?.recycler_view_category!!.scrollToPosition(it.position)
            }

        })

        viewModel.eventLiveData.observe(this, Observer {
            if(it.isNotEmpty()){

                if(viewModel.page == "1") eventGridAdapter.listEvent = it
                else{ it.forEach { eventGridAdapter.listEvent.add(it) } }

                eventGridAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun observeViewState(){
        viewModel.isItRefreshing.observe(this, Observer { swipe_refresh_layout.isRefreshing = it })
        viewModel.isItShimmering.observe(this, Observer { showOrHideShimmer(it) })
        viewModel.showParentView.observe(this, Observer { showOrHideParentView(it) })
        viewModel.showResetFilter.observe(this, Observer { showOrHideResetFilter(it) })
    }

    private fun showOrHideResetFilter(state: Boolean) {
        if(state) activity?.resetFilter?.visibility = View.VISIBLE
        else activity?.resetFilter?.visibility = View.GONE
    }

    private fun showOrHideParentView(state: Boolean){
        if(state) activity?.parent_view?.visibility = View.VISIBLE
        else activity?.parent_view?.visibility = View.GONE
    }

    private fun showOrHideShimmer(state: Boolean){
        if(state) activity?.shimering_layout?.visibility = View.VISIBLE
        else activity?.shimering_layout?.visibility = View.GONE
    }

    private fun getEventData(){
        viewModel.getData()
    }
}