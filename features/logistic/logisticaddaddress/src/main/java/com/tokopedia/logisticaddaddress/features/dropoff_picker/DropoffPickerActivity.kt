package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.logisticaddaddress.R
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.abstraction.base.view.activity.BaseActivity


class DropoffPickerActivity : BaseActivity(), OnMapReadyCallback {

    lateinit var mMapView: MapView
    var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropoff_picker)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        setSupportActionBar(toolbar)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map_dropoff) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
