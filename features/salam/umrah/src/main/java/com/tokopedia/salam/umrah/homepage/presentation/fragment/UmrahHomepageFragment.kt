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
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getYearNow
import com.tokopedia.salam.umrah.homepage.data.*
import com.tokopedia.salam.umrah.homepage.di.UmrahHomepageComponent
import com.tokopedia.salam.umrah.homepage.presentation.activity.UmrahHomepageActivity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactoryImpl
import com.tokopedia.salam.umrah.homepage.presentation.adapter.viewholder.UmrahHomepageCategoryViewHolder
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.salam.umrah.homepage.presentation.viewmodel.UmrahHomepageViewModel
import kotlinx.android.synthetic.main.fragment_umrah_home_page.*
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
    lateinit var trackingUmrahUtil: UmrahTrackingAnalytics

    override fun getAdapterTypeFactory(): UmrahHomepageFactoryImpl =  UmrahHomepageFactoryImpl(this)

    override fun getScreenName(): String = getString(R.string.umrah_home_page_activity_label, getYearNow())

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_umrah_home_page

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        isRequestedMyUmrah = false
        DREAM_FUND_VIEWED = false
        isRequestedCategory = false
    }

    override fun initInjector() = getComponent(UmrahHomepageComponent::class.java)
            .inject(this)

    override fun loadData(p0: Int) {
        umrahHomepageViewModel.getIntialList(swipeToRefresh?.isRefreshing ?: false)
    }

    override fun onItemClicked(p0: UmrahHomepageModel?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as UmrahHomepageActivity).updateTitle(screenName)
        rv_umrah_home_page.setItemViewCacheSize(20)
        rv_umrah_home_page.setHasFixedSize(true)
        rv_umrah_home_page.setDrawingCacheEnabled(true)
        rv_umrah_home_page.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRequestedMyUmrah = false
        isRequestedCategory = false
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
                    NetworkErrorHelper.showEmptyState(context, view?.rootView) { loadDataAll() }
                }
            }
        })
    }

    private fun loadDataAll() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()
        umrahHomepageViewModel.getIntialList(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_home_page, container, false)

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_umrah_home_page
    }

    override fun onBindParameterVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getSearchParamData(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_home_page_search_parameter), isLoadedFromCloud)
    }

    override fun onBindMyUmrahVH(isLoadFromCloud: Boolean) {
        umrahHomepageViewModel.getUmrahSayaData(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_saya_list), isLoadFromCloud)
    }

    override fun onBindCategoryVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getCategoryData(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_home_page_category), isLoadedFromCloud)
    }

    override fun onBindCategoryFeaturedVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getCategoryFeaturedData(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_umrah_home_page_featured), isLoadedFromCloud)
    }

    override fun onClickDanaImpian() {
        trackingUmrahUtil.umrahClickDanaImpianTracker()
    }

    override fun onClickUmrahMyUmrah(title: String, myUmrahEntity: MyUmrahEntity, position: Int) {
        trackingUmrahUtil.umrahClickUmrahSayaTracker(title, myUmrahEntity, position)
    }

    override fun onImpressionDanaImpian() {
        trackingUmrahUtil.umrahImpressionDanaImpianTracker()
    }

    override fun onImpressionMyUmrah(headerTitle: String, myUmrahEntity: MyUmrahEntity, position: Int) {
        trackingUmrahUtil.umrahImpressionUmrahSayaTracker(headerTitle, myUmrahEntity, position)
    }

    override fun onClickCategory(umrahCategories: UmrahCategories, position: Int) {
        trackingUmrahUtil.umrahClickCategoryTracker(umrahCategories,position)
    }

    override fun onImpressionCategory(umrahCategories: UmrahCategories, position: Int) {
        trackingUmrahUtil.umrahImpressionCategoryTracker(umrahCategories, position)
    }

    override fun onSearchProduct(period: String, location: String, price: String) {
        trackingUmrahUtil.umrahSearchProductTracker(period,location,price)
    }

    override fun onClickFeaturedCategory(headerTitle: String, positionDC: Int, products: Products, position: Int) {
        trackingUmrahUtil.umrahClickFeaturedCategoryTracker(headerTitle,positionDC,products,position)
    }

    override fun onImpressionFeaturedCategory(headerTitle: String, product: Products, position: Int, positionDC: Int) {
        trackingUmrahUtil.umrahImpressionFeaturedCategoryTracker(headerTitle,product, position, positionDC)
    }
    companion object {
        fun getInstance(): UmrahHomepageFragment = UmrahHomepageFragment()
        var isRequestedMyUmrah = false
        var DREAM_FUND_VIEWED = false
        var isRequestedCategory = false
    }
}


