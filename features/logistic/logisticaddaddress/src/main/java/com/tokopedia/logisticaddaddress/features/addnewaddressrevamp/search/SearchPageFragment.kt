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
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_DISTRICT_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_PINPOINT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POLYGON
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_KOTA_KECAMATAN
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.GPS_REQUEST
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUndefinedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentSearchAddressBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.EXTRA_PLACE_ID
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.LOCATION_NOT_FOUND
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DENIED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DONT_ASK_AGAIN
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_GRANTED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_NOT_DEFINED
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class SearchPageFragment: BaseDaggerFragment(), AutoCompleteListAdapter.AutoCompleteItemListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SearchPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchPageViewModel::class.java)
    }

    private lateinit var autoCompleteAdapter: AutoCompleteListAdapter

    private var bottomSheetLocUndefined: BottomSheetUnify? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var hasRequestedLocation: Boolean = false
    private var isPositiveFlow: Boolean = true

    private var isFromPinpoint: Boolean = false

    private var isPermissionAccessed: Boolean = false

    private var saveDataModel: SaveAddressDataModel? = null
    private var currentKotaKecamatan: String? = ""
    private var currentLat: Double = DEFAULT_LAT
    private var currentLong: Double = DEFAULT_LONG
    private var permissionState: Int = PERMISSION_NOT_DEFINED
    private var isPolygon: Boolean = false
    private var distrcitId: Long? = null

    private var isEdit: Boolean = false
    private var source: String = ""
    private var isAccessAppPermissionFromSettings: Boolean = false


    private val requiredPermissions: Array<String>
        get() = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    private val compositeSubs: CompositeSubscription by lazy { CompositeSubscription() }
    private var binding by autoClearedNullable<FragmentSearchAddressBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchAddressBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isPositiveFlow = it.getBoolean(EXTRA_IS_POSITIVE_FLOW)
            isFromPinpoint = it.getBoolean(EXTRA_FROM_PINPOINT)
            currentKotaKecamatan = it.getString(EXTRA_KOTA_KECAMATAN)
            saveDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
            isPolygon = it.getBoolean(EXTRA_IS_POLYGON)
            distrcitId = it.getLong(EXTRA_DISTRICT_ID)
            isEdit = it.getBoolean(EXTRA_IS_EDIT, false)
            source = it.getString(PARAM_SOURCE, "")
        }
        if (saveDataModel != null) {
            saveDataModel?.let {
                viewModel.setAddress(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkGms()
        initView()
        showInitialLoadMessage()
        setSearchView()
        setViewListener()
        initObserver()
    }

    private fun checkGms() {
        context?.let {
            val gmsAvailable = MapsAvailabilityHelper.isMapsAvailable(it)
            viewModel.isGmsAvailable = gmsAvailable
            if (!gmsAvailable) {
                goToAddressForm()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_PINPOINT_PAGE) {
                val isFromAddressForm = data?.getBooleanExtra(EXTRA_FROM_ADDRESS_FORM, false)
                var newAddress = data?.getParcelableExtra<SaveAddressDataModel>(LogisticConstant.EXTRA_ADDRESS_NEW)
                if (newAddress == null) {
                    newAddress = data?.getParcelableExtra(EXTRA_SAVE_DATA_UI_MODEL)
                }
                if (isFromAddressForm != null && newAddress != null) {
                    finishActivity(newAddress, isFromAddressForm)
                }
            } else if (requestCode == REQUEST_ADDRESS_FORM_PAGE) {
                val newAddress = data?.getParcelableExtra<SaveAddressDataModel>(LogisticConstant.EXTRA_ADDRESS_NEW)
                newAddress?.let { finishActivity(it, false) }
            } else if (requestCode == GPS_REQUEST) {
                bottomSheetLocUndefined?.dismiss()
                if (allPermissionsGranted()) {
                    binding?.loaderCurrentLocation?.visibility = View.VISIBLE
                    Handler().postDelayed({ getLocation() }, GPS_DELAY)
                }
            }
        } else {
            showInitialLoadMessage()
            if (requestCode == REQUEST_ADDRESS_FORM_PAGE && !viewModel.isGmsAvailable) {
                activity?.finish()
            }
        }
    }

    private fun finishActivity(data: SaveAddressDataModel?, isFromAddressForm: Boolean) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(LogisticConstant.EXTRA_ADDRESS_NEW, data)
                putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
            })
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (activity != null && context != null) {
            permissionState = AddNewAddressUtils.getPermissionStateFromResult(requireActivity(), requireContext(), permissions)
        }
        when (permissionState) {
            PERMISSION_GRANTED -> {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickAllowLocationSearch(userSession.userId)
                }
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    showBottomSheetLocUndefined(false)
                }
            }
            PERMISSION_DENIED -> {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickDontAllowLocationSearch(userSession.userId)
                }
            }
            PERMISSION_DONT_ASK_AGAIN -> {
                showBottomSheetLocUndefined(true)
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickDontAllowLocationSearch(userSession.userId)
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
        compositeSubs.clear()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    private fun initView() {
        autoCompleteAdapter = AutoCompleteListAdapter(this)
        binding?.let {
            it.rvAddressList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.rvAddressList.adapter = autoCompleteAdapter
            if (isEdit) {
                it.tvMessageSearch.visibility = View.GONE
                it.tvSearchCurrentLocation.text = getString(R.string.tv_discom_current_location_text)
            }
        }
    }

    private fun showInitialLoadMessage() {
        binding?.searchPageInput?.searchBarPlaceholder = getString(R.string.txt_hint_search)
        binding?.searchPageInput?.searchBarTextField?.setText("")
        binding?.tvMessageSearch?.text = getString(R.string.txt_message_initial_load)
        binding?.tvMessageSearch?.setOnClickListener {
            AddNewAddressRevampAnalytics.onClickIsiAlamatManualSearch(userSession.userId)
            goToAddressForm()
        }
    }

    private fun goToAddressForm() {
        val intent = Intent(context, AddressFormActivity::class.java).apply {
            putExtra(EXTRA_IS_POSITIVE_FLOW, false)
            putExtra(EXTRA_SAVE_DATA_UI_MODEL, viewModel.getAddress())
            putExtra(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
            putExtra(PARAM_SOURCE, source)
            putExtra(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
        }
        startActivityForResult(intent, REQUEST_ADDRESS_FORM_PAGE)
    }

    private fun setSearchView() {
        binding?.searchPageInput?.searchBarTextField?.run {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldCariLokasi(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldCariLokasi(userSession.userId)
                    }
                }
            }

            setOnClickListener {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickFieldCariLokasi(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickFieldCariLokasi(userSession.userId)
                }
            }

            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //no-op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding?.tvMessageSearch?.text = getString(R.string.txt_message_ana_negative)
                    if (TextUtils.isEmpty(binding?.searchPageInput?.searchBarTextField?.text.toString())) {
                        hideListLocation()
                    } else {
                        loadAutoComplete(binding?.searchPageInput?.searchBarTextField?.text.toString())
                    }
                }

            })
        }
    }


    private fun setViewListener() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
        binding?.rlSearchCurrentLocation?.setOnClickListener {
            if (!isEdit) {
                AddNewAddressRevampAnalytics.onClickGunakanLokasiSaatIniSearch(userSession.userId)
            } else {
                EditAddressRevampAnalytics.onClickGunakanLokasiSaatIniSearch(userSession.userId)
            }
            if (allPermissionsGranted()) {
                permissionState = PERMISSION_GRANTED
                if (AddNewAddressUtils.isGpsEnabled(context)) {
                    getLocation()
                } else {
                    showBottomSheetLocUndefined(false)
                }
            } else {
                when(permissionState) {
                    PERMISSION_DENIED, PERMISSION_NOT_DEFINED -> {
                        requestPermissionLocation()
                    }
                    PERMISSION_DONT_ASK_AGAIN -> {
                        showBottomSheetLocUndefined(true)
                    }
                }
            }
        }
    }

    private fun showBottomSheetLocUndefined(isDontAskAgain: Boolean){
        isPermissionAccessed = true
        bottomSheetLocUndefined = BottomSheetUnify()
        val viewBinding = BottomsheetLocationUndefinedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomSheetLocUndefined(viewBinding, isDontAskAgain)

        bottomSheetLocUndefined?.apply {
            setCloseClickListener {
                isPermissionAccessed = false
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickXOnBlockGpsSearch(userSession.userId)
                }
                dismiss()
            }
            setChild(viewBinding.root)
            setOnDismissListener {
                isPermissionAccessed = false
                dismiss()
            }
        }

        childFragmentManager.let {
            bottomSheetLocUndefined?.show(it, "")
        }
    }

    private fun setupBottomSheetLocUndefined(viewBinding: BottomsheetLocationUndefinedBinding, isDontAskAgain: Boolean) {
        viewBinding.run {
            imgLocUndefined.setImageUrl(LOCATION_NOT_FOUND)
            tvLocUndefined.text = getString(R.string.txt_location_not_detected)
            tvInfoLocUndefined.text = getString(R.string.txt_info_location_not_detected)
            btnActivateLocation.setOnClickListener {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickAktifkanLayananLokasiSearch(userSession.userId)
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
                .addOnFailureListener(context, OnFailureListener { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                startIntentSenderForResult(rae.resolution.intentSender, GPS_REQUEST, null, 0, 0, 0, null)
                            } catch (sie: IntentSender.SendIntentException) {
                                sie.printStackTrace()
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
        return isGpsOn
    }

    private fun initObserver() {
        viewModel.autoCompleteList.observe(viewLifecycleOwner, {
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
        })
    }

    private fun loadAutoComplete(input: String) {
        if (currentLat != DEFAULT_LAT && currentLong != DEFAULT_LONG) {
            viewModel.getAutoCompleteList(input, "$currentLat,$currentLong")
        } else {
            viewModel.getAutoCompleteList(input, "")
        }
    }

    private fun loadListLocation(suggestedPlace: Place) {
        if (suggestedPlace.data.isNotEmpty()) {
            binding?.rvAddressList?.visibility = View.VISIBLE
            binding?.layoutEmptyState?.visibility = View.GONE
            autoCompleteAdapter.setData(suggestedPlace.data)
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
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        binding?.loaderCurrentLocation?.visibility = View.VISIBLE
        fusedLocationClient?.lastLocation?.addOnSuccessListener { data ->
            isPermissionAccessed = false
            if (data != null) {
                binding?.loaderCurrentLocation?.visibility = View.GONE
                currentLat = data.latitude
                currentLong = data.longitude
                goToPinpointPage(null, data.latitude, data.longitude,
                    isFromAddressForm = false,
                    isPositiveFlow = true
                )
            } else {
                fusedLocationClient?.requestLocationUpdates(AddNewAddressUtils.getLocationRequest(),
                    locationCallback, null)
            }

        }
    }

    private val locationCallback : LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (!hasRequestedLocation) {
                    //send to maps
                    hasRequestedLocation = true
                }
                stopLocationUpdate()
                binding?.loaderCurrentLocation?.visibility = View.GONE
                currentLat = locationResult.lastLocation.latitude
                currentLong = locationResult.lastLocation.longitude
                goToPinpointPage(null, locationResult.lastLocation.latitude, locationResult.lastLocation.longitude,
                    isFromAddressForm = false,
                    isPositiveFlow = true)
            }
        }

    override fun onItemClicked(placeId: String) {
        if (!isEdit) {
            AddNewAddressRevampAnalytics.onClickDropdownSuggestion(userSession.userId)
        } else {
            EditAddressRevampAnalytics.onClickDropdownSuggestionAlamat(userSession.userId)
        }
        isPolygon = false
        if (!isPositiveFlow && isFromPinpoint) goToPinpointPage(placeId, null, null,
            isFromAddressForm = true,
            isPositiveFlow = false
        )
        else goToPinpointPage(placeId, null, null, isFromAddressForm = false, isPositiveFlow = true)
    }

    private fun goToPinpointPage(placeId: String?, latitude: Double?, longitude: Double?, isFromAddressForm: Boolean, isPositiveFlow: Boolean) {
        val bundle = Bundle()
        bundle.putString(EXTRA_PLACE_ID, placeId)
        latitude?.let { bundle.putDouble(EXTRA_LAT, it) }
        longitude?.let { bundle.putDouble(EXTRA_LONG, it) }
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
        bundle.putBoolean(EXTRA_IS_POLYGON, isPolygon)
        bundle.putString(PARAM_SOURCE, source)
        distrcitId?.let { bundle.putLong(EXTRA_DISTRICT_ID, it) }
        if (!isEdit) {
            startActivityForResult(context?.let { PinpointNewPageActivity.createIntent(it, bundle) }, REQUEST_PINPOINT_PAGE)
        } else {
            activity?.run {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_PLACE_ID, placeId)
                    latitude?.let { putExtra(EXTRA_LAT, it) }
                    longitude?.let { putExtra(EXTRA_LONG, it) }
                    putExtra(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
                    putExtra(EXTRA_FROM_ADDRESS_FORM, isFromAddressForm)
                    putExtra(EXTRA_IS_POLYGON, isPolygon)
                    distrcitId?.let { putExtra(EXTRA_DISTRICT_ID, it) }
                    putExtra(EXTRA_IS_EDIT, isEdit)
                })
                finish()
            }
        }
    }

    companion object {
        private const val RESULT_PERMISSION_CODE = 1234

        private const val REQUEST_ADDRESS_FORM_PAGE = 1599
        private const val REQUEST_PINPOINT_PAGE = 1998
        private const val REQUEST_CODE_PERMISSION = 9876

        private const val LOCATION_REQUEST_INTERVAL = 10000L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L
        private const val GPS_DELAY = 1000L
        private const val DEFAULT_LONG = 0.0
        private const val DEFAULT_LAT = 0.0

        fun newInstance(bundle: Bundle): SearchPageFragment {
            return SearchPageFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_KOTA_KECAMATAN, bundle.getString(EXTRA_KOTA_KECAMATAN))
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, bundle.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, bundle.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putBoolean(EXTRA_FROM_PINPOINT, bundle.getBoolean(EXTRA_FROM_PINPOINT))
                    putBoolean(EXTRA_IS_POLYGON, bundle.getBoolean(EXTRA_IS_POLYGON))
                    putLong(EXTRA_DISTRICT_ID, bundle.getLong(EXTRA_DISTRICT_ID))
                    putBoolean(EXTRA_IS_EDIT, bundle.getBoolean(EXTRA_IS_EDIT))
                    putString(PARAM_SOURCE, bundle.getString(PARAM_SOURCE, ""))
                }
            }
        }
    }
}
