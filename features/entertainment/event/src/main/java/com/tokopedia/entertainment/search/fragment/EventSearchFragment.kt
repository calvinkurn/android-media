package com.tokopedia.entertainment.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.common.util.EventQuery.getEventHistory
import com.tokopedia.entertainment.common.util.EventQuery.getEventSearchLocation
import com.tokopedia.entertainment.databinding.EntSearchFragmentBinding
import com.tokopedia.entertainment.search.Link
import com.tokopedia.entertainment.search.activity.EventSearchActivity
import com.tokopedia.entertainment.search.adapter.SearchEventAdapter
import com.tokopedia.entertainment.search.adapter.factory.SearchTypeFactoryImp
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.analytics.EventSearchPageTracking
import com.tokopedia.entertainment.search.di.EventSearchComponent
import com.tokopedia.entertainment.search.listener.EventSearchBarActionListener
import com.tokopedia.entertainment.search.listener.EventSearchBarDataListener
import com.tokopedia.entertainment.search.viewmodel.EventSearchViewModel
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class EventSearchFragment : BaseDaggerFragment(), CoroutineScope,
        SearchEventListViewHolder.SearchEventListListener,
        SearchLocationListViewHolder.SearchLocationListener, EventSearchBarActionListener
{

    lateinit var searchadapter:SearchEventAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(EventSearchViewModel::class.java) }

    lateinit var performanceMonitoring: PerformanceMonitoring

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<EntSearchFragmentBinding>()

    var job: Job = Job()

    private var searchBarDataListener: EventSearchBarDataListener? = null

    fun setListener(searchBarDataListener: EventSearchBarDataListener) {
        this.searchBarDataListener = searchBarDataListener
    }
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
        binding = EntSearchFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as EventSearchActivity).eventSearchBarActionListener = this
        observeSearchList()
        setupAdapter()
        setupSwipeRefresh()
    }

    private fun setupSwipeRefresh(){
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            initializePerformance()
            getData(CacheType.ALWAYS_CLOUD)
        }
    }

    private fun setupAdapter(){
        searchadapter = SearchEventAdapter(SearchTypeFactoryImp(::allLocation, this, this))

        binding?.recyclerViewParent?.run {
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

        viewModel.isItRefreshing.observe(viewLifecycleOwner, Observer { binding?.swipeRefreshLayout?.isRefreshing = it })

        viewModel.errorReport.observe(viewLifecycleOwner,
                Observer {
                    lifecycleScope.launch {
                        delay(DELAY_TIME)
                        NetworkErrorHelper.createSnackbarRedWithAction(activity, ErrorHandler.getErrorMessage(context, it)) {
                            getData(CacheType.ALWAYS_CLOUD)
                        }.showRetrySnackbar()
                    }
                }
        )
    }

    private fun getData(cacheType: CacheType = CacheType.CACHE_FIRST){
        binding?.swipeRefreshLayout?.isRefreshing = true
        searchBarDataListener?.getKeyWord()?.let { keyword ->
            if(keyword.toString().isNotEmpty()
                || keyword.toString().isNotBlank()){
                viewModel.getSearchData(keyword.toString(), cacheType, getEventSearchLocation())
            } else{
                viewModel.getHistorySearch(cacheType, getEventHistory(), userSession.isLoggedIn)
            }
        }
    }

    private fun allLocation(){
        RouteManager.route(context, Link.EVENT_LOCATION)
    }

    override fun afterSearchBarTextChanged(keyword: CharSequence?) {
        if (keyword.toString().isEmpty()) {
            if (job.isActive) job.cancel()
            viewModel.cancelRequest()
            viewModel.getHistorySearch(CacheType.CACHE_FIRST, getEventHistory(), userSession.isLoggedIn)
        } else {
            if (job.isActive) job.cancel()
            job = launch {
                delay(DELAY_TIME)
                EventSearchPageTracking.getInstance().clickSearchBarOnKeyWordSearchActivity(keyword.toString())
                viewModel.getSearchData(keyword.toString(), CacheType.CACHE_FIRST, getEventSearchLocation())
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun clickEventSearchSuggestion(event: SearchEventListViewHolder.KegiatanSuggestion, listsEvent: List<SearchEventListViewHolder.KegiatanSuggestion>, position: Int) {
        RouteManager.route(context, event.app_url)
        EventSearchPageTracking.getInstance().onClickedEventSearchSuggestion(event, listsEvent, position + Int.ONE, userSession.userId)
        activity?.finish()
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
        activity?.finish()
    }

    companion object{
        fun newInstance() = EventSearchFragment()
        val TAG = EventSearchFragment::class.java.simpleName

        const val ENT_SEARCH_PERFORMANCE = "et_event_search"
        private const val DELAY_TIME = 200L
    }

}
