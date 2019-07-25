package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailMapActivity
import kotlinx.android.synthetic.main.fragment_hotel_detail_map.*


/**
 * @author by furqan on 29/04/19
 */
class HotelDetailMapFragment : TkpdBaseV4Fragment(), OnMapReadyCallback {

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

    override fun getScreenName(): String = ""

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
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = true
            googleMap.uiSettings.isScrollGesturesEnabled = true

            context?.run {
                googleMap.addMarker(MarkerOptions().position(latLng).icon(bitmapDescriptorFromVector(this, R.drawable.ic_hotel_pin_location))
                        .title(getString(R.string.hotel_detail_map_marker_title, propertyName))
                        .snippet(getString(R.string.hotel_detail_map_marker_snippet, address))
                        .draggable(false))
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.setOnMapClickListener {
                // do nothing
                // need this even it's not used
                // it's used to override default function of OnMapClickListener
                // which is navigate to default Google Map Apps
            }
        }

        iv_go_to_gmap.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.hotel_google_map_intent_link, latitude, longitude, propertyName))
            })
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