package com.tokopedia.entertainment.search.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.adapter.SearchEventAdapter
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactoryImp
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.viewmodel.EventSearchViewModel
import com.tokopedia.entertainment.search.viewmodel.factory.EventSearchViewModelFactory
import kotlinx.android.synthetic.main.ent_search_activity.*
import kotlinx.android.synthetic.main.ent_search_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class EventSearchFragment : BaseDaggerFragment(), CoroutineScope {

    lateinit var searchadapter:SearchEventAdapter
    @Inject
    lateinit var factory : EventSearchViewModelFactory
    lateinit var viewModel : EventSearchViewModel


    override fun getScreenName(): String {
        return TAG
    }

    override fun initInjector() {
        getComponent(EventSearchComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ent_search_fragment, container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this, factory).get(EventSearchViewModel::class.java)
            viewModel.resources = resources
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity?.txt_search?.searchBarTextField?.text!!.isNotBlank()){
            viewModel.getSearchData(activity?.txt_search?.searchBarTextField?.text.toString())
        } else {
            viewModel.getHistorySearch()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearchList()
        setupAdapter()
        initSearchBar()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh(){
        swipe_refresh_layout.setOnRefreshListener {
            if(activity?.txt_search?.searchBarTextField?.text?.toString()!!.isNotEmpty()
                    || activity?.txt_search?.searchBarTextField?.text?.toString()!!.isNotBlank()){
                viewModel.getSearchData(activity?.txt_search?.searchBarTextField?.text?.toString()!!)
            } else{
                viewModel.getHistorySearch()
            }
        }
    }

    private fun setupAdapter(){
        searchadapter = SearchEventAdapter(SearchTypeFactoryImp(::allLocation))

        recycler_viewParent.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = searchadapter
        }
    }

    private fun observeSearchList(){
        viewModel.searchList.observe(this,
                Observer {
                    searchadapter.setItems(it)
                })
        viewModel.isItRefreshing.observe(this,
                Observer {
                    swipe_refresh_layout.isRefreshing = it
                })
    }

    private fun allLocation(){
        RouteManager.route(context, ApplinkConstInternalEntertainment.EVENT_LOCATION)
    }

    private fun initSearchBar(){
        activity?.txt_search?.searchBarTextField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().length == 0){
                    viewModel.getHistorySearch()
                }
                else{
                    launch {
                        delay(200)
                        viewModel.getSearchData(p0.toString())
                    }
                }
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object{
        fun newInstance() = EventSearchFragment()
        val TAG = EventSearchFragment::class.java.simpleName
    }

}