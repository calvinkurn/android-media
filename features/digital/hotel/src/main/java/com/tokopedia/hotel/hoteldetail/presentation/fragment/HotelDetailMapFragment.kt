package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailMapActivity
import kotlinx.android.synthetic.main.fragment_hotel_detail_map.*

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private lateinit var propertyName: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            propertyName = it.getString(HotelDetailMapActivity.EXTRA_NAME, "")
            latitude = it.getDouble(HotelDetailMapActivity.EXTRA_LATITUDE)
            longitude = it.getDouble(HotelDetailMapActivity.EXTRA_LONGITUDE)
            address = it.getString(HotelDetailMapActivity.EXTRA_ADDRESS, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHotelLocationMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        setGoogleMap()
    }

    private fun initHotelLocationMap() {
        if (map_view != null) {
            map_view.onCreate(null)
            map_view.onResume()
            map_view.getMapAsync(this)
        }

        setGoogleMap()
    }

    private fun setGoogleMap() {
        if (::googleMap.isInitialized) {
            val latitude = latitude
            val longitude = longitude
            val latLng = LatLng(latitude, longitude)

            googleMap.uiSettings.isMapToolbarEnabled = false
            googleMap.uiSettings.isZoomControlsEnabled = false
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.addMarker(MarkerOptions().position(latLng).icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_hotel_pin_location))
                    .title(getString(R.string.hotel_detail_map_marker_title, propertyName, address))
                    .draggable(false))

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.setOnMapClickListener {
                // need this even it's not used
                // it's used to override default function of OnMapClickListener
                // which is navigate to default Google Map Apps
            }
        }
    }

    companion object {
        fun getInstance(propertyName: String, latitude: Double, longitude: Double, address: String): HotelDetailMapFragment =
                HotelDetailMapFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelDetailMapActivity.EXTRA_NAME, propertyName)
                        putDouble(HotelDetailMapActivity.EXTRA_LATITUDE, latitude)
                        putDouble(HotelDetailMapActivity.EXTRA_LONGITUDE, longitude)
                        putString(HotelDetailMapActivity.EXTRA_ADDRESS, address)
                    }
                }
    }
}