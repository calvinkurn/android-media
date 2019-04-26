package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.fragment_hotel_detail.*

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailFragment : BaseDaggerFragment() {

//    private lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelDetailComponent::class.java).inject(this)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        this.googleMap = googleMap
//        setGoogleMap()
//    }

    private fun setupLayout() {
        (activity as HotelDetailActivity).setSupportActionBar(detail_toolbar)
        (activity as HotelDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as HotelDetailActivity).updateTitle(arguments?.getString(HotelDetailActivity.EXTRA_PROPERTY_ID))

        collapsing_toolbar.title = " "
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = "Avissa Suites"
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })

        iv_main_photo_preview.loadImage("https://q-xx.bstatic.com/xdata/images/hotel/max300/183774920.jpg?k=ac0ee9d89053eae24e3d5e23dabeb0f3aadbc4d5857cdf4cf130c950999c6a06&amp;o=")
        iv_first_photo_preview.loadImage("https://q-xx.bstatic.com/xdata/images/hotel/max300/183774920.jpg?k=ac0ee9d89053eae24e3d5e23dabeb0f3aadbc4d5857cdf4cf130c950999c6a06&amp;o=")
        iv_second_photo_preview.loadImage("https://q-xx.bstatic.com/xdata/images/hotel/max300/183774923.jpg?k=7cb4bfda6f21d7c9aa427e5472dbe7ecae218d8050b6e5c01d2c529fc7d2474c&amp;o=")
        iv_third_photo_preview.loadImage("https://q-xx.bstatic.com/xdata/images/hotel/max300/183774928.jpg?k=11c5465ec9f67150f3591694051e2431c7350c82137dc8f50e3c589e376851b9&amp;o=")

        for (i in 1..5) {
            hotel_rating_container.addView(RatingStarView(context!!))
        }

        iv_hotel_detail_location.loadImage("https://media-cdn.tripadvisor.com/media/photo-s/11/4d/61/99/location-map.jpg")

//        initHotelLocationMap()
    }

//    private fun initHotelLocationMap() {
//        if (map_view != null) {
//            map_view.onCreate(null)
//            map_view.onResume()
//            map_view.getMapAsync(this)
//        }
//
//        setGoogleMap()
//    }

//    private fun setGoogleMap() {
//        if (::googleMap.isInitialized) {
//            val latitude = getLatitude("-6.221091").toDouble()
//            val longitude = getLongitude("106.821637").toDouble()
//            val latLng = LatLng(latitude, longitude)
//
//            googleMap.uiSettings.isMapToolbarEnabled = false
//            googleMap.uiSettings.isZoomControlsEnabled = false
//            googleMap.uiSettings.isMyLocationButtonEnabled = false
//            googleMap.addMarker(
//                    MarkerOptions().position(latLng).icon(
//                            BitmapDescriptorFactory.fromResource(R.drawable.ic_hotel_pin_location))
//            ).isDraggable = false
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
//            googleMap.setOnMapClickListener {
//                // need this even it's not used
//                // it's used to override default function of OnMapClickListener
//                // which is navigate to default Google Map Apps
//            }
//        }
//    }

    private fun getLatitude(latitude: String): String = if (!latitude.isEmpty()) latitude else DEFAULT_LATITUDE

    private fun getLongitude(longitude: String): String = if (!longitude.isEmpty()) longitude else DEFAULT_LONGITUDE

    companion object {

        val DEFAULT_LATITUDE = "-6.221212"
        val DEFAULT_LONGITUDE = "106.819494"

        fun getInstance(checkInDate: String, checkOutDate: String, propertyId: Int, roomCount: Int,
                        adultCount: Int, childCount: Int = 0, enableButton: Boolean = true): HotelDetailFragment =
                HotelDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelDetailActivity.EXTRA_CHECK_IN_DATE, checkInDate)
                        putString(HotelDetailActivity.EXTRA_CHECK_OUT_DATE, checkOutDate)
                        putInt(HotelDetailActivity.EXTRA_PROPERTY_ID, propertyId)
                        putInt(HotelDetailActivity.EXTRA_ROOM_COUNT, roomCount)
                        putInt(HotelDetailActivity.EXTRA_ADULT_COUNT, adultCount)
                        putInt(HotelDetailActivity.EXTRA_CHILD_COUNT, childCount)
                        putBoolean(HotelDetailActivity.EXTRA_ENABLE_BUTTON, enableButton)
                    }
                }

    }
}