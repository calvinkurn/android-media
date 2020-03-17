package com.tokopedia.entertainment.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.activity.EventCategoryActivity
import com.tokopedia.entertainment.search.adapter.DetailEventAdapter
import com.tokopedia.entertainment.search.adapter.factory.DetailTypeFactoryImp
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.viewmodel.EventDetailViewModel
import com.tokopedia.entertainment.search.viewmodel.factory.EventDetailViewModelFactory
import kotlinx.android.synthetic.main.ent_search_category_text.*
import kotlinx.android.synthetic.main.ent_search_detail_activity.*
import kotlinx.android.synthetic.main.ent_search_fragment.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,March,2020
 */

class EventCategoryFragment : BaseDaggerFragment() {

    lateinit var categoryAdapter: DetailEventAdapter
    lateinit var eventAdapter : DetailEventAdapter
    private var QUERY_TEXT : String = ""
    private var CITY_ID : String = ""
    private var CATEGORY_ID: String = ""

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
        QUERY_TEXT = if ((activity as EventCategoryActivity).getQueryText() != null) (activity as EventCategoryActivity).getQueryText()!! else ""
        CITY_ID = if ((activity as EventCategoryActivity).getCityId() != null) (activity as EventCategoryActivity).getCityId()!! else ""
        CATEGORY_ID = if((activity as EventCategoryActivity).getCategoryId() != null) (activity as EventCategoryActivity).getCategoryId()!! else ""
        return inflater.inflate(R.layout.ent_search_fragment, container, false)
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
        observeSearchList()
        observeErrorReport()
        setInitCategory()
        getEventData(QUERY_TEXT, CITY_ID)

        categoryAdapter = DetailEventAdapter(DetailTypeFactoryImp(::onCategoryClicked))

        activity?.recycler_view_category!!.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        eventAdapter = DetailEventAdapter(DetailTypeFactoryImp(::resetFilter))

        recycler_viewParent.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = eventAdapter
        }

        swipe_refresh_layout.apply {
            setOnRefreshListener {
                getEventData(QUERY_TEXT, CITY_ID)
            }
        }
    }

    private fun setInitCategory(){
        if(CATEGORY_ID.isNotBlank()){
            val cat = CATEGORY_ID.split(",")
            cat.forEach{
                viewModel.putCategoryToQuery(it)
            }
        }
    }

    private fun observeErrorReport(){
        viewModel.errorReport.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun resetFilter(FLAG : Any?) {
        viewModel.clearFilter()
    }

    private fun onCategoryClicked(id : Any?){
        viewModel.putCategoryToQuery(id.toString())
    }

    private fun observeSearchList(){
        viewModel.isItRefreshing.observe(this, Observer {
            swipe_refresh_layout.isRefreshing = it
        })

        viewModel.catLiveData.observe(this, Observer {
            categoryAdapter.setItems(it)
        })

        viewModel.eventLiveData.observe(this, Observer {
            eventAdapter.setItems(it)
        })
    }

    private fun getEventData(searchQuery: String, city_id: String){
        viewModel.getData(city_id, searchQuery)
    }
}