package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.DaggerDropoffPickerComponent
import com.tokopedia.logisticdata.data.constant.LogisticConstant
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class DropoffPickerActivity : BaseActivity(), OnMapReadyCallback {

    private val REQUEST_CODE_LOCATION: Int = 1
    private val GREEN_ARGB = 0x40388E3C

    lateinit var mPermissionChecker: PermissionCheckerHelper
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mLocationCallback: LocationCallback

    private var mMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mNearbiesBehavior: BottomSheetBehavior<View>? = null
    private var mDetailBehavior: BottomSheetBehavior<View>? = null
    private var mLastLocation: LatLng = LatLng(0.0, 0.0)
    private val mNearbyAdapter: NearbyStoreAdapter = NearbyStoreAdapter()
    private val mMarkerList: MutableList<Marker> = arrayListOf()

    lateinit var mSearchInput: SearchInputView
    lateinit var mSearchText: EditText
    lateinit var mDisabledLocationView: View
    lateinit var mNoPermissionsView: View
    lateinit var mButtonActivate: UnifyButton
    lateinit var mButtonGrant: UnifyButton
    lateinit var mStoreDetail: LocationDetailBottomSheet

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(DropoffPickerViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropoff_picker)

        DaggerDropoffPickerComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build().inject(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        setSupportActionBar(toolbar)
        mSearchInput = findViewById(R.id.search_input_dropoff)
        mDisabledLocationView = findViewById(R.id.view_gps_empty)
        mNoPermissionsView = findViewById(R.id.view_no_permissions)
        mButtonActivate = findViewById(R.id.button_activate_gps)
        mButtonActivate.setOnClickListener {
            checkAndRequestLocation()
        }
        mButtonGrant = findViewById(R.id.button_grant_permission)
        mButtonGrant.setOnClickListener {
            checkForPermission()
        }
        mSearchText = mSearchInput.searchTextView
        mSearchText.isCursorVisible = false
        mSearchText.isFocusable = false
        mStoreDetail = findViewById(R.id.bottom_sheet_detail)
        mStoreDetail.setOnCancelClickListener { mDetailBehavior?.state = BottomSheetBehavior.STATE_HIDDEN }
        mStoreDetail.setOnOkClickListener { _, data ->
            val resultIntent = Intent().apply {
                putExtra(LogisticConstant.RESULT_DATA_STORE_LOCATION, data) }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val rv = findViewById<RecyclerView>(R.id.rv_dropoff)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mNearbyAdapter
        mNearbyAdapter.setActionListener(object : NearbyStoreAdapter.ActionListener {
            override fun onItemClicked(view: View) {
                showStoreDetail(view.tag as LocationDataModel)
            }
        })

        mNearbiesBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
        mNearbiesBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        mDetailBehavior = BottomSheetBehavior.from(mStoreDetail)
        mDetailBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this,
                    com.tokopedia.design.R.color.green_600)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mPermissionChecker = PermissionCheckerHelper()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                stopLocationRequest()
                if (result != null && result.locations.isNotEmpty()) {
                    val location = result.locations[0]
                    mLastLocation = LatLng(location.latitude, location.longitude)
                    moveCamera(mLastLocation)
                }
            }
        }

        mMapFragment = supportFragmentManager.findFragmentById(R.id.map_dropoff) as SupportMapFragment
        mMapFragment?.getMapAsync(this)

        setObservers()
        checkForPermission()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.setOnMapClickListener {
            setDefaultMap()
        }
        mMap?.setOnMarkerClickListener {
            val tag: Any? = it.tag
            if (tag is LocationDataModel) {
                showStoreDetail(tag)
            }
            true
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (resultCode == Activity.RESULT_CANCELED) setLocationEmptyView()
                else if (resultCode == Activity.RESULT_OK) checkForPermission()
            }
        }
    }

    private fun setObservers() {
        viewModel.storeData.observe(this, Observer { result ->
            when (result) {
                is Fail -> Toast.makeText(this@DropoffPickerActivity, result.toString(), Toast.LENGTH_SHORT).show()
                is Success -> {
                    drawStoreLocations(result.data.nearbyStores)
                    mNearbyAdapter.setData(result.data.nearbyStores)
                    mNearbiesBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                    drawCircle(result.data.radius)
                }
            }
        })
    }

    private fun checkForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionChecker.checkPermissions(this, getPermissions(),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            mPermissionChecker.onPermissionDenied(this@DropoffPickerActivity, permissionText)
                            setNoPermissionsView()
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            mPermissionChecker.onNeverAskAgain(this@DropoffPickerActivity, permissionText)
                            setNoPermissionsView()
                        }

                        override fun onPermissionGranted() {
                            mFusedLocationClient.lastLocation
                                    .addOnSuccessListener {
                                        if (it != null) {
                                            mLastLocation = LatLng(it.latitude, it.longitude)
                                            moveCamera(mLastLocation)
                                        } else {
                                            // If it is null, either Google Play Service has just
                                            // been restarted or the location service is deactivated
                                            checkAndRequestLocation()
                                        }
                                    }
                                    .addOnFailureListener { _ -> setLocationEmptyView() }
                        }

                    },
                    "")
        }
    }

    private fun setDefaultMap() {
        mNearbiesBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        mDetailBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        mMarkerList.forEach {
            it.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_store_map_green))
        }
    }

    private fun drawCircle(radius: Int) {
        if (radius > 0) {
            mMap?.addCircle(CircleOptions().center(mLastLocation)
                    .radius(radius * 1000.0)
                    .fillColor(GREEN_ARGB)
                    .strokeWidth(0.0f))
        }
    }

    private fun moveCamera(latLng: LatLng) {
        setMapView()
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(16f)
                .build()
        mMap?.addMarker(MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_self_map_green)))
        mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        viewModel.getStores("${latLng.latitude},${latLng.longitude}")
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

    private fun showStoreDetail(datum: LocationDataModel) {
        mMarkerList.forEach {
            val tag = it.tag
            if (tag is LocationDataModel) {
                if (tag.addrId == datum.addrId) {
                    it.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_store_map_white))
                } else {
                    it.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_store_map_green))
                }
            }
        }
        mStoreDetail.setStore(datum)
        if (mNearbiesBehavior?.state != BottomSheetBehavior.STATE_HIDDEN) {
            mNearbiesBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        mDetailBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun drawStoreLocations(data: List<LocationDataModel>) {
        mMarkerList.clear()
        for (datum in data) {
            val marker = mMap?.addMarker(MarkerOptions()
                    .position(LatLng(datum.latitude.toDouble(), datum.longitude.toDouble()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_store_map_green)))
            marker?.let {
                it.tag = datum
                mMarkerList.add(it)
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }

    private fun checkAndRequestLocation() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            // Request location update once then remove when settings are satisfied
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null)
        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    it.startResolutionForResult(this@DropoffPickerActivity, REQUEST_CODE_LOCATION)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

    }

    private fun stopLocationRequest() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

}
