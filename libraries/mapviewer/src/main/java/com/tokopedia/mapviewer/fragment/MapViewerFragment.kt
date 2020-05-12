package com.tokopedia.mapviewer.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.mapviewer.R
import com.tokopedia.mapviewer.activity.MapViewerActivity
import kotlinx.android.synthetic.main.fragment_map_viewer.*

class MapViewerFragment : TkpdBaseV4Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private lateinit var propertyName: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var address: String
    private lateinit var pin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            propertyName = it.getString(MapViewerActivity.EXTRA_NAME, "")
            latitude = it.getDouble(MapViewerActivity.EXTRA_LATITUDE)
            longitude = it.getDouble(MapViewerActivity.EXTRA_LONGITUDE)
            address = it.getString(MapViewerActivity.EXTRA_ADDRESS, "")
            pin = it.getString(MapViewerActivity.EXTRA_PIN,"")
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        setGoogleMap()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_map_viewer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHotelLocationMap()
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
            googleMap.uiSettings.isMyLocationButtonEnabled = false
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isRotateGesturesEnabled = true
            googleMap.uiSettings.isScrollGesturesEnabled = true

            context?.run {
                googleMap.addMarker(MarkerOptions().position(latLng).icon(bitmapDescriptorFromVector(this, getPin(pin)))
                        .title(getString(R.string.mapviewer_detail_map_marker_title, propertyName))
                        .snippet(getString(R.string.mapviewer_detail_map_marker_snippet, address))
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
                data = Uri.parse(getString(R.string.mapviewer_google_map_intent_link, latitude, longitude, latitude, longitude, propertyName))
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

    private fun getPin(pin : String ): Int{
        return if(pin.equals(HOTEL_PIN)){
            R.drawable.ic_hotel_pin
        }
        else {
            R.drawable.ic_event_pin
        }
    }

    companion object {
        const val HOTEL_PIN = "HOTEL_PIN"
        const val DEFAULT_PIN = "DEFAULT_PIN"

        fun getInstance(propertyName: String, latitude: Double, longitude: Double, address: String, pin: String): MapViewerFragment =
                MapViewerFragment().also {
                    it.arguments = Bundle().apply {
                        putString(MapViewerActivity.EXTRA_NAME, propertyName)
                        putDouble(MapViewerActivity.EXTRA_LATITUDE, latitude)
                        putDouble(MapViewerActivity.EXTRA_LONGITUDE, longitude)
                        putString(MapViewerActivity.EXTRA_ADDRESS, address)
                        putString(MapViewerActivity.EXTRA_PIN, pin)
                    }
                }
    }
}