package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search

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
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.OnFailureListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isAdd
import com.tokopedia.logisticCommon.uimodel.isEditOrPinpointOnly
import com.tokopedia.logisticCommon.uimodel.toAddressUiState
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_STATE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_PINPOINT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POLYGON
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_REF
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentSearchAddressBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.analytics.LogisticAddAddressAnalytics
import com.tokopedia.logisticaddaddress.features.analytics.LogisticEditAddressAnalytics
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.LOCATION_NOT_FOUND
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DENIED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DONT_ASK_AGAIN
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_GRANTED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_NOT_DEFINED
import com.tokopedia.logisticaddaddress.utils.AddNewAddressUtils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

class SearchPageFragment : BaseDaggerFragment(), AutoCompleteListAdapter.AutoCompleteItemListener {
    companion object {
        private const val RESULT_PERMISSION_CODE = 1234
        private const val REQUEST_CODE_PERMISSION = 9876

        private const val LOCATION_REQUEST_INTERVAL = 10000L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L
        private const val GPS_DELAY = 1000L

        private const val DEFAULT_LONG = 0.0
        private const val DEFAULT_LAT = 0.0

        fun newInstance(bundle: Bundle): SearchPageFragment {
            return SearchPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, bundle.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, bundle.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putBoolean(EXTRA_FROM_PINPOINT, bundle.getBoolean(EXTRA_FROM_PINPOINT))
                    putBoolean(EXTRA_IS_POLYGON, bundle.getBoolean(EXTRA_IS_POLYGON))
                    putString(EXTRA_ADDRESS_STATE, bundle.getString(EXTRA_ADDRESS_STATE, AddressUiState.AddAddress.name))
                    putString(PARAM_SOURCE, bundle.getString(PARAM_SOURCE, ""))
                    putString(EXTRA_REF, bundle.getString(EXTRA_REF, ""))
                    putBoolean(
                        EXTRA_IS_GET_PINPOINT_ONLY,
                        bundle.getBoolean(EXTRA_IS_GET_PINPOINT_ONLY)
                    )
                }
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchPageViewModel::class.java)
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false
    private var permissionState: Int = PERMISSION_NOT_DEFINED
    private var isAccessAppPermissionFromSettings: Boolean = false
    private val requiredPermissions: Array<String>
        get() = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private var saveAddressDataModel = SaveAddressDataModel()
    var currentLat: Double = DEFAULT_LAT
    var currentLong: Double = DEFAULT_LONG

    private var isGmsAvailable: Boolean = true
    private var isPositiveFlow: Boolean = true
    private var isFromPinpoint: Boolean = false
    private var isPolygon: Boolean = false
    private var source: String = ""

    private var addressUiState: AddressUiState = AddressUiState.AddAddress

    private var binding by autoClearedNullable<FragmentSearchAddressBinding>()
    private var autoCompleteAdapter: AutoCompleteListAdapter? = null
    private var bottomSheetLocUndefined: BottomSheetUnify? = null

