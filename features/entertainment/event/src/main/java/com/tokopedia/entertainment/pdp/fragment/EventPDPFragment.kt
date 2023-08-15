package com.tokopedia.entertainment.pdp.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.common.util.EventGlobalError
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.common.util.EventQuery.eventContentById
import com.tokopedia.entertainment.databinding.BottomSheetEventPdpAboutBinding
import com.tokopedia.entertainment.databinding.BottomSheetEventPdpFacilitiesBinding
import com.tokopedia.entertainment.databinding.BottomSheetEventPdpHowToGoThereBinding
import com.tokopedia.entertainment.databinding.BottomSheetEventPdpOpenHourBinding
import com.tokopedia.entertainment.databinding.FragmentEventPdpBinding
import com.tokopedia.entertainment.databinding.WidgetEventPdpCalendarBinding
import com.tokopedia.entertainment.navigation.EventNavigationActivity
import com.tokopedia.entertainment.pdp.adapter.EventPDPFacilitiesBottomSheetAdapter
import com.tokopedia.entertainment.pdp.adapter.EventPDPLocationDetailAdapter
import com.tokopedia.entertainment.pdp.adapter.EventPDPOpenHourAdapter
import com.tokopedia.entertainment.pdp.adapter.factory.EventPDPFactoryImpl
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.common.util.CurrencyFormatter
import com.tokopedia.entertainment.pdp.common.util.EventShare
import com.tokopedia.entertainment.pdp.data.EventPDPContentCombined
import com.tokopedia.entertainment.pdp.data.Facilities
import com.tokopedia.entertainment.pdp.data.Outlet
import com.tokopedia.entertainment.pdp.data.ProductDetailData
import com.tokopedia.entertainment.pdp.data.ValueBullet
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPModel
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPTabEntity
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
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.mapviewer.activity.MapViewerActivity
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.ref.WeakReference
import java.util.TimeZone
import java.util.Date
import javax.inject.Inject

