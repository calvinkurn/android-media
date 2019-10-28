package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.permissionchecker.PermissionCheckerHelper


class DropoffPickerActivity : BaseActivity(), OnMapReadyCallback {

    lateinit var mMapView: MapView
    lateinit var mSearchInput: SearchInputView
    lateinit var mSearchText: EditText
    lateinit var mPermissionChecker: PermissionCheckerHelper
    var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropoff_picker)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        setSupportActionBar(toolbar)
        mSearchInput = findViewById(R.id.search_input_dropoff)
        mSearchText = mSearchInput.searchTextView
        mSearchText.isCursorVisible = false
        mSearchText.isFocusable = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this,
                    com.tokopedia.design.R.color.green_600)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mPermissionChecker = PermissionCheckerHelper()

        val mapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map_dropoff) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        val locationHelper = LocationDetectorHelper(mPermissionChecker, LocationServices.getFusedLocationProviderClient(this), this)
        locationHelper.getLocation({ location ->
            val newLoc = LatLng(location.latitude, location.longitude)
            moveCamera(newLoc)
        }, this, LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        mPermissionChecker.onRequestPermissionsResult(this,
                requestCode, permissions,
                grantResults)
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(16f)
                .build()
        mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}
