package com.tokopedia.logisticaddaddress.features.pinpoint

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_CITY_NAME
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_DISTRICT_NAME
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_WH_DISTRICT_ID
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_ADDRESS_NEW
import com.tokopedia.logisticCommon.data.constant.PinpointSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.response.Data
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
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT_WAREHOUSE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POLYGON
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_NEGATIVE_FULL_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_PINPOINT_MODEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_ADDRESS_DATA
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentPinpointNewBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper.map
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.analytics.LogisticAddAddressAnalytics
import com.tokopedia.logisticaddaddress.features.analytics.LogisticEditAddressAnalytics
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointViewModel
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.BottomSheetState
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.MapsGeocodeState
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointAction
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel.PinpointUiModel
import com.tokopedia.logisticaddaddress.features.pinpoint.webview.PinpointWebviewActivity
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.IMAGE_OUTSIDE_INDONESIA
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
import com.tokopedia.usecase.coroutines.Fail
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

        const val FOREIGN_COUNTRY_MESSAGE = "Lokasi di luar Indonesia."
        const val LOCATION_NOT_FOUND_MESSAGE = "Lokasi gagal ditemukan"
        const val BOTTOMSHEET_OUT_OF_INDO = 1
        const val BOTTOMSHEET_NOT_FOUND_LOC = 2

        const val SUCCESS = "success"
        const val NOT_SUCCESS = "not success"

        fun newInstance(extra: Bundle): PinpointFragment {
            val state = if (extra.getBoolean(EXTRA_IS_GET_PINPOINT_ONLY, false)) {
                AddressUiState.PinpointOnly.name
            } else {
                extra.getString(EXTRA_ADDRESS_STATE, AddressUiState.PinpointOnly.name)
            }

            return PinpointFragment().apply {
                arguments = Bundle().apply {
                    // general
                    putDouble(EXTRA_LAT, extra.getDouble(EXTRA_LAT))
                    putDouble(EXTRA_LONG, extra.getDouble(EXTRA_LONG))
                    putString(PARAM_SOURCE, extra.getString(PARAM_SOURCE))
                    putString(EXTRA_ADDRESS_STATE, state)

                    // from address form
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, extra.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putString(EXTRA_ADDRESS_ID, extra.getString(EXTRA_ADDRESS_ID))
                    putLong(EXTRA_DISTRICT_ID, extra.getLong(EXTRA_DISTRICT_ID))

                    // from search page
                    putString(EXTRA_PLACE_ID, extra.getString(EXTRA_PLACE_ID))

                    // search page
                    putBoolean(EXTRA_IS_POLYGON, extra.getBoolean(EXTRA_IS_POLYGON))

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

                    // todo can be removed
                    putBoolean(EXTRA_FROM_ADDRESS_FORM, extra.getBoolean(EXTRA_FROM_ADDRESS_FORM))
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
    private var isEditWarehouse: Boolean = false
    private var whDistrictId: Long = 0

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
            }
        }

    // google map
    private var composite = CompositeSubscription()
    private var googleMap: GoogleMap? = null
    private var currentPlaceId: String? = ""

    // to differentiate positive flow or negative flow
    private var isPositiveFlow: Boolean = true
    private var showIllustrationMap: Boolean = false
    private var isFromAddressForm: Boolean = false
    private var isPolygon: Boolean = false

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
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
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
        checkMapsAvailability(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_PLACE_ID, currentPlaceId)

        viewModel.uiModel.let {
            if (it.lat != 0.0 && it.long != 0.0) {
                outState.putDouble(EXTRA_LAT, it.lat)
                outState.putDouble(EXTRA_LONG, it.long)
            }

            if (it.districtName.isNotBlank() && it.cityName.isNotBlank()) {
                outState.putString(EXTRA_DISTRICT_NAME, it.districtName)
                outState.putString(EXTRA_CITY_NAME, it.cityName)
            }

            outState.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, it)
        }

        outState.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        outState.putBoolean(EXTRA_IS_POLYGON, isPolygon)
        outState.putBoolean(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
        outState.putBoolean(EXTRA_IS_EDIT_WAREHOUSE, isEditWarehouse)
        outState.putLong(EXTRA_WH_DISTRICT_ID, whDistrictId)
        outState.putString(EXTRA_ADDRESS_STATE, addressUiState.name)
        outState.putString(PARAM_SOURCE, source)

        super.onSaveInstanceState(outState)
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

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        activity?.let { MapsInitializer.initialize(activity) }

        moveMap(
            getLatLng(viewModel.uiModel.lat, viewModel.uiModel.long)
        )

        if (isPolygon) {
            viewModel.getDistrictBoundaries()
        }

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

    private fun checkMapsAvailability(savedInstanceState: Bundle?) {
        if (isGmsAvailable) {
            prepareMap(savedInstanceState)
            setFusedLocationClient()
            fetchData()
            initView()
            setViewListener()
            initObserver()
        } else {
            context?.let { ctx -> goToLitePinpoint(ctx) }
        }
    }

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
                if (isPositiveFlow) PinpointSource.ADD_ADDRESS_POSITIVE else PinpointSource.ADD_ADDRESS_NEGATIVE
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

    private fun initView() {
        if (addressUiState.isEdit()) {
            binding?.bottomsheetLocation?.let {
                it.btnPrimary.text = getString(R.string.choose_this_location)
                it.btnSecondary.visibility = View.GONE
            }
        }
    }

    private fun onResultFromAddressForm(data: Intent?) {
        val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
        finishActivity(newAddress, false)
    }

    private fun onResultFromSearchPage(data: Intent?) {
        if (addressUiState.isEditOrPinpointOnly()) {
            currentPlaceId = data?.getStringExtra(EXTRA_PLACE_ID)
            val currentLat = data?.getDoubleExtra(EXTRA_LAT, 0.0).orZero()
            val currentLong = data?.getDoubleExtra(EXTRA_LONG, 0.0).orZero()
            if (!currentPlaceId.isNullOrEmpty()) {
                currentPlaceId?.let { viewModel.getDistrictLocation(it) }
            } else if (currentLong != 0.0 && currentLat != 0.0) {
                moveMap(getLatLng(currentLat, currentLong))
                viewModel.getDistrictData(currentLat, currentLong)
            }
//            else {
//                goToAddressForm(true)
//            }
        } else {
            // todo i think this is not reachable ? because extra_address_new source is from address form
            val newAddress =
                data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_NEW)
            val isFromAddressForm = data?.getBooleanExtra(EXTRA_FROM_ADDRESS_FORM, false)
            isFromAddressForm?.let { finishActivity(newAddress, it) }
        }
    }

    private fun handlePinpointLite(data: Intent) {
        data.run {
            getParcelableExtra<SaveAddressDataModel>(KEY_ADDRESS_DATA)?.let { addressData ->
                viewModel.setAddress(
                    addressData
                )
            }
            // todo
            onChoosePinpoint()
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?, isFromAddressForm: Boolean) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_ADDRESS_NEW, data)
                    putExtra(EXTRA_NEGATIVE_FULL_FLOW, true)
                    putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
                    putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
                }
            )
            finish()
        }
    }

    private fun stopLocationUpdate() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    private fun initData() {
        arguments?.apply {
            addressUiState = getString(EXTRA_ADDRESS_STATE).toAddressUiState()
            viewModel.onViewCreated(
                districtName = getString(EXTRA_DISTRICT_NAME).orEmpty(),
                cityName = getString(EXTRA_CITY_NAME).orEmpty(),
                placeId = getString(EXTRA_PLACE_ID).orEmpty(),
                lat = getDouble(EXTRA_LAT),
                long = getDouble(EXTRA_LONG),
                districtId = getLong(EXTRA_DISTRICT_ID),
                whDistrictId = getLong(EXTRA_WH_DISTRICT_ID, 0),
                addressId = getString(EXTRA_ADDRESS_ID, ""),
                uiState = getString(EXTRA_ADDRESS_STATE).toAddressUiState()
            )

            isEditWarehouse = getBoolean(EXTRA_IS_EDIT_WAREHOUSE, false)
//            whDistrictId = getLong(EXTRA_WH_DISTRICT_ID, 0)
//            viewModel.addressId = getString(EXTRA_ADDRESS_ID, "")

            isPositiveFlow = getBoolean(EXTRA_IS_POSITIVE_FLOW, true)
            isPolygon = getBoolean(EXTRA_IS_POLYGON, false)
            isFromAddressForm = getBoolean(EXTRA_FROM_ADDRESS_FORM)
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
//        if (viewModel.uiModel.placeId.isNotEmpty()) {
//            viewModel.getDistrictLocation(viewModel.uiModel.placeId)
//        } else if (viewModel.uiModel.hasPinpoint()) {
//            viewModel.getLocationFromLatLong()
//        } else if (viewModel.uiModel.districtId != 0L) {
//            viewModel.getDistrictCenter()
//        } else if (viewModel.uiModel.hasDistrictAndCityName) {
//            viewModel.getGeocodeByDistrictAndCityName()
//        } else if (addressUiState.isPinpointOnly()) {
//            getCurrentLocation()
//        }
        viewModel.fetchData()
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

    private fun initObserver() {
        viewModel.autofillDistrictData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.mapsEmpty?.visibility = View.GONE
                    binding?.mapViews?.visibility = View.VISIBLE
                    if (it.data.messageError.isEmpty()) {
                        updateAfterOnSuccessAutofill(it.data.data)
                    } else {
                        val msg = it.data.messageError.getOrNull(0)
                        msg?.let { error ->
                            when {
                                error.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                                error.contains(LOCATION_NOT_FOUND_MESSAGE) -> showNotFoundLocation()
                            }
                        }
                    }
                }

                is Fail -> {
                    val msg = it.throwable.message.toString()
                    when {
                        msg.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                        else -> showNotFoundLocation()
                    }
                }

                else -> {
                    // no-op
                }
            }
        }

        viewModel.districtLocation.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    binding?.mapsEmpty?.visibility = View.GONE
                    binding?.mapViews?.visibility = View.VISIBLE
                    onSuccessPlaceGetDistrict(it.data)
                }

                is Fail -> {
                    val msg = it.throwable.message.toString()
                    when {
                        msg.contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()
                        else -> {
                            showIllustrationMap = true
                            showNotFoundLocation()
                        }
                    }
                }

                else -> {
                    // no-op
                }
            }
        }

        viewModel.districtCenter.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    moveMap(getLatLng(it.data.latitude, it.data.longitude))
                    viewModel.getDistrictData(it.data.latitude, it.data.longitude)
                }

                else -> {
                    // no-op
                }
            }
        }

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

        viewModel.mapsGeocodeState.observe(viewLifecycleOwner) {
            when (it) {
                is MapsGeocodeState.Success -> {
                    moveMap(getLatLng(it.location.lat, it.location.lng))
                    viewModel.getLocationFromLatLong()
                }

                is MapsGeocodeState.Fail -> {
                    when {
                        it.errorMessage.orEmpty()
                            .contains(FOREIGN_COUNTRY_MESSAGE) -> showOutOfReachBottomSheet()

                        else -> {
                            showIllustrationMap = true
                            showNotFoundLocation()
                        }
                    }
                }
            }
        }

        viewModel.action.observe(viewLifecycleOwner) {
            when (it) {
                PinpointAction.GetCurrentLocation -> {
                    getCurrentLocation()
                }

                PinpointAction.InvalidDistrictPinpoint -> {
                    if (addressUiState.isAdd()) {
                        LogisticAddAddressAnalytics.onViewToasterPinpointTidakSesuai(userSession.userId)
                    }
                    setupPrimaryButton(false)
                    view?.let { view ->
                        Toaster.build(
                            view,
                            getString(R.string.txt_toaster_pinpoint_unmatched),
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }

                is PinpointAction.MoveMap -> {
                    moveMap(LatLng(it.lat, it.long))
                }
            }
        }

        viewModel.pinpointBottomSheet.observe(viewLifecycleOwner) {
            when (it) {
                is BottomSheetState.LocationDetail -> {
                    showDistrictBottomSheet(it)
                }
                is BottomSheetState.LocationInvalid -> {
                    showInvalidBottomSheet(it)
                }
            }
        }
    }

    private fun setupPrimaryButton(isSuccess: Boolean) {
        // todo can this be observed through uiModel.hasPinpoint ?
        binding?.bottomsheetLocation?.btnPrimary?.setOnClickListener {
            if (addressUiState.isAdd() && !isPositiveFlow) {
                LogisticAddAddressAnalytics.onClickPilihLokasiNegative(
                    userSession.userId,
                    if (isSuccess) SUCCESS else NOT_SUCCESS
                )
                if (isSuccess) goToAddressForm(true)
            }
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

    private fun showDistrictBottomSheet(state: BottomSheetState.LocationDetail) {
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

                if (state.buttonPrimary.type != -1) {
                    buttonType = state.buttonPrimary.type
                }

                if (state.buttonPrimary.variant != -1) {
                    buttonVariant = state.buttonPrimary.variant
                }

                setOnClickListener {
                    when (addressUiState) {
                        AddressUiState.EditAddress -> {
                            LogisticEditAddressAnalytics.onClickPilihLokasiIni(userSession.userId)
                        }

                        AddressUiState.AddAddress -> {
                            if (isPositiveFlow) {
                                LogisticAddAddressAnalytics.onClickPilihLokasiPositive(userSession.userId)
                            } else {
                                LogisticAddAddressAnalytics.onClickPilihLokasiNegative(
                                    userSession.userId,
                                    if (state.buttonPrimary.state.success) SUCCESS else NOT_SUCCESS
                                )
                            }
                        }

                        else -> {
                            // no op
                        }
                    }

                    if (state.buttonPrimary.enable) {
                        onChoosePinpoint()
                    }
                }
            }

            btnSecondary.run {
                if (state.buttonSecondary.show) {
                    visible()
                } else {
                    gone()
                }

                if (state.buttonSecondary.text.isNotEmpty()) {
                    text = state.buttonSecondary.text
                }

                if (state.buttonSecondary.type != -1) {
                    buttonType = state.buttonSecondary.type
                }

                if (state.buttonSecondary.variant != -1) {
                    buttonVariant = state.buttonSecondary.variant
                }

                setOnClickListener {
                    LogisticAddAddressAnalytics.onClickIsiAlamatManual(userSession.userId)

                    if (state.buttonSecondary.enable) {
                        goToAddressForm(false)
                    }
                }
            }
        }
    }

    private fun showInvalidBottomSheet(data: BottomSheetState.LocationInvalid) {
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
                if (data.imageUrl.isNotEmpty()) {
                    imgInvalidLoc.setImageUrl(data.imageUrl)
                }
                if (data.title.isNotEmpty()) {
                    tvInvalidLoc.text = data.title
                }

                if (data.detail.isNotEmpty()) {
                    tvInvalidLocDetail.text = data.detail
                }
                btnAnaNegative.run {
                    if (data.buttonState.show) {
                        if (data.buttonState.text.isNotEmpty()) {
                            text = getString(R.string.mismatch_btn_title)
                        }

                        if (data.buttonState.variant != -1) {
                            buttonVariant = data.buttonState.variant
                        }

                        if (data.buttonState.type != -1) {
                            buttonType = data.buttonState.type
                        }
                        setOnClickListener {
                            if (addressUiState.isAdd()) {
                                when (data.type) {
                                    BottomSheetState.LocationInvalid.LocationInvalidType.OUT_OF_COVERAGE -> {
                                        LogisticAddAddressAnalytics.onClickIsiAlamatOutOfIndo(
                                            userSession.userId
                                        )
                                    }
                                    BottomSheetState.LocationInvalid.LocationInvalidType.LOCATION_NOT_FOUND -> {
                                        LogisticAddAddressAnalytics.onClickIsiAlamatManualUndetectedLocation(
                                            userSession.userId
                                        )
                                    }
                                }
                            }
                            if (data.buttonState.enable) {
                                goToAddressForm(false)
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

    private fun hitInvalidLocationImpressionAnalytic(type: Int) {
        if (type == BOTTOMSHEET_OUT_OF_INDO) {
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
        } else if (type == BOTTOMSHEET_NOT_FOUND_LOC) {
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

    private fun getAutofill() {
        val target: LatLng? = this.googleMap?.cameraPosition?.target
        val latTarget = target?.latitude ?: 0.0
        val longTarget = target?.longitude ?: 0.0

        viewModel.getDistrictData(latTarget, longTarget)
    }

    private fun onSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
        if ((data.postalCode.isEmpty() && viewModel.uiModel.postalCode.isEmpty()) || data.districtId == 0L) {
            viewModel.setLatLong(data.latitude.toDoubleOrZero(), data.longitude.toDoubleOrZero())
            moveMap(getLatLng(data.latitude, data.longitude))
            showNotFoundLocation()
        } else {
            doAfterSuccessPlaceGetDistrict(data)
        }
    }

    private fun doAfterSuccessPlaceGetDistrict(data: GetDistrictDataUiModel) {
        showDistrictBottomSheet()

        moveMap(getLatLng(data.latitude, data.longitude))

        viewModel.uiModel = viewModel.uiModel.map(data, null)

        with(data.errMessage) {
            if (this != null && this.contains(LOCATION_NOT_FOUND_MESSAGE)) {
                showNotFoundLocation()
            } else {
                updateGetDistrictBottomSheet(viewModel.uiModel)
            }
        }
    }

    private fun updateGetDistrictBottomSheet(data: PinpointUiModel) {
//        viewModel.setAddress(data)
        setDefaultResultGetDistrict(data)
    }

    private fun setDefaultResultGetDistrict(data: PinpointUiModel) {
        showDistrictBottomSheet()

        binding?.bottomsheetLocation?.run {
            tvAddress.text = data.title
            tvAddressDesc.text = data.formattedAddress
        }
    }

    private fun prepareMap(savedInstanceState: Bundle?) {
        binding?.mapViews?.onCreate(savedInstanceState)
        binding?.mapViews?.getMapAsync(this)
    }

    private fun setFusedLocationClient() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
    }

    private fun setViewListener() {
        binding?.run {
            bottomsheetLocation.btnInfo.setOnClickListener {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickIconQuestion(userSession.userId)
                }
                bottomsheetLocation.root.hide()
                showBottomSheetInfo()
            }

            bottomsheetLocation.btnPrimary.setOnClickListener {
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        LogisticEditAddressAnalytics.onClickPilihLokasiIni(userSession.userId)
                    }

                    AddressUiState.AddAddress -> {
                        if (isPositiveFlow) {
                            LogisticAddAddressAnalytics.onClickPilihLokasiPositive(userSession.userId)
                        } else {
                            LogisticAddAddressAnalytics.onClickPilihLokasiNegative(
                                userSession.userId,
                                SUCCESS
                            )
                        }
                    }

                    else -> {
                        // no op
                    }
                }
                isPositiveFlow = true
                onChoosePinpoint()
            }

            bottomsheetLocation.btnSecondary.setOnClickListener {
                LogisticAddAddressAnalytics.onClickIsiAlamatManual(userSession.userId)
                if (isPositiveFlow) {
                    isPositiveFlow = false
                    goToAddressForm(false)
                } else {
                    activity?.run {
                        setResult(
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(EXTRA_NEGATIVE_FULL_FLOW, true)
                                putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
                            }
                        )
                        finish()
                    }
                }
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

    private fun onChoosePinpoint() {
        if (addressUiState.isEditOrPinpointOnly()) {
            if (isEditWarehouse && whDistrictId != 0L && whDistrictId != viewModel.uiModel.districtId) {
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.toaster_not_avail_shop_loc),
                        Toaster.LENGTH_SHORT,
                        type = Toaster.TYPE_ERROR
                    ).show()
                }
            } else {
                setPinpointResult()
            }
        } else {
            goToAddressForm(true)
        }
    }

    private fun setPinpointResult() {
        val saveModel = viewModel.getAddress()
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_PINPOINT_MODEL, viewModel.uiModel)
                    putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveModel)
                    putExtra(EXTRA_NEGATIVE_FULL_FLOW, false)
                }
            )
            finish()
        }
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
    private fun getLocation() {
        showLoading()
        fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
            if (data != null) {
                moveMap(getLatLng(data.latitude, data.longitude))
                viewModel.getDistrictData(data.latitude, data.longitude)
            } else {
                fusedLocationClient?.requestLocationUpdates(
                    AddNewAddressUtils.getLocationRequest(),
                    locationCallback,
                    null
                )
            }
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

    private fun moveMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(ZOOM_LEVEL)
            .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun updateAfterOnSuccessAutofill(data: Data) {
        showDistrictBottomSheet()

        if (isPolygon) {
            if (data.districtId != viewModel.uiModel.districtId) {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onViewToasterPinpointTidakSesuai(userSession.userId)
                }
                binding?.bottomsheetLocation?.btnPrimary?.setOnClickListener {
                    if (addressUiState.isAdd()) {
                        LogisticAddAddressAnalytics.onClickPilihLokasiNegative(
                            userSession.userId,
                            NOT_SUCCESS
                        )
                    }
                }
                view?.let { view ->
                    Toaster.build(
                        view,
                        getString(R.string.txt_toaster_pinpoint_unmatched),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            } else {
                binding?.bottomsheetLocation?.btnPrimary?.setOnClickListener {
                    if (addressUiState.isAdd()) {
                        LogisticAddAddressAnalytics.onClickPilihLokasiNegative(
                            userSession.userId,
                            SUCCESS
                        )
                    }
                    onChoosePinpoint()
                }
                viewModel.uiModel = viewModel.uiModel.map(data, null)
                updateGetDistrictBottomSheet(viewModel.uiModel)
            }
        } else {
            viewModel.uiModel = viewModel.uiModel.map(data, null)
            updateGetDistrictBottomSheet(viewModel.uiModel)
        }
    }

    private fun showOutOfReachBottomSheet() {
        showInvalidBottomSheet()
        updateInvalidBottomSheetData(BOTTOMSHEET_OUT_OF_INDO)
    }

    private fun showNotFoundLocation() {
        showInvalidBottomSheet()
        updateInvalidBottomSheetData(BOTTOMSHEET_NOT_FOUND_LOC)
    }

    private fun updateInvalidBottomSheetData(type: Int) {
        if (type == BOTTOMSHEET_OUT_OF_INDO) {
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
            binding?.bottomsheetLocation?.run {
                imgInvalidLoc.setImageUrl(IMAGE_OUTSIDE_INDONESIA)
                tvInvalidLoc.text = getString(R.string.out_of_indonesia_title)
                tvInvalidLocDetail.text = getString(R.string.out_of_indonesia_desc_new)
                if (addressUiState.isEditOrPinpointOnly()) {
                    btnAnaNegative.visibility = View.GONE
                }
            }
        } else {
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
            if (showIllustrationMap) {
                binding?.mapsEmpty?.visibility = View.VISIBLE
                binding?.mapViews?.visibility = View.GONE
                binding?.imgMapsEmpty?.setImageUrl(MAPS_EMPTY)
                showIllustrationMap = false
            }
            binding?.bottomsheetLocation?.run {
                imgInvalidLoc.setImageUrl(LOCATION_NOT_FOUND)
                if (addressUiState.isEditOrPinpointOnly()) {
                    tvInvalidLoc.text = getString(R.string.undetected_location_new_edit)
                    if (viewModel.uiModel.hasPinpoint() || addressUiState.isPinpointOnly()) {
                        tvInvalidLocDetail.text =
                            getString(R.string.undetected_location_desc_edit_w_pinpoint)
                        btnAnaNegative.visibility = View.GONE
                    } else {
                        tvInvalidLocDetail.text =
                            getString(R.string.undetected_location_desc_edit_wo_pinpoint)
                        btnAnaNegative.let {
                            it.buttonVariant = UnifyButton.Variant.GHOST
                            it.buttonType = UnifyButton.Type.MAIN
                            it.text = getString(R.string.mismatch_btn_title)
                            it.setOnClickListener {
                                goToAddressForm(false)
                            }
                        }
                    }
                } else {
                    tvInvalidLoc.text = getString(R.string.undetected_location_new)
                    tvInvalidLocDetail.text = getString(R.string.undetected_location_desc_new)
                }
            }
        }

        binding?.bottomsheetLocation?.btnAnaNegative?.setOnClickListener {
            if (addressUiState.isAdd()) {
                if (type == BOTTOMSHEET_OUT_OF_INDO) {
                    LogisticAddAddressAnalytics.onClickIsiAlamatOutOfIndo(userSession.userId)
                } else {
                    LogisticAddAddressAnalytics.onClickIsiAlamatManualUndetectedLocation(
                        userSession.userId
                    )
                }
            }
            isPositiveFlow = false
            goToAddressForm(false)
        }
    }

    private fun goToSearchPage() {
        if (addressUiState.isEditOrPinpointOnly() || !isPositiveFlow) {
            context?.let {
                searchPageContract.launch(
                    Intent(it, SearchPageActivity::class.java).apply {
                        putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
                        putExtra(EXTRA_ADDRESS_STATE, addressUiState.name)
                    }
                )
            }
        } else {
            activity?.finish()
        }
    }

    private fun goToAddressForm(saveChanges: Boolean) {
        val intent = RouteManager.getIntent(
            context,
            "${ApplinkConstInternalLogistic.EDIT_ADDRESS_REVAMP}${viewModel.addressId}"
        )
        intent.apply {
            if (saveChanges) {
                putExtra(EXTRA_PINPOINT_MODEL, viewModel.uiModel)
            }
            putExtra(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
            putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
            putExtra(EXTRA_ADDRESS_STATE, addressUiState.name)
            putExtra(PARAM_SOURCE, source)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP)
        }
        addressFormContract.launch(intent)
    }
}
