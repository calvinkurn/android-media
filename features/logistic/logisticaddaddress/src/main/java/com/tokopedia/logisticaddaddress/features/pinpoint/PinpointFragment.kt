package com.tokopedia.logisticaddaddress.features.pinpoint

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.tasks.OnFailureListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_CITY_NAME
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_DISTRICT_NAME
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_WH_DISTRICT_ID
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_ADDRESS_NEW
import com.tokopedia.logisticCommon.data.constant.PinpointSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isAdd
import com.tokopedia.logisticCommon.uimodel.isEdit
import com.tokopedia.logisticCommon.uimodel.isEditOrPinpointOnly
import com.tokopedia.logisticCommon.uimodel.isPinpointOnly
import com.tokopedia.logisticCommon.uimodel.toAddressUiState
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticCommon.util.rxPinPoint
import com.tokopedia.logisticCommon.util.toCompositeSubs
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_STATE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_DISTRICT_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT_WAREHOUSE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_PINPOINT_MODEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_ADDRESS_DATA
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentPinpointNewBinding
import com.tokopedia.logisticaddaddress.di.addeditaddress.AddEditAddressComponent
import com.tokopedia.logisticaddaddress.features.addeditaddress.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.features.analytics.LogisticAddAddressAnalytics
import com.tokopedia.logisticaddaddress.features.analytics.LogisticEditAddressAnalytics
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.ChoosePinpoint
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.uimodel.PinpointBottomSheetState
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.PinpointWebviewActivity
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_AUTOCOMPLETE
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.LOCATION_NOT_FOUND
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.MAPS_EMPTY
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DENIED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DONT_ASK_AGAIN
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_GRANTED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_NOT_DEFINED
import com.tokopedia.logisticaddaddress.utils.AddNewAddressUtils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject


class PinpointFragment : BaseDaggerFragment(), OnMapReadyCallback {

