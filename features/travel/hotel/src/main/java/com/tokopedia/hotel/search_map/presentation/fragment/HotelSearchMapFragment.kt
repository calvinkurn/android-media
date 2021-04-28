package com.tokopedia.hotel.search_map.presentation.fragment

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.util.HotelSearchMapQuery
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.*
import com.tokopedia.hotel.search.data.model.FilterV2.Companion.FILTER_TYPE_SORT
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultAdapter
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search.presentation.widget.HotelFilterBottomSheets
import com.tokopedia.hotel.search.presentation.widget.SubmitFilterListener
import com.tokopedia.hotel.search_map.data.HotelLoadingModel
import com.tokopedia.hotel.search_map.di.HotelSearchMapComponent
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity.Companion.SEARCH_SCREEN_NAME
import com.tokopedia.hotel.search_map.presentation.viewmodel.HotelSearchMapViewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setHeadingText
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_hotel_search_map.*
import javax.inject.Inject

/**
 * @author by furqan on 01/03/2021
 */
class HotelSearchMapFragment : BaseListFragment<Property, PropertyAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, HotelSearchResultAdapter.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener, SubmitFilterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var hotelSearchMapViewModel: HotelSearchMapViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    private lateinit var localCacheHandler: LocalCacheHandler

    private lateinit var adapterCardList: HotelSearchResultAdapter

    private lateinit var googleMap: GoogleMap

    private var searchDestinationName = ""
    private var searchDestinationType = ""
    private var allMarker: ArrayList<Marker> = ArrayList()
    private var markerCounter: Int = INIT_MARKER_TAG
    private var cardListPosition: Int = SELECTED_POSITION_INIT
    private var hotelSearchModel: HotelSearchModel = HotelSearchModel()
    private var isFirstInitializeFilter = true
    private var quickFilters: List<QuickFilter> = listOf()
    private var searchPropertiesMap: ArrayList<LatLng> = arrayListOf()
    private var isSearchByMap: Boolean = false
    private var lastHorizontalTrackingPositionSent: Int = -1

    private var isViewFullMap: Boolean = false

    private lateinit var filterBottomSheet: HotelFilterBottomSheets
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val snapHelper: SnapHelper = LinearSnapHelper()

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun createAdapterInstance(): BaseListAdapter<Property, PropertyAdapterTypeFactory> =
            HotelSearchResultAdapter(this, adapterTypeFactory)

    override fun initInjector() {
        getComponent(HotelSearchMapComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvVerticalPropertiesHotelSearchMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            hotelSearchMapViewModel = viewModelProvider.get(HotelSearchMapViewModel::class.java)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            permissionCheckerHelper = PermissionCheckerHelper()
        }

        arguments?.let {
            val selectedParam = it.getParcelable(ARG_FILTER_PARAM) ?: ParamFilterV2()
            if (selectedParam.name.isNotEmpty()) {
                hotelSearchMapViewModel.addFilter(listOf(selectedParam), false)
            }

            hotelSearchModel = it.getParcelable(ARG_HOTEL_SEARCH_MODEL) ?: HotelSearchModel()
            hotelSearchMapViewModel.initSearchParam(hotelSearchModel)

            searchDestinationName = hotelSearchModel.name
            searchDestinationType = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
        }

        activity?.let {
            (it as HotelSearchMapActivity).setSupportActionBar(headerHotelSearchMap)
        }

        setCardListViewAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hotelSearchMapViewModel.liveSearchResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    showCollapsingHeader()
                    onSuccessGetResult(it.data)
                    if (!it.data.properties.isNullOrEmpty()) {
                        changeMarkerState(cardListPosition)
                    } else {
                        hideLoader()
                        hideLoadingCardListMap()
                        hideLoading()
                    }
                }
                is Fail -> {
                    hideLoader()
                    hideCollapsingHeader()
                    hideSearchWithMap()
                    hideQuickFilter()
                    expandBottomSheet()
                    showGetListError(it.throwable)
                }
            }
        })

        hotelSearchMapViewModel.latLong.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    addMyLocation(LatLng(it.data.second, it.data.first))
                    hotelSearchModel.apply {
                        searchType = HotelTypeEnum.COORDINATE.value
                        searchId = ""
                        name = getString(R.string.hotel_header_title_nearby)
                        lat = it.data.second
                        long = it.data.first
                    }
                    hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
                    removeAllMarker()
                    showCardListView()
                    hideFindNearHereView()
                    changeHeaderTitle()
                    loadInitialData()
                }
                is Fail -> {
                    if (it.throwable.message.isNullOrEmpty()) {
                        checkGps()
                    }
                }
            }
        })

        hotelSearchMapViewModel.radius.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    hotelSearchModel.apply {
                        searchType = HotelTypeEnum.COORDINATE.value
                        searchId = ""
                        name = if (isSearchByMap) getString(R.string.hotel_header_title_nearby) else getString(R.string.hotel_header_title_nearby_area)
                        radius = it.data
                    }
                    hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
                    loadInitialData()
                }
                is Fail -> {
                    Toaster.build(requireView(), it.throwable.message.toString(), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                }
            }
        })

        hotelSearchMapViewModel.screenMidPoint.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    hotelSearchModel.apply {
                        long = it.data.longitude
                        lat = it.data.latitude
                    }
                }
            }
        })

        hotelSearchMapViewModel.liveSelectedFilter.observe(viewLifecycleOwner, Observer { (data, notifyUi) ->
            if (notifyUi) {
                hideFindNearHereView()
                showQuickFilterShimmering(true)
                setupQuickFilterBaseOnSelectedFilter(data)
                loadInitialData()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHANGE_SEARCH_HOTEL) {
            data?.let {
                hotelSearchModel = HotelSearchModel(
                        id = it.getLongExtra(HotelChangeSearchActivity.DESTINATION_ID, 0),
                        name = it.getStringExtra(HotelChangeSearchActivity.DESTINATION_NAME) ?: "",
                        type = it.getStringExtra(HotelChangeSearchActivity.DESTINATION_TYPE) ?: "",
                        lat = it.getDoubleExtra(HotelChangeSearchActivity.DESTINATION_LAT, 0.0),
                        long = it.getDoubleExtra(HotelChangeSearchActivity.DESTINATION_LONG, 0.0),
                        checkIn = it.getStringExtra(HotelChangeSearchActivity.CHECK_IN_DATE) ?: "",
                        checkOut = it.getStringExtra(HotelChangeSearchActivity.CHECK_OUT_DATE)
                                ?: "",
                        room = it.getIntExtra(HotelChangeSearchActivity.NUM_OF_ROOMS, 1),
                        adult = it.getIntExtra(HotelChangeSearchActivity.NUM_OF_GUESTS, 0),
                        searchType = it.getStringExtra(HotelChangeSearchActivity.SEARCH_TYPE) ?: "",
                        searchId = it.getStringExtra(HotelChangeSearchActivity.SEARCH_ID) ?: "")
                hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
                searchDestinationName = hotelSearchModel.name
                searchDestinationType = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type

                setUpTitleAndSubtitle()
                hotelSearchMapViewModel.searchParam.page = defaultInitialPage
                hotelSearchMapViewModel.addFilter(listOf())
                isSearchByMap = false
            }
        } else if (requestCode == REQUEST_CODE_GPS) {
            getCurrentLocation()
        }
    }

    override fun isLoadMoreEnabledByDefault(): Boolean = true

    override fun isAutoLoadEnabled(): Boolean = true

    override fun getMinimumScrollableNumOfItems(): Int = MINIMUM_NUMBER_OF_RESULT_LOADED

    override fun loadInitialData() {
        hotelSearchMapViewModel.searchParam.page = defaultInitialPage
        tvHotelSearchListTitleLoader.visible()
        tvHotelSearchListTitle.gone()

        cardListPosition = SELECTED_POSITION_INIT
        adapterCardList.clearAllElements()
        removeAllMarker()

        hideErrorNoResult()
        showHotelResultList()
        showLoader()
        super.loadInitialData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewMap()
        initFloatingButton()
        initLocationMap()
        setupToolbarAction()
        setUpTitleAndSubtitle()
        setupFindWithMapButton()
        hideFindNearHereView()
        initGetMyLocation()
        setupPersistentBottomSheet()
        halfExpandBottomSheet()

        ivHotelSearchMapNoResult.loadImage(getString(R.string.hotel_url_empty_search_map_result))

        trackingHotelUtil.viewHotelSearchMap(context,
                searchDestinationName,
                searchDestinationType,
                hotelSearchMapViewModel.searchParam,
                SEARCH_SCREEN_NAME)
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        setGoogleMap()
    }

    override fun onCameraMove() {
        showFindNearHereView()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        allMarker.forEach {
            if (it.tag == marker.tag) {
                cardListPosition = it.tag as Int
                rvHorizontalPropertiesHotelSearchMap.scrollToPosition(cardListPosition)
                changeMarkerState(cardListPosition)
            }
        }
        return true
    }

    override fun onItemClicked(property: Property, position: Int) {
        with(hotelSearchMapViewModel.searchParam) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                trackingHotelUtil.chooseHotelFromMap(
                        context,
                        searchDestinationName,
                        searchDestinationType,
                        this,
                        property,
                        position,
                        SEARCH_SCREEN_NAME)
            } else {
                trackingHotelUtil.chooseHotel(
                        context,
                        searchDestinationName,
                        searchDestinationType,
                        this,
                        property,
                        position,
                        SEARCH_SCREEN_NAME)
            }

            context?.run {
                startActivityForResult(HotelDetailActivity.getCallingIntent(this,
                        checkIn, checkOut, property.id, room, guest.adult,
                        searchDestinationType, searchDestinationName, property.isDirectPayment),
                        REQUEST_CODE_DETAIL_HOTEL)
            }
        }
    }

    override fun loadData(page: Int) {
        val searchQuery = HotelSearchMapQuery.propertySearchInput
        hotelSearchMapViewModel.searchProperty(page, searchQuery)
    }

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory = PropertyAdapterTypeFactory(this)

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {}

    override fun onItemClicked(t: Property) {}

    override fun showLoading() {
        if (adapterCardList.dataSize <= MINIMUM_NUMBER_OF_RESULT_LOADED) {
            if (isLoadingInitialData) {
                adapterCardList.clearAllElements()
                adapterCardList.addElement(HotelLoadingModel(isForHorizontalItem = true))
                hideGetMyLocation()
            }
        }
        hideGetMyLocation()
        setupPersistentBottomSheet()

        super.showLoading()

        if (adapter.list.size > 0 && adapter.list[0] is LoadingModel) {
            hideSearchWithMap()
        }
    }

    override fun hideLoading() {
        tvHotelSearchListTitleLoader.gone()
        tvHotelSearchListTitle.visible()
        super.hideLoading()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper.onRequestPermissionsResult(it,
                        requestCode, permissions,
                        grantResults)
            }
        }
    }

    override fun onDestroyView() {
        googleMap.clear()
        super.onDestroyView()
    }

    override fun onSubmitFilter(selectedFilter: MutableList<ParamFilterV2>) {
        trackingHotelUtil.clickSubmitFilterOnBottomSheet(context, SEARCH_SCREEN_NAME, selectedFilter)

        var sortIndex: Int? = null
        selectedFilter.forEachIndexed { index, it ->
            if (it.name == FILTER_TYPE_SORT) {
                sortIndex = index
            }
        }

        sortIndex?.let { index ->
            val sort = findSortValue(selectedFilter[index])
            sort?.let { hotelSearchMapViewModel.addSort(it) }
            selectedFilter.removeAt(index)
        }

        hotelSearchMapViewModel.addFilter(selectedFilter)
    }

    override fun onGetListErrorWithEmptyData(throwable: Throwable?) {
        super.onGetListErrorWithEmptyData(throwable)
        bottomSheetBehavior.peekHeight = hotel_search_map_bottom_sheet.measuredHeight
        hideSearchWithMap()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        bottomSheetBehavior.peekHeight = hotel_search_map_bottom_sheet.measuredHeight
        hideSearchWithMap()
        return super.getEmptyDataViewModel()
    }

    private fun setupToolbarAction() {
        context?.let {
            val wrapper = LinearLayout(it).apply {
                val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                layoutParams = param
            }

            val textView = Typography(it)
            val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            textView.layoutParams = param
            textView.text = resources.getString(R.string.hotel_search_result_change)
            textView.fontType = Typography.BODY_2
            textView.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500))

            wrapper.addView(textView)
            wrapper.setOnClickListener {
                changeSearchParameter()
            }

            headerHotelSearchMap.addCustomRightContent(wrapper)
            headerHotelSearchMap.isShowBackButton = true
            headerHotelSearchMap.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setupPersistentBottomSheet() {
        if (!::bottomSheetBehavior.isInitialized) {
            bottomSheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(hotel_search_map_bottom_sheet)
        }
        bottomSheetBehavior.isFitToContents = false

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        showSearchWithMap()
                        setupContentMargin(true)
                        googleMap.uiSettings.setAllGesturesEnabled(false)

                        if (isViewFullMap) {
                            trackingHotelUtil.searchCloseMap(context,
                                    searchDestinationName,
                                    searchDestinationType,
                                    SEARCH_SCREEN_NAME)
                            isViewFullMap = false
                        }
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(MAPS_ZOOM_OUT))
                        setupContentMargin(false)
                        googleMap.uiSettings.setAllGesturesEnabled(false)

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(MAPS_ZOOM_IN))
                        setupContentMargin(false)
                        googleMap.uiSettings.setAllGesturesEnabled(true)

                        if (!isViewFullMap) {
                            trackingHotelUtil.searchViewFullMap(context,
                                    searchDestinationName,
                                    searchDestinationType,
                                    SEARCH_SCREEN_NAME)
                            isViewFullMap = true
                        }
                    }
                    else -> {
                        setupContentMargin(false)
                    }
                }
            }

        })

        tvHotelSearchListTitle.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tvHotelSearchListTitle.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val titleLayoutParam = tvHotelSearchListTitle.layoutParams as ViewGroup.MarginLayoutParams

                var bottomSheetHeaderHeight = tvHotelSearchListTitle.measuredHeight // title height
                bottomSheetHeaderHeight = bottomSheetHeaderHeight.plus(resources.getDimensionPixelSize(
                        com.tokopedia.unifycomponents.R.dimen.bottom_sheet_knob_height)) // knob height
                bottomSheetHeaderHeight = bottomSheetHeaderHeight.plus(resources.getDimensionPixelSize(
                        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)) // knob top margin
                bottomSheetHeaderHeight = bottomSheetHeaderHeight.plus(titleLayoutParam.topMargin) // title top margin
                bottomSheetHeaderHeight = bottomSheetHeaderHeight.plus(resources.getDimensionPixelSize(
                        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)) // add margin

                bottomSheetBehavior.peekHeight = bottomSheetHeaderHeight
            }
        })
    }

    private fun setupContentMargin(isExpanded: Boolean) {
        if (isExpanded) {
            rvVerticalPropertiesHotelSearchMap.setMargin(0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7),
                    0,
                    0)
            containerEmptyResultState.setMargin(0,
                    resources.getDimensionPixelSize(R.dimen.hotel_80dp),
                    0,
                    0)
        } else {
            rvVerticalPropertiesHotelSearchMap.setMargin(0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3),
                    0,
                    0)
            containerEmptyResultState.setMargin(0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4),
                    0,
                    0)
        }

        if (isHotelListShowingError()) {
            hideSearchWithMap()
            rvVerticalPropertiesHotelSearchMap.setMargin(0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7),
                    0,
                    0)
        }

        rvVerticalPropertiesHotelSearchMap.requestLayout()
        containerEmptyResultState.requestLayout()
    }

    private fun setUpTitleAndSubtitle() {
        context?.let {
            val hotelSearchModel = hotelSearchMapViewModel.hotelSearchModel
            val checkInString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelSearchModel.checkIn))
            val checkOutString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelSearchModel.checkOut))

            headerHotelSearchMap.title = hotelSearchModel.name
            headerHotelSearchMap.subtitle = getString(R.string.template_search_subtitle,
                    checkInString,
                    checkOutString,
                    hotelSearchModel.room,
                    hotelSearchModel.adult)
            headerHotelSearchMap.subheaderView?.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        }
    }

    private fun changeSearchParameter() {
        context?.let {
            val type = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
            trackingHotelUtil.hotelClickChangeSearch(it, type,
                    hotelSearchModel.name, hotelSearchModel.room, hotelSearchModel.adult,
                    hotelSearchModel.checkIn, hotelSearchModel.checkOut, SEARCH_SCREEN_NAME)
            startActivityForResult(HotelChangeSearchActivity.getIntent(it,
                    hotelSearchModel.id,
                    hotelSearchModel.name,
                    hotelSearchModel.type,
                    hotelSearchModel.lat.toDouble(),
                    hotelSearchModel.long.toDouble(),
                    hotelSearchModel.checkIn,
                    hotelSearchModel.checkOut,
                    hotelSearchModel.adult,
                    hotelSearchModel.room,
                    hotelSearchModel.searchId,
                    hotelSearchModel.searchType,
                    getString(R.string.hotel_search_result_change_toolbar_title)),
                    REQUEST_CHANGE_SEARCH_HOTEL)
        }
    }

    private fun setCardListViewAdapter() {
        adapterCardList = HotelSearchResultAdapter(this, adapterTypeFactory)
    }

    private fun initRecyclerViewMap() {
        rvHorizontalPropertiesHotelSearchMap.adapter = adapterCardList
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvHorizontalPropertiesHotelSearchMap.layoutManager = linearLayoutManager

        if (rvHorizontalPropertiesHotelSearchMap.onFlingListener == null) {
            snapHelper.attachToRecyclerView(rvHorizontalPropertiesHotelSearchMap)
        }

        initScrollCardList()
    }

    private fun initScrollCardList() {
        rvHorizontalPropertiesHotelSearchMap.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    cardListPosition = getCurrentItemCardList()
                    changeMarkerState(cardListPosition)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val currentPosition = getCurrentItemCardList()
                if (currentPosition != -1 &&
                        currentPosition != lastHorizontalTrackingPositionSent &&
                        adapterCardList.data[currentPosition] is Property) {

                    lastHorizontalTrackingPositionSent = currentPosition
                    trackingHotelUtil.hotelViewHotelListMapImpression(context,
                            searchDestinationName,
                            searchDestinationType,
                            hotelSearchMapViewModel.searchParam,
                            listOf(adapterCardList.data[currentPosition]),
                            adapterCardList.dataSize,
                            SEARCH_SCREEN_NAME)

                }
            }
        })
    }

    private fun getCurrentItemCardList(): Int =
            (rvHorizontalPropertiesHotelSearchMap.layoutManager as LinearLayoutManager)
                    .findFirstCompletelyVisibleItemPosition()

    private fun setGoogleMap() {
        if (::googleMap.isInitialized) {
            googleMap.uiSettings.isMapToolbarEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = true
            googleMap.uiSettings.isScrollGesturesEnabled = true

            googleMap.setOnMarkerClickListener(this)
            googleMap.setOnCameraMoveListener(this)

            mapHotelSearchMap.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> v.performClick()
                    }
                    return true
                }
            })
            mapHotelSearchMap.setOnClickListener {
                collapseBottomSheet()
            }
            googleMap.setOnMapClickListener {
                collapseBottomSheet()
            }
        }
    }

    private fun initLocationMap() {
        if (mapHotelSearchMap != null) {
            mapHotelSearchMap.onCreate(null)
            mapHotelSearchMap.onResume()
            mapHotelSearchMap.getMapAsync(this)
        }
        setGoogleMap()
    }

    private fun initFloatingButton() {
        context?.let {
            val wrapper = LinearLayout(it)
            wrapper.gravity = Gravity.CENTER

            val imageView = ImageView(it)
            imageView.setPadding(BUTTON_RADIUS_PADDING_ALL, BUTTON_RADIUS_PADDING_ALL, BUTTON_RADIUS_PADDING_RIGHT, BUTTON_RADIUS_PADDING_ALL)
            imageView.setImageResource(R.drawable.ic_hotel_search_map_search)
            wrapper.addView(imageView)

            val textView = Typography(it)
            textView.apply {
                setHeadingText(BUTTON_RADIUS_HEADING_SIZE)
                setTextColor(ContextCompat.getColor(context, R.color.hotel_color_active_price_marker))
                text = getString(R.string.hotel_search_map_around_here)
            }
            wrapper.addView(textView)
            wrapper.setOnClickListener {
                trackingHotelUtil.searchNearLocation(context,
                        searchDestinationName,
                        searchDestinationType,
                        SEARCH_SCREEN_NAME)

                onSearchByMap()
            }

            btnGetRadiusHotelSearchMap.addItem(wrapper)
        }
        btnGetRadiusHotelSearchMap.setMargins(0, resources.getDimensionPixelSize(R.dimen.hotel_70dp), 0, 0)
        btnGetRadiusHotelSearchMap.gone()
    }

    private fun onSearchByMap() {
        removeAllMarker()
        showCardListView()
        hideFindNearHereView()
        changeHeaderTitle()
        hotelSearchMapViewModel.getMidPoint(googleMap.cameraPosition.target)
        hotelSearchMapViewModel.getVisibleRadius(googleMap)
    }

    private fun addMyLocation(latLong: LatLng) {
        googleMap.addMarker(MarkerOptions().position(latLong)
                .icon(bitmapDescriptorFromVector(requireContext(), getPin(MY_LOCATION_PIN)))
                .anchor(ANCHOR_MARKER_X, ANCHOR_MARKER_Y)
                .draggable(false))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLong))

        removeAllMarker()
    }

    private fun addMarker(latitude: Double, longitude: Double, price: String) {
        val latLng = LatLng(latitude, longitude)

        context?.run {
            val marker = googleMap.addMarker(MarkerOptions().position(latLng).icon(createCustomMarker(this, HOTEL_PRICE_INACTIVE_PIN, price))
                    .title(price)
                    .anchor(ANCHOR_MARKER_X, ANCHOR_MARKER_Y)
                    .draggable(false))
            marker.tag = markerCounter
            allMarker.add(marker)
            markerCounter++
        }
    }

    private fun changeMarkerState(position: Int) {
        resetMarkerState()
        try {
            if (!allMarker.isNullOrEmpty()) {
                if (cardListPosition == position && !searchPropertiesMap.isNullOrEmpty()) {
                    allMarker[position].setIcon(createCustomMarker(requireContext(), HOTEL_PRICE_ACTIVE_PIN, allMarker[position].title))
                    if (cardListPosition == SELECTED_POSITION_INIT) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(searchPropertiesMap[position]))
                    } else {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchPropertiesMap[position], MAPS_STREET_LEVEL_ZOOM))
                    }
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun resetMarkerState() {
        if (!allMarker.isNullOrEmpty()) {
            allMarker.forEach {
                it.setIcon(createCustomMarker(requireContext(), HOTEL_PRICE_INACTIVE_PIN, it.title))
            }
        }
    }

    private fun removeAllMarker() {
        searchPropertiesMap.clear()
        markerCounter = INIT_MARKER_TAG
        allMarker.forEach {
            it.remove()
        }
        allMarker.clear()
    }

    /** Location permission is handled by LocationDetector */
    private fun initGetMyLocation() {
        ivGetLocationHotelSearchMap.setOnClickListener {
            trackingHotelUtil.searchClickMyLocation(context,
                    searchDestinationName,
                    searchDestinationType,
                    SEARCH_SCREEN_NAME)

            isSearchByMap = true
            getCurrentLocation()
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes drawableId: Int): BitmapDescriptor {
        var drawable = ContextCompat.getDrawable(context, drawableId)

        val bitmap = Bitmap.createBitmap(drawable?.intrinsicWidth ?: 0,
                drawable?.intrinsicHeight ?: 0, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun createCustomMarker(context: Context, markerType: String, price: String): BitmapDescriptor {
        val marker: View = View.inflate(context, R.layout.hotel_custom_price_marker, null)

        val txtPrice = marker.findViewById<View>(R.id.txtPriceHotelMarker) as Typography
        with(txtPrice) {
            text = price
            background = ContextCompat.getDrawable(context, getPin(markerType))
            if (markerType == HOTEL_PRICE_ACTIVE_PIN) {
                setTextColor(color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                setWeight(Typography.BOLD)
            }
        }

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

        val bitmap = Bitmap.createBitmap(marker.measuredWidth, marker.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getPin(markerType: String): Int {
        return when (markerType) {
            MY_LOCATION_PIN -> R.drawable.ic_hotel_my_location
            HOTEL_PRICE_ACTIVE_PIN -> R.drawable.bg_price_marker_selected
            HOTEL_PRICE_INACTIVE_PIN -> R.drawable.bg_price_marker_default
            else -> R.drawable.bg_price_marker_default
        }
    }

    private fun onSuccessGetResult(data: PropertySearch) {
        showGetMylocation()
        context?.let {
            val searchParam = hotelSearchMapViewModel.searchParam
            trackingHotelUtil.hotelViewHotelListImpression(it,
                    searchDestinationName,
                    searchDestinationType,
                    searchParam,
                    data.properties,
                    adapter.dataSize,
                    SEARCH_SCREEN_NAME)
        }

        hideLoader()
        showQuickFilterShimmering(false)

        val searchProperties = data.properties
        if (searchProperties.isNotEmpty()) {
            showCardListView()
            if (adapterCardList.itemCount <= MINIMUM_NUMBER_OF_RESULT_LOADED) {
                renderCardListMap(searchProperties)
                searchProperties.forEach {
                    addMarker(it.location.latitude.toDouble(), it.location.longitude.toDouble(), it.roomPrice[0].price)
                    searchPropertiesMap.add(LatLng(it.location.latitude.toDouble(), it.location.longitude.toDouble()))
                }
            } else {
                hideLoadingCardListMap()
            }

            renderList(searchProperties.map {
                it.isForHorizontalItem = false
                it
            }.toList(), searchProperties.isNotEmpty())

            if (hotelSearchMapViewModel.searchParam.page == defaultInitialPage) {
                halfExpandBottomSheet()
            }
            hotelSearchMapViewModel.searchParam.page.plus(DEFAULT_INCREMENT_PAGE)

            showSearchWithMap()
        } else if (adapter.data.isEmpty()) {
            hideCardListView()
            hideSearchWithMap()
            hideHotelResultList()
            showErrorNoResult()
        }

        if (isFirstInitializeFilter) {
            isFirstInitializeFilter = false
            initializeQuickFilter(data.quickFilter, data.filters, data.displayInfo.sort)

            quickFilterSortHotelSearchMap.chipItems?.filter {
                it.type == ChipsUnify.TYPE_SELECTED
            }?.forEach {
                quickFilterSortHotelSearchMap.indicatorCounter -= 1
            }

            showCoachMark()
        }
    }

    private fun hideLoadingCardListMap() {
        adapterCardList.hideLoading()
    }

    private fun renderCardListMap(listProperty: List<Property>) {
        adapterCardList.removeElement(HotelLoadingModel(isForHorizontalItem = true))
        hideLoadingCardListMap()

        val dataCollection = mutableListOf<Visitable<*>>()
        dataCollection.addAll(listProperty.map {
            val newProperty = it.copy()
            newProperty.isForHorizontalItem = true
            newProperty
        }.toList())
        adapterCardList.addElement(dataCollection)
    }

    private fun showCoachMark() {
        context?.let {
            val coachmarkShowed = localCacheHandler.getBoolean(SHOW_COACH_MARK_KEY, false)
            if (!coachmarkShowed) {
                val coachMarkItem = arrayListOf(
                        CoachMark2Item(
                                topHotelSearchMapListKnob,
                                getString(R.string.hotel_search_map_coach_mark_map_title),
                                getString(R.string.hotel_search_map_coach_mark_map_desc),
                                CoachMark2.POSITION_BOTTOM
                        ),
                        CoachMark2Item(
                                invisibleViewBottomSheet,
                                getString(R.string.hotel_search_map_coach_mark_list_title),
                                getString(R.string.hotel_search_map_coach_mark_list_desc),
                                CoachMark2.POSITION_TOP
                        ),
                        CoachMark2Item(
                                requireView().findViewById(com.tokopedia.sortfilter.R.id.sort_filter_prefix),
                                getString(R.string.hotel_search_map_coach_mark_filter_title),
                                getString(R.string.hotel_search_map_coach_mark_filter_desc),
                                CoachMark2.POSITION_BOTTOM
                        )
                )
                val coachmark = CoachMark2(it)
                coachmark.setStepListener(object : CoachMark2.OnStepListener {
                    override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                        if (currentIndex == COACHMARK_MAP_STEP_POSITION) {
                            halfExpandBottomSheet()
                        } else if (currentIndex == COACHMARK_LIST_STEP_POSITION) {
                            collapseBottomSheet()
                        } else if (currentIndex == COACHMARK_FILTER_STEP_POSITION) {
                            halfExpandBottomSheet()
                        }
                    }
                })
                coachmark.showCoachMark(coachMarkItem, null, 0)
                localCacheHandler.putBoolean(SHOW_COACH_MARK_KEY, true)
                localCacheHandler.applyEditor()
            }
        }
    }

    private fun showCollapsingHeader() {
        tvHotelSearchListTitle.visible()
        topHotelSearchMapListKnob.visible()
    }

    private fun hideCollapsingHeader() {
        tvHotelSearchListTitle.gone()
        topHotelSearchMapListKnob.gone()
    }

    private fun showCardListView() {
        rvHorizontalPropertiesHotelSearchMap.visible()
    }

    private fun hideCardListView() {
        rvHorizontalPropertiesHotelSearchMap.gone()
    }

    private fun showLoader() {
        hotel_loader.show()
    }

    private fun hideLoader() {
        hotel_loader.gone()
    }

    private fun showGetMylocation() {
        ivGetLocationHotelSearchMap.visible()
    }

    private fun hideGetMyLocation() {
        ivGetLocationHotelSearchMap.gone()
    }

    private fun changeHeaderTitle() {
        headerHotelSearchMap.title = if (isSearchByMap) getString(R.string.hotel_header_title_nearby) else getString(R.string.hotel_header_title_nearby_area)
    }

    private fun showFindNearHereView() {
        view?.let {
            btnGetRadiusHotelSearchMap.visible()
        }
        animatebtnGetRadiusHotelSearchMap(BUTTON_RADIUS_SHOW_VALUE)
    }

    private fun hideFindNearHereView() {
        animatebtnGetRadiusHotelSearchMap(BUTTON_RADIUS_HIDE_VALUE)
        view?.let {
            btnGetRadiusHotelSearchMap.gone()
        }
    }

    private fun animatebtnGetRadiusHotelSearchMap(value: Float) {
        ObjectAnimator.ofFloat(btnGetRadiusHotelSearchMap, BUTTON_RADIUS_ANIMATION_Y, value).apply {
            duration = DELAY_BUTTON_RADIUS
            start()
        }
    }

    //for setup quick filter after click submit in bottom sheet
    private fun setupQuickFilterBaseOnSelectedFilter(selectedFilters: List<ParamFilterV2>) {
        quickFilterSortHotelSearchMap.chipItems?.forEach { it.type = ChipsUnify.TYPE_NORMAL }
        val selectedFiltersMap = selectedFilters.associateBy({ it.name }, { it })
        quickFilters.forEachIndexed { index, quickFilter ->
            if (selectedFiltersMap.containsKey(quickFilter.name)) {
                val selectedFilter = selectedFiltersMap[quickFilter.name]
                selectedFilter?.let { selectedFilterMap ->
                    var contains = true
                    for (quickFilterValue in quickFilter.values) {
                        for ((i, selectedFilterValue) in selectedFilterMap.values.withIndex()) {
                            if (quickFilterValue == selectedFilterValue) break
                            else if (i == selectedFilterMap.values.lastIndex) contains = false
                        }
                        if (!contains) break
                    }
                    quickFilterSortHotelSearchMap.chipItems?.let {
                        if (contains) it[index].type = ChipsUnify.TYPE_SELECTED
                    }
                }
            }
        }
    }

    private fun showQuickFilterShimmering(isShimmering: Boolean) {
        if (isShimmering) {
            shimmeringQuickFilterHotelSearchMap.show()
            quickFilterSortHotelSearchMap.hide()
        } else {
            shimmeringQuickFilterHotelSearchMap.hide()
            quickFilterSortHotelSearchMap.show()
        }
        quickFilterSortHotelSearchMap.indicatorCounter = hotelSearchMapViewModel.getFilterCount()
    }

    private fun hideQuickFilter() {
        shimmeringQuickFilterHotelSearchMap.hide()
        quickFilterSortHotelSearchMap.hide()
    }

    private fun initializeQuickFilter(quickFilters: List<QuickFilter>, filters: List<FilterV2>, sort: List<Sort>) {
        this.quickFilters = quickFilters.map { itemQuickFilter ->
            val item = filters.filter {
                it.name.equals(itemQuickFilter.name, true)
            }

            if (item.isNotEmpty()) {
                itemQuickFilter.type = (item.firstOrNull() ?: FilterV2()).type
            }

            itemQuickFilter
        }

        quickFilterSortHotelSearchMap.chipItems?.let {
            quickFilterSortHotelSearchMap.dismissListener = {
                hotelSearchMapViewModel.addFilter(quickFilters, it)
            }
        }

        val sortFilterItem = quickFilters.map {
            val item = SortFilterItem(
                    title = it.displayName,
                    type = if (it.selected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            )
            item.listener = {
                item.toggleSelected()
            }
            item
        }
        quickFilterSortHotelSearchMap.addItem(ArrayList(sortFilterItem))

        quickFilterSortHotelSearchMap.chipItems?.let { sortFilterItemList ->
            for ((index, item) in sortFilterItemList.withIndex()) {
                item.refChipUnify.setOnClickListener {
                    item.toggleSelected()
                    trackingHotelUtil.clickOnQuickFilter(context, SEARCH_SCREEN_NAME, item.title.toString(), index)
                    hotelSearchMapViewModel.addFilter(quickFilters, sortFilterItemList)
                }
            }
        }

        quickFilterSortHotelSearchMap.filterType = SortFilter.TYPE_ADVANCED
        quickFilterSortHotelSearchMap.parentListener = {
            trackingHotelUtil.clickOnAdvancedFilter(context, SEARCH_SCREEN_NAME)
            initiateAdvancedFilter(filters, sort)
        }

        quickFilterSortHotelSearchMap.show()
    }

    private fun initiateAdvancedFilter(filters: List<FilterV2>, sort: List<Sort>) {
        val filterItems: MutableList<FilterV2> = arrayListOf()
        filterItems.addAll(filters)
        val sortInFilterBottomSheets = FilterV2(
                type = FILTER_TYPE_SORT,
                name = FILTER_TYPE_SORT,
                displayName = getString(R.string.hotel_bottomsheet_sort_title)
        )
        if (hotelSearchMapViewModel.selectedSort.displayName.isEmpty()) {
            val sortDisplayName = sort.firstOrNull {
                it.name == hotelSearchMapViewModel.selectedSort.name
            } ?: Sort()
            hotelSearchMapViewModel.selectedSort.displayName = sortDisplayName.displayName
            hotelSearchMapViewModel.defaultSort = sortDisplayName.displayName
        }
        sortInFilterBottomSheets.options = sort.map { it.displayName }
        sortInFilterBottomSheets.optionSelected = listOf(hotelSearchMapViewModel.selectedSort.displayName)
        sortInFilterBottomSheets.defaultOption = hotelSearchMapViewModel.defaultSort
        filterItems.add(0, sortInFilterBottomSheets)

        val selectedFilter: MutableList<ParamFilterV2> = mutableListOf()
        selectedFilter.addAll(hotelSearchMapViewModel.getSelectedFilter())
        selectedFilter.add(ParamFilterV2(FILTER_TYPE_SORT, mutableListOf(hotelSearchMapViewModel.selectedSort.displayName)))

        filterBottomSheet = HotelFilterBottomSheets()
                .setSubmitFilterListener(this)
                .setSelected(selectedFilter)
                .setFilter(filterItems)
        filterBottomSheet.show(childFragmentManager, this.javaClass.simpleName)
    }

    private fun findSortValue(filter: ParamFilterV2): Sort? =
            if (hotelSearchMapViewModel.liveSearchResult.value != null &&
                    hotelSearchMapViewModel.liveSearchResult.value is Success) {
                var sortOption = (hotelSearchMapViewModel.liveSearchResult.value as Success).data.displayInfo.sort
                sortOption.firstOrNull { it.displayName == filter.values.firstOrNull() }
            } else null

    private fun checkGps() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && permissionCheckerHelper.hasPermission(requireContext(), arrayOf(PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION))) {
            showDialogEnableGPS()
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        val locationDetectorHelper = LocationDetectorHelper(
                permissionCheckerHelper,
                fusedLocationClient,
                requireActivity().applicationContext)

        activity?.let {
            permissionCheckerHelper.checkPermission(it,
                    PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            permissionCheckerHelper.onPermissionDenied(it, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                        }

                        override fun onPermissionGranted() {
                            locationDetectorHelper.getLocation(hotelSearchMapViewModel.onGetLocation(), requireActivity(),
                                    LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                                    requireActivity().getString(R.string.hotel_destination_need_permission))
                        }

                    }, getString(R.string.hotel_destination_need_permission))
        }

    }

    private fun showDialogEnableGPS() {
        val dialog = DialogUnify(activity as AppCompatActivity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.hotel_recommendation_gps_dialog_title))
        dialog.setDescription(getString(R.string.hotel_recommendation_gps_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.hotel_recommendation_gps_dialog_ok))
        dialog.setPrimaryCTAClickListener {
            startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_GPS)
            dialog.dismiss()
        }
        dialog.setSecondaryCTAText(getString(R.string.hotel_recommendation_gps_dialog_cancel))
        dialog.setSecondaryCTAClickListener {
            onErrorGetLocation()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun onErrorGetLocation() {
        view?.let { v ->
            Toaster.build(v, getString(R.string.hotel_destination_error_get_location),
                    Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
        }
    }

    private fun setupFindWithMapButton() {
        context?.let {
            val wrapper = LinearLayout(it)
            wrapper.gravity = Gravity.CENTER

            val imageView = ImageView(it)
            imageView.setPadding(BUTTON_RADIUS_PADDING_ALL, BUTTON_RADIUS_PADDING_ALL, BUTTON_RADIUS_PADDING_RIGHT, BUTTON_RADIUS_PADDING_ALL)
            imageView.setImageResource(R.drawable.ic_hotel_search_maps)
            wrapper.addView(imageView)

            val textView = Typography(it)
            textView.apply {
                setHeadingText(BUTTON_RADIUS_HEADING_SIZE)
                setTextColor(ContextCompat.getColor(context, R.color.hotel_color_active_price_marker))
                text = getString(R.string.hotel_search_map_search_with_map)
            }
            wrapper.addView(textView)

            btnHotelSearchWithMap.addItem(wrapper)
        }
        btnHotelSearchWithMap.setOnClickListener {
            rvVerticalPropertiesHotelSearchMap.scrollTo(0, 0)
            collapseBottomSheet()
        }
    }

    private fun showHotelResultList() {
        rvVerticalPropertiesHotelSearchMap.visible()
    }

    private fun hideHotelResultList() {
        rvVerticalPropertiesHotelSearchMap.gone()
    }

    private fun showSearchWithMap() {
        btnHotelSearchWithMap.visible()
        btnHotelSearchWithMap.show()
    }

    private fun hideSearchWithMap() {
        btnHotelSearchWithMap.hide()
        btnHotelSearchWithMap.gone()
    }

    private fun showErrorNoResult() {
        trackingHotelUtil.searchHotelNotFound(context,
                searchDestinationName,
                searchDestinationType,
                SEARCH_SCREEN_NAME)

        containerEmptyResultState.visible()
        val viewTree = containerEmptyResultState.viewTreeObserver
        viewTree.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTree.removeOnGlobalLayoutListener(this)
                bottomSheetBehavior.peekHeight = containerEmptyResultState.measuredHeight +
                        resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

                collapseBottomSheet()
            }
        })
    }

    private fun hideErrorNoResult() {
        containerEmptyResultState.gone()
    }

    private fun expandBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun halfExpandBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun collapseBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun isHotelListShowingError(): Boolean =
            adapter.list.size > 0 && adapter.list[0] is ErrorNetworkModel

    companion object {
        private const val COACHMARK_MAP_STEP_POSITION = 0
        private const val COACHMARK_LIST_STEP_POSITION = 1
        private const val COACHMARK_FILTER_STEP_POSITION = 2

        private const val REQUEST_CODE_DETAIL_HOTEL = 101
        private const val REQUEST_CHANGE_SEARCH_HOTEL = 105
        private const val REQUEST_CODE_GPS = 108

        private const val ANCHOR_MARKER_X: Float = 0.8f
        private const val ANCHOR_MARKER_Y: Float = 1f

        private const val MY_LOCATION_PIN = "MY LOCATION"
        private const val HOTEL_PRICE_ACTIVE_PIN = "HOTEL PRICE ACTIVE"
        private const val HOTEL_PRICE_INACTIVE_PIN = "HOTEL PRICE INACTIVE"

        private const val BUTTON_RADIUS_HEADING_SIZE = 6
        private const val BUTTON_RADIUS_PADDING_ALL = 0
        private const val BUTTON_RADIUS_PADDING_RIGHT = 6
        private const val BUTTON_RADIUS_ANIMATION_Y = "translationY"

        private const val INIT_MARKER_TAG = 0
        private const val DEFAULT_INCREMENT_PAGE = 1
        private const val MINIMUM_NUMBER_OF_RESULT_LOADED = 20

        const val ARG_HOTEL_SEARCH_MODEL = "arg_hotel_search_model"
        private const val ARG_FILTER_PARAM = "arg_hotel_filter_param"

        const val SELECTED_POSITION_INIT = 0
        const val DELAY_BUTTON_RADIUS: Long = 1000L
        const val BUTTON_RADIUS_SHOW_VALUE: Float = 128f
        const val BUTTON_RADIUS_HIDE_VALUE: Float = -150f

        private const val MAPS_STREET_LEVEL_ZOOM: Float = 15f
        private const val MAPS_ZOOM_IN: Float = 11f
        private const val MAPS_ZOOM_OUT: Float = 9f

        private const val PREFERENCES_NAME = "hotel_search_map_preferences"
        private const val SHOW_COACH_MARK_KEY = "hotel_search_map_show_coach_mark"

        fun createInstance(hotelSearchModel: HotelSearchModel, selectedParam: ParamFilterV2): HotelSearchMapFragment =
                HotelSearchMapFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
                        putParcelable(ARG_FILTER_PARAM, selectedParam)
                    }
                }
    }
}
