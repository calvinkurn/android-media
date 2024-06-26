package com.tokopedia.dropoff.ui.dropoff_picker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.dropoff.R
import com.tokopedia.dropoff.databinding.ActivityDropoffPickerBinding
import com.tokopedia.dropoff.di.DaggerDropoffPickerComponent
import com.tokopedia.dropoff.domain.mapper.GetStoreMapper
import com.tokopedia.dropoff.ui.autocomplete.AutoCompleteActivity
import com.tokopedia.dropoff.ui.dropoff_picker.model.DropoffNearbyModel
import com.tokopedia.dropoff.util.SimpleVerticalDivider
import com.tokopedia.dropoff.util.getDescription
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticCommon.util.bitmapDescriptorFromVector
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

const val REQUEST_CODE_LOCATION: Int = 1
const val REQUEST_CODE_AUTOCOMPLETE: Int = 2

class DropoffPickerActivity : BaseActivity(), OnMapReadyCallback {

    companion object {
        private const val MAP_CAMERA_ZOOM = 16f
        private const val LATITUDE_KEY = "BUNDLE_LATITUDE"
        private const val LONGITUDE_KEY = "BUNDLE_LONGITUDE"
        private const val REVERSE_GEOCODE_DELAY = 1000L
    }

    private var mPermissionChecker: PermissionCheckerHelper? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null

    private var mMap: GoogleMap? = null
    private var mMapFragment: SupportMapFragment? = null
    private var mNearbiesBehavior: BottomSheetBehavior<View>? = null
    private var mDetailBehavior: BottomSheetBehavior<View>? = null
    private var mLastLocation: LatLng = LatLng(0.0, 0.0)
    private val mNearbyAdapter: NearbyStoreAdapter = NearbyStoreAdapter()
    private val mMarkerList: MutableList<Marker> = arrayListOf()

    private var reverseGeocodeJob: Job? = null

    private val storeBitmap: BitmapDescriptor? by lazy {
        bitmapDescriptorFromVector(this, R.drawable.ic_map_store_green)
    }

    @Inject
    lateinit var dropoffMapper: GetStoreMapper