    companion object {
        private const val RESULT_PERMISSION_CODE = 1234
        private const val REQUEST_CODE_PERMISSION = 9876

        private const val ZOOM_LEVEL = 16f
        private const val MAP_BOUNDARY_STROKE_WIDTH = 3F
        private const val LOCATION_REQUEST_INTERVAL = 10000L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L
        private const val GPS_DELAY = 1000L

        const val SUCCESS = "success"
        const val NOT_SUCCESS = "not success"

        fun newInstance(extra: Bundle): PinpointFragment {
            val state = if (extra.getBoolean(EXTRA_IS_GET_PINPOINT_ONLY, false)) {
                AddressUiState.PinpointOnly.name
            } else {
                extra.getString(EXTRA_ADDRESS_STATE, AddressUiState.PinpointOnly.name)
            }

            val isPositiveFlow = if (extra.containsKey(EXTRA_IS_POSITIVE_FLOW)) {
                extra.getBoolean(EXTRA_IS_POSITIVE_FLOW)
            } else {
                null
            }

            return PinpointFragment().apply {
                arguments = Bundle().apply {
                    // general
                    putDouble(EXTRA_LAT, extra.getDouble(EXTRA_LAT))
                    putDouble(EXTRA_LONG, extra.getDouble(EXTRA_LONG))
                    putString(PARAM_SOURCE, extra.getString(PARAM_SOURCE))
                    putString(EXTRA_ADDRESS_STATE, state)

                    // from address form
                    if (isPositiveFlow != null) {
                        putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
                    }
                    putString(EXTRA_ADDRESS_ID, extra.getString(EXTRA_ADDRESS_ID))
                    putLong(EXTRA_DISTRICT_ID, extra.getLong(EXTRA_DISTRICT_ID))

                    // from search page
                    putParcelable(EXTRA_AUTOCOMPLETE, extra.getParcelable(EXTRA_AUTOCOMPLETE))

                    // pinpoint only
                    putParcelable(
                        EXTRA_SAVE_DATA_UI_MODEL,
                        extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
                    )
                    putString(EXTRA_DISTRICT_NAME, extra.getString(EXTRA_DISTRICT_NAME))
                    putString(EXTRA_CITY_NAME, extra.getString(EXTRA_CITY_NAME))
                    // pinpoint only, shop address use case
                    putBoolean(EXTRA_IS_EDIT_WAREHOUSE, extra.getBoolean(EXTRA_IS_EDIT_WAREHOUSE))
                    putLong(EXTRA_WH_DISTRICT_ID, extra.getLong(EXTRA_WH_DISTRICT_ID))
                }
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PinpointViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PinpointViewModel::class.java)
    }

    // device location
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false
    private var permissionState: Int = PERMISSION_NOT_DEFINED
    private var isAccessAppPermissionFromSettings: Boolean = false
    private var isGmsAvailable: Boolean = true

    private val requiredPermissions: Array<String>
        get() = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!hasRequestedLocation) {
                    // send to maps
                    hasRequestedLocation = true
                }
                stopLocationUpdate()
                moveMap(
                    getLatLng(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    )
                )
                viewModel.getDistrictData(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude
                )
                context?.let { ctx ->
                    LocationDetectorHelper(ctx).saveToCache(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude
                    )
                }
            }
        }

    // google map
    private var composite = CompositeSubscription()
    private var googleMap: GoogleMap? = null
    private var currentPlaceId: String = ""

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    // state
    private var addressUiState: AddressUiState = AddressUiState.AddAddress
    private var source: String? = ""

    private var binding by autoClearedNullable<FragmentPinpointNewBinding> {
        it.mapViews.onDestroy()
    }
    private var bottomSheetInfo: BottomSheetUnify? = null
    private var bottomSheetLocUndefined: BottomSheetUnify? = null

    private val gpsResultResolutionContract = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            bottomSheetLocUndefined?.dismiss()
            if (allPermissionsGranted()) {
                showLoading()
                Handler().postDelayed({ getLocation() }, GPS_DELAY)
            }
        }
    }

    private val addressFormContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onResultFromAddressForm(it.data)
        }
    }

    private val searchPageContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onResultFromSearchPage(it.data)
        }
    }

    private val pinpointLiteContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { result -> handlePinpointLite(result) }
        } else {
            activity?.finish()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddEditAddressComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinpointNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBackPressed()
        initObserver()
        checkMapsAvailability(savedInstanceState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (activity != null && context != null) {
            permissionState = AddNewAddressUtils.getPermissionStateFromResult(
                requireActivity(),
                requireContext(),
                permissions
            )
        }
        when (permissionState) {
            PERMISSION_GRANTED -> {
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    showBottomSheetLocUndefined(false)
                }
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickAllowLocationPinpoint(userSession.userId)
                }
            }

            PERMISSION_DENIED -> {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickDontAllowLocationPinpoint(userSession.userId)
                }
            }

            PERMISSION_DONT_ASK_AGAIN -> {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickDontAllowLocationPinpoint(userSession.userId)
                }
                showBottomSheetLocUndefined(true)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let { MapsInitializer.initialize(requireActivity()) }

        moveMap(
            getLatLng(viewModel.uiModel.lat, viewModel.uiModel.long)
        )

        viewModel.getDistrictBoundaries()

        this.googleMap?.setOnCameraMoveStartedListener { _ ->
            showLoading()
        }
        this.googleMap?.let {
            rxPinPoint(it).subscribe(object : Subscriber<Boolean>() {
                override fun onNext(t: Boolean?) {
                    getAutofill()
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                }
            }).toCompositeSubs(composite)
        }
    }

    override fun onResume() {
        super.onResume()
        binding?.mapViews?.onResume()
        if (isAccessAppPermissionFromSettings) {
            isAccessAppPermissionFromSettings = false
            if (allPermissionsGranted()) {
                bottomSheetLocUndefined?.dismiss()
                permissionState = PERMISSION_GRANTED
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    showBottomSheetLocUndefined(false)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding?.mapViews?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapViews?.onStop()
    }

    override fun onPause() {
        binding?.mapViews?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding?.mapViews?.onDestroy()
        composite.unsubscribe()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapViews?.onLowMemory()
    }

    // region init
    private fun initData() {
        arguments?.apply {
            addressUiState = getString(EXTRA_ADDRESS_STATE).toAddressUiState()
            val isPositiveFlow = if (containsKey(EXTRA_IS_POSITIVE_FLOW)) {
                getBoolean(EXTRA_IS_POSITIVE_FLOW)
            } else {
                null
            }
            viewModel.onViewCreated(
                districtName = getString(EXTRA_DISTRICT_NAME).orEmpty(),
                cityName = getString(EXTRA_CITY_NAME).orEmpty(),
                lat = getDouble(EXTRA_LAT),
                long = getDouble(EXTRA_LONG),
                districtId = getLong(EXTRA_DISTRICT_ID),
                whDistrictId = getLong(EXTRA_WH_DISTRICT_ID, 0),
                addressId = getString(EXTRA_ADDRESS_ID, ""),
                uiState = getString(EXTRA_ADDRESS_STATE).toAddressUiState(),
                isEditWarehouse = getBoolean(EXTRA_IS_EDIT_WAREHOUSE, false),
                source = getString(PARAM_SOURCE, ""),
                isPositiveFlow = isPositiveFlow,
                searchAddressData = getParcelable(EXTRA_AUTOCOMPLETE)
            )
            source = getString(PARAM_SOURCE, "")

            getGmsAvailability(this)
        }
    }

    private fun getGmsAvailability(bundle: Bundle) {
        isGmsAvailable = if (bundle.containsKey(EXTRA_GMS_AVAILABILITY)) {
            bundle.getBoolean(
                EXTRA_GMS_AVAILABILITY,
                true
            )
        } else {
            context?.let { ctx -> MapsAvailabilityHelper.isMapsAvailable(ctx) } ?: true
        }
    }

    private fun fetchData() {
        viewModel.fetchData()
    }

    private fun initObserver() {
        viewModel.districtBoundary.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showBoundaries(it.data.geometry.listCoordinates)
                }

                else -> {
                    // no-op
                }
            }
        }

        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                PinpointAction.GetCurrentLocation -> {
                    getCurrentLocation()
                }

                is PinpointAction.InvalidDistrictPinpoint -> {
                    if (addressUiState.isAdd()) {
                        LogisticAddAddressAnalytics.onViewToasterPinpointTidakSesuai(userSession.userId)
                    }
                    val error = when (it.source) {
                        PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.ADD_ADDRESS_BUYER -> {
                            getString(R.string.txt_toaster_pinpoint_unmatched)
                        }

                        PinpointAction.InvalidDistrictPinpoint.InvalidDistrictPinpointSource.SHOP_ADDRESS -> {
                            getString(R.string.toaster_not_avail_shop_loc)
                        }
                    }
                    showErrorToaster(error)
                }

                is PinpointAction.NetworkError -> {
                    if (it.errorText.isNotEmpty()) {
                        showErrorToaster(it.errorText)
                    }
                }
            }
        }

        viewModel.pinpointBottomSheet.observe(viewLifecycleOwner) {
            when (it) {
                is PinpointBottomSheetState.LocationDetail -> {
                    showMap()
                    showDistrictBottomSheet(it)
                }

                is PinpointBottomSheetState.LocationInvalid -> {
                    showInvalidPinpointBottomSheet(it)
                }
            }
        }

        viewModel.choosePinpoint.observe(viewLifecycleOwner) {
            when (it) {
                is ChoosePinpoint.GoToAddressForm -> {
                    goToAddressForm(it)
                }

                is ChoosePinpoint.SetPinpointResult -> {
                    setPinpointResult(it)
                }
            }
        }

        viewModel.map.observe(viewLifecycleOwner) {
            showMap()
            moveMap(getLatLng(it.lat, it.long))
        }
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (addressUiState) {
                        AddressUiState.EditAddress -> {
                            LogisticEditAddressAnalytics.onClickBackPinpoint(userSession.userId)
                        }

                        AddressUiState.AddAddress -> {
                            LogisticAddAddressAnalytics.onClickBackArrowPinpoint(userSession.userId)
                        }

                        else -> {
                            // no op
                        }
                    }
                    activity?.finish()
                }
            }
        )
    }

    private fun setFusedLocationClient() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
    }

    private fun checkMapsAvailability(savedInstanceState: Bundle?) {
        if (isGmsAvailable) {
            prepareMap(savedInstanceState)
            setFusedLocationClient()
            fetchData()
            setViewListener()
        } else {
            context?.let { ctx -> goToLitePinpoint(ctx) }
        }
    }

    // region maps
    private fun stopLocationUpdate() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    private fun getCurrentLocation() {
        if (allPermissionsGranted()) {
            permissionState = PERMISSION_GRANTED
            if (AddNewAddressUtils.isGpsEnabled(context)) {
                getLocation()
            } else {
                showBottomSheetLocUndefined(false)
            }
        } else {
            when (permissionState) {
                PERMISSION_DENIED, PERMISSION_NOT_DEFINED -> {
                    requestPermissionLocation()
                }

                PERMISSION_DONT_ASK_AGAIN -> {
                    showBottomSheetLocUndefined(true)
                }
            }
        }
    }

    private fun getAutofill() {
        val target: LatLng? = this.googleMap?.cameraPosition?.target
        val latTarget = target?.latitude ?: 0.0
        val longTarget = target?.longitude ?: 0.0

        viewModel.getDistrictData(latTarget, longTarget)
    }

    private fun moveMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(ZOOM_LEVEL)
            .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun prepareMap(savedInstanceState: Bundle?) {

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        binding?.mapViews?.onCreate(mapViewBundle)
        binding?.mapViews?.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        binding?.mapViews?.onSaveInstanceState(mapViewBundle)
    }

    private fun requestPermissionLocation() {
        requestPermissions(requiredPermissions, REQUEST_CODE_PERMISSION)
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (activity?.let {
                ContextCompat.checkSelfPermission(
                        it,
                        permission
                    )
            } != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(looper: Looper? = Looper.myLooper()) {
        showLoading()
        fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
            if (data != null) {
                moveMap(getLatLng(data.latitude, data.longitude))
                viewModel.getDistrictData(data.latitude, data.longitude)
                context?.let { ctx ->
                    LocationDetectorHelper(ctx).saveToCache(
                        data.latitude, data.longitude
                    )
                }
            } else {
                looper?.let {
                    fusedLocationClient?.requestLocationUpdates(
                        AddNewAddressUtils.getLocationRequest(),
                        locationCallback,
                        looper
                    )
                }
            }
        }
    }

    private fun goToSettingLocationDevice() {
        context?.let { turnGPSOn(it) }
    }

    private fun goToSettingLocationApps() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        isAccessAppPermissionFromSettings = true
        activity?.startActivityForResult(intent, RESULT_PERMISSION_CODE)
    }

    private fun turnGPSOn(context: Context): Boolean {
        var isGpsOn = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mSettingsClient = LocationServices.getSettingsClient(context)

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = LOCATION_REQUEST_INTERVAL
        locationRequest.fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGpsOn = true
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(context as Activity) {
                    //  GPS is already enable, callback GPS status through listener
                    isGpsOn = true
                }
                .addOnFailureListener(
                    context,
                    OnFailureListener { e ->
                        if (e is ApiException) {
                            when (e.statusCode) {
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        if (e is ResolvableApiException) {
                                            val intentSenderRequest = IntentSenderRequest.Builder(
                                                e.resolution.intentSender
                                            ).build()
                                            gpsResultResolutionContract.launch(intentSenderRequest)
                                        }
                                    } catch (sie: IntentSender.SendIntentException) {
                                        sie.printStackTrace()
                                    }

                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                    val errorMessage =
                                        "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                )
        }
        return isGpsOn
    }

    // region ui
    private fun setViewListener() {
        binding?.run {
            bottomsheetLocation.btnInfo.setOnClickListener {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickIconQuestion(userSession.userId)
                }
                bottomsheetLocation.root.hide()
                showBottomSheetInfo()
            }

            chipsCurrentLoc.chipImageResource =
                context?.let { getIconUnifyDrawable(it, IconUnify.TARGET) }
            chipsCurrentLoc.setOnClickListener {
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onClickGunakanLokasiSaatIniPinpoint(userSession.userId)
                    }

                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onClickGunakanLokasiSaatIniPinpoint(userSession.userId)
                    }

                    else -> {
                        // no op
                    }
                }
                getCurrentLocation()
            }
            if (addressUiState.isEdit()) {
                chipsSearch.chipText = getString(R.string.pinpointpage_chips_search_edit)
            }
            chipsSearch.chipImageResource =
                context?.let { getIconUnifyDrawable(it, IconUnify.SEARCH) }
            chipsSearch.setOnClickListener {
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onClickCariUlangAlamat(userSession.userId)
                    }

                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onClickCariUlangAlamat(userSession.userId)
                    }

                    else -> {
                        // no op
                    }
                }
                goToSearchPage()
            }
        }
    }

    private fun showMap() {
        binding?.run {
            mapViews.visible()
            mapsEmpty.gone()
        }
    }

    private fun showBoundaries(boundaries: List<LatLng>) {
        this.googleMap?.addPolygon(
            PolygonOptions()
                .addAll(boundaries)
                .strokeWidth(MAP_BOUNDARY_STROKE_WIDTH)
        )
    }

    private fun showLoading() {
        binding?.bottomsheetLocation?.run {
            wholeLoadingContainer.visibility = View.VISIBLE
            districtLayout.visibility = View.GONE
            invalidLayout.visibility = View.GONE
        }
    }

    private fun showErrorToaster(errorMessage: String) {
        view?.let { view ->
            Toaster.build(
                view,
                errorMessage,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    private fun showDistrictBottomSheet(state: PinpointBottomSheetState.LocationDetail) {
        binding?.bottomsheetLocation?.run {
            districtLayout.visible()
            wholeLoadingContainer.gone()
            invalidLayout.gone()

            if (state.title.isNotEmpty()) {
                tvAddress.text = state.title
            }

            if (state.description.isNotEmpty()) {
                tvAddressDesc.text = state.description
            }

            btnPrimary.run {
                if (state.buttonPrimary.show) {
                    visible()
                } else {
                    gone()
                }
                if (state.buttonPrimary.text.isNotEmpty()) {
                    text = state.buttonPrimary.text
                }

                setOnClickListener {
                    when (addressUiState) {
                        AddressUiState.EditAddress -> {
                            LogisticEditAddressAnalytics.onClickPilihLokasiIni(userSession.userId)
                        }

                        AddressUiState.AddAddress -> {
                            if (viewModel.isPositiveFlow) {
                                LogisticAddAddressAnalytics.onClickPilihLokasiPositive(userSession.userId)
                            } else {
                                LogisticAddAddressAnalytics.onClickPilihLokasiNegative(
                                    userSession.userId,
                                    if (state.buttonPrimary.success) SUCCESS else NOT_SUCCESS
                                )
                            }
                        }

                        else -> {
                            // no op
                        }
                    }

                    if (state.buttonPrimary.enable) {
                        viewModel.validatePinpoint()
                    }
                }
            }

            btnSecondary.run {
                if (state.buttonSecondary.show) {
                    visible()
                } else {
                    gone()
                }

                setOnClickListener {
                    LogisticAddAddressAnalytics.onClickIsiAlamatManual(userSession.userId)

                    if (state.buttonSecondary.enable) {
                        viewModel.discardPinpoint()
                    }
                }
            }
        }
    }

    private fun showInvalidPinpointBottomSheet(data: PinpointBottomSheetState.LocationInvalid) {
        binding?.run {
            if (data.showMapIllustration) {
                mapsEmpty.visible()
                mapViews.gone()
                imgMapsEmpty.setImageUrl(MAPS_EMPTY)
            }
            bottomsheetLocation.run {
                invalidLayout.visible()
                wholeLoadingContainer.gone()
                districtLayout.gone()
                imgInvalidLoc.setImageUrl(data.image)
                if (data.title.isNotEmpty()) {
                    tvInvalidLoc.text = data.title
                }
                if (data.description.isNotEmpty()) {
                    tvInvalidLocDetail.text = data.description
                }

                btnAnaNegative.run {
                    if (data.buttonState.show) {
                        setInvalidButtonData(data)
                        setOnClickListener {
                            if (addressUiState.isAdd()) {
                                when (data.type) {
                                    PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE -> {
                                        LogisticAddAddressAnalytics.onClickIsiAlamatOutOfIndo(
                                            userSession.userId
                                        )
                                    }

                                    PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND -> {
                                        LogisticAddAddressAnalytics.onClickIsiAlamatManualUndetectedLocation(
                                            userSession.userId
                                        )
                                    }
                                }
                            }
                            if (data.buttonState.enable) {
                                viewModel.discardPinpoint()
                            }
                        }
                        visible()
                    } else {
                        gone()
                    }
                }
            }
            hitInvalidLocationImpressionAnalytic(data.type)
        }
    }

    private fun hitInvalidLocationImpressionAnalytic(type: PinpointBottomSheetState.LocationInvalid.LocationInvalidType) {
        when (type) {
            PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE -> {
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onImpressBottomSheetOutOfIndo(userSession.userId)
                    }

                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onImpressBottomSheetOutOfIndo(userSession.userId)
                    }

                    else -> {
                        // no op
                    }
                }
            }

            PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND -> {
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onImpressBottomSheetAlamatTidakTerdeteksi(
                            userSession.userId
                        )
                    }

                    AddressUiState.AddAddress -> {
                        LogisticAddAddressAnalytics.onImpressBottomSheetAlamatTidakTerdeteksi(
                            userSession.userId
                        )
                    }

                    else -> {
                        // no op
                    }
                }
            }
        }
    }

    private val PinpointBottomSheetState.LocationInvalid.title: CharSequence
        get() {
            return when (this.type) {
                PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE -> {
                    getString(R.string.out_of_indonesia_title)
                }

                PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND -> {
                    getString(R.string.undetected_location_new_edit)
                }
            }
        }
    private val PinpointBottomSheetState.LocationDetail.PrimaryButtonUiModel.text: CharSequence
        get() {
            return if (addressUiState.isPinpointOnly()) {
                getString(R.string.btn_choose_this_location)
            } else {
                getString(R.string.btn_choose_location)
            }
        }
    private val PinpointBottomSheetState.LocationInvalid.description: CharSequence
        get() {
            return if (type == PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE) {
                getString(R.string.out_of_indonesia_desc_new)
            } else if (uiModel.hasPinpoint() || uiState.isPinpointOnly()) {
                getString(R.string.undetected_location_desc_edit_w_pinpoint)
            } else {
                getString(R.string.undetected_location_desc_edit_wo_pinpoint)
            }
        }

    private val PinpointBottomSheetState.LocationInvalid.image: String
        get() {
            return when (this.type) {
                PinpointBottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE -> TokopediaImageUrl.IMAGE_OUTSIDE_INDONESIA
                PinpointBottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND -> TokopediaImageUrl.LOCATION_NOT_FOUND
            }
        }

    private fun UnifyButton.setInvalidButtonData(data: PinpointBottomSheetState.LocationInvalid) {
        if (data.uiState.isEdit() && !data.uiModel.hasPinpoint()) {
            buttonVariant = UnifyButton.Variant.GHOST
            buttonType = UnifyButton.Type.MAIN
            text = getString(R.string.mismatch_btn_title)
        }
    }

    private fun showBottomSheetLocUndefined(isDontAskAgain: Boolean) {
        bottomSheetLocUndefined = BottomSheetUnify()
        val viewBinding =
            BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetLocUndefined(viewBinding, isDontAskAgain)

        bottomSheetLocUndefined?.apply {
            setCloseClickListener {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickXOnBlockGpsPinpoint(userSession.userId)
                }
                dismiss()
            }
            setChild(viewBinding.root)
            setOnDismissListener {
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetLocUndefined?.show(it, "")
        }
    }

    private fun setupBottomSheetLocUndefined(
        viewBinding: BottomsheetLocationUndefinedBinding,
        isDontAskAgain: Boolean
    ) {
        viewBinding.run {
            imgLocUndefined.setImageUrl(LOCATION_NOT_FOUND)
            tvLocUndefined.text = getString(R.string.txt_location_not_detected)
            tvInfoLocUndefined.text = getString(R.string.txt_info_location_not_detected)
            btnActivateLocation.setOnClickListener {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickAktifkanLayananLokasiPinpoint(userSession.userId)
                }
                if (!isDontAskAgain) {
                    goToSettingLocationDevice()
                } else {
                    goToSettingLocationApps()
                }
            }
        }
    }

    private fun showBottomSheetInfo() {
        bottomSheetInfo = BottomSheetUnify()
        val viewBinding =
            BottomsheetLocationUnmatchedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetInfo(viewBinding)

        bottomSheetInfo?.apply {
            setCloseClickListener {
                binding?.bottomsheetLocation?.root?.show()
                dismiss()
            }
            setChild(viewBinding.root)
            setOnDismissListener {
                binding?.bottomsheetLocation?.root?.show()
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetInfo?.show(it, "")
        }
    }

    private fun setupBottomSheetInfo(viewBinding: BottomsheetLocationUnmatchedBinding) {
        viewBinding.run {
            btnClose.setOnClickListener {
                binding?.bottomsheetLocation?.root?.show()
                bottomSheetInfo?.dismiss()
            }
        }
    }

    // region get result

    private fun handlePinpointLite(data: Intent) {
        data.run {
            getParcelableExtra<SaveAddressDataModel>(KEY_ADDRESS_DATA)?.let { addressData ->
                viewModel.setAddress(
                    addressData
                )
            }
            viewModel.validatePinpoint()
        }
    }

    private fun onResultFromAddressForm(data: Intent?) {
        val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
        finishActivity(newAddress)
    }

    private fun onResultFromSearchPage(data: Intent?) {
        val currentLat = data?.getDoubleExtra(EXTRA_LAT, 0.0).orZero()
        val currentLong = data?.getDoubleExtra(EXTRA_LONG, 0.0).orZero()
        viewModel.onResultFromSearchAddress(
            searchAddressData = data?.getParcelableExtra(
                EXTRA_AUTOCOMPLETE
            ),
            lat = currentLat,
            long = currentLong
        )
    }

    fun onNewIntent(bundle: Bundle) {
        val currentLat = bundle.getDouble(EXTRA_LAT, 0.0).orZero()
        val currentLong = bundle.getDouble(EXTRA_LONG, 0.0).orZero()
        viewModel.onResultFromSearchAddress(
            searchAddressData = bundle.getParcelable(
                EXTRA_AUTOCOMPLETE
            ),
            lat = currentLat,
            long = currentLong
        )
    }

    // region navigation
    private fun goToLitePinpoint(context: Context) {
        val intent = getLitePinpointIntent(context)
        pinpointLiteContract.launch(intent)
    }

    private fun getLitePinpointIntent(context: Context): Intent {
        if (addressUiState.isEditOrPinpointOnly()) {
            if (viewModel.uiModel.hasPinpoint()) {
                return PinpointWebviewActivity.getIntent(
                    context = context,
                    saveAddressDataModel = viewModel.getAddress(),
                    lat = viewModel.uiModel.lat,
                    lng = viewModel.uiModel.long,
                    source = PinpointSource.EDIT_ADDRESS
                )
            } else {
                return PinpointWebviewActivity.getIntent(
                    context = context,
                    saveAddressDataModel = viewModel.getAddress(),
                    districtId = viewModel.uiModel.districtId,
                    source = PinpointSource.EDIT_ADDRESS
                )
            }
        } else {
            val source =
                if (viewModel.isPositiveFlow) PinpointSource.ADD_ADDRESS_POSITIVE else PinpointSource.ADD_ADDRESS_NEGATIVE
            return PinpointWebviewActivity.getIntent(
                context = context,
                saveAddressDataModel = viewModel.getAddress(),
                districtId = viewModel.uiModel.districtId,
                lat = viewModel.uiModel.lat,
                lng = viewModel.uiModel.long,
                source = source
            )
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_ADDRESS_NEW, data)
                    putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
                }
            )
            finish()
        }
    }

    private fun setPinpointResult(choosePinpoint: ChoosePinpoint.SetPinpointResult) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_PINPOINT_MODEL, choosePinpoint.pinpointUiModel)
                    putExtra(EXTRA_SAVE_DATA_UI_MODEL, choosePinpoint.saveAddressDataModel)
                }
            )
            finish()
        }
    }

    private fun goToSearchPage() {
        context?.let {
            searchPageContract.launch(
                Intent(it, SearchPageActivity::class.java).apply {
                    putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
                    putExtra(EXTRA_ADDRESS_STATE, addressUiState.name)
                }
            )
        }
    }

    private fun goToAddressForm(choosePinpoint: ChoosePinpoint.GoToAddressForm) {
        val intent = RouteManager.getIntent(
            context,
            "${ApplinkConstInternalLogistic.EDIT_ADDRESS_REVAMP}${viewModel.addressId}"
        )
        intent.apply {
            if (choosePinpoint.saveChanges) {
                putExtra(EXTRA_PINPOINT_MODEL, viewModel.uiModel)
            }
            putExtra(EXTRA_IS_POSITIVE_FLOW, choosePinpoint.isPositiveFlow)
            putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
            choosePinpoint.addressState?.let { state ->
                putExtra(EXTRA_ADDRESS_STATE, state.name)
            }
            putExtra(PARAM_SOURCE, choosePinpoint.source)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        addressFormContract.launch(intent)
    }
    // end region
}
