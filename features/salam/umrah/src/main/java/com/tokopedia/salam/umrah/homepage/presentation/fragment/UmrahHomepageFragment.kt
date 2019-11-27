package com.tokopedia.salam.umrah.homepage.presentation.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.TrackingUmrahUtil
import com.tokopedia.salam.umrah.homepage.data.*
import com.tokopedia.salam.umrah.homepage.di.UmrahHomepageComponent
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactoryImpl
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.salam.umrah.homepage.presentation.viewmodel.UmrahHomepageViewModel
import javax.inject.Inject

/**
 * @author by furqan on 14/10/2019
 */
class UmrahHomepageFragment : BaseListFragment<UmrahHomepageModel, UmrahHomepageFactoryImpl>(), onItemBindListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var umrahHomepageViewModel: UmrahHomepageViewModel

    @Inject
    lateinit var trackingUmrahUtil: TrackingUmrahUtil

    private val adapterFactory by lazy {
        UmrahHomepageFactoryImpl(this, trackingUmrahUtil)
    }

    override fun getAdapterTypeFactory(): UmrahHomepageFactoryImpl = adapterFactory

    override fun getScreenName(): String = ""

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_umrah_home_page

    override fun initInjector() = getComponent(UmrahHomepageComponent::class.java)
            .inject(this)

    override fun loadData(p0: Int) {
        umrahHomepageViewModel.getIntialList(swipeToRefresh?.isRefreshing ?: false)
    }

    override fun onItemClicked(p0: UmrahHomepageModel?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            umrahHomepageViewModel = viewModelProvider.get(UmrahHomepageViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahHomepageViewModel.homePageModel.observe(this, Observer {
            clearAllData()
            it?.run { renderList(this) }
        })

        umrahHomepageViewModel.isAllError.observe(this, Observer {
            it?.let {
                if (it) {
                    NetworkErrorHelper.showEmptyState(context, view?.rootView, object : NetworkErrorHelper.RetryClickedListener {
                        override fun onRetryClicked() {
                            loadDataAll()
                        }
                    })
                }
            }
        })
    }

    private fun loadDataAll() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()
        umrahHomepageViewModel.getIntialList(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_home_page, container, false)

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_umrah_home_page
    }

    override fun onBindParameterVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getSearchParamUseCase(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_home_page_search_parameter), isLoadedFromCloud)
    }

    override fun onBindMyUmrahVH(isLoadFromCloud: Boolean) {
        umrahHomepageViewModel.getUmrahSayaUseCase(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_saya_list), isLoadFromCloud)
    }

    override fun onBindCategoryVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getCategoryUseCase(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_home_page_category), isLoadedFromCloud)
    }

    override fun onBindCategoryFeaturedVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getCategoryFeaturedUseCase(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_home_page_featured), isLoadedFromCloud)
    }

    companion object {
        fun getInstance(): UmrahHomepageFragment = UmrahHomepageFragment()
    }
}


