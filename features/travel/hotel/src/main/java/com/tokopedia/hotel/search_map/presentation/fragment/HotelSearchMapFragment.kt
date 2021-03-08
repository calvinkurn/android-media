package com.tokopedia.hotel.search_map.presentation.fragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultAdapter
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search_map.di.HotelSearchMapComponent
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity.Companion.SEARCH_SCREEN_NAME
import com.tokopedia.hotel.search_map.presentation.viewmodel.HotelSearchMapViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setHeadingText
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_hotel_search_map.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

/**
 * @author by furqan on 01/03/2021
 */
class HotelSearchMapFragment : BaseListFragment<Property, PropertyAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, HotelSearchResultAdapter.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var hotelSearchMapViewModel: HotelSearchMapViewModel

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    private lateinit var adapterCardList: HotelSearchResultAdapter

    private lateinit var googleMap: GoogleMap
    private lateinit var valueAnimator: ValueAnimator

    private var searchDestinationName = ""
    private var searchDestinationType = ""
    private var allMarker: ArrayList<Marker> = ArrayList()
    private var markerCounter: Int = INIT_MARKER_TAG
    private var screenHeight: Int = 0
    private var isInAnimation: Boolean = false
    private var cardListPosition: Int = SELECTED_POSITION_INIT
    private var hotelSearchModel: HotelSearchModel = HotelSearchModel()

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun createAdapterInstance(): BaseListAdapter<Property, PropertyAdapterTypeFactory> =
            HotelSearchResultAdapter(this, adapterTypeFactory)

    override fun initInjector() {
        getComponent(HotelSearchMapComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvVerticalPropertiesHotelSearchMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            hotelSearchMapViewModel = viewModelProvider.get(HotelSearchMapViewModel::class.java)

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            permissionCheckerHelper = PermissionCheckerHelper()
        }

        hotelSearchMapViewModel.setPermissionHelper(permissionCheckerHelper)

        arguments?.let {
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
                    changeMarkerState(cardListPosition)
                }
                is Fail -> {
                    hideCollapsingHeader()
                    animateCollapsingToolbar(COLLAPSING_FULL_SCREEN)
                    showGetListError(it.throwable)
                }
            }
        })

        hotelSearchMapViewModel.latLong.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    hotelSearchModel.apply {
                        long = it.data.first.toFloat()
                        lat = it.data.second.toFloat()
                    }
                    addMyLocation(LatLng(it.data.first, it.data.second))
                }
            }
        })

        /**Will add radius as search param*/
        hotelSearchMapViewModel.radius.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    hideFindNearHereView()
                    hotelSearchModel.apply {
                        searchType = HotelTypeEnum.COORDINATE.value
                        searchId = ""
                    }
                    hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
                    loadInitialData()
                }
                is Fail -> {
                    Toaster.make(requireView(), it.throwable.message.toString(), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                }
            }
        })

        hotelSearchMapViewModel.screenMidPoint.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    /** Should have trigger the LatLng here
                    hotelSearchModel.apply {
                        long = it.data.longitude.toFloat()
                        lat = it.data.latitude.toFloat()
                    }
                    */
                }
            }
        })
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        adapter.clearAllElements()
        adapterCardList.clearAllElements()
        showLoading()
        loadData(defaultInitialPage)
    }

    override fun showLoading() {
        adapter.removeErrorNetwork()
        adapter.setLoadingModel(loadingModel)
        adapter.showLoading()
        adapterCardList.setLoadingModel(loadingModel)
        adapterCardList.showLoading()
        hideSnackBarRetry()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewMap()
        initFloatingButton()
        initLocationMap()
        setUpTitleAndSubtitle()
        setupCollapsingToolbar()
        Handler().postDelayed({
            animateCollapsingToolbar(COLLAPSING_HALF_OF_SCREEN)
        }, ANIMATION_DETAIL_TIMES)
        initGetMyLocation()
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        setGoogleMap()
    }

    override fun onCameraIdle() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(DELAY_BUTTON_RADIUS)
            showFindNearHereView()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        allMarker.forEach {
            if (it.tag == marker.tag) {
                cardListPosition = it.tag as Int
                rvHorizontalPropertiesHotelSearchMap.smoothScrollToPosition(cardListPosition)
                changeMarkerState(cardListPosition)
            }
        }
        return true
    }

    override fun onItemClicked(property: Property, position: Int) {
        with(hotelSearchMapViewModel.searchParam) {
            context?.run {
                startActivityForResult(HotelDetailActivity.getCallingIntent(this,
                        checkIn, checkOut, property.id, room, guest.adult,
                        searchDestinationType, searchDestinationName, property.isDirectPayment),
                        REQUEST_CODE_DETAIL_HOTEL)
            }
        }
    }

    override fun loadData(page: Int) {
        val searchQuery = GraphqlHelper.loadRawString(resources, R.raw.gql_get_property_search)
        hotelSearchMapViewModel.searchProperty(page, searchQuery)
    }

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory = PropertyAdapterTypeFactory(this)

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {}

    override fun onItemClicked(t: Property) {}

    override fun showLoading() {
        tvHotelSearchListTitleLoader.visible()
        tvHotelSearchListTitle.gone()
        super.showLoading()
    }

    override fun hideLoading() {
        tvHotelSearchListTitleLoader.gone()
        tvHotelSearchListTitle.visible()
        super.hideLoading()
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

    private fun setCardListViewAdapter() {
        adapterCardList = HotelSearchResultAdapter(this, adapterTypeFactory)
    }

    private fun initRecyclerViewMap() {
        rvHorizontalPropertiesHotelSearchMap.adapter = adapterCardList
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvHorizontalPropertiesHotelSearchMap.layoutManager = linearLayoutManager

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
        })
    }

    private fun getCurrentItemCardList(): Int {
        return (rvHorizontalPropertiesHotelSearchMap.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
    }

    /**
     * Function to setup collapsing toolbar position
     *
     * use peekSize if you want to set the peekSize
     * use collapsingHeightSize if you want to set the collapsing toolbar by it's height multiplied by some number
     */
    private fun setupCollapsingToolbar(peekSize: Int = 0, collapsingHeightSize: Double = 1.0) {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                display.getRealSize(size)
            } catch (error: NoSuchMethodError) {
                display.getSize(size)
            }

            screenHeight = size.y
            val height = screenHeight * collapsingHeightSize
            val titleBarHeight = getActionBarHeight()

            val tmpHeight = height - peekSize - abs(titleBarHeight) - getSoftButtonsBarHeight()

            val layoutParams = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tmpHeight.toInt())
            layoutParams.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
            layoutParams.parallaxMultiplier = 0.6f
            toolbarConstraintContainer.layoutParams = layoutParams
            toolbarConstraintContainer.requestLayout()

            appBarHotelSearchMap.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                /**
                val oneThreeOfScreen = (screenHeight * COLLAPSING_ONE_THREE_OF_SCREEN).toInt()

                if (abs(verticalOffset) < oneThreeOfScreen && !isInAnimation) {
                showCardListView()
                } else {
                hideCardListView()
                }

                if (abs(verticalOffset) == 0) {
                showFindNearHereView()
                showTargetView()
                } else {
                hideFindNearHereView()
                hideTargetView()
                }*/
            })
        }
    }

    private fun getActionBarHeight(): Int {
        activity?.let {
            val tv = TypedValue()
            if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            }
        }
        return 0
    }

    @SuppressLint("NewApi")
    private fun getSoftButtonsBarHeight(): Int {
        val metrics = DisplayMetrics()
        activity?.let {
            it.windowManager.defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            it.windowManager.defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight) {
                realHeight - usableHeight
            } else {
                0
            }
        }
        return 0
    }

    private fun setGoogleMap() {
        if (::googleMap.isInitialized) {
            googleMap.uiSettings.isMapToolbarEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = true
            googleMap.uiSettings.isScrollGesturesEnabled = true

            googleMap.setOnMarkerClickListener(this)

            googleMap.setOnCameraIdleListener(this)

            googleMap.setOnMapClickListener {
                // do nothing
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
        val wrapper = LinearLayout(context)
        wrapper.gravity = Gravity.CENTER

        val imageView = ImageView(context)
        imageView.setPadding(BUTTON_RADIUS_PADDING_ALL, BUTTON_RADIUS_PADDING_ALL, BUTTON_RADIUS_PADDING_RIGHT, BUTTON_RADIUS_PADDING_ALL)
        imageView.setImageResource(R.drawable.ic_hotel_search_map_search)
        wrapper.addView(imageView)

        val textView = TextView(context)
        textView.apply {
            setHeadingText(BUTTON_RADIUS_HEADING_SIZE)
            setTextColor(ContextCompat.getColor(context, R.color.hotel_color_active_price_marker))
            text = getString(R.string.hotel_search_map_around_here)
        }
        wrapper.addView(textView)
        wrapper.setOnClickListener {
            showCardListView()
            hotelSearchMapViewModel.getMidPoint(googleMap.cameraPosition.target)
            hotelSearchMapViewModel.getVisibleRadius(googleMap)
        }

        btnGetRadiusHotelSearchMap.addItem(wrapper)
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun changeMarkerState(position: Int) {
        resetMarkerState()
        if (cardListPosition == position) allMarker[position].setIcon(createCustomMarker(requireContext(), HOTEL_PRICE_ACTIVE_PIN, allMarker[position].title))
    }

    private fun resetMarkerState() {
        allMarker.forEach {
            it.setIcon(createCustomMarker(requireContext(), HOTEL_PRICE_INACTIVE_PIN, it.title))
        }
    }

    private fun removeAllMarker() {
        markerCounter = INIT_MARKER_TAG
        allMarker.forEach {
            it.remove()
        }
        allMarker.clear()
    }

    /**Location permission is handled by LocationDetector*/
    private fun initGetMyLocation() {
        ivGetLocationHotelSearchMap.setOnClickListener {
            hideCardListView()
            showFindNearHereView()
            getCurrentLocation()
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes drawableId: Int): BitmapDescriptor {
        var drawable = ContextCompat.getDrawable(context, drawableId)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable?.run {
                drawable = DrawableCompat.wrap(this).mutate()
            }
        }

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
            background = resources.getDrawable(getPin(markerType))
            if (markerType == HOTEL_PRICE_ACTIVE_PIN) {
                setTextColor(color = resources.getColor(R.color.Unify_N0))
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
        val searchProperties = data.properties
        renderCardListMap(searchProperties)
        renderList(searchProperties.map {
            it.isForHorizontalItem = false
            it
        }.toList())

        searchProperties.forEach {
            addMarker(it.location.latitude.toDouble(), it.location.longitude.toDouble(), it.roomPrice[0].price)
        }

        showCoachMark()
    }

    private fun hideLoadingCardListMap() {
        adapterCardList.hideLoading()
    }

    private fun renderCardListMap(listProperty: List<Property>) {
        hideLoadingCardListMap()

        val dataCollection = mutableListOf<Visitable<*>>()
        dataCollection.addAll(listProperty.map {
            val newProperty = it.copy()
            newProperty.isForHorizontalItem = true
            newProperty
        }.toList())
        adapterCardList.clearAllElements()
        adapterCardList.addElement(dataCollection)
    }

    private fun setupValueAnimator() {
        val params: CoordinatorLayout.LayoutParams = appBarHotelSearchMap.layoutParams as CoordinatorLayout.LayoutParams
        val behavior: AppBarLayout.Behavior? = params.behavior as AppBarLayout.Behavior?

        behavior?.let {
            valueAnimator = ValueAnimator.ofInt()
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener { animation ->
                behavior.topAndBottomOffset = animation.animatedValue as Int
                appBarHotelSearchMap.requestLayout()
            }
        }
    }

    private fun animateCollapsingToolbar(collapsingHeightSize: Double = 1.0) {
        if (!::valueAnimator.isInitialized) {
            setupValueAnimator()
        }

        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                display.getRealSize(size)
            } catch (error: NoSuchMethodError) {
                display.getSize(size)
            }
            screenHeight = size.y
            val height = screenHeight.toDouble() * collapsingHeightSize

            val params: CoordinatorLayout.LayoutParams = appBarHotelSearchMap.layoutParams as CoordinatorLayout.LayoutParams
            val behavior: AppBarLayout.Behavior? = params.behavior as AppBarLayout.Behavior?

            behavior?.let {
                valueAnimator.cancel()
                valueAnimator.setIntValues(behavior.topAndBottomOffset, height.toInt() * -1)
                valueAnimator.duration = ANIMATION_DETAIL_TIMES
                valueAnimator.start()
            }
        }
    }

    private fun showCoachMark() {
        context?.let {
            if (PersistentCacheManager.instance.get(KEY_SEARCH_MAP_COACHMARK, Boolean::class.java, false) != true) {
                isInAnimation = true
                val coachMarkItem = arrayListOf(
                        CoachMark2Item(
                                invisibleView,
                                getString(R.string.hotel_search_map_coach_mark_map_title),
                                getString(R.string.hotel_search_map_coach_mark_map_desc),
                                CoachMark2.POSITION_BOTTOM
                        ),
                        CoachMark2Item(
                                contentContainerHotelSearchMap,
                                getString(R.string.hotel_search_map_coach_mark_list_title),
                                getString(R.string.hotel_search_map_coach_mark_list_desc),
                                CoachMark2.POSITION_TOP
                        ),
                        CoachMark2Item(
                                headerHotelSearchMap, // need to change to filter view later
                                getString(R.string.hotel_search_map_coach_mark_filter_title),
                                getString(R.string.hotel_search_map_coach_mark_filter_desc),
                                CoachMark2.POSITION_BOTTOM
                        )
                )
                val coachmark = CoachMark2(it)
                coachmark.setStepListener(object : CoachMark2.OnStepListener {
                    override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                        if (currentIndex == COACHMARK_LIST_STEP_POSITION) {
                            animateCollapsingToolbar(COLLAPSING_ONE_TENTH_OF_SCREEN)
                        } else if (currentIndex == COACHMARK_FILTER_STEP_POSITION) {
                            animateCollapsingToolbar(COLLAPSING_HALF_OF_SCREEN)
                        }
                    }
                })
                coachmark.onFinishListener = {
                    isInAnimation = false
                    PersistentCacheManager.instance.put(KEY_SEARCH_MAP_COACHMARK, true, TimeUnit.DAYS.toMillis(DAYS_A_YEAR))
                }
                coachmark.showCoachMark(coachMarkItem, null, 0)
            }
        }
    }

    private fun showCollapsingHeader(){
        tvHotelSearchListTitle.visible()
        topHotelSearchMapListKnob.visible()
    }

    private fun hideCollapsingHeader(){
        tvHotelSearchListTitle.gone()
        topHotelSearchMapListKnob.gone()
    }

    private fun showCardListView() {
        rvHorizontalPropertiesHotelSearchMap.visible()
    }

    private fun hideCardListView() {
        rvHorizontalPropertiesHotelSearchMap.gone()
    }

    private fun showTargetView() {
        ivGetLocationHotelSearchMap.visible()
    }

    private fun hideTargetView() {
        ivGetLocationHotelSearchMap.gone()
    }

    private fun showFindNearHereView() {
        btnGetRadiusHotelSearchMap.visible()
    }

    private fun hideFindNearHereView() {
        btnGetRadiusHotelSearchMap.gone()
    }

    private fun getCurrentLocation() {
        hotelSearchMapViewModel.getCurrentLocation(fusedLocationClient, requireActivity())
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

    companion object {
        private const val KEY_SEARCH_MAP_COACHMARK = "key_hotel_search_map_coachmark"
        private const val COACHMARK_LIST_STEP_POSITION = 1
        private const val COACHMARK_FILTER_STEP_POSITION = 2
        private const val DAYS_A_YEAR: Long = 365

        private const val ANIMATION_DETAIL_TIMES: Long = 500

        private const val COLLAPSING_HALF_OF_SCREEN = 1.0 / 2.0
        private const val COLLAPSING_ONE_THREE_OF_SCREEN = 1.0 / 3.0
        private const val COLLAPSING_ONE_TENTH_OF_SCREEN = 1.0 / 10.0
        private const val COLLAPSING_FULL_SCREEN = 1.0

        private const val REQUEST_CODE_DETAIL_HOTEL = 101

        private const val ANCHOR_MARKER_X: Float = 0.8f
        private const val ANCHOR_MARKER_Y: Float = 1f

        private const val MY_LOCATION_PIN = "MY LOCATION"
        private const val HOTEL_PRICE_ACTIVE_PIN = "HOTEL PRICE ACTIVE"
        private const val HOTEL_PRICE_INACTIVE_PIN = "HOTEL PRICE INACTIVE"

        private const val BUTTON_RADIUS_HEADING_SIZE = 6
        private const val BUTTON_RADIUS_PADDING_ALL = 0
        private const val BUTTON_RADIUS_PADDING_RIGHT = 6

        private const val INIT_MARKER_TAG = 0

        const val ARG_HOTEL_SEARCH_MODEL = "arg_hotel_search_model"
        private const val ARG_FILTER_PARAM = "arg_hotel_filter_param"

        const val SELECTED_POSITION_INIT = 0
        const val DELAY_BUTTON_RADIUS: Long = 1000L

        fun createInstance(hotelSearchModel: HotelSearchModel, selectedParam: ParamFilterV2): HotelSearchMapFragment =
                HotelSearchMapFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
                        putParcelable(ARG_FILTER_PARAM, selectedParam)
                    }
                }
    }
}