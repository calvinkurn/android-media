package com.tokopedia.salam.umrah.pdp.presentation.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.analytics.UmrahPdpTrackingUserAction
import com.tokopedia.salam.umrah.common.analytics.UmrahTrackingAnalytics
import com.tokopedia.salam.umrah.common.data.UmrahItemWidgetModel
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentWidgetModel
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter.getRupiahFormat
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil.getSlashedPrice
import com.tokopedia.salam.umrah.common.util.UmrahQuery
import com.tokopedia.salam.umrah.common.util.UmrahSpaceItemDecoration
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment
import com.tokopedia.salam.umrah.pdp.data.ParamPurchase
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpFeaturedFacilityModel
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpGreenRectWidgetModel
import com.tokopedia.salam.umrah.pdp.di.UmrahPdpComponent
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_IS_EMPTY
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_MAX_PASSENGER
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_SLUG_NAME
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PASSENGER
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_TOTAL_PRICE
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.EXTRA_VARIANT_ROOM
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpActivity.Companion.REQUEST_PDP_DETAIL
import com.tokopedia.salam.umrah.pdp.presentation.activity.UmrahPdpDetailActivity
import com.tokopedia.salam.umrah.pdp.presentation.adapter.*
import com.tokopedia.salam.umrah.pdp.presentation.fragment.UmrahPdpDetailFragment.Companion.paramPurchase
import com.tokopedia.salam.umrah.pdp.presentation.itemdecoration.UmrahPdpFaqIndicator
import com.tokopedia.salam.umrah.pdp.presentation.viewmodel.UmrahPdpViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottom_sheets_umrah_pdp_facilities.view.*
import kotlinx.android.synthetic.main.fragment_umrah_pdp.*
import kotlinx.android.synthetic.main.fragment_umrah_pdp.view.*
import kotlinx.android.synthetic.main.widget_umrah_pdp_green_rect.view.*
import kotlinx.android.synthetic.main.widget_umrah_pdp_itinerary.view.*
import javax.inject.Inject


/**
 * @author by M on 30/10/19
 */
class UmrahPdpFragment : BaseDaggerFragment(), UmrahPdpActivity.OnBackListener, AppBarLayout.OnOffsetChangedListener {
    @Inject
    lateinit var umrahPdpViewModel: UmrahPdpViewModel
    @Inject
    lateinit var umrahTrackingUtil: UmrahTrackingAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val umrahPdpImageViewPagerAdapter by lazy { UmrahPdpImageViewPagerAdapter() }
    private val umrahPdpHotelAdapter by lazy { UmrahPdpHotelAdapter() }
    private val umrahPdpAirlineAdapter by lazy { UmrahPdpAirlineAdapter() }
    private val umrahPdpFeaturedFacilityAdapter by lazy { UmrahPdpFeaturedFacilityAdapter() }
    private val umrahPdpFacilityAdapter by lazy { UmrahPdpFacilityAdapter() }
    private val umrahPdpNonFacilityAdapter by lazy { UmrahPdpNonFacilityAdapter() }
    private val umrahPdpFaqAdapter by lazy { UmrahPdpFaqAdapter() }

    private var slugName: String? = ""

    private var isFaqsScrolled = false
    private var isAirlinesScrolled = false
    private var isHotelsScrolled = false

    private lateinit var swipeToRefresh: SwipeRefreshLayout