    @Inject
    lateinit var tracker: DropOffAnalytics

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, factory)[DropoffPickerViewModel::class.java]
    }

    private var binding: ActivityDropoffPickerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDropoffPickerBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initInjector()
        setSupportActionBar(binding?.toolbarSearch)

        binding?.viewGpsEmpty?.buttonActivateGps?.apply {
            setOnClickListener {
                tracker.trackClickActivateGps()
                checkAndRequestLocation()
            }
        }

        binding?.viewNoPermissions?.buttonGrantPermission?.apply {
            setOnClickListener {
                checkForPermission()
            }
        }

        binding?.bottomSheetDetail?.apply {
            setOnCancelClickListener { _, data ->
                data?.let { tracker.trackClickBatalOnDetail(it) }
                mDetailBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }

            setOnOkClickListener { _, data ->
                data?.let { tracker.trackClickPilihOnDetail(it) }
                val resultIntent = Intent().apply {
                    val intentData = data?.let { dropoffMapper.mapToIntentModel(it) }
                    putExtra(LogisticConstant.RESULT_DATA_STORE_LOCATION, intentData)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        binding?.searchInputDropoff?.apply {
            setOnClickListener(goToAutoComplete)
            with(searchBarTextField) {
                setOnClickListener(goToAutoComplete)
                isCursorVisible = false
                isFocusable = false
            }
        }

        binding?.bottomSheetDropoff?.apply {
            rvDropoff.apply {
                layoutManager = LinearLayoutManager(this@DropoffPickerActivity)
                setHasFixedSize(true)
                addItemDecoration(
                    SimpleVerticalDivider(
                        this@DropoffPickerActivity,
                        R.layout.item_nearby_location
                    )
                )
                adapter = mNearbyAdapter
            }

            mNearbyAdapter.setActionListener(adapterListener)

            mNearbiesBehavior = BottomSheetBehavior.from(bottomSheet)
            mNearbiesBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            mNearbiesBehavior?.setBottomSheetCallback(
                object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) {
                        // no op
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            tracker.trackExpandList()
                        }
                    }
                }
            )
        }

        binding?.bottomSheetDetail?.let {
            mDetailBehavior = BottomSheetBehavior.from(it)
            mDetailBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setObservers()
        initializeMap()
    }

    private fun initializeMap() {
        mMapFragment =
            supportFragmentManager.findFragmentById(R.id.map_dropoff) as SupportMapFragment
        if (MapsAvailabilityHelper.isMapsAvailable(this)) {
            mPermissionChecker = PermissionCheckerHelper()
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    stopLocationRequest()
                    if (result.locations.isNotEmpty()) {
                        val location = result.locations[0]
                        moveCamera(getLatLng(location.latitude, location.longitude))
                    }
                }
            }

            mMapFragment?.getMapAsync(this)
            checkForPermission()
        } else {
            setNoMapsAvailableView()
            getNearestStoreFromIntent()
        }
    }

    private fun getNearestStoreFromIntent(data: Intent = intent) {
        var latitude = data.getStringExtra(LATITUDE_KEY) ?: ""
        var longitude = data.getStringExtra(LONGITUDE_KEY) ?: ""
        if (latitude.isEmpty() || longitude.isEmpty()) {
            val lca = ChooseAddressUtils.getLocalizingAddressData(this)
            if (lca.lat.isNotEmpty() && lca.long.isNotEmpty()) {
                latitude = lca.lat
                longitude = lca.long
            }
        }
        if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
            mNearbyAdapter.setStateLoading()
            viewModel.getStores("$latitude,$longitude")
        }
    }

    private fun showDisableLocAndNoPermissionView(
        isShowDisabledLocationView: Boolean,
        isShowNoPermissionView: Boolean
    ) {
        binding?.viewGpsEmpty?.root?.isVisible = isShowDisabledLocationView
        binding?.viewNoPermissions?.root?.isVisible = isShowNoPermissionView
    }

    private fun setNoMapsAvailableView() {
        showDisableLocAndNoPermissionView(
            isShowDisabledLocationView = false,
            isShowNoPermissionView = false
        )
        mMapFragment?.let {
            supportFragmentManager.beginTransaction().hide(it).commit()
        }
        findViewById<ImageView>(R.id.iv_pinpoint).visibility = View.GONE
        findViewById<Ticker>(R.id.ticker_map_unavailable).visibility = View.VISIBLE
        mNearbiesBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.setOnMapClickListener {
            setDefaultMap()
        }
        mMap?.setOnMarkerClickListener {
            val tag: Any? = it.tag
            if (tag is DropoffNearbyModel) {
                tracker.trackSelectIndoMaretMap(tag)
                showStoreDetail(tag)
            }
            true
        }
        mMap?.let {
            it.setupOnCameraMoveListener {
                val target = it.cameraPosition.target
                mLastLocation = getLatLng(target.latitude, target.longitude)
                mNearbyAdapter.setStateLoading()
                mNearbiesBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                viewModel.getStores("${target.latitude},${target.longitude}")
            }
        }
    }

    private fun GoogleMap.setupOnCameraMoveListener(onNext: () -> Unit) {
        setOnCameraMoveListener {
            reverseGeocodeJob?.cancel()
            reverseGeocodeJob = CoroutineScope(Dispatchers.Main).launch {
                delay(REVERSE_GEOCODE_DELAY)
                onNext.invoke()
            }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionChecker?.onRequestPermissionsResult(
            this,
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == mPermissionChecker?.REQUEST_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                tracker.trackUserClickIzinkan()
            } else {
                tracker.trackUserClickNantiSaja()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    setLocationEmptyView()
                } else if (resultCode == Activity.RESULT_OK) checkForPermission()
            }
            REQUEST_CODE_AUTOCOMPLETE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (MapsAvailabilityHelper.isMapsAvailable(this)) {
                        val latitude = data.getStringExtra(LATITUDE_KEY) ?: ""
                        val longitude = data.getStringExtra(LONGITUDE_KEY) ?: ""
                        moveCamera(getLatLng(latitude, longitude))
                    } else {
                        getNearestStoreFromIntent(data)
                    }
                }
            }
        }
    }

    private fun initInjector() {
        DaggerDropoffPickerComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun setObservers() {
        viewModel.storeData.observe(
            this,
            Observer { result ->
                when (result) {
                    is Fail -> mNearbyAdapter.setError()
                    is Success -> {
                        drawStoreLocations(result.data.nearbyStores)
                        mNearbyAdapter.setData(result.data.nearbyStores)
                        mNearbiesBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                        drawCircle(result.data.radius)
                    }
                }
            }
        )
    }

    @SuppressWarnings("MissingPermission")
    private fun checkForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionChecker?.checkPermissions(
                this,
                getPermissions(),
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        mPermissionChecker?.onPermissionDenied(
                            this@DropoffPickerActivity,
                            permissionText
                        )
                        setNoPermissionsView()
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        mPermissionChecker?.onNeverAskAgain(
                            this@DropoffPickerActivity,
                            permissionText
                        )
                        setNoPermissionsView()
                    }

                    override fun onPermissionGranted() {
                        mFusedLocationClient?.lastLocation
                            ?.addOnSuccessListener {
                                if (it != null) {
                                    moveCamera(getLatLng(it.latitude, it.longitude))
                                    LocationDetectorHelper(this@DropoffPickerActivity).saveToCache(
                                        it.latitude, it.longitude
                                    )
                                } else {
                                    // If it is null, either Google Play Service has just
                                    // been restarted or the location service is deactivated
                                    checkAndRequestLocation()
                                }
                            }
                            ?.addOnFailureListener { _ -> setLocationEmptyView() }
                    }
                },
                ""
            )
        }
    }

    private fun setDefaultMap() {
        tracker.trackClickMap()
        mNearbiesBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        mDetailBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        mMarkerList.forEach {
            it.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_map_store_green))
        }
    }

    private fun drawCircle(radius: Int) {
        if (radius > 0) {
            val circleColor =
                ContextCompat.getColor(this, unifyprinciplesR.color.Unify_GN600)
            val alphaCircleColor = ColorUtils.setAlphaComponent(circleColor, 40)
            mMap?.addCircle(
                CircleOptions().center(mLastLocation)
                    .radius(radius * 1000.0)
                    .fillColor(alphaCircleColor)
                    .strokeWidth(0.0f)
            )
        }
    }

    private fun moveCamera(latLng: LatLng) {
        mLastLocation = latLng
        setMapView()
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(MAP_CAMERA_ZOOM)
            .build()
        mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mNearbyAdapter.setStateLoading()
        mNearbiesBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        viewModel.getStores("${latLng.latitude},${latLng.longitude}")
    }

    private fun setLocationEmptyView() {
        if (mMapFragment?.isVisible == true) {
            supportFragmentManager.beginTransaction().hide(mMapFragment!!).commit()
        }
        showDisableLocAndNoPermissionView(
            isShowDisabledLocationView = true,
            isShowNoPermissionView = false
        )
    }

    private fun setNoPermissionsView() {
        if (mMapFragment?.isVisible == true) {
            supportFragmentManager.beginTransaction().hide(mMapFragment!!).commit()
        }
        showDisableLocAndNoPermissionView(
            isShowDisabledLocationView = false,
            isShowNoPermissionView = true
        )
    }

    private fun setMapView() {
        if (mMapFragment?.isHidden == true) {
            supportFragmentManager.beginTransaction().show(mMapFragment!!).commit()
        }
        showDisableLocAndNoPermissionView(
            isShowDisabledLocationView = false,
            isShowNoPermissionView = false
        )
    }

    private fun showStoreDetail(datum: DropoffNearbyModel) {
        mMarkerList.forEach {
            val tag = it.tag
            if (tag is DropoffNearbyModel) {
                if (tag.addrId == datum.addrId) {
                    it.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_map_store_white_big))
                } else {
                    it.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_map_store_green))
                }
            }
        }
        binding?.bottomSheetDetail?.setStore(datum)
        if (mNearbiesBehavior?.state != BottomSheetBehavior.STATE_HIDDEN) {
            mNearbiesBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
        mDetailBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun drawStoreLocations(data: List<DropoffNearbyModel>) {
        mMarkerList.clear()
        mMap?.clear()
        for (datum in data) {
            val marker = mMap?.addMarker(
                MarkerOptions()
                    .position(getLatLng(datum.latitude, datum.longitude))
                    .icon(storeBitmap)
            )
            marker?.let {
                it.tag = datum
                mMarkerList.add(it)
            }
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
            PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun checkAndRequestLocation(looper: Looper? = Looper.myLooper() ) {
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
            looper?.let {
                mLocationCallback?.let { it1 ->
                    mFusedLocationClient?.requestLocationUpdates(locationRequest,
                        it1, looper)
                }
            }
        }
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    it.startResolutionForResult(this@DropoffPickerActivity, REQUEST_CODE_LOCATION)
                } catch (@Suppress("SwallowedException") sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun stopLocationRequest() {

        mLocationCallback?.let { mFusedLocationClient?.removeLocationUpdates(it) }
    }

    private val goToAutoComplete: (View?) -> Unit = {
        if (binding?.viewGpsEmpty?.root?.visibility == View.VISIBLE) {
            tracker.trackClickSearchBarGpsOff("")
        } else {
            tracker.trackClickSearchBarGpsOn("")
        }
        val intent = Intent(this, AutoCompleteActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        overridePendingTransition(R.anim.autocomplete_slide_in_up, R.anim.stay_still)
    }

    private val adapterListener: NearbyStoreAdapter.ActionListener =
        object : NearbyStoreAdapter.ActionListener {
            override fun onItemClicked(view: View, position: Int) {
                val data = view.tag as DropoffNearbyModel
                if (position == NearbyStoreAdapter.NEAREST_ITEM) {
                    tracker.trackSelectStoreListFirst(
                        data.addrName,
                        data.getDescription(),
                        data.type.toString()
                    )
                } else {
                    tracker.trackSelectStoreListAll(
                        data.addrName,
                        data.getDescription(),
                        data.type.toString()
                    )
                }
                showStoreDetail(data)
            }

            override fun requestAutoComplete() {
                goToAutoComplete.invoke(null)
            }
        }
}
