package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst.SALAM_UMRAH_AGEN
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsInput
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.activity.UmrahTravelListActivity
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelListAdapterTypeFactory
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelListViewModel
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_umrah_travel_list.*

class UmrahTravelListFragment : BaseListFragment<TravelAgent, UmrahTravelListAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, UmrahTravelListActivity.TravelListListener{

    @Inject
    lateinit var umrahTravelListViewModel: UmrahTravelListViewModel

    @Inject
    lateinit var umrahTrackingAnalytics: UmrahTrackingAnalytics

    var travelList : List<TravelAgent> = listOf()

    var umrahTravelListInput = UmrahTravelAgentsInput()
    lateinit var performanceMonitoring: PerformanceMonitoring

    override fun getAdapterTypeFactory(): UmrahTravelListAdapterTypeFactory = UmrahTravelListAdapterTypeFactory(this)

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun loadData(page: Int) {
        requestData(page)
    }

    override fun hasInitialSwipeRefresh(): Boolean  = true

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_umrah_travel_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_list, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahTravelListViewModel.travelAgents.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> {
                    onSuccessResult(it.data)
                    travelList = it.data.umrahTravelAgents
                    performanceMonitoring.stopTrace()
                }

                is Fail -> {
                    performanceMonitoring.stopTrace()
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, null, null, null, R.drawable.img_umrah_pdp_empty_state) {
                        loadInitialData()
                    }
                }
            }
        })
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_umrah_travel_list

    private fun requestData(page: Int){
        umrahTravelListViewModel.requestTravelAgentsData(
                UmrahQuery.UMRAH_COMMON_TRAVEL_AGENT_QUERY,
                page, true
        )
    }

    private fun onSuccessResult(data : UmrahTravelAgentsEntity){
        rv_umrah_travel_list.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                    }
                }
            })
        }
        renderList(data.umrahTravelAgents, data.umrahTravelAgents.size >= umrahTravelListInput.limit)
    }

    private fun trackImpression(startIndex: Int, lastIndex: Int, data: MutableList<out Any>) {
        for (i in startIndex..lastIndex) {
            if (i < data.size) {
                if (data[i] is TravelAgent) {
                    val travel = data[i] as TravelAgent
                    if (!travel.isViewed) {
                        umrahTrackingAnalytics.umrahTravelListImpression(travel,i)
                        travel.isViewed = true
                    }
                }
            }
        }
    }

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(UMRAH_TRAVEL_LIST_PAGE_PERFORMANCE)
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        initializePerformance()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    override fun onBackPressed() {
        if (!isDetached) {
            umrahTrackingAnalytics.umrahTravelListClickBack()
        }
    }

    override fun onEmptyButtonClicked() {

    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onItemClicked(travelAgent: TravelAgent) {
         val position = travelList.indexOf(travelAgent)
         umrahTrackingAnalytics.umrahTravelListClick(travelAgent,position)
         RouteManager.route(context, SALAM_UMRAH_AGEN, travelAgent.slugName)

    }

    companion object{
        const val UMRAH_TRAVEL_LIST_PAGE_PERFORMANCE = "sl_umrah_travel_list"
        fun createInstance() = UmrahTravelListFragment()
    }
}