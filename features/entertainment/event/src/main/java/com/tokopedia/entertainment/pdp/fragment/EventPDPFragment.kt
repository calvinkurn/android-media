package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.navigation.EventNavigationActivity
import com.tokopedia.entertainment.pdp.adapter.EventPDPFacilitiesBottomSheetAdapter
import com.tokopedia.entertainment.pdp.adapter.EventPDPLocationDetailAdapter
import com.tokopedia.entertainment.pdp.adapter.EventPDPOpenHourAdapter
import com.tokopedia.entertainment.pdp.adapter.factory.EventPDPFactoryImpl
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter
import com.tokopedia.entertainment.pdp.common.util.EventShare
import com.tokopedia.entertainment.pdp.data.Facilities
import com.tokopedia.entertainment.pdp.data.Outlet
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.ValueBullet
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPModel
import com.tokopedia.entertainment.pdp.data.pdp.OpenHour
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getActiveDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getEndDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getStartDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.isScheduleWithDatePicker
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.isScheduleWithoutDatePicker
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventLocationMapper.getLatitude
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventLocationMapper.getLongitude
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventMediaMapper.mapperMediaPDP
import com.tokopedia.entertainment.pdp.di.DaggerEventPDPComponent
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPViewModel
import com.tokopedia.entertainment.pdp.widget.WidgetEventPDPCarousel
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mapviewer.activity.MapViewerActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_about.view.*
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_facilities.view.*
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_how_to_go_there.view.*
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_open_hour.view.*
import kotlinx.android.synthetic.main.fragment_event_pdp.*
import kotlinx.android.synthetic.main.partial_event_pdp_price.*
import kotlinx.android.synthetic.main.widget_event_pdp_calendar.view.*
import java.util.*
import javax.inject.Inject

