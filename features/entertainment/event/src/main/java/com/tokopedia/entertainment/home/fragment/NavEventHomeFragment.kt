package com.tokopedia.entertainment.home.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.factory.HomeTypeFactoryImpl
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventGridEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemLocationModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.entertainment.home.di.DaggerEventHomeComponent
import com.tokopedia.entertainment.home.utils.NavigationEventController
import com.tokopedia.entertainment.home.viewmodel.EventHomeViewModel
import com.tokopedia.entertainment.home.widget.MenuSheet
import com.tokopedia.entertainment.navigation.EventNavigationActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_home_event.*
import javax.inject.Inject

class NavEventHomeFragment: BaseListFragment<HomeEventItem, HomeTypeFactoryImpl>(),
        MenuSheet.ItemClickListener, TrackingListener, EventGridEventViewHolder.ClickGridListener,
        EventCarouselEventViewHolder.ClickCarouselListener{

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: EventHomePageTracking

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: EventHomeViewModel

    lateinit var performanceMonitoring: PerformanceMonitoring
    lateinit var localCacheHandler: LocalCacheHandler

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerEventHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        viewModel = viewModelProvider.get(EventHomeViewModel::class.java)

        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_event, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
        renderToolbar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.eventHomeListData.observe(viewLifecycleOwner, Observer {
            clearAllData()
            when(it){
                is Success -> {
                    onSuccessGetData(it.data)
                }

                is Fail -> {
                    onErrorGetData(it.throwable)
                }
            }
        })
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout_home

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view_home

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        loadAllData()
    }

    private fun requestData(isLoadFromCloud: Boolean = false){
        viewModel.getHomeData(isLoadFromCloud)
    }

    private fun loadAllData(){
        viewModel.getIntialList()
        requestData(true)
    }

    private fun onSuccessGetData(data: List<HomeEventItem>) {
        analytics.openHomeEvent()
        renderList(data)
        performanceMonitoring.stopTrace()
        swipe_refresh_layout_home?.isRefreshing = false
        startShowCase()
    }

    private fun onErrorGetData(throwable: Throwable) {
        swipe_refresh_layout_home?.isRefreshing = false
        errorHandler()
        performanceMonitoring.stopTrace()
        Toast.makeText(context, throwable.message, Toast.LENGTH_LONG).show()
    }

    private fun errorHandler(){
        NetworkErrorHelper.showEmptyState(context, view?.rootView) {
            loadAllData()
        }
    }

    private fun startShowCase() {
        val coachMarkShown = localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, false)
        if (coachMarkShown) return

        val coachItems = ArrayList<CoachMarkItem>()
        coachItems.add(CoachMarkItem(txt_search_home, getString(R.string.ent_home_page_coach_mark_title_1), getString(R.string.ent_home_page_coach_mark_desc_1)))
        val coachMark = CoachMarkBuilder().build()
        coachMark.show(activity, COACH_MARK_TAG, coachItems)
        localCacheHandler.apply {
            putBoolean(SHOW_COACH_MARK_KEY, true)
            applyEditor()
        }
    }

    private fun renderToolbar(){
        (activity as EventNavigationActivity).setSupportActionBar(toolbar_home)
        (activity as EventNavigationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as EventNavigationActivity).supportActionBar?.title =
                getString(R.string.ent_home_page_event_hiburan)

        val navIcon = toolbar_home.navigationIcon

        context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_96) }?.let {
            navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
        }
        (activity as EventNavigationActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        txt_search_home.searchBarTextField.inputType = 0
        txt_search_home.searchBarTextField.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> openEventSearch()
            }
            return@setOnTouchListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.entertainment_menu_homepage, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_more -> actionMenuMore()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuPromoClick() {
        RouteManager.route(context, PROMOURL)
    }

    override fun onMenuHelpClick() {
        RouteManager.route(context, FAQURL)
    }

    override fun onMenuTransactionListClick() {
        if (userSession.isLoggedIn) {
            RouteManager.route(context, ApplinkConst.EVENTS_ORDER)
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN_TRANSACTION)
        }
    }

    override fun getAdapterTypeFactory(): HomeTypeFactoryImpl =
            HomeTypeFactoryImpl(this,
                    this, this)

    override fun loadData(page: Int) {
        viewModel.getIntialList()
    }

    override fun onItemClicked(t: HomeEventItem?) {

    }

    override fun clickBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int) {
        analytics.clickBanner(item, position)
    }

    override fun impressionBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int) {
        analytics.impressionBanner(item, position)
    }

    override fun clickCategoryIcon(item: CategoryEventViewHolder.CategoryItemModel, position: Int) {
        analytics.clickCategoryIcon(item, position)
    }

    override fun clickLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int) {
        analytics.clickLocationEvent(item, listItems, position)
    }

    override fun clickSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, title: String, position: Int) {
        analytics.clickSectionEventProduct(item, listItems, title, position)
    }

    override fun clickSeeAllCuratedEventProduct(title: String, position: Int) {
        analytics.clickSeeAllCuratedEventProduct(title, position)
    }

    override fun clickSeeAllTopEventProduct() {
        analytics.clickSeeAllTopEventProduct()
    }

    override fun clickTopEventProduct(item: EventItemModel, listItems: List<String>, position: Int) {
        analytics.clickTopEventProduct(item, listItems, position)
    }

    override fun impressionLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int) {
        analytics.impressionLocationEvent(item, listItems, position)
    }

    override fun impressionSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, title: String, position: Int) {
        analytics.impressionSectionEventProduct(item, listItems, title, position)
    }

    override fun impressionTopEventProduct(item: EventItemModel, listItems: List<String>, position: Int) {
        analytics.impressionTopEventProduct(item, listItems, position)
    }

    override fun redirectToPDPEvent(applink: String) {
        redirectPdp(applink)
    }

    override fun redirectCarouselToPDPEvent(applink: String) {
        redirectPdp(applink)
    }

    override fun likeCarousel(itemModel: EventItemModel) {
        if (userSession.isLoggedIn) {
            viewModel.likeProduct(itemModel, userSession.userId)
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN_POST_LIKES)
        }
    }

    override fun likeProductGrid(itemModel: EventItemModel) {
        if (userSession.isLoggedIn) {
            viewModel.likeProduct(itemModel, userSession.userId)
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN_POST_LIKES)
        }
    }

    private fun redirectPdp(applink: String){
        val destination = NavEventHomeFragmentDirections.actionHomeEventFragmentToPdpEventFragment(applink)
        NavigationEventController.navigate(this, destination)
    }

    private fun actionMenuMore() {
        context?.let { MenuSheet.newInstance(it, this).show() }
    }

    private fun openEventSearch(): Boolean {
        RouteManager.route(context, ApplinkConstInternalEntertainment.EVENT_SEARCH)
        return true
    }

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(ENT_HOME_PAGE_PERFORMANCE)
    }

    companion object {

        val TAG = NavEventHomeFragment::class.java.simpleName


        const val PROMOURL = "https://www.tokopedia.com/promo/tiket/events/"
        const val FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/"
        const val REQUEST_LOGIN_FAVORITE = 213
        const val REQUEST_LOGIN_TRANSACTION = 214
        const val REQUEST_LOGIN_POST_LIKES = 215
        const val COACH_MARK_TAG = "event_home"
        const val ENT_HOME_PAGE_PERFORMANCE = "et_event_homepage"

        const val PREFERENCES_NAME = "event_home_preferences"
        const val SHOW_COACH_MARK_KEY = "show_coach_mark_key_home"
    }
}