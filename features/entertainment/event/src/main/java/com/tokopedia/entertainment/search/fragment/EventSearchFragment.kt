package com.tokopedia.entertainment.search.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery.getEventHistory
import com.tokopedia.entertainment.common.util.EventQuery.getEventSearchLocation
import com.tokopedia.entertainment.search.Link
import com.tokopedia.entertainment.search.adapter.SearchEventAdapter
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactoryImp
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.analytics.EventSearchPageTracking
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.viewmodel.EventSearchViewModel
import com.tokopedia.entertainment.search.viewmodel.factory.EventSearchViewModelFactory
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_search_activity.*
import kotlinx.android.synthetic.main.ent_search_fragment.*
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class EventSearchFragment : BaseDaggerFragment(), CoroutineScope,
        SearchEventListViewHolder.SearchEventListListener,
        SearchLocationListViewHolder.SearchLocationListener
{

    lateinit var searchadapter:SearchEventAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(EventSearchViewModel::class.java) }

    lateinit var performanceMonitoring: PerformanceMonitoring

    @Inject
    lateinit var userSession: UserSessionInterface

    var job: Job = Job()

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(ENT_SEARCH_PERFORMANCE)
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun initInjector() {
        getComponent(EventSearchComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ent_search_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        activity?.run {
            viewModel.resources = resources
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()
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
            initializePerformance()
            getData(CacheType.ALWAYS_CLOUD)
        }
    }

    private fun setupAdapter(){
        searchadapter = SearchEventAdapter(SearchTypeFactoryImp(::allLocation, this, this))

        recycler_viewParent.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = searchadapter
        }
    }

    private fun observeSearchList(){
        viewModel.searchList.observe(viewLifecycleOwner, Observer {
            searchadapter.setItems(it)
            performanceMonitoring.stopTrace()
        })

        viewModel.isItRefreshing.observe(viewLifecycleOwner, Observer { swipe_refresh_layout.isRefreshing = it })

        viewModel.errorReport.observe(viewLifecycleOwner,
                Observer { throwable ->
                    Log.d("ERROR_SNACK", "SHOW ERROR SNACK HISTORY FRAGMENT")
                    view?.let {
                        Log.d("ERROR_SNACK", "SHOW ERROR SNACK HISTORY FRAGMENT")
                        val snackbar = Snackbar.make(it, ErrorHandler.getErrorMessage(context, throwable), Snackbar.LENGTH_INDEFINITE)
                        snackbar.setAction("Coba Lagi") {
                            context?.let {
                                Log.d("ERROR_SNACK", "SHOW ERROR SNACK HISTORY FRAGMENT CONTEXT not null")
                                getData(CacheType.ALWAYS_CLOUD)
                            }
                        }
                        snackbar.show()
                    }
//                    NetworkErrorHelper.createSnackbarRedWithAction(activity,
//                            ErrorHandler.getErrorMessage(context, it)) { getData(CacheType.ALWAYS_CLOUD) }.showRetrySnackbar()
                }
        )
    }

    private fun getData(cacheType: CacheType = CacheType.CACHE_FIRST){
        swipe_refresh_layout.isRefreshing = true
        if(activity?.txt_search?.searchBarTextField?.text?.toString()!!.isNotEmpty()
                || activity?.txt_search?.searchBarTextField?.text?.toString()!!.isNotBlank()){
            Log.d("ERROR_SNACK", "SHOW ERROR SNACK HISTORY FRAGMENT CONTEXT not null search data")
            viewModel.getSearchData(activity?.txt_search?.searchBarTextField?.text?.toString()!!, cacheType, getEventSearchLocation())
        } else{
            Log.d("ERROR_SNACK", "SHOW ERROR SNACK HISTORY FRAGMENT CONTEXT not null history data")
            viewModel.getHistorySearch(cacheType, getEventHistory(), userSession.isLoggedIn)
        }
    }

    private fun allLocation(){
        RouteManager.route(context, Link.EVENT_LOCATION)
    }

    private fun initSearchBar(){
        activity?.txt_search?.searchBarTextField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isEmpty()) {
                    if (job.isActive) job.cancel()
                    viewModel.cancelRequest()
                    viewModel.getHistorySearch(CacheType.CACHE_FIRST, getEventHistory(), userSession.isLoggedIn)
                } else {
                    if (job.isActive) job.cancel()
                    job = launch {
                        delay(200)
                        EventSearchPageTracking.getInstance().clickSearchBarOnKeyWordSearchActivity(p0.toString())
                        viewModel.getSearchData(p0.toString(), CacheType.CACHE_FIRST, getEventSearchLocation())
                    }
                }
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun clickEventSearchSuggestion(event: SearchEventListViewHolder.KegiatanSuggestion, listsEvent: List<SearchEventListViewHolder.KegiatanSuggestion>, position: Int) {
        EventSearchPageTracking.getInstance().onClickedEventSearchSuggestion(event, listsEvent, position + 1, userSession.userId)
    }

    override fun impressionEventSearchSuggestion(listsEvent: SearchEventListViewHolder.KegiatanSuggestion, position: Int) {
        EventSearchPageTracking.getInstance().impressionEventSearchSuggestion(listsEvent, position, userSession.userId)
    }

    override fun impressionLocationEvent(listsCity: SearchLocationListViewHolder.LocationSuggestion, position: Int) {
        EventSearchPageTracking.getInstance().impressionCitySearchSuggestion(listsCity, position, userSession.userId)
    }

    override fun clickLocationEvent(location: SearchLocationListViewHolder.LocationSuggestion, listsLocation: SearchLocationListViewHolder.LocationSuggestion, position: Int) {
        EventSearchPageTracking.getInstance().onClickLocationSuggestion(location,
                listsLocation, position, userSession.userId)
    }

    companion object{
        fun newInstance() = EventSearchFragment()
        val TAG = EventSearchFragment::class.java.simpleName

        const val ENT_SEARCH_PERFORMANCE = "et_event_search"
    }

}