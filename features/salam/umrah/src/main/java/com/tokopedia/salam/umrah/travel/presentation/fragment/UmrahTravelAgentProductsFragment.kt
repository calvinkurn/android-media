package com.tokopedia.salam.umrah.travel.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.search.data.UmrahSearchProductDataParam
import com.tokopedia.salam.umrah.travel.data.UmrahTravelProduct
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelProductAdapterTypeFactory
import com.tokopedia.salam.umrah.travel.presentation.adapter.viewholder.UmrahTravelAgentProductViewHolder
import com.tokopedia.salam.umrah.travel.presentation.viewmodel.UmrahTravelProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_umrah_travel_agent_products.*
import javax.inject.Inject

class UmrahTravelAgentProductsFragment: BaseListFragment<UmrahTravelProduct, UmrahTravelProductAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, UmrahTravelAgentProductViewHolder.SetOnClickListener{

    var products : List<UmrahTravelProduct> =  emptyList()
    private val searchParam = UmrahSearchProductDataParam()
    private var slugName: String? = ""

    @Inject
    lateinit var umrahTravelProductViewModel: UmrahTravelProductViewModel

    @Inject
    lateinit var umrahTracking: UmrahTrackingAnalytics

    lateinit var performanceMonitoring: PerformanceMonitoring

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        slugName = arguments?.getString(UmrahTravelFragment.EXTRA_SLUGNAME, "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_travel_agent_products,container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahTravelProductViewModel.searchResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetResult(it.data)
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

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(UMRAH_TRAVEL_PRODUCT_PERFORMANCE)
    }

    private fun onSuccessGetResult(data: List<UmrahTravelProduct>){
        rv_umrah_travel_products.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(userVisibleHint) {
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && userVisibleHint) {
                        val firstVisibleItem = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        val dataList = (adapter as BaseListAdapter<*, *>).data
                        trackImpression(firstVisibleItem, lastVisibleItem, dataList)
                    }
                }
            })
        }
        renderList(data, true)
    }

    private fun trackImpression(startIndex: Int, lastIndex: Int, data: MutableList<out Any>) {
        for (i in startIndex..lastIndex) {
            if (i < data.size) {
                if (data[i] is UmrahTravelProduct) {
                    val product = data[i] as UmrahTravelProduct
                    if (!product.isViewed) {
                        umrahTracking.umrahTravelAgentProductImpression(product,i)
                        product.isViewed = true
                    }
                }
            }
        }
    }

    private fun requestData(page:Int){
        slugName?.let {
            umrahTravelProductViewModel.getDataProductTravel(page,it,
                    UmrahQuery.UMRAH_SEARCH_PRODUCT_QUERY)
        }
    }

    override fun getAdapterTypeFactory(): UmrahTravelProductAdapterTypeFactory = UmrahTravelProductAdapterTypeFactory(this,this)

    override fun getScreenName(): String  = ""

    override fun initInjector() = getComponent(UmrahTravelComponent::class.java).inject(this)

    override fun loadData(page: Int) {
        requestData(page)
    }

    override fun onEmptyButtonClicked() {
        RouteManager.route(context, ApplinkConst.SALAM_UMRAH)
    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onItemClicked(t: UmrahTravelProduct) {

    }

    override fun onProductClicked(umrahTravelProduct: UmrahTravelProduct, position: Int) {
        umrahTracking.umrahTravelAgentProductClick(umrahTravelProduct, position)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rv_umrah_travel_products

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.umrah_img_empty_search_png
        emptyModel.title = getString(R.string.umrah_search_empty_title)
        emptyModel.content = getString(R.string.umrah_search_empty_subtitle)
        emptyModel.buttonTitle = getString(R.string.umrah_empty_button)
        return emptyModel
    }

    companion object {
        fun createInstance(slugName: String) =
                UmrahTravelAgentProductsFragment().also {
                    it.arguments = Bundle().apply {
                        putString(UmrahTravelFragment.EXTRA_SLUGNAME, slugName)
                    }
                }
        const val UMRAH_TRAVEL_PRODUCT_PERFORMANCE = "sl_umrah_travel_agent_product"
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }
}
