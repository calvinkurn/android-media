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
import androidx.recyclerview.widget.PagerSnapHelper
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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.databinding.FragmentHotelSearchMapBinding
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search_map.data.model.*
import com.tokopedia.hotel.search_map.data.model.FilterV2.Companion.FILTER_TYPE_SORT
import com.tokopedia.hotel.search_map.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search_map.di.HotelSearchMapComponent
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity.Companion.SEARCH_SCREEN_NAME
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultAdapter
import com.tokopedia.hotel.search_map.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search_map.presentation.viewmodel.HotelSearchMapViewModel
import com.tokopedia.hotel.search_map.presentation.widget.HotelFilterBottomSheets
import com.tokopedia.hotel.search_map.presentation.widget.SubmitFilterListener
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.setHeadingText
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

/**
 * @author by furqan on 01/03/2021
 */
class HotelSearchMapFragment : BaseListFragment<Property, PropertyAdapterTypeFactory>(),
    BaseEmptyViewHolder.Callback, HotelSearchResultAdapter.OnClickListener,
    OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SubmitFilterListener,
    GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var hotelSearchMapViewModel: HotelSearchMapViewModel

    private var binding by autoClearedNullable<FragmentHotelSearchMapBinding>()

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
    private var hotelProperties: ArrayList<Property> = arrayListOf()
    private var quickFilters: List<QuickFilter> = listOf()
    private var searchPropertiesMap: ArrayList<LatLng> = arrayListOf()
    private var isSearchByMap: Boolean = false
    private var lastHorizontalTrackingPositionSent: Int = -1

    private var isViewFullMap: Boolean = false

    private lateinit var filterBottomSheet: HotelFilterBottomSheets
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val snapHelper: SnapHelper = PagerSnapHelper()
    private lateinit var linearLayoutManager: LinearLayoutManager

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

            val selectedSort = it.getString(ARG_SORT_PARAM) ?: ""
            if (selectedSort.isNotEmpty()) {
                hotelSearchMapViewModel.selectedSort = Sort(selectedSort)
            }

            hotelSearchModel = it.getParcelable(ARG_HOTEL_SEARCH_MODEL) ?: HotelSearchModel()
            hotelSearchMapViewModel.initSearchParam(hotelSearchModel)

            searchDestinationName = hotelSearchModel.name
            searchDestinationType =
                if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
        }

        activity?.let {
            (it as HotelSearchMapActivity).setSupportActionBar(binding?.headerHotelSearchMap)
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
                    if (!it.data.properties.isNullOrEmpty() && currentPage == defaultInitialPage) {
                        changeMarkerState(cardListPosition)
                    } else {
                        buildFilter(it.data)
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
                        name =
                            if (isSearchByMap) getString(R.string.hotel_header_title_nearby) else getString(
                                R.string.hotel_header_title_nearby_area
                            )
                        radius = it.data
                    }
                    hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
                    loadInitialData()
                }
                is Fail -> {
                    Toaster.build(
                        requireView(),
                        it.throwable.message.toString(),
                        Snackbar.LENGTH_SHORT,
                        Toaster.TYPE_NORMAL
                    )
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

        hotelSearchMapViewModel.liveSelectedFilter.observe(
            viewLifecycleOwner,
            Observer { (data, notifyUi) ->
                if (notifyUi) {
                    hideFindNearHereView()
                    setupQuickFilterBaseOnSelectedFilter(data)
                    showQuickFilterShimmering(true)
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
                    searchId = it.getStringExtra(HotelChangeSearchActivity.SEARCH_ID) ?: ""
                )
                hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
                searchDestinationName = hotelSearchModel.name
                searchDestinationType =
                    if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type

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

    override fun showGetListError(throwable: Throwable?) {
        binding?.containerError?.root?.visible()
        context?.run {
            binding?.containerError?.globalError?.let {
                ErrorHandlerHotel.getErrorUnify(
                    this, throwable,
                    { onRetryClicked() }, it
                )
            }
        }
    }

    override fun onRetryClicked() {
        binding?.let {
            it.containerError.root.hide()
            halfExpandBottomSheet()
        }
        super.onRetryClicked()
    }

    override fun loadInitialData() {
        hotelSearchMapViewModel.searchParam.page = defaultInitialPage
        binding?.tvHotelSearchListTitleLoader?.visible()
        binding?.tvHotelSearchListTitle?.gone()

        cardListPosition = SELECTED_POSITION_INIT
        adapterCardList.clearAllElements()
        removeAllMarker()

        hideErrorNoResult()
        showHotelResultList()
        showLoader()
        super.loadInitialData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotelSearchMapBinding.inflate(inflater, container, false)
        binding?.root?.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0)
        return binding?.root
    }

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
        initInfoMaxRadius()

        binding?.ivHotelSearchMapNoResult?.loadImage(getString(R.string.hotel_url_empty_search_map_result))

        trackingHotelUtil.viewHotelSearchMap(
            context,
            searchDestinationType,
            searchDestinationName,
            hotelSearchMapViewModel.searchParam,
            SEARCH_SCREEN_NAME
        )
    }

    override fun onMapReady(map: GoogleMap) {
        //for SS-testing purpose
        binding?.mapHotelSearchMap?.contentDescription = "MAP READY"
        this.googleMap = map
        setGoogleMap()
    }

    override fun onCameraMove() {
        getInfoMaxRadius()
    }

    override fun onCameraMoveStarted(reason: Int) {
        when (reason) {
            GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {
                showFindNearHereView()
                googleMap.setOnCameraMoveListener(this)
            }
            else -> hideFindNearHereView()
        }
    }

    private fun getInfoMaxRadius() {
        val zoomLevel = googleMap.cameraPosition.zoom
        if (zoomLevel <= MAX_RADIUS) {
            hideFindNearHereView()
            showInfoMaxRadius()
        } else {
            hideInfoMaxRadius()
            showFindNearHereView()
        }
    }

    private fun initInfoMaxRadius() {
        context?.let {
            val wrapper = LinearLayout(it)
            wrapper.gravity = Gravity.CENTER

            val textView = Typography(it)
            textView.apply {
                setHeadingText(BUTTON_RADIUS_HEADING_SIZE)
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.hotel_dms_max_radius_font_color
                    )
                )
                text = getString(R.string.hotel_search_map_max_radius_info)
            }
            wrapper.addView(textView)
            binding?.fabHotelInfoMaxRadius?.addItem(wrapper)
        }
        binding?.fabHotelInfoMaxRadius?.setMargins(
            0,
            resources.getDimensionPixelSize(R.dimen.hotel_70dp),
            0,
            0
        )
        hideInfoMaxRadius()
    }

    private fun hideInfoMaxRadius() {
        binding?.let {
            animateFAB(it.fabHotelInfoMaxRadius, false)
        }
    }

    private fun showInfoMaxRadius() {
        binding?.let {
            animateFAB(it.fabHotelInfoMaxRadius, true)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        allMarker.forEach {
            if (it.tag == marker.tag) {
                cardListPosition = it.tag as Int
                binding?.rvHorizontalPropertiesHotelSearchMap?.scrollToCenterPosition(
                    cardListPosition
                )
                changeMarkerState(cardListPosition)
                if (cardListPosition != -1 &&
                    cardListPosition != lastHorizontalTrackingPositionSent &&
                    adapterCardList.data[cardListPosition] is Property && !adapterCardList.data.isNullOrEmpty()
                ) {

                    lastHorizontalTrackingPositionSent = cardListPosition
                    trackingHotelUtil.hotelViewHotelListMapImpression(
                        context,
                        searchDestinationType,
                        searchDestinationName,
                        hotelSearchMapViewModel.searchParam,
                        listOf(adapterCardList.data[cardListPosition]),
                        cardListPosition,
                        SEARCH_SCREEN_NAME
                    )
                }
            }
        }
        return true
    }

    fun RecyclerView.scrollToCenterPosition(position: Int) {
        if (::linearLayoutManager.isInitialized) {
            try {
                binding?.rvHorizontalPropertiesHotelSearchMap?.scrollToPosition(position)
                binding?.rvHorizontalPropertiesHotelSearchMap?.post {
                    val itemView = linearLayoutManager.findViewByPosition(position)
                    if (itemView != null) {
                        val snapDistance: IntArray =
                            snapHelper.calculateDistanceToFinalSnap(linearLayoutManager, itemView)
                                ?: intArrayOf()
                        if (snapDistance.isNotEmpty()) {
                            if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                                binding?.rvHorizontalPropertiesHotelSearchMap?.scrollBy(
                                    snapDistance[0],
                                    snapDistance[1]
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                binding?.rvHorizontalPropertiesHotelSearchMap?.smoothScrollToPosition(position)
            }
        }
    }

    override fun onItemClicked(property: Property, position: Int) {
        with(hotelSearchMapViewModel.searchParam) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                trackingHotelUtil.chooseHotelFromMap(
                    context,
                    searchDestinationType,
                    searchDestinationName,
                    this,
                    property,
                    position,
                    SEARCH_SCREEN_NAME
                )
            } else {
                trackingHotelUtil.chooseHotel(
                    context,
                    searchDestinationType,
                    searchDestinationName,
                    this,
                    property,
                    position,
                    SEARCH_SCREEN_NAME
                )
            }

            context?.run {
                startActivityForResult(
                    HotelDetailActivity.getCallingIntent(
                        this,
                        checkIn, checkOut, property.id, room, guest.adult,
                        searchDestinationType, searchDestinationName, property.isDirectPayment
                    ),
                    REQUEST_CODE_DETAIL_HOTEL
                )
            }
        }
    }

    override fun loadData(page: Int) {
        val searchQuery = HotelGqlQuery.PROPERTY_SEARCH
        hotelSearchMapViewModel.searchProperty(page, searchQuery)
    }

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory =
        PropertyAdapterTypeFactory(this)

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
        binding?.tvHotelSearchListTitleLoader?.gone()
        binding?.tvHotelSearchListTitle?.visible()
        super.hideLoading()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper.onRequestPermissionsResult(
                    it,
                    requestCode, permissions,
                    grantResults
                )
            }
        }
    }

    override fun onSubmitFilter(selectedFilter: MutableList<ParamFilterV2>) {
        trackingHotelUtil.clickSubmitFilterOnBottomSheet(
            context,
            SEARCH_SCREEN_NAME,
            selectedFilter
        )

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
        bottomSheetBehavior.peekHeight = binding?.hotelSearchMapBottomSheet?.measuredHeight ?: 0
        hideSearchWithMap()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        bottomSheetBehavior.peekHeight = binding?.hotelSearchMapBottomSheet?.measuredHeight ?: 0
        hideSearchWithMap()
        return super.getEmptyDataViewModel()
    }

    private fun setupToolbarAction() {
        context?.let {
            val wrapper = LinearLayout(it).apply {
                val param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setBackgroundColor(
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
                layoutParams = param
            }

            val textView = Typography(it)
            val param = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.layoutParams = param
            textView.text = resources.getString(R.string.hotel_search_result_change)
            textView.fontType = Typography.BODY_2
            textView.setTextColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
            )

            wrapper.addView(textView)
            wrapper.setOnClickListener {
                changeSearchParameter()
            }

            binding?.headerHotelSearchMap?.headerView?.setTextColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                )
            )
            binding?.headerHotelSearchMap?.addCustomRightContent(wrapper)
            binding?.headerHotelSearchMap?.isShowBackButton = true
            binding?.headerHotelSearchMap?.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setupPersistentBottomSheet() {
        if (!::bottomSheetBehavior.isInitialized) {
            binding?.hotelSearchMapBottomSheet?.let {
                bottomSheetBehavior = BottomSheetBehavior.from(it)
            }
        }
        bottomSheetBehavior.isFitToContents = false

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val opacity = SLIDE_MINUS_OPACITY - (slideOffset * SLIDE_MULT_OPACITY)
                binding?.rvHorizontalPropertiesHotelSearchMap?.let {
                    it.alpha = opacity
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        showSearchWithMap()
                        setupContentMargin(true)
                        if (::googleMap.isInitialized){
                            googleMap.uiSettings.setAllGesturesEnabled(false)
                        }

                        if (isViewFullMap) {
                            trackingHotelUtil.searchCloseMap(
                                context,
                                searchDestinationType,
                                searchDestinationName,
                                SEARCH_SCREEN_NAME
                            )
                            isViewFullMap = false
                        }
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        if (::googleMap.isInitialized){
                            if (searchPropertiesMap.isNullOrEmpty()) {
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(MAPS_ZOOM_OUT))
                            } else {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(searchPropertiesMap[0]))
                                val newLatLng = getMapCenter(searchPropertiesMap[0])
                                googleMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        newLatLng,
                                        MAPS_ZOOM_OUT
                                    )
                                )
                            }
                            setupContentMargin(false)
                            if (binding?.containerEmptyResultState?.isVisible == true) {
                                googleMap.uiSettings.setAllGesturesEnabled(false)
                                enabledMapsClick()
                            } else {
                                enabledMapsGesture()
                            }
                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        if (::googleMap.isInitialized){
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(MAPS_ZOOM_IN))
                        }
                        setupContentMargin(false)

                        enabledMapsGesture()

                        if (!isViewFullMap) {
                            trackingHotelUtil.searchViewFullMap(
                                context,
                                searchDestinationType,
                                searchDestinationName,
                                SEARCH_SCREEN_NAME
                            )
                            isViewFullMap = true
                        }
                    }
                    else -> {
                        setupContentMargin(false)
                    }
                }
            }

        })
        binding?.tvHotelSearchListTitle?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding?.tvHotelSearchListTitle?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                val titleLayoutParam =
                    binding?.tvHotelSearchListTitle?.layoutParams as ViewGroup.MarginLayoutParams

                var bottomSheetHeaderHeight =
                    binding?.tvHotelSearchListTitle?.measuredHeight // title height
                bottomSheetHeaderHeight = bottomSheetHeaderHeight?.plus(
                    resources.getDimensionPixelSize(
                        com.tokopedia.unifycomponents.R.dimen.bottom_sheet_knob_height
                    )
                ) // knob height
                bottomSheetHeaderHeight = bottomSheetHeaderHeight?.plus(
                    resources.getDimensionPixelSize(
                        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3
                    )
                ) // knob top margin
                bottomSheetHeaderHeight =
                    bottomSheetHeaderHeight?.plus(titleLayoutParam.topMargin) // title top margin
                bottomSheetHeaderHeight = bottomSheetHeaderHeight?.plus(
                    resources.getDimensionPixelSize(
                        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4
                    )
                ) // add margin

                bottomSheetBehavior.peekHeight = bottomSheetHeaderHeight ?: 0
            }
        })
    }

    private fun setupContentMargin(isExpanded: Boolean) {
        binding?.let {
            if (isExpanded) {
                it.rvVerticalPropertiesHotelSearchMap.setMargin(
                    0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7),
                    0,
                    0
                )
                it.containerEmptyResultState.setMargin(
                    0,
                    resources.getDimensionPixelSize(R.dimen.hotel_80dp),
                    0,
                    0
                )
            } else {
                it.rvVerticalPropertiesHotelSearchMap.setMargin(
                    0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3),
                    0,
                    0
                )
                it.containerEmptyResultState.setMargin(
                    0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4),
                    0,
                    0
                )
            }

            if (isHotelListShowingError()) {
                hideSearchWithMap()
                it.rvVerticalPropertiesHotelSearchMap.setMargin(
                    0,
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl7),
                    0,
                    0
                )
            }

            it.rvVerticalPropertiesHotelSearchMap.requestLayout()
            it.containerEmptyResultState.requestLayout()
        }
    }

    private fun setUpTitleAndSubtitle() {
        context?.let {
            val hotelSearchModel = hotelSearchMapViewModel.hotelSearchModel
            val checkInString = hotelSearchModel.checkIn.toDate(DateUtil.YYYY_MM_DD)
                .toString(DateUtil.VIEW_FORMAT_WITHOUT_YEAR)
            val checkOutString = hotelSearchModel.checkOut.toDate(DateUtil.YYYY_MM_DD)
                .toString(DateUtil.VIEW_FORMAT_WITHOUT_YEAR)

            binding?.headerHotelSearchMap?.let { view ->
                view.title = hotelSearchModel.name
                view.subtitle = getString(
                    R.string.template_search_subtitle,
                    checkInString,
                    checkOutString,
                    hotelSearchModel.room,
                    hotelSearchModel.adult
                )
                view.subheaderView?.setTextColor(
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_44
                    )
                )
            }
        }
    }

    private fun changeSearchParameter() {
        context?.let {
            val type =
                if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
            trackingHotelUtil.hotelClickChangeSearch(
                it, type,
                hotelSearchModel.name, hotelSearchModel.room, hotelSearchModel.adult,
                hotelSearchModel.checkIn, hotelSearchModel.checkOut, SEARCH_SCREEN_NAME
            )
            startActivityForResult(
                HotelChangeSearchActivity.getIntent(
                    it,
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
                    getString(R.string.hotel_search_result_change_toolbar_title)
                ),
                REQUEST_CHANGE_SEARCH_HOTEL
            )
        }
    }

    private fun setCardListViewAdapter() {
        adapterCardList = HotelSearchResultAdapter(this, adapterTypeFactory)
    }

    private fun initRecyclerViewMap() {
        binding?.rvHorizontalPropertiesHotelSearchMap?.adapter = adapterCardList
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvHorizontalPropertiesHotelSearchMap?.layoutManager = linearLayoutManager

        if (binding?.rvHorizontalPropertiesHotelSearchMap?.onFlingListener == null) {
            snapHelper.attachToRecyclerView(binding?.rvHorizontalPropertiesHotelSearchMap)
        }

        initScrollCardList()
    }

    private fun initScrollCardList() {
        binding?.rvHorizontalPropertiesHotelSearchMap?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    cardListPosition = getCurrentItemCardList()
                    changeMarkerState(cardListPosition)
                    if (cardListPosition != -1 &&
                        cardListPosition != lastHorizontalTrackingPositionSent &&
                        adapterCardList.data[cardListPosition] is Property
                    ) {

                        lastHorizontalTrackingPositionSent = cardListPosition
                        trackingHotelUtil.hotelViewHotelListMapImpression(
                            context,
                            searchDestinationType,
                            searchDestinationName,
                            hotelSearchMapViewModel.searchParam,
                            listOf(adapterCardList.data[cardListPosition]),
                            cardListPosition,
                            SEARCH_SCREEN_NAME
                        )
                    }
                }
            }
        })
    }

    private fun getCurrentItemCardList(): Int =
        (binding?.rvHorizontalPropertiesHotelSearchMap?.layoutManager as LinearLayoutManager)
            .findFirstCompletelyVisibleItemPosition()

    private fun setGoogleMap() {
        if (::googleMap.isInitialized) {
            googleMap.uiSettings.isMapToolbarEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = false
            googleMap.uiSettings.isScrollGesturesEnabled = true
            googleMap.uiSettings.isTiltGesturesEnabled = false

            googleMap.setOnMarkerClickListener(this)
            googleMap.setOnCameraMoveStartedListener(this)
            enabledMapsClick()
        }
    }

    fun enabledMapsClick() {
        binding?.mapHotelSearchMap?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> v.performClick()
                }
                return true
            }
        })
        binding?.mapHotelSearchMap?.setOnClickListener {
            collapseBottomSheet()
        }
        if (::googleMap.isInitialized) {
            googleMap.setOnMapClickListener {
                collapseBottomSheet()
            }
        }
    }

    fun enabledMapsGesture() {
        if (::googleMap.isInitialized) {
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = false
            googleMap.uiSettings.isScrollGesturesEnabled = true
            googleMap.uiSettings.isTiltGesturesEnabled = false
        }
    }

    private fun initLocationMap() {
        if (binding?.mapHotelSearchMap != null) {
            binding?.mapHotelSearchMap?.onCreate(null)
            binding?.mapHotelSearchMap?.onResume()
            binding?.mapHotelSearchMap?.getMapAsync(this)
        }
        setGoogleMap()
    }

    private fun initFloatingButton() {
        context?.let {
            val wrapper = LinearLayout(it)
            wrapper.gravity = Gravity.CENTER

            val imageView = ImageView(it)
            imageView.setPadding(
                BUTTON_RADIUS_PADDING_ALL,
                BUTTON_RADIUS_PADDING_ALL,
                BUTTON_RADIUS_PADDING_RIGHT,
                BUTTON_RADIUS_PADDING_ALL
            )
            imageView.setImageResource(R.drawable.ic_hotel_search_map_search)
            wrapper.addView(imageView)

            val textView = Typography(it)
            textView.apply {
                setHeadingText(BUTTON_RADIUS_HEADING_SIZE)
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.hotel_dms_active_price_marker_color
                    )
                )
                text = getString(R.string.hotel_search_map_around_here)
            }
            wrapper.addView(textView)
            wrapper.setOnClickListener {
                trackingHotelUtil.searchNearLocation(
                    context,
                    searchDestinationType,
                    searchDestinationName,
                    SEARCH_SCREEN_NAME
                )

                onSearchByMap()
            }

            binding?.btnGetRadiusHotelSearchMap?.addItem(wrapper)
        }
        binding?.btnGetRadiusHotelSearchMap?.setMargins(
            0,
            resources.getDimensionPixelSize(R.dimen.hotel_70dp),
            0,
            0
        )
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
        if (::googleMap.isInitialized) {
            googleMap.addMarker(
                MarkerOptions().position(latLong)
                    .icon(bitmapDescriptorFromVector(requireContext(), getPin(MY_LOCATION_PIN)))
                    .anchor(ANCHOR_MARKER_X, ANCHOR_MARKER_Y)
                    .draggable(false)
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLong))

            removeAllMarker()
        }
    }

    private fun addMarker(latitude: Double, longitude: Double, price: String) {
        val latLng = LatLng(latitude, longitude)

        if (::googleMap.isInitialized) {
            context?.run {
                val marker = googleMap.addMarker(
                    MarkerOptions().position(latLng)
                        .icon(createCustomMarker(this, HOTEL_PRICE_INACTIVE_PIN, price))
                        .title(price)
                        .anchor(ANCHOR_MARKER_X, ANCHOR_MARKER_Y)
                        .draggable(false)
                )
                marker.tag = markerCounter
                allMarker.add(marker)
                markerCounter++
            }
        }
    }

    private fun changeMarkerState(position: Int) {
        resetMarkerState()
        try {
            if (!allMarker.isNullOrEmpty()) {
                if (cardListPosition == position && !searchPropertiesMap.isNullOrEmpty() && position < searchPropertiesMap.size) {
                    allMarker[position].setIcon(
                        createCustomMarker(
                            requireContext(),
                            HOTEL_PRICE_ACTIVE_PIN,
                            allMarker[position].title
                        )
                    )
                    putPriceMarkerOnTop(position)
                    if (::googleMap.isInitialized){
                        if (cardListPosition == SELECTED_POSITION_INIT) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(searchPropertiesMap[position]))
                            val newLatLng = getMapCenter(searchPropertiesMap[position])
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng))
                        } else {
                            googleMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    searchPropertiesMap[position],
                                    MAPS_STREET_LEVEL_ZOOM
                                )
                            )
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun getMapCenter(latLng: LatLng): LatLng {
        return try {
            val mapCenter: Double = googleMap.cameraPosition.target.latitude
            val southMap: Double =
                googleMap.projection.visibleRegion.latLngBounds.southwest.latitude
            val diff = (mapCenter - southMap) / MAP_CENTER_DIVIDER
            val newLat: Double = latLng.latitude - diff
            LatLng(newLat, latLng.longitude)
        } catch (t: Throwable) {
            latLng
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
        binding?.ivGetLocationHotelSearchMap?.setOnClickListener {
            trackingHotelUtil.searchClickMyLocation(
                context,
                searchDestinationType,
                searchDestinationName,
                SEARCH_SCREEN_NAME
            )

            isSearchByMap = true
            getCurrentLocation()
        }
    }

    private fun bitmapDescriptorFromVector(
        context: Context,
        @DrawableRes drawableId: Int
    ): BitmapDescriptor {
        var drawable = ContextCompat.getDrawable(context, drawableId)

        val bitmap = Bitmap.createBitmap(
            drawable?.intrinsicWidth ?: 0,
            drawable?.intrinsicHeight ?: 0, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable?.setBounds(0, 0, canvas.width, canvas.height)
        drawable?.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun createCustomMarker(
        context: Context,
        markerType: String,
        price: String
    ): BitmapDescriptor {
        val marker: View = View.inflate(context, R.layout.hotel_custom_price_marker, null)

        val txtPrice = marker.findViewById<View>(R.id.txtPriceHotelMarker) as Typography
        with(txtPrice) {
            text = price
            background = ContextCompat.getDrawable(context, getPin(markerType))
            if (markerType == HOTEL_PRICE_ACTIVE_PIN) {
                setTextColor(
                    color = ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
                setWeight(Typography.BOLD)
            }
        }

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
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
            trackingHotelUtil.hotelViewHotelListImpression(
                it,
                searchDestinationType,
                searchDestinationName,
                searchParam,
                data.properties,
                adapter.dataSize,
                SEARCH_SCREEN_NAME
            )
        }

        hideLoader()
        val searchProperties = data.properties
        if (searchProperties.isNotEmpty()) {
            showCardListView()
            if (adapterCardList.itemCount <= MINIMUM_NUMBER_OF_RESULT_LOADED) {
                renderCardListMap(searchProperties)
                searchProperties.forEach {
                    addMarker(
                        it.location.latitude.toDouble(),
                        it.location.longitude.toDouble(),
                        it.roomPrice[0].price
                    )
                    searchPropertiesMap.add(
                        LatLng(
                            it.location.latitude.toDouble(),
                            it.location.longitude.toDouble()
                        )
                    )
                    hotelProperties.add(it)
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
        showCoachMark()
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
                        binding?.topHotelSearchMapListKnob ?: View(requireContext()),
                        getString(R.string.hotel_search_map_coach_mark_map_title),
                        getString(R.string.hotel_search_map_coach_mark_map_desc),
                        CoachMark2.POSITION_BOTTOM
                    ),
                    CoachMark2Item(
                        binding?.invisibleViewBottomSheet ?: View(requireContext()),
                        getString(R.string.hotel_search_map_coach_mark_list_title),
                        getString(R.string.hotel_search_map_coach_mark_list_desc),
                        CoachMark2.POSITION_TOP
                    ),
                    CoachMark2Item(
                        binding?.quickFilterSortHotelSearchMap?.sortFilterPrefix ?: View(
                            requireContext()
                        ),
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
        binding?.let {
            it.tvHotelSearchListTitle.visible()
            it.topHotelSearchMapListKnob.visible()
        }
    }

    private fun hideCollapsingHeader() {
        binding?.let {
            it.tvHotelSearchListTitle.gone()
            it.topHotelSearchMapListKnob.gone()
        }
    }

    private fun showCardListView() {
        binding?.rvHorizontalPropertiesHotelSearchMap?.visible()
    }

    private fun hideCardListView() {
        binding?.rvHorizontalPropertiesHotelSearchMap?.gone()
    }

    private fun showLoader() {
        binding?.hotelLoader?.show()
    }

    private fun hideLoader() {
        binding?.hotelLoader?.gone()
    }

    private fun showGetMylocation() {
        binding?.ivGetLocationHotelSearchMap?.visible()
    }

    private fun hideGetMyLocation() {
        binding?.ivGetLocationHotelSearchMap?.gone()
    }

    private fun changeHeaderTitle() {
        binding?.headerHotelSearchMap?.title =
            if (isSearchByMap) getString(R.string.hotel_header_title_nearby) else getString(R.string.hotel_header_title_nearby_area)
    }

    private fun showFindNearHereView() {
        binding?.let {
            it.btnGetRadiusHotelSearchMap.visible()
            animateFAB(it.btnGetRadiusHotelSearchMap, true)
        }
    }

    private fun hideFindNearHereView() {
        binding?.let {
            animateFAB(it.btnGetRadiusHotelSearchMap, false)
            it.btnGetRadiusHotelSearchMap.gone()
        }
    }

    private fun animateFAB(button: View, visibility: Boolean) {
        val alphaValue: Float =
            if (visibility) BUTTON_RADIUS_SHOW_VALUE else BUTTON_RADIUS_HIDE_VALUE
        ObjectAnimator.ofFloat(button, BUTTON_RADIUS_ANIMATION_Y, alphaValue).apply {
            start()
        }
    }

    //for setup quick filter after click submit in bottom sheet
    private fun setupQuickFilterBaseOnSelectedFilter(selectedFilters: List<ParamFilterV2>) {
        binding?.quickFilterSortHotelSearchMap?.chipItems?.forEach {
            it.type = ChipsUnify.TYPE_NORMAL
        }
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
                    binding?.quickFilterSortHotelSearchMap?.chipItems?.let {
                        if (contains) it[index].type = ChipsUnify.TYPE_SELECTED
                    }
                }
            }
        }
    }

    private fun showQuickFilterShimmering(isShimmering: Boolean) {
        binding?.let {
            if (isShimmering) {
                it.shimmeringQuickFilterHotelSearchMap.root.show()
                it.quickFilterSortHotelSearchMap.hide()
            } else {
                it.shimmeringQuickFilterHotelSearchMap.root.hide()
                it.quickFilterSortHotelSearchMap.show()
            }
            it.quickFilterSortHotelSearchMap.indicatorCounter =
                hotelSearchMapViewModel.getFilterCount()
        }
    }

    private fun hideQuickFilter() {
        binding?.shimmeringQuickFilterHotelSearchMap?.root?.hide()
        binding?.quickFilterSortHotelSearchMap?.hide()
    }

    private fun initializeQuickFilter(
        quickFilters: List<QuickFilter>,
        filters: List<FilterV2>,
        sort: List<Sort>
    ) {
        this.quickFilters = quickFilters.map { itemQuickFilter ->
            val item = filters.filter {
                it.name.equals(itemQuickFilter.name, true)
            }

            if (item.isNotEmpty()) {
                itemQuickFilter.type = (item.firstOrNull() ?: FilterV2()).type
            }

            itemQuickFilter
        }

        binding?.quickFilterSortHotelSearchMap?.chipItems?.let {
            binding?.quickFilterSortHotelSearchMap?.dismissListener = {
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
        binding?.quickFilterSortHotelSearchMap?.addItem(ArrayList(sortFilterItem))

        binding?.quickFilterSortHotelSearchMap?.chipItems?.let { sortFilterItemList ->
            for ((index, item) in sortFilterItemList.withIndex()) {
                item.refChipUnify.setOnClickListener {
                    item.toggleSelected()
                    trackingHotelUtil.clickOnQuickFilter(
                        context,
                        SEARCH_SCREEN_NAME,
                        item.title.toString(),
                        index
                    )
                    hotelSearchMapViewModel.addFilter(quickFilters, sortFilterItemList)
                }
            }
        }

        binding?.quickFilterSortHotelSearchMap?.filterType = SortFilter.TYPE_ADVANCED
        binding?.quickFilterSortHotelSearchMap?.parentListener = {
            trackingHotelUtil.clickOnAdvancedFilter(context, SEARCH_SCREEN_NAME)
            initiateAdvancedFilter(filters, sort)
        }

        binding?.quickFilterSortHotelSearchMap?.show()
        binding?.quickFilterSortHotelSearchMap?.indicatorCounter =
            hotelSearchMapViewModel.getFilterCount()
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
        sortInFilterBottomSheets.optionSelected =
            listOf(hotelSearchMapViewModel.selectedSort.displayName)
        sortInFilterBottomSheets.defaultOption = hotelSearchMapViewModel.defaultSort
        filterItems.add(0, sortInFilterBottomSheets)

        val selectedFilter: MutableList<ParamFilterV2> = mutableListOf()
        selectedFilter.addAll(hotelSearchMapViewModel.getSelectedFilter())
        selectedFilter.add(
            ParamFilterV2(
                FILTER_TYPE_SORT,
                mutableListOf(hotelSearchMapViewModel.selectedSort.displayName)
            )
        )

        filterBottomSheet = HotelFilterBottomSheets()
            .setSubmitFilterListener(this)
            .setSelected(selectedFilter)
            .setFilter(filterItems)
        filterBottomSheet.show(childFragmentManager, this.javaClass.simpleName)
    }

    private fun findSortValue(filter: ParamFilterV2): Sort? =
        if (hotelSearchMapViewModel.liveSearchResult.value != null &&
            hotelSearchMapViewModel.liveSearchResult.value is Success
        ) {
            var sortOption =
                (hotelSearchMapViewModel.liveSearchResult.value as Success).data.displayInfo.sort
            sortOption.firstOrNull { it.displayName == filter.values.firstOrNull() }
        } else null

    private fun checkGps() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && permissionCheckerHelper.hasPermission(
                requireContext(),
                arrayOf(PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION)
            )
        ) {
            showDialogEnableGPS()
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        val locationDetectorHelper = LocationDetectorHelper(
            permissionCheckerHelper,
            fusedLocationClient,
            requireActivity().applicationContext
        )

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
                        locationDetectorHelper.getLocation(
                            hotelSearchMapViewModel.onGetLocation(), requireActivity(),
                            LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                            requireActivity().getString(R.string.hotel_destination_need_permission)
                        )
                    }

                }, getString(R.string.hotel_destination_need_permission)
            )
        }

    }

    private fun showDialogEnableGPS() {
        val dialog = DialogUnify(
            activity as AppCompatActivity,
            DialogUnify.HORIZONTAL_ACTION,
            DialogUnify.NO_IMAGE
        )
        dialog.setTitle(getString(R.string.hotel_recommendation_gps_dialog_title))
        dialog.setDescription(getString(R.string.hotel_recommendation_gps_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.hotel_recommendation_gps_dialog_ok))
        dialog.setPrimaryCTAClickListener {
            startActivityForResult(
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                REQUEST_CODE_GPS
            )
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
            Toaster.build(
                v, getString(R.string.hotel_destination_error_get_location),
                Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                getString(com.tokopedia.resources.common.R.string.general_label_ok)
            ).show()
        }
    }

    private fun setupFindWithMapButton() {
        context?.let {
            binding?.btnHotelSearchWithMap?.color = FloatingButtonUnify.COLOR_GREEN
            val wrapper = LinearLayout(it)
            wrapper.gravity = Gravity.CENTER

            val imageView = ImageView(it)
            imageView.setPadding(
                BUTTON_RADIUS_PADDING_ALL,
                BUTTON_RADIUS_PADDING_ALL,
                BUTTON_RADIUS_PADDING_RIGHT,
                BUTTON_RADIUS_PADDING_ALL
            )
            imageView.setImageResource(R.drawable.ic_hotel_search_maps)
            wrapper.addView(imageView)

            val textView = Typography(it)
            textView.apply {
                setHeadingText(BUTTON_RADIUS_HEADING_SIZE)
                setTextColor(
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
                text = getString(R.string.hotel_search_map_search_with_map)
            }
            wrapper.addView(textView)

            binding?.btnHotelSearchWithMap?.addItem(wrapper)
        }
        binding?.btnHotelSearchWithMap?.setOnClickListener {
            binding?.rvVerticalPropertiesHotelSearchMap?.scrollTo(0, 0)
            collapseBottomSheet()
        }
    }

    private fun showHotelResultList() {
        binding?.rvVerticalPropertiesHotelSearchMap?.visible()
    }

    private fun hideHotelResultList() {
        binding?.rvVerticalPropertiesHotelSearchMap?.gone()
    }

    private fun showSearchWithMap() {
        binding?.let {
            it.btnHotelSearchWithMap.visible()
            it.btnHotelSearchWithMap.show()
        }
    }

    private fun hideSearchWithMap() {
        binding?.let {
            it.btnHotelSearchWithMap.hide()
            it.btnHotelSearchWithMap.gone()
        }
    }

    private fun showErrorNoResult() {
        trackingHotelUtil.searchHotelNotFound(
            context,
            searchDestinationType,
            searchDestinationName,
            SEARCH_SCREEN_NAME
        )

        binding?.containerEmptyResultState?.visible()
        binding?.containerEmptyResultState?.doOnNextLayout {
            bottomSheetBehavior.peekHeight = binding?.containerEmptyResultState?.bottom ?: 0 +
                    resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl6)
        }
    }

    inline fun View.doOnNextLayout(crossinline action: (view: View) -> Unit) {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                view.removeOnLayoutChangeListener(this)
                action(view)
            }
        })
    }

    private fun hideErrorNoResult() {
        binding?.containerEmptyResultState?.gone()
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

    private fun putPriceMarkerOnTop(position: Int) {
        resetStackPriceMarker()
        if (!allMarker.isNullOrEmpty() && position != -1 && position < allMarker.size) {
            allMarker[position].zIndex = 1.0f
        }
    }

    private fun resetStackPriceMarker() {
        if (!allMarker.isNullOrEmpty()) {
            allMarker.forEach {
                it.zIndex = 0.0f
            }
        }
    }

    private fun buildFilter(data: PropertySearch) {
        showQuickFilterShimmering(false)

        initializeQuickFilter(data.quickFilter, data.filters, data.displayInfo.sort)

        binding?.quickFilterSortHotelSearchMap?.let { quickFilter ->
            quickFilter.chipItems?.filter {
                it.type == ChipsUnify.TYPE_SELECTED
            }?.forEach { _ ->
                quickFilter.indicatorCounter -= 1
            }
        }
    }

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
        private const val ARG_SORT_PARAM = "arg_hotel_sort_param"

        const val SELECTED_POSITION_INIT = 0
        const val DELAY_EMPTY_STATE: Long = 100L
        const val BUTTON_RADIUS_SHOW_VALUE: Float = 128f
        const val BUTTON_RADIUS_HIDE_VALUE: Float = -150f

        private const val MAPS_STREET_LEVEL_ZOOM: Float = 15f
        private const val MAPS_ZOOM_IN: Float = 11f
        private const val MAPS_ZOOM_OUT: Float = 9f
        private const val MAX_RADIUS: Float = 10.5f
        private const val MAP_CENTER_DIVIDER = 4
        private const val SLIDE_MINUS_OPACITY: Float = 1f
        private const val SLIDE_MULT_OPACITY: Float = 3f

        const val PREFERENCES_NAME = "hotel_search_map_preferences"
        const val SHOW_COACH_MARK_KEY = "hotel_search_map_show_coach_mark"

        fun createInstance(
            hotelSearchModel: HotelSearchModel,
            selectedParam: ParamFilterV2,
            selectedSort: String
        ): HotelSearchMapFragment =
            HotelSearchMapFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
                    putParcelable(ARG_FILTER_PARAM, selectedParam)
                    putString(ARG_SORT_PARAM, selectedSort)
                }
            }
    }
}
