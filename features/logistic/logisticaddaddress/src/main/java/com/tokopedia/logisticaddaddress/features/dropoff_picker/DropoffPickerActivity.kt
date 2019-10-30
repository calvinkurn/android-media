package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.unifycomponents.UnifyButton


class DropoffPickerActivity : BaseActivity(), OnMapReadyCallback {

    lateinit var mSearchInput: SearchInputView
    lateinit var mSearchText: EditText
    lateinit var mDisabledLocationView: View
    lateinit var mNoPermissionsView: View
    lateinit var mButtonActivate: UnifyButton
    lateinit var mPermissionChecker: PermissionCheckerHelper
    lateinit var mLocationHelper: LocationDetectorHelper
    private var mMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropoff_picker)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        setSupportActionBar(toolbar)
        mSearchInput = findViewById(R.id.search_input_dropoff)
        mDisabledLocationView = findViewById(R.id.view_gps_empty)
        mNoPermissionsView = findViewById(R.id.view_no_permissions)
        mButtonActivate = findViewById(R.id.button_activate_gps)
        mButtonActivate.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
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
        mLocationHelper = LocationDetectorHelper(mPermissionChecker,
                LocationServices.getFusedLocationProviderClient(this), this)

        mMapFragment = supportFragmentManager.findFragmentById(R.id.map_dropoff) as SupportMapFragment
        mMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mLocationHelper.getLocation(onLocationReceived(), this, LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD)
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
                requestCode, permissions, grantResults)
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(16f)
                .build()
        mMap?.addMarker(MarkerOptions().position(latLng))
        mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun onLocationReceived(): (DeviceLocation) -> Unit = {
        if (it.hasLocation()) {
            val newLoc = LatLng(it.latitude, it.longitude)
            moveCamera(newLoc)
        } else {
            setLocationEmptyView()
        }
    }

    private fun setLocationEmptyView() {
        if (mMapFragment?.isVisible == true) {
            supportFragmentManager.beginTransaction().hide(mMapFragment!!).commit()
        }
        mDisabledLocationView.visibility = View.VISIBLE
        mNoPermissionsView.visibility = View.GONE
    }

    private fun setNoPermissionsView() {
        if (mMapFragment?.isVisible == true) {
            supportFragmentManager.beginTransaction().hide(mMapFragment!!).commit()
        }
        mDisabledLocationView.visibility = View.GONE
        mNoPermissionsView.visibility = View.VISIBLE
    }

    private fun setMapView() {
        if (mMapFragment?.isHidden == true) {
            supportFragmentManager.beginTransaction().show(mMapFragment!!).commit()
        }
        mDisabledLocationView.visibility = View.GONE
        mNoPermissionsView.visibility = View.GONE
    }

}
