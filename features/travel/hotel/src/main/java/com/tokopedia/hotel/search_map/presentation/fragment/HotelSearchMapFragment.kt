package com.tokopedia.hotel.search_map.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
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
    private lateinit var recyclerViewCardList: RecyclerView

    private lateinit var googleMap: GoogleMap

    var searchDestinationName = ""
    var searchDestinationType = ""

    var allMarker: List<Marker> = listOf()

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun createAdapterInstance(): BaseListAdapter<Property, PropertyAdapterTypeFactory> {
        return HotelSearchResultAdapter(this, adapterTypeFactory)
    }

    override fun initInjector() {
        getComponent(HotelSearchMapComponent::class.java).inject(this)
    }

    private fun initRecyclerViewMap(view: View){
        recyclerViewCardList = view.findViewById(R.id.recycler_view_map)
        adapterCardList = HotelSearchResultAdapter(this, adapterTypeFactory)

        recyclerViewCardList.adapter = adapterCardList
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCardList.layoutManager = linearLayoutManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        hotelSearchMapViewModel = viewModelProvider.get(HotelSearchMapViewModel::class.java)

        arguments?.let {
            val hotelSearchModel = it.getParcelable(ARG_HOTEL_SEARCH_MODEL) ?: HotelSearchModel()
            hotelSearchMapViewModel.initSearchParam(hotelSearchModel)
            searchDestinationName = hotelSearchModel.name
            searchDestinationType = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        hotelSearchMapViewModel.liveSearchResult.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessGetResult(it.data)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_search_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewMap(view)

        showLoadingCardListMap()

        initLocationMap()

        setupContentPeekSize()
    }

    private fun setupContentPeekSize() {
        activity?.let {
            val display = it.windowManager.defaultDisplay
            val size = Point()
            try {
                display.getRealSize(size)
            } catch (error: NoSuchMethodError) {
                display.getSize(size)
            }

            val height = size.y
            val titleBarHeight = getTitleBarHeight(it)

            val tmpHeight = height - abs(titleBarHeight) - getSoftButtonsBarHeight()

            val layoutParams = CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, tmpHeight)
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
        if (map_view != null) {
            map_view.onCreate(null)
            map_view.onResume()
            map_view.getMapAsync(this)
        }
        setGoogleMap()
    }

    private fun addMarker(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)

        context?.run {
            googleMap.addMarker(MarkerOptions().position(latLng).icon(bitmapDescriptorFromVector(this, getPin(HOTEL_PRICE_INACTIVE_PIN)))
                    .draggable(false))
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
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

    private fun getPin(markerType: String): Int{
        return when(markerType){
            MY_LOCATION_PIN -> R.drawable.ic_get_location
            HOTEL_PRICE_ACTIVE_PIN -> R.drawable.ic_hotel_price_active
            HOTEL_PRICE_INACTIVE_PIN -> R.drawable.ic_hotel_price_inactive
            else -> R.drawable.ic_get_location
        }
    }

    private fun onSuccessGetResult(data: PropertySearch) {
        val searchProperties = data.properties
        renderCardListMap(searchProperties)
        renderList(searchProperties)

        searchProperties.forEach {
            addMarker(it.location.latitude.toDouble(),it.location.longitude.toDouble())
            Log.d("Sukses",it.location.latitude.toString() + it.location.longitude.toString())
        }
        /** add marker*/
    }

    private fun showLoadingCardListMap(){
        adapter.removeErrorNetwork()
        adapter.setLoadingModel(loadingModel)
        adapter.showLoading()
    }

    private fun hideLoadingCardListMap(){
        adapterCardList.hideLoading()
    }

    private fun renderCardListMap(listProperty: List<Property>){
        hideLoadingCardListMap()

        val dataCollection = mutableListOf<Visitable<*>>()
        dataCollection.addAll(listProperty)
        adapterCardList.renderList(dataCollection)
    }

    companion object {
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