package com.tokopedia.entertainment.home.fragment

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.home.adapter.HomeEventAdapter
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
import com.tokopedia.entertainment.home.di.EventHomeComponent
import com.tokopedia.entertainment.home.utils.NavigationEventController
import com.tokopedia.entertainment.home.viewmodel.FragmentView
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModel
import com.tokopedia.entertainment.home.viewmodel.HomeEventViewModelFactory
import com.tokopedia.entertainment.home.widget.MenuSheet
import com.tokopedia.entertainment.pdp.fragment.EventPDPTicketFragment
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_home_fragment.*
import kotlinx.android.synthetic.main.ent_layout_shimering_home.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 27,January,2020
 */

class EventHomeFragment : BaseDaggerFragment(), FragmentView, MenuSheet.ItemClickListener,
        TrackingListener, EventGridEventViewHolder.ClickGridListener,
        EventCarouselEventViewHolder.ClickCarouselListener{

    companion object {
        fun getInstance(): EventHomeFragment = EventHomeFragment()
        val TAG = EventHomeFragment::class.java.simpleName
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

    @Inject
    lateinit var factory: HomeEventViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface
    lateinit var viewModel: HomeEventViewModel
    lateinit var homeAdapter: HomeEventAdapter
    lateinit var performanceMonitoring: PerformanceMonitoring
    private lateinit var localCacheHandler: LocalCacheHandler

    @Inject
    lateinit var analytics: EventHomePageTracking

    var favMenuItem: View? = null

    private fun initializePerformance() {
        performanceMonitoring = PerformanceMonitoring.start(ENT_HOME_PAGE_PERFORMANCE)
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun initInjector() {
        getComponent(EventHomeComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        setHasOptionsMenu(true)
        activity?.run {
            viewModel = ViewModelProviders.of(this, factory).get(HomeEventViewModel::class.java)
        }
        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getHomeData(EventQuery.getEventHomeQuery(), ::onSuccessGetData, ::onErrorGetData, CacheType.CACHE_FIRST)

        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            homeAdapter = HomeEventAdapter(HomeTypeFactoryImpl(::actionItemAdapter, this@EventHomeFragment,
                    this@EventHomeFragment, this@EventHomeFragment))
            adapter = homeAdapter
        }

        swipe_refresh_layout.setOnRefreshListener {
            initializePerformance()
            viewModel.getHomeData(EventQuery.getEventHomeQuery(), ::onSuccessGetData, ::onErrorGetData, CacheType.CLOUD_THEN_CACHE)
        }
    }

    private fun actionItemAdapter(item: EventItemModel, onSuccessPostLike: ((EventItemModel) -> Unit),
                                  onErrorPostLike: ((Throwable) -> Unit)) {
        if (userSession.isLoggedIn) {
            viewModel.postLiked(item, onSuccessPostLike, onErrorPostLike)
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN_POST_LIKES)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.ent_home_fragment, container, false)
        return view
    }


    private fun onErrorGetData(throwable: Throwable) {
        swipe_refresh_layout?.isRefreshing = false
        performanceMonitoring.stopTrace()
        Toast.makeText(context, throwable.message, Toast.LENGTH_LONG).show()
    }

    private fun onSuccessGetData(data: List<HomeEventItem<*>>) {
        shimering_layout.visibility = View.GONE
        analytics.openHomeEvent()
        homeAdapter.setItems(data)
        performanceMonitoring.stopTrace()
        swipe_refresh_layout?.isRefreshing = false
        startShowCase()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_LOGIN_FAVORITE -> actionMenuFavorite()
            REQUEST_LOGIN_TRANSACTION -> onMenuTransactionListClick()
        }
    }

    private fun actionMenuMore() {
        context?.let { MenuSheet.newInstance(it, this).show() }
    }

    private fun actionMenuFavorite() {
        if (userSession.isLoggedIn) {
            RouteManager.route(context, ApplinkConstInternalEntertainment.EVENT_FAVORITE)
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_LOGIN_FAVORITE)
        }
    }

    private fun startShowCase() {
        val coachMarkShown = localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, false)
        if (coachMarkShown) return

        var coachItems = ArrayList<CoachMarkItem>()
        coachItems.add(CoachMarkItem(view?.rootView?.findViewById(R.id.txt_search), getString(R.string.ent_home_page_coach_mark_title_1), getString(R.string.ent_home_page_coach_mark_desc_1)))
        val coachMark = CoachMarkBuilder().build()
        coachMark.show(activity, COACH_MARK_TAG, coachItems)
        localCacheHandler.apply {
            putBoolean(SHOW_COACH_MARK_KEY, true)
            applyEditor()
        }
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

    override fun getRes(): Resources = resources

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

    private fun redirectPdp(applink: String){
        val destination = EventHomeFragmentDirections.
        actionHomeEventFragmentToPdpEventFragment(applink)
        NavigationEventController.navigate(this@EventHomeFragment, destination)
    }
}