    private val gpsResultResolutionContract = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onResultFromGpsRequest()
        }
    }

    private val pinpointPageContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onResultFromPinpoint(it.data)
        } else {
            showInitialLoadMessage()
        }
    }

    private val addressFormContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onResultFromAddressForm(it.data)
        } else {
            showInitialLoadMessage()
            if (!isGmsAvailable) {
                activity?.finish()
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchAddressBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataFromArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkGms()
        initView()
        showInitialLoadMessage()
        setSearchView()
        setViewListener()
        initObserver()
        setOnBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        outState.putBoolean(EXTRA_FROM_PINPOINT, isFromPinpoint)
        outState.putBoolean(EXTRA_IS_POLYGON, isPolygon)
        outState.putString(EXTRA_ADDRESS_STATE, addressUiState.name)
        outState.putString(PARAM_SOURCE, source)
        outState.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, saveAddressDataModel)
        outState.putDouble(EXTRA_LAT, currentLat)
        outState.putDouble(EXTRA_LONG, currentLong)
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
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickAllowLocationSearch(userSession.userId)
                }
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    showBottomSheetLocUndefined(false)
                }
            }

            PERMISSION_DENIED -> {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickDontAllowLocationSearch(userSession.userId)
                }
            }

            PERMISSION_DONT_ASK_AGAIN -> {
                showBottomSheetLocUndefined(true)
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickDontAllowLocationSearch(userSession.userId)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdate()
    }

    override fun onItemClicked(placeId: String) {
        when (addressUiState) {
            AddressUiState.AddAddress -> LogisticAddAddressAnalytics.onClickDropdownSuggestion(userSession.userId)
            AddressUiState.EditAddress -> LogisticEditAddressAnalytics.onClickDropdownSuggestionAlamat(userSession.userId)
            else -> {
                // no op
            }
        }

        isPolygon = false
        if (!isPositiveFlow && isFromPinpoint) {
            goToPinpointPage(
                placeId,
                null,
                null,
                isFromAddressForm = true,
                isPositiveFlow = false
            )
        } else {
            goToPinpointPage(placeId, null, null, isFromAddressForm = false, isPositiveFlow = true)
        }
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    hitAnalyticOnBackPress()
                    activity?.finish()
                }
            }
        )
    }

    private fun hitAnalyticOnBackPress() {
        when (addressUiState) {
            AddressUiState.AddAddress -> LogisticAddAddressAnalytics.onClickBackArrowSearch(userSession.userId)
            AddressUiState.EditAddress -> LogisticEditAddressAnalytics.onClickBackArrowSearch(userSession.userId)
            else -> {
                // no op
            }
        }
    }

    private fun initDataFromArguments() {
        arguments?.apply {
            isPositiveFlow = getBoolean(EXTRA_IS_POSITIVE_FLOW)
            isFromPinpoint = getBoolean(EXTRA_FROM_PINPOINT)
            isPolygon = getBoolean(EXTRA_IS_POLYGON)
            addressUiState = getString(EXTRA_ADDRESS_STATE).toAddressUiState()
            currentLat = getDouble(EXTRA_LAT, DEFAULT_LAT)
            currentLong = getDouble(EXTRA_LONG, DEFAULT_LONG)
            source = getString(PARAM_SOURCE, "")
            getParcelable<SaveAddressDataModel>(EXTRA_SAVE_DATA_UI_MODEL)?.apply {
                saveAddressDataModel = this
            }
            getString(EXTRA_REF)?.takeIf { addressUiState.isAdd() }?.let { from ->
                LogisticAddAddressAnalytics.sendScreenName(from)
            }
        }
    }

    private fun checkGms() {
        context?.let {
            val gmsAvailable = MapsAvailabilityHelper.isMapsAvailable(it)
            isGmsAvailable = gmsAvailable
            if (!gmsAvailable) {
                goToAddAddressForm()
            }
        }
    }

    private fun onResultFromPinpoint(data: Intent?) {
        val isFromAddressForm = data?.getBooleanExtra(EXTRA_FROM_ADDRESS_FORM, false)
        val newAddress =
            data?.getParcelableExtra<SaveAddressDataModel>(LogisticConstant.EXTRA_ADDRESS_NEW)
                ?: data?.getParcelableExtra(EXTRA_SAVE_DATA_UI_MODEL)
        if (isFromAddressForm != null && newAddress != null) {
            finishActivity(newAddress, isFromAddressForm)
        }
    }

    private fun onResultFromAddressForm(data: Intent?) {
        val newAddress =
            data?.getParcelableExtra<SaveAddressDataModel>(LogisticConstant.EXTRA_ADDRESS_NEW)
        newAddress?.let { finishActivity(it, false) }
    }

    private fun onResultFromGpsRequest() {
        bottomSheetLocUndefined?.dismiss()
        if (allPermissionsGranted()) {
            binding?.loaderCurrentLocation?.visibility = View.VISIBLE
            Handler().postDelayed({ getLocation() }, GPS_DELAY)
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?, isFromAddressForm: Boolean) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(LogisticConstant.EXTRA_ADDRESS_NEW, data)
                    putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
                }
            )
            finish()
        }
    }

    private fun stopLocationUpdate() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    private fun initView() {
        autoCompleteAdapter = AutoCompleteListAdapter(this)
        binding?.let {
            it.rvAddressList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.rvAddressList.adapter = autoCompleteAdapter
            if (addressUiState.isEditOrPinpointOnly()) {
                it.tvMessageSearch.visibility = View.GONE
                it.tvSearchCurrentLocation.text =
                    getString(R.string.tv_discom_current_location_text)
            }
            it.headerSearchAddress.run {
                setNavigationOnClickListener {
                    hitAnalyticOnBackPress()
                    activity?.finish()
                }
            }
        }
    }

    private fun showInitialLoadMessage() {
        binding?.searchPageInput?.searchBarPlaceholder = getString(R.string.txt_hint_search)
        binding?.searchPageInput?.searchBarTextField?.setText("")
        binding?.tvMessageSearch?.text = getString(R.string.txt_message_initial_load)
        binding?.tvMessageSearch?.setOnClickListener {
            LogisticAddAddressAnalytics.onClickIsiAlamatManualSearch(userSession.userId)
            goToAddAddressForm()
        }
    }

    private fun goToAddAddressForm() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.EDIT_ADDRESS_REVAMP)
        intent.apply {
            putExtra(EXTRA_IS_POSITIVE_FLOW, false)
            putExtra(EXTRA_SAVE_DATA_UI_MODEL, saveAddressDataModel)
            putExtra(PARAM_SOURCE, source)
            putExtra(EXTRA_ADDRESS_STATE, addressUiState.name)
            putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
        }
        addressFormContract.launch(intent)
    }

    private fun setSearchView() {
        binding?.searchPageInput?.searchBarTextField?.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    when (addressUiState) {
                        AddressUiState.AddAddress -> LogisticAddAddressAnalytics.onClickFieldCariLokasi(userSession.userId)
                        AddressUiState.EditAddress -> LogisticEditAddressAnalytics.onClickFieldCariLokasi(userSession.userId)
                        else -> {
                            // no op
                        }
                    }
                }
            }

            setOnClickListener {
                when (addressUiState) {
                    AddressUiState.AddAddress -> LogisticAddAddressAnalytics.onClickFieldCariLokasi(userSession.userId)
                    AddressUiState.EditAddress -> LogisticEditAddressAnalytics.onClickFieldCariLokasi(userSession.userId)
                    else -> {
                        // no op
                    }
                }
            }

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // no-op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding?.tvMessageSearch?.text = getString(R.string.txt_message_ana_negative)
                    if (TextUtils.isEmpty(binding?.searchPageInput?.searchBarTextField?.text.toString())) {
                        hideListLocation()
                    } else {
                        viewModel.getAutoCompleteList(
                            keyword = binding?.searchPageInput?.searchBarTextField?.text.toString(),
                            latlng = if (currentLat != DEFAULT_LAT && currentLong != DEFAULT_LONG) {
                                "$currentLat,$currentLong"
                            } else {
                                ""
                            }
                        )
                    }
                }
            })
        }
    }

    private fun setViewListener() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
        binding?.tvSearchCurrentLocation?.setOnClickListener {
            doGetCurrentLocation()
        }
        binding?.ivSearchCurrentLocation?.setOnClickListener { doGetCurrentLocation() }
    }

    private fun doGetCurrentLocation() {
        when (addressUiState) {
            AddressUiState.AddAddress -> LogisticAddAddressAnalytics.onClickGunakanLokasiSaatIniSearch(userSession.userId)
            AddressUiState.EditAddress -> LogisticEditAddressAnalytics.onClickGunakanLokasiSaatIniSearch(userSession.userId)
            else -> {
                // no op
            }
        }

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

    private fun showBottomSheetLocUndefined(isDontAskAgain: Boolean) {
        bottomSheetLocUndefined = BottomSheetUnify()
        val viewBinding =
            BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetLocUndefined(viewBinding, isDontAskAgain)

        bottomSheetLocUndefined?.apply {
            setCloseClickListener {
                if (addressUiState.isAdd()) {
                    LogisticAddAddressAnalytics.onClickXOnBlockGpsSearch(userSession.userId)
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
                    LogisticAddAddressAnalytics.onClickAktifkanLayananLokasiSearch(userSession.userId)
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
                                        if (e is ResolvableApiException) {
                                            val intentSenderRequest =
                                                IntentSenderRequest.Builder(e.resolution.intentSender)
                                                    .build()
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

    private fun initObserver() {
        viewModel.autoCompleteList.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    loadListLocation(it.data)
                }

                is Fail -> {
                    Timber.d(it.throwable)
                    hideListLocation()
                    binding?.layoutEmptyState?.visibility = View.VISIBLE
                    binding?.ivEmptyState?.setImageUrl(LOCATION_NOT_FOUND)
                }
            }
        }
    }

    private fun loadListLocation(suggestedPlace: Place) {
        if (suggestedPlace.data.isNotEmpty()) {
            binding?.rvAddressList?.visibility = View.VISIBLE
            binding?.layoutEmptyState?.visibility = View.GONE
            autoCompleteAdapter?.setData(suggestedPlace.data)
        }
    }

    private fun hideListLocation() {
        binding?.rvAddressList?.visibility = View.GONE
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
        binding?.loaderCurrentLocation?.visibility = View.VISIBLE
        fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
            if (data != null) {
                binding?.loaderCurrentLocation?.visibility = View.GONE
                currentLat = data.latitude
                currentLong = data.longitude
                goToPinpointPage(
                    null,
                    data.latitude,
                    data.longitude,
                    isFromAddressForm = false,
                    isPositiveFlow = true
                )
            } else {
                fusedLocationClient?.requestLocationUpdates(
                    AddNewAddressUtils.getLocationRequest(),
                    locationCallback,
                    null
                )
            }
        }
    }

    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!hasRequestedLocation) {
                    // send to maps
                    hasRequestedLocation = true
                }
                stopLocationUpdate()
                binding?.loaderCurrentLocation?.visibility = View.GONE
                currentLat = locationResult.lastLocation.latitude
                currentLong = locationResult.lastLocation.longitude
                goToPinpointPage(
                    null,
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude,
                    isFromAddressForm = false,
                    isPositiveFlow = true
                )
            }
        }

    private fun goToPinpointPage(
        placeId: String?,
        latitude: Double?,
        longitude: Double?,
        isFromAddressForm: Boolean,
        isPositiveFlow: Boolean
    ) {
        val bundle = Bundle()
        bundle.putString(EXTRA_PLACE_ID, placeId)
        latitude?.let { bundle.putDouble(EXTRA_LAT, it) }
        longitude?.let { bundle.putDouble(EXTRA_LONG, it) }
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
        bundle.putBoolean(EXTRA_IS_POLYGON, isPolygon)
        bundle.putString(PARAM_SOURCE, source)
        bundle.putString(EXTRA_ADDRESS_STATE, addressUiState.name)
        bundle.putBoolean(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
        if (addressUiState.isAdd()) {
            pinpointPageContract.launch(
                context?.let {
                    PinpointNewPageActivity.createIntent(
                        it,
                        bundle
                    )
                }
            )
        } else {
            activity?.run {
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply {
                        putExtra(EXTRA_PLACE_ID, placeId)
                        latitude?.let { putExtra(EXTRA_LAT, it) }
                        longitude?.let { putExtra(EXTRA_LONG, it) }
                        putExtra(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
                        putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
                        putExtra(EXTRA_IS_POLYGON, isPolygon)
                        putExtra(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
                    }
                )
                finish()
            }
        }
    }
}
