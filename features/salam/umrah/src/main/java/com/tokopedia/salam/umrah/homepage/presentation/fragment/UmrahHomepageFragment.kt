package com.tokopedia.salam.umrah.homepage.presentation.fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getYearNow
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.homepage.data.*
import com.tokopedia.salam.umrah.homepage.di.UmrahHomepageComponent
import com.tokopedia.salam.umrah.homepage.presentation.activity.UmrahHomepageActivity
import com.tokopedia.salam.umrah.homepage.presentation.adapter.UmrahHomepageBottomSheetAdapter
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactoryImpl
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import com.tokopedia.salam.umrah.homepage.presentation.viewmodel.UmrahHomepageViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheets_umrah_home_page.view.*
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

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    lateinit var performanceMonitoring: PerformanceMonitoring

    override fun getAdapterTypeFactory(): UmrahHomepageFactoryImpl =  UmrahHomepageFactoryImpl(this, userSessionInterface)

    override fun getScreenName(): String = getString(R.string.umrah_home_page_activity_label, getYearNow())

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_umrah_home_page

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        initializePerformance()
        resetIsRequested()
        setHideFAB()
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
        fab_umrah_home_page_message.bringToFront()
        fab_umrah_home_page_message.setOnClickListener {
            checkChatSession()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> context?.let{checkChatSession()}
            }
        }
    }

    private fun checkChatSession(){
        if (userSessionInterface.isLoggedIn){
            context?.let {
                startChatUmrah(it)
            }
        }else{
            goToLoginPage()
        }
    }

    private fun startChatUmrah(context: Context){
        val intent = RouteManager.getIntent(context,
                ApplinkConst.TOPCHAT_ASKSELLER,
                resources.getString(R.string.umrah_shop_id), "",
                resources.getString(R.string.umrah_shop_source), resources.getString(R.string.umrah_shop_name), "")
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        resetIsRequested()
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            umrahHomepageViewModel = viewModelProvider.get(UmrahHomepageViewModel::class.java)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHideFAB()
        umrahHomepageViewModel.homePageModel.observe(viewLifecycleOwner, Observer {
            clearAllData()
            it?.run {
                renderList(this)
                if(this.get(UMRAH_SEARCH_PARAM_INDEX).isSuccess)
                fab_umrah_home_page_message.show()
            }
        })

        umrahHomepageViewModel.isError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    performanceMonitoring.stopTrace()
                    resetIsRequested()
                    NetworkErrorHelper.showEmptyState(context, view?.rootView,null,null,null,R.drawable.umrah_img_empty_search_png) {
                        loadDataAll()
                    }
                }
            }
        })
    }

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(UMRAH_HOME_PAGE_PERFORMANCE)
    }

    private fun resetIsRequested(){
        isRequestedMyUmrah = false
        isRequestedCategory = false
        isDreamFundViewed = false
        isRequestedSpinnerLike = false
        isRequestedBanner = false
        isRequestedPartner = false
    }

    private fun loadDataAll() {
        resetIsRequested()
        isLoadingInitialData = true
        adapter.clearAllElements()
        showLoading()
        umrahHomepageViewModel.getIntialList(true)
    }

    private fun setHideFAB(){
        fab_umrah_home_page_message.hide()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_home_page, container, false)

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_umrah_home_page
    }

    override fun onBindParameterVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getSearchParamData(
                UmrahQuery.UMRAH_HOMEPAGE_SEARCH_PARAM_QUERY, isLoadedFromCloud)
    }

    override fun onBindMyUmrahVH(isLoadFromCloud: Boolean) {
        umrahHomepageViewModel.getUmrahSayaData(UmrahQuery.UMRAH_SAYA_LIST_QUERY, isLoadFromCloud)
    }

    override fun onBindCategoryVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getCategoryData(
                UmrahQuery.UMRAH_HOME_PAGE_CATEGORY_QUERY, isLoadedFromCloud)
    }

    override fun onBindCategoryFeaturedVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getCategoryFeaturedData(
                UmrahQuery.UMRAH_HOME_PAGE_FEATURED_QUERY, isLoadedFromCloud)
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

    override fun onBindBannerVH(isLoadedFromCloud: Boolean) {
        umrahHomepageViewModel.getBannerData(
                UmrahQuery.UMRAH_HOME_PAGE_BANNER_QUERY, isLoadedFromCloud)
    }

    override fun onClickBanner(banner: UmrahBanner, position: Int) {
        trackingUmrahUtil.umrahClickBannerTracker(banner, position)
    }

    override fun onImpressionBanner(banner: UmrahBanner, position: Int) {
        trackingUmrahUtil.umrahImpressionBannerTracker(banner,position)
    }

    override fun onBindPartnerVH(isLoadFromCloud: Boolean) {
        umrahHomepageViewModel.getPartnerTravelData(UmrahQuery.UMRAH_COMMON_TRAVEL_AGENT_QUERY, isLoadFromCloud)
    }

    override fun onPerformanceHomepageListener() {
        performanceMonitoring.stopTrace()
    }

    override fun onImpressionPartnerTravel(headerTitle: String, umrahTravelAgentsEntity: UmrahTravelAgentsEntity) {
        trackingUmrahUtil.umrahHomepagePartnerTravelImpression(headerTitle,umrahTravelAgentsEntity)

    }

    override fun onClickPartnerTravel(headerTitle: String, travelAgent: TravelAgent) {
        trackingUmrahUtil.umrahHomepagePartnerTravelClick(headerTitle,travelAgent)
    }

    override fun onClickAllPartner() {
        RouteManager.route(context, ApplinkConst.SALAM_UMRAH_LIST_AGEN)
    }

    override fun showBottomSheetSearchParam(title: String, listBottomSheet: UmrahHomepageBottomSheetData, defaultOption: Int, adapter: UmrahHomepageBottomSheetAdapter) {
        adapter.lastCheckedPosition = defaultOption
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_home_page, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.rv_umrah_home_page_bottom_sheet.apply {
            adapter.setList(listBottomSheet)
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        }
        bottomSheets.show(fragmentManager!!, "")
        adapter.bottomSheetDialog = bottomSheets
    }

    companion object {
        fun getInstance(): UmrahHomepageFragment = UmrahHomepageFragment()
        var isRequestedMyUmrah = false
        var isDreamFundViewed = false
        var isRequestedCategory = false
        var isRequestedSpinnerLike = false
        var isRequestedBanner = false
        var isRequestedPartner = false
        const val REQUEST_CODE_LOGIN = 400
        const val UMRAH_SEARCH_PARAM_INDEX = 0

        const val UMRAH_HOME_PAGE_PERFORMANCE = "sl_umrah_homepage"
    }

    private fun goToLoginPage() {
        if (activity != null) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN)
        }
    }
}