    lateinit var performanceMonitoring: PerformanceMonitoring

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(UmrahPdpComponent::class.java).inject(this)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahPdpViewModel.pdpData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
                is Fail -> showGetListError()
            }
        })
    }

    private fun setupSwipeToRefresh(view: View) {
        swipeToRefresh = view.umrah_pdp_swipe_to_refresh
        swipeToRefresh.setColorSchemeColors(resources.getColor(com.tokopedia.unifyprinciples.R.color.Green_G600))
        swipeToRefresh.setOnRefreshListener {
            initializePerformance()
            hideData()
            swipeToRefresh.isRefreshing = true
            requestData()
            resetParamPurchase()
        }
    }

    private fun showGetListError() {
        performanceMonitoring.stopTrace()
        swipeToRefresh.isEnabled = false
        NetworkErrorHelper.showEmptyState(context, view?.rootView,null,null,null,R.drawable.img_umrah_pdp_empty_state) {
            requestData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_PDP_DETAIL -> {
                if (data != null) {
                    val isEmpty = data.getBooleanExtra(EXTRA_IS_EMPTY, false)
                    if (isEmpty) {
                        activity?.apply {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    } else {
                        paramPurchase.apply {
                            variant = data.getStringExtra(EXTRA_VARIANT_ROOM)
                            totalPassenger = data.getIntExtra(EXTRA_TOTAL_PASSENGER, 1)
                            val maxPassenger = data.getIntExtra(EXTRA_MAX_PASSENGER, 1)
                            totalPrice = data.getIntExtra(EXTRA_TOTAL_PRICE, 1)
                            setupHotelTypeItem()
                            if (maxPassenger != 0) setupPriceAndPurchaseButton()
                        }
                    }
                }
            }

            REQUEST_CODE_LOGIN->
                if (resultCode == Activity.RESULT_OK) {
                    context?.let { checkChatSession() }
                }
        }
    }

    private fun requestData() {
        slugName?.let {
            umrahPdpViewModel.requestPdpData(
                    UmrahQuery.UMRAH_PDP_QUERY, it)
        }
    }

    private fun onSuccessGetResult(umrahProduct: UmrahProductModel.UmrahProduct) {
        enableSwipeToRefresh()
        UmrahPdpFragment.umrahProduct = umrahProduct
        setupAll()
        performanceMonitoring.stopTrace()
    }

    private fun setupAll() {
        setupFAB()
        setupCollapsingToolbar()
        setupTopImages()
        setupPdpHeader()
        setupTravelAgentItem()
        setupHotelTypeItem()
        setupCalendarItem()
        setupHotelItem()
        setupPlaneItem()
        setupRVHotels()
        setupRVAirlines()
        setupItineraries()
        setupRVFeaturedFacilities()
        setupRVNonFacilities()
        setupRVFaq()
        setupAdditionalInformation()
        initPackageAvailability()
        showData()
    }

    private fun setupFAB(){
        fab_umrah_pdp_message.bringToFront()
        fab_umrah_pdp_message.setOnClickListener {
            checkChatSession()
        }
    }

    private fun checkChatSession(){
        if (userSessionInterface.isLoggedIn) {
            context?.let {
                startChatUmroh(it)
            }
        } else {
            goToLoginPage()
        }
    }

    private fun startChatUmroh(context: Context){
        val intent = RouteManager.getIntent(context,
                ApplinkConst.TOPCHAT_ASKSELLER,
                resources.getString(R.string.umrah_shop_id), resources.getString(R.string.umrah_shop_link, umrahProduct.slugName),
                resources.getString(R.string.umrah_shop_source), resources.getString(R.string.umrah_shop_name), "")
        startActivity(intent)
    }

    private fun enableSwipeToRefresh() {
        swipeToRefresh.isRefreshing = false
        swipeToRefresh.isEnabled = true
    }

    private fun hideData() {
        container_umrah_pdp.visibility = GONE
        container_umrah_pdp_shimmering.visibility = VISIBLE
    }

    private fun showData() {
        container_umrah_pdp.visibility = VISIBLE
        container_umrah_pdp_shimmering.visibility = GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePerformance()
        slugName = arguments?.getString(EXTRA_SLUG_NAME, "")
        resetObject()
    }

    private fun initializePerformance(){
        performanceMonitoring = PerformanceMonitoring.start(UMRAH_PDP_PAGE_PERFORMANCE)
    }

    private fun resetObject() {
        umrahProduct = UmrahProductModel.UmrahProduct()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_pdp, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeToRefresh(view)

        if (umrahProduct.title != "") setupAll()
        else requestData()
    }

    private fun setupCollapsingToolbar() {
        (activity as UmrahPdpActivity).setSupportActionBar(umrah_pdp_toolbar)
        (activity as UmrahPdpActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navIcon = umrah_pdp_toolbar.navigationIcon
        context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N0) }?.let { navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP) }
        (activity as UmrahPdpActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        umrah_pdp_collapsing_toolbar.title = ""
        umrah_pdp_app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    umrah_pdp_collapsing_toolbar.title = umrahProduct.title
                    context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N700_96) }?.let { navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP) }
                    isShow = true
                } else if (isShow) {
                    umrah_pdp_collapsing_toolbar.title = ""
                    context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N0) }?.let { navIcon?.setColorFilter(it, PorterDuff.Mode.SRC_ATOP) }
                    isShow = false
                }
            }
        })
    }

    private fun setupTopImages() {
        umrahPdpImageViewPagerAdapter.apply {
            imageUrls = umrahProduct.banners
            onClickListener = object : UmrahPdpImageViewPagerAdapter.SetOnClickListener {
                override fun onClick(position: Int) {
                    umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_PACKAGE_PHOTOS)
                    context?.run {
                        startActivity(ImagePreviewSliderActivity.getCallingIntent(
                                this, umrahProduct.title, umrahPdpImageViewPagerAdapter.imageUrls, umrahPdpImageViewPagerAdapter.imageUrls, position
                        ))
                    }
                }
            }
        }
        vp_umrah_pdp_images_top.adapter = umrahPdpImageViewPagerAdapter
        tl_umrah_pdp_images_indicator.setupWithViewPager(vp_umrah_pdp_images_top, true)
    }

    private fun setupPdpHeader() {
        tg_umrah_pdp_title.text = umrahProduct.title
    }

    private fun setupTravelAgentItem() {
        val travelAgent = umrahProduct.travelAgent
        val umrahItemWidgetModel: UmrahItemWidgetModel = UmrahItemWidgetModel().apply {
            title = travelAgent.name
            imageUri = travelAgent.imageUrl
            desc = travelAgent.permissionOfUmrah
        }
        val umrahTravelAgentWidgetModel : UmrahTravelAgentWidgetModel = UmrahTravelAgentWidgetModel().apply {
            establishedSince = travelAgent.establishedSince
            pilgrimsPerYear = travelAgent.ui.pilgrimsPerYear
            availableSeat = umrahProduct.ui.availableSeat
        }

        iw_umrah_pdp_travel_agent.umrahItemWidgetModel = umrahItemWidgetModel
        iw_umrah_pdp_travel_agent.buildView()
        iw_umrah_pdp_travel_agent.setPermissionTravel()
        iw_umrah_pdp_travel_agent.setVerifiedTravel()

        uta_umrah_pdp_travel_agent.umrahTravelAgentModel = umrahTravelAgentWidgetModel
        uta_umrah_pdp_travel_agent.buildView()

        iw_umrah_pdp_travel_agent.setOnClickListener {
            umrahTrackingUtil.umrahTravelAgentClickPDP()
            RouteManager.route(context,ApplinkConst.SALAM_UMRAH_AGEN, travelAgent.slugName)
        }

    }

    private fun setupHotelTypeItem() {
        if (paramPurchase.variant == "") paramPurchase.variant = umrahProduct.variants[0].name
        val umrahPdpGreenRectWidgetModel = UmrahPdpGreenRectWidgetModel().apply {
            title = getString(R.string.umrah_pdp_hotel_type_x, paramPurchase.variant)
            imageFirstLineUrl = R.drawable.umrah_ic_pdp_person
            descFirstLine = getString(R.string.umrah_pdp_detail_total_passenger, paramPurchase.totalPassenger)
            imageActionUrl = R.drawable.umrah_ic_right
        }
        grw_umrah_pdp_hotel_type.iv_widget_umrah_pdp_green_rect_action.setPadding(3, 3, 3, 3)
        grw_umrah_pdp_hotel_type.umrahPdpGreenRectWidgetModel = umrahPdpGreenRectWidgetModel
        grw_umrah_pdp_hotel_type.buildView()
        grw_umrah_pdp_hotel_type.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CHOOSE_ROOM_TYPE_UP)
            startActivityForResult(context?.let { it1 -> UmrahPdpDetailActivity.createIntent(it1, umrahProduct.slugName, umrahProduct.availableSeat) }, REQUEST_PDP_DETAIL)
        }
    }

    private fun setupCalendarItem() {
        iw_umrah_pdp_calendar.apply {
            umrahItemWidgetModel = UmrahItemWidgetModel(
                    imageDrawable = R.drawable.umrah_ic_calendar,
                    title = umrahProduct.ui.travelDates,
                    desc = umrahProduct.ui.travelDurations)
            buildView()
        }
    }

    private fun setupHotelItem() {
        iw_umrah_pdp_hotel.apply {
            umrahItemWidgetModel = UmrahItemWidgetModel(
                    imageDrawable = R.drawable.umrah_ic_hotel,
                    title = umrahProduct.ui.hotelStars,
                    desc = umrahProduct.ui.variant)
            buildView()
        }
    }

    private fun setupPlaneItem() {
        iw_umrah_pdp_plane.apply {
            umrahItemWidgetModel = UmrahItemWidgetModel(
                    imageDrawable = R.drawable.umrah_ic_plane,
                    title = umrahProduct.airlines[0].name,
                    desc = umrahProduct.ui.transitCity
            )
            buildView()
        }
    }

    private fun setupRVHotels() {
        val hotels = umrahProduct.hotels
        umrahPdpHotelAdapter.hotels = hotels
        rv_umrah_pdp_accommodation.apply {
            isNestedScrollingEnabled = false
            adapter = umrahPdpHotelAdapter.apply {
                onImageListener = object : UmrahPdpHotelImagesAdapter.UmrahPdpHotelImagesListener {
                    override fun onImageClicked(itemPosition: Int, position: Int) {
                        umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_HOTEL_PHOTOS)
                        context?.run {
                            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                                    this, hotels[itemPosition].name, hotels[itemPosition].imageUrls, hotels[itemPosition].imageUrls, position
                            ))
                        }
                    }
                }
                onScrollListener = object : UmrahPdpHotelAdapter.OnScrollListener {
                    override fun onScrollStateChanged() {
                        if (!isHotelsScrolled) {
                            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.SCROLLING_HOTEL)
                            isHotelsScrolled = true
                        }
                    }
                }
            }
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4),
                    RecyclerView.VERTICAL))
        }
    }

    private fun setupRVAirlines() {
        umrahPdpAirlineAdapter.airlines = umrahProduct.airlines
        rv_umrah_pdp_airline.apply {
            isNestedScrollingEnabled = false
            adapter = umrahPdpAirlineAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2),
                    RecyclerView.HORIZONTAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!isAirlinesScrolled) {
                        umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.SCROLLING_PENERBANGAN)
                        isAirlinesScrolled = true
                    }
                }
            })
        }
    }

    private fun setupItineraries() {
        val maxDisplayedItineraries = 4
        iw_umrah_pdp_itineraries.apply {
            setItem(umrahProduct.itineraries, maxDisplayedItineraries)
            buildView()
            read_more.setOnClickListener {
                umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.LIHAT_ITINERARY_SELENGKAPNYA)
                openBottomSheetItinerary()
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheetItinerary() {
        val itineraryBottomSheet = BottomSheetUnify()
        itineraryBottomSheet.setCloseClickListener { itineraryBottomSheet.dismiss() }
        itineraryBottomSheet.setTitle(resources.getString(R.string.umrah_pdp_itinerary_title))
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_pdp_itinerary, null)
        view.apply {
            iw_umrah_pdp_itineraries.setItem(umrahProduct.itineraries)
            iw_umrah_pdp_itineraries.buildView()
        }
        itineraryBottomSheet.setChild(view)
        itineraryBottomSheet.show(fragmentManager!!, "")
    }

    private fun setupRVFeaturedFacilities() {
        umrahPdpFeaturedFacilityAdapter.featuredFacilities = getHardcodedData()
        rv_umrah_pdp_facilities.apply {
            isNestedScrollingEnabled = false
            adapter = umrahPdpFeaturedFacilityAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3),
                    RecyclerView.HORIZONTAL))
        }
        tg_umrah_pdp_featured_facilities_see_all.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.LIHAT_SEMUA_FASILITAS)
            openBottomSheetFacilities()
        }
    }

    private fun getHardcodedData(): ArrayList<UmrahPdpFeaturedFacilityModel> {
        val icons = resources.obtainTypedArray(R.array.umrah_pdp_list_image_facility)
        val descriptions = resources.getStringArray(R.array.umrah_pdp_list_desc_facility).toMutableList()
        val featuredFacilities = arrayListOf<UmrahPdpFeaturedFacilityModel>()
        for (i in 0 until 5) {
            val umrahPdpFeaturedFacilityModel = UmrahPdpFeaturedFacilityModel("", icons.getResourceId(i,0), descriptions[i])
            featuredFacilities.add(umrahPdpFeaturedFacilityModel)
        }
        icons.recycle()
        return featuredFacilities
    }

    @SuppressLint("InflateParams")
    private fun openBottomSheetFacilities() {
        val facilitiesBottomSheet = BottomSheetUnify()
        facilitiesBottomSheet.setCloseClickListener { facilitiesBottomSheet.dismiss() }
        facilitiesBottomSheet.setTitle(resources.getString(R.string.umrah_pdp_facilities_title))
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheets_umrah_pdp_facilities, null)
        view.bs_rv_umrah_pdp_facilities.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = umrahPdpFacilityAdapter
        }
        umrahPdpFacilityAdapter.facilities = umrahProduct.facilities
        facilitiesBottomSheet.setChild(view)
        facilitiesBottomSheet.show(fragmentManager!!, "")
    }

    private fun setupRVNonFacilities() {
        umrahPdpNonFacilityAdapter.nonFacilities = umrahProduct.nonFacilities
        rv_umrah_pdp_non_facilities.apply {
            isNestedScrollingEnabled = false
            adapter = umrahPdpNonFacilityAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun setupRVFaq() {
        umrahPdpFaqAdapter.faqs = umrahProduct.faqs.contents
        rv_umrah_pdp_faqs.apply {
            isNestedScrollingEnabled = false
            adapter = umrahPdpFaqAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            while (itemDecorationCount > 0) removeItemDecorationAt(0)
            addItemDecoration(UmrahSpaceItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2), RecyclerView.HORIZONTAL))
            addItemDecoration(UmrahPdpFaqIndicator())
            onFlingListener = null
            PagerSnapHelper().attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!isFaqsScrolled) {
                        umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.SCROLL_FAQ)
                        isFaqsScrolled = true
                    }
                }
            })
        }
        umrahPdpFaqAdapter.onItemListener = object : UmrahPdpFaqAdapter.OnClickListener {
            override fun onClick(link: String) {
                umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_FAQ)
                RouteManager.route(context, link)
            }
        }
        tg_umrah_pdp_faq_see_all.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.LIHAT_SEMUA_FAQ)
            RouteManager.route(context, umrahProduct.faqs.allContentsLink)
        }
    }

    private fun setupAdditionalInformation() {
        var bulletData = getString(R.string.umrah_pdp_additional_information_bullet, umrahProduct.additionalInformation[0])
        for (i in 1 until umrahProduct.additionalInformation.size) {
            bulletData += getString(R.string.umrah_pdp_additional_information_bullet, umrahProduct.additionalInformation[i])
        }
        ticker_umrah_pdp_additional_info.setTextDescription(bulletData)
        ticker_umrah_pdp_additional_info.tickerTitle = getString(R.string.umrah_pdp_additional_information_title)
    }

    private fun setupPriceAndPurchaseButton() {
        if (paramPurchase.totalPrice == 0) {
            paramPurchase.totalPrice = umrahProduct.originalPrice
        }
        tg_umrah_pdp_price.text = getRupiahFormat(paramPurchase.totalPrice)
        tg_umrah_pdp_price_start_from.text = getSlashedPrice(resources, umrahProduct.slashPrice)
        bt_umrah_pdp_buy_package.setOnClickListener {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CHOOSE_ROOM_TYPE_DOWN)
            startActivityForResult(context?.let { it1 -> UmrahPdpDetailActivity.createIntent(it1, umrahProduct.slugName, umrahProduct.availableSeat) }, REQUEST_PDP_DETAIL)
        }
    }

    private fun initPackageAvailability() {
        if (umrahProduct.availableSeat != 0) {
            setupPriceAndPurchaseButton()
        } else {
            showSoldOutPackage()
        }
    }

    private fun showSoldOutPackage() {
        tg_umrah_pdp_price_start_from.visibility = GONE
        tg_umrah_pdp_price.visibility = GONE
        tg_umrah_pdp_include_visa_and_equipments.visibility = GONE
        tg_umrah_pdp_empty_price_label.visibility = VISIBLE
        bt_umrah_pdp_buy_package.buttonType = UnifyButton.Type.MAIN
        bt_umrah_pdp_buy_package.text = getString(R.string.umrah_pdp_change_search)
        bt_umrah_pdp_buy_package.setOnClickListener {
            activity?.finish()
        }
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 400
        const val UMRAH_PDP_PAGE_PERFORMANCE = "sl_umrah_pdp"

        var umrahProduct: UmrahProductModel.UmrahProduct = UmrahProductModel.UmrahProduct()

        fun getInstance(slugName: String) =
                UmrahPdpFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_SLUG_NAME, slugName)
                    }
                    resetParamPurchase()
                }

        private fun resetParamPurchase() {
            paramPurchase = ParamPurchase()
        }
    }

    override fun onBackPressed() {
        if (!isDetached) {
            umrahTrackingUtil.umrahPdpAllClick(UmrahPdpTrackingUserAction.CLICK_BACK)
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        swipeToRefresh.isEnabled = umrah_pdp_collapsing_toolbar.height + verticalOffset >= 2 * ViewCompat.getMinimumHeight(umrah_pdp_collapsing_toolbar)
    }

    override fun onResume() {
        super.onResume()
        umrah_pdp_app_bar_layout.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        umrah_pdp_app_bar_layout.removeOnOffsetChangedListener(this)
    }

    private fun goToLoginPage() {
        if (activity != null) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN)
        }
    }

    override fun onDestroyView() {
        performanceMonitoring.stopTrace()
        super.onDestroyView()
    }
}