package com.tokopedia.hotel.search_map.presentation.fragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultAdapter
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search_map.di.HotelSearchMapComponent
import com.tokopedia.hotel.search_map.presentation.activity.HotelSearchMapActivity.Companion.SEARCH_SCREEN_NAME
import com.tokopedia.hotel.search_map.presentation.viewmodel.HotelSearchMapViewModel
import com.tokopedia.unifycomponents.setHeadingText
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_search_map.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * @author by furqan on 01/03/2021
 */
class HotelSearchMapFragment : BaseListFragment<Property, PropertyAdapterTypeFactory>(),
        BaseEmptyViewHolder.Callback, HotelSearchResultAdapter.OnClickListener, OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var hotelSearchMapViewModel: HotelSearchMapViewModel

    private lateinit var adapterCardList: HotelSearchResultAdapter

    private lateinit var googleMap: GoogleMap
    private lateinit var valueAnimator: ValueAnimator

    private lateinit var localCacheHandler: LocalCacheHandler

    private var searchDestinationName = ""
    private var searchDestinationType = ""
    private var allMarker: ArrayList<Marker> = ArrayList()

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun createAdapterInstance(): BaseListAdapter<Property, PropertyAdapterTypeFactory> =
            HotelSearchResultAdapter(this, adapterTypeFactory)

    override fun initInjector() {
        getComponent(HotelSearchMapComponent::class.java).inject(this)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvVerticalPropertiesHotelSearchMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        hotelSearchMapViewModel = viewModelProvider.get(HotelSearchMapViewModel::class.java)

        context?.let {
            localCacheHandler = LocalCacheHandler(it, HOTEL_SEARCH_MAP_PREFERENCE)
        }

        arguments?.let {
            val hotelSearchModel = it.getParcelable(ARG_HOTEL_SEARCH_MODEL) ?: HotelSearchModel()
            hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
            searchDestinationName = hotelSearchModel.name
            searchDestinationType = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hotelSearchMapViewModel.liveSearchResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewMap()
        initFloatingButton()
        showLoadingCardListMap()
        initLocationMap()
        setupContentPeekSize()
        Handler().postDelayed({
            animateCollapsingToolbar(COLLAPSING_HALF_OF_SCREEN)
        }, 500)
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        setGoogleMap()
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

    private fun initRecyclerViewMap() {
        adapterCardList = HotelSearchResultAdapter(this, adapterTypeFactory)

        rvHorizontalPropertiesHotelSearchMap.adapter = adapterCardList
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvHorizontalPropertiesHotelSearchMap.layoutManager = linearLayoutManager
    }

    /**
     * Function to setup collapsing toolbar position
     *
     * use peekSize if you want to set the peekSize
     * use collapsingHeightSize if you want to set the collapsing toolbar by it's height multiplied by some number
     */
    private fun setupContentPeekSize(peekSize: Int = 0, collapsingHeightSize: Double = 1.0) {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                display.getRealSize(size)
            } catch (error: NoSuchMethodError) {
                display.getSize(size)
            }

            val height = size.y * collapsingHeightSize
            val titleBarHeight = getTitleBarHeight(it)

            val tmpHeight = height - peekSize - abs(titleBarHeight) - getSoftButtonsBarHeight()

            val layoutParams = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tmpHeight.toInt())
            layoutParams.collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
            layoutParams.parallaxMultiplier = 0.6f
            toolbarConstraintContainer.layoutParams = layoutParams
            toolbarConstraintContainer.requestLayout()
        }
    }

    private fun getTitleBarHeight(fragmentActivity: FragmentActivity): Int {
        val rect = Rect()
        val window = fragmentActivity.window
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight = rect.top
        val contentViewTop = window.findViewById<View>(ID_ANDROID_CONTENT).top
        return contentViewTop - statusBarHeight
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

        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.ic_hotel_search_map_search)
        wrapper.addView(imageView)

        val textView = TextView(context)
        textView.setHeadingText(5)
        textView.text = getString(R.string.hotel_search_map_around_here)
        wrapper.addView(textView)
        wrapper.setOnClickListener {}

        btnGetRadiusHotelSearchMap.addItem(wrapper)
    }

    private fun addMarker(latitude: Double, longitude: Double, price: String) {
        val latLng = LatLng(latitude, longitude)

        context?.run {
            allMarker.add(googleMap.addMarker(MarkerOptions().position(latLng).icon(createCustomMarker(this, price))
                    .draggable(false)))
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun createCustomMarker(context: Context, price: String): BitmapDescriptor {
        val marker: View = View.inflate(context, R.layout.custom_price_marker, null)

        val bgMarker = marker.findViewById<View>(R.id.bg_marker)
        bgMarker.setBackgroundResource(R.drawable.price_marker_default)

        val txtPrice = marker.findViewById<View>(R.id.txtPriceHotelMarker) as Typography
        txtPrice.text = price

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
            MY_LOCATION_PIN -> R.drawable.ic_get_location
            HOTEL_PRICE_ACTIVE_PIN -> R.drawable.ic_hotel_price_active
            HOTEL_PRICE_INACTIVE_PIN -> R.drawable.price_marker_default
            else -> R.drawable.ic_get_location
        }
    }

    private fun onSuccessGetResult(data: PropertySearch) {
        val searchProperties = data.properties
        renderCardListMap(searchProperties)
        renderList(searchProperties)

        searchProperties.forEach {
            addMarker(it.location.latitude.toDouble(), it.location.longitude.toDouble(), it.roomPrice[0].price)
        }

        showCoachMark()
    }

    private fun showLoadingCardListMap() {
        adapter.removeErrorNetwork()
        adapter.setLoadingModel(loadingModel)
        adapter.showLoading()
    }

    private fun hideLoadingCardListMap() {
        adapterCardList.hideLoading()
    }

    private fun renderCardListMap(listProperty: List<Property>) {
        hideLoadingCardListMap()

        val dataCollection = mutableListOf<Visitable<*>>()
        dataCollection.addAll(listProperty)
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
            val height = size.y.toDouble() * collapsingHeightSize

            val params: CoordinatorLayout.LayoutParams = appBarHotelSearchMap.layoutParams as CoordinatorLayout.LayoutParams
            val behavior: AppBarLayout.Behavior? = params.behavior as AppBarLayout.Behavior?

            behavior?.let {
                valueAnimator.cancel()
                valueAnimator.setIntValues(behavior.topAndBottomOffset, height.toInt() * -1)
                valueAnimator.duration = 400
                valueAnimator.start()
            }
        }
    }

    private fun showCoachMark() {
        context?.let {
            localCacheHandler.putBoolean(KEY_SEARCH_MAP_COACHMARK, false)

            if (!localCacheHandler.getBoolean(KEY_SEARCH_MAP_COACHMARK, false)) {
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
                            animateCollapsingToolbar(COLLAPSING_ONE_TENTHS_OF_SCREEN)
                        } else if (currentIndex == COACHMARK_FILTER_STEP_POSITION) {
                            animateCollapsingToolbar(COLLAPSING_HALF_OF_SCREEN)
                        }
                    }

                })
                coachmark.onFinishListener = {
                    localCacheHandler.putBoolean(KEY_SEARCH_MAP_COACHMARK, true)
                }
                coachmark.showCoachMark(coachMarkItem, null, 0)
            }
        }
    }

    companion object {
        private const val HOTEL_SEARCH_MAP_PREFERENCE = "hotel_search_map_preference"
        private const val KEY_SEARCH_MAP_COACHMARK = "key_hotel_search_map_coachmark"
        private const val COACHMARK_LIST_STEP_POSITION = 1
        private const val COACHMARK_FILTER_STEP_POSITION = 2

        private const val COLLAPSING_HALF_OF_SCREEN = 1.0 / 2.0
        private const val COLLAPSING_ONE_TENTHS_OF_SCREEN = 1.0 / 10.0

        private const val REQUEST_CODE_DETAIL_HOTEL = 101

        private const val MY_LOCATION_PIN = "MY LOCATION"
        private const val HOTEL_PRICE_ACTIVE_PIN = "HOTEL PRICE ACTIVE"
        private const val HOTEL_PRICE_INACTIVE_PIN = "HOTEL PRICE INACTIVE"

        const val ARG_HOTEL_SEARCH_MODEL = "arg_hotel_search_model"
        private const val ARG_FILTER_PARAM = "arg_hotel_filter_param"

        fun createInstance(hotelSearchModel: HotelSearchModel, selectedParam: ParamFilterV2): HotelSearchMapFragment =
                HotelSearchMapFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
                        putParcelable(ARG_FILTER_PARAM, selectedParam)
                    }
                }
    }
}