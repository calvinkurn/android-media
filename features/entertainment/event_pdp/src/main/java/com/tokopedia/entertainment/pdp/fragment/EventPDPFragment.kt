package com.tokopedia.entertainment.pdp.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.entertainment.pdp.R
import com.tokopedia.entertainment.pdp.activity.EventPDPActivity
import com.tokopedia.entertainment.pdp.activity.EventPDPActivity.Companion.EXTRA_URL_PDP
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
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getSizeSchedule
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventDateMapper.getStartDate
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventLocationMapper.getLatitude
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventLocationMapper.getLongitude
import com.tokopedia.entertainment.pdp.data.pdp.mapper.EventMediaMapper.mapperMediaPDP
import com.tokopedia.entertainment.pdp.di.EventPDPComponent
import com.tokopedia.entertainment.pdp.listener.OnBindItemListener
import com.tokopedia.entertainment.pdp.viewmodel.EventPDPViewModel
import com.tokopedia.entertainment.pdp.widget.WidgetEventPDPCarousel
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mapviewer.activity.MapViewerActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_about.view.*
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_facilities.view.*
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_how_to_go_there.view.*
import kotlinx.android.synthetic.main.bottom_sheet_event_pdp_open_hour.view.*
import kotlinx.android.synthetic.main.fragment_event_pdp.*
import kotlinx.android.synthetic.main.partial_event_pdp_price.*
import kotlinx.android.synthetic.main.widget_event_pdp_calendar.view.*
import kotlinx.android.synthetic.main.widget_event_pdp_carousel.*
import kotlinx.android.synthetic.main.widget_event_pdp_tab_section.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class EventPDPFragment : BaseListFragment<EventPDPModel, EventPDPFactoryImpl>(), OnBindItemListener,
        EventPDPActivity.PDPListener, AppBarLayout.OnOffsetChangedListener {

    private var urlPDP: String? = ""

    var listHoliday: List<Legend> = arrayListOf()
    var productDetailData: ProductDetailData = ProductDetailData()

    @Inject
    lateinit var eventPDPViewModel: EventPDPViewModel

    @Inject
    lateinit var eventPDPTracking: EventPDPTracking

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(EventPDPComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_pdp, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlPDP = arguments?.getString(EXTRA_URL_PDP, "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        eventPDPViewModel.eventProductDetailList.observe(this, Observer {
            clearAllData()
            it?.run {
                renderList(this)

            }
        })

        eventPDPViewModel.eventProductDetail.observe(this, Observer {
            productDetailData = it.eventProductDetail.productDetailData
            context?.let {
                renderView(it, productDetailData)
            }
        })

        eventPDPViewModel.isError.observe(this, Observer {
            it?.let {
                if (it.error) {
                    NetworkErrorHelper.showEmptyState(context, view?.rootView) {
                        loadDataAll()
                    }
                }
            }
        })

        eventPDPViewModel.eventHoliday.observe(this, Observer {
            listHoliday = it
        })
    }

    private fun requestData() {
        urlPDP?.let {
            eventPDPViewModel.getDataProductDetail(GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_product_detail),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_event_content_by_id), it)
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

    private fun loadCalendar(context: Context, productDetailData: ProductDetailData) {
        btn_event_pdp_cek_tiket.setOnClickListener {
            eventPDPTracking.onClickCariTicket(productDetailData)
            if (getSizeSchedule(productDetailData)) {

                val view = LayoutInflater.from(context).inflate(R.layout.widget_event_pdp_calendar, null)
                val bottomSheets = BottomSheetUnify()
                bottomSheets.apply {
                    setChild(view)
                    setTitle(context.resources.getString(R.string.ent_pdp_choose_date_bottom_sheet))
                    setCloseClickListener { bottomSheets.dismiss() }
                }

                view.bottom_sheet_calendar.apply {
                    getActiveDate(productDetailData).firstOrNull()?.let {
                        calendarPickerView?.init(Date(productDetailData.minStartDate.toLong() * 1000),
                                Date(productDetailData.maxEndDate.toLong() * 1000), listHoliday, getActiveDate(productDetailData))
                                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                                ?.withSelectedDate(it)
                    }

                    calendarPickerView?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
                        override fun onDateSelected(date: Date) {
                            val selectedDate = (date.time / 1000).toString()
                            eventPDPTracking.onClickPickDate()
                            goToTicketPage(productDetailData, selectedDate)
                            bottomSheets.dismiss()
                        }

                        override fun onDateUnselected(date: Date) {

                        }
                    })
                }
                fragmentManager?.let {
                    bottomSheets.show(it, "")
                }
            } else {
                goToTicketPageWithoutDate()
            }
        }
    }

    private fun loadTab(productDetailData: ProductDetailData) {

        container_price.show()
        shimmering_price.gone()
        (activity as EventPDPActivity).setSupportActionBar(event_pdp_toolbar)
        (activity as EventPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as EventPDPActivity).supportActionBar?.title = ""

        val navIcon = event_pdp_toolbar.navigationIcon

        context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N0) }?.let {
            navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
        }
        (activity as EventPDPActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

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
                    context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N700_96) }?.let {
                        navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
                    }
                    event_pdp_toolbar.menu.getItem(0).setIcon(R.drawable.ic_event_pdp_share_black)
                    widget_event_pdp_tab_section.setScrolledMode()
                    widget_event_pdp_tab_section.show()
                    isShow = true
                } else if (isShow) {
                    event_pdp_collapsing_toolbar.title = ""
                    context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N0) }?.let {
                        navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
                    }
                    event_pdp_toolbar.menu.getItem(0).setIcon(R.drawable.ic_event_pdp_share_white)
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
            RouteManager.route(it, getString(R.string.ent_pdp_param_to_package,ApplinkConstInternalEntertainment.EVENT_PACKAGE,urlPDP,selectedDate,startDate,endDate))
        }
    }

    private fun goToTicketPageWithoutDate() {
        context?.let {
            RouteManager.route(it,
                    "${ApplinkConstInternalEntertainment.EVENT_PACKAGE}/$urlPDP")
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
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_event_pdp_about, null)
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(view)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }

            view.web_event_pdp_about.loadData(value, "text/html", "UTF-8")
            fragmentManager?.let {
                bottomSheets.show(it, "")
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
            EventShare(this).shareTravelAgent(productDetailData, { showLoading() }, { hideLoading() }, this.applicationContext)
        }
    }

    override fun shareLink() {
        share(productDetailData)
    }


    companion object {

        const val DEFAULT_PIN = "DEFAULT_PIN"

        fun newInstance(urlPDP: String) = EventPDPFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, urlPDP)
            }
        }
    }
}