class EventPDPFragment : BaseListFragment<EventPDPModel, EventPDPFactoryImpl>(), OnBindItemListener
        ,AppBarLayout.OnOffsetChangedListener {

    lateinit var performanceMonitoring: PerformanceMonitoring

    private var urlPDP: String? = ""

    var listHoliday: List<Legend> = arrayListOf()
    var productDetailData: ProductDetailData = ProductDetailData()
    var selectedDate = ""
    private lateinit var eventShare: EventShare

    @Inject
    lateinit var eventPDPViewModel: EventPDPViewModel

    @Inject
    lateinit var eventPDPTracking: EventPDPTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentEventPdpBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerEventPDPComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEventPdpBinding.inflate(inflater)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
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

        eventPDPViewModel.eventProductDetail.observe(viewLifecycleOwner, Observer { eventPDPContentCombined ->
            productDetailData = eventPDPContentCombined.eventProductDetailEntity.eventProductDetail.productDetailData
            context?.let {
                renderView(it, eventPDPContentCombined)
                if(userSession.isLoggedIn){
                    eventPDPViewModel.getWhiteListUser(userSession.userId.toIntSafely(),userSession.email, productDetailData)
                }
            }
        })

        eventPDPViewModel.validateScanner.observe(viewLifecycleOwner, Observer {
            renderScanner(it)
        })

        eventPDPViewModel.isError.observe(viewLifecycleOwner, Observer {
            it?.let { error ->
                if (error.error) {
                    context?.let { it ->
                        binding?.run {
                            EventGlobalError.errorEventHandlerGlobalError(it, error.throwable, containerErrorEventPdp,
                                globalErrorPdpEvent, { loadDataAll() })
                        }
                    }
                    performanceMonitoring.stopTrace()
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
        binding?.run {
            containerErrorEventPdp.hide()
            globalErrorPdpEvent.hide()
            eventPDPViewModel.getIntialList()
            requestData()
        }
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
        binding?.carouselEventPdp?.shimmering()
        requestData()
    }

    fun renderView(context: Context, combined: EventPDPContentCombined) {
        loadMedia(productDetailData)
        context?.let {
            loadTab(productDetailData,
                    eventPDPViewModel.getTabsTitleData(combined,
                            it.resources.getString(R.string.ent_pdp_about_this),
                            it.resources.getString(R.string.ent_pdp_facilities),
                            it.resources.getString(R.string.ent_pdp_detail_lokasi))
            )
        }
        loadCalendar(context, productDetailData)
        loadPrice(productDetailData)
    }

    private fun loadMedia(productDetailData: ProductDetailData) {
        binding?.carouselEventPdp?.run {
            setImages(mapperMediaPDP(productDetailData))
            imageViewPagerListener = object : WidgetEventPDPCarousel.ImageViewPagerListener {
                override fun onImageClicked(position: Int) {
                    context?.run {
                        startActivity(ImagePreviewSliderActivity.getCallingIntent(this, productDetailData.title,
                            mapperMediaPDP(productDetailData), mapperMediaPDP(productDetailData), position))
                    }
                }
            }
            buildView()
        }
    }

    private fun loadPrice(productDetailData: ProductDetailData) {
        binding?.run {
            val price = productDetailData.salesPrice.toIntSafely()
            containerPdpPrice.tgEventPdpPrice.apply {
                text = if (price != ZERO_PRICE) {
                    CurrencyFormatter.getRupiahFormat(productDetailData.salesPrice.toIntSafely())
                } else {
                    context?.resources?.getString(R.string.ent_free_price) ?: ""
                }
            }
        }
    }

    private fun renderScanner(isValidated: Boolean){
        binding?.run {
            if (isValidated && userSession.isLoggedIn){
                containerPdpPrice.qrRedeemPdp.show()
                containerPdpPrice.qrRedeemPdp.setOnClickListener {
                    RouteManager.route(context, ApplinkConstInternalMarketplace.QR_SCANNEER)
                }
            }
        }
    }

    private fun loadCalendar(context: Context, productDetailData: ProductDetailData) {
        binding?.run {
            containerPdpPrice.btnEventPdpCekTiket.setOnClickListener {
                eventPDPTracking.onClickCariTicket(productDetailData)
                if (isScheduleWithDatePicker(productDetailData)) {

                    val bindingCalendar = WidgetEventPdpCalendarBinding.inflate(
                        LayoutInflater.from(context)
                    )
                    val bottomSheets = BottomSheetUnify()
                    bottomSheets.apply {
                        setChild(bindingCalendar.root)
                        setTitle(context.resources.getString(R.string.ent_pdp_choose_date_bottom_sheet))
                        setCloseClickListener { bottomSheets.dismiss() }
                    }

                    bindingCalendar.bottomSheetCalendar.apply {
                        getActiveDate(productDetailData.dates).firstOrNull()?.let {
                            calendarPickerView?.init(
                                it,
                                Date(productDetailData.maxEndDate.toLong() * DATE_LONG_VALUE),
                                listHoliday,
                                getActiveDate(productDetailData.dates)
                            )
                                ?.inMode(CalendarPickerView.SelectionMode.SINGLE)
                        }

                        calendarPickerView?.setOnDateSelectedListener(object :
                            CalendarPickerView.OnDateSelectedListener {
                            override fun onDateSelected(date: Date) {
                                selectedDate = (date.time / DATE_LONG_VALUE).toString()
                                eventPDPTracking.onClickPickDate()
                                if (userSession.isLoggedIn) {
                                    goToTicketPage(productDetailData, selectedDate)
                                } else {
                                    startActivityForResult(
                                        RouteManager.getIntent(context, ApplinkConst.LOGIN),
                                        REQUEST_CODE_LOGIN_WITH_DATE
                                    )
                                }
                                bottomSheets.dismiss()
                            }

                            override fun onDateUnselected(date: Date) {

                            }
                        })
                    }
                    childFragmentManager.let {
                        bottomSheets.show(it, "")
                    }
                } else if (isScheduleWithoutDatePicker(productDetailData)) {
                    selectedDate = productDetailData.dates.first()
                    if (userSession.isLoggedIn) {
                        goToTicketPageWithoutDate()
                    } else {
                        startActivityForResult(
                            RouteManager.getIntent(context, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN_WITHOUT_DATE
                        )
                    }
                } else {
                    view?.let {
                        Toaster.build(
                            it,
                            it.context.getString(R.string.ent_pdp_empty_package),
                            Toaster.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            it.context.getString(R.string.ent_checkout_error)
                        ).show()
                    }
                }
            }
        }
    }

    private fun loadTab(productDetailData: ProductDetailData,
                        tabsTitle: List<EventPDPTabEntity>) {
        binding?.run {
            containerPdpPrice.containerPrice.show()
            containerPdpPrice.shimmeringPrice.gone()

            (activity as EventNavigationActivity).setSupportActionBar(eventPdpToolbar)
            (activity as EventNavigationActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as EventNavigationActivity).supportActionBar?.title = ""

            val navIcon = eventPdpToolbar.navigationIcon

            context?.let {
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            }?.let {
                navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP)
            }
            (activity as EventNavigationActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

            eventPdpCollapsingToolbar.title = ""
            context?.let {
                eventPdpCollapsingToolbar.setCollapsedTitleTypeface(
                    Typography.getFontType(
                        it,
                        true,
                        Typography.DISPLAY_1
                    )
                )
            }
            eventPdpAppBarLayout.addOnOffsetChangedListener(object :
                AppBarLayout.OnOffsetChangedListener {
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    context?.let { context ->
                        var color = 0
                        if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                            eventPdpCollapsingToolbar.title = productDetailData.title
                            color = ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                            )
                            widgetEventPdpTabSection.setScrolledMode()
                            widgetEventPdpTabSection.show()
                        } else if (verticalOffset == 0) {
                            eventPdpCollapsingToolbar.title = ""
                            color = ContextCompat.getColor(
                                context,
                                com.tokopedia.unifyprinciples.R.color.Unify_NN0
                            )
                            widgetEventPdpTabSection.setNullMode()
                            widgetEventPdpTabSection.hide()
                        }
                        setDrawableColorFilter(navIcon, color)
                        if (eventPdpToolbar.menu.isNotEmpty()) {
                            setDrawableColorFilter(eventPdpToolbar.menu.getItem(0).icon, color)
                        }
                    }
                }
            })

            if (tabsTitle.isNotEmpty()) {
                widgetEventPdpTabSection.setRecycleView(rvEventPdp)
                widgetEventPdpTabSection.setDynamicTitle(tabsTitle)

                rvEventPdp.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        widgetEventPdpTabSection.setScrolledSection(checkVisibilityItem())
                    }
                })
            }
        }
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        binding?.run {
            swipeToRefresh.isEnabled = eventPdpCollapsingToolbar.height + verticalOffset >= 2 * ViewCompat.getMinimumHeight(eventPdpCollapsingToolbar)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.run {
            eventPdpAppBarLayout.addOnOffsetChangedListener(this@EventPDPFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        binding?.run {
            eventPdpAppBarLayout.removeOnOffsetChangedListener(this@EventPDPFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        hideShareLoading()
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
        val bindingOpenHour = BottomSheetEventPdpOpenHourBinding.inflate(
            LayoutInflater.from(context)
        )
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(bindingOpenHour.root)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        bindingOpenHour.rvEventPdpOpenHour.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = openHourAdapter
        }

        childFragmentManager.let {
            bottomSheets.show(it, "")
        }
    }

    override fun seeAllFacilities(facilities: List<Facilities>, title: String) {
        val facilitiesBottomSheetAdapter = EventPDPFacilitiesBottomSheetAdapter()
        facilitiesBottomSheetAdapter.setList(facilities)
        val bindingFacilities = BottomSheetEventPdpFacilitiesBinding.inflate(
            LayoutInflater.from(context)
        )
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(bindingFacilities.root)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        bindingFacilities.rvEventPdpFacilitiesBottomSheet.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = facilitiesBottomSheetAdapter
        }
        childFragmentManager.let {
            bottomSheets.show(it, "")
        }
    }

    override fun seeAllHowtoGoThere(listBullet: List<ValueBullet>, title: String) {
        val eventPDPLocationDetailAdapter = EventPDPLocationDetailAdapter()
        eventPDPLocationDetailAdapter.setList(listBullet)
        val bindingHowToGo = BottomSheetEventPdpHowToGoThereBinding.inflate(
            LayoutInflater.from(context)
        )
        val bottomSheets = BottomSheetUnify()
        bottomSheets.apply {
            setChild(bindingHowToGo.root)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }
        bindingHowToGo.rvEventPdpHowToGotThereBottomSheet.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = eventPDPLocationDetailAdapter
        }
        fragmentManager?.let {
            bottomSheets.show(it, "")
        }
    }

    override fun seeAllAbout(value: String, title: String) {

        val bindingAbout = BottomSheetEventPdpAboutBinding.inflate(LayoutInflater.from(context))
        val bottomSheets = BottomSheetUnify()
        val webView = bindingAbout.webEventPdpAbout
        bottomSheets.apply {
            isFullpage = true
            setChild(bindingAbout.root)
            setTitle(title)
            setCloseClickListener { bottomSheets.dismiss() }
        }

        fragmentManager?.let {
            bottomSheets.show(it, "")
        }

        bottomSheets.setShowListener {
            val loader = bindingAbout.loaderUnifyEventPdp

            webView.loadPartialWebView(value)
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
        activity?.let { activity ->
            val context = WeakReference<Activity>(activity)
            if(!::eventShare.isInitialized) eventShare = EventShare(context)
            val titleShare = getString(R.string.ent_pdp_share_title, productDetailData.title)
            eventShare.shareEvent(productDetailData, titleShare, requireContext(), { showShareLoading() }, { hideShareLoading() })
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
        binding?.run {
            eventPdpPb.hide()
        }
    }

    fun showShareLoading() {
        binding?.run {
            eventPdpPb.show()
        }
    }

    private fun checkVisibilityItem(): Int{
        return (binding?.rvEventPdp?.layoutManager
                as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    private fun setDrawableColorFilter(drawable: Drawable?, color: Int) {
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    companion object {

        const val DEFAULT_PIN = "DEFAULT_PIN"
        const val ENT_PDP_PERFORMANCE = "et_event_pdp"
        const val GMT = "GMT+7"
        const val EXTRA_URL_PDP = "EXTRA_URL_PDP"

        const val REQUEST_CODE_LOGIN_WITH_DATE = 100
        const val REQUEST_CODE_LOGIN_WITHOUT_DATE = 101

        const val DATE_LONG_VALUE = 1000
        const val ZERO_PRICE = 0

        fun newInstance(urlPDP: String) = EventPDPFragment().also {
            it.arguments = Bundle().apply {
                putString(EXTRA_URL_PDP, urlPDP)
            }
        }
    }
}
