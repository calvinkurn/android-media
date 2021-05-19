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
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery.eventQueryFullLocation
import com.tokopedia.entertainment.search.adapter.SearchEventAdapter
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactoryImp
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.viewmodel.EventLocationViewModel
import com.tokopedia.entertainment.search.viewmodel.factory.EventLocationViewModelFactory
import kotlinx.android.synthetic.main.ent_search_fragment.*
import javax.inject.Inject

class EventLocationFragment : BaseDaggerFragment(),
        SearchEventListViewHolder.SearchEventListListener,
        SearchLocationListViewHolder.SearchLocationListener {

    lateinit var searchadapter: SearchEventAdapter
    @Inject
    lateinit var factory : EventLocationViewModelFactory
    lateinit var viewModel : EventLocationViewModel

    override fun initInjector() {
        getComponent(EventSearchComponent::class.java).inject(this)
    }


    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ent_location_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this, factory).get(EventLocationViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearchList()
        getLocationData()

        searchadapter = SearchEventAdapter(SearchTypeFactoryImp(searchEventListener = this,
                searchLocationListener = this))

        recycler_viewParent.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = searchadapter
        }
    }

    private fun observeSearchList(){
        viewModel.errorReport.observe(this, Observer {
            NetworkErrorHelper.createSnackbarRedWithAction(activity, resources.getString(R.string.ent_search_error_message)){
                getLocationData()
            }.showRetrySnackbar()
        })

        viewModel.searchList.observe(this, Observer {
            searchadapter.setItems(it)
        })
    }

    private fun getLocationData(){
        viewModel.getFullLocationData(eventQueryFullLocation())
    }

    override fun clickEventSearchSuggestion(event: SearchEventListViewHolder.KegiatanSuggestion, listsEvent: List<SearchEventListViewHolder.KegiatanSuggestion>, position: Int) {
        // do nothing
    }

    override fun clickLocationEvent(location: SearchLocationListViewHolder.LocationSuggestion, listsLocation: SearchLocationListViewHolder.LocationSuggestion, position: Int) {
        // do nothing
    }

    override fun impressionEventSearchSuggestion(listsEvent: SearchEventListViewHolder.KegiatanSuggestion, position: Int) {
        // do nothing
    }

    override fun impressionLocationEvent(listsCity: SearchLocationListViewHolder.LocationSuggestion, position: Int) {
        // do nothing
    }

    companion object{
        fun newInstance() = EventLocationFragment()
        val TAG = EventLocationFragment::class.java.simpleName
    }
}