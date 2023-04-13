package com.tokopedia.hotel.homepage.presentation.fragment

import com.tokopedia.imageassets.TokopediaImageUrl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.DeeplinkMapper.getRegisteredNavigation
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.widget.TravelVideoBannerWidget
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelSourceEnum
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.common.util.MutationDeleteRecentSearch
import com.tokopedia.hotel.common.util.QueryHotelHomepageDefaultParameter
import com.tokopedia.hotel.common.util.QueryHotelRecentSearchData
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_HOMEPAGE
import com.tokopedia.hotel.databinding.FragmentHotelHomepageBinding
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPropertyDefaultHome
import com.tokopedia.hotel.homepage.di.HotelHomepageComponent
import com.tokopedia.hotel.homepage.presentation.activity.HotelHomepageActivity.Companion.HOMEPAGE_SCREEN_NAME
import com.tokopedia.hotel.homepage.presentation.adapter.HotelLastSearchAdapter
import com.tokopedia.hotel.homepage.presentation.adapter.viewholder.HotelLastSearchViewHolder
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.homepage.presentation.model.HotelRecentSearchModel
import com.tokopedia.hotel.homepage.presentation.model.viewmodel.HotelHomepageViewModel
import com.tokopedia.hotel.homepage.presentation.widget.HotelHomepagePopularCitiesWidget
import com.tokopedia.hotel.homepage.presentation.widget.HotelRoomAndGuestBottomSheets
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search_map.data.model.HotelSearchModel
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by furqan on 28/03/19
 */