class EventPDPFragment : BaseListFragment<EventPDPModel, EventPDPFactoryImpl>(), OnBindItemListener
        ,AppBarLayout.OnOffsetChangedListener {

    lateinit var performanceMonitoring: PerformanceMonitoring

    private var urlPDP: String? = ""

    var listHoliday: List<Legend> = arrayListOf()
    var productDetailData: ProductDetailData = ProductDetailData()
    var selectedDate = ""

    @Inject
    lateinit var eventPDPViewModel: EventPDPViewModel

    @Inject
    lateinit var eventPDPTracking: EventPDPTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerEventPDPComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_pdp, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        TimeZone.setDefault(TimeZone.getTimeZone(GMT));
        urlPDP = arguments?.getString(EXTRA_URL_PDP, "")

        arguments?.let {
            urlPDP= EventPDPFragmentArgs.fromBundle(it).seo
        }
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventPDPViewModel.eventProductDetailList.observe(viewLifecycleOwner, Observer {
            clearAllData()
            it?.run {
                renderList(this)
            }
        })

        eventPDPViewModel.eventProductDetail.observe(viewLifecycleOwner, Observer {
            productDetailData = it.eventProductDetail.productDetailData
            context?.let {
                renderView(it, productDetailData)
                if(userSession.isLoggedIn){
                    eventPDPViewModel.getWhiteListUser(userSession.userId.toInt(),userSession.email, productDetailData)
                }
            }
        })

        eventPDPViewModel.validateScanner.observe(viewLifecycleOwner, Observer {
            renderScanner(it)
        })

        eventPDPViewModel.isError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.error) {
                    NetworkErrorHelper.showEmptyState(context, view?.rootView) {
                        loadDataAll()
                        performanceMonitoring.stopTrace()
                    }
                }
            }
        })

        eventPDPViewModel.eventHoliday.observe(viewLifecycleOwner, Observer {
            listHoliday = it
        })
    }

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(ENT_PDP_PERFORMANCE)
    }

    private fun requestData() {
        urlPDP?.let {
            eventPDPViewModel.getDataProductDetail(EventQuery.eventPDPV3(),
                    eventContentById(), it)
        }

    }

    private fun loadDataAll() {
        eventPDPViewModel.getIntialList()
        requestData()
    }

    override fun getAdapterTypeFactory(): EventPDPFactoryImpl = EventPDPFactoryImpl(this)

    override fun loadData(p0: Int) {
        eventPDPViewModel.getIntialList()
    }

    override fun onItemClicked(p0: EventPDPModel) {

    }

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_event_pdp

    override fun getRecyclerViewResourceId(): Int = R.id.rv_event_pdp

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        carousel_event_pdp.shimmering()
        requestData()
    }

    fun renderView(context: Context, productDetailData: ProductDetailData) {
        loadMedia(productDetailData)
        loadTab(productDetailData)
        loadCalendar(context, productDetailData)
        loadPrice(productDetailData)
    }

    private fun loadMedia(productDetailData: ProductDetailData) {
        carousel_event_pdp.setImages(mapperMediaPDP(productDetailData))
        carousel_event_pdp.imageViewPagerListener = object : WidgetEventPDPCarousel.ImageViewPagerListener {
            override fun onImageClicked(position: Int) {
                context?.run {
                    startActivity(ImagePreviewSliderActivity.getCallingIntent(this, productDetailData.title,
                            mapperMediaPDP(productDetailData), mapperMediaPDP(productDetailData), position))
                }
            }
        }
        carousel_event_pdp.buildView()
    }

    private fun loadPrice(productDetailData: ProductDetailData) {
        tg_event_pdp_price.text = CurrencyFormatter.getRupiahFormat(productDetailData.salesPrice.toInt())
    }

    private fun renderScanner(isValidated: Boolean){
        if (isValidated && userSession.isLoggedIn){
            qr_redeem_pdp.show()
            qr_redeem_pdp.setOnClickListener {
                RouteManager.route(context, ApplinkConstInternalMarketplace.QR_SCANNEER)
            }
        }
    }

    private fun loadCalendar(context: Context, productDetailData: ProductDetailData) {
        btn_event_pdp_cek_tiket.setOnClickListener {
            eventPDPTracking.onClickCariTicket(productDetailData)
            if (isScheduleWithDatePicker(productDetailData)) {

                val view = LayoutInflater.from(context).inflate(R.layout.widget_event_pdp_calendar, null)
                val bottomSheets = BottomSheetUnify()
                bottomSheets.apply {
                    setChild(view)
                    setTitle(context.resources.getString(R.string.ent_pdp_choose_date_bottom_sheet))
                    setCloseClickListener { bottomSheets.dismiss() }
                }

                view.bottom_sheet_calendar.apply {
                    getActiveDate(productDetailData.dates).firstOrNull()?.let {
                        calendarPickerView?.init(it,Date(productDetailData.maxEndDate.toLong() * 1000), listHoliday, getActiveDate(productDetailData.dates))
                                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                    }

                    calendarPickerView?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                        override fun onDateSelected(date: Date) {
                            selectedDate = (date.time / 1000).toString()
                            eventPDPTracking.onClickPickDate()
                            if (userSession.isLoggedIn) { goToTicketPage(productDetailData, selectedDate) }
                            else {
                                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                                        REQUEST_CODE_LOGIN_WITH_DATE)
                            }
                            bottomSheets.dismiss()
                        }

                        override fun onDateUnselected(date: Date) {

                        }
                    })
                }
                fragmentManager?.let {
                    bottomSheets.show(it, "")
                }
            } else if(isScheduleWithoutDatePicker(productDetailData)) {
                selectedDate = productDetailData.dates.first()
                if (userSession.isLoggedIn) { goToTicketPageWithoutDate() }
                else {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN_WITHOUT_DATE)
                }
            } else {
                view?.let {
                    Toaster.build(it, it.context.getString(R.string.ent_pdp_empty_package), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, it.context.getString(R.string.ent_checkout_error)).show()
                }
            }
        }
    }

    private fun loadTab(productDetailData: ProductDetailData) {

        container_price.show()
        shimmering_price.gone()
        (activity as EventNavigationActivity).setSupportActionBar(event_pdp_toolbar)
        (activity as EventNavigationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as EventNavigationActivity).supportActionBar?.title = ""

        val navIcon = event_pdp_toolbar.navigationIcon

        context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0) }?.let {
            navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
        }
        (activity as EventNavigationActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        event_pdp_collapsing_toolbar.title = ""
        event_pdp_app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    event_pdp_collapsing_toolbar.title = productDetailData.title
                    context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_96) }?.let {
                        navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
                    }
                    event_pdp_toolbar.menu.getItem(0).setIcon(com.tokopedia.entertainment.R.drawable.ic_event_pdp_share_black)
                    widget_event_pdp_tab_section.setScrolledMode()
                    widget_event_pdp_tab_section.show()
                    isShow = true
                } else if (isShow) {
                    event_pdp_collapsing_toolbar.title = ""
                    context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0) }?.let {
                        navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
                    }
                    event_pdp_toolbar.menu.getItem(0).setIcon(com.tokopedia.entertainment.R.drawable.ic_event_pdp_share_white)
                    widget_event_pdp_tab_section.setNullMode()
                    widget_event_pdp_tab_section.hide()
                    isShow = false
                }
            }
        })


        widget_event_pdp_tab_section.setRecycleView(rv_event_pdp)

        rv_event_pdp.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                widget_event_pdp_tab_section.setScrolledSection((rv_event_pdp.layoutManager
                        as LinearLayoutManager).findFirstVisibleItemPosition())
            }
        })
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        swipeToRefresh.isEnabled = event_pdp_collapsing_toolbar.height + verticalOffset >= 2 * ViewCompat.getMinimumHeight(event_pdp_collapsing_toolbar)
    }

    override fun onResume() {
        super.onResume()
        event_pdp_app_bar_layout.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        event_pdp_app_bar_layout.removeOnOffsetChangedListener(this)
    }

    private fun goToTicketPage(productDetailData: ProductDetailData, selectedDate: String) {
        val startDate = getStartDate(productDetailData)
        val endDate = getEndDate(productDetailData)
        context?.let {
            RouteManager.route(it, getString(R.string.ent_pdp_param_to_package, ApplinkConstInternalEntertainment.EVENT_PACKAGE, urlPDP, selectedDate, startDate, endDate))
        }
    }

    private fun goToTicketPageWithoutDate() {
        context?.let {
            RouteManager.route(it,
                    getString(R.string.ent_pdp_param_to_package, ApplinkConstInternalEntertainment.EVENT_PACKAGE, urlPDP, selectedDate, "", ""))
        }
    }

    override fun seeAllOpenHour(openHour: List<OpenHour>, title: String) {
        val openHourAdapter = EventPDPOpenHourAdapter()
        openHourAdapter.setList(openHour)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_pdp_open_hour, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.rv_event_pdp_open_hour.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = openHourAdapter
        }

        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    override fun seeAllFacilities(facilities: List<Facilities>, title: String) {
        val facilitiesBottomSheetAdapter = EventPDPFacilitiesBottomSheetAdapter()
        facilitiesBottomSheetAdapter.setList(facilities)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_pdp_facilities, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.rv_event_pdp_facilities_bottom_sheet.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = facilitiesBottomSheetAdapter
        }
        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    override fun seeAllHowtoGoThere(listBullet: List<ValueBullet>, title: String) {
        val eventPDPLocationDetailAdapter = EventPDPLocationDetailAdapter()
        eventPDPLocationDetailAdapter.setList(listBullet)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_pdp_how_to_go_there, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        view.rv_event_pdp_how_to_got_there_bottom_sheet.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = eventPDPLocationDetailAdapter
        }
        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    override fun seeAllAbout(value: String, title: String) {

        val viewParent = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_pdp_about, null)
        val bottomSheets = BottomSheetUnify()
        val webView = viewParent.web_event_pdp_about
        bottomSheets.apply {
            isFullpage = true
            setChild(viewParent)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }

        fragmentManager?.let {
            bottomSheets.show(it, "")
        }

        bottomSheets.setShowListener {
            val loader = viewParent.loader_unify_event_pdp

            webView.loadData(value, "text/html", "UTF-8")
            webView.webViewClient = object : WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    webView.visibility = View.VISIBLE
                    loader.visibility = View.GONE
                }
            }
        }

    }

    override fun seeLocationDetail(outlet: Outlet) {
        val latitude = getLatitude(outlet)
        val longitude = getLongitude(outlet)
        context?.let {
            startActivity(MapViewerActivity.getCallingIntent(it, outlet.name, latitude, longitude
                    , outlet.gmapAddress, DEFAULT_PIN))
        }
    }

    fun share(productDetailData: ProductDetailData) {
        activity?.run {
            EventShare(this).shareEvent(productDetailData, { showShareLoading() }, { hideShareLoading() }, this.applicationContext)
        }
    }

    fun shareLink() {
        share(productDetailData)
    }

    override fun performancePdp() {
        performanceMonitoring.stopTrace()
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN_WITH_DATE -> context?.let {
                    goToTicketPage(productDetailData, selectedDate)
                }

                REQUEST_CODE_LOGIN_WITHOUT_DATE -> context?.let {
                    goToTicketPageWithoutDate()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId ?: "" == R.id.action_overflow_menu) {
            shareLink()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun hideShareLoading() {
        event_pdp_pb.hide()
    }

    fun showShareLoading() {
        event_pdp_pb.show()
    }

    companion object {

        const val DEFAULT_PIN = "DEFAULT_PIN"
        const val ENT_PDP_PERFORMANCE = "et_event_pdp"
        const val GMT = "GMT+7"
        const val EXTRA_URL_PDP = "EXTRA_URL_PDP"

        const val REQUEST_CODE_LOGIN_WITH_DATE = 100
        const val REQUEST_CODE_LOGIN_WITHOUT_DATE = 101

        fun newInstance(urlPDP: String) = EventPDPFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, urlPDP)
            }
        }
    }
}