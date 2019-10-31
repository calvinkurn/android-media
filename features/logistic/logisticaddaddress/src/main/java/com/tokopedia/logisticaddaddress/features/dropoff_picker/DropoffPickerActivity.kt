package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.DaggerDropoffPickerComponent
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class DropoffPickerActivity : BaseActivity(), OnMapReadyCallback {

    private val REQUEST_CODE_LOCATION: Int = 1
    lateinit var mPermissionChecker: PermissionCheckerHelper
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mLocationCallback: LocationCallback
    private var mMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null

    lateinit var mSearchInput: SearchInputView
    lateinit var mSearchText: EditText
    lateinit var mDisabledLocationView: View
    lateinit var mNoPermissionsView: View
    lateinit var mButtonActivate: UnifyButton
    lateinit var mButtonGrant: UnifyButton
    lateinit var mBottomSheet: View
    lateinit var mBehavior: BottomSheetBehavior<View>
    val mNearbyAdapter: NearbyStoreAdapter = NearbyStoreAdapter()

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
        mBottomSheet = findViewById(R.id.bottom_sheet)
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

        val rv = findViewById<RecyclerView>(R.id.rv_dropoff)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = mNearbyAdapter
        mNearbyAdapter.setActionListener(object: NearbyStoreAdapter.ActionListener {
            override fun onItemClicked(view: View) {
                Toast.makeText(this@DropoffPickerActivity, "Item clicked!", Toast.LENGTH_SHORT).show()
            }
        })

        mBehavior = BottomSheetBehavior.from(mBottomSheet)
        mBehavior.state = BottomSheetBehavior.STATE_HIDDEN

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
                    val newLoc = LatLng(location.latitude, location.longitude)
                    moveCamera(newLoc)
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
        viewModel.stores.observe(this, Observer { result ->
            when (result) {
                is Fail -> Toast.makeText(this@DropoffPickerActivity, result.toString(), Toast.LENGTH_SHORT).show()
                is Success -> {
                    mNearbyAdapter.setData(result.data)
                    mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })
    }

    private fun moveCamera(latLng: LatLng) {
        setMapView()
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(16f)
                .build()
        mMap?.addMarker(MarkerOptions().position(latLng))
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
                                            val newLoc = LatLng(it.latitude, it.longitude)
                                            moveCamera(newLoc)
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