class HotelHomepageFragment :
    HotelBaseFragment(),
    HotelRoomAndGuestBottomSheets.HotelGuestListener,
    HotelLastSearchViewHolder.LastSearchListener,
    TravelVideoBannerWidget.ActionListener,
    HotelHomepagePopularCitiesWidget.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var homepageViewModel: HotelHomepageViewModel
    private var mbinding: FragmentHotelHomepageBinding? = null
    private val binding get() = mbinding

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil
    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false

    private var hotelHomepageModel: HotelHomepageModel = HotelHomepageModel()

    private lateinit var remoteConfig: RemoteConfig
    private var bannerWidthInPixels = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_HOMEPAGE)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            homepageViewModel = viewModelProvider.get(HotelHomepageViewModel::class.java)
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_MODEL)) {
            hotelHomepageModel = savedInstanceState.getParcelable<HotelHomepageModel>(EXTRA_HOTEL_MODEL)
                ?: HotelHomepageModel()
        } else if (arguments != null) {
            // for applink with param searchId and searchType
            if (arguments?.containsKey(EXTRA_PARAM_SEARCH_ID) == true) {
                hotelHomepageModel.searchId = arguments?.getString(EXTRA_PARAM_SEARCH_ID) ?: ""
                hotelHomepageModel.searchType = arguments?.getString(EXTRA_PARAM_SEARCH_TYPE) ?: ""

                hotelHomepageModel.locId = 0
                hotelHomepageModel.locType = ""
            }

            // for older applink
            if (arguments?.containsKey(EXTRA_PARAM_ID) == true) {
                hotelHomepageModel.locId = arguments?.getLong(EXTRA_PARAM_ID) ?: 0
                hotelHomepageModel.locType = arguments?.getString(EXTRA_PARAM_TYPE) ?: ""
            }

            hotelHomepageModel.locName = arguments?.getString(EXTRA_PARAM_NAME) ?: ""
            hotelHomepageModel.adultCount = arguments?.getInt(EXTRA_ADULT, 1) ?: 1
            hotelHomepageModel.roomCount = arguments?.getInt(EXTRA_ROOM, 1) ?: 1
            hotelHomepageModel.checkInDate = arguments?.getString(EXTRA_PARAM_CHECKIN) ?: ""
            hotelHomepageModel.checkOutDate = arguments?.getString(EXTRA_PARAM_CHECKOUT) ?: ""

            if (hotelHomepageModel.checkInDate.isNotBlank()) {
                hotelHomepageModel.checkInDateFmt = hotelHomepageModel.checkInDate
                    .toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT)
            }
            if (hotelHomepageModel.checkOutDate.isNotBlank()) {
                hotelHomepageModel.checkOutDateFmt = hotelHomepageModel.checkOutDate
                    .toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT)
            }
            if (hotelHomepageModel.checkInDate.isNotBlank() && hotelHomepageModel.checkOutDate.isNotBlank()) {
                hotelHomepageModel.nightCounter = countRoomDuration()
            }
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
        measureBannerWidth()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mbinding = FragmentHotelHomepageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        hidePromoContainer()
        loadPromoData()
        loadPopularCitiesData()
        loadTickerData()

        if (hotelHomepageModel.locName.isEmpty()) {
            homepageViewModel.getDefaultHomepageParameter(QueryHotelHomepageDefaultParameter())
        }
    }

    override fun onResume() {
        super.onResume()

        // last search need to reload every time user back to homepage
        hideHotelLastSearchContainer()
        loadRecentSearchData()
        renderView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homepageViewModel.homepageDefaultParam.observe(
            viewLifecycleOwner,
            Observer {
                renderHotelParam(it)
            }
        )

        homepageViewModel.promoData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.banners.isNotEmpty()) {
                            renderHotelPromo(it.data.banners)
                        } else {
                            hidePromoContainer()
                        }
                    }
                }
                stopTrace()
            }
        )

        homepageViewModel.recentSearch.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        renderHotelLastSearch(it.data)
                    }
                }
            }
        )

        homepageViewModel.deleteRecentSearch.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data) {
                            loadRecentSearchData()
                        }
                    }
                }
            }
        )

        homepageViewModel.popularCitiesLiveData.observe(
            viewLifecycleOwner,
            Observer {
                fetchVideoBannerData()
                when (it) {
                    is Success -> {
                        renderPopularCities(it.data)
                    }
                    is Fail -> {
                        showPopularCitiesWidget(false)
                    }
                }
            }
        )

        homepageViewModel.videoBannerLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> renderVideoBanner(it.data)
                    is Fail -> showHotelHomepageVideoBanner(false)
                }
            }
        )

        homepageViewModel.tickerData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.message.isNotEmpty()) {
                            renderTickerView(it.data)
                        } else {
                            hideTickerView()
                        }
                    }
                    is Fail -> {
                        hideTickerView()
                    }
                }
            }
        )
    }

    override fun onVideoBannerClicked(bannerData: TravelVideoBannerModel) {
        trackingHotelUtil.hotelClickVideoBanner(context, bannerData, HOMEPAGE_SCREEN_NAME)
    }

    override fun onPopularCityClicked(popularSearch: PopularSearch) {
        RouteManager.route(
            requireContext(),
            getString(
                R.string.hotel_search_result_applink_format,
                popularSearch.type,
                popularSearch.searchId,
                popularSearch.name
            )
        )
    }

    override fun onErrorRetryClicked() {
        loadPromoData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_HOTEL_MODEL, hotelHomepageModel)
    }

    override fun initInjector() {
        getComponent(HotelHomepageComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onSaveGuest(room: Int, adult: Int) {
        trackingHotelUtil.hotelSelectRoomGuest(context, room, adult, HOMEPAGE_SCREEN_NAME)

        hotelHomepageModel.roomCount = room
        hotelHomepageModel.adultCount = adult

        renderView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DESTINATION -> if (resultCode == Activity.RESULT_OK && data != null) {
                if (data.hasExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG)) {
                    onDestinationNearBy(
                        data.getDoubleExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LANG, 0.0),
                        data.getDoubleExtra(HotelDestinationActivity.HOTEL_CURRENT_LOCATION_LAT, 0.0)
                    )
                } else if (data.hasExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID)) {
                    onDestinationChanged(
                        data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_NAME)
                            ?: "",
                        searchId = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_ID)
                            ?: "",
                        searchType = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_SEARCH_TYPE)
                            ?: "",
                        source = data.getStringExtra(HotelDestinationActivity.HOTEL_DESTINATION_RESULT_SOURCE)
                            ?: ""
                    )
                }
            }
        }
    }

    override fun onItemBind(item: TravelRecentSearchModel.Item, position: Int) {
        trackingHotelUtil.hotelLastSearchImpression(context, item, position, HOMEPAGE_SCREEN_NAME)
    }

    override fun onItemClick(item: TravelRecentSearchModel.Item, position: Int) {
        trackingHotelUtil.hotelLastSearchClick(context, item, position, HOMEPAGE_SCREEN_NAME)
    }

    private fun hideTickerView() {
        binding?.hotelHomepageTicker?.hide()
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        if (travelTickerModel.title.isNotEmpty()) binding?.hotelHomepageTicker?.tickerTitle = travelTickerModel.title
        var message = travelTickerModel.message
        if (travelTickerModel.url.isNotEmpty()) message += getString(R.string.hotel_ticker_desc, travelTickerModel.url)
        binding?.hotelHomepageTicker?.setHtmlDescription(message)
        binding?.hotelHomepageTicker?.tickerType = Ticker.TYPE_WARNING
        binding?.hotelHomepageTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl.isNotEmpty()) {
                    RouteManager.route(context, linkUrl.toString())
                }
            }

            override fun onDismiss() {}
        })
        if (travelTickerModel.url.isNotEmpty()) {
            binding?.hotelHomepageTicker?.setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        binding?.hotelHomepageTicker?.show()
    }

    private fun renderHotelParam(hotelPropertyDefaultHome: HotelPropertyDefaultHome) {
        hotelHomepageModel.checkInDate = hotelPropertyDefaultHome.checkIn
        hotelHomepageModel.checkOutDate = hotelPropertyDefaultHome.checkOut
        checkCheckInAndCheckOutDate()
        hotelHomepageModel.roomCount = hotelPropertyDefaultHome.totalRoom
        hotelHomepageModel.adultCount = hotelPropertyDefaultHome.totalGuest
        onDestinationChanged(
            hotelPropertyDefaultHome.label,
            searchId = hotelPropertyDefaultHome.searchId,
            searchType = hotelPropertyDefaultHome.searchType,
            shouldTrackChanges = false
        )
    }

    private fun measureBannerWidth() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        bannerWidthInPixels = (displayMetrics.widthPixels / BANNER_WIDTH_DIVIDER).toInt()
        context?.resources?.let {
            bannerWidthInPixels -= it.getDimensionPixelSize(R.dimen.hotel_banner_offset)
        }
    }

    private fun loadTickerData() {
        homepageViewModel.fetchTickerData()
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring?.stopTrace()
            isTraceStop = true
        }
    }

    private fun initView() {
        binding?.let {
            it.ivHotelHomepageLocation.run {
                setImageDrawable(getIconUnifyDrawable(context, IconUnify.LOCATION_FILLED, ContextCompat.getColor(context, R.color.hotel_dms_icon_color)))
            }
            it.ivHotelHomepageBackground.loadImage(HOMEPAGE_BG_IMAGE_URL)
            checkCheckInAndCheckOutDate()

            it.tvHotelHomepageDestination.textFieldInput.tag = R.id.tv_hotel_homepage_destination.toString()
            it.tvHotelHomepageDestination.textFieldInput.isClickable = true
            it.tvHotelHomepageDestination.textFieldInput.isFocusable = false
            it.tvHotelHomepageDestination.textFieldWrapper.isHelperTextEnabled = false
            it.tvHotelHomepageDestination.textFieldInput.setOnClickListener { onDestinationChangeClicked() }
            it.tvHotelHomepageDestination.textFieldInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

            it.tvHotelHomepageCheckinDate.textFieldInput.tag = R.id.tv_hotel_homepage_checkin_date.toString()
            it.tvHotelHomepageCheckinDate.textFieldInput.isClickable = true
            it.tvHotelHomepageCheckinDate.textFieldInput.isFocusable = false
            it.tvHotelHomepageCheckinDate.textFieldWrapper.isHelperTextEnabled = false
            it.tvHotelHomepageCheckinDate.textFieldInput.setOnClickListener { configAndRenderCheckInDate() }
            it.tvHotelHomepageCheckinDate.textFieldInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

            it.tvHotelHomepageCheckoutDate.textFieldInput.tag = R.id.tv_hotel_homepage_checkout_date.toString()
            it.tvHotelHomepageCheckoutDate.textFieldInput.isClickable = true
            it.tvHotelHomepageCheckoutDate.textFieldInput.isFocusable = false
            it.tvHotelHomepageCheckoutDate.textFieldWrapper.isHelperTextEnabled = false
            it.tvHotelHomepageCheckoutDate.textFieldInput.setOnClickListener { configAndRenderCheckOutDate() }
            it.tvHotelHomepageCheckoutDate.textFieldInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

            it.tvHotelHomepageGuestInfo.textFieldInput.tag = R.id.tv_hotel_homepage_guest_info.toString()
            it.tvHotelHomepageGuestInfo.textFieldInput.isClickable = true
            it.tvHotelHomepageGuestInfo.textFieldInput.isFocusable = false
            it.tvHotelHomepageGuestInfo.textFieldWrapper.isHelperTextEnabled = false
            it.tvHotelHomepageGuestInfo.textFieldInput.setOnClickListener { onGuestInfoClicked() }
            it.tvHotelHomepageGuestInfo.textFieldInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

            it.btnHotelHomepageSearch.setOnClickListener { onSearchButtonClicked() }
            it.tvHotelHomepageAllPromo.setOnClickListener { RouteManager.route(context, ApplinkConstInternalTravel.HOTEL_PROMO_LIST) }
        }
    }

    private fun checkCheckInAndCheckOutDate() {
        val updatedCheckInCheckOutDate = HotelUtils.validateCheckInAndCheckOutDate(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        hotelHomepageModel.checkInDate = updatedCheckInCheckOutDate.first
        hotelHomepageModel.checkOutDate = updatedCheckInCheckOutDate.second
        hotelHomepageModel.checkInDateFmt = hotelHomepageModel.checkInDate.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT)
        hotelHomepageModel.checkOutDateFmt = hotelHomepageModel.checkOutDate.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT)
        hotelHomepageModel.nightCounter = countRoomDuration()
    }

    private fun renderView() {
        binding?.let {
            it.btnHotelHomepageSearch.isEnabled = hotelHomepageModel.locName.isNotEmpty()
            it.tvHotelHomepageDestination.textFieldInput.setText(hotelHomepageModel.locName)
            it.tvHotelHomepageCheckinDate.textFieldInput.setText(hotelHomepageModel.checkInDateFmt)
            it.tvHotelHomepageCheckoutDate.textFieldInput.setText(hotelHomepageModel.checkOutDateFmt)
            it.tvHotelHomepageNightCount.text = String.format(getString(R.string.hotel_homepage_night_counter), hotelHomepageModel.nightCounter)
            it.tvHotelHomepageGuestInfo.textFieldInput.setText(
                String.format(
                    getString(R.string.hotel_homepage_guest_detail_without_child),
                    hotelHomepageModel.roomCount,
                    hotelHomepageModel.adultCount
                )
            )
        }
    }

    private fun renderPopularCities(popularCities: List<PopularSearch>) {
        if (popularCities.isNotEmpty()) {
            binding?.widgetHotelHomepagePopularCities?.setActionListener(this)
            binding?.widgetHotelHomepagePopularCities?.addPopularCities(popularCities)
            showPopularCitiesWidget(true)
        } else {
            showPopularCitiesWidget(false)
        }
    }

    private fun renderVideoBanner(videoBannerModel: TravelCollectiveBannerModel) {
        showHotelHomepageVideoBanner(true)
        binding?.hotelHomepageVideoBanner?.listener = this
        binding?.hotelHomepageVideoBanner?.setData(videoBannerModel)
        binding?.hotelHomepageVideoBanner?.build()
        trackingHotelUtil.hotelVideoBannerImpression(context, binding?.hotelHomepageVideoBanner?.getData() ?: TravelVideoBannerModel(), HOMEPAGE_SCREEN_NAME)
    }

    private fun showPopularCitiesWidget(show: Boolean) {
        if (show) {
            binding?.widgetHotelHomepagePopularCities?.show()
        } else {
            binding?.widgetHotelHomepagePopularCities?.hide()
        }
    }

    private fun showHotelHomepageVideoBanner(show: Boolean) {
        if (show) {
            binding?.hotelHomepageVideoBanner?.show()
        } else {
            binding?.hotelHomepageVideoBanner?.hide()
        }
    }

    private fun onDestinationChangeClicked() {
        trackingHotelUtil.hotelClickChangeDestination(context, HOMEPAGE_SCREEN_NAME)
        context?.run {
            startActivityForResult(HotelDestinationActivity.createInstance(this), REQUEST_CODE_DESTINATION)
        }
        activity?.overridePendingTransition(
            com.tokopedia.common.travel.R.anim.travel_slide_up_in,
            com.tokopedia.common.travel.R.anim.travel_anim_stay
        )
    }

    private fun configAndRenderCheckInDate() {
        openCalendarDialog(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
    }

    private fun configAndRenderCheckOutDate() {
        openCalendarDialog(checkIn = hotelHomepageModel.checkInDate)
    }

    private fun onGuestInfoClicked() {
        activity?.let {
            val hotelRoomAndGuestBottomSheets = HotelRoomAndGuestBottomSheets()
            hotelRoomAndGuestBottomSheets.listener = this
            hotelRoomAndGuestBottomSheets.roomCount = hotelHomepageModel.roomCount
            hotelRoomAndGuestBottomSheets.adultCount = hotelHomepageModel.adultCount
            hotelRoomAndGuestBottomSheets.show(it.supportFragmentManager, TAG_GUEST_INFO)
        }
    }

    private fun onCheckInDateChanged(newCheckInDate: Date) {
        hotelHomepageModel.checkInDate = newCheckInDate.toString(DateUtil.YYYY_MM_DD)
        hotelHomepageModel.checkInDateFmt = newCheckInDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)

        if (newCheckInDate >= hotelHomepageModel.checkOutDate.toDate(DateUtil.YYYY_MM_DD)) {
            val tomorrow = newCheckInDate.addTimeToSpesificDate(Calendar.DATE, 1)
            hotelHomepageModel.checkOutDate = tomorrow.toString(DateUtil.YYYY_MM_DD)
            hotelHomepageModel.checkOutDateFmt = tomorrow.toString(DateUtil.DEFAULT_VIEW_FORMAT)
        }
        hotelHomepageModel.nightCounter = countRoomDuration()

        renderView()
    }

    private fun onCheckOutDateChanged(newCheckOutDate: Date) {
        hotelHomepageModel.checkOutDate = newCheckOutDate.toString(DateUtil.YYYY_MM_DD)
        hotelHomepageModel.checkOutDateFmt = newCheckOutDate.toString(DateUtil.DEFAULT_VIEW_FORMAT)
        hotelHomepageModel.nightCounter = countRoomDuration()

        trackRoomDates()
        renderView()
    }

    private fun trackRoomDates() {
        val dateRange = DateUtil.getDayDiff(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        trackingHotelUtil.hotelSelectStayDate(context, hotelHomepageModel.checkInDate, dateRange.toInt(), HOMEPAGE_SCREEN_NAME)
    }

    private fun onDestinationNearBy(longitude: Double, latitude: Double) {
        hotelHomepageModel.locName = getString(R.string.hotel_homepage_near_by_destination)
        hotelHomepageModel.locLat = latitude
        hotelHomepageModel.locLong = longitude
        hotelHomepageModel.locId = 0
        hotelHomepageModel.locType = ""
        hotelHomepageModel.searchType = HotelTypeEnum.COORDINATE.value
        hotelHomepageModel.searchId = ""
        renderView()
    }

    private fun onDestinationChanged(
        name: String,
        id: Long = 0,
        type: String = "",
        searchType: String = "",
        searchId: String = "",
        source: String = "",
        shouldTrackChanges: Boolean = true
    ) {
        if (shouldTrackChanges) {
            val tempType = if (searchType.isNotEmpty()) searchType else type
            trackingHotelUtil.hotelSelectDestination(context, tempType, name, source, HOMEPAGE_SCREEN_NAME)
        }

        hotelHomepageModel.locName = name
        hotelHomepageModel.locId = id
        hotelHomepageModel.locType = type
        hotelHomepageModel.locLat = 0.0
        hotelHomepageModel.locLong = 0.0
        hotelHomepageModel.searchType = searchType
        hotelHomepageModel.searchId = searchId
        renderView()
    }

    private fun onSearchButtonClicked() {
        val type = if (hotelHomepageModel.searchType.isNotEmpty()) {
            hotelHomepageModel.searchType
        } else {
            hotelHomepageModel.locType
        }
        trackingHotelUtil.searchHotel(
            context,
            type,
            hotelHomepageModel.locName,
            hotelHomepageModel.roomCount,
            hotelHomepageModel.adultCount,
            hotelHomepageModel.checkInDate,
            hotelHomepageModel.nightCounter.toInt(),
            HOMEPAGE_SCREEN_NAME
        )

        context?.run {
            when {
                hotelHomepageModel.locType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                    startActivityForResult(
                        HotelDetailActivity.getCallingIntent(
                            this,
                            hotelHomepageModel.checkInDate,
                            hotelHomepageModel.checkOutDate,
                            hotelHomepageModel.locId,
                            hotelHomepageModel.roomCount,
                            hotelHomepageModel.adultCount,
                            hotelHomepageModel.locType,
                            hotelHomepageModel.locName,
                            source = HotelSourceEnum.HOMEPAGE.value
                        ),
                        REQUEST_CODE_DETAIL
                    )
                }

                hotelHomepageModel.searchType.equals(HotelTypeEnum.PROPERTY.value, false) -> {
                    startActivityForResult(
                        HotelDetailActivity.getCallingIntent(
                            this,
                            hotelHomepageModel.checkInDate,
                            hotelHomepageModel.checkOutDate,
                            hotelHomepageModel.searchId.toLong(),
                            hotelHomepageModel.roomCount,
                            hotelHomepageModel.adultCount,
                            hotelHomepageModel.searchType,
                            hotelHomepageModel.locName,
                            source = HotelSourceEnum.HOMEPAGE.value
                        ),
                        REQUEST_CODE_DETAIL
                    )
                }

                else -> {
                    val hotelSearchModel = HotelSearchModel(
                        name = hotelHomepageModel.locName,
                        id = hotelHomepageModel.locId,
                        type = hotelHomepageModel.locType,
                        lat = hotelHomepageModel.locLat,
                        long = hotelHomepageModel.locLong,
                        checkIn = hotelHomepageModel.checkInDate,
                        checkOut = hotelHomepageModel.checkOutDate,
                        room = hotelHomepageModel.roomCount,
                        adult = hotelHomepageModel.adultCount,
                        searchType = hotelHomepageModel.searchType,
                        searchId = hotelHomepageModel.searchId
                    )
                    startActivityForResult(HotelSearchMapActivity.createIntent(this, hotelSearchModel), REQUEST_CODE_SEARCH)
                }
            }
        }
    }

    private fun countRoomDuration(): Long = DateUtil.getDayDiff(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)

    private fun loadPromoData() {
        homepageViewModel.getHotelPromo()
    }

    private fun loadPopularCitiesData() {
        homepageViewModel.getPopularCitiesData()
    }

    private fun fetchVideoBannerData() {
        homepageViewModel.fetchVideoBannerData()
    }

    private fun loadRecentSearchData() {
        homepageViewModel.getRecentSearch(QueryHotelRecentSearchData())
    }

    val bannerImpressionIndex: HashSet<Int> = hashSetOf()

    private fun renderHotelPromo(promoDataList: List<TravelCollectiveBannerModel.Banner>) {
        showPromoContainer()
        bannerImpressionIndex.add(0)
        trackingHotelUtil.hotelBannerImpression(context, promoDataList.firstOrNull(), 0, HOMEPAGE_SCREEN_NAME)

        binding?.bannerHotelHomepagePromo?.apply {
            freeMode = false
            centerMode = true
            slideToScroll = 1
            indicatorPosition = CarouselUnify.INDICATOR_BL

            if (promoDataList.size == 1) {
                autoplay = false
                infinite = false
                slideToShow = 1.0f
                setMargin(left = 12.toPx(), top = 8.toPx(), bottom = 0, right = 12.toPx())
            } else {
                slideToShow = SLIDE_TO_SHOW
                autoplay = true
                infinite = true
            }

            onActiveIndexChangedListener = object : CarouselUnify.OnActiveIndexChangedListener {
                override fun onActiveIndexChanged(prev: Int, current: Int) {
                    if (!bannerImpressionIndex.contains(current)) {
                        bannerImpressionIndex.add(current)
                        trackingHotelUtil.hotelBannerImpression(context, promoDataList.getOrNull(current), current, HOMEPAGE_SCREEN_NAME)
                    }
                }
            }

            val itemParam = { view: View, data: Any ->
                data as TravelCollectiveBannerModel.Banner

                val image = view.findViewById<ImageUnify>(R.id.hotelPromoImageCarousel)
                if (bannerWidthInPixels > 0) {
                    image.layoutParams.height = (BANNER_HEIGHT_RATIO * bannerWidthInPixels).toInt()
                    image.layoutParams.width = bannerWidthInPixels
                } else {
                    image.layoutParams.height = resources.getDimensionPixelSize(R.dimen.hotel_banner_height)
                    image.layoutParams.width = resources.getDimensionPixelSize(R.dimen.hotel_banner_width)
                }
                image.loadImage(data.attribute.imageUrl)
                image.setOnClickListener {
                    onPromoClicked(data, data.position)
                }
            }

            promoDataList.forEachIndexed { index, banner -> banner.position = index }
            addItems(R.layout.hotel_carousel_item, ArrayList(promoDataList), itemParam)
        }
    }

    private fun renderHotelLastSearch(data: HotelRecentSearchModel) {
        if (data.items.isEmpty()) {
            hideHotelLastSearchContainer()
            return
        }

        showHotelLastSearchContainer()

        binding?.tvHotelLastSearchTitle?.text = data.title
        binding?.tvHotelHomepageDeleteLastSearch?.setOnClickListener {
            homepageViewModel.deleteRecentSearch(MutationDeleteRecentSearch())
        }
        binding?.rvHotelHomepageLastSearch?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding?.rvHotelHomepageLastSearch?.adapter = HotelLastSearchAdapter(data.items, this)
    }

    private fun openCalendarDialog(checkIn: String? = null, checkOut: String? = null) {
        val minSelectDateFromToday = SelectionRangeCalendarWidget.DEFAULT_MIN_SELECTED_DATE_TODAY
        val hotelCalendarDialog = SelectionRangeCalendarWidget.getInstance(
            checkIn,
            checkOut,
            SelectionRangeCalendarWidget.DEFAULT_RANGE_CALENDAR_YEAR,
            SelectionRangeCalendarWidget.DEFAULT_RANGE_DATE_SELECTED_ONE_MONTH.toLong(),
            getString(R.string.hotel_min_date_label),
            getString(R.string.hotel_max_date_label),
            minSelectDateFromToday
        )

        hotelCalendarDialog.listener = object : SelectionRangeCalendarWidget.OnDateClickListener {
            override fun onDateClick(dateIn: Date, dateOut: Date) {
                onCheckInDateChanged(dateIn)
                onCheckOutDateChanged(dateOut)
            }
        }
        hotelCalendarDialog.listenerMaxRange = object : SelectionRangeCalendarWidget.OnNotifyMaxRange {
            override fun onNotifyMax() {
                Toast.makeText(context, R.string.hotel_calendar_error_max_range, Toast.LENGTH_SHORT).show()
            }
        }
        fragmentManager?.let { hotelCalendarDialog.show(it, "test") }
    }

    private fun showPromoContainer() {
        binding?.hotelContainerPromo?.visibility = View.VISIBLE
    }

    private fun hidePromoContainer() {
        binding?.hotelContainerPromo?.visibility = View.GONE
    }

    private fun onPromoClicked(promo: TravelCollectiveBannerModel.Banner?, position: Int) {
        promo?.let {
            context?.let { contextNotNull ->
                trackingHotelUtil.hotelClickBanner(contextNotNull, it, position, HOMEPAGE_SCREEN_NAME)
                when {
                    RouteManager.isSupportApplink(contextNotNull, it.attribute.appUrl) -> {
                        RouteManager.route(contextNotNull, it.attribute.appUrl)
                    }
                    getRegisteredNavigation(contextNotNull, it.attribute.appUrl).isNotEmpty() -> {
                        RouteManager.route(contextNotNull, getRegisteredNavigation(contextNotNull, it.attribute.appUrl))
                    }
                    it.attribute.webUrl.isNotEmpty() -> {
                        RouteManager.route(contextNotNull, it.attribute.webUrl)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun showHotelLastSearchContainer() {
        binding?.hotelContainerLastSearch?.visibility = View.VISIBLE
    }

    private fun hideHotelLastSearchContainer() {
        binding?.hotelContainerLastSearch?.visibility = View.GONE
    }

    override fun onDestroyView() {
        binding?.bannerHotelHomepagePromo?.timer?.cancel()
        super.onDestroyView()
        mbinding = null
    }

    companion object {
        const val REQUEST_CODE_DESTINATION = 101
        const val REQUEST_CODE_SEARCH = 102
        const val REQUEST_CODE_DETAIL = 103

        const val EXTRA_HOTEL_MODEL = "EXTRA_HOTEL_MODEL"

        // banner height ratio 414:139
        const val BANNER_HEIGHT_RATIO = 0.336f
        const val BANNER_WIDTH_DIVIDER = 1.1

        const val SLIDE_TO_SHOW = 1.1f

        const val EXTRA_PARAM_SEARCH_ID = "param_search_id"
        const val EXTRA_PARAM_SEARCH_TYPE = "param_search_type"
        const val EXTRA_PARAM_NAME = "param_name"
        const val EXTRA_PARAM_CHECKIN = "param_check_in"
        const val EXTRA_PARAM_CHECKOUT = "param_check_out"
        const val EXTRA_ADULT = "param_adult"
        const val EXTRA_ROOM = "param_room"

        // for older applink
        const val EXTRA_PARAM_ID = "param_id"
        const val EXTRA_PARAM_TYPE = "param_type"

        const val TAG_GUEST_INFO = "guestHotelInfo"

        const val HOMEPAGE_BG_IMAGE_URL = TokopediaImageUrl.HOMEPAGE_BG_IMAGE_URL

        fun getInstance(): HotelHomepageFragment = HotelHomepageFragment()

        fun getInstance(searchId: String, name: String, searchType: String, checkIn: String, checkOut: String, adult: Int, room: Int): HotelHomepageFragment =
            HotelHomepageFragment().also {
                it.arguments = Bundle().apply {
                    putString(EXTRA_PARAM_SEARCH_ID, searchId)
                    putString(EXTRA_PARAM_NAME, name)
                    putString(EXTRA_PARAM_SEARCH_TYPE, searchType)
                    if (checkIn.isNotBlank()) putString(EXTRA_PARAM_CHECKIN, checkIn)
                    if (checkOut.isNotBlank()) putString(EXTRA_PARAM_CHECKOUT, checkOut)
                    if (adult != 0) putInt(EXTRA_ADULT, adult)
                    if (room != 0) putInt(EXTRA_ROOM, room)
                }
            }

        // for older applink
        fun getInstance(id: Long, name: String, type: String, checkIn: String, checkOut: String, adult: Int, room: Int): HotelHomepageFragment =
            HotelHomepageFragment().also {
                it.arguments = Bundle().apply {
                    putLong(EXTRA_PARAM_ID, id)
                    putString(EXTRA_PARAM_NAME, name)
                    putString(EXTRA_PARAM_TYPE, type)
                    if (checkIn.isNotBlank()) putString(EXTRA_PARAM_CHECKIN, checkIn)
                    if (checkOut.isNotBlank()) putString(EXTRA_PARAM_CHECKOUT, checkOut)
                    if (adult != 0) putInt(EXTRA_ADULT, adult)
                    if (room != 0) putInt(EXTRA_ROOM, room)
                }
            }
    }
}
