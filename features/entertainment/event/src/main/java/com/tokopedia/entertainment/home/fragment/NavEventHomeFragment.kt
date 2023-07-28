package com.tokopedia.entertainment.home.fragment

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.common_digital.common.presentation.bottomsheet.DigitalDppoConsentBottomSheet
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventGlobalError.errorEventHandlerGlobalError
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
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_home_event.*
import javax.inject.Inject

class NavEventHomeFragment :
    BaseListFragment<HomeEventItem, HomeTypeFactoryImpl>(),
    MenuSheet.ItemClickListener,
    TrackingListener,
    EventGridEventViewHolder.ClickGridListener,
    EventCarouselEventViewHolder.ClickCarouselListener {

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
        analytics.openHomeEvent()
        requestData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        renderToolbar()
        viewModel.eventHomeListData.observe(
            viewLifecycleOwner,
            Observer {
                clearAllData()
                when (it) {
                    is Success -> {
                        onSuccessGetData(it.data)
                    }

                    is Fail -> {
                        onErrorGetData(it.throwable)
                    }
                }
            }
        )

        viewModel.dppoConsent.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    activity?.invalidateOptionsMenu()
                }
                is Fail -> {}
            }
        }
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout_home

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view_home

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        loadAllData()
    }

    private fun requestData(isLoadFromCloud: Boolean = false) {
        viewModel.getHomeData(isLoadFromCloud)
        viewModel.getDppoConsent()
    }

    private fun loadAllData() {
        viewModel.getIntialList()
        requestData(true)
    }

    private fun onSuccessGetData(data: List<HomeEventItem>) {
        global_error_home_event.hide()
        container_event_home.show()
        renderList(data)
        performanceMonitoring.stopTrace()
        swipe_refresh_layout_home?.isRefreshing = false
        startShowCase()
    }

    private fun onErrorGetData(throwable: Throwable) {
        container_event_home.hide()
        swipe_refresh_layout_home?.isRefreshing = false
        performanceMonitoring.stopTrace()
        context?.let {
            errorEventHandlerGlobalError(
                it,
                throwable,
                container_error_home,
                global_error_home_event,
                { loadAllData() }
            )
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

    private fun renderToolbar() {
        (activity as EventNavigationActivity).setSupportActionBar(toolbar_home)
        (activity as EventNavigationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as EventNavigationActivity).supportActionBar?.title =
            getString(R.string.ent_home_page_event_hiburan)

        val navIcon = toolbar_home.navigationIcon

        context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96) }?.let {
            navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
        }
        (activity as EventNavigationActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        txt_search_home.searchBarTextField.inputType = 0
        txt_search_home.searchBarTextField.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> openEventSearch()
            }
            return@setOnTouchListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        val dppoConsentData = viewModel.dppoConsent.value
        inflater.inflate(R.menu.entertainment_menu_homepage, menu)
        if (dppoConsentData is Success && dppoConsentData.data.description.isNotEmpty()) {
            menu.showConsentIcon()
            menu.setupConsentIcon(dppoConsentData.data.description)
            menu.setupKebabIcon()
        } else {
            menu.hideConsentIcon()
            menu.setupKebabIcon()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMenuPromoClick() {
        RouteManager.route(context, PROMOURL)
    }

    override fun onMenuHelpClick() {
        RouteManager.route(context, FAQURL)
    }

    override fun onMenuTransactionListClick() {
        RouteManager.route(context, ApplinkConst.EVENTS_ORDER)
    }

    override fun getAdapterTypeFactory(): HomeTypeFactoryImpl =
        HomeTypeFactoryImpl(
            this,
            this,
            this
        )

    override fun loadData(page: Int) {
        viewModel.getIntialList()
    }

    override fun onItemClicked(t: HomeEventItem?) {
    }

    override fun clickBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int) {
        analytics.clickBanner(item, position, userSession.userId)
    }

    override fun impressionBanner(item: EventHomeDataResponse.Data.EventHome.Layout.Item, position: Int) {
        analytics.impressionBanner(item, position, userSession.userId)
    }

    override fun clickCategoryIcon(item: CategoryEventViewHolder.CategoryItemModel, position: Int) {
        analytics.clickCategoryIcon(item, position)
    }

    override fun clickLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int) {
        analytics.clickLocationEvent(item, listItems, position, userSession.userId)
    }

    override fun clickSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, title: String, position: Int) {
        analytics.clickSectionEventProduct(item, listItems, title, position, userSession.userId)
    }

    override fun clickSeeAllCuratedEventProduct(title: String, position: Int) {
        analytics.clickSeeAllCuratedEventProduct(title, position)
    }

    override fun clickSeeAllTopEventProduct() {
        analytics.clickSeeAllTopEventProduct()
    }

    override fun clickTopEventProduct(item: EventItemModel, listItems: List<String>, position: Int) {
        analytics.clickTopEventProduct(item, listItems, position, userSession.userId)
    }

    override fun impressionLocationEvent(item: EventItemLocationModel, listItems: List<EventItemLocationModel>, position: Int) {
        analytics.impressionLocationEvent(item, listItems, position, userSession.userId)
    }

    override fun impressionSectionEventProduct(item: EventItemModel, listItems: List<EventItemModel>, title: String, position: Int) {
        analytics.impressionSectionEventProduct(item, listItems, title, position, userSession.userId)
    }

    override fun impressionTopEventProduct(item: EventItemModel, listItems: List<String>, position: Int) {
        analytics.impressionTopEventProduct(item, listItems, position, userSession.userId)
    }

    override fun redirectToPDPEvent(applink: String) {
        redirectPdp(applink)
    }

    override fun redirectCarouselToPDPEvent(applink: String) {
        redirectPdp(applink)
    }

    private fun redirectPdp(applink: String) {
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

    private fun Menu.hideConsentIcon() {
        findItem(R.id.action_dppo_consent).isVisible = false
    }

    private fun Menu.showConsentIcon() {
        findItem(R.id.action_dppo_consent).isVisible = true
    }

    private fun Menu.setupConsentIcon(description: String) {
        if (description.isNotEmpty()) {
            context?.let { ctx ->
                val iconUnify = getIconUnifyDrawable(
                    ctx,
                    IconUnify.INFORMATION,
                    ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
                )
                iconUnify?.toBitmap()?.let {
                    getItem(0).setOnMenuItemClickListener {
                        val bottomSheet = DigitalDppoConsentBottomSheet(description)
                        bottomSheet.show(childFragmentManager)
                        true
                    }
                    getItem(0).icon = BitmapDrawable(
                        ctx.resources,
                        Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true)
                    )
                }
            }
        }
    }

    private fun Menu.setupKebabIcon() {
        context?.let { ctx ->
            val iconUnify = getIconUnifyDrawable(
                ctx,
                IconUnify.MENU_KEBAB_VERTICAL,
                ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            )
            iconUnify?.toBitmap()?.let {
                getItem(1).setOnMenuItemClickListener {
                    actionMenuMore()
                    true
                }
                getItem(1).icon = BitmapDrawable(
                    ctx.resources,
                    Bitmap.createScaledBitmap(it, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE, true)
                )
            }
        }
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

        private const val TOOLBAR_ICON_SIZE = 64
    }
}